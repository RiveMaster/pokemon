package Proyecto.studio.gui;

import Proyecto.studio.BaseDatos;
import Proyecto.studio.PuenteProyecto;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Editor gráfico (Swing) de la base de datos de Pokémon Studio. Mismas operaciones que el editor de
 * consola: Pokémon, movimientos, habilidades, objetos, tipos, zonas, gimnasios, jefes, generaciones y reglas.
 */
public final class EditorGrafico {
    private EditorGrafico() {}

    /** Los paneles del editor ya enlazados al contexto. {@code alGuardar} reúne lo que hay que aplicar antes de guardar. */
    public static final class Paneles {
        public final JTabbedPane pestañas = new JTabbedPane();
        public final List<Runnable> antesDeGuardar = new ArrayList<>();
        public final Registros<?> pokemon, movimientos, habilidades, objetos, zonas, gimnasios, jefes, generaciones;
        public final PanelReglasAcceso reglas;
        final PanelTipos tipos;

        /** Acceso a lo imprescindible del panel de reglas (para pruebas). */
        public interface PanelReglasAcceso {
            Proyecto.studio.DatosReglas leer();
            void aplicarSiCambio();
            void cargar();
        }

        Paneles(Contexto ctx) {
            pokemon = PanelPokemon.crear(ctx);
            movimientos = PanelesSimples.movimientos(ctx);
            habilidades = PanelesSimples.habilidades(ctx);
            objetos = PanelesSimples.objetos(ctx);
            tipos = new PanelTipos(ctx);
            zonas = PanelesSimples.zonas(ctx);
            gimnasios = PanelesSimples.gimnasios(ctx);
            jefes = PanelesSimples.jefes(ctx);
            generaciones = PanelesSimples.generaciones(ctx);
            PanelReglas pr = new PanelReglas(ctx);
            reglas = new PanelReglasAcceso() {
                public Proyecto.studio.DatosReglas leer() { return pr.leer(); }
                public void aplicarSiCambio() { pr.aplicarSiCambio(); }
                public void cargar() { pr.cargar(); }
            };

            pestañas.addTab("Pokémon", pokemon);
            pestañas.addTab("Movimientos", movimientos);
            pestañas.addTab("Habilidades", habilidades);
            pestañas.addTab("Objetos", objetos);
            pestañas.addTab("Tipos", tipos);
            pestañas.addTab("Zonas", zonas);
            pestañas.addTab("Gimnasios", gimnasios);
            pestañas.addTab("Jefes", jefes);
            pestañas.addTab("Generaciones", generaciones);
            pestañas.addTab("Reglas", pr);

            antesDeGuardar.add(pr::aplicarSiCambio);
            // las reglas se aplican al salir de su pestaña
            int[] anterior = {0};
            pestañas.addChangeListener(e -> {
                if (anterior[0] == pestañas.indexOfComponent(pr)) pr.aplicarSiCambio();
                anterior[0] = pestañas.getSelectedIndex();
            });
            // otro panel pudo cambiar las reglas (p. ej. borrar un Pokémon excluido): se recarga al entrar
            pestañas.addChangeListener(e -> { if (pestañas.getSelectedComponent() == pr) pr.cargar(); });

            ctx.alSustituir(() -> {
                pokemon.recargarFormulario();
                movimientos.recargarFormulario();
                habilidades.recargarFormulario();
                objetos.recargarFormulario();
                zonas.recargarFormulario();
                gimnasios.recargarFormulario();
                jefes.recargarFormulario();
                generaciones.recargarFormulario();
                tipos.refrescar();
                pr.cargar();
            });
        }
    }

    // ------------------------------------------------------------------ operaciones

    /** Valida y guarda en disco. Devuelve false si el usuario cancela. */
    public static boolean guardar(Contexto ctx, Paneles paneles) {
        paneles.antesDeGuardar.forEach(Runnable::run);
        List<String> errores = ctx.bd().validar();
        if (!errores.isEmpty()
                && !ctx.confirmar("Hay " + errores.size() + " problemas de validación:\n" + Util.primeras(errores, 8)
                + "\n¿Guardar de todos modos?")) return false;
        try {
            ctx.bd().guardar(ctx.carpeta());
        } catch (IOException e) {
            ctx.error("No se pudo guardar: " + e.getMessage());
            return false;
        }
        ctx.guardado();
        return true;
    }

