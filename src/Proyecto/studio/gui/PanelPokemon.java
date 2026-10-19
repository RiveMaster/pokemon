package Proyecto.studio.gui;

import Proyecto.studio.Aprendizaje;
import Proyecto.studio.CurvaExperiencia;
import Proyecto.studio.DatosEspecie;
import Proyecto.studio.DatosEvolucion;
import Proyecto.studio.DatosForma;
import Proyecto.studio.DatosObjeto;
import Proyecto.studio.Estadistica;
import Proyecto.studio.PuenteProyecto;
import Proyecto.studio.Referencias;
import Proyecto.studio.TipoPokemon;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Panel de edición de especies de Pokémon. */
final class PanelPokemon {
    private PanelPokemon() {}

    static Registros<DatosEspecie> crear(Contexto ctx) {
        EditorEspecie ed = new EditorEspecie(ctx);
        return new Registros<>(ctx, "Pokémon", () -> PuenteProyecto.ordenadas(ctx.bd()), DatosEspecie::simbolo,
                e -> String.format("#%-4d %s", e.id(), e.nombre()), ed, e -> ctx.bd().poner(e), e -> {
            List<String> refs = Referencias.deEspecie(ctx.bd(), e.simbolo());
            String msg = "¿Borrar a " + e.nombre() + "?";
            if (!refs.isEmpty()) msg += "\n\nEstá en uso; se quitará de:\n" + Util.primeras(refs, 8);
            if (!ctx.confirmar(msg)) return;
            Referencias.borrarEspecie(ctx.bd(), e.simbolo());
            ctx.cambiado();
        });
    }

    static final class EditorEspecie implements Registros.Editor<DatosEspecie> {
        private final Contexto ctx;
        private final JPanel raiz = new JPanel(new BorderLayout());

        // cabecera
        private final Campo<String> nombre = Campos.texto(16);
        private final Campo<Integer> id = Campos.entero(1, 99999, 1);
        private final JLabel simbolo = new JLabel(" ");
        private final JComboBox<Integer> comboFormas = new JComboBox<>();

        // datos de la forma
        private final Campo<TipoPokemon> tipo1 = Campos.tipo(false);
        private final Campo<TipoPokemon> tipo2 = Campos.tipo(true);
        private final Map<Estadistica, Campo<Integer>> stats = new EnumMap<>(Estadistica.class);
        private final Map<Estadistica, Campo<Integer>> evs = new EnumMap<>(Estadistica.class);
        private final Map<Estadistica, JProgressBar> barras = new EnumMap<>(Estadistica.class);
        private final JLabel total = new JLabel();
        private final Campo<Double> altura = Campos.decimal(0, 100, 0.1);
        private final Campo<Double> peso = Campos.decimal(0, 10000, 0.1);
        private final Campo<CurvaExperiencia> curva = Campos.combo(CurvaExperiencia.values(), CurvaExperiencia::name);
        private final JLabel infoCurva = new JLabel(" ");
        private final Campo<Integer> expBase = Campos.entero(0, 1000, 64);
        private final Campo<Integer> felicidad = Campos.entero(0, 255, 70);
        private final Campo<Integer> captura = Campos.entero(0, 255, 45);
        private final Campo<Double> hembra = Campos.decimal(-1, 100, 12.5);
        private final Campo<Integer> pasos = Campos.entero(0, 100000, 5120);
        private final Campo<String> grupos = Campos.texto(22);

        // listas
        private List<DatosEvolucion> evoluciones = new ArrayList<>();
        private List<Aprendizaje> porNivel = new ArrayList<>();
        private final ModeloEvoluciones modeloEvo = new ModeloEvoluciones();
        private final ModeloMovimientos modeloMov = new ModeloMovimientos();
        private final JTable tablaEvo = new JTable(modeloEvo);
        private final JTable tablaMov = new JTable(modeloMov);
        private final EditorLista mt, huevo, tutor, habilidades;
        private final Campo<String> oculta;

        // estado de edición
        private DatosEspecie trabajo;
        private int formaActual;
        private boolean cambiandoForma;

