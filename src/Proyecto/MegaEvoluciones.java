package Proyecto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Catálogo de las 93 megaevoluciones oficiales documentadas hasta Pokémon Legends: Z-A / Mega Dimension.
 * Las estadísticas, tipos, habilidades y piedras se mantienen como datos de forma; no dependen de red
 * durante la partida. Las formas Mega no se incluyen en las selecciones de Pokémon salvajes normales.
 */
public final class MegaEvoluciones {
    private MegaEvoluciones() { }

    // El juego permite una megaevolución por lado y combate.
    private static boolean megaUsadaEnCombate;

    public static void iniciarCombate() { megaUsadaEnCombate = false; }
    public static void terminarCombate() { megaUsadaEnCombate = false; }
    public static boolean yaSeMegaEvolucionoEnCombate() { return megaUsadaEnCombate; }

    public record Forma(String especie, String nombreForma, String tipo1, String tipo2,
                        int vida, int ataque, int defensa, int ataqueEspecial, int defensaEspecial,
                        int velocidad, String megapiedra, String habilidad) {
        public String piedraMostrada() {
            if (megapiedra != null) return megapiedra;
            return "No requiere megapiedra (requiere Ascenso Draco)";
        }
    }

    private static final List<Forma> FORMAS = List.of(
        new Forma("Venusaur", "Venusaur-Mega", "Grass", "Poison", 80, 100, 123, 122, 120, 80, "Venusaurite", "Thick Fat"),
        new Forma("Charizard", "Charizard-Mega-X", "Fire", "Dragon", 78, 130, 111, 130, 85, 100, "Charizardite X", "Tough Claws"),
        new Forma("Charizard", "Charizard-Mega-Y", "Fire", "Flying", 78, 104, 78, 159, 115, 100, "Charizardite Y", "Drought"),
        new Forma("Blastoise", "Blastoise-Mega", "Water", null, 79, 103, 120, 135, 115, 78, "Blastoisinite", "Mega Launcher"),
        new Forma("Beedrill", "Beedrill-Mega", "Bug", "Poison", 65, 150, 40, 15, 80, 145, "Beedrillite", "Adaptability"),
        new Forma("Pidgeot", "Pidgeot-Mega", "Normal", "Flying", 83, 80, 80, 135, 80, 121, "Pidgeotite", "No Guard"),
        new Forma("Raichu", "Raichu-Mega-X", "Electric", null, 60, 135, 95, 90, 95, 110, "Raichunite X", "Electric Surge"),
        new Forma("Raichu", "Raichu-Mega-Y", "Electric", null, 60, 100, 55, 160, 80, 130, "Raichunite Y", "No Guard"),
        new Forma("Clefable", "Clefable-Mega", "Fairy", "Flying", 95, 80, 93, 135, 110, 70, "Clefablite", "Magic Bounce"),
        new Forma("Alakazam", "Alakazam-Mega", "Psychic", null, 55, 50, 65, 175, 105, 150, "Alakazite", "Trace"),
        new Forma("Victreebel", "Victreebel-Mega", "Grass", "Poison", 80, 125, 85, 135, 95, 70, "Victreebelite", "Innards Out"),
        new Forma("Slowbro", "Slowbro-Mega", "Water", "Psychic", 95, 75, 180, 130, 80, 30, "Slowbronite", "Shell Armor"),
        new Forma("Gengar", "Gengar-Mega", "Ghost", "Poison", 60, 65, 80, 170, 95, 130, "Gengarite", "Shadow Tag"),
        new Forma("Kangaskhan", "Kangaskhan-Mega", "Normal", null, 105, 125, 100, 60, 100, 100, "Kangaskhanite", "Parental Bond"),
        new Forma("Starmie", "Starmie-Mega", "Water", "Psychic", 60, 100, 105, 130, 105, 120, "Starminite", "Huge Power"),
        new Forma("Pinsir", "Pinsir-Mega", "Bug", "Flying", 65, 155, 120, 65, 90, 105, "Pinsirite", "Aerilate"),
        new Forma("Gyarados", "Gyarados-Mega", "Water", "Dark", 95, 155, 109, 70, 130, 81, "Gyaradosite", "Mold Breaker"),
        new Forma("Aerodactyl", "Aerodactyl-Mega", "Rock", "Flying", 80, 135, 85, 70, 95, 150, "Aerodactylite", "Tough Claws"),
        new Forma("Dragonite", "Dragonite-Mega", "Dragon", "Flying", 91, 124, 115, 145, 125, 100, "Dragoninite", "Multiscale"),
        new Forma("Mewtwo", "Mewtwo-Mega-X", "Psychic", "Fighting", 106, 190, 100, 154, 100, 130, "Mewtwonite X", "Steadfast"),
        new Forma("Mewtwo", "Mewtwo-Mega-Y", "Psychic", null, 106, 150, 70, 194, 120, 140, "Mewtwonite Y", "Insomnia"),
        new Forma("Meganium", "Meganium-Mega", "Grass", "Fairy", 80, 92, 115, 143, 115, 80, "Meganiumite", "Mega Sol"),
        new Forma("Feraligatr", "Feraligatr-Mega", "Water", "Dragon", 85, 160, 125, 89, 93, 78, "Feraligite", "Dragonize"),
        new Forma("Ampharos", "Ampharos-Mega", "Electric", "Dragon", 90, 95, 105, 165, 110, 45, "Ampharosite", "Mold Breaker"),
        new Forma("Steelix", "Steelix-Mega", "Steel", "Ground", 75, 125, 230, 55, 95, 30, "Steelixite", "Sand Force"),
        new Forma("Scizor", "Scizor-Mega", "Bug", "Steel", 70, 150, 140, 65, 100, 75, "Scizorite", "Technician"),
        new Forma("Heracross", "Heracross-Mega", "Bug", "Fighting", 80, 185, 115, 40, 105, 75, "Heracronite", "Skill Link"),
        new Forma("Skarmory", "Skarmory-Mega", "Steel", "Flying", 65, 140, 110, 40, 100, 110, "Skarmorite", "Stalwart"),
        new Forma("Houndoom", "Houndoom-Mega", "Dark", "Fire", 75, 90, 90, 140, 90, 115, "Houndoominite", "Solar Power"),
        new Forma("Tyranitar", "Tyranitar-Mega", "Rock", "Dark", 100, 164, 150, 95, 120, 71, "Tyranitarite", "Sand Stream"),
        new Forma("Sceptile", "Sceptile-Mega", "Grass", "Dragon", 70, 110, 75, 145, 85, 145, "Sceptilite", "Lightning Rod"),
        new Forma("Blaziken", "Blaziken-Mega", "Fire", "Fighting", 80, 160, 80, 130, 80, 100, "Blazikenite", "Speed Boost"),
        new Forma("Swampert", "Swampert-Mega", "Water", "Ground", 100, 150, 110, 95, 110, 70, "Swampertite", "Swift Swim"),
        new Forma("Gardevoir", "Gardevoir-Mega", "Psychic", "Fairy", 68, 85, 65, 165, 135, 100, "Gardevoirite", "Pixilate"),
        new Forma("Sableye", "Sableye-Mega", "Dark", "Ghost", 50, 85, 125, 85, 115, 20, "Sablenite", "Magic Bounce"),
        new Forma("Mawile", "Mawile-Mega", "Steel", "Fairy", 50, 105, 125, 55, 95, 50, "Mawilite", "Huge Power"),
        new Forma("Aggron", "Aggron-Mega", "Steel", null, 70, 140, 230, 60, 80, 50, "Aggronite", "Filter"),
        new Forma("Medicham", "Medicham-Mega", "Fighting", "Psychic", 60, 100, 85, 80, 85, 100, "Medichamite", "Pure Power"),
        new Forma("Manectric", "Manectric-Mega", "Electric", null, 70, 75, 80, 135, 80, 135, "Manectite", "Intimidate"),
        new Forma("Sharpedo", "Sharpedo-Mega", "Water", "Dark", 70, 140, 70, 110, 65, 105, "Sharpedonite", "Strong Jaw"),
        new Forma("Camerupt", "Camerupt-Mega", "Fire", "Ground", 70, 120, 100, 145, 105, 20, "Cameruptite", "Sheer Force"),
        new Forma("Altaria", "Altaria-Mega", "Dragon", "Fairy", 75, 110, 110, 110, 105, 80, "Altarianite", "Pixilate"),
        new Forma("Banette", "Banette-Mega", "Ghost", null, 64, 165, 75, 93, 83, 75, "Banettite", "Prankster"),
        new Forma("Chimecho", "Chimecho-Mega", "Psychic", "Steel", 75, 50, 110, 135, 120, 65, "Chimechite", "Levitate"),
        new Forma("Absol", "Absol-Mega", "Dark", null, 65, 150, 60, 115, 60, 115, "Absolite", "Magic Bounce"),
        new Forma("Absol", "Absol-Mega-Z", "Dark", "Ghost", 65, 154, 60, 75, 60, 151, "Absolite Z", "Sharpness"),
        new Forma("Glalie", "Glalie-Mega", "Ice", null, 80, 120, 80, 120, 80, 100, "Glalitite", "Refrigerate"),
        new Forma("Salamence", "Salamence-Mega", "Dragon", "Flying", 95, 145, 130, 120, 90, 120, "Salamencite", "Aerilate"),
        new Forma("Metagross", "Metagross-Mega", "Steel", "Psychic", 80, 145, 150, 105, 110, 110, "Metagrossite", "Tough Claws"),
        new Forma("Latias", "Latias-Mega", "Dragon", "Psychic", 80, 100, 120, 140, 150, 110, "Latiasite", "Levitate"),
        new Forma("Latios", "Latios-Mega", "Dragon", "Psychic", 80, 130, 100, 160, 120, 110, "Latiosite", "Levitate"),
        new Forma("Rayquaza", "Rayquaza-Mega", "Dragon", "Flying", 105, 180, 100, 180, 100, 115, null, "Delta Stream"),
        new Forma("Staraptor", "Staraptor-Mega", "Fighting", "Flying", 85, 140, 100, 60, 90, 110, "Staraptite", "Contrary"),
        new Forma("Lopunny", "Lopunny-Mega", "Normal", "Fighting", 65, 136, 94, 54, 96, 135, "Lopunnite", "Scrappy"),
        new Forma("Garchomp", "Garchomp-Mega", "Dragon", "Ground", 108, 170, 115, 120, 95, 92, "Garchompite", "Sand Force"),
        new Forma("Garchomp", "Garchomp-Mega-Z", "Dragon", null, 108, 130, 85, 141, 85, 151, "Garchompite Z", "Levitate"),
        new Forma("Lucario", "Lucario-Mega", "Fighting", "Steel", 70, 145, 88, 140, 70, 112, "Lucarionite", "Adaptability"),
        new Forma("Lucario", "Lucario-Mega-Z", "Fighting", "Steel", 70, 100, 70, 164, 70, 151, "Lucarionite Z", "Aura Guard"),
        new Forma("Abomasnow", "Abomasnow-Mega", "Grass", "Ice", 90, 132, 105, 132, 105, 30, "Abomasite", "Snow Warning"),
        new Forma("Gallade", "Gallade-Mega", "Psychic", "Fighting", 68, 165, 95, 65, 115, 110, "Galladite", "Inner Focus"),
        new Forma("Froslass", "Froslass-Mega", "Ice", "Ghost", 70, 80, 70, 140, 100, 120, "Froslassite", "Snow Warning"),
        new Forma("Heatran", "Heatran-Mega", "Fire", "Steel", 91, 120, 106, 175, 141, 67, "Heatranite", "Flash Fire"),
        new Forma("Darkrai", "Darkrai-Mega", "Dark", null, 70, 120, 130, 165, 130, 85, "Darkranite", "Bad Dreams"),
        new Forma("Emboar", "Emboar-Mega", "Fire", "Fighting", 110, 148, 75, 110, 110, 75, "Emboarite", "Mold Breaker"),
        new Forma("Excadrill", "Excadrill-Mega", "Ground", "Steel", 110, 165, 100, 65, 65, 103, "Excadrite", "Piercing Drill"),
        new Forma("Audino", "Audino-Mega", "Normal", "Fairy", 103, 60, 126, 80, 126, 50, "Audinite", "Healer"),
        new Forma("Scolipede", "Scolipede-Mega", "Bug", "Poison", 60, 140, 149, 75, 99, 62, "Scolipite", "Shell Armor"),
        new Forma("Scrafty", "Scrafty-Mega", "Dark", "Fighting", 65, 130, 135, 55, 135, 68, "Scraftinite", "Intimidate"),
        new Forma("Eelektross", "Eelektross-Mega", "Electric", null, 85, 145, 80, 135, 90, 80, "Eelektrossite", "Eelevate"),
        new Forma("Chandelure", "Chandelure-Mega", "Ghost", "Fire", 60, 75, 110, 175, 110, 90, "Chandelurite", "Infiltrator"),
        new Forma("Golurk", "Golurk-Mega", "Ground", "Ghost", 89, 159, 105, 70, 105, 55, "Golurkite", "Unseen Fist"),
        new Forma("Chesnaught", "Chesnaught-Mega", "Grass", "Fighting", 88, 137, 172, 74, 115, 44, "Chesnaughtite", "Bulletproof"),
        new Forma("Delphox", "Delphox-Mega", "Fire", "Psychic", 75, 69, 72, 159, 125, 134, "Delphoxite", "Levitate"),
        new Forma("Greninja", "Greninja-Mega", "Water", "Dark", 72, 125, 77, 133, 81, 142, "Greninjite", "Protean"),
        new Forma("Pyroar", "Pyroar-Mega", "Fire", "Normal", 86, 88, 92, 129, 86, 126, "Pyroarite", "Fire Mane"),
        new Forma("Floette", "Floette-Mega", "Fairy", null, 74, 85, 87, 155, 148, 102, "Floettite", "Fairy Aura"),
        new Forma("Meowstic", "Meowstic-M-Mega", "Psychic", null, 74, 48, 76, 143, 101, 124, "Meowsticite", "Trace"),
        new Forma("Malamar", "Malamar-Mega", "Dark", "Psychic", 86, 102, 88, 98, 120, 88, "Malamarite", "Contrary"),
        new Forma("Barbaracle", "Barbaracle-Mega", "Rock", "Fighting", 72, 140, 130, 64, 106, 88, "Barbaracite", "Tough Claws"),
        new Forma("Dragalge", "Dragalge-Mega", "Poison", "Dragon", 65, 85, 105, 132, 163, 44, "Dragalgite", "Regenerator"),
        new Forma("Hawlucha", "Hawlucha-Mega", "Fighting", "Flying", 78, 137, 100, 74, 93, 118, "Hawluchanite", "No Guard"),
        new Forma("Zygarde", "Zygarde-Mega", "Dragon", "Ground", 216, 70, 91, 216, 85, 100, "Zygardite", "Aura Break"),
        new Forma("Diancie", "Diancie-Mega", "Rock", "Fairy", 50, 160, 110, 160, 110, 110, "Diancite", "Magic Bounce"),
        new Forma("Crabominable", "Crabominable-Mega", "Fighting", "Ice", 97, 157, 122, 62, 107, 33, "Crabominite", "Iron Fist"),
        new Forma("Golisopod", "Golisopod-Mega", "Bug", "Steel", 75, 150, 175, 70, 120, 40, "Golisopite", "Tough Claws"),
        new Forma("Drampa", "Drampa-Mega", "Normal", "Dragon", 78, 85, 110, 160, 116, 36, "Drampanite", "Berserk"),
        new Forma("Magearna", "Magearna-Mega", "Steel", "Fairy", 80, 125, 115, 170, 115, 95, "Magearnite", "Soul-Heart"),
        new Forma("Zeraora", "Zeraora-Mega", "Electric", null, 88, 157, 75, 147, 80, 153, "Zeraorite", "Volt Absorb"),
        new Forma("Falinks", "Falinks-Mega", "Fighting", null, 65, 135, 135, 70, 65, 100, "Falinksite", "Defiant"),
        new Forma("Scovillain", "Scovillain-Mega", "Grass", "Fire", 65, 138, 85, 138, 85, 75, "Scovillainite", "Spicy Spray"),
        new Forma("Glimmora", "Glimmora-Mega", "Rock", "Poison", 83, 90, 105, 150, 96, 101, "Glimmoranite", "Adaptability"),
        new Forma("Tatsugiri", "Tatsugiri-Curly-Mega", "Dragon", "Water", 68, 65, 90, 135, 125, 92, "Tatsugirinite", "Commander"),
        new Forma("Baxcalibur", "Baxcalibur-Mega", "Dragon", "Ice", 115, 175, 117, 105, 101, 87, "Baxcalibrite", "Thermal Exchange")
    );

