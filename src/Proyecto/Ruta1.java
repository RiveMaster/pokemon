package Proyecto;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static Proyecto.PokemonLuchador.combate;

public class Ruta1 {

    private Random rand = new Random();

    public static void ruta1(Scanner sc) {
    }

    // Generar Pokémon salvaje
    private PokemonBase generarPokemonBase() {
        int prob = rand.nextInt(1000) + 1;
        if (prob <= 500) return Pokedex.buscarPorNombre("Bunnelby");
        else if (prob <= 800) return Pokedex.buscarPorNombre("Pidgey");
        else if (prob <= 950) return Pokedex.buscarPorNombre("Ambipom");
        else if (prob <= 999) return Pokedex.buscarPorNombre("Shinx");
        else return Pokedex.buscarPorNombre("Mewtow");
    }

    private int generarNivel() {
        return rand.nextInt(3) + 3;
    }

    private Pokemon generarPokemonSalvaje() {
        return new Pokemon(generarPokemonBase(), generarNivel());
    }

    public void irAPuebloVillaverde() {
        System.out.println("Has vuelto a Pueblo Villaverde");
    }

    // Clase interna Pokémon
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

    // Método principal de la ruta
    public static void entrarRuta1(Scanner sc, Jugador jugadorCompleto) {
        Ruta1 ruta = new Ruta1();
        boolean seguir = true;

        while (seguir) {
            System.out.println("\nCaminando por la Ruta 1...");
            Pokemon baseSalvaje = ruta.generarPokemonSalvaje();
            PokemonLuchador salvaje = new PokemonLuchador(baseSalvaje.getBase(), ruta.generarNivel());

            System.out.println("¡Un " + salvaje + " ha aparecido!");

            Combate.combateSalvaje(jugadorCompleto, jugadorCompleto.getPokemon(), salvaje, sc);

            // Menú tras combate
            System.out.println("\n¿Qué quieres hacer ahora?");
            System.out.println("1. Seguir caminando por la ruta");
            System.out.println("2. Ir a Oviedo");
            System.out.println("3. Volver a Pueblo Villaverde");
            System.out.print("Elige: ");
            int accion = sc.nextInt();

            switch (accion) {
                case 1 -> {} // seguir el while
                case 2 -> {
                    System.out.println("Te diriges a Oviedo...");
                    Oviedo.entrarOviedo(sc, jugadorCompleto);
                    seguir = false;
                }
                case 3 -> {
                    ruta.irAPuebloVillaverde();
                    seguir = false;
                }
                default -> System.out.println("Opción no válida. Regresas al pueblo.");
            }
        }
        Pokemon salvaje = ruta.generarPokemonSalvaje();
        PokemonLuchador rival = new PokemonLuchador(salvaje.getBase(), salvaje.getNivel());
        System.out.println("¡Un " + salvaje + " ha aparecido!");
    }

    // Método auxiliar para que el jugador elija el movimiento
    private static void jugadorAtaca(Scanner sc, PokemonLuchador jugador, PokemonLuchador rival) {
        List<movimiento> movs = jugador.getMovimientos();
        if (movs.isEmpty()) {
            System.out.println("¡Tu Pokémon no tiene movimientos!");
            return;
        }

        System.out.println("\nElige un movimiento:");
        for (int i = 0; i < movs.size(); i++) {
            System.out.println((i + 1) + ". " + movs.get(i).getNombre() +
                    " (Potencia " + movs.get(i).getPotencia() + ")");
        }

        int eleccion = sc.nextInt();
        if (eleccion < 1 || eleccion > movs.size()) {
            System.out.println("Movimiento inválido. Pierdes el turno.");
        } else {
            movimiento movJugador = movs.get(eleccion - 1);
            System.out.println("\n" + jugador.getNombre() + " usa " + movJugador.getNombre() + "!");
            rival.recibirDaño(movJugador.getPotencia());
        }
    }
}