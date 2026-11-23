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
    public static final Path PAQUETE_INICIAL = Path.of("Tiles", "AtlasUsuarioFase1.png");
    public static final int ANCHO_COMPACTO = 256;
    public static final int ALTO_COMPACTO = 128;
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
    private static final Map<String, int[]> COMPACTO = crearCoordenadasCompactas();
    private final BufferedImage imagen;
    private final boolean compacto;

    private AtlasUsuario16(BufferedImage imagen) {
        if (imagen == null) throw new IllegalArgumentException("Atlas nulo");
        compacto = imagen.getWidth() == ANCHO_COMPACTO
                && imagen.getHeight() == ALTO_COMPACTO;
        if (!compacto && (imagen.getWidth() != ANCHO_ESPERADO
                || imagen.getHeight() != ALTO_ESPERADO))
            throw new IllegalArgumentException(
                    "El atlas debe medir 864x925 o ser el paquete inicial de 256x128.");
        this.imagen = imagen;
    }

    public boolean esOriginalCompleto() { return !compacto; }
    public static boolean instalado() {
        return Files.isRegularFile(ARCHIVO) || Files.isRegularFile(PAQUETE_INICIAL);
    }

    public static AtlasUsuario16 cargar() throws IOException {
        // Al instalar el PNG original, el juego lo utilizará automáticamente.
        Path ruta = Files.isRegularFile(ARCHIVO) ? ARCHIVO : PAQUETE_INICIAL;
        if (!Files.isRegularFile(ruta))
            throw new IOException("Falta el atlas del usuario en Tiles/");
        BufferedImage imagen = ImageIO.read(ruta.toFile());
        if (imagen == null)
            throw new IOException("El archivo no contiene un PNG válido: " + ruta);
        try { return new AtlasUsuario16(imagen); }
        catch (IllegalArgumentException ex) { throw new IOException(ex.getMessage(), ex); }
    }

    /** También sirve para pruebas de la cuadrícula y del manifiesto. */
    public static AtlasUsuario16 desdeImagen(BufferedImage imagen) {
        return new AtlasUsuario16(imagen);
    }

    public BufferedImage recortar(Pieza pieza) {
        int x = pieza.x(), y = pieza.y();
        if (compacto) {
            int[] local = COMPACTO.get(pieza.nombre());
            if (local == null)
                throw new IllegalArgumentException("Sprite ausente en paquete inicial: " + pieza.nombre());
            x = local[0];
            y = local[1];
        }
        if (x + pieza.ancho() > imagen.getWidth()
                || y + pieza.alto() > imagen.getHeight())
            throw new IllegalArgumentException("Pieza fuera del atlas: " + pieza.nombre());
        BufferedImage recorte = new BufferedImage(
                pieza.ancho(), pieza.alto(), BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = recorte.createGraphics();
        g.drawImage(imagen, 0, 0, pieza.ancho(), pieza.alto(),
                x, y, x + pieza.ancho(), y + pieza.alto(), null);
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

    /** Posiciones de los mismos recortes en el PNG compacto incluido con el juego. */
    private static Map<String, int[]> crearCoordenadasCompactas() {
        Map<String, int[]> m = new LinkedHashMap<>();
        m.put("SUELO_CLARO", new int[]{0,0});
        m.put("SUELO_TIERRA", new int[]{16,0});
        m.put("SUELO_PIEDRA", new int[]{32,0});
        m.put("SUELO_AGUA", new int[]{48,0});
        m.put("SUELO_VERDE", new int[]{64,0});
        m.put("ARBOL_REDONDO", new int[]{0,17});
        m.put("ARBOL_REDONDO_2", new int[]{17,17});
        m.put("PINO", new int[]{36,17});
        m.put("PINO_2", new int[]{59,17});
        m.put("ARBUSTO", new int[]{84,17});
        m.put("ARBUSTO_2", new int[]{102,17});
        m.put("CASA_ROJA", new int[]{0,49});
        m.put("CASA_VERDE", new int[]{34,49});
        m.put("TIENDA_AZUL", new int[]{67,49});
        m.put("EDIFICIO_AZUL", new int[]{104,49});
        m.put("CASA_MARRON", new int[]{134,49});
        m.put("CASA_GRANDE", new int[]{165,49});
        m.put("INTERIOR_SUELO", new int[]{204,49});
        m.put("INTERIOR_PARED", new int[]{222,49});
        m.put("INTERIOR_OBJETO", new int[]{204,67});
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
