package Proyecto;

import java.util.List;
import java.util.Scanner;

/**
 * Combate por turnos contra un entrenador con varios Pokémon (líderes de gimnasio,
 * jefes del Gobierno...). Solo se encarga del combate: las recompensas las da quien llama.
 */
public class CombateEntrenador {

    /** Devuelve true si el jugador derrota a todo el equipo rival. */
    public static boolean combatir(Scanner sc, Jugador jugador, String entrenador, List<PokemonLuchador> rivales) {
        if (rivales.isEmpty()) {
            System.out.println(entrenador + " no tiene Pokémon disponibles, ¡victoria por incomparecencia!");
            return true;
        }

        int indiceRival = 0;
        PokemonLuchador rival = rivales.get(indiceRival);
        PokemonLuchador activo = primerVivo(jugador);

        System.out.println("\n" + entrenador + " envía a " + rival.getNombre() + " (Nv." + rival.getNivel() + ")!");
        System.out.println("¡Adelante " + activo.getNombre() + "!");

        while (true) {
            Combate.mostrarEstado(activo, rival);

            Combate.efecto(rival);
            Combate.efecto(activo);

            // El veneno o las quemaduras pueden haber debilitado a alguien antes de actuar
            if (!rival.estaVivo()) {
                indiceRival++;
                rival = siguienteRival(entrenador, rivales, indiceRival);
                if (rival == null) return true;
                continue;
            }
            if (!activo.estaVivo()) {
                activo = cambioForzado(sc, jugador, activo);
                if (activo == null) return false;
                continue;
            }

            System.out.println("1. Atacar");
            System.out.println("2. Cambiar Pokémon");
            System.out.println("3. Rendirse");
            System.out.print("Elige acción: ");
            int opcion = Entrada.leerEntero(sc);

            boolean rivalAtaca;

            if (opcion == 1) {
                Combate.turnoJugador(activo, rival, sc);
                rivalAtaca = true;

                if (!rival.estaVivo()) {
                    System.out.println("\n¡" + rival.getNombre() + " de " + entrenador + " ha sido debilitado!");
                    activo.ganarExp(rival.getNivel() * 10);
                    indiceRival++;
                    rival = siguienteRival(entrenador, rivales, indiceRival);
                    if (rival == null) return true;
                    rivalAtaca = false; // el nuevo Pokémon acaba de salir
                }
            } else if (opcion == 2) {
                PokemonLuchador nuevo = elegirCambio(sc, jugador, activo);
                if (nuevo == null) {
                    continue; // canceló: no gasta turno
                }
                activo = nuevo;
                System.out.println("¡Adelante " + activo.getNombre() + "!");
                rivalAtaca = true; // el rival aprovecha el cambio
            } else if (opcion == 3) {
                System.out.println("Te rindes ante " + entrenador + "...");
                return false;
            } else {
                System.out.println("Opción no válida.");
                continue;
            }

            if (rivalAtaca) {
                Combate.turnoRival(activo, rival);
            }

            if (!activo.estaVivo()) {
                activo = cambioForzado(sc, jugador, activo);
                if (activo == null) return false;
            }
        }
    }

    private static PokemonLuchador siguienteRival(String entrenador, List<PokemonLuchador> rivales, int indice) {
        if (indice >= rivales.size()) {
            return null;
        }
        PokemonLuchador siguiente = rivales.get(indice);
        System.out.println("\n" + entrenador + " envía a " + siguiente.getNombre() + " (Nv." + siguiente.getNivel() + ")!");
        return siguiente;
    }

    private static PokemonLuchador primerVivo(Jugador jugador) {
        for (PokemonLuchador p : jugador.getEquipo()) {
            if (p.estaVivo()) {
                return p;
            }
        }
        return null;
    }

    /** Cambio voluntario. Devuelve null si el jugador cancela o la elección no vale. */
    private static PokemonLuchador elegirCambio(Scanner sc, Jugador jugador, PokemonLuchador activo) {
        List<PokemonLuchador> equipo = jugador.getEquipo();
        System.out.println("Elige un Pokémon para enviar al combate (0 para cancelar):");
        for (int k = 0; k < equipo.size(); k++) {
            PokemonLuchador p = equipo.get(k);
            String estado = p.estaVivo() ? "Vivo" : "Debilitado";
            System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
        }
        int idx = Entrada.leerEntero(sc) - 1;
        if (idx < 0 || idx >= equipo.size()) {
            return null;
        }
        PokemonLuchador elegido = equipo.get(idx);
        if (!elegido.estaVivo()) {
            System.out.println("No puedes sacar a un Pokémon debilitado.");
            return null;
        }
        if (elegido == activo) {
            System.out.println("¡" + elegido.getNombre() + " ya está en combate!");
            return null;
        }
        return elegido;
    }

    /** Cambio obligatorio tras un KO. Devuelve null si no quedan Pokémon vivos. */
    private static PokemonLuchador cambioForzado(Scanner sc, Jugador jugador, PokemonLuchador debilitado) {
        System.out.println("\n" + debilitado.getNombre() + " ha sido debilitado.");
        if (!jugador.equipoVM()) {
            System.out.println("¡Todos tus Pokémon han sido debilitados!");
            return null;
        }
        while (true) {
            System.out.println("¡Debes sacar a otro Pokémon!");
            PokemonLuchador nuevo = elegirCambio(sc, jugador, debilitado);
            if (nuevo != null) {
                System.out.println("¡Adelante " + nuevo.getNombre() + "!");
                return nuevo;
            }
        }
    }
}