    static void mostrarValidacion(Contexto ctx, Paneles paneles) {
        paneles.antesDeGuardar.forEach(Runnable::run);
        List<String> errores = ctx.bd().validar();
        if (errores.isEmpty()) {
            JOptionPane.showMessageDialog(ctx.ventana(), "Todo correcto: no se ha encontrado ningún problema.", "Validación", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextArea t = new JTextArea(String.join("\n", errores), 14, 60);
        t.setEditable(false);
        JOptionPane.showMessageDialog(ctx.ventana(), new JScrollPane(t), errores.size() + " problemas", JOptionPane.WARNING_MESSAGE);
    }

    /** Pregunta si guardar los cambios pendientes. Devuelve false si se cancela la acción. */
    static boolean resolverPendientes(Contexto ctx, Paneles paneles, String accion) {
        paneles.antesDeGuardar.forEach(Runnable::run);
        if (!ctx.sinGuardar()) return true;
        int r = JOptionPane.showOptionDialog(ctx.ventana(), "Hay cambios sin guardar.\n¿Quieres guardarlos antes de " + accion + "?",
                "Cambios sin guardar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Guardar", "Descartar", "Cancelar"}, "Guardar");
        if (r == 0) return guardar(ctx, paneles);
        return r == 1;
    }

    // ---------------------------------------------------------------------- ventana

    public static JFrame crearVentana(Contexto ctx) {
        Paneles paneles = new Paneles(ctx);
        JFrame f = new JFrame();
        ctx.ventana(f);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel estado = new JLabel();
        estado.setBorder(new EmptyBorder(3, 8, 3, 8));
        Runnable actualizar = () -> {
            BaseDatos bd = ctx.bd();
            f.setTitle("Pokémon Studio — " + ctx.carpeta() + (ctx.sinGuardar() ? "  *" : ""));
            estado.setText(bd.especies().size() + " Pokémon · " + bd.movimientos().size() + " movimientos · "
                    + bd.zonas().size() + " zonas · " + bd.gimnasios().size() + " gimnasios     "
                    + (ctx.sinGuardar() ? "● cambios sin guardar" : "✔ guardado"));
        };
        ctx.alCambiar(actualizar);
        actualizar.run();

        f.setJMenuBar(menu(ctx, paneles, f));
        f.add(paneles.pestañas, BorderLayout.CENTER);
        JPanel sur = new JPanel(new BorderLayout());
        sur.add(estado, BorderLayout.WEST);
        f.add(sur, BorderLayout.SOUTH);
        f.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (resolverPendientes(ctx, paneles, "salir")) f.dispose();
            }
        });
        f.setPreferredSize(new Dimension(1100, 720));
        f.pack();
        f.setLocationRelativeTo(null);
        return f;
    }

    private static JMenuBar menu(Contexto ctx, Paneles paneles, JFrame f) {
        int ctrl = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem guardar = new JMenuItem("Guardar");
        guardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrl));
        guardar.addActionListener(e -> guardar(ctx, paneles));
        JMenuItem recargar = new JMenuItem("Recargar del disco");
        recargar.addActionListener(e -> {
            if (ctx.sinGuardar() && !ctx.confirmar("Se perderán los cambios sin guardar.\n¿Recargar del disco?")) return;
            try {
                ctx.sustituir(BaseDatos.cargar(ctx.carpeta()), false);
            } catch (IOException | RuntimeException ex) {
                ctx.error("No se pudo recargar: " + ex.getMessage());
            }
        });
        JMenuItem reimportar = new JMenuItem("Reimportar lo que el juego tiene cargado");
        reimportar.addActionListener(e -> {
            if (!ctx.confirmar("Se sustituirá TODO lo que hay en memoria por lo que el juego tiene cargado ahora.\n¿Continuar?")) return;
            ctx.sustituir(PuenteProyecto.importar().base(), true);
        });
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> { if (resolverPendientes(ctx, paneles, "salir")) f.dispose(); });
        archivo.add(guardar);
        archivo.addSeparator();
        archivo.add(recargar);
        archivo.add(reimportar);
        archivo.addSeparator();
        archivo.add(salir);

        JMenu herramientas = new JMenu("Herramientas");
        JMenuItem validar = new JMenuItem("Validar");
        validar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ctrl));
        validar.addActionListener(e -> mostrarValidacion(ctx, paneles));
        herramientas.add(validar);
        JMenuItem editorMapas = new JMenuItem("Editor de mapas por tiles");
        editorMapas.addActionListener(e -> EditorMapas.abrir());
        herramientas.addSeparator();
        herramientas.add(editorMapas);

        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem acerca = new JMenuItem("Acerca de");
        acerca.addActionListener(e -> JOptionPane.showMessageDialog(f,
                "Editor de datos de Pokémon Studio para Java.\n\nEdita los JSON de " + ctx.carpeta()
                        + ".\nEl juego los carga al arrancar (o al cerrar este editor desde su menú).\n\n"
                        + "Puedes crear hasta 24 lugares nuevos en el mapa: ocupan las ranuras libres del juego.",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        ayuda.add(acerca);

        JMenuBar mb = new JMenuBar();
        mb.add(archivo);
        mb.add(herramientas);
        mb.add(ayuda);
        return mb;
    }

    // ---------------------------------------------------------------------- arranque

    private static void instalarAspecto() {
        try {
            String so = System.getProperty("os.name", "").toLowerCase();
            if (so.contains("win") || so.contains("mac")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                for (UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels())
                    if (i.getName().equals("Nimbus")) UIManager.setLookAndFeel(i.getClassName());
            }
        } catch (Exception ignorada) {
            // se queda con el aspecto por defecto
        }
    }

    /**
     * Abre el editor sobre {@code carpeta}. Si {@code esperar} es true, no vuelve hasta que se cierre la ventana
     * (para usarlo desde el menú del juego).
     */
    public static void abrir(Path carpeta, boolean esperar) throws IOException {
        if (GraphicsEnvironment.isHeadless())
            throw new IllegalStateException("No hay entorno gráfico disponible (usa el editor de consola).");
        BaseDatos bd;
        if (Files.exists(carpeta.resolve("pokemon.json"))) {
            bd = BaseDatos.cargar(carpeta);
        } else {
            int r = JOptionPane.showConfirmDialog(null, "No hay datos en " + carpeta + ".\n¿Importarlos del juego (Pokédex, Ataques, mapa...)?",
                    "Editor de datos", JOptionPane.YES_NO_OPTION);
            bd = r == JOptionPane.YES_OPTION ? PuenteProyecto.importar().base() : new BaseDatos();
        }
        Contexto ctx = new Contexto(bd, carpeta);
        CountDownLatch cerrada = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            instalarAspecto();
            JFrame f = crearVentana(ctx);
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e) { cerrada.countDown(); }
            });
            f.setVisible(true);
        });
        if (esperar) {
            try {
                cerrada.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
