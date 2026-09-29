package Proyecto;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class Villaverde {

    public static void mostrarMenu(Scanner sc, Jugador jugadorCompleto) {

        boolean continuar = true;

        while (continuar) {
            jugadorCompleto.setUbicacion(Ubicacion.VILLAVERDE);
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║      PUEBLO VILLAVERDE             ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("1. Visitar a mamá");
            System.out.println("2. Ir a Ruta 1");
            System.out.println("3. Menú (mochila, MT, objetos, guardar)");
            System.out.print("\nElige una opción: ");
            int opcion = leerEntero(sc);
            sc.nextLine();

            switch (opcion) {
                case 1 -> visitarMama(sc, jugadorCompleto);
                case 2 -> Ruta1.entrarRuta1(sc, jugadorCompleto);
                case 3 -> Menu.mostrarmenu(sc, jugadorCompleto);
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
                            System.out.println("Introduce el nombre del ataque: ");
                            Movimiento mov = Ataques.buscarPorNombre(sc.nextLine());
                            if (mov == null) {
                                System.out.println("No se encontró ese ataque.");
                            } else {
                                jugadorCompleto.añadirMT(mov.getNombre());
                                System.out.println("MT añadida: " + mov.getNombre());
                            }
                        }
                        case 4->{
                            System.out.println("Objetos disponibles:");
                            for (ObjetoEquipable o : ObjetoEquipable.values()) {
                                System.out.println("  " + o.getNombre());
                            }
                            System.out.println("Introduce el nombre del objeto: ");
                            ObjetoEquipable objeto = ObjetoEquipable.buscarPorNombre(sc.nextLine());
                            if (objeto == null) {
                                System.out.println("No se encontró ese objeto.");
                            } else {
                                jugadorCompleto.añadirObjetoEquipable(objeto, 1);
                                System.out.println("Objeto añadido: " + objeto.getNombre());
                            }
                        }
                        case 5->{return;}
                        default -> System.out.println("Valor no permitido");
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