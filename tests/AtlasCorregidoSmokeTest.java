package Proyecto;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Pruebas del cargador corregido. En CI sin ZIP verifica que el motor siga
 * funcionando; con un ZIP de prueba opcional valida sprites y escenarios.
 *
 * Uso local: java -ea -cp build Proyecto.AtlasCorregidoSmokeTest
 *       [ruta/tilesetMapa_CORREGIDO_para_proyecto.zip]
 */
public final class AtlasCorregidoSmokeTest {
    private AtlasCorregidoSmokeTest() { }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            Path archivo = Path.of(args[0]);
            assert Files.isRegularFile(archivo) : "No existe el ZIP corregido";
            assert AtlasCorregido.importar(archivo) == 201;
        }
        AtlasCorregido atlas = AtlasCorregido.actual();
        if (atlas == null)
            throw new AssertionError("Debe cargarse el atlas integrado o el ZIP corregido");
        if (atlas.paqueteCompleto()) {
            assert atlas.cantidadLista() == 201;
            assert atlas.cantidadPendiente() == 170;
            assert atlas.catalogo().size() == 371;
        } else {
            assert atlas.cantidadLista() == 16;
            assert atlas.cantidadPendiente() == 0;
            assert atlas.catalogo().size() == 16;
        }
        for (String id : new String[]{"CentroPokemon1","TiendaAzul1",
                "GimnasioRojo1","CasaRoja1","Pino01","Arbusto03","ArbolCopa01"}) {
            BufferedImage img = atlas.sprite(id);
            assert img != null && img.getWidth() > 0 && img.getHeight() > 0;
        }
        assert atlas.sprite("NoExiste") == null;
        BufferedImage base = atlas.terreno("CESPED", 1);
        assert base.getWidth() == 32 && base.getHeight() == 32;
        assert (base.getRGB(0,0) >>> 24) == 255 :
                "El terreno debe ser opaco";
        BufferedImage centro = atlas.edificio("CENTRO_POKEMON",6,5,0);
        assert centro.getWidth() == 192 && centro.getHeight() == 160;
        boolean pixelDibujado=false;
        for (int y=0;y<centro.getHeight();y++)
            for (int x=0;x<centro.getWidth();x++)
                if ((centro.getRGB(x,y)>>>24)>128) pixelDibujado=true;
        assert pixelDibujado : "El Centro Pokémon debe conservar su fachada";
        System.out.println("AtlasCorregidoSmokeTest: "+atlas.cantidadLista()
                +" sprites originales disponibles y renderizador compatible OK");
    }
}
