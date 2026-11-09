# Integración de sprites de SpriteCollab

El proyecto integra retratos Pokémon de [PMDCollab/SpriteCollab](https://github.com/PMDCollab/SpriteCollab) mediante `Proyecto.SpriteCollabUI`.

## Dónde aparecen

- **Combates:** al actualizarse el estado del combate se abre/actualiza una ventana Swing con los dos Pokémon, nivel y PS.
- **Equipo:** al mostrar la lista del equipo se abre una cuadrícula de retratos.
- **PC:** al mostrar la lista del PC se abre una cuadrícula de retratos.

La interfaz de consola sigue funcionando y no necesita conexión para continuar la partida. Los PNG se descargan bajo demanda en segundo plano y se guardan en `~/.pokemon/spritecollab`; después se reutilizan desde la caché.

## Fuente, licencia y atribución

- Fuente de los recursos: [SpriteCollab](https://sprites.pmdcollab.org/) y [repositorio PMDCollab/SpriteCollab](https://github.com/PMDCollab/SpriteCollab).
- Los retratos de la comunidad están sujetos a **CC BY-NC 4.0** (atribución, uso no comercial y compartir bajo los términos aplicables). Que el videojuego sea gratuito y de código abierto no elimina la obligación de atribuir a los artistas ni permite usos comerciales.
- La integración descarga el archivo `credits.txt` de cada retrato y muestra su contenido en la información emergente de la imagen. Conserva esos créditos junto a los recursos redistribuidos.
- Revisa los términos del repositorio y las condiciones de cada contribución antes de publicar un paquete que incluya los PNG. Esta integración descarga los recursos bajo demanda en lugar de copiarlos masivamente al repositorio del juego.

## Limitaciones actuales

La primera integración utiliza el retrato `Normal.png` de cada especie y una caché local. No importa todavía las hojas de animación de combate `sprite/*/AnimData.xml`; esas animaciones requieren un reproductor de fotogramas y selección de estados (reposo, ataque, daño, etc.). Las formas que no tengan retrato específico recurren al número de Pokédex de su especie base cuando el juego las reconoce.
