package Proyecto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Atlas EXCLUSIVO aportado por el usuario. No descarga imágenes, ni usa
 * SpriteCollab, RPGPixelPack o recursos dibujados por código como sustitutos.
 *
 * Importante: la imagen contiene objetos que NO comienzan siempre en múltiplos
 * de 16. Los recortes se expresan en píxeles absolutos y no por celdas.
 */
public final class AtlasUsuario16 {
    public static final Path ARCHIVO = Path.of("Tiles", "AtlasUsuario.png");
    public static final int ANCHO_ESPERADO = 864;
    public static final int ALTO_ESPERADO = 925;
    public static final int TILE = 16;

    public record Pieza(String nombre, int x, int y, int ancho, int alto,
                        boolean solido, String categoria) {
        public Pieza {
            if (nombre == null || nombre.isBlank() || x < 0 || y < 0
                    || ancho < 1 || alto < 1)
                throw new IllegalArgumentException("Pieza o rectángulo inválido");
        }
    }

    /** Rectángulos inspeccionados en el PNG ORIGINAL (864 × 925). */
    private static final Map<String, Pieza> PIEZAS = crearCatalogo();
    private final BufferedImage imagen;

    private AtlasUsuario16(BufferedImage imagen) {
        if (imagen == null || imagen.getWidth() != ANCHO_ESPERADO
                || imagen.getHeight() != ALTO_ESPERADO) {
            throw new IllegalArgumentException(
                    "Se esperaba el atlas original de 864 x 925 píxeles.");
        }
        this.imagen = imagen;
    }

    public static boolean instalado() { return Files.isRegularFile(ARCHIVO); }

    public static AtlasUsuario16 cargar() throws IOException {
        if (!instalado())
            throw new IOException("Falta " + ARCHIVO
                    + ". Instala únicamente el PNG facilitado por el usuario.");
        BufferedImage imagen = ImageIO.read(ARCHIVO.toFile());
        if (imagen == null)
            throw new IOException("El archivo del atlas no es un PNG válido.");
        try {
            return new AtlasUsuario16(imagen);
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }

    /** También sirve para pruebas de la cuadrícula y del manifiesto. */
    public static AtlasUsuario16 desdeImagen(BufferedImage imagen) {
        return new AtlasUsuario16(imagen);
    }

    public BufferedImage recortar(Pieza pieza) {
        if (pieza.x() + pieza.ancho() > imagen.getWidth()
                || pieza.y() + pieza.alto() > imagen.getHeight())
            throw new IllegalArgumentException("Pieza fuera del atlas: " + pieza.nombre());
        BufferedImage recorte = new BufferedImage(
                pieza.ancho(), pieza.alto(), BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = recorte.createGraphics();
        g.drawImage(imagen, 0, 0, pieza.ancho(), pieza.alto(),
                pieza.x(), pieza.y(), pieza.x() + pieza.ancho(),
                pieza.y() + pieza.alto(), null);
        g.dispose();
        return recorte;
    }

    public BufferedImage sprite(String nombre) {
        Pieza pieza = PIEZAS.get(nombre);
        if (pieza == null)
            throw new IllegalArgumentException("Pieza desconocida: " + nombre);
        return recortar(pieza);
    }

    public static Map<String, Pieza> piezas() { return PIEZAS; }

    private static Map<String, Pieza> crearCatalogo() {
        Map<String, Pieza> m = new LinkedHashMap<>();
        registrar(m, "SUELO_CLARO",       6,  93, 16, 16, false, "TERRENO");
        registrar(m, "SUELO_TIERRA",    270,  95, 16, 16, false, "TERRENO");
        registrar(m, "SUELO_PIEDRA",    291,  95, 16, 16, false, "TERRENO");
        registrar(m, "SUELO_AGUA",      277, 154, 16, 16, true,  "AGUA");
        registrar(m, "SUELO_VERDE",     327,  93, 16, 16, false, "TERRENO");
        registrar(m, "ARBOL_REDONDO",     8,   1, 15, 18, true,  "VEGETACION");
        registrar(m, "ARBOL_REDONDO_2",  26,   1, 15, 18, true,  "VEGETACION");
        registrar(m, "PINO",              8,  24, 21, 27, true,  "VEGETACION");
        registrar(m, "PINO_2",           57,  24, 21, 27, true,  "VEGETACION");
        registrar(m, "ARBUSTO",         217,  56, 16, 19, true,  "VEGETACION");
        registrar(m, "ARBUSTO_2",       198,  55, 18, 20, true,  "VEGETACION");
        registrar(m, "CASA_ROJA",       211, 479, 32, 44, true,  "CASA");
        registrar(m, "CASA_VERDE",      295, 480, 31, 42, true,  "CASA");
        registrar(m, "TIENDA_AZUL",     401, 483, 35, 37, true,  "TIENDA");
        registrar(m, "EDIFICIO_AZUL",   370, 483, 28, 37, true,  "CENTRO_POKEMON");
        registrar(m, "CASA_MARRON",     475, 478, 29, 44, true,  "CASA");
        registrar(m, "CASA_GRANDE",     332, 480, 38, 42, true,  "CASA");
        registrar(m, "INTERIOR_SUELO",  206, 198, 16, 16, false, "INTERIOR");
        registrar(m, "INTERIOR_PARED",  222, 198, 16, 16, true,  "INTERIOR");
        registrar(m, "INTERIOR_OBJETO", 169, 280, 16, 16, true,  "INTERIOR");
        return Map.copyOf(m);
    }

    private static void registrar(Map<String, Pieza> m, String nombre,
                                  int x, int y, int w, int h,
                                  boolean solido, String categoria) {
        Pieza p = new Pieza(nombre, x, y, w, h, solido, categoria);
        if (x + w > ANCHO_ESPERADO || y + h > ALTO_ESPERADO)
            throw new IllegalArgumentException("Fuera del atlas: " + nombre);
        m.put(nombre, p);
    }
}
