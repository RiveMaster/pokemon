package Proyecto;

import java.util.HashMap;
import java.util.Map;

/**
 * Habilidades de los Pokémon con efecto en combate (solo si la regla de habilidades está activada).
 * Efectos disponibles:
 * <ul>
 *   <li>potencia_tipo: con la vida al tercio o menos, los ataques del tipo hacen daño x valor (p. ej. 1.5).</li>
 *   <li>inmunidad_tipo: no recibe daño de los ataques de ese tipo.</li>
 *   <li>contacto_estado: al recibir un golpe, valor % de probabilidad de dejar al atacante con ese estado.</li>
 *   <li>regeneracion: al empezar cada turno recupera 1/valor de su vida máxima (p. ej. 16).</li>
 * </ul>
 */
public final class Habilidades {
    public record Definicion(String nombre, String descripcion, String efecto, String tipo, String estado, double valor) {}

    private static Map<String, Definicion> definiciones = new HashMap<>();

    private Habilidades() {}

    public static void reemplazar(Map<String, Definicion> nuevas) { definiciones = new HashMap<>(nuevas); }

    public static Definicion de(String simbolo) { return simbolo == null ? null : definiciones.get(simbolo); }

    public static Map<String, Definicion> todas() { return java.util.Collections.unmodifiableMap(definiciones); }
}
