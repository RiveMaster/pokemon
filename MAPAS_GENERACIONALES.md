# Atlas generacional 2D: 100 mapas jugables

Este proyecto genera **diez mapas distintos por cada una de las diez generaciones**:
**100 escenarios originales**. La inspiración es la lectura visual de los
RPG de monstruos de Game Boy Advance (top-down, pixel-art, siluetas muy claras
y sprites desde cuatro ángulos), no los sprites ni los mapas comerciales.

## Cómo visitarlos dentro del juego

1. Inicia o carga una partida.
2. Abre **Menú del entrenador → Atlas regional (10 mapas)**.
3. Elige uno de los diez mapas de tu generación.
4. Camina con **WASD**, flechas o la cruceta de pantalla.
5. Para salir del escenario, pulsa **Esc** y elige otro mapa.
6. Puedes cambiar de generación en **Pueblo Villaverde**, siempre que
   la hayas desbloqueado con la progresión normal.

El atlas aparece también en el menú de Villaverde. Las entradas del Centro
Pokémon y la tienda llevan a sus servicios reales. El gimnasio regional
no concede medallas fuera de una ciudad que tenga un líder de gimnasio.

Las ubicaciones originales de la aventura, incluidos los pueblos y rutas
y sus mapas `.pmap` de Pokémon Studio, siguen funcionando. Cuando no existe
un mapa personalizado, el juego genera uno con los gráficos y la paleta de
la generación actual. Los archivos de partidas siguen guardando las
coordenadas lógicas de las casillas, por separado para cada región.

## Diez tipos de mapa por generación

| Número | Estilo | Diferencias jugables |
| --- | --- | --- |
| 01 | Pueblo | Centro, tienda, casas y calles de piedra |
| 02 | Bosque | Bosques espesos, arbustos y hierba alta |
| 03 | Costa | Arena, agua, rocas y vegetación |
| 04 | Montaña | Senderos rocosos, nieve y columnas |
| 05 | Lago | Dos lagunas, bancos de arena y árboles |
| 06 | Ruinas | Plataformas de piedra, muros y columnas |
| 07 | Ciudad | Calles, edificios de gran tamaño y gimnasio |
| 08 | Pantano | Canales, hierba alta y puentes |
| 09 | Cueva | Roca oscura, columnas y senderos |
| 10 | Jardín | Paseos, flores, estanques y arbustos |

Cada generación tiene su **propia paleta**, su propio nombre para los
diez lugares y una semilla determinista que define la colocación
del entorno. La prueba automática exige **100 identificadores y 100
composiciones visuales diferentes**, más caminos transitables entre
las salidas laterales y accesos a las puertas.

## Recursos visuales y formatos

- **ArteRegion2D.java**: terreno y fachadas originales de 32×32 píxeles
  por casilla con paletas distintas para cada generación.
- **EntrenadorRegion2D.java**: protagonista original de 32×48 píxeles,
  doce fotogramas (3 pasos en 4 orientaciones).
- **MapasRegion2D.java**: capas, edificios, hierba alta, agua y colisiones.
- **CatalogoMapasGeneracionales.java**: los 100 nombres, biomas, semillas
  e identificadores.
- **ExploradorMapa.java**: dibujo a escala entera 2x y movimientos del
  entrenador; mantiene la lectura de mapas históricos a 16 píxeles.
- **AtlasRegional.java**: selección gráfica de los diez mapas de cada gen.

Las ilustraciones se generan por código: no requieren conexión ni imágenes
extraídas de videojuegos oficiales.

## Obtener los 100 PNG (sin instalar otra herramienta)

GitHub Actions ejecuta `ExportadorMapasGeneracionales` tras pasar
la batería de pruebas y publica un archivo descargable llamado
**mapas-2d-100-generaciones** en la sección *Artifacts* de la ejecución.

El paquete contiene:

- `MapasGenerados/GEN01/` hasta `GEN10/`.
- Diez PNG por generación, con resolución **1152×768**.
- `GALERIA_GENERACION.png` en cada carpeta, con diez vistas en mosaico.

También se pueden generar manualmente (requiere Java):

```bash
mkdir -p build
find src -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d build
java -Djava.awt.headless=true -cp build Proyecto.ExportadorMapasGeneracionales
```

Las imágenes PNG son vistas renderizadas del mismo modelo que recorre el
jugador, **no sustitutos del mapa interactivo**. Para modificar un mapa
personalizado, puedes seguir usando Pokémon Studio y su formato `.pmap`.

## Compatibilidad y pruebas

`GeneracionesMapasSmokeTest` comprueba automáticamente:

- 10 generaciones × 10 mapas = 100.
- Identificadores, biomas y firmas visuales sin duplicados.
- Tiles de 32×32 y entrenador de 32×48.
- Camino entre ambas salidas del escenario.
- Puertas transitables y conectadas desde la calle.

Se conservan las pruebas previas de guardado lógico, zonas y animaciones.
