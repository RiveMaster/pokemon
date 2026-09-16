package Proyecto;

import java.io.*;

public class GuardarCargar {
    private static final String ARCHIVO = "Juego.txt";

    /* -----------------------
     * Método para guardar
     *-----------------------*/
    public static void guardarPartida(Jugador jugador) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(jugador);
            System.out.println("✔ Partida guardada correctamente.");
        } catch (IOException e) {
            System.err.println("✖ Error al guardar la partida: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /* -----------------------
     * Método para cargar
     *-----------------------*/
    public static Jugador cargarPartida() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            Jugador jugador = (Jugador) ois.readObject();
            System.out.println("✔ Partida cargada correctamente.");
            return jugador;
        } catch (FileNotFoundException e) {
            System.err.println("✖ No existe partida guardada.");
        } catch (IOException e) {
            System.err.println("✖ Error al cargar la partida: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("✖ Error: Clase no encontrada al cargar.");
            e.printStackTrace();
        }
        return null;
    }

}

