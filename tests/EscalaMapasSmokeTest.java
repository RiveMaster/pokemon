package Proyecto;

import java.awt.image.BufferedImage;

/**
 * Comprueba que el edificio se recorte por su alfa y se escale al
 * máximo factor entero sin perder píxeles ni sobresalir de su solar.
 */
public final class EscalaMapasSmokeTest {
    private EscalaMapasSmokeTest() {}

    private static int[] limites(BufferedImage img) {
        int minX=img.getWidth(),minY=img.getHeight(),maxX=-1,maxY=-1;
        for (int y=0;y<img.getHeight();y++)
            for (int x=0;x<img.getWidth();x++)
                if((img.getRGB(x,y)>>>24)!=0) {
                    minX=Math.min(minX,x);minY=Math.min(minY,y);
                    maxX=Math.max(maxX,x);maxY=Math.max(maxY,y);
                }
        if(maxX<minX)return new int[]{0,0,0,0};
        return new int[]{minX,minY,maxX-minX+1,maxY-minY+1};
    }

    public static void main(String[] args) {
        AtlasCorregido atlas=AtlasCorregido.actual();
        assert atlas!=null : "La selección incorporada debe estar disponible";
        for (String clase : new String[]{"CENTRO_POKEMON","TIENDA","GIMNASIO","CASA"}) {
            BufferedImage imagen=atlas.edificio(clase,7,7,0);
            assert imagen!=null;
            assert imagen.getWidth()==224 && imagen.getHeight()==224;
            int[] caja=limites(imagen);
            assert caja[2]>0 && caja[3]>0;
            assert caja[0]>=0 && caja[1]>=0;
            assert caja[0]+caja[2]<=224 && caja[1]+caja[3]<=224;
            assert caja[1]+caja[3]==224 : "Edificio no apoyado al suelo: "+clase;
            BufferedImage fuente=atlas.sprite(switch(clase) {
                case "CENTRO_POKEMON" -> "CentroPokemon1";
                case "TIENDA" -> "TiendaAzul1";
                case "GIMNASIO" -> "GimnasioRojo1";
                default -> "CasaRoja1";
            });
            BufferedImage recorte=AtlasCorregido.recortarTransparencia(fuente);
            int escala=AtlasCorregido.escalaEntera(recorte,224,224);
            assert escala>=3 : "Edificio todavía muy pequeño: "+clase;
            assert caja[2]==recorte.getWidth()*escala;
            assert caja[3]==recorte.getHeight()*escala;
            System.out.println(clase+" · "+caja[2]+"x"+caja[3]+" dentro de 224x224");
        }
        // El recortador nunca elimina píxeles de un sprite con transparencia.
        BufferedImage prueba=new BufferedImage(10,12,BufferedImage.TYPE_INT_ARGB);
        prueba.setRGB(2,3,0xff234567);
        prueba.setRGB(6,10,0xffabcdef);
        BufferedImage recorte=AtlasCorregido.recortarTransparencia(prueba);
        assert recorte.getWidth()==5 && recorte.getHeight()==8;
        assert recorte.getRGB(0,0)==0xff234567;
        assert recorte.getRGB(4,7)==0xffabcdef;
        assert AtlasCorregido.escalaEntera(recorte,224,224)>=4;
        System.out.println("EscalaMapasSmokeTest: fachadas grandes sin recortes OK");
    }
}
