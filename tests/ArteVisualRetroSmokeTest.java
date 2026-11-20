package Proyecto;

import java.awt.*;
import java.awt.image.BufferedImage;

/** Comprueba que el atlas gráfico realmente existe y se usa en el motor. */
public final class ArteVisualRetroSmokeTest {
    private ArteVisualRetroSmokeTest() {}

    public static void main(String[] args) {
        assert ArteVisualRetro.disponible() : "Falta Tiles/VisualRetro.png";
        BufferedImage base = ArteVisualRetro.suelo(0, null);
        BufferedImage camino = ArteVisualRetro.suelo(1, null);
        assert base != null && base.getWidth() == 16 && base.getHeight() == 16;
        assert camino != null && camino.getHeight() == 16;
        assert !mismosPixeles(base, camino) : "El suelo y el camino tienen que ser diferentes";

        for (String tipo : new String[]{"CENTRO_POKEMON", "TIENDA", "GIMNASIO", "CASA"}) {
            BufferedImage edificio = ArteEdificios.crear(tipo, 5, 4, 1);
            assert edificio.getWidth() == 80 && edificio.getHeight() == 64;
            assert edificio.getRGB(35, 20) != 0 : "Fachada vacía para " + tipo;
        }

        SpriteJugador2D jugador = SpriteJugador2D.cargar();
        assert jugador.esPersonalizado() : "El protagonista debe utilizar el atlas nuevo";
        for (int direccion = 0; direccion < 4; direccion++)
            for (int paso = 0; paso < 3; paso++)
                assert jugador.fotograma(direccion, paso) != null;

        BufferedImage fondo = new BufferedImage(640, 320, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fondo.createGraphics();
        ArteVisualRetro.pintarFondoCombate(g, 640, 320);
        g.dispose();
        assert fondo.getRGB(30, 20) != fondo.getRGB(30, 300);
        System.out.println("ArteVisualRetroSmokeTest: atlas, fachadas, entrenador y batalla OK");
    }

    private static boolean mismosPixeles(BufferedImage a, BufferedImage b) {
        for (int y = 0; y < 16; y++) for (int x = 0; x < 16; x++)
            if (a.getRGB(x, y) != b.getRGB(x, y)) return false;
        return true;
    }
}
