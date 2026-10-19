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
    private List<Movimiento> movimientosAprendidos = new ArrayList<>();
    private ObjetoEquipable objeto; // objeto equipado (null = ninguno)

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

    // Combate avanzado (solo con las reglas activadas; las partidas antiguas los leen como null)
    private String habilidad;                       // símbolo de la habilidad elegida
    private java.util.Map<String, Integer> ppGastados; // nombre del movimiento -> veces usado desde el último descanso

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
        vidaMax = base.getVidaBase() + ivVida +getNivel();
        ataque = base.getAtaqueBase() + ivAtaque+getNivel();
        defensa = base.getDefensaBase() + ivDefensa+getNivel();
        velocidad = base.getVelocidadBase() + ivVelocidad+getNivel();

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

    public List<Movimiento> getMovimientos() {
        List<Movimiento> todos = new ArrayList<>(base.getMovimientosPorNivel());
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

    /** Los Pokémon de tipo Volador o Dragón (en cualquiera de sus dos tipos) pueden llevar al jugador volando. */
    public boolean puedeVolar() {
        PokemonBase b = getBase();
        return "Flying".equals(b.getTipo1()) || "Dragon".equals(b.getTipo1())
                || "Flying".equals(b.getTipo2()) || "Dragon".equals(b.getTipo2());
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
        this.ppGastados = null; // el descanso también restaura los PP
    }

    // ---------------------------
    // OBJETOS EQUIPABLES
    // ---------------------------
    public ObjetoEquipable getObjeto() {
        return objeto;
    }

    public void setObjeto(ObjetoEquipable objeto) {
        this.objeto = objeto;
    }

    // Recupera vida sin pasar del máximo (no se usa curar(int), que está sin implementar)
    private void restaurarVida(int cantidad) {
        vidaActual = Math.min(vidaMax, vidaActual + cantidad);
    }

    // Efecto de objetos que actúan al inicio de cada turno (Restos)
    public void efectoObjetoInicioTurno() {
        if (objeto == ObjetoEquipable.RESTOS && estaVivo() && vidaActual < vidaMax) {
            restaurarVida(Math.max(1, vidaMax / 16));
            System.out.println(getNombre() + " recupera vida gracias a sus Restos.");
        }
    }

    // ---------------------------
    // COMBATE AVANZADO (reglas opcionales de ReglasCombate)
    // ---------------------------

    /** Habilidad de este Pokémon (se elige al azar entre las de su especie la primera vez que hace falta). */
    public String getHabilidad() {
        java.util.List<String> posibles = base.getHabilidades();
        if (posibles.isEmpty()) return null;
        if (habilidad == null || !posibles.contains(habilidad))
            habilidad = posibles.get(ReglasCombate.azar().nextInt(posibles.size()));
        return habilidad;
    }

    private Habilidades.Definicion habilidadActiva() {
        return ReglasCombate.habilidades() ? Habilidades.de(getHabilidad()) : null;
    }

    public int ppRestantes(Movimiento m) {
        if (m.getPp() <= 0) return Integer.MAX_VALUE; // sin límite
        int usados = ppGastados == null ? 0 : ppGastados.getOrDefault(m.getNombre(), 0);
        return Math.max(0, m.getPp() - usados);
    }

    /** Con la regla de PP desactivada siempre se puede usar. */
    public boolean tienePp(Movimiento m) {
        return !ReglasCombate.pp() || ppRestantes(m) > 0;
    }

    /** true si la regla de PP está activa y ningún movimiento tiene PP. */
    public boolean sinPp() {
        if (!ReglasCombate.pp()) return false;
        for (Movimiento m : getMovimientos()) if (ppRestantes(m) > 0) return false;
        return true;
    }

    private void gastarPp(Movimiento m) {
        if (!ReglasCombate.pp() || m.getPp() <= 0) return;
        if (ppGastados == null) ppGastados = new java.util.HashMap<>();
        ppGastados.merge(m.getNombre(), 1, Integer::sum);
    }

    public void restaurarPp() { ppGastados = null; }

    /** Ataque o ataque especial efectivo (el especial usa la base de la especie y el mismo crecimiento por nivel e IV). */
    private double ataqueEfectivo(Movimiento m) {
        if (m.esEspecial() && base.getAtaqueEspBase() > 0)
            return base.getAtaqueEspBase() + ivAtaque + (ataque - base.getAtaqueBase() - ivAtaque);
        return ataque;
    }

    private double defensaEfectiva(Movimiento m) {
        if (m.esEspecial() && base.getDefensaEspBase() > 0)
            return base.getDefensaEspBase() + ivDefensa + (defensa - base.getDefensaBase() - ivDefensa);
        return defensa;
    }

    /** Usa un movimiento: gasta PP y ataca. Forcejeo (sin PP) tiene retroceso. Sin reglas activas equivale a atacarCon. */
    public void usarMovimiento(Movimiento mov, double danoBase, PokemonLuchador objetivo) {
        boolean forcejeo = mov == Movimiento.FORCEJEO;
        if (!forcejeo) gastarPp(mov);
        atacarCon(mov, danoBase, objetivo);
        if (forcejeo && ReglasCombate.pp() && estaVivo()) {
            recibirDaño(Math.max(1, vidaMax / 4));
            System.out.println(getNombre() + " se hace daño por el retroceso.");
        }
    }

    /** Regeneración de la habilidad al empezar el turno. */
    public void efectoHabilidadInicioTurno() {
        Habilidades.Definicion h = habilidadActiva();
        if (h != null && "regeneracion".equals(h.efecto()) && estaVivo() && vidaActual < vidaMax) {
            restaurarVida(Math.max(1, (int) (vidaMax / Math.max(1.0, h.valor()))));
            System.out.println(getNombre() + " se regenera gracias a " + h.nombre() + ".");
        }
    }

    private void aplicarEstado(EstadoAlterado nuevo) {
        if (estado != EstadoAlterado.NORMAL || !estaVivo()) return;
        estado = nuevo;
        switch (nuevo) {
            case DORMIDO -> {
                turnosDormido = 2 + ReglasCombate.azar().nextInt(2);
                System.out.println(getNombre() + " se ha quedado dormido.");
            }
            case PARALIZADO -> System.out.println(getNombre() + " ha sido paralizado.");
            case ENVENENADO -> System.out.println(getNombre() + " ha sido envenenado.");
            case QUEMADO -> System.out.println(getNombre() + " ha sido quemado.");
            case CONGELADO -> System.out.println(getNombre() + " ha sido congelado.");
            default -> estado = EstadoAlterado.NORMAL; // INSTA / CARGAR no son estados persistentes
        }
    }

    /** Efecto secundario del movimiento (estado alterado o golpe K.O.) con su probabilidad. */
    private void aplicarEfectoDeMovimiento(Movimiento mov, PokemonLuchador objetivo) {
        Efectos e = mov.getEfecto();
        if (e == null || e.getEstado() == null) return;
        if (ReglasCombate.azar().nextInt(100) >= e.getProbabilidad()) return;
        switch (e.getEstado()) {
            case INSTA -> {
                if (objetivo.estaVivo()) {
                    objetivo.vidaActual = 0;
                    System.out.println("¡Es un golpe K.O.!");
                }
            }
            case CARGAR, NORMAL -> { /* los movimientos de carga no están soportados */ }
            default -> { if (objetivo.estaVivo()) objetivo.aplicarEstado(e.getEstado()); }
        }
    }

    // Ataca a un objetivo aplicando los bonus del objeto equipado
    public void atacarCon(Movimiento mov, double danoBase, PokemonLuchador objetivo) {
        // Precisión (regla opcional): un movimiento puede fallar
        if (ReglasCombate.precision()) {
            int p = mov.getPrecision();
            if (p > 0 && p < 100 && ReglasCombate.azar().nextInt(100) >= p) {
                System.out.println("¡" + getNombre() + " falla el ataque!");
                return;
            }
        }

        double dano = danoBase;

        // Categoría y estadísticas (regla opcional): ataque contra defensa, acotado a x0,5 .. x2
        if (ReglasCombate.estadisticas() && dano > 0 && !mov.esDeEstado()) {
            double ratio = ataqueEfectivo(mov) / Math.max(1.0, objetivo.defensaEfectiva(mov));
            dano *= Math.max(0.5, Math.min(2.0, ratio));
        }

        if (objeto != null) {
            switch (objeto) {
                case CINTA_FUERTE -> dano *= 1.10;
                case VIDASFERA -> dano *= 1.30;
                default -> {
                    String tipo = objeto.getTipoPotenciado();
                    if (tipo != null && tipo.equalsIgnoreCase(mov.getTipo())) {
                        dano *= 1.20;
                    }
                }
            }
        }

        // Habilidad del atacante: más potencia con poca vida
        Habilidades.Definicion mia = habilidadActiva();
        if (mia != null && "potencia_tipo".equals(mia.efecto()) && dano > 0 && vidaActual * 3 <= vidaMax
                && mov.getTipo() != null && mov.getTipo().equalsIgnoreCase(mia.tipo())) {
            dano *= mia.valor() > 0 ? mia.valor() : 1.5;
            System.out.println(getNombre() + " potencia su ataque con " + mia.nombre() + ".");
        }

        // Eficacia de tipos (solo si la regla está activada en los datos de Studio)
        double eficacia = Tipos.multiplicador(mov.getTipo(), objetivo.getBase().getTipo1(), objetivo.getBase().getTipo2());
        if (eficacia != 1.0 && dano > 0) {
            dano *= eficacia;
            System.out.println(Tipos.mensaje(eficacia, objetivo.getNombre()));
        }

        // Habilidad del defensor: inmunidad a un tipo
        Habilidades.Definicion suya = objetivo.habilidadActiva();
        if (suya != null && "inmunidad_tipo".equals(suya.efecto()) && dano > 0
                && mov.getTipo() != null && mov.getTipo().equalsIgnoreCase(suya.tipo())) {
            dano = 0;
            System.out.println(objetivo.getNombre() + " es inmune gracias a " + suya.nombre() + ".");
        }

        objetivo.recibirAtaque(dano);

        if (objeto == ObjetoEquipable.VIDASFERA && dano > 0 && estaVivo()) {
            recibirDaño(Math.max(1, vidaMax / 10));
            System.out.println(getNombre() + " pierde vida por la Vidasfera.");
        }

        // Habilidad del defensor: al recibir un golpe puede dejar un estado al atacante
        if (suya != null && "contacto_estado".equals(suya.efecto()) && dano > 0 && estaVivo() && suya.estado() != null
                && ReglasCombate.azar().nextInt(100) < suya.valor()) {
            try {
                aplicarEstado(EstadoAlterado.valueOf(suya.estado()));
            } catch (IllegalArgumentException ignorada) { /* estado desconocido: no hace nada */ }
        }

        // Efecto secundario del movimiento (regla opcional)
        if (ReglasCombate.efectos()) aplicarEfectoDeMovimiento(mov, objetivo);
    }

    // Daño recibido por un ataque (a diferencia de recibirDaño, tiene en cuenta el objeto)
    public void recibirAtaque(double cantidad) {
        double dano = cantidad;

        if (objeto == ObjetoEquipable.PETO_DURO) {
            dano *= 0.85;
        }

        if (objeto == ObjetoEquipable.BANDA_FOCUS && vidaActual == vidaMax && dano >= vidaActual) {
            vidaActual = 1;
            objeto = null; // se consume
            System.out.println(getNombre() + " aguanta el golpe con su Banda Focus.");
            return;
        }

        recibirDaño(dano);

        if (objeto == ObjetoEquipable.BAYA_ARANJA && estaVivo() && vidaActual * 2 <= vidaMax) {
            objeto = null; // se consume
            restaurarVida(30);
            System.out.println(getNombre() + " se come su Baya Aranja y recupera vida.");
        }
    }

    public void curar(int i) {
    }

    public boolean aprenderMovimiento(Movimiento nuevo) {
        if (nuevo == null) {
            return false;
        }
        for (Movimiento m : getMovimientos()) {
            if (m.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                return false; // ya lo conoce
            }
        }
        movimientosAprendidos.add(nuevo);
        return true;
    }


}