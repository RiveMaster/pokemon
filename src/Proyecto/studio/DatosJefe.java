package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/** El jefe de la sede del Gobierno de una generación (uno por generación). */
public record DatosJefe(
        int generacion,
        String nombre,
        String cargo,
        List<TipoPokemon> tipos,
        int nivelBase,
        String fraseDerrota,
        List<String> memes) {

    public DatosJefe {
        Objects.requireNonNull(nombre, "nombre");
        if (generacion < 1) throw new IllegalArgumentException("Generación no válida: " + generacion);
        cargo = cargo == null ? "" : cargo;
        fraseDerrota = fraseDerrota == null ? "" : fraseDerrota;
        tipos = tipos == null ? List.of() : List.copyOf(tipos);
        memes = memes == null ? List.of() : List.copyOf(memes);
    }
}
