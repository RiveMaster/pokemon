package Proyecto;

import java.util.Scanner;

public class Menu {
    /**
     * Menú del juego. Devuelve null normalmente; si el jugador vuela, devuelve el destino
     * y quien lo llama debe llevarle allí.
     */
    public static Ubicacion mostrarmenu(Scanner sc, Jugador jugador){
        System.out.println("\n1. Ver dinero");
        System.out.println("2. Ver estado del Pokémon");
        System.out.println("3. Ver mochila");
        System.out.println("4. Usar MT");
        System.out.println("5. Equipar objeto");
        System.out.println("6. Volar");
        System.out.println("7. Guardar partida");
        System.out.println("8. Salir del juego");
        System.out.println("9. Volver");
        System.out.println("Elegir opcion:");
        int opcion = Entrada.leerEntero(sc);
        switch (opcion){
            case 1->System.out.println("\n💰 Tienes " + jugador.getDinero() + "€");
            case 2->jugador.mostrarEstadoEquipo();
            case 3->jugador.mostrar(jugador);
            case 4->jugador.usarMT(sc);
            case 5->jugador.menuEquipar(sc);
            case 6->{
                return Vuelo.elegirDestino(sc, jugador);
            }
            case 7->GuardarCargar.guardarPartida(sc, jugador); // ya informa de si se guardó o falló
            case 8->{
                System.out.println("\n¡Gracias por jugar!");
                System.exit(1);
            }
            case 9-> {return null;}
            default -> System.out.println("Opción no válida.");
        }
        return null;
    }
}
