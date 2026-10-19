package Proyecto.studio;

/** Tipo de lugar del mapa. */
public enum ClaseZona {
    INICIO("start"),       // el pueblo de partida (menú propio)
    RUTA("route"),         // camino con Pokémon salvajes y entrenadores NPC
    POBLACION("town");     // ciudad o pueblo con Centro Pokémon, tienda, gimnasio...

    private final String clave;

    ClaseZona(String clave) { this.clave = clave; }

    public String clave() { return clave; }

    /** Nombre para mostrar. */
    public String nombre() {
        return switch (this) { case INICIO -> "Inicio"; case RUTA -> "Ruta"; case POBLACION -> "Población"; };
    }

    public static ClaseZona desdeClave(String c) {
        for (ClaseZona x : values()) if (x.clave.equalsIgnoreCase(c) || x.name().equalsIgnoreCase(c)) return x;
        throw new IllegalArgumentException("Clase de zona desconocida: " + c);
    }
}