    private static final Map<String, List<Forma>> POR_ESPECIE = FORMAS.stream()
            .collect(Collectors.groupingBy(f -> GeneticaPokemon.normalizar(f.especie()),
                    LinkedHashMap::new, Collectors.toList()));

    public static List<Forma> todas() { return FORMAS; }

    public static List<Forma> paraEspecie(String especie) {
        return POR_ESPECIE.getOrDefault(GeneticaPokemon.normalizar(especie), List.of());
    }

    public static Forma buscarNombre(String nombre) {
        String n = GeneticaPokemon.normalizar(nombre);
        for (Forma f : FORMAS) if (GeneticaPokemon.normalizar(f.nombreForma()).equals(n)) return f;
        return null;
    }

    public static Set<String> todasLasPiedras() {
        Set<String> piedras = FORMAS.stream().map(Forma::megapiedra).filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        piedras.add("Ascenso Draco"); // Rayquaza megaevoluciona al conocer este movimiento.
        return Collections.unmodifiableSet(piedras);
    }

    /** Añade las 93 formas a la Pokédex, pero las deja marcadas para que no aparezcan como especies salvajes. */
    public static void registrarEnPokedex(List<PokemonBase> pokemons) {
        Set<String> yaRegistradas = new HashSet<>();
        for (PokemonBase p : pokemons) yaRegistradas.add(GeneticaPokemon.normalizar(p.getNombre()));
        Map<String, PokemonBase> normales = new HashMap<>();
        for (PokemonBase p : pokemons) if (!p.esMega()) normales.putIfAbsent(GeneticaPokemon.normalizar(p.getNombre()), p);

        for (Forma f : FORMAS) {
            if (yaRegistradas.contains(GeneticaPokemon.normalizar(f.nombreForma()))) continue;
            PokemonBase original = normales.get(GeneticaPokemon.normalizar(f.especie()));
            if (original == null) continue;
            PokemonBase mega = crearBase(original, f);
            pokemons.add(mega);
            yaRegistradas.add(GeneticaPokemon.normalizar(f.nombreForma()));
        }
    }

