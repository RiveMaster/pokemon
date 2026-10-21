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
    private static final int TILE = 16, ZOOM = 2, MAGIC = 0x504D4150, VERSION = 5;
    private final JFrame frame = new JFrame("Pokémon Studio — Editor de mapas");
    private int ancho = 20, alto = 15, seleccionado = 0, capa = 0;
    private final List<BufferedImage> tiles = new ArrayList<>();
    private final List<TipoTile> tiposTile = new ArrayList<>();
    private final List<ObjetoMapa> objetos = new ArrayList<>();
    private final List<ColocacionObjeto> colocaciones = new ArrayList<>();
    private int objetoSeleccionado = -1;
    private boolean modoObjeto = false;
    private JList<ObjetoMapa> listaObjetos;
    private int[][] capas = new int[3][ancho * alto];
    private boolean[] colisiones = new boolean[ancho * alto];
    private boolean[] colisionesManuales = new boolean[ancho * alto];
    private boolean[] hierbaEncuentros = new boolean[ancho * alto];
    private boolean modoColision = false, rejilla = true, mostrarPropiedades = false;
    private final MapaCanvas canvas = new MapaCanvas();
    private final DefaultListModel<Integer> modeloPaleta = new DefaultListModel<>();
    private final JList<Integer> listaPaleta = new JList<>(modeloPaleta);
    private final JLabel estado = new JLabel("Capa: Suelo · clic izquierdo pinta, derecho borra");
    private final JComboBox<String> selectorCapa = new JComboBox<>(new String[]{"Suelo", "Objetos", "Superior"});
    private final JComboBox<TipoTile> selectorTipoTile = new JComboBox<>(TipoTile.values());
    private boolean actualizandoSelectorTipo = false;

    private EditorMapas(int anchoInicial, int altoInicial) {
        ancho = anchoInicial;
        alto = altoInicial;
        capas = new int[3][ancho * alto];
        colisiones = new boolean[ancho * alto];
        colisionesManuales = new boolean[ancho * alto];
        hierbaEncuentros = new boolean[ancho * alto];
        for (int[] capaInicial : capas) java.util.Arrays.fill(capaInicial, -1);
        crearTiles();
        if (tiles.isEmpty() && !hayPngEnCarpetaTiles()) crearTilesEjemplo();
        while (tiposTile.size() < tiles.size()) tiposTile.add(TipoTile.NORMAL);
        seleccionado = tiles.isEmpty() ? -1 : 0;
        for (int i = 0; i < tiles.size(); i++) modeloPaleta.addElement(i);
        listaPaleta.setSelectedIndex(seleccionado);
        actualizarTipoSeleccionado();
        construirVentana();
    }

    public static void abrir() {
        if (GraphicsEnvironment.isHeadless()) throw new IllegalStateException("No hay entorno gráfico disponible.");
        SwingUtilities.invokeLater(() -> {
            int[] dimensiones = pedirDimensionesIniciales();
            if (dimensiones != null) new EditorMapas(dimensiones[0], dimensiones[1]).frame.setVisible(true);
        });
    }

    private static int[] pedirDimensionesIniciales() {
        JTextField ancho = new JTextField("30", 6);
        JTextField alto = new JTextField("20", 6);
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.add(new JLabel("Ancho del mapa (tiles):"));
        panel.add(ancho);
        panel.add(new JLabel("Alto del mapa (tiles):"));
        panel.add(alto);
        while (true) {
            int resultado = JOptionPane.showConfirmDialog(null, panel, "Nuevo mapa Pokémon",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (resultado != JOptionPane.OK_OPTION) return null;
            try {
                int w = Integer.parseInt(ancho.getText().trim());
                int h = Integer.parseInt(alto.getText().trim());
                if (w >= 1 && w <= 300 && h >= 1 && h <= 300) return new int[]{w, h};
            } catch (NumberFormatException ignorado) {
                // Se muestra el aviso común de debajo.
            }
            JOptionPane.showMessageDialog(null, "El ancho y alto deben estar entre 1 y 300 tiles.",
                    "Dimensiones no válidas", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void construirVentana() {
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(6, 6));
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton nuevo = new JButton("Nuevo mapa");
        nuevo.addActionListener(e -> nuevoMapa());
        JButton generar = new JButton("Generar mapa");
        generar.addActionListener(e -> generarMapaDialogo());
        JButton importar = new JButton("Importar PNG");
        importar.addActionListener(e -> importarPng());
        JButton guardar = new JButton("Guardar proyecto");
        guardar.addActionListener(e -> guardar());
        JButton abrir = new JButton("Abrir proyecto");
        abrir.addActionListener(e -> cargar());
        JButton exportar = new JButton("Exportar PNG");
        exportar.addActionListener(e -> exportarPng());
        JButton exportarTiles = new JButton("Exportar tiles");
        exportarTiles.addActionListener(e -> exportarTiles());
        JButton col = new JButton("Pintar colisiones");
        col.addActionListener(e -> { modoColision = !modoColision; actualizarEstado(); canvas.repaint(); });
        JCheckBox cuadricula = new JCheckBox("Cuadrícula", true);
        cuadricula.addActionListener(e -> { rejilla = cuadricula.isSelected(); canvas.repaint(); });
        JCheckBox propiedades = new JCheckBox("Ver propiedades");
        propiedades.addActionListener(e -> { mostrarPropiedades = propiedades.isSelected(); canvas.repaint(); });
        barra.add(nuevo); barra.add(generar); barra.add(abrir); barra.add(guardar); barra.add(importar);
        barra.add(exportarTiles); barra.add(exportar); barra.add(col); barra.add(cuadricula); barra.add(propiedades);
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
                actualizarTipoSeleccionado();
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

    /** Abre el asistente de generación procedural y aplica el resultado al mapa editable. */
    private void generarMapaDialogo() {
        if (tiles.isEmpty()) {
            error("Primero importa o crea al menos un tile.");
            return;
        }

        JDialog dialogo = new JDialog(frame, "Generador de mapas Pokémon", true);
        dialogo.setLayout(new BorderLayout(8, 8));

        JSpinner anchoCampo = new JSpinner(new SpinnerNumberModel(ancho, 1, 300, 1));
        JSpinner altoCampo = new JSpinner(new SpinnerNumberModel(alto, 1, 300, 1));
        JTextField semillaCampo = new JTextField(Long.toString(System.currentTimeMillis()), 14);
        JComboBox<GeneradorMapas.Estilo> estiloCampo = new JComboBox<>(GeneradorMapas.Estilo.values());
        JSlider densidadCampo = new JSlider(0, 100, 45);
        densidadCampo.setMajorTickSpacing(25);
        densidadCampo.setMinorTickSpacing(5);
        densidadCampo.setPaintTicks(true);
        densidadCampo.setPaintLabels(true);

        JCheckBox camino = new JCheckBox("Crear caminos conectados", true);
        JCheckBox hierba = new JCheckBox("Crear zonas de hierba", true);
        JCheckBox agua = new JCheckBox("Crear lagos / costa", true);
        JCheckBox obstaculos = new JCheckBox("Crear árboles / obstáculos", true);
        JCheckBox edificios = new JCheckBox("Colocar edificios", true);
        JCheckBox casas = new JCheckBox("Casas", true);
        JCheckBox centroPokemon = new JCheckBox("Centro Pokémon", true);
        JCheckBox gimnasio = new JCheckBox("Gimnasio", true);
        JCheckBox tienda = new JCheckBox("Tienda", true);
        JLabel clasificarTiles = new JLabel("<html>Los tiles elegidos se clasifican automáticamente por función.<br>La propiedad se guarda en este proyecto y afecta a sus otros usos.</html>");

        JComboBox<Integer> tileBase = crearSelectorTile();
        JComboBox<Integer> tileCamino = crearSelectorTile();
        JComboBox<Integer> tileHierba = crearSelectorTile();
        JComboBox<Integer> tileAgua = crearSelectorTile();
        JComboBox<Integer> tileObstaculo = crearSelectorTile();

        int baseDefault = primerTileTipo(TipoTile.NORMAL, -1);
        if (baseDefault < 0) baseDefault = 0;
        int caminoDefault = siguienteTileNormal(baseDefault);
        if (caminoDefault < 0) caminoDefault = baseDefault;
        int hierbaDefault = primerTileTipo(TipoTile.HIERBA, baseDefault);
        if (hierbaDefault < 0) hierbaDefault = distintoDe(baseDefault, caminoDefault, -1);
        int aguaDefault = primerTileTipo(TipoTile.AGUA, baseDefault, caminoDefault, hierbaDefault);
        if (aguaDefault < 0) aguaDefault = distintoDe(baseDefault, caminoDefault, hierbaDefault);
        int obstaculoDefault = primerTileTipo(TipoTile.BLOQUEO, baseDefault, caminoDefault, hierbaDefault, aguaDefault);
        if (obstaculoDefault < 0) obstaculoDefault = distintoDe(baseDefault, caminoDefault, hierbaDefault, aguaDefault);

        tileBase.setSelectedItem(baseDefault);
        tileCamino.setSelectedItem(caminoDefault);
        tileHierba.setSelectedItem(hierbaDefault);
        tileAgua.setSelectedItem(aguaDefault);
        tileObstaculo.setSelectedItem(obstaculoDefault);
        if (hierbaDefault < 0) hierba.setSelected(false);
        if (aguaDefault < 0) agua.setSelected(false);
        if (obstaculoDefault < 0) obstaculos.setSelected(false);

        JPanel dimensiones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dimensiones.add(new JLabel("Ancho (tiles):")); dimensiones.add(anchoCampo);
        dimensiones.add(new JLabel("Alto (tiles):")); dimensiones.add(altoCampo);
        dimensiones.add(new JLabel("Semilla:")); dimensiones.add(semillaCampo);

        JPanel terreno = new JPanel(new GridLayout(0, 1, 4, 4));
        terreno.setBorder(BorderFactory.createTitledBorder("Estilo y densidad"));
        terreno.add(estiloCampo);
        JPanel sliderPanel = new JPanel(new BorderLayout(6, 0));
        sliderPanel.add(new JLabel("Densidad de vegetación:"), BorderLayout.WEST);
        sliderPanel.add(densidadCampo, BorderLayout.CENTER);
        terreno.add(sliderPanel);

        JPanel opciones = new JPanel(new GridLayout(0, 2, 6, 3));
        opciones.setBorder(BorderFactory.createTitledBorder("Elementos a generar"));
        opciones.add(camino); opciones.add(hierba);
        opciones.add(agua); opciones.add(obstaculos);
        opciones.add(edificios); opciones.add(new JLabel("Los edificios se usan en estilo Pueblo."));
        opciones.add(casas); opciones.add(centroPokemon);
        opciones.add(gimnasio); opciones.add(tienda);

        JPanel paleta = new JPanel(new GridLayout(0, 2, 6, 5));
        paleta.setBorder(BorderFactory.createTitledBorder("Tiles para cada función"));
        paleta.add(new JLabel("Suelo base:")); paleta.add(tileBase);
        paleta.add(new JLabel("Camino:")); paleta.add(tileCamino);
        paleta.add(new JLabel("Hierba:")); paleta.add(tileHierba);
        paleta.add(new JLabel("Agua:")); paleta.add(tileAgua);
        paleta.add(new JLabel("Árbol / obstáculo:")); paleta.add(tileObstaculo);
        paleta.add(clasificarTiles); paleta.add(new JLabel("Los tiles se muestran como miniaturas."));

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(dimensiones);
        centro.add(terreno);
        centro.add(opciones);
        centro.add(paleta);
        dialogo.add(new JScrollPane(centro), BorderLayout.CENTER);

        JLabel nota = new JLabel("<html>Genera mapas reproducibles con la misma semilla. " +
                "Los caminos conectan los bordes; los objetos usan la biblioteca de «Crear objetos».</html>");
        nota.setBorder(new EmptyBorder(4, 8, 4, 8));
        dialogo.add(nota, BorderLayout.NORTH);

        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dialogo.dispose());
        JButton generar = new JButton("Generar y reemplazar mapa");
        generar.addActionListener(e -> {
            try {
                int w = (Integer) anchoCampo.getValue();
                int h = (Integer) altoCampo.getValue();
                long seed = Long.parseLong(semillaCampo.getText().trim());
                int base = valorSelector(tileBase);
                int road = camino.isSelected() ? valorSelector(tileCamino) : -1;
                int grassTile = hierba.isSelected() ? valorSelector(tileHierba) : -1;
                int waterTile = agua.isSelected() ? valorSelector(tileAgua) : -1;
                int obstacleTile = obstaculos.isSelected() ? valorSelector(tileObstaculo) : -1;

                if (base < 0) throw new IllegalArgumentException("Selecciona un tile de suelo base.");
                validarRolesTiles(base, road, grassTile, waterTile, obstacleTile);

                String aviso = "Se reemplazará el mapa que estás editando. ¿Continuar?"
                        + "\n\nLos tiles elegidos se clasificarán automáticamente según su función. La propiedad se guarda en este proyecto y afecta a los demás lugares donde se usan esos tiles.";
                if (JOptionPane.showConfirmDialog(dialogo, aviso, "Generar mapa",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;

                tiposTile.set(base, TipoTile.NORMAL);
                if (road >= 0) tiposTile.set(road, TipoTile.NORMAL);
                if (grassTile >= 0) tiposTile.set(grassTile, TipoTile.HIERBA);
                if (waterTile >= 0) tiposTile.set(waterTile, TipoTile.AGUA);
                if (obstacleTile >= 0) tiposTile.set(obstacleTile, TipoTile.BLOQUEO);

                List<GeneradorMapas.DefinicionObjeto> biblioteca = new ArrayList<>();
                for (int i = 0; i < objetos.size(); i++) {
                    ObjetoMapa o = objetos.get(i);
                    biblioteca.add(new GeneradorMapas.DefinicionObjeto(i, o.nombre, o.ancho, o.alto, o.celdas, o.categoria.name()));
                }

                GeneradorMapas.Opciones ajustes = new GeneradorMapas.Opciones(
                        w, h, seed, (GeneradorMapas.Estilo) estiloCampo.getSelectedItem(),
                        densidadCampo.getValue(), camino.isSelected(), hierba.isSelected(), agua.isSelected(),
                        obstaculos.isSelected(), edificios.isSelected(), base, road, grassTile, waterTile, obstacleTile,
                        casas.isSelected(), centroPokemon.isSelected(), gimnasio.isSelected(), tienda.isSelected());
                GeneradorMapas.Resultado resultado = GeneradorMapas.generar(ajustes, biblioteca);

                ancho = resultado.ancho;
                alto = resultado.alto;
                capas = new int[3][];
                for (int capaActual = 0; capaActual < 3; capaActual++) capas[capaActual] = resultado.capas[capaActual].clone();
                colisionesManuales = resultado.colisionesManuales.clone();
                hierbaEncuentros = resultado.hierbaEncuentros.clone();
                aplicarVariantesCamino(road);
                colocaciones.clear();
                for (GeneradorMapas.Colocacion p : resultado.colocaciones) {
                    if (p.objeto >= 0 && p.objeto < objetos.size())
                        colocaciones.add(new ColocacionObjeto(p.objeto, p.x, p.y, p.capa));
                }

                modoColision = false;
                modoObjeto = false;
                objetoSeleccionado = -1;
                capa = 0;
                seleccionado = base;
                listaPaleta.setSelectedIndex(seleccionado);
                actualizarTipoSeleccionado();
                recalcularTodasPropiedades();
                canvas.revalidate();
                canvas.repaint();
                actualizarEstado();
                dialogo.dispose();
                JOptionPane.showMessageDialog(frame,
                        "Mapa generado correctamente.\nTamaño: " + ancho + " × " + alto +
                                " tiles (" + (ancho * 16) + " × " + (alto * 16) + " píxeles).\nSemilla: " + seed +
                                "\nObjetos colocados: " + colocaciones.size() +
                                "\nPuedes seguir editándolo y guardarlo como .pmap.",
                        "Generador de mapas", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "La semilla debe ser un número entero.", "Semilla no válida", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Configuración no válida", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(cancelar);
        botones.add(generar);
        dialogo.add(botones, BorderLayout.SOUTH);
        dialogo.setSize(820, 660);
        dialogo.setLocationRelativeTo(frame);
        dialogo.setVisible(true);
    }

    private JComboBox<Integer> crearSelectorTile() {
        JComboBox<Integer> combo = new JComboBox<>();
        for (int i = 0; i < tiles.size(); i++) combo.addItem(i);
        combo.setRenderer(new TileRenderer());
        combo.setPreferredSize(new Dimension(230, 40));
        return combo;
    }

    private int valorSelector(JComboBox<Integer> combo) {
        Integer value = (Integer) combo.getSelectedItem();
        return value == null ? -1 : value;
    }

    /**
     * Aplica automáticamente tiles de borde, rectas, finales, cruces y esquinas
     * cuando el usuario ha clasificado esas variantes en la paleta.
     */
    private void aplicarVariantesCamino(int tileCaminoBase) {
        if (tileCaminoBase < 0 || ancho < 1 || alto < 1) return;
        int[] original = capas[0].clone();
        for (int y = 0; y < alto; y++) for (int x = 0; x < ancho; x++) {
            int idx = y * ancho + x;
            if (original[idx] != tileCaminoBase) continue;
            boolean n = esCamino(original, x, y - 1, tileCaminoBase);
            boolean e = esCamino(original, x + 1, y, tileCaminoBase);
            boolean s = esCamino(original, x, y + 1, tileCaminoBase);
            boolean o = esCamino(original, x - 1, y, tileCaminoBase);
            int vecinos = (n ? 1 : 0) + (e ? 1 : 0) + (s ? 1 : 0) + (o ? 1 : 0);
            TipoTile variante = null;
            if (vecinos == 4) variante = TipoTile.CAMINO_CRUCE;
            else if (vecinos == 3) {
                if (!n) variante = TipoTile.CAMINO_T_N;
                else if (!e) variante = TipoTile.CAMINO_T_E;
                else if (!s) variante = TipoTile.CAMINO_T_S;
                else variante = TipoTile.CAMINO_T_O;
            } else if (vecinos == 2 && n && s) variante = TipoTile.CAMINO_VERTICAL;
            else if (vecinos == 2 && e && o) variante = TipoTile.CAMINO_HORIZONTAL;
            else if (vecinos == 1) {
                if (n) variante = TipoTile.CAMINO_FINAL_N;
                else if (e) variante = TipoTile.CAMINO_FINAL_E;
                else if (s) variante = TipoTile.CAMINO_FINAL_S;
                else variante = TipoTile.CAMINO_FINAL_O;
            } else if (vecinos == 2) {
                boolean diagonal = n && e ? esCamino(original, x + 1, y - 1, tileCaminoBase)
                        : n && o ? esCamino(original, x - 1, y - 1, tileCaminoBase)
                        : s && e ? esCamino(original, x + 1, y + 1, tileCaminoBase)
                        : esCamino(original, x - 1, y + 1, tileCaminoBase);
                if (n && e) variante = diagonal ? TipoTile.CAMINO_INTERIOR_NE : TipoTile.CAMINO_EXTERIOR_NE;
                else if (n && o) variante = diagonal ? TipoTile.CAMINO_INTERIOR_NO : TipoTile.CAMINO_EXTERIOR_NO;
                else if (s && e) variante = diagonal ? TipoTile.CAMINO_INTERIOR_SE : TipoTile.CAMINO_EXTERIOR_SE;
                else variante = diagonal ? TipoTile.CAMINO_INTERIOR_SO : TipoTile.CAMINO_EXTERIOR_SO;
            }
            if (variante != null) {
                int tile = primerTileTipo(variante, -1);
                if (tile >= 0) capas[0][idx] = tile;
            }
        }
    }

    private boolean esCamino(int[] layer, int x, int y, int tileCaminoBase) {
        return x >= 0 && y >= 0 && x < ancho && y < alto
                && layer[y * ancho + x] == tileCaminoBase;
    }

    private int primerTileTipo(TipoTile tipo, int... excluidos) {
        for (int i = 0; i < tiposTile.size(); i++) {
            boolean excluido = false;
            for (int n : excluidos) if (i == n) excluido = true;
            if (!excluido && tiposTile.get(i) == tipo) return i;
        }
        return -1;
    }

    private int siguienteTileNormal(int base) {
        int normal = primerTileTipo(TipoTile.NORMAL, base);
        if (normal >= 0) return normal;
        return distintoDe(base, -1, -1);
    }

    private int distintoDe(int... excluidos) {
        for (int i = 0; i < tiles.size(); i++) {
            boolean excluido = false;
            for (int indice : excluidos) if (i == indice) {
                excluido = true;
                break;
            }
            if (!excluido) return i;
        }
        return -1;
    }

    private void validarRolesTiles(int base, int road, int grass, int water, int obstacle) {
        if (tiles.size() < 1) throw new IllegalArgumentException("Importa o crea tiles antes de generar.");
        if (grass >= 0 && (grass == base || grass == road)) throw new IllegalArgumentException("El tile de hierba debe ser distinto del suelo y el camino.");
        if (water >= 0 && (water == base || water == road || water == grass)) throw new IllegalArgumentException("El tile de agua debe ser distinto del suelo, camino y hierba.");
        if (obstacle >= 0 && (obstacle == base || obstacle == road || obstacle == grass || obstacle == water))
            throw new IllegalArgumentException("El tile de obstáculos debe ser distinto de los demás tiles seleccionados.");
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
            colisionesManuales = new boolean[ancho * alto];
            hierbaEncuentros = new boolean[ancho * alto];
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
                    tiposTile.add(TipoTile.NORMAL);
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

        JButton crear = new JButton("Crear tile píxel a píxel");
        crear.addActionListener(e -> crearTilePixelArt());
        JButton editar = new JButton("Editar tile seleccionado");
        editar.addActionListener(e -> editarTileSeleccionado());
        JPanel pixelBotones = new JPanel(new GridLayout(1, 2, 4, 4));
        pixelBotones.add(crear);
        pixelBotones.add(editar);

        selectorTipoTile.setToolTipText("Propiedad lógica del tile: normal, bloquea, hierba, agua o evento.");
        selectorTipoTile.addActionListener(e -> {
            if (actualizandoSelectorTipo) return;
            if (seleccionado >= 0 && seleccionado < tiposTile.size()
                    && selectorTipoTile.getSelectedItem() instanceof TipoTile tipo) {
                tiposTile.set(seleccionado, tipo);
                actualizarPropiedadesTileEnMapa(seleccionado);
                actualizarEstado();
                canvas.repaint();
                listaPaleta.repaint();
            }
        });
        JPanel tipoPanel = new JPanel(new BorderLayout(4, 4));
        tipoPanel.add(new JLabel("Tipo del tile seleccionado:"), BorderLayout.NORTH);
        tipoPanel.add(selectorTipoTile, BorderLayout.CENTER);

        JButton limpiar = new JButton("Vaciar capa");
        limpiar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(frame, "¿Vaciar la capa " + nombreCapa(capa) + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                java.util.Arrays.fill(capas[capa], -1);
                if (capa == 0) java.util.Arrays.fill(colisionesManuales, false);
                recalcularTodasPropiedades();
                canvas.repaint();
            }
        });
        JPanel opciones = new JPanel(new BorderLayout(4, 4));
        opciones.add(selectorCapa, BorderLayout.NORTH);
        opciones.add(limpiar, BorderLayout.SOUTH);
        JPanel abajo = new JPanel(new GridLayout(0, 1, 4, 4));
        abajo.add(pixelBotones);
        abajo.add(tipoPanel);
        abajo.add(opciones);
        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTipoSeleccionado() {
        if (seleccionado >= 0 && seleccionado < tiposTile.size()) {
            actualizandoSelectorTipo = true;
            try {
                selectorTipoTile.setSelectedItem(tiposTile.get(seleccionado));
            } finally {
                actualizandoSelectorTipo = false;
            }
        }
    }

    private void crearTilePixelArt() {
        BufferedImage nuevo = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        tiles.add(nuevo);
        tiposTile.add(TipoTile.NORMAL);
        int indice = tiles.size() - 1;
        modeloPaleta.addElement(indice);
        listaPaleta.setSelectedIndex(indice);
        seleccionado = indice;
        actualizarTipoSeleccionado();
        editarTile(indice);
    }

    private void editarTileSeleccionado() {
        if (seleccionado < 0 || seleccionado >= tiles.size()) {
            JOptionPane.showMessageDialog(frame, "Selecciona un tile primero.", "Sin tile seleccionado",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        editarTile(seleccionado);
    }

    private void editarTile(int indice) {
        if (indice < 0 || indice >= tiles.size()) return;
        BufferedImage trabajo = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D copia = trabajo.createGraphics();
        copia.drawImage(tiles.get(indice), 0, 0, null);
        copia.dispose();

        JDialog dialogo = new JDialog(frame, "Editor de píxeles — tile " + indice, true);
        dialogo.setLayout(new BorderLayout(8, 8));
        final Color[] colorActual = {Color.BLACK};
        JLabel muestraColor = new JLabel("Color actual: #000000");
        JButton elegirColor = new JButton("Elegir color…");
        elegirColor.addActionListener(e -> {
            Color elegido = JColorChooser.showDialog(dialogo, "Color del pincel", colorActual[0]);
            if (elegido != null) {
                colorActual[0] = elegido;
                muestraColor.setText(String.format(java.util.Locale.ROOT, "Color actual: #%02X%02X%02X",
                        elegido.getRed(), elegido.getGreen(), elegido.getBlue()));
                muestraColor.setForeground(elegido);
            }
        });

        PixelCanvas pixelCanvas = new PixelCanvas(trabajo, () -> colorActual[0]);
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(elegirColor);
        barra.add(muestraColor);
        barra.add(new JLabel("Clic izquierdo pinta; clic derecho borra a transparente."));
        dialogo.add(barra, BorderLayout.NORTH);
        dialogo.add(new JScrollPane(pixelCanvas), BorderLayout.CENTER);

        JButton borrarTodo = new JButton("Borrar todo");
        borrarTodo.addActionListener(e -> {
            Graphics2D g = trabajo.createGraphics();
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0, 0, TILE, TILE);
            g.dispose();
            pixelCanvas.repaint();
        });
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dialogo.dispose());
        JButton guardar = new JButton("Guardar tile");
        guardar.addActionListener(e -> {
            tiles.set(indice, trabajo);
            modeloPaleta.set(indice, indice);
            listaPaleta.repaint();
            canvas.repaint();
            dialogo.dispose();
        });
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(borrarTodo);
        botones.add(cancelar);
        botones.add(guardar);
        dialogo.add(botones, BorderLayout.SOUTH);
        dialogo.setSize(730, 650);
        dialogo.setLocationRelativeTo(frame);
        dialogo.setVisible(true);
    }

    private final class PixelCanvas extends JPanel {
        private final BufferedImage imagen;
        private final java.util.function.Supplier<Color> color;

        PixelCanvas(BufferedImage imagen, java.util.function.Supplier<Color> color) {
            this.imagen = imagen;
            this.color = color;
            setBackground(new Color(90, 90, 90));
            MouseAdapter raton = new MouseAdapter() {
                private void pintar(MouseEvent e) {
                    int celda = Math.max(16, Math.min(32, Math.min(getWidth() / TILE, getHeight() / TILE)));
                    int x = e.getX() / celda;
                    int y = e.getY() / celda;
                    if (x < 0 || y < 0 || x >= TILE || y >= TILE) return;
                    if (esClicDerecho(e)) imagen.setRGB(x, y, 0x00000000);
                    else imagen.setRGB(x, y, PixelCanvas.this.color.get().getRGB());
                    repaint();
                }
                @Override public void mousePressed(MouseEvent e) { pintar(e); }
                @Override public void mouseDragged(MouseEvent e) { pintar(e); }
            };
            addMouseListener(raton);
            addMouseMotionListener(raton);
        }

        @Override public Dimension getPreferredSize() { return new Dimension(TILE * 28, TILE * 28); }

        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            int celda = Math.max(16, Math.min(32, Math.min(getWidth() / TILE, getHeight() / TILE)));
            for (int y = 0; y < TILE; y++) for (int x = 0; x < TILE; x++) {
                g.setColor(((x + y) & 1) == 0 ? new Color(235, 235, 235) : new Color(190, 190, 190));
                g.fillRect(x * celda, y * celda, celda, celda);
                int argb = imagen.getRGB(x, y);
                if (((argb >>> 24) & 0xFF) > 0) {
                    g.setColor(new Color(argb, true));
                    g.fillRect(x * celda, y * celda, celda, celda);
                }
                g.setColor(new Color(0, 0, 0, 100));
                g.drawRect(x * celda, y * celda, celda, celda);
            }
            g.dispose();
        }
    }

    private enum TipoTile {
        NORMAL("Normal"),
        BLOQUEO("Bloquea el paso"),
        HIERBA("Hierba (encuentros Pokémon)"),
        AGUA("Agua"),
        EVENTO("Evento"),
        CAMINO_HORIZONTAL("Camino recto horizontal"),
        CAMINO_VERTICAL("Camino recto vertical"),
        CAMINO_FINAL_N("Camino: final norte"), CAMINO_FINAL_E("Camino: final este"),
        CAMINO_FINAL_S("Camino: final sur"), CAMINO_FINAL_O("Camino: final oeste"),
        CAMINO_ESQUINA_NE("Camino: esquina NE"), CAMINO_ESQUINA_NO("Camino: esquina NO"),
        CAMINO_ESQUINA_SE("Camino: esquina SE"), CAMINO_ESQUINA_SO("Camino: esquina SO"),
        CAMINO_INTERIOR_NE("Camino: esquina interior NE"), CAMINO_INTERIOR_NO("Camino: esquina interior NO"),
        CAMINO_INTERIOR_SE("Camino: esquina interior SE"), CAMINO_INTERIOR_SO("Camino: esquina interior SO"),
        CAMINO_EXTERIOR_NE("Camino: esquina exterior NE"), CAMINO_EXTERIOR_NO("Camino: esquina exterior NO"),
        CAMINO_EXTERIOR_SE("Camino: esquina exterior SE"), CAMINO_EXTERIOR_SO("Camino: esquina exterior SO"),
        CAMINO_T_N("Camino: cruce en T abierto al norte"), CAMINO_T_E("Camino: cruce en T abierto al este"),
        CAMINO_T_S("Camino: cruce en T abierto al sur"), CAMINO_T_O("Camino: cruce en T abierto al oeste"),
        CAMINO_CRUCE("Camino: cruce de cuatro direcciones");
        private final String etiqueta;
        TipoTile(String etiqueta) { this.etiqueta = etiqueta; }
        @Override public String toString() { return etiqueta; }
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
            recalcularTodasPropiedades();
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
        JComboBox<CategoriaObjeto> categoria = new JComboBox<>(CategoriaObjeto.values());
        if (original != null) categoria.setSelectedItem(original.categoria);
        else categoria.setSelectedItem(CategoriaObjeto.inferir(nombre.getText()));
        nombre.addActionListener(e -> {
            if (original == null) categoria.setSelectedItem(CategoriaObjeto.inferir(nombre.getText()));
        });
        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        encabezado.add(new JLabel("Nombre:"));
        encabezado.add(nombre);
        encabezado.add(new JLabel("Ancho (tiles):"));
        encabezado.add(columnas);
        encabezado.add(new JLabel("Alto (tiles):"));
        encabezado.add(filas);
        encabezado.add(new JLabel("Tipo:"));
        encabezado.add(categoria);

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
        JPanel ladoPaleta = new JPanel(new BorderLayout(4, 4));
        ladoPaleta.add(new JLabel("Selecciona un tile"), BorderLayout.NORTH);
        ladoPaleta.add(new JScrollPane(paleta), BorderLayout.CENTER);
        JButton importarTilesObjeto = new JButton("Importar PNG...");
        importarTilesObjeto.setToolTipText("Importa una spritesheet o imagen con dimensiones múltiplos de 16 y añade sus tiles a esta paleta.");
        importarTilesObjeto.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Importar tiles para este objeto (PNG)");
            if (fc.showOpenDialog(dialogo) != JFileChooser.APPROVE_OPTION) return;
            try {
                BufferedImage img = ImageIO.read(fc.getSelectedFile());
                if (img == null || img.getWidth() % TILE != 0 || img.getHeight() % TILE != 0)
                    throw new IOException("La imagen debe tener dimensiones múltiplos de 16 píxeles.");
                int añadidos = 0, vacios = 0;
                for (int yy = 0; yy < img.getHeight(); yy += TILE) {
                    for (int xx = 0; xx < img.getWidth(); xx += TILE) {
                        BufferedImage t = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = t.createGraphics();
                        g.drawImage(img, 0, 0, TILE, TILE, xx, yy, xx + TILE, yy + TILE, null);
                        g.dispose();
                        if (!tienePixelVisible(t)) { vacios++; continue; }
                        tiles.add(t);
                        tiposTile.add(TipoTile.NORMAL);
                        int nuevoIndice = tiles.size() - 1;
                        modeloPaleta.addElement(nuevoIndice);
                        paleta.setListData(modeloPaleta.toArray(new Integer[0]));
                        añadidos++;
                    }
                }
                if (añadidos > 0) {
                    int nuevoIndice = tiles.size() - 1;
                    paleta.setSelectedValue(nuevoIndice, true);
                    tileActual[0] = nuevoIndice;
                    listaPaleta.setSelectedIndex(nuevoIndice);
                    seleccionado = nuevoIndice;
                    actualizarTipoSeleccionado();
                    canvas.repaint();
                }
                JOptionPane.showMessageDialog(dialogo, "Tiles añadidos: " + añadidos + ". Vacíos omitidos: " + vacios + ".");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialogo, "No se pudo importar el PNG: " + ex.getMessage(),
                        "Error de importación", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton crearTileObjeto = new JButton("Crear tile nuevo...");
        crearTileObjeto.addActionListener(e -> {
            crearTilePixelArt();
            if (!tiles.isEmpty()) {
                int nuevoIndice = tiles.size() - 1;
                paleta.setListData(modeloPaleta.toArray(new Integer[0]));
                paleta.setSelectedValue(nuevoIndice, true);
                tileActual[0] = nuevoIndice;
            }
        });
        JPanel herramientasPaleta = new JPanel(new GridLayout(0, 1, 3, 3));
        herramientasPaleta.add(importarTilesObjeto);
        herramientasPaleta.add(crearTileObjeto);
        ladoPaleta.add(herramientasPaleta, BorderLayout.SOUTH);
        ladoPaleta.setPreferredSize(new Dimension(220, 430));

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
            ObjetoMapa guardado = new ObjetoMapa(n, lienzo.getColumnas(), lienzo.getFilas(), lienzo.getCeldas(),
                    (CategoriaObjeto) categoria.getSelectedItem());
            if (indiceOriginal >= 0 && indiceOriginal < objetos.size()) objetos.set(indiceOriginal, guardado);
            else objetos.add(guardado);
            lista.setListData(objetos.toArray(new ObjetoMapa[0]));
            int seleccionar = indiceOriginal >= 0 ? indiceOriginal : objetos.size() - 1;
            lista.setSelectedIndex(seleccionar);
            objetoSeleccionado = seleccionar;
            modoObjeto = true;
            recalcularTodasPropiedades();
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

    private enum CategoriaObjeto {
        CASA("Casa / edificio"), CENTRO_POKEMON("Centro Pokémon"), GIMNASIO("Gimnasio"),
        TIENDA("Tienda"), ARBOL("Árbol / vegetación"), ROCA("Roca / obstáculo"),
        DECORACION("Decoración"), OTRO("Otro");

        private final String etiqueta;
        CategoriaObjeto(String etiqueta) { this.etiqueta = etiqueta; }
        @Override public String toString() { return etiqueta; }

        static CategoriaObjeto inferir(String nombre) {
            String n = nombre == null ? "" : nombre.toLowerCase(java.util.Locale.ROOT);
            if (n.contains("pokecentro") || n.contains("centro pok") || n.contains("pokemon center")) return CENTRO_POKEMON;
            if (n.contains("gimnasio") || n.contains("gym")) return GIMNASIO;
            if (n.contains("tienda") || n.contains("shop")) return TIENDA;
            if (n.contains("casa") || n.contains("house") || n.contains("edificio")) return CASA;
            if (n.contains("arbol") || n.contains("árbol") || n.contains("tree") || n.contains("arbusto")) return ARBOL;
            if (n.contains("roca") || n.contains("piedra") || n.contains("rock")) return ROCA;
            return OTRO;
        }
    }

    private static final class ObjetoMapa {
        final String nombre;
        final int ancho, alto;
        final int[] celdas;
        final CategoriaObjeto categoria;
        ObjetoMapa(String nombre, int ancho, int alto, int[] celdas) {
            this(nombre, ancho, alto, celdas, CategoriaObjeto.inferir(nombre));
        }
        ObjetoMapa(String nombre, int ancho, int alto, int[] celdas, CategoriaObjeto categoria) {
            this.nombre = nombre;
            this.ancho = ancho;
            this.alto = alto;
            this.celdas = celdas.clone();
            this.categoria = categoria == null ? CategoriaObjeto.OTRO : categoria;
        }
        @Override public String toString() { return nombre + " [" + categoria + "] (" + ancho + "×" + alto + ")"; }
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
        if (objetoSeleccionado >= 0 && objetoSeleccionado < objetos.size()) {
            colocaciones.add(new ColocacionObjeto(objetoSeleccionado, x, y, capaActual));
            recalcularTodasPropiedades();
        }
    }

    private void borrarObjetoEn(int x, int y) {
        for (int i = colocaciones.size() - 1; i >= 0; i--) {
            ColocacionObjeto p = colocaciones.get(i);
            if (p.objeto < 0 || p.objeto >= objetos.size()) continue;
            ObjetoMapa o = objetos.get(p.objeto);
            if (x >= p.x && y >= p.y && x < p.x + o.ancho && y < p.y + o.alto) {
                colocaciones.remove(i);
                recalcularTodasPropiedades();
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
            for (TipoTile tipo : tiposTile) out.writeByte(tipo.ordinal());
            for (int[] layer : capas) for (int v : layer) out.writeInt(v);
            for (boolean b : colisiones) out.writeBoolean(b);
            for (boolean b : colisionesManuales) out.writeBoolean(b);
            for (boolean b : hierbaEncuentros) out.writeBoolean(b);

            out.writeInt(objetos.size());
            for (ObjetoMapa objeto : objetos) {
                out.writeUTF(objeto.nombre);
                out.writeInt(objeto.ancho);
                out.writeInt(objeto.alto);
                for (int tile : objeto.celdas) out.writeInt(tile);
                out.writeUTF(objeto.categoria.name());
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

            List<TipoTile> nuevosTipos = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                TipoTile tipo = TipoTile.NORMAL;
                if (version >= 3) {
                    int ordinal = in.readUnsignedByte();
                    if (ordinal >= TipoTile.values().length) throw new IOException("Tipo de tile desconocido.");
                    tipo = TipoTile.values()[ordinal];
                }
                nuevosTipos.add(tipo);
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
            boolean[] newManualCollisions = newCollisions.clone();
            if (version >= 4) {
                for (int i = 0; i < size; i++) newManualCollisions[i] = in.readBoolean();
            }
            boolean[] newGrass = new boolean[size];
            if (version >= 3) {
                for (int i = 0; i < size; i++) newGrass[i] = in.readBoolean();
            }

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
                    CategoriaObjeto categoriaObjeto = CategoriaObjeto.inferir(nombre);
                    if (version >= 5) {
                        String categoriaTexto = in.readUTF();
                        try { categoriaObjeto = CategoriaObjeto.valueOf(categoriaTexto); }
                        catch (IllegalArgumentException ex) { throw new IOException("Categoría de objeto desconocida: " + categoriaTexto); }
                    }
                    nuevosObjetos.add(new ObjetoMapa(nombre, ow, oh, celdasObjeto, categoriaObjeto));
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
            tiposTile.clear();
            tiposTile.addAll(nuevosTipos);
            capas = newLayers;
            colisiones = newCollisions;
            colisionesManuales = newManualCollisions;
            hierbaEncuentros = newGrass;
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
            actualizarTipoSeleccionado();
            recalcularTodasPropiedades();
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

    /**
     * Exporta por separado todos los PNG de Tiles/. Cada spritesheet tiene su propia
     * carpeta y dentro se escriben los tiles visibles de 16x16.
     */
    private void exportarTiles() {
        Path fuente = Path.of("Tiles");
        if (!Files.isDirectory(fuente)) {
            error("No existe la carpeta de origen: " + fuente.toAbsolutePath());
            return;
        }

        List<Path> pngs;
        try (java.util.stream.Stream<Path> archivos = Files.list(fuente)) {
            pngs = archivos
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(java.util.Locale.ROOT).endsWith(".png"))
                    .sorted(java.util.Comparator.comparing(
                            p -> p.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } catch (IOException ex) {
            error("No se pudo leer la carpeta Tiles/: " + ex.getMessage());
            return;
        }

        if (pngs.isEmpty()) {
            error("No hay archivos PNG en " + fuente.toAbsolutePath());
            return;
        }

        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Selecciona dónde crear las carpetas de tiles");
        selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        selector.setAcceptAllFileFilterUsed(false);
        if (selector.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;

        Path destino = selector.getSelectedFile().toPath();
        int hojasExportadas = 0;
        int tilesExportados = 0;
        int tilesVacios = 0;
        int hojasOmitidas = 0;
        java.util.Set<String> carpetasUsadas = new java.util.HashSet<>();

        try {
            Files.createDirectories(destino);
            for (Path png : pngs) {
                BufferedImage imagen = ImageIO.read(png.toFile());
                if (imagen == null || imagen.getWidth() % TILE != 0 || imagen.getHeight() % TILE != 0) {
                    hojasOmitidas++;
                    continue;
                }

                String base = png.getFileName().toString();
                int punto = base.lastIndexOf('.');
                if (punto > 0) base = base.substring(0, punto);
                base = base.replaceAll("[^\\p{L}\\p{N}._-]", "_");
                if (base.isBlank() || base.equals(".") || base.equals("..")) base = "spritesheet";
                String unico = base;
                int sufijo = 2;
                while (!carpetasUsadas.add(unico.toLowerCase(java.util.Locale.ROOT))) {
                    unico = base + "_" + sufijo++;
                }

                Path carpeta = destino.resolve(unico);
                Files.createDirectories(carpeta);
                int deEstaHoja = 0;
                for (int y = 0; y < imagen.getHeight(); y += TILE) {
                    for (int x = 0; x < imagen.getWidth(); x += TILE) {
                        BufferedImage tile = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = tile.createGraphics();
                        g.drawImage(imagen, 0, 0, TILE, TILE, x, y, x + TILE, y + TILE, null);
                        g.dispose();

                        if (!tienePixelVisible(tile)) {
                            tilesVacios++;
                            continue;
                        }

                        String nombreTile = String.format(java.util.Locale.ROOT,
                                "tile_%03d_%03d.png", y / TILE, x / TILE);
                        if (!ImageIO.write(tile, "png", carpeta.resolve(nombreTile).toFile())) {
                            throw new IOException("No se pudo escribir " + carpeta.resolve(nombreTile));
                        }
                        tilesExportados++;
                        deEstaHoja++;
                    }
                }
                if (deEstaHoja > 0) hojasExportadas++;
            }

            String mensaje = "Exportación terminada.\nCarpetas con tiles: " + hojasExportadas
                    + "\nTiles PNG guardados: " + tilesExportados
                    + "\nTiles transparentes omitidos: " + tilesVacios
                    + "\nSpritesheets inválidas omitidas: " + hojasOmitidas
                    + "\nDestino: " + destino.toAbsolutePath();
            JOptionPane.showMessageDialog(frame, mensaje, "Exportar tiles", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            error("La exportación se interrumpió: " + ex.getMessage());
        }
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
                        if (tienePixelVisible(tile)) {
                            tiles.add(tile);
                            tiposTile.add(TipoTile.NORMAL);
                        }
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

    /** Recalcula las propiedades derivadas de las capas y los objetos colocados. */
    private void actualizarPropiedadesTileEnMapa(int tileModificado) {
        recalcularTodasPropiedades();
        canvas.repaint();
    }

    private void aplicarPropiedadesTile(int indiceMapa, int indiceTile) {
        if (indiceMapa < 0 || indiceMapa >= colisiones.length) return;
        recalcularPropiedadesCelda(indiceMapa);
    }

    private void recalcularTodasPropiedades() {
        for (int i = 0; i < ancho * alto; i++) recalcularPropiedadesCelda(i);
    }

    private void recalcularPropiedadesCelda(int indiceMapa) {
        if (indiceMapa < 0 || indiceMapa >= colisiones.length) return;
        boolean bloquea = colisionesManuales[indiceMapa];
        boolean hierba = false;

        for (int capaActual = 0; capaActual < capas.length; capaActual++) {
            int t = capas[capaActual][indiceMapa];
            if (t < 0 || t >= tiposTile.size()) continue;
            TipoTile tipo = tiposTile.get(t);
            if (tipo == TipoTile.BLOQUEO || tipo == TipoTile.AGUA) bloquea = true;
            if (capaActual == 0 && tipo == TipoTile.HIERBA) hierba = true;
        }

        int x = indiceMapa % ancho;
        int y = indiceMapa / ancho;
        for (ColocacionObjeto colocacion : colocaciones) {
            if (colocacion.objeto < 0 || colocacion.objeto >= objetos.size()) continue;
            ObjetoMapa objeto = objetos.get(colocacion.objeto);
            int ox = x - colocacion.x;
            int oy = y - colocacion.y;
            if (ox < 0 || oy < 0 || ox >= objeto.ancho || oy >= objeto.alto) continue;
            int t = objeto.celdas[oy * objeto.ancho + ox];
            if (t < 0 || t >= tiposTile.size()) continue;
            TipoTile tipo = tiposTile.get(t);
            if (tipo == TipoTile.BLOQUEO || tipo == TipoTile.AGUA) bloquea = true;
            if (colocacion.capa == 0 && tipo == TipoTile.HIERBA) hierba = true;
        }

        colisiones[indiceMapa] = bloquea;
        hierbaEncuentros[indiceMapa] = !bloquea && hierba;
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
                        colisionesManuales[i] = !derecho;
                        recalcularPropiedadesCelda(i);
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
                    } else if (derecho) {
                        capas[capa][i] = -1;
                        recalcularPropiedadesCelda(i);
                    } else if (seleccionado >= 0 && seleccionado < tiles.size()) {
                        capas[capa][i] = seleccionado;
                        aplicarPropiedadesTile(i, seleccionado);
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
                if ((modoColision || mostrarPropiedades) && colisiones[i]) {
                    g.setColor(new Color(235, 45, 45, 125));
                    g.fillRect(x, y, TILE * ZOOM, TILE * ZOOM);
                } else if (mostrarPropiedades && hierbaEncuentros[i]) {
                    g.setColor(new Color(30, 220, 70, 125));
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