        EditorEspecie(Contexto ctx) {
            this.ctx = ctx;
            mt = lista("Añadir movimiento (MT/MO)");
            huevo = lista("Añadir movimiento huevo");
            tutor = lista("Añadir movimiento de tutor");
            habilidades = new EditorLista("Añadir habilidad", () -> Opciones.habilidades(ctx), s -> Opciones.habilidad(ctx, s));
            oculta = Campos.referencia(() -> Opciones.habilidades(ctx));
            construir();
        }

        private EditorLista lista(String titulo) {
            return new EditorLista(titulo, () -> Opciones.movimientos(ctx), s -> Opciones.movimiento(ctx, s));
        }

        // ------------------------------------------------------------ construcción

        private void construir() {
            JPanel cab = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            cab.add(new JLabel("Nombre"));
            cab.add(nombre.comp());
            cab.add(new JLabel("Nº"));
            cab.add(id.comp());
            cab.add(simbolo);
            cab.add(new JLabel("   Forma"));
            cab.add(comboFormas);
            JButton mas = new JButton("+");
            mas.setToolTipText("Añadir una forma (copia de la actual)");
            mas.addActionListener(e -> añadirForma());
            JButton menos = new JButton("-");
            menos.setToolTipText("Quitar la forma actual");
            menos.addActionListener(e -> quitarForma());
            cab.add(mas);
            cab.add(menos);
            comboFormas.addActionListener(e -> {
                if (cambiandoForma || comboFormas.getSelectedItem() == null) return;
                volcarForma();
                formaActual = (Integer) comboFormas.getSelectedItem();
                cargarForma(trabajo.forma(formaActual));
            });

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Estadísticas", new JScrollPane(pestañaDatos()));
            tabs.addTab("Cría y experiencia", new JScrollPane(pestañaCria()));
            tabs.addTab("Evoluciones", pestañaEvoluciones());
            tabs.addTab("Movimientos", pestañaMovimientos());
            tabs.addTab("Habilidades", new JScrollPane(pestañaHabilidades()));
            raiz.add(cab, BorderLayout.NORTH);
            raiz.add(tabs, BorderLayout.CENTER);
            raiz.setPreferredSize(new Dimension(620, 560));
        }

        private static final Map<Estadistica, String> NOMBRES = new EnumMap<>(Map.of(
                Estadistica.VIDA, "Vida (PS)", Estadistica.ATAQUE, "Ataque", Estadistica.DEFENSA, "Defensa",
                Estadistica.VELOCIDAD, "Velocidad", Estadistica.ATAQUE_ESP, "Ataque especial",
                Estadistica.DEFENSA_ESP, "Defensa especial"));

        private JComponent pestañaDatos() {
            Formulario f = new Formulario();
            f.fila("Tipo 1", tipo1);
            f.fila("Tipo 2", tipo2);
            f.seccion("Estadísticas base");
            for (Estadistica s : Estadistica.values()) {
                Campo<Integer> c = Campos.entero(0, 255, 50);
                JProgressBar barra = new JProgressBar(0, 255);
                barra.setPreferredSize(new Dimension(160, 14));
                ((JSpinner) c.comp()).addChangeListener(e -> { barra.setValue(c.get()); actualizarTotal(); });
                stats.put(s, c);
                barras.put(s, barra);
                JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                fila.add(c.comp());
                fila.add(barra);
                f.fila(NOMBRES.get(s), fila);
            }
            f.fila("Total", total);
            f.seccion("EVs que da al ser derrotado");
            for (Estadistica s : Estadistica.values()) {
                Campo<Integer> c = Campos.entero(0, 3, 0);
                evs.put(s, c);
                f.fila(NOMBRES.get(s), c);
            }
            return f.rellenar();
        }

        private void actualizarTotal() {
            int t = 0;
            for (Campo<Integer> c : stats.values()) t += c.get();
            total.setText(String.valueOf(t));
        }

