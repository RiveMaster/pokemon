package Proyecto.studio.gui;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

/** Diálogo para elegir un elemento de una lista larga escribiendo para filtrar. */
public final class Selector {
    private Selector() {}

    static String norm(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }

    /** Devuelve la opción elegida, o null si se cancela. */
    public static Opcion elegir(Component padre, String titulo, List<Opcion> opciones) {
        Window w = padre == null ? null : SwingUtilities.getWindowAncestor(padre);
        JDialog d = new JDialog(w, titulo, JDialog.ModalityType.APPLICATION_MODAL);
        JTextField filtro = new JTextField();
        DefaultListModel<Opcion> modelo = new DefaultListModel<>();
        JList<Opcion> lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JLabel cuenta = new JLabel(" ");
        Opcion[] resultado = new Opcion[1];

        Runnable filtrar = () -> {
            String q = norm(filtro.getText());
            modelo.clear();
            int n = 0;
            for (Opcion o : opciones) {
                if (q.isEmpty() || norm(o.etiqueta()).contains(q) || norm(o.clave()).contains(q)) { modelo.addElement(o); n++; }
            }
            cuenta.setText(n + " de " + opciones.size());
            if (n > 0) lista.setSelectedIndex(0);
        };
        filtro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar.run(); }
            public void removeUpdate(DocumentEvent e) { filtrar.run(); }
            public void changedUpdate(DocumentEvent e) { filtrar.run(); }
        });
        Runnable aceptar = () -> { resultado[0] = lista.getSelectedValue(); if (resultado[0] != null) d.dispose(); };
        lista.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) aceptar.run(); }
        });
        filtro.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) aceptar.run();
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) lista.setSelectedIndex(Math.min(lista.getSelectedIndex() + 1, modelo.size() - 1));
                else if (e.getKeyCode() == KeyEvent.VK_UP) lista.setSelectedIndex(Math.max(lista.getSelectedIndex() - 1, 0));
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) d.dispose();
            }
        });
        JButton ok = new JButton("Elegir");
        ok.addActionListener(e -> aceptar.run());
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> d.dispose());

        JPanel norte = new JPanel(new BorderLayout(4, 4));
        norte.add(new JLabel("Buscar:"), BorderLayout.WEST);
        norte.add(filtro, BorderLayout.CENTER);
        norte.add(cuenta, BorderLayout.EAST);
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(cancelar);
        sur.add(ok);
        JPanel raiz = new JPanel(new BorderLayout(6, 6));
        raiz.setBorder(new EmptyBorder(10, 10, 10, 10));
        raiz.add(norte, BorderLayout.NORTH);
        raiz.add(new JScrollPane(lista), BorderLayout.CENTER);
        raiz.add(sur, BorderLayout.SOUTH);
        d.setContentPane(raiz);
        d.setPreferredSize(new Dimension(380, 440));
        d.pack();
        d.setLocationRelativeTo(padre);
        filtrar.run();
        d.getRootPane().setDefaultButton(ok);
        d.setVisible(true);
        return resultado[0];
    }
}
