# Crianza, IVs, sexos y megaevolución

Este documento describe las mecánicas añadidas a Pokémon, al guardado de partida y a los menús de combate.

## Dónde está cada función

- Menú principal, opción **10. Criadero Pokémon**: criar y consultar sexo, IVs y tiempo restante de cada Pokémon.
- Menú principal, opción **11. Centro de Megaevolución**: ver y comprar megapiedras, equiparlas o retirarlas.
- Durante el combate, una vez elegido «Atacar», la opción **0. Mega-evolucionar** usa el turno completo. Solo se permite una megaevolución por lado y combate.
- Las estadísticas del equipo y del Centro Pokémon muestran el sexo, los seis IVs, ataque/defensa especial, megapiedra y cooldown.

## IVs y estadísticas individuales

Cada Pokémon tiene seis IVs independientes, entre **0 y 31**: PS, ataque, defensa, ataque especial, defensa especial y velocidad. Los IVs se generan al crear o capturar un Pokémon y se guardan con la partida. Los Pokémon de partidas antiguas migran sus antiguos valores IV al nuevo rango al cargarse.

Los IVs participan en los stats individuales del juego. Para las estadísticas que el juego no tenía separadas, se usan los valores especiales de la especie cuando existen y se recurre a ataque/defensa base como compatibilidad con los datos antiguos.

## Sexo y compatibilidad

El sexo individual es **Macho**, **Hembra**, **Ambos (Ditto)** o **Sin género**. Las probabilidades macho/hembra y los grupos huevo se incorporan como datos locales para las especies de la Pokédex del juego; no hace falta conexión de red durante la partida.

- Dos Pokémon no Ditto necesitan sexos complementarios y al menos un grupo huevo compartido.
- Ditto puede criar con cualquier especie apta que no sea otro Ditto; funciona como ambos sexos.
- Las especies con grupo huevo **Undiscovered** no pueden criar.
- La cría corresponde a la especie base de la madre. Cuando uno de los padres es Ditto, la determina el otro progenitor.
- La cría nace al **nivel 1**, con un sexo asignado según la proporción de su especie.

### Herencia de los IVs

Cada uno de los seis IVs de la cría se calcula como la media de los IVs de ambos padres, redondeada al entero más cercano:

`IV_cría = redondear((IV_padre_1 + IV_padre_2) / 2)`

Por ejemplo, si los padres tienen 10 y 21 de IV de ataque, la cría recibe 16.

### Cooldown de crianza

Después de una crianza válida, **ambos progenitores** tienen 30 minutos de cooldown de tiempo real. El final del cooldown se guarda como marca de tiempo Unix dentro del propio Pokémon serializado; cerrar el juego no reinicia la espera. Las parejas incompatibles o los intentos durante cooldown no consumen el tiempo.

## Mega evolución

El catálogo contiene **93 formas Mega** con especie de origen, forma, tipos, seis estadísticas base, habilidad y megapiedra. Las formas se registran en la Pokédex para poder buscarlas y se excluyen de los encuentros aleatorios normales.

La mayoría se activa con la megapiedra compatible equipada. El Centro de Megaevolución permite comprar las piedras que aún no están en la mochila por 2.500 €. No se permite equipar una misma piedra a dos Pokémon a la vez. Rayquaza tiene el requisito especial representado por **Ascenso Draco** en este juego.

Una megaevolución es temporal: aplica las estadísticas/tipos/habilidad de la forma y vuelve a la especie normal al salir del combate. El límite global evita megaevolucionar más de un Pokémon del mismo lado en el mismo combate. Los cambios conservan los IVs, sexo y cooldown del individuo.

## Catálogo de formas

1. Venusaur-Mega, Charizard-Mega-X, Charizard-Mega-Y, Blastoise-Mega, Beedrill-Mega, Pidgeot-Mega, Raichu-Mega-X, Raichu-Mega-Y
2. Clefable-Mega, Alakazam-Mega, Victreebel-Mega, Slowbro-Mega, Gengar-Mega, Kangaskhan-Mega, Starmie-Mega, Pinsir-Mega
3. Gyarados-Mega, Aerodactyl-Mega, Dragonite-Mega, Mewtwo-Mega-X, Mewtwo-Mega-Y, Meganium-Mega, Feraligatr-Mega, Ampharos-Mega
4. Steelix-Mega, Scizor-Mega, Heracross-Mega, Skarmory-Mega, Houndoom-Mega, Tyranitar-Mega, Sceptile-Mega, Blaziken-Mega
5. Swampert-Mega, Gardevoir-Mega, Sableye-Mega, Mawile-Mega, Aggron-Mega, Medicham-Mega, Manectric-Mega, Sharpedo-Mega
6. Camerupt-Mega, Altaria-Mega, Banette-Mega, Chimecho-Mega, Absol-Mega, Absol-Mega-Z, Glalie-Mega, Salamence-Mega
7. Metagross-Mega, Latias-Mega, Latios-Mega, Rayquaza-Mega, Staraptor-Mega, Lopunny-Mega, Garchomp-Mega, Garchomp-Mega-Z
8. Lucario-Mega, Lucario-Mega-Z, Abomasnow-Mega, Gallade-Mega, Froslass-Mega, Heatran-Mega, Darkrai-Mega, Emboar-Mega
9. Excadrill-Mega, Audino-Mega, Scolipede-Mega, Scrafty-Mega, Eelektross-Mega, Chandelure-Mega, Golurk-Mega, Chesnaught-Mega
10. Delphox-Mega, Greninja-Mega, Pyroar-Mega, Floette-Mega, Meowstic-M-Mega, Malamar-Mega, Barbaracle-Mega, Dragalge-Mega
11. Hawlucha-Mega, Zygarde-Mega, Diancie-Mega, Crabominable-Mega, Golisopod-Mega, Drampa-Mega, Magearna-Mega, Zeraora-Mega
12. Falinks-Mega, Scovillain-Mega, Glimmora-Mega, Tatsugiri-Curly-Mega, Baxcalibur-Mega

