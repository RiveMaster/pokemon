package Proyecto;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class Villaverde {

    private static int dinero = 1000;
    private static boolean mamaVisitada = false;

    // Tu equipo Pokémon
    private static List<PokemonLuchador> tuEquipo;

    public static void setEquipo(List<PokemonLuchador> equipo) {
        tuEquipo = equipo;
    }

    public static void mostrarMenu(Scanner sc) {

        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== Pueblo Villaverde ===");
            System.out.println("1. Visitar a mamá");
            System.out.println("2. Ir a Ruta 1");
            System.out.println("3. Ver dinero");
            System.out.println("4. Salir del pueblo");
            System.out.print("Elige una opción: ");

            int opcion = leerEntero(sc);

            switch (opcion) {

                case 1 -> visitarMama();

                case 2 -> {
                    System.out.println("\nTe diriges hacia Ruta 1...");
                    // Tomamos tu Pokémon principal del equipo
                    PokemonLuchador jugador = tuEquipo.get(0);
                    Jugador jugadorCompleto=null;
                    // Entramos en la ruta
                    Ruta1.entrarRuta1(sc, jugadorCompleto);
                }
                case 3 -> System.out.println("Tienes " + dinero + "€");

                case 4 -> {
                    System.out.println("Saliendo del pueblo...");
                    continuar = false;
                }

                case 777999222 ->{
                    System.out.println("Entrando en modo debug para estats modificadas.");
                    System.out.println("Introduce la opcion:");
                    System.out.println("1. Dinero");
                    System.out.println("2. Pokemon");
                    System.out.println("3. MT");
                    System.out.println("4. Objeto");
                    System.out.println("5. salir.");
                    Jugador jugadorCompleto = null;
                    int opcional=sc.nextInt();
                    switch (opcional){
                        case 1->{
                            System.out.println("Introduce la cantidad de dinero deseada: ");
                            Jugador.ganarDinero(sc.nextInt(), jugadorCompleto);
                        }
                        case 2->{
                            System.out.println("Introduce el nombre del pokemon deseado: ");
                            PokemonBase nuevo = new PokemonBase(Pokedex.buscarPorNombre(sc.nextLine()));
                        }
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

    private static void visitarMama() {
        System.out.println("\nCasa de mamá...");

        if (!mamaVisitada) {
            System.out.println("Mamá: Hola cariño. Te doy 1000€ para tu aventura.");
            dinero += 1000;
            mamaVisitada = true;
        } else {
            System.out.println("Mamá: Ya te di dinero. No seas avaricioso.");
        }

        System.out.println("Ahora tienes: " + dinero + "€");
    }
}