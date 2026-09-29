package Proyecto;

import java.text.Normalizer;

/**
 * Objetos que un Pokémon puede llevar equipados (uno por Pokémon).
 * Los efectos se aplican en PokemonLuchador (atacarCon, recibirAtaque y
 * efectoObjetoInicioTurno), que Combate invoca durante los turnos.
 */
public enum ObjetoEquipable {
    RESTOS("Restos", "Recupera 1/16 de la vida máxima al inicio de cada turno.", 600, null),
    BAYA_ARANJA("Baya Aranja", "Cura 30 PS cuando la vida baja a la mitad o menos. Se consume.", 150, null),
    BANDA_FOCUS("Banda Focus", "Con la vida completa, aguanta con 1 PS un golpe que lo debilitaría. Se consume.", 500, null),
    CINTA_FUERTE("Cinta Fuerte", "Aumenta un 10% el daño de todos sus ataques.", 400, null),
    VIDASFERA("Vidasfera", "Aumenta un 30% el daño, pero pierde 1/10 de su vida al atacar.", 700, null),
    PETO_DURO("Peto Duro", "Reduce un 15% el daño recibido.", 450, null),
    CARBON("Carbón", "Aumenta un 20% el daño de los ataques de tipo Fuego.", 350, "Fire"),
    AGUA_MISTICA("Agua Mística", "Aumenta un 20% el daño de los ataques de tipo Agua.", 350, "Water"),
    SEMILLA_MILAGRO("Semilla Milagro", "Aumenta un 20% el daño de los ataques de tipo Planta.", 350, "Grass"),
    IMAN("Imán", "Aumenta un 20% el daño de los ataques de tipo Eléctrico.", 350, "Electric"),
    PIEDRA_DURA("Piedra Dura", "Aumenta un 20% el daño de los ataques de tipo Roca.", 350, "Rock");

    private final String nombre;
    private final String descripcion;
    private final int precio;
    private final String tipoPotenciado; // tipo de ataque que refuerza (null si no aplica)

    ObjetoEquipable(String nombre, String descripcion, int precio, String tipoPotenciado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoPotenciado = tipoPotenciado;
    }

    public String getNombre() { return nombre; }

    public String getDescripcion() { return descripcion; }

    public int getPrecio() { return precio; }

    public String getTipoPotenciado() { return tipoPotenciado; }

    /** Busca por nombre visible o interno, sin distinguir mayúsculas ni tildes. */
    public static ObjetoEquipable buscarPorNombre(String texto) {
        if (texto == null) return null;
        String buscado = normalizar(texto);
        for (ObjetoEquipable o : values()) {
            if (normalizar(o.nombre).equals(buscado) || normalizar(o.name()).equals(buscado)) {
                return o;
            }
        }
        return null;
    }

    private static String normalizar(String s) {
        String sinTildes = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.trim().toLowerCase().replace('_', ' ');
    }
}
