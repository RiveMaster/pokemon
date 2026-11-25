package Proyecto;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Genera 100 escenarios originales y transitables en pixel-art nativo de 32px.
 *
 * Las coordenadas y colisiones siguen midiéndose en CASILLAS, nunca en píxeles.
 * Las salidas laterales de la ruta están libres y las puertas tienen una casilla
 * transitable; el renderizador muestra el arte a 2x sin reducir la resolución.
 */
public final class MapasRegion2D {
    public static final int ANCHO = 36;
    public static final int ALTO = 24;
    public static final byte NORMAL = 0, BLOQUEO = 1, HIERBA = 2, AGUA = 3;

    public record Puerta(int x, int y, String servicio) { }

    public static final class Resultado {
        public final int ancho, alto;
        public final BufferedImage[] tiles;
        public final byte[] tipos;
        public final int[][] capas;
        public final boolean[] colisiones, manuales, hierba;
        public final List<Puerta> puertas;
        public final String id, nombre, bioma;

        private Resultado(Estado e) {
            ancho=e.w; alto=e.h;
            tiles=e.paleta.toArray(new BufferedImage[0]);
            tipos=new byte[e.tipos.size()];
            for (int i=0;i<tipos.length;i++) tipos[i]=e.tipos.get(i);
            capas=e.capas;
            colisiones=e.colisiones;
            manuales=e.manuales;
            hierba=e.hierba;
            puertas=List.copyOf(e.puertas);
            id=e.mapa.id();
            nombre=e.mapa.nombre();
            bioma=e.mapa.bioma();
        }
    }

