package Proyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Transporte multijugador TCP para partidas en LAN.
 *
 * El anfitrión mantiene el servidor; los clientes envían su ubicación y casilla.
 * No se serializan objetos Java ni partidas completas por la red.
 */
public final class RedLAN {
    public static final int PUERTO_PREDETERMINADO = 47821;
    private static volatile RedLAN instancia;

    public static final class JugadorRemoto {
        public final String nombre;
        public final String ubicacion;
        public final int x, y;
        JugadorRemoto(String nombre, String ubicacion, int x, int y) {
            this.nombre = nombre; this.ubicacion = ubicacion; this.x = x; this.y = y;
        }
    }

    private final boolean anfitrion;
    private final String nombreLocal;
    private volatile String ubicacionLocal = "";
    private volatile int xLocal, yLocal;
    private volatile boolean activa = true;
    private ServerSocket servidor;
    private Socket socket;
    private BufferedWriter salidaCliente;
    private Thread hiloAceptacion, hiloLectura;
    private final Map<Socket, BufferedWriter> clientes = new ConcurrentHashMap<>();
    private final Map<String, JugadorRemoto> jugadores = new ConcurrentHashMap<>();
    private final List<String> eventos = new CopyOnWriteArrayList<>();

    private RedLAN(boolean anfitrion, String nombreLocal) {
        this.anfitrion = anfitrion;
        this.nombreLocal = nombreLocal == null || nombreLocal.isBlank() ? "Entrenador" : nombreLocal.trim();
    }

    public static synchronized RedLAN alojar(String nombre, int puerto) throws IOException {
        cerrar();
        RedLAN red = new RedLAN(true, nombre);
        red.servidor = new ServerSocket(puerto, 16, InetAddress.getByName("0.0.0.0"));
        instancia = red;
        red.iniciarAceptacion();
        return red;
    }

    public static synchronized RedLAN conectar(String nombre, String ip, int puerto) throws IOException {
        cerrar();
        RedLAN red = new RedLAN(false, nombre);
        red.socket = new Socket();
        red.socket.connect(new java.net.InetSocketAddress(ip.trim(), puerto), 8000);
        red.salidaCliente = new BufferedWriter(new OutputStreamWriter(red.socket.getOutputStream(), StandardCharsets.UTF_8));
        instancia = red;
        red.enviarCliente("JOIN|" + codificar(red.nombreLocal));
        red.iniciarLecturaCliente();
        return red;
    }

    public static RedLAN actual() { return instancia; }
    public boolean esAnfitrion() { return anfitrion; }
    public String getNombreLocal() { return nombreLocal; }
    public boolean estaConectada() { return activa && (anfitrion ? servidor != null && !servidor.isClosed()
            : socket != null && socket.isConnected() && !socket.isClosed()); }

    public static synchronized void cerrar() {
        RedLAN anterior = instancia;
        instancia = null;
        if (anterior != null) anterior.cerrarInterno();
    }

    private void cerrarInterno() {
        activa = false;
        try { if (socket != null) socket.close(); } catch (IOException ignored) { }
        try { if (servidor != null) servidor.close(); } catch (IOException ignored) { }
        for (Socket s : clientes.keySet()) try { s.close(); } catch (IOException ignored) { }
        clientes.clear();
    }

