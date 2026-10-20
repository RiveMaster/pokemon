# Pokémon Studio → Java

Paquete: `src/Proyecto/studio/`. Sin dependencias externas (JSON propio).

## Fase 1 · Pokémon, movimientos y tipos
`DatosEspecie`/`DatosForma`, `DatosMovimiento`/`DatosEfecto`, `DatosHabilidad`, `DatosObjeto`,
`TipoPokemon`, `TablaTipos`, `CurvaExperiencia`, `Estadistica`, `CategoriaMovimiento`.

## Fase 2 · Mundo
| Clase | Qué modela | Origen en el juego |
|---|---|---|
| `DatosZona` (+ `ClaseZona`) | Rutas, poblaciones y pueblo de inicio, con el camino lineal anterior/siguiente | `Ubicacion`, `Zona`, `Pueblo`, `Mapa` |
| `DatosGimnasio` | Gimnasios: líder, 3 miembros, medalla, MT, nivel base | `Gimnasio` |
| `DatosJefe` | Jefe del Gobierno de cada generación | `Gobierno.Jefe` |
| `DatosGeneracion` | Rango de la Pokédex y bonus de nivel | `Generacion` |
| `DatosReglas` | Probabilidad/nombres/frases de NPC, premios, Pokémon excluidos de salvajes | constantes de `Npc`, `Gimnasio`, `Gobierno`, `Generacion` |
| `DatosObjeto` | Objetos de la tienda y equipables | `Tienda`, `ObjetoEquipable` |

`BaseDatos.validar()` comprueba referencias cruzadas: evoluciones, movimientos, camino de zonas
(alcanzable desde el inicio, sin ciclos), un gimnasio por ciudad, MT de recompensa, jefe por generación...

## Fase 3 · Editor en consola
    java Proyecto.studio.Estudio editor data/studio

Menú principal: Pokémon, Movimientos, Habilidades, Objetos, Tabla de tipos, Zonas, Gimnasios, Jefes,
Generaciones, Reglas, Validar, Guardar, Recargar y Reimportar. Convenciones:
- **Enter mantiene el valor actual**; en listas, `-` las vacía; en referencias, `0` quita y un texto busca.
- Los textos con comas (frases) se separan con `|`.
- Crear y editar usan el mismo formulario. Los símbolos (`db_symbol`) se generan solos y no cambian al renombrar.
- **Borrar limpia las referencias** (borrar un movimiento lo quita de todos los Pokémon y de las MT de gimnasio;
  borrar una especie quita las evoluciones hacia ella...) y antes te muestra qué se verá afectado.
- Zonas: al guardar una zona ofrece reenlazar a sus vecinas (insertar en el camino); al borrarla, las une.
- Nada se escribe en disco hasta `Guardar`, que valida primero y avisa si hay problemas.
- Se puede llamar desde el juego: `EditorConsola.abrir(Path.of("data/studio"), scanner);`
  (solo lee líneas completas, así que puede compartir el `Scanner` del juego).

## Uso
    java Proyecto.studio.Estudio importar data/studio   # juego -> JSON
    java Proyecto.studio.Estudio validar  data/studio
    java Proyecto.studio.Estudio resumen  data/studio
    java Proyecto.studio.Estudio probar                 # ida y vuelta + comparación con la Pokedex

## Cambios en tu código original
- Getters nuevos: `Movimiento.getEfecto()`, `Efectos.getEstado()/getProbabilidad()`, `Ataques.getTodos()`,
  `Pueblo` (descripción, tipos, rutas), `Gimnasio` (título, recompensas, nivel, equipo),
  `Gobierno.Jefe` (cargo, tipos, nivel, derrota, memes), `Generacion` (desde, hasta, excluidos),
  `Npc` (probabilidad, nombres, frases).
- Datos corregidos: tipos `Phsyc`→`Psychic` y `Volador`→`Flying` en `Ataques`; evoluciones de
  Smoochum, Elekid, Magby, Cascoon y Skiddo; nivel de evolución de Thievul y Eldegoss a 0 (son finales).

## Fase 4 · El juego lee de la base de datos
Al arrancar, `Aventura` llama a `DatosJuego.iniciar()`: si existe `data/studio/pokemon.json` carga la carpeta,
la **valida** y sustituye los datos del juego; si no existe o algo falla, el juego sigue con sus datos integrados
(y lo avisa). En el menú principal hay una opción **3. Editor de datos**: al cerrarlo se aplican los cambios guardados.

