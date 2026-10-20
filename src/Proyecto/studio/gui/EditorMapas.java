package Proyecto.studio.gui;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Editor de mapas por tiles para Pokémon Studio. No requiere dependencias externas.
 * Los mapas se guardan en formato binario .pmap e incluyen la paleta de tiles.
 */
public final class EditorMapas {
    private static final int TILE = 16, ZOOM = 2, MAGIC = 0x504D4150, VERSION = 2;
    private final JFrame frame = new JFrame("Pokémon Studio — Editor de mapas");
    private int ancho = 20, alto = 15, seleccionado = 0, capa = 0;
    private final List<BufferedImage> tiles = new ArrayList<>();
    private final List<ObjetoMapa> objetos = new ArrayList<>();
    private final List<ColocacionObjeto> colocaciones = new ArrayList<>();
    private int objetoSeleccionado = -1;
    private boolean modoObjeto = false;
    private JList<ObjetoMapa> listaObjetos;
    private int[][] capas = new int[3][ancho * alto];
    private boolean[] colisiones = new boolean[ancho * alto];
    private boolean modoColision = false, rejilla = true;
    private final MapaCanvas canvas = new MapaCanvas();
    private final DefaultListModel<Integer> modeloPaleta = new DefaultListModel<>();
    private final JList<Integer> listaPaleta = new JList<>(modeloPaleta);
    private final JLabel estado = new JLabel("Capa: Suelo · clic izquierdo pinta, derecho borra");
    private final JComboBox<String> selectorCapa = new JComboBox<>(new String[]{"Suelo", "Objetos", "Superior", "Hierba", "Evento", "Agua"});

    private EditorMapas() {
        for (int[] capaInicial : capas) java.util.Arrays.fill(capaInicial, -1);
        crearTiles();
        if (tiles.isEmpty() && !hayPngEnCarpetaTiles()) crearTilesEjemplo();
        seleccionado = tiles.isEmpty() ? -1 : 0;
        for (int i = 0; i < tiles.size(); i++) modeloPaleta.addElement(i);
        listaPaleta.setSelectedIndex(seleccionado);
        construirVentana();
    }

    public static void abrir() {
        if (GraphicsEnvironment.isHeadless()) throw new IllegalStateException("No hay entorno gráfico disponible.");
        SwingUtilities.invokeLater(() -> new EditorMapas().frame.setVisible(true));
    }

    private void construirVentana() {
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(6, 6));
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton nuevo = new JButton("Nuevo mapa");
        nuevo.addActionListener(e -> nuevoMapa());
        JButton importar = new JButton("Importar PNG");
        importar.addActionListener(e -> importarPng());
        JButton guardar = new JButton("Guardar proyecto");
        guardar.addActionListener(e -> guardar());
        JButton abrir = new JButton("Abrir proyecto");
        abrir.addActionListener(e -> cargar());
        JButton exportar = new JButton("Exportar PNG");
        exportar.addActionListener(e -> exportarPng());
        JButton col = new JButton("Pintar colisiones");
        col.addActionListener(e -> { modoColision = !modoColision; actualizarEstado(); canvas.repaint(); });
        JCheckBox cuadricula = new JCheckBox("Cuadrícula", true);
        cuadricula.addActionListener(e -> { rejilla = cuadricula.isSelected(); canvas.repaint(); });
        barra.add(nuevo); barra.add(abrir); barra.add(guardar); barra.add(importar); barra.add(exportar); barra.add(col); barra.add(cuadricula);
        frame.add(barra, BorderLayout.NORTH);

