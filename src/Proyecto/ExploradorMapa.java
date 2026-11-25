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
    // En los mapas regionales los PNG son de 32x32. La vista compacta
    // muestra 16 px por casilla (reducción exacta 2:1, sin suavizado),
    // para que un mapa de 36x24 ocupe 576x384 px en pantalla.
    private static final int[] ZOOM_REGION_PX = {16, 32, 64, 96, 128};

    static int pixelesPorCasilla(int resolucion, int nivelRegional) {
        if (resolucion != 32) return TILE * ZOOM;
        int nivel = Math.max(0, Math.min(ZOOM_REGION_PX.length-1, nivelRegional));
        return ZOOM_REGION_PX[nivel];
    }
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
        private final Ubicacion destino;
        private final int entradaX, entradaY;
        private final String evento;
        private Resultado(boolean encuentro, int x, int y) { this(encuentro, x, y, null, -1, -1, null); }
        private Resultado(boolean encuentro, int x, int y, Ubicacion destino, int entradaX, int entradaY) {
            this(encuentro, x, y, destino, entradaX, entradaY, null);
        }
        private Resultado(boolean encuentro, int x, int y, Ubicacion destino, int entradaX, int entradaY, String evento) {
            this.encuentro = encuentro; this.x = x; this.y = y;
            this.destino = destino; this.entradaX = entradaX; this.entradaY = entradaY;
            this.evento = evento;
        }
        public boolean huboEncuentro() { return encuentro; }
        public int getX() { return x; }
        public int getY() { return y; }
        public Ubicacion getDestino() { return destino; }
        /** Servicio o casa en cuya puerta ha entrado el jugador; null si solo exploró. */
        public String getEvento() { return evento; }
    }

    /**
     * Abre el mapa y espera a que el jugador lo cierre o encuentre un Pokémon.
     * El método se invoca desde el hilo principal de la aventura, nunca desde Swing.
     */
    public static Resultado explorar(Jugador jugador, Zona zona) {
        if (zona == null) return new Resultado(false, 0, 0);
        return explorarArea(jugador, zona.getUbicacion(), zona, true);
    }

    /** Explora un pueblo o ciudad sin encuentros salvajes. */
    public static Resultado explorarCiudad(Jugador jugador, Ubicacion ubicacion) {
        return explorarArea(jugador, ubicacion, null, false);
    }

    /** Abre uno de los cien mapas del atlas sin alterar la ubicación principal. */
    public static Resultado explorarMapaRegional(Jugador jugador, CatalogoMapasGeneracionales.Mapa regional) {
        if (jugador == null || regional == null) return new Resultado(false, 0, 0);
        return explorarArea(jugador, jugador.getUbicacion(), null, false, regional);
    }

    private static Resultado explorarArea(Jugador jugador, Ubicacion ubicacion,
                                           Zona zona, boolean permitirEncuentros) {
        return explorarArea(jugador, ubicacion, zona, permitirEncuentros, null);
    }

    private static Resultado explorarArea(Jugador jugador, Ubicacion ubicacion,
                                           Zona zona, boolean permitirEncuentros,
                                           CatalogoMapasGeneracionales.Mapa regional) {
        if (jugador == null || ubicacion == null) return new Resultado(false, 0, 0);
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("La exploración WASD necesita un entorno gráfico.");
            return new Resultado(false, 0, 0);
        }
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("La exploración debe iniciarse desde el hilo del juego.");
        }

        String mapaId = regional == null ? ubicacion.name() : regional.id();
        String titulo = regional != null
                ? regional.generacion().getRegion() + " · " + regional.nombre()
                : zona == null ? ubicacion.getNombre() : zona.getNombre();
        MapaDatos mapa = regional == null ? cargarMapa(ubicacion, jugador.getGeneracion())
                : convertir(MapasRegion2D.generar(regional));
        int[] guardada = jugador.getPosicionMapa(mapaId);
        Point inicio = buscarInicio(mapa, guardada, jugador.puedeNavegarAgua());
        SpriteJugador2D jugadorSprite = SpriteJugador2D.cargar();

        CountDownLatch fin = new CountDownLatch(1);
        AtomicReference<Resultado> resultado = new AtomicReference<>();
        SwingUtilities.invokeLater(() -> abrirVentana(jugador, titulo, zona, permitirEncuentros,
                mapaId, mapa, jugadorSprite, inicio, resultado, fin));

        try {
            fin.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return new Resultado(false, inicio.x, inicio.y);
        }

        Resultado salida = resultado.get();
        if (salida == null) salida = new Resultado(false, inicio.x, inicio.y);
        jugador.guardarPosicionMapa(mapaId, salida.x, salida.y);
        if (salida.destino != null) {
            jugador.guardarPosicionMapa(salida.destino.name(), salida.entradaX, salida.entradaY);
            System.out.println("Has llegado a " + salida.destino.getNombre() + ".");
        }
        return salida;
    }

    private static void abrirVentana(Jugador jugador, String titulo, Zona zona, boolean permitirEncuentros,
                                     String mapaId, MapaDatos mapa, SpriteJugador2D jugadorSprite, Point inicio,
                                     AtomicReference<Resultado> resultado, CountDownLatch fin) {
        JFrame ventana = new JFrame("Pokémon — " + titulo);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        JLabel estado = new JLabel();
        estado.setOpaque(true);
        estado.setBackground(new Color(24, 55, 102));
        estado.setForeground(Color.WHITE);
        estado.setFont(new Font("SansSerif", Font.BOLD, 14));
        estado.setBorder(BorderFactory.createEmptyBorder(11, 16, 11, 16));
        JLabel ayuda = new JLabel("WASD / flechas: caminar · T: chat · Enter: enviar · Esc: volver");
        ayuda.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ayuda.setForeground(new Color(64, 79, 96));
        ayuda.setBorder(BorderFactory.createEmptyBorder(5, 4, 8, 4));

        MapaCanvas lienzo = new MapaCanvas(mapa, jugadorSprite, inicio.x, inicio.y,
                identificadorRed(mapaId, jugador));
        JScrollPane desplazamiento = new JScrollPane(lienzo);
        desplazamiento.getViewport().setBackground(new Color(30, 35, 35));
        ventana.add(estado, BorderLayout.NORTH);
        ventana.add(desplazamiento, BorderLayout.CENTER);
        JPanel controles = new JPanel(new BorderLayout(8, 4));
        controles.setBackground(new Color(239, 245, 250));
        controles.setBorder(BorderFactory.createEmptyBorder(9, 14, 12, 14));
        controles.add(ayuda, BorderLayout.NORTH);
        JPanel cruceta = new JPanel(new GridLayout(2, 3, 7, 7));
        cruceta.setOpaque(false);
        JButton arriba = new JButton("▲");
        JButton izquierda = new JButton("◀");
        JButton abajo = new JButton("▼");
        JButton derecha = new JButton("▶");
        cruceta.add(new JLabel()); cruceta.add(arriba); cruceta.add(new JLabel());
        cruceta.add(izquierda); cruceta.add(abajo); cruceta.add(derecha);
        for (JButton b : new JButton[]{arriba, izquierda, abajo, derecha}) {
            b.setFont(new Font("SansSerif", Font.BOLD, 20));
            b.setForeground(new Color(35, 73, 119));
            b.setBackground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(177, 198, 219), 1, true),
                    BorderFactory.createEmptyBorder(4, 16, 4, 16)));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        controles.add(cruceta, BorderLayout.CENTER);
        if (mapa.resolucion == 32) {
            JPanel zoomControles = new JPanel(new GridLayout(3,1,3,3));
            JButton ampliar = new JButton("Zoom +");
            JButton reducir = new JButton("Zoom −");
            JButton compacto = new JButton("Ver todo");
            ampliar.addActionListener(e -> {
                lienzo.ajustarZoomRegional(1);
                centrarVista(desplazamiento,lienzo);
                lienzo.requestFocusInWindow();
            });
            reducir.addActionListener(e -> {
                lienzo.ajustarZoomRegional(-1);
                centrarVista(desplazamiento,lienzo);
                lienzo.requestFocusInWindow();
            });
            compacto.addActionListener(e -> {
                lienzo.vistaCompacta();
                centrarVista(desplazamiento,lienzo);
                lienzo.requestFocusInWindow();
            });
            zoomControles.add(ampliar);
            zoomControles.add(reducir);
            zoomControles.add(compacto);
            controles.add(zoomControles,BorderLayout.WEST);
        }
        ChatExploracion chat = new ChatExploracion(lienzo);
        JPanel lateralChat = new JPanel(new BorderLayout(3,3));
        lateralChat.add(chat,BorderLayout.CENTER);
        JButton botonChat = new JButton("Chat (T)");
        botonChat.addActionListener(e -> chat.abrirEntrada());
        lateralChat.add(botonChat,BorderLayout.SOUTH);
        controles.add(lateralChat,BorderLayout.EAST);
        ventana.add(controles, BorderLayout.SOUTH);

        Runnable cerrar = () -> {
            lienzo.detenerTodo();
            MapaCanvas.Posicion p = lienzo.getPosicion();
            resultado.compareAndSet(null, new Resultado(false, p.x, p.y));
            fin.countDown();
            ventana.dispose();
        };

        actualizarEstado(estado, jugador, lienzo, mapa, mapaId);
        RedLAN redInicial = RedLAN.actual();
        if (redInicial != null && redInicial.estaConectada())
            redInicial.publicarPosicion(identificadorRed(mapaId, jugador), inicio.x, inicio.y);
        instalarMovimiento(ventana, jugador, zona, permitirEncuentros, mapa, lienzo, estado, mapaId,
                resultado, fin, cerrar, chat);
        configurarBotonMovimiento(arriba, lienzo, "arriba");
        configurarBotonMovimiento(izquierda, lienzo, "izquierda");
        configurarBotonMovimiento(abajo, lienzo, "abajo");
        configurarBotonMovimiento(derecha, lienzo, "derecha");

        ventana.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                lienzo.detenerTodo();
                MapaCanvas.Posicion p = lienzo.getPosicion();
                resultado.compareAndSet(null, new Resultado(false, p.x, p.y));
                fin.countDown();
            }
        });

        // Respetar el área útil del monitor, incluidos portátiles con
        // barra de tareas: la ventana ya no se abre más alta que la pantalla.
        Rectangle escritorio = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        ventana.setSize(Math.min(1050, Math.max(480, escritorio.width - 24)),
                Math.min(740, Math.max(400, escritorio.height - 24)));
        ventana.setMinimumSize(new Dimension(
                Math.min(640, escritorio.width), Math.min(480, escritorio.height)));
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        SwingUtilities.invokeLater(() -> centrarVista(desplazamiento, lienzo));
        lienzo.requestFocusInWindow();
    }

    /** Mantener pulsado un control repite el movimiento sin exigir clics sucesivos. */
    private static void configurarBotonMovimiento(JButton boton, JComponent lienzo, String accion) {
        Timer repeticion = new Timer(145, e -> ejecutarMovimiento(lienzo, accion));
        repeticion.setInitialDelay(0);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) repeticion.start();
            }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { repeticion.stop(); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if ((e.getModifiersEx() & java.awt.event.InputEvent.BUTTON1_DOWN_MASK) == 0)
                    repeticion.stop();
            }
        });
        boton.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { repeticion.stop(); }
        });
    }

    private static void ejecutarMovimiento(JComponent lienzo, String accion) {
        javax.swing.Action movimiento = lienzo.getActionMap().get(accion);
        if (movimiento != null) movimiento.actionPerformed(
                new java.awt.event.ActionEvent(lienzo,
                        java.awt.event.ActionEvent.ACTION_PERFORMED, accion));
    }

    private static void instalarMovimiento(JFrame ventana, Jugador jugador, Zona zona, boolean permitirEncuentros,
                                           MapaDatos mapa, MapaCanvas lienzo, JLabel estado, String mapaId,
                                           AtomicReference<Resultado> resultado, CountDownLatch fin,
                                           Runnable cerrar, ChatExploracion chat) {
        InputMap entradas = lienzo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap acciones = lienzo.getActionMap();

        entradas.put(KeyStroke.getKeyStroke("W"), "arriba");
        entradas.put(KeyStroke.getKeyStroke("A"), "izquierda");
        entradas.put(KeyStroke.getKeyStroke("S"), "abajo");
        entradas.put(KeyStroke.getKeyStroke("D"), "derecha");
        entradas.put(KeyStroke.getKeyStroke("UP"), "arriba");
        entradas.put(KeyStroke.getKeyStroke("LEFT"), "izquierda");
        entradas.put(KeyStroke.getKeyStroke("DOWN"), "abajo");
        entradas.put(KeyStroke.getKeyStroke("RIGHT"), "derecha");
        entradas.put(KeyStroke.getKeyStroke("ESCAPE"), "salir");
        if (mapa.resolucion == 32) {
            entradas.put(KeyStroke.getKeyStroke("PLUS"), "zoom_mas");
            entradas.put(KeyStroke.getKeyStroke("EQUALS"), "zoom_mas");
            entradas.put(KeyStroke.getKeyStroke("MINUS"), "zoom_menos");
            acciones.put("zoom_mas", new AbstractAction() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    if(!chat.estaEscribiendo()) {
                        lienzo.ajustarZoomRegional(1);
                        JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(
                                JScrollPane.class,lienzo);
                        centrarVista(sp,lienzo);
                    }
                }
            });
            acciones.put("zoom_menos", new AbstractAction() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    if(!chat.estaEscribiendo()) {
                        lienzo.ajustarZoomRegional(-1);
                        JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(
                                JScrollPane.class,lienzo);
                        centrarVista(sp,lienzo);
                    }
                }
            });
        }
        acciones.put("salir", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                if (chat.estaEscribiendo()) chat.cancelarEntrada();
                else cerrar.run();
            }
        });

        // Se mantiene un manejador local para que el cierre por encuentro sea inmediato.
        class Movimiento {
            void avanzar(int dx, int dy) {
                // WASD, flechas y Esc se reservan al campo de texto al pulsar T.
                if (chat.estaEscribiendo() || lienzo.estaMoviendo()) return;
                lienzo.mirar(dx, dy);
                MapaCanvas.Posicion p = lienzo.getPosicion();
                int nx = p.x + dx, ny = p.y + dy;
                int centroY = mapa.alto / 2;
                // Las salidas de cada zona están en el centro de los laterales.
                if ((nx < 0 || nx >= mapa.ancho) && Math.abs(ny - centroY) <= 1) {
                    Ubicacion destino = vecinoHorizontal(mapaId, dx);
                    if (destino != null) {
                        MapaDatos mapaDestino = cargarMapa(destino, jugador.getGeneracion());
                        int entradaX = dx < 0 ? Math.max(1, mapaDestino.ancho - 2) : 1;
                        int entradaY = mapaDestino.alto / 2;
                        resultado.set(new Resultado(false, p.x, p.y, destino, entradaX, entradaY));
                        fin.countDown(); ventana.dispose(); return;
                    }
                }
                if (nx < 0 || ny < 0 || nx >= mapa.ancho || ny >= mapa.alto) {
                    estado.setText("Has llegado al límite del mapa. La salida está en el centro del lateral.");
                    return;
                }
                if ((nx == 0 || nx == mapa.ancho - 1) && Math.abs(ny - centroY) <= 1) {
                    Ubicacion destino = vecinoHorizontal(mapaId, dx);
                    if (destino != null) {
                        MapaDatos mapaDestino = cargarMapa(destino, jugador.getGeneracion());
                        int entradaX = dx < 0 ? Math.max(1, mapaDestino.ancho - 2) : 1;
                        resultado.set(new Resultado(false, p.x, p.y, destino, entradaX, mapaDestino.alto / 2));
                        fin.countDown(); ventana.dispose(); return;
                    }
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

                String evento = mapa.eventoEn(nx, ny);
                boolean hayEncuentro = permitirEncuentros && zona != null && mapa.esHierba(nx, ny)
                        && AZAR.nextInt(100) < 16;
                // La puerta y el encuentro se activan al terminar el paso: así la animación
                // de caminar sigue siendo visible antes de cambiar de pantalla.
                RedLAN red = RedLAN.actual();
                if (red != null && red.estaConectada())
                    red.publicarPosicion(identificadorRed(mapaId, jugador), nx, ny);
                lienzo.caminar(nx, ny, dx, dy, () -> {
                    if (evento != null) {
                        // Al volver al exterior se conserva la casilla anterior a la puerta.
                        resultado.set(new Resultado(false, p.x, p.y, null, -1, -1, evento));
                    } else if (hayEncuentro) {
                        resultado.set(new Resultado(true, nx, ny));
                        estado.setText("¡Algo se mueve entre la hierba!");
                    }
                    if (evento != null || hayEncuentro) {
                        fin.countDown();
                        ventana.dispose();
                    }
                });
                actualizarEstado(estado, jugador, lienzo, mapa, mapaId);
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

    /** No mezcla jugadores de generaciones distintas aunque compartan ciudad. */
    private static String identificadorRed(String mapaId, Jugador jugador) {
        if (mapaId.startsWith("REGION_")) return mapaId;
        return mapaId + ":GEN" + jugador.getGeneracion().getNumero();
    }

    private static Ubicacion vecinoHorizontal(String mapaId, int dx) {
        Ubicacion actual;
        try { actual = Ubicacion.valueOf(mapaId); } catch (IllegalArgumentException ex) { return null; }
        List<Ubicacion> recorrido = Mapa.getRecorrido();
        int indice = recorrido.indexOf(actual);
        int vecino = indice + (dx < 0 ? -1 : 1);
        return indice < 0 || vecino < 0 || vecino >= recorrido.size() ? null : recorrido.get(vecino);
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
        double[] posicion = lienzo.posicionInterpolada();
        JViewport vista = scroll.getViewport();
        int tam = lienzo.escalaCasilla();
        int px = (int) Math.round((posicion[0] + 0.5) * tam - vista.getWidth() / 2.0);
        int py = (int) Math.round((posicion[1] + 0.5) * tam - vista.getHeight() / 2.0);
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
        return cargarMapa(ubicacion, Generacion.GEN1);
    }

    private static MapaDatos cargarMapa(Ubicacion ubicacion, Generacion generacion) {
        for (Path candidato : rutasMapa(ubicacion)) {
            if (!Files.isRegularFile(candidato)) continue;
            try {
                return leerPmap(candidato);
            } catch (IOException | RuntimeException ex) {
                System.err.println("No se pudo cargar el mapa " + candidato + ": " + ex.getMessage());
            }
        }
        int indice = ubicacion == Ubicacion.VILLAVERDE ? 0
                : Mapa.puebloDe(ubicacion) != null ? 6
                : 1 + Math.floorMod(ubicacion.ordinal(), 9);
        CatalogoMapasGeneracionales.Mapa tema =
                CatalogoMapasGeneracionales.obtener(generacion, indice);
        return convertir(MapasRegion2D.generar(tema, ubicacion));
    }

    private static MapaDatos convertir(MapasRegion2D.Resultado generado) {
        List<EntradaEdificio> puertas = new ArrayList<>();
        for (MapasRegion2D.Puerta p : generado.puertas)
            puertas.add(new EntradaEdificio(p.x(), p.y(), p.servicio()));
        return new MapaDatos(generado.ancho, generado.alto, generado.tiles, generado.tipos,
                generado.capas, generado.colisiones, generado.manuales,
                generado.hierba, List.of(), List.of(), true, puertas, ArteRegion2D.PIXELES);
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
            // Hasta la versión 3 no existía una máscara separada para colisiones manuales.
            // No copiamos la máscara completa: incluía el agua y habría impedido nadar.
            boolean[] manuales = new boolean[size];
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

    /** Ciudad pixel-art provisional: manzanas accesibles y edificios con función reconocible. */
    private static MapaDatos crearMapaProvisional(Ubicacion ubicacion) {
        int ancho = 36, alto = 24, size = ancho * alto;
        boolean ciudad = ubicacion == Ubicacion.VILLAVERDE || Mapa.puebloDe(ubicacion) != null;
        List<BufferedImage> paleta = new ArrayList<>();
        List<Byte> categorias = new ArrayList<>();
        agregarTile(paleta, categorias, ArteVisualRetro.suelo(0, tile(new Color(78, 157, 70), new Color(95, 177, 79))), TIPO_NORMAL); // 0 pradera
        agregarTile(paleta, categorias, ArteVisualRetro.suelo(1, tile(new Color(190, 164, 118), new Color(212, 188, 141))), TIPO_NORMAL); // 1 sendero
        agregarTile(paleta, categorias, ArteVisualRetro.suelo(5, hierbaTile()), TIPO_HIERBA); // 2
        agregarTile(paleta, categorias, ArteVisualRetro.suelo(3, aguaTile()), TIPO_AGUA); // 3
        agregarTile(paleta, categorias, ArteVisualRetro.suelo(11, arbolTile()), TIPO_BLOQUEO); // 4
        agregarTile(paleta, categorias, ArteVisualRetro.suelo(13, paredTile()), TIPO_BLOQUEO); // 5
        agregarTile(paleta, categorias, edificioTile(), TIPO_BLOQUEO); // 6 legado
        int adoquin = agregarTile(paleta, categorias, ArteVisualRetro.suelo(7, adoquinTile()), TIPO_NORMAL);
        int flores = agregarTile(paleta, categorias, ArteVisualRetro.suelo(6, floresTile()), TIPO_NORMAL);
        int farola = agregarTile(paleta, categorias, farolaTile(), TIPO_BLOQUEO);
        int tierraOscura = agregarTile(paleta, categorias,
                ArteVisualRetro.suelo(8, tile(new Color(98, 151, 77), new Color(65, 122, 57))), TIPO_NORMAL);
        int tierraClara = agregarTile(paleta, categorias,
                ArteVisualRetro.suelo(15, tile(new Color(116, 175, 85), new Color(156, 195, 102))), TIPO_NORMAL);
        int sueloFlor = agregarTile(paleta, categorias, ArteVisualRetro.suelo(12, floresTile()), TIPO_NORMAL);
        int sueloPiedra = agregarTile(paleta, categorias, ArteVisualRetro.suelo(14, rocaSueloTile()), TIPO_NORMAL);
        int banco = agregarTile(paleta, categorias, bancoTile(), TIPO_BLOQUEO);
        int senal = agregarTile(paleta, categorias, cartelRutaTile(), TIPO_BLOQUEO);
        int[][] capas = new int[3][size];
        for (int[] capa : capas) Arrays.fill(capa, -1);
        Arrays.fill(capas[0], 0);
        // Césped con variación de tonos y vegetación de píxeles, sin ruido aleatorio
        // que provoque cambios de aspecto entre dos visitas a la misma zona.
        long semilla = ubicacion.ordinal() * 277L + 41L;
        for (int y = 1; y < alto - 1; y++) for (int x = 1; x < ancho - 1; x++) {
            long n = (x * 73856093L ^ y * 19349663L ^ semilla * 83492791L);
            int variante = (int) Math.floorMod(n, 13);
            if (variante == 0 || variante == 1) capas[0][y * ancho + x] = tierraOscura;
            else if (variante == 2 || variante == 3) capas[0][y * ancho + x] = tierraClara;
        }
        boolean[] colisiones = new boolean[size], manuales = new boolean[size], hierba = new boolean[size];
        List<EntradaEdificio> entradas = new ArrayList<>();

        int centroY = alto / 2;
        if (ciudad) {
            // Calles de tres casillas y una plaza central que comunica las puertas con la ruta.
            for (int y = centroY - 1; y <= centroY + 1; y++) {
                for (int x = 0; x < ancho; x++) capas[1][y * ancho + x] = adoquin;
            }
            for (int y = 1; y < alto - 1; y++) {
                for (int x = 17; x <= 19; x++) capas[1][y * ancho + x] = adoquin;
            }
            // Aceras longitudinales en ambos lados: todas las puertas quedan
            // conectadas con la plaza sin atravesar edificios ni jardines.
            for (int x = 1; x < ancho - 1; x++) {
                capas[1][8 * ancho + x] = adoquin;
                capas[1][21 * ancho + x] = adoquin;
            }
            for (int y = 9; y <= 15; y++) {
                for (int x = 14; x <= 22; x++) capas[1][y * ancho + x] = adoquin;
            }
            // Arquitectura deliberadamente distinta por categoría y con paleta original.
            String[] servicios = {
                    "CENTRO_POKEMON", "TIENDA", "GIMNASIO", "CASA",
                    "CASA", "CASA", "CASA", "CASA"
            };
            if (Gimnasio.deCiudad(ubicacion) == null) servicios[2] = "CASA";
            if (ubicacion == Ubicacion.COMILLAS) servicios[2] = "CASINO";
            if (ubicacion == Ubicacion.VITORIA) servicios[7] = "GOBIERNO";
            if (ubicacion == Ubicacion.VILLAVERDE) servicios[4] = "CASA_MAMA";
            // Cada familia tiene un volumen reconocible: el gimnasio es más ancho,
            // los centros y sedes son grandes, y las casas tienen fachadas más estrechas.
            int[] xs = {3, 11, 21, 29, 3, 11, 21, 29};
            int[] anchos = {6, 5, 7, 4, 4, 5, 5, 6};
            int[] altos = {4, 4, 5, 4, 4, 4, 4, 5};
            int[] ys = {3, 3, 3, 3, 16, 16, 16, 15};
            for (int i = 0; i < servicios.length; i++) {
                int bx = xs[i], by = ys[i], bw = anchos[i], bh = altos[i];
                String tipo = servicios[i];
                dibujarEdificio(paleta, categorias, capas[2], colisiones, manuales,
                        entradas, ancho, alto, bx, by, bw, bh, tipo, i + ubicacion.ordinal());
                // Caminos de acceso frontales. Las fachadas no pueden bloquear la calzada.
                int puertaX = bx + bw / 2;
                int desde = by + bh, hasta = by == 3 ? centroY - 1 : 20;
                int y1 = Math.min(desde, hasta), y2 = Math.max(desde, hasta);
                for (int y = y1; y <= y2; y++) {
                    int idx = y * ancho + puertaX;
                    if (capas[2][idx] < 0) capas[1][idx] = adoquin;
                }
            }
            // Plazas: bancos frente a la calle y parterres en los jardines.
            for (int[] pos : new int[][]{{15, 10}, {21, 14}}) {
                int idx = pos[1] * ancho + pos[0];
                if (capas[2][idx] < 0) {
                    capas[2][idx] = banco;
                    colisiones[idx] = true;
                    manuales[idx] = true;
                }
            }
            for (int x = 2; x < ancho - 2; x++) {
                int y = x % 2 == 0 ? 9 : 15;
                int idx = y * ancho + x;
                if (capas[1][idx] < 0 && capas[2][idx] < 0)
                    capas[1][idx] = (x % 6 == 0) ? sueloFlor : tierraClara;
            }
            // Jardines y mobiliario: separados de los accesos y de las vías transitables.
            for (int x = 7; x <= 9; x++) capas[1][10 * ancho + x] = flores;
            for (int x = 25; x <= 27; x++) capas[1][14 * ancho + x] = flores;
            for (int[] pos : new int[][]{{9, 9}, {26, 9}, {9, 14}, {26, 14}}) {
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, pos[0], pos[1], farola);
            }
            // Los laterales son pasos de zona: dejarlos abiertos para viajar.
            for (int x = 0; x < ancho; x++) {
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, 0, 4);
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, alto - 1, 4);
            }
            for (int y = 1; y < alto - 1; y++) {
                if (Math.abs(y - centroY) <= 1) continue;
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, 0, y, 4);
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, ancho - 1, y, 4);
            }
        } else {
            boolean costa = ubicacion == Ubicacion.RUTA4 || ubicacion == Ubicacion.RUTA8;
            boolean montaña = ubicacion == Ubicacion.RUTA6 || ubicacion == Ubicacion.RUTA10;
            boolean bosque = ubicacion == Ubicacion.RUTA3 || ubicacion == Ubicacion.RUTA7;
            // Biomas reconocibles para evitar que todas las rutas tengan la misma
            // pradera: costa, montaña, bosque y llanuras tienen detalles propios.
            for (int y = 1; y < alto - 1; y++) for (int x = 1; x < ancho - 1; x++) {
                int idx = y * ancho + x;
                int patron = Math.floorMod(x * 11 + y * 17 + ubicacion.ordinal() * 7, 31);
                if (costa && patron < 10) capas[0][idx] = 1;
                else if (montaña && patron < 9) capas[0][idx] = sueloPiedra;
                else if (bosque && patron < 12) capas[0][idx] = tierraOscura;
                else if (!costa && patron == 0) capas[0][idx] = sueloFlor;
            }
            // Sendero serpenteante que permanece conectado con las dos salidas.
            for (int x = 0; x < ancho; x++) {
                int cy = centroY + (x > 3 && x < ancho - 3
                        ? (x % 13 > 7 ? 1 : 0) : 0);
                capas[1][cy * ancho + x] = 1;
                if (x > 3 && x < ancho - 3 && x % 13 == 8)
                    capas[1][centroY * ancho + x] = 1;
            }
            for (int x = 0; x < ancho; x++) capas[1][centroY * ancho + x] = 1;
            for (int y = 0; y < alto; y++) capas[1][y * ancho + ancho / 2] = 1;
            for (int y = 4; y < 10; y++) for (int x = 4; x < 11; x++) {
                capas[1][y * ancho + x] = 2;
                hierba[y * ancho + x] = true;
            }
            for (int y = 14; y < 20; y++) for (int x = 24; x < 32; x++) {
                capas[1][y * ancho + x] = 2;
                hierba[y * ancho + x] = true;
            }
            for (int y = 3; y < 9; y++) for (int x = 23; x < 29; x++)
                if (capas[1][y * ancho + x] != 1) capas[1][y * ancho + x] = 3;
            for (int x = 0; x < ancho; x++) {
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, 0, 4);
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, alto - 1, 4);
            }
            for (int y = 1; y < alto - 1; y++) {
                if (Math.abs(y - centroY) <= 1) continue;
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, 0, y, 4);
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, ancho - 1, y, 4);
            }
            for (int y = 3; y < 7; y++) for (int x = 14; x < 19; x++)
                if (y == 3 || y == 6 || x == 14 || x == 18)
                    ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, y, 5);
            // Carteles de ruta junto al sendero, nunca encima del camino.
            if (capas[2][10 * ancho + 3] < 0)
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, 3, 10, senal);
            for (int i = 0; i < 26; i++) {
                int fx = 2 + Math.floorMod(i * 19 + ubicacion.ordinal() * 3, ancho - 4);
                int fy = 2 + Math.floorMod(i * 7 + ubicacion.ordinal() * 11, alto - 4);
                int pos = fy * ancho + fx;
                if (capas[1][pos] < 0 && capas[2][pos] < 0) {
                    if ((i % 5) == 0) capas[1][pos] = sueloPiedra;
                    else if ((i % 3) == 0) capas[1][pos] = sueloFlor;
                    else capas[1][pos] = tierraClara;
                }
            }
            Random r = new Random(ubicacion.ordinal() * 919L + 47);
            for (int n = 0; n < 24; n++) {
                int x = 2 + r.nextInt(ancho - 4), y = 2 + r.nextInt(alto - 4);
                if (capas[1][y * ancho + x] >= 0 || capas[2][y * ancho + x] >= 0) continue;
                ponerObstaculo(capas[2], colisiones, manuales, ancho, alto, x, y, 4);
            }
        }
        byte[] tipos = new byte[categorias.size()];
        for (int i = 0; i < tipos.length; i++) tipos[i] = categorias.get(i);
        return new MapaDatos(ancho, alto, paleta.toArray(new BufferedImage[0]), tipos,
                capas, colisiones, manuales, hierba, List.of(), List.of(), true, entradas);
    }

    private static int agregarTile(List<BufferedImage> tiles, List<Byte> tipos,
                                   BufferedImage imagen, int tipo) {
        tiles.add(imagen);
        tipos.add((byte) tipo);
        return tiles.size() - 1;
    }

    private static void dibujarEdificio(List<BufferedImage> paleta, List<Byte> tipos,
                                         int[] capa, boolean[] colisiones, boolean[] manuales,
                                         List<EntradaEdificio> entradas, int ancho, int alto,
                                         int x, int y, int w, int h, String clase, int variante) {
        BufferedImage fachada = ArteEdificios.crear(clase, w, h, variante);
        int centro = x + w / 2;
        for (int oy = 0; oy < h; oy++) for (int ox = 0; ox < w; ox++) {
            int px = x + ox, py = y + oy;
            if (px < 1 || px >= ancho - 1 || py < 1 || py >= alto - 1) continue;
            BufferedImage trozo = fachada.getSubimage(ox * TILE, oy * TILE, TILE, TILE);
            boolean puerta = oy == h - 1 && px == centro;
            int tile = agregarTile(paleta, tipos, trozo, puerta ? TIPO_NORMAL : TIPO_BLOQUEO);
            int idx = py * ancho + px;
            capa[idx] = tile;
            colisiones[idx] = !puerta;
            manuales[idx] = !puerta;
        }
        entradas.add(new EntradaEdificio(centro, y + h - 1, clase));
    }

    private static BufferedImage rocaSueloTile() {
        BufferedImage im = tile(new Color(129, 140, 123), new Color(173, 178, 144));
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(83, 99, 90));
        g.fillRect(3, 6, 7, 5);
        g.fillRect(10, 9, 4, 4);
        g.setColor(new Color(190, 193, 166));
        g.fillRect(3, 6, 5, 2);
        g.fillRect(10, 9, 3, 1);
        g.dispose();
        return im;
    }

    private static BufferedImage bancoTile() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(82, 55, 39));
        g.fillRect(1, 6, 14, 3);
        g.fillRect(2, 11, 12, 2);
        g.setColor(new Color(189, 126, 67));
        g.fillRect(2, 4, 12, 3);
        g.fillRect(2, 9, 12, 2);
        g.setColor(new Color(70, 82, 86));
        g.fillRect(3, 12, 2, 4);
        g.fillRect(11, 12, 2, 4);
        g.dispose();
        return im;
    }

    private static BufferedImage cartelRutaTile() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(65, 79, 77));
        g.fillRect(7, 4, 2, 12);
        g.setColor(new Color(222, 186, 102));
        g.fillRect(1, 1, 14, 8);
        g.setColor(new Color(110, 88, 61));
        g.drawRect(1, 1, 13, 7);
        g.setColor(new Color(78, 107, 75));
        g.fillRect(4, 3, 8, 2);
        g.dispose();
        return im;
    }

    private static BufferedImage adoquinTile() {
        BufferedImage im = tile(new Color(191, 194, 175), new Color(210, 213, 192));
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(143, 154, 138));
        g.drawLine(0, 7, 15, 7);
        g.drawLine(8, 0, 8, 7);
        g.drawLine(3, 8, 3, 15);
        g.dispose();
        return im;
    }

    private static BufferedImage floresTile() {
        BufferedImage im = tile(new Color(88, 163, 80), new Color(119, 183, 101));
        Graphics2D g = im.createGraphics();
        for (int[] p : new int[][]{{3, 4}, {10, 10}, {12, 4}, {5, 12}}) {
            g.setColor(new Color(36, 99, 48)); g.fillRect(p[0], p[1] + 2, 1, 4);
            g.setColor(new Color(245, 214, 99)); g.fillRect(p[0] - 1, p[1], 3, 3);
            g.setColor(new Color(233, 99, 124)); g.fillRect(p[0], p[1] + 1, 1, 1);
        }
        g.dispose();
        return im;
    }

    private static BufferedImage farolaTile() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(49, 64, 79)); g.fillRect(7, 5, 2, 11);
        g.fillRect(3, 4, 10, 2);
        g.setColor(new Color(251, 227, 124)); g.fillRect(4, 1, 8, 4);
        g.setColor(new Color(245, 247, 218)); g.fillRect(6, 2, 4, 2);
        g.dispose();
        return im;
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

    private static BufferedImage edificioTile() {
        BufferedImage im = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setColor(new Color(149, 63, 54));
        g.fillPolygon(new int[]{0, 8, 16}, new int[]{7, 0, 7}, 3);
        g.setColor(new Color(220, 205, 164)); g.fillRect(2, 7, 12, 9);
        g.setColor(new Color(63, 52, 48)); g.fillRect(6, 10, 4, 6);
        g.setColor(new Color(111, 177, 210)); g.fillRect(3, 9, 2, 3); g.fillRect(11, 9, 2, 3);
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

    static boolean puedeEntrarCasilla(boolean esAgua, boolean bloqueada, boolean puedeNadarOVolar) {
        return !bloqueada && permiteAgua(esAgua, puedeNadarOVolar);
    }

    /** Puerta transitable asociada a un servicio; no se serializa ni modifica partidas. */
    private static final class EntradaEdificio {
        final int x, y;
        final String tipo;
        EntradaEdificio(int x, int y, String tipo) {
            this.x = x; this.y = y; this.tipo = tipo;
        }
    }

    private static final class MapaDatos {
        final int ancho, alto;
        final BufferedImage[] tiles;
        /** Píxeles originales por casilla, 16 clásicos o 32 regionales. */
        final int resolucion;
        final byte[] tipos;
        final int[][] capas;
        final boolean[] colisiones, manuales, hierba;
        final List<ObjetoMapa> objetos;
        final List<Colocacion> colocaciones;
        final boolean provisional;
        final boolean[] colisionObjetos;
        final List<EntradaEdificio> entradas;

        MapaDatos(int ancho, int alto, BufferedImage[] tiles, byte[] tipos, int[][] capas,
                  boolean[] colisiones, boolean[] manuales, boolean[] hierba,
                  List<ObjetoMapa> objetos, List<Colocacion> colocaciones, boolean provisional) {
            this(ancho, alto, tiles, tipos, capas, colisiones, manuales, hierba,
                    objetos, colocaciones, provisional, List.of());
        }

        MapaDatos(int ancho, int alto, BufferedImage[] tiles, byte[] tipos, int[][] capas,
                  boolean[] colisiones, boolean[] manuales, boolean[] hierba,
                  List<ObjetoMapa> objetos, List<Colocacion> colocaciones,
                  boolean provisional, List<EntradaEdificio> puertas) {
            this(ancho, alto, tiles, tipos, capas, colisiones, manuales,
                    hierba, objetos, colocaciones, provisional, puertas, TILE);
        }

        MapaDatos(int ancho, int alto, BufferedImage[] tiles, byte[] tipos, int[][] capas,
                  boolean[] colisiones, boolean[] manuales, boolean[] hierba,
                  List<ObjetoMapa> objetos, List<Colocacion> colocaciones,
                  boolean provisional, List<EntradaEdificio> puertas, int resolucion) {
            this.resolucion = resolucion;
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
            this.entradas = new ArrayList<>(puertas);
            // En mapas diseñados en Studio, se detectan entradas a partir de los
            // objetos etiquetados. Se respeta el diseño original del editor.
            if (!provisional) {
                for (Colocacion p : colocaciones) {
                    if (p.objeto < 0 || p.objeto >= objetos.size()) continue;
                    ObjetoMapa o = objetos.get(p.objeto);
                    String categoria = o.categoria.toUpperCase(Locale.ROOT);
                    if (categoria.equals("CENTRO_POKEMON") || categoria.equals("CENTRO_POKÉMON")
                            || categoria.equals("GIMNASIO") || categoria.equals("TIENDA")
                            || categoria.equals("CASA") || categoria.equals("CASINO")
                            || categoria.equals("GOBIERNO")) {
                        int px = p.x + o.ancho / 2, py = p.y + o.alto;
                        if (dentro(px, py)) this.entradas.add(new EntradaEdificio(px, py, categoria));
                    }
                }
            }
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

        String eventoEn(int x, int y) {
            for (EntradaEdificio entrada : entradas)
                if (entrada.x == x && entrada.y == y) return entrada.tipo;
            return null;
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
            boolean bloqueada = manuales[i] || colisionObjetos[i];
            for (int[] capa : capas) {
                int tile = capa[i];
                if (esTipo(tile, TIPO_BLOQUEO)) bloqueada = true;
            }
            // La máscara de colisión del editor incluye agua y objetos. Para nadar
            // solo se ignora la marca genérica de agua, nunca árboles o paredes.
            if (!agua && colisiones[i]) bloqueada = true;
            return puedeEntrarCasilla(agua, bloqueada, puedeNavegarAgua);
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

    /** Mantiene las coordenadas lógicas en tiles y las visuales entre casillas. */
    private static final class MapaCanvas extends JPanel {
        private final MapaDatos mapa;
        private final SpriteJugador2D sprite;
        private int jugadorX, jugadorY;
        private double pantallaX, pantallaY;
        private int direccion = SpriteJugador2D.FRENTE;
        private int pasoSecuencia;
        private int fotogramaPaso;
        private boolean moviendo;
        private Timer pasoTimer;
        private Timer ambienteTimer;
        private final Timer redTimer;
        private final String mapaId;
        private int faseAgua;
        private int zoomRegional = 0; // Compacto: mapa completo visible en pantallas habituales.

        MapaCanvas(MapaDatos mapa, SpriteJugador2D sprite, int x, int y, String mapaId) {
            this.mapaId = mapaId;
            redTimer = new Timer(200, e -> {
                RedLAN red = RedLAN.actual();
                if (red != null && red.estaConectada()) repaint();
            });
            redTimer.setCoalesce(true);
            redTimer.start();
            this.mapa = mapa;
            this.sprite = sprite;
            jugadorX = x;
            jugadorY = y;
            pantallaX = x;
            pantallaY = y;
            setOpaque(true);
            setBackground(new Color(30, 35, 35));
            setPreferredSize(new Dimension(mapa.ancho * escalaCasilla(),
                    mapa.alto * escalaCasilla()));
            setFocusable(true);
            boolean agua = false;
            for (int iy = 0; iy < mapa.alto && !agua; iy++)
                for (int ix = 0; ix < mapa.ancho; ix++)
                    if (mapa.esAgua(ix, iy)) { agua = true; break; }
            if (agua) {
                ambienteTimer = new Timer(255, e -> {
                    faseAgua = (faseAgua + 1) % 4;
                    repaint();
                });
                ambienteTimer.start();
            }
        }

        /** Los mapas regionales usan x4 por defecto; se permite ajustar a x2..x5. */
        int escalaCasilla() {
            return pixelesPorCasilla(mapa.resolucion, zoomRegional);
        }

        void ajustarZoomRegional(int delta) {
            if (mapa.resolucion != 32) return;
            int siguiente = Math.max(0, Math.min(ZOOM_REGION_PX.length - 1, zoomRegional + delta));
            if (zoomRegional == siguiente) return;
            zoomRegional = siguiente;
            setPreferredSize(new Dimension(mapa.ancho*escalaCasilla(),
                    mapa.alto*escalaCasilla()));
            revalidate();
            repaint();
        }

        void vistaCompacta() {
            ajustarZoomRegional(-zoomRegional);
        }

        Posicion getPosicion() { return new Posicion(jugadorX, jugadorY); }
        double[] posicionInterpolada() { return new double[]{pantallaX, pantallaY}; }
        boolean estaMoviendo() { return moviendo; }

        void mirar(int dx, int dy) {
            if (dy > 0) direccion = SpriteJugador2D.FRENTE;
            else if (dy < 0) direccion = SpriteJugador2D.ESPALDA;
            else if (dx < 0) direccion = SpriteJugador2D.IZQUIERDA;
            else if (dx > 0) direccion = SpriteJugador2D.DERECHA;
            repaint();
        }

        void caminar(int x, int y, int dx, int dy, Runnable fin) {
            if (moviendo) return;
            mirar(dx, dy);
            final double anteriorX = pantallaX, anteriorY = pantallaY;
            final long inicio = System.nanoTime();
            final long duracion = 138_000_000L; // un paso ~= 138 ms; ~7 tiles/seg
            jugadorX = x;
            jugadorY = y;
            moviendo = true;
            fotogramaPaso = 1 + (pasoSecuencia++ % 2);
            pasoTimer = new Timer(16, e -> {
                double avance = Math.min(1.0, (System.nanoTime() - inicio) / (double) duracion);
                pantallaX = anteriorX + (x - anteriorX) * avance;
                pantallaY = anteriorY + (y - anteriorY) * avance;
                repaint();
                JScrollPane sc = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
                centrarVista(sc, this);
                if (avance >= 1.0) {
                    detenerAnimacion();
                    if (fin != null) fin.run();
                }
            });
            pasoTimer.setCoalesce(true);
            pasoTimer.start();
        }

        void detenerTodo() {
            detenerAnimacion();
            if (ambienteTimer != null) ambienteTimer.stop();
            ambienteTimer = null;
            redTimer.stop();
        }

        void detenerAnimacion() {
            if (pasoTimer != null) pasoTimer.stop();
            pasoTimer = null;
            moviendo = false;
            pantallaX = jugadorX;
            pantallaY = jugadorY;
            repaint();
        }

        @Override protected void paintComponent(Graphics gOriginal) {
            super.paintComponent(gOriginal);
            Graphics2D g = (Graphics2D) gOriginal.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            int tam = escalaCasilla();
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
            // Brillos de agua en cuatro fases, a la misma cuadrícula que los tiles.
            if (ambienteTimer != null) {
                g.setColor(new Color(230, 249, 255, 105));
                for (int iy = 0; iy < mapa.alto; iy++) for (int ix = 0; ix < mapa.ancho; ix++) {
                    if (!mapa.esAgua(ix, iy)) continue;
                    int escala = Math.max(1, tam / mapa.resolucion);
                    int desplazamiento = Math.floorMod(ix * 3 + iy + faseAgua * 4,
                            mapa.resolucion - 7);
                    int px = ix * tam + desplazamiento * escala;
                    int py = iy * tam + (3 + Math.floorMod(iy + faseAgua, 7)) * escala;
                    g.fillRect(px, py, 6 * escala, escala);
                    g.fillRect(px + 2 * escala, py + 4 * escala, 4 * escala, escala);
                }
            }
            int x = (int) Math.round(pantallaX * tam);
            int y = (int) Math.round(pantallaY * tam);
            // Sombra corta y discreta bajo los pies, hecha con píxeles escalados.
            g.setColor(new Color(30, 55, 49, 80));
            g.fillRect(x + tam * 3 / 16, y + tam * 14 / 16,
                    tam * 10 / 16, Math.max(2, tam / 16));
            // Los compañeros conectados se muestran en la misma casilla virtual.
            // Sus coordenadas y mapas son mensajes limitados del protocolo TCP.
            RedLAN red = RedLAN.actual();
            if (red != null && red.estaConectada()) {
                for (RedLAN.JugadorRemoto remoto : red.jugadoresEn(mapaId)) {
                    if (!mapa.dentro(remoto.x, remoto.y)) continue;
                    int rx = remoto.x * tam, ry = remoto.y * tam;
                    if (mapa.resolucion == ArteRegion2D.PIXELES) {
                        g.drawImage(EntrenadorRegion2D.fotograma(SpriteJugador2D.FRENTE, 0),
                                rx, ry - tam / 2, tam, tam * 3 / 2, null);
                    } else {
                        g.drawImage(sprite.fotograma(SpriteJugador2D.FRENTE, 0),
                                rx, ry, tam, tam, null);
                    }
                    g.setFont(new Font("SansSerif", Font.BOLD, Math.max(11, tam / 5)));
                    FontMetrics m = g.getFontMetrics();
                    String nombre = remoto.nombre.length() > 24
                            ? remoto.nombre.substring(0, 24) : remoto.nombre;
                    int anchoNombre = m.stringWidth(nombre) + 9;
                    int nombreY = Math.max(14, ry - tam / 2 - 8);
                    g.setColor(new Color(20, 38, 62, 210));
                    g.fillRoundRect(rx + tam / 2 - anchoNombre / 2, nombreY - 14,
                            anchoNombre, 17, 5, 5);
                    g.setColor(Color.WHITE);
                    g.drawString(nombre, rx + tam / 2 - m.stringWidth(nombre) / 2, nombreY - 2);
                }
            }
            if (mapa.resolucion == ArteRegion2D.PIXELES) {
                // Sprite original de 32x48, con los pies alineados con la casilla.
                BufferedImage fotograma = EntrenadorRegion2D.fotograma(direccion,
                        moviendo ? fotogramaPaso : 0);
                g.drawImage(fotograma, x, y - tam / 2, tam, tam * 3 / 2, null);
            } else {
                BufferedImage fotograma = sprite.fotograma(direccion, moviendo ? fotogramaPaso : 0);
                g.drawImage(fotograma, x, y, tam, tam, null);
            }
            g.dispose();
        }

        private static final class Posicion {
            final int x, y;
            Posicion(int x, int y) { this.x = x; this.y = y; }
        }
    }
}
