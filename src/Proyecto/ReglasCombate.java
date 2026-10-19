package Proyecto;

import java.util.Random;

/**
 * Reglas opcionales del combate. TODAS están desactivadas por defecto: el combate original no las tenía.
 * El cargador de datos de Studio las activa según reglas.json.
 * <ul>
 *   <li>efectos: los movimientos aplican su estado alterado (dormido, quemado...) con su probabilidad.</li>
 *   <li>precision: los movimientos pueden fallar según su precisión.</li>
 *   <li>pp: cada movimiento tiene PP; sin PP se usa Forcejeo. El Centro Pokémon los restaura.</li>
 *   <li>estadisticas: el daño depende de la categoría (físico/especial) y de ataque/defensa.</li>
 *   <li>habilidades: las habilidades de los Pokémon tienen efecto.</li>
 * </ul>
 */
public final class ReglasCombate {
    private static boolean efectos, precision, pp, estadisticas, habilidades;
    private static Random azar = new Random();

    private ReglasCombate() {}

    public static void configurar(boolean efectos, boolean precision, boolean pp, boolean estadisticas, boolean habilidades) {
        ReglasCombate.efectos = efectos;
        ReglasCombate.precision = precision;
        ReglasCombate.pp = pp;
        ReglasCombate.estadisticas = estadisticas;
        ReglasCombate.habilidades = habilidades;
    }

    public static boolean efectos() { return efectos; }
    public static boolean precision() { return precision; }
    public static boolean pp() { return pp; }
    public static boolean estadisticas() { return estadisticas; }
    public static boolean habilidades() { return habilidades; }

    /** Generador de números aleatorios del combate (se puede fijar con una semilla en las pruebas). */
    public static Random azar() { return azar; }
    public static void azar(Random nuevo) { azar = nuevo; }
}
