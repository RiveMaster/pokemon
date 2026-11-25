package Proyecto;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exporta los 120 mapas del juego como archivos PNG separados y diez mosaicos
 * de referencia. El juego usa el mismo generador; estas imágenes son vistas
 * previas y no son necesarias para que las regiones sean jugables.
 *
 * Ejecutar: java -Djava.awt.headless=true -cp build Proyecto.ExportadorMapasGeneracionales
 */
public final class ExportadorMapasGeneracionales {
    private ExportadorMapasGeneracionales() { }

    public static void main(String[] args) throws IOException {
        Path destino = args.length > 0 ? Path.of(args[0]) : Path.of("MapasGenerados");
        int total=0;
        java.util.List<java.util.Map<String,Object>> manifiesto=new java.util.ArrayList<>();
        for(Generacion generacion:Generacion.values()){
            Path carpeta=destino.resolve(String.format("GEN%02d",generacion.getNumero()));
            Files.createDirectories(carpeta);
            int thumbW=576,thumbH=384;
            BufferedImage mosaico=new BufferedImage(thumbW*6,thumbH*2,BufferedImage.TYPE_INT_ARGB);
            Graphics2D galeria=mosaico.createGraphics();
            galeria.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            java.util.List<CatalogoMapasGeneracionales.Mapa> escenas =
                    new java.util.ArrayList<>(CatalogoMapasGeneracionales.listar(generacion));
            escenas.addAll(CatalogoMapasGeneracionales.nuevos(generacion));
            for(CatalogoMapasGeneracionales.Mapa descripcion:escenas){
                MapasRegion2D.Resultado mapa=MapasRegion2D.generar(descripcion);
                BufferedImage imagen=pintar(mapa);
                Path archivo=carpeta.resolve(String.format("MAPA_%02d_%s.png",
                        descripcion.indice()+1,descripcion.bioma().toLowerCase(java.util.Locale.ROOT)));
                if(!ImageIO.write(imagen,"png",archivo.toFile()))
                    throw new IOException("El sistema no puede escribir PNG: " + archivo);
                java.util.Map<String,Object> entrada=new java.util.LinkedHashMap<>();
                entrada.put("id", descripcion.id());
                entrada.put("nombre", descripcion.nombre());
                entrada.put("bioma", descripcion.bioma());
                entrada.put("generacion", generacion.getNumero());
                entrada.put("nuevo", descripcion.id().startsWith("NUEVO_"));
                entrada.put("archivo", destino.relativize(archivo).toString().replace('\\','/'));
                manifiesto.add(entrada);
                int col=descripcion.indice()%6,fila=descripcion.indice()/6;
                galeria.drawImage(imagen,col*thumbW,fila*thumbH,thumbW,thumbH,null);
                total++;
            }
            galeria.dispose();
            ImageIO.write(mosaico,"png",carpeta.resolve("GALERIA_GENERACION.png").toFile());
            System.out.println("Exportada: "+generacion.etiqueta()+" (12 mapas)");
        }
        if(total!=120)throw new IllegalStateException("Se esperaban 120 mapas: "+total);
        java.util.Map<String,Object> indice=new java.util.LinkedHashMap<>();
        indice.put("total",total);
        indice.put("originales",100);
        indice.put("nuevos",20);
        indice.put("tamano_tile_pixeles",32);
        indice.put("mapas",manifiesto);
        Files.writeString(destino.resolve("mapas_manifest.json"),
                Proyecto.studio.Json.escribir(indice),java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Exportación completada: 120 PNG + 10 galerías + manifiesto en "+destino);
    }

    public static BufferedImage pintar(MapasRegion2D.Resultado mapa) {
        int tam=ArteRegion2D.PIXELES;
        BufferedImage imagen=new BufferedImage(mapa.ancho*tam,mapa.alto*tam,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        for(int capa=0;capa<mapa.capas.length;capa++) {
            for(int y=0;y<mapa.alto;y++)
                for(int x=0;x<mapa.ancho;x++) {
                    int t=mapa.capas[capa][y*mapa.ancho+x];
                    if(t>=0&&t<mapa.tiles.length)
                        g.drawImage(mapa.tiles[t],x*tam,y*tam,null);
                }
        }
        g.dispose();
        return imagen;
    }
}
