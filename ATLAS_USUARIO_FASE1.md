# Fase 1 del mundo basado exclusivamente en el tileset del usuario

**Estado:** primera zona de prueba integrada; no se ha reemplazado aún toda la
aventura y los interiores están pendientes. Se conserva el progreso anterior.

## Recursos

Fuente exclusiva: el PNG de 864 × 925 píxeles facilitado por el usuario
en la conversación. No se descargaron nuevos sprites ni se utilizaron
RPGPixelPack, VisualRetro o el arte generado por código **en esta vista previa**.

Se incluye `Tiles/AtlasUsuarioFase1.png`, un pequeño subconjunto de
recortes extraído de dicha imagen, reducido a una paleta de 64 colores para
poder incorporar una primera entrega compacta al repositorio. No se
distribuye todavía la hoja completa. Si se desea trabajar con los colores
originales completos, se coloca el archivo facilitado por el usuario, sin
redimensionar, en `Tiles/AtlasUsuario.png`. El motor lo preferirá al paquete
compacto y reconocerá todos los recortes por sus coordenadas originales.

El cargador `src/Proyecto/AtlasUsuario16.java` registra **20 recortes
inspeccionados**, con sus coordenadas exactas y dimensiones naturales.
No todos comienzan en múltiplos de 16, por lo que no se recorta la
hoja completa como una cuadrícula homogénea.

Se incluyen suelos de 16×16 y edificios completos, pinos y arbustos de
diferentes dimensiones. El único suelo continuo que se recompone
(`SUELO_VERDE`) utiliza exclusivamente muestras de color de los
árboles del mismo atlas. No se importa ninguna textura exterior.

## Probarlo en el juego

1. Inicia la versión actual del proyecto.
2. Abre **Menú del entrenador → Probar nuevo mapa (atlas único)**.
3. Se abre un pueblo de 32×22 casillas, a escala gráfica 3×, usando solo
   los sprites del usuario para el escenario.
4. Camina mediante **WASD** o las **flechas**; Esc cierra la prueba.
5. Puedes acercarte a las entradas de las casas, la tienda o el Centro
   Pokémon; las puertas están registradas y muestran un aviso.

El jugador aparece con un **marcador amarillo de posición UI**:
todavía no se ha colocado un sprite de protagonista o de NPC, porque
el usuario enviará sus personajes después. El marcador no forma parte
de la decoración del mapa ni es un recurso gráfico externo.

Las fachadas están dibujadas con sus dimensiones **reales**. El motor
no estira la casa para llenar un solar. Las posiciones del jugador se
miden en casillas, no en píxeles.

## Archivos

- `Tiles/AtlasUsuarioFase1.png`: paquete de recortes inicial.
- `src/Proyecto/AtlasUsuario16.java`: manifiesto y carga de recortes.
- `src/Proyecto/MapaAtlasUsuario.java`: mapa de prueba, colisiones,
  caminos, edificios, vegetación y puertas.
- `src/Proyecto/Menu.java`: botón para entrar a la prueba.
- `tests/AtlasUsuarioFase1SmokeTest.java`: valida la carga de los
  sprites, sus dimensiones, los caminos y las entradas.

## Próximas fases previstas

**Fase 2:** identificar recortes de interior con paredes, suelos,
mostradores, camas, estanterías y máquinas en el mismo atlas, y construir
interiores transitables para casa, tienda y Centro Pokémon.

**Fase 3:** sustituir de forma progresiva los 100 mapas regionales
manteniendo la compatibilidad con partidas guardadas, escenas, colisiones,
guardados y eventos. Solo cuando se comprueben los mapas se desactivará
el sistema visual anterior para la aventura principal.

**Fase 4:** añadir sprites de jugador y NPC enviados por el usuario;
no se reutilizan personajes de otros packs mientras tanto.

## Derechos

El archivo fue proporcionado por el usuario para emplearlo en el juego.
No se ha verificado una licencia de redistribución de terceros para
publicar el atlas completo. Antes de distribuir comercialmente los
recursos, confirma que dispones del permiso correspondiente.