    private static final class Estado {
        final int w=ANCHO,h=ALTO;
        final int[][] capas=new int[3][w*h];
        final boolean[] colisiones=new boolean[w*h], manuales=new boolean[w*h], hierba=new boolean[w*h];
        final ArrayList<BufferedImage> paleta=new ArrayList<>();
        final ArrayList<Byte> tipos=new ArrayList<>();
        final ArrayList<Puerta> puertas=new ArrayList<>();
        final CatalogoMapasGeneracionales.Mapa mapa;
        final ArteRegion2D.Paleta tema;
        final Random rnd;
        int cesped, oscuro, claro, camino, tallo, flores, agua, arena, roca, cueva,
                arbol, arbol2, arbusto, columna, senal, lampara, puente, valla,
                nieve, piedra, suelo;
        Estado(CatalogoMapasGeneracionales.Mapa mapa) {
            this.mapa=mapa;
            this.tema=ArteRegion2D.paleta(mapa.generacion().getNumero(), mapa.indice());
            this.rnd=new Random(mapa.semilla());
            for(int[] c:capas)Arrays.fill(c,-1);
        }
        int tile(String tipo,int variante,byte categoria){
            AtlasCorregido atlas = AtlasCorregido.actual();
            BufferedImage sprite = atlas != null ? atlas.terreno(tipo,variante) : null;
            if (sprite == null) {
                // Compatibilidad con instalaciones que aún no tengan el ZIP.
                sprite = "CESPED".equals(tipo) ? null
                        : RecursosCC0.terreno(mapa.bioma(),tipo,variante);
            }
            paleta.add(sprite!=null?sprite:ArteRegion2D.terreno(tipo,tema,variante));
            tipos.add(categoria);
            return paleta.size()-1;
        }
        int camino(int mascara,int variante){
            AtlasCorregido atlas = AtlasCorregido.actual();
            BufferedImage sprite = atlas != null ? atlas.camino(mascara,variante)
                    : RecursosCC0.camino(mapa.bioma(),variante);
            paleta.add(sprite!=null?sprite:ArteRegion2D.camino(tema,mascara,variante));
            tipos.add(NORMAL);
            return paleta.size()-1;
        }
        int pos(int x,int y){return y*w+x;}
        boolean dentro(int x,int y){return x>=0&&x<w&&y>=0&&y<h;}
        void suelo(int x,int y,int t){
            if(dentro(x,y)&&capas[2][pos(x,y)]<0) capas[1][pos(x,y)]=t;
        }
        void sueloFondo(int x,int y,int t){
            if(dentro(x,y))capas[0][pos(x,y)]=t;
        }
        void solido(int x,int y,int t){
            if(dentro(x,y)){
                int i=pos(x,y);
                capas[2][i]=t;
                colisiones[i]=manuales[i]=true;
            }
        }
        void hierba(int x,int y){
            if(dentro(x,y)&&capas[2][pos(x,y)]<0){
                suelo(x,y,tallo);hierba[pos(x,y)]=true;
            }
        }
        void lago(int x,int y){
            if(dentro(x,y)&&capas[2][pos(x,y)]<0){
                suelo(x,y,agua);
                // El agua puede cruzarse solo con un Pokémon capaz de navegar.
                colisiones[pos(x,y)]=true;
            }
        }
        /** Sprite de árbol de 32x64 dividido en dos tiles sin estirar el PNG. */
        void pinoAlto(int x,int y,boolean nevado) {
            if(!dentro(x,y)||!dentro(x,y-1))return;
            int inferior=pos(x,y),superior=pos(x,y-1);
            if(capas[2][inferior]>=0||capas[2][superior]>=0
                    || colisiones[inferior]||colisiones[superior])return;
            AtlasCorregido atlas = AtlasCorregido.actual();
            BufferedImage img = null;
            if (atlas != null) {
                BufferedImage pino = atlas.sprite(nevado ? "Pino02"
                        : Math.floorMod(x+y,2)==0 ? "Pino01" : "Pino07");
                if (pino != null) {
                    img = new BufferedImage(32,64,BufferedImage.TYPE_INT_ARGB);
                    java.awt.Graphics2D g = img.createGraphics();
                    // Se conserva el tamaño original dentro de dos casillas,
                    // sin estirar ni recortar la silueta del usuario.
                    g.drawImage(pino, (32-pino.getWidth())/2,64-pino.getHeight(),null);
                    g.dispose();
                }
            }
            if (img == null) img=TerrenosConVecinos32.pino(x+y*3,nevado);
            for(int dy=0;dy<2;dy++){
                BufferedImage trozo=img.getSubimage(0,dy*32,32,32);
                int id=paleta.size();
                paleta.add(trozo);
                tipos.add(BLOQUEO);
                int indice=pos(x,y-1+dy);
                capas[2][indice]=id;
                colisiones[indice]=manuales[indice]=true;
            }
        }

        /**
         * Árbol completo de 2x2 casillas con un único sprite real del atlas.
         * Los cuatro cuadrantes comparten el mismo lienzo para evitar cortes
         * y sus cuatro colisiones se activan juntas.
         */
        void arbolGrande(int x,int y,int variante) {
            AtlasCorregido atlas=AtlasCorregido.actual();
            if(atlas==null || !dentro(x,y)||!dentro(x+1,y+1))return;
            for(int dy=0;dy<2;dy++)for(int dx=0;dx<2;dx++) {
                int p=pos(x+dx,y+dy);
                if(colisiones[p]||capas[2][p]>=0) return;
            }
            BufferedImage origen=atlas.sprite("ArbolAlto0"+(1+Math.floorMod(variante,4)));
            if(origen==null)origen=atlas.sprite(
                    Math.floorMod(variante,2)==0?"Pino01":"Pino07");
            if(origen==null)return;
            BufferedImage lienzo=new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g=lienzo.createGraphics();
            g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                    java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int escala=origen.getWidth()*2<=64 && origen.getHeight()*2<=64 ? 2 : 1;
            int ww=origen.getWidth()*escala,hh=origen.getHeight()*escala;
            g.drawImage(origen,(64-ww)/2,64-hh,ww,hh,null);
            g.dispose();
            for(int dy=0;dy<2;dy++)for(int dx=0;dx<2;dx++) {
                BufferedImage trozo=lienzo.getSubimage(dx*32,dy*32,32,32);
                int id=paleta.size(),p=pos(x+dx,y+dy);
                paleta.add(trozo);
                tipos.add(BLOQUEO);
                capas[2][p]=id;
                colisiones[p]=manuales[p]=true;
            }
        }

