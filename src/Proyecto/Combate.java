package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Combate {

    private static Random rand = new Random();


    /*
    * Los combates se dividen en secciones distintas, los de evento como "combateInicial", los de pokemon salvaje, los
    * del gobierno, y contra los npcs
    * */
    public static void combateInicial(Jugador jugador, Rival rival, Scanner sc) {

        System.out.println("\n=== ¡COMIENZA EL COMBATE! ===\n");

        if (!jugador.equipoVM()) {
            System.out.println("No tienes ningún Pokémon disponible para combatir.");
            return;
        }
        if (!jugador.getPokemon().estaVivo()) {
            PokemonLuchador inicial = pokemuerto(jugador.getPokemon(), jugador, sc);
            if (inicial == null) return;
        }
        jugador.getEquipo().forEach(PokemonLuchador::iniciarCombate);
        rival.getPokemon().iniciarCombate();
        while (jugador.getPokemon().estaVivo() && rival.getPokemon().estaVivo()) {
            if (SpriteCollabUI.interfazDisponible()) {
                PokemonLuchador activo = jugador.getPokemon();
                PokemonLuchador enemigo = rival.getPokemon();
                mostrarEstado(activo, enemigo);
                efecto(enemigo);
                efecto(activo);
                if (!enemigo.estaVivo()) {
                    SpriteCollabUI.mostrarMensaje("¡" + enemigo.getNombre() + " ha sido debilitado!", activo, enemigo);
                    activo.ganarExp(enemigo.getNivel() * 10);
                    break;
                }
                if (!activo.estaVivo()) {
                    PokemonLuchador siguiente = pokemuerto(activo, jugador, sc);
                    if (siguiente == null) break;
                    continue;
                }
                SpriteCollabUI.elegirAccion(activo, enemigo, "Luchar");
                turnoJugador(activo, enemigo, sc);
                if (!enemigo.estaVivo()) {
                    SpriteCollabUI.mostrarMensaje("¡" + enemigo.getNombre() + " ha sido debilitado!", activo, enemigo);
                    activo.ganarExp(enemigo.getNivel() * 10);
                    break;
                }
                turnoRival(activo, enemigo);
                if (!activo.estaVivo()) {
                    PokemonLuchador siguiente = pokemuerto(activo, jugador, sc);
                    if (siguiente == null) break;
                }
                continue;
            }
            SpriteCollabUI.mostrarCombate(jugador.getPokemon(), rival.getPokemon());

            // Mostrar barras de vida
            System.out.println("\n===============================");
            System.out.println("Tu Pokémon: " + jugador.getPokemon().getNombre() + "  Nv." + jugador.getPokemon().getNivel());
            System.out.println("Vida: " + jugador.getPokemon().getVidaActual() + "/" + jugador.getPokemon().getVidaMax());
            System.out.println("-------------------------------");
            System.out.println("Rival: " + rival.getPokemon().getNombre() + "  Nv." + rival.getPokemon().getNivel());
            System.out.println("Vida: " + rival.getPokemon().getVidaActual() + "/" + rival.getPokemon().getVidaMax());
            System.out.println("===============================\n");

            System.out.println("1. Atacar");
            System.out.print("Elige acción: ");

            int opcion = sc.nextInt();

            if (opcion == 1) {

                List<Movimiento> movs = jugador.getPokemon().getMovimientos();

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

                    Movimiento movJugador = movs.get(eleccion - 1);

                    System.out.println("\n" + jugador.getNombre() + " usa " + movJugador.getNombre() + "!");

                    SpriteCollabUI.animarAtaque(jugador.getPokemon());
                    rival.getPokemon().recibirDaño(movJugador.getPotencia());
                    if (rival.getPokemon().estaVivo()) SpriteCollabUI.animarDaño(rival.getPokemon());
                    else SpriteCollabUI.animarDerrota(rival.getPokemon());

                    if (!rival.getPokemon().estaVivo()) {
                        System.out.println("\n" + rival.getNombre() + " ha sido debilitado!");
                        jugador.getPokemon().ganarExp(rival.getPokemon().getNivel()*10);
                        break;
                    }
                }

                // Turno rival
                Movimiento movRival = rival.getPokemon().getMovimientos().get(0);
                System.out.println("\nEl rival usa " + movRival.getNombre() + "!");
                SpriteCollabUI.animarAtaque(rival.getPokemon());
                jugador.getPokemon().recibirDaño(movRival.getPotencia());
                if (jugador.getPokemon().estaVivo()) SpriteCollabUI.animarDaño(jugador.getPokemon());
                else SpriteCollabUI.animarDerrota(jugador.getPokemon());

                if (!jugador.getPokemon().estaVivo()) {
                    PokemonLuchador siguiente = pokemuerto(jugador.getPokemon(), jugador, sc);
                    if (siguiente == null) break;
                }

            }
        }
    }


    // Combate contra un rival u otro jugador
    public static void combateRival(Jugador Jugar, Rival tontito,PokemonLuchador jugador, PokemonLuchador rival, Scanner sc) {
        System.out.println("\n=== ¡COMIENZA EL COMBATE CONTRA " + rival.getNombre() + "! ===");

        MegaEvoluciones.iniciarCombate();
        jugador.iniciarCombate();
        rival.iniciarCombate();
        boolean combateActivo = !equipoMR(tontito)&&!equipoM(Jugar);
        while (combateActivo) {
            mostrarEstado(jugador, rival);

            efecto(rival);
            efecto(jugador);

            if (!jugador.estaVivo()) {
                PokemonLuchador siguiente = pokemuerto(jugador, Jugar, sc);
                if (siguiente == null) break;
                jugador = siguiente;
                continue;
            }

            int opcion;
            if (SpriteCollabUI.interfazDisponible()) {
                opcion = SpriteCollabUI.elegirAccion(jugador, rival, "Luchar") + 1;
            } else {
                System.out.println("1. Atacar");
                System.out.print("Elige acción: ");
                opcion = sc.nextInt();
            }

            if (opcion == 1) {
                turnoJugador(jugador, rival, sc);

                if (!rival.estaVivo()) {
                    SpriteCollabUI.mostrarMensaje("¡" + rival.getNombre() + " ha sido debilitado!", jugador, rival);
                    System.out.println("\n¡Has ganado el combate contra " + rival.getNombre() + "!");
                    jugador.ganarExp(200); // EXP por rival
                    break;
                }

                turnoRival(jugador, rival);

                if (!jugador.estaVivo()) {
                    PokemonLuchador siguiente = pokemuerto(jugador, Jugar, sc);
                    if (siguiente == null) break;
                    jugador = siguiente;
                }

            } else {
                System.out.println("Opción inválida, pierdes el turno.");
            }
        }
        jugador.desmegaevolucionar();
        rival.desmegaevolucionar();
        MegaEvoluciones.terminarCombate();
    }

    public static void combatepodemita(Jugador jugadorCompleto, PokemonLuchador pokemonJugador, PokemonLuchador pokemonpodemita, Gobierno gobierno, Scanner sc){
        System.out.println("\n=== ¡Un Podemita ha aparecido y quiere pelear contra tí! ===");
        System.out.println("Podemita: ¡Ganare por Pedro Sanchez, viva la izquierda!");
        MegaEvoluciones.iniciarCombate();
        finalizarMegaJugador(jugadorCompleto);
        jugadorCompleto.getEquipo().forEach(PokemonLuchador::iniciarCombate);
        jugadorCompleto.getPc().forEach(PokemonLuchador::iniciarCombate);
        pokemonJugador.iniciarCombate();
        pokemonpodemita.iniciarCombate();
        boolean combateActivo = !equipoMG(gobierno)&&!equipoM(jugadorCompleto);
        while (combateActivo){
            mostrarEstado(pokemonJugador, pokemonpodemita);

            efecto(pokemonpodemita);
            efecto(pokemonJugador);

            int opcion;
            if (SpriteCollabUI.interfazDisponible()) {
                opcion = SpriteCollabUI.elegirAccion(pokemonJugador, pokemonpodemita,
                        "Luchar", "Huir", "Mochila") + 1;
            } else {
                System.out.println("1. Atacar");
                System.out.println("2. Huir");
                System.out.println("3. Mochila");
                System.out.print("Elige acción: ");
                opcion = sc.nextInt();
                sc.nextLine();
            }

            if (opcion == 1){
                turnoJugador(pokemonJugador, pokemonpodemita, sc);
                if (!pokemonpodemita.estaVivo()){
                    SpriteCollabUI.mostrarMensaje("¡" + pokemonpodemita.getNombre() + " ha sido debilitado!", pokemonJugador, pokemonpodemita);
                    System.out.println("\n¡"+pokemonpodemita+" ha sido debilitado!");
                    pokemonJugador.ganarExp(pokemonJugador.getNivel()*10);
                    pokemuertoR(pokemonpodemita, gobierno, combateActivo);
                    // hacer en futuro importante
                }
                turnoRival(pokemonJugador, pokemonpodemita);
                if (!pokemonJugador.estaVivo()){
                    SpriteCollabUI.mostrarMensaje("¡" + pokemonJugador.getNombre() + " ha sido debilitado!", pokemonJugador, pokemonpodemita);
                    {
                    PokemonLuchador siguiente = pokemuerto(pokemonJugador, jugadorCompleto, sc);
                    if (siguiente == null) {
                        combateActivo = false;
                    } else {
                        pokemonJugador = siguiente;
                    }
                }
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
        finalizarMegaJugador(jugadorCompleto);
        pokemonJugador.desmegaevolucionar();
        pokemonpodemita.desmegaevolucionar();
        MegaEvoluciones.terminarCombate();
    }

    // Combate contra Pokémon salvaje
    public static void combateSalvaje(Jugador jugadorCompleto, PokemonLuchador pokemonJugador, PokemonLuchador salvaje, Scanner sc) {
        System.out.println("\n=== ¡Un Pokémon salvaje aparece! ===");

        MegaEvoluciones.iniciarCombate();
        finalizarMegaJugador(jugadorCompleto);
        jugadorCompleto.getEquipo().forEach(PokemonLuchador::iniciarCombate);
        jugadorCompleto.getPc().forEach(PokemonLuchador::iniciarCombate);
        pokemonJugador.iniciarCombate();
        salvaje.iniciarCombate();
        boolean combateActivo = salvaje.estaVivo()&&!equipoM(jugadorCompleto);

        while (combateActivo) {
            mostrarEstado(pokemonJugador, salvaje);

            efecto(salvaje);
            efecto(pokemonJugador);

            if (!pokemonJugador.estaVivo()) {
                PokemonLuchador siguiente = pokemuerto(pokemonJugador, jugadorCompleto, sc);
                if (siguiente == null) {
                    combateActivo = false;
                    break;
                }
                pokemonJugador = siguiente;
                continue;
            }

            int opcion;
            if (SpriteCollabUI.interfazDisponible()) {
                opcion = SpriteCollabUI.elegirAccion(pokemonJugador, salvaje,
                        "Luchar", "Huir", "Mochila") + 1;
            } else {
                System.out.println("1. Atacar");
                System.out.println("2. Huir");
                System.out.println("3. Mochila");
                System.out.print("Elige acción: ");
                opcion = sc.nextInt();
                sc.nextLine();
            }

            if (opcion == 1) {
                turnoJugador(pokemonJugador, salvaje, sc);


                if (!salvaje.estaVivo()) {
                    SpriteCollabUI.mostrarMensaje("¡El Pokémon salvaje ha sido debilitado!", pokemonJugador, salvaje);
                    System.out.println("\n¡El Pokémon salvaje ha sido debilitado!");
                    pokemonJugador.ganarExp(300); // EXP por salvaje
                    break;
                }

                turnoRivalSalvaje(pokemonJugador, salvaje);

                if (!pokemonJugador.estaVivo()) {
                    {
                    PokemonLuchador siguiente = pokemuerto(pokemonJugador, jugadorCompleto, sc);
                    if (siguiente == null) {
                        combateActivo = false;
                    } else {
                        pokemonJugador = siguiente;
                    }
                }
                }

            } else if (opcion == 2) {
                System.out.println("Escapas del combate...");
                combateActivo = false;
            }else if (opcion == 3){
                int n;
                if (SpriteCollabUI.interfazDisponible()) {
                    n = SpriteCollabUI.elegirAccion(pokemonJugador, salvaje,
                            "Capturar", "Usar objeto", "Cambiar Pokémon") + 1;
                } else {
                    System.out.println("1. Capturar");
                    System.out.println("2. Usar pocion");
                    System.out.println("3. Cambiar pokemon");
                    System.out.print("Elige acción: ");
                    n = sc.nextInt();
                    sc.nextLine();
                }
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
                            {
                    PokemonLuchador siguiente = pokemuerto(pokemonJugador, jugadorCompleto, sc);
                    if (siguiente == null) {
                        combateActivo = false;
                    } else {
                        pokemonJugador = siguiente;
                    }
                }
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
                    List<PokemonLuchador> equipo = jugadorCompleto.getEquipo();
                    int idx;
                    if (SpriteCollabUI.interfazDisponible()) {
                        idx = SpriteCollabUI.elegirPokemon(equipo, pokemonJugador, true);
                    } else {
                        System.out.println("Elige un Pokémon para enviar al combate:");
                        for (int k = 0; k < equipo.size(); k++) {
                            PokemonLuchador p = equipo.get(k);
                            String estado = p.estaVivo() ? "Vivo" : "Debilitado";
                            System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
                        }
                        idx = sc.nextInt() - 1;
                    }
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
                                {
                    PokemonLuchador siguiente = pokemuerto(pokemonJugador, jugadorCompleto, sc);
                    if (siguiente == null) {
                        combateActivo = false;
                    } else {
                        pokemonJugador = siguiente;
                    }
                }
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
        finalizarMegaJugador(jugadorCompleto);
        pokemonJugador.desmegaevolucionar();
        salvaje.desmegaevolucionar();
        MegaEvoluciones.terminarCombate();
    }

    private static void finalizarMegaJugador(Jugador jugador) {
        if (jugador == null) return;
        for (PokemonLuchador p : jugador.getEquipo()) p.desmegaevolucionar();
        for (PokemonLuchador p : jugador.getPc()) p.desmegaevolucionar();
    }

    // Mostrar estado de los Pokémon
    static void mostrarEstado(PokemonLuchador jugador, PokemonLuchador rival) {
        // Ventana gráfica opcional; el flujo de combate de consola permanece intacto.
        SpriteCollabUI.refrescarCombate(jugador, rival);
        System.out.println("\n===============================");
        System.out.println("Tu Pokémon: " + jugador.getNombre() + "  Nv." + jugador.getNivel());
        System.out.println("Vida: " + jugador.getVidaActual() + "/" + jugador.getVidaMax());
        System.out.println("-------------------------------");
        System.out.println("Rival: " + rival.getNombre() + "  Nv." + rival.getNivel());
        System.out.println("Vida: " + rival.getVidaActual() + "/" + rival.getVidaMax());
        System.out.println("===============================");
    }

    // Información extra de un movimiento en el menú (solo con las reglas de PP / precisión activadas)
    private static String detalleMovimiento(PokemonLuchador p, Movimiento m) {
        StringBuilder sb = new StringBuilder();
        if (ReglasCombate.pp() && m.getPp() > 0) sb.append("  PP ").append(p.ppRestantes(m)).append('/').append(m.getPp());
        if (ReglasCombate.precision() && m.getPrecision() > 0 && m.getPrecision() < 100)
            sb.append("  Prec. ").append(m.getPrecision()).append('%');
        return sb.toString();
    }

    // Turno del jugador
    public static void turnoJugador(PokemonLuchador jugador, PokemonLuchador rival, Scanner sc) {
        List<Movimiento> movs = jugador.getMovimientos();
        boolean puedeMega = MegaEvoluciones.puedeOfrecerse(jugador);
        boolean puedeGigamax = Dinamax.puedeActivarse(jugador);

        if (movs.isEmpty() && !puedeMega && !puedeGigamax) {
            System.out.println("¡Tu Pokémon no tiene movimientos!");
            return;
        }

        if (jugador.sinPp() && !puedeMega && !puedeGigamax) {
            System.out.println("¡A " + jugador.getNombre() + " no le quedan PP!");
            System.out.println("\n" + jugador.getNombre() + " usa " + Movimiento.FORCEJEO.getNombre() + "!");
            jugador.usarMovimiento(Movimiento.FORCEJEO, Movimiento.FORCEJEO.getPotencia() + jugador.getNivel() / 2, rival);
            return;
        }

        if (jugador.sinPp()) {
            if (SpriteCollabUI.interfazDisponible()) {
                List<String> opciones = new ArrayList<>();
                if (puedeMega) opciones.add("Mega-evolucionar");
                if (puedeGigamax) opciones.add("Activar Gigamax");
                opciones.add("Forcejeo");
                int accion = SpriteCollabUI.elegirAccion(jugador, rival, opciones.toArray(new String[0]));
                int indiceForcejeo = opciones.indexOf("Forcejeo");
                if (accion == opciones.indexOf("Mega-evolucionar") && puedeMega) {
                    MegaEvoluciones.intentarDuranteCombate(jugador, sc);
                } else if (accion == opciones.indexOf("Activar Gigamax") && puedeGigamax) {
                    activarGigamax(jugador);
                } else if (accion == indiceForcejeo) {
                    SpriteCollabUI.mostrarMensaje(jugador.getNombre() + " usa Forcejeo!", jugador, rival);
                    jugador.usarMovimiento(Movimiento.FORCEJEO, Movimiento.FORCEJEO.getPotencia()
                            + jugador.getNivel() / 2, rival);
                }
                SpriteCollabUI.refrescarCombate(jugador, rival);
            } else {
                System.out.println("\n¡A " + jugador.getNombre() + " no le quedan PP!");
                System.out.println("0. Mega-evolucionar (consume el turno)");
                if (puedeGigamax) System.out.println("-1. Activar Gigamax (3 turnos)");
                System.out.println("99. Usar Forcejeo");
                System.out.print("Elige acción: ");
                int accion = Entrada.leerEntero(sc);
                if (accion == 0) MegaEvoluciones.intentarDuranteCombate(jugador, sc);
                else if (accion == -1 && puedeGigamax) { activarGigamax(jugador); }
                else if (accion == 99) {
                    System.out.println("\n" + jugador.getNombre() + " usa " + Movimiento.FORCEJEO.getNombre() + "!");
                    jugador.usarMovimiento(Movimiento.FORCEJEO, Movimiento.FORCEJEO.getPotencia() + jugador.getNivel() / 2, rival);
                } else {
                    System.out.println("Acción no válida; pierdes el turno.");
                }
            }
            return;
        }

        if (SpriteCollabUI.interfazDisponible()) {
            int seleccion = SpriteCollabUI.elegirMovimiento(jugador, rival, movs, puedeMega, puedeGigamax);
            if (seleccion < 0) return;
            if (seleccion == movs.size() && puedeMega) {
                MegaEvoluciones.intentarDuranteCombate(jugador, sc);
                return;
            }
            if (seleccion == movs.size() + (puedeMega ? 1 : 0) && puedeGigamax) {
                activarGigamax(jugador);
                SpriteCollabUI.refrescarCombate(jugador, rival);
                return;
            }
            if (seleccion >= movs.size()) return;
            Movimiento elegido = movs.get(seleccion);
            if (!jugador.tienePp(elegido)) return;
            SpriteCollabUI.mostrarMensaje(jugador.getNombre() + " usa " + elegido.getNombre() + "!", jugador, rival);
            SpriteCollabUI.animarAtaque(jugador);
            int vidaAntes = rival.getVidaActual();
            jugador.usarMovimiento(elegido, elegido.getPotencia() + jugador.getNivel() / 2, rival);
            if (rival.getVidaActual() < vidaAntes) {
                if (rival.estaVivo()) SpriteCollabUI.animarDaño(rival);
                else SpriteCollabUI.animarDerrota(rival);
            }
            SpriteCollabUI.refrescarCombate(jugador, rival);
            return;
        }

        if (!movs.isEmpty()) {
            System.out.println("\nElige un movimiento:");
            for (int i = 0; i < movs.size(); i++) {
                System.out.println((i + 1) + ". " + movs.get(i).getNombre() + " (Potencia "
                        + (movs.get(i).getPotencia() + jugador.getNivel() / 2) + ")"
                        + detalleMovimiento(jugador, movs.get(i)));
            }
        }
        if (puedeMega) System.out.println("0. Mega-evolucionar (consume el turno)");
        if (puedeGigamax) System.out.println("-1. Gigamax (3 turnos, consume el turno)");
        if (jugador.estaEnGigamax()) System.out.println("Gigamax activo: " + jugador.getTurnosGigamax() + " turnos restantes.");
        System.out.print("Elige acción: ");

        while (true) {
            int eleccion = Entrada.leerEntero(sc);
            if (eleccion == -1 && puedeGigamax) { activarGigamax(jugador); return; }
            if (eleccion == 0 && puedeMega) {
                MegaEvoluciones.intentarDuranteCombate(jugador, sc);
                return;
            }
            if (eleccion < 1 || eleccion > movs.size()) {
                System.out.println("Movimiento inválido. Pierdes el turno.");
                return;
            }

            Movimiento movJugador = movs.get(eleccion - 1);
            if (!jugador.tienePp(movJugador)) {
                System.out.println("¡No quedan PP para " + movJugador.getNombre() + "! Elige otro movimiento.");
                continue;
            }
            System.out.println("\n" + jugador.getNombre() + " usa " + movJugador.getNombre() + "!");
            SpriteCollabUI.animarAtaque(jugador);
            int vidaAntes = rival.getVidaActual();
            jugador.usarMovimiento(movJugador, movJugador.getPotencia() + jugador.getNivel() / 2, rival);
            if (rival.getVidaActual() < vidaAntes) {
                if (rival.estaVivo()) SpriteCollabUI.animarDaño(rival);
                else SpriteCollabUI.animarDerrota(rival);
            }
            return;
        }
    }

    private static void activarGigamax(PokemonLuchador pokemon) {
        if (Dinamax.activar(pokemon)) {
            System.out.println("¡" + pokemon.getNombre() + " se ha vuelto Gigamax! (3 turnos)");
        } else {
            System.out.println("No se puede activar Gigamax. Comprueba la Banda Dinamax y la especie.");
        }
    }

    // Elige el movimiento de un Pokémon controlado por el juego (al azar; con PP solo entre los que tienen)
    private static Movimiento elegirMovimientoRival(PokemonLuchador p) {
        List<Movimiento> movs = p.getMovimientos();
        if (!ReglasCombate.pp()) return movs.get(rand.nextInt(movs.size()));
        List<Movimiento> utiles = new java.util.ArrayList<>();
        for (Movimiento m : movs) if (p.tienePp(m)) utiles.add(m);
        if (utiles.isEmpty()) return Movimiento.FORCEJEO;
        return utiles.get(rand.nextInt(utiles.size()));
    }

    // Turno del rival (si es un rival "entrenador")
    public static void turnoRival(PokemonLuchador jugador, PokemonLuchador rival) {
        Movimiento movRival = elegirMovimientoRival(rival);
        SpriteCollabUI.mostrarMensaje(rival.getNombre() + " usa " + movRival.getNombre() + "!", jugador, rival);
        System.out.println("\n" + rival.getNombre() + " usa " + movRival.getNombre() + "!");
        SpriteCollabUI.animarAtaque(rival);
        int vidaAntes = jugador.getVidaActual();
        rival.usarMovimiento(movRival, movRival.getPotencia(), jugador);
        if (jugador.getVidaActual() < vidaAntes) {
            if (jugador.estaVivo()) SpriteCollabUI.animarDaño(jugador);
            else SpriteCollabUI.animarDerrota(jugador);
        }
        SpriteCollabUI.refrescarCombate(jugador, rival);
        avanzarDuracionGigamax(jugador, rival);
    }

    // Turno del rival salvaje (usa siempre un movimiento aleatorio)
    private static void turnoRivalSalvaje(PokemonLuchador jugador, PokemonLuchador salvaje) {
        Movimiento movRival = elegirMovimientoRival(salvaje);
        SpriteCollabUI.mostrarMensaje("El salvaje " + salvaje.getNombre() + " usa " + movRival.getNombre() + "!", jugador, salvaje);
        System.out.println("\nEl salvaje " + salvaje.getNombre() + " usa " + movRival.getNombre() + "!");
        SpriteCollabUI.animarAtaque(salvaje);
        int vidaAntes = jugador.getVidaActual();
        salvaje.usarMovimiento(movRival, movRival.getPotencia(), jugador);
        if (jugador.getVidaActual() < vidaAntes) {
            if (jugador.estaVivo()) SpriteCollabUI.animarDaño(jugador);
            else SpriteCollabUI.animarDerrota(jugador);
        }
        SpriteCollabUI.refrescarCombate(jugador, salvaje);
        avanzarDuracionGigamax(jugador, salvaje);
    }

    /** Consume un turno Gigamax al completar la respuesta rival de cada ronda. */
    private static void avanzarDuracionGigamax(PokemonLuchador primero, PokemonLuchador segundo) {
        if (primero != null) primero.avanzarTurnoGigamax();
        if (segundo != null && segundo != primero) segundo.avanzarTurnoGigamax();
    }

    // Funcion para cuando uno de tus pokemon se debilita

    // Cuando cae el Pokémon del jugador. Devuelve el que sigue en combate:
    // el mismo si sigue vivo, el elegido si hay que sustituirlo, o null si ya no queda ninguno.
    // (Antes era void: el cambio se hacía en una copia local y el combate seguía con el debilitado.)
    private static PokemonLuchador pokemuerto(PokemonLuchador pokemonJugador, Jugador jugadorCompleto, Scanner sc) {
        if (pokemonJugador.estaVivo()) {
            return pokemonJugador;
        }

        // Ojo: equipoM devuelve true cuando TODO el equipo está debilitado
        if (equipoM(jugadorCompleto)) {
            SpriteCollabUI.mostrarMensaje("¡Todos tus Pokémon han sido debilitados!", pokemonJugador, pokemonJugador);
            System.out.println("\n¡Todos tus Pokémons han sido debilitados!");
            return null;
        }

        SpriteCollabUI.mostrarMensaje("¡" + pokemonJugador.getNombre() + " ha sido debilitado! Elige otro Pokémon.",
                pokemonJugador, primerPokemonVivo(jugadorCompleto));
        System.out.println("\n" + pokemonJugador.getNombre() + " ha sido debilitado.");
        System.out.println("¡Debes sacar a otro Pokémon!");

        List<PokemonLuchador> equipo = jugadorCompleto.getEquipo();
        while (true) {
            int idx;
            if (SpriteCollabUI.interfazDisponible()) {
                idx = SpriteCollabUI.elegirPokemon(equipo, pokemonJugador, false);
            } else {
                System.out.println("Elige un Pokémon vivo:");
                for (int k = 0; k < equipo.size(); k++) {
                    PokemonLuchador p = equipo.get(k);
                    String estado = p.estaVivo() ? "Vivo" : "Debilitado";
                    System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
                }
                idx = Entrada.leerEntero(sc) - 1;
            }
            if (idx >= 0 && idx < equipo.size()) {
                PokemonLuchador candidato = equipo.get(idx);
                if (candidato.estaVivo()) {
                    if (idx > 0) Collections.swap(equipo, 0, idx);
                    System.out.println("¡Adelante " + candidato.getNombre() + "!");
                    return candidato;
                }
                System.out.println("Ese Pokémon está debilitado. Elige otro.");
            } else {
                System.out.println("Opción no válida.");
            }
        }
    }

    private static PokemonLuchador primerPokemonVivo(Jugador jugador) {
        for (PokemonLuchador p : jugador.getEquipo()) if (p.estaVivo()) return p;
        return jugador.getPokemon();
    }

    // Funcion para que cuando a un rival se le muera un pokemon que saque a otro

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

    // Funcion para saber si todos los pokemons del jugador estan debilitados
    public static boolean equipoM(Jugador jugadorCompleto){
        if (!jugadorCompleto.equipoVM()) {
            System.out.println("\n¡Todos tus Pokémons han sido debilitados!");
            return true;
        }else {
            return false;
        }
    }

     // Funcion para saber si todos los pokemons del rival estan debilitados
    public static boolean equipoMR(Rival jugadorCompleto){
        if (!jugadorCompleto.equipoRVM()) {
            System.out.println("\n¡Todos sus Pokémons han sido debilitados!");
            return true;
        }else {
            return false;
        }
    }

    // Funcion para saber si todos los pokemons del gobierno estan debilitados
    public static boolean equipoMG(Gobierno gobierno){
        if (!gobierno.ministerioVM()){
            System.out.println("\n¡Todos los Pokemons del "+gobierno+" han sido debilitados!");
            System.out.println("Podemita: ¡Noooooo! ¡Pedro Sanchez me quitará la paga!");
            return true;
        }else{
            return false;
        }
    }

    // Funcion para los efectos de los pokemons que esten luchando en la batalla
    public static void efecto(PokemonLuchador pokemon){
        pokemon.efectoObjetoInicioTurno();
        pokemon.efectoHabilidadInicioTurno();
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