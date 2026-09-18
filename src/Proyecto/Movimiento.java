package Proyecto;

import java.io.Serializable;

public class Movimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private double potencia;
    private String tipo;
    private int nivelRequerido;
    private Efectos efecto;


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
    }

