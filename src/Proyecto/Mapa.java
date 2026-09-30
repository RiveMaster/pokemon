package Proyecto;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * El mapa del juego y su bucle principal: mira dónde está el jugador y ejecuta ese lugar,
 * que devuelve a dónde va después. Como parte siempre de jugador.getUbicacion(), sirve
 * igual para una partida nueva que para una cargada.
 *
 * Para añadir una ruta o ciudad: nuevo valor en Ubicacion + una línea aquí. Para poner un gimnasio
 * en una ciudad, añádelo en Gimnasio con esa ciudad (máximo uno por ciudad).
 */
public class Mapa {

    public interface Accion {
        Ubicacion ejecutar();
    }

    /** Opción de menú. Si su acción devuelve null, el jugador se queda donde está. */
    public static class Opcion {
        final String texto;
        final Accion accion;

        public Opcion(String texto, Accion accion) {
            this.texto = texto;
            this.accion = accion;
        }
    }

    private static final Map<Ubicacion, Zona> ZONAS = new EnumMap<>(Ubicacion.class);
    private static final Map<Ubicacion, Pueblo> PUEBLOS = new EnumMap<>(Ubicacion.class);

    static {
        // ---- Rutas: Zona(lugar, tipos que salen, nivel min, nivel max, descripción, anterior, siguiente, pueblo cercano, clases de NPC...)
        zona(new Zona(Ubicacion.RUTA1, Generacion.tipos("Normal", "Flying", "Bug"), 3, 5,
                "Hierba alta entre Villaverde y Nava.", Ubicacion.VILLAVERDE, Ubicacion.NAVA, Ubicacion.VILLAVERDE,
                "Niño explorador", "Campista", "Aficionado a los bichos"));
        zona(new Zona(Ubicacion.RUTA11, Generacion.tipos("Normal", "Grass", "Bug", "Fairy"), 4, 7,
                "Unas sendas rodeadas de manzanos, entre Nava y Oviedo.", Ubicacion.NAVA, Ubicacion.OVIEDO, Ubicacion.NAVA,
                "Sidrero", "Pastor", "Ciclista"));
        zona(new Zona(Ubicacion.RUTA2, Generacion.tipos("Normal", "Poison", "Grass", "Fairy"), 5, 8,
                "Un camino tranquilo con flores y algún que otro bicho.", Ubicacion.OVIEDO, Ubicacion.LLANES, Ubicacion.OVIEDO,
                "Jardinero", "Excursionista", "Chica de picnic"));
        zona(new Zona(Ubicacion.RUTA3, Generacion.tipos("Bug", "Grass", "Poison"), 8, 12,
                "El Bosque Cantabria: árboles altos y mucha humedad.", Ubicacion.LLANES, Ubicacion.GIJON, Ubicacion.LLANES,
                "Cazabichos", "Guarda forestal", "Botánico"));
        zona(new Zona(Ubicacion.RUTA4, Generacion.tipos("Water", "Flying", "Normal"), 14, 18,
                "La Costa Cantábrica: olas, gaviotas y mucha sal.", Ubicacion.GIJON, Ubicacion.COMILLAS, Ubicacion.GIJON,
                "Pescador", "Bañista", "Surfista"));
        zona(new Zona(Ubicacion.RUTA8, Generacion.tipos("Water", "Ground", "Normal"), 16, 20,
                "Un camino que sigue el cauce de la ría, entre Comillas y Santander.", Ubicacion.COMILLAS, Ubicacion.SANTANDER, Ubicacion.COMILLAS,
                "Marisquero", "Remero", "Paseante"));
        zona(new Zona(Ubicacion.RUTA5, Generacion.tipos("Electric", "Steel", "Fighting"), 18, 23,
                "Una llanura llena de torres eléctricas y tormentas.", Ubicacion.SANTANDER, Ubicacion.LAREDO, Ubicacion.SANTANDER,
                "Ingeniero", "Motero", "Electricista"));
        zona(new Zona(Ubicacion.RUTA9, Generacion.tipos("Electric", "Water", "Steel"), 20, 25,
                "Unas marismas con cables tendidos sobre el agua.", Ubicacion.LAREDO, Ubicacion.BILBAO, Ubicacion.LAREDO,
                "Técnico de antenas", "Repartidor", "Científico"));
        zona(new Zona(Ubicacion.RUTA10, Generacion.tipos("Grass", "Bug", "Poison", "Fairy"), 23, 28,
                "El Hayedo Silencioso: hojas por el suelo y niebla entre los troncos.", Ubicacion.BILBAO, Ubicacion.ORDUNA, Ubicacion.BILBAO,
                "Leñador", "Micólogo", "Cazador de setas"));
        zona(new Zona(Ubicacion.RUTA6, Generacion.tipos("Fire", "Rock", "Ground"), 24, 29,
                "Una cueva caliente donde el suelo quema.", Ubicacion.ORDUNA, Ubicacion.VITORIA, Ubicacion.ORDUNA,
                "Minero", "Montañero", "Herrero"));
        zona(new Zona(Ubicacion.RUTA7, Generacion.tipos("Ghost", "Dark", "Psychic", "Dragon"), 30, 36,
                "Un sendero oscuro más allá de Vitoria. Aquí solo llegan los más valientes.", Ubicacion.VITORIA, Ubicacion.ALDEA_SOMBRIA, Ubicacion.VITORIA,
                "Médium", "Ocultista", "Vidente"));

        // ---- Ciudades: Pueblo(lugar, descripción, tipos típicos, sede Gobierno, casino, ruta anterior, ruta siguiente)
        // El gimnasio de cada ciudad se define en Gimnasio (una ciudad, un gimnasio como máximo).
        pueblo(new Pueblo(Ubicacion.NAVA, "Un pueblo de manzanos y lagares, sin gimnasio. Huele a sidra.",
                Generacion.tipos("Normal", "Grass"), false, false, Ubicacion.RUTA1, Ubicacion.RUTA11));
        pueblo(new Pueblo(Ubicacion.OVIEDO, "Una ciudad de piedra y montaña.",
                Generacion.tipos("Rock", "Ground"), false, false, Ubicacion.RUTA11, Ubicacion.RUTA2));
        pueblo(new Pueblo(Ubicacion.LLANES, "Un pueblo pesquero y tranquilo, sin gimnasio, donde descansar a mitad de camino.",
                Generacion.tipos("Normal", "Fairy"), false, false, Ubicacion.RUTA2, Ubicacion.RUTA3));
        pueblo(new Pueblo(Ubicacion.GIJON, "Una ciudad de puerto, con olor a mar.",
                Generacion.tipos("Water"), false, false, Ubicacion.RUTA3, Ubicacion.RUTA4));
        pueblo(new Pueblo(Ubicacion.COMILLAS, "Un pueblo de veraneo, sin gimnasio, famoso por su casino frente al mar.",
                Generacion.tipos("Water", "Normal"), false, true, Ubicacion.RUTA4, Ubicacion.RUTA8));
        pueblo(new Pueblo(Ubicacion.SANTANDER, "Una ciudad de paseos marítimos y tormentas de verano.",
                Generacion.tipos("Electric"), false, false, Ubicacion.RUTA8, Ubicacion.RUTA5));
        pueblo(new Pueblo(Ubicacion.LAREDO, "Un pueblo de playa larga y torres de alta tensión, sin gimnasio.",
                Generacion.tipos("Electric", "Water"), false, false, Ubicacion.RUTA5, Ubicacion.RUTA9));
        pueblo(new Pueblo(Ubicacion.BILBAO, "Una ciudad de parques enormes y hierba muy cuidada.",
                Generacion.tipos("Grass"), false, false, Ubicacion.RUTA9, Ubicacion.RUTA10));
        pueblo(new Pueblo(Ubicacion.ORDUNA, "Un pueblo de montaña rodeado de hayas, sin gimnasio, donde se respira tranquilidad.",
                Generacion.tipos("Grass", "Bug"), false, false, Ubicacion.RUTA10, Ubicacion.RUTA6));
        pueblo(new Pueblo(Ubicacion.VITORIA, "La ciudad del Gobierno: burocracia en cada esquina.",
                Generacion.tipos("Fire"), true, false, Ubicacion.RUTA6, Ubicacion.RUTA7));
        pueblo(new Pueblo(Ubicacion.ALDEA_SOMBRIA, "Una aldea perdida al final del camino, sin gimnasio. Aquí el sol casi no llega.",
                Generacion.tipos("Ghost", "Dark"), false, false, Ubicacion.RUTA7, null));
    }

