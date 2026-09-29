package Proyecto;

import java.util.Scanner;

public class Aventura {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Jugador jugador = null;
        boolean reanudar = false; // true si se cargó una partida: continuar en la posición guardada

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
                reanudarDesdeUbicacion(jugador);
            }
            menuPuebloVillaverde(jugador);

            // Al salir, guardar automáticamente
            System.out.println("\n💾 Guardando partida...");
            GuardarCargar.guardarPartida(jugador);
            System.out.println("¡Hasta pronto, " + jugador.getNombre() + "!");
        }
    }

    // Lleva al jugador al lugar donde guardó. Al salir de él (p. ej. volver al pueblo)
    // el juego sigue con el menú de Villaverde, que es la base de todos los caminos.
    private static void reanudarDesdeUbicacion(Jugador jugador) {
        System.out.println("\nContinúas en: " + jugador.getUbicacion().getNombre());
        switch (jugador.getUbicacion()) {
            case RUTA1 -> Ruta1.entrarRuta1(sc, jugador);
            case OVIEDO -> Oviedo.entrarOviedo(sc, jugador);
            case RUTA2 -> Ruta2.entrarRuta2(sc, jugador);
            case VILLAVERDE -> { }
        }
    }

    public static void menuPuebloVillaverde(Jugador jugadorCompleto) {
        while (true) {
            Villaverde.mostrarMenu(sc, jugadorCompleto);
        }
    }

}
