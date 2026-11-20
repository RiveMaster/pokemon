package Proyecto;

import java.util.List;
import java.util.Scanner;

public class CentroPokemon {

    // Método principal del Centro Pokémon
    public static void visitar(Scanner sc, PokemonLuchador miPokemon, Jugador jugadorCompleto) {

        if (UIJuego.disponible()) {
            while (true) {
                int opcion = UIJuego.elegir("Centro Pokémon",
                        "¡Bienvenido! Aquí cuidamos de tus Pokémon.\n"
                                + "Pokémon en el equipo: " + jugadorCompleto.getEquipo().size(),
                        "Curar a todo el equipo", "Consultar estadísticas", "PC Pokémon", "Salir");
                List<PokemonLuchador> equipo = jugadorCompleto.getEquipo();
                switch (opcion) {
                    case 0 -> {
                        for (PokemonLuchador p : equipo) p.curarTotal();
                        UIJuego.mensaje("Equipo recuperado", "¡Tus Pokémon han recuperado todos sus PS y están listos para la aventura!");
                    }
                    case 1 -> {
                        if (equipo.isEmpty()) {
                            UIJuego.mensaje("Equipo Pokémon", "No tienes Pokémon en el equipo.");
                            break;
                        }
                        String[] opciones = equipo.stream()
                                .map(p -> p.getNombre() + " · Nv. " + p.getNivel())
                                .toArray(String[]::new);
                        int elegido = UIJuego.elegir("Estadísticas", "Selecciona un Pokémon:", opciones);
                        if (elegido >= 0 && elegido < equipo.size()) {
                            PokemonLuchador p = equipo.get(elegido);
                            UIJuego.mensaje("Datos de " + p.getNombre(),
                                    "Nivel: " + p.getNivel() + "\nPS: " + p.getVidaActual() + "/" + p.getVidaMax()
                                            + "\nAtaque: " + p.getAtaque() + " · Defensa: " + p.getDefensa()
                                            + "\nAtaque especial: " + p.getAtaqueEspecial()
                                            + " · Defensa especial: " + p.getDefensaEspecial()
                                            + "\nVelocidad: " + p.getVelocidad() + "\nIV: " + p.resumenIVs());
                        }
                    }
                    case 2 -> Jugador.menuPC(sc);
                    default -> { return; }
                }
            }
        }

        while (true) {
            System.out.println("\n=== Centro Pokémon ===");
            System.out.println("1. Curar a tu Pokémon");
            System.out.println("2. Ver estadísticas de tu Pokémon");
            System.out.println("3. Acceder al PC");
            System.out.println("4. Salir del Centro Pokémon");
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
                case 3 -> Jugador.menuPC(sc);
                case 4 -> {
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
        System.out.println("Sexo: " + pokemon.getSexo());
        System.out.println("Vida: " + pokemon.getVidaActual() + "/" + pokemon.getVidaMax());

        System.out.println("Ataque: " + pokemon.getAtaque() + " (Base: " + base.getAtaqueBase() + ")");
        System.out.println("Defensa: " + pokemon.getDefensa() + " (Base: " + base.getDefensaBase() + ")");
        System.out.println("Ataque especial: " + pokemon.getAtaqueEspecial() + " (Base: " + (base.getAtaqueEspBase() > 0 ? base.getAtaqueEspBase() : base.getAtaqueBase()) + ")");
        System.out.println("Defensa especial: " + pokemon.getDefensaEspecial() + " (Base: " + (base.getDefensaEspBase() > 0 ? base.getDefensaEspBase() : base.getDefensaBase()) + ")");
        System.out.println("Velocidad: " + pokemon.getVelocidad() + " (Base: " + base.getVelocidadBase() + ")");
        System.out.println("IVs: " + pokemon.resumenIVs());
        System.out.println("Megapiedra: " + (pokemon.getMegapiedra() == null ? "ninguna" : pokemon.getMegapiedra()));
        if (!pokemon.puedeCriarAhora())
            System.out.println("Cooldown de crianza: " + (pokemon.milisegundosParaCriar() / 60000) + " min restantes");

        System.out.println("Experiencia: " + pokemon.getExpActual() + "/" + pokemon.getExpParaSubirNivel());
        System.out.println("==============================\n");
    }
}