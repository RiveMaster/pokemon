package Proyecto;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

/** Prueba que los PNG externos CC0 existen y se dibujan como tiles 32x32. */
public final class SpritesExternosSmokeTest {
    private SpritesExternosSmokeTest() { }

    public static void main(String[] args) {
        assert RecursosCC0.disponible() : "No se ha incorporado el pack gráfico CC0.";
        String[] biomas={"village","forest","desert","swamp","snow","dungeon",
                "castle","ruins","space_colony","research_lab","cyber_city"};
        for(String bioma:biomas) {
            BufferedImage atlas=RecursosCC0.cargar(
                    "environments/tilesets/"+bioma+".png");
            assert atlas!=null : "Falta el tileset: "+bioma;
            assert atlas.getWidth()==256 && atlas.getHeight()==128
                    : "El atlas no conserva la cuadrícula original de 8x4 tiles.";
        }
        for(String nombre:new String[]{"PUEBLO","BOSQUE","COSTA","PANTANO","CIUDAD"}) {
            BufferedImage suelo=RecursosCC0.terreno(nombre,"PIEDRA",2);
            BufferedImage camino=RecursosCC0.camino(nombre,2);
            assert suelo!=null && suelo.getWidth()==32 && suelo.getHeight()==32;
            assert camino!=null && camino.getWidth()==32 && camino.getHeight()==32;
        }
        for(String tipo:new String[]{"CENTRO_POKEMON","TIENDA","GIMNASIO",
                "CASA","CASINO","GOBIERNO"}) {
            BufferedImage edificio=RecursosCC0.edificio(tipo,7,6,0);
            assert edificio!=null && edificio.getWidth()==224
                    && edificio.getHeight()==192 : tipo;
            boolean visible=false;
            for(int y=0;y<edificio.getHeight()&&!visible;y++)
                for(int x=0;x<edificio.getWidth();x++)
                    if((edificio.getRGB(x,y)>>>24)!=0){visible=true;break;}
            assert visible : "Edificio transparente: "+tipo;
        }
        assert RecursosCC0.cargar("../secreto.png")==null;
        assert Files.isRegularFile(Path.of("Tiles","RPGPixelPack","README.md"));
        System.out.println("SpritesExternosSmokeTest: 11 atlas 32px y fachadas CC0 cargados OK");
    }
}
