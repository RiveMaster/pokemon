package Proyecto;

import java.util.List;
import java.util.Scanner;

public class Tienda {

    public static void entrar(Scanner sc, Jugador jugadorCompleto) {

        while (true) {
            System.out.println("\n=== Tienda Pokémon ===");
            System.out.println("1. Comprar Pokéball (50€)");
            System.out.println("2. Comprar Poción (300€)");
            System.out.println("3. Comprar MT aleatoria (200€)");
            System.out.println("4. Ver mochila");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> comprarPokeball(jugadorCompleto);
                case 2 -> comprarPocion(jugadorCompleto);
                case 3 -> comprarMT(sc, jugadorCompleto);
                case 4 -> jugadorCompleto.mostrar(jugadorCompleto);
                case 5 -> {
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
    private static void comprarMT(Scanner sc, Jugador jugadorCompleto){
        int precio = 200;

        if (!Jugador.gastarDinero(precio, jugadorCompleto)) {
            System.out.println("No tienes suficiente dinero.");
            return;
        }

        Movimiento mt = Ataques.getAtaqueAleatorio();

        if (mt == null) {
            System.out.println("Error: no se pudo generar la MT. Se te devuelve el dinero.");
            jugadorCompleto.agregarDinero(precio);
            return;
        }

        System.out.println("\n¡Has obtenido la MT: " + mt.getNombre() + "!");
        System.out.println("Tipo: " + mt.getTipo() + " | Potencia: " + mt.getPotencia());
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");

        jugadorCompleto.añadirMT(mt.getNombre());

        // Preguntar a qué Pokémon del equipo se le quiere enseñar la MT
        List<PokemonLuchador> equipo = jugadorCompleto.getEquipo();

        if (equipo.isEmpty()) {
            System.out.println("No tienes Pokémon en el equipo para enseñarle la MT ahora. La has guardado en la mochila.");
            return;
        }

        System.out.println("\n¿A qué Pokémon quieres enseñarle " + mt.getNombre() + "? (0 para no enseñarla ahora)");
        for (int i = 0; i < equipo.size(); i++) {
            System.out.println((i + 1) + ". " + equipo.get(i).getNombre() + " Nv." + equipo.get(i).getNivel());
        }
        System.out.print("Elige una opción: ");

        int eleccion = sc.nextInt();

        if (eleccion < 1 || eleccion > equipo.size()) {
            System.out.println("De acuerdo, la MT se queda guardada en la mochila.");
            return;
        }

        PokemonLuchador elegido = equipo.get(eleccion - 1);

        // Creamos una copia nueva del movimiento por si el original lleva un efecto compartido
        boolean aprendido = elegido.aprenderMovimiento(mt);

        if (aprendido) {
            System.out.println(elegido.getNombre() + " ha aprendido " + mt.getNombre() + "!");
        } else {
            System.out.println(elegido.getNombre() + " ya conocía ese movimiento.");
        }
    }


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
