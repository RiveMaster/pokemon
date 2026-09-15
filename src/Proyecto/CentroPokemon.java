package Proyecto;

import java.util.List;
import java.util.Scanner;

public class CentroPokemon {

    // Método principal del Centro Pokémon
    public static void visitar(Scanner sc, PokemonLuchador miPokemon, Jugador jugadorCompleto) {

        while (true) {
            System.out.println("\n=== Centro Pokémon ===");
            System.out.println("1. Curar a tu Pokémon");
            System.out.println("2. Ver estadísticas de tu Pokémon");
            System.out.println("3. Salir del Centro Pokémon");
            System.out.print("Elige una opción: ");

            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer
            List<PokemonLuchador> equipo = jugadorCompleto.getEquipo();

            switch (opcion) {
                case 1 -> {
                    for (int k = 0; k < equipo.size(); k++) {
                        PokemonLuchador p = equipo.get(k);
                        p.curarTotal();
                    }
                    System.out.println("¡Tus Pokémons han sido completamente curados!");
                }
                case 2 -> {
                    for (int k = 0; k < equipo.size(); k++) {
                        PokemonLuchador p = equipo.get(k);
                        mostrarEstadisticas(p);
                    }
                }
                case 3 -> {
                    System.out.println("¡Vuelve pronto!");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    // Mostrar estadísticas del Pokémon
    private static void mostrarEstadisticas(PokemonLuchador pokemon) {
        PokemonBase base = pokemon.getBase();

        System.out.println("\n=== Estadísticas de " + base.getNombre() + " ===");
        System.out.println("Nivel: " + pokemon.getNivel());
        System.out.println("Vida: " + pokemon.getVidaActual() + "/" + pokemon.getVidaMax());

        System.out.println("Ataque: " + pokemon.getAtaque() + " (Base: " + base.getAtaqueBase() + ")");
        System.out.println("Defensa: " + pokemon.getDefensa() + " (Base: " + base.getDefensaBase() + ")");
        System.out.println("Velocidad: " + pokemon.getVelocidad() + " (Base: " + base.getVelocidadBase() + ")");

        System.out.println("Experiencia: " + pokemon.getExpActual() + "/" + pokemon.getExpParaSubirNivel());
        System.out.println("==============================\n");
    }
}