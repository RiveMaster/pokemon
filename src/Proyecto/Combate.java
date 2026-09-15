package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Combate {

    private static Random rand = new Random();

    public static void combateInicial(Jugador jugador, Rival rival, Scanner sc) {

        System.out.println("\n=== ¡COMIENZA EL COMBATE! ===\n");

        while (jugador.getPokemon().estaVivo() && rival.getPokemon().estaVivo()) {

            // Mostrar barras de vida
            System.out.println("\n===============================");
            System.out.println("Tu Pokémon: " + jugador.getNombre() + "  Nv." + jugador.getNivel());
            System.out.println("Vida: " + jugador.getPokemon().getVidaActual() + "/" + jugador.getPokemon().getVidaMax());
            System.out.println("-------------------------------");
            System.out.println("Rival: " + rival.getNombre() + "  Nv." + rival.getPokemon().getNivel());
            System.out.println("Vida: " + rival.getPokemon().getVidaActual() + "/" + rival.getPokemon().getVidaMax());
            System.out.println("===============================\n");

            System.out.println("1. Atacar");
            System.out.print("Elige acción: ");

            int opcion = sc.nextInt();

            if (opcion == 1) {

                List<movimiento> movs = jugador.getPokemon().getMovimientos();

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

                    rival.getPokemon().recibirDaño(movJugador.getPotencia());

                    if (!rival.getPokemon().estaVivo()) {
                        System.out.println("\n" + rival.getNombre() + " ha sido debilitado!");
                        jugador.getPokemon().ganarExp(rival.getPokemon().getNivel()*10);
                        break;
                    }
                }

                // Turno rival
                movimiento movRival = rival.getPokemon().getMovimientos().get(0);
                System.out.println("\nEl rival usa " + movRival.getNombre() + "!");
                jugador.getPokemon().recibirDaño(movRival.getPotencia());

                if (!jugador.getPokemon().estaVivo()) {
                    System.out.println("\n" + jugador.getNombre() + " ha sido debilitado...");
                    break;
                }

            }
        }
    }


    // Combate contra un rival u otro jugador
    public static void combateRival(Jugador Jugar, Rival tontito,PokemonLuchador jugador, PokemonLuchador rival, Scanner sc) {
        System.out.println("\n=== ¡COMIENZA EL COMBATE CONTRA " + rival.getNombre() + "! ===");

        boolean combateActivo = !equipoMR(tontito)&&!equipoM(Jugar);
        while (combateActivo) {
            mostrarEstado(jugador, rival);

            efecto(rival);
            efecto(jugador);

            System.out.println("1. Atacar");
            System.out.print("Elige acción: ");
            int opcion = sc.nextInt();

            if (opcion == 1) {
                turnoJugador(jugador, rival, sc);

                if (!rival.estaVivo()) {
                    System.out.println("\n¡Has ganado el combate contra " + rival.getNombre() + "!");
                    jugador.ganarExp(200); // EXP por rival
                    break;
                }

                turnoRival(jugador, rival);

                if (!jugador.estaVivo()) {
                    System.out.println("\n¡Tu Pokémon ha sido debilitado!");
                    break;
                }

            } else {
                System.out.println("Opción inválida, pierdes el turno.");
            }
        }
    }

    public static void combatepodemita(Jugador jugadorCompleto, PokemonLuchador pokemonJugador, PokemonLuchador pokemonpodemita, Gobierno gobierno, Scanner sc){
        System.out.println("\n=== ¡Un Podemita ha aparecido y quiere pelear contra tí! ===");
        System.out.println("Podemita: ¡Ganare por Pedro Sanchez, viva la izquierda!");
        boolean combateActivo = !equipoMG(gobierno)&&!equipoM(jugadorCompleto);
        while (combateActivo){
            mostrarEstado(pokemonJugador, pokemonpodemita);

            efecto(pokemonpodemita);
            efecto(pokemonJugador);

            System.out.println("1. Atacar");
            System.out.println("2. Huir");
            System.out.println("3. Mochila");
            System.out.print("Elige acción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            if (opcion == 1){
                turnoJugador(pokemonJugador, pokemonpodemita, sc);
                if (!pokemonpodemita.estaVivo()){
                    System.out.println("\n¡"+pokemonpodemita+" ha sido debilitado!");
                    pokemonJugador.ganarExp(pokemonJugador.getNivel()*10);
                    pokemuertoR(pokemonpodemita, gobierno, combateActivo);
                    // hacer en futuro importante
                }
                turnoRival(pokemonJugador, pokemonpodemita);
                if (!pokemonJugador.estaVivo()){
                    pokemuerto(pokemonJugador, jugadorCompleto, sc, combateActivo);
                }
            }else if (opcion == 2){
                System.out.println("Escapas del combate para no aguantar a un rojo tocacojones.");
                combateActivo = false;
            }else if (opcion == 3){
                System.out.println("1. Usar pocion");
                System.out.println("2. Cambiar pokemon");
                System.out.println("Elegir opcion:");
                int n = sc.nextInt();
                if (n==1){
                    System.out.println("Elige el tipo de cura (ej. 'pocion', 'superpocion', 'hiperpocion'):");
                    String tipomedicina= sc.nextLine();

                    // Llamar al void de Jugador
                    jugadorCompleto.curarPokemon(pokemonJugador, tipomedicina);
                    turnoJugador(pokemonJugador, pokemonpodemita, sc);
                }
            }

        }
    }

    // Combate contra Pokémon salvaje
    public static void combateSalvaje(Jugador jugadorCompleto, PokemonLuchador pokemonJugador, PokemonLuchador salvaje, Scanner sc) {
        System.out.println("\n=== ¡Un Pokémon salvaje aparece! ===");

        boolean combateActivo = salvaje.estaVivo()&&!equipoM(jugadorCompleto);

        while (combateActivo) {
            mostrarEstado(pokemonJugador, salvaje);

            efecto(salvaje);
            efecto(pokemonJugador);

            System.out.println("1. Atacar");
            System.out.println("2. Huir");
            System.out.println("3. Mochila");
            System.out.print("Elige acción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            if (opcion == 1) {
                turnoJugador(pokemonJugador, salvaje, sc);


                if (!salvaje.estaVivo()) {
                    System.out.println("\n¡El Pokémon salvaje ha sido debilitado!");
                    pokemonJugador.ganarExp(300); // EXP por salvaje
                    break;
                }

                turnoRivalSalvaje(pokemonJugador, salvaje);

                if (!pokemonJugador.estaVivo()) {
                    pokemuerto(pokemonJugador, jugadorCompleto, sc, combateActivo);
                }

            } else if (opcion == 2) {
                System.out.println("Escapas del combate...");
                combateActivo = false;
            }else if (opcion == 3){
                System.out.println("1. Capturar");
                System.out.println("2. Usar pocion");
                System.out.println("3. Cambiar pokemon");
                System.out.print("Elige acción: ");
                int n = sc.nextInt();
                sc.nextLine();
                if (n==1) {
                    System.out.println("Elige el tipo de Poké Ball (ej. 'Poke Ball', 'Super Ball'):");
                    String tipoPokeball = sc.nextLine();
                    // Llamar a la función booleana de Jugador
                    if (jugadorCompleto.capturarPokemon(salvaje, tipoPokeball)) {
                        System.out.println("¡Combate terminado! Pokémon capturado.");
                        combateActivo = false;  // Terminar el combate si capturado
                    } else {
                        System.out.println("El Pokémon no fue capturado. ¡Continúa el combate!");
                        // Continuar con el turno del salvaje después de fallo
                        turnoRivalSalvaje(pokemonJugador, salvaje);
                        if (!pokemonJugador.estaVivo()) {
                            pokemuerto(pokemonJugador, jugadorCompleto, sc, combateActivo);
                        }
                    }
                }else if (n==2){
                    System.out.println("Elige el tipo de cura (ej. 'pocion', 'superpocion', 'hiperpocion'):");
                    String tipomedicina= sc.nextLine();

                    // Llamar al void de Jugador
                    jugadorCompleto.curarPokemon(pokemonJugador, tipomedicina);
                    turnoJugador(pokemonJugador, salvaje, sc);
                }else if (n==3){
                //aqui viene cuando la mata.......
                    System.out.println("Elige un Pokémon para enviar al combate:");
                    List<PokemonLuchador> equipo = jugadorCompleto.getEquipo(); // Asegúrate que este método existe en Jugador

                    for (int k = 0; k < equipo.size(); k++) {
                        PokemonLuchador p = equipo.get(k);
                        String estado = p.estaVivo() ? "Vivo" : "Debilitado";
                        System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
                    }

                    int idx = sc.nextInt() - 1;
                    if (idx >= 0 && idx < equipo.size()) {
                        PokemonLuchador nuevo = equipo.get(idx);

                        if (nuevo == pokemonJugador) {
                            System.out.println("¡" + nuevo.getNombre() + " ya está en combate!");
                        } else if (!nuevo.estaVivo()) {
                            System.out.println("No puedes sacar a un Pokémon debilitado.");
                        } else {
                            System.out.println("¡Vuelve " + pokemonJugador.getNombre() + "!");
                            pokemonJugador = nuevo; // Actualizamos el Pokémon activo
                            System.out.println("¡Adelante " + pokemonJugador.getNombre() + "!");

                            // Al cambiar, el rival aprovecha para atacar
                            turnoRivalSalvaje(pokemonJugador, salvaje);

                            if (!pokemonJugador.estaVivo()) {
                                pokemuerto(pokemonJugador, jugadorCompleto, sc, combateActivo);
                            }
                        }
                    } else {
                        System.out.println("Selección inválida.");
                    }
                }else{
                    System.out.println("Opción inválida, pierdes el turno.");
                }
            } else {
                System.out.println("Opción inválida, pierdes el turno.");
            }
        }
    }

    // Mostrar estado de los Pokémon
    private static void mostrarEstado(PokemonLuchador jugador, PokemonLuchador rival) {
        System.out.println("\n===============================");
        System.out.println("Tu Pokémon: " + jugador.getNombre() + "  Nv." + jugador.getNivel());
        System.out.println("Vida: " + jugador.getVidaActual() + "/" + jugador.getVidaMax());
        System.out.println("-------------------------------");
        System.out.println("Rival: " + rival.getNombre() + "  Nv." + rival.getNivel());
        System.out.println("Vida: " + rival.getVidaActual() + "/" + rival.getVidaMax());
        System.out.println("===============================");
    }

    // Turno del jugador
    private static void turnoJugador(PokemonLuchador jugador, PokemonLuchador rival, Scanner sc) {
        List<movimiento> movs = jugador.getMovimientos();

        if (movs.isEmpty()) {
            System.out.println("¡Tu Pokémon no tiene movimientos!");
            return;
        }

        System.out.println("\nElige un movimiento:");
        for (int i = 0; i < movs.size(); i++) {
            System.out.println((i + 1) + ". " + movs.get(i).getNombre() + " (Potencia " + (movs.get(i).getPotencia()+jugador.getNivel()/2) + ")");
        }

        int eleccion = sc.nextInt();
        if (eleccion < 1 || eleccion > movs.size()) {
            System.out.println("Movimiento inválido. Pierdes el turno.");
            return;
        }

        movimiento movJugador = movs.get(eleccion - 1);
        System.out.println("\n" + jugador.getNombre() + " usa " + movJugador.getNombre() + "!");
        rival.recibirDaño((movJugador.getPotencia()+jugador.getNivel()/2));
    }

    // Turno del rival (si es un rival "entrenador")
    private static void turnoRival(PokemonLuchador jugador, PokemonLuchador rival) {
        List<movimiento> movs = rival.getMovimientos();
        movimiento movRival = movs.get(rand.nextInt(movs.size()));
        System.out.println("\n" + rival.getNombre() + " usa " + movRival.getNombre() + "!");
        jugador.recibirDaño(movRival.getPotencia());
    }

    // Turno del rival salvaje (usa siempre un movimiento aleatorio)
    private static void turnoRivalSalvaje(PokemonLuchador jugador, PokemonLuchador salvaje) {
        List<movimiento> movs = salvaje.getMovimientos();
        movimiento movRival = movs.get(rand.nextInt(movs.size()));
        System.out.println("\nEl salvaje " + salvaje.getNombre() + " usa " + movRival.getNombre() + "!");
        jugador.recibirDaño(movRival.getPotencia());
    }

    private static void pokemuerto(PokemonLuchador pokemonJugador, Jugador jugadorCompleto, Scanner sc, boolean combateActivo) {
        if (!pokemonJugador.estaVivo()) {
            // Comprobamos si nos queda algún Pokémon vivo en el equipo global
            if (equipoM(jugadorCompleto)) {
                System.out.println("\n¡Todos tus Pokémons han sido debilitados!");
            } else {
                // Si hay vivos, obligamos a cambiar
                System.out.println("\n" + pokemonJugador.getNombre() + " ha sido debilitado.");
                System.out.println("¡Debes sacar a otro Pokémon!");

                List<PokemonLuchador> equipo = jugadorCompleto.getEquipo();
                boolean cambioRealizado = false;

                while (!cambioRealizado) {
                    System.out.println("Elige un Pokémon vivo:");
                    for (int k = 0; k < equipo.size(); k++) {
                        PokemonLuchador p = equipo.get(k);
                        String estado = p.estaVivo() ? "Vivo" : "Debilitado";
                        System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
                    }

                    int idx = sc.nextInt() - 1;
                    if (idx >= 0 && idx < equipo.size()) {
                        PokemonLuchador candidato = equipo.get(idx);
                        if (candidato.estaVivo()) {
                            pokemonJugador = candidato; // Actualizamos el Pokémon activo en el combate
                            cambioRealizado = true;
                            System.out.println("¡Adelante " + pokemonJugador.getNombre() + "!");
                        } else {
                            System.out.println("Ese Pokémon está debilitado. Elige otro.");
                        }
                    } else {
                        System.out.println("Opción no válida.");
                    }
                }
            }
        }
    }

    private static void pokemuertoR(PokemonLuchador pokemonjugador, Gobierno gobierno , boolean combateActivo) {
        if (!pokemonjugador.estaVivo()) {
            // Comprobamos si nos queda algún Pokémon vivo en el equipo global
            if (equipoMG(gobierno)) {
                System.out.println("\n¡Todos sus Pokémons han sido debilitados!");
            } else {
                // Si hay vivos, obligamos a cambiar

                System.out.println("\n" + pokemonjugador.getNombre() + " ha sido debilitado.");

                List<PokemonLuchador> ministerio = gobierno.getministerio();
                List<PokemonLuchador> pokemonsVivos = new ArrayList<>();

                // Filtrar solo los Pokémon vivos
                for (PokemonLuchador p : ministerio) {
                    if (p.estaVivo()) {
                        pokemonsVivos.add(p);
                    }
                }

                // Seleccionar uno aleatorio de los vivos
                if (!pokemonsVivos.isEmpty()) {
                    Random rand = new Random();
                    PokemonLuchador nuevoPokemon = pokemonsVivos.get(rand.nextInt(pokemonsVivos.size()));

                    System.out.println("¡El rival envía a " + nuevoPokemon.getNombre() + "!");
                    // Aquí deberías actualizar la referencia del pokemonRival en el combate
                    // Esto dependerá de cómo gestiones el retorno/actualización
                }
            }
        }
    }

    public static boolean equipoM(Jugador jugadorCompleto){
        if (!jugadorCompleto.equipoVM()) {
            System.out.println("\n¡Todos tus Pokémons han sido debilitados!");
            return true;
        }else {
            return false;
        }
    }

    public static boolean equipoMR(Rival jugadorCompleto){
        if (!jugadorCompleto.equipoRVM()) {
            System.out.println("\n¡Todos tus Pokémons han sido debilitados!");
            return true;
        }else {
            return false;
        }
    }

    public static boolean equipoMG(Gobierno gobierno){
        if (!gobierno.ministerioVM()){
            System.out.println("\n¡Todos los Pokemons del "+gobierno+" han sido debilitados!");
            System.out.println("Podemita: ¡Noooooo! ¡Pedro Sanchez me quitará la paga!");
            return true;
        }else{
            return false;
        }
    }

    public static void efecto(PokemonLuchador pokemon){
        if (pokemon.getEstado() == EstadoAlterado.DORMIDO) {
            pokemon.setTurnosDormido(pokemon.getTurnosDormido() - 1);
            System.out.println(pokemon.getNombre() + " está dormido...");

            if (pokemon.getTurnosDormido() <= 0) {
                pokemon.setEstado(EstadoAlterado.NORMAL);
                System.out.println(pokemon.getNombre() + " se ha despertado.");
            }
            return; // pierde el turno
        }
        if (pokemon.getEstado() == EstadoAlterado.PARALIZADO) {
            Random r = new Random();
            if (r.nextInt(100) < 25) {
                System.out.println(pokemon.getNombre() + " está paralizado y no puede moverse.");
                return; // pierde el turno
            }
        }
        if (pokemon.getEstado() == EstadoAlterado.ENVENENADO) {
            int dano = pokemon.getVidaMax() / 18;
            pokemon.recibirDaño(dano);
            System.out.println(pokemon.getNombre() + " sufre daño por veneno.");
        }
        if (pokemon.getEstado() == EstadoAlterado.QUEMADO) {
            int dano = pokemon.getVidaMax() / 16;
            pokemon.recibirDaño(dano);
            System.out.println(pokemon.getNombre() + " sufre daño por quemadura.");
        }
        if (pokemon.getEstado() == EstadoAlterado.CONGELADO) {
            Random r = new Random();
            if (r.nextInt(100) < 25) {
                System.out.println(pokemon.getNombre() + " está congelado y no puede moverse.");
                return; // pierde el turno6
            }
        }
    }
}