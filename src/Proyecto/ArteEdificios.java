package Proyecto;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Fachadas originales en pixel art de 16 px por casilla.
 * Se crean a resolución nativa y el mapa las amplía sin interpolación.
 * Cada servicio tiene una silueta, colores y señalización propios.
 */
public final class ArteEdificios {
    private static final Color CONTORNO = new Color(37, 47, 67);
    private static final Color SOMBRA = new Color(52, 81, 64, 105);
    private static final Color CRISTAL = new Color(108, 203, 228);
    private static final Color BRILLO = new Color(221, 248, 249);

    private ArteEdificios() { }

    public static BufferedImage crear(String tipo, int anchoCasillas, int altoCasillas, int variante) {
        int w = anchoCasillas * 16, h = altoCasillas * 16;
        if (w < 48 || h < 48) throw new IllegalArgumentException("Edificio demasiado pequeño");
        BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        String clase = tipo == null ? "CASA" : tipo.toUpperCase(java.util.Locale.ROOT);
        switch (clase) {
            case "CENTRO_POKEMON" -> centro(g, w, h);
            case "TIENDA" -> tienda(g, w, h);
            case "GIMNASIO" -> gimnasio(g, w, h);
            case "CASINO" -> casino(g, w, h);
            case "GOBIERNO" -> gobierno(g, w, h);
            default -> casa(g, w, h, variante);
        }
        g.dispose();
        return im;
    }

    private static void base(Graphics2D g, int w, int h, Color muro, Color zocalo) {
        rect(g, SOMBRA, 7, h - 8, w - 10, 8);
        rect(g, CONTORNO, 5, 23, w - 10, h - 25);
        rect(g, muro, 8, 26, w - 16, h - 31);
        rect(g, zocalo, 8, h - 13, w - 16, 8);
        rect(g, new Color(255, 255, 255, 75), 9, 27, 2, h - 41);
    }

    private static void tejado(Graphics2D g, int w, Color oscuro, Color principal, Color claro, boolean aDosAguas) {
        if (aDosAguas) {
            int[] xs = {3, w / 2, w - 3};
            int[] ys = {29, 3, 29};
            g.setColor(oscuro);
            g.fillPolygon(xs, ys, 3);
            g.setColor(principal);
            g.fillPolygon(new int[]{7, w / 2, w - 7}, new int[]{25, 8, 25}, 3);
            rect(g, claro, 11, 23, w - 22, 3);
        } else {
            rect(g, oscuro, 3, 7, w - 6, 25);
            rect(g, principal, 5, 9, w - 10, 18);
            rect(g, claro, 7, 10, w - 14, 4);
            rect(g, oscuro, 3, 26, w - 6, 5);
        }
        // Reflejos y tejas dibujados como píxeles, no como degradados suaves.
        for (int x = 13; x < w - 8; x += 19) {
            rect(g, new Color(255, 255, 255, 45), x, 15, 8, 2);
        }
    }

    private static void puerta(Graphics2D g, int w, int h, Color color, boolean doble) {
        int ancho = doble ? 25 : 16;
        int x = w / 2 - ancho / 2;
        rect(g, CONTORNO, x - 3, h - 27, ancho + 6, 26);
        rect(g, color, x, h - 23, ancho, 21);
        rect(g, BRILLO, x + 3, h - 20, doble ? 6 : 4, 9);
        if (doble) rect(g, CONTORNO, x + ancho / 2, h - 23, 2, 21);
        rect(g, new Color(230, 198, 93), x + ancho - 5, h - 12, 2, 2);
        rect(g, new Color(218, 213, 195), x - 5, h - 2, ancho + 10, 2);
    }

    private static void ventana(Graphics2D g, int x, int y, int ancho, int alto) {
        rect(g, CONTORNO, x - 2, y - 2, ancho + 4, alto + 4);
        rect(g, CRISTAL, x, y, ancho, alto);
        rect(g, BRILLO, x + 2, y + 1, 3, alto - 3);
        rect(g, new Color(55, 137, 182), x + ancho / 2, y, 2, alto);
        rect(g, new Color(236, 243, 230), x - 3, y + alto + 2, ancho + 6, 3);
    }

    private static void cartel(Graphics2D g, int w, int y, Color fondo, String texto) {
        int ancho = Math.min(w - 22, Math.max(33, texto.length() * 9 + 14));
        int x = (w - ancho) / 2;
        rect(g, CONTORNO, x - 2, y - 2, ancho + 4, 15);
        rect(g, fondo, x, y, ancho, 11);
        g.setFont(new Font("Monospaced", Font.BOLD, 10));
        g.setColor(Color.WHITE);
        int textoAncho = g.getFontMetrics().stringWidth(texto);
        g.drawString(texto, (w - textoAncho) / 2, y + 9);
    }

    private static void centro(Graphics2D g, int w, int h) {
        base(g, w, h, new Color(248, 232, 214), new Color(193, 185, 178));
        tejado(g, w, new Color(111, 41, 56), new Color(216, 60, 76), new Color(251, 129, 126), false);
        rect(g, new Color(250, 245, 228), w / 2 - 12, 9, 24, 24);
        rect(g, CONTORNO, w / 2 - 11, 10, 22, 21);
        g.setColor(new Color(248, 247, 239)); g.fillOval(w / 2 - 9, 11, 18, 18);
        g.setColor(new Color(221, 66, 73)); g.fillArc(w / 2 - 8, 12, 16, 16, 0, 180);
        rect(g, CONTORNO, w / 2 - 8, 20, 16, 2);
        g.setColor(CONTORNO); g.fillOval(w / 2 - 3, 18, 6, 6);
        g.setColor(Color.WHITE); g.fillOval(w / 2 - 1, 20, 2, 2);
        ventana(g, 15, h - 44, 18, 13);
        ventana(g, w - 33, h - 44, 18, 13);
        puerta(g, w, h, new Color(119, 194, 201), true);
        rect(g, new Color(192, 58, 71), w / 2 - 18, h - 33, 36, 5);
    }

