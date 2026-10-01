package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * Entrenadores NPC que pueden retarte al caminar por una ruta. Se generan al vuelo
 * (nombre, equipo y nivel), así que no hay que guardarlos: cada encuentro es nuevo.
 * Todos los nombres son inventados.
 */
public class Npc {

    private static final Random RAND = new Random();

    // Probabilidad de que, en vez de un Pokémon salvaje, te rete un entrenador (ajustable desde pruebas).
    static double probabilidad = 0.30;

    private static final String[] NOMBRES = {
        "Marcos", "Lucía", "Iker", "Nerea", "Hugo", "Paula", "Aitor", "Carmen", "Dani", "Sofía",
        "Unai", "Irene", "Pablo", "Marta", "Jon", "Alba", "Gael", "Noa", "Rubén", "Elena"
    };

    private static final String[] FRASES_RETO = {
        "¡Nuestras miradas se han cruzado! ¡Eso significa combate!",
        "¡Llevaba toda la mañana esperando a alguien con quien probar mi equipo!",
        "¡Eh, tú! ¡Sí, tú! ¿Te atreves con un combate rápido?",
        "¡No pasarás por aquí sin luchar!",
        "Mis Pokémon están entrenadísimos. ¿Y los tuyos?"
    };

    private static final String[] FRASES_DERROTA = {
        "¡Vaya! Me has ganado. Toma, te lo has ganado.",
        "Buf... tendré que entrenar más. Aquí tienes tu premio.",
        "¡Qué combate! La próxima vez ganaré yo."
    };

    /*
     * Con cierta probabilidad, un NPC te reta. Devuelve true si hubo encuentro con NPC
     * (aceptado o rechazado), y entonces NO debe salir un Pokémon salvaje en esta vuelta.
     */
    public static boolean intentar(Scanner sc, Jugador jugador, Zona zona) {
        return intentar(sc, jugador, zona, RAND.nextDouble() < probabilidad);
    }

    // Versión que permite forzar el encuentro (para pruebas).
    static boolean intentar(Scanner sc, Jugador jugador, Zona zona, boolean aparece) {
        String[] clases = zona.getEntrenadores();
        if (!aparece || clases == null || clases.length == 0) {
            return false;
        }

        Generacion gen = jugador.getGeneracion();
        String nombre = clases[RAND.nextInt(clases.length)] + " " + NOMBRES[RAND.nextInt(NOMBRES.length)];

        // 1 a 3 Pokémon distintos de los tipos de la ruta, a niveles de la zona
        int cantidad = 1 + RAND.nextInt(3);
        List<PokemonLuchador> equipo = new ArrayList<>();
        int sumaNiveles = 0;
        for (int i = 0; i < cantidad; i++) {
            int nivel = zona.getNivelMin() + RAND.nextInt(zona.getNivelMax() - zona.getNivelMin() + 1) + gen.getBonusNivel();
            PokemonBase base = gen.aleatorio(zona.getTipos(), nivel, RAND);
            if (yaEsta(equipo, base) && i > 0) {
                base = gen.aleatorio(zona.getTipos(), nivel, RAND); // un segundo intento para no repetir
            }
            equipo.add(new PokemonLuchador(base, nivel));
            sumaNiveles += nivel;
        }

        System.out.println("\n👤 ¡" + nombre + " quiere combatir! (" + cantidad + (cantidad == 1 ? " Pokémon" : " Pokémon")
                + ", nivel medio " + (sumaNiveles / cantidad) + ")");
        System.out.println(nombre + ": " + FRASES_RETO[RAND.nextInt(FRASES_RETO.length)]);
        System.out.println("1. Aceptar el combate");
        System.out.println("2. Rechazar y seguir tu camino");
        System.out.print("Elige: ");

        if (Entrada.leerEntero(sc) != 1) {
            System.out.println("Rechazas el reto y sigues tu camino.");
            return true;
        }

        boolean victoria = CombateEntrenador.combatir(sc, jugador, nombre, equipo);
        if (victoria) {
            int premio = sumaNiveles * 6;
            jugador.agregarDinero(premio);
            System.out.println("\n" + nombre + ": " + FRASES_DERROTA[RAND.nextInt(FRASES_DERROTA.length)]);
            System.out.println(" Has ganado " + premio + "€.");
        } else {
            System.out.println("\n" + nombre + ": ¡He ganado! ¡Cuida mejor de tu equipo!");
        }
        return true;
    }

    private static boolean yaEsta(List<PokemonLuchador> equipo, PokemonBase base) {
        for (PokemonLuchador p : equipo) {
            if (p.getNombre().equals(base.getNombre())) return true;
        }
        return false;
    }
}
