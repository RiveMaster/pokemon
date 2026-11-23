package Proyecto;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Transiciones originales de 32 px inspiradas en la organización de los
 * elementos de la referencia del usuario (NO se redistribuye esa imagen).
 *
 * Máscaras: N=1, E=2, S=4, O=8. El borde se dibuja solo cuando la casilla
 * adyacente no pertenece al mismo terreno. Las diagonales redondean esquinas.
 */
public final class TerrenosConVecinos32 {
    public static final int N=1,E=2,S=4,O=8;
    private TerrenosConVecinos32() { }

    public static int mascara(boolean norte,boolean este,boolean sur,boolean oeste) {
        return (norte?N:0)|(este?E:0)|(sur?S:0)|(oeste?O:0);
    }

    /** Claro de tierra: las cuatro esquinas se abren según los vecinos. */
    public static BufferedImage claro(ArteRegion2D.Paleta p,int mascara,int diagonales,
                                       int semilla) {
        BufferedImage imagen=ArteRegion2D.terreno("CESPED",p,semilla);
        Graphics2D g=imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        Color sombra=new Color(125,105,69),tierra=p.tierra();
        int n=(mascara&N)!=0?0:4, s=(mascara&S)!=0?32:28;
        int o=(mascara&O)!=0?0:4, e=(mascara&E)!=0?32:28;
        g.setColor(sombra);
        g.fillRect(o,n,e-o,s-n);
        g.setColor(tierra);
        g.fillRect(o+1,n+1,Math.max(1,e-o-2),Math.max(1,s-n-2));
        if((mascara&N)!=0){g.setColor(tierra);g.fillRect(o+2,0,e-o-4,4);}
        if((mascara&S)!=0){g.setColor(tierra);g.fillRect(o+2,28,e-o-4,4);}
        if((mascara&O)!=0){g.setColor(tierra);g.fillRect(0,n+2,4,s-n-4);}
        if((mascara&E)!=0){g.setColor(tierra);g.fillRect(28,n+2,4,s-n-4);}
        // Los rincones interiores diagonales impiden agujeros cuadrados.
        g.setColor(sombra);
        if((mascara&(N|O))==(N|O) && (diagonales&1)==0)g.fillRect(0,0,3,3);
        if((mascara&(N|E))==(N|E) && (diagonales&2)==0)g.fillRect(29,0,3,3);
        if((mascara&(S|O))==(S|O) && (diagonales&4)==0)g.fillRect(0,29,3,3);
        if((mascara&(S|E))==(S|E) && (diagonales&8)==0)g.fillRect(29,29,3,3);
        g.setColor(new Color(238,213,157));
        for(int i=0;i<9;i++){
            int x=5+Math.floorMod(i*13+semilla,22);
            int y=5+Math.floorMod(i*17+semilla/3,22);
            g.fillRect(x,y,2,1);
        }
        g.dispose();
        return imagen;
    }

    /**
     * Plataformas elevadas: la cima conserva la hierba y la parte inferior
     * añade pared de roca. Solo el lado sur muestra la pared vertical.
     */
    public static BufferedImage plataforma(ArteRegion2D.Paleta p,int mascara,int variante) {
        BufferedImage img=ArteRegion2D.terreno("CESPED",p,variante);
        Graphics2D g=img.createGraphics();
        Color sombra=new Color(75,66,53),roca=new Color(138,92,61);
        if((mascara&S)==0){
            g.setColor(sombra);g.fillRect(0,23,32,9);
            g.setColor(roca);g.fillRect(2,24,28,7);
            g.setColor(new Color(176,123,80));g.fillRect(4,24,21,2);
            for(int x=4;x<30;x+=7){
                g.setColor(new Color(108,72,54));g.fillRect(x,28,2,4);
            }
        }
        g.setColor(new Color(32,96,51));
        if((mascara&N)==0)g.fillRect(0,0,32,2);
        if((mascara&O)==0)g.fillRect(0,0,2,23);
        if((mascara&E)==0)g.fillRect(30,0,2,23);
        g.dispose();
        return img;
    }

    /** Pino original como respaldo, 32×64, con raíces en la casilla inferior. */
    public static BufferedImage pinoOriginal(int variante) {
        BufferedImage img=new BufferedImage(32,64,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=img.createGraphics();
        Color base=variante%2==0?new Color(57,128,72):new Color(60,105,90);
        g.setColor(new Color(105,68,48));g.fillRect(13,45,7,16);
        g.setColor(new Color(69,49,43));g.fillRect(16,48,3,13);
        for(int nivel=0;nivel<3;nivel++){
            int puntaY=4+nivel*13,ancho=11+nivel*3;
            int[] xs={16,16-ancho,16+ancho};
            int[] ys={puntaY,puntaY+22,puntaY+22};
            g.setColor(new Color(32,66,56));
            g.fillPolygon(xs,ys,3);
            g.setColor(base);g.fillPolygon(
                    new int[]{16,16-ancho+3,16+ancho-3},
                    new int[]{puntaY+2,puntaY+18,puntaY+18},3);
            g.setColor(new Color(110,176,94));
            g.fillRect(11-nivel, puntaY+11,5,2);
        }
        g.dispose();
        return img;
    }

    public static BufferedImage pino(int variante,boolean nieve) {
        String ruta=nieve?"tree_snowpine.png":"tree_pine.png";
        BufferedImage png=RecursosCC0.cargar("environments/props/"+ruta);
        return png!=null&&png.getWidth()==32&&png.getHeight()==64
                ?png:pinoOriginal(variante);
    }
}
