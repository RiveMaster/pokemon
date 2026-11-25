# Integración del tileset corregido del usuario

La versión de prueba y los **100 mapas regionales** pueden utilizar los sprites del
paquete `tilesetMapa_CORREGIDO_para_proyecto.zip`. Este paquete contiene
**371 imágenes PNG catalogadas**: **201 sprites completos** y **170 módulos
pendientes de montaje**. Los módulos pendientes se conservan, pero
**no se dibujan individualmente** para evitar los cortes detectados.

## Instalar el paquete sin escribir rutas ni comandos

1. Descarga `tilesetMapa_CORREGIDO_para_proyecto.zip` desde la conversación.
2. Abre el juego y carga una partida.
3. En **Menú del entrenador**, pulsa **Instalar tileset corregido (ZIP)**.
4. Selecciona el ZIP descargado.
5. Aparecerá el aviso de instalación correcta. Vuelve a entrar al mapa
   o al Atlas regional para visualizar los edificios y la vegetación actualizados.

El instalador escribe únicamente `Tiles/AtlasUsuarioCorregido.zip`:
no altera `Tiles/Pokemon/`, los gráficos de combate, las partidas,
los datos de Pokémon, los eventos ni la lógica del juego. Comprueba
**todos los hashes SHA-256 de los 371 recursos y del atlas original**,
sus identificadores, rutas y dimensiones antes de sustituir el paquete.

La instalación se puede repetir para reemplazar el archivo ZIP del usuario.
Si el ZIP no está instalado, el renderizador conserva los gráficos antiguos.

## Uso en el mapa

- `AtlasCorregido.java` administra las imágenes identificadas en el manifiesto,
  las carga desde el ZIP cuando hacen falta y evita acceder a módulos incompletos.
- `MapasRegion2D.java` usa las fachadas de Centros Pokémon, tiendas, gimnasios,
  casas y edificios especiales, además de pinos, vegetación y otros elementos
  identificados. Los edificios se centran respetando las proporciones y
  sin estiramientos no enteros.
- Los terrenos transitables utilizan píxeles y colores tomados del mismo
  atlas original. **No se han declarado los 78 módulos de 16×16 como superficies
  seamless**, porque el manifiesto los marca pendientes de montaje.
  Se ofrece una textura provisional hasta definir y unir sus bordes.
- Las colisiones de edificios, hierba alta, agua y pasos siguen gestionadas
  por el motor existente. Las casillas completamente transparentes de
  fachadas importadas no bloquean el movimiento.

## Alcance

La integración visual queda habilitada en **los mapas regionales generados**
y en las localidades del recorrido principal que usan ese generador.
Los mapas personalizados `.pmap` conservan sus propias texturas y
no se sobrescriben automáticamente. Los interiores de prueba anteriores
continúan utilizando `AtlasUsuario16`; sus nuevos recortes pendientes
se montarán cuando sea seguro hacerlo.

Aunque se reconocen todos los identificadores de los 371 recursos,
**no se muestran simultáneamente los 201 sprites completos**:
el generador utiliza los que corresponden a las categorías de terreno
y edificios. Los demás permanecen disponibles en el catálogo para
nuevos diseños de mapas. Esto evita colocar vehículos o edificios
especiales arbitrariamente.

## Derechos

El atlas fue proporcionado por el usuario para su uso en el proyecto;
la licencia de redistribución de terceros no ha sido verificada.
No publiques el ZIP como arte libre sin confirmar los derechos.
