package Proyecto;

import java.io.Serializable;

public class Movimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private double potencia;
    private String tipo;
    private int nivelRequerido;
    private Efectos efecto;

    // Datos de combate avanzado (solo se usan si las reglas correspondientes están activadas).
    // Las partidas guardadas antiguas no tienen estos campos y los leen como 0 (= sin límite / nunca falla).
    private int precision = 100;     // 0..100; 0 = nunca falla
    private int pp = 20;             // 0 = sin límite
    private int prioridad = 0;       // se guarda pero el orden de turno del juego no la usa
    private String categoria;        // "physical", "special" o "status"; null = según la potencia

    /** Movimiento de último recurso cuando no quedan PP. */
    public static final Movimiento FORCEJEO = new Movimiento("Forcejeo", 20, "Normal", 0);
    static { FORCEJEO.pp = 0; }


    public Movimiento(String nombre, double potencia, String tipo, int nivelRequerido, Efectos efecto) {
        this.nombre = nombre;
        this.potencia = potencia;
        this.tipo = tipo;
        this.nivelRequerido = nivelRequerido;
        this.efecto = efecto;
    }
    public Movimiento(String nombre, double potencia, String tipo, int nivelRequerido) {
        this.nombre = nombre;
        this.potencia = potencia;
        this.tipo = tipo;
        this.nivelRequerido = nivelRequerido;
    }

    public String getNombre() {
            return nombre;
        }

    public double getPotencia() {
            return potencia;
        }

    public String getTipo() {
            return tipo;
        }

    public int getNivelRequerido() {
            return nivelRequerido;
        }

    public Efectos getEfecto() {
            return efecto;
        }

    public int getPrecision() { return precision; }

    public int getPp() { return pp; }

    public int getPrioridad() { return prioridad; }

    /** "physical", "special" o "status" (si no se ha fijado: físico si tiene potencia, de estado si no). */
    public String getCategoria() {
        if (categoria != null) return categoria;
        return potencia > 0 ? "physical" : "status";
    }

    public boolean esDeEstado() { return "status".equals(getCategoria()); }

    public boolean esEspecial() { return "special".equals(getCategoria()); }

    /** Lo usa el cargador de datos de Studio. */
    public void configurar(String categoria, int precision, int pp, int prioridad) {
        this.categoria = categoria;
        this.precision = precision;
        this.pp = pp;
        this.prioridad = prioridad;
    }
    }

