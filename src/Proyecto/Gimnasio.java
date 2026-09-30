package Proyecto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Gimnasio Pokémon. Cada uno está en UNA ciudad distinta (se comprueba al registrarlo; puede haber
 * ciudades sin gimnasio) y tiene un tipo. Para conseguir la medalla hay que superar a sus 3 miembros
 * en orden y después al líder. El progreso con los miembros se guarda, así que se puede salir del
 * gimnasio, curar el equipo y volver sin repetir combates.
 *
 * El gimnasio es el mismo en las 10 generaciones, pero sus equipos cambian: se forman con Pokémon de
 * su tipo de la generación en la que está el jugador, y los niveles suben con la generación.
 *
 * Líderes y miembros son personajes FICTICIOS inspirados en el mundo del streaming/YouTube.
 * Para cambiar nombres o títulos basta con tocar las llamadas a registrar(...) de abajo.
 */
public class Gimnasio {

    private static final int NUM_MIEMBROS = 3;
    private static final int POKEMON_POR_MIEMBRO = 2;

    private static final List<Gimnasio> GIMNASIOS = new ArrayList<>();

    static {
        registrar(new Gimnasio("Gimnasio Peñascal", Ubicacion.OVIEDO, "Roca", Generacion.tipos("Rock"),
                "Roque Unboxing", "el youtuber de unboxings",
                new String[]{"Pedrusco el Editor", "Guija la Moderadora", "Canto el Community Manager"},
                "Medalla Roca", 800, "Avalancha", 11, 3));

        registrar(new Gimnasio("Gimnasio Marejada", Ubicacion.GIJON, "Agua", Generacion.tipos("Water"),
                "Marina Directo", "la streamer de directos eternos",
                new String[]{"Gota la Guionista", "Ola el Cámara", "Coral la Diseñadora de Miniaturas"},
                "Medalla Cascada", 1200, "Cascada", 16, 3));

        registrar(new Gimnasio("Gimnasio Voltio", Ubicacion.SANTANDER, "Eléctrico", Generacion.tipos("Electric"),
                "Volta Speedrun", "el speedrunner más rápido",
                new String[]{"Chispa la Técnica de Sonido", "Amper el Streamer Novato", "Watio el Editor de Directos"},
                "Medalla Trueno", 1600, "Trueno", 20, 3));

        registrar(new Gimnasio("Gimnasio Frondoso", Ubicacion.BILBAO, "Planta", Generacion.tipos("Grass"),
                "Flora Tutoriales", "la youtuber de manualidades y plantas",
                new String[]{"Hojarasca la Jardinera", "Brote el Tutorialero", "Musgo la Reseñista"},
                "Medalla Hoja", 2000, "Danza Petalo", 25, 3));

        registrar(new Gimnasio("Gimnasio Ascua", Ubicacion.VITORIA, "Fuego", Generacion.tipos("Fire"),
                "Cenizo Reto", "el rey del reto picante",
                new String[]{"Brasa la Retadora", "Ceniza el Probador", "Chile el Catador de Picantes"},
                "Medalla Volcán", 2500, "Lanzallamas", 30, 4));
    }

    private static void registrar(Gimnasio g) {
        for (Gimnasio otro : GIMNASIOS) {
            if (otro.ciudad == g.ciudad) {
                throw new IllegalStateException("Ya hay un gimnasio en " + g.ciudad.getNombre()
                        + " (" + otro.nombre + "): cada gimnasio debe estar en una ciudad distinta.");
            }
        }
        if (g.miembros.length != NUM_MIEMBROS) {
            throw new IllegalStateException(g.nombre + " debe tener exactamente " + NUM_MIEMBROS + " miembros.");
        }
        GIMNASIOS.add(g);
    }

    public static List<Gimnasio> getGimnasios() {
        return GIMNASIOS;
    }

    /** El gimnasio de esa ciudad, o null si en ella no hay ninguno. */
    public static Gimnasio deCiudad(Ubicacion ciudad) {
        for (Gimnasio g : GIMNASIOS) {
            if (g.ciudad == ciudad) return g;
        }
        return null;
    }

    // ------------------------------------------------------------------

    private final String nombre;
    private final Ubicacion ciudad;
    private final String tipo;
    private final Set<String> tiposInternos;
    private final String lider;
    private final String tituloLider;
    private final String[] miembros;
    private final String medalla;
    private final int recompensaDinero;
    private final String mtRecompensa;
    private final int nivelBase;
    private final int tamanoEquipo;