    /** Orden real del camino, de principio a fin (lo usa el menú de vuelo). */
    private static final List<Ubicacion> RECORRIDO = List.of(
            Ubicacion.VILLAVERDE, Ubicacion.RUTA1, Ubicacion.NAVA, Ubicacion.RUTA11, Ubicacion.OVIEDO,
            Ubicacion.RUTA2, Ubicacion.LLANES, Ubicacion.RUTA3, Ubicacion.GIJON, Ubicacion.RUTA4,
            Ubicacion.COMILLAS, Ubicacion.RUTA8, Ubicacion.SANTANDER, Ubicacion.RUTA5, Ubicacion.LAREDO,
            Ubicacion.RUTA9, Ubicacion.BILBAO, Ubicacion.RUTA10, Ubicacion.ORDUNA, Ubicacion.RUTA6,
            Ubicacion.VITORIA, Ubicacion.RUTA7, Ubicacion.ALDEA_SOMBRIA);

    public static List<Ubicacion> getRecorrido() {
        return RECORRIDO;
    }

    public static Zona zonaDe(Ubicacion u) {
        return ZONAS.get(u);
    }

    public static Pueblo puebloDe(Ubicacion u) {
        return PUEBLOS.get(u);
    }

    /** Etiqueta corta para listados: "Ciudad Oviedo - gimnasio de Roca", "Pueblo Comillas - casino"... */
    public static String descripcionCorta(Ubicacion u) {
        Pueblo p = PUEBLOS.get(u);
        if (p == null) {
            return ZONAS.containsKey(u) ? u.getNombre() + " - ruta" : u.getNombre();
        }
        List<String> extras = new java.util.ArrayList<>();
        Gimnasio g = Gimnasio.deCiudad(u);
        if (g != null) extras.add("gimnasio de " + g.getTipo());
        if (p.tieneCasino()) extras.add("casino");
        if (p.tieneSedeGobierno()) extras.add("sede del Gobierno");
        return extras.isEmpty() ? u.getNombre() : u.getNombre() + " - " + String.join(", ", extras);
    }

