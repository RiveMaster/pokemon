# Referencia de diseño: Pokémon Essentials BES 3.1

Este documento registra patrones observados en el archivo de referencia `Essentials-BES-3.1.zip` y cómo conviene adaptarlos al proyecto Java. No propone copiar los scripts Ruby/RGSS directamente: pertenecen a otro motor y no son compatibles con Java/Swing.

## 1. Separación entre combate y presentación

En Essentials, la lógica del combate y su presentación están separadas:

- `Data/Scripts/011_Battle/012_PokeBattle_Battle.rb`: fases y reglas del combate. Por ejemplo, `pbCommandPhase` organiza la elección de acciones.
- `Data/Scripts/012_Battle scene/003_PokeBattle_Scene.rb`: escena, ventanas, mensajes, sprites y entrada del jugador.
- `Data/Scripts/012_Battle scene/006_PokeBattle_SceneCommands.rb`: menú principal y menú de movimientos.
- `Data/Scripts/012_Battle scene/005_PokeBattle_SceneDatabox.rb`: panel de estado y animación de PS/experiencia.

**Adaptación recomendada para Java:** mantener `Combate` como controlador de reglas/turnos y extraer de `SpriteCollabUI` componentes visuales independientes: panel de estado, menú de acciones, menú de movimientos, selector de equipo y cuadro de mensajes. La UI debe solicitar decisiones y devolverlas al controlador; no debe ejecutar daño, experiencia ni cambios de turno por su cuenta.

## 2. Flujo de acciones de combate

El menú de Essentials presenta cuatro decisiones habituales: Luchar, Mochila, Pokémon y Huir. La elección de Luchar abre un menú de movimientos independiente. El código conserva el último comando y el último movimiento seleccionados, permite cancelar y vuelve al menú anterior.

Referencias:
- `006_PokeBattle_SceneCommands.rb`, métodos `pbCommandMenu`, `pbCommandMenuEx` y `pbFightMenu`.
- `011_Battle/012_PokeBattle_Battle.rb`, método `pbCommandPhase`.

**Adaptación recomendada:** menú 2×2 para las acciones principales; submenú de cuatro movimientos con nombre, tipo, categoría y PP actual/máximo; navegación por teclado y ratón; cancelar vuelve al menú anterior sin consumir turno. La validación de PP y la ejecución del movimiento permanecen en la lógica del combate.

## 3. Paneles de estado y barras animadas

La clase `PokemonDataBox` dibuja nombre, nivel, género, estado, PS actuales/máximos y una barra de PS por tramos de color. También contempla experiencia y anima el cambio de PS entre el valor anterior y el nuevo mediante `animateHP(oldhp,newhp)`.

Referencia: `012_Battle scene/005_PokeBattle_SceneDatabox.rb`, clase `PokemonDataBox`, especialmente `refresh`, `animateHP` y `update`.

**Adaptación recomendada:** un componente de estado para cada combatiente, barra de PS animada y cambio de color según porcentaje. Actualizarlo después de cada daño, curación, cambio de estado o sustitución, sin reconstruir toda la ventana.

## 4. Mensajes que esperan confirmación

`pbDisplayPausedMessage` mantiene el cuadro de texto visible y espera una entrada de confirmación antes de continuar. Es el patrón usado para mensajes importantes; no equivale a imprimir texto y seguir automáticamente.

Referencia: `012_Battle scene/003_PokeBattle_Scene.rb`, método `pbDisplayPausedMessage`.

**Adaptación recomendada:** todos los eventos importantes —ataque, efectividad, debilitamiento, cambio de Pokémon y fin de combate— deben pasar por una cola de mensajes. Cada mensaje espera un clic o Enter/Espacio antes de avanzar. El controlador no debe abandonar el combate al debilitarse el Pokémon activo si todavía hay miembros sanos en el equipo.

## 5. Sustitución de Pokémon y fin de combate

El controlador de Essentials organiza las decisiones por fases y contempla la selección de un nuevo Pokémon cuando el activo cae debilitado. La escena se ocupa de presentar la elección y los mensajes, mientras la lógica decide si todavía hay Pokémon disponibles.

Referencias:
- `011_Battle/012_PokeBattle_Battle.rb`, `pbCommandPhase` y `pbEndOfBattle`.
- `012_Battle scene/003_PokeBattle_Scene.rb`, `pbDisplayPausedMessage`.

**Adaptación recomendada:** centralizar la comprobación de equipo disponible en un único método; si quedan Pokémon vivos, abrir el selector obligatorio y continuar el mismo combate. Solo terminar cuando el equipo completo esté debilitado o se cumpla otra condición real de finalización.

## 6. Recursos gráficos: separar uso y estado

La escena de batalla de Essentials carga sprites de combate y los muestra en posiciones distintas para jugador y rival; los paneles de estado se dibujan por separado. Esto favorece que cada recurso tenga un propósito visual concreto.

**Adaptación recomendada para el sistema SpriteCollab existente:**
- **Retrato/perfil:** iconos o retratos para mochila, equipo y PC.
- **Sprite de combate:** animación de pie/Idle y sus animaciones de acción.
- **Sprite debilitado:** animación Faint o pose final tumbada, que permanece mientras el Pokémon siga a 0 PS.

No se debe asumir que cualquier paquete contiene las tres imágenes como archivos independientes: hay que comprobar los recursos y metadatos de cada especie. Si falta una pose Faint, se mantiene una alternativa segura sin romper la carga del sprite.

## 7. Orden propuesto de implementación

1. **Estabilidad del flujo de combate:** sustitución obligatoria al debilitarse, fin solo cuando todo el equipo esté debilitado y mensajes con confirmación.
2. **Interfaz de batalla modular:** paneles de estado, menú de acciones y menú de movimientos separados del controlador.
3. **Información de movimientos:** PP actual/máximo y datos del movimiento, con navegación/cancelación coherentes.
4. **Animación de PS y sprites:** transiciones de barras, entrada/salida del Pokémon y pose Faint persistente.
5. **Mochila, equipo y PC:** reutilizar los mismos componentes de lista y retrato para mantener consistencia.

## Nota de compatibilidad

El ZIP es una distribución de Pokémon Essentials BES 3.1 basada en Ruby y RGSS. Sirve como referencia funcional y de diseño, pero sus clases, ventanas, recursos y APIs no se pueden incorporar directamente al juego Java. La implementación debe traducir esos patrones a las clases y recursos propios del repositorio.
