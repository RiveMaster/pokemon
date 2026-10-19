package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/** Un movimiento. Equivale a un "move" de Pokémon Studio. */
public record DatosMovimiento(
        int id,
        String simbolo,
        String nombre,
        TipoPokemon tipo,
        CategoriaMovimiento categoria,
        double potencia,
        int precision,          // 0..100 (0 = nunca falla)
        int pp,
        int prioridad,
        DatosEfecto efecto,     // puede ser null
        List<String> banderas) {

    public DatosMovimiento {
        Objects.requireNonNull(simbolo, "simbolo");
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(tipo, "tipo");
        Objects.requireNonNull(categoria, "categoria");
        banderas = banderas == null ? List.of() : List.copyOf(banderas);
    }
}
