# Fase 2 — Interiores construidos solo con el tileset del usuario

Se han creado **tres interiores jugables**, vinculados a las puertas del
**pueblo de prueba** que se incorporó en la fase 1. Los suelos, paredes,
camas, mesas, estanterías, mostradores, terminales y máquinas se recortaron
del PNG de 864×925 aportado por el usuario. **No se descargaron sprites ni
se utilizó ningún pack externo en estos interiores.**

## Cómo entrar y salir

1. Arranca el juego y abre **Menú del entrenador → Probar nuevo mapa (atlas único)**.
2. Camina con **WASD** o las flechas hasta la casilla de entrada de un edificio.
3. El juego abre el interior apropiado en una ventana de exploración.
4. Camina, pulsa **E** o **espacio** cerca de los muebles para inspeccionarlos.
5. Sal de nuevo por la puerta de la parte inferior o pulsa **Esc**.
   Regresarás al pueblo **en la misma posición**.

## Tipos de interior

| Edificio | Suelo y paredes | Mobiliario del propio atlas |
|---|---|---|
| Casa | Piso cálido y pared doméstica | Cama, mesa, estantería, maceta |
| Tienda | Piso azul/gris y pared de comercio | Mostrador, estanterías, vitrina |
| Centro Pokémon | Piso claro y pared de centro | Mostrador, máquinas, terminal, maceta |

El interior tiene **18×13 casillas** de **16×16 píxeles**, ampliadas tres veces
con nearest-neighbor. Los muebles conservan sus dimensiones de origen y bloquean
varias casillas si son grandes. Se ha mantenido un pasillo de entrada y salida.

## Imágenes y recortes

- `Tiles/AtlasUsuarioInteriorFase2.png`: **22 recortes** originales,
  compactados con una paleta indexada para limitar el tamaño del repositorio.
- `src/Proyecto/AtlasUsuario16.java`: dos coordenadas por recurso:
  ubicación en la imagen original completa y ubicación en el paquete
  compacto. Si se instala `Tiles/AtlasUsuario.png` (864×925) se
  utilizará automáticamente la **hoja original** con sus colores.
- `src/Proyecto/InterioresAtlasUsuario.java`: estructura de las
  habitaciones, mobiliario, colisiones, salida, ventana interactiva.
- `src/Proyecto/MapaAtlasUsuario.java`: conexiones entre las puertas del
  pueblo y los tres interiores.
- `tests/AtlasUsuarioFase2SmokeTest.java`: carga los 22 sprites,
  comprueba que todos los muebles sean accesibles desde la entrada
  y verifica que las salidas no estén bloqueadas.

## Alcance y límites actuales

Esta es una **vista de prueba** del nuevo sistema. Los interiores todavía
no sustituyen todas las casas/tiendas/centros del mundo principal. Por ahora
los muebles pueden inspeccionarse, pero no existe aún un NPC de tienda ni
una enfermera dentro de esta vista. Por ello los mostradores no activan
todavía la compraventa ni la curación de la aventura principal.

El jugador se representa con **un indicador gráfico de posición**, no con
un personaje inventado. Cuando se reciban sprites del protagonista y
los NPC, se reemplazará el indicador y se añadirán interacciones reales.

No se ha eliminado el motor de los 100 mapas ni se ha alterado el
formato de las partidas guardadas. El atlas exclusivo se integrará
progresivamente después de validar las nuevas escenas.

**Derechos:** el atlas original se facilitó para este trabajo, pero
no se ha verificado una licencia pública de redistribución. Antes
de publicar el juego, comprueba que su licencia permite hacerlo.
