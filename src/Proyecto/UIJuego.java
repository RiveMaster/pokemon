package Proyecto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Sistema visual común: paleta Pokémon, paneles redondeados y controles grandes.
 * Todos los menús principales utilizan esta clase para mantener una apariencia coherente.
 */
public final class UIJuego {
    private static final Color AZUL = new Color(37, 91, 160);
    private static final Color AZUL_OSCURO = new Color(24, 55, 102);
    private static final Color ROJO = new Color(220, 65, 65);
    private static final Color FONDO = new Color(239, 245, 250);
    private static final Color TEXTO = new Color(37, 49, 66);
    private static final Font FUENTE = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font TITULO = new Font("SansSerif", Font.BOLD, 23);

    private UIJuego() { }

    public static boolean disponible() {
        return !GraphicsEnvironment.isHeadless();
    }

    public static int elegir(String titulo, String mensaje, String... opciones) {
        if (!disponible() || opciones == null || opciones.length == 0) return -1;
        final int[] seleccion = {-1};
        JDialog dialogo = crearDialogo(titulo, mensaje);
        JPanel botones = new JPanel(new GridLayout(0, opciones.length > 4 ? 2 : 1, 9, 9));
        botones.setOpaque(false);
        for (int i = 0; i < opciones.length; i++) {
            final int indice = i;
            JButton boton = boton(opciones[i], i == 0);
            boton.addActionListener(e -> { seleccion[0] = indice; dialogo.dispose(); });
            botones.add(boton);
        }
        JPanel contenido = (JPanel) dialogo.getContentPane().getComponent(1);
        contenido.add(botones, BorderLayout.SOUTH);
        dialogo.getRootPane().registerKeyboardAction(e -> dialogo.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialogo.pack();
        dialogo.setMinimumSize(new Dimension(410, dialogo.getHeight()));
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);
        return seleccion[0];
    }

    public static String pedirTexto(String titulo, String mensaje, String inicial) {
        if (!disponible()) return null;
        JDialog dialogo = crearDialogo(titulo, mensaje);
        JTextField campo = new JTextField(inicial == null ? "" : inicial, 22);
        campo.setFont(FUENTE);
        campo.setForeground(TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 207, 224), 1, true),
                new EmptyBorder(11, 12, 11, 12)));
        JPanel contenido = (JPanel) dialogo.getContentPane().getComponent(1);
        contenido.add(campo, BorderLayout.CENTER);
        final String[] valor = {null};
        JButton aceptar = boton("Aceptar", true);
        JButton cancelar = boton("Cancelar", false);
        JPanel botones = new JPanel(new GridLayout(1, 2, 9, 0));
        botones.setOpaque(false);
        botones.add(aceptar);
        botones.add(cancelar);
        contenido.add(botones, BorderLayout.SOUTH);
        aceptar.addActionListener(e -> { valor[0] = campo.getText().trim(); dialogo.dispose(); });
        cancelar.addActionListener(e -> dialogo.dispose());
        campo.addActionListener(e -> aceptar.doClick());
        dialogo.getRootPane().setDefaultButton(aceptar);
        dialogo.getRootPane().registerKeyboardAction(e -> dialogo.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialogo.pack();
        dialogo.setMinimumSize(new Dimension(410, dialogo.getHeight()));
        dialogo.setLocationRelativeTo(null);
        campo.requestFocusInWindow();
        dialogo.setVisible(true);
        return valor[0];
    }

    public static void mensaje(String titulo, String mensaje) {
        if (!disponible()) { System.out.println(mensaje); return; }
        JDialog dialogo = crearDialogo(titulo, mensaje);
        JPanel contenido = (JPanel) dialogo.getContentPane().getComponent(1);
        JButton aceptar = boton("Continuar", true);
        aceptar.addActionListener(e -> dialogo.dispose());
        JPanel botones = new JPanel(new BorderLayout());
        botones.setOpaque(false);
        botones.add(aceptar, BorderLayout.EAST);
        contenido.add(botones, BorderLayout.SOUTH);
        dialogo.getRootPane().setDefaultButton(aceptar);
        dialogo.pack();
        dialogo.setMinimumSize(new Dimension(390, dialogo.getHeight()));
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);
    }

    public static boolean confirmar(String titulo, String mensaje) {
        if (!disponible()) return false;
        return elegir(titulo, mensaje, "Sí", "No") == 0;
    }

    private static JDialog crearDialogo(String titulo, String mensaje) {
        Window propietario = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        JDialog dialogo = new JDialog(propietario, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialogo.setResizable(false);
        dialogo.getContentPane().setLayout(new BorderLayout());
        JPanel cabecera = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, AZUL, getWidth(), getHeight(), AZUL_OSCURO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillOval(getWidth() - 76, -38, 112, 112);
                g2.dispose();
            }
        };
        cabecera.setBorder(new EmptyBorder(17, 22, 17, 22));
        JLabel marca = new JLabel("POKÉMON");
        marca.setForeground(new Color(255, 224, 105));
        marca.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel encabezado = new JLabel(titulo == null ? "Pokémon" : titulo);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(TITULO);
        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 3));
        titulos.setOpaque(false);
        titulos.add(marca);
        titulos.add(encabezado);
        cabecera.add(titulos, BorderLayout.CENTER);

        JPanel contenido = new JPanel(new BorderLayout(0, 18));
        contenido.setBackground(FONDO);
        contenido.setBorder(new EmptyBorder(20, 22, 20, 22));
        JTextArea texto = new JTextArea(mensaje == null ? "" : mensaje);
        texto.setFont(FUENTE);
        texto.setForeground(TEXTO);
        texto.setBackground(FONDO);
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setColumns(30);
        texto.setRows(Math.max(2, Math.min(5, (mensaje == null ? 0 : mensaje.length()) / 43 + 1)));
        contenido.add(texto, BorderLayout.NORTH);
        dialogo.getContentPane().add(cabecera, BorderLayout.NORTH);
        dialogo.getContentPane().add(contenido, BorderLayout.CENTER);
        return dialogo;
    }

    private static JButton boton(String texto, boolean principal) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = principal ? AZUL : Color.WHITE;
                if (getModel().isPressed()) base = principal ? AZUL_OSCURO : new Color(222, 233, 244);
                else if (getModel().isRollover()) base = principal ? new Color(51, 112, 187) : new Color(235, 243, 250);
                g2.setColor(base);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setForeground(principal ? Color.WHITE : TEXTO);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(principal ? AZUL : new Color(197, 211, 225), 1, true),
                new EmptyBorder(11, 15, 11, 15)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
