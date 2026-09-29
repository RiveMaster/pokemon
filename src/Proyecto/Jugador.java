package Proyecto;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Set;

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

    // Objetos equipables en la mochila, medallas de gimnasio y posición actual
    private HashMap<ObjetoEquipable, Integer> objetosEquipables = new HashMap<>();
    private Set<String> medallas = new LinkedHashSet<>();
    private Ubicacion ubicacion = Ubicacion.VILLAVERDE;

    // Generación en la que se juega, la más alta desbloqueada y jefes del Gobierno derrotados
    private int generacionActual = 1;
    private int generacionMaxima = 1;
    private Set<Integer> jefesDerrotados = new LinkedHashSet<>();

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

    public Ubicacion getUbicacion() { return ubicacion; }

    public Set<String> getMedallas() { return medallas; }

    public boolean tieneMedalla(String medalla) { return medallas.contains(medalla); }

    // ---- Generaciones ----
    public Generacion getGeneracion() { return Generacion.de(generacionActual); }

    public int getGeneracionMaxima() { return generacionMaxima; }

    /** Cambia de generación (solo a las desbloqueadas). Devuelve false si no está permitida. */
    public boolean viajarAGeneracion(int numero) {
        if (numero < 1 || numero > generacionMaxima) return false;
        generacionActual = numero;
        return true;
    }

    public void desbloquearGeneracion(Generacion gen) {
        generacionMaxima = Math.max(generacionMaxima, gen.getNumero());
    }

    public void desbloquearTodasLasGeneraciones() {
        generacionMaxima = 10;
    }

    public boolean jefeDerrotado(Generacion gen) { return jefesDerrotados.contains(gen.getNumero()); }

    public void marcarJefeDerrotado(Generacion gen) { jefesDerrotados.add(gen.getNumero()); }

    /** Cuántas medallas tiene de una generación (las medallas se guardan con el nombre de la región). */
    public int medallasEnGeneracion(Generacion gen) {
        String sufijo = " (" + gen.getRegion() + ")";
        int n = 0;
        for (String m : medallas) {
            if (m.endsWith(sufijo)) n++;
        }
        return n;
    }

    /** Primer Pokémon del equipo que pueda combatir (null si todos están debilitados). */
    public PokemonLuchador getPokemonActivo() {
        for (PokemonLuchador p : equipo) {
            if (p.estaVivo()) return p;
        }
        return null;
    }

    public void curarEquipoTotal() {
        for (PokemonLuchador p : equipo) {
            p.curarTotal();
        }
    }

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

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void añadirMedalla(String medalla) {
        medallas.add(medalla);
    }

    // =====================================================
    // ===============   GUARDADO / CARGA   ================
    // =====================================================
    // equipo, pc y la mochila (medicinas, pokeballs, objetos clave y MT) son
    // static, y Java NO serializa campos static: sin esto, al cargar una partida
    // en otra ejecución el equipo y la mochila aparecían vacíos. Por eso se
    // escriben/leen aquí a mano, justo detrás de los campos normales.

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new HashMap<>(medicinas));
        out.writeObject(new HashMap<>(pokeballs));
        out.writeObject(new HashMap<>(objetosClave));
        out.writeObject(new HashMap<>(mts));
        out.writeObject(new ArrayList<>(equipo));
        out.writeObject(new ArrayList<>(pc));
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Los campos añadidos después de crear una partida vienen a null al cargarla
        if (objetosEquipables == null) objetosEquipables = new HashMap<>();
        if (medallas == null) medallas = new LinkedHashSet<>();
        if (ubicacion == null) ubicacion = Ubicacion.VILLAVERDE;
        if (jefesDerrotados == null) jefesDerrotados = new LinkedHashSet<>();
        if (generacionActual < 1) generacionActual = 1;   // partidas guardadas antes de existir las generaciones
        if (generacionMaxima < generacionActual) generacionMaxima = generacionActual;

        try {
            reemplazar(medicinas, (HashMap<String, Integer>) in.readObject());
            reemplazar(pokeballs, (HashMap<String, Integer>) in.readObject());
            reemplazar(objetosClave, (HashMap<String, Integer>) in.readObject());
            reemplazar(mts, (HashMap<String, Integer>) in.readObject());
            equipo.clear();
            equipo.addAll((List<PokemonLuchador>) in.readObject());
            pc.clear();
            pc.addAll((List<PokemonLuchador>) in.readObject());
        } catch (OptionalDataException | EOFException e) {
            // Partida guardada con una versión antigua que no incluía estos datos
        }
    }

    private static void reemplazar(HashMap<String, Integer> destino, HashMap<String, Integer> origen) {
        destino.clear();
        destino.putAll(origen);
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
            ObjetoEquipable llevado = equipo.get(i).getObjeto();
            System.out.println("Objeto: " + (llevado == null ? "ninguno" : llevado.getNombre()));
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
        añadirObjeto(mts, nombre, 1);
    }

    public void añadirObjetoEquipable(ObjetoEquipable objeto, int cantidad) {
        objetosEquipables.put(objeto, objetosEquipables.getOrDefault(objeto, 0) + cantidad);
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
    // =====================================================
    // =====================   MT   ========================
    // =====================================================
    // Las MT se guardan por nombre de ataque y son de un solo uso: se gastan al
    // enseñárselas a un Pokémon (si ya conocía el ataque, no se gastan).

    // Menú "Usar MT": elegir una MT de la mochila y el Pokémon del equipo que la aprende
    public void usarMT(Scanner sc) {
        List<String> disponibles = new ArrayList<>();
        for (String nombre : mts.keySet()) {
            if (mts.get(nombre) > 0) {
                disponibles.add(nombre);
            }
        }
        if (disponibles.isEmpty()) {
            System.out.println("No tienes ninguna MT. Puedes comprarlas en la Tienda o ganarlas en los gimnasios.");
            return;
        }
        java.util.Collections.sort(disponibles);

        System.out.println("\n-- Tus MT --");
        for (int i = 0; i < disponibles.size(); i++) {
            String nombre = disponibles.get(i);
            Movimiento mov = Ataques.buscarPorNombre(nombre);
            String detalle = (mov == null) ? "" : " [" + mov.getTipo() + ", potencia " + (int) mov.getPotencia() + "]";
            System.out.println((i + 1) + ". " + nombre + detalle + " x" + mts.get(nombre));
        }
        System.out.print("Elige una MT (0 para cancelar): ");

        int eleccion = Entrada.leerEntero(sc);
        if (eleccion < 1 || eleccion > disponibles.size()) {
            System.out.println("Cancelado.");
            return;
        }
        enseñarMT(sc, disponibles.get(eleccion - 1));
    }

    // Pregunta a qué Pokémon del equipo enseñar la MT. Devuelve true si la aprendió (y se gasta).
    public boolean enseñarMT(Scanner sc, String nombreMT) {
        Movimiento mov = Ataques.buscarPorNombre(nombreMT);
        if (mov == null) {
            System.out.println("No se reconoce el ataque de esta MT: " + nombreMT);
            return false;
        }
        if (mts.getOrDefault(nombreMT, 0) <= 0) {
            System.out.println("No tienes la MT " + nombreMT + ".");
            return false;
        }

        PokemonLuchador elegido = elegirPokemonEquipo(sc,
                "¿A qué Pokémon quieres enseñarle " + nombreMT + "? (0 para cancelar)");
        if (elegido == null) {
            System.out.println("La MT se queda en la mochila.");
            return false;
        }

        if (!elegido.aprenderMovimiento(mov)) {
            System.out.println(elegido.getNombre() + " ya conoce " + nombreMT + ". La MT no se gasta.");
            return false;
        }

        int quedan = mts.get(nombreMT) - 1;
        if (quedan <= 0) {
            mts.remove(nombreMT);
        } else {
            mts.put(nombreMT, quedan);
        }
        System.out.println("¡" + elegido.getNombre() + " ha aprendido " + nombreMT + "!");
        return true;
    }

    // Lista el equipo y devuelve el Pokémon elegido (null si cancela o la opción no es válida)
    private PokemonLuchador elegirPokemonEquipo(Scanner sc, String pregunta) {
        if (equipo.isEmpty()) {
            System.out.println("No tienes Pokémon en el equipo.");
            return null;
        }
        System.out.println("\n" + pregunta);
        for (int i = 0; i < equipo.size(); i++) {
            PokemonLuchador p = equipo.get(i);
            ObjetoEquipable llevado = p.getObjeto();
            System.out.println((i + 1) + ". " + p.getNombre() + " Nv." + p.getNivel()
                    + (llevado == null ? "" : " (lleva " + llevado.getNombre() + ")"));
        }
        System.out.print("Elige una opción: ");
        int idx = Entrada.leerEntero(sc) - 1;
        if (idx < 0 || idx >= equipo.size()) {
            return null;
        }
        return equipo.get(idx);
    }

    // =====================================================
    // ============   OBJETOS EQUIPABLES   =================
    // =====================================================

    // Menú "Equipar objeto": ver, cambiar o quitar el objeto que lleva un Pokémon
    public void menuEquipar(Scanner sc) {
        PokemonLuchador pokemon = elegirPokemonEquipo(sc,
                "¿A qué Pokémon quieres gestionarle el objeto? (0 para cancelar)");
        if (pokemon == null) {
            return;
        }

        ObjetoEquipable actual = pokemon.getObjeto();
        System.out.println("\n" + pokemon.getNombre() + " lleva: "
                + (actual == null ? "nada" : actual.getNombre() + " - " + actual.getDescripcion()));
        System.out.println("1. Equipar un objeto de la mochila");
        System.out.println("2. Quitar el objeto (vuelve a la mochila)");
        System.out.println("3. Cancelar");
        System.out.print("Elige una opción: ");

        switch (Entrada.leerEntero(sc)) {
            case 1 -> equiparDesdeMochila(sc, pokemon);
            case 2 -> {
                if (actual == null) {
                    System.out.println(pokemon.getNombre() + " no lleva ningún objeto.");
                } else {
                    pokemon.setObjeto(null);
                    añadirObjetoEquipable(actual, 1);
                    System.out.println("Has guardado " + actual.getNombre() + " en la mochila.");
                }
            }
            default -> System.out.println("Cancelado.");
        }
    }

    private void equiparDesdeMochila(Scanner sc, PokemonLuchador pokemon) {
        List<ObjetoEquipable> disponibles = new ArrayList<>();
        for (ObjetoEquipable o : ObjetoEquipable.values()) {
            if (objetosEquipables.getOrDefault(o, 0) > 0) {
                disponibles.add(o);
            }
        }
        if (disponibles.isEmpty()) {
            System.out.println("No tienes objetos equipables. Puedes comprarlos en la Tienda.");
            return;
        }

        System.out.println("\n-- Objetos equipables --");
        for (int i = 0; i < disponibles.size(); i++) {
            ObjetoEquipable o = disponibles.get(i);
            System.out.println((i + 1) + ". " + o.getNombre() + " x" + objetosEquipables.get(o) + " - " + o.getDescripcion());
        }
        System.out.print("Elige un objeto (0 para cancelar): ");

        int eleccion = Entrada.leerEntero(sc);
        if (eleccion < 1 || eleccion > disponibles.size()) {
            System.out.println("Cancelado.");
            return;
        }

        ObjetoEquipable nuevo = disponibles.get(eleccion - 1);
        ObjetoEquipable viejo = pokemon.getObjeto();

        int quedan = objetosEquipables.get(nuevo) - 1;
        if (quedan <= 0) {
            objetosEquipables.remove(nuevo);
        } else {
            objetosEquipables.put(nuevo, quedan);
        }

        pokemon.setObjeto(nuevo);
        if (viejo != null) {
            añadirObjetoEquipable(viejo, 1);
            System.out.println(viejo.getNombre() + " vuelve a la mochila.");
        }
        System.out.println("¡" + pokemon.getNombre() + " lleva ahora " + nuevo.getNombre() + "!");
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
        else mts.forEach((k,v) -> System.out.println("  " + k + " x" + v));

        System.out.println("\n-- Objetos equipables --");
        if (objetosEquipables.isEmpty()) System.out.println("  (vacío)");
        else objetosEquipables.forEach((k,v) -> System.out.println("  " + k.getNombre() + " x" + v));

        System.out.println("\n-- Medallas -- (generación actual: " + getGeneracion().etiqueta() + ")");
        if (medallas.isEmpty()) System.out.println("  (ninguna)");
        else medallas.forEach(m -> System.out.println("  " + m));

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