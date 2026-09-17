package Proyecto;

import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Encuentros implements Serializable {

        private static final long serialVersionUID = 1L;
    public static final int ENCUENTRO_INICIAL = 0;
    public static final int ENCUENTRO_COMPLETADO = 1;
    public static final int ENCUENTRO_DIFERENTE = 2;
    public static final int ENCUENTRO_POTENTE = 3;
    public static final int SINENCUENTROS = 4;


    public static Jugador encuentroInicial(Scanner sc){
        Random random = new Random();
            System.out.println("Profesor Garcia: ¡Hola! ¡Bienvenido al mundo de Pokémon!");
            System.out.println("Mi nombre es Martin Garcia. Pero la gente me llama Profesor Garcia.\n");

            System.out.print("Profesor Garcia: Antes de empezar... ¿Cómo te llamas? ");
            String nombreJugador = sc.nextLine();

            System.out.println("Profesor Garcia: " + nombreJugador + " este es tu primo");
            System.out.println("sois muy buenos amigos pero veo que teneis una pequeña rivalidad");
            System.out.print("Profesor Garcia: ¿Cómo dijiste que se llamaba tu primo? ");
            String nombreRival = sc.nextLine();

            System.out.println("\nProfesor Garcia: Muy bien " + nombreJugador + ".");
            System.out.println("Profesor Garcia: Ir por la hierba alta sin pokemones es peligroso.");
            System.out.println("Aquí tienes tres Pokémon. Elige uno:");

            System.out.println("1 - Bulbasaur (Planta/Veneno)");
            System.out.println("2 - Charmander (Fuego)");
            System.out.println("3 - Squirtle (Agua)");
            System.out.print("Elige tu Pokémon (1-3): ");

            int eleccion = sc.nextInt();
            sc.nextLine();

            List<PokemonBase> iniciales = Arrays.asList(
                    Pokedex.buscarPorNombre("Bulbasaur"),
                    Pokedex.buscarPorNombre("Charmander"),
                    Pokedex.buscarPorNombre("Squirtle")
            );

            PokemonBase elegidoJugador = iniciales.get(Math.max(0, Math.min(2, eleccion - 1)));

            List<PokemonBase> restantes = new ArrayList<>(iniciales);
            restantes.remove(elegidoJugador);


            PokemonBase elegidoRival = restantes.get(random.nextInt(restantes.size()));

            System.out.println("\nProfesor Garcia: ¡Has elegido a " + elegidoJugador.getNombre() + "!");
            System.out.println("Profesor Garcia: " + nombreRival + " eligió a " + elegidoRival.getNombre() + ".");

            PokemonLuchador miPokemon = new PokemonLuchador(elegidoJugador, 5);
            miPokemon.getNombre();
            PokemonLuchador pokemonRival = new PokemonLuchador(elegidoRival, 5);

            Jugador jugador = new Jugador(nombreJugador, miPokemon, 1000);
            Rival rival = new Rival(nombreRival, pokemonRival);

            // Combate inicial
            System.out.println("\nProfesor Garcia: ¡Tu primo quiere un combate contigo!");
            Combate.combateInicial(jugador, rival, sc);

        jugador.setEncuentroActual(ENCUENTRO_COMPLETADO);

        System.out.println("\n¡El encuentro inicial ha sido completado!");
        System.out.println("Ahora puedes explorar libremente.\n");

        return jugador;
    }

    public static Jugador ENCUENTRO_COMPLETADO(Jugador jugador, Scanner sc){
        System.out.println(jugador.getRival()+": Oye, ¿que coño pasa con los putos inmigrantes de mierda? No paran de intentar quitarme los pokemons y el dinero, bueno todo en general..." +
                " en fin... Peleemos");
        jugador.getRival().setEquipoRival1(Equipo1(jugador.getRival()));
        Combate.combateRival(jugador, jugador.getRival(), jugador.getPokemon(), jugador.getRival().getPokemon(), sc);
        jugador.setEncuentroActual(ENCUENTRO_DIFERENTE);
        return jugador;
    }

    public static Jugador ENCUENTRO_DIFERENTE(Jugador jugador, Scanner sc){
        System.out.println(jugador.getRival()+": adivina a quien le van a abrir el culo hoy... Exacto, después de hoy quedarás como mi putita. ");
        jugador.getRival().setEquipoRival1(Equipo2(jugador.getRival()));
        Combate.combateRival(jugador, jugador.getRival(), jugador.getPokemon(), jugador.getRival().getPokemon(), sc);
        jugador.setEncuentroActual(ENCUENTRO_POTENTE);
        return jugador;
    }

    public static Jugador ENCUENTRO_POTENTE(Jugador jugador, Scanner sc){
        System.out.println(jugador.getRival()+": Se acabo, aquí y ahora te ganaré, tengo un equipo mas que perfecto para ganar... Aprovecha para aprender por que este será nuestro ultimo encuentro.");
        jugador.getRival().setEquipoRival1(Equipo3(jugador.getRival()));
        Combate.combateRival(jugador, jugador.getRival(), jugador.getPokemon(), jugador.getRival().getPokemon(), sc);
        jugador.setEncuentroActual(SINENCUENTROS);
        return jugador;
    }

    private static List<PokemonLuchador> Equipo1(Rival rival) {
        List<PokemonLuchador> equipo1 = new ArrayList<>();
        equipo1.add(new PokemonLuchador(Pokedex.buscarPorNombre("Pikachu"), 10));
        equipo1.add(new PokemonLuchador(rival.getPokemon().getBase(),10));
        return equipo1;
    }
    private static List<PokemonLuchador> Equipo2(Rival rival) {

        List<PokemonLuchador> equipo2 = new ArrayList<>();
        equipo2.add(new PokemonLuchador(rival.getPokemon().getBase(),30));
        equipo2.add(new PokemonLuchador(Pokedex.buscarPorNombre("Meowth"),25));
        equipo2.add(new PokemonLuchador(Pokedex.buscarPorNombre("Spearow"),25));
        equipo2.add(new PokemonLuchador(Pokedex.buscarPorNombre("Sandshrew"),25));
        return equipo2;
    }
    private static List<PokemonLuchador> Equipo3(Rival rival){
        List<PokemonLuchador> equipo3 = new ArrayList<>();
        equipo3.add(new PokemonLuchador(rival.getPokemon().getBase(),60));
        equipo3.add(new PokemonLuchador(Pokedex.buscarPorNombre("Pidgeot"),55));
        equipo3.add(new PokemonLuchador(Pokedex.buscarPorNombre("Alakazam"),55));
        equipo3.add(new PokemonLuchador(Pokedex.buscarPorNombre("Gyarados"),55));
        equipo3.add(new PokemonLuchador(Pokedex.buscarPorNombre("Exeggutor"),55));
        equipo3.add(new PokemonLuchador(Pokedex.buscarPorNombre("Arcanine"),55));
        return equipo3;
    }
}
