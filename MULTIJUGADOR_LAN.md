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

Al crear la partida online, el juego intenta abrir automáticamente **TCP 47821** en el router mediante UPnP IGD y elimina la regla al cerrar la sesión. Para que funcione, UPnP debe estar activado y permitido en el router. Si falla, configura manualmente el reenvío TCP 47821 hacia la IP local del ordenador y permite Java en el cortafuegos. El cliente introduce la IP pública del anfitrión y el mismo puerto TCP elegido al crear la sala. Algunos proveedores usan CGNAT o doble NAT; en ese caso UPnP en el router doméstico no bastará y será necesario solicitar una IP pública al proveedor o usar una VPN de red privada que sitúe ambos equipos en la misma LAN virtual.

No compartas la IP pública en repositorios o capturas públicas.

## Alcance pendiente

Esta entrega establece el transporte y el flujo de conexión. La sincronización visual en tiempo real del mapa, el control de autoridad del anfitrión, la persistencia de una sesión compartida y la ejecución completa de combates PvP (validación de turnos, equipos, daño y resultados) requieren integración adicional con los bucles de exploración y combate existentes. No debe considerarse todavía una experiencia cooperativa/PvP terminada.

## Seguridad

Conectar solo con personas de confianza: esta versión no incorpora autenticación, cifrado TLS, protección contra mensajes maliciosos ni recuperación de sesión. No abrir el puerto a Internet en redes sensibles.


## Jugar online detrás de CGNAT sin alojar un servidor de Pokémon externo

UPnP no puede atravesar CGNAT. Para mantener el ordenador del anfitrión como servidor del juego, se puede usar una red privada virtual punto a punto como **Tailscale**:

1. Instalad Tailscale en el ordenador anfitrión y en cada ordenador invitado.
2. Iniciad sesión y añadid todos los equipos a la misma red privada (tailnet).
3. El anfitrión crea la partida desde **6. Multijugador online**. El juego intenta detectar y mostrar su IPv4 de Tailscale.
4. El invitado elige **Unirse a una partida** e introduce esa IP Tailscale (normalmente del rango 100.64.0.0/10). Se usa el puerto TCP 47821.
5. Mantened Tailscale y el juego abiertos mientras dure la partida.

Tailscale intenta una conexión directa entre los equipos y puede usar su infraestructura DERP como transporte cifrado de respaldo cuando la conexión directa no es posible. DERP no aloja la partida ni ejecuta lógica del juego: el servidor de Pokémon sigue siendo el ordenador anfitrión. No obstante, Tailscale sí requiere su servicio de coordinación y no equivale a una conexión P2P autónoma sin infraestructura alguna.

La detección de IP Tailscale en el juego es automática, pero la instalación, el inicio de sesión y la pertenencia a la misma red privada deben configurarse en ambos equipos. Si no aparece una IP Tailscale, comprueba que la aplicación esté conectada y que la interfaz esté activa.

**Limitación:** esta alternativa proporciona conectividad de red a través de CGNAT; no completa por sí sola la sincronización del mundo ni los combates PvP, que siguen requiriendo integración en los bucles del juego.
