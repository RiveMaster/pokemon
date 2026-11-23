# Referencia de paisajes: tiles.jpg (304 × 128)

## Qué aparece en la referencia del usuario

La imagen **no representa un mosaico rectangular homogéneo**. Su fondo
gris cuadriculado es una guía visual y algunos elementos están diseñados
para ocupar *varias* casillas. No conviene recortar directamente cada
subcuadro de 32 × 32, porque partiría árboles y cornisas a mitad.

Lectura de izquierda a derecha (posiciones aproximadas):

1. **Claro arenoso** (extremo izquierdo, x≈16–59, y≈26–83):
   tierra beige irregular con reborde de césped verde. Es transitable.
2. **Sendero de tierra** (x≈64–82, y≈61–110):
   suelo ocre, que se une al terreno del claro y se estrecha en los extremos.
3. **Pino verde principal** (x≈89–134, y≈35–114):
   copa triangular en varios niveles, tronco y sombra. Objeto de
   altura mayor que una sola casilla y con base bloqueada.
4. **Meseta verde** (x≈145–197, y≈45–111):
   cima de césped y paredes de roca marrón con sombra bajo el borde;
   no es un rectángulo de tierra plana.
5. **Sendero vertical estrecho** (x≈196–227, y≈65–113):
   suelo terroso con orillas verdes, junto a la roca.
6. **Flores y matas pequeñas** (x≈188–238, y≈45–70):
   decoración superpuesta a la hierba; no bloquea el paso.
7. **Arbusto redondeado** (x≈227–260, y≈44–73):
   objeto sólido de vegetación baja.
8. **Pino verde oscuro** (x≈250–294, y≈35–104):
   variante de silueta más oscura que da variedad al bosque.
9. **Firma «ZEO»** (esquina inferior derecha):
   marca gráfica no utilizada como terreno.

Estos valores son aproximados: la compresión JPEG y la guía de
tablero impiden identificar coordenadas de tiles originales con precisión.

## Reglas de implementación

| Elemento | Tamaño lógico | Vecindad | Colisiones |
| --- | --- | --- | --- |
| Claro de tierra | 1 tile repetible (32×32) | N, E, S, O y diagonales | Transitable |
| Sendero | 1 tile repetible (32×32) | Eje y bordes conectados | Transitable |
| Pinos | 1×2 tiles (32×64) | Se coloca la copa sobre el tronco | Dos casillas sólidas |
| Meseta rocosa | Región de tiles 32×32 | Pared visible solo sin vecino sur | Sólida |
| Flores y hierba | 1 tile 32×32 | Variantes por semilla | Transitable / hierba alta |
| Arbustos | 1 tile 32×32 | Separación de senderos | Sólido |
| Agua | 1 tile 32×32 | Mantiene la lógica de navegación previa | Según equipo |

Las máscaras usan `N=1, E=2, S=4, O=8`, y las cuatro diagonales
complementan las esquinas. Se dibujan las celdas individuales sin perder
la continuidad de los bordes: `TerrenosConVecinos32.claro()` y
`TerrenosConVecinos32.plataforma()`.

## Procedencia de los gráficos

La imagen compartida es una **referencia visual** y no se publica como
asset redistribuible. Tiene firma, pero la licencia y el autor no están
verificados. Para la implementación se emplean:

- Gráficos nuevos generados por `TerrenosConVecinos32.java`.
- Pinos CC0 del repositorio
  https://github.com/JaiGod/rpg-pixel-pack
  (véase `Tiles/RPGPixelPack/README.md`).

Se han incluido pinos normales, nevados, otoñales y floridos para que los
biomas tengan variedad. Se usan solo las piezas correspondientes al
entorno.

## Integración en el mundo

`MapasRegion2D.Estado` integra `claro`, `meseta` y `pinoAlto`,
respetando el mapa existente y sus casillas. El bosque genera claros,
una cornisa y árboles de dos casillas. La montaña recibe coníferas
nevadas. Los pasillos de salida se reservan para no impedir la
exploración; los edificios y los eventos siguen igual.

**Comprobación automática:** `TerrenosConVecinosSmokeTest` verifica
el cálculo de máscaras, las diferencias entre bordes e interiores,
las dimensiones 32×64 de los pinos y el comportamiento de colisión
de la meseta y del claro. `GeneracionesMapasSmokeTest` recorre los
100 mapas y confirma las salidas/entradas transitables.
