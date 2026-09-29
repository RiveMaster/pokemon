package Proyecto;

/**
 * Lugares del mapa donde puede estar el jugador (se guarda en la partida).
 * Los nuevos valores van al final para no romper partidas guardadas.
 *
 * Camino: Villaverde - R1 - Oviedo - R2 - R3 - Gijón - R4 - Santander - R5 - Bilbao - R6 - Vitoria - R7
 */
public enum Ubicacion {
    VILLAVERDE("Pueblo Villaverde"),
    RUTA1("Ruta 1"),
    OVIEDO("Ciudad Oviedo"),
    RUTA2("Ruta 2"),
    RUTA3("Ruta 3 (Bosque Cantabria)"),
    GIJON("Ciudad Gijón"),
    RUTA4("Ruta 4 (Costa Cantábrica)"),
    SANTANDER("Ciudad Santander"),
    RUTA5("Ruta 5 (Llanura Tormentosa)"),
    BILBAO("Ciudad Bilbao"),
    RUTA6("Ruta 6 (Cueva Ardiente)"),
    VITORIA("Ciudad Vitoria"),
    RUTA7("Ruta 7 (Sendero Sombrío)");

    private final String nombre;

    Ubicacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
