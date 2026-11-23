package Proyecto;

import java.awt.image.BufferedImage;

/** Autotiles de 32x32 y objetos de dos casillas: prueba sin interfaz. */
public final class TerrenosConVecinosSmokeTest {
    private TerrenosConVecinosSmokeTest() { }

    public static void main(String[] args) {
        ArteRegion2D.Paleta p=ArteRegion2D.paleta(1,1);
        assert TerrenosConVecinos32.mascara(true,false,true,false)==5;
        assert TerrenosConVecinos32.mascara(false,true,false,true)==10;
        assert TerrenosConVecinos32.mascara(true,true,true,true)==15;
        BufferedImage aislado=TerrenosConVecinos32.claro(p,0,0,17);
        BufferedImage central=TerrenosConVecinos32.claro(p,15,15,17);
        assert aislado.getWidth()==32 && aislado.getHeight()==32;
        assert central.getWidth()==32 && central.getHeight()==32;
        assert !iguales(aislado,central)
                : "El borde del claro debe depender de los vecinos";
        BufferedImage borde=TerrenosConVecinos32.plataforma(p,0,3);
        BufferedImage interior=TerrenosConVecinos32.plataforma(p,15,3);
        assert !iguales(borde,interior)
                : "El muro frontal solo debe aparecer cuando falta el vecino sur";
        BufferedImage pino=TerrenosConVecinos32.pino(0,false);
        BufferedImage nieve=TerrenosConVecinos32.pino(1,true);
        assert pino.getWidth()==32 && pino.getHeight()==64;
        assert nieve.getWidth()==32 && nieve.getHeight()==64;

        CatalogoMapasGeneracionales.Mapa bosque=
                CatalogoMapasGeneracionales.obtener(Generacion.GEN1,1);
        MapasRegion2D.Resultado mapa=MapasRegion2D.generar(bosque);
        int superior=4*mapa.ancho+21;
        assert mapa.colisiones[superior] : "La plataforma debe ser sólida";
        assert mapa.capas[2][superior]>=0 : "La plataforma no se ha dibujado";
        int claro=4*mapa.ancho+5;
        assert !mapa.colisiones[claro] : "El claro debe poder recorrerse";
        assert mapa.capas[1][claro]>=0 : "El claro necesita textura visible";
        System.out.println("TerrenosConVecinosSmokeTest: transiciones y objetos multicelda OK");
    }

    private static boolean iguales(BufferedImage a,BufferedImage b) {
        if(a.getWidth()!=b.getWidth()||a.getHeight()!=b.getHeight())return false;
        for(int y=0;y<a.getHeight();y++)for(int x=0;x<a.getWidth();x++)
            if(a.getRGB(x,y)!=b.getRGB(x,y))return false;
        return true;
    }
}
