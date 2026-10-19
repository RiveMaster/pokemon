package Proyecto.studio.gui;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Panel maestro/detalle: lista con buscador a la izquierda y formulario a la derecha, con
 * Nuevo / Borrar / Guardar. Si hay cambios sin guardar en el formulario, pregunta antes de descartarlos.
 */
public final class Registros<T> extends JPanel {
    /** El formulario de un tipo de registro. */
    public interface Editor<T> {
        JComponent componente();
        /** Muestra el registro; null = formulario en blanco para uno nuevo. */
        void cargar(T registro);
        /** Construye el registro a partir del formulario; lanza IllegalArgumentException si algo no es válido. */
        T leer(T actual);
    }

    private final Contexto ctx;
    private final String titulo;
    private final Supplier<Collection<T>> items;
    private final Function<T, String> clave;
    private final Function<T, String> etiqueta;
    private final Editor<T> editor;
    private final Consumer<T> guardar;
    private final Consumer<T> borrar;

    private final DefaultListModel<T> modelo = new DefaultListModel<>();
    private final JList<T> lista = new JList<>(modelo);
    private final JTextField buscador = new JTextField();
    private final JLabel cuenta = new JLabel(" ");
    private final JLabel cabecera = new JLabel(" ");
    private final JButton botonGuardar = new JButton("Guardar cambios");
    private final JButton botonBorrar = new JButton("Borrar");
    private final JButton botonNuevo = new JButton("Nuevo");

    private T actual;                 // registro cargado en el formulario (null = nuevo)
    private boolean cargando;

