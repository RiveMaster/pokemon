package Proyecto.studio.gui;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/** Lista editable de símbolos (movimientos, habilidades, especies...) con Añadir / Quitar. */
public final class EditorLista implements Campo<List<String>> {
    private final JPanel panel = new JPanel(new BorderLayout(4, 4));
    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(modelo);
    private final Function<String, String> etiqueta;

    /**
     * @param titulo    título del selector al pulsar Añadir
     * @param opciones  elementos disponibles (se piden en cada Añadir, así están siempre al día)
     * @param etiqueta  cómo mostrar un símbolo (p. ej. nombre del movimiento)
     */
    public EditorLista(String titulo, Supplier<List<Opcion>> opciones, Function<String, String> etiqueta) {
        this.etiqueta = etiqueta;
        lista.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                return super.getListCellRendererComponent(l, etiqueta.apply((String) v), i, s, f);
            }
        });
        JButton añadir = new JButton("Añadir…");
        añadir.addActionListener(e -> {
            Opcion o = Selector.elegir(panel, titulo, opciones.get());
            if (o != null) añadir(o.clave());
        });
        JButton quitar = new JButton("Quitar");
        quitar.addActionListener(e -> quitarSeleccionado());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        botones.add(añadir);
        botones.add(quitar);
        JScrollPane sp = new JScrollPane(lista);
        sp.setPreferredSize(new Dimension(200, 110));
        panel.add(sp, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
    }

    public void añadir(String simbolo) {
        if (!modelo.contains(simbolo)) modelo.addElement(simbolo);
    }

    public void quitarSeleccionado() {
        int i = lista.getSelectedIndex();
        if (i >= 0) modelo.remove(i);
    }

    public JComponent comp() { return panel; }

    public List<String> get() {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < modelo.size(); i++) l.add(modelo.get(i));
        return l;
    }

    public void set(List<String> v) {
        modelo.clear();
        if (v != null) v.forEach(modelo::addElement);
    }

    public String etiquetaDe(String simbolo) { return etiqueta.apply(simbolo); }
}
