package Proyecto;

import java.util.List;
import java.util.Scanner;

public class Tienda {
    private static int precioPokeball = 50;
    private static int precioPocion = 300;
    private static int precioMt = 200;

    /** Lo usa el cargador de datos de Studio. */
    public static void configurarPrecios(int pokeball, int pocion, int mt) {
        precioPokeball = pokeball;
        precioPocion = pocion;
        precioMt = mt;
    }


    public static void entrar(Scanner sc, Jugador jugadorCompleto) {

        if (UIJuego.disponible()) {
            while (true) {
                int opcion = UIJuego.elegir("Tienda Pokémon",
                        "¡Bienvenido al Poké Mart!\\nDinero disponible: " + jugadorCompleto.getDinero() + "€",
                        "Poké Ball · " + precioPokeball + "€",
                        "Poción · " + precioPocion + "€",
                        "MT sorpresa · " + precioMt + "€",
                        "Ver mochila", "Objetos equipables", "Salir");
                switch (opcion) {
                    case 0 -> comprarPokeball(jugadorCompleto);
                    case 1 -> comprarPocion(jugadorCompleto);
                    case 2 -> comprarMT(sc, jugadorCompleto);
                    case 3 -> jugadorCompleto.mostrar(jugadorCompleto);
                    case 4 -> comprarObjetoEquipable(sc, jugadorCompleto);
                    default -> { return; }
                }
            }
        }

        while (true) {
            System.out.println("\n=== Tienda Pokémon ===");
            System.out.println("1. Comprar Pokéball (" + precioPokeball + "€)");
            System.out.println("2. Comprar Poción (" + precioPocion + "€)");
            System.out.println("3. Comprar MT aleatoria (" + precioMt + "€)");
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
        int precio = precioMt;

        if (!Jugador.gastarDinero(precio, jugadorCompleto)) {
            UIJuego.mensaje("Dinero insuficiente", "No tienes suficientes monedas para esta compra.");
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
        UIJuego.mensaje("MT sorpresa", "¡Has recibido la MT " + mt.getNombre()
                + "!\\nTipo: " + mt.getTipo() + " · Potencia: " + mt.getPotencia());

        // Enseñársela ahora a un Pokémon del equipo (si cancela, se queda en la mochila)
        if (jugadorCompleto.getEquipo().isEmpty()) {
            System.out.println("No tienes Pokémon en el equipo para enseñarle la MT ahora. La has guardado en la mochila.");
            return;
        }
        if (!UIJuego.disponible() || UIJuego.confirmar("MT adquirida",
                "¿Quieres enseñar " + mt.getNombre() + " a un Pokémon ahora?")) {
            jugadorCompleto.enseñarMT(sc, mt.getNombre());
        }
    }

    private static void comprarObjetoEquipable(Scanner sc, Jugador jugadorCompleto) {
        ObjetoEquipable[] objetos = ObjetoEquipable.values();

        int eleccion;
        if (UIJuego.disponible()) {
            String[] opciones = java.util.Arrays.stream(objetos)
                    .map(o -> o.getNombre() + " · " + o.getPrecio() + "€")
                    .toArray(String[]::new);
            eleccion = UIJuego.elegir("Objetos equipables",
                    "Elige qué objeto quieres comprar.\\nDinero: " + jugadorCompleto.getDinero() + "€",
                    opciones) + 1;
        } else {
            System.out.println("\\n-- Objetos equipables -- (dinero: " + jugadorCompleto.getDinero() + "€)");
            for (int i = 0; i < objetos.length; i++) {
                System.out.println((i + 1) + ". " + objetos[i].getNombre()
                        + " (" + objetos[i].getPrecio() + "€) - " + objetos[i].getDescripcion());
            }
            System.out.print("Elige un objeto (0 para cancelar): ");
            eleccion = Entrada.leerEntero(sc);
        }
        if (eleccion < 1 || eleccion > objetos.length) return;

        ObjetoEquipable objeto = objetos[eleccion - 1];
        if (!Jugador.gastarDinero(objeto.getPrecio(), jugadorCompleto)) {
            UIJuego.mensaje("Dinero insuficiente", "No tienes suficientes monedas para esta compra.");
            return;
        }

        jugadorCompleto.añadirObjetoEquipable(objeto, 1);
        UIJuego.mensaje("Compra completada", "Has comprado " + objeto.getNombre()
                + ".\\n" + objeto.getDescripcion());
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");
    }


    private static void comprarPokeball(Jugador jugadorCompleto) {
        int precio = precioPokeball;

        if (!Jugador.gastarDinero(precio, jugadorCompleto)) {
            UIJuego.mensaje("Dinero insuficiente", "No tienes suficientes monedas para esta compra.");
            return;
        }

        jugadorCompleto.añadirPokeball("Poke Ball", 1);
        UIJuego.mensaje("Compra completada", "¡Has comprado una Poké Ball!");
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");
    }

    private static void comprarPocion(Jugador jugadorCompleto) {
        int precio = precioPocion;

        if (!Jugador.gastarDinero(precio, jugadorCompleto)) {
            UIJuego.mensaje("Dinero insuficiente", "No tienes suficientes monedas para esta compra.");
            return;
        }

        jugadorCompleto.añadirMedicina("Pocion", 1);
        UIJuego.mensaje("Compra completada", "¡Has comprado una Poción!");
        System.out.println("Dinero restante: " + jugadorCompleto.getDinero() + "€");
    }
}