## Archivos principales

- `src/Proyecto/PokemonLuchador.java`: sexo, seis IVs, stats individuales, cooldown persistente y estado de megaevolución.
- `src/Proyecto/SexoPokemon.java`: enum de sexo.
- `src/Proyecto/GeneticaPokemon.java`: grupos huevo, proporciones de sexo y preevoluciones.
- `src/Proyecto/SistemaCrianza.java`: criadero, compatibilidad, media de IVs y cooldown.
- `src/Proyecto/MegaEvoluciones.java`: formas Mega, megapiedras, gestión y activación en combate.
- `src/Proyecto/PokemonBase.java`: metadatos de las formas Mega.
- `src/Proyecto/Combate.java` y `CombateEntrenador.java`: elección en batalla y restauración de la forma normal al terminar.
- `tests/SistemaCrianzaMegaSmokeTest.java`: prueba de catálogo, sexos, compatibilidad, crianza, IVs, serialización, cooldown y megaevolución.
- `.github/workflows/pokemon-genetics-mega.yml`: compila el código del juego y ejecuta esas pruebas con Java 17.

## Fuentes de datos

El catálogo de formas se contrastó con la información pública de [Pokémon Legends: Z-A — Mega Pokémon](https://legends.pokemon.com/en-us/mega-pokemon) y sus formas disponibles. Los grupos huevo, proporciones de sexo, preevoluciones y datos de formas se incorporan al código para no requerir Internet en tiempo de ejecución. La lista de formas y sus valores se puede revisar en `MegaEvoluciones.java`.


## Objetos equipables y herencia dirigida de IVs

La tienda ofrece seis objetos de crianza, uno por atributo:

| Objeto | IV que hereda directamente del portador |
|---|---|
| Pesa recia (IV PS) | PS |
| Brazal recio (IV Ataque) | Ataque |
| Cinto recio (IV Defensa) | Defensa |
| Lente recia (IV At. Esp.) | Ataque especial |
| Banda recia (IV Def. Esp.) | Defensa especial |
| Franja recia (IV Velocidad) | Velocidad |

Cuestan 1.800 € cada uno. Equipa el objeto al progenitor que tiene el IV deseado antes de criar. Ese IV de la cría será exactamente el del portador; los otros cinco siguen usando la media redondeada de ambos padres. Si los dos llevan un objeto que fuerza el mismo IV, se elige aleatoriamente cuál de los dos lo transmite. Los objetos se guardan en la mochila/partida como el resto de equipables.

## Editor de mapas: biblioteca y pueblos

En Pokémon Studio, abre **Editor de mapas → Crear objetos**. Cada objeto puede clasificarse como Casa/edificio, Centro Pokémon, Gimnasio, Tienda, Árbol/vegetación, Roca/obstáculo, Decoración u Otro. La categoría se guarda dentro del proyecto `.pmap`; los proyectos anteriores siguen siendo compatibles y su categoría se infiere a partir del nombre.

En **Generar mapa → Pueblo / ciudad**, puedes decidir si el generador coloca casas, Centro Pokémon, gimnasio y tienda, además de controlar la densidad de vegetación y el tamaño/semilla del mapa. Crea al menos un objeto con cada categoría que quieras usar. Los edificios conservan una zona de acceso y las colocaciones no deben solaparse.

### Caminos y esquinas automáticas

En la paleta de tiles, asigna el tipo correspondiente a las variantes que tengas dibujadas en tu tileset: recta horizontal/vertical, final norte/este/sur/oeste, esquinas NE/NO/SE/SO, esquinas interiores/exteriores, cruces en T y cruce de cuatro direcciones. Al generar el camino, el editor detecta las conexiones de los tiles y sustituye el tile de camino base por la variante que corresponda cuando exista en la paleta. Si no has importado una variante concreta, usa el tile de camino normal como respaldo. Para que las esquinas queden visualmente correctas, importa las variantes del mismo estilo gráfico y clasifica cada PNG en la paleta.
