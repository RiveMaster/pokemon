package Proyecto;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/** Apertura/cierre de puertos TCP mediante UPnP IGD. */
public final class UPnPPortMapper {
    private static final String SEARCH = "M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:1900\r\nMAN: \"ssdp:discover\"\r\nMX: 2\r\nST: urn:schemas-upnp-org:device:InternetGatewayDevice:1\r\n\r\n";
    private UPnPPortMapper() {}

    public static final class Mapping {
        private final URL control;
        private final String service;
        private final int port;
        private Mapping(URL control, String service, int port) {
            this.control = control; this.service = service; this.port = port;
        }
        public void cerrar() {
            String body = "<u:DeletePortMapping xmlns:u=\"" + service + "\">"
                    + "<NewRemoteHost></NewRemoteHost><NewExternalPort>" + port
                    + "</NewExternalPort><NewProtocol>TCP</NewProtocol></u:DeletePortMapping>";
            try { soap(control, service, "DeletePortMapping", body); } catch (Exception ignored) {}
        }
    }

    public static Mapping abrirPuertoTCP(int port) throws Exception {
        URL location = descubrirDescripcion();
        InetAddress local;
        try (DatagramSocket s = new DatagramSocket()) {
            s.connect(InetAddress.getByName(location.getHost()), location.getPort() > 0 ? location.getPort() : 80);
            local = s.getLocalAddress();
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        Document doc;
        HttpURLConnection get = (HttpURLConnection) location.openConnection();
        get.setConnectTimeout(3000); get.setReadTimeout(3000);
        try (InputStream in = get.getInputStream()) { doc = factory.newDocumentBuilder().parse(in); }
        finally { get.disconnect(); }

        NodeList services = doc.getElementsByTagName("service");
        URL control = null; String service = null;
        for (int i = 0; i < services.getLength(); i++) {
            Node n = services.item(i);
            String type = child(n, "serviceType"), path = child(n, "controlURL");
            if (type != null && path != null && (type.contains(":WANIPConnection:") || type.contains(":WANPPPConnection:"))) {
                control = new URL(location, path); service = type;
                if (type.contains(":WANIPConnection:")) break;
            }
        }
        if (control == null) throw new IOException("El router no ofrece un servicio UPnP WAN compatible.");
        String body = "<u:AddPortMapping xmlns:u=\"" + service + "\">"
                + "<NewRemoteHost></NewRemoteHost><NewExternalPort>" + port + "</NewExternalPort>"
                + "<NewProtocol>TCP</NewProtocol><NewInternalPort>" + port + "</NewInternalPort>"
                + "<NewInternalClient>" + local.getHostAddress() + "</NewInternalClient>"
                + "<NewEnabled>1</NewEnabled><NewPortMappingDescription>Pokemon Online</NewPortMappingDescription>"
                + "<NewLeaseDuration>0</NewLeaseDuration></u:AddPortMapping>";
        soap(control, service, "AddPortMapping", body);
        return new Mapping(control, service, port);
    }

    private static URL descubrirDescripcion() throws Exception {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3500);
            byte[] data = SEARCH.getBytes(StandardCharsets.US_ASCII);
            socket.send(new DatagramPacket(data, data.length, InetAddress.getByName("239.255.255.250"), 1900));
            byte[] buffer = new byte[8192];
            long until = System.currentTimeMillis() + 3500;
            while (System.currentTimeMillis() < until) {
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                try { socket.receive(response); }
                catch (SocketTimeoutException ex) { break; }
                String headers = new String(response.getData(), response.getOffset(), response.getLength(), StandardCharsets.US_ASCII);
                for (String line : headers.split("\\r?\\n"))
                    if (line.toLowerCase().startsWith("location:"))
                        return new URL(line.substring(line.indexOf(':') + 1).trim());
            }
        }
        throw new IOException("No se encontró un router UPnP. Comprueba que UPnP esté activado.");
    }

    private static String child(Node parent, String name) {
        NodeList nodes = parent.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
            if (name.equals(nodes.item(i).getNodeName())) return nodes.item(i).getTextContent().trim();
        return null;
    }

    private static void soap(URL url, String service, String action, String body) throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>"
                + body + "</s:Body></s:Envelope>";
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST"); c.setConnectTimeout(4000); c.setReadTimeout(4000); c.setDoOutput(true);
        c.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        c.setRequestProperty("SOAPAction", "\"" + service + "#" + action + "\"");
        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        c.setFixedLengthStreamingMode(bytes.length);
        try (OutputStream out = c.getOutputStream()) { out.write(bytes); }
        int status = c.getResponseCode();
        InputStream response = status >= 400 ? c.getErrorStream() : c.getInputStream();
        if (response != null) try (InputStream in = response; ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            byte[] chunk = new byte[1024]; int count;
            while ((count = in.read(chunk)) >= 0) b.write(chunk, 0, count);
            String result = new String(b.toByteArray(), StandardCharsets.UTF_8);
            if (result.contains("<errorCode>")) throw new IOException("El router rechazó la solicitud UPnP: " + result);
        }
        c.disconnect();
        if (status < 200 || status >= 300) throw new IOException("Respuesta UPnP HTTP " + status);
    }
}
