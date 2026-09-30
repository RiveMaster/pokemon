package Proyecto;

/**
 * Lugares del mapa donde puede estar el jugador (se guarda en la partida).
 * Los nuevos valores van al final para no romper partidas guardadas.
 *
 * Camino (el orden completo está en Mapa.getRecorrido()):
 *   Villaverde - R1 - Nava - R11 - Oviedo* - R2 - Llanes - R3 - Gijón* - R4 - Comillas(casino) - R8 - Santander*
 *   - R5 - Laredo - R9 - Bilbao* - R10 - Orduña - R6 - Vitoria*(sede del Gobierno) - R7 - Aldea Sombría
 * (* = ciudad con gimnasio; el resto no tiene gimnasio)
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
    RUTA7("Ruta 7 (Sendero Sombrío)"),
    LLANES("Pueblo Llanes"),          // sin gimnasio: entre la Ruta 2 y la Ruta 3
    ALDEA_SOMBRIA("Aldea Sombría"),   // sin gimnasio: al final de la Ruta 7
    NAVA("Pueblo Nava"),              // sin gimnasio: entre la Ruta 1 y la Ruta 11
    COMILLAS("Pueblo Comillas"),      // sin gimnasio, con casino: entre la Ruta 4 y la Ruta 8
    LAREDO("Pueblo Laredo"),          // sin gimnasio: entre la Ruta 5 y la Ruta 9
    ORDUNA("Pueblo Orduña"),          // sin gimnasio: entre la Ruta 10 y la Ruta 6
    RUTA8("Ruta 8 (Camino de la Ría)"),
    RUTA9("Ruta 9 (Marisma Eléctrica)"),
    RUTA10("Ruta 10 (Hayedo Silencioso)"),
    RUTA11("Ruta 11 (Sendas de la Sidra)");

    private final String nombre;

    Ubicacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
