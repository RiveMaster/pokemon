package Proyecto;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Pruebas funcionales básicas para el sistema de crianza y las megaevoluciones. */
public final class SistemaCrianzaMegaSmokeTest {
    private SistemaCrianzaMegaSmokeTest() { }

    public static void main(String[] args) {
        new Pokedex().cargarpokemons();
        probarCatalogoMega();
        probarSexoYCompatibilidad();
        probarCrianzaEIVs();
        probarCooldown();
        probarMegaEnCombate();
        System.out.println("OK: catálogo Mega, sexos, compatibilidad, herencia de seis IVs, cooldown y megaevolución.");
    }

    private static void probarCatalogoMega() {
        long mega = Pokedex.getTodos().stream().filter(PokemonBase::esMega).count();
        check(mega == 93, "Esperadas 93 formas Mega, encontradas " + mega);
        check(MegaEvoluciones.paraEspecie("Charizard").size() == 2, "Charizard debe tener formas Mega X e Y.");
        check(MegaEvoluciones.paraEspecie("Raichu").size() == 2, "Raichu debe tener formas Mega X e Y.");
        check(MegaEvoluciones.paraEspecie("Garchomp").stream().anyMatch(f -> f.nombreForma().equals("Garchomp-Mega-Z")),
                "Falta Mega-Garchomp Z.");
        check(MegaEvoluciones.todasLasPiedras().contains("Ascenso Draco"), "Falta Ascenso Draco.");
        check(!Generacion.GEN1.getPokemons().stream().anyMatch(PokemonBase::esMega),
                "Las formas Mega no deben aparecer en encuentros normales.");
    }

    private static void probarSexoYCompatibilidad() {
        PokemonBase bulbasaur = required("Bulbasaur");
        PokemonBase charmander = required("Charmander");
        PokemonBase ditto = required("Ditto");
        PokemonLuchador madre = new PokemonLuchador(bulbasaur, 10, new int[]{0, 0, 0, 0, 0, 0}, SexoPokemon.HEMBRA);
        PokemonLuchador padre = new PokemonLuchador(charmander, 10, new int[]{31, 31, 31, 31, 31, 31}, SexoPokemon.MACHO);
        PokemonLuchador otroMacho = new PokemonLuchador(charmander, 10, new int[]{1, 1, 1, 1, 1, 1}, SexoPokemon.MACHO);
        PokemonLuchador dittoP = new PokemonLuchador(ditto, 10, new int[]{3, 3, 3, 3, 3, 3}, SexoPokemon.AMBOS);
        check(SistemaCrianza.sonCompatibles(madre, padre), "Bulbasaur y Charmander deben ser compatibles por grupo huevo.");
        check(!SistemaCrianza.sonCompatibles(madre, otroMacho), "Dos Pokémon del mismo sexo no deben criar.");
        check(SistemaCrianza.sonCompatibles(dittoP, otroMacho), "Ditto debe criar con un Pokémon macho.");
        check(!SistemaCrianza.sonCompatibles(dittoP,
                new PokemonLuchador(ditto, 10, new int[]{1, 1, 1, 1, 1, 1}, SexoPokemon.AMBOS)),
                "Dos Ditto no deben criar.");
        check(dittoP.getSexo() == SexoPokemon.AMBOS, "Ditto debe tener sexo especial AMBOS.");
    }

    private static void probarCrianzaEIVs() {
        PokemonBase bulbasaur = required("Bulbasaur");
        PokemonBase charmander = required("Charmander");
        int[] ivMadre = {0, 10, 20, 30, 31, 1};
        int[] ivPadre = {10, 20, 30, 31, 0, 31};
        PokemonLuchador madre = new PokemonLuchador(bulbasaur, 20, ivMadre, SexoPokemon.HEMBRA);
        PokemonLuchador padre = new PokemonLuchador(charmander, 20, ivPadre, SexoPokemon.MACHO);
        Jugador jugador = new Jugador("SmokeTest", madre, 0);
        jugador.agregarPokemon(padre);

        int anteriores = jugador.getEquipo().size();
        boolean creado = SistemaCrianza.criar(new Scanner("1\n2\n"), jugador);
        check(creado, "La pareja de prueba debería criar correctamente.");
        check(jugador.getEquipo().size() == anteriores + 1, "La cría no se añadió al equipo.");
        PokemonLuchador cria = jugador.getEquipo().get(jugador.getEquipo().size() - 1);
        check(cria.getNombre().equalsIgnoreCase("Bulbasaur"), "La cría debe corresponder a la especie base de la madre.");
        check(Arrays.equals(cria.getIVs(), new int[]{5, 15, 25, 31, 16, 16}),
                "Los IV de la cría deben ser la media redondeada: " + Arrays.toString(cria.getIVs()));
        check(cria.getNivel() == 1, "La cría debe empezar al nivel 1.");
        check(madre.getProximaCrianzEpochMs() == padre.getProximaCrianzEpochMs(),
                "Los dos padres deben compartir el mismo instante de fin de cooldown.");
        check(madre.getProximaCrianzEpochMs() - System.currentTimeMillis() <= SistemaCrianza.COOLDOWN_MS
                && madre.getProximaCrianzEpochMs() > System.currentTimeMillis(),
                "Los padres deben entrar en cooldown de 30 minutos reales.");
    }

    private static void probarCooldown() {
        PokemonLuchador padre = new PokemonLuchador(required("Bulbasaur"), 10,
                new int[]{4, 5, 6, 7, 8, 9}, SexoPokemon.MACHO);
        long fin = System.currentTimeMillis() + SistemaCrianza.COOLDOWN_MS;
        padre.setProximaCrianzEpochMs(fin);
        check(!padre.puedeCriarAhora(), "El cooldown futuro debe impedir la crianza.");
        padre.setProximaCrianzEpochMs(System.currentTimeMillis() - 1);
        check(padre.puedeCriarAhora(), "El cooldown vencido debe permitir criar.");
    }

    private static void probarMegaEnCombate() {
        PokemonBase charizard = required("Charizard");
        PokemonLuchador pokemon = new PokemonLuchador(charizard, 50,
                new int[]{16, 20, 18, 28, 15, 24}, SexoPokemon.MACHO);
        pokemon.setMegapiedra("Charizardite X");
        int ataqueOriginal = pokemon.getAtaque();
        pokemon.iniciarCombate();
        MegaEvoluciones.Forma forma = MegaEvoluciones.paraEspecie("Charizard").stream()
                .filter(f -> f.nombreForma().equals("Charizard-Mega-X")).findFirst()
                .orElseThrow(() -> new AssertionError("Falta Mega-Charizard X."));
        check(pokemon.megaevolucionar(MegaEvoluciones.crearBase(charizard, forma)),
                "Charizard con Charizardite X debe megaevolucionar a X.");
        check(pokemon.getNombre().equals("Charizard-Mega-X"), "No cambió a la forma Mega.");
        check(!pokemon.puedeMegaEvolucionarEsteCombate(), "No debe poder megaevolucionar dos veces en el mismo combate.");
        pokemon.desmegaevolucionar();
        check(pokemon.getNombre().equals("Charizard"), "Debe volver a la forma normal al terminar el combate.");
        check(pokemon.getAtaque() == ataqueOriginal, "Los stats deben restaurarse al desmegaevolucionar.");
    }

    private static PokemonBase required(String nombre) {
        PokemonBase base = Pokedex.buscarPorNombre(nombre);
        if (base == null) throw new AssertionError("No existe en la Pokédex: " + nombre);
        return base;
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
