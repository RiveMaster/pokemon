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
        if (UIJuego.disponible()) {
            if (!carpeta.exists()) carpeta.mkdirs();
            File[] partidas = carpeta.listFiles((dir, nombre) -> nombre.endsWith(".txt"));
            String[] existentes = partidas == null ? new String[0]
                    : java.util.Arrays.stream(partidas).map(File::getName).toArray(String[]::new);
            String[] opciones = new String[existentes.length + 2];
            opciones[0] = "Guardar como nueva partida";
            for (int i = 0; i < existentes.length; i++) opciones[i + 1] = "Sobrescribir: " + existentes[i];
            opciones[opciones.length - 1] = "Cancelar";
            int seleccion = UIJuego.elegir("Guardar partida", "Elige cómo guardar:", opciones);
            if (seleccion < 0 || seleccion == opciones.length - 1) return;
            File destino;
            if (seleccion == 0) {
                String nombre = UIJuego.pedirTexto("Guardar partida", "Nombre de la partida:", jugador.getNombre());
                if (nombre == null || nombre.isBlank()) return;
                nombre = nombre.replaceAll("[\\\\/:*?\"<>|]", "_");
                destino = new File(carpeta, nombre + "(" + LocalDate.now() + ").txt");
            } else destino = new File(carpeta, existentes[seleccion - 1]);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destino))) {
                oos.writeObject(jugador);
                UIJuego.mensaje("Partida guardada", "Progreso guardado correctamente en " + destino.getName());
            } catch (IOException ex) {
                UIJuego.mensaje("Error al guardar", ex.getMessage());
            }
            return;
        }
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
        if (UIJuego.disponible()) {
            File[] partidas = carpeta.listFiles((dir, nombre) -> nombre.endsWith(".txt"));
            if (partidas == null || partidas.length == 0) {
                UIJuego.mensaje("Cargar partida", "No hay partidas guardadas.");
                return null;
            }
            String[] nombres = java.util.Arrays.stream(partidas).map(File::getName).toArray(String[]::new);
            int seleccion = UIJuego.elegir("Cargar partida", "Selecciona una partida:", nombres);
            if (seleccion < 0 || seleccion >= partidas.length) return null;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(partidas[seleccion]))) {
                Jugador jugador = (Jugador) ois.readObject();
                UIJuego.mensaje("Partida cargada", "Partida cargada: " + partidas[seleccion].getName());
                return jugador;
            } catch (IOException | ClassNotFoundException ex) {
                UIJuego.mensaje("Error al cargar", ex.getMessage());
                return null;
            }
        }
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

