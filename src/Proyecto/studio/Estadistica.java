package Proyecto.studio;

/** Las seis estadísticas. {@code clave} coincide con las de Pokémon Studio (hp, atk, dfe, spd, ats, dfs). */
public enum Estadistica {
    VIDA("hp"),
    ATAQUE("atk"),
    DEFENSA("dfe"),
    VELOCIDAD("spd"),
    ATAQUE_ESP("ats"),
    DEFENSA_ESP("dfs");

    private final String clave;

    Estadistica(String clave) { this.clave = clave; }

    public String clave() { return clave; }

    public static Estadistica desdeClave(String c) {
        for (Estadistica e : values()) if (e.clave.equalsIgnoreCase(c) || e.name().equalsIgnoreCase(c)) return e;
        throw new IllegalArgumentException("Estadística desconocida: " + c);
    }
}
