package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Lógica común de todas las rutas. Cada visita = un encuentro salvaje + un menú.
 * Devuelve a dónde va el jugador después (la misma ruta si sigue caminando).
 */
public class Ruta {

    private static final Random RAND = new Random();

    public static Ubicacion explorar(Scanner sc, Jugador jugador, Zona zona) {
        Generacion gen = jugador.getGeneracion();

        if (!jugador.equipoVM()) {
            System.out.println("\nTodos tus Pokémon están debilitados. Te llevan a " + zona.getPuebloCercano().getNombre() + " para curarlos.");
            jugador.curarEquipoTotal();
            return zona.getPuebloCercano();
        }

        System.out.println("\nCaminando por " + zona.getNombre() + " [" + gen.etiqueta() + "]...");

        // A veces te retan entrenadores NPC; si no, sale un Pokémon salvaje de la generación actual
        if (!Npc.intentar(sc, jugador, zona)) {
            int nivel = zona.getNivelMin() + RAND.nextInt(zona.getNivelMax() - zona.getNivelMin() + 1) + gen.getBonusNivel();
            PokemonBase base = gen.aleatorio(zona.getTipos(), nivel, RAND);
            PokemonLuchador salvaje = new PokemonLuchador(base, nivel);
            System.out.println("¡Un " + salvaje.getNombre() + " salvaje (Nivel " + nivel + ") ha aparecido!");

            Combate.combateSalvaje(jugador, jugador.getPokemonActivo(), salvaje, sc);
        }

        if (!jugador.equipoVM()) {
            System.out.println("\nTe has quedado sin Pokémon en pie. Despiertas en " + zona.getPuebloCercano().getNombre() + ".");
            jugador.curarEquipoTotal();
            return zona.getPuebloCercano();
        }

        // Menú tras el combate; las opciones que no mueven (menú) vuelven a preguntar
        while (true) {
            List<Mapa.Opcion> opciones = new ArrayList<>();
            opciones.add(new Mapa.Opcion("Seguir caminando por la ruta", zona::getUbicacion));
            if (zona.getSiguiente() != null) {
                Ubicacion s = zona.getSiguiente();
                opciones.add(new Mapa.Opcion("Ir a " + s.getNombre(), () -> s));
            }
            Ubicacion a = zona.getAnterior();
            opciones.add(new Mapa.Opcion("Volver a " + a.getNombre(), () -> a));
            opciones.add(new Mapa.Opcion("Menú (mochila, MT, objetos, volar, guardar)", () -> Menu.mostrarmenu(sc, jugador)));

            System.out.println("\n¿Qué quieres hacer ahora?");
            Ubicacion destino = Mapa.elegir(sc, opciones);
            if (destino != null) {
                return destino;
            }
        }
    }
}
