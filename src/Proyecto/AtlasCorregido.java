package Proyecto;

import Proyecto.studio.Json;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Integra los sprites listos del atlas corregido entregado por el usuario.
 * Nunca usa los 170 fragmentos marcados como pendientes de montaje.
 * El ZIP se instala desde el menú gráfico, sin extraer/copiar PNG a mano.
 */
public final class AtlasCorregido {
    public static final Path ARCHIVO = Path.of("Tiles", "AtlasUsuarioCorregido.zip");
    private static final String RAIZ = "Tiles/AtlasUsuarioCorregido/";
    private static final String HASH_ORIGINAL =
            "64dfc4461d31236aee871e558be826d5ceb9542ea311aeb1c004cb5535699657";

    public record Pieza(String nombre, String archivo, String categoria,
                        int ancho, int alto, String hash, boolean listo) {}
    private static volatile AtlasCorregido instancia;
    private final Map<String, Pieza> piezas;
    private final Map<String, BufferedImage> cache = new ConcurrentHashMap<>();
    private volatile BufferedImage original;

    private AtlasCorregido(Map<String, Pieza> piezas) {
        this.piezas = Collections.unmodifiableMap(piezas);
    }

    public static boolean instalado() { return Files.isRegularFile(ARCHIVO); }

    public static AtlasCorregido actual() {
        if (!instalado()) return null;
        AtlasCorregido local = instancia;
        if (local != null) return local;
        synchronized (AtlasCorregido.class) {
            if (instancia == null) {
                try { instancia = validar(ARCHIVO, false); }
                catch (IOException e) {
                    System.err.println("Atlas corregido inválido; se conserva el mapa anterior: "
                            + e.getMessage());
                    return null;
                }
            }
            return instancia;
        }
    }

    public int cantidadLista() {
        return (int) piezas.values().stream().filter(Pieza::listo).count();
    }

    public int cantidadPendiente() { return piezas.size() - cantidadLista(); }

    public Map<String, Pieza> catalogo() { return piezas; }

    /** Devuelve null para sprites ausentes o módulos que requieren montaje. */
    public BufferedImage sprite(String id) {
        Pieza p = piezas.get(id);
        if (p == null || !p.listo()) return null;
        BufferedImage cached = cache.get(id);
        if (cached != null) return cached;
        try {
            BufferedImage img = leerPng(p.archivo());
            if (img.getWidth() != p.ancho() || img.getHeight() != p.alto())
                throw new IOException("Dimensiones incorrectas: " + id);
            cache.put(id, img);
            return img;
        } catch (IOException e) {
            throw new IllegalStateException("Sprite corregido defectuoso: " + id, e);
        }
    }

    private BufferedImage leerPng(String relativa) throws IOException {
        try (ZipFile z = new ZipFile(ARCHIVO.toFile())) {
            ZipEntry entry = z.getEntry(RAIZ + relativa);
            if (entry == null) throw new IOException("No existe: " + relativa);
            try (InputStream stream = z.getInputStream(entry)) {
                BufferedImage img = ImageIO.read(stream);
                if (img == null) throw new IOException("PNG ilegible: " + relativa);
                return img;
            }
        }
    }

    private BufferedImage atlasOriginal() {
        BufferedImage img = original;
        if (img != null) return img;
        synchronized (this) {
            if (original == null) {
                try { original = leerPng("Original/tilesetMapa.png"); }
                catch (IOException e) { throw new IllegalStateException(e); }
            }
            return original;
        }
    }