    public static PokemonBase crearBase(PokemonBase original, Forma f) {
        PokemonBase base = new PokemonBase(f.nombreForma(), f.tipo1(), f.tipo2(),
                f.vida(), f.ataque(), f.defensa(), f.velocidad(), 0, null,
                new ArrayList<>(original.getMovimientosPorNivel()));
        List<String> habilidades = f.habilidad() == null ? original.getHabilidades() : List.of(f.habilidad());
        base.configurarCombate(f.ataqueEspecial(), f.defensaEspecial(), habilidades);
        base.configurarMega(f.especie(), f.megapiedra(), f.habilidad());
        return base;
    }

    /** Formas disponibles para un Pokémon normal. Ditto, especies ya megaevolucionadas y formas Mega no pueden usarlo. */
    public static List<Forma> paraPokemon(PokemonLuchador pokemon) {
        if (pokemon == null || pokemon.getBase() == null || pokemon.getBase().esMega()) return List.of();
        return paraEspecie(pokemon.getBase().getNombre());
    }

    public static boolean tieneFormaMega(PokemonBase base) {
        return base != null && base.esMega();
    }

    public static boolean puedeOfrecerse(PokemonLuchador pokemon) {
        if (megaUsadaEnCombate || pokemon == null || pokemon.getBase() == null || pokemon.getMegapiedra() == null
                || !pokemon.puedeMegaEvolucionarEsteCombate()) return false;
        String especie = pokemon.getBase().getNombre();
        return paraEspecie(especie).stream().anyMatch(f ->
                Objects.equals(f.megapiedra(), pokemon.getMegapiedra())
                        || (f.megapiedra() == null && "Rayquaza".equalsIgnoreCase(f.especie())
                        && "Ascenso Draco".equalsIgnoreCase(pokemon.getMegapiedra())));
    }

