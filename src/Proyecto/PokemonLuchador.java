package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.Serializable;

public class PokemonLuchador implements Serializable {
    private static final long serialVersionUID = 1L;

    private PokemonBase base;
    private int nivel;
    private EstadoAlterado estado = EstadoAlterado.NORMAL;
    private int turnosDormido = 0;
    private List<movimiento> movimientosAprendidos = new ArrayList<>();

    // Stats
    private int vidaMax;
    private int vidaActual;
    private int ataque;
    private int defensa;
    private int velocidad;

    // IVs
    private int ivVida;
    private int ivAtaque;
    private int ivDefensa;
    private int ivVelocidad;

    // Experiencia
    private int expActual = 0;
    private int expNecesaria = 70;



    public PokemonLuchador(PokemonBase base, int nivel) {

        this.base = base;
        this.nivel = nivel;

        Random r = new Random();

        // IVs: entre -3 y +3
        ivVida = r.nextInt(7) - 3;
        ivAtaque = r.nextInt(7) - 3;
        ivDefensa = r.nextInt(7) - 3;
        ivVelocidad = r.nextInt(7) - 3;

        // Stats totales
        vidaMax = base.getVidaBase() + ivVida;
        ataque = base.getAtaqueBase() + ivAtaque;
        defensa = base.getDefensaBase() + ivDefensa;
        velocidad = base.getVelocidadBase() + ivVelocidad;

        vidaActual = vidaMax;
    }

    public PokemonLuchador(PokemonBase trucado, int nivel, int stats){
        this.base = trucado;
        this.nivel = nivel;
        // IVs: entre -3 y +3
        ivVida = stats;
        ivAtaque = stats;
        ivDefensa = stats;
        ivVelocidad = stats;
        // Stats totales
        vidaMax = base.getVidaBase() + ivVida;
        ataque = base.getAtaqueBase() + ivAtaque;
        defensa = base.getDefensaBase() + ivDefensa;
        velocidad = base.getVelocidadBase() + ivVelocidad;

        vidaActual = vidaMax;
    }

    public static void combate(PokemonLuchador jugador, PokemonLuchador rival) {
    }