        private JComponent pestañaCria() {
            Formulario f = new Formulario();
            f.fila("Altura (m)", altura);
            f.fila("Peso (kg)", peso);
            f.fila("Curva de experiencia", curva);
            f.fila("", infoCurva);
            ((JComboBox<?>) curva.comp()).addActionListener(e -> {
                CurvaExperiencia c = curva.get();
                if (c != null) infoCurva.setText("Nivel 50: " + c.expParaNivel(50) + "  ·  Nivel 100: " + c.expParaNivel(100));
            });
            f.fila("Experiencia base", expBase);
            f.fila("Felicidad base", felicidad);
            f.fila("Ratio de captura (0-255)", captura);
            f.fila("% de hembras (-1 = sin género)", hembra);
            f.fila("Pasos de eclosión", pasos);
            f.fila("Grupos de huevo (separados por comas)", grupos);
            return f.rellenar();
        }

        private JComponent pestañaEvoluciones() {
            tablaEvo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JButton porNivel = new JButton("Añadir por nivel…");
            porNivel.addActionListener(e -> {
                Opcion d = Selector.elegir(raiz, "Evoluciona a", Opciones.especies(ctx));
                if (d == null) return;
                Integer n = pedirNivel("Nivel mínimo", 16);
                if (n != null) añadirEvolucionNivel(d.clave(), n);
            });
            JButton porObjeto = new JButton("Añadir por objeto…");
            porObjeto.addActionListener(e -> {
                Opcion d = Selector.elegir(raiz, "Evoluciona a", Opciones.especies(ctx));
                if (d == null) return;
                Opcion o = Selector.elegir(raiz, "Objeto necesario", Opciones.objetos(ctx));
                if (o != null) añadirEvolucionObjeto(d.clave(), o.clave());
            });
            JButton quitar = new JButton("Quitar");
            quitar.addActionListener(e -> quitarEvolucion(tablaEvo.getSelectedRow()));
            JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
            botones.add(porNivel);
            botones.add(porObjeto);
            botones.add(quitar);
            JPanel p = new JPanel(new BorderLayout());
            p.add(new JScrollPane(tablaEvo), BorderLayout.CENTER);
            p.add(botones, BorderLayout.SOUTH);
            return p;
        }

        private JComponent pestañaMovimientos() {
            tablaMov.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaMov.getColumnModel().getColumn(0).setMaxWidth(80);
            JButton añadir = new JButton("Añadir…");
            añadir.addActionListener(e -> {
                Opcion m = Selector.elegir(raiz, "Movimiento", Opciones.movimientos(ctx));
                if (m == null) return;
                Integer n = pedirNivel("Nivel en que lo aprende", 1);
                if (n != null) añadirMovimiento(m.clave(), n);
            });
            JButton quitar = new JButton("Quitar");
            quitar.addActionListener(e -> quitarMovimiento(tablaMov.getSelectedRow()));
            JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
            botones.add(new JLabel("Por nivel:"));
            botones.add(añadir);
            botones.add(quitar);
            JPanel arriba = new JPanel(new BorderLayout());
            arriba.add(botones, BorderLayout.NORTH);
            arriba.add(new JScrollPane(tablaMov), BorderLayout.CENTER);
            JTabbedPane otras = new JTabbedPane();
            otras.addTab("MT / MO", mt.comp());
            otras.addTab("Huevo", huevo.comp());
            otras.addTab("Tutor", tutor.comp());
            JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, arriba, otras);
            sp.setResizeWeight(0.6);
            sp.setBorder(null);
            return sp;
        }

        private JComponent pestañaHabilidades() {
            Formulario f = new Formulario();
            f.ancho("Habilidades normales", habilidades.comp());
            f.fila("Habilidad oculta", oculta);
            if (ctx.bd().habilidades().isEmpty())
                f.ancho(null, Util.nota("Aún no hay habilidades: créalas en la pestaña «Habilidades» del editor."));
            return f.rellenar();
        }

        private Integer pedirNivel(String texto, int inicial) {
            JSpinner s = new JSpinner(new SpinnerNumberModel(inicial, 0, 100, 1));
            int r = JOptionPane.showConfirmDialog(raiz, new Object[]{texto + ":", s}, "Nivel", JOptionPane.OK_CANCEL_OPTION);
            return r == JOptionPane.OK_OPTION ? ((Number) s.getValue()).intValue() : null;
        }

