# RPG Pixel Pack: recursos externos CC0 integrados

**Fuente:** https://github.com/JaiGod/rpg-pixel-pack

**Autor original:** Jaineil Rana (JaKing).

**Licencia de las imágenes:** CC0 1.0 Universal (dedicación al dominio público).
Licencia y texto del autor: https://github.com/JaiGod/rpg-pixel-pack/blob/main/LICENSE
Texto legal: https://creativecommons.org/publicdomain/zero/1.0/legalcode

## Contenido importado

Se han copiado los PNG originales (sin redibujarlos ni extraerlos de videojuegos Pokémon):

- 11 atlas de ambientes de 256×128 píxeles, distribuidos en **8 columnas y 4 filas de tiles 32×32**: village, forest, desert, swamp, snow, dungeon, castle, ruins, space_colony, research_lab y cyber_city.
- Edificios: dos tiendas, dos posadas, dos casas, una mansión y un castillo.
- Un árbol de referencia.

Los archivos están en `Tiles/RPGPixelPack/`, conservando los nombres de su repositorio.

## Integración en Java

`RecursosCC0.java` lee tiles 32×32 exactos con `ImageIO` y verifica la cuadrícula. `MapasRegion2D.java` usa esos PNG para caminos, piedras, árboles, superficies de ciudades, casas y edificios; conserva una paleta de césped diferente por generación para que no se repitan los cien mapas. `ExploradorMapa.java` los muestra a escala entera con `nearest-neighbor` sin difuminar píxeles.

El motor puede seguir trabajando sin esos archivos mediante la implementación de respaldo `ArteRegion2D`. Las colisiones y coordenadas están en casillas, no en píxeles. Los archivos originales de Pokémon Studio (`.pmap`) conservan sus gráficos y su formato.

## Compatibilidad y licencia

La estética busca sprites legibles de RPG 2D clásico, pero **no contiene recursos oficiales extraídos de Pokémon**.

CC0 permite usar, modificar y distribuir los gráficos. Se conservan la procedencia y la licencia para que sea fácil atribuir al artista, aunque la licencia no exige reconocimiento obligatorio.

Los mapas generados y los edificios enlazados mantienen los eventos y servicios del juego (centro, tienda, gimnasio, casas) sin alterar el inventario ni las partidas anteriores.
