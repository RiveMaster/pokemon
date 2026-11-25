# Integración del tileset corregido del usuario

Los **100 mapas regionales existentes se han rediseñado** con distintas
disposiciones de edificios, caminos y vegetación. Se han añadido **20 mapas
nuevos (dos por generación)**, con IDs estables `NUEVO_01_01` hasta
`NUEVO_10_02`, conservando intactos los 100 IDs antiguos.

El juego **incluye de serie** `Tiles/AtlasUsuarioSeleccion96.png`, con
**16 sprites auténticos del atlas corregido** en un formato PNG compacto
de paleta de 96 colores. Por tanto, los 120 mapas tienen casas, tiendas,
Centro Pokémon, gimnasio y vegetación nuevos sin instalar nada adicional.

Para disponer de más fachadas y objetos, también se admite el paquete
`tilesetMapa_CORREGIDO_para_proyecto.zip`. Este paquete contiene
**371 imágenes PNG catalogadas**: **201 sprites completos** y **170 módulos
pendientes de montaje**. Los módulos pendientes se conservan, pero
**no se dibujan individualmente** para evitar los cortes detectados.

## Instalar el paquete completo (opcional) sin escribir comandos

No es obligatorio instalar nada para ver los primeros 16 sprites: ya están
en el repositorio y se usan automáticamente. Para desbloquear hasta
22 fachadas distintas de casa, 8 de tienda, 3 de gimnasio y 2 de Centro
Pokémon en el generador regional:

1. Descarga `tilesetMapa_CORREGIDO_para_proyecto.zip` desde la conversación.
2. Abre el juego y carga una partida.
3. En **Menú del entrenador**, pulsa **Instalar tileset corregido (ZIP)**.
4. Selecciona el ZIP descargado.
5. Aparecerá el aviso de instalación correcta. Vuelve a entrar al mapa
   o al Atlas regional para visualizar los edificios y la vegetación actualizados.

El instalador escribe únicamente `Tiles/AtlasUsuarioCorregido.zip`:
si no está presente, la selección compacta sigue funcionando.
No altera `Tiles/Pokemon/`, los gráficos de combate, las partidas,
los datos de Pokémon, los eventos ni la lógica del juego. Comprueba
**todos los hashes SHA-256 de los 371 recursos y del atlas original**,
sus identificadores, rutas y dimensiones antes de sustituir el paquete.

La instalación se puede repetir para reemplazar el archivo ZIP del usuario.
Si el ZIP no está instalado, el renderizador conserva los gráficos antiguos.

## Corrección de escala de edificios y terreno

Las fachadas del atlas se recortan por transparencia antes de calcular su
tamaño. Se dibujan con el mayor factor entero que entra en su parcela,
utilizando nearest-neighbor, sin deformarlas ni recortarlas.

- Los ocho edificios de cada ciudad ocupan solares de 6–7 × 7 casillas de 32 px,
  con una franja de calle frente a cada puerta.
- El mapa regional aparece inicialmente con zoom x4 (128 píxeles en
  pantalla por casilla de 32 px). Se puede ajustar con los botones
  Zoom + / Zoom − o con las teclas + / − dentro del rango x2..x5.
- Los mapas .pmap de 16 px mantienen su zoom anterior para no romper
  los escenarios personalizados.
- Ninguna casa puede sobresalir de su parcela: si una imagen no cabe, su
  ampliación entera se limita antes de dibujarla. Se evita que árboles
  y farolas sobrescriban las fachadas nuevas.
- El recorrido y las posiciones guardadas siguen expresados en casillas,
  de modo que aumentar el zoom no altera colisiones ni ubicaciones.

Las pruebas EscalaMapasSmokeTest y GeneracionesMapasSmokeTest validan
las dimensiones de los edificios y las rutas transitables de los 120 mapas.

## Uso en el mapa

- `AtlasCorregido.java` administra las imágenes identificadas en el manifiesto,
  las carga desde el ZIP cuando hacen falta y evita acceder a módulos incompletos.
- `MapasRegion2D.java` utiliza cuatro diseños de ciudad, rutas arbóreas
  nuevas con árboles completos de 2×2 casillas y las fachadas de Centros
  Pokémon, tiendas, gimnasios, casas y edificios especiales; además
  incorpora pinos, vegetación y otros elementos
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

La integración visual queda habilitada en **los 120 mapas regionales generados**
y en las localidades del recorrido principal que usan ese generador.
Se accede desde el menú **Atlas regional (12 mapas)**, que incluye
los diez anteriores y dos adicionales de la generación actual.
Las salidas, puertas y colisiones se prueban automáticamente en los 120 mapas.
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

## Exportación y vistas previas

GitHub Actions compila Java 17, valida las 120 rutas y exporta un ZIP con
**120 PNG** de 36×24 casillas (32 píxeles por casilla) y **10 galerías**
visualizando todos los escenarios. Los recursos Pokémon y los guardados
no se modifican. La paleta del PNG de selección es una reducción de colores,
no un sustituto exacto de los colores originales del ZIP completo.
