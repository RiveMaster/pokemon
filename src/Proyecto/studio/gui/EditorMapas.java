package Proyecto.studio.gui;

import javax.imageio.ImageIO;
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
    private static final int TILE = 16, ZOOM = 2, MAGIC = 0x504D4150, VERSION = 1;
    private final JFrame frame = new JFrame("Pokémon Studio — Editor de mapas");
    private int ancho = 20, alto = 15, seleccionado = 0, capa = 0;
    private final List<BufferedImage> tiles = new ArrayList<>();
    private int[][] capas = new int[3][ancho * alto];
    private boolean[] colisiones = new boolean[ancho * alto];
    private boolean modoColision = false, rejilla = true;
    private final MapaCanvas canvas = new MapaCanvas();
    private final DefaultListModel<Integer> modeloPaleta = new DefaultListModel<>();
    private final JList<Integer> listaPaleta = new JList<>(modeloPaleta);
    private final JLabel estado = new JLabel("Capa: Suelo · clic izquierdo pinta, derecho borra");
    private final JComboBox<String> selectorCapa = new JComboBox<>(new String[]{"Suelo", "Objetos", "Superior"});

    private EditorMapas() {
        for (int[] capaInicial : capas) java.util.Arrays.fill(capaInicial, -1);
        crearTiles();
        if (tiles.isEmpty()) crearTilesEjemplo();
        for (int i = 0; i < tiles.size(); i++) modeloPaleta.addElement(i);
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
        JPanel panelIzq = new JPanel(new BorderLayout(4, 4));
        panelIzq.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelIzq.add(new JLabel("Tiles (16 × 16)"), BorderLayout.NORTH);
        listaPaleta.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listaPaleta.setVisibleRowCount(-1);
        listaPaleta.setFixedCellWidth(48);
        listaPaleta.setFixedCellHeight(48);
        listaPaleta.setCellRenderer(new TileRenderer());
        listaPaleta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPaleta.setSelectedIndex(0);
        listaPaleta.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaPaleta.getSelectedValue() != null) {
                seleccionado = listaPaleta.getSelectedValue();
                actualizarEstado();
            }
        });
        panelIzq.add(new JScrollPane(listaPaleta), BorderLayout.CENTER);
        JButton limpiar = new JButton("Vaciar capa");
        limpiar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(frame, "¿Vaciar la capa " + nombreCapa(capa) + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                java.util.Arrays.fill(capas[capa], -1); canvas.repaint();
            }
        });
        JPanel opciones = new JPanel(new BorderLayout());
        opciones.add(selectorCapa, BorderLayout.NORTH);
        opciones.add(limpiar, BorderLayout.SOUTH);
        panelIzq.add(opciones, BorderLayout.SOUTH);
        panelIzq.setPreferredSize(new Dimension(230, 600));
        frame.add(panelIzq, BorderLayout.WEST);

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
        estado.setText((modoColision ? "MODO COLISIONES · " : "") + "Capa: " + nombreCapa(capa)
                + " · Tile: " + seleccionado + " · " + ancho + "×" + alto + " · clic izq. pinta, der. borra");
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
            colisiones = new boolean[ancho * alto]; canvas.revalidate(); canvas.repaint(); actualizarEstado();
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
            for (int y = 0; y < img.getHeight(); y += TILE) for (int x = 0; x < img.getWidth(); x += TILE) {
                tiles.add(img.getSubimage(x, y, TILE, TILE));
                modeloPaleta.addElement(tiles.size() - 1); n++;
            }
            listaPaleta.setSelectedIndex(tiles.size() - n);
            JOptionPane.showMessageDialog(frame, "Importados " + n + " tiles de " + fc.getSelectedFile().getName() + ".");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "No se pudo importar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        JFileChooser fc = new JFileChooser(); fc.setSelectedFile(new java.io.File("mapa.pmap"));
        if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        Path path = fc.getSelectedFile().toPath();
        if (!path.toString().toLowerCase().endsWith(".pmap")) path = Path.of(path + ".pmap");
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            out.writeInt(MAGIC); out.writeInt(VERSION); out.writeInt(ancho); out.writeInt(alto);
            out.writeInt(tiles.size());
            for (BufferedImage tile : tiles) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ImageIO.write(tile, "png", bytes);
                byte[] data = bytes.toByteArray(); out.writeInt(data.length); out.write(data);
            }
            for (int[] layer : capas) for (int v : layer) out.writeInt(v);
            for (boolean b : colisiones) out.writeBoolean(b);
            JOptionPane.showMessageDialog(frame, "Proyecto guardado en:\n" + path.toAbsolutePath());
        } catch (IOException ex) { error("No se pudo guardar el proyecto: " + ex.getMessage()); }
    }

    private void cargar() {
        JFileChooser fc = new JFileChooser(); fc.setDialogTitle("Abrir proyecto de mapa");
        if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(fc.getSelectedFile().toPath())))) {
            if (in.readInt() != MAGIC) throw new IOException("El archivo no es un proyecto .pmap válido.");
            int version = in.readInt(); if (version != VERSION) throw new IOException("Versión de proyecto no compatible: " + version);
            int nw = in.readInt(), nh = in.readInt(), count = in.readInt();
            if (nw < 1 || nh < 1 || nw > 300 || nh > 300 || count < 1 || count > 10000) throw new IOException("Dimensiones o paleta inválidas.");
            List<BufferedImage> newTiles = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int len = in.readInt(); if (len < 1 || len > 10_000_000) throw new IOException("Tile corrupto.");
                byte[] data = in.readNBytes(len); if (data.length != len) throw new EOFException();
                BufferedImage tile = ImageIO.read(new ByteArrayInputStream(data));
                if (tile == null) throw new IOException("No se pudo leer un tile.");
                BufferedImage fixed = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                fixed.getGraphics().drawImage(tile, 0, 0, TILE, TILE, null); newTiles.add(fixed);
            }
            int size = nw * nh; int[][] newLayers = new int[3][size];
            for (int[] layer : newLayers) for (int i = 0; i < size; i++) {
                int v = in.readInt(); if (v < -1 || v >= count) throw new IOException("Índice de tile fuera de rango."); layer[i] = v;
            }
            boolean[] newCollisions = new boolean[size]; for (int i = 0; i < size; i++) newCollisions[i] = in.readBoolean();
            ancho = nw; alto = nh; tiles.clear(); tiles.addAll(newTiles); capas = newLayers; colisiones = newCollisions;
            modeloPaleta.clear(); for (int i = 0; i < tiles.size(); i++) modeloPaleta.addElement(i);
            seleccionado = 0; listaPaleta.setSelectedIndex(0); canvas.revalidate(); canvas.repaint(); actualizarEstado();
        } catch (IOException | RuntimeException ex) { error("No se pudo abrir el proyecto: " + ex.getMessage()); }
    }

    private void exportarPng() {
        JFileChooser fc = new JFileChooser(); fc.setSelectedFile(new java.io.File("mapa.png"));
        if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        Path path = fc.getSelectedFile().toPath();
        if (!path.toString().toLowerCase().endsWith(".png")) path = Path.of(path + ".png");
        BufferedImage img = new BufferedImage(ancho * TILE, alto * TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        for (int c = 0; c < 3; c++) for (int i = 0; i < ancho * alto; i++) {
            int t = capas[c][i];
            if (t >= 0) g.drawImage(tiles.get(t), (i % ancho) * TILE, (i / ancho) * TILE, null);
        }
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
                        tiles.add(tile);
                    }
                }
            }
        } catch (IOException e) {
            error("No se pudieron importar los tiles desde " + ruta.toAbsolutePath() + ": " + e.getMessage());
        }
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

    private final class MapaCanvas extends JPanel {
        MapaCanvas() {
            setBackground(new Color(50, 50, 50));
            setPreferredSize(new Dimension(ancho * TILE * ZOOM, alto * TILE * ZOOM));
            MouseAdapter mouse = new MouseAdapter() {
                private void pintar(MouseEvent e) {
                    int x = e.getX() / (TILE * ZOOM), y = e.getY() / (TILE * ZOOM);
                    if (x < 0 || y < 0 || x >= ancho || y >= alto) return;
                    int i = y * ancho + x;
                    if (modoColision) colisiones[i] = SwingUtilities.isRightMouseButton(e) ? false : true;
                    else capas[capa][i] = SwingUtilities.isRightMouseButton(e) ? -1 : seleccionado;
                    repaint(x * TILE * ZOOM, y * TILE * ZOOM, TILE * ZOOM, TILE * ZOOM);
                }
                @Override public void mousePressed(MouseEvent e) { pintar(e); }
                @Override public void mouseDragged(MouseEvent e) { pintar(e); }
            };
            addMouseListener(mouse); addMouseMotionListener(mouse);
        }
        @Override public Dimension getPreferredSize() { return new Dimension(ancho * TILE * ZOOM, alto * TILE * ZOOM); }
        @Override protected void paintComponent(Graphics gg) {
            super.paintComponent(gg); Graphics2D g = (Graphics2D) gg.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            for (int i = 0; i < ancho * alto; i++) {
                int x = (i % ancho) * TILE * ZOOM, y = (i / ancho) * TILE * ZOOM;
                for (int c = 0; c < 3; c++) {
                    int t = capas[c][i];
                    if (t >= 0 && t < tiles.size()) g.drawImage(tiles.get(t), x, y, TILE * ZOOM, TILE * ZOOM, null);
                }
                if (modoColision && colisiones[i]) {
                    g.setColor(new Color(235, 45, 45, 115)); g.fillRect(x, y, TILE * ZOOM, TILE * ZOOM);
                }
                if (rejilla) { g.setColor(new Color(0, 0, 0, 75)); g.drawRect(x, y, TILE * ZOOM, TILE * ZOOM); }
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
