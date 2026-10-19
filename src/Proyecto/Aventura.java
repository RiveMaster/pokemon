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

        if (opcion == 1) {
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

    public static void menuPuebloVillaverde(Jugador jugadorCompleto) {
        Mapa.jugar(sc, jugadorCompleto);
    }

}
