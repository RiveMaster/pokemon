package Proyecto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Las 10 "generaciones" del juego. De la 1 a la 9 son las reales (Pokédex nacional por
 * rangos). La 10 es una mezcla de todas. La generación en la que está el jugador cambia
 * qué Pokémon salen en rutas, gimnasios y sedes del Gobierno, y sube el nivel del mundo.
 */
public enum Generacion {
    GEN1(1, "Kanto", 1, 151, "Los 151 originales: donde empezó todo."),
    GEN2(2, "Johto", 152, 251, "Tradición y nostalgia: los bichos de la segunda hornada."),
    GEN3(3, "Hoenn", 252, 386, "Clima extremo, mar por todas partes y bichos de otro mundo."),
    GEN4(4, "Sinnoh", 387, 493, "Montañas nevadas y mitos de tiempo y espacio."),
    GEN5(5, "Teselia", 494, 649, "Todo Pokémon nuevo: aquí no hay ninguno de los antiguos."),
    GEN6(6, "Kalos", 650, 721, "Elegancia, hadas y evoluciones espectaculares."),
    GEN7(7, "Alola", 722, 809, "Islas tropicales, formas regionales y bichos de otra dimensión."),
    GEN8(8, "Galar", 810, 905, "Estadios llenos, ligas de fama y bichos gigantes."),
    GEN9(9, "Paldea", 906, 1025, "Mundo abierto: sin caminos marcados ni ruta obligada."),
    GEN10(10, "Mezcla", 1, 1025, "Todas las regiones fundidas en una: aquí vale todo.");

    private final int numero;
    private final String region;
    private final int desde;
    private final int hasta;
    private final String ambiente;

    Generacion(int numero, String region, int desde, int hasta, String ambiente) {
        this.numero = numero;
        this.region = region;
        this.desde = desde;
        this.hasta = hasta;
        this.ambiente = ambiente;
    }

    public int getNumero() { return numero; }

    public String getRegion() { return region; }

    public String getAmbiente() { return ambiente; }

    public String etiqueta() { return "Gen " + numero + " - " + region; }

    public boolean esUltima() { return numero == 10; }

    public static Generacion de(int numero) {
        for (Generacion g : values()) {
            if (g.numero == numero) return g;
        }
        return GEN1;
    }

    public Generacion siguiente() {
        return esUltima() ? null : de(numero + 1);
    }

    /** Cuánto se sube el nivel de todo el mundo en esta generación (gen 1 = 0, gen 10 = +27). */
    public int getBonusNivel() { return (numero - 1) * 3; }

    public static Set<String> tipos(String... tipos) {
        return new LinkedHashSet<>(Arrays.asList(tipos));
    }

    // ------------------------------------------------------------------
    // Selección de Pokémon de esta generación
    // ------------------------------------------------------------------

    // Legendarios, míticos, Ultraentes y Paradoja: no salen en rutas, ni en gimnasios, ni en las sedes
    private static final Set<String> EXCLUIDOS = new java.util.HashSet<>(Arrays.asList(
            "Articuno", "Zapdos", "Moltres", "Mewtwo", "Mew",
            "Raikou", "Entei", "Suicune", "Lugia", "Ho-oh", "Celebi",
            "Regirock", "Regice", "Registeel", "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Jirachi", "Deoxys",
            "Uxie", "Mesprit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina", "Cresselia",
            "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus",
            "Victini", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram", "Zekrom",
            "Landorus", "Kyurem", "Keldeo", "Meloetta", "Genesect",
            "Xerneas", "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion",
            "Type: Null", "Silvally", "Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Cosmog", "Cosmoem",
            "Solgaleo", "Lunala", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana",
            "Guzzlord", "Necrozma", "Magearna", "Marshadow", "Poipole", "Naganadel", "Stakataka", "Blacephalon",
            "Zeraora", "Meltan", "Melmetal",
            "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Zarude", "Regieleki", "Regidrago",
            "Glastrier", "Spectrier", "Calyrex", "Enamorus",
            "Koraidon", "Miraidon", "Wo-Chien", "Chien-Pao", "Ting-Lu", "Chi-Yu", "Okidogi", "Munkidori",
            "Fezandipiti", "Ogerpon", "Terapagos", "Pecharunt",
            "Great Tusk", "Scream Tail", "Brute Bonnet", "Flutter Mane", "Slither Wing", "Sandy Shocks",
            "Roaring Moon", "Walking Wake", "Gouging Fire", "Raging Bolt",
            "Iron Treads", "Iron Bundle", "Iron Hands", "Iron Jugulis", "Iron Moth", "Iron Thorns",
            "Iron Valiant", "Iron Leaves", "Iron Boulder", "Iron Crown"));

