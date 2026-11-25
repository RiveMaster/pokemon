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
        if (atlas == null) {
            assert !AtlasCorregido.instalado() :
                    "El archivo existe, pero el atlas no se ha podido validar";
            System.out.println("AtlasCorregidoSmokeTest: ZIP opcional ausente; fallback compatible OK");
            return;
        }
        assert atlas.cantidadLista() == 201;
        assert atlas.cantidadPendiente() == 170;
        assert atlas.catalogo().size() == 371;
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
        assert centro.getRGB(centro.getWidth()/2,centro.getHeight()-10) != 0;
        System.out.println("AtlasCorregidoSmokeTest: 201 sprites utilizables, 170 módulos excluidos OK");
    }
}
