package Proyecto;

import java.awt.image.BufferedImage;

/** Verifica fotogramas y compatibilidad de sprites sin abrir una ventana Swing. */
public final class Animaciones2DSmokeTest {
    private Animaciones2DSmokeTest() { }

    public static void main(String[] args) {
        SpriteJugador2D original = SpriteJugador2D.provisional();
        assert !original.esPersonalizado();
        assert original.fotograma(SpriteJugador2D.FRENTE, 0).getWidth() == 16;
        assert original.fotograma(SpriteJugador2D.ESPALDA, 1).getHeight() == 16;

        // Las poses deben dibujar formas distintas, no desplazar un PNG idéntico.
        assert !iguales(original.fotograma(SpriteJugador2D.FRENTE, 0),
                original.fotograma(SpriteJugador2D.ESPALDA, 0));
        assert !iguales(original.fotograma(SpriteJugador2D.FRENTE, 1),
                original.fotograma(SpriteJugador2D.FRENTE, 2));
        assert !iguales(original.fotograma(SpriteJugador2D.IZQUIERDA, 0),
                original.fotograma(SpriteJugador2D.DERECHA, 0));

        // Una hoja personal de 3 columnas y 4 filas respeta direcciones y pasos.
        BufferedImage hoja = new BufferedImage(48, 64, BufferedImage.TYPE_INT_ARGB);
        for (int fila = 0; fila < 4; fila++) for (int col = 0; col < 3; col++) {
            int color = 0xFF000000 | (fila << 12) | (col << 4);
            for (int y = 0; y < 16; y++) for (int x = 0; x < 16; x++)
                hoja.setRGB(col * 16 + x, fila * 16 + y, color);
        }
        SpriteJugador2D personalizado = SpriteJugador2D.desdeHoja(hoja);
        assert personalizado.esPersonalizado();
        for (int fila = 0; fila < 4; fila++) for (int col = 0; col < 3; col++) {
            int esperado = 0xFF000000 | (fila << 12) | (col << 4);
            assert personalizado.fotograma(fila, col).getRGB(0, 0) == esperado;
        }

        // Los PNG antiguos de una única pose siguen funcionando en cuatro direcciones.
        BufferedImage antiguo = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        antiguo.setRGB(5, 5, 0xFFFFAABB);
        SpriteJugador2D compatible = SpriteJugador2D.desdeHoja(antiguo);
        assert compatible.fotograma(SpriteJugador2D.ESPALDA, 2).getRGB(5, 5) == 0xFFFFAABB;

        boolean rechaza = false;
        try { SpriteJugador2D.desdeHoja(new BufferedImage(17, 16, BufferedImage.TYPE_INT_ARGB)); }
        catch (IllegalArgumentException ex) { rechaza = true; }
        assert rechaza : "No deben cortarse PNG de dimensiones inválidas";
        System.out.println("Animaciones2DSmokeTest: 4 direcciones, pasos y PNG antiguos OK");
    }

    private static boolean iguales(BufferedImage a, BufferedImage b) {
        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) return false;
        for (int y = 0; y < a.getHeight(); y++)
            for (int x = 0; x < a.getWidth(); x++)
                if (a.getRGB(x, y) != b.getRGB(x, y)) return false;
        return true;
    }
}