    public Registros(Contexto ctx, String titulo, Supplier<Collection<T>> items, Function<T, String> clave,
                     Function<T, String> etiqueta, Editor<T> editor, Consumer<T> guardar, Consumer<T> borrar) {
        super(new BorderLayout());
        this.ctx = ctx;
        this.titulo = titulo;
        this.items = items;
        this.clave = clave;
        this.etiqueta = etiqueta;
        this.editor = editor;
        this.guardar = guardar;
        this.borrar = borrar;

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            @SuppressWarnings("unchecked")
            public java.awt.Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                return super.getListCellRendererComponent(l, etiqueta.apply((T) v), i, s, f);
            }
        });
        lista.addListSelectionListener(e -> { if (!e.getValueIsAdjusting() && !cargando) alSeleccionar(); });
        buscador.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refrescarLista(); }
            public void removeUpdate(DocumentEvent e) { refrescarLista(); }
            public void changedUpdate(DocumentEvent e) { refrescarLista(); }
        });

        JPanel izq = new JPanel(new BorderLayout(4, 4));
        izq.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));
        JPanel norte = new JPanel(new BorderLayout(4, 2));
        norte.add(new JLabel("Buscar:"), BorderLayout.WEST);
        norte.add(buscador, BorderLayout.CENTER);
        norte.add(cuenta, BorderLayout.SOUTH);
        izq.add(norte, BorderLayout.NORTH);
        izq.add(new JScrollPane(lista), BorderLayout.CENTER);
        JPanel botonesIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        botonesIzq.add(botonNuevo);
        botonesIzq.add(botonBorrar);
        izq.add(botonesIzq, BorderLayout.SOUTH);
        izq.setPreferredSize(new Dimension(270, 400));

        cabecera.setFont(cabecera.getFont().deriveFont(Font.BOLD, 15f));
        cabecera.setBorder(BorderFactory.createEmptyBorder(10, 12, 2, 8));
        JScrollPane centro = new JScrollPane(editor.componente());
        centro.setBorder(BorderFactory.createEmptyBorder());
        centro.getVerticalScrollBar().setUnitIncrement(16);
        JPanel botonesDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        JButton descartar = new JButton("Descartar cambios");
        descartar.addActionListener(e -> cargarEnFormulario(actual));
        botonesDer.add(descartar);
        botonesDer.add(botonGuardar);
        JPanel der = new JPanel(new BorderLayout());
        der.add(cabecera, BorderLayout.NORTH);
        der.add(centro, BorderLayout.CENTER);
        der.add(botonesDer, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izq, der);
        split.setBorder(null);
        split.setContinuousLayout(true);
        split.setResizeWeight(0.0);
        split.setDividerLocation(280);
        add(split, BorderLayout.CENTER);

        botonNuevo.addActionListener(e -> nuevo());
        botonGuardar.addActionListener(e -> guardarActual());
        botonBorrar.addActionListener(e -> borrarActual());
        refrescar();
        cargarEnFormulario(null);
    }

    // ------------------------------------------------------------------- acciones

    private void alSeleccionar() {
        T elegido = lista.getSelectedValue();
        if (elegido == null || elegido == actual) return;
        if (hayCambios() && !ctx.confirmar("Hay cambios sin guardar en este registro.\n¿Descartarlos?")) {
            seleccionarSinEvento(actual);
            return;
        }
        cargarEnFormulario(elegido);
    }

    /** Indica si el formulario difiere del registro cargado (los registros son inmutables: se compara con equals). */
    public boolean hayCambios() {
        if (actual == null) return false;
        try {
            return !editor.leer(actual).equals(actual);
        } catch (RuntimeException e) {
            return true;
        }
    }

    private void cargarEnFormulario(T t) {
        actual = t;
        editor.cargar(t);
        cabecera.setText(t == null ? "Crear " + titulo.toLowerCase() : etiqueta.apply(t));
        botonBorrar.setEnabled(t != null);
        botonGuardar.setText(t == null ? "Crear" : "Guardar cambios");
    }

    private void seleccionarSinEvento(T t) {
        cargando = true;
        try {
            if (t == null) lista.clearSelection();
            else lista.setSelectedValue(t, true);
        } finally {
            cargando = false;
        }
    }

    public void nuevo() {
        if (hayCambios() && !ctx.confirmar("Hay cambios sin guardar en este registro.\n¿Descartarlos?")) return;
        seleccionarSinEvento(null);
        cargarEnFormulario(null);
    }

    public void guardarActual() {
        T nuevo;
        try {
            nuevo = editor.leer(actual);
        } catch (RuntimeException e) {
            ctx.error("No se puede guardar: " + (e.getMessage() == null ? e.toString() : e.getMessage()));
            return;
        }
        guardar.accept(nuevo);
        ctx.cambiado();
        refrescar();
        seleccionar(clave.apply(nuevo));
    }

    public void borrarActual() {
        if (actual == null) return;
        borrar.accept(actual);
        refrescar();
        T sigue = buscar(clave.apply(actual));
        if (sigue == null) { seleccionarSinEvento(null); cargarEnFormulario(null); }
        else seleccionar(clave.apply(sigue));
    }

    // -------------------------------------------------------------------- lista

    private List<T> todos() { return new ArrayList<>(items.get()); }

    private T buscar(String k) {
        for (T t : items.get()) if (clave.apply(t).equals(k)) return t;
        return null;
    }

    /** Vuelve a leer los datos (tras cambios externos) conservando la selección si sigue existiendo. */
    public void refrescar() {
        String k = actual == null ? null : clave.apply(actual);
        refrescarLista();
        if (k != null) {
            T sigue = buscar(k);
            if (sigue != null) { actual = sigue; seleccionarSinEvento(sigue); }
        }
    }

    /** Tras una sustitución completa de la base: recarga el formulario con el registro actualizado. */
    public void recargarFormulario() {
        T sigue = actual == null ? null : buscar(clave.apply(actual));
        refrescarLista();
        seleccionarSinEvento(sigue);
        cargarEnFormulario(sigue);
    }

    private void refrescarLista() {
        String q = Selector.norm(buscador.getText());
        T seleccionado = lista.getSelectedValue();
        cargando = true;
        try {
            modelo.clear();
            int n = 0;
            List<T> todos = todos();
            for (T t : todos) {
                if (q.isEmpty() || Selector.norm(etiqueta.apply(t)).contains(q) || Selector.norm(clave.apply(t)).contains(q)) {
                    modelo.addElement(t);
                    n++;
                }
            }
            cuenta.setText(n == todos.size() ? n + " elementos" : n + " de " + todos.size());
            if (seleccionado != null) lista.setSelectedValue(seleccionado, true);
        } finally {
            cargando = false;
        }
    }

    public void seleccionar(String k) {
        T t = buscar(k);
        if (t == null) return;
        if (modelo.indexOf(t) < 0) buscador.setText("");
        seleccionarSinEvento(t);
        cargarEnFormulario(t);
    }

    // ---------------------------------------------------- acceso para pruebas

    public JList<T> lista() { return lista; }
    public T actual() { return actual; }
    public JTextField buscador() { return buscador; }
    public Editor<T> editor() { return editor; }
}