        /** Claro irregular: cada tile examina sus cuatro vecinos y diagonales. */
        void claro(int x,int y,int ancho,int alto,int variante) {
            for(int dy=0;dy<alto;dy++)for(int dx=0;dx<ancho;dx++){
                int tx=x+dx,ty=y+dy;
                if(!dentro(tx,ty) || capas[2][pos(tx,ty)]>=0 || colisiones[pos(tx,ty)])
                    continue;
                int m=TerrenosConVecinos32.mascara(
                        dy>0,dx<ancho-1,dy<alto-1,dx>0);
                int d=(dx>0&&dy>0?1:0)|(dx<ancho-1&&dy>0?2:0)
                        |(dx>0&&dy<alto-1?4:0)
                        |(dx<ancho-1&&dy<alto-1?8:0);
                AtlasCorregido atlas=AtlasCorregido.actual();
                BufferedImage tile=atlas!=null ? atlas.terreno("CLARO",variante+dx+dy*17)
                        : TerrenosConVecinos32.claro(tema,m,d,variante+dx+dy*17);
                int id=paleta.size();
                paleta.add(tile);
                tipos.add(NORMAL);
                capas[1][pos(tx,ty)]=id;
            }
        }

        /** Plataforma rocosa con borde frontal; bloquea por altura. */
        void meseta(int x,int y,int ancho,int alto,int variante) {
            for(int dy=0;dy<alto;dy++)for(int dx=0;dx<ancho;dx++){
                int tx=x+dx,ty=y+dy;
                if(!dentro(tx,ty)||capas[2][pos(tx,ty)]>=0)continue;
                int m=TerrenosConVecinos32.mascara(
                        dy>0,dx<ancho-1,dy<alto-1,dx>0);
                AtlasCorregido atlas=AtlasCorregido.actual();
                BufferedImage tile=atlas!=null ? atlas.terreno("PIEDRA",variante+dx+dy*7)
                        : TerrenosConVecinos32.plataforma(tema,m,variante+dx+dy*7);
                int id=paleta.size();
                paleta.add(tile);
                tipos.add(BLOQUEO);
                capas[2][pos(tx,ty)]=id;
                colisiones[pos(tx,ty)]=manuales[pos(tx,ty)]=true;
            }
        }

        void dejarPaso(int x,int y) {
            if(dentro(x,y)){
                int i=pos(x,y);
                capas[2][i]=-1;
                capas[1][i]=camino;
                colisiones[i]=manuales[i]=hierba[i]=false;
            }
        }
    }

    private MapasRegion2D() { }

    public static Resultado generar(CatalogoMapasGeneracionales.Mapa mapa) {
        return generar(mapa,null);
    }

