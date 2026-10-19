package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/** Una especie de Pokémon: identificador, nombre y sus formas. Equivale a un "pokemon" de Studio. */
public record DatosEspecie(int id, String simbolo, String nombre, List<DatosForma> formas) {
    public DatosEspecie {
        Objects.requireNonNull(simbolo, "simbolo");
        Objects.requireNonNull(nombre, "nombre");
        formas = formas == null ? List.of() : List.copyOf(formas);
        if (formas.isEmpty()) throw new IllegalArgumentException("La especie '" + simbolo + "' necesita al menos una forma");
    }

    /** La forma con ese número, o la primera si no existe. */
    public DatosForma forma(int numero) {
        for (DatosForma f : formas) if (f.forma() == numero) return f;
        return formas.get(0);
    }

    public DatosEspecie conFormas(List<DatosForma> nuevas) {
        return new DatosEspecie(id, simbolo, nombre, nuevas);
    }
}
