# Tiles y objetos del editor de mapas

Coloca aquí tus archivos PNG para que Pokémon Studio los lea al abrir el editor de mapas.

- Se aceptan tiles individuales de **16 × 16 píxeles**.
- También se aceptan spritesheets PNG con ancho y alto múltiplos de 16; cada imagen se divide en tiles de 16 × 16.
- Se omite cualquier tile completamente transparente: tiene que haber al menos un píxel con alfa mayor que cero.
- Los PNG se leen directamente desde esta carpeta (no desde subcarpetas) y se ordenan alfabéticamente por nombre.
- Si la carpeta no contiene PNG, se muestran los tiles de ejemplo. Si contiene PNG pero todos están vacíos, no se añadirán tiles de ejemplo.
- El botón **Exportar tiles** del editor genera una carpeta por cada PNG de origen; cada carpeta contiene sus tiles visibles como PNG de 16 × 16, nombrados por fila y columna (por ejemplo, `tile_000_001.png`). Los tiles totalmente transparentes se omiten. El botón **Exportar PNG** sigue exportando el mapa compuesto completo.
- Los objetos grandes se crean desde la pestaña **Crear objetos** del editor: escoge ancho/alto, pinta el diseño con tiles, dale un nombre y guárdalo. Después selecciona el objeto y haz clic sobre el mapa para colocar copias. Clic derecho sobre una colocación la elimina.
- La biblioteca de objetos, sus colocaciones, los tiles, las capas y colisiones se guardan en el archivo de proyecto `.pmap`.

Inicia el editor desde la raíz del proyecto para que encuentre esta carpeta:

```sh
java Proyecto.studio.Estudio mapas
```