    /**
     * El parámetro ubicación solo se usa para añadir servicios reales del
     * recorrido principal; los 100 mapas del atlas no modifican la aventura.
     */
    public static Resultado generar(CatalogoMapasGeneracionales.Mapa mapa, Ubicacion ubicacion) {
        if(mapa==null)throw new IllegalArgumentException("Mapa regional requerido");
        Estado e=new Estado(mapa);
        int variacion=mapa.generacion().getNumero()*7+mapa.indice()*13;
        e.cesped=e.tile("CESPED",variacion,NORMAL);
        e.oscuro=e.tile("CESPED_OSCURO",variacion+1,NORMAL);
        e.claro=e.tile("CESPED",variacion+2,NORMAL);
        e.camino=e.camino(15,variacion);
        e.tallo=e.tile("HIERBA_ALTA",variacion+3,HIERBA);
        e.flores=e.tile("FLORES",variacion+4,NORMAL);
        e.agua=e.tile("AGUA",variacion+5,AGUA);
        e.arena=e.tile("ARENA",variacion+6,NORMAL);
        e.roca=e.tile("ROCA",variacion+7,BLOQUEO);
        e.cueva=e.tile("CUEVA",variacion+8,NORMAL);
        e.arbol=e.tile("ARBOL",variacion+9,BLOQUEO);
        e.arbol2=e.tile("ARBOL2",variacion+10,BLOQUEO);
        e.arbusto=e.tile("ARBUSTO",variacion+11,BLOQUEO);
        e.columna=e.tile("COLUMNA",variacion+12,BLOQUEO);
        e.senal=e.tile("CARTEL",variacion+13,BLOQUEO);
        e.lampara=e.tile("FAROLA",variacion+14,BLOQUEO);
        e.puente=e.tile("PUENTE",variacion+15,NORMAL);
        e.valla=e.tile("VALLA",variacion+16,BLOQUEO);
        e.nieve=e.tile("NIEVE",variacion+17,NORMAL);
        e.piedra=e.tile("PIEDRA",variacion+18,NORMAL);
        e.suelo=e.tile("SENDA",variacion+19,NORMAL);
        Arrays.fill(e.capas[0],e.cesped);
        for(int y=1;y<e.h-1;y++)for(int x=1;x<e.w-1;x++){
            long n=(long)x*73856093L ^ (long)y*19349663L ^ mapa.semilla()*83492791L;
            int v=(int)Math.floorMod(n,17);
            if(v==0||v==1)e.sueloFondo(x,y,e.oscuro);
            else if(v==2)e.sueloFondo(x,y,e.claro);
        }

        switch(mapa.bioma()) {
            case "PUEBLO","CIUDAD" -> ciudad(e,ubicacion);
            case "BOSQUE" -> {
                if (mapa.id().startsWith("NUEVO_")) bosqueNuevo(e);
                else bosque(e);
            }
            case "COSTA" -> costa(e);
            case "MONTANA" -> montana(e);
            case "LAGO" -> lagos(e);
            case "RUINAS" -> ruinas(e);
            case "PANTANO" -> pantano(e);
            case "CUEVA" -> cueva(e);
            case "JARDIN" -> jardin(e);
            default -> bosque(e);
        }

        // Eje central obligatorio para cruzar el mapa; no hace invisibles
        // los elementos que están por encima o por debajo de él.
        int cy=e.h/2;
        // Las nuevas sendas forestales tienen un sendero de UNA casilla,
        // próximo al ritmo y escala de las rutas clásicas de Game Boy.
        // Los mapas existentes mantienen sus tres carriles para no alterar
        // las reglas ni los accesos que utilizan las partidas guardadas.
        int radioPaso=mapa.id().startsWith("NUEVO_") && !mapa.urbano()?0:1;
        for(int x=0;x<e.w;x++)
            for(int y=cy-radioPaso;y<=cy+radioPaso;y++) e.dejarPaso(x,y);
        if(!mapa.urbano() && !mapa.bioma().equals("JARDIN")){
            // Senderos con textura natural y cruces suaves.
            for(int x=0;x<e.w;x++)
                if(x%5==0) e.suelo(x,cy+2,e.piedra);
        }
        for(int x=0;x<e.w;x++){
            e.solido(x,0,e.arbol);
            e.solido(x,e.h-1,e.arbol2);
        }
        for(int y=1;y<e.h-1;y++){
            if(Math.abs(y-cy)<=1)continue;
            e.solido(0,y,e.arbol);
            e.solido(e.w-1,y,e.arbol2);
        }
        for(int y=cy-radioPaso;y<=cy+radioPaso;y++){
            e.dejarPaso(0,y);e.dejarPaso(e.w-1,y);
        }
        return new Resultado(e);
    }

