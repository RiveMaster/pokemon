package Proyecto.studio.gui;

import javax.swing.JComponent;

/** Un control de formulario con valor tipado. */
public interface Campo<V> {
    JComponent comp();
    V get();
    void set(V valor);
}
