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
- El botón **Generar mapa** crea mapas proceduralmente con estilos Ruta/Bosque, Pueblo/Ciudad, Costa/Lago y Pradera. Puedes ajustar ancho, alto, semilla, densidad, caminos, hierba, agua, obstáculos y colocación de objetos compuestos. La misma semilla y configuración generan el mismo resultado. Los tiles elegidos se etiquetan automáticamente como Normal, Hierba, Agua o Bloqueo.
- El botón **Exportar tiles** del editor genera una carpeta por cada PNG de origen; cada carpeta contiene sus tiles visibles como PNG de 16 × 16, nombrados por fila y columna (por ejemplo, `tile_000_001.png`). Los tiles totalmente transparentes se omiten. El botón **Exportar PNG** sigue exportando el mapa compuesto completo.
- Los objetos grandes se crean desde la pestaña **Crear objetos** del editor: escoge ancho/alto, pinta el diseño con tiles, dale un nombre y guárdalo. Después selecciona el objeto y haz clic sobre el mapa para colocar copias. Clic derecho sobre una colocación la elimina.
- La biblioteca de objetos, sus colocaciones, los tiles, las capas y colisiones se guardan en el archivo de proyecto `.pmap`.

Inicia el editor desde la raíz del proyecto para que encuentre esta carpeta:

```sh
java Proyecto.studio.Estudio mapas
```
