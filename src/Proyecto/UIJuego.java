package Proyecto;

import javax.swing.*;
import java.awt.*;

/** Diálogos gráficos compartidos por los menús del juego. */
public final class UIJuego {
    private UIJuego() { }

    public static boolean disponible() {
        return !GraphicsEnvironment.isHeadless();
    }

    public static int elegir(String titulo, String mensaje, String... opciones) {
        if (!disponible()) return -1;
        return JOptionPane.showOptionDialog(null, mensaje, titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones.length == 0 ? null : opciones[0]);
    }

    public static String pedirTexto(String titulo, String mensaje, String inicial) {
        if (!disponible()) return null;
        Object valor = JOptionPane.showInputDialog(null, mensaje, titulo,
                JOptionPane.QUESTION_MESSAGE, null, null, inicial == null ? "" : inicial);
        return valor == null ? null : valor.toString().trim();
    }

    public static void mensaje(String titulo, String mensaje) {
        if (disponible()) JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
        else System.out.println(mensaje);
    }

    public static boolean confirmar(String titulo, String mensaje) {
        return disponible() && JOptionPane.showConfirmDialog(null, mensaje, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
