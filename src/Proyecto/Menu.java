package Proyecto;

import java.util.Scanner;

public class Menu {
    /**
     * Menú del juego. Devuelve null normalmente; si el jugador vuela, devuelve el destino
     * y quien lo llama debe llevarle allí.
     */
    public static Ubicacion mostrarmenu(Scanner sc, Jugador jugador){
        if (UIJuego.disponible()) {
            while (true) {
                int opcion = UIJuego.elegir("Menú del entrenador",
                        jugador.getNombre() + " · " + jugador.getGeneracion().etiqueta()
                                + "\nDinero: " + jugador.getDinero() + "€"
                                + " · Medallas: " + jugador.medallasEnGeneracion(jugador.getGeneracion()),
                        "Equipo Pokémon", "Mochila", "Usar MT",
                        "Equipar objeto", "Volar", "Guardar partida",
                        "Criadero Pokémon", "Megaevolución", "Atlas regional (10 mapas)",
                        "Volver al mapa", "Salir del juego");
                switch (opcion) {
                    case 0 -> jugador.mostrarEstadoEquipo();
                    case 1 -> jugador.mostrar(jugador);
                    case 2 -> jugador.usarMT(sc);
                    case 3 -> jugador.menuEquipar(sc);
                    case 4 -> {
                        Ubicacion destino = Vuelo.elegirDestino(sc, jugador);
                        if (destino != null) return destino;
                    }
                    case 5 -> GuardarCargar.guardarPartida(sc, jugador);
                    case 6 -> SistemaCrianza.visitar(sc, jugador);
                    case 7 -> MegaEvoluciones.gestionarPiedras(sc, jugador);
                    case 8 -> AtlasRegional.abrir(sc, jugador);
                    case 9, -1 -> { return null; }
                    case 10 -> {
                        if (UIJuego.confirmar("Salir de la partida",
                                "¿Quieres guardar la partida antes de salir?")) {
                            GuardarCargar.guardarPartida(sc, jugador);
                        }
                        if (UIJuego.confirmar("Salir del juego",
                                "¿Seguro que quieres cerrar la partida?")) System.exit(0);
                    }
                    default -> { return null; }
                }
            }
        }

        System.out.println("\n1. Ver dinero");
        System.out.println("2. Ver estado del Pokémon");
        System.out.println("3. Ver mochila");
        System.out.println("4. Usar MT");
        System.out.println("5. Equipar objeto");
        System.out.println("6. Volar");
        System.out.println("7. Guardar partida");
        System.out.println("8. Salir del juego");
        System.out.println("9. Volver");
        System.out.println("10. Criadero Pokémon");
        System.out.println("11. Centro de Megaevolución");
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
            case 10 -> SistemaCrianza.visitar(sc, jugador);
            case 11 -> MegaEvoluciones.gestionarPiedras(sc, jugador);
            default -> System.out.println("Opción no válida.");
        }
        return null;
    }
}
