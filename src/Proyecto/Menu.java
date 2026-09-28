package Proyecto;

import java.util.Scanner;

public class Menu {
    public static void mostrarmenu(Scanner sc, Jugador jugador){
        System.out.println("1. Ver dinero");
        System.out.println("2. Ver estado del Pokémon");
        System.out.println("3. Guardar partida");
        System.out.println("4. Salir del juego");
        System.out.println("5. Volver");
        System.out.println("Elegir opcion:");
        int opcion = sc.nextInt();
        switch (opcion){
            case 1->System.out.println("\n💰 Tienes " + jugador.getDinero() + "€");
            case 2->jugador.mostrarEstadoEquipo();
            case 3->{
                GuardarCargar.guardarPartida(jugador);
                System.out.println("✔ Partida guardada con éxito.");
            }
            case 4->{
                System.out.println("\n¡Gracias por jugar!");
            }
            case 5->{
                return;
            }
            default -> System.out.println("Opción no válida.");
        }
    }
}
