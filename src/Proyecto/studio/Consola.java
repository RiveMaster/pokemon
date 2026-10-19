package Proyecto.studio;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Entrada/salida por consola para el editor. Lee siempre líneas completas (nextLine), así que no deja
 * saltos de línea pendientes. Convención en todos los datos: Enter = mantener el valor actual.
 */
final class Consola {
    /** Se lanza cuando se acaba la entrada (Ctrl+D o script terminado). */
    static final class FinEntrada extends RuntimeException {
        FinEntrada() { super("Fin de la entrada"); }
    }

    private final Scanner sc;

    Consola(Scanner sc) { this.sc = sc; }

    Scanner scanner() { return sc; }

    void println(String s) { System.out.println(s); }

    String leer(String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) throw new FinEntrada();
        return sc.nextLine().trim();
    }

    static String norm(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT).trim();
    }

    // ------------------------------------------------------------- valores simples

    String texto(String etiqueta, String actual) {
        String r = leer(etiqueta + (actual == null || actual.isEmpty() ? "" : " [" + actual + "]") + ": ");
        return r.isEmpty() ? actual : r;
    }

    String textoObligatorio(String etiqueta, String actual) {
        while (true) {
            String r = texto(etiqueta, actual);
            if (r != null && !r.isBlank()) return r;
            println("  Este dato es obligatorio.");
        }
    }

    int entero(String etiqueta, int actual, int min, int max) {
        while (true) {
            String r = leer(etiqueta + " [" + actual + "] (" + min + ".." + max + "): ");
            if (r.isEmpty()) return actual;
            try {
                int v = Integer.parseInt(r);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignorada) { /* se vuelve a preguntar */ }
            println("  Valor no válido.");
        }
    }

    double decimal(String etiqueta, double actual, double min, double max) {
        while (true) {
            String r = leer(etiqueta + " [" + actual + "] (" + min + ".." + max + "): ");
            if (r.isEmpty()) return actual;
            try {
                double v = Double.parseDouble(r.replace(',', '.'));
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignorada) { /* se vuelve a preguntar */ }
            println("  Valor no válido.");
        }
    }

    boolean confirmar(String pregunta, boolean porDefecto) {
        String r = norm(leer(pregunta + (porDefecto ? " [S/n]: " : " [s/N]: ")));
        if (r.isEmpty()) return porDefecto;
        return r.startsWith("s") || r.startsWith("y");
    }

    // ------------------------------------------------------------------- menús

    /** Devuelve 1..n, o 0 para volver. */
    int menu(String titulo, String... opciones) {
        println("\n" + titulo);
        for (int i = 0; i < opciones.length; i++) println((i + 1) + ". " + opciones[i]);
        println("0. Volver");
        while (true) {
            String r = leer("Elige: ");
            try {
                int v = Integer.parseInt(r);
                if (v >= 0 && v <= opciones.length) return v;
            } catch (NumberFormatException ignorada) { /* se vuelve a preguntar */ }
            println("  Opción no válida.");
        }
    }

    /** Elige un valor de una lista numerada; Enter mantiene {@code actual}. */
    <T> T elegir(String etiqueta, List<T> valores, Function<T, String> nombre, T actual) {
        for (int i = 0; i < valores.size(); i++) println("  " + (i + 1) + ". " + nombre.apply(valores.get(i)));
        while (true) {
            String r = leer(etiqueta + " [" + (actual == null ? "ninguno" : nombre.apply(actual)) + "]: ");
            if (r.isEmpty()) return actual;
            try {
                int v = Integer.parseInt(r);
                if (v >= 1 && v <= valores.size()) return valores.get(v - 1);
            } catch (NumberFormatException ignorada) { /* se vuelve a preguntar */ }
            println("  Valor no válido.");
        }
    }

    <T> T elegirEnum(String etiqueta, T[] valores, T actual, Function<T, String> nombre) {
        return elegir(etiqueta, Arrays.asList(valores), nombre, actual);
    }

    /** Número o nombre de tipo. Enter mantiene; "0" = ninguno si está permitido. */
    TipoPokemon tipo(String etiqueta, TipoPokemon actual, boolean permitirNinguno) {
        TipoPokemon[] ts = TipoPokemon.values();
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < ts.length; i++) {
            sb.append(String.format("%2d.%-11s", i + 1, ts[i].nombre()));
            if ((i + 1) % 6 == 0 && i + 1 < ts.length) sb.append("\n  ");
        }
        println(sb.toString());
        while (true) {
            String r = leer(etiqueta + " [" + (actual == null ? "ninguno" : actual.nombre()) + "]"
                    + (permitirNinguno ? " (0 = ninguno)" : "") + ": ");
            if (r.isEmpty()) return actual;
            if (permitirNinguno && r.equals("0")) return null;
            try {
                int v = Integer.parseInt(r);
                if (v >= 1 && v <= ts.length) return ts[v - 1];
            } catch (NumberFormatException e) {
                TipoPokemon t = TipoPokemon.buscar(r).orElse(null);
                if (t != null) return t;
            }
            println("  Tipo no válido.");
        }
    }

    // ---------------------------------------------------------------- listas

    List<String> listaTexto(String etiqueta, List<String> actual, String separador) {
        String sep = separador.equals("|") ? "\\|" : separador;
        while (true) {
            String r = leer(etiqueta + " [" + String.join(separador.equals("|") ? " | " : ", ", actual)
                    + "] (separa con '" + separador + "', '-' = vaciar): ");
            if (r.isEmpty()) return actual;
            if (r.equals("-")) return new ArrayList<>();
            List<String> l = new ArrayList<>();
            for (String p : r.split(sep)) if (!p.isBlank()) l.add(p.trim());
            return l;
        }
    }

    List<TipoPokemon> listaTipos(String etiqueta, List<TipoPokemon> actual) {
        List<String> nombres = new ArrayList<>();
        for (TipoPokemon t : actual) nombres.add(t.nombre());
        while (true) {
            String r = leer(etiqueta + " [" + String.join(", ", nombres) + "] (separa con comas, '-' = vaciar): ");
            if (r.isEmpty()) return actual;
            if (r.equals("-")) return new ArrayList<>();
            List<TipoPokemon> l = new ArrayList<>();
            boolean ok = true;
            for (String p : r.split(",")) {
                if (p.isBlank()) continue;
                TipoPokemon t = TipoPokemon.buscar(p).orElse(null);
                if (t == null) { println("  Tipo desconocido: " + p.trim()); ok = false; break; }
                if (!l.contains(t)) l.add(t);
            }
            if (ok) return l;
        }
    }

    // -------------------------------------------------------------- búsqueda

    /** Resuelve un texto contra una colección: coincidencia exacta, única, o elección numerada. Null si nada. */
    <T> T resolver(String q, Collection<T> items, Function<T, String> clave, Function<T, String> etiqueta) {
        String n = norm(q);
        List<T> res = new ArrayList<>();
        for (T t : items) {
            if (norm(etiqueta.apply(t)).contains(n) || norm(clave.apply(t)).contains(n)) res.add(t);
        }
        for (T t : res) {
            if (norm(clave.apply(t)).equals(n) || norm(etiqueta.apply(t)).equals(n)) return t;
        }
        if (res.isEmpty()) { println("  Sin resultados para '" + q + "'."); return null; }
        if (res.size() == 1) { println("  -> " + etiqueta.apply(res.get(0))); return res.get(0); }
        int max = Math.min(res.size(), 15);
        for (int i = 0; i < max; i++) println("  " + (i + 1) + ". " + etiqueta.apply(res.get(i)));
        if (res.size() > max) println("  ... y " + (res.size() - max) + " más; afina la búsqueda.");
        String r = leer("Número (0 = descartar): ");
        try {
            int v = Integer.parseInt(r);
            if (v >= 1 && v <= max) return res.get(v - 1);
        } catch (NumberFormatException ignorada) { /* descartado */ }
        return null;
    }

    /** Pide un texto a buscar hasta resolverlo o cancelar con Enter vacío (devuelve null). */
    <T> T buscar(String titulo, Collection<T> items, Function<T, String> clave, Function<T, String> etiqueta) {
        if (items.isEmpty()) { println("  No hay ningún elemento para elegir."); return null; }
        while (true) {
            String q = leer(titulo + " (texto a buscar, Enter = cancelar): ");
            if (q.isEmpty()) return null;
            T t = resolver(q, items, clave, etiqueta);
            if (t != null) return t;
        }
    }

    /**
     * Referencia opcional por símbolo: Enter mantiene, "0" quita, otro texto busca.
     * Devuelve el símbolo resultante (null = ninguno).
     */
    <T> String referencia(String etiqueta, String actual, Collection<T> items,
                          Function<T, String> clave, Function<T, String> nombre) {
        while (true) {
            String r = leer(etiqueta + " [" + (actual == null ? "ninguna" : actual)
                    + "] (Enter = mantener, 0 = ninguna, o texto a buscar): ");
            if (r.isEmpty()) return actual;
            if (r.equals("0")) return null;
            T t = resolver(r, items, clave, nombre);
            if (t != null) return clave.apply(t);
        }
    }
}