    private static void zona(Zona z) {
        ZONAS.put(z.getUbicacion(), z);
    }

    private static void pueblo(Pueblo p) {
        PUEBLOS.put(p.getUbicacion(), p);
    }

    /** Bucle principal del juego. No termina: se sale desde el menú. */
    public static void jugar(Scanner sc, Jugador jugador) {
        while (true) {
            Ubicacion actual = jugador.getUbicacion();
            jugador.setUbicacion(actual); // por si viene de una partida vieja sin ubicación

            Ubicacion destino;
            if (actual == Ubicacion.VILLAVERDE) {
                destino = Villaverde.mostrarMenu(sc, jugador);
            } else if (ZONAS.containsKey(actual)) {
                destino = Ruta.explorar(sc, jugador, ZONAS.get(actual));
            } else if (PUEBLOS.containsKey(actual)) {
                destino = Pueblo.visitar(sc, jugador, PUEBLOS.get(actual));
            } else {
                destino = Ubicacion.VILLAVERDE;
            }
            jugador.setUbicacion(destino);
        }
    }

    /** Muestra las opciones numeradas y ejecuta la elegida. Null si no cambia de lugar o es inválida. */
    public static Ubicacion elegir(Scanner sc, List<Opcion> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i).texto);
        }
        System.out.print("Elige: ");
        int n = Entrada.leerEntero(sc);
        if (n < 1 || n > opciones.size()) {
            System.out.println("Opción no válida.");
            return null;
        }
        return opciones.get(n - 1).accion.ejecutar();
    }
}