    /** Aísla un terreno del MISMO atlas; casilla visual de 32x32. */
    public BufferedImage terreno(String tipo, int variante) {
        BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // Los patrones de 16x16 del manifiesto están expresamente marcados
        // como "PorMontar": no se repiten como suelo definitivo porque
        // contienen bordes de otros dibujos. La textura provisional utiliza
        // EXCLUSIVAMENTE colores muestreados del PNG original.
        BufferedImage source = atlasOriginal();
        int x = 15, y = 9; // verde del primer árbol del atlas
        if (tipo.equals("ARENA") || tipo.equals("SENDA")) {
            x=20; y=105; // beige de suelo del propio atlas
        } else if (tipo.equals("PIEDRA") || tipo.equals("ROCA")
                || tipo.equals("MURO") || tipo.equals("CUEVA")
                || tipo.equals("COLUMNA") || tipo.equals("NIEVE")) {
            x=290; y=105; // gris del mismo atlas
        } else if (tipo.equals("AGUA")) {
            x=287; y=164; // azul del mismo atlas
        } else if (tipo.equals("HIERBA_ALTA") || tipo.equals("CESPED_OSCURO")) {
            x=20; y=13; // verde más oscuro del mismo árbol
        }
        int muestra = source.getRGB(x,y);
        g.setColor(new Color(0xff000000 | (muestra & 0x00ffffff), true));
        g.fillRect(0,0,32,32);
        if (!tipo.equals("AGUA")) {
            int detalle = source.getRGB(tipo.equals("ARENA") || tipo.equals("SENDA")
                    ? 18 : 17, tipo.equals("ARENA") || tipo.equals("SENDA") ? 102 : 8);
            g.setColor(new Color(0xff000000 | (detalle & 0x00ffffff),true));
            for(int i=0;i<9;i++) {
                int px=Math.floorMod(i*13+variante*3,30);
                int py=Math.floorMod(i*17+variante*7,31);
                g.fillRect(px,py,2,1);
            }
        }

        String decoracion = switch (tipo) {
            case "ARBOL" -> variante % 2 == 0 ? "ArbolCopa01" : "ArbolCopa20";
            case "ARBOL2" -> variante % 2 == 0 ? "Pino01" : "Pino07";
            case "ARBUSTO" -> variante % 2 == 0 ? "Arbusto03" : "Arbusto27";
            case "ROCA" -> "RocaCiudad1";
            case "CARTEL" -> "SenalCiudad1";
            case "FLORES" -> "Arbusto27";
            default -> null;
        };
        if (decoracion != null) {
            BufferedImage s = sprite(decoracion);
            if (s != null && s.getWidth() <= 32 && s.getHeight() <= 32)
                g.drawImage(s,(32-s.getWidth())/2,32-s.getHeight(),null);
        }
        g.dispose();
        return img;
    }

    public BufferedImage camino(int mascara, int variante) {
        return terreno("SENDA", variante);
    }

    /** Encaja fachadas completas con escalado entero, nunca las deforma. */
    public BufferedImage edificio(String servicio, int ancho, int alto, int variante) {
        String id = switch (servicio) {
            case "CENTRO_POKEMON" -> "CentroPokemon1";
            case "TIENDA" -> variante % 2 == 0 ? "TiendaAzul1" : "TiendaVerde1";
            case "GIMNASIO" -> variante % 2 == 0 ? "GimnasioRojo1" : "GimnasioGrande1";
            case "CASA_MAMA" -> "CasaRoja1";
            case "CASA" -> switch (Math.floorMod(variante,5)) {
                case 0 -> "CasaRoja1";
                case 1 -> "CasaVerde1";
                case 2 -> "CasaAzul1";
                case 3 -> "CasaMarron1";
                default -> "CasaNaranja1";
            };
            case "CASINO" -> "TiendaAzul1";
            case "GOBIERNO" -> "GimnasioGrande1";
            default -> "CasaRoja1";
        };
        BufferedImage src = sprite(id);
        if (src == null) return null;
        int w=ancho*32,h=alto*32;
        BufferedImage canvas = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int scale=src.getWidth()*2<=w && src.getHeight()*2<=h ? 2 : 1;
        int sw=src.getWidth()*scale,sh=src.getHeight()*scale;
        g.drawImage(src,(w-sw)/2,h-sh,sw,sh,null);
        g.dispose();
        return canvas;
    }

    private static String hash(byte[] b) {
        try {
            byte[] bytes=MessageDigest.getInstance("SHA-256").digest(b);
            StringBuilder sb=new StringBuilder(64);
            for(byte x:bytes)sb.append(String.format(Locale.ROOT,"%02x",x & 255));
            return sb.toString();
        } catch (Exception e) { throw new IllegalStateException(e); }
    }

    private static byte[] leer(ZipFile zip, String ruta) throws IOException {
        ZipEntry e=zip.getEntry(RAIZ+ruta);
        if(e==null || e.getSize()>4_000_000)throw new IOException("Falta o excede tamaño: "+ruta);
        try(InputStream in=zip.getInputStream(e)){
            byte[] datos=in.readNBytes(4_000_001);
            if(datos.length>4_000_000)throw new IOException("Archivo demasiado grande: "+ruta);
            return datos;
        }
    }

