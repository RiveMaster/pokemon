package Proyecto.studio.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/** Generador procedural determinista para mapas de Pokémon Studio. */
public final class GeneradorMapas {
    private GeneradorMapas() { }

    public enum Estilo {
        RUTA_BOSQUE("Ruta y bosque"),
        PUEBLO("Pueblo / ciudad"),
        COSTA_LAGO("Costa y lago"),
        PRADERA("Pradera abierta");

        private final String etiqueta;
        Estilo(String etiqueta) { this.etiqueta = etiqueta; }
        @Override public String toString() { return etiqueta; }
    }

    public static final class Opciones {
        public final int ancho, alto, densidad;
        public final long semilla;
        public final Estilo estilo;
        public final boolean incluirCamino, incluirHierba, incluirAgua, incluirObstaculos, incluirObjetos;
        public final int tileBase, tileCamino, tileHierba, tileAgua, tileObstaculo;

        public Opciones(int ancho, int alto, long semilla, Estilo estilo, int densidad,
                        boolean incluirCamino, boolean incluirHierba, boolean incluirAgua,
                        boolean incluirObstaculos, boolean incluirObjetos,
                        int tileBase, int tileCamino, int tileHierba, int tileAgua, int tileObstaculo) {
            if (ancho < 1 || ancho > 300 || alto < 1 || alto > 300)
                throw new IllegalArgumentException("El mapa debe medir entre 1 y 300 tiles por lado.");
            this.ancho = ancho;
            this.alto = alto;
            this.semilla = semilla;
            this.estilo = estilo == null ? Estilo.RUTA_BOSQUE : estilo;
            this.densidad = Math.max(0, Math.min(100, densidad));
            this.incluirCamino = incluirCamino;
            this.incluirHierba = incluirHierba;
            this.incluirAgua = incluirAgua;
            this.incluirObstaculos = incluirObstaculos;
            this.incluirObjetos = incluirObjetos;
            this.tileBase = tileBase;
            this.tileCamino = tileCamino;
            this.tileHierba = tileHierba;
            this.tileAgua = tileAgua;
            this.tileObstaculo = tileObstaculo;
        }
    }

    public static final class DefinicionObjeto {
        public final int indice, ancho, alto;
        public final String nombre;
        public final int[] celdas;

        public DefinicionObjeto(int indice, String nombre, int ancho, int alto, int[] celdas) {
            this.indice = indice;
            this.nombre = nombre == null ? "" : nombre;
            this.ancho = ancho;
            this.alto = alto;
            this.celdas = celdas == null ? new int[0] : celdas.clone();
        }
    }

    public static final class Colocacion {
        public final int objeto, x, y, capa;
        public Colocacion(int objeto, int x, int y, int capa) {
            this.objeto = objeto;
            this.x = x;
            this.y = y;
            this.capa = capa;
        }
    }

    public static final class Resultado {
        public final int ancho, alto;
        public final int[][] capas;
        public final boolean[] colisionesManuales, hierbaEncuentros;
        public final List<Colocacion> colocaciones;
        public final long semilla;

        private Resultado(int ancho, int alto, int[][] capas, boolean[] colisionesManuales,
                          boolean[] hierbaEncuentros, List<Colocacion> colocaciones, long semilla) {
            this.ancho = ancho;
            this.alto = alto;
            this.capas = capas;
            this.colisionesManuales = colisionesManuales;
            this.hierbaEncuentros = hierbaEncuentros;
            this.colocaciones = colocaciones;
            this.semilla = semilla;
        }
    }

