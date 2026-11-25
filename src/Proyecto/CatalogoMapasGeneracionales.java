package Proyecto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Atlas de 100 escenarios diferentes: diez mapas originales para cada una
 * de las diez generaciones. Los índices son 0..9 y no alteran Ubicacion,
 * los guardados antiguos ni el orden del recorrido principal.
 */
public final class CatalogoMapasGeneracionales {
    private static final String[] BIOMAS = {
        "PUEBLO", "BOSQUE", "COSTA", "MONTANA", "LAGO",
        "RUINAS", "CIUDAD", "PANTANO", "CUEVA", "JARDIN"
    };
    private static final String[][] NOMBRES = {
        {"Villa Brote","Sendero Verde","Bahia Clara","Sierra Granito","Lago Aurora",
         "Ruinas del Eco","Ciudad Carmesi","Humedal Sauce","Gruta Brillante","Jardin Malva"},
        {"Villa Campana","Bosque Cedro","Costa Cobre","Cumbre de Bronce","Lago Espejo",
         "Santuario Antiguo","Ciudad Linterna","Pantano Junco","Caverna Musgo","Jardin del Alba"},
        {"Villa Brisa","Selva Esmeralda","Playa Zafiro","Picos del Vapor","Laguna Coral",
         "Altar de la Lluvia","Puerto Turquesa","Manglar Salino","Tuneles Magma","Jardin Lluvia"},
        {"Villa Escarcha","Arboleda Nevada","Costa Glaciar","Cumbres Blancas","Lago Diamante",
         "Templo Neblina","Ciudad Cristal","Tundra Azul","Cueva del Hielo","Jardin Boreal"},
        {"Villa Horizonte","Bosque de Otono","Costa Obsidiana","Meseta Industrial","Lago Central",
         "Ruinas Electra","Ciudad Ferrea","Cienaga Acero","Galeria de Cristal","Jardin Metropolitano"},
        {"Villa Lavanda","Bosque de Hadas","Costa Azulada","Sierra de Cuarzo","Lago Perfume",
         "Palacio Velado","Ciudad Jazmin","Humedal Lirio","Gruta de Cristal","Jardin de Rosas"},
        {"Villa Palmeras","Selva Tropical","Playa Atolon","Volcan Dorado","Laguna Sol",
         "Templo de Arena","Ciudad Isla","Manglar de Coral","Cueva del Volcan","Jardin Hibisco"},
        {"Villa Carbon","Bosque Brezal","Costa Brumosa","Colinas del Norte","Lago Tormenta",
         "Fortaleza de Piedra","Ciudad Estadio","Pantano Turba","Minas Antiguas","Jardin Campestre"},
        {"Villa Naranja","Bosque Encina","Costa Ocre","Sierra Abierta","Lago Meseta",
         "Ruinas Solana","Ciudad Terraza","Marisma Roja","Cueva del Viento","Jardin Naranjo"},
        {"Villa Prisma","Bosque Iris","Costa Estelar","Cumbre Celeste","Lago Resonancia",
         "Ruinas Eternas","Ciudad Nexo","Pantano Lumen","Caverna Prisma","Jardin del Tiempo"}
    };

    public record Mapa(Generacion generacion, int indice, String id,
                       String nombre, String bioma, long semilla) {
        public String etiqueta() {
            String prefijo = id.startsWith("NUEVO_") ? "NUEVO · " :
                    String.format("%02d/10 · ", indice + 1);
            return prefijo + nombre + " · " + bioma.toLowerCase(java.util.Locale.ROOT);
        }
        public boolean urbano() {
            return bioma.equals("PUEBLO") || bioma.equals("CIUDAD");
        }
    }

    /**
     * Veinte lugares NUEVOS, dos por generación. No se alteran los cien IDs
     * originales ni los índices 0..9 que ya pueden existir en partidas guardadas.
     * La distribución específica de estos escenarios está en MapasRegion2D.
     */
    private static final String[][] NOMBRES_NUEVOS = {
        {"Villa Encina", "Vereda del Olivar"},
        {"Pueblo Campanario", "Senda del Hayedo"},
        {"Villa Azahar", "Sendero de la Bruma"},
        {"Villa del Hielo", "Paso del Abedul"},
        {"Pueblo Forja", "Ruta del Nogal"},
        {"Villa de los Vientos", "Camino de las Rosas"},
        {"Puerto Palma", "Sendero Volcánico"},
        {"Villa Robledal", "Ruta de Brezos"},
        {"Pueblo Terracota", "Camino del Almendro"},
        {"Villa Alba", "Vereda de Cristal"}
    };

    private CatalogoMapasGeneracionales() { }

    public static Mapa obtener(Generacion generacion, int indice) {
        if (generacion == null || indice < 0 || indice >= 10)
            throw new IllegalArgumentException("Generación o mapa inválido");
        int g = generacion.getNumero() - 1;
        long semilla = 4171L * generacion.getNumero()
                + 7139L * (indice + 1) + 101L * g * (indice + 3);
        return new Mapa(generacion, indice,
                String.format("REGION_%02d_%02d", generacion.getNumero(), indice + 1),
                NOMBRES[g][indice], BIOMAS[indice], semilla);
    }

    public static List<Mapa> listar(Generacion generacion) {
        List<Mapa> mapas = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) mapas.add(obtener(generacion, i));
        return Collections.unmodifiableList(mapas);
    }

    public static List<Mapa> todos() {
        List<Mapa> mapas = new ArrayList<>(100);
        for (Generacion generacion : Generacion.values()) mapas.addAll(listar(generacion));
        return Collections.unmodifiableList(mapas);
    }

    /** Dos nuevos escenarios por generación sin reescribir los 100 anteriores. */
    public static List<Mapa> nuevos(Generacion generacion) {
        if (generacion == null) throw new IllegalArgumentException("Generación requerida");
        int g = generacion.getNumero() - 1;
        List<Mapa> resultado = new ArrayList<>(2);
        for (int i=0; i<2; i++) {
            long semilla = 982451653L + generacion.getNumero()*92117L + i*7177L;
            resultado.add(new Mapa(generacion, 10+i,
                    String.format("NUEVO_%02d_%02d", generacion.getNumero(),i+1),
                    NOMBRES_NUEVOS[g][i], i==0 ? "PUEBLO" : "BOSQUE",semilla));
        }
        return Collections.unmodifiableList(resultado);
    }

    public static List<Mapa> todosConNuevos() {
        List<Mapa> mapas = new ArrayList<>(120);
        for (Generacion generacion : Generacion.values()) {
            mapas.addAll(listar(generacion));
            mapas.addAll(nuevos(generacion));
        }
        return Collections.unmodifiableList(mapas);
    }
}
