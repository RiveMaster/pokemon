package Proyecto.studio.gui;

/** Una opción de un desplegable o selector: {@code clave} (símbolo) y el texto que se muestra. */
public record Opcion(String clave, String etiqueta) {
    @Override
    public String toString() { return etiqueta; }
}