        selectorCapa.addActionListener(e -> { capa = selectorCapa.getSelectedIndex(); actualizarEstado(); canvas.repaint(); });
        listaPaleta.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listaPaleta.setVisibleRowCount(-1);
        listaPaleta.setFixedCellWidth(54);
        listaPaleta.setFixedCellHeight(54);
        listaPaleta.setCellRenderer(new TileRenderer());
        listaPaleta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPaleta.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaPaleta.getSelectedValue() != null) {
                seleccionado = listaPaleta.getSelectedValue();
                modoObjeto = false;
                actualizarEstado();
            }
        });
        JTabbedPane herramientas = new JTabbedPane();
        herramientas.addTab("Tiles", crearPanelTiles());
        herramientas.addTab("Crear objetos", crearPanelObjetos());
        herramientas.addChangeListener(e -> {
            modoObjeto = herramientas.getSelectedIndex() == 1 && objetoSeleccionado >= 0;
            actualizarEstado();
            canvas.repaint();
        });
        herramientas.setPreferredSize(new Dimension(310, 600));
        frame.add(herramientas, BorderLayout.WEST);

        JScrollPane mapaScroll = new JScrollPane(canvas);
        mapaScroll.getViewport().setBackground(new Color(65, 65, 65));
        frame.add(mapaScroll, BorderLayout.CENTER);
        estado.setBorder(new EmptyBorder(4, 8, 6, 8));
        frame.add(estado, BorderLayout.SOUTH);
        frame.setSize(1100, 760);
        frame.setLocationRelativeTo(null);
        actualizarEstado();
    }

    private static String nombreCapa(int c) { return new String[]{"Suelo", "Objetos", "Superior"}[c]; }
    private void actualizarEstado() {
        String herramienta = modoColision ? "MODO COLISIONES" :
                (modoObjeto && objetoSeleccionado >= 0 && objetoSeleccionado < objetos.size()
                        ? "Objeto: " + objetos.get(objetoSeleccionado).nombre : "Tile: " + seleccionado);
        estado.setText(herramienta + " · Capa: " + nombreCapa(capa) + " · " + ancho + "×" + alto
                + " · clic izq. coloca/pinta, der. borra");
    }

    private void nuevoMapa() {
        JTextField w = new JTextField(String.valueOf(ancho)), h = new JTextField(String.valueOf(alto));
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.add(new JLabel("Ancho en tiles:")); p.add(w); p.add(new JLabel("Alto en tiles:")); p.add(h);
        if (JOptionPane.showConfirmDialog(frame, p, "Nuevo mapa", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            int nw = Integer.parseInt(w.getText().trim()), nh = Integer.parseInt(h.getText().trim());
            if (nw < 1 || nh < 1 || nw > 300 || nh > 300) throw new NumberFormatException();
            ancho = nw; alto = nh; capas = new int[3][ancho * alto];
            for (int[] c : capas) java.util.Arrays.fill(c, -1);
            colisiones = new boolean[ancho * alto];
            colocaciones.clear();
            canvas.revalidate(); canvas.repaint(); actualizarEstado();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Usa dimensiones entre 1 y 300 tiles.", "Dimensiones no válidas", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void importarPng() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Importar spritesheet PNG (se divide en tiles de 16×16)");
        if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        try {
            BufferedImage img = ImageIO.read(fc.getSelectedFile());
            if (img == null || img.getWidth() % TILE != 0 || img.getHeight() % TILE != 0)
                throw new IOException("La imagen debe ser PNG y sus dimensiones múltiplos de 16 píxeles.");
            int n = 0;
            int vacios = 0;
            for (int y = 0; y < img.getHeight(); y += TILE) {
                for (int x = 0; x < img.getWidth(); x += TILE) {
                    BufferedImage tile = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = tile.createGraphics();
                    g.drawImage(img, 0, 0, TILE, TILE, x, y, x + TILE, y + TILE, null);
                    g.dispose();
                    if (!tienePixelVisible(tile)) {
                        vacios++;
                        continue;
                    }
                    tiles.add(tile);
                    modeloPaleta.addElement(tiles.size() - 1);
                    n++;
                }
            }
            if (n > 0) listaPaleta.setSelectedIndex(tiles.size() - n);
            JOptionPane.showMessageDialog(frame, "Importados " + n + " tiles de " + fc.getSelectedFile().getName()
                    + (vacios > 0 ? ". Se omitieron " + vacios + " tiles totalmente transparentes." : ".")
                    + (n == 0 ? "\nNo se ha añadido ningún tile: todos estaban vacíos." : ""));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "No se pudo importar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private JPanel crearPanelTiles() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(new JLabel("Tiles (16 × 16)"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listaPaleta), BorderLayout.CENTER);
        JButton limpiar = new JButton("Vaciar capa");
        limpiar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(frame, "¿Vaciar la capa " + nombreCapa(capa) + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                java.util.Arrays.fill(capas[capa], -1);
                canvas.repaint();
            }
        });
        JPanel opciones = new JPanel(new BorderLayout(4, 4));
        opciones.add(selectorCapa, BorderLayout.NORTH);
        opciones.add(limpiar, BorderLayout.SOUTH);
        panel.add(opciones, BorderLayout.SOUTH);
        return panel;
    }

    /** Pestaña de biblioteca para crear y reutilizar objetos compuestos por tiles. */
    private JPanel crearPanelObjetos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(6, 6, 6, 6));
        panel.add(new JLabel("<html><b>Objetos compuestos</b><br>Árboles, casas, gimnasios...</html>"), BorderLayout.NORTH);
        JList<ObjetoMapa> lista = new JList<>();
        listaObjetos = lista;
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setListData(objetos.toArray(new ObjetoMapa[0]));
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                objetoSeleccionado = lista.getSelectedIndex();
                modoObjeto = objetoSeleccionado >= 0;
                actualizarEstado();
                canvas.repaint();
            }
        });

        JButton nuevo = new JButton("Crear objeto...");
        nuevo.addActionListener(e -> editarObjeto(null, -1, lista));
        JButton editar = new JButton("Editar seleccionado");
        editar.addActionListener(e -> {
            int idx = lista.getSelectedIndex();
            if (idx >= 0 && idx < objetos.size()) editarObjeto(objetos.get(idx), idx, lista);
        });
        JButton borrar = new JButton("Eliminar objeto");
        borrar.addActionListener(e -> {
            int idx = lista.getSelectedIndex();
            if (idx < 0 || idx >= objetos.size()) return;
            ObjetoMapa obj = objetos.get(idx);
            if (JOptionPane.showConfirmDialog(frame,
                    "Eliminar «" + obj.nombre + "» y todas sus colocaciones del mapa?",
                    "Eliminar objeto", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            objetos.remove(idx);
            colocaciones.removeIf(p -> p.objeto == idx);
            for (ColocacionObjeto p : colocaciones) if (p.objeto > idx) p.objeto--;
            objetoSeleccionado = -1;
            modoObjeto = false;
            lista.clearSelection();
            lista.setListData(objetos.toArray(new ObjetoMapa[0]));
            canvas.repaint();
            actualizarEstado();
        });
        JButton pintarTiles = new JButton("Volver a pintar tiles");
        pintarTiles.addActionListener(e -> {
            objetoSeleccionado = -1;
            modoObjeto = false;
            lista.clearSelection();
            actualizarEstado();
            canvas.repaint();
        });
        JPanel botones = new JPanel(new GridLayout(0, 1, 4, 4));
        botones.add(nuevo);
        botones.add(editar);
        botones.add(borrar);
        botones.add(pintarTiles);
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void editarObjeto(ObjetoMapa original, int indiceOriginal, JList<ObjetoMapa> lista) {
        JDialog dialogo = new JDialog(frame, original == null ? "Crear objeto" : "Editar objeto", true);
        dialogo.setLayout(new BorderLayout(6, 6));
        JTextField nombre = new JTextField(original == null ? "Nuevo objeto" : original.nombre, 18);
        int anchoInicial = original == null ? 3 : original.ancho;
        int altoInicial = original == null ? 3 : original.alto;
        JSpinner columnas = new JSpinner(new SpinnerNumberModel(anchoInicial, 1, 32, 1));
        JSpinner filas = new JSpinner(new SpinnerNumberModel(altoInicial, 1, 32, 1));
        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        encabezado.add(new JLabel("Nombre:"));
        encabezado.add(nombre);
        encabezado.add(new JLabel("Ancho (tiles):"));
        encabezado.add(columnas);
        encabezado.add(new JLabel("Alto (tiles):"));
        encabezado.add(filas);

        final int[] tileActual = {seleccionado};
        int[] datosIniciales = new int[anchoInicial * altoInicial];
        java.util.Arrays.fill(datosIniciales, -1);
        if (original != null) {
            for (int y = 0; y < Math.min(original.alto, altoInicial); y++)
                for (int x = 0; x < Math.min(original.ancho, anchoInicial); x++)
                    datosIniciales[y * anchoInicial + x] = original.celdas[y * original.ancho + x];
        }
        final LienzoObjeto lienzo = new LienzoObjeto(anchoInicial, altoInicial, datosIniciales,
                () -> tileActual[0]);
        columnas.addChangeListener(e -> lienzo.redimensionar((Integer) columnas.getValue(), (Integer) filas.getValue()));
        filas.addChangeListener(e -> lienzo.redimensionar((Integer) columnas.getValue(), (Integer) filas.getValue()));

        JList<Integer> paleta = new JList<>(modeloPaleta);
        paleta.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        paleta.setVisibleRowCount(-1);
        paleta.setFixedCellWidth(48);
        paleta.setFixedCellHeight(48);
        paleta.setCellRenderer(new TileRenderer());
        paleta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (tileActual[0] >= 0 && tileActual[0] < tiles.size()) paleta.setSelectedIndex(tileActual[0]);
        paleta.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && paleta.getSelectedValue() != null)
                tileActual[0] = paleta.getSelectedValue();
        });

        JPanel contenidoCentro = new JPanel(new BorderLayout(5, 5));
        contenidoCentro.add(new JLabel("Pinta el objeto: clic izquierdo coloca tile, clic derecho borra."),
                BorderLayout.NORTH);
        contenidoCentro.add(new JScrollPane(lienzo), BorderLayout.CENTER);
        JPanel ladoPaleta = new JPanel(new BorderLayout());
        ladoPaleta.add(new JLabel("Selecciona un tile"), BorderLayout.NORTH);
        ladoPaleta.add(new JScrollPane(paleta), BorderLayout.CENTER);
        ladoPaleta.setPreferredSize(new Dimension(190, 390));

        JPanel contenido = new JPanel(new BorderLayout(6, 6));
        contenido.add(encabezado, BorderLayout.NORTH);
        contenido.add(contenidoCentro, BorderLayout.CENTER);
        contenido.add(ladoPaleta, BorderLayout.EAST);
        dialogo.add(contenido, BorderLayout.CENTER);

        JButton guardarObjeto = new JButton("Guardar objeto");
        guardarObjeto.addActionListener(e -> {
            String n = nombre.getText().trim();
            if (n.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Escribe un nombre para el objeto.",
                        "Falta el nombre", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ObjetoMapa guardado = new ObjetoMapa(n, lienzo.getColumnas(), lienzo.getFilas(), lienzo.getCeldas());
            if (indiceOriginal >= 0 && indiceOriginal < objetos.size()) objetos.set(indiceOriginal, guardado);
            else objetos.add(guardado);
            lista.setListData(objetos.toArray(new ObjetoMapa[0]));
            int seleccionar = indiceOriginal >= 0 ? indiceOriginal : objetos.size() - 1;
            lista.setSelectedIndex(seleccionar);
            objetoSeleccionado = seleccionar;
            modoObjeto = true;
            actualizarEstado();
            canvas.repaint();
            dialogo.dispose();
        });
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dialogo.dispose());
        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inferior.add(cancelar);
        inferior.add(guardarObjeto);
        dialogo.add(inferior, BorderLayout.SOUTH);
        dialogo.setSize(820, 570);
        dialogo.setLocationRelativeTo(frame);
        dialogo.setVisible(true);
    }

    private final class LienzoObjeto extends JPanel {
        private int columnas, filas;
        private int[] celdas;
        private final java.util.function.IntSupplier tileActual;

        LienzoObjeto(int columnas, int filas, int[] iniciales, java.util.function.IntSupplier tileActual) {
            this.columnas = columnas;
            this.filas = filas;
            this.celdas = iniciales.clone();
            this.tileActual = tileActual;
            setBackground(Color.WHITE);
            MouseAdapter raton = new MouseAdapter() {
                private void pintar(MouseEvent e) {
                    int x = e.getX() / 32, y = e.getY() / 32;
                    if (x < 0 || y < 0 || x >= LienzoObjeto.this.columnas || y >= LienzoObjeto.this.filas) return;
                    celdas[y * LienzoObjeto.this.columnas + x] = esClicDerecho(e) ? -1 : LienzoObjeto.this.tileActual.getAsInt();
                    repaint(x * 32, y * 32, 32, 32);
                }
                @Override public void mousePressed(MouseEvent e) { pintar(e); }
                @Override public void mouseDragged(MouseEvent e) { pintar(e); }
            };
            addMouseListener(raton);
            addMouseMotionListener(raton);
        }

        void redimensionar(int nuevasColumnas, int nuevasFilas) {
            int[] nuevas = new int[nuevasColumnas * nuevasFilas];
            java.util.Arrays.fill(nuevas, -1);
            for (int y = 0; y < Math.min(filas, nuevasFilas); y++)
                for (int x = 0; x < Math.min(columnas, nuevasColumnas); x++)
                    nuevas[y * nuevasColumnas + x] = celdas[y * columnas + x];
            columnas = nuevasColumnas;
            filas = nuevasFilas;
            celdas = nuevas;
            revalidate();
            repaint();
        }

        int getColumnas() { return columnas; }
        int getFilas() { return filas; }
        int[] getCeldas() { return celdas.clone(); }
        @Override public Dimension getPreferredSize() { return new Dimension(columnas * 32, filas * 32); }

        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setColor(new Color(238, 238, 238));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            for (int y = 0; y < filas; y++) for (int x = 0; x < columnas; x++) {
                int tile = celdas[y * columnas + x];
                if (tile >= 0 && tile < tiles.size()) g.drawImage(tiles.get(tile), x * 32, y * 32, 32, 32, null);
                g.setColor(new Color(0, 0, 0, 100));
                g.drawRect(x * 32, y * 32, 32, 32);
            }
            g.dispose();
        }
    }

    private static final class ObjetoMapa {
        final String nombre;
        final int ancho, alto;
        final int[] celdas;
        ObjetoMapa(String nombre, int ancho, int alto, int[] celdas) {
            this.nombre = nombre;
            this.ancho = ancho;
            this.alto = alto;
            this.celdas = celdas.clone();
        }
        @Override public String toString() { return nombre + " (" + ancho + "×" + alto + ")"; }
    }

    private static final class ColocacionObjeto {
        int objeto, x, y, capa;
        ColocacionObjeto(int objeto, int x, int y, int capa) {
            this.objeto = objeto;
            this.x = x;
            this.y = y;
            this.capa = capa;
        }
    }

    private static boolean esClicDerecho(MouseEvent e) {
        return SwingUtilities.isRightMouseButton(e)
                || (e.getModifiersEx() & java.awt.event.InputEvent.BUTTON3_DOWN_MASK) != 0;
    }

    private void colocarObjeto(int x, int y, int capaActual) {
        if (objetoSeleccionado >= 0 && objetoSeleccionado < objetos.size())
            colocaciones.add(new ColocacionObjeto(objetoSeleccionado, x, y, capaActual));
    }

    private void borrarObjetoEn(int x, int y) {
        for (int i = colocaciones.size() - 1; i >= 0; i--) {
            ColocacionObjeto p = colocaciones.get(i);
            if (p.objeto < 0 || p.objeto >= objetos.size()) continue;
            ObjetoMapa o = objetos.get(p.objeto);
            if (x >= p.x && y >= p.y && x < p.x + o.ancho && y < p.y + o.alto) {
                colocaciones.remove(i);
                return;
            }
        }
    }

