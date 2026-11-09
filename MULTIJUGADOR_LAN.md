# Multijugador LAN

## Estado de esta primera integración

Se añade una capa de transporte TCP (Proyecto.RedLAN) y una entrada en el menú principal para alojar una partida o conectarse a una IP. El transporte usa mensajes de texto delimitados por líneas y codifica los campos de usuario; no transmite objetos Java serializados.

- Puerto predeterminado: **47821/TCP**.
- Anfitrión: abre un servidor escuchando en todas las interfaces de red.
- Cliente: conecta con la IP y puerto del anfitrión.
- Se incluyen mensajes para anunciar posiciones, listar jugadores remotos, solicitar/rechazar desafíos y transportar datos de combate.

## Conexión en la misma red

1. Ejecuta el juego en el equipo anfitrión y selecciona **5. Multijugador LAN**.
2. Selecciona **1. Crear partida** y comparte la IP local que aparece en pantalla.
3. En el otro equipo selecciona **5. Multijugador LAN**, después **2. Unirse** e introduce esa IP.
4. Permite Java en el cortafuegos para conexiones TCP entrantes al puerto 47821.

## Conexión online mediante IP pública

En el menú **5. Multijugador**, elige **3. Crear partida online** en el equipo anfitrión. El juego intenta mostrar la IP pública mediante un servicio externo; si no puede consultarlo, puedes comprobarla desde el navegador. En el equipo invitado elige **4. Unirse por IP pública** e introduce esa dirección.

El anfitrión debe configurar en su router una regla de reenvío de **TCP 47821** hacia la IP local de su ordenador y permitir ese puerto en el cortafuegos. El cliente introduce la IP pública del anfitrión y el mismo puerto TCP elegido al crear la sala. Algunos proveedores usan CGNAT; en ese caso el reenvío de puertos convencional no funcionará y será necesario usar una VPN de red privada que sitúe ambos equipos en la misma LAN virtual.

No compartas la IP pública en repositorios o capturas públicas.

## Alcance pendiente

Esta entrega establece el transporte y el flujo de conexión. La sincronización visual en tiempo real del mapa, el control de autoridad del anfitrión, la persistencia de una sesión compartida y la ejecución completa de combates PvP (validación de turnos, equipos, daño y resultados) requieren integración adicional con los bucles de exploración y combate existentes. No debe considerarse todavía una experiencia cooperativa/PvP terminada.

## Seguridad

Conectar solo con personas de confianza: esta versión no incorpora autenticación, cifrado TLS, protección contra mensajes maliciosos ni recuperación de sesión. No abrir el puerto a Internet en redes sensibles.
