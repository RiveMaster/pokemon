package Proyecto.studio.gui;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/** Panel de formulario: etiqueta a la izquierda y control a la derecha, con secciones. */
public final class Formulario extends JPanel {
    private int fila = 0;

    public Formulario() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    private GridBagConstraints c(int x, double peso) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x;
        g.gridy = fila;
        g.weightx = peso;
        g.insets = new Insets(3, 3, 3, 3);
        g.anchor = GridBagConstraints.NORTHWEST;
        g.fill = x == 1 ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        return g;
    }

    public <V> Campo<V> fila(String etiqueta, Campo<V> campo) {
        fila(etiqueta, campo.comp());
        return campo;
    }

    public void fila(String etiqueta, JComponent comp) {
        boolean alto = comp.getPreferredSize().height > 45;
        GridBagConstraints l = c(0, 0);
        l.anchor = alto ? GridBagConstraints.NORTHWEST : GridBagConstraints.WEST;
        if (alto) l.insets = new Insets(8, 3, 3, 3);
        add(new JLabel(etiqueta), l);
        GridBagConstraints g = c(1, 1);
        if (comp instanceof JSpinner || comp instanceof JCheckBox || comp instanceof JLabel || comp instanceof JComboBox) {
            g.fill = GridBagConstraints.NONE;      // no estirar controles que no lo necesitan
            g.anchor = GridBagConstraints.WEST;
        }
        add(comp, g);
        fila++;
    }

    /** Un control a todo el ancho, con una etiqueta encima. */
    public void ancho(String etiqueta, JComponent comp) {
        GridBagConstraints g = c(0, 1);
        g.gridwidth = 2;
        if (etiqueta != null) { add(new JLabel(etiqueta), g); fila++; g = c(0, 1); g.gridwidth = 2; }
        g.fill = GridBagConstraints.HORIZONTAL;
        add(comp, g);
        fila++;
    }

    public void seccion(String titulo) {
        JLabel l = new JLabel(titulo);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
        GridBagConstraints g = c(0, 1);
        g.gridwidth = 2;
        g.insets = new Insets(12, 3, 2, 3);
        add(l, g);
        fila++;
        GridBagConstraints s = c(0, 1);
        s.gridwidth = 2;
        s.fill = GridBagConstraints.HORIZONTAL;
        add(new JSeparator(), s);
        fila++;
    }

    /** Empuja todo hacia arriba. Llamar al terminar de añadir filas. */
    public Formulario rellenar() {
        GridBagConstraints g = c(0, 1);
        g.gridwidth = 2;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        add(new JPanel(), g);
        fila++;
        return this;
    }
}
