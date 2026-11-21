package Proyecto;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * Arte ORIGINAL de RPG de monstruos 2D: píxeles definidos, perfiles oscuros,
 * tres niveles de sombra y textura de 32x32 por casilla.
 * No incluye sprites, mapas ni fondos extraídos de juegos comerciales.
 */
public final class ArteRegion2D {
    public static final int PIXELES = 32;
    private ArteRegion2D() { }

    public record Paleta(Color verde, Color verdeClaro, Color verdeOscuro,
                         Color tierra, Color tierraOscura, Color agua,
                         Color arena, Color roca, Color muro, Color tejado,
                         Color acento, Color flores) { }

    /** Las diez generaciones tienen paletas reconocibles y diferenciadas. */
    public static Paleta paleta(int generacion, int escenario) {
        int g = Math.max(1, Math.min(10, generacion));
        Color[] vegetacion = {
                rgb(87, 164, 79), rgb(151, 161, 91), rgb(68, 171, 108),
                rgb(117, 156, 179), rgb(82, 158, 130), rgb(121, 184, 112),
                rgb(65, 178, 139), rgb(118, 162, 96), rgb(155, 171, 98),
                rgb(116, 133, 192)
        };
        Color[] acentos = {
                rgb(220, 65, 72), rgb(184, 110, 55), rgb(41, 159, 193),
                rgb(84, 123, 193), rgb(222, 165, 69), rgb(180, 107, 186),
                rgb(232, 119, 103), rgb(138, 91, 180), rgb(216, 128, 67),
                rgb(109, 226, 213)
        };
        Color v = vegetacion[g-1], a = acentos[g-1];
        Color tierra = switch (escenario) {
            case 2,3 -> rgb(237, 204, 142);
            case 4,5,8 -> rgb(168, 160, 145);
            case 6 -> rgb(117, 116, 91);
            default -> rgb(205, 177, 125);
        };
        Color agua = switch (g) {
            case 3,7 -> rgb(62, 175, 218);
            case 4 -> rgb(93, 166, 201);
            case 10 -> rgb(112, 121, 218);
            default -> rgb(67, 149, 206);
        };
        return new Paleta(v, aclarar(v, 33), oscurecer(v, 35),
                tierra, oscurecer(tierra, 47), agua,
                rgb(243, 215, 158), rgb(124, 130, 126),
                rgb(238, 225, 197), a, aclarar(a, 46),
                new Color[]{rgb(254, 208, 78),rgb(245, 148, 173),
                        rgb(160, 199, 255),rgb(255, 242, 200)}[(g+escenario)%4]);
    }

