package Proyecto;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Animación de entrenador estilo RPG 2D, 16x16 píxeles por fotograma.
 *
 * Permite sustituir el diseño original por Tiles/Jugador.png o Tiles/Player.png
 * con 3 ó 4 columnas (reposo, paso 1, paso 2) y 4 filas
 * (frente, izquierda, derecha, espalda). Los PNG de 16x16 antiguos
 * siguen cargándose sin modificar su diseño.
 */
public final class SpriteJugador2D {
    public static final int FRENTE = 0;
    public static final int IZQUIERDA = 1;
    public static final int DERECHA = 2;
    public static final int ESPALDA = 3;

    private static final int PIXELES = 16;
    private static final Color BORDE = new Color(33, 42, 65);
    private static final Color GORRA = new Color(205, 52, 65);
    private static final Color GORRA_LUZ = new Color(246, 105, 103);
    private static final Color PIEL = new Color(248, 194, 145);
    private static final Color CABELLO = new Color(91, 62, 52);
    private static final Color ROPA = new Color(51, 116, 183);
    private static final Color ROPA_LUZ = new Color(95, 171, 226);
    private static final Color PANTALON = new Color(62, 67, 111);
    private static final Color ZAPATO = new Color(36, 49, 67);
    private static final Color MOCHILA = new Color(206, 162, 74);

    private final BufferedImage[][] fotogramas = new BufferedImage[4][3];
    private final boolean personalizado;

    private SpriteJugador2D(boolean personalizado) {
        this.personalizado = personalizado;
    }

    public boolean esPersonalizado() { return personalizado; }

    /** El llamador recibe una imagen lista para dibujar, nunca una vista mutable de un atlas. */
    public BufferedImage fotograma(int direccion, int paso) {
        return fotogramas[Math.max(0, Math.min(3, direccion))][Math.floorMod(paso, 3)];
    }

    /** Importa sprites de usuario si existen; si no, crea un protagonista original animado. */
    public static SpriteJugador2D cargar() {
        for (String archivo : List.of("Jugador.png", "Player.png", "jugador.png",
                "JugadorWalk.png", "player-walk.png", "Entrenador.png")) {
            Path ruta = Path.of("Tiles", archivo);
            if (!Files.isRegularFile(ruta)) continue;
            try {
                BufferedImage hoja = ImageIO.read(ruta.toFile());
                if (hoja != null && hoja.getWidth() >= PIXELES && hoja.getHeight() >= PIXELES)
                    return desdeHoja(hoja);
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Sprite del entrenador ilegible: " + ruta + ": " + e.getMessage());
            }
        }
        return provisional();
    }