    private static void ciudad(Estado e,Ubicacion ubicacion){
        boolean grande=e.mapa.bioma().equals("CIUDAD");
        int cy=e.h/2;
        for(int x=0;x<e.w;x++)for(int y=cy-2;y<=cy+2;y++) e.suelo(x,y,e.piedra);
        for(int y=1;y<e.h-1;y++){
            for(int x=17;x<=19;x++)e.suelo(x,y,e.piedra);
        }
        for(int y:new int[]{8,21})for(int x=1;x<e.w-1;x++)e.suelo(x,y,e.piedra);
        // Tres planos urbanos distintos para las localidades antiguas; la
        // Villa nueva utiliza un cuarto plano con plazas y fachadas singulares.
        int forma = e.mapa.id().startsWith("NUEVO_") ? 3
                : Math.floorMod(e.mapa.generacion().getNumero()+e.mapa.indice()*7,3);
        int[][] planosX={
            {2,10,21,29,2,11,21,29},
            {3,11,20,29,3,11,20,29},
            {2,11,21,29,3,10,20,29},
            {2,10,21,29,2,10,21,29}
        };
        int[] bx=planosX[forma];
        // Fachadas de 6-7 casillas y siete de altura: tamaño suficiente
        // para que la mayoría de sprites lleguen a x4 sin deformarse.
        // Se deja una calle al sur de cada fila de edificios: y=8 y y=21.
        int[] by={1,1,1,1,14,14,14,14};
        int[] ancho={7,6,7,6,7,6,7,6};
        int[] alto={7,7,7,7,7,7,7,7};
        String[] edificios={"CENTRO_POKEMON","TIENDA",
                grande?"GIMNASIO":"CASA","CASA",
                "CASA","CASA","CASA","CASA"};
        if(ubicacion!=null){
            // El gimnasio y los servicios conservan las reglas de la ciudad.
            if(Gimnasio.deCiudad(ubicacion)==null) edificios[2]="CASA";
            if(ubicacion==Ubicacion.COMILLAS)edificios[2]="CASINO";
            if(ubicacion==Ubicacion.VITORIA)edificios[6]="GOBIERNO";
            if(ubicacion==Ubicacion.VILLAVERDE)edificios[4]="CASA_MAMA";
        }else{
            // Atlas libre: un pueblo tiene casas, una ciudad tiene gimnasio.
            if(!grande)edificios[2]="CASA";
        }
        for(int i=0;i<bx.length;i++){
            // Distintas cubiertas y colores según localidad; el servicio
            // real asociado a la puerta no cambia.
            int decoracion = i + e.mapa.indice()*11
                    + e.mapa.generacion().getNumero()*7;
            edificio(e,bx[i],by[i],ancho[i],alto[i],edificios[i],decoracion);
            int puertaX=bx[i]+ancho[i]/2;
            int desde=by[i]+alto[i];
            int hasta=by[i]<10 ? 8 : 21;
            for(int y=desde;y<=hasta;y++)
                if(e.dentro(puertaX,y)&&e.capas[2][e.pos(puertaX,y)]<0)
                    e.suelo(puertaX,y,e.piedra);
        }
        for(int x=7;x<10;x++){
            e.suelo(x,10,e.flores);
            e.suelo(x,14,e.flores);
        }
        for(int x=25;x<28;x++){
            e.suelo(x,10,e.flores);
            e.suelo(x,14,e.flores);
        }
        // Las decoraciones nunca deben reemplazar un cuadrante de fachada
        // ni ocultar la puerta de un edificio de siete casillas de alto.
        for(int[] p:new int[][]{{9,9},{25,9},{9,15},{25,15}})
            if(e.capas[2][e.pos(p[0],p[1])]<0)
                e.solido(p[0],p[1],grande?e.lampara:e.arbusto);
        if(e.capas[2][e.pos(15,9)]<0)e.solido(15,9,e.senal);
        if(e.capas[2][e.pos(21,15)]<0)e.solido(21,15,e.lampara);

        // Jardines laterales que no pisan los portales ni el corredor central.
        for(int x=4;x<e.w-4;x+=3) {
            if(x>=16&&x<=20)continue;
            for(int y:new int[]{9,15}) {
                if(e.capas[2][e.pos(x,y)]<0
                        && (x+e.mapa.indice())%4!=0)
                    e.suelo(x,y,e.flores);
            }
        }
        if(forma==3) {
            e.arbolGrande(8,9,e.mapa.indice());
            e.arbolGrande(26,15,e.mapa.generacion().getNumero());
        }

        if (forma==3) {
            // Plaza española rodeada de una vía de 3 casillas de anchura;
            // mosaicos y arriates quedan fuera del eje obligatorio.
            for(int x=13;x<=22;x++)
                for(int y=10;y<=14;y++)
                    e.suelo(x,y,e.piedra);
            for(int[] p:new int[][]{{13,9},{22,9},{13,15},{22,15}}) {
                if(e.capas[2][e.pos(p[0],p[1])]<0)
                    e.suelo(p[0],p[1],e.flores);
            }
        } else if (forma==1) {
            // Calle lateral y bordes ajardinados.
            for(int y=8;y<=21;y++) {
                if(e.dentro(8,y)&&e.capas[2][e.pos(8,y)]<0)
                    e.suelo(8,y,e.camino);
                if(e.dentro(27,y)&&e.capas[2][e.pos(27,y)]<0)
                    e.suelo(27,y,e.camino);
            }
        } else if (forma==2) {
            // Avenida pavimentada en torno al cruce.
            for(int x=11;x<=25;x++)
                for(int y:new int[]{10,14})
                    if(e.capas[2][e.pos(x,y)]<0) e.suelo(x,y,e.arena);
        }
    }

