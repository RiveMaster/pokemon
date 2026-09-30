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
    private final String[] entrenadores;    // tipos de NPC que pueden retarte en esta ruta

    public Zona(Ubicacion ubicacion, Set<String> tipos, int nivelMin, int nivelMax, String descripcion,
                Ubicacion anterior, Ubicacion siguiente, Ubicacion puebloCercano, String... entrenadores) {
        this.ubicacion = ubicacion;
        this.tipos = tipos;
        this.nivelMin = nivelMin;
        this.nivelMax = nivelMax;
        this.descripcion = descripcion;
        this.anterior = anterior;
        this.siguiente = siguiente;
        this.puebloCercano = puebloCercano;
        this.entrenadores = entrenadores;
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
    public String[] getEntrenadores() { return entrenadores; }
}
