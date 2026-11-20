# Animaciones de Pokémon y entrenador — integración 2D

La interfaz de combate utiliza sprites pixel-art en Proyecto.SpriteCollabUI.
El mapa reproduce fotogramas de caminar con Proyecto.SpriteJugador2D y
desplaza la cámara entre casillas mediante un temporizador de Swing.

## Pokémon en combate

- **Entrada:** aparición con desplazamiento y opacidad progresiva.
- **Reposo:** ciclo de fotogramas y respiración ligera.
- **Ataque:** fotogramas Attack y avance del Pokémon.
- **Impacto:** Hurt con sacudida y parpadeo.
- **Desmayo:** Faint con caída y atenuación.
- Las acciones se presentan secuencialmente sin bloquear el hilo gráfico.
- La perspectiva del jugador se muestra desde atrás y la del rival, de frente,
  cuando los recursos incluyen esas orientaciones.
- La ampliación de imágenes usa nearest-neighbor, sin difuminar los píxeles.

## Sprites de batalla aportados por el proyecto

Para utilizar sprites 2D de frente y espalda, puedes colocar arte **propio
o con permiso de uso** en la carpeta Tiles/Pokemon/.

| Archivo | Uso |
| --- | --- |
| Tiles/Pokemon/0001_front.png | Vista frontal del rival |
| Tiles/Pokemon/0001_back.png | Vista trasera del jugador |
| Tiles/Pokemon/0001_shiny_front.png | Rival shiny |
| Tiles/Pokemon/0001_shiny_back.png | Jugador shiny |

0001 es el identificador de especie de cuatro dígitos. El juego prefiere estos
PNG a los recursos comunitarios, y les aplica las transiciones de combate.
Si faltan, utiliza las animaciones de SpriteCollab o un marcador mientras se
cargan. Un conjunto local frontal + trasero no necesita conexión.

## Entrenador caminando

Puedes sustituir el diseño generado por el código añadiendo
Tiles/Jugador.png o Tiles/Player.png:

- **48 × 64 píxeles:** tres columnas (reposo, paso izquierdo, paso derecho)
  y cuatro filas (frente, izquierda, derecha, espalda).
- Se admiten también cuatro columnas, usando las tres primeras.
- Los archivos de **16 × 16** anteriores siguen funcionando, aunque solo
  disponen de una pose.
- Si no se proporciona una hoja, se dibuja un entrenador original pixel-art
  con gorra, mochila y poses para las cuatro orientaciones.
- WASD, flechas y botones de dirección utilizan los mismos fotogramas.

La posición lógica, colisiones y eventos siguen usando las casillas de 16x16;
solo se anima el desplazamiento visual.

## Recursos comunitarios y derechos

Se mantiene la integración de [SpriteCollab](https://github.com/PMDCollab/SpriteCollab)
como fuente comunitaria de hojas Idle, Attack, Hurt y Faint, más AnimData.xml.
Las imágenes se descargan bajo demanda y se guardan en
~/.pokemon/spritecollab/. Los créditos específicos se muestran al pasar el ratón
cuando están disponibles.

Los recursos de SpriteCollab requieren la atribución y las licencias de sus
autores (incluidas condiciones CC BY-NC 4.0 según corresponda).
**No se han incluido sprites extraídos de videojuegos comerciales Pokémon.**
Comprueba los permisos de cualquier imagen que añadas a Tiles/Pokemon/ antes
de distribuir el proyecto.