    public static Resultado generar(Opciones o, List<DefinicionObjeto> biblioteca) {
        if (o == null) throw new IllegalArgumentException("Faltan las opciones del generador.");
        int w = o.ancho, h = o.alto, size = w * h;
        Random random = new Random(o.semilla);
        int[][] layers = new int[3][size];
        for (int[] layer : layers) java.util.Arrays.fill(layer, -1);
        boolean[] manual = new boolean[size];
        boolean[] grass = new boolean[size];
        boolean[] path = new boolean[size];
        boolean[] water = new boolean[size];
        boolean[] grassPatch = new boolean[size];
        boolean[] obstacles = new boolean[size];
        List<Colocacion> placements = new ArrayList<>();

        if (o.tileBase < 0) throw new IllegalArgumentException("Selecciona un tile de suelo base.");
        java.util.Arrays.fill(layers[0], o.tileBase);

        if (o.incluirCamino) crearCaminos(path, w, h, o.estilo, random);
        if (o.incluirAgua && o.estilo == Estilo.COSTA_LAGO) crearAgua(water, path, w, h, random, true);
        else if (o.incluirAgua && random.nextInt(100) < 22) crearAgua(water, path, w, h, random, false);
        if (o.incluirHierba) crearParchesHierba(grassPatch, path, water, w, h, o.densidad, random);

        if (o.incluirObjetos && biblioteca != null && !biblioteca.isEmpty()) {
            if (o.estilo == Estilo.PUEBLO)
                colocarEdificios(placements, manual, path, water, grassPatch, w, h, biblioteca, random);
            colocarElementosNaturales(placements, manual, path, water, grassPatch, w, h,
                    biblioteca, o.densidad, o.estilo, random);
        }
        if (o.incluirObstaculos) crearObstaculos(obstacles, path, water, w, h, o.densidad, o.estilo, random);

        // Evita que los obstáculos generados tapen los edificios.
        for (Colocacion colocacion : placements) {
            DefinicionObjeto obj = buscarObjeto(biblioteca, colocacion.objeto);
            if (obj == null) continue;
            for (int oy = 0; oy < obj.alto; oy++) {
                for (int ox = 0; ox < obj.ancho; ox++) {
                    int x = colocacion.x + ox, y = colocacion.y + oy;
                    if (dentro(x, y, w, h)) obstacles[y * w + x] = false;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            if (water[i] && o.tileAgua >= 0) {
                layers[0][i] = o.tileAgua;
            }
            if (obstacles[i] && !path[i] && !water[i] && o.tileObstaculo >= 0) {
                layers[1][i] = o.tileObstaculo;
            }
            if (o.incluirHierba && grassPatch[i] && !water[i] && !path[i] && o.tileHierba >= 0) {
                layers[0][i] = o.tileHierba;
                grass[i] = true;
            }
        }

        if (o.incluirCamino && o.tileCamino >= 0) {
            for (int i = 0; i < size; i++) {
                if (path[i]) {
                    layers[0][i] = o.tileCamino;
                    manual[i] = false;
                    grass[i] = false;
                }
            }
        }

        // Los edificios bloquean la huella y conservan un acceso; árboles/rocas bloquean su base.
        for (Colocacion p : placements) {
            DefinicionObjeto obj = buscarObjeto(biblioteca, p.objeto);
            if (obj == null) continue;
            int puertaX = p.x + obj.ancho / 2;
            int puertaY = p.y + obj.alto - 1;
            for (int oy = 0; oy < obj.alto; oy++) {
                for (int ox = 0; ox < obj.ancho; ox++) {
                    int x = p.x + ox, y = p.y + oy;
                    if (!dentro(x, y, w, h)) continue;
                    int idx = y * w + x;
                    grass[idx] = false;
                    if (p.capa == 1 && (x != puertaX || y != puertaY)) manual[idx] = true;
                }
            }
            if (p.capa == 2 && dentro(puertaX, puertaY, w, h))
                manual[puertaY * w + puertaX] = true;
        }

        for (int i = 0; i < size; i++) if (manual[i]) grass[i] = false;
        return new Resultado(w, h, layers, manual, grass, placements, o.semilla);
    }

    private static void crearCaminos(boolean[] path, int w, int h, Estilo estilo, Random r) {
        if (estilo == Estilo.PUEBLO) {
            int grosor = h >= 16 && w >= 16 ? 2 : 1;
            marcarHorizontal(path, w, h, h / 2, grosor);
            marcarVertical(path, w, h, w / 2, grosor);
            if (w >= 28) {
                marcarVertical(path, w, h, w / 4, 1);
                marcarVertical(path, w, h, (w * 3) / 4, 1);
            }
            if (h >= 24) {
                marcarHorizontal(path, w, h, h / 4, 1);
                marcarHorizontal(path, w, h, (h * 3) / 4, 1);
            }
            return;
        }

        int y = Math.max(0, h / 2 + r.nextInt(Math.max(1, h / 5)) - h / 10);
        int radio = h >= 14 ? 1 : 0;
        for (int x = 0; x < w; x++) {
            for (int dy = -radio; dy <= radio; dy++) marcar(path, w, h, x, y + dy);
            if (x % 2 == 0 && h > 4) y = limitar(y + r.nextInt(3) - 1, 2, h - 3);
        }
        if (h >= 10 && w >= 12) {
            int ramales = Math.max(1, w / 18);
            for (int n = 0; n < ramales; n++) {
                int x = 2 + r.nextInt(Math.max(1, w - 4));
                int cy = encontrarCaminoY(path, w, h, x);
                int direccion = r.nextBoolean() ? 1 : -1;
                int largo = 2 + r.nextInt(Math.max(2, h / 4));
                for (int k = 1; k <= largo; k++) marcar(path, w, h, x, cy + direccion * k);
            }
        }
    }

    private static void crearAgua(boolean[] water, boolean[] path, int w, int h, Random r, boolean costa) {
        if (w < 3 || h < 3) return;
        int rx = Math.max(2, costa ? w / 3 : w / 5);
        int ry = Math.max(2, h / 3);
        int cx = costa ? Math.min(w - 1, w - rx / 2)
                : limitar(w * 2 / 3 + r.nextInt(Math.max(1, w / 6)) - w / 12, 0, w - 1);
        int cy = costa ? limitar(h / 2 + r.nextInt(Math.max(1, h / 4)) - h / 8, 0, h - 1) : h / 2;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double dx = (x - cx) / (double) rx;
                double dy = (y - cy) / (double) ry;
                boolean bordeCosta = costa && x > w - rx / 2;
                if (dx * dx + dy * dy <= 1.0 || bordeCosta) {
                    int i = y * w + x;
                    if (!path[i]) water[i] = true;
                }
            }
        }
    }