| Se lee de los JSON | Clase del juego |
|---|---|
| Pokémon (nombre, tipos, stats base, evolución, movimientos por nivel), orden por id | `Pokedex` |
| Movimientos (nombre, tipo, potencia, efecto y estado) | `Ataques` |
| Rutas y pueblos (nombre, descripción, tipos, niveles, NPC, casino, sede, camino) | `Mapa`, `Zona`, `Pueblo`, `Ubicacion` |
| Gimnasios, jefes del Gobierno, generaciones (región, rango, bonus de nivel) | `Gimnasio`, `Gobierno`, `Generacion` |
| Reglas: NPC (probabilidad, nombres, frases, premio), miembros por gimnasio, jefe, excluidos | `Npc`, `Gimnasio`, `Gobierno`, `Generacion` |
| Precios de Poke Ball, Poción, MT y objetos equipables (precio y descripción) | `Tienda`, `ObjetoEquipable` |

Garantías: `aplicar()` valida y construye todos los objetos **antes** de tocar el juego; una base inválida lanza
`IllegalStateException` y el juego queda como estaba. Las categorías vacías (gimnasios, jefes, mapa) mantienen las del juego.

Opciones: `-Dstudio=off` desactiva la carga · `-Dstudio.dir=ruta` usa otra carpeta.

### Límites conocidos
- **Lugares del mapa:** se pueden editar los 23 originales y crear **hasta 24 nuevos** (ver «Lugares nuevos»). Quitar un
  lugar del camino es seguro: las partidas guardadas allí vuelven a Villaverde. No hay más de 10 generaciones.
- **Datos que el juego no usa por defecto:** ataque/defensa especiales, categoría, PP, precisión, prioridad y habilidades
  se guardan siempre, pero solo actúan si activas las reglas de combate (ver «Reglas de combate»). Altura, peso, captura,
  curva de experiencia y formas extra siguen sin tener efecto en el juego.
- **Movimientos de potencia aleatoria:** `Doblebofeton`, `Puino cometa`, `Ataque Furia` y `Pin Misil` calculaban su
  potencia con `Math.random()` al arrancar. Con los datos de Studio su potencia es **fija** (la del JSON, editable).
- Los Pokémon de gimnasio/jefe se eligen por tipo y generación como antes; lo que cambia es la lista de Pokémon y tipos.

### Pruebas
    java Proyecto.studio.Estudio probar         # JSON ida y vuelta + regeneración de la Pokedex
    java Proyecto.studio.Estudio probar-juego   # aplica datos modificados y comprueba que el juego los usa

## Fase 5 · Editor gráfico (Swing)
    java Proyecto.studio.Estudio gui data/studio
    (o, desde el menú del juego: «4. Editor de datos (ventana gráfica)»)

Ventana con una pestaña por categoría: Pokémon, Movimientos, Habilidades, Objetos, Tipos, Zonas, Gimnasios, Jefes,
Generaciones y Reglas. Solo usa el JDK (Swing), sin librerías externas.
- **Lista con buscador + formulario** en cada pestaña; Nuevo / Guardar cambios / Descartar / Borrar.
- **Pokémon:** estadísticas con barras y total, tipos, EVs, cría y curva de experiencia, evoluciones (por nivel o por objeto),
  movimientos por nivel y listas MT/huevo/tutor, habilidades y formas (＋/−). Los Pokémon y movimientos se eligen con un
  selector con búsqueda, no con desplegables de 1000 elementos.
- **Tipos:** matriz 18×18 coloreada y editable (doble clic) y una calculadora de eficacia.
- **Zonas:** al guardar ofrece reenlazar a las vecinas (insertar en el camino); al borrar las une. Avisa si el lugar no
  existe en el juego, y muestra cuántas ranuras libres quedan para lugares nuevos.
- **Seguridad:** si cambias de registro con cambios sin guardar, pregunta; los valores fuera de rango de un control lo
  amplían en vez de recortarse en silencio; el título muestra `*` con cambios pendientes y al cerrar pregunta
  Guardar / Descartar / Cancelar; guardar valida primero.
- **Menús:** Archivo (Guardar Ctrl+S, Recargar del disco, Reimportar lo que el juego tiene cargado, Salir),
  Herramientas (Validar Ctrl+T), Ayuda.
- Necesita entorno gráfico; sin él, el menú del juego avisa y se puede usar el editor de consola (opción 3).

Pruebas: `java Proyecto.studio.Estudio probar-gui` (sin pantalla): cada formulario reproduce exactamente el registro que
carga (1025 Pokémon, movimientos, zonas...), editar/crear/borrar a través de los paneles, preguntar antes de descartar,
zonas, tipos, guardar y recargar.

