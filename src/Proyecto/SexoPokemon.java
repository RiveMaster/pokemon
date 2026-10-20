package Proyecto;

/** Sexo individual de un Pokémon. Ditto usa AMBOS porque puede criar con cualquiera de los dos sexos. */
public enum SexoPokemon {
    MACHO("Macho"),
    HEMBRA("Hembra"),
    AMBOS("Ambos (Ditto)"),
    SIN_GENERO("Sin género");

    private final String etiqueta;
    SexoPokemon(String etiqueta) { this.etiqueta = etiqueta; }
    @Override public String toString() { return etiqueta; }

    public boolean compatibleCon(SexoPokemon otro) {
        if (this == AMBOS || otro == AMBOS) return true;
        return (this == MACHO && otro == HEMBRA) || (this == HEMBRA && otro == MACHO);
    }
}
