package Proyecto.studio;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Los 18 tipos (generación 6+). {@code clave} es el db_symbol de Pokémon Studio. */
public enum TipoPokemon {
    NORMAL("normal", "Normal"),
    FUEGO("fire", "Fuego"),
    AGUA("water", "Agua"),
    ELECTRICO("electric", "Eléctrico"),
    PLANTA("grass", "Planta"),
    HIELO("ice", "Hielo"),
    LUCHA("fighting", "Lucha"),
    VENENO("poison", "Veneno"),
    TIERRA("ground", "Tierra"),
    VOLADOR("flying", "Volador"),
    PSIQUICO("psychic", "Psíquico"),
    BICHO("bug", "Bicho"),
    ROCA("rock", "Roca"),
    FANTASMA("ghost", "Fantasma"),
    DRAGON("dragon", "Dragón"),
    SINIESTRO("dark", "Siniestro"),
    ACERO("steel", "Acero"),
    HADA("fairy", "Hada");

    private final String clave;
    private final String nombre;

    TipoPokemon(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }

    /** db_symbol de Studio: "fire", "water"... */
    public String clave() { return clave; }

    public String nombre() { return nombre; }

    /** Texto con el formato que usa el resto del proyecto: "Fire", "Water"... */
    public String nombreProyecto() {
        return Character.toUpperCase(clave.charAt(0)) + clave.substring(1);
    }

    // Erratas que ya existen en los datos del proyecto original.
    private static final Map<String, String> ALIAS = Map.of("phsyc", "psychic");

    /** Acepta clave inglesa, nombre español, con o sin tildes/mayúsculas y las erratas conocidas. */
    public static Optional<TipoPokemon> buscar(String texto) {
        if (texto == null) return Optional.empty();
        String n = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT).trim();
        n = ALIAS.getOrDefault(n, n);
        for (TipoPokemon t : values()) {
            if (t.clave.equals(n) || sinTildes(t.nombre).equals(n) || t.name().toLowerCase(Locale.ROOT).equals(n))
                return Optional.of(t);
        }
        return Optional.empty();
    }

    private static String sinTildes(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