    public static BufferedImage terreno(String tipo, Paleta p, int variante) {
        BufferedImage im = new BufferedImage(PIXELES, PIXELES, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int seed = variante * 437 + tipo.hashCode() * 11;
        switch (tipo) {
            case "CESPED" -> hierba(g,p,seed,false);
            case "CESPED_OSCURO" -> hierba(g,p,seed,true);
            case "SENDA" -> { BufferedImage senda = camino(p,15,seed); g.drawImage(senda,0,0,null); }
            case "ARENA" -> suelo(g,p.arena,p.tierraOscura,seed);
            case "PIEDRA" -> suelo(g,aclarar(p.roca,29),p.roca,seed);
            case "NIEVE" -> suelo(g,rgb(226,238,241),rgb(175,202,215),seed);
            case "CUEVA" -> suelo(g,rgb(103,96,106),rgb(63,67,83),seed);
            case "AGUA" -> agua(g,p,seed);
            case "HIERBA_ALTA" -> { hierba(g,p,seed,true); matas(g,p,seed); }
            case "FLORES" -> { hierba(g,p,seed,false); flores(g,p,seed); }
            case "ARBOL" -> arbol(g,p,seed);
            case "ARBOL2" -> arbol(g,p,seed+9);
            case "ARBUSTO" -> arbusto(g,p,seed);
            case "ROCA" -> roca(g,p,seed);
            case "MURO" -> muro(g,p,seed);
            case "FAROLA" -> farola(g,p);
            case "CARTEL" -> cartel(g,p);
            case "VALLA" -> valla(g,p);
            case "COLUMNA" -> columna(g,p);
            case "PUENTE" -> puente(g,p);
            default -> hierba(g,p,seed,false);
        }
        g.dispose();
        return im;
    }

    public static BufferedImage camino(Paleta p, int mascara, int variante) {
        BufferedImage im = terreno("CESPED",p,variante);
        Graphics2D g = im.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // Bits: N=1, E=2, S=4, O=8. Bordes irregulares pero conectados.
        int l=(mascara&8)!=0?0:3, r=(mascara&2)!=0?32:29;
        int t=(mascara&1)!=0?0:3, b=(mascara&4)!=0?32:29;
        rect(g,p.tierraOscura,l,t,r-l,b-t);
        rect(g,p.tierra,l+2,t+2,Math.max(0,r-l-4),Math.max(0,b-t-4));
        if ((mascara&1)!=0) rect(g,p.tierra,l+2,0,r-l-4,5);
        if ((mascara&4)!=0) rect(g,p.tierra,l+2,b-5,r-l-4,5);
        if ((mascara&8)!=0) rect(g,p.tierra,0,t+2,5,b-t-4);
        if ((mascara&2)!=0) rect(g,p.tierra,r-5,t+2,5,b-t-4);
        for (int k=0;k<9;k++){
            int x=4+Math.floorMod(k*11+variante*7,24);
            int y=4+Math.floorMod(k*17+variante*3,24);
            rect(g,(k%2==0?aclarar(p.tierra,23):p.tierraOscura),x,y,k%3+2,1);
        }
        g.dispose();
        return im;
    }

    /** Fachadas completas, de tamaño natural 32 px por casilla, sin estirarlas. */
    public static BufferedImage edificio(String tipo, int anchoCasillas, int altoCasillas,
                                          Paleta p, int variante) {
        int w=anchoCasillas*PIXELES, h=altoCasillas*PIXELES;
        BufferedImage im = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=im.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        String clase=tipo==null?"CASA":tipo.toUpperCase(Locale.ROOT);
        Color tejado = switch(clase){
            case "CENTRO_POKEMON" -> rgb(210,60,75);
            case "TIENDA" -> rgb(42,136,193);
            case "GIMNASIO" -> rgb(107,94,166);
            case "GOBIERNO" -> rgb(86,127,123);
            case "CASINO" -> rgb(153,81,167);
            default -> switch(Math.floorMod(variante,4)){
                case 0 -> rgb(177,92,63); case 1 -> rgb(93,143,107);
                case 2 -> rgb(156,106,159); default -> rgb(172,130,72);
            };
        };
        Color fachada=clase.equals("GIMNASIO")?rgb(193,197,212):p.muro;
        Color perfil=rgb(48,56,74);
        // Sombra y zócalo en los últimos píxeles.
        rect(g,new Color(34,46,62,70),9,h-14,w-18,11);
        rect(g,perfil,6,31,w-12,h-39);
        rect(g,fachada,10,34,w-20,h-46);
        rect(g,oscurecer(fachada,31),10,h-24,w-20,9);
        rect(g,perfil,4,11,w-8,29);
        rect(g,oscurecer(tejado,45),7,8,w-14,29);
        rect(g,tejado,9,9,w-18,21);
        rect(g,aclarar(tejado,52),12,11,w-24,6);
        for(int y=19;y<31;y+=7) for(int x=13;x<w-12;x+=26)
            rect(g,oscurecer(tejado,19),x+(y%3),y,13,2);
        rect(g,perfil,5,30,w-10,6);
        rect(g,aclarar(tejado,33),8,29,w-16,3);
        // Edificios grandes: más columnas, ventanas y volumen.
        if(clase.equals("GIMNASIO")||clase.equals("GOBIERNO")){
            for(int x=17;x<w-16;x+=39){
                rect(g,perfil,x-2,47,17,h-76);
                rect(g,aclarar(fachada,17),x,49,13,h-79);
                rect(g,oscurecer(fachada,31),x+10,50,3,h-80);
            }
            rect(g,rgb(232,190,80),11,38,w-22,6);
        }
        int puertaW=clase.equals("CASA")||clase.equals("CASA_MAMA")?23:37;
        int px=w/2-puertaW/2;
        rect(g,perfil,px-3,h-50,puertaW+6,48);
        rect(g,rgb(87,120,139),px,h-47,puertaW,44);
        rect(g,rgb(146,203,207),px+3,h-43,puertaW-6,25);
        rect(g,rgb(230,247,237),px+6,h-40,5,19);
        if(puertaW>30)rect(g,perfil,px+puertaW/2,h-46,3,42);
        rect(g,rgb(237,192,94),px+puertaW-7,h-19,3,3);
        rect(g,rgb(220,213,186),px-6,h-5,puertaW+12,5);
        // Ventanas laterales, persianas y alféizares.
        for(int x=15;x<w-36;x+=40) {
            if (x+28>=px-3 && x<=px+puertaW+3) continue;
            int wy=h-73;
            rect(g,perfil,x-2,wy-3,30,26);
            rect(g,rgb(99,174,203),x,wy,26,20);
            rect(g,rgb(207,238,237),x+3,wy+2,5,13);
            rect(g,rgb(57,105,143),x+12,wy,3,20);
            rect(g,aclarar(fachada,22),x-5,wy+22,36,4);
        }
        // Emblemas originales identifican al servicio sin usar logos oficiales.
        if(!clase.startsWith("CASA")){
            int cx=w/2;
            rect(g,perfil,cx-22,12,44,29);
            rect(g,clase.equals("CENTRO_POKEMON")?rgb(251,247,240):tejado,cx-19,14,38,24);
            g.setColor(clase.equals("CENTRO_POKEMON")?rgb(208,61,73):rgb(250,228,154));
            if(clase.equals("CENTRO_POKEMON")){
                g.fillOval(cx-10,17,20,20);
                rect(g,perfil,cx-10,26,20,3);
                g.setColor(rgb(245,248,245));g.fillOval(cx-4,24,8,8);
            }else if(clase.equals("TIENDA")) {
                rect(g,rgb(251,229,143),cx-12,20,24,4);
                rect(g,rgb(251,229,143),cx-12,27,24,4);
            }else if(clase.equals("GIMNASIO")) {
                g.fillPolygon(new int[]{cx,cx+5,cx+15,cx+7,cx+9,cx,cx-9,cx-7,cx-15,cx-5},
                     new int[]{15,22,22,28,38,32,38,28,22,22},10);
            }else{
                rect(g,rgb(251,218,137),cx-6,20,12,13);
            }
        }else{
            rect(g,rgb(96,93,98),18,3,14,14);
            rect(g,rgb(207,198,172),21,5,8,11);
        }
        // Jardineras en los extremos de la vivienda.
        if(clase.startsWith("CASA")){
            for(int x:new int[]{12,w-23}){
                rect(g,rgb(172,91,66),x,h-17,11,13);
                rect(g,rgb(55,139,76),x+2,h-25,7,14);
                rect(g,p.flores,x+4,h-26,3,4);
            }
        }
        g.dispose();
        return im;
    }

    private static void hierba(Graphics2D g,Paleta p,int semilla,boolean oscura){
        Color base=oscura?p.verdeOscuro:p.verde;
        rect(g,base,0,0,32,32);
        for(int i=0;i<24;i++){
            int x=Math.floorMod(i*19+semilla,31), y=Math.floorMod(i*13+semilla/7,31);
            Color c=(i%4==0?p.verdeOscuro:p.verdeClaro);
            rect(g,c,x,y,2+i%3,1);
            if(i%3==0){rect(g,p.verdeOscuro,x+1,y-2,1,3);rect(g,p.verdeClaro,x+2,y-2,1,2);}
        }
    }
    private static void suelo(Graphics2D g,Color base,Color oscuro,int seed){
        rect(g,base,0,0,32,32);
        for(int i=0;i<18;i++){
            int x=Math.floorMod(i*13+seed,28),y=Math.floorMod(i*19+seed/3,30);
            rect(g,oscuro,x,y,2+i%3,1);
            if(i%6==0)rect(g,aclarar(base,21),x+3,y+2,3,2);
        }
    }
    private static void agua(Graphics2D g,Paleta p,int seed){
        rect(g,p.agua,0,0,32,32);
        rect(g,oscurecer(p.agua,16),0,28,32,4);
        for(int i=0;i<6;i++){
            int x=Math.floorMod(i*13+seed,25),y=3+Math.floorMod(i*11+seed/5,26);
            rect(g,aclarar(p.agua,65),x,y,7,2);
            rect(g,aclarar(p.agua,25),x+2,y+3,5,1);
        }
    }
    private static void matas(Graphics2D g,Paleta p,int seed){
        for(int i=0;i<6;i++){
            int x=2+Math.floorMod(i*13+seed,25),y=7+Math.floorMod(i*9+seed/3,19);
            rect(g,p.verdeOscuro,x,y,2,8);
            rect(g,p.verdeClaro,x-2,y+2,2,5);
            rect(g,p.verde,x+3,y+1,3,6);
        }
    }
    private static void flores(Graphics2D g,Paleta p,int seed){
        for(int i=0;i<5;i++){
            int x=4+Math.floorMod(i*19+seed,23),y=5+Math.floorMod(i*11+seed/4,22);
            rect(g,p.verdeOscuro,x+1,y+3,2,5);
            rect(g,p.flores,x,y,5,4);
            rect(g,rgb(248,235,159),x+2,y+1,1,2);
        }
    }
    private static void arbol(Graphics2D g,Paleta p,int seed){
        int eje=Math.floorMod(seed,4);
        rect(g,new Color(31,42,38,50),7,26,24,6);
        rect(g,rgb(118,78,53),14,20,6,12);
        rect(g,rgb(84,57,48),16,21,2,9);
        Color copa=mezclar(p.verdeOscuro,rgb(33,109,61),0.50f);
        rect(g,rgb(29,56,49),6+eje,8,22,17);
        rect(g,copa,4+eje,10,26,11);
        rect(g,copa,8,5,18,18);
        rect(g,p.verde,8,5,17,8);
        rect(g,p.verdeClaro,10,7,12,3);
        rect(g,oscurecer(copa,14),10,21,17,3);
        rect(g,p.verdeClaro,6+eje,11,6,4);
    }
    private static void arbusto(Graphics2D g,Paleta p,int seed){
        Color c=mezclar(p.verde,p.verdeOscuro,0.5f);
        rect(g,new Color(33,49,37,65),3,27,27,4);
        rect(g,p.verdeOscuro,2,16,28,13);
        rect(g,c,5,9,22,16);
        rect(g,p.verde,5,11,20,9);
        rect(g,p.verdeClaro,8,13,8,3);
        if(seed%3==0)rect(g,p.flores,20,14,3,3);
    }
    private static void roca(Graphics2D g,Paleta p,int seed){
        Color o=oscurecer(p.roca,54);
        rect(g,new Color(28,40,44,66),3,25,27,5);
        rect(g,o,4,13,24,14);
        rect(g,p.roca,8,7,18,18);
        rect(g,aclarar(p.roca,48),10,8,12,5);
        rect(g,oscurecer(p.roca,15),23,13,3,9);
        rect(g,aclarar(p.roca,14),8,18,11,3);
    }
    private static void muro(Graphics2D g,Paleta p,int seed){
        rect(g,rgb(65,70,78),0,0,32,32);
        rect(g,aclarar(p.roca,24),2,2,28,28);
        for(int y=3;y<32;y+=10){
            rect(g,p.roca,2,y+7,28,2);
            for(int x=(y%3==0?3:17);x<32;x+=16)
                rect(g,oscurecer(p.roca,27),x,y,2,7);
        }
    }
    private static void farola(Graphics2D g,Paleta p){
        rect(g,rgb(48,56,73),14,10,5,21);
        rect(g,rgb(48,56,73),9,7,16,4);
        rect(g,rgb(248,218,116),10,2,14,9);
        rect(g,rgb(253,248,192),12,3,8,5);
    }
    private static void cartel(Graphics2D g,Paleta p){
        rect(g,rgb(93,73,54),14,13,4,19);
        rect(g,rgb(58,61,69),4,6,24,14);
        rect(g,rgb(236,210,137),6,8,20,10);
        rect(g,rgb(92,125,82),9,12,14,3);
    }
    private static void valla(Graphics2D g,Paleta p){
        rect(g,rgb(100,75,61),1,8,30,4);
        rect(g,rgb(166,124,80),1,10,30,3);
        rect(g,rgb(100,75,61),1,21,30,3);
        for(int x=4;x<32;x+=14){
            rect(g,rgb(71,66,65),x,4,5,24);
            rect(g,rgb(204,163,105),x+1,6,3,17);
        }
    }
    private static void columna(Graphics2D g,Paleta p){
        rect(g,new Color(26,34,44,80),9,28,20,3);
        rect(g,rgb(70,72,92),8,5,18,26);
        rect(g,aclarar(p.roca,75),11,6,12,20);
        rect(g,aclarar(p.roca,105),13,7,4,17);
        rect(g,rgb(78,89,105),6,2,22,6);
        rect(g,rgb(85,83,94),7,26,20,5);
    }
    private static void puente(Graphics2D g,Paleta p){
        rect(g,rgb(85,80,71),0,0,32,32);
        for(int y=1;y<32;y+=7){
            rect(g,rgb(186,145,87),1,y,30,5);
            rect(g,rgb(230,195,120),2,y,27,2);
        }
        rect(g,rgb(67,67,80),0,0,4,32);
        rect(g,rgb(67,67,80),28,0,4,32);
    }
    private static Color rgb(int r,int g,int b){return new Color(r,g,b);}
    private static Color aclarar(Color c,int delta){
        return new Color(Math.min(255,c.getRed()+delta),Math.min(255,c.getGreen()+delta),
                Math.min(255,c.getBlue()+delta),c.getAlpha());
    }
    private static Color oscurecer(Color c,int delta){
        return new Color(Math.max(0,c.getRed()-delta),Math.max(0,c.getGreen()-delta),
                Math.max(0,c.getBlue()-delta),c.getAlpha());
    }
    private static Color mezclar(Color a,Color b,float t){
        return new Color((int)(a.getRed()*(1-t)+b.getRed()*t),
                (int)(a.getGreen()*(1-t)+b.getGreen()*t),
                (int)(a.getBlue()*(1-t)+b.getBlue()*t));
    }
    private static void rect(Graphics2D g,Color c,int x,int y,int w,int h){
        if(w<1||h<1)return;g.setColor(c);g.fillRect(x,y,w,h);
    }
}