    private static void crearParchesHierba(boolean[] grass, boolean[] path, boolean[] water,
                                            int w, int h, int densidad, Random r) {
        if (densidad <= 0 || w < 3 || h < 3) return;
        int parches = Math.max(1, (w * h * densidad) / 650);
        for (int p = 0; p < parches; p++) {
            int cx = r.nextInt(w), cy = r.nextInt(h);
            int rx = 1 + r.nextInt(Math.max(1, Math.min(5, w / 6 + 1)));
            int ry = 1 + r.nextInt(Math.max(1, Math.min(3, h / 6 + 1)));
            for (int y = Math.max(0, cy - ry); y <= Math.min(h - 1, cy + ry); y++) {
                for (int x = Math.max(0, cx - rx); x <= Math.min(w - 1, cx + rx); x++) {
                    double dx = (x - cx) / (double) rx, dy = (y - cy) / (double) ry;
                    if (dx * dx + dy * dy > 1.15 || r.nextInt(100) < 12) continue;
                    int i = y * w + x;
                    if (!path[i] && !water[i]) grass[i] = true;
                }
            }
        }
    }

    private static void crearObstaculos(boolean[] obstacles, boolean[] path, boolean[] water,
                                        int w, int h, int densidad, Estilo estilo, Random r) {
        if (densidad <= 0 || w < 3 || h < 3) return;
        if (estilo == Estilo.RUTA_BOSQUE || estilo == Estilo.COSTA_LAGO) {
            int grosor = h >= 12 ? 1 : 0;
            for (int x = 0; x < w; x++) {
                for (int y = 0; y <= grosor; y++) {
                    if (r.nextInt(100) < 75) marcar(obstacles, w, h, x, y);
                    if (r.nextInt(100) < 75) marcar(obstacles, w, h, x, h - 1 - y);
                }
            }
        }
        int parches = Math.max(1, (w * h * densidad) / (estilo == Estilo.PUEBLO ? 1600 : 1100));
        for (int p = 0; p < parches; p++) {
            int cx = r.nextInt(w), cy = r.nextInt(h);
            int rx = 1 + r.nextInt(Math.max(1, Math.min(3, w / 8 + 1)));
            int ry = 1 + r.nextInt(Math.max(1, Math.min(2, h / 8 + 1)));
            for (int y = Math.max(0, cy - ry); y <= Math.min(h - 1, cy + ry); y++) {
                for (int x = Math.max(0, cx - rx); x <= Math.min(w - 1, cx + rx); x++) {
                    double dx = (x - cx) / (double) rx, dy = (y - cy) / (double) ry;
                    if (dx * dx + dy * dy > 1.1 || r.nextInt(100) < 20) continue;
                    int i = y * w + x;
                    if (!path[i] && !water[i]) obstacles[i] = true;
                }
            }
        }
        for (int x = 0; x < Math.min(3, w); x++) {
            int y = h / 2;
            for (int dy = -1; dy <= 1; dy++) if (dentro(x, y + dy, w, h)) obstacles[(y + dy) * w + x] = false;
        }
        for (int x = Math.max(0, w - 3); x < w; x++) {
            int y = h / 2;
            for (int dy = -1; dy <= 1; dy++) if (dentro(x, y + dy, w, h)) obstacles[(y + dy) * w + x] = false;
        }
        for (int i = 0; i < obstacles.length; i++) if (path[i] || water[i]) obstacles[i] = false;
    }

