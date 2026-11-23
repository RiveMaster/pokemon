package Proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PRIMER MAPA DE PRUEBA: usa exclusivamente los píxeles del atlas facilitado
 * por el usuario. No usa ArteRegion2D, RecursosCC0, VisualRetro ni imágenes
 * generadas programáticamente. El cursor del jugador es solo una indicación UI
 * hasta que el usuario suministre sprites del protagonista y NPC.
 */
public final class MapaAtlasUsuario {
    public static final int TILE = 16;
    public static final int ESCALA = 3;
    public static final int ANCHO = 32;
    public static final int ALTO = 22;

    public record Objeto(int x, int y, String sprite, String evento) { }

    public static final class Mundo {
        private final String[][] terreno = new String[ALTO][ANCHO];
        private final boolean[][] bloqueado = new boolean[ALTO][ANCHO];
        private final List<Objeto> objetos = new ArrayList<>();
        private final AtlasUsuario16 atlas;

        private Mundo(AtlasUsuario16 atlas) {
            this.atlas = atlas;
        }

        public boolean puedePisar(int x, int y) {
            return x >= 0 && y >= 0 && x < ANCHO && y < ALTO
                    && !bloqueado[y][x];
        }

        public String terreno(int x, int y) { return terreno[y][x]; }
        public List<Objeto> objetos() { return List.copyOf(objetos); }
        public boolean bloqueado(int x, int y) { return bloqueado[y][x]; }

        private void objeto(int x, int y, String sprite, String evento) {
            BufferedImage imagen = atlas.sprite(sprite);
            int casillasAncho = (imagen.getWidth() + TILE - 1) / TILE;
            int casillasAlto = (imagen.getHeight() + TILE - 1) / TILE;
            if (x < 0 || y < 0 || x + casillasAncho >= ANCHO
                    || y + casillasAlto >= ALTO)
                throw new IllegalArgumentException("Objeto fuera del mapa: " + sprite);
            for (int dy = 0; dy < casillasAlto; dy++)
                for (int dx = 0; dx < casillasAncho; dx++)
                    bloqueado[y + dy][x + dx] = true;
            objetos.add(new Objeto(x, y, sprite, evento));
            // La entrada queda fuera de la fachada, para no atravesar paredes.
            int puertaX = x + casillasAncho / 2, puertaY = y + casillasAlto;
            if (evento != null) {
                bloqueado[puertaY][puertaX] = false;
                for (int sy = puertaY; sy <= ALTO/2 + 1 && sy < ALTO; sy++) {
                    if (!bloqueado[sy][puertaX]) terreno[sy][puertaX] = "SUELO_TIERRA";
                }
            }
        }

        public String eventoFrente(int x, int y) {
            for (Objeto o : objetos) {
                if (o.evento() == null) continue;
                BufferedImage imagen = atlas.sprite(o.sprite());
                int ancho = (imagen.getWidth() + TILE - 1) / TILE;
                int alto = (imagen.getHeight() + TILE - 1) / TILE;
                if (x == o.x() + ancho / 2 && y == o.y() + alto)
                    return o.evento();
            }
            return null;
        }
    }

    private MapaAtlasUsuario() { }

    public static Mundo generar(AtlasUsuario16 atlas) {
        Mundo m = new Mundo(atlas);
        // Solamente cinco superficies recortadas del atlas, sin tiles inventados.
        for (int y = 0; y < ALTO; y++) for (int x = 0; x < ANCHO; x++) {
            int patron = Math.floorMod(x*37 + y*17, 23);
            m.terreno[y][x] = patron < 3 ? "SUELO_CLARO" : "SUELO_VERDE";
        }
        // Calles de 2-3 casillas con cruce central y acceso a las fachadas.
        for (int x = 0; x < ANCHO; x++)
            for (int y = 11; y <= 13; y++) m.terreno[y][x] = "SUELO_TIERRA";
        for (int y = 1; y < ALTO-1; y++)
            for (int x = 15; x <= 17; x++) m.terreno[y][x] = "SUELO_PIEDRA";

        m.objeto(3, 4, "CASA_ROJA", "CASA");
        m.objeto(8, 4, "CASA_VERDE", "CASA");
        m.objeto(21, 4, "TIENDA_AZUL", "TIENDA");
        m.objeto(27, 4, "EDIFICIO_AZUL", "CENTRO_POKEMON");
        m.objeto(5, 16, "CASA_MARRON", "CASA");
        m.objeto(24, 16, "CASA_GRANDE", "CASA");

        // Bosque de borde (objetos con su altura original, 16-27 px).
        for (int x = 0; x < ANCHO; x += 2) {
            m.objeto(x, 0, x % 4 == 0 ? "PINO" : "ARBOL_REDONDO", null);
            if (x != 14 && x != 16)
                m.objeto(x, ALTO-3, x % 4 == 0 ? "PINO_2" : "ARBOL_REDONDO_2", null);
        }
        for (int y = 3; y < ALTO-3; y += 3) {
            m.objeto(0, y, "ARBUSTO", null);
            m.objeto(ANCHO-2, y, "ARBUSTO_2", null);
        }
        return m;
    }

