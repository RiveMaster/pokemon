package Proyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/** Prueba sin interfaz: servidor, chat TCP, validación y rango de VPN. */
public final class ChatVpnSmokeTest {
    private ChatVpnSmokeTest() { }

    private static String b64(String s) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws Exception {
        assert "Hola mundo".equals(RedLAN.validarChat(" \nHola mundo\r "));
        assert RedLAN.validarChat(" \n\t ")==null;
        assert RedLAN.validarChat("X".repeat(400)).length()==240;
        assert Aventura.esIpTailscale("100.64.0.1");
        assert Aventura.esIpTailscale("100.127.255.255");
        assert !Aventura.esIpTailscale("100.128.0.1");
        assert !Aventura.esIpTailscale("192.168.1.5");
        assert !Aventura.esIpTailscale("100.64.255.300");

        int puerto;
        try(ServerSocket temporal=new ServerSocket(0,1,InetAddress.getLoopbackAddress())){
            puerto=temporal.getLocalPort();
        }
        RedLAN host=RedLAN.alojar("Anfitrión",puerto,"127.0.0.1");
        try(Socket invitado=new Socket("127.0.0.1",puerto)) {
            invitado.setSoTimeout(3000);
            BufferedWriter escritor=new BufferedWriter(
                    new OutputStreamWriter(invitado.getOutputStream(),StandardCharsets.UTF_8));
            BufferedReader lector=new BufferedReader(
                    new InputStreamReader(invitado.getInputStream(),StandardCharsets.UTF_8));
            escritor.write("JOIN|"+b64("Invitado"));
            escritor.newLine();
            escritor.flush();
            assert lector.readLine().startsWith("WELCOME|");

            escritor.write("CHAT|"+b64("¡Hola desde la VPN!"));
            escritor.newLine();
            escritor.flush();
            List<String> recibidos=List.of();
            for(int i=0;i<30;i++){
                Thread.sleep(35);
                recibidos=host.consumirChat();
                if(!recibidos.isEmpty())break;
            }
            assert recibidos.stream().anyMatch(s->s.contains("Invitado: ¡Hola"))
                    : "El servidor no recibió el mensaje de un invitado.";

            host.enviarChat("¡Bienvenido!");
            String red=null;
            for(int i=0;i<8;i++){
                String linea=lector.readLine();
                if(linea.startsWith("CHAT|")){red=linea;break;}
            }
            assert red!=null && red.startsWith("CHAT|")
                    : "El anfitrión no transmitió el chat al cliente.";
        } finally {
            RedLAN.cerrar();
        }
        System.out.println("ChatVpnSmokeTest: chat TCP, VPN y límite de mensajes OK");
    }
}