        // ------------------------------------------------------- operaciones (públicas para pruebas)

        void añadirEvolucionNivel(String destino, int nivel) {
            evoluciones.add(DatosEvolucion.porNivel(destino, nivel));
            modeloEvo.fireTableDataChanged();
        }

        void añadirEvolucionObjeto(String destino, String objeto) {
            evoluciones.add(new DatosEvolucion(destino, Map.of(DatosEvolucion.OBJETO, objeto)));
            modeloEvo.fireTableDataChanged();
        }

        void quitarEvolucion(int fila) {
            if (fila < 0 || fila >= evoluciones.size()) return;
            evoluciones.remove(fila);
            modeloEvo.fireTableDataChanged();
        }

        void añadirMovimiento(String movimiento, int nivel) {
            porNivel.add(new Aprendizaje(movimiento, nivel));
            porNivel.sort(Comparator.comparingInt(Aprendizaje::nivel));
            modeloMov.fireTableDataChanged();
        }

        void quitarMovimiento(int fila) {
            if (fila < 0 || fila >= porNivel.size()) return;
            porNivel.remove(fila);
            modeloMov.fireTableDataChanged();
        }

        EditorLista listaMt() { return mt; }

        // ------------------------------------------------------------------ formas

        private void refrescarComboFormas() {
            cambiandoForma = true;
            try {
                comboFormas.removeAllItems();
                for (DatosForma f : trabajo.formas()) comboFormas.addItem(f.forma());
                comboFormas.setSelectedItem(formaActual);
            } finally {
                cambiandoForma = false;
            }
        }

        private void añadirForma() {
            volcarForma();
            int nueva = trabajo.formas().stream().mapToInt(DatosForma::forma).max().orElse(0) + 1;
            List<DatosForma> l = new ArrayList<>(trabajo.formas());
            l.add(trabajo.forma(formaActual).conNumero(nueva));
            trabajo = trabajo.conFormas(l);
            formaActual = nueva;
            refrescarComboFormas();
            cargarForma(trabajo.forma(formaActual));
        }

        private void quitarForma() {
            if (trabajo.formas().size() == 1) { ctx.error("Una especie necesita al menos una forma."); return; }
            volcarForma();
            List<DatosForma> l = new ArrayList<>(trabajo.formas());
            l.removeIf(f -> f.forma() == formaActual);
            trabajo = trabajo.conFormas(l);
            formaActual = l.get(0).forma();
            refrescarComboFormas();
            cargarForma(trabajo.forma(formaActual));
        }

        // -------------------------------------------------- formulario <-> modelo

        private void cargarForma(DatosForma f) {
            tipo1.set(f.tipo1());
            tipo2.set(f.tipo2());
            for (Estadistica s : Estadistica.values()) {
                stats.get(s).set(f.stat(s));
                barras.get(s).setValue(f.stat(s));
                evs.get(s).set(f.ev(s));
            }
            actualizarTotal();
            altura.set(f.altura());
            peso.set(f.peso());
            curva.set(f.curva());
            infoCurva.setText("Nivel 50: " + f.curva().expParaNivel(50) + "  ·  Nivel 100: " + f.curva().expParaNivel(100));
            expBase.set(f.expBase());
            felicidad.set(f.felicidadBase());
            captura.set(f.ratioCaptura());
            hembra.set(f.ratioHembra());
            pasos.set(f.pasosEclosion());
            grupos.set(String.join(", ", f.gruposHuevo()));
            evoluciones = new ArrayList<>(f.evoluciones());
            porNivel = new ArrayList<>(f.porNivel());
            modeloEvo.fireTableDataChanged();
            modeloMov.fireTableDataChanged();
            mt.set(f.mt());
            huevo.set(f.huevo());
            tutor.set(f.tutor());
            habilidades.set(f.habilidades());
            oculta.set(f.habilidadOculta());
        }

