package Proyecto.studio.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

/** Utilidades pequeñas de interfaz. */
final class Util {
    private Util() {}

    /** Habilita o deshabilita un control y todo lo que contiene. */
    static void habilitar(Component c, boolean si) {
        c.setEnabled(si);
        if (c instanceof Container k) for (Component h : k.getComponents()) habilitar(h, si);
    }

    static void habilitar(Campo<?> c, boolean si) { habilitar(c.comp(), si); }

    static JLabel nota(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(l.getFont().deriveFont(Font.ITALIC));
        return l;
    }

    static String mensaje(RuntimeException e) {
        return e.getMessage() == null ? e.toString() : e.getMessage();
    }

    static String primeras(java.util.List<String> l, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(n, l.size()); i++) sb.append("  • ").append(l.get(i)).append('\n');
        if (l.size() > n) sb.append("  … y ").append(l.size() - n).append(" más\n");
        return sb.toString();
    }

    static void asegurarVisible(JComponent c) { c.scrollRectToVisible(c.getBounds()); }
}
