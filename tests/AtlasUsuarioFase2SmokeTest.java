package Proyecto;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

/** Valida tres interiores y carga única de recursos del usuario sin ventanas. */
public final class AtlasUsuarioFase2SmokeTest {
    private AtlasUsuarioFase2SmokeTest(){}

    public static void main(String[] args) throws Exception {
        AtlasUsuario16 atlas=AtlasUsuario16.cargar();
        assert AtlasUsuario16.piezasInterior().size()==22
                : "Deben registrarse 22 recortes del tileset del usuario";
        for (String nombre:AtlasUsuario16.piezasInterior().keySet()) {
            BufferedImage sprite=atlas.spriteInterior(nombre);
            int[] esperado=AtlasUsuario16.piezasInterior().get(nombre);
            assert sprite.getWidth()==esperado[2] && sprite.getHeight()==esperado[3]
                    : "Dimensiones del recorte: "+nombre;
            assert sprite.getWidth()>0 && sprite.getHeight()>0;
        }

        for(InterioresAtlasUsuario.Tipo tipo:InterioresAtlasUsuario.Tipo.values()){
            InterioresAtlasUsuario.Interior m=InterioresAtlasUsuario.generar(tipo,atlas);
            assert m.puertaX()==9 && m.puertaY()==12;
            assert m.puedePisar(m.puertaX(),m.puertaY());
            assert m.puedePisar(m.puertaX(),m.puertaY()-1);
            assert !m.puedePisar(0,0);
            assert !m.puedePisar(17,6);
            assert m.salidaAlcanzable(): "Salida inaccesible de "+tipo;
            assert !m.muebles().isEmpty();
            boolean[][] alcanzables=explorar(m);
            int cnt=0;
            for(InterioresAtlasUsuario.Mueble mueble:m.muebles()){
                BufferedImage sprite=atlas.spriteInterior(mueble.sprite());
                int ancho=(sprite.getWidth()+15)/16,alto=(sprite.getHeight()+15)/16;
                boolean visto=false;
                for(int y=mueble.y();y<mueble.y()+alto;y++)
                    for(int x=mueble.x();x<mueble.x()+ancho;x++)
                        for(int[] d:new int[][]{{0,1},{1,0},{0,-1},{-1,0}}){
                            int tx=x+d[0],ty=y+d[1];
                            if(tx>=0&&ty>=0&&tx<18&&ty<13&&alcanzables[ty][tx]
                                    &&m.interactuar(tx,ty)!=null)visto=true;
                        }
                assert visto:"Mueble no accesible desde entrada en "+tipo+": "+mueble.sprite();
                cnt++;
            }
            assert cnt>=3:"Hay que decorar el interior";
            System.out.println(tipo+": "+cnt+" muebles accesibles y entrada/salida OK");
        }
        System.out.println("AtlasUsuarioFase2SmokeTest: 22 sprites y 3 interiores 100% atlas OK");
    }

    private static boolean[][] explorar(InterioresAtlasUsuario.Interior m){
        boolean[][] visto=new boolean[13][18];
        ArrayDeque<Point> cola=new ArrayDeque<>();
        cola.add(new Point(m.puertaX(),m.puertaY()-1));
        while(!cola.isEmpty()){
            Point p=cola.removeFirst();
            if(p.x<0||p.y<0||p.x>=18||p.y>=13||visto[p.y][p.x]
                    ||!m.puedePisar(p.x,p.y))continue;
            visto[p.y][p.x]=true;
            cola.add(new Point(p.x+1,p.y));
            cola.add(new Point(p.x-1,p.y));
            cola.add(new Point(p.x,p.y+1));
            cola.add(new Point(p.x,p.y-1));
        }
        return visto;
    }
}
