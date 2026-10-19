package Proyecto.studio;

import java.util.Objects;

/** Un movimiento que se aprende al llegar a {@code nivel} (nivel 0 = lo sabe al nacer/capturarse). */
public record Aprendizaje(String movimiento, int nivel) {
    public Aprendizaje {
        Objects.requireNonNull(movimiento, "movimiento");
        if (nivel < 0 || nivel > 100) throw new IllegalArgumentException("Nivel fuera de rango: " + nivel);
    }
}
