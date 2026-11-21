package Proyecto;

import java.util.Scanner;

public class Aventura {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Proyecto.studio.DatosJuego.iniciar();
        if (!UIJuego.disponible()) {
            System.err.println("Este juego necesita un entorno gráfico para iniciar. Usa una sesión de escritorio.");
            return;
        }
        Jugador jugador = null;
        boolean reanudar = false;
        while (true) {
            String[] opciones = {"Nueva partida", "Cargar partida", "Pokémon Studio (ventana gráfica)",
                    "Multijugador LAN", "Multijugador online", "Multijugador VPN (Tailscale)", "Salir"};
            int opcion = UIJuego.elegir("Pokémon", "¡Bienvenido al mundo Pokémon!\n¿Qué quieres hacer?", opciones);
            if (opcion == 0) {
                jugador = Encuentros.encuentroInicial(sc);
                break;
            } else if (opcion == 1) {
                jugador = GuardarCargar.cargarPartida(sc);
                if (jugador != null) {
                    jugador.initializeScanner();
                    reanudar = true;
                    break;
                }
                UIJuego.mensaje("Cargar partida", "No se pudo cargar la partida seleccionada.");
            } else if (opcion == 2) {
                try {
                    Proyecto.studio.gui.EditorGrafico.abrir(Proyecto.studio.DatosJuego.carpeta(), true);
                    Proyecto.studio.DatosJuego.iniciar();
                } catch (java.io.IOException | IllegalStateException e) {
                    UIJuego.mensaje("Pokémon Studio", "No se pudo abrir el editor: " + e.getMessage());
                }
            } else if (opcion == 3 || opcion == 4 || opcion == 5) {
                if (opcion == 5) {
                    if (!configurarMultijugadorVPN()) continue;
                } else configurarMultijugador(opcion == 4);
                int modo = UIJuego.elegir("Partida", "¿Cómo quieres continuar?",
                        "Nueva partida", "Cargar partida", "Volver al menú");
                if (modo == 0) jugador = Encuentros.encuentroInicial(sc);
                else if (modo == 1) jugador = GuardarCargar.cargarPartida(sc);
                if (jugador != null) break;
            } else {
                return;
            }
        }
        if (jugador != null) {
            if (reanudar) UIJuego.mensaje("Partida cargada",
                    "Bienvenido de nuevo, " + jugador.getNombre() + ".\nUbicación: " + jugador.getUbicacion().getNombre());
            menuPuebloVillaverde(jugador);
            if (UIJuego.confirmar("Guardar partida", "¿Quieres guardar tu progreso antes de salir?")) {
                GuardarCargar.guardarPartida(sc, jugador);
            }
            UIJuego.mensaje("Pokémon", "¡Hasta pronto, " + jugador.getNombre() + "!");
        }
    }

    private static void configurarMultijugador(boolean online) {
        String titulo = online ? "Multijugador online" : "Multijugador LAN";
        int modo = UIJuego.elegir(titulo, "¿Quieres crear una partida o unirte a una existente?",
                "Crear partida", "Unirse a una partida", "Cancelar");
        if (modo < 0 || modo == 2) return;
        String nombre = UIJuego.pedirTexto(titulo, "Nombre de entrenador:", "Entrenador");
        if (nombre == null) return;
        if (nombre.isBlank()) nombre = "Entrenador";
        try {
            if (modo == 0) {
                RedLAN red = RedLAN.alojar(nombre, RedLAN.PUERTO_PREDETERMINADO);
                StringBuilder info = new StringBuilder("Partida creada.\nPuerto: " + RedLAN.PUERTO_PREDETERMINADO);
                if (online) {
                    boolean abierto = red.abrirPuertoAutomaticamente(RedLAN.PUERTO_PREDETERMINADO);
                    String ip = obtenerIpPublica();
                    String tailscale = obtenerIpTailscale();
                    info.append("\nUPnP: ").append(abierto ? "puerto abierto" : "no disponible");
                    if (ip != null) info.append("\nIP pública: ").append(ip);
                    if (tailscale != null) info.append("\nIP Tailscale: ").append(tailscale);
                    else info.append("\nSi tienes CGNAT, ambos jugadores necesitarán una VPN como Tailscale.");
                } else {
                    info.append("\nIP local: ").append(java.net.InetAddress.getLocalHost().getHostAddress());
                }
                UIJuego.mensaje(titulo, info.toString());
            } else {
                String ip = UIJuego.pedirTexto(titulo,
                        online ? "IP pública o Tailscale del anfitrión:" : "IP local del anfitrión:", "");
                if (ip == null || ip.isBlank()) return;
                RedLAN.conectar(nombre, ip, RedLAN.PUERTO_PREDETERMINADO);
                UIJuego.mensaje(titulo, "Conectado a la partida.");
            }
        } catch (Exception ex) {
            RedLAN.cerrar();
            UIJuego.mensaje(titulo, "No se pudo crear o conectar la partida: " + ex.getMessage());
        }
    }

    /**
     * Tailscale crea el túnel WireGuard fuera del proceso del juego.
     * Aquí solo se enlaza el servidor TCP a su interfaz privada.
     * No abrimos UPnP ni publicamos la IP en Internet.
     */
    private static boolean configurarMultijugadorVPN() {
        String titulo = "Multijugador por VPN privada";
        String local = obtenerIpTailscale();
        if (local == null) {
            int ayuda = UIJuego.elegir(titulo,
                    "No se ha detectado una interfaz Tailscale activa.\n"
                    + "Instala Tailscale en ambos ordenadores e inicia sesión.\n"
                    + "El anfitrión debe invitar al otro jugador o compartir su dispositivo.",
                    "Abrir instalación oficial", "Abrir instrucciones de VPN", "Volver");
            if (ayuda == 0) abrirPagina("https://tailscale.com/download");
            else if (ayuda == 1) abrirPagina("https://tailscale.com/docs/how-to/quickstart");
            return false;
        }
        int modo=UIJuego.elegir(titulo,
                "VPN disponible: "+local+"\nPuerto privado: "+RedLAN.PUERTO_PREDETERMINADO
                + "\nAmbos jugadores deben tener permiso de acceso en Tailscale.",
                "Crear partida privada", "Unirse con IP Tailscale",
                "Copiar mi IP VPN", "Cómo compartir la VPN", "Cancelar");
        if (modo == 2) {
            try {
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new java.awt.datatransfer.StringSelection(local), null);
                UIJuego.mensaje(titulo, "IP VPN copiada al portapapeles.");
            } catch (Exception ex) {
                UIJuego.mensaje(titulo, "Tu IP VPN es: " + local);
            }
            return false;
        }
        if (modo == 3) {
            abrirPagina("https://tailscale.com/docs/features/sharing");
            return false;
        }
        if (modo<0 || modo>=4)return false;
        String nombre=UIJuego.pedirTexto(titulo,"Nombre del entrenador:","Entrenador");
        if (nombre==null)return false;
        if (nombre.isBlank())nombre="Entrenador";
        try {
            if(modo==0) {
                RedLAN.alojar(nombre,RedLAN.PUERTO_PREDETERMINADO,local);
                UIJuego.mensaje(titulo,"Partida privada creada.\nIP del anfitrión: "+local
                        + "\nPuerto: "+RedLAN.PUERTO_PREDETERMINADO
                        + "\nComparte la IP solamente con tus invitados de Tailscale."
                        + "\nNo se ha abierto ningún puerto público.");
            }else{
                String ip=UIJuego.pedirTexto(titulo,
                        "IP Tailscale 100.x.y.z del anfitrión:","");
                if(ip==null)return false;
                if(!esIpTailscale(ip.strip())) {
                    UIJuego.mensaje(titulo,"Introduce una dirección IPv4 del rango privado"
                            +" de Tailscale (100.64.0.0 a 100.127.255.255).");
                    return false;
                }
                RedLAN.conectar(nombre,ip.strip(),RedLAN.PUERTO_PREDETERMINADO);
                UIJuego.mensaje(titulo,"Conectado a través de la red VPN.");
            }
        }catch(Exception ex){
            RedLAN.cerrar();
            UIJuego.mensaje(titulo,"Error de VPN: "+ex.getMessage());
            return false;
        }
        return true;
    }

    private static void abrirPagina(String enlace) {
        try {
            if (java.awt.Desktop.isDesktopSupported())
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(enlace));
            else UIJuego.mensaje("Instrucciones", enlace);
        } catch (Exception ex) {
            UIJuego.mensaje("Instrucciones", enlace);
        }
    }

    static boolean esIpTailscale(String texto) {
        if(texto==null)return false;
        String[] p=texto.split("\\.",-1);
        if(p.length!=4)return false;
        int[] b=new int[4];
        for(int i=0;i<4;i++){
            if(!p[i].matches("[0-9]{1,3}"))return false;
            b[i]=Integer.parseInt(p[i]);
            if(b[i]>255)return false;
        }
        return b[0]==100 && b[1]>=64 && b[1]<=127;
    }

    /**
     * Busca una IPv4 de Tailscale (rango CGNAT 100.64.0.0/10) en las interfaces
     * de red locales. No requiere dependencias externas.
     */
    private static String obtenerIpTailscale() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> interfaces =
                    java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                java.net.NetworkInterface ni = interfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;
                String nombre = ni.getName().toLowerCase(java.util.Locale.ROOT);
                String display = ni.getDisplayName().toLowerCase(java.util.Locale.ROOT);
                if (!nombre.contains("tailscale") && !display.contains("tailscale")) continue;
                java.util.Enumeration<java.net.InetAddress> direcciones = ni.getInetAddresses();
                while (direcciones.hasMoreElements()) {
                    java.net.InetAddress direccion = direcciones.nextElement();
                    if (direccion instanceof java.net.Inet4Address) {
                        byte[] b = direccion.getAddress();
                        int primero = b[0] & 0xff, segundo = b[1] & 0xff;
                        if (primero == 100 && segundo >= 64 && segundo <= 127)
                            return direccion.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) { }
        return null;
    }

    /** Consulta la IP pública mediante un servicio externo; devuelve null si no hay acceso. */
    private static String obtenerIpPublica() {
        try {
            java.net.URL url = new java.net.URL("https://api.ipify.org");
            java.net.URLConnection conexion = url.openConnection();
            conexion.setConnectTimeout(3000);
            conexion.setReadTimeout(3000);
            try (java.io.BufferedReader lector = new java.io.BufferedReader(
                    new java.io.InputStreamReader(conexion.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                return lector.readLine();
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static void menuPuebloVillaverde(Jugador jugadorCompleto) {
        Mapa.jugar(sc, jugadorCompleto);
    }

}