    /** Solo admite hojas en cuadrícula exacta para no cortar imágenes arbitrariamente. */
    public static SpriteJugador2D desdeHoja(BufferedImage hoja) {
        if (hoja == null || hoja.getWidth() % PIXELES != 0 || hoja.getHeight() % PIXELES != 0)
            throw new IllegalArgumentException("El sprite debe tener casillas de 16x16 píxeles.");
        int columnas = hoja.getWidth() / PIXELES;
        int filas = hoja.getHeight() / PIXELES;
        SpriteJugador2D atlas = new SpriteJugador2D(true);
        for (int dir = 0; dir < 4; dir++) {
            int fila = filas >= 4 ? dir : 0;
            for (int paso = 0; paso < 3; paso++) {
                int columna = columnas >= 3 ? paso : 0;
                BufferedImage copia = new BufferedImage(PIXELES, PIXELES, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = copia.createGraphics();
                g.drawImage(hoja, 0, 0, PIXELES, PIXELES,
                        columna * PIXELES, fila * PIXELES,
                        (columna + 1) * PIXELES, (fila + 1) * PIXELES, null);
                g.dispose();
                atlas.fotogramas[dir][paso] = copia;
            }
        }
        return atlas;
    }

    public static SpriteJugador2D provisional() {
        SpriteJugador2D atlas = new SpriteJugador2D(false);
        for (int dir = 0; dir < 4; dir++)
            for (int paso = 0; paso < 3; paso++)
                atlas.fotogramas[dir][paso] = dibujar(dir, paso);
        return atlas;
    }

    private static BufferedImage dibujar(int dir, int paso) {
        BufferedImage im = new BufferedImage(PIXELES, PIXELES, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int rebote = paso == 0 ? 0 : (paso == 1 ? -1 : 0);

        // Calzado y piernas: zancada alterna, visible también desde atrás.
        int pieIzq = paso == 1 ? 3 : 5;
        int pieDer = paso == 2 ? 10 : 9;
        rect(g, PANTALON, 5, 11 + rebote, 3, 3);
        rect(g, PANTALON, 9, 11 + rebote, 3, 3);
        rect(g, ZAPATO, pieIzq, 14 + rebote, 4, 2);
        rect(g, ZAPATO, pieDer, 14 + rebote, 4, 2);

        if (dir == IZQUIERDA || dir == DERECHA) {
            // Perfil: visera y pie adelantado apuntan hacia donde camina.
            if (dir == IZQUIERDA) {
                rect(g, BORDE, 3, 3 + rebote, 10, 7);
                rect(g, PIEL, 5, 5 + rebote, 6, 5);
                rect(g, CABELLO, 9, 4 + rebote, 3, 4);
                rect(g, GORRA, 4, 2 + rebote, 8, 3);
                rect(g, GORRA_LUZ, 3, 3 + rebote, 5, 1);
                rect(g, GORRA, 1, 5 + rebote, 5, 2);
                rect(g, BORDE, 4, 7 + rebote, 1, 1);
                rect(g, ROPA, 5, 10 + rebote, 7, 3);
                rect(g, ROPA_LUZ, 5, 10 + rebote, 3, 2);
                rect(g, PIEL, paso == 1 ? 3 : 4, 10 + rebote, 2, 3);
                rect(g, MOCHILA, 11, 10 + rebote, 3, 3);
            } else {
                rect(g, BORDE, 3, 3 + rebote, 10, 7);
                rect(g, PIEL, 5, 5 + rebote, 6, 5);
                rect(g, CABELLO, 4, 4 + rebote, 3, 4);
                rect(g, GORRA, 4, 2 + rebote, 8, 3);
                rect(g, GORRA_LUZ, 8, 3 + rebote, 5, 1);
                rect(g, GORRA, 11, 5 + rebote, 4, 2);
                rect(g, BORDE, 11, 7 + rebote, 1, 1);
                rect(g, ROPA, 4, 10 + rebote, 7, 3);
                rect(g, ROPA_LUZ, 8, 10 + rebote, 3, 2);
                rect(g, PIEL, paso == 2 ? 12 : 11, 10 + rebote, 2, 3);
                rect(g, MOCHILA, 2, 10 + rebote, 3, 3);
            }
        } else if (dir == ESPALDA) {
            rect(g, BORDE, 4, 3 + rebote, 8, 8);
            rect(g, CABELLO, 5, 5 + rebote, 6, 4);
            rect(g, GORRA, 4, 2 + rebote, 8, 4);
            rect(g, GORRA_LUZ, 5, 3 + rebote, 5, 1);
            rect(g, PIEL, 5, 9 + rebote, 6, 2);
            rect(g, ROPA, 4, 10 + rebote, 8, 4);
            rect(g, MOCHILA, 6, 10 + rebote, 5, 4);
            rect(g, new Color(250, 207, 122), 7, 11 + rebote, 2, 2);
            rect(g, PIEL, paso == 1 ? 2 : 3, 10 + rebote, 2, 3);
            rect(g, PIEL, paso == 2 ? 12 : 11, 10 + rebote, 2, 3);
        } else {
            rect(g, BORDE, 4, 3 + rebote, 8, 8);
            rect(g, PIEL, 5, 5 + rebote, 6, 5);
            rect(g, CABELLO, 4, 5 + rebote, 2, 3);
            rect(g, CABELLO, 10, 5 + rebote, 2, 3);
            rect(g, GORRA, 4, 2 + rebote, 8, 4);
            rect(g, GORRA_LUZ, 5, 3 + rebote, 6, 1);
            rect(g, GORRA, 3, 5 + rebote, 10, 2);
            rect(g, BORDE, 6, 7 + rebote, 1, 1);
            rect(g, BORDE, 9, 7 + rebote, 1, 1);
            rect(g, ROPA, 4, 10 + rebote, 8, 4);
            rect(g, ROPA_LUZ, 5, 10 + rebote, 6, 2);
            rect(g, PIEL, paso == 1 ? 2 : 3, 10 + rebote, 2, 3);
            rect(g, PIEL, paso == 2 ? 12 : 11, 10 + rebote, 2, 3);
            rect(g, MOCHILA, 11, 11 + rebote, 2, 2);
        }
        g.dispose();
        return im;
    }

    private static void rect(Graphics2D g, Color color, int x, int y, int w, int h) {
        g.setColor(color);
        g.fillRect(x, y, w, h);
    }
}