    /** Menú fuera de combate para comprar megapiedras y equiparlas a Pokémon del equipo/PC. */
    public static void gestionarPiedras(Scanner sc, Jugador jugador) {
        while (true) {
            System.out.println("\n=== Centro de Megaevolución ===");
            System.out.println("1. Ver megapiedras");
            System.out.println("2. Comprar megapiedra (2.500 €)");
            System.out.println("3. Equipar megapiedra a un Pokémon");
            System.out.println("4. Quitar una megapiedra");
            System.out.println("5. Volver");
            System.out.print("Elige: ");
            int op = Entrada.leerEntero(sc);
            if (op == 1) {
                System.out.println("Megapiedras en la mochila:");
                if (jugador.getMegapiedras().isEmpty()) System.out.println("  (ninguna)");
                else jugador.getMegapiedras().forEach(p -> System.out.println("  - " + p));
            } else if (op == 2) comprarPiedra(sc, jugador);
            else if (op == 3) equiparPiedra(sc, jugador);
            else if (op == 4) quitarPiedra(sc, jugador);
            else if (op == 5) return;
            else System.out.println("Opción no válida.");
        }
    }

    private static void comprarPiedra(Scanner sc, Jugador jugador) {
        List<String> disponibles = todasLasPiedras().stream()
                .filter(p -> !jugador.getMegapiedras().contains(p))
                .sorted(String.CASE_INSENSITIVE_ORDER).toList();
        if (disponibles.isEmpty()) {
            System.out.println("Ya tienes todas las megapiedras de este catálogo.");
            return;
        }
        for (int i = 0; i < disponibles.size(); i++)
            System.out.println((i + 1) + ". " + disponibles.get(i) + " — 2.500 €");
        System.out.print("Elige una (0 cancela): ");
        int n = Entrada.leerEntero(sc);
        if (n == 0) return;
        if (n < 1 || n > disponibles.size()) {
            System.out.println("Selección no válida.");
            return;
        }
        if (jugador.getDinero() < 2500) {
            System.out.println("No tienes dinero suficiente.");
            return;
        }
        String piedra = disponibles.get(n - 1);
        jugador.setDinero(jugador.getDinero() - 2500);
        jugador.obtenerMegapiedra(piedra);
        System.out.println("Has comprado " + piedra + ".");
    }

