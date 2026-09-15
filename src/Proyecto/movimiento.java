package Proyecto;

import java.io.Serializable;

public class movimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private int potencia;
    private String tipo;
    private int nivelRequerido;
    private Efectos efecto;


    public movimiento(String nombre, int potencia, String tipo, int nivelRequerido, Efectos efecto) {
        this.nombre = nombre;
        this.potencia = potencia;
        this.tipo = tipo;
        this.nivelRequerido = nivelRequerido;
        this.efecto = efecto;
    }
    public movimiento(String nombre, int potencia, String tipo, int nivelRequerido) {
        this.nombre = nombre;
        this.potencia = potencia;
        this.tipo = tipo;
        this.nivelRequerido = nivelRequerido;
    }

    public String getNombre() {
            return nombre;
        }

    public int getPotencia() {
            return potencia;
        }

    public String getTipo() {
            return tipo;
        }

    public int getNivelRequerido() {
            return nivelRequerido;
        }
    }

