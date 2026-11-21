package Proyecto;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Protagonista de 32x48 píxeles, original, con tres fotogramas por dirección.
 * El sprite tiene más detalle que el personaje provisional de 16x16 y conserva
 * la nitidez al ampliar con nearest-neighbor.
 */
public final class EntrenadorRegion2D {
    public static final int ANCHO=32, ALTO=48;
    private static final BufferedImage[][] FRAMES=new BufferedImage[4][3];
    private static final Color TRAZO=new Color(35,44,64);
    private static final Color PIEL=new Color(246,191,151);
    private static final Color PIEL_SOMBRA=new Color(206,143,116);
    private static final Color GORRA=new Color(201,58,75);
    private static final Color GORRA_LUZ=new Color(255,112,114);
    private static final Color PELO=new Color(78,57,58);
    private static final Color CHAQUETA=new Color(49,112,175);
    private static final Color CHAQUETA_LUZ=new Color(93,162,212);
    private static final Color PANTALON=new Color(61,70,102);
    private static final Color ZAPATO=new Color(48,52,61);
    private static final Color MOCHILA=new Color(198,152,70);
    static {
        for(int dir=0;dir<4;dir++)
            for(int paso=0;paso<3;paso++)
                FRAMES[dir][paso]=dibujar(dir,paso);
    }
    private EntrenadorRegion2D() { }
    public static BufferedImage fotograma(int direccion,int paso) {
        return FRAMES[Math.max(0,Math.min(3,direccion))][Math.floorMod(paso,3)];
    }
    private static BufferedImage dibujar(int dir,int paso) {
        BufferedImage im=new BufferedImage(ANCHO,ALTO,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=im.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        int bob=paso==1?-1:0;
        int brazoIzq=paso==1?2:0, brazoDer=paso==2?2:0;
        // Las piernas alternan zancadas; el pie adelantado mira hacia la ruta.
        rect(g,TRAZO,8,35+bob,17,8);
        rect(g,PANTALON,9,35+bob,14,8);
        rect(g,TRAZO,paso==1?6:9,42+bob,9,5);
        rect(g,TRAZO,paso==2?19:17,42+bob,9,5);
        rect(g,ZAPATO,paso==1?7:10,43+bob,8,4);
        rect(g,ZAPATO,paso==2?20:18,43+bob,8,4);
        if(dir==SpriteJugador2D.ESPALDA) {
            cabeza(g,bob,true);
            rect(g,TRAZO,7,24+bob,19,14);
            rect(g,CHAQUETA,9,25+bob,15,12);
            rect(g,TRAZO,10,26+bob,13,13);
            rect(g,MOCHILA,11,27+bob,11,10);
            rect(g,new Color(237,203,112),13,28+bob,7,3);
            rect(g,TRAZO,5,26+bob+brazoIzq,4,11);
            rect(g,TRAZO,25,26+bob+brazoDer,4,11);
            rect(g,PIEL,6,35+bob+brazoIzq,3,3);
            rect(g,PIEL,26,35+bob+brazoDer,3,3);
        }else if(dir==SpriteJugador2D.FRENTE) {
            cabeza(g,bob,false);
            rect(g,TRAZO,7,24+bob,19,14);
            rect(g,CHAQUETA,9,26+bob,16,11);
            rect(g,CHAQUETA_LUZ,10,26+bob,7,10);
            rect(g,new Color(247,237,210),16,26+bob,3,11);
            rect(g,TRAZO,6,26+bob+brazoIzq,4,10);
            rect(g,TRAZO,24,26+bob+brazoDer,4,10);
            rect(g,PIEL,6,35+bob+brazoIzq,4,3);
            rect(g,PIEL,25,35+bob+brazoDer,3,3);
            rect(g,MOCHILA,25,29+bob,3,8);
        }else{
            boolean izquierda=dir==SpriteJugador2D.IZQUIERDA;
            rect(g,TRAZO,8,6+bob,18,19);
            rect(g,PELO,11,9+bob,14,13);
            rect(g,PIEL,11,13+bob,13,10);
            rect(g,PIEL_SOMBRA,izquierda?20:11,18+bob,3,4);
            rect(g,TRAZO,izquierda?12:22,16+bob,2,3);
            rect(g,TRAZO,10,7+bob,16,7);
            rect(g,GORRA,11,7+bob,15,6);
            rect(g,GORRA_LUZ,12,8+bob,10,2);
            rect(g,GORRA,izquierda?3:22,12+bob,8,4);
            rect(g,TRAZO,8,24+bob,17,13);
            rect(g,CHAQUETA,10,26+bob,14,10);
            rect(g,CHAQUETA_LUZ,izquierda?10:20,27+bob,4,6);
            rect(g,MOCHILA,izquierda?22:7,27+bob,5,10);
            rect(g,TRAZO,izquierda?8:23,27+bob+brazoIzq,3,8);
            rect(g,PIEL,izquierda?8:23,34+bob+brazoIzq,4,4);
        }
        g.dispose();
        return im;
    }
    private static void cabeza(Graphics2D g,int bob,boolean espalda){
        rect(g,TRAZO,8,7+bob,18,18);
        rect(g,espalda?PELO:PIEL,10,10+bob,14,13);
        if(espalda){
            rect(g,PELO,10,9+bob,14,13);
            rect(g,PIEL_SOMBRA,11,22+bob,12,2);
        }else{
            rect(g,PELO,9,11+bob,3,9);
            rect(g,PELO,23,11+bob,3,9);
            rect(g,TRAZO,13,17+bob,2,3);
            rect(g,TRAZO,20,17+bob,2,3);
            rect(g,new Color(242,232,225),14,17+bob,1,1);
            rect(g,PIEL_SOMBRA,16,22+bob,5,2);
        }
        rect(g,TRAZO,9,5+bob,17,10);
        rect(g,GORRA,10,6+bob,15,7);
        rect(g,GORRA_LUZ,11,7+bob,11,3);
        rect(g,GORRA,7,13+bob,21,3);
        if(espalda)rect(g,new Color(236,212,195),15,10+bob,4,3);
    }
    private static void rect(Graphics2D g,Color c,int x,int y,int w,int h){
        g.setColor(c);g.fillRect(x,y,w,h);
    }
}
