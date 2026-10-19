package Proyecto.studio;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

/**
 * Evolución hacia {@code destino} (db_symbol de la especie). Las condiciones son pares clave/valor
 * flexibles, como en Studio: "nivel_minimo", "objeto", "felicidad", "intercambio"...
 */
public record DatosEvolucion(String destino, Map<String, String> condiciones) {
    public static final String NIVEL_MINIMO = "nivel_minimo";
    public static final String OBJETO = "objeto";

    public DatosEvolucion {
        Objects.requireNonNull(destino, "destino");
        condiciones = condiciones == null
                ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(condiciones));
    }

    public static DatosEvolucion porNivel(String destino, int nivel) {
        return new DatosEvolucion(destino, Map.of(NIVEL_MINIMO, Integer.toString(nivel)));
    }

    public OptionalInt nivelMinimo() {
        String v = condiciones.get(NIVEL_MINIMO);
        return v == null ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(v));
    }
}