    private void iniciarAceptacion() {
        hiloAceptacion = new Thread(() -> {
            while (activa) {
                try {
                    Socket cliente = servidor.accept();
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream(), StandardCharsets.UTF_8));
                    clientes.put(cliente, out);
                    Thread lector = new Thread(() -> atenderCliente(cliente), "Pokemon-LAN-cliente");
                    lector.setDaemon(true);
                    lector.start();
                } catch (SocketException ex) {
                    if (activa) eventos.add("ERROR|" + ex.getMessage());
                    break;
                } catch (IOException ex) {
                    if (activa) eventos.add("ERROR|" + ex.getMessage());
                }
            }
        }, "Pokemon-LAN-anfitrion");
        hiloAceptacion.setDaemon(true);
        hiloAceptacion.start();
    }

    private void atenderCliente(Socket cliente) {
        String nombre = "Invitado";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while (activa && (linea = in.readLine()) != null) {
                String[] p = linea.split("\\|", -1);
                if (p.length == 2 && "JOIN".equals(p[0])) {
                    nombre = decodificar(p[1]);
                    eventos.add("JOIN|" + nombre);
                    enviarA(cliente, "WELCOME|" + codificar(nombreLocal));
                } else if (p.length == 5 && "POS".equals(p[0])) {
                    String loc = decodificar(p[2]);
                    int x = Integer.parseInt(p[3]), y = Integer.parseInt(p[4]);
                    jugadores.put(nombre, new JugadorRemoto(nombre, loc, x, y));
                    difundir("STATE|" + codificar(nombre) + "|" + codificar(loc) + "|" + x + "|" + y, cliente);
                } else if (p.length >= 2 && ("DUEL_REQUEST".equals(p[0]) || "DUEL_ACCEPT".equals(p[0])
                        || "DUEL_REJECT".equals(p[0]) || "DUEL_DATA".equals(p[0]))) {
                    eventos.add(linea);
                    difundir(linea, cliente);
                }
            }
        } catch (IOException | RuntimeException ex) {
            if (activa) eventos.add("DISCONNECT|" + nombre);
        } finally {
            clientes.remove(cliente);
            jugadores.remove(nombre);
            difundir("LEAVE|" + codificar(nombre), null);
            try { cliente.close(); } catch (IOException ignored) { }
        }
    }

    private void iniciarLecturaCliente() {
        hiloLectura = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
                String linea;
                while (activa && (linea = in.readLine()) != null) procesarCliente(linea);
            } catch (IOException ex) {
                if (activa) eventos.add("DISCONNECT|Anfitrión");
            } finally { activa = false; }
        }, "Pokemon-LAN-lector");
        hiloLectura.setDaemon(true);
        hiloLectura.start();
    }

    private void procesarCliente(String linea) {
        String[] p = linea.split("\\|", -1);
        try {
            if (p.length == 5 && "STATE".equals(p[0])) {
                String nombre = decodificar(p[1]);
                jugadores.put(nombre, new JugadorRemoto(nombre, decodificar(p[2]),
                        Integer.parseInt(p[3]), Integer.parseInt(p[4])));
            } else if (p.length == 2 && "LEAVE".equals(p[0])) {
                String nombre = decodificar(p[1]); jugadores.remove(nombre); eventos.add("LEAVE|" + nombre);
            } else if (p.length >= 2) eventos.add(linea);
        } catch (RuntimeException ex) { eventos.add("ERROR|Mensaje de red inválido"); }
    }

    public void publicarPosicion(String ubicacion, int x, int y) {
        ubicacionLocal = ubicacion == null ? "" : ubicacion;
        xLocal = x; yLocal = y;
        String linea = "POS|" + codificar(nombreLocal) + "|" + codificar(ubicacionLocal) + "|" + x + "|" + y;
        if (anfitrion) {
            jugadores.put(nombreLocal, new JugadorRemoto(nombreLocal, ubicacionLocal, x, y));
            difundir("STATE|" + codificar(nombreLocal) + "|" + codificar(ubicacionLocal) + "|" + x + "|" + y, null);
        } else enviarCliente(linea);
    }

    public List<JugadorRemoto> jugadoresEn(String ubicacion) {
        List<JugadorRemoto> resultado = new ArrayList<>();
        for (JugadorRemoto j : jugadores.values())
            if (j != null && j.ubicacion.equals(ubicacion) && !j.nombre.equals(nombreLocal)) resultado.add(j);
        return Collections.unmodifiableList(resultado);
    }

    public List<String> consumirEventos() {
        List<String> copia = new ArrayList<>(eventos);
        eventos.removeAll(copia);
        return copia;
    }

    public void solicitarCombate(String nombreRemoto) {
        enviar("DUEL_REQUEST|" + codificar(nombreLocal) + "|" + codificar(nombreRemoto));
    }

    public void responderCombate(String nombreRemoto, boolean aceptar) {
        enviar((aceptar ? "DUEL_ACCEPT|" : "DUEL_REJECT|") + codificar(nombreLocal) + "|" + codificar(nombreRemoto));
    }

    /** Canal de mensajes para intercambiar acciones/datos de un combate entre clientes. */
    public void enviarDatoCombate(String dato) {
        enviar("DUEL_DATA|" + codificar(nombreLocal) + "|" + codificar(dato == null ? "" : dato));
    }

    private void enviar(String linea) {
        if (anfitrion) difundir(linea, null); else enviarCliente(linea);
    }

    private void enviarCliente(String linea) {
        synchronized (this) {
            try {
                if (salidaCliente == null) return;
                salidaCliente.write(linea); salidaCliente.newLine(); salidaCliente.flush();
            } catch (IOException ex) { eventos.add("ERROR|" + ex.getMessage()); }
        }
    }

    private void difundir(String linea, Socket excluir) {
        for (Map.Entry<Socket, BufferedWriter> e : clientes.entrySet()) {
            if (e.getKey() == excluir) continue;
            try {
                synchronized (e.getValue()) {
                    e.getValue().write(linea); e.getValue().newLine(); e.getValue().flush();
                }
            } catch (IOException ex) {
                try { e.getKey().close(); } catch (IOException ignored) { }
            }
        }
    }

    private static String codificar(String valor) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString((valor == null ? "" : valor).getBytes(StandardCharsets.UTF_8));
    }
    private static String decodificar(String valor) {
        return new String(Base64.getUrlDecoder().decode(valor), StandardCharsets.UTF_8);
    }
}
