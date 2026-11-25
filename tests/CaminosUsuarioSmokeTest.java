package Proyecto;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Confirma que los senderos de la imagen original ya no se sustituyen
 * por manchas beige: se eligen según los vecinos de cada casilla.
 */
public final class CaminosUsuarioSmokeTest {
    private CaminosUsuarioSmokeTest() { }

    private static int[] esquina(BufferedImage tile, int x, int y) {
        return new int[] {tile.getRGB(x,y),tile.getRGB(x+1,y+1)};
    }

    public static void main(String[] args) {
        assert CaminosUsuario.disponible() :
                "Falta Tiles/CaminosUsuario.png, derivado del atlas del usuario";
        Set<Integer> firmas = new HashSet<>();
        for (boolean piedra : new boolean[] {false,true}) {
            for (int m=0;m<16;m++)for (int d=0;d<16;d++) {
                BufferedImage tile=CaminosUsuario.tile(piedra,m,d);
                assert tile!=null && tile.getWidth()==32 && tile.getHeight()==32;
                for(int y=0;y<32;y++)for(int x=0;x<32;x++)
                    assert (tile.getRGB(x,y)>>>24)==255 :
                            "Transparencia inesperada en camino "+m+"/"+d;
                firmas.add(tile.getRGB(0,0) ^ tile.getRGB(16,16) ^ tile.getRGB(16,0));
            }
        }
        assert firmas.size()>2 : "Las texturas originales deben ser diferentes";

        BufferedImage vertical=CaminosUsuario.tile(false,5,0);
        BufferedImage horizontal=CaminosUsuario.tile(false,10,0);
        BufferedImage cruz=CaminosUsuario.tile(false,15,15);
        assert vertical.getRGB(16,0)!=horizontal.getRGB(16,0);
        assert horizontal.getRGB(0,16)!=vertical.getRGB(0,16);
        assert cruz.getRGB(0,0)!=horizontal.getRGB(0,0);

        // Bordes entre vecinos complementarios: los colores exteriores
        // coinciden a escala nativa para no dejar costuras.
        for (int mask=0;mask<16;mask++) {
            if((mask&2)!=0) {
                BufferedImage izquierda=CaminosUsuario.tile(false,mask,0);
                BufferedImage derecha=CaminosUsuario.tile(false,8,0);
                for(int y=0;y<32;y++)
                    assert izquierda.getRGB(31,y)==derecha.getRGB(0,y) :
                            "Unión E-O defectuosa en máscara "+mask;
            }
            if((mask&4)!=0) {
                BufferedImage arriba=CaminosUsuario.tile(true,mask,0);
                BufferedImage abajo=CaminosUsuario.tile(true,1,0);
                for(int x=0;x<32;x++)
                    assert arriba.getRGB(x,31)==abajo.getRGB(x,0) :
                            "Unión N-S defectuosa en máscara "+mask;
            }
        }

        int[][] ejemplo={
            {0,1,0},
            {1,1,1},
            {0,1,0}
        };
        assert MapasRegion2D.mascaraCamino(ejemplo,1,1,1,false)==15;
        assert MapasRegion2D.mascaraCamino(ejemplo,1,1,1,true)==0;
        assert MapasRegion2D.mascaraCamino(ejemplo,1,0,1,false)==4;
        assert MapasRegion2D.mascaraCamino(ejemplo,0,1,1,false)==2;
        assert MapasRegion2D.mascaraCamino(ejemplo,2,1,1,false)==8;
        assert MapasRegion2D.mascaraCamino(ejemplo,1,2,1,false)==1;

        CatalogoMapasGeneracionales.Mapa urbano =
                CatalogoMapasGeneracionales.listar(Generacion.GEN1).stream()
                .filter(CatalogoMapasGeneracionales.Mapa::urbano)
                .findFirst().orElseThrow();
        MapasRegion2D.Resultado pueblo=MapasRegion2D.generar(urbano);
        int caminos=0;
        Set<Integer> suelos=new HashSet<>();
        for(int i=0;i<pueblo.ancho*pueblo.alto;i++) {
            int t=pueblo.capas[1][i];
            if(t>=0 && pueblo.tipos[t]==MapasRegion2D.NORMAL) {
                suelos.add(t);
                caminos++;
            }
        }
        assert caminos>100 && suelos.size()>=8 :
                "El pueblo debe tener caminos conectables de distintos tamaños";

        System.out.println("CaminosUsuarioSmokeTest: 512 variantes de caminos y cruces OK");
    }
}