    private static void colocarEdificios(List<Colocacion> salida, boolean[] manual, boolean[] path,
                                         boolean[] water, boolean[] grass, int w, int h,
                                         List<DefinicionObjeto> biblioteca, Random random) {
        List<DefinicionObjeto> candidatos = new ArrayList<>();
        for (DefinicionObjeto obj : biblioteca) {
            if (obj != null && obj.ancho > 0 && obj.alto > 0 && obj.ancho <= w - 2 && obj.alto <= h - 2)
                candidatos.add(obj);
        }
        List<DefinicionObjeto> edificiosNombrados = new ArrayList<>();
        for (DefinicionObjeto obj : candidatos) if (esObjetoEdificio(obj)) edificiosNombrados.add(obj);
        if (!edificiosNombrados.isEmpty()) candidatos = edificiosNombrados;
        else candidatos.removeIf(GeneradorMapas::esObjetoNatural);
        if (candidatos.isEmpty()) return;
        candidatos.sort(Comparator.comparingInt(GeneradorMapas::puntuacionObjeto).reversed());
        int limite = Math.max(1, w / 7);
        int colocados = 0;
        int maxIntentos = Math.max(30, limite * 10);
        for (int intento = 0; intento < maxIntentos && colocados < limite; intento++) {
            DefinicionObjeto obj = candidatos.get(intento % candidatos.size());
            int x = 1 + random.nextInt(Math.max(1, w - obj.ancho - 1));
            int y = random.nextBoolean() ? 1 : Math.max(1, h - obj.alto - 1);
            if (y + obj.alto >= h) y = Math.max(0, h - obj.alto - 1);
            if (!rectanguloLibre(x, y, obj.ancho, obj.alto, path, water, w, h)) continue;
            boolean overlap = false;
            for (Colocacion actual : salida) {
                DefinicionObjeto otro = buscarObjeto(candidatos, actual.objeto);
                if (otro != null && seSolapan(x, y, obj.ancho, obj.alto, actual.x, actual.y, otro.ancho, otro.alto)) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) continue;
            salida.add(new Colocacion(obj.indice, x, y, 1));
            colocados++;
            int doorX = x + obj.ancho / 2, doorY = y + obj.alto - 1;
            for (int oy = 0; oy < obj.alto; oy++) for (int ox = 0; ox < obj.ancho; ox++) {
                int px = x + ox, py = y + oy;
                if (!dentro(px, py, w, h)) continue;
                int cell = py * w + px;
                grass[cell] = false;
                if (px != doorX || py != doorY) manual[cell] = true;
            }
        }
    }

    /** Sitúa árboles, arbustos y rocas de la biblioteca sobre la capa superior. */
    private static void colocarElementosNaturales(List<Colocacion> salida, boolean[] manual, boolean[] path,
                                                  boolean[] water, boolean[] grass, int w, int h,
                                                  List<DefinicionObjeto> biblioteca, int densidad,
                                                  Estilo estilo, Random random) {
        if (densidad <= 0 || w < 5 || h < 5) return;
        List<DefinicionObjeto> candidatos = new ArrayList<>();
        for (DefinicionObjeto obj : biblioteca) {
            if (obj != null && esObjetoNatural(obj) && obj.ancho > 0 && obj.alto > 0
                    && obj.ancho <= Math.min(12, w - 2) && obj.alto <= Math.min(12, h - 2))
                candidatos.add(obj);
        }
        if (candidatos.isEmpty()) return;
        candidatos.sort(Comparator.comparingInt((DefinicionObjeto obj) -> obj.ancho * obj.alto).reversed());
        int divisor = estilo == Estilo.PUEBLO ? 5200 : (estilo == Estilo.PRADERA ? 3200 : 1900);
        int limite = Math.min(80, Math.max(1, (w * h * densidad) / divisor));
        int colocados = 0;
        int intentos = Math.max(40, limite * 15);
        for (int intento = 0; intento < intentos && colocados < limite; intento++) {
            DefinicionObjeto obj = candidatos.get(random.nextInt(candidatos.size()));
            int x = random.nextInt(Math.max(1, w - obj.ancho + 1));
            int y = random.nextInt(Math.max(1, h - obj.alto + 1));
            if (!rectanguloLibre(x, y, obj.ancho, obj.alto, path, water, w, h)) continue;
            boolean enHierba = false;
            for (int yy = y; yy < y + obj.alto; yy++)
                for (int xx = x; xx < x + obj.ancho; xx++)
                    if (grass[yy * w + xx]) enHierba = true;
            if (enHierba) continue;
            boolean overlap = false;
            for (Colocacion actual : salida) {
                DefinicionObjeto otro = buscarObjeto(biblioteca, actual.objeto);
                if (otro != null && seSolapan(x, y, obj.ancho, obj.alto, actual.x, actual.y, otro.ancho, otro.alto)) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) continue;
            salida.add(new Colocacion(obj.indice, x, y, 2));
            colocados++;
            int baseX = x + obj.ancho / 2, baseY = y + obj.alto - 1;
            if (dentro(baseX, baseY, w, h)) manual[baseY * w + baseX] = true;
            for (int yy = y; yy < y + obj.alto; yy++)
                for (int xx = x; xx < x + obj.ancho; xx++) grass[yy * w + xx] = false;
        }
    }

