package Proyecto;

import java.util.Scanner;

public class Aventura {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Proyecto.studio.DatosJuego.iniciar(); // usa los datos de data/studio si existen (Pokémon Studio)
        Jugador jugador = null;
        boolean reanudar = false; // true si se cargó una partida: continúa en la posición guardada (Mapa.jugar la lee)

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   BIENVENIDO AL MUNDO POKÉMON      ║");
        System.out.println("╚════════════════════════════════════╝\n");

        int opcion;
        while (true) {
            System.out.println("1. Nueva partida");
            System.out.println("2. Cargar partida");
            System.out.println("3. Editor de datos (Pokémon Studio, consola)");
            System.out.println("4. Editor de datos (Pokémon Studio, ventana gráfica)");
            System.out.println("5. Multijugador LAN (anfitrión / conectar por IP)");
            System.out.print("\nElige una opción: ");

            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer
            if (opcion != 3 && opcion != 4) break;

            try {
                if (opcion == 3) Proyecto.studio.EditorConsola.abrir(Proyecto.studio.DatosJuego.carpeta(), sc);
                else Proyecto.studio.gui.EditorGrafico.abrir(Proyecto.studio.DatosJuego.carpeta(), true);
            } catch (java.io.IOException | IllegalStateException e) {
                System.out.println("No se pudo usar el editor: " + e.getMessage());
            }
            Proyecto.studio.DatosJuego.iniciar(); // aplica lo que se haya guardado en el editor
            System.out.println();
        }

        if (opcion == 5) {
            configurarMultijugador();
            System.out.println("\n1. Nueva partida\n2. Cargar partida");
            System.out.print("Elige una opción: ");
            int modo = Entrada.leerEntero(sc);
            jugador = modo == 2 ? GuardarCargar.cargarPartida(sc) : Encuentros.encuentroInicial(sc);
        } else if (opcion == 1) {
            // ===== NUEVA PARTIDA =====
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║      EMPEZANDO NUEVA PARTIDA       ║");
            System.out.println("╚════════════════════════════════════╝\n");

            // Ejecutar el encuentro inicial
            jugador = Encuentros.encuentroInicial(sc);

        } else if (opcion == 2) {
            // ===== CARGAR PARTIDA =====
            jugador = GuardarCargar.cargarPartida(sc);

            if (jugador != null) {
                jugador.initializeScanner();
                System.out.println("\n╔════════════════════════════════════╗");
                System.out.println("║      ¡PARTIDA CARGADA!             ║");
                System.out.println("╚════════════════════════════════════╝");
                System.out.println("Bienvenido de vuelta, " + jugador.getNombre() + "!");
                jugador.mostrarEquipo();
                System.out.println("Dinero: " + jugador.getDinero() + "€");

                if (jugador.getRival() != null) {
                    System.out.println("Rival: " + jugador.getRival().getNombre());
                }
                System.out.println();
                jugador.mostrar(jugador);
                System.out.println("Ubicación guardada: " + jugador.getUbicacion().getNombre());
                System.out.println("Contunuar..........");
                reanudar = true;
            } else {
                System.out.println("No se pudo cargar la partida. Iniciando nueva partida...\n");
                jugador = Encuentros.encuentroInicial(sc);
            }
        } else {
            System.out.println("Opción no válida. Iniciando nueva partida...\n");
            jugador = Encuentros.encuentroInicial(sc);
        }

        // ===== COMENZAR EL JUEGO =====
        if (jugador != null) {
            if (reanudar) {
                System.out.println("\nContinúas en: " + jugador.getUbicacion().getNombre()
                        + " [" + jugador.getGeneracion().etiqueta() + "]");
            }
            menuPuebloVillaverde(jugador);

            // Al salir, guardar automáticamente
            System.out.println("\n💾 Guardando partida...");
            GuardarCargar.guardarPartida(sc, jugador);
            System.out.println("¡Hasta pronto, " + jugador.getNombre() + "!");
        }
    }

    private static void configurarMultijugador() {
        System.out.println("\n=== MULTIJUGADOR ===");
        System.out.println("1. Crear partida en LAN");
        System.out.println("2. Unirse por IP local (LAN)");
        System.out.println("3. Crear partida online (Internet)");
        System.out.println("4. Unirse por IP pública (Internet)");
        System.out.print("Elige: ");
        int modo = Entrada.leerEntero(sc);
        sc.nextLine(); // Consumir el salto de línea antes de leer el nombre/IP.
        System.out.print("Nombre de entrenador: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) nombre = "Entrenador";

        boolean online = modo == 3 || modo == 4;
        try {
            if (modo == 1 || modo == 3) {
                System.out.print("Puerto TCP [" + RedLAN.PUERTO_PREDETERMINADO + "]: ");
                String texto = sc.nextLine().trim();
                int puerto = texto.isEmpty() ? RedLAN.PUERTO_PREDETERMINADO : Integer.parseInt(texto);
                RedLAN.alojar(nombre, puerto);
                System.out.println("Servidor iniciado en el puerto TCP " + puerto + ".");
                System.out.println("IP local: " + java.net.InetAddress.getLocalHost().getHostAddress());
                if (online) {
                    String ipPublica = obtenerIpPublica();
                    if (ipPublica != null) System.out.println("IP pública detectada: " + ipPublica);
                    else System.out.println("No se pudo detectar la IP pública automáticamente.");
                    System.out.println("Configura en el router el reenvío TCP del puerto " + puerto
                            + " hacia este equipo y permite Java en el cortafuegos.");
                    System.out.println("Comparte la IP pública con el otro jugador.");
                    System.out.println("Si tu proveedor usa CGNAT, necesitarás una VPN tipo LAN virtual.");
                } else {
                    System.out.println("Comparte la IP local con los jugadores de tu misma red.");
                    System.out.println("Permite Java en el cortafuegos para el puerto TCP " + puerto + ".");
                }
            } else if (modo == 2 || modo == 4) {
                System.out.print(online ? "IP pública del anfitrión: " : "IP local del anfitrión: ");
                String ip = sc.nextLine().trim();
                System.out.print("Puerto [" + RedLAN.PUERTO_PREDETERMINADO + "]: ");
                String texto = sc.nextLine().trim();
                int puerto = texto.isEmpty() ? RedLAN.PUERTO_PREDETERMINADO : Integer.parseInt(texto);
                RedLAN.conectar(nombre, ip, puerto);
                System.out.println("Conectado a " + ip + ":" + puerto);
            } else {
                System.out.println("Modo no válido; continuarás sin conexión de red.");
            }
        } catch (Exception ex) {
            RedLAN.cerrar();
            System.out.println("No se pudo iniciar la conexión: " + ex.getMessage());
        }
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
