package Proyecto.studio;

import java.util.List;
import java.util.Objects;

/**
 * Un lugar del mapa. Equivale a una "zona" de Pokémon Studio.
 * <ul>
 *   <li>RUTA: {@code tipos} = tipos de Pokémon salvajes; nivel mín./máx.; {@code clasesEntrenador} = NPC que pueden retar.</li>
 *   <li>POBLACION: {@code tipos} = tipos que se ven por las calles; casino / sede del Gobierno.</li>
 * </ul>
 * El camino es lineal: {@code anterior} y {@code siguiente} son símbolos de otras zonas (null en los extremos).
 */
public record DatosZona(
        String simbolo,
        String nombre,
        ClaseZona clase,
        String descripcion,
        List<TipoPokemon> tipos,
        int nivelMin,
        int nivelMax,
        String anterior,
        String siguiente,
        String puebloCercano,          // dónde despiertas si pierdes (solo rutas)
        List<String> clasesEntrenador,
        boolean casino,
        boolean sedeGobierno) {

    public DatosZona {
        Objects.requireNonNull(simbolo, "simbolo");
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(clase, "clase");
        descripcion = descripcion == null ? "" : descripcion;
        tipos = tipos == null ? List.of() : List.copyOf(tipos);
        clasesEntrenador = clasesEntrenador == null ? List.of() : List.copyOf(clasesEntrenador);
        if (nivelMin < 0 || nivelMax < nivelMin)
            throw new IllegalArgumentException("Rango de niveles no válido en '" + simbolo + "': " + nivelMin + ".." + nivelMax);
    }

    public DatosZona conAnterior(String nuevo) {
        return new DatosZona(simbolo, nombre, clase, descripcion, tipos, nivelMin, nivelMax, nuevo, siguiente,
                puebloCercano, clasesEntrenador, casino, sedeGobierno);
    }

    public DatosZona conSiguiente(String nuevo) {
        return new DatosZona(simbolo, nombre, clase, descripcion, tipos, nivelMin, nivelMax, anterior, nuevo,
                puebloCercano, clasesEntrenador, casino, sedeGobierno);
    }

    public DatosZona conPuebloCercano(String nuevo) {
        return new DatosZona(simbolo, nombre, clase, descripcion, tipos, nivelMin, nivelMax, anterior, siguiente,
                nuevo, clasesEntrenador, casino, sedeGobierno);
    }
}
