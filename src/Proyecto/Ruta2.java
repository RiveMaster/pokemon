package Proyecto;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Ruta2 {

    private Random rand = new Random();

    // ===============================
    //      GENERAR POKÉMON SALVAJE
    // ===============================
    private PokemonBase generarPokemonBase() {
        int prob = rand.nextInt(1000) + 1;
        if (prob <= 200) return Pokedex.buscarPorNombre("Pidgey");
        else if (prob <= 400) return Pokedex.buscarPorNombre("Nidoran");
        else if (prob <= 600) return Pokedex.buscarPorNombre("Nidorana");
        else if (prob <= 800)  return Pokedex.buscarPorNombre("Budew");
        else if (prob <= 950) return Pokedex.buscarPorNombre("Dedenne");
        else if (prob <= 999) return Pokedex.buscarPorNombre("Togepi");
        else return Pokedex.buscarPorNombre("Mewtow");
    }

    private int generarNivel() {
        return rand.nextInt(3) + 5;
    }

    private Pokemon generarPokemonSalvaje() {
        return new Pokemon(generarPokemonBase(), generarNivel());
    }

    // ===============================
    //   CLASE INTERNA POKÉMON SALVAJE
    // ===============================
    public static class Pokemon {
        private PokemonBase base;
        private int nivel;

        public Pokemon(PokemonBase base, int nivel) {
            this.base = base;
            this.nivel = nivel;
        }

        public PokemonBase getBase() { return base; }
        public int getNivel() { return nivel; }

        @Override
        public String toString() {
            return base.getNombre() + " (Nivel " + nivel + ")";
        }
    }

    // ===============================
    //         ENTRAR A RUTA 2
    // ===============================
    public static void entrarRuta2(Scanner sc, Jugador jugadorCompleto) {

        Ruta2 ruta = new Ruta2();
        boolean seguir = true;

        while (seguir) {

            // ===============================
            // ENCUENTRO SALVAJE
            // ===============================
            System.out.println("\nCaminando por la Ruta 1...");
            Ruta2.Pokemon baseSalvaje = ruta.generarPokemonSalvaje();
            PokemonLuchador salvaje = new PokemonLuchador(baseSalvaje.getBase(), ruta.generarNivel());

            System.out.println("¡Un " + salvaje + " ha aparecido!");

            boolean combateActivo = true;

            // ===============================
            // COMBATE
            // ===============================

            Combate.combateSalvaje(jugadorCompleto, jugadorCompleto.getPokemon(), salvaje, sc);

            // ===============================
            // MENÚ DESPUÉS DEL COMBATE
            // ===============================
            System.out.println("\n¿Qué quieres hacer ahora?");
            System.out.println("1. Seguir caminando por la ruta");
            System.out.println("2. Ir al Bosque Cantabria");
            System.out.println("3. Volver a Ciudad Oviedo");
            System.out.print("Elige: ");

            int accion = sc.nextInt();

            switch (accion) {
                case 1 -> {} // repetir el bucle
                case 2 -> {
                    System.out.println("Te diriges al Bosque Cantabria...");
                    seguir = false;
                }
                case 3 -> {
                    System.out.println("Vuelves a Ciudad Oviedo...");
                    Oviedo.entrarOviedo(sc, jugadorCompleto);
                    return;
                }
                default -> System.out.println("Opción no válida. Sigues caminando.");
            }
        }
    }
}