    private static void equiparPiedra(Scanner sc, Jugador jugador) {
        List<String> disponibles = jugador.getMegapiedras().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
        if (disponibles.isEmpty()) {
            System.out.println("No tienes megapiedras. Compra una primero.");
            return;
        }
        List<PokemonLuchador> pokemons = new ArrayList<>();
        pokemons.addAll(jugador.getEquipo());
        pokemons.addAll(jugador.getPc());
        if (pokemons.isEmpty()) {
            System.out.println("No tienes Pokémon.");
            return;
        }
        System.out.println("Elige Pokémon (equipo y PC):");
        for (int i = 0; i < pokemons.size(); i++) {
            PokemonLuchador p = pokemons.get(i);
            System.out.println((i + 1) + ". " + p.getNombre() + " " + (p.getMegapiedra() == null ? "" : "[lleva " + p.getMegapiedra() + "]"));
        }
        System.out.print("Pokémon (0 cancela): ");
        int pi = Entrada.leerEntero(sc);
        if (pi == 0) return;
        if (pi < 1 || pi > pokemons.size()) {
            System.out.println("Selección no válida.");
            return;
        }
        PokemonLuchador pokemon = pokemons.get(pi - 1);
        List<String> compatibles = disponibles.stream()
                .filter(stone -> pokemons.stream().noneMatch(otro ->
                        otro != pokemon && stone.equalsIgnoreCase(otro.getMegapiedra() == null ? "" : otro.getMegapiedra())))
                .filter(stone -> paraEspecie(pokemon.getBase().getNombre()).stream().anyMatch(f ->
                        Objects.equals(stone, f.megapiedra())
                                || (f.megapiedra() == null && "Rayquaza".equalsIgnoreCase(f.especie())
                                && "Ascenso Draco".equalsIgnoreCase(stone))))
                .toList();
        if (compatibles.isEmpty()) {
            System.out.println(pokemon.getNombre()
                    + " no tiene una megapiedra compatible libre, o no dispone de megaevolución.");
            return;
        }
        for (int i = 0; i < compatibles.size(); i++) System.out.println((i + 1) + ". " + compatibles.get(i));
        System.out.print("Megapiedra (0 cancela): ");
        int si = Entrada.leerEntero(sc);
        if (si == 0) return;
        if (si < 1 || si > compatibles.size()) {
            System.out.println("Selección no válida.");
            return;
        }
        pokemon.setMegapiedra(compatibles.get(si - 1));
        System.out.println(pokemon.getNombre() + " lleva ahora " + pokemon.getMegapiedra() + ".");
    }

