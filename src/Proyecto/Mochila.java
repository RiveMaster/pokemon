package Proyecto;

import java.io.Serializable;
import java.util.HashMap;

public class Mochila implements Serializable {
   /* private static final long serialVersionUID = 1L;

    // === Dinero ===


    // === Compartimentos ===
    private static HashMap<String, Integer> medicinas = new HashMap<>();
    private static HashMap<String, Integer> pokeballs = new HashMap<>();
    private static HashMap<String, Integer> objetosClave = new HashMap<>();
    private static HashMap<String, Integer> mts = new HashMap<>();




    // === Métodos de dinero ===


    public static boolean gastarDinero(int cantidad, Jugador jugadorCompleto) {
        if (jugadorCompleto.getDinero() < cantidad) {
            return false;
        }else {
            jugadorCompleto.gastarDinero(cantidad);
            return true;
        }
    }

    public static void ganarDinero(int cantidad, Jugador jugadorCompleto) {
        jugadorCompleto.agregarDinero(cantidad);
    }

    // === Añadir objetos ===
    private void añadirObjeto(HashMap<String, Integer> bolsa, String nombre, int cantidad) {
        bolsa.put(nombre, bolsa.getOrDefault(nombre, 0) + cantidad);
    }

    public void añadirMedicina(String nombre, int cantidad) {
        añadirObjeto(medicinas, nombre, cantidad);
    }

    public void añadirPokeball(String nombre, int cantidad) {
        añadirObjeto(pokeballs, nombre, cantidad);
    }

    public void añadirObjetoClave(String nombre) {
        objetosClave.put(nombre, 1);
    }

    public void añadirMT(String nombre) {
        mts.put(nombre, 1);
    }

    // === Usar objetos ===
    public boolean usarMedicina(String nombre, PokemonLuchador pokemon) {
        if (!medicinas.containsKey(nombre) || medicinas.get(nombre) <= 0) {
            System.out.println("No tienes " + nombre + ".");
            return false;
        }

        medicinas.put(nombre, medicinas.get(nombre) - 1);

        switch (nombre.toLowerCase()) {
            case "poción" -> pokemon.curar(20);
            case "superpoción" -> pokemon.curar(50);
            case "hiperpoción" -> pokemon.curar(120);
            default -> {
                System.out.println("No reconozco esta medicina.");
                return false;
            }
        }

        System.out.println("Usaste " + nombre + ".");
        return true;
    }

    public boolean usarPokeball(String nombre) {
        if (!pokeballs.containsKey(nombre) || pokeballs.get(nombre) <= 0) {
            System.out.println("No tienes " + nombre + ".");
            return false;
        }

        pokeballs.put(nombre, pokeballs.get(nombre) - 1);
        System.out.println("Lanzas una " + nombre + "...");
        return true;
    }

    // === Mostrar mochila ===
    public void mostrar(Jugador jugadorCompleto) {
        System.out.println("\n========= MOCHILA =========");
        System.out.println("Dinero: " + jugadorCompleto.getDinero() + "€");

        System.out.println("\n-- Medicinas --");
        if (medicinas.isEmpty()) System.out.println("  (vacío)");
        else medicinas.forEach((k,v) -> System.out.println("  " + k + " x" + v));

        System.out.println("\n-- Poké Balls --");
        if (pokeballs.isEmpty()) System.out.println("  (vacío)");
        else pokeballs.forEach((k,v) -> System.out.println("  " + k + " x" + v));

        System.out.println("\n-- Objetos Clave --");
        if (objetosClave.isEmpty()) System.out.println("  (vacío)");
        else objetosClave.forEach((k,v) -> System.out.println("  " + k));

        System.out.println("\n-- MT/MO --");
        if (mts.isEmpty()) System.out.println("  (vacío)");
        else mts.forEach((k,v) -> System.out.println("  " + k));

        System.out.println("===========================\n");
    }*/
}