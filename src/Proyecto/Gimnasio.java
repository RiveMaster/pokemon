package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Gimnasio Pokémon. Hay uno por ciudad y cada uno tiene un tipo. El gimnasio es el mismo
 * en las 10 generaciones, pero su equipo cambia: se forma con Pokémon de ese tipo de la
 * generación en la que está el jugador, y los niveles suben con la generación.
 *
 * Los líderes son personajes FICTICIOS inspirados en el mundo del streaming/YouTube.
 * Para cambiar nombres o títulos basta con tocar las llamadas a registrar(...) de abajo.
 */
public class Gimnasio {

    private static final List<Gimnasio> GIMNASIOS = new ArrayList<>();

    static {
        //                    gimnasio            tipo (visible) tipos internos             líder                título del líder                    medalla          €     MT             nv  nº
        registrar(new Gimnasio("Gimnasio Peñascal", "Roca", Generacion.tipos("Rock"), "Roque Unboxing", "el youtuber de unboxings", "Medalla Roca", 800, "Avalancha", 11, 3));
        registrar(new Gimnasio("Gimnasio Marejada", "Agua", Generacion.tipos("Water"), "Marina Directo", "la streamer de directos eternos", "Medalla Cascada", 1200, "Cascada", 16, 3));
        registrar(new Gimnasio("Gimnasio Voltio", "Eléctrico", Generacion.tipos("Electric"), "Volta Speedrun", "el speedrunner más rápido", "Medalla Trueno", 1600, "Trueno", 20, 3));
        registrar(new Gimnasio("Gimnasio Frondoso", "Planta", Generacion.tipos("Grass"), "Flora Tutoriales", "la youtuber de manualidades y plantas", "Medalla Hoja", 2000, "Danza Petalo", 25, 3));
        registrar(new Gimnasio("Gimnasio Ascua", "Fuego", Generacion.tipos("Fire"), "Cenizo Reto", "el rey del reto picante", "Medalla Volcán", 2500, "Lanzallamas", 30, 4));
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
    private final Set<String> tiposInternos;
    private final String lider;
    private final String tituloLider;
    private final String medalla;
    private final int recompensaDinero;
    private final String mtRecompensa;
    private final int nivelBase;
    private final int tamanoEquipo;

    private Gimnasio(String nombre, String tipo, Set<String> tiposInternos, String lider, String tituloLider,
                     String medalla, int recompensaDinero, String mtRecompensa, int nivelBase, int tamanoEquipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tiposInternos = tiposInternos;
        this.lider = lider;
        this.tituloLider = tituloLider;
        this.medalla = medalla;
        this.recompensaDinero = recompensaDinero;
        this.mtRecompensa = mtRecompensa;
        this.nivelBase = nivelBase;
        this.tamanoEquipo = tamanoEquipo;
    }

    public String getNombre() { return nombre; }

    public String getTipo() { return tipo; }

    public Set<String> getTiposInternos() { return tiposInternos; }

    public String getLider() { return lider; }

    public String getMedalla() { return medalla; }

    /** La medalla se guarda con la región, así cada generación tiene la suya. */
    public String claveMedalla(Generacion gen) {
        return medalla + " (" + gen.getRegion() + ")";
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

    /** Descripción de una línea para mostrar en el pueblo. */
    public String resumen(Generacion gen, Jugador jugador) {
        String estado = jugador.tieneMedalla(claveMedalla(gen)) ? "✔ superado" : "pendiente";
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
            System.out.println("Todos tus Pokémon están debilitados. Ve al Centro Pokémon antes de desafiar a " + lider + ".");
            return;
        }

        boolean primeraVez = !jugador.tieneMedalla(clave);
        System.out.println(lider + " (" + tituloLider + "): ¡Bienvenido a " + gen.getRegion() + "!");
        if (primeraVez) {
            System.out.println(lider + ": Aquí se viene a demostrar lo que uno vale. ¡No te olvides de suscribirte... a la victoria!");
        } else {
            System.out.println(lider + ": Ya tienes la " + medalla + " de aquí, pero un combate de revancha siempre da visitas.");
            System.out.println("(La revancha no da recompensas.)");
        }

        List<PokemonLuchador> equipoLider = crearEquipo(gen);
        boolean victoria = CombateEntrenador.combatir(sc, jugador, lider, equipoLider);

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
}