    private static void bosqueNuevo(Estado e) {
        // Senderos arbolados completamente nuevos, con claros en diagonal,
        // dos zonas de hierba alta y un camino secundario norte-sur.
        int cy=e.h/2, variante=e.mapa.generacion().getNumero();
        e.claro(3,3,8,6,variante*11);
        e.claro(23,15,9,6,variante*19);
        for(int x=3;x<e.w-3;x++) {
            e.suelo(x,7,e.camino);
            e.suelo(x,18,e.camino);
        }
        for(int y=3;y<e.h-3;y++)
            for(int x=17;x<=18;x++) e.suelo(x,y,e.camino);
        for(int x=4;x<e.w-4;x+=3)
            for(int y:new int[]{4,5,19,20}) {
                if((x+y+variante)%3==0) e.solido(x,y,e.arbusto);
                else if(y<6 && x%2==0 && x!=17 && x!=18)
                    e.pinoAlto(x,y,false);
            }
        for(int x=4;x<e.w-4;x++) {
            if(x%3!=0) {
                e.hierba(x,9);
                e.hierba(x,16);
            }
        }
        for(int[] p:new int[][]{{6,7},{12,7},{24,18},{30,18}})
            e.suelo(p[0],p[1],e.flores);
        // Pinares completos de dos por dos. No ocupan la avenida central,
        // el sendero inferior ni la conexión norte-sur.
        int[][] bosquetes={
            {3,3},{9,3},{24,3},{30,3},
            {3,20},{10,20},{24,20},{30,20}
        };
        for(int k=0;k<bosquetes.length;k++)
            e.arbolGrande(bosquetes[k][0],bosquetes[k][1],variante+k);
        for(int k=0;k<42;k++) {
            int x=2+e.rnd.nextInt(e.w-4),y=2+e.rnd.nextInt(e.h-4);
            if(Math.abs(y-cy)<=3 || y==7 || y==18 || x==17 || x==18)continue;
            if(e.capas[2][e.pos(x,y)]>=0)continue;
            if(k%5==0)e.solido(x,y,e.arbol);
            else if(k%3==0)e.solido(x,y,e.arbusto);
            else e.suelo(x,y,e.flores);
        }
        // El eje central se restablece al final de generar(), de modo que
        // ambos extremos siempre son accesibles en todas las generaciones.
    }

