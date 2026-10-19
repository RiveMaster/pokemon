package Proyecto;

/**
 * Eficacia de tipos en combate. Está DESACTIVADA por defecto (el combate original ignora los tipos);
 * el cargador de datos de Studio la activa si la regla «eficacia de tipos» está marcada, y le da la tabla.
 */
public final class Tipos {
    /** Multiplicador de un tipo de ataque contra un tipo defensor (nombres como "Fire", "Water"...). */
    public interface Eficacia {
        double de(String tipoAtaque, String tipoDefensor);
    }

    private static Eficacia eficacia = null; // null = desactivada

    private Tipos() {}

    public static void configurar(Eficacia nueva) { eficacia = nueva; }

    public static boolean activa() { return eficacia != null; }

    /** Multiplicador contra un Pokémon de uno o dos tipos; 1.0 si la eficacia está desactivada. */
    public static double multiplicador(String tipoAtaque, String tipo1, String tipo2) {
        if (eficacia == null || tipoAtaque == null) return 1.0;
        double m = tipo1 == null ? 1.0 : eficacia.de(tipoAtaque, tipo1);
        if (tipo2 != null) m *= eficacia.de(tipoAtaque, tipo2);
        return m;
    }

    /** Mensaje de combate para ese multiplicador (cadena vacía si es neutro). */
    public static String mensaje(double m, String objetivo) {
        if (m == 0) return "No afecta a " + objetivo + "...";
        if (m > 1) return "¡Es muy eficaz!";
        if (m < 1) return "No es muy eficaz...";
        return "";
    }
}
