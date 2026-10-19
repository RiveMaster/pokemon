package Proyecto.studio;

import java.util.Objects;

/** Efecto secundario de un movimiento: estado alterado con cierta probabilidad y/o daño extra. */
public record DatosEfecto(String nombre, double dano, String estado, int probabilidad) {
    /** Estados alterados que entiende el juego (nombres de Proyecto.EstadoAlterado, sin NORMAL). */
    public static final java.util.List<String> ESTADOS =
            java.util.List.of("DORMIDO", "PARALIZADO", "ENVENENADO", "QUEMADO", "CONGELADO", "INSTA", "CARGAR");

    public DatosEfecto {
        Objects.requireNonNull(nombre, "nombre");
        if (probabilidad < 0 || probabilidad > 100)
            throw new IllegalArgumentException("Probabilidad fuera de 0..100: " + probabilidad);
    }
}
