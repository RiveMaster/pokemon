package Proyecto;

import java.util.Scanner;

public class Aventura {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Jugador jugador = null;

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   BIENVENIDO AL MUNDO POKÉMON      ║");
        System.out.println("╚════════════════════════════════════╝\n");

        System.out.println("1. Nueva partida");
        System.out.println("2. Cargar partida");
        System.out.print("\nElige una opción: ");

        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        if (opcion == 1) {
            // ===== NUEVA PARTIDA =====
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║      EMPEZANDO NUEVA PARTIDA       ║");
            System.out.println("╚════════════════════════════════════╝\n");

            // Ejecutar el encuentro inicial
            jugador = Encuentros.encuentroInicial(sc);

        } else if (opcion == 2) {
            // ===== CARGAR PARTIDA =====
            jugador = GuardarCargar.cargarPartida();

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
                System.out.println("Contunuar..........");
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
            menuPuebloVillaverde(jugador);

            // Al salir, guardar automáticamente
            System.out.println("\n💾 Guardando partida...");
            GuardarCargar.guardarPartida(jugador);
            System.out.println("¡Hasta pronto, " + jugador.getNombre() + "!");
        }
    }

    public static void menuPuebloVillaverde(Jugador jugadorCompleto) {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║      PUEBLO VILLAVERDE             ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("1. Visitar a mamá");
            System.out.println("2. Ir a Ruta 1");
            System.out.println("3. Ver dinero");
            System.out.println("4. Ver estado del Pokémon");
            System.out.println("5. Guardar partida");
            System.out.println("6. Salir del juego");
            System.out.print("\nElige una opción: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> visitarMama(jugadorCompleto);
                case 2 -> Ruta1.entrarRuta1(sc, jugadorCompleto);
                case 3 -> System.out.println("\n💰 Tienes " + jugadorCompleto.getDinero() + "€");
                case 4 -> jugadorCompleto.mostrarEstadoEquipo();
                case 5 -> {
                    GuardarCargar.guardarPartida(jugadorCompleto);
                    System.out.println("✔ Partida guardada con éxito.");
                }
                case 6 -> {
                    System.out.println("\n¡Gracias por jugar!");
                    return;
                }
                default -> System.out.println("❌ Opción no válida.");
            }
        }
    }

    public static void visitarMama(Jugador jugador) {
        System.out.println("\n🏠 Casa de mamá...");

        if (!jugador.isMamaVisitada()) {
            System.out.println("Mamá: ¡Hijo mío! Te doy 1000€ para empezar tu aventura.");
            jugador.agregarDinero(1000);
            jugador.setMamaVisitada(true);
            System.out.println("✔ Has recibido 1000€");
            System.out.println("Contunuar..........");
            sc.nextLine();
            sc.nextLine();
        } else {
            System.out.println("Mamá: Ya te di dinero, ¡ve y conviértete en un gran entrenador!");
        }

        System.out.println("💰 Ahora tienes " + jugador.getDinero() + "€");
        System.out.println("Contunuar..........");
        sc.nextLine();
    }

}
