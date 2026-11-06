package Proyecto;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Exploración gráfica de rutas con movimiento por casillas (WASD).
 *
 * Lee los proyectos .pmap exportados por Pokémon Studio. Si todavía no existe
 * un mapa para la ruta, construye un escenario provisional para que la función
 * sea jugable desde el primer momento.
 */
public final class ExploradorMapa {
    private static final int MAGIC_PMAP = 0x504D4150;
    private static final int TILE = 16;
    private static final int ZOOM = 3;
    private static final int TIPO_NORMAL = 0;
    private static final int TIPO_BLOQUEO = 1;
    private static final int TIPO_HIERBA = 2;
    private static final int TIPO_AGUA = 3;
    private static final Random AZAR = new Random();

    private ExploradorMapa() { }

    /** Resultado de una sesión de exploración. El combate se ejecuta fuera del EDT. */
    public static final class Resultado {
        private final boolean encuentro;
        private final int x;
        private final int y;

        private Resultado(boolean encuentro, int x, int y) {
            this.encuentro = encuentro;
            this.x = x;
            this.y = y;
        }

        public boolean huboEncuentro() { return encuentro; }
        public int getX() { return x; }
        public int getY() { return y; }
    }

    /**
     * Abre el mapa y espera a que el jugador lo cierre o encuentre un Pokémon.
     * El método se invoca desde el hilo principal de la aventura, nunca desde Swing.
     */
    public static Resultado explorar(Jugador jugador, Zona zona) {
        if (jugador == null || zona == null) return new Resultado(false, 0, 0);
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("La exploración WASD necesita un entorno gráfico.");
            return new Resultado(false, 0, 0);
        }
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("La exploración debe iniciarse desde el hilo del juego.");
        }

        String mapaId = zona.getUbicacion().name();
        MapaDatos mapa = cargarMapa(zona.getUbicacion());
        int[] guardada = jugador.getPosicionMapa(mapaId);
        Point inicio = buscarInicio(mapa, guardada, jugador.puedeNavegarAgua());
        BufferedImage jugadorSprite = cargarSpriteJugador();

        CountDownLatch fin = new CountDownLatch(1);
        AtomicReference<Resultado> resultado = new AtomicReference<>();
        SwingUtilities.invokeLater(() -> abrirVentana(jugador, zona, mapaId, mapa,
                jugadorSprite, inicio, resultado, fin));

        try {
            fin.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return new Resultado(false, inicio.x, inicio.y);
        }

        Resultado salida = resultado.get();
        if (salida == null) salida = new Resultado(false, inicio.x, inicio.y);
        jugador.guardarPosicionMapa(mapaId, salida.x, salida.y);
        return salida;
    }

    private static void abrirVentana(Jugador jugador, Zona zona, String mapaId, MapaDatos mapa,
                                     BufferedImage jugadorSprite, Point inicio,
                                     AtomicReference<Resultado> resultado, CountDownLatch fin) {
        JFrame ventana = new JFrame("Pokémon — " + zona.getNombre());
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        JLabel estado = new JLabel();
        estado.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        JLabel ayuda = new JLabel("W A S D: moverse    ·    Esc: volver al menú    ·    Los Pokémon aparecen al caminar por la hierba");
        ayuda.setBorder(BorderFactory.createEmptyBorder(5, 10, 8, 10));

        MapaCanvas lienzo = new MapaCanvas(mapa, jugadorSprite, inicio.x, inicio.y);
        JScrollPane desplazamiento = new JScrollPane(lienzo);
        desplazamiento.getViewport().setBackground(new Color(30, 35, 35));
        ventana.add(estado, BorderLayout.NORTH);
        ventana.add(desplazamiento, BorderLayout.CENTER);
        ventana.add(ayuda, BorderLayout.SOUTH);

        Runnable cerrar = () -> {
            MapaCanvas.Posicion p = lienzo.getPosicion();
            resultado.compareAndSet(null, new Resultado(false, p.x, p.y));
            fin.countDown();
            ventana.dispose();
        };

        actualizarEstado(estado, jugador, lienzo, mapa, mapaId);
        instalarMovimiento(ventana, jugador, zona, mapa, lienzo, estado,
                resultado, fin, cerrar);

        ventana.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                MapaCanvas.Posicion p = lienzo.getPosicion();
                resultado.compareAndSet(null, new Resultado(false, p.x, p.y));
                fin.countDown();
            }
        });

        ventana.setSize(1000, 760);
        ventana.setMinimumSize(new Dimension(640, 480));
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        SwingUtilities.invokeLater(() -> centrarVista(desplazamiento, lienzo));
        lienzo.requestFocusInWindow();
    }

    private static void instalarMovimiento(JFrame ventana, Jugador jugador, Zona zona,
                                           MapaDatos mapa, MapaCanvas lienzo, JLabel estado,
                                           AtomicReference<Resultado> resultado, CountDownLatch fin,
                                           Runnable cerrar) {
        InputMap entradas = lienzo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap acciones = lienzo.getActionMap();

        entradas.put(KeyStroke.getKeyStroke("ESCAPE"), "salir");
        acciones.put("salir", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { cerrar.run(); }
        });

        // Se mantiene un manejador local para que el cierre por encuentro sea inmediato.
        class Movimiento {
            void avanzar(int dx, int dy) {
                MapaCanvas.Posicion p = lienzo.getPosicion();
                int nx = p.x + dx, ny = p.y + dy;
                if (nx < 0 || ny < 0 || nx >= mapa.ancho || ny >= mapa.alto) {
                    estado.setText("Has llegado al límite del mapa. Pulsa Esc para volver al menú.");
                    return;
                }
                boolean puedeNavegar = jugador.puedeNavegarAgua();
                if (mapa.esAgua(nx, ny) && !puedeNavegar) {
                    estado.setText("El agua te impide avanzar. Necesitas un Pokémon de tipo Agua, Volador o Dragón que esté en pie.");
                    return;
                }
                if (!mapa.puedePisar(nx, ny, puedeNavegar)) {
                    estado.setText("No puedes pasar: hay un árbol, una pared u otro obstáculo.");
                    return;
                }

                lienzo.setPosicion(nx, ny);
                lienzo.repaint();
                centrarVista((JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, lienzo), lienzo);
                actualizarEstado(estado, jugador, lienzo, mapa, mapaId);

                if (mapa.esHierba(nx, ny) && AZAR.nextInt(100) < 16) {
                    MapaCanvas.Posicion actual = lienzo.getPosicion();
                    resultado.set(new Resultado(true, actual.x, actual.y));
                    fin.countDown();
                    ventana.dispose();
                    estado.setText("¡Algo se mueve entre la hierba!");
                }
            }
        }
        Movimiento movimiento = new Movimiento();

        acciones.put("arriba", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { movimiento.avanzar(0, -1); }
        });
        acciones.put("izquierda", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { movimiento.avanzar(-1, 0); }
        });
        acciones.put("abajo", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { movimiento.avanzar(0, 1); }
        });
        acciones.put("derecha", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { movimiento.avanzar(1, 0); }
        });
    }

    private static void actualizarEstado(JLabel etiqueta, Jugador jugador, MapaCanvas lienzo,
                                         MapaDatos mapa, String mapaId) {
        MapaCanvas.Posicion p = lienzo.getPosicion();
        String agua = jugador.puedeNavegarAgua() ? "Puedes cruzar agua con tu equipo." : "No puedes cruzar agua todavía.";
        etiqueta.setText("  " + mapaId + "  ·  Posición: " + (p.x + 1) + ", " + (p.y + 1)
                + "  ·  " + agua);
    }

    private static void centrarVista(JScrollPane scroll, MapaCanvas lienzo) {
        if (scroll == null) return;
        MapaCanvas.Posicion p = lienzo.getPosicion();
        JViewport vista = scroll.getViewport();
        int px = p.x * TILE * ZOOM + TILE * ZOOM / 2 - vista.getWidth() / 2;
        int py = p.y * TILE * ZOOM + TILE * ZOOM / 2 - vista.getHeight() / 2;
        int maxX = Math.max(0, lienzo.getWidth() - vista.getWidth());
        int maxY = Math.max(0, lienzo.getHeight() - vista.getHeight());
        vista.setViewPosition(new Point(Math.max(0, Math.min(maxX, px)), Math.max(0, Math.min(maxY, py))));
    }

    private static Point buscarInicio(MapaDatos mapa, int[] guardada, boolean puedeNavegarAgua) {
        if (guardada != null && guardada.length == 2
                && mapa.dentro(guardada[0], guardada[1])
                && mapa.puedePisar(guardada[0], guardada[1], puedeNavegarAgua)) {
            return new Point(guardada[0], guardada[1]);
        }
        int cx = mapa.ancho / 2;
        int cy = mapa.alto / 2;
        for (int radio = 0; radio < Math.max(mapa.ancho, mapa.alto); radio++) {
            for (int y = Math.max(0, cy - radio); y <= Math.min(mapa.alto - 1, cy + radio); y++) {
                for (int x = Math.max(0, cx - radio); x <= Math.min(mapa.ancho - 1, cx + radio); x++) {
                    if (Math.abs(x - cx) + Math.abs(y - cy) != radio) continue;
                    if (mapa.puedePisar(x, y, puedeNavegarAgua) && !mapa.esAgua(x, y)) return new Point(x, y);
                }
            }
        }
        return new Point(0, 0);
    }

    private static MapaDatos cargarMapa(Ubicacion ubicacion) {
        for (Path candidato : rutasMapa(ubicacion)) {
            if (!Files.isRegularFile(candidato)) continue;
            try {
                return leerPmap(candidato);
            } catch (IOException | RuntimeException ex) {
                System.err.println("No se pudo cargar el mapa " + candidato + ": " + ex.getMessage());
            }
        }
        return crearMapaProvisional(ubicacion);
    }

    private static List<Path> rutasMapa(Ubicacion ubicacion) {
        String nombre = ubicacion.name();
        List<Path> rutas = new ArrayList<>();
        rutas.add(Path.of("Mapas", nombre + ".pmap"));
        rutas.add(Path.of("mapas", nombre + ".pmap"));
        rutas.add(Path.of("Maps", nombre + ".pmap"));
        rutas.add(Path.of(nombre + ".pmap"));
        rutas.add(Path.of("mapa.pmap"));
        return rutas;
    }

    /** Lee el formato binario .pmap usado por EditorMapas (versiones 1 a 5). */
    private static MapaDatos leerPmap(Path archivo) throws IOException {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(archivo)))) {
            if (in.readInt() != MAGIC_PMAP) throw new IOException("Cabecera .pmap no reconocida.");
            int version = in.readInt();
            if (version < 1 || version > 5) throw new IOException("Versión .pmap no compatible: " + version);
            int ancho = in.readInt(), alto = in.readInt(), cantidadTiles = in.readInt();
            if (ancho < 1 || alto < 1 || ancho > 300 || alto > 300
                    || cantidadTiles < 0 || cantidadTiles > 10000 || (version == 1 && cantidadTiles == 0)) {
                throw new IOException("Dimensiones o paleta del mapa no válidas.");
            }
            int size = ancho * alto;
            BufferedImage[] tiles = new BufferedImage[cantidadTiles];
            for (int i = 0; i < cantidadTiles; i++) {
                int longitud = in.readInt();
                if (longitud < 1 || longitud > 10_000_000) throw new IOException("Imagen de tile corrupta.");
                byte[] bytes = in.readNBytes(longitud);
                if (bytes.length != longitud) throw new EOFException();
                BufferedImage original = ImageIO.read(new java.io.ByteArrayInputStream(bytes));
                if (original == null) throw new IOException("No se pudo decodificar un tile del mapa.");
                BufferedImage fijo = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = fijo.createGraphics();
                g.drawImage(original, 0, 0, TILE, TILE, null);
                g.dispose();
                tiles[i] = fijo;
            }

            byte[] tipos = new byte[cantidadTiles];
            for (int i = 0; i < cantidadTiles; i++) {
                int tipo = version >= 3 ? in.readUnsignedByte() : TIPO_NORMAL;
                if (tipo > 30) throw new IOException("Tipo de tile desconocido.");
                tipos[i] = (byte) tipo;
            }

            int[][] capas = new int[3][size];
            for (int c = 0; c < 3; c++) {
                for (int i = 0; i < size; i++) {
                    int tile = in.readInt();
                    if (tile < -1 || tile >= cantidadTiles) throw new IOException("Índice de tile fuera de rango.");
                    capas[c][i] = tile;
                }
            }

            boolean[] colisiones = new boolean[size];
            for (int i = 0; i < size; i++) colisiones[i] = in.readBoolean();
            boolean[] manuales = colisiones.clone();
            if (version >= 4) for (int i = 0; i < size; i++) manuales[i] = in.readBoolean();
            boolean[] hierba = new boolean[size];
            if (version >= 3) for (int i = 0; i < size; i++) hierba[i] = in.readBoolean();

            List<ObjetoMapa> objetos = new ArrayList<>();
            List<Colocacion> colocaciones = new ArrayList<>();
            if (version >= 2) {
                int numeroObjetos = in.readInt();
                if (numeroObjetos < 0 || numeroObjetos > 10000) throw new IOException("Número de objetos inválido.");
                for (int i = 0; i < numeroObjetos; i++) {
                    String nombre = in.readUTF();
                    int ow = in.readInt(), oh = in.readInt();
                    if (ow < 1 || oh < 1 || ow > 32 || oh > 32) throw new IOException("Dimensiones de objeto inválidas.");
                    int[] celdas = new int[ow * oh];
                    for (int j = 0; j < celdas.length; j++) {
                        celdas[j] = in.readInt();
                        if (celdas[j] < -1 || celdas[j] >= cantidadTiles)
                            throw new IOException("Tile de objeto fuera de rango.");
                    }
                    String categoria = version >= 5 ? in.readUTF() : inferirCategoria(nombre);
                    objetos.add(new ObjetoMapa(nombre, ow, oh, celdas, categoria));
                }
                int numeroColocaciones = in.readInt();
                if (numeroColocaciones < 0 || numeroColocaciones > 1_000_000)
                    throw new IOException("Número de colocaciones inválido.");
                for (int i = 0; i < numeroColocaciones; i++) {
                    int objeto = in.readInt(), x = in.readInt(), y = in.readInt(), capa = in.readInt();
                    if (objeto < 0 || objeto >= objetos.size() || x < 0 || y < 0
                            || x >= ancho || y >= alto || capa < 0 || capa > 2)
                        throw new IOException("Colocación de objeto inválida.");
                    colocaciones.add(new Colocacion(objeto, x, y, capa));
                }
            }
            return new MapaDatos(ancho, alto, tiles, tipos, capas, colisiones, manuales,
                    hierba, objetos, colocaciones, false);
        }
    }

    private static String inferirCategoria(String nombre) {
        String n = nombre == null ? "" : normalizar(nombre);
        if (n.contains("centropokemon") || n.contains("pokecentro")) return "CENTRO_POKEMON";
        if (n.contains("gimnasio") || n.contains("gym")) return "GIMNASIO";
        if (n.contains("tienda") || n.contains("shop")) return "TIENDA";
        if (n.contains("arbol") || n.contains("tree")) return "ARBOL";
        if (n.contains("roca") || n.contains("rock")) return "ROCA";
        if (n.contains("casa") || n.contains("house")) return "CASA";
        return "OTRO";
    }

    private static String normalizar(String valor) {
        return Normalizer.normalize(valor == null ? "" : valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
    }

    private static BufferedImage cargarSpriteJugador() {
        for (String nombre : List.of("Jugador.png", "Player.png", "jugador.png")) {
            Path ruta = Path.of("Tiles", nombre);
            if (!Files.isRegularFile(ruta)) continue;
            try {
                BufferedImage hoja = ImageIO.read(ruta.toFile());
                if (hoja != null && hoja.getWidth() >= TILE && hoja.getHeight() >= TILE) {
                    BufferedImage tile = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = tile.createGraphics();
                    g.drawImage(hoja, 0, 0, TILE, TILE, 0, 0, TILE, TILE, null);
                    g.dispose();
                    if (tienePixelVisible(tile)) return tile;
                }
            } catch (IOException ex) {
                System.err.println("No se pudo cargar el sprite del jugador: " + ex.getMessage());
            }
        }
        return crearSpriteJugador();
    }

    private static BufferedImage crearSpriteJugador() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(30, 45, 70)); g.fillRect(5, 2, 6, 4);
        g.setColor(new Color(235, 190, 145)); g.fillRect(5, 5, 6, 5);
        g.setColor(new Color(45, 90, 175)); g.fillRect(4, 10, 8, 4);
        g.setColor(new Color(35, 55, 95)); g.fillRect(4, 14, 3, 2); g.fillRect(9, 14, 3, 2);
        g.setColor(Color.BLACK); g.fillRect(6, 7, 1, 1); g.fillRect(9, 7, 1, 1);
        g.dispose();
        return im;
    }

    private static boolean tienePixelVisible(BufferedImage im) {
        for (int y = 0; y < im.getHeight(); y++)
            for (int x = 0; x < im.getWidth(); x++)
                if ((im.getRGB(x, y) >>> 24) != 0) return true;
        return false;
    }

    private static MapaDatos crearMapaProvisional(Ubicacion ubicacion) {
        int ancho = 36, alto = 24, size = ancho * alto;
        BufferedImage[] tiles = {
                tile(new Color(78, 157, 70), new Color(95, 177, 79)),
                tile(new Color(190, 164, 118), new Color(212, 188, 141)),
                hierbaTile(),
                aguaTile(),
                arbolTile(),
                paredTile()
        };
        byte[] tipos = {TIPO_NORMAL, TIPO_NORMAL, TIPO_HIERBA, TIPO_AGUA, TIPO_BLOQUEO, TIPO_BLOQUEO};
        int[][] capas = new int[3][size];
        for (int[] capa : capas) Arrays.fill(capa, -1);
        Arrays.fill(capas[0], 0);
        boolean[] colisiones = new boolean[size], manuales = new boolean[size], hierba = new boolean[size];

        int caminoY = alto / 2;
        for (int x = 0; x < ancho; x++) capas[1][caminoY * ancho + x] = 1;
        for (int y = 0; y < alto; y++) capas[1][y * ancho + ancho / 2] = 1;
        for (int y = 4; y < 10; y++) for (int x = 4; x < 11; x++) {
            if (capas[1][y * ancho + x] < 0) {
                capas[1][y * ancho + x] = 2;
                hierba[y * ancho + x] = true;
            }
        }
        for (int y = 14; y < 20; y++) for (int x = 24; x < 32; x++) {
            if (capas[1][y * ancho + x] < 0) {
                capas[1][y * ancho + x] = 2;
                hierba[y * ancho + x] = true;
            }
        }
        for (int y = 3; y < 9; y++) for (int x = 23; x < 29; x++) {
            if (capas[1][y * ancho + x] != 1) capas[1][y * ancho + x] = 3;
        }

        // Árboles y una pequeña pared perimetral: todos son sólidos.
        for (int x = 0; x < ancho; x++) {
            ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, 0, 4);
            ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, alto - 1, 4);
        }
        for (int y = 1; y < alto - 1; y++) {
            ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, 0, y, 4);
            ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, ancho - 1, y, 4);
        }
        for (int y = 3; y < 7; y++) for (int x = 14; x < 19; x++) {
            if (y == 3 || y == 6 || x == 14 || x == 18)
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, y, 5);
        }
        Random r = new Random(ubicacion == null ? 1 : ubicacion.ordinal() * 919L + 47);
        for (int n = 0; n < 24; n++) {
            int x = 2 + r.nextInt(ancho - 4), y = 2 + r.nextInt(alto - 4);
            int idx = y * ancho + x;
            if (capas[1][idx] == 1 || capas[1][idx] == 3 || (x >= 14 && x <= 18 && y >= 3 && y <= 6)) continue;
            ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, y, 4);
        }
        return new MapaDatos(ancho, alto, tiles, tipos, capas, colisiones, manuales, hierba,
                List.of(), List.of(), true);
    }

    private static void ponerObstaculo(int[] capa, boolean[] colisiones, boolean[] manuales,
                                       int ancho, int alto, int x, int y, int tile) {
        if (x < 0 || y < 0 || x >= ancho || y >= alto) return;
        int idx = y * ancho + x;
        capa[idx] = tile;
        colisiones[idx] = true;
        manuales[idx] = true;
    }

    private static BufferedImage tile(Color base, Color detalle) {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(base); g.fillRect(0, 0, TILE, TILE);
        g.setColor(detalle);
        for (int y = 2; y < TILE; y += 5)
            for (int x = (y % 2) * 3; x < TILE; x += 7) g.fillRect(x, y, 2, 1);
        g.dispose();
        return im;
    }

    private static BufferedImage hierbaTile() {
        BufferedImage im = tile(new Color(46, 123, 54), new Color(32, 91, 43));
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(143, 199, 91));
        for (int x = 2; x < 16; x += 5) { g.drawLine(x, 14, x - 1, 7); g.drawLine(x, 14, x + 1, 9); }
        g.dispose();
        return im;
    }

    private static BufferedImage aguaTile() {
        BufferedImage im = tile(new Color(43, 117, 188), new Color(113, 187, 224));
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(170, 215, 238)); g.drawLine(1, 5, 8, 5); g.drawLine(8, 12, 15, 12);
        g.dispose();
        return im;
    }

    private static BufferedImage arbolTile() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(26, 76, 38)); g.fillOval(0, 0, 16, 14);
        g.setColor(new Color(47, 130, 57)); g.fillOval(2, 0, 12, 12);
        g.setColor(new Color(119, 171, 76)); g.fillOval(5, 2, 5, 4);
        g.setColor(new Color(104, 70, 42)); g.fillRect(7, 10, 3, 6);
        g.dispose();
        return im;
    }

    private static BufferedImage paredTile() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(116, 111, 119)); g.fillRect(0, 0, 16, 16);
        g.setColor(new Color(167, 157, 157)); g.drawLine(0, 5, 15, 5); g.drawLine(0, 11, 15, 11);
        g.setColor(new Color(73, 70, 76)); g.drawRect(0, 0, 15, 15);
        g.dispose();
        return im;
    }

    /** Reglas puras para que las prueben los tests sin abrir una ventana. */
    static boolean permiteAgua(boolean esAgua, boolean puedeNadarOVolar) {
        return !esAgua || puedeNadarOVolar;
    }

    static boolean generaEncuentro(boolean esHierba) {
        return esHierba;
    }

    private static final class MapaDatos {
        final int ancho, alto;
        final BufferedImage[] tiles;
        final byte[] tipos;
        final int[][] capas;
        final boolean[] colisiones, manuales, hierba;
        final List<ObjetoMapa> objetos;
        final List<Colocacion> colocaciones;
        final boolean provisional;
        final boolean[] colisionObjetos;

        MapaDatos(int ancho, int alto, BufferedImage[] tiles, byte[] tipos, int[][] capas,
                  boolean[] colisiones, boolean[] manuales, boolean[] hierba,
                  List<ObjetoMapa> objetos, List<Colocacion> colocaciones, boolean provisional) {
            this.ancho = ancho;
            this.alto = alto;
            this.tiles = tiles;
            this.tipos = tipos;
            this.capas = capas;
            this.colisiones = colisiones;
            this.manuales = manuales;
            this.hierba = hierba;
            this.objetos = objetos;
            this.colocaciones = colocaciones;
            this.provisional = provisional;
            this.colisionObjetos = new boolean[ancho * alto];
            for (Colocacion p : colocaciones) {
                if (p.objeto < 0 || p.objeto >= objetos.size()) continue;
                ObjetoMapa o = objetos.get(p.objeto);
                boolean solido = o.esSolido();
                for (int oy = 0; oy < o.alto; oy++) for (int ox = 0; ox < o.ancho; ox++) {
                    int indiceObjeto = oy * o.ancho + ox;
                    if (indiceObjeto >= o.celdas.length || o.celdas[indiceObjeto] < 0) continue;
                    int x = p.x + ox, y = p.y + oy;
                    if (dentro(x, y) && solido) colisionObjetos[y * ancho + x] = true;
                }
            }
        }

        boolean dentro(int x, int y) { return x >= 0 && y >= 0 && x < ancho && y < alto; }

        boolean esAgua(int x, int y) {
            if (!dentro(x, y)) return false;
            int i = y * ancho + x;
            for (int[] capa : capas) if (esTipo(capa[i], TIPO_AGUA)) return true;
            return false;
        }

        boolean esHierba(int x, int y) {
            if (!dentro(x, y)) return false;
            int i = y * ancho + x;
            if (hierba[i]) return true;
            for (int[] capa : capas) if (esTipo(capa[i], TIPO_HIERBA)) return true;
            return false;
        }

        boolean puedePisar(int x, int y, boolean puedeNavegarAgua) {
            if (!dentro(x, y)) return false;
            int i = y * ancho + x;
            boolean agua = esAgua(x, y);
            if (!permiteAgua(agua, puedeNavegarAgua)) return false;
            if (manuales[i] || colisionObjetos[i]) return false;

            for (int[] capa : capas) {
                int tile = capa[i];
                if (esTipo(tile, TIPO_BLOQUEO)) return false;
            }
            // La máscara de colisión del editor incluye el agua. Si el equipo puede
            // navegar, se ignora esa marca concreta, no las colisiones de arriba.
            return agua || !colisiones[i];
        }

        private boolean esTipo(int indice, int tipo) {
            return indice >= 0 && indice < tipos.length && (tipos[indice] & 0xFF) == tipo;
        }
    }

    private static final class ObjetoMapa {
        final String nombre, categoria;
        final int ancho, alto;
        final int[] celdas;

        ObjetoMapa(String nombre, int ancho, int alto, int[] celdas, String categoria) {
            this.nombre = nombre == null ? "" : nombre;
            this.ancho = ancho;
            this.alto = alto;
            this.celdas = celdas;
            this.categoria = categoria == null ? "OTRO" : categoria;
        }

        boolean esSolido() {
            String c = categoria.toUpperCase(Locale.ROOT);
            return c.equals("ARBOL") || c.equals("ROCA") || c.equals("CASA")
                    || c.equals("CENTRO_POKEMON") || c.equals("CENTRO_POKÉMON")
                    || c.equals("TIENDA") || c.equals("GIMNASIO");
        }
    }

    private static final class Colocacion {
        final int objeto, x, y, capa;
        Colocacion(int objeto, int x, int y, int capa) {
            this.objeto = objeto; this.x = x; this.y = y; this.capa = capa;
        }
    }

    private static final class MapaCanvas extends JPanel {
        private final MapaDatos mapa;
        private final BufferedImage sprite;
        private int jugadorX, jugadorY;

        MapaCanvas(MapaDatos mapa, BufferedImage sprite, int x, int y) {
            this.mapa = mapa;
            this.sprite = sprite;
            jugadorX = x;
            jugadorY = y;
            setOpaque(true);
            setBackground(new Color(30, 35, 35));
            setPreferredSize(new Dimension(mapa.ancho * TILE * ZOOM, mapa.alto * TILE * ZOOM));
            setFocusable(true);
        }

        Posicion getPosicion() { return new Posicion(jugadorX, jugadorY); }
        void setPosicion(int x, int y) { jugadorX = x; jugadorY = y; }

        @Override protected void paintComponent(Graphics gOriginal) {
            super.paintComponent(gOriginal);
            Graphics2D g = (Graphics2D) gOriginal.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int tam = TILE * ZOOM;
            for (int capa = 0; capa < mapa.capas.length; capa++) {
                for (int i = 0; i < mapa.ancho * mapa.alto; i++) {
                    int tile = mapa.capas[capa][i];
                    if (tile < 0 || tile >= mapa.tiles.length || mapa.tiles[tile] == null) continue;
                    g.drawImage(mapa.tiles[tile], (i % mapa.ancho) * tam,
                            (i / mapa.ancho) * tam, tam, tam, null);
                }
                for (Colocacion p : mapa.colocaciones) {
                    if (p.capa != capa || p.objeto < 0 || p.objeto >= mapa.objetos.size()) continue;
                    ObjetoMapa o = mapa.objetos.get(p.objeto);
                    for (int oy = 0; oy < o.alto; oy++) for (int ox = 0; ox < o.ancho; ox++) {
                        int pos = oy * o.ancho + ox;
                        int tile = pos < o.celdas.length ? o.celdas[pos] : -1;
                        if (tile < 0 || tile >= mapa.tiles.length || mapa.tiles[tile] == null) continue;
                        g.drawImage(mapa.tiles[tile], (p.x + ox) * tam, (p.y + oy) * tam, tam, tam, null);
                    }
                }
            }
            g.drawImage(sprite, jugadorX * tam, jugadorY * tam, tam, tam, null);
            g.dispose();
        }

        private static final class Posicion {
            final int x, y;
            Posicion(int x, int y) { this.x = x; this.y = y; }
        }
    }
}
