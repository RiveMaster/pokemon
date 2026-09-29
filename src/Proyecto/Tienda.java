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
            System.out.println("5. Comprar objeto equipable");
            System.out.println("6. Salir");
            System.out.print("Elige una opción: ");

            int opcion = Entrada.leerEntero(sc);

            switch (opcion) {
                case 1 -> comprarPokeball(jugadorCompleto);
                case 2 -> comprarPocion(jugadorCompleto);
                case 3 -> comprarMT(sc, jugadorCompleto);
                case 4 -> jugadorCompleto.mostrar(jugadorCompleto);
                case 5 -> comprarObjetoEquipable(sc, jugadorCompleto);
                case 6 -> {
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

        // Enseñársela ahora a un Pokémon del equipo (si cancela, se queda en la mochila)
        if (jugadorCompleto.getEquipo().isEmpty()) {
            System.out.println("No tienes Pokémon en el equipo para enseñarle la MT ahora. La has guardado en la mochila.");
            return;
        }
        jugadorCompleto.enseñarMT(sc, mt.getNombre());
    }

    private static void comprarObjetoEquipable(Scanner sc, Jugador jugadorCompleto) {
        ObjetoEquipable[] objetos = ObjetoEquipable.values();

        System.out.println("\n-- Objetos equipables -- (dinero: " + jugadorCompleto.getDinero() + "€)");
        for (int i = 0; i < objetos.length; i++) {
            System.out.println((i + 1) + ". " + objetos[i].getNombre() + " (" + objetos[i].getPrecio() + "€) - "
                    + objetos[i].getDescripcion());
        }
        System.out.print("Elige un objeto (0 para cancelar): ");

        int eleccion = Entrada.leerEntero(sc);
        if (eleccion < 1 || eleccion > objetos.length) {
            return;
        }

        ObjetoEquipable objeto = objetos[eleccion - 1];
        if (!Jugador.gastarDinero(objeto.getPrecio(), jugadorCompleto)) {
            System.out.println("No tienes suficiente dinero.");
            return;
        }

        jugadorCompleto.añadirObjetoEquipable(objeto, 1);
        System.out.println("Compraste " + objeto.getNombre() + ". Equípaselo a un Pokémon desde el menú.");
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");
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
