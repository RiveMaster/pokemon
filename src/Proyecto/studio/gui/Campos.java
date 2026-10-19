package Proyecto.studio.gui;

import Proyecto.studio.TipoPokemon;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/** Fábrica de controles de formulario con valor tipado. */
public final class Campos {
    private Campos() {}

    private static final Object NINGUNO = new Object() {
        @Override public String toString() { return "(ninguno)"; }
    };

    public static Campo<String> texto(int columnas) {
        JTextField t = new JTextField(columnas);
        return new Campo<>() {
            public JComponent comp() { return t; }
            public String get() { return t.getText().trim(); }
            public void set(String v) { t.setText(v == null ? "" : v); t.setCaretPosition(0); }
        };
    }

    /** Texto de varias líneas. */
    public static Campo<String> area(int filas) {
        JTextArea a = new JTextArea(filas, 24);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(a);
        return new Campo<>() {
            public JComponent comp() { return sp; }
            public String get() { return a.getText().trim(); }
            public void set(String v) { a.setText(v == null ? "" : v); a.setCaretPosition(0); }
        };
    }

    /** Una línea de texto por elemento de la lista (las líneas vacías se ignoran). */
    public static Campo<List<String>> lineas(int filas) {
        JTextArea a = new JTextArea(filas, 24);
        JScrollPane sp = new JScrollPane(a);
        return new Campo<>() {
            public JComponent comp() { return sp; }
            public List<String> get() {
                List<String> l = new ArrayList<>();
                for (String s : a.getText().split("\\R")) if (!s.isBlank()) l.add(s.trim());
                return l;
            }
            public void set(List<String> v) { a.setText(v == null ? "" : String.join("\n", v)); a.setCaretPosition(0); }
        };
    }

    private static void confirmar(JSpinner s) {
        try { s.commitEdit(); } catch (ParseException ignorada) { /* se queda con el último valor válido */ }
    }

    public static Campo<Integer> entero(int min, int max, int inicial) {
        SpinnerNumberModel modelo = new SpinnerNumberModel(inicial, min, max, 1);
        JSpinner s = new JSpinner(modelo);
        s.setPreferredSize(new Dimension(95, s.getPreferredSize().height));
        return new Campo<>() {
            public JComponent comp() { return s; }
            public Integer get() { confirmar(s); return ((Number) s.getValue()).intValue(); }
            public void set(Integer v) {
                int x = v == null ? inicial : v;
                // un dato fuera de rango no se recorta en silencio: el control amplía su rango para conservarlo
                if (x > ((Number) modelo.getMaximum()).intValue()) modelo.setMaximum(x);
                if (x < ((Number) modelo.getMinimum()).intValue()) modelo.setMinimum(x);
                s.setValue(x);
            }
        };
    }

    public static Campo<Double> decimal(double min, double max, double paso) {
        SpinnerNumberModel modelo = new SpinnerNumberModel(Math.max(min, 0.0), min, max, paso);
        JSpinner s = new JSpinner(modelo);
        s.setEditor(new JSpinner.NumberEditor(s, "0.####"));
        s.setPreferredSize(new Dimension(120, s.getPreferredSize().height));
        return new Campo<>() {
            public JComponent comp() { return s; }
            public Double get() { confirmar(s); return ((Number) s.getValue()).doubleValue(); }
            public void set(Double v) {
                double x = v == null ? 0.0 : v;
                if (x > ((Number) modelo.getMaximum()).doubleValue()) modelo.setMaximum(x);
                if (x < ((Number) modelo.getMinimum()).doubleValue()) modelo.setMinimum(x);
                s.setValue(x);
            }
        };
    }

    public static Campo<Boolean> check(String texto) {
        JCheckBox c = new JCheckBox(texto);
        return new Campo<>() {
            public JComponent comp() { return c; }
            public Boolean get() { return c.isSelected(); }
            public void set(Boolean v) { c.setSelected(Boolean.TRUE.equals(v)); }
        };
    }

    // ----------------------------------------------------------------- desplegables

    public static <T> Campo<T> combo(T[] valores, Function<T, String> nombre) {
        return combo(valores, nombre, false);
    }

