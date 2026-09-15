package Proyecto;

public class Efectos {
    private String nombre;
    private int dano;
    private EstadoAlterado estado;
    private int probabilidad;

    public Efectos(String nombre, int dano, EstadoAlterado estado, int probabilidad) {
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

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }
}
