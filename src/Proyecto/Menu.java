package Proyecto;

import java.util.Scanner;

public class Menu {
    public static void mostrarmenu(Scanner sc, Jugador jugador){
        System.out.println("\n1. Ver dinero");
        System.out.println("2. Ver estado del Pokémon");
        System.out.println("3. Ver mochila");
        System.out.println("4. Usar MT");
        System.out.println("5. Equipar objeto");
        System.out.println("6. Guardar partida");
        System.out.println("7. Salir del juego");
        System.out.println("8. Volver");
        System.out.println("Elegir opcion:");
        int opcion = Entrada.leerEntero(sc);
        switch (opcion){
            case 1->System.out.println("\n💰 Tienes " + jugador.getDinero() + "€");
            case 2->jugador.mostrarEstadoEquipo();
            case 3->jugador.mostrar(jugador);
            case 4->jugador.usarMT(sc);
            case 5->jugador.menuEquipar(sc);
            case 6->GuardarCargar.guardarPartida(jugador); // ya informa de si se guardó o falló
            case 7->{
                System.out.println("\n¡Gracias por jugar!");
                System.exit(1);
            }
            case 8-> {return;}
            default -> System.out.println("Opción no válida.");
        }
    }
}
