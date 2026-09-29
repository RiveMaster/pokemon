package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gimnasio Pokémon: tiene un tipo, un líder con su equipo, una medalla y una
 * recompensa (dinero + una MT). El progreso del jugador (medallas) se guarda
 * en Jugador; los gimnasios en sí son datos fijos y no se serializan.
 *
 * Para añadir un gimnasio nuevo basta con una llamada a registrar(...) en
 * el bloque estático de abajo.
 */
public class Gimnasio {

    /** Pokémon del equipo del líder: nombre en la Pokédex y nivel. */
    private static class Miembro {
        final String pokemon;
        final int nivel;

        Miembro(String pokemon, int nivel) {
            this.pokemon = pokemon;
            this.nivel = nivel;
        }
    }

    private static final List<Gimnasio> GIMNASIOS = new ArrayList<>();

    static {
        registrar(new Gimnasio("Gimnasio Peñascal", "Roca", "Roque", "Medalla Roca", 800, "Avalancha",
                new Miembro("Geodude", 11), new Miembro("Graveler", 13), new Miembro("Onix", 15)));

        registrar(new Gimnasio("Gimnasio Marejada", "Agua", "Marina", "Medalla Cascada", 1200, "Cascada",
                new Miembro("Staryu", 16), new Miembro("Golduck", 18), new Miembro("Starmie", 21)));

        registrar(new Gimnasio("Gimnasio Voltio", "Eléctrico", "Volta", "Medalla Trueno", 1600, "Trueno",
                new Miembro("Pikachu", 20), new Miembro("Magneton", 22), new Miembro("Raichu", 24)));

        registrar(new Gimnasio("Gimnasio Frondoso", "Planta", "Flora", "Medalla Hoja", 2000, "Danza Petalo",
                new Miembro("Gloom", 25), new Miembro("Tangela", 26), new Miembro("Exeggutor", 29)));

        registrar(new Gimnasio("Gimnasio Ascua", "Fuego", "Cenizo", "Medalla Volcán", 2500, "Lanzallamas",
                new Miembro("Ninetales", 30), new Miembro("Rapidash", 32), new Miembro("Arcanine", 34)));
    }

    private static void registrar(Gimnasio g) {
        GIMNASIOS.add(g);
    }

    public static List<Gimnasio> getGimnasios() {
        return GIMNASIOS;
    }

    // ------------------------------------------------------------------

    private final String nombre;
    private final String tipo;
    private final String lider;
    private final String medalla;
    private final int recompensaDinero;
    private final String mtRecompensa;
    private final Miembro[] equipoLider;

