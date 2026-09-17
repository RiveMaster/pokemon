package Proyecto;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class Villaverde {

    public static void mostrarMenu(Scanner sc, Jugador jugadorCompleto) {

        boolean continuar = true;

        while (continuar) {
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
            int opcion = leerEntero(sc);
            sc.nextLine();

            switch (opcion) {

                case 1 -> visitarMama(sc, jugadorCompleto);
                case 2 -> Ruta1.entrarRuta1(sc, jugadorCompleto);
                case 3 -> System.out.println("\n💰 Tienes " + jugadorCompleto.getDinero() + "€");
                case 4 -> jugadorCompleto.mostrarEstadoEquipo();
                case 5 -> {
                    GuardarCargar.guardarPartida(jugadorCompleto);
                    System.out.println("✔ Partida guardada con éxito.");
                }
                case 6 -> {
                    System.out.println("\n¡Gracias por jugar!");
                    return;}

                case 777999222 ->{
                    System.out.println("Entrando en modo debug para estats modificadas.");
                    System.out.println("Introduce la opcion:");
                    System.out.println("1. Dinero");
                    System.out.println("2. Pokemon");
                    System.out.println("3. MT");
                    System.out.println("4. Objeto");
                    System.out.println("5. salir.");
                    int opcional=sc.nextInt();
                    sc.nextLine();
                    switch (opcional){
                        case 1->{
                            System.out.println("Introduce la cantidad de dinero deseada: ");
                            Jugador.ganarDinero(sc.nextInt(), jugadorCompleto);
                        }
                        case 2->{
                            System.out.println("Introduce el nombre del pokemon deseado: ");
                            String nombre=sc.nextLine();
                            if (Pokedex.buscarPorNombre(nombre) == null) {
                                System.out.println("No se encontro el Pokemon deseado, saliendo del menu de pokemon.");
                            } else{
                                System.out.println("Introduce el nivel: ");
                                jugadorCompleto.agregarPokemon(new PokemonLuchador(Pokedex.buscarPorNombre(nombre), sc.nextInt(), 3));
                            }
                        }
                        case 3->{

                        }
                        case 4->{

                        }
                        case 5->{return;}
                        default -> throw new IllegalStateException("Unexpected value: " + opcional);
                    }
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Ingresa un número: ");
                sc.nextLine();
            }
        }
    }
    public static void visitarMama(Scanner sc, Jugador jugador) {
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