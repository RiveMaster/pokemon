package Proyecto;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Caminos del PNG del usuario, preparados como autotiles de 16x16.
 *
 * El atlas contiene dos familias (sendero de tierra y pavimento) y
 * 16 mascaras cardinales x 16 mascaras diagonales por familia.
 * El resultado se amplia exactamente 2x para los mapas regionales de 32px.
 *
 * N=1, E=2, S=4, O=8. Diagonales: NO=1, NE=2, SE=4, SO=8.
 * Ningun dibujo ni color procede de un paquete grafico externo.
 */
public final class CaminosUsuario {
    public static final Path ARCHIVO = Path.of("Tiles", "CaminosUsuario.png");
    private static volatile BufferedImage atlas;
    private static final BufferedImage[] CACHE = new BufferedImage[512];

    private CaminosUsuario() { }

    public static boolean disponible() {
        return Files.isRegularFile(ARCHIVO);
    }

    private static BufferedImage cargar() {
        if (atlas != null) return atlas;
        synchronized (CaminosUsuario.class) {
            if (atlas != null) return atlas;
            if (!disponible()) return null;
            try {
                BufferedImage original = ImageIO.read(ARCHIVO.toFile());
                if (original == null || original.getWidth() != 256
                        || original.getHeight() != 512)
                    throw new IOException("Se esperaba un atlas de caminos de 256x512 px");
                atlas = original;
                return original;
            } catch (IOException ex) {
                throw new IllegalStateException(
                        "El atlas de caminos del usuario está dañado: " + ARCHIVO, ex);
            }
        }
    }

    /**
     * @param piedra false = sendero beige; true = pavimento gris
     * @param cardinal conexiones N/E/S/O en bits 1/2/4/8
     * @param diagonales conexiones NO/NE/SE/SO en bits 1/2/4/8
     */
    public static BufferedImage tile(boolean piedra, int cardinal, int diagonales) {
        BufferedImage fuente = cargar();
        if (fuente == null) return null;
        int c = cardinal & 15, d = diagonales & 15;
        int key = (piedra ? 256 : 0) + d * 16 + c;
        BufferedImage guardado = CACHE[key];
        if (guardado != null) return guardado;
        synchronized (CaminosUsuario.class) {
            if (CACHE[key] != null) return CACHE[key];
            int y = (piedra ? 256 : 0) + d * 16;
            BufferedImage resultado = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resultado.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.drawImage(fuente, 0, 0, 32, 32, c * 16, y, c * 16 + 16, y + 16, null);
            g.dispose();
            CACHE[key] = resultado;
            return resultado;
        }
    }
}