    private static boolean esObjetoNatural(DefinicionObjeto obj) {
        String n = obj.nombre.toLowerCase(Locale.ROOT);
        return contiene(n, "arbol", "árbol", "tree", "arbusto", "bush", "roca", "rock",
                "piedra", "stone", "tronco", "log", "pino", "pine", "planta", "plant");
    }

    private static boolean esObjetoEdificio(DefinicionObjeto obj) {
        String n = obj.nombre.toLowerCase(Locale.ROOT);
        return contiene(n, "casa", "house", "hogar", "edificio", "building", "gimnasio",
                "gym", "centro", "tienda", "shop", "pokecentro", "pokemon center", "estacion", "station");
    }

    private static int puntuacionObjeto(DefinicionObjeto obj) {
        String n = obj.nombre.toLowerCase(Locale.ROOT);
        int score = 0;
        if (contiene(n, "casa", "house", "hogar", "edificio", "building")) score += 8;
        if (contiene(n, "gimnasio", "gym", "centro", "tienda", "shop", "pokecentro", "pokemon center")) score += 6;
        if (contiene(n, "arbol", "árbol", "tree", "roca", "rock", "arbusto", "bush")) score -= 5;
        return score;
    }

    private static boolean contiene(String texto, String... palabras) {
        for (String palabra : palabras) if (texto.contains(palabra)) return true;
        return false;
    }

    private static boolean rectanguloLibre(int x, int y, int ow, int oh, boolean[] path, boolean[] water, int w, int h) {
        if (x < 0 || y < 0 || x + ow >= w || y + oh >= h) return false;
        for (int yy = y - 1; yy <= y + oh; yy++) for (int xx = x - 1; xx <= x + ow; xx++) {
            if (!dentro(xx, yy, w, h)) continue;
            int i = yy * w + xx;
            if (path[i] || water[i]) return false;
        }
        return true;
    }

    private static boolean seSolapan(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    private static DefinicionObjeto buscarObjeto(List<DefinicionObjeto> lista, int indice) {
        if (lista == null) return null;
        for (DefinicionObjeto obj : lista) if (obj != null && obj.indice == indice) return obj;
        return null;
    }

    private static int encontrarCaminoY(boolean[] path, int w, int h, int x) {
        for (int y = 0; y < h; y++) if (path[y * w + x]) return y;
        return h / 2;
    }

    private static void marcarHorizontal(boolean[] a, int w, int h, int y, int grosor) {
        for (int x = 0; x < w; x++) for (int dy = 0; dy < grosor; dy++)
            marcar(a, w, h, x, y + dy - (grosor - 1) / 2);
    }

    private static void marcarVertical(boolean[] a, int w, int h, int x, int grosor) {
        for (int y = 0; y < h; y++) for (int dx = 0; dx < grosor; dx++)
            marcar(a, w, h, x + dx - (grosor - 1) / 2, y);
    }

    private static void marcar(boolean[] a, int w, int h, int x, int y) {
        if (dentro(x, y, w, h)) a[y * w + x] = true;
    }

    private static boolean dentro(int x, int y, int w, int h) {
        return x >= 0 && y >= 0 && x < w && y < h;
    }

    private static int limitar(int valor, int minimo, int maximo) {
        if (maximo < minimo) return Math.max(0, (minimo + maximo) / 2);
        return Math.max(minimo, Math.min(maximo, valor));
    }
}
