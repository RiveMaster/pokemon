package Proyecto.studio.gui;

import Proyecto.studio.BaseDatos;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Estado compartido del editor gráfico: la base de datos en memoria, si hay cambios sin guardar y
 * los avisos a los paneles. Los diálogos son sustituibles (para las pruebas sin pantalla).
 */
public final class Contexto {
    private BaseDatos bd;
    private final Path carpeta;
    private boolean sinGuardar;
    private final List<Runnable> alCambiar = new ArrayList<>();
    private final List<Runnable> alSustituir = new ArrayList<>();
    private Component ventana;

    /** Muestra un error al usuario. */
    public Consumer<String> avisador = msg -> JOptionPane.showMessageDialog(null, msg, "Editor de datos", JOptionPane.ERROR_MESSAGE);
    /** Pide confirmación (true = sí). */
    public Predicate<String> confirmador = msg -> JOptionPane.showConfirmDialog(null, msg, "Editor de datos",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;

    public Contexto(BaseDatos bd, Path carpeta) {
        this.bd = bd;
        this.carpeta = carpeta;
    }

    public BaseDatos bd() { return bd; }
    public Path carpeta() { return carpeta; }
    public boolean sinGuardar() { return sinGuardar; }
    public Component ventana() { return ventana; }
    public void ventana(Component v) { this.ventana = v; }

    /** Se llama tras cualquier modificación de la base. */
    public void cambiado() {
        sinGuardar = true;
        alCambiar.forEach(Runnable::run);
    }

    public void guardado() {
        sinGuardar = false;
        alCambiar.forEach(Runnable::run);
    }

    /** Sustituye la base entera (recargar / reimportar) y refresca todos los paneles. */
    public void sustituir(BaseDatos nueva, boolean marcarSinGuardar) {
        this.bd = nueva;
        this.sinGuardar = marcarSinGuardar;
        alSustituir.forEach(Runnable::run);
        alCambiar.forEach(Runnable::run);
    }

    public void alCambiar(Runnable r) { alCambiar.add(r); }
    public void alSustituir(Runnable r) { alSustituir.add(r); }

    public void error(String mensaje) { avisador.accept(mensaje); }
    public boolean confirmar(String mensaje) { return confirmador.test(mensaje); }
}