private void guardar() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("mapa.pmap"));
        if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        Path path = fc.getSelectedFile().toPath();
        if (!path.toString().toLowerCase(java.util.Locale.ROOT).endsWith(".pmap")) path = Path.of(path + ".pmap");
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            out.writeInt(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(ancho);
            out.writeInt(alto);
            out.writeInt(tiles.size());
            for (BufferedImage tile : tiles) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ImageIO.write(tile, "png", bytes);
                byte[] data = bytes.toByteArray();
                out.writeInt(data.length);
                out.write(data);
            }
            for (int[] layer : capas) for (int v : layer) out.writeInt(v);
            for (boolean b : colisiones) out.writeBoolean(b);

            out.writeInt(objetos.size());
            for (ObjetoMapa objeto : objetos) {
                out.writeUTF(objeto.nombre);
                out.writeInt(objeto.ancho);
                out.writeInt(objeto.alto);
                for (int tile : objeto.celdas) out.writeInt(tile);
            }
            out.writeInt(colocaciones.size());
            for (ColocacionObjeto p : colocaciones) {
                out.writeInt(p.objeto);
                out.writeInt(p.x);
                out.writeInt(p.y);
                out.writeInt(p.capa);
            }
            JOptionPane.showMessageDialog(frame, "Proyecto guardado en:\n" + path.toAbsolutePath());
        } catch (IOException ex) {
            error("No se pudo guardar el proyecto: " + ex.getMessage());
        }
    }

    private void cargar() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Abrir proyecto de mapa");
        if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(fc.getSelectedFile().toPath())))) {
            if (in.readInt() != MAGIC) throw new IOException("El archivo no es un proyecto .pmap válido.");
            int version = in.readInt();
            if (version < 1 || version > VERSION) throw new IOException("Versión de proyecto no compatible: " + version);
            int nw = in.readInt(), nh = in.readInt(), count = in.readInt();
            if (nw < 1 || nh < 1 || nw > 300 || nh > 300 || count < 0 || count > 10000
                    || (version == 1 && count == 0))
                throw new IOException("Dimensiones o paleta inválidas.");

            List<BufferedImage> newTiles = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int len = in.readInt();
                if (len < 1 || len > 10_000_000) throw new IOException("Tile corrupto.");
                byte[] data = in.readNBytes(len);
                if (data.length != len) throw new EOFException();
                BufferedImage tile = ImageIO.read(new ByteArrayInputStream(data));
                if (tile == null) throw new IOException("No se pudo leer un tile.");
                BufferedImage fixed = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tg = fixed.createGraphics();
                tg.drawImage(tile, 0, 0, TILE, TILE, null);
                tg.dispose();
                newTiles.add(fixed);
            }

            int size = nw * nh;
            int[][] newLayers = new int[3][size];
            for (int[] layer : newLayers) {
                for (int i = 0; i < size; i++) {
                    int v = in.readInt();
                    if (v < -1 || v >= count) throw new IOException("Índice de tile fuera de rango.");
                    layer[i] = v;
                }
            }
            boolean[] newCollisions = new boolean[size];
            for (int i = 0; i < size; i++) newCollisions[i] = in.readBoolean();

            List<ObjetoMapa> nuevosObjetos = new ArrayList<>();
            List<ColocacionObjeto> nuevasColocaciones = new ArrayList<>();
            if (version >= 2) {
                int numeroObjetos = in.readInt();
                if (numeroObjetos < 0 || numeroObjetos > 10000) throw new IOException("Cantidad de objetos inválida.");
                for (int i = 0; i < numeroObjetos; i++) {
                    String nombre = in.readUTF();
                    int ow = in.readInt(), oh = in.readInt();
                    if (ow < 1 || oh < 1 || ow > 32 || oh > 32) throw new IOException("Dimensiones de objeto inválidas.");
                    int[] celdasObjeto = new int[ow * oh];
                    for (int j = 0; j < celdasObjeto.length; j++) {
                        int t = in.readInt();
                        if (t < -1 || t >= count) throw new IOException("Índice de tile de objeto fuera de rango.");
                        celdasObjeto[j] = t;
                    }
                    nuevosObjetos.add(new ObjetoMapa(nombre, ow, oh, celdasObjeto));
                }
                int numeroColocaciones = in.readInt();
                if (numeroColocaciones < 0 || numeroColocaciones > 1_000_000)
                    throw new IOException("Cantidad de objetos colocados inválida.");
                for (int i = 0; i < numeroColocaciones; i++) {
                    int objeto = in.readInt(), x = in.readInt(), y = in.readInt(), capaObjeto = in.readInt();
                    if (objeto < 0 || objeto >= nuevosObjetos.size() || x < 0 || y < 0
                            || x >= nw || y >= nh || capaObjeto < 0 || capaObjeto > 2)
                        throw new IOException("Colocación de objeto inválida.");
                    nuevasColocaciones.add(new ColocacionObjeto(objeto, x, y, capaObjeto));
                }
            }

            ancho = nw;
            alto = nh;
            tiles.clear();
            tiles.addAll(newTiles);
            capas = newLayers;
            colisiones = newCollisions;
            objetos.clear();
            objetos.addAll(nuevosObjetos);
            if (listaObjetos != null) {
                listaObjetos.clearSelection();
                listaObjetos.setListData(objetos.toArray(new ObjetoMapa[0]));
            }
            colocaciones.clear();
            colocaciones.addAll(nuevasColocaciones);
            objetoSeleccionado = -1;
            modoObjeto = false;
            modeloPaleta.clear();
            for (int i = 0; i < tiles.size(); i++) modeloPaleta.addElement(i);
            seleccionado = tiles.isEmpty() ? -1 : 0;
            listaPaleta.setSelectedIndex(seleccionado);
            canvas.revalidate();
            canvas.repaint();
            actualizarEstado();
            JOptionPane.showMessageDialog(frame, "Proyecto cargado correctamente.");
        } catch (IOException | RuntimeException ex) {
            error("No se pudo abrir el proyecto: " + ex.getMessage());
        }
    }

    private void exportarPng() {
        JFileChooser fc = new JFileChooser(); fc.setSelectedFile(new java.io.File("mapa.png"));
        if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        Path path = fc.getSelectedFile().toPath();
        if (!path.toString().toLowerCase().endsWith(".png")) path = Path.of(path + ".png");
        BufferedImage img = new BufferedImage(ancho * TILE, alto * TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        dibujarContenido(g, 1);
        g.dispose();
        try { ImageIO.write(img, "png", path.toFile()); JOptionPane.showMessageDialog(frame, "PNG exportado (" + img.getWidth() + "×" + img.getHeight() + "):\n" + path.toAbsolutePath()); }
        catch (IOException ex) { error("No se pudo exportar el PNG: " + ex.getMessage()); }
    }

    private void error(String msg) { JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    /**
     * Carga PNG desde la carpeta Tiles/, relativa al directorio desde el que se inicia el juego.
     * Acepta PNG sueltos de 16x16 y spritesheets cuyas dimensiones sean múltiplos de 16.
     */
    private void crearTiles() {
        Path ruta = Path.of("Tiles");
        if (!Files.isDirectory(ruta)) return; // Se usarán los tiles de ejemplo si no existe la carpeta.

        try (java.util.stream.Stream<Path> archivos = Files.list(ruta)) {
            List<Path> pngs = archivos
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(java.util.Locale.ROOT).endsWith(".png"))
                    .sorted(java.util.Comparator.comparing(
                            p -> p.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .toList();

            for (Path archivo : pngs) {
                BufferedImage imagen = ImageIO.read(archivo.toFile());
                if (imagen == null) {
                    System.err.println("No se pudo leer como PNG: " + archivo);
                    continue;
                }
                if (imagen.getWidth() % TILE != 0 || imagen.getHeight() % TILE != 0) {
                    System.err.println("Se omite " + archivo + ": ancho y alto deben ser múltiplos de " + TILE + " píxeles.");
                    continue;
                }

                for (int y = 0; y < imagen.getHeight(); y += TILE) {
                    for (int x = 0; x < imagen.getWidth(); x += TILE) {
                        BufferedImage tile = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = tile.createGraphics();
                        g.drawImage(imagen, 0, 0, TILE, TILE, x, y, x + TILE, y + TILE, null);
                        g.dispose();
                        if (tienePixelVisible(tile)) tiles.add(tile);
                    }
                }
            }
        } catch (IOException e) {
            error("No se pudieron importar los tiles desde " + ruta.toAbsolutePath() + ": " + e.getMessage());
        }
    }

    private boolean hayPngEnCarpetaTiles() {
        Path ruta = Path.of("Tiles");
        if (!Files.isDirectory(ruta)) return false;
        try (java.util.stream.Stream<Path> archivos = Files.list(ruta)) {
            return archivos.anyMatch(p -> Files.isRegularFile(p)
                    && p.getFileName().toString().toLowerCase(java.util.Locale.ROOT).endsWith(".png"));
        } catch (IOException ex) {
            return false;
        }
    }

    /** Un tile cuenta como ocupado si al menos un píxel tiene alfa distinto de cero. */
    private static boolean tienePixelVisible(BufferedImage imagen) {
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                if (((imagen.getRGB(x, y) >>> 24) & 0xFF) != 0) return true;
            }
        }
        return false;
    }

    private void crearTilesEjemplo() {
        tiles.add(tile(new Color(78, 160, 72), new Color(100, 185, 82), 1));
        tiles.add(tile(new Color(210, 190, 125), new Color(231, 211, 151), 2));
        tiles.add(tile(new Color(75, 140, 205), new Color(110, 180, 230), 3));
        tiles.add(tile(new Color(120, 120, 120), new Color(165, 165, 165), 4));
        tiles.add(tile(new Color(130, 90, 52), new Color(166, 121, 70), 5));
        tiles.add(tile(new Color(55, 125, 65), new Color(35, 90, 45), 6));
        tiles.add(tile(new Color(45, 115, 190), new Color(80, 155, 220), 7));
        tiles.add(tile(new Color(150, 190, 225), new Color(185, 215, 235), 8));
        tiles.add(tile(new Color(220, 220, 190), new Color(245, 240, 210), 9));
        tiles.add(tile(new Color(140, 65, 55), new Color(190, 90, 70), 10));
        tiles.add(tile(new Color(170, 150, 95), new Color(200, 180, 120), 11));
        tiles.add(tile(new Color(45, 45, 55), new Color(80, 80, 95), 12));
    }

    private BufferedImage tile(Color base, Color detalle, int tipo) {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics(); g.setColor(base); g.fillRect(0, 0, TILE, TILE);
        g.setColor(detalle);
        for (int y = 1; y < TILE; y += 4) for (int x = (y % 2 == 0 ? 1 : 3); x < TILE; x += 5) g.fillRect(x, y, 2, 2);
        switch (tipo) {
            case 3 -> { g.setColor(new Color(230, 240, 250)); g.drawLine(0, 5, 15, 5); g.drawLine(0, 12, 15, 12); }
            case 4 -> { g.setColor(Color.DARK_GRAY); g.drawRect(1, 1, 13, 13); g.drawLine(0, 0, 15, 15); }
            case 5 -> { g.setColor(new Color(85, 55, 30)); g.fillRect(2, 2, 12, 12); g.setColor(detalle); g.fillRect(4, 4, 8, 8); }
            case 6 -> { g.setColor(new Color(30, 75, 35)); g.fillOval(1, 0, 14, 14); g.setColor(new Color(100, 65, 35)); g.fillRect(7, 9, 3, 7); }
            case 9 -> { g.setColor(new Color(100, 140, 190)); g.drawRect(2, 2, 11, 11); g.drawLine(2, 7, 13, 7); }
            case 10 -> { g.setColor(new Color(90, 45, 35)); g.fillRect(1, 1, 14, 14); g.setColor(detalle); g.fillRect(3, 3, 10, 10); }
        }
        g.dispose(); return im;
    }

    private void dibujarContenido(Graphics2D g, int escala) {
        int tam = TILE * escala;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        for (int capaActual = 0; capaActual < 3; capaActual++) {
            for (int i = 0; i < ancho * alto; i++) {
                int t = capas[capaActual][i];
                if (t >= 0 && t < tiles.size()) {
                    int x = (i % ancho) * tam;
                    int y = (i / ancho) * tam;
                    g.drawImage(tiles.get(t), x, y, tam, tam, null);
                }
            }
            // Los objetos se dibujan en su capa, después de los tiles de esa capa.
            for (ColocacionObjeto p : colocaciones) {
                if (p.capa != capaActual || p.objeto < 0 || p.objeto >= objetos.size()) continue;
                ObjetoMapa objeto = objetos.get(p.objeto);
                for (int oy = 0; oy < objeto.alto; oy++) {
                    for (int ox = 0; ox < objeto.ancho; ox++) {
                        int t = objeto.celdas[oy * objeto.ancho + ox];
                        if (t < 0 || t >= tiles.size()) continue;
                        int x = (p.x + ox) * tam;
                        int y = (p.y + oy) * tam;
                        g.drawImage(tiles.get(t), x, y, tam, tam, null);
                    }
                }
            }
        }
    }

    private final class MapaCanvas extends JPanel {
        MapaCanvas() {
            setBackground(new Color(50, 50, 50));
            setPreferredSize(new Dimension(ancho * TILE * ZOOM, alto * TILE * ZOOM));
            MouseAdapter mouse = new MouseAdapter() {
                private int ultimoStampX = -1, ultimoStampY = -1;

                private void pintar(MouseEvent e) {
                    int x = e.getX() / (TILE * ZOOM), y = e.getY() / (TILE * ZOOM);
                    if (x < 0 || y < 0 || x >= ancho || y >= alto) return;
                    int i = y * ancho + x;
                    boolean derecho = esClicDerecho(e);
                    if (modoColision) {
                        colisiones[i] = !derecho;
                    } else if (modoObjeto && objetoSeleccionado >= 0 && objetoSeleccionado < objetos.size()) {
                        if (derecho) {
                            borrarObjetoEn(x, y);
                            ultimoStampX = x;
                            ultimoStampY = y;
                        } else if (x != ultimoStampX || y != ultimoStampY) {
                            colocarObjeto(x, y, capa);
                            ultimoStampX = x;
                            ultimoStampY = y;
                        }
                    } else {
                        capas[capa][i] = derecho ? -1 : seleccionado;
                    }
                    repaint();
                }

                @Override public void mousePressed(MouseEvent e) { pintar(e); }
                @Override public void mouseDragged(MouseEvent e) { pintar(e); }
                @Override public void mouseReleased(MouseEvent e) {
                    ultimoStampX = -1;
                    ultimoStampY = -1;
                }
            };
            addMouseListener(mouse);
            addMouseMotionListener(mouse);
        }

        @Override public Dimension getPreferredSize() {
            return new Dimension(ancho * TILE * ZOOM, alto * TILE * ZOOM);
        }

        @Override protected void paintComponent(Graphics gg) {
            super.paintComponent(gg);
            Graphics2D g = (Graphics2D) gg.create();
            dibujarContenido(g, ZOOM);
            for (int i = 0; i < ancho * alto; i++) {
                int x = (i % ancho) * TILE * ZOOM;
                int y = (i / ancho) * TILE * ZOOM;
                if (modoColision && colisiones[i]) {
                    g.setColor(new Color(235, 45, 45, 115));
                    g.fillRect(x, y, TILE * ZOOM, TILE * ZOOM);
                }
                if (rejilla) {
                    g.setColor(new Color(0, 0, 0, 75));
                    g.drawRect(x, y, TILE * ZOOM, TILE * ZOOM);
                }
            }
            g.dispose();
        }
    }

    private final class TileRenderer extends JLabel implements ListCellRenderer<Integer> {
        TileRenderer() { setOpaque(true); setHorizontalAlignment(CENTER); setVerticalAlignment(CENTER); }
        @Override public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value, int index, boolean selected, boolean focus) {
            setText(String.valueOf(value));
            BufferedImage tile = (value != null && value >= 0 && value < tiles.size()) ? tiles.get(value) : null;
            setIcon(tile == null ? null : new ImageIcon(tile.getScaledInstance(32, 32, Image.SCALE_FAST)));
            setBackground(selected ? new Color(180, 205, 245) : Color.WHITE);
            setBorder(BorderFactory.createLineBorder(selected ? Color.BLUE : new Color(210, 210, 210)));
            return this;
        }
    }
}
