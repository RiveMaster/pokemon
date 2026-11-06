# Tiles y objetos del editor de mapas

Coloca aquí tus archivos PNG para que Pokémon Studio los lea al abrir el editor de mapas.

- Se aceptan tiles individuales de **16 × 16 píxeles**.
- También se aceptan spritesheets PNG con ancho y alto múltiplos de 16; cada imagen se divide en tiles de 16 × 16.
- Se omite cualquier tile completamente transparente: tiene que haber al menos un píxel con alfa mayor que cero.
- Los PNG se leen directamente desde esta carpeta (no desde subcarpetas) y se ordenan alfabéticamente por nombre.
- Si la carpeta no contiene PNG, se muestran los tiles de ejemplo. Si contiene PNG pero todos están vacíos, no se añadirán tiles de ejemplo.
- Al abrir el editor se elige el tamaño inicial del mapa (ancho y alto en tiles, de 1 a 300).
- En la pestaña **Tiles** puedes crear un tile transparente desde cero y dibujarlo píxel a píxel en una cuadrícula de 16×16: elige un color y pinta con clic izquierdo; usa clic derecho para borrar píxeles a transparencia.
- Cada tile tiene una propiedad: **Normal**, **Bloquea el paso**, **Hierba (encuentros Pokémon)**, **Agua** o **Evento**. Elige el tipo antes de pintar el tile en el mapa o cámbialo luego; las celdas pintadas se actualizan. «Ver propiedades» muestra en rojo las colisiones y en verde las zonas de hierba.
- Las propiedades y las celdas de hierba se guardan en el archivo `.pmap`. Importante: el juego actual todavía no carga los mapas `.pmap` ni genera Pokémon salvajes a partir de la bandera de hierba; esa integración con el motor del juego queda pendiente.
- El botón **Generar mapa** crea mapas con estilos Ruta/Bosque, Pueblo/Ciudad, Costa/Lago y Pradera. El tema visual del generador está definido por código y no ofrece selectores de apariencia: genera un fondo continuo de césped, terreno intermedio, agua, hierba, suelo y 13 variantes orientadas de camino, más edificios y árboles compuestos. Los gráficos cargados para el tema permanecen protegidos frente a la edición/eliminación desde el editor.
- El generador busca primero sus imágenes en esta carpeta **Tiles/**, sin distinguir mayúsculas, minúsculas, espacios ni tildes. Nombres reconocidos: `Césped.png`; `SueloBase.png` (también `Suelo.png`); `Hierba.png`; `Agua.png`; `Camino1.png` hasta `Camino13.png` (también `Camino01.png`... `Camino13.png`). En lugar de los trece PNG de camino se admite `Caminos.png`, `Caminos13.png` o `Camino.png` con al menos 13 celdas de 16×16 en orden de lectura, de izquierda a derecha y de arriba abajo.
- Los objetos compuestos se leen de `Arboles.png` (o `Arbol.png`/`Tree.png`) con un área mínima de 64×64 píxeles; `Casa.png`, `Casa2.png`, `Casa3.png`, `CentroPokemon.png`, `TiendaPokemon.png` y `Gimnasio.png` con un área mínima de 48×48 píxeles. Se conserva la transparencia PNG. En el árbol se respeta además la máscara 4×4 de diez celdas: las posiciones vacías siguen siendo huecos.
- Los recursos básicos del terreno pueden ser PNG de 16×16 o spritesheets con celdas de 16×16; se toma la primera celda visible. Cuando un archivo aún no existe o no tiene dimensiones válidas, se utiliza una textura provisional dibujada por código. Por eso el generador funciona aunque `Tiles/` esté vacía; al añadir los PNG con los nombres y dimensiones indicados, se cargarán automáticamente al abrir el editor.
- En los pueblos, el generador divide el mapa en sectores-ciudad y distribuye automáticamente un Centro Pokémon, una tienda, un gimnasio y varias casas por sector cuando caben. Las casas y edificios del tema se colocan en la capa superior; los árboles usan un objeto compuesto de 4×4 con las diez celdas especificadas y huecos transparentes conservados.
- Las capas generadas siguen este orden visual: fondo de césped; terreno (agua, hierba, suelo base y caminos autotilados); objetos superiores (árboles y edificios). La misma semilla y configuración generan el mismo resultado.
- El botón **Exportar tiles** del editor genera una carpeta por cada PNG de origen; cada carpeta contiene sus tiles visibles como PNG de 16 × 16, nombrados por fila y columna (por ejemplo, `tile_000_001.png`). Los tiles totalmente transparentes se omiten. El botón **Exportar PNG** sigue exportando el mapa compuesto completo.
- Los objetos grandes se crean desde la pestaña **Crear objetos** del editor: escoge ancho/alto, pinta el diseño con tiles, dale un nombre y guárdalo. Después selecciona el objeto y haz clic sobre el mapa para colocar copias. Clic derecho sobre una colocación la elimina.
- La biblioteca de objetos, sus colocaciones, los tiles, las capas y colisiones se guardan en el archivo de proyecto `.pmap`.

## Exploración del juego

- En rutas, pueblos y ciudades el jugador se mueve por casillas con **W**, **A**, **S** y **D**. Pulsa **Esc** para volver al menú de la zona.
- El jugador tiene un sprite de 16×16 generado por el programa. Puedes sustituirlo añadiendo `Tiles/Jugador.png` o `Tiles/Player.png` (se usa la celda superior izquierda).
- El motor busca mapas de juego en `Mapas/UBICACION.pmap`, `mapas/UBICACION.pmap`, `Maps/UBICACION.pmap` o `UBICACION.pmap`, usando el nombre de la ubicación del código (por ejemplo, `RUTA1.pmap` o `BILBAO.pmap`). También acepta `mapa.pmap` como mapa común. Los archivos se crean con **Guardar proyecto** desde el editor.
- Si no existe un `.pmap` para la ubicación, el juego crea un escenario provisional para esa zona y conserva la posición del jugador al volver a entrar.
- Los encuentros salvajes se activan solo al caminar sobre tiles clasificados como **Hierba** en una ruta. En pueblos y ciudades no aparecen Pokémon salvajes.
- El agua bloquea el paso salvo que el equipo tenga un Pokémon vivo de tipo Agua, Volador o Dragón. Las colisiones manuales, paredes, árboles, rocas y edificios siguen bloqueando el paso incluso con esa capacidad.

Inicia el editor desde la raíz del proyecto para que encuentre esta carpeta:

```sh
java Proyecto.studio.Estudio mapas
```