    private static void quitarPiedra(Scanner sc, Jugador jugador) {
        List<PokemonLuchador> pokemons = new ArrayList<>();
        pokemons.addAll(jugador.getEquipo());
        pokemons.addAll(jugador.getPc());
        List<PokemonLuchador> equipados = pokemons.stream().filter(p -> p.getMegapiedra() != null).toList();
        if (equipados.isEmpty()) {
            System.out.println("Ningún Pokémon lleva una megapiedra.");
            return;
        }
        for (int i = 0; i < equipados.size(); i++) System.out.println((i + 1) + ". " + equipados.get(i).getNombre() + " — " + equipados.get(i).getMegapiedra());
        System.out.print("Elige Pokémon (0 cancela): ");
        int n = Entrada.leerEntero(sc);
        if (n < 1 || n > equipados.size()) return;
        PokemonLuchador p = equipados.get(n - 1);
        p.setMegapiedra(null);
        System.out.println("Piedra retirada.");
    }

    /** Opción de combate: la megaevolución usa la piedra que lleva el Pokémon y ocupa la acción del turno. */
    public static boolean intentarDuranteCombate(PokemonLuchador pokemon, Scanner sc) {
        if (megaUsadaEnCombate || pokemon == null || pokemon.getMegapiedra() == null
                || !pokemon.puedeMegaEvolucionarEsteCombate()) {
            System.out.println("Este Pokémon no puede megaevolucionar: necesita una megapiedra compatible y no haber megaevolucionado ya en este combate.");
            return false;
        }
        List<Forma> opciones = paraEspecie(pokemon.getBase().getNombre()).stream()
                .filter(f -> Objects.equals(f.megapiedra(), pokemon.getMegapiedra())
                        || (f.megapiedra() == null && "Rayquaza".equalsIgnoreCase(f.especie())
                        && "Ascenso Draco".equalsIgnoreCase(pokemon.getMegapiedra())))
                .toList();
        if (opciones.isEmpty()) {
            System.out.println("La megapiedra que lleva no corresponde a su especie.");
            return false;
        }
        if (opciones.size() == 1) {
            Forma f = opciones.get(0);
            PokemonBase forma = crearBase(pokemon.getBase(), f);
            if (pokemon.megaevolucionar(forma)) {
                megaUsadaEnCombate = true;
                System.out.println("¡" + pokemon.getNombre() + " ha megaevolucionado!");
                return true;
            }
            return false;
        }
        System.out.println("Elige la megaevolución:");
        for (int i = 0; i < opciones.size(); i++) System.out.println((i + 1) + ". " + opciones.get(i).nombreForma());
        int n = Entrada.leerEntero(sc);
        if (n < 1 || n > opciones.size()) return false;
        Forma f = opciones.get(n - 1);
        if (pokemon.megaevolucionar(crearBase(pokemon.getBase(), f))) {
            megaUsadaEnCombate = true;
            System.out.println("¡" + pokemon.getNombre() + " ha megaevolucionado!");
            return true;
        }
        return false;
    }
}