        /** Pasa lo que hay en los controles a la forma actual del registro de trabajo. */
        private void volcarForma() {
            Map<Estadistica, Integer> st = new EnumMap<>(Estadistica.class), ev = new EnumMap<>(Estadistica.class);
            for (Estadistica s : Estadistica.values()) { st.put(s, stats.get(s).get()); ev.put(s, evs.get(s).get()); }
            List<String> g = new ArrayList<>();
            for (String x : grupos.get().split(",")) if (!x.isBlank()) g.add(x.trim());
            DatosForma f = new DatosForma(formaActual, altura.get(), peso.get(), tipo1.get(), tipo2.get(), st, ev,
                    new ArrayList<>(evoluciones), curva.get(), expBase.get(), felicidad.get(), captura.get(),
                    hembra.get(), g, pasos.get(), habilidades.get(), oculta.get(), new ArrayList<>(porNivel),
                    mt.get(), huevo.get(), tutor.get());
            List<DatosForma> l = new ArrayList<>(trabajo.formas());
            for (int i = 0; i < l.size(); i++) if (l.get(i).forma() == formaActual) l.set(i, f);
            trabajo = trabajo.conFormas(l);
        }

        public JComponent componente() { return raiz; }

        public void cargar(DatosEspecie e) {
            if (e == null) {
                int nuevo = ctx.bd().especies().values().stream().mapToInt(DatosEspecie::id).max().orElse(0) + 1;
                Map<Estadistica, Integer> base = new EnumMap<>(Estadistica.class);
                for (Estadistica s : Estadistica.values()) base.put(s, 50);
                trabajo = new DatosEspecie(nuevo, "nuevo", "Nuevo",
                        List.of(DatosForma.porDefecto(TipoPokemon.NORMAL, null).conStats(base)));
                nombre.set("");
                simbolo.setText("(se genera al crear)");
                id.set(nuevo);
            } else {
                trabajo = e;
                nombre.set(e.nombre());
                simbolo.setText(e.simbolo());
                id.set(e.id());
            }
            formaActual = trabajo.formas().get(0).forma();
            refrescarComboFormas();
            cargarForma(trabajo.forma(formaActual));
        }

        public DatosEspecie leer(DatosEspecie actual) {
            volcarForma();
            String n = nombre.get();
            if (n.isBlank()) throw new IllegalArgumentException("El campo 'Nombre' es obligatorio.");
            String sim = actual != null ? actual.simbolo()
                    : PuenteProyecto.unico(PuenteProyecto.simbolo(n), ctx.bd().especies().keySet());
            return new DatosEspecie(id.get(), sim, n, trabajo.formas());
        }

        // ------------------------------------------------------------------- tablas

        private final class ModeloEvoluciones extends AbstractTableModel {
            public int getRowCount() { return evoluciones.size(); }
            public int getColumnCount() { return 2; }
            @Override public String getColumnName(int c) { return c == 0 ? "Evoluciona a" : "Condición"; }

            public Object getValueAt(int r, int c) {
                DatosEvolucion ev = evoluciones.get(r);
                if (c == 0) return Opciones.especie(ctx, ev.destino());
                if (ev.nivelMinimo().isPresent()) return "Nivel " + ev.nivelMinimo().getAsInt();
                String obj = ev.condiciones().get(DatosEvolucion.OBJETO);
                if (obj != null) {
                    DatosObjeto o = ctx.bd().objeto(obj);
                    return "Objeto: " + (o == null ? obj + " (¡no existe!)" : o.nombre());
                }
                return ev.condiciones().isEmpty() ? "(sin condición)" : ev.condiciones().toString();
            }
        }

        private final class ModeloMovimientos extends AbstractTableModel {
            public int getRowCount() { return porNivel.size(); }
            public int getColumnCount() { return 2; }
            @Override public String getColumnName(int c) { return c == 0 ? "Nivel" : "Movimiento"; }

            public Object getValueAt(int r, int c) {
                Aprendizaje a = porNivel.get(r);
                return c == 0 ? (Object) a.nivel() : Opciones.movimiento(ctx, a.movimiento());
            }

            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Integer.class : String.class; }
        }
    }
}