    private static AtlasCorregido validar(Path archivo, boolean completo) throws IOException {
        if(Files.size(archivo)>15_000_000)throw new IOException("Paquete demasiado grande");
        try(ZipFile zip=new ZipFile(archivo.toFile())){
            Map<String,Object> manifest=Json.mapa(Json.parse(new String(
                    leer(zip,"tiles_manifest.json"),StandardCharsets.UTF_8)));
            if(Json.entero(manifest,"version",-1)!=2 ||
                    !HASH_ORIGINAL.equals(Json.texto(manifest,"sha256_atlas","")))
                throw new IOException("Manifiesto o versión del atlas incorrecta");
            Map<String,Pieza> piezas=new LinkedHashMap<>();
            for(Object obj:Json.lista(manifest.get("piezas"))){
                Map<String,Object> p=Json.mapa(obj);
                String id=Json.texto(p,"id","");
                String path=Json.texto(p,"archivo","");
                String cat=Json.texto(p,"categoria","");
                String checksum=Json.texto(p,"sha256","");
                Map<String,Object> dim=Json.mapa(p.get("resolucion"));
                int w=Json.entero(dim,"ancho",0),h=Json.entero(dim,"alto",0);
                boolean listo=Boolean.TRUE.equals(p.get("uso_directo"));
                if(id.isBlank()||piezas.containsKey(id)||path.startsWith("/")||
                        path.contains("..")||path.contains("\\")||!path.endsWith(".png")||
                        w<=0||h<=0||w>1024||h>1024||checksum.length()!=64)
                    throw new IOException("Identificador o ruta de sprite inválidos: "+id);
                piezas.put(id,new Pieza(id,path,cat,w,h,checksum,listo));
            }
            AtlasCorregido atlas=new AtlasCorregido(piezas);
            if(piezas.size()!=371||atlas.cantidadLista()!=201)
                throw new IOException("El paquete no contiene 201 sprites y 170 módulos");
            if(completo){
                if(!HASH_ORIGINAL.equals(hash(leer(zip,"Original/tilesetMapa.png"))))
                    throw new IOException("La imagen original no coincide con la fuente validada");
                for(Pieza p:piezas.values()){
                    byte[] bytes=leer(zip,p.archivo());
                    if(!p.hash().equals(hash(bytes)))
                        throw new IOException("Hash incorrecto: "+p.nombre());
                    if(p.listo()){
                        BufferedImage img=ImageIO.read(new ByteArrayInputStream(bytes));
                        if(img==null||img.getWidth()!=p.ancho()||img.getHeight()!=p.alto())
                            throw new IOException("Imagen inválida: "+p.nombre());
                    }
                }
            }
            return atlas;
        }catch(IllegalArgumentException e){
            throw new IOException("No se pudo leer el manifiesto",e);
        }
    }

    /**
     * Guarda únicamente el ZIP verificado en Tiles/. No cambia otros assets
     * ni requiere extraer los 371 PNG; todos se cargan por su identificador.
     */
    public static synchronized int importar(Path origen) throws IOException {
        if(origen==null||!Files.isRegularFile(origen))throw new IOException("No existe el ZIP");
        AtlasCorregido comprobado=validar(origen,true);
        Files.createDirectories(ARCHIVO.toAbsolutePath().getParent());
        Path temporal=Files.createTempFile(ARCHIVO.toAbsolutePath().getParent(),"atlas-",".zip");
        try{
            Files.copy(origen,temporal,StandardCopyOption.REPLACE_EXISTING);
            try{Files.move(temporal,ARCHIVO.toAbsolutePath(),
                    StandardCopyOption.REPLACE_EXISTING);}
            catch(IOException e){throw new IOException("No se pudo instalar el atlas",e);}
        }finally{Files.deleteIfExists(temporal);}
        instancia=comprobado;
        return comprobado.cantidadLista();
    }

    public static void instalarGraficamente(Component padre){
        if(GraphicsEnvironment.isHeadless())return;
        JFileChooser selector=new JFileChooser();
        selector.setDialogTitle("Seleccionar tilesetMapa_CORREGIDO_para_proyecto.zip");
        selector.setFileFilter(new FileNameExtensionFilter("Paquetes ZIP","zip"));
        if(selector.showOpenDialog(padre)!=JFileChooser.APPROVE_OPTION)return;
        try{
            int total=importar(selector.getSelectedFile().toPath());
            JOptionPane.showMessageDialog(padre,total+" sprites listos instalados.\n"
                    +"Se han reservado 170 módulos para montaje.\n"
                    +"Abre un mapa para ver el nuevo arte.","Tileset corregido",
                    JOptionPane.INFORMATION_MESSAGE);
        }catch(IOException e){
            JOptionPane.showMessageDialog(padre,e.getMessage(),
                    "No se ha instalado el tileset",JOptionPane.ERROR_MESSAGE);
        }
    }
}
