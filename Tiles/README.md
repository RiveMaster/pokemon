# Tiles del editor de mapas

Coloca aquí tus archivos PNG para que los lea Pokémon Studio al abrir el editor de mapas.

- Se aceptan tiles individuales de **16 × 16 píxeles**.
- También se aceptan spritesheets PNG con ancho y alto múltiplos de 16; cada imagen se divide en una cuadrícula de tiles de 16 × 16.
- Se leen los PNG que estén directamente dentro de esta carpeta (no subcarpetas).
- Los archivos se ordenan alfabéticamente por nombre. Si necesitas mantener un orden concreto en una spritesheet, organiza los tiles dentro de la propia imagen.
- Si no hay ningún PNG válido, el editor usará los tiles de ejemplo.

Inicia el editor desde la raíz del proyecto para que encuentre esta carpeta:

```sh
java Proyecto.studio.Estudio mapas
```
