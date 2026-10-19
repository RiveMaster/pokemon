package Proyecto.studio.gui;

import Proyecto.studio.TablaTipos;
import Proyecto.studio.TipoPokemon;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

/** Matriz de eficacia de tipos: filas = tipo del ataque, columnas = tipo del defensor. */
final class PanelTipos extends JPanel {
    private final Contexto ctx;
    private final Modelo modelo = new Modelo();
    private final JTable tabla = new JTable(modelo);
    private final JLabel resultado = new JLabel(" ");

    private static final Color VERDE = new Color(0xB7E4B0), NARANJA = new Color(0xF7D08A), GRIS = new Color(0x777777),
            ROJO = new Color(0xF4A6A0);

    PanelTipos(Contexto ctx) {
        super(new BorderLayout(6, 6));
        this.ctx = ctx;
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel ayuda = new JLabel("Filas: tipo del ataque · Columnas: tipo del defensor · Doble clic en una casilla para cambiarla");
        ayuda.setFont(ayuda.getFont().deriveFont(Font.ITALIC));
        add(ayuda, BorderLayout.NORTH);

        tabla.setRowHeight(24);
        tabla.setCellSelectionEnabled(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(150);
        for (int c = 1; c <= TipoPokemon.values().length; c++) tabla.getColumnModel().getColumn(c).setPreferredWidth(44);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foco, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (c == 0) {
                    setFont(getFont().deriveFont(Font.BOLD));
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setBackground(new Color(0xE6E6E6));
                    setForeground(Color.BLACK);
                    setText(String.valueOf(v));
                } else {
                    double m = (Double) v;
                    setText(texto(m));
                    setForeground(m == 0 ? Color.WHITE : Color.BLACK);
                    setBackground(m == 0 ? GRIS : m == 2 ? VERDE : m == 0.5 ? NARANJA : m == 1 ? Color.WHITE : ROJO);
                    if (sel) setBackground(getBackground().darker());
                }
                return this;
            }
        };
        tabla.setDefaultRenderer(Object.class, render);
        tabla.setDefaultRenderer(Double.class, render);

        JComboBox<Double> editor = new JComboBox<>(new Double[]{0.0, 0.5, 1.0, 2.0});
        editor.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> l, Object v, int i, boolean s, boolean f) {
                return super.getListCellRendererComponent(l, "×" + texto((Double) v) + (v.equals(0.0) ? "  (sin efecto)" : v.equals(0.5) ? "  (poco eficaz)" : v.equals(2.0) ? "  (muy eficaz)" : ""), i, s, f);
            }
        });
        tabla.setDefaultEditor(Double.class, new DefaultCellEditor(editor));
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // calculadora + restaurar
        Campo<TipoPokemon> atq = Campos.tipo(false), d1 = Campos.tipo(false), d2 = Campos.tipo(true);
        atq.set(TipoPokemon.AGUA);
        d1.set(TipoPokemon.FUEGO);
        d2.set(TipoPokemon.ROCA);
        Runnable calcular = () -> resultado.setText("  = ×" + texto(ctx.bd().tipos().multiplicador(atq.get(), d1.get(), d2.get())));
        for (Campo<TipoPokemon> c : java.util.List.of(atq, d1, d2)) ((JComboBox<?>) c.comp()).addActionListener(e -> calcular.run());
        modelo.addTableModelListener(e -> calcular.run());
        calcular.run();
        JButton restaurar = new JButton("Restaurar tabla oficial");
        restaurar.addActionListener(e -> {
            if (!ctx.confirmar("¿Restaurar toda la tabla de tipos a la oficial?\nSe perderán tus cambios en ella.")) return;
            ctx.bd().tipos(TablaTipos.estandar());
            modelo.fireTableDataChanged();
            ctx.cambiado();
        });
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        sur.add(new JLabel("Calculadora:  ataque"));
        sur.add(atq.comp());
        sur.add(new JLabel("contra"));
        sur.add(d1.comp());
        sur.add(new JLabel("/"));
        sur.add(d2.comp());
        sur.add(resultado);
        sur.add(new JLabel("      "));
        sur.add(restaurar);
        add(sur, BorderLayout.SOUTH);
    }

    static String texto(double m) {
        if (m == 0.5) return "½";
        if (m == (long) m) return Long.toString((long) m);
        return Double.toString(m);
    }

    void refrescar() { modelo.fireTableDataChanged(); }

    JTable tabla() { return tabla; }

    private final class Modelo extends AbstractTableModel {
        private final TipoPokemon[] tipos = TipoPokemon.values();

        public int getRowCount() { return tipos.length; }
        public int getColumnCount() { return tipos.length + 1; }

        @Override
        public String getColumnName(int c) {
            if (c == 0) return "Ataque ↓   Defensa →";
            String n = tipos[c - 1].nombre();
            return n.length() > 3 ? n.substring(0, 3) : n;
        }

        @Override public Class<?> getColumnClass(int c) { return c == 0 ? String.class : Double.class; }
        @Override public boolean isCellEditable(int r, int c) { return c > 0; }

        public Object getValueAt(int r, int c) {
            return c == 0 ? tipos[r].nombre() : (Object) ctx.bd().tipos().multiplicador(tipos[r], tipos[c - 1]);
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            double nuevo = ((Number) v).doubleValue();
            if (nuevo == ctx.bd().tipos().multiplicador(tipos[r], tipos[c - 1])) return;
            ctx.bd().tipos().establecer(tipos[r], tipos[c - 1], nuevo);
            fireTableCellUpdated(r, c);
            ctx.cambiado();
        }
    }
}