    private static void edificio(Estado e,int x,int y,int w,int h,String clase,int variante) {
        AtlasCorregido atlas = AtlasCorregido.actual();
        BufferedImage fachada = atlas != null
                ? atlas.edificio(clase,w,h,variante+e.mapa.generacion().getNumero())
                : null;
        if(fachada==null) fachada=RecursosCC0.edificio(clase,w,h,
                variante+e.mapa.generacion().getNumero());
        if(fachada==null)
            fachada=ArteRegion2D.edificio(clase,w,h,e.tema,
                    variante+e.mapa.generacion().getNumero());
        int cx=x+w/2;
        for(int dy=0;dy<h;dy++)for(int dx=0;dx<w;dx++){
            int tx=x+dx,ty=y+dy;
            if(!e.dentro(tx,ty))continue;
            BufferedImage recorte=fachada.getSubimage(dx*ArteRegion2D.PIXELES,
                    dy*ArteRegion2D.PIXELES,ArteRegion2D.PIXELES,ArteRegion2D.PIXELES);
            boolean puerta=dy==h-1&&tx==cx;
            // El sprite corregido es más pequeño que el solar. Los tiles
            // totalmente transparentes no son muros invisibles.
            if (atlas != null && !puerta) {
                boolean visible=false;
                for(int py=0;py<32 && !visible;py++)
                    for(int px=0;px<32;px++)
                        if((recorte.getRGB(px,py)>>>24)>=128) {
                            visible=true;
                            break;
                        }
                if(!visible)continue;
            }
            int id=e.paleta.size();
            e.paleta.add(recorte);
            e.tipos.add(puerta?NORMAL:BLOQUEO);
            int indice=e.pos(tx,ty);
            e.capas[2][indice]=id;
            e.colisiones[indice]=e.manuales[indice]=!puerta;
        }
        e.puertas.add(new Puerta(cx,y+h-1,clase));
    }

    private static void bosque(Estado e){
        // Primero se colocan claros y una cornisa en espacios seguros.
        // Después llegan los árboles: sus copas no cortan las salidas.
        e.claro(3,3,5,4,11+e.mapa.indice());
        e.claro(23,16,6,4,19+e.mapa.generacion().getNumero());
        e.meseta(20,3,4,3,e.mapa.indice());
        for(int i=0;i<115;i++){
            int x=1+e.rnd.nextInt(e.w-2),y=1+e.rnd.nextInt(e.h-2);
            if(Math.abs(y-e.h/2)<=3)continue;
            if(i%6==0)e.hierba(x,y);
            else if(i%4==0)e.solido(x,y,e.arbusto);
            else if(i%3==0 && y>=3)e.pinoAlto(x,y,false);
            else e.solido(x,y,i%2==0?e.arbol:e.arbol2);
        }
        for(int i=0;i<30;i++){
            int x=2+e.rnd.nextInt(e.w-4),y=2+e.rnd.nextInt(e.h-4);
            if(Math.abs(y-e.h/2)>3)e.hierba(x,y);
        }
    }

    private static void costa(Estado e){
        for(int y=1;y<e.h-1;y++)for(int x=1;x<e.w-1;x++){
            if(y<8||y>17){
                e.sueloFondo(x,y,e.arena);
                if(y<5||y>20)e.lago(x,y);
                else if((x+y)%11==0)e.suelo(x,y,e.piedra);
            }
        }
        for(int x=3;x<e.w-2;x+=5){
            e.suelo(x,8,e.flores);
            e.suelo(x,16,e.arena);
        }
        for(int x=8;x<e.w-5;x+=9) {
            e.solido(x,9,e.arbusto);
            e.solido(x+2,16,e.roca);
        }
    }

    private static void montana(Estado e){
        for(int y=1;y<e.h-1;y++)for(int x=1;x<e.w-1;x++){
            int d=Math.abs(y-e.h/2);
            e.sueloFondo(x,y,d>6?e.nieve:e.piedra);
            if(d>3&&e.rnd.nextInt(5)==0)e.solido(x,y,e.roca);
            else if(d>4&&e.rnd.nextInt(9)==0 && y>2)e.pinoAlto(x,y,true);
            else if(d>4&&e.rnd.nextInt(7)==0)e.solido(x,y,e.columna);
        }
        for(int i=0;i<18;i++){
            int x=2+e.rnd.nextInt(e.w-4),y=2+e.rnd.nextInt(e.h-4);
            if(Math.abs(y-12)>3)e.suelo(x,y,e.cueva);
        }
    }

