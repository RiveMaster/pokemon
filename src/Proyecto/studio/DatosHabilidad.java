package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/**
 * Una habilidad. Equivale a un "ability" de Pokémon Studio. Si la regla de habilidades del combate está activada,
 * {@code efecto} le da un efecto real en combate:
 * <ul>
 *   <li>ninguno: solo informativa.</li>
 *   <li>potencia_tipo: con la vida al tercio o menos, los ataques de {@code tipo} hacen daño x {@code valor} (1.5).</li>
 *   <li>inmunidad_tipo: no recibe daño de los ataques de {@code tipo}.</li>
 *   <li>contacto_estado: al recibir un golpe, {@code valor} % de dejar al atacante en {@code estado}.</li>
 *   <li>regeneracion: al empezar cada turno recupera 1/{@code valor} de su vida máxima (16).</li>
 * </ul>
 */
public record DatosHabilidad(String simbolo, String nombre, String descripcion, String efecto, TipoPokemon tipo,
                             String estado, double valor) {
    public static final List<String> EFECTOS =
            List.of("ninguno", "potencia_tipo", "inmunidad_tipo", "contacto_estado", "regeneracion");

    public DatosHabilidad {
        Objects.requireNonNull(simbolo, "simbolo");
        Objects.requireNonNull(nombre, "nombre");
        descripcion = descripcion == null ? "" : descripcion;
        efecto = efecto == null ? "ninguno" : efecto;
        if (valor < 0) throw new IllegalArgumentException("El valor de la habilidad no puede ser negativo: " + valor);
    }

    /** Habilidad solo informativa (sin efecto en combate). */
    public DatosHabilidad(String simbolo, String nombre, String descripcion) {
        this(simbolo, nombre, descripcion, "ninguno", null, null, 0);
    }
}
