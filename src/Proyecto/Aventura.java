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
            System.out.println("5. Multijugador LAN (red local)");
            System.out.println("6. Multijugador online (Internet)");
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

        if (opcion == 5 || opcion == 6) {
            configurarMultijugador(opcion == 6);
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

    private static void configurarMultijugador(boolean online) {
        System.out.println(online ? "\n=== JUGAR ONLINE ===" : "\n=== JUGAR POR LAN ===");
        System.out.println("1. Crear partida");
        System.out.println("2. Unirse a una partida");
        System.out.print("Elige: ");
        int modo = Entrada.leerEntero(sc);
        sc.nextLine();
        System.out.print("Nombre de entrenador: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) nombre = "Entrenador";
        try {
            if (modo == 1) {
                RedLAN red = RedLAN.alojar(nombre, RedLAN.PUERTO_PREDETERMINADO);
                System.out.println("\n¡Partida creada!");
                if (online) {
                    System.out.println("Intentando abrir el puerto del router mediante UPnP...");
                    boolean abierto = red.abrirPuertoAutomaticamente(RedLAN.PUERTO_PREDETERMINADO);
                    System.out.println(abierto ? "Puerto abierto automáticamente." :
                            "UPnP no disponible. Actívalo en el router o utiliza una VPN.");
                    String ip = obtenerIpPublica();
                    if (ip != null) System.out.println("IP pública para tu amigo: " + ip);
                    else System.out.println("No se pudo detectar la IP pública.");
                } else {
                    System.out.println("IP local: " + java.net.InetAddress.getLocalHost().getHostAddress());
                    System.out.println("Comparte esta IP con jugadores de tu misma red.");
                }
                System.out.println("Puerto: " + RedLAN.PUERTO_PREDETERMINADO);
            } else if (modo == 2) {
                System.out.print(online ? "IP pública del anfitrión: " : "IP local del anfitrión: ");
                String ip = sc.nextLine().trim();
                RedLAN.conectar(nombre, ip, RedLAN.PUERTO_PREDETERMINADO);
                System.out.println("\n¡Conectado a la partida!");
            } else {
                System.out.println("Opción no válida.");
            }
        } catch (Exception ex) {
            RedLAN.cerrar();
            System.out.println("No se pudo crear o conectar la partida: " + ex.getMessage());
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
