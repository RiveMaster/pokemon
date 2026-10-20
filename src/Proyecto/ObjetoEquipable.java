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
    PIEDRA_DURA("Piedra Dura", "Aumenta un 20% el daño de los ataques de tipo Roca.", 350, "Rock"),
    PESA_REcia("Pesa recia (IV PS)", "Al criar, fuerza que el IV de PS se herede del Pokémon que la lleva.", 1800, null, 0),
    BRAZAL_RECIO("Brazal recio (IV Ataque)", "Al criar, fuerza que el IV de Ataque se herede del Pokémon que lo lleva.", 1800, null, 1),
    CINTA_RECIA("Cinto recio (IV Defensa)", "Al criar, fuerza que el IV de Defensa se herede del Pokémon que lo lleva.", 1800, null, 2),
    LENTE_RECIA("Lente recia (IV At. Esp.)", "Al criar, fuerza que el IV de Ataque Especial se herede del Pokémon que la lleva.", 1800, null, 3),
    BANDA_RECIA("Banda recia (IV Def. Esp.)", "Al criar, fuerza que el IV de Defensa Especial se herede del Pokémon que la lleva.", 1800, null, 4),
    FRANJA_RECIA("Franja recia (IV Velocidad)", "Al criar, fuerza que el IV de Velocidad se herede del Pokémon que la lleva.", 1800, null, 5);

    private final String nombre;
    private String descripcion;
    private int precio;
    private final String tipoPotenciado; // tipo de ataque que refuerza (null si no aplica)
    private final int ivHeredado; // -1 si no modifica la crianza; 0 PS, 1 At., 2 Def., 3 At.Esp., 4 Def.Esp., 5 Vel.

    ObjetoEquipable(String nombre, String descripcion, int precio, String tipoPotenciado) {
        this(nombre, descripcion, precio, tipoPotenciado, -1);
    }

    ObjetoEquipable(String nombre, String descripcion, int precio, String tipoPotenciado, int ivHeredado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoPotenciado = tipoPotenciado;
        this.ivHeredado = ivHeredado;
    }

    /** Lo usa el cargador de datos de Studio (el nombre y el efecto no cambian: están en el código de combate). */
    public void configurar(String descripcion, int precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }

    public String getDescripcion() { return descripcion; }

    public int getPrecio() { return precio; }

    public String getTipoPotenciado() { return tipoPotenciado; }

    /** Índice de IV que este objeto fuerza a heredar, o -1 si no tiene efecto de crianza. */
    public int getIvHeredado() { return ivHeredado; }

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
