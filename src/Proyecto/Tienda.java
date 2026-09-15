package Proyecto;

import java.util.Scanner;

public class Tienda {

    public static void entrar(Scanner sc, Jugador jugadorCompleto) {

        while (true) {
            System.out.println("\n=== Tienda Pokémon ===");
            System.out.println("1. Comprar Pokéball (50€)");
            System.out.println("2. Comprar Poción (300€)");
            System.out.println("3. Ver mochila");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> comprarPokeball(jugadorCompleto);
                case 2 -> comprarPocion(jugadorCompleto);
                case 3 -> jugadorCompleto.mostrar(jugadorCompleto);
                case 4 -> {
                    System.out.println("Gracias por visitar la tienda.");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    // =============================
    //      MÉTODOS DE COMPRA
    // =============================

    private static void comprarPokeball(Jugador jugadorCompleto) {
        int precio = 50;

        if (!Jugador.gastarDinero(precio, jugadorCompleto)) {
            System.out.println("No tienes suficiente dinero.");
            return;
        }

        jugadorCompleto.añadirPokeball("Poke Ball", 1);
        System.out.println("Compraste 1 Poke Ball.");
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");
    }

    private static void comprarPocion(Jugador jugadorCompleto) {
        int precio = 300;

        if (!Jugador.gastarDinero(precio, jugadorCompleto)) {
            System.out.println("No tienes suficiente dinero.");
            return;
        }

        jugadorCompleto.añadirMedicina("Pocion", 1);
        System.out.println("Compraste 1 Pocion.");
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");
    }
}
