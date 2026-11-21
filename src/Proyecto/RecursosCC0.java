package Proyecto;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Recursos externos 32x32 de JaiGod/rpg-pixel-pack.
 * Arte CC0-1.0, fuente: https://github.com/JaiGod/rpg-pixel-pack
 *
 * Los originales se cargan con sus dimensiones NATIVAS. Si faltan recursos
 * no se detiene el juego: se mantiene el arte local programático.
 */
public final class RecursosCC0 {
    private static final Path RAIZ=Path.of("Tiles","RPGPixelPack");
    private static final Map<String, BufferedImage> CACHE=new ConcurrentHashMap<>();
    private static final String[] BIOMAS={"village","forest","desert","swamp","snow",
            "dungeon","castle","ruins","space_colony","research_lab","cyber_city"};
    private RecursosCC0() { }

    public static String fuente() { return "https://github.com/JaiGod/rpg-pixel-pack"; }

    public static BufferedImage cargar(String ruta) {
        if(ruta==null || ruta.contains("..") || ruta.startsWith("/") || ruta.contains("\\"))
            return null;
        BufferedImage enCache=CACHE.get(ruta);
        if(enCache!=null)return enCache;
        Path p=RAIZ.resolve(ruta).normalize();
        if(!p.startsWith(RAIZ) || !Files.isRegularFile(p))return null;
        try{
            BufferedImage im=ImageIO.read(p.toFile());
            if(im==null||im.getWidth()>4096||im.getHeight()>4096)return null;
            CACHE.put(ruta,im);
            return im;
        }catch(IOException e){
            System.err.println("No se pudo cargar el sprite CC0: "+ruta);
            return null;
        }
    }

    public static String bioma(String ambiente) {
        return switch(ambiente==null?"":ambiente.toUpperCase(java.util.Locale.ROOT)){
            case "BOSQUE","JARDIN" -> "forest";
            case "COSTA" -> "desert";
            case "MONTANA" -> "snow";
            case "LAGO" -> "village";
            case "RUINAS" -> "ruins";
            case "PANTANO" -> "swamp";
            case "CUEVA" -> "dungeon";
            case "CIUDAD" -> "castle";
            default -> "village";
        };
    }

    /** Toma exactamente una celda 32x32 del PNG 8x4 del bioma. */
    public static BufferedImage terreno(String ambiente,String tipo,int variante) {
        BufferedImage atlas=cargar("environments/tilesets/"+bioma(ambiente)+".png");
        if(atlas==null||atlas.getWidth()<256||atlas.getHeight()<128)return null;
        int col,fila;
        switch(tipo){
            case "CESPED","CESPED_OSCURO","FLORES" -> {
                fila=0;col=Math.floorMod(variante,8);
            }
            case "ARENA","PIEDRA","SENDA","CUEVA","NIEVE" -> {
                fila=3;col=Math.floorMod(variante,4);
            }
            case "ARBOL","ARBOL2","ARBUSTO","ROCA","MURO","VALLA","COLUMNA" -> {
                fila=3;col=4+Math.floorMod(variante,4);
            }
            default -> {return null;} // agua y hierba de encuentro conservan sus reglas.
        }
        BufferedImage tile=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=tile.createGraphics();
        g.drawImage(atlas,0,0,32,32,col*32,fila*32,(col+1)*32,(fila+1)*32,null);
        g.dispose();
        return tile;
    }

    public static BufferedImage camino(String ambiente,int variante) {
        BufferedImage im=cargar("environments/tilesets/"+bioma(ambiente)+".png");
        if(im==null||im.getWidth()<256||im.getHeight()<128)return null;
        BufferedImage tile=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=tile.createGraphics();
        int col=Math.floorMod(variante,4);
        g.drawImage(im,0,0,32,32,col*32,96,(col+1)*32,128,null);
        g.dispose();
        return tile;
    }

    /**
     * Los edificios se centran sin estirar los píxeles. Si la imagen original
     * es mayor que el solar, se mantiene el generador del proyecto.
     */
    public static BufferedImage edificio(String clase,int ancho,int alto,int variante) {
        String ruta=switch(clase){
            case "CENTRO_POKEMON" -> "buildings/shops/shop_potion.png";
            case "TIENDA" -> "buildings/shops/shop_general.png";
            case "GIMNASIO" -> "buildings/inns/inn_lodge.png";
            case "GOBIERNO" -> "buildings/large_houses/house_large_manor.png";
            case "CASINO" -> "buildings/inns/inn_harbor.png";
            case "CASA_MAMA" -> "buildings/small_houses/house_small_cottage.png";
            case "CASA" -> Math.floorMod(variante,2)==0 ?
                    "buildings/small_houses/house_small_stone.png" :
                    "buildings/small_houses/house_small_cottage.png";
            default -> null;
        };
        BufferedImage origen=cargar(ruta);
        int w=ancho*32,h=alto*32;
        if(origen==null||origen.getWidth()>w||origen.getHeight()>h)return null;
        BufferedImage destino=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=destino.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // La última fila del sprite queda alineada con la entrada del edificio.
        g.drawImage(origen,(w-origen.getWidth())/2,h-origen.getHeight(),null);
        g.dispose();
        return destino;
    }

    /** Para pruebas, permite inspeccionar el formato real sin acceso a la red. */
    public static boolean disponible() {
        BufferedImage im=cargar("environments/tilesets/village.png");
        return im!=null&&im.getWidth()%32==0&&im.getHeight()%32==0;
    }
}
