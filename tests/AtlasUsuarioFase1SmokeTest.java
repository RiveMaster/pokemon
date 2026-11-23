package Proyecto;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Prueba inicial: el mapa utiliza exclusivamente el atlas aportado y sus
 * objetos respetan las entradas. No necesita abrir ventanas.
 */
public final class AtlasUsuarioFase1SmokeTest {
    private AtlasUsuarioFase1SmokeTest() { }

    public static void main(String[] args) throws Exception {
        assert AtlasUsuario16.instalado() : "Falta la imagen compacta del usuario";
        AtlasUsuario16 atlas = AtlasUsuario16.cargar();
        assert !atlas.esOriginalCompleto() || atlas.sprite("CASA_ROJA").getWidth() == 32;
        assert AtlasUsuario16.piezas().size() >= 20;
        for (AtlasUsuario16.Pieza pieza : AtlasUsuario16.piezas().values()) {
            BufferedImage imagen = atlas.sprite(pieza.nombre());
            assert imagen.getWidth() == pieza.ancho() && imagen.getHeight() == pieza.alto();
        }

        MapaAtlasUsuario.Mundo mundo = MapaAtlasUsuario.generar(atlas);
        assert MapaAtlasUsuario.ANCHO == 32 && MapaAtlasUsuario.ALTO == 22;
        assert mundo.puedePisar(16, 12) : "El camino central está bloqueado";
        Set<String> servicios = new HashSet<>();
        for (MapaAtlasUsuario.Objeto obj : mundo.objetos()) {
            if (obj.evento() == null) continue;
            servicios.add(obj.evento());
            BufferedImage img = atlas.sprite(obj.sprite());
            int puertaX = obj.x() + ((img.getWidth() + 15) / 16) / 2;
            int puertaY = obj.y() + (img.getHeight() + 15) / 16;
            assert mundo.puedePisar(puertaX, puertaY)
                    : "Puerta tapada: " + obj.sprite();
            assert obj.evento().equals(mundo.eventoFrente(puertaX, puertaY));
        }
        assert servicios.contains("CASA");
        assert servicios.contains("CENTRO_POKEMON");
        assert servicios.contains("TIENDA");
        System.out.println("AtlasUsuarioFase1SmokeTest: sprites originales y entradas OK");
    }
}
