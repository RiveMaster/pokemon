package Proyecto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lector del paquete pixel-art original de la previsualización.
 * Atlas opcional: Tiles/VisualRetro.png (192x64 o 384x128).
 *
 * Diseño del atlas:
 * - y=0, seis fachadas de 64x48 (centro, gimnasio, tienda, casas)
 * - y=48, 16 suelos de 16x16
 * - y=64, 3x4 poses de entrenador de 16x16
 *
 * El motor conserva los mapas y colisiones de 16x16, y puede seguir funcionando
 * sin este recurso utilizando las ilustraciones que existían antes.
 */
public final class ArteVisualRetro {
    private static final Path ARCHIVO = Path.of("Tiles", "VisualRetro.png");
    private static BufferedImage atlas;
    private static boolean consultado;

    private ArteVisualRetro() { }

    private static synchronized BufferedImage atlas() {
        if (consultado) return atlas;
        consultado = true;
        if (!Files.isRegularFile(ARCHIVO)) return null;
        try {
            BufferedImage imagen = ImageIO.read(ARCHIVO.toFile());
            if (imagen != null && imagen.getWidth() >= 192 && imagen.getHeight() >= 64)
                atlas = imagen;
        } catch (IOException ex) {
            System.err.println("El atlas visual no se pudo abrir: " + ex.getMessage());
        }
        return atlas;
    }

    public static boolean disponible() { return atlas() != null; }

    private static BufferedImage cortar(int x, int y, int w, int h) {
        BufferedImage recurso = atlas();
        if (recurso == null || x < 0 || y < 0 || x + w > 384 || y + h > 128) return null;
        // Las coordenadas se expresan sobre el atlas lógico 384x128.
        // El recurso distribuido ocupa la mitad y se amplía sin difuminar píxeles.
        double escalaX = recurso.getWidth() / 384.0;
        double escalaY = recurso.getHeight() / 128.0;
        int sx = (int) Math.round(x * escalaX);
        int sy = (int) Math.round(y * escalaY);
        int ex = (int) Math.round((x+w) * escalaX);
        int ey = (int) Math.round((y+h) * escalaY);
        BufferedImage copia = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = copia.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(recurso, 0, 0, w, h, sx, sy, ex, ey, null);
        g.dispose();
        return copia;
    }

    public static BufferedImage suelo(int indice, BufferedImage original) {
        BufferedImage imagen = cortar(Math.floorMod(indice, 16) * 16, 48, 16, 16);
        return imagen == null ? original : imagen;
    }

    public static BufferedImage edificio(String tipo, int anchoCasillas, int altoCasillas, int variante) {
        String clase = tipo == null ? "CASA" : tipo.toUpperCase(java.util.Locale.ROOT);
        int indice = switch (clase) {
            case "CENTRO_POKEMON", "CENTRO_POKÉMON" -> 0;
            case "GIMNASIO" -> 1;
            case "TIENDA" -> 2;
            case "CASA_MAMA" -> 4;
            case "CASA" -> 3 + Math.floorMod(variante, 3);
            default -> -1;
        };
        if (indice < 0) return null;
        BufferedImage original = cortar(indice*64, 0, 64, 48);
        if (original == null) return null;
        int w = anchoCasillas * 16, h = altoCasillas * 16;
        BufferedImage imagen = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(original, 0, 0, w, h, null);
        g.dispose();
        return imagen;
    }

    private static BufferedImage fondoCombate;

    /**
     * Fondo 2D original: capas de cielo, bosque, montañas y pradera con
     * textura del pack. Se pinta a baja resolución y se escala por vecino
     * próximo para respetar el aspecto retro.
     */
    public static void pintarFondoCombate(Graphics2D pantalla, int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) return;
        if (fondoCombate == null) fondoCombate = construirFondoCombate();
        Object interpolacionAnterior = pantalla.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        pantalla.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        pantalla.drawImage(fondoCombate, 0, 0, ancho, alto, null);
        if (interpolacionAnterior != null)
            pantalla.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolacionAnterior);
    }

    private static BufferedImage construirFondoCombate() {
        final int w = 320, h = 160;
        BufferedImage fondo = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = fondo.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(new Color(105, 190, 232)); g.fillRect(0, 0, w, 75);
        g.setColor(new Color(146, 209, 237)); g.fillRect(0, 45, w, 29);
        g.setColor(new Color(186, 223, 233)); g.fillRect(0, 74, w, 25);
        // Nubes escalonadas sin bordes suavizados.
        for (int x : new int[]{18, 125, 239}) {
            g.setColor(new Color(242, 251, 246));
            g.fillRect(x+9, 12, 29, 5);
            g.fillRect(x+4, 17, 45, 8);
            g.fillRect(x+16, 7, 16, 5);
            g.setColor(new Color(212, 235, 239));
            g.fillRect(x+8, 25, 35, 3);
        }
        g.setColor(new Color(92, 164, 158));
        g.fillPolygon(new int[]{0,40,92,123,169,218,262,320,320,0},
                new int[]{90,63,84,55,83,58,86,64,114,114},10);
        g.setColor(new Color(59, 129, 129));
        g.fillPolygon(new int[]{0,50,104,151,195,245,300,320,320,0},
                new int[]{105,86,112,77,106,84,110,99,125,125},10);
        for (int i = 0; i < 36; i++) {
            int x = i * 10 - 5;
            int altura = 17 + (i * 13) % 14;
            int y = 110;
            g.setColor(i % 3 == 0 ? new Color(37, 105, 85) : new Color(52, 128, 94));
            g.fillPolygon(new int[]{x,x+7,x+14},new int[]{y,y-altura,y},3);
            g.fillRect(x+5, y-4, 4, 8);
        }
        g.setColor(new Color(110, 170, 99)); g.fillRect(0, 110, w, 50);
        g.setColor(new Color(83, 148, 80)); g.fillRect(0, 136, w, 24);

        BufferedImage pasto = suelo(0, null);
        if (pasto != null) {
            for (int y = 114; y < h; y += 16) for (int x = 0; x < w; x += 16) {
                g.drawImage(pasto, x, y, 16, 16, null);
            }
        } else {
            g.setColor(new Color(132, 184, 103));
            for (int i=0; i<125; i++) {
                int x=(i*41)%w, y=117+(i*23)%42;
                g.fillRect(x,y,3,1);
            }
        }
        // Iluminación de la pradera y flores sueltas.
        for (int i=0; i<27; i++) {
            int x=(i*47+9)%w, y=122+(i*17)%35;
            g.setColor(new Color(209, 231, 146));
            g.fillRect(x,y,4,1);
            if (i%4==0) {
                g.setColor(new Color(239, 201, 126));
                g.fillRect(x+1,y-2,2,2);
            }
        }
        g.dispose();
        return fondo;
    }

    public static BufferedImage entrenador(int direccion, int paso) {
        if (direccion < 0 || direccion > 3) return null;
        return cortar(Math.floorMod(paso,3) * 16, 64+direccion*16, 16, 16);
    }
}
