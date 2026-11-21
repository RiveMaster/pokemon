# Multijugador, VPN privada y chat en el mundo

La aventura comparte un mapa con otros jugadores por medio de `RedLAN.java`
(TCP, puerto **47821**). El protocolo **no ejecuta código remoto**: envía
coordenadas, nombres de jugador y texto de chat, con UTF-8 y Base64URL.

## Jugar mediante VPN privada con Tailscale

La VPN **no está implementada desde cero**: se integra la red Tailscale, que
usa el protocolo de túnel cifrado **WireGuard** y dispone de autenticación,
asignación de IP privada y opciones de acceso. Para evitar exponer el puerto
públicamente, el servidor del modo VPN escucha **solo en la IP local de
Tailscale** y no solicita UPnP.

1. En ambos equipos, instala Tailscale desde su fuente oficial:
   https://tailscale.com/download
2. Inicia sesión en ambos y asegúrate de que pueden comunicarse dentro
   de la misma tailnet. Puedes **invitar al otro jugador** o compartir
   específicamente el dispositivo anfitrión:
   https://tailscale.com/docs/features/sharing
3. En el menú inicial del juego pulsa **Multijugador VPN (Tailscale)**.
4. El anfitrión elige **Crear partida privada**. El juego detecta su IP
   Tailscale (rango 100.64.0.0/10), abre el servidor TCP en esa interfaz y
   muestra la dirección privada.
5. El otro jugador elige **Unirse con IP Tailscale** e introduce esa IP.
   El puerto es 47821 para ambas personas.
6. Abre una partida: los jugadores podrán verse al estar en el mismo mapa
   y generación. Los nombres se dibujan sobre los sprites.

El menú también permite **copiar la IP VPN**, abrir la descarga oficial o
consultar la guía de configuración. Si Tailscale no está instalado, el juego
no simula que existe una VPN ni solicita claves de acceso.

Tailscale debe instalarse y autorizarse por cada jugador. La aplicación no
genera por sí sola una VPN autónoma, un servidor público de intermediación
ni cuentas de Tailscale.

**Seguridad:** no compartas invitaciones de tailnet con desconocidos.
Puedes limitar quién tiene acceso al equipo anfitrión con las políticas
de Tailscale. El tráfico es cifrado por WireGuard dentro de la VPN; el
protocolo TCP del juego **no cifra por sí mismo** las conexiones LAN o
directas por Internet. Por eso recomendamos VPN frente a puertos públicos.

- Guía inicial: https://tailscale.com/docs/how-to/quickstart
- Privacidad y WireGuard: https://tailscale.com/docs/concepts/wireguard
- Acceso a dispositivos: https://tailscale.com/docs/features/sharing

## Chat integrado en la exploración

- **T** abre la entrada de mensajes.
- **Enter** envía el mensaje al anfitrión y a los demás participantes,
  y cierra el campo de escritura para volver a caminar.
- **Esc**, mientras se escribe, cancela el texto y devuelve el foco al mapa.
- También hay un botón gráfico **Chat (T)**.
- El historial muestra la hora, el remitente y el texto.
- Límite de 240 caracteres por mensaje, eliminación de saltos de línea y
  caracteres de control.
- Sin conexión activa, la ventana indica que primero hay que entrar en
  una partida LAN o VPN.
- El canal `CHAT` utiliza un buzón independiente de los eventos de batalla,
  de modo que el chat no consume solicitudes de combate.

## Mensajes del protocolo

- `JOIN|<nombre_base64url>`: cliente nuevo.
- `POS|<nombre_base64url>|<mapa_base64url>|<x>|<y>`:
  publicación de casilla.
- `STATE|<nombre_base64url>|<mapa_base64url>|<x>|<y>`:
  sincronización del estado de otros jugadores.
- `CHAT|<texto_base64url>`: mensaje del cliente al anfitrión.
- `CHAT|<nombre_base64url>|<texto_base64url>`: difusión del anfitrión.
- `DUEL_REQUEST`, `DUEL_DATA`, etc.: combate multijugador ya existente.

El servidor determina el nombre del remitente a partir del evento JOIN:
no confía en un nombre suministrado con cada mensaje de chat. El cliente
no ejecuta comandos contenidos en el texto recibido.

## Pruebas

`ChatVpnSmokeTest` abre un servidor y un socket en loopback, envía
mensajes en ambos sentidos, verifica el límite y los caracteres válidos,
y comprueba el rango de IP de Tailscale. La compilación Java 17 se valida
en GitHub Actions junto con las pruebas de cien mapas y sprites CC0.

**Limitación actual:** no existe sincronización completa de las acciones
de combate, intercambio Pokémon ni una sala pública con emparejamiento.
La vista compartida, el chat y las solicitudes de combate utilizan
el transporte existente, pero los combates en red requieren todavía
integración y pruebas de partida completa.
