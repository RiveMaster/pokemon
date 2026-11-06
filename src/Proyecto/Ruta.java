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

        while (true) {
            if (!jugador.equipoVM()) {
                System.out.println("\nTodos tus Pokémon están debilitados. Te llevan a "
                        + zona.getPuebloCercano().getNombre() + " para curarlos.");
                jugador.curarEquipoTotal();
                return zona.getPuebloCercano();
            }

            System.out.println("\nExplorando " + zona.getNombre() + " [" + gen.etiqueta() + "]...");
            ExploradorMapa.Resultado exploracion = ExploradorMapa.explorar(jugador, zona);

            if (exploracion.huboEncuentro()) {
                System.out.println("\n¡La hierba se mueve!");
                // Los entrenadores mantienen su sistema original, pero solo pueden
                // aparecer tras pisar hierba y activar un encuentro en el mapa.
                boolean entrenador = Npc.intentar(sc, jugador, zona);
                if (!entrenador) {
                    int nivel = zona.getNivelMin()
                            + RAND.nextInt(zona.getNivelMax() - zona.getNivelMin() + 1)
                            + gen.getBonusNivel();
                    PokemonBase base = gen.aleatorio(zona.getTipos(), nivel, RAND);
                    PokemonLuchador salvaje = new PokemonLuchador(base, nivel);
                    System.out.println("¡Un " + salvaje.getNombre() + " salvaje (Nivel " + nivel + ") ha aparecido!");
                    Combate.combateSalvaje(jugador, jugador.getPokemonActivo(), salvaje, sc);
                }

                if (!jugador.equipoVM()) {
                    System.out.println("\nTe has quedado sin Pokémon en pie. Despiertas en "
                            + zona.getPuebloCercano().getNombre() + ".");
                    jugador.curarEquipoTotal();
                    return zona.getPuebloCercano();
                }
                // Tras el combate regresa al mismo punto del mapa y continúa explorando.
                continue;
            }

            // Esc para salir del mapa: se conserva el menú tradicional de la ruta.
            while (true) {
                List<Mapa.Opcion> opciones = new ArrayList<>();
                opciones.add(new Mapa.Opcion("Seguir explorando por la ruta", zona::getUbicacion));
                if (zona.getSiguiente() != null) {
                    Ubicacion siguiente = zona.getSiguiente();
                    opciones.add(new Mapa.Opcion("Ir a " + siguiente.getNombre(), () -> siguiente));
                }
                Ubicacion anterior = zona.getAnterior();
                opciones.add(new Mapa.Opcion("Volver a " + anterior.getNombre(), () -> anterior));
                opciones.add(new Mapa.Opcion("Menú (mochila, MT, objetos, volar, guardar)",
                        () -> Menu.mostrarmenu(sc, jugador)));

                System.out.println("\n¿Qué quieres hacer ahora?");
                Ubicacion destino = Mapa.elegir(sc, opciones);
                if (destino != null) {
                    if (destino == zona.getUbicacion()) break;
                    return destino;
                }
            }
        }
    }
}
