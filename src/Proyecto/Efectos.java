package Proyecto;

import java.io.Serializable;

public class Efectos implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private double dano;
    private EstadoAlterado estado;
    private int probabilidad;

    public Efectos(String nombre, double dano, EstadoAlterado estado, int probabilidad) {
        this.nombre = nombre;
        this.dano = dano;
        this.estado = estado;
        this.probabilidad = probabilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getDano() {
        return dano;
    }

    public void setDano(double dano) {
        this.dano = dano;
    }
}
