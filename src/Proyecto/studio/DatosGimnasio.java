package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/**
 * Un gimnasio: 3 miembros que hay que vencer en orden y después un líder. Equivale a un "dex/gym" de Studio.
 * Los equipos se forman al vuelo con Pokémon de {@code tiposEquipo} de la generación en curso.
 */
public record DatosGimnasio(
        String simbolo,
        String nombre,
        String ciudad,                 // símbolo de la DatosZona donde está (una ciudad, un gimnasio)
        List<TipoPokemon> tiposEquipo,
        String lider,
        String tituloLider,
        List<String> miembros,
        String medalla,
        int recompensaDinero,
        String mtRecompensa,           // símbolo del movimiento que da como MT
        int nivelBase,
        int tamanoEquipo) {

    public DatosGimnasio {
        Objects.requireNonNull(simbolo, "simbolo");
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(ciudad, "ciudad");
        Objects.requireNonNull(lider, "lider");
        tiposEquipo = tiposEquipo == null ? List.of() : List.copyOf(tiposEquipo);
        miembros = miembros == null ? List.of() : List.copyOf(miembros);
        tituloLider = tituloLider == null ? "" : tituloLider;
        if (tamanoEquipo < 1 || tamanoEquipo > 6)
            throw new IllegalArgumentException("El equipo del líder debe tener de 1 a 6 Pokémon: " + tamanoEquipo);
    }

    /** Tipo principal del gimnasio (el primero de su equipo). */
    public TipoPokemon tipo() { return tiposEquipo.isEmpty() ? null : tiposEquipo.get(0); }
}