    private Gimnasio(String nombre, Ubicacion ciudad, String tipo, Set<String> tiposInternos, String lider,
                     String tituloLider, String[] miembros, String medalla, int recompensaDinero,
                     String mtRecompensa, int nivelBase, int tamanoEquipo) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.tiposInternos = tiposInternos;
        this.lider = lider;
        this.tituloLider = tituloLider;
        this.miembros = miembros;
        this.medalla = medalla;
        this.recompensaDinero = recompensaDinero;
        this.mtRecompensa = mtRecompensa;
        this.nivelBase = nivelBase;
        this.tamanoEquipo = tamanoEquipo;
    }

    public String getNombre() { return nombre; }

    public Ubicacion getCiudad() { return ciudad; }

    public String getTipo() { return tipo; }

    public Set<String> getTiposInternos() { return tiposInternos; }

    public String getLider() { return lider; }

    public String[] getMiembros() { return miembros; }

    public String getMedalla() { return medalla; }

    /** La medalla se guarda con la región, así cada generación tiene la suya. */
    public String claveMedalla(Generacion gen) {
        return medalla + " (" + gen.getRegion() + ")";
    }

    /** Clave con la que se guarda que ya se venció a un miembro en esa generación. */
    public String claveMiembro(Generacion gen, int indice) {
        return "Miembro:" + claveMedalla(gen) + ":" + indice;
    }

    public int getNivelRecomendado(Generacion gen) {
        return nivelBase + 2 * (tamanoEquipo - 1) + gen.getBonusNivel();
    }

    /** Crea el equipo del líder para esa generación, con la vida completa. */
    private List<PokemonLuchador> crearEquipo(Generacion gen) {
        int nivelMax = getNivelRecomendado(gen);
        List<PokemonBase> bases = gen.equipoPara(tiposInternos, nivelMax, tamanoEquipo);

        List<PokemonLuchador> equipo = new ArrayList<>();
        for (int i = 0; i < bases.size(); i++) {
            int nivel = nivelBase + gen.getBonusNivel() + 2 * i;
            // Constructor con stats fijos: suma el nivel a las estadísticas, así el líder resulta más duro
            equipo.add(new PokemonLuchador(bases.get(i), nivel, 2));
        }
        return equipo;
    }

    /**
     * Equipo de un miembro: 2 Pokémon del MISMO tipo del gimnasio, más flojos que los del líder
     * (van a menor nivel). Se eligen entre los Pokémon de ese tipo que el líder no usa, de menos a más
     * poder, repartidos entre los 3 miembros. El grupo de candidatos es algo más amplio que el del líder
     * porque los miembros llevan menos nivel. Si aun así hay pocos, se repiten entre miembros antes
     * que meter Pokémon de otro tipo.
     */
    private List<PokemonLuchador> crearEquipoMiembro(Generacion gen, int indice) {
        int nivelMiembro = nivelBase + gen.getBonusNivel() - 3 + indice; // p. ej. gimnasio 1 en gen 1: niveles 8, 9 y 10

        List<PokemonBase> delTipo = gen.candidatos(tiposInternos, getNivelRecomendado(gen) + 8);
        List<String> delLider = new ArrayList<>();
        for (PokemonBase b : gen.equipoPara(tiposInternos, getNivelRecomendado(gen), tamanoEquipo)) {
            delLider.add(b.getNombre());
        }

        List<PokemonBase> resto = new ArrayList<>();
        for (PokemonBase b : delTipo) {
            if (!delLider.contains(b.getNombre())) resto.add(b);
        }
        resto.sort(Comparator.comparingInt(Generacion::poder)); // de menos a más fuerte

        // Si sobran pocos, se completan con especies que también usa el líder (los miembros van a menor nivel)
        if (resto.size() < NUM_MIEMBROS * POKEMON_POR_MIEMBRO) {
            List<PokemonBase> comoLider = new ArrayList<>();
            for (PokemonBase b : delTipo) {
                if (delLider.contains(b.getNombre())) comoLider.add(b);
            }
            comoLider.sort(Comparator.comparingInt(Generacion::poder));
            for (PokemonBase b : comoLider) {
                if (resto.size() >= NUM_MIEMBROS * POKEMON_POR_MIEMBRO) break;
                resto.add(b);
            }
        }

        List<PokemonLuchador> equipo = new ArrayList<>();
        for (int j = 0; j < POKEMON_POR_MIEMBRO; j++) {
            PokemonBase base = resto.get((indice * POKEMON_POR_MIEMBRO + j) % resto.size());
            equipo.add(new PokemonLuchador(base, nivelMiembro + j, 1));
        }
        return equipo;
    }

    /** Descripción de una línea para mostrar en el pueblo. */
    public String resumen(Generacion gen, Jugador jugador) {
        String estado;
        if (jugador.tieneMedalla(claveMedalla(gen))) {
            estado = "✔ superado";
        } else {
            int hechos = 0;
            for (int i = 0; i < NUM_MIEMBROS; i++) {
                if (jugador.miembroGimnasioDerrotado(claveMiembro(gen, i))) hechos++;
            }
            estado = "pendiente - miembros vencidos " + hechos + "/" + NUM_MIEMBROS;
        }
        return nombre + " de " + gen.getRegion() + " [" + tipo + "] - Líder: " + lider
                + " - Nv. recomendado: " + getNivelRecomendado(gen) + " (" + estado + ")";
    }

    // =====================================================
    // ================   COMBATE DE GIMNASIO   ============
    // =====================================================

    public void desafiar(Scanner sc, Jugador jugador) {
        Generacion gen = jugador.getGeneracion();
        String clave = claveMedalla(gen);

        System.out.println("\n=== " + nombre.toUpperCase() + " DE " + gen.getRegion().toUpperCase() + " (tipo " + tipo + ") ===");

        if (!jugador.equipoVM()) {
            System.out.println("Todos tus Pokémon están debilitados. Ve al Centro Pokémon antes de entrar.");
            return;
        }

        boolean primeraVez = !jugador.tieneMedalla(clave);

        if (primeraVez) {
            System.out.println("Recepción: Bienvenido a " + gen.getRegion() + ". Para llegar a " + lider
                    + " (" + tituloLider + ") tendrás que vencer antes a sus " + NUM_MIEMBROS + " miembros.");

            if (!superarMiembros(sc, jugador, gen)) {
                return; // perdió, se rindió o salió del gimnasio
            }
            System.out.println("\n" + lider + ": ¡Has llegado hasta aquí! Aquí se viene a demostrar lo que uno vale. "
                    + "¡No te olvides de suscribirte... a la victoria!");
        } else {
            System.out.println(lider + " (" + tituloLider + "): Ya tienes la " + medalla
                    + " de aquí, pero un combate de revancha siempre da visitas.");
            System.out.println("(La revancha es solo contra el líder y no da recompensas.)");
        }

        boolean victoria = CombateEntrenador.combatir(sc, jugador, lider, crearEquipo(gen));

        if (!victoria) {
            System.out.println("\n" + lider + ": ¡Vuelve cuando estés más preparado!");
            System.out.println("Puedes curar a tus Pokémon en el Centro Pokémon.");
            return;
        }

        System.out.println("\n¡Has derrotado a " + lider + "!");
        if (primeraVez) {
            int dinero = recompensaDinero + recompensaDinero * (gen.getNumero() - 1) / 4;
            jugador.añadirMedalla(clave);
            jugador.agregarDinero(dinero);
            jugador.añadirMT(mtRecompensa);
            System.out.println(lider + ": Has ganado. Toma la " + medalla + ".");
            System.out.println("🏅 Has obtenido la " + clave + " (" + jugador.medallasEnGeneracion(gen) + "/" + GIMNASIOS.size() + " en " + gen.getRegion() + ").");
            System.out.println("💰 Has ganado " + dinero + "€.");
            System.out.println("📀 Has recibido la MT " + mtRecompensa + " (úsala desde el menú).");
        }
    }

    /**
     * Combate a los 3 miembros por orden. Devuelve true si están los 3 vencidos y el jugador
     * quiere seguir hacia el líder. Los ya vencidos en visitas anteriores se saltan.
     */
    private boolean superarMiembros(Scanner sc, Jugador jugador, Generacion gen) {
        for (int i = 0; i < NUM_MIEMBROS; i++) {
            String claveMiembro = claveMiembro(gen, i);

            if (jugador.miembroGimnasioDerrotado(claveMiembro)) {
                System.out.println("(" + miembros[i] + " ya está vencido.)");
                continue;
            }
            if (!jugador.equipoVM()) {
                System.out.println("No te quedan Pokémon en pie. Cúralos en el Centro Pokémon y vuelve.");
                return false;
            }

            System.out.println("\n[Miembro " + (i + 1) + "/" + NUM_MIEMBROS + "] " + miembros[i]
                    + ": ¡Para llegar al líder tendrás que pasar por mí!");

            boolean victoria = CombateEntrenador.combatir(sc, jugador, miembros[i], crearEquipoMiembro(gen, i));
            if (!victoria) {
                System.out.println("\n" + miembros[i] + ": ¡Vuelve cuando estés más preparado!");
                System.out.println("Los miembros que ya has vencido siguen vencidos. Cura a tus Pokémon y vuelve.");
                return false;
            }

            jugador.marcarMiembroGimnasioDerrotado(claveMiembro);
            System.out.println("\n¡Has derrotado a " + miembros[i] + "! (" + (i + 1) + "/" + NUM_MIEMBROS + ")");

            String siguiente = (i + 1 < NUM_MIEMBROS) ? miembros[i + 1] : lider + " (líder)";
            if (!seguir(sc, siguiente)) {
                System.out.println("Sales del gimnasio. Tu progreso queda guardado.");
                return false;
            }
        }
        return true;
    }

    private boolean seguir(Scanner sc, String siguiente) {
        System.out.println("Siguiente rival: " + siguiente);
        System.out.println("1. Continuar");
        System.out.println("2. Salir del gimnasio (el progreso se guarda)");
        System.out.print("Elige: ");
        return Entrada.leerEntero(sc) == 1;
    }
}
