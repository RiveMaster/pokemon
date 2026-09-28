package Proyecto;

import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;


    private static HashMap<String, Integer> medicinas = new HashMap<>();
    private static HashMap<String, Integer> pokeballs = new HashMap<>();
    private static HashMap<String, Integer> objetosClave = new HashMap<>();
    private static HashMap<String, Integer> mts = new HashMap<>();
    private String nombre;
    private static List<PokemonLuchador> equipo = new ArrayList<>();
    private static List<PokemonLuchador> pc = new ArrayList<>();
    private int dinero;
    private Rival rival;
    private int encuentroActual;
    private boolean mamaVisitada;
    private transient Scanner sc;

    public Jugador(String nombre, PokemonLuchador pokemonInicial, int dineroInicial) {
        this.nombre = nombre;
        this.equipo.add(pokemonInicial);
        this.dinero = dineroInicial;
        this.rival = null;
        this.encuentroActual = 0;
        this.mamaVisitada = false;

    }

    // -------- GETTERS --------


    public String getNombre() { return nombre; }

    public PokemonLuchador getPokemon() {
        if (equipo.isEmpty()) {
            return null;
        }
        return equipo.get(0); // Retorna el Pokémon principal (el primero en el equipo) para compatibilidad
    }

    public List<PokemonLuchador> getEquipo() {
        return equipo;
    }

    public List<PokemonLuchador> getPc(){return pc;}

    public int getDinero() { return dinero; }

    public Rival getRival() { return rival; }

    public int getEncuentroActual() { return encuentroActual; }

    public boolean isMamaVisitada() { return mamaVisitada; }

    // -------- SETTERS --------


    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public void setPokemon(PokemonLuchador pokemon) {
        if (!equipo.isEmpty()) {
            equipo.set(0, pokemon); // Actualiza el Pokémon principal para compatibilidad
        } else {
            equipo.add(pokemon);
        }
    }

    public void setRival(Rival rival) { // ← NUEVO
        this.rival = rival;
    }

    public void setEncuentroActual(int encuentro) { // ← NUEVO
        this.encuentroActual = encuentro;
    }

    public void setMamaVisitada(boolean visitada) { // ← NUEVO
        this.mamaVisitada = visitada;
    }


    public void agregarDinero(int cantidad) {
        dinero += cantidad;
    }

    public boolean gastarDinero(int cantidad) {
        if (dinero >= cantidad) {
            dinero -= cantidad;
            return true;
        }
        return false;
    }

    // =====================================================
    // ================   SISTEMA DE PC   =================
    // =====================================================
    // Permite guardar Pokémon del equipo en el PC y sacarlos
    // de vuelta al equipo, además de enviar automáticamente
    // al PC cualquier Pokémon capturado cuando el equipo (6)
    // ya está completo.

    private static void mostrarListaEquipo() {
        if (equipo.isEmpty()) {
            System.out.println("  (no tienes Pokémon en el equipo)");
            return;
        }
        for (int k = 0; k < equipo.size(); k++) {
            PokemonLuchador p = equipo.get(k);
            String estado = p.estaVivo() ? "Vivo" : "Debilitado";
            System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
        }
    }

    private static void mostrarListaPc() {
        if (pc.isEmpty()) {
            System.out.println("  (el PC está vacío)");
            return;
        }
        for (int k = 0; k < pc.size(); k++) {
            PokemonLuchador p = pc.get(k);
            String estado = p.estaVivo() ? "Vivo" : "Debilitado";
            System.out.println((k + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " (" + estado + ")");
        }
    }

    // Envía un Pokémon del equipo al PC. Devuelve true si se depositó.
    public static boolean depositarPokemon(Scanner sc) {
        if (equipo.size() <= 1) {
            System.out.println("¡No puedes depositar a tu último Pokémon! Necesitas al menos uno en el equipo.");
            return false;
        }

        System.out.println("Selecciona un Pokémon de tu equipo para depositar en el PC:");
        mostrarListaEquipo();
        System.out.print("Elige una opción (0 para cancelar): ");

        int idx = sc.nextInt() - 1;
        if (idx == -1) {
            System.out.println("Cancelado.");
            return false;
        }
        if (idx < 0 || idx >= equipo.size()) {
            System.out.println("Selección inválida.");
            return false;
        }

        PokemonLuchador elegido = equipo.get(idx);
        equipo.remove(idx);
        pc.add(elegido);
        System.out.println("¡" + elegido.getNombre() + " ha sido enviado al PC!");
        return true;
    }

    // Saca un Pokémon del PC y lo añade al equipo. Devuelve true si se retiró.
    public static boolean retirarPokemon(Scanner sc) {
        if (pc.isEmpty()) {
            System.out.println("El PC está vacío, no hay Pokémon que retirar.");
            return false;
        }
        if (equipo.size() >= 6) {
            System.out.println("Tu equipo ya está completo (máximo 6 Pokémon). Deposita alguno primero.");
            return false;
        }

        System.out.println("Selecciona un Pokémon del PC para añadir a tu equipo:");
        mostrarListaPc();
        System.out.print("Elige una opción (0 para cancelar): ");

        int idx = sc.nextInt() - 1;
        if (idx == -1) {
            System.out.println("Cancelado.");
            return false;
        }
        if (idx < 0 || idx >= pc.size()) {
            System.out.println("Selección inválida.");
            return false;
        }

        PokemonLuchador elegido = pc.get(idx);
        pc.remove(idx);
        equipo.add(elegido);
        System.out.println("¡" + elegido.getNombre() + " se ha unido a tu equipo!");
        return true;
    }

    // Envía directamente un Pokémon al PC (p.ej. al capturar con el equipo lleno)
    public static void salvajePc(PokemonLuchador pokemon){
        System.out.println("Tu equipo está lleno. " + pokemon.getNombre() + " ha sido enviado directamente al PC.");
        pc.add(pokemon);
    }

    // Menú interactivo del PC, pensado para invocarse desde el Centro Pokémon
    public static void menuPC(Scanner sc) {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║              PC POKÉMON             ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("1. Ver mi equipo");
            System.out.println("2. Ver Pokémon guardados en el PC");
            System.out.println("3. Depositar un Pokémon (equipo -> PC)");
            System.out.println("4. Retirar un Pokémon (PC -> equipo)");
            System.out.println("5. Salir del PC");
            System.out.print("Elige una opción: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> {
                    System.out.println("\n-- Tu equipo --");
                    mostrarListaEquipo();
                }
                case 2 -> {
                    System.out.println("\n-- Pokémon en el PC --");
                    mostrarListaPc();
                }
                case 3 -> depositarPokemon(sc);
                case 4 -> retirarPokemon(sc);
                case 5 -> {
                    System.out.println("Cerrando sesión del PC...");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    public void agregarPokemon(PokemonLuchador pokemon) {
        if (equipo.size() < 6) {
            equipo.add(pokemon);
        } else {
            Jugador.salvajePc(pokemon);
        }
    }

    public boolean equipoLleno() {
        return equipo.size() >= 6;
    }

    public void mostrarEquipo() {
        System.out.println("Equipo de " + nombre + ":");
        for (int i = 0; i < equipo.size(); i++) {
            System.out.println((i + 1) + " - " + equipo.get(i).getNombre()+" Nv." + equipo.get(i).getNivel());
        }
    }

    public void mostrarEstadoEquipo() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      ESTADO DEL EQUIPO             ║");
        System.out.println("╚════════════════════════════════════╝");
        for (int i=0;i< equipo.size();i++) {
            System.out.println("Nombre: " + equipo.get(i).getNombre());
            System.out.println("Nivel: " + equipo.get(i).getNivel());
            System.out.println("Vida: " + equipo.get(i).getVidaActual() + "/" + equipo.get(i).getVidaMax());
            System.out.println("Ataque: " + equipo.get(i).getAtaque());
            System.out.println("Defensa: " + equipo.get(i).getDefensa());
            System.out.println("Velocidad: " + equipo.get(i).getVelocidad());
            System.out.println("EXP: " + equipo.get(i).getExpActual() + "/" + equipo.get(i).getExpParaSubirNivel());
            System.out.println("--------------------");
        }
    }

    public boolean equipoVM(){
        for (int i=0;i< equipo.size();i++) {
            if (equipo.get(i).estaVivo()){
                return true;
            }
        }
        return false;
    }
    public void obtenerPokemon(String pokemon){
        PokemonLuchador nuevo=new PokemonLuchador(Pokedex.buscarPorNombre(pokemon), 100, 3);
        agregarPokemon(nuevo);
    }

    public boolean capturarPokemon(PokemonLuchador pokemon, String tipoPokeball) {
        // Nota: ya no se bloquea la captura si el equipo está lleno.
        // Si se captura y no hay hueco en el equipo, agregarPokemon()
        // se encarga de enviar el Pokémon automáticamente al PC.

        // Intentar usar la Poké Ball de la mochila
        if (!usarPokeball(tipoPokeball)) {
            return false; // No se pudo usar la Poké Ball (no hay disponibles)
        }

        // Calcular probabilidad de captura basada en la vida restante del Pokémon
        // Fórmula simple: cuanto más debilitado, mayor la probabilidad
        // Probabilidad base: 30% + (100% - (vidaActual / vidaMax * 100%)) + bonus por tipo de ball
        double vidaPorcentaje = (double) pokemon.getVidaActual() / pokemon.getVidaMax() * 100;
        double probabilidadBase = 30 + (100 - vidaPorcentaje); // Máximo 130 si vida=0, mínimo 30 si vida=full

        // Bonus por tipo de Poké Ball (ajustar según tipos disponibles)
        double bonus = switch (tipoPokeball.toLowerCase()) {
            case "poke ball" -> 0;    // Normal, sin bonus
            case "super ball" -> 20;  // +20%
            case "ultra ball" -> 40;  // +40%
            default -> 0;
        };

        double probabilidadFinal = probabilidadBase + bonus;

        // Simular captura (aleatorio)
        Random random = new Random();
        boolean capturado = random.nextDouble() * 100 < probabilidadFinal;

        if (capturado) {
            System.out.println("¡Felicidades! Has capturado a " + pokemon.getNombre() + ".");
            agregarPokemon(pokemon); // Si el equipo está lleno, se envía automáticamente al PC
            return true;
        } else {
            System.out.println("¡Oh no! El Pokémon se escapó de la " + tipoPokeball + ".");
            return false;
        }
    }

    public void curarPokemon(PokemonLuchador pokemon, String tipocura) {
        if (!usarMedicina(tipocura, pokemon)) {
            System.out.println("No tienes esa cura."); // No se pudo usar la cura (no hay disponibles)
        }if (tipocura.equals("pocion")) {
            pokemon.curar(100);
        }if (tipocura.equals("superpocion")) {
            pokemon.curar(150);
        }if (tipocura.equals("hiperpocion")) {
            pokemon.curar(200);
        }
    }

    public String getNivel() {
        return getNivel();
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
    public void usarMT(Jugador jugador){

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
    }


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

    public void initializeScanner() {
        this.sc = new Scanner(System.in);
    }
}