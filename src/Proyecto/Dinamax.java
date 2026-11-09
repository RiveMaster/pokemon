package Proyecto;

import java.util.Set;

/** Reglas básicas de Dinamax/Gigamax para los Pokémon con forma Gigamax oficial. */
public final class Dinamax {
    private static final Set<String> ESPECIES_GIGAMAX = Set.of(
            "Venusaur", "Charizard", "Blastoise", "Butterfree", "Pikachu", "Meowth", "Machamp", "Gengar",
            "Kingler", "Lapras", "Eevee", "Snorlax", "Garbodor", "Melmetal",
            "Rillaboom", "Cinderace", "Inteleon", "Corviknight", "Orbeetle",
            "Drednaw", "Coalossal", "Flapple", "Appletun", "Sandaconda",
            "Centiskorch", "Toxtricity", "Hatterene", "Grimmsnarl", "Alcremie", "Copperajah",
            "Duraludon", "Urshifu"
    );

    private Dinamax() { }

    public static boolean especieCompatible(String especie) {
        if (especie == null) return false;
        return ESPECIES_GIGAMAX.stream().anyMatch(n -> n.equalsIgnoreCase(especie.trim()));
    }

    public static boolean tieneBanda() {
        return Jugador.tieneObjetoClave("Banda Dinamax");
    }

    public static boolean puedeActivarse(PokemonLuchador pokemon) {
        return tieneBanda() && pokemon != null && pokemon.estaVivo()
                && !pokemon.estaEnGigamax() && especieCompatible(pokemon.getNombre());
    }

    public static boolean activar(PokemonLuchador pokemon) {
        return puedeActivarse(pokemon) && pokemon.activarGigamax();
    }
}
