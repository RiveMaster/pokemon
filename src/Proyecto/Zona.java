package Proyecto;

import java.util.Set;

/** Datos de una ruta: qué tipos de Pokémon salen, a qué nivel y con qué conecta. */
public class Zona {
    private final Ubicacion ubicacion;
    private final Set<String> tipos;
    private final int nivelMin;
    private final int nivelMax;
    private final String descripcion;
    private final Ubicacion anterior;
    private final Ubicacion siguiente;      // null si es un callejón sin salida
    private final Ubicacion puebloCercano;  // dónde despiertas si pierdes

    public Zona(Ubicacion ubicacion, Set<String> tipos, int nivelMin, int nivelMax, String descripcion,
                Ubicacion anterior, Ubicacion siguiente, Ubicacion puebloCercano) {
        this.ubicacion = ubicacion;
        this.tipos = tipos;
        this.nivelMin = nivelMin;
        this.nivelMax = nivelMax;
        this.descripcion = descripcion;
        this.anterior = anterior;
        this.siguiente = siguiente;
        this.puebloCercano = puebloCercano;
    }

    public Ubicacion getUbicacion() { return ubicacion; }
    public String getNombre() { return ubicacion.getNombre(); }
    public Set<String> getTipos() { return tipos; }
    public int getNivelMin() { return nivelMin; }
    public int getNivelMax() { return nivelMax; }
    public String getDescripcion() { return descripcion; }
    public Ubicacion getAnterior() { return anterior; }
    public Ubicacion getSiguiente() { return siguiente; }
    public Ubicacion getPuebloCercano() { return puebloCercano; }
}
