package Proyecto.studio;

import java.util.Objects;

/**
 * Una "generación" del juego: qué rango de la Pokédex nacional sale, y cuánto sube el nivel del mundo.
 * {@code desde}/{@code hasta} son números de especie (ids de {@link DatosEspecie}), ambos incluidos.
 */
public record DatosGeneracion(int numero, String region, int desde, int hasta, String ambiente, int bonusNivel) {
    public DatosGeneracion {
        Objects.requireNonNull(region, "region");
        ambiente = ambiente == null ? "" : ambiente;
        if (numero < 1) throw new IllegalArgumentException("Número de generación no válido: " + numero);
        if (desde < 1 || hasta < desde) throw new IllegalArgumentException("Rango de Pokédex no válido: " + desde + ".." + hasta);
    }
}
