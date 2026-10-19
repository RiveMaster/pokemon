package Proyecto.studio;

/**
 * Las seis curvas de experiencia. {@code id} es el índice que usa Pokémon Studio (experience_type).
 * {@link #expParaNivel(int)} devuelve la experiencia total necesaria para alcanzar ese nivel.
 */
public enum CurvaExperiencia {
    RAPIDA_MEDIA(0),   // medium fast
    ERRATICA(1),       // erratic
    FLUCTUANTE(2),     // fluctuating
    PARABOLICA(3),     // medium slow
    RAPIDA(4),         // fast
    LENTA(5);          // slow

    private final int id;

    CurvaExperiencia(int id) { this.id = id; }

    public int id() { return id; }

    public static CurvaExperiencia desdeId(int id) {
        for (CurvaExperiencia c : values()) if (c.id == id) return c;
        throw new IllegalArgumentException("Curva de experiencia desconocida: " + id);
    }

    public long expParaNivel(int nivel) {
        if (nivel <= 1) return 0;
        long n = nivel;
        long n3 = n * n * n;
        long r = switch (this) {
            case RAPIDA_MEDIA -> n3;
            case RAPIDA -> 4 * n3 / 5;
            case LENTA -> 5 * n3 / 4;
            case PARABOLICA -> 6 * n3 / 5 - 15 * n * n + 100 * n - 140;
            case ERRATICA -> {
                if (n <= 50) yield n3 * (100 - n) / 50;
                if (n <= 68) yield n3 * (150 - n) / 100;
                if (n <= 98) yield n3 * ((1911 - 10 * n) / 3) / 500;
                yield n3 * (160 - n) / 100;
            }
            case FLUCTUANTE -> {
                if (n <= 15) yield n3 * (((n + 1) / 3) + 24) / 50;
                if (n <= 35) yield n3 * (n + 14) / 50;
                yield n3 * ((n / 2) + 32) / 50;
            }
        };
        return Math.max(0, r);
    }
}
