package Proyecto;

/**
 * Lugares del mapa donde puede estar el jugador. Se guarda en la partida
 * para poder reanudar exactamente donde se dejó el juego.
 */
public enum Ubicacion {
    VILLAVERDE("Pueblo Villaverde"),
    RUTA1("Ruta 1"),
    OVIEDO("Ciudad Oviedo"),
    RUTA2("Ruta 2");

    private final String nombre;

    Ubicacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