    public String getNombre() {
        return base.getNombre();
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean estaVivo() {
        return vidaActual > 0;
    }

    public List<movimiento> getMovimientos() {
        List<movimiento> todos = new ArrayList<>(base.getMovimientosPorNivel());
        todos.addAll(movimientosAprendidos);
        return todos;
    }

    public void recibirDaño(double cantidad) {
        vidaActual -= cantidad;
        if (vidaActual < 0) vidaActual = 0;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getExpActual() {
        return expActual;
    }

    public int getExpParaSubirNivel() {
        return expNecesaria;
    }

    public PokemonBase getBase() {
        return base;
    }

    public EstadoAlterado getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlterado estado) {
        this.estado = estado;
    }

    public int getTurnosDormido() {
        return turnosDormido;
    }

    public void setTurnosDormido(int turnosDormido) {
        this.turnosDormido = turnosDormido;
    }

    public void intentarDormir(PokemonLuchador objetivo) {
        Random r = new Random();
        if (r.nextInt(100) < 30 && objetivo.getEstado() == EstadoAlterado.NORMAL) {
            objetivo.setEstado(EstadoAlterado.DORMIDO);
            objetivo.setTurnosDormido(2 + r.nextInt(2)); // 2–3 turnos
            System.out.println(objetivo.getNombre() + " se ha quedado dormido.");
        }
    }

    // Ganar experiencia
    public void ganarExp(int cantidad) {
        expActual += cantidad;
        System.out.println(getNombre() + " gana " + cantidad + " EXP.");

        while (expActual >= expNecesaria*nivel/4) {
            expActual -= expNecesaria*nivel/4;
            subirNivel();
        }
    }

    // ---------------------------
    // MÉTODO DE COMBATE (corregido)
    // ---------------------------
    /*public static void combate(Jugador jugador, Rival rival, Scanner sc) {

        System.out.println("\n=== ¡COMIENZA EL COMBATE! ===\n");

        while (jugador.estaVivo() && rival.estaVivo()) {

            // Mostrar barras de vida
            System.out.println("\n===============================");
            System.out.println("Tu Pokémon: " + jugador.getNombre() + "  Nv." + jugador.getNivel());
            System.out.println("Vida: " + jugador.getVidaActual() + "/" + jugador.getVidaMax());
            System.out.println("-------------------------------");
            System.out.println("Rival: " + rival.getNombre() + "  Nv." + rival.getNivel());
            System.out.println("Vida: " + rival.getVidaActual() + "/" + rival.getVidaMax());
            System.out.println("===============================\n");

            System.out.println("1. Atacar");
            System.out.print("Elige acción: ");

            int opcion = sc.nextInt();

            if (opcion == 1) {

                List<movimiento> movs = jugador.getMovimientos();

                if (movs.isEmpty()) {
                    System.out.println("¡Tu Pokémon no tiene movimientos!");
                    return;
                }

                System.out.println("\nElige un movimiento:");

                for (int i = 0; i < movs.size(); i++) {
                    System.out.println((i + 1) + ". " + movs.get(i).getNombre() +
                            " (Potencia " + movs.get(i).getPotencia() + ")");
                }

                int eleccion = sc.nextInt();

                if (eleccion < 1 || eleccion > movs.size()) {
                    System.out.println("Movimiento inválido. Pierdes el turno.");
                } else {

                    movimiento movJugador = movs.get(eleccion - 1);

                    System.out.println("\n" + jugador.getNombre() + " usa " + movJugador.getNombre() + "!");

                    rival.recibirDaño(movJugador.getPotencia());

                    if (!rival.estaVivo()) {
                        System.out.println("\n" + rival.getNombre() + " ha sido debilitado!");
                        jugador.ganarExp(rival.getNivel()*10);
                        break;
                    }
                }

                // Turno rival
                movimiento movRival = rival.getMovimientos().get(0);
                System.out.println("\nEl rival usa " + movRival.getNombre() + "!");
                jugador.recibirDaño(movRival.getPotencia());

                if (!jugador.estaVivo()) {
                    System.out.println("\n" + jugador.getNombre() + " ha sido debilitado...");
                    break;
                }

            }
        }
    }*/

    // ---------------------------
    // SUBIR DE NIVEL
    // ---------------------------
    private void subirNivel() {
        nivel++;
        System.out.println("\n¡" + getNombre() + " subió al nivel " + nivel + "!");

        vidaMax += 3;
        ataque += 2;
        defensa += 2;
        velocidad += 2;

        vidaActual = vidaMax; // Cura al subir de nivel

        System.out.println("Sus estadísticas mejoraron y recuperó toda la vida.");

        // Verifica si puede evolucionar
        if (nivel >= base.getNivelEvolucion() && base.getEvolucion() != null) {
            String nombreEvolucion = base.getEvolucion();
            PokemonBase nuevaBase = Pokedex.getByName(nombreEvolucion);

            if (nuevaBase != null) {
                // Evoluciona: actualiza la base
                String nombreAntiguo = this.base.getNombre();
                this.base = nuevaBase;

                // Recalcula stats basados en la nueva base + IVs existentes
                vidaMax = base.getVidaBase() + ivVida + (nivel * 3);  // Ajusta con crecimiento por nivel
                ataque = base.getAtaqueBase() + ivAtaque + (nivel * 2);
                defensa = base.getDefensaBase() + ivDefensa + (nivel * 2);
                velocidad = base.getVelocidadBase() + ivVelocidad + (nivel * 2);
                vidaActual = vidaMax;  // Cura completa tras evolución

                System.out.println("\n¡Qué! ¡" + nombreAntiguo + " está evolucionando!");
                System.out.println(nombreAntiguo + " ha evolucionado a " + getNombre() + "!");
            } else {
                System.out.println("Error: No se encontró la evolución para " + nombreEvolucion);
            }
        }
    }

    public void curarTotal() {
        this.vidaActual = this.vidaMax;
    }

    public void curar(int i) {
    }

    public boolean aprenderMovimiento(movimiento nuevo) {
        if (nuevo == null) {
            return false;
        }
        for (movimiento m : getMovimientos()) {
            if (m.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                return false; // ya lo conoce
            }
        }
        movimientosAprendidos.add(nuevo);
        return true;
    }


}