## Reglas de combate (opcionales)
El combate original es muy simple: el daño es la potencia del movimiento (+ nivel/2 para el jugador); no hay tipos, estadísticas,
precisión, PP, estados ni habilidades. Por eso los datos de Studio que describen todo eso no tenían efecto. Ahora hay **seis
reglas independientes en `reglas.json`, todas desactivadas por defecto** (con todas apagadas el combate es exactamente el de
siempre). Se activan con las casillas «Combate» de la pestaña Reglas del editor gráfico, o en Reglas > «Reglas de combate»
del editor de consola.

| Clave en `reglas.json` | Qué hace |
|---|---|
| `eficacia_tipos` | El daño se multiplica por la tabla de tipos de los datos (x2, x0.5, x0, x4 con dos tipos) y se muestra el mensaje. |
| `efectos_movimientos` | Los movimientos aplican su estado alterado (dormido 2-3 turnos, paralizado, envenenado, quemado, congelado) con su probabilidad. `INSTA` deja K.O. Hoy **ningún** movimiento disparaba su efecto: la maquinaria de estados existía pero nada la llamaba. |
| `precision` | Un movimiento con precisión 1-99 puede fallar. Precisión 0 o 100 nunca falla. |
| `pp` | Cada movimiento tiene PP por Pokémon (se ven en el menú y se mantienen entre combates). Al agotarlos se usa **Forcejeo** (potencia 20, retroceso de 1/4 de la vida máxima). El Centro Pokémon (y despertar tras perder un combate) los restaura. **PP 0 = sin límite.** También rige para los rivales controlados por el juego. |
| `estadisticas_en_dano` | Los movimientos son físicos, especiales o de estado. El daño se multiplica por `ataque / defensa` (el especial usa ataque y defensa especiales), **acotado entre x0.5 y x2**. Sin dato de ataque/defensa especial (0) se usa el físico. Los movimientos de estado no hacen daño. |
| `habilidades_en_combate` | Las habilidades tienen efecto (ver abajo). |

**Por qué la fórmula de daño es un cociente acotado y no la oficial de Pokémon:** en este juego un golpe ya quita casi media vida y
las estadísticas apenas crecen con el nivel; la fórmula oficial lo desquiciaría. Multiplicar el daño actual por ataque/defensa
conserva el equilibrio y hace que las estadísticas importen.

**Habilidades.** Cada habilidad tiene un `efecto` (editable en la pestaña Habilidades):

| Efecto | Parámetros | Qué hace |
|---|---|---|
| `ninguno` | — | Solo informativa. |
| `potencia_tipo` | tipo, valor (1.5) | Con la vida al tercio o menos, sus ataques de ese tipo hacen daño x valor. |
| `inmunidad_tipo` | tipo | No recibe daño de ataques de ese tipo. |
| `contacto_estado` | estado, valor (% de 1 a 100) | Al recibir un golpe puede dejar al atacante en ese estado. |
| `regeneracion` | valor (16) | Al empezar cada turno recupera 1/valor de su vida máxima. |

Cada Pokémon recibe una de las habilidades de su especie (al azar, la primera vez que hace falta). No hay habilidad oculta en el
juego. El validador comprueba que cada efecto tenga sus parámetros.

**Partidas guardadas antiguas:** los movimientos y Pokémon guardados no tienen estos campos y se leen como «precisión 0 (nunca falla),
PP 0 (sin límite), sin habilidad», así que no cambian aunque actives las reglas (se comprueba en `probar-juego`).

**Qué no hace:**
- El **orden de turno** sigue siendo «primero el jugador»: la prioridad de los movimientos y la velocidad se guardan pero no se usan
  (habría que reescribir los cuatro bucles de combate).
- Los movimientos de **carga** (estado `CARGAR`) no están soportados.
- **No afectan** al duelo introductorio de Villaverde (`Combate.combateInicial`), que no pasa por el ataque normal.
- Los PP y el estado de un Pokémon no se reinician al subir de nivel (sí en el Centro Pokémon).

## Eficacia de tipos
Es la primera de las reglas de arriba. La tabla se edita en la pestaña **Tipos**. Código: `Proyecto.Tipos` (en el juego) y
`DatosJuego` (le pasa la tabla).