    public static <T> Campo<T> comboOpcional(T[] valores, Function<T, String> nombre) {
        return combo(valores, nombre, true);
    }

    @SuppressWarnings("unchecked")
    private static <T> Campo<T> combo(T[] valores, Function<T, String> nombre, boolean opcional) {
        DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
        if (opcional) m.addElement(NINGUNO);
        for (T v : valores) m.addElement(v);
        JComboBox<Object> c = new JComboBox<>(m);
        c.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> l, Object v, int i, boolean s, boolean f) {
                return super.getListCellRendererComponent(l, v == NINGUNO || v == null ? "(ninguno)" : nombre.apply((T) v), i, s, f);
            }
        });
        ensanchar(c);
        return new Campo<>() {
            public JComponent comp() { return c; }
            public T get() { Object o = c.getSelectedItem(); return o == NINGUNO ? null : (T) o; }
            public void set(T v) { c.setSelectedItem(v == null && opcional ? NINGUNO : v); }
        };
    }

    public static Campo<TipoPokemon> tipo(boolean opcional) {
        return opcional ? comboOpcional(TipoPokemon.values(), TipoPokemon::nombre)
                : combo(TipoPokemon.values(), TipoPokemon::nombre);
    }

    /** Referencia opcional por símbolo: las opciones se vuelven a pedir cada vez que se despliega. */
    public static Campo<String> referencia(Supplier<List<Opcion>> proveedor) {
        JComboBox<Object> c = new JComboBox<>();
        Runnable rellenar = () -> {
            Object actual = c.getSelectedItem();
            String clave = actual instanceof Opcion o ? o.clave() : null;
            cargar(c, proveedor.get(), clave);
        };
        c.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) { rellenar.run(); }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
            public void popupMenuCanceled(PopupMenuEvent e) { }
        });
        cargar(c, proveedor.get(), null);
        ensanchar(c);
        return new Campo<>() {
            public JComponent comp() { return c; }
            public String get() { return c.getSelectedItem() instanceof Opcion o ? o.clave() : null; }
            public void set(String clave) { cargar(c, proveedor.get(), clave); }
        };
    }

    private static void ensanchar(JComboBox<?> c) {
        Dimension d = c.getPreferredSize();
        c.setPreferredSize(new Dimension(Math.max(d.width, 230), d.height));
    }

    private static void cargar(JComboBox<Object> c, List<Opcion> opciones, String clave) {
        DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
        m.addElement(NINGUNO);
        Object elegido = NINGUNO;
        for (Opcion o : opciones) {
            m.addElement(o);
            if (o.clave().equals(clave)) elegido = o;
        }
        if (clave != null && elegido == NINGUNO) {            // no perder un valor que ya no existe
            Opcion rota = new Opcion(clave, clave + "  (¡no existe!)");
            m.addElement(rota);
            elegido = rota;
        }
        c.setModel(m);
        c.setSelectedItem(elegido);
    }

    // --------------------------------------------------------------------- tipos

    /** Casillas de los 18 tipos; se conserva el orden en que se marcan (el primero es el principal). */
    public static Campo<List<TipoPokemon>> tipos() {
        JPanel p = new JPanel(new GridLayout(3, 6, 4, 0));
        List<TipoPokemon> orden = new ArrayList<>();
        List<JCheckBox> casillas = new ArrayList<>();
        for (TipoPokemon t : TipoPokemon.values()) {
            JCheckBox c = new JCheckBox(t.nombre());
            c.addActionListener(e -> {
                if (c.isSelected()) { if (!orden.contains(t)) orden.add(t); } else orden.remove(t);
            });
            casillas.add(c);
            p.add(c);
        }
        return new Campo<>() {
            public JComponent comp() { return p; }
            public List<TipoPokemon> get() { return new ArrayList<>(orden); }
            public void set(List<TipoPokemon> v) {
                orden.clear();
                if (v != null) orden.addAll(v);
                for (int i = 0; i < casillas.size(); i++) casillas.get(i).setSelected(orden.contains(TipoPokemon.values()[i]));
            }
        };
    }
}
