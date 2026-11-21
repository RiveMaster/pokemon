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
            return String.format("%02d/10 · %s · %s", indice + 1, nombre,
                    bioma.toLowerCase(java.util.Locale.ROOT));
        }
        public boolean urbano() {
            return bioma.equals("PUEBLO") || bioma.equals("CIUDAD");
        }
    }

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
}