    private Gimnasio(String nombre, String tipo, String lider, String medalla,
                     int recompensaDinero, String mtRecompensa, Miembro... equipoLider) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.lider = lider;
        this.medalla = medalla;
        this.recompensaDinero = recompensaDinero;
        this.mtRecompensa = mtRecompensa;
        this.equipoLider = equipoLider;
    }

    public String getNombre() { return nombre; }

    public String getTipo() { return tipo; }

    public String getLider() { return lider; }

    public String getMedalla() { return medalla; }

    public int getNivelRecomendado() {
        int max = 0;
        for (Miembro m : equipoLider) {
            max = Math.max(max, m.nivel);
        }
        return max;
    }

    /** Crea un equipo nuevo (con la vida completa) para cada combate. */
    private List<PokemonLuchador> crearEquipo() {
        List<PokemonLuchador> equipo = new ArrayList<>();
        for (Miembro m : equipoLider) {
            PokemonBase base = Pokedex.buscarPorNombre(m.pokemon);
            if (base == null) {
                System.out.println("(Aviso: no se encontró a " + m.pokemon + " en la Pokédex, se omite.)");
                continue;
            }
            // Constructor con stats fijos: suma el nivel a las estadísticas, así el líder resulta más duro
            equipo.add(new PokemonLuchador(base, m.nivel, 2));
        }
        return equipo;
    }

    // =====================================================
    // ================   MENÚ DE GIMNASIOS   ==============
    // =====================================================

    public static void menuGimnasios(Scanner sc, Jugador jugador) {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║           GIMNASIOS                ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("Medallas: " + jugador.getMedallas().size() + "/" + GIMNASIOS.size());

            for (int i = 0; i < GIMNASIOS.size(); i++) {
                Gimnasio g = GIMNASIOS.get(i);
                String estado = jugador.tieneMedalla(g.medalla) ? "✔ superado" : "pendiente";
                System.out.println((i + 1) + ". " + g.nombre + " [" + g.tipo + "] - Líder: " + g.lider
                        + " - Nv. recomendado: " + g.getNivelRecomendado() + " (" + estado + ")");
            }
            System.out.println("0. Salir");
            System.out.print("Elige un gimnasio: ");

            int opcion = Entrada.leerEntero(sc);
            if (opcion == 0) {
                return;
            }
            if (opcion < 1 || opcion > GIMNASIOS.size()) {
                System.out.println("Opción no válida.");
                continue;
            }
            GIMNASIOS.get(opcion - 1).desafiar(sc, jugador);
        }
    }

    // =====================================================
    // ================   COMBATE DE GIMNASIO   ============
    // =====================================================

    public void desafiar(Scanner sc, Jugador jugador) {
        System.out.println("\n=== " + nombre.toUpperCase() + " (tipo " + tipo + ") ===");

        if (!jugador.equipoVM()) {
            System.out.println("Todos tus Pokémon están debilitados. Ve al Centro Pokémon antes de desafiar a " + lider + ".");
            return;
        }

        boolean primeraVez = !jugador.tieneMedalla(medalla);
        if (primeraVez) {
            System.out.println(lider + ": ¡Bienvenido! Soy el líder de este gimnasio. ¡Demuestra lo que vales!");
        } else {
            System.out.println(lider + ": Ya tienes la " + medalla + ", pero no le diré que no a un combate de revancha.");
            System.out.println("(La revancha no da recompensas.)");
        }

        boolean victoria = combatir(sc, jugador);

        if (!victoria) {
            System.out.println("\n" + lider + ": ¡Vuelve cuando estés más preparado!");
            System.out.println("Puedes curar a tus Pokémon en el Centro Pokémon.");
            return;
        }

        System.out.println("\n¡Has derrotado a " + lider + "!");
        if (primeraVez) {
            jugador.añadirMedalla(medalla);
            jugador.agregarDinero(recompensaDinero);
            jugador.añadirMT(mtRecompensa);
            System.out.println(lider + ": Has ganado. Toma la " + medalla + ".");
            System.out.println("🏅 Has obtenido la " + medalla + ".");
            System.out.println("💰 Has ganado " + recompensaDinero + "€.");
            System.out.println("📀 Has recibido la MT " + mtRecompensa + " (úsala desde el menú).");
        }
    }

    /** Devuelve true si el jugador derrota a todo el equipo del líder. */
    private boolean combatir(Scanner sc, Jugador jugador) {
        List<PokemonLuchador> rivales = crearEquipo();
        if (rivales.isEmpty()) {
            System.out.println("El líder no tiene Pokémon disponibles, ¡victoria por incomparecencia!");
            return true;
        }

        int indiceRival = 0;
        PokemonLuchador rival = rivales.get(indiceRival);
        PokemonLuchador activo = primerVivo(jugador);

        System.out.println("\n" + lider + " envía a " + rival.getNombre() + " (Nv." + rival.getNivel() + ")!");
        System.out.println("¡Adelante " + activo.getNombre() + "!");

        while (true) {
            Combate.mostrarEstado(activo, rival);

            Combate.efecto(rival);
            Combate.efecto(activo);

            // El veneno o las quemaduras pueden haber debilitado a alguien antes de actuar
            if (!rival.estaVivo()) {
                indiceRival++;
                rival = siguienteRival(rivales, indiceRival, activo);
                if (rival == null) return true;
                continue;
            }
            if (!activo.estaVivo()) {
                activo = cambioForzado(sc, jugador, activo);
                if (activo == null) return false;
                continue;
            }

            System.out.println("1. Atacar");
            System.out.println("2. Cambiar Pokémon");
            System.out.println("3. Rendirse");
            System.out.print("Elige acción: ");
            int opcion = Entrada.leerEntero(sc);

            boolean rivalAtaca;

            if (opcion == 1) {
                Combate.turnoJugador(activo, rival, sc);
                rivalAtaca = true;

                if (!rival.estaVivo()) {
                    System.out.println("\n¡" + rival.getNombre() + " de " + lider + " ha sido debilitado!");
                    activo.ganarExp(rival.getNivel() * 10);
                    indiceRival++;
                    rival = siguienteRival(rivales, indiceRival, activo);
                    if (rival == null) return true;
                    rivalAtaca = false; // el nuevo Pokémon acaba de salir
                }
            } else if (opcion == 2) {
                PokemonLuchador nuevo = elegirCambio(sc, jugador, activo);
                if (nuevo == null) {
                    continue; // canceló: no gasta turno
                }
                activo = nuevo;
                System.out.println("¡Adelante " + activo.getNombre() + "!");
                rivalAtaca = true; // el rival aprovecha el cambio
            } else if (opcion == 3) {
                System.out.println("Te rindes ante " + lider + "...");
                return false;
            } else {
                System.out.println("Opción no válida.");
                continue;
            }

            if (rivalAtaca) {
                Combate.turnoRival(activo, rival);
            }

            if (!activo.estaVivo()) {
                activo = cambioForzado(sc, jugador, activo);
                if (activo == null) return false;
            }
        }
    }

    private PokemonLuchador siguienteRival(List<PokemonLuchador> rivales, int indice, PokemonLuchador activo) {
        if (indice >= rivales.size()) {
            return null;
        }
        PokemonLuchador siguiente = rivales.get(indice);
        System.out.println("\n" + lider + " envía a " + siguiente.getNombre() + " (Nv." + siguiente.getNivel() + ")!");
        return siguiente;
    }

    private PokemonLuchador primerVivo(Jugador jugador) {
        for (PokemonLuchador p : jugador.getEquipo()) {
            if (p.estaVivo()) {
                return p;
            }
        }
        return null;
    }

    /** Cambio voluntario. Devuelve null si el jugador cancela o la elección no vale. */
    private PokemonLuchador elegirCambio(Scanner sc, Jugador jugador, PokemonLuchador activo) {
        List<PokemonLuchador> equipo = jugador.getEquipo();
        System.out.println("Elige un Pokémon para enviar al combate (0 para cancelar):");
        for (int k = 0; k < equipo.size(); k++) {
            PokemonLuchador p = equipo.get(k);
            String estado = p.estaVivo() ? "Vivo" : "Debilitado";
            System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
        }
        int idx = Entrada.leerEntero(sc) - 1;
        if (idx < 0 || idx >= equipo.size()) {
            return null;
        }
        PokemonLuchador elegido = equipo.get(idx);
        if (!elegido.estaVivo()) {
            System.out.println("No puedes sacar a un Pokémon debilitado.");
            return null;
        }
        if (elegido == activo) {
            System.out.println("¡" + elegido.getNombre() + " ya está en combate!");
            return null;
        }
        return elegido;
    }

    /** Cambio obligatorio tras un KO. Devuelve null si no quedan Pokémon vivos. */
    private PokemonLuchador cambioForzado(Scanner sc, Jugador jugador, PokemonLuchador debilitado) {
        System.out.println("\n" + debilitado.getNombre() + " ha sido debilitado.");
        if (!jugador.equipoVM()) {
            System.out.println("¡Todos tus Pokémon han sido debilitados!");
            return null;
        }
        while (true) {
            System.out.println("¡Debes sacar a otro Pokémon!");
            PokemonLuchador nuevo = elegirCambio(sc, jugador, debilitado);
            if (nuevo != null) {
                System.out.println("¡Adelante " + nuevo.getNombre() + "!");
                return nuevo;
            }
        }
    }
}