    private List<PokemonBase> cache; // se calcula una vez por generación

    public List<PokemonBase> getPokemons() {
        if (cache == null) {
            List<PokemonBase> todos = Pokedex.getTodos();
            List<PokemonBase> lista = new ArrayList<>();
            for (PokemonBase b : todos.subList(desde - 1, Math.min(hasta, todos.size()))) {
                if (!EXCLUIDOS.contains(b.getNombre())) lista.add(b);
            }
            cache = lista;
        }
        return cache;
    }

    // "Poder" aproximado: suma de estadísticas base
    private static int poder(PokemonBase b) {
        return b.getVidaBase() + b.getAtaqueBase() + b.getDefensaBase() + b.getVelocidadBase();
    }

    // Poder máximo permitido a un nivel: evita ver formas finales o legendarios al principio
    private static int poderMaximo(int nivel) {
        return Math.min(420, 170 + 8 * nivel);
    }

    private static boolean tieneTipo(PokemonBase b, Set<String> tipos) {
        return tipos == null
                || tipos.contains(b.getTipo1())
                || (b.getTipo2() != null && tipos.contains(b.getTipo2()));
    }

    /** Pokémon de esta generación con alguno de esos tipos y adecuados para ese nivel. */
    public List<PokemonBase> candidatos(Set<String> tipos, int nivel) {
        int max = poderMaximo(nivel);
        List<PokemonBase> res = new ArrayList<>();
        for (PokemonBase b : getPokemons()) {
            if (tieneTipo(b, tipos) && poder(b) <= max) res.add(b);
        }
        if (res.isEmpty()) {
            // Ninguno tan flojo con esos tipos: los 3 más débiles de esos tipos
            res = masDebiles(tipos, 3);
        }
        if (res.isEmpty()) {
            res = masDebiles(null, 3);
        }
        return res;
    }

    private List<PokemonBase> masDebiles(Set<String> tipos, int n) {
        List<PokemonBase> lista = new ArrayList<>();
        for (PokemonBase b : getPokemons()) {
            if (tieneTipo(b, tipos)) lista.add(b);
        }
        lista.sort(Comparator.comparingInt(Generacion::poder));
        return new ArrayList<>(lista.subList(0, Math.min(n, lista.size())));
    }

    /** Un Pokémon salvaje al azar para una ruta. */
    public PokemonBase aleatorio(Set<String> tipos, int nivel, Random rand) {
        List<PokemonBase> c = candidatos(tipos, nivel);
        return c.get(rand.nextInt(c.size()));
    }

    /**
     * Equipo fijo (siempre el mismo) para líderes y jefes: los n más fuertes que encajan
     * con esos tipos a ese nivel, ordenados de más flojo a más fuerte (el "as" sale el último).
     */
    public List<PokemonBase> equipoPara(Set<String> tipos, int nivelMax, int n) {
        List<PokemonBase> elegidos = new ArrayList<>(candidatos(tipos, nivelMax));
        elegidos.sort(Comparator.comparingInt(Generacion::poder).reversed());
        if (elegidos.size() > n) {
            elegidos = new ArrayList<>(elegidos.subList(0, n));
        }
        if (elegidos.size() < n) {
            // Faltan por tipo: se completa con los mejores de cualquier tipo
            List<PokemonBase> resto = new ArrayList<>(candidatos(null, nivelMax));
            resto.sort(Comparator.comparingInt(Generacion::poder).reversed());
            for (PokemonBase b : resto) {
                if (elegidos.size() >= n) break;
                if (!elegidos.contains(b)) elegidos.add(b);
            }
        }
        elegidos.sort(Comparator.comparingInt(Generacion::poder));
        return elegidos;
    }

    /** Unos pocos nombres típicos de esos tipos, para dar ambiente a los pueblos. */
    public List<String> muestra(Set<String> tipos, int n) {
        List<String> nombres = new ArrayList<>();
        for (PokemonBase b : candidatos(tipos, 25)) {
            if (nombres.size() >= n) break;
            nombres.add(b.getNombre());
        }
        return nombres;
    }
}
