package Proyecto;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;

/**
 * Verifica 100 escenarios generados con arte de 32px, rutas transitables,
 * identificadores únicos y puertas accesibles desde el mapa.
 */
public final class GeneracionesMapasSmokeTest {
    private GeneracionesMapasSmokeTest() { }

    public static void main(String[] args) {
        Set<String> ids=new HashSet<>();
        Set<Long> huellas=new HashSet<>();
        int generados=0;
        for(Generacion gen:Generacion.values()){
            Set<String> biomas=new HashSet<>();
            for(CatalogoMapasGeneracionales.Mapa mapa:CatalogoMapasGeneracionales.listar(gen)){
                MapasRegion2D.Resultado escenario=MapasRegion2D.generar(mapa);
                generados++;
                assert ids.add(mapa.id()) : "ID repetido: " + mapa.id();
                assert biomas.add(mapa.bioma()) : "Bioma duplicado dentro de " + gen;
                assert escenario.ancho==36 && escenario.alto==24;
                assert escenario.tiles.length==escenario.tipos.length;
                for(BufferedImage tile:escenario.tiles) {
                    assert tile!=null && tile.getWidth()==32 && tile.getHeight()==32:
                        "Tile que no tiene 32px: " + mapa.id();
                }
                long huella=huellaVisual(escenario);
                assert huellas.add(huella):"Dos mapas parecen idénticos: " + mapa.id();
                boolean[] accesibles=recorrido(escenario);
                int cx=escenario.ancho/2,cy=escenario.alto/2;
                assert accesibles[cy*escenario.ancho] : "Salida izquierda aislada";
                assert accesibles[cy*escenario.ancho+escenario.ancho-1] :
                        "Salida derecha aislada";
                assert escenario.puertas.size()==(mapa.urbano()?8:0);
                for(MapasRegion2D.Puerta puerta:escenario.puertas) {
                    int index=puerta.y()*escenario.ancho+puerta.x();
                    assert accesibles[index] : "Puerta inaccesible en " + mapa.id()
                            + ": " + puerta.servicio();
                    assert !escenario.colisiones[index] && !escenario.manuales[index];
                }
            }
            assert biomas.size()==10 : "Cada generación debe tener diez ambientes";
        }
        assert generados==100 && ids.size()==100 && huellas.size()==100;
        assert CatalogoMapasGeneracionales.todos().size()==100 :
                "Los 100 identificadores originales no deben desaparecer";
        assert CatalogoMapasGeneracionales.todosConNuevos().size()==120;
        for (Generacion gen : Generacion.values()) {
            var nuevas = CatalogoMapasGeneracionales.nuevos(gen);
            assert nuevas.size()==2;
            for (CatalogoMapasGeneracionales.Mapa nueva:nuevas) {
                assert nueva.id().startsWith("NUEVO_");
                assert ids.add(nueva.id()) : "Mapa nuevo con ID repetido";
                MapasRegion2D.Resultado mapa=MapasRegion2D.generar(nueva);
                generados++;
                assert mapa.tiles.length==mapa.tipos.length;
                for(BufferedImage tile:mapa.tiles)
                    assert tile!=null && tile.getWidth()==32 && tile.getHeight()==32;
                long huella=huellaVisual(mapa);
                assert huellas.add(huella) :
                        "Escenario nuevo idéntico a otro: "+nueva.id();
                boolean[] accesibles=recorrido(mapa);
                int cy=mapa.alto/2;
                assert accesibles[cy*mapa.ancho];
                assert accesibles[cy*mapa.ancho+mapa.ancho-1];
                assert mapa.puertas.size()==(nueva.urbano()?8:0);
                for(MapasRegion2D.Puerta puerta:mapa.puertas) {
                    int index=puerta.y()*mapa.ancho+puerta.x();
                    assert accesibles[index] :
                            "Puerta nueva inaccesible: "+nueva.id()+" / "+puerta.servicio();
                    assert !mapa.colisiones[index] && !mapa.manuales[index];
                }
            }
        }
        assert generados==120 && ids.size()==120 && huellas.size()==120;
        BufferedImage f=EntrenadorRegion2D.fotograma(SpriteJugador2D.FRENTE,0);
        BufferedImage b=EntrenadorRegion2D.fotograma(SpriteJugador2D.ESPALDA,0);
        assert f.getWidth()==32 && f.getHeight()==48;
        assert !mismosPixeles(f,b):"El entrenador debe girar de verdad";
        assert !mismosPixeles(f,EntrenadorRegion2D.fotograma(SpriteJugador2D.FRENTE,1)):
            "Los tres pasos deben tener poses distintas";
        System.out.println("GeneracionesMapasSmokeTest: 120 mapas únicos con puertas y caminos transitables OK");
    }

    private static boolean[] recorrido(MapasRegion2D.Resultado m) {
        boolean[] visto=new boolean[m.ancho*m.alto];
        ArrayDeque<int[]> cola=new ArrayDeque<>();
        cola.add(new int[]{m.ancho/2,m.alto/2});
        while(!cola.isEmpty()){
            int[] p=cola.removeFirst(),xv={p[0]-1,p[0]+1,p[0],p[0]},
                    yv={p[1],p[1],p[1]-1,p[1]+1};
            int x=p[0],y=p[1];
            if(x<0||y<0||x>=m.ancho||y>=m.alto)continue;
            int i=y*m.ancho+x;
            if(visto[i]||m.colisiones[i]||m.manuales[i])continue;
            boolean bloqueado=false;
            for(int[] capa:m.capas){
                int t=capa[i];
                if(t>=0 && (m.tipos[t]==MapasRegion2D.BLOQUEO ||
                            m.tipos[t]==MapasRegion2D.AGUA))bloqueado=true;
            }
            if(bloqueado)continue;
            visto[i]=true;
            for(int k=0;k<4;k++)cola.add(new int[]{xv[k],yv[k]});
        }
        return visto;
    }

    private static long huellaVisual(MapasRegion2D.Resultado m) {
        CRC32 crc=new CRC32();
        for(int i=0;i<m.ancho*m.alto;i++){
            for(int[] capa:m.capas) {
                int t=capa[i];
                if(t>=0){
                    BufferedImage tile=m.tiles[t];
                    int argb=tile.getRGB(12,12);
                    crc.update(argb);crc.update(argb>>>8);crc.update(argb>>>16);
                    crc.update(argb>>>24);
                }else crc.update(255);
            }
        }
        return crc.getValue();
    }

    private static boolean mismosPixeles(BufferedImage a,BufferedImage b) {
        for(int y=0;y<a.getHeight();y++)for(int x=0;x<a.getWidth();x++)
            if(a.getRGB(x,y)!=b.getRGB(x,y))return false;
        return true;
    }
}
