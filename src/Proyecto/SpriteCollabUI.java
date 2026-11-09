package Proyecto;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Visor gráfico de retratos Pokémon de SpriteCollab.
 * Descarga bajo demanda desde el repositorio oficial y conserva una caché local.
 * No bloquea el hilo de Swing mientras se descargan los PNG.
 */
public final class SpriteCollabUI {
    private static final String RAW = "https://raw.githubusercontent.com/PMDCollab/SpriteCollab/master/";
    private static final Path CACHE = Paths.get(System.getProperty("user.home"), ".pokemon", "spritecollab");
    private static final ExecutorService DESCARGAS = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "spritecollab-cache"); t.setDaemon(true); return t;
    });
    private static JFrame ventanaCombate;
    private static JLabel imagenJugador, imagenRival, datosJugador, datosRival;
    private static String ultimoJugador = "", ultimoRival = "";

    private SpriteCollabUI() { }

    /** Abre/actualiza una ventana persistente durante el combate. */
    public static void mostrarCombate(PokemonLuchador jugador, PokemonLuchador rival) {
        if (GraphicsEnvironment.isHeadless() || jugador == null || rival == null) return;
        SwingUtilities.invokeLater(() -> {
            if (ventanaCombate == null || !ventanaCombate.isDisplayable()) crearVentanaCombate();
            actualizarLado(jugador, imagenJugador, datosJugador, true);
            actualizarLado(rival, imagenRival, datosRival, false);
            ventanaCombate.setVisible(true);
        });
    }

    public static void cerrarCombate() {
        SwingUtilities.invokeLater(() -> {
            if (ventanaCombate != null) ventanaCombate.dispose();
            ventanaCombate = null;
        });
    }

    private static void crearVentanaCombate() {
        ventanaCombate = new JFrame("Pokémon — Combate");
        ventanaCombate.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel fondo = new JPanel(new GridLayout(1, 2, 18, 0));
        fondo.setBorder(new EmptyBorder(18, 18, 18, 18));
        fondo.setBackground(new Color(35, 48, 62));
        imagenJugador = new JLabel("Cargando sprite…", SwingConstants.CENTER);
        imagenRival = new JLabel("Cargando sprite…", SwingConstants.CENTER);
        datosJugador = new JLabel("", SwingConstants.CENTER);
        datosRival = new JLabel("", SwingConstants.CENTER);
        fondo.add(panelLado("TU POKÉMON", imagenJugador, datosJugador));
        fondo.add(panelLado("POKÉMON RIVAL", imagenRival, datosRival));
        ventanaCombate.add(fondo);
        ventanaCombate.setSize(680, 340);
        ventanaCombate.setLocationRelativeTo(null);
    }

    private static JPanel panelLado(String titulo, JLabel imagen, JLabel datos) {
        JPanel p = new JPanel(new BorderLayout(5, 8));
        p.setBackground(new Color(238, 243, 248));
        JLabel h = new JLabel(titulo, SwingConstants.CENTER);
        h.setFont(h.getFont().deriveFont(Font.BOLD, 15f));
        p.add(h, BorderLayout.NORTH);
        imagen.setPreferredSize(new Dimension(220, 190));
        imagen.setFont(imagen.getFont().deriveFont(Font.PLAIN, 13f));
        p.add(imagen, BorderLayout.CENTER);
        datos.setBorder(new EmptyBorder(5, 4, 8, 4));
        p.add(datos, BorderLayout.SOUTH);
        return p;
    }

    private static void actualizarLado(PokemonLuchador p, JLabel imagen, JLabel datos, boolean jugador) {
        String firma = p.getNombre() + "|" + p.getNivel() + "|" + p.getVidaActual() + "|" + p.getVidaMax();
        datos.setText("<html><div style='text-align:center'><b>" + escapar(p.getNombre()) + " · Nv. " + p.getNivel()
                + "</b><br>PS: " + p.getVidaActual() + " / " + p.getVidaMax()
                + (p.estaVivo() ? "" : "<br><b>DEBILITADO</b>") + "</div></html>");
        imagen.setText("Cargando sprite…");
        imagen.setIcon(null);
        cargarRetrato(p, 190, 190, imagen);
    }

    /** Muestra el equipo o el PC en una cuadrícula de retratos. */
    public static void mostrarColeccion(String titulo, List<PokemonLuchador> pokemon) {
        if (GraphicsEnvironment.isHeadless()) return;
        List<PokemonLuchador> copia = pokemon == null ? Collections.emptyList() : new ArrayList<>(pokemon);
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame(titulo);
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JPanel rejilla = new JPanel(new GridLayout(0, 3, 10, 10));
            rejilla.setBorder(new EmptyBorder(12, 12, 12, 12));
            rejilla.setBackground(new Color(235, 241, 247));
            if (copia.isEmpty()) rejilla.add(new JLabel("No hay Pokémon.", SwingConstants.CENTER));
            for (PokemonLuchador p : copia) rejilla.add(tarjeta(p));
            f.add(new JScrollPane(rejilla));
            f.setSize(650, 520);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    private static JPanel tarjeta(PokemonLuchador p) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 202, 214)),
                new EmptyBorder(7, 7, 7, 7)));
        JLabel img = new JLabel("Cargando…", SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(130, 100));
        JLabel info = new JLabel("<html><center><b>" + escapar(p.getNombre()) + "</b><br>Nv. " + p.getNivel()
                + "<br>PS " + p.getVidaActual() + "/" + p.getVidaMax()
                + "<br>" + (p.estaVivo() ? "Disponible" : "Debilitado") + "</center></html>", SwingConstants.CENTER);
        card.add(img, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);
        cargarRetrato(p, 100, 100, img);
        return card;
    }

    private static void cargarRetrato(PokemonLuchador p, int ancho, int alto, JLabel destino) {
        DESCARGAS.execute(() -> {
            try {
                int dex = numeroDex(p);
                if (dex <= 0) throw new IOException("No se encontró el número de Pokédex");
                Files.createDirectories(CACHE);
                Path png = CACHE.resolve(String.format(Locale.ROOT, "%04d-Normal.png", dex));
                Path creditos = CACHE.resolve(String.format(Locale.ROOT, "%04d-credits.txt", dex));
                if (!Files.isRegularFile(png)) descargar("portrait/" + String.format(Locale.ROOT, "%04d", dex) + "/Normal.png", png);
                if (!Files.isRegularFile(creditos)) {
                    try { descargar("portrait/" + String.format(Locale.ROOT, "%04d", dex) + "/credits.txt", creditos); }
                    catch (IOException ignorado) { /* Los créditos pueden no existir para una especie. */ }
                }
                BufferedImage original = ImageIO.read(png.toFile());
                if (original == null) throw new IOException("Imagen no válida");
                Image escalada = original.getScaledInstance(ancho, alto, Image.SCALE_NEAREST);
                String autor = Files.isRegularFile(creditos)
                        ? new String(Files.readAllBytes(creditos), StandardCharsets.UTF_8).trim()
                        : "Consulta los créditos del repositorio SpriteCollab.";
                SwingUtilities.invokeLater(() -> {
                    destino.setText("");
                    destino.setIcon(new ImageIcon(escalada));
                    destino.setToolTipText("<html><b>SpriteCollab · " + escapar(p.getNombre())
                            + "</b><br>Créditos del recurso:<br>" + escapar(autor).replace("\n", "<br>")
                            + "<br>https://github.com/PMDCollab/SpriteCollab</html>");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> destino.setText("Sprite no disponible"));
            }
        });
    }

    private static int numeroDex(PokemonLuchador pokemon) {
        String nombre = pokemon.getNombre();
        List<PokemonBase> lista = Pokedex.getTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre().equalsIgnoreCase(nombre)) return i + 1;
        }
        // Las formas mega usan el número de su especie base cuando el nombre no figura en la lista.
        String base = pokemon.getBase().getEspecieBaseMega();
        if (base != null) for (int i = 0; i < lista.size(); i++)
            if (lista.get(i).getNombre().equalsIgnoreCase(base)) return i + 1;
        return -1;
    }

    private static void descargar(String recurso, Path destino) throws IOException {
        URL url = new URL(RAW + recurso);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setConnectTimeout(7000);
        c.setReadTimeout(12000);
        c.setRequestProperty("User-Agent", "Pokemon-Open-Source-Fangame/1.0");
        try (InputStream in = c.getInputStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        } finally { c.disconnect(); }
    }

    private static String escapar(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
