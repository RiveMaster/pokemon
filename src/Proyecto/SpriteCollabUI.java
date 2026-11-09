package Proyecto;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Combate animado con spritesheets oficiales de SpriteCollab.
 * Las animaciones se leen de AnimData.xml y se reproducen a 60 FPS aprox.
 * Los recursos se descargan bajo demanda y se guardan en la caché del usuario.
 */
public final class SpriteCollabUI {
    private static final String RAW = "https://raw.githubusercontent.com/PMDCollab/SpriteCollab/master/";
    private static final Path CACHE = Paths.get(System.getProperty("user.home"), ".pokemon", "spritecollab");
    private static final ExecutorService DESCARGAS = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "spritecollab-cache"); t.setDaemon(true); return t;
    });
    private static final Map<Integer, CompletableFuture<SpriteSet>> RECURSOS = new ConcurrentHashMap<>();

    private static JFrame ventanaCombate;
    private static SpriteActor actorJugador, actorRival;
    private static JLabel datosJugador, datosRival;

    private SpriteCollabUI() { }

    public static void mostrarCombate(PokemonLuchador jugador, PokemonLuchador rival) {
        if (GraphicsEnvironment.isHeadless() || jugador == null || rival == null) return;
        SwingUtilities.invokeLater(() -> {
            if (ventanaCombate == null || !ventanaCombate.isDisplayable()) crearVentanaCombate();
            actualizarLado(jugador, actorJugador, datosJugador, true);
            actualizarLado(rival, actorRival, datosRival, false);
            ventanaCombate.setVisible(true);
        });
    }

    public static void cerrarCombate() {
        SwingUtilities.invokeLater(() -> {
            if (ventanaCombate != null) ventanaCombate.dispose();
            ventanaCombate = null;
        });
    }

    /** Reproduce el ataque del Pokémon que acaba de elegir un movimiento. */
    public static void animarAtaque(PokemonLuchador atacante) {
        animar(atacante, "Attack", false);
    }

    /** Reproduce el impacto recibido. */
    public static void animarDaño(PokemonLuchador objetivo) {
        animar(objetivo, "Hurt", false);
    }

    /** Reproduce el desmayo y deja al Pokémon tumbado. */
    public static void animarDerrota(PokemonLuchador derrotado) {
        animar(derrotado, "Faint", false);
    }

    private static void animar(PokemonLuchador pokemon, String nombre, boolean ignorado) {
        if (pokemon == null) return;
        SwingUtilities.invokeLater(() -> {
            if (actorJugador != null && actorJugador.representa(pokemon)) actorJugador.reproducir(nombre);
            else if (actorRival != null && actorRival.representa(pokemon)) actorRival.reproducir(nombre);
        });
    }

    private static void crearVentanaCombate() {
        ventanaCombate = new JFrame("Pokémon — Combate");
        ventanaCombate.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel campo = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(202, 230, 245),
                        0, getHeight(), new Color(226, 237, 205)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(120, 160, 104));
                g2.fillOval(35, 125, 250, 72);
                g2.fillOval(Math.max(310, getWidth() - 285), 35, 245, 70);
                g2.dispose();
            }
        };
        campo.setPreferredSize(new Dimension(720, 340));

        actorRival = new SpriteActor(false);
        actorJugador = new SpriteActor(true);
        actorRival.setBounds(430, 12, 230, 205);
        actorJugador.setBounds(55, 110, 250, 215);
        campo.add(actorRival);
        campo.add(actorJugador);

        JPanel panelDatos = new JPanel(new GridLayout(1, 2, 14, 0));
        panelDatos.setBorder(new EmptyBorder(8, 14, 12, 14));
        panelDatos.setBackground(new Color(35, 48, 62));
        datosJugador = crearEtiquetaDatos();
        datosRival = crearEtiquetaDatos();
        panelDatos.add(datosJugador);
        panelDatos.add(datosRival);

        ventanaCombate.setLayout(new BorderLayout());
        ventanaCombate.add(campo, BorderLayout.CENTER);
        ventanaCombate.add(panelDatos, BorderLayout.SOUTH);
        ventanaCombate.setSize(740, 410);
        ventanaCombate.setLocationRelativeTo(null);
    }

    private static JLabel crearEtiquetaDatos() {
        JLabel label = new JLabel("Cargando Pokémon…", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }

    private static void actualizarLado(PokemonLuchador p, SpriteActor actor, JLabel datos, boolean jugador) {
        datos.setText("<html><div style='text-align:center'><b>" + escapar(p.getNombre())
                + " · Nv. " + p.getNivel() + "</b><br>PS: " + p.getVidaActual() + " / "
                + p.getVidaMax() + (p.estaVivo() ? "" : "<br><b>DEBILITADO</b>") + "</div></html>");
        actor.mostrarPokemon(p);
    }

    /** Equipo y PC conservan sus cuadrículas de retratos. */
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
                BorderFactory.createLineBorder(new Color(190, 202, 214)), new EmptyBorder(7, 7, 7, 7)));
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
        int dex = numeroDex(p);
        if (dex <= 0) return;
        obtenerRecursos(dex).thenAccept(set -> SwingUtilities.invokeLater(() -> {
            if (set.retrato != null) {
                Image scaled = set.retrato.getScaledInstance(ancho, alto, Image.SCALE_NEAREST);
                destino.setText("");
                destino.setIcon(new ImageIcon(scaled));
                destino.setToolTipText(set.creditos);
            }
        }));
    }

    private static CompletableFuture<SpriteSet> obtenerRecursos(int dex) {
        return RECURSOS.computeIfAbsent(dex, id -> CompletableFuture.supplyAsync(() -> cargarRecursos(id), DESCARGAS));
    }

    private static SpriteSet cargarRecursos(int dex) {
        SpriteSet set = new SpriteSet();
        String carpeta = String.format(Locale.ROOT, "sprite/%04d/", dex);
        try {
            Files.createDirectories(CACHE.resolve(String.format(Locale.ROOT, "%04d", dex)));
            Path dir = CACHE.resolve(String.format(Locale.ROOT, "%04d", dex));
            Path xml = asegurar(carpeta + "AnimData.xml", dir.resolve("AnimData.xml"));
            set.animaciones = leerAnimaciones(xml);
            String[] nombres = {"Idle", "Attack", "Hurt", "Faint"};
            for (String nombre : nombres) {
                AnimInfo info = set.animaciones.get(nombre);
                if (info == null) continue;
                try {
                    Path png = asegurar(carpeta + nombre + "-Anim.png", dir.resolve(nombre + "-Anim.png"));
                    set.hojas.put(nombre, ImageIO.read(png.toFile()));
                } catch (Exception ignorado) { /* Algunas especies no tienen todas las animaciones. */ }
            }
            try {
                Path portrait = asegurar("portrait/" + String.format(Locale.ROOT, "%04d", dex)
                        + "/Normal.png", dir.resolve("Normal.png"));
                set.retrato = ImageIO.read(portrait.toFile());
            } catch (Exception ignorado) { }
            try {
                Path credits = asegurar(carpeta + "credits.txt", dir.resolve("credits.txt"));
                set.creditos = new String(Files.readAllBytes(credits), StandardCharsets.UTF_8).trim();
            } catch (Exception ignorado) {
                set.creditos = "Créditos: PMDCollab SpriteCollab — https://github.com/PMDCollab/SpriteCollab";
            }
        } catch (Exception ex) {
            set.creditos = "SpriteCollab no disponible para esta especie.";
        }
        return set;
    }

    private static Path asegurar(String remoto, Path local) throws IOException {
        if (!Files.isRegularFile(local) || Files.size(local) == 0) descargar(remoto, local);
        return local;
    }

    private static Map<String, AnimInfo> leerAnimaciones(Path xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document doc = factory.newDocumentBuilder().parse(xml.toFile());
        Map<String, AnimInfo> resultado = new HashMap<>();
        NodeList nodos = doc.getElementsByTagName("Anim");
        for (int i = 0; i < nodos.getLength(); i++) {
            Element e = (Element) nodos.item(i);
            String nombre = texto(e, "Name");
            if (nombre == null) continue;
            AnimInfo info = new AnimInfo();
            info.ancho = entero(e, "FrameWidth", 32);
            info.alto = entero(e, "FrameHeight", 40);
            NodeList duraciones = e.getElementsByTagName("Duration");
            for (int j = 0; j < duraciones.getLength(); j++) {
                try { info.duraciones.add(Integer.parseInt(duraciones.item(j).getTextContent().trim())); }
                catch (NumberFormatException ignorado) { info.duraciones.add(4); }
            }
            resultado.put(nombre, info);
        }
        return resultado;
    }

    private static String texto(Element e, String tag) {
        NodeList n = e.getElementsByTagName(tag);
        return n.getLength() == 0 ? null : n.item(0).getTextContent().trim();
    }

    private static int entero(Element e, String tag, int defecto) {
        try { return Integer.parseInt(texto(e, tag)); } catch (Exception ex) { return defecto; }
    }

    private static int numeroDex(PokemonLuchador pokemon) {
        List<PokemonBase> lista = Pokedex.getTodos();
        String nombre = pokemon.getNombre();
        for (int i = 0; i < lista.size(); i++)
            if (lista.get(i).getNombre().equalsIgnoreCase(nombre)) return i + 1;
        String base = pokemon.getBase().getEspecieBaseMega();
        if (base != null) for (int i = 0; i < lista.size(); i++)
            if (lista.get(i).getNombre().equalsIgnoreCase(base)) return i + 1;
        return -1;
    }

    private static void descargar(String recurso, Path destino) throws IOException {
        URL url = new URL(RAW + recurso);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setConnectTimeout(8000);
        c.setReadTimeout(15000);
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

    private static final class AnimInfo {
        int ancho, alto;
        final List<Integer> duraciones = new ArrayList<>();
    }

    private static final class SpriteSet {
        final Map<String, BufferedImage> hojas = new HashMap<>();
        Map<String, AnimInfo> animaciones = Collections.emptyMap();
        BufferedImage retrato;
        String creditos = "";
    }

    /** Panel animado: cada PNG contiene los fotogramas en horizontal y las direcciones en vertical. */
    private static final class SpriteActor extends JPanel {
        private final boolean jugador;
        private PokemonLuchador pokemon;
        private SpriteSet recursos;
        private String animacion = "Idle";
        private int frame;
        private int tickFallback;
        private javax.swing.Timer timer;
        private boolean finalizado;

        SpriteActor(boolean jugador) {
            this.jugador = jugador;
            setOpaque(false);
        }

        boolean representa(PokemonLuchador p) { return pokemon == p; }

        void mostrarPokemon(PokemonLuchador p) {
            if (pokemon == p) {
                repaint();
                return;
            }
            pokemon = p;
            recursos = null;
            animacion = "Idle";
            frame = 0;
            repaint();
            int dex = numeroDex(p);
            if (dex > 0) obtenerRecursos(dex).thenAccept(set -> SwingUtilities.invokeLater(() -> {
                if (pokemon == p) {
                    recursos = set;
                    setToolTipText("<html>" + escapar(set.creditos).replace("\n", "<br>") + "</html>");
                    reproducir("Idle");
                }
            }));
            else reproducir("Idle");
        }

        void reproducir(String nombre) {
            if (pokemon == null) return;
            if (timer != null) timer.stop();
            animacion = nombre;
            frame = 0;
            tickFallback = 0;
            finalizado = false;
            avanzar();
        }

        private void avanzar() {
            if (timer != null) timer.stop();
            AnimInfo info = recursos == null ? null : recursos.animaciones.get(animacion);
            BufferedImage hoja = recursos == null ? null : recursos.hojas.get(animacion);
            if (info == null || hoja == null || info.duraciones.isEmpty()) {
                // Respaldo visual animado para especies que no tengan esa hoja de animación.
                timer = new javax.swing.Timer(animacion.equals("Idle") ? 140 : 85, e -> {
                    tickFallback++;
                    if (!animacion.equals("Idle") && tickFallback >= 8) {
                        if (animacion.equals("Faint")) finalizado = true;
                        else { animacion = "Idle"; tickFallback = 0; avanzar(); }
                    }
                    repaint();
                });
                timer.start();
                return;
            }
            int total = Math.max(1, Math.min(info.duraciones.size(), hoja.getWidth() / Math.max(1, info.ancho)));
            if (frame >= total) {
                if (animacion.equals("Idle")) frame = 0;
                else {
                    finalizado = true;
                    if (animacion.equals("Faint")) frame = total - 1;
                    else { animacion = "Idle"; frame = 0; }
                    repaint();
                    if (!animacion.equals("Faint")) avanzar();
                    return;
                }
            }
            repaint();
            int duracion = Math.max(1, info.duraciones.get(Math.min(frame, info.duraciones.size() - 1)));
            timer = new javax.swing.Timer(Math.max(35, duracion * 1000 / 60), e -> {
                frame++;
                avanzar();
            });
            timer.setRepeats(false);
            timer.start();
        }

        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            BufferedImage hoja = recursos == null ? null : recursos.hojas.get(animacion);
            AnimInfo info = recursos == null ? null : recursos.animaciones.get(animacion);
            if (hoja != null && info != null && info.ancho > 0 && info.alto > 0) {
                int columnas = Math.max(1, hoja.getWidth() / info.ancho);
                int filas = Math.max(1, hoja.getHeight() / info.alto);
                int direccion = jugador ? Math.min(4, filas - 1) : 0;
                int f = Math.min(frame, columnas - 1);
                int sx = f * info.ancho, sy = direccion * info.alto;
                double escala = Math.min((getWidth() - 16.0) / info.ancho, (getHeight() - 12.0) / info.alto);
                int w = Math.max(1, (int) (info.ancho * escala));
                int h = Math.max(1, (int) (info.alto * escala));
                int x = (getWidth() - w) / 2;
                int y = getHeight() - h - 4;
                g.drawImage(hoja, x, y, x + w, y + h, sx, sy, sx + info.ancho, sy + info.alto, null);
            } else if (recursos != null && recursos.retrato != null) {
                int w = Math.min(getWidth() - 20, 180), h = Math.min(getHeight() - 20, 180);
                int x = (getWidth() - w) / 2, y = (getHeight() - h) / 2;
                if (animacion.equals("Attack")) x += jugador ? 28 : -28;
                if (animacion.equals("Hurt")) x += (tickFallback % 2 == 0 ? -9 : 9);
                if (animacion.equals("Faint") || finalizado) {
                    y += 24;
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
                } else if (animacion.equals("Idle")) y += (tickFallback % 2 == 0 ? 0 : -3);
                g.drawImage(recursos.retrato, x, y, w, h, null);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.drawString(pokemon == null ? "" : pokemon.getNombre(), 15, getHeight() / 2);
            }
            g.dispose();
        }
    }
}
