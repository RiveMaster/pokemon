package Proyecto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lector del paquete pixel-art original de la previsualización.
 * Atlas opcional: Tiles/VisualRetro.png (384x128).
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
            if (imagen != null && imagen.getWidth() >= 384 && imagen.getHeight() >= 128)
                atlas = imagen;
        } catch (IOException ex) {
            System.err.println("El atlas visual no se pudo abrir: " + ex.getMessage());
        }
        return atlas;
    }

    public static boolean disponible() { return atlas() != null; }

    private static BufferedImage cortar(int x, int y, int w, int h) {
        BufferedImage recurso = atlas();
        if (recurso == null || x < 0 || y < 0
                || x + w > recurso.getWidth() || y + h > recurso.getHeight()) return null;
        BufferedImage copia = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = copia.createGraphics();
        g.drawImage(recurso, 0, 0, w, h, x, y, x+w, y+h, null);
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

    public static BufferedImage entrenador(int direccion, int paso) {
        if (direccion < 0 || direccion > 3) return null;
        return cortar(Math.floorMod(paso,3) * 16, 64+direccion*16, 16, 16);
    }
}