## Lugares nuevos (hasta 24)
`Ubicacion` sigue siendo un `enum` (se guarda por nombre dentro de las partidas, así que cambiarlo a otro tipo las rompería).
En su lugar, **al final del enum hay 24 ranuras libres, `EXTRA1`..`EXTRA24`**; añadir constantes al final no afecta a las
partidas guardadas con el código original (se comprueba en `probar-juego` con una partida generada con tu código sin tocar).

- Una zona cuyo símbolo sea `extra1`..`extra24` usa la ranura correspondiente. Los editores (consola y gráfico) asignan solos
  la primera ranura libre al crear una zona, y avisan si no quedan. Rutas, poblaciones y gimnasios nuevos funcionan igual que
  los originales: camino, vuelo, centro Pokémon, tienda, combates salvajes y de gimnasio.
- Para insertar un lugar en el camino: crea la zona con su anterior y siguiente, y el editor ofrece reenlazar a las vecinas.
- Toda zona (salvo la de inicio) necesita **zona anterior**: el juego siempre ofrece «Volver» (el validador lo exige).
- Al quitar un lugar nuevo su ranura queda libre y vuelve a llamarse «Lugar libre N». Una partida guardada en ese lugar
  vuelve a Villaverde; y si luego reutilizas la ranura para otra zona, esa partida aparecerá en la zona nueva.
- La salida de Villaverde ya no está fija en la Ruta 1: lleva a la primera ruta del camino de los datos.

## Nota sobre `slot1.txt`
El `slot1.txt` que venía en el zip está **corrupto de origen** (la cabecera de serialización de Java `AC ED` aparece como
`EF BF BD`, señal de un binario que pasó por un conversor de texto), así que no se puede cargar. Tampoco `Jugador`, que está
vacío. Si tus partidas buenas están fuera del zip, no se ven afectadas. `data/pruebas/partida_original.bin` es una partida
válida generada con el código original, usada solo por las pruebas.

## Pendiente
- Orden de turno por velocidad y prioridad (obliga a reescribir los bucles de combate).
- Movimientos de carga, habilidad oculta, y un catálogo de habilidades más amplio que los cuatro efectos actuales.


## Editor gráfico de mapas por tiles (nuevo)
Lanzamiento desde la raíz del proyecto:
```sh
java Proyecto.studio.Estudio mapas
```
Al abrirlo pregunta el ancho y alto del mapa en tiles (de 1 a 300). Usa tiles de 16×16, lee PNG de `Tiles/` e importa spritesheets cuyos tamaños sean múltiplos de 16; omite tiles totalmente transparentes. En la pestaña «Tiles» permite crear tiles desde cero y editarlos píxel a píxel con un selector de color y borrado a transparencia. Cada tile puede marcarse como Normal, Bloquea el paso, Hierba (encuentros Pokémon), Agua o Evento. Al pintar tiles del suelo, las propiedades actualizan las banderas de colisión y hierba del mapa, que se muestran con «Ver propiedades». La pestaña «Crear objetos» permite diseñar y nombrar composiciones de varios tiles (árboles, casas, gimnasios) y colocarlas como sellos. También permite guardar/abrir proyectos `.pmap`, exportar el mapa compuesto a PNG y exportar los tiles individuales a carpetas. El proyecto conserva tiles, tipos, capas, colisiones, las marcas de hierba, objetos y colocaciones.

El botón **Generar mapa** abre un asistente procedural integrado. Permite elegir tamaño (1–300 tiles por lado), semilla reproducible, estilo (ruta/bosque, pueblo/ciudad, costa/lago o pradera), densidad y qué elementos generar. Se eligen miniaturas de tile para suelo, camino, hierba, agua y obstáculos; el generador les asigna las propiedades lógicas correspondientes. Si la biblioteca «Crear objetos» contiene objetos con nombres reconocibles como casas, gimnasios, árboles o rocas, se pueden reutilizar para colocar edificios y vegetación compuesta. El mapa generado reemplaza el mapa actual tras pedir confirmación, y luego se puede seguir editando o guardar como `.pmap`.

La generación es procedural y propia del proyecto; el ZIP de referencia solo contenía una aplicación Unity compilada, no el código fuente del generador original.

**Importante:** las propiedades de colisión y hierba se guardan dentro del proyecto `.pmap`, pero el juego actual todavía no carga estos proyectos. Para que el mundo se dibuje con estos tiles, las colisiones bloqueen el movimiento y aparezcan Pokémon salvajes al caminar por la hierba, falta integrar el formato con el motor del juego. El mapa lógico actual sigue siendo el de `Mapa`, `Zona`, `Pueblo` y `Ubicacion`.
