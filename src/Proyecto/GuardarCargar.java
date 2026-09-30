package Proyecto;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class GuardarCargar {

    /* -----------------------
     * Método para guardar
     *-----------------------*/
    public static void guardarPartida(Scanner sc, Jugador jugador) {
        File carpeta = new File("Partidas");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        sc.nextLine();
        System.out.println("1. Guardar \n2. Sobreescribir: ");
        switch (sc.nextInt()) {
            case 1 ->{
                System.out.println("Introduzca el nombre de la partida: ");
                File fichero = new File(carpeta, sc.nextLine() + "(" + LocalDate.now() + ")" + ".txt");
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
                    oos.writeObject(jugador);
                    System.out.println("✔ Partida guardada correctamente en: " + jugador.getUbicacion().getNombre() + ".");
                } catch (IOException e) {
                    System.err.println("✖ Error al guardar la partida: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            case 2->{
                System.out.println("Introduzca la partida que desea sobreescribir (0 para salir): ");
                int seleccion=0;
                String[] listado = carpeta.list();
                for (String nombreArchivo : listado) {
                    seleccion++;
                    System.out.println(seleccion + ". " + nombreArchivo);
                }
                int elegir = sc.nextInt();
                if (elegir - 1 > listado.length || elegir < 0){
                    System.out.println("Opcion invalida.");
                }else if (elegir == 0){ }
                else {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(listado[elegir - 1]))) {
                        oos.writeObject(jugador);
                        System.out.println("✔ Partida guardada correctamente en: " + jugador.getUbicacion().getNombre() + ".");
                    } catch (IOException e) {
                        System.err.println("✖ Error al guardar la partida: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            default -> System.out.println("Opcion invalida.");
        }
    }


    /* -----------------------
     * Método para cargar
     *-----------------------*/
    public static Jugador cargarPartida(Scanner sc) {
        File carpeta = new File("Partidas");
        if (!carpeta.exists()) {
            System.out.println("No existen partidas guardadas.");;
        }else {
            System.out.println("Introduzca la partida que desea cargar: ");
            int seleccion=0;
            String[] listado = carpeta.list();
            for (String nombreArchivo : listado) {
                seleccion++;
                System.out.println(seleccion + ". " + nombreArchivo);
            }
            int elegir= sc.nextInt();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(carpeta + "/" + listado[elegir-1]))) {
                Jugador jugador = (Jugador) ois.readObject();
                System.out.println("✔ Partida" + listado[elegir-1] + "cargada correctamente. Ubicación: " + jugador.getUbicacion().getNombre() + ".");
                return jugador;
            } catch (IOException e) {
                System.err.println("✖ Error al cargar la partida: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("✖ Error: Clase no encontrada al cargar.");
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}