    /** Vista previa independiente: no modifica posiciones ni guardados del juego. */
    public static void abrir() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("El mapa de prueba necesita una pantalla.");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            final AtlasUsuario16 atlas;
            try { atlas = AtlasUsuario16.cargar(); }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Atlas del usuario", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Mundo m = generar(atlas);
            JFrame ventana = new JFrame("Mapa de prueba · Solo tileset del usuario");
            ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            ventana.setLayout(new BorderLayout());
            JLabel mensaje = new JLabel(" WASD o flechas: caminar · Esc: salir · Entradas: interiores en fase 2");
            mensaje.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
            ventana.add(mensaje, BorderLayout.NORTH);
            JPanel lienzo = new JPanel() {
                int px=16, py=12;
                {
                    setPreferredSize(new Dimension(ANCHO*TILE*ESCALA, ALTO*TILE*ESCALA));
                    setFocusable(true);
                    javax.swing.InputMap entradas = getInputMap(WHEN_IN_FOCUSED_WINDOW);
                    for (int[] k : new int[][]{
                            {KeyEvent.VK_W,0,-1},{KeyEvent.VK_UP,0,-1},
                            {KeyEvent.VK_S,0,1},{KeyEvent.VK_DOWN,0,1},
                            {KeyEvent.VK_A,-1,0},{KeyEvent.VK_LEFT,-1,0},
                            {KeyEvent.VK_D,1,0},{KeyEvent.VK_RIGHT,1,0}
                    }) {
                        String nombre = "caminar_" + k[0];
                        entradas.put(KeyStroke.getKeyStroke(k[0],0),nombre);
                        getActionMap().put(nombre, new AbstractAction() {
                            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                                int nx=px+k[1],ny=py+k[2];
                                if (!m.puedePisar(nx,ny)) return;
                                px=nx; py=ny;
                                repaint();
                                String evento=m.eventoFrente(nx,ny);
                                if(evento!=null)
                                    mensaje.setText(" " + evento
                                            + " · Entrada detectada; interior en fase 2.");
                            }
                        });
                    }
                    entradas.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"salir");
                    getActionMap().put("salir",new AbstractAction() {
                        @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                            ventana.dispose();
                        }
                    });
                }
                @Override protected void paintComponent(Graphics original) {
                    super.paintComponent(original);
                    Graphics2D g=(Graphics2D) original.create();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    for (int y=0;y<ALTO;y++) for (int x=0;x<ANCHO;x++)
                        g.drawImage(atlas.sprite(m.terreno(x,y)),x*TILE*ESCALA,
                                y*TILE*ESCALA,TILE*ESCALA,TILE*ESCALA,null);
                    for (Objeto o:m.objetos()) {
                        BufferedImage sprite=atlas.sprite(o.sprite());
                        int x=o.x()*TILE*ESCALA,y=o.y()*TILE*ESCALA;
                        g.drawImage(sprite,x,y,
                                sprite.getWidth()*ESCALA,sprite.getHeight()*ESCALA,null);
                    }
                    // Marcador de posición UI, no sprite inventado.
                    g.setColor(new Color(245,235,79));
                    g.drawRect(px*TILE*ESCALA+ESCALA,py*TILE*ESCALA+ESCALA,
                            TILE*ESCALA-2*ESCALA,TILE*ESCALA-2*ESCALA);
                    g.dispose();
                }
            };
            ventana.add(new JScrollPane(lienzo),BorderLayout.CENTER);
            ventana.setSize(1024,720);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            lienzo.requestFocusInWindow();
        });
    }
}
