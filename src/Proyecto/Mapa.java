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
 * Para añadir una ruta o ciudad: nuevo valor en Ubicacion + una línea aquí.
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
        // ---- Rutas: Zona(lugar, tipos que salen, nivel min, nivel max, descripción, anterior, siguiente, pueblo cercano)
        zona(new Zona(Ubicacion.RUTA1, Generacion.tipos("Normal", "Flying", "Bug"), 3, 5,
                "Hierba alta entre Villaverde y Oviedo.", Ubicacion.VILLAVERDE, Ubicacion.OVIEDO, Ubicacion.VILLAVERDE));
        zona(new Zona(Ubicacion.RUTA2, Generacion.tipos("Normal", "Poison", "Grass", "Fairy"), 5, 8,
                "Un camino tranquilo con flores y algún que otro bicho.", Ubicacion.OVIEDO, Ubicacion.RUTA3, Ubicacion.OVIEDO));
        zona(new Zona(Ubicacion.RUTA3, Generacion.tipos("Bug", "Grass", "Poison"), 8, 12,
                "El Bosque Cantabria: árboles altos y mucha humedad.", Ubicacion.RUTA2, Ubicacion.GIJON, Ubicacion.OVIEDO));
        zona(new Zona(Ubicacion.RUTA4, Generacion.tipos("Water", "Flying", "Normal"), 14, 18,
                "La Costa Cantábrica: olas, gaviotas y mucha sal.", Ubicacion.GIJON, Ubicacion.SANTANDER, Ubicacion.GIJON));
        zona(new Zona(Ubicacion.RUTA5, Generacion.tipos("Electric", "Steel", "Fighting"), 18, 23,
                "Una llanura llena de torres eléctricas y tormentas.", Ubicacion.SANTANDER, Ubicacion.BILBAO, Ubicacion.SANTANDER));
        zona(new Zona(Ubicacion.RUTA6, Generacion.tipos("Fire", "Rock", "Ground"), 24, 29,
                "Una cueva caliente donde el suelo quema.", Ubicacion.BILBAO, Ubicacion.VITORIA, Ubicacion.BILBAO));
        zona(new Zona(Ubicacion.RUTA7, Generacion.tipos("Ghost", "Dark", "Psychic", "Dragon"), 30, 36,
                "Un sendero oscuro más allá de Vitoria. Aquí solo llegan los más valientes.", Ubicacion.VITORIA, null, Ubicacion.VITORIA));

        // ---- Ciudades: Pueblo(lugar, descripción, tipos típicos, nº gimnasio (-1 = ninguno), sede Gobierno, ruta anterior, ruta siguiente)
        pueblo(new Pueblo(Ubicacion.OVIEDO, "Una ciudad de piedra y montaña.",
                Generacion.tipos("Rock", "Ground"), 0, false, Ubicacion.RUTA1, Ubicacion.RUTA2));
        pueblo(new Pueblo(Ubicacion.GIJON, "Una ciudad de puerto, con olor a mar.",
                Generacion.tipos("Water"), 1, false, Ubicacion.RUTA3, Ubicacion.RUTA4));
        pueblo(new Pueblo(Ubicacion.SANTANDER, "Una ciudad de paseos marítimos y tormentas de verano.",
                Generacion.tipos("Electric"), 2, false, Ubicacion.RUTA4, Ubicacion.RUTA5));
        pueblo(new Pueblo(Ubicacion.BILBAO, "Una ciudad de parques enormes y hierba muy cuidada.",
                Generacion.tipos("Grass"), 3, false, Ubicacion.RUTA5, Ubicacion.RUTA6));
        pueblo(new Pueblo(Ubicacion.VITORIA, "La ciudad del Gobierno: burocracia en cada esquina.",
                Generacion.tipos("Fire"), 4, true, Ubicacion.RUTA6, Ubicacion.RUTA7));
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
