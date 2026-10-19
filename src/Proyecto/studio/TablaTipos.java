package Proyecto.studio;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** Tabla de eficacia de tipos (ataque x defensa). Editable, con la tabla oficial de la gen. 6+ por defecto. */
public final class TablaTipos {
    private static final int N = TipoPokemon.values().length;
    private final double[][] m = new double[N][N];

    public TablaTipos() {
        for (double[] fila : m) Arrays.fill(fila, 1.0);
    }

    public double multiplicador(TipoPokemon ataque, TipoPokemon defensa) {
        return m[ataque.ordinal()][defensa.ordinal()];
    }

    /** Multiplicador contra un Pokémon de uno o dos tipos ({@code defensa2} puede ser null). */
    public double multiplicador(TipoPokemon ataque, TipoPokemon defensa1, TipoPokemon defensa2) {
        double r = multiplicador(ataque, defensa1);
        return defensa2 == null ? r : r * multiplicador(ataque, defensa2);
    }

    public void establecer(TipoPokemon ataque, TipoPokemon defensa, double mult) {
        if (mult < 0) throw new IllegalArgumentException("El multiplicador no puede ser negativo");
        m[ataque.ordinal()][defensa.ordinal()] = mult;
    }

    private void fila(TipoPokemon atq, String muyEficaz, String pocoEficaz, String inmune) {
        aplicar(atq, muyEficaz, 2.0);
        aplicar(atq, pocoEficaz, 0.5);
        aplicar(atq, inmune, 0.0);
    }

    private void aplicar(TipoPokemon atq, String lista, double mult) {
        for (String s : lista.trim().split("\\s+")) {
            if (!s.isEmpty()) establecer(atq, TipoPokemon.valueOf(s), mult);
        }
    }

    /** Tabla oficial (generación 6 en adelante, con tipo Hada). */
    public static TablaTipos estandar() {
        TablaTipos t = new TablaTipos();
        t.fila(TipoPokemon.NORMAL, "", "ROCA ACERO", "FANTASMA");
        t.fila(TipoPokemon.FUEGO, "PLANTA HIELO BICHO ACERO", "FUEGO AGUA ROCA DRAGON", "");
        t.fila(TipoPokemon.AGUA, "FUEGO TIERRA ROCA", "AGUA PLANTA DRAGON", "");
        t.fila(TipoPokemon.ELECTRICO, "AGUA VOLADOR", "ELECTRICO PLANTA DRAGON", "TIERRA");
        t.fila(TipoPokemon.PLANTA, "AGUA TIERRA ROCA", "FUEGO PLANTA VENENO VOLADOR BICHO DRAGON ACERO", "");
        t.fila(TipoPokemon.HIELO, "PLANTA TIERRA VOLADOR DRAGON", "FUEGO AGUA HIELO ACERO", "");
        t.fila(TipoPokemon.LUCHA, "NORMAL HIELO ROCA SINIESTRO ACERO", "VENENO VOLADOR PSIQUICO BICHO HADA", "FANTASMA");
        t.fila(TipoPokemon.VENENO, "PLANTA HADA", "VENENO TIERRA ROCA FANTASMA", "ACERO");
        t.fila(TipoPokemon.TIERRA, "FUEGO ELECTRICO VENENO ROCA ACERO", "PLANTA BICHO", "VOLADOR");
        t.fila(TipoPokemon.VOLADOR, "PLANTA LUCHA BICHO", "ELECTRICO ROCA ACERO", "");
        t.fila(TipoPokemon.PSIQUICO, "LUCHA VENENO", "PSIQUICO ACERO", "SINIESTRO");
        t.fila(TipoPokemon.BICHO, "PLANTA PSIQUICO SINIESTRO", "FUEGO LUCHA VENENO VOLADOR FANTASMA ACERO HADA", "");
        t.fila(TipoPokemon.ROCA, "FUEGO HIELO VOLADOR BICHO", "LUCHA TIERRA ACERO", "");
        t.fila(TipoPokemon.FANTASMA, "PSIQUICO FANTASMA", "SINIESTRO", "NORMAL");
        t.fila(TipoPokemon.DRAGON, "DRAGON", "ACERO", "HADA");
        t.fila(TipoPokemon.SINIESTRO, "PSIQUICO FANTASMA", "LUCHA SINIESTRO HADA", "");
        t.fila(TipoPokemon.ACERO, "HIELO ROCA HADA", "FUEGO AGUA ELECTRICO ACERO", "");
        t.fila(TipoPokemon.HADA, "LUCHA DRAGON SINIESTRO", "FUEGO VENENO ACERO", "");
        return t;
    }

    // --- JSON: { "fire": { "grass": 2.0, ... } } solo con las entradas distintas de 1 ---

    public Map<String, Object> aMapa() {
        Map<String, Object> out = new LinkedHashMap<>();
        for (TipoPokemon a : TipoPokemon.values()) {
            Map<String, Object> fila = new LinkedHashMap<>();
            for (TipoPokemon d : TipoPokemon.values()) {
                double v = multiplicador(a, d);
                if (v != 1.0) fila.put(d.clave(), v);
            }
            out.put(a.clave(), fila);
        }
        return out;
    }

    public static TablaTipos desdeMapa(Map<String, Object> mapa) {
        TablaTipos t = new TablaTipos();
        for (Map.Entry<String, Object> e : mapa.entrySet()) {
            TipoPokemon a = TipoPokemon.buscar(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo desconocido en tipos.json: " + e.getKey()));
            for (Map.Entry<String, Object> f : Json.mapa(e.getValue()).entrySet()) {
                TipoPokemon d = TipoPokemon.buscar(f.getKey())
                        .orElseThrow(() -> new IllegalArgumentException("Tipo desconocido en tipos.json: " + f.getKey()));
                t.establecer(a, d, ((Number) f.getValue()).doubleValue());
            }
        }
        return t;
    }
}