    private static void tienda(Graphics2D g, int w, int h) {
        base(g, w, h, new Color(247, 239, 200), new Color(179, 179, 143));
        tejado(g, w, new Color(32, 91, 115), new Color(41, 158, 192), new Color(137, 221, 223), true);
        cartel(g, w, 29, new Color(46, 114, 176), "MART");
        ventana(g, 12, h - 43, 15, 13);
        ventana(g, w - 27, h - 43, 15, 13);
        for (int x = 8; x < w - 8; x += 10) {
            rect(g, (x / 10) % 2 == 0 ? new Color(250, 192, 66) : Color.WHITE,
                    x, h - 35, Math.min(10, w - 8 - x), 7);
        }
        puerta(g, w, h, new Color(47, 112, 160), false);
    }

    private static void gimnasio(Graphics2D g, int w, int h) {
        base(g, w, h, new Color(190, 198, 214), new Color(83, 95, 122));
        tejado(g, w, new Color(45, 53, 96), new Color(83, 86, 159), new Color(130, 139, 206), false);
        rect(g, new Color(225, 191, 76), 14, 30, w - 28, 3);
        rect(g, new Color(69, 77, 118), 10, 35, 11, h - 49);
        rect(g, new Color(69, 77, 118), w - 21, 35, 11, h - 49);
        rect(g, new Color(237, 215, 124), 9, 35, 13, 5);
        rect(g, new Color(237, 215, 124), w - 22, 35, 13, 5);
        cartel(g, w, 38, new Color(69, 74, 127), "GYM");
        g.setColor(new Color(250, 206, 73));
        g.fillPolygon(new int[]{w / 2, w / 2 + 4, w / 2 + 13, w / 2 + 6,
                        w / 2 + 9, w / 2, w / 2 - 9, w / 2 - 6, w / 2 - 13, w / 2 - 4},
                new int[]{5, 14, 14, 20, 29, 24, 29, 20, 14, 14}, 10);
        ventana(g, 25, h - 42, 13, 10);
        ventana(g, w - 38, h - 42, 13, 10);
        puerta(g, w, h, new Color(72, 116, 169), true);
    }

    private static void casino(Graphics2D g, int w, int h) {
        base(g, w, h, new Color(238, 216, 227), new Color(140, 96, 139));
        tejado(g, w, new Color(82, 48, 107), new Color(155, 72, 174), new Color(210, 138, 218), false);
        cartel(g, w, 34, new Color(141, 82, 169), "CASINO");
        rect(g, new Color(242, 194, 85), 9, 30, w - 18, 3);
        ventana(g, 16, h - 42, 17, 13);
        ventana(g, w - 33, h - 42, 17, 13);
        puerta(g, w, h, new Color(114, 76, 140), true);
    }

    private static void gobierno(Graphics2D g, int w, int h) {
        base(g, w, h, new Color(221, 208, 182), new Color(158, 149, 133));
        tejado(g, w, new Color(68, 91, 105), new Color(103, 128, 132), new Color(153, 173, 170), true);
        rect(g, new Color(237, 224, 176), 15, 31, w - 30, 5);
        for (int x : new int[]{18, 32, w - 43, w - 29}) {
            rect(g, CONTORNO, x - 1, 39, 9, h - 56);
            rect(g, new Color(241, 232, 203), x, 39, 7, h - 58);
        }
        cartel(g, w, 36, new Color(86, 107, 107), "SEDE");
        puerta(g, w, h, new Color(87, 114, 126), true);
    }

    private static void casa(Graphics2D g, int w, int h, int variante) {
        Color[] techos = {
                new Color(184, 83, 67), new Color(115, 102, 169),
                new Color(177, 127, 55), new Color(73, 130, 104),
                new Color(183, 100, 122)
        };
        Color[] muros = {
                new Color(239, 222, 191), new Color(236, 226, 245),
                new Color(248, 235, 198), new Color(218, 237, 212),
                new Color(243, 222, 215)
        };
        int v = Math.floorMod(variante, techos.length);
        base(g, w, h, muros[v], new Color(163, 148, 142));
        tejado(g, w, techos[v].darker(), techos[v], techos[v].brighter(), v % 2 == 0);
        if (v != 1) {
            rect(g, new Color(70, 72, 83), 13, 8, 9, 15);
            rect(g, new Color(190, 177, 167), 15, 10, 5, 11);
        }
        ventana(g, 10, h - 39, 14, 12);
        ventana(g, w - 24, h - 39, 14, 12);
        puerta(g, w, h, v % 2 == 0 ? new Color(128, 78, 62) : new Color(83, 107, 118), false);
        // Macetas laterales para distinguir los hogares de los edificios públicos.
        rect(g, new Color(161, 90, 62), 5, h - 15, 6, 7);
        rect(g, new Color(53, 143, 71), 6, h - 19, 4, 7);
        rect(g, new Color(161, 90, 62), w - 11, h - 15, 6, 7);
        rect(g, new Color(53, 143, 71), w - 10, h - 19, 4, 7);
    }

    private static void rect(Graphics2D g, Color c, int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) return;
        g.setColor(c);
        g.fillRect(x, y, w, h);
    }
}
