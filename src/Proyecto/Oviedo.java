package Proyecto;

import java.util.Scanner;

public class Oviedo {

    public static void entrarOviedo(Scanner sc, Jugador jugadorCompleto) {

        while (true) {
            jugadorCompleto.setUbicacion(Ubicacion.OVIEDO);

            System.out.println("\n=== Ciudad de Oviedo ===");
            System.out.println("1. Centro Pokémon");
            System.out.println("2. Tienda");
            System.out.println("3. Gimnasios");
            System.out.println("4. Menú (mochila, MT, objetos, guardar)");
            System.out.println("5. Ir a Ruta 2");
            System.out.println("6. Volver a Ruta 1");
            System.out.println("7. Salir");
            System.out.print("Elige una opción: ");

            int opcion = Entrada.leerEntero(sc);

            switch (opcion) {

                case 1 -> {
                    System.out.println("Vas al Centro Pokémon.");
                    CentroPokemon.visitar(sc, jugadorCompleto.getPokemon(), jugadorCompleto);  // ✔️ Pokémon enviado
                }

                case 2 -> {
                    System.out.println("Entras a la Tienda.");
                    Tienda.entrar(sc, jugadorCompleto);
                }

                case 3 -> Gimnasio.menuGimnasios(sc, jugadorCompleto);

                case 4 -> Menu.mostrarmenu(sc, jugadorCompleto);

                case 5 -> {
                    System.out.println("Te diriges a la Ruta 2...");
                    Ruta2.entrarRuta2(sc, jugadorCompleto);
                }

                case 6 -> {
                    System.out.println("Regresas a la Ruta 1...");
                    Ruta1.entrarRuta1(sc, jugadorCompleto);
                    return;
                }

                case 7 -> {
                    System.out.println("Saliendo de Oviedo...");
                    return;
                }

                default -> System.out.println("Opción no válida.");
            }
        }
    }
}