    private static void lagos(Estado e){
        for(int[] centro:new int[][]{{9,7},{27,17}}){
            for(int y=1;y<e.h-1;y++)for(int x=1;x<e.w-1;x++){
                double dx=(x-centro[0])/6.8,dy=(y-centro[1])/4.2;
                if(dx*dx+dy*dy<1)e.lago(x,y);
                else if(dx*dx+dy*dy<1.6)e.suelo(x,y,e.arena);
            }
        }
        for(int i=0;i<28;i++){
            int x=2+e.rnd.nextInt(e.w-4),y=2+e.rnd.nextInt(e.h-4);
            if(Math.abs(y-12)>3&&e.capas[1][e.pos(x,y)]!=e.agua){
                if(i%4==0)e.solido(x,y,e.arbol);
                else e.hierba(x,y);
            }
        }
    }

    private static void ruinas(Estado e){
        for(int y=2;y<e.h-2;y++)for(int x=3;x<e.w-3;x++)
            e.sueloFondo(x,y,e.piedra);
        for(int x=5;x<e.w-4;x+=6){
            e.solido(x,5,e.columna);
            e.solido(x,19,e.columna);
        }
        for(int y=3;y<e.h-3;y+=5){
            e.solido(4,y,e.valla);
            e.solido(e.w-5,y,e.valla);
        }
        for(int x=12;x<25;x++)for(int y=9;y<16;y++)
            e.suelo(x,y,e.piedra);
        e.solido(18,8,e.senal);
    }

    private static void pantano(Estado e){
        for(int y=1;y<e.h-1;y++)for(int x=1;x<e.w-1;x++){
            int n=Math.floorMod(x*7+y*13+(int)e.mapa.semilla(),23);
            if((y<9||y>15)&&n<11)e.lago(x,y);
            else if(n<15&&Math.abs(y-12)>2)e.hierba(x,y);
        }
        for(int i=0;i<24;i++){
            int x=1+e.rnd.nextInt(e.w-2),y=1+e.rnd.nextInt(e.h-2);
            if(Math.abs(y-12)>3) e.solido(x,y,e.arbusto);
        }
        for(int x=5;x<e.w-5;x+=8){
            e.suelo(x,9,e.puente);
            e.suelo(x,16,e.puente);
        }
    }

    private static void cueva(Estado e){
        for(int y=1;y<e.h-1;y++)for(int x=1;x<e.w-1;x++){
            e.sueloFondo(x,y,e.cueva);
            if(Math.abs(y-12)>4&&e.rnd.nextInt(7)==0)
                e.solido(x,y,e.roca);
            else if(Math.abs(y-12)>4&&e.rnd.nextInt(13)==0)
                e.solido(x,y,e.columna);
        }
        for(int x=4;x<e.w-3;x+=7){
            e.suelo(x,8,e.piedra);
            e.suelo(x,17,e.piedra);
        }
    }

    private static void jardin(Estado e){
        for(int y=3;y<e.h-3;y++)for(int x=3;x<e.w-3;x++)
            e.sueloFondo(x,y,e.claro);
        for(int x=4;x<e.w-4;x++){
            e.suelo(x,9,e.piedra);
            e.suelo(x,16,e.piedra);
        }
        for(int x=4;x<e.w-4;x+=4)
            for(int y:new int[]{6,7,17,18}) e.suelo(x,y,e.flores);
        for(int x=5;x<e.w-4;x+=7){
            e.solido(x,5,e.arbusto);
            e.solido(x,19,e.arbusto);
        }
        for(int y=2;y<e.h-2;y++)
            if(y<9||y>15)e.suelo(17,y,e.agua);
        e.suelo(17,8,e.puente);
        e.suelo(17,16,e.puente);
    }
}
