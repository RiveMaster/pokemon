package Proyecto.studio;

/** Categoría de un movimiento. {@code clave} coincide con la de Pokémon Studio. */
public enum CategoriaMovimiento {
    FISICO("physical"),
    ESPECIAL("special"),
    ESTADO("status");

    private final String clave;

    CategoriaMovimiento(String clave) { this.clave = clave; }

    public String clave() { return clave; }

    /** Nombre para mostrar. */
    public String nombre() {
        return switch (this) { case FISICO -> "Físico"; case ESPECIAL -> "Especial"; case ESTADO -> "Estado"; };
    }

    public static CategoriaMovimiento desdeClave(String c) {
        for (CategoriaMovimiento x : values()) if (x.clave.equalsIgnoreCase(c) || x.name().equalsIgnoreCase(c)) return x;
        throw new IllegalArgumentException("Categoría de movimiento desconocida: " + c);
    }
}
