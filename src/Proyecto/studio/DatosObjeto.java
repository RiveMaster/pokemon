package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/** Un objeto. Equivale a un "item" de Pokémon Studio. */
public record DatosObjeto(int id, String simbolo, String nombre, String bolsillo, int precio,
                          String descripcion, List<String> banderas) {
    public DatosObjeto {
        Objects.requireNonNull(simbolo, "simbolo");
        Objects.requireNonNull(nombre, "nombre");
        bolsillo = bolsillo == null ? "otros" : bolsillo;
        descripcion = descripcion == null ? "" : descripcion;
        banderas = banderas == null ? List.of() : List.copyOf(banderas);
        if (precio < 0) throw new IllegalArgumentException("Precio negativo: " + precio);
    }
}
