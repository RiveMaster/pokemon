package Proyecto;

import java.io.*;
import java.util.Scanner;

public class GuardarCargar {
    private static final String slot1 = "Juego1.txt";
    private static final String slot2 = "Juego2.txt";
    private static final String slot3 = "Juego3.txt";
    private static final String slot4 = "Juego4.txt";
    /* -----------------------
     * Método para guardar
     *-----------------------*/
    public static void guardarPartida(Scanner sc, Jugador jugador) {
        System.out.println("En que slot desea guardar la partida?(slot1, slot2, slot3, slot4)");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(sc.nextLine()+".txt"))) {
            oos.writeObject(jugador);
            System.out.println("✔ Partida guardada correctamente en: " + jugador.getUbicacion().getNombre() + ".");
        } catch (IOException e) {
            System.err.println("✖ Error al guardar la partida: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /* -----------------------
     * Método para cargar
     *-----------------------*/
    public static Jugador cargarPartida(Scanner sc) {
        System.out.println("Que slot desea cargar?(slot1, slot2, slot3, slot4)");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sc.nextLine()+".txt"))) {
            Jugador jugador = (Jugador) ois.readObject();
            System.out.println("✔ Partida cargada correctamente. Ubicación: " + jugador.getUbicacion().getNombre() + ".");
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

