package Proyecto.studio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON mínimo y sin dependencias (el proyecto no usa Maven/Gradle).
 * Objetos -> LinkedHashMap, arrays -> ArrayList, números -> Long o Double.
 */
public final class Json {
    private Json() {}

    // ------------------------------------------------------------------ lectura

    public static Object parse(String texto) {
        Parser p = new Parser(texto);
        p.saltar();
        Object v = p.valor();
        p.saltar();
        if (p.i < texto.length()) throw p.error("texto sobrante");
        return v;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> mapa(Object o) {
        if (o instanceof Map<?, ?>) return (Map<String, Object>) o;
        throw new IllegalArgumentException("Se esperaba un objeto JSON y llegó: " + o);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> lista(Object o) {
        if (o == null) return new ArrayList<>();
        if (o instanceof List<?>) return (List<Object>) o;
        throw new IllegalArgumentException("Se esperaba un array JSON y llegó: " + o);
    }

    public static String texto(Map<String, Object> m, String clave, String porDefecto) {
        Object v = m.get(clave);
        return v == null ? porDefecto : v.toString();
    }

    public static int entero(Map<String, Object> m, String clave, int porDefecto) {
        Object v = m.get(clave);
        return v instanceof Number n ? n.intValue() : porDefecto;
    }

    public static double decimal(Map<String, Object> m, String clave, double porDefecto) {
        Object v = m.get(clave);
        return v instanceof Number n ? n.doubleValue() : porDefecto;
    }

    public static List<String> textos(Object o) {
        List<String> r = new ArrayList<>();
        for (Object x : lista(o)) r.add(String.valueOf(x));
        return r;
    }

    // ---------------------------------------------------------------- escritura

    public static String escribir(Object valor) {
        StringBuilder sb = new StringBuilder();
        escribir(sb, valor, 0);
        return sb.append('\n').toString();
    }

    private static void escribir(StringBuilder sb, Object v, int nivel) {
        if (v == null) {
            sb.append("null");
        } else if (v instanceof String s) {
            cadena(sb, s);
        } else if (v instanceof Boolean || v instanceof Integer || v instanceof Long) {
            sb.append(v);
        } else if (v instanceof Number n) {
            double d = n.doubleValue();
            if (Double.isNaN(d) || Double.isInfinite(d))
                throw new IllegalArgumentException("Número no representable en JSON: " + d);
            sb.append(Double.toString(d));
        } else if (v instanceof Map<?, ?> m) {
            if (m.isEmpty()) { sb.append("{}"); return; }
            sb.append("{\n");
            int k = 0;
            for (Map.Entry<?, ?> e : m.entrySet()) {
                sangria(sb, nivel + 1);
                cadena(sb, String.valueOf(e.getKey()));
                sb.append(": ");
                escribir(sb, e.getValue(), nivel + 1);
                if (++k < m.size()) sb.append(',');
                sb.append('\n');
            }
            sangria(sb, nivel);
            sb.append('}');
        } else if (v instanceof Collection<?> c) {
            if (c.isEmpty()) { sb.append("[]"); return; }
            boolean simple = true;
            for (Object x : c) if (x instanceof Map<?, ?> || x instanceof Collection<?>) { simple = false; break; }
            if (simple) {                       // arrays de escalares en una sola línea
                sb.append('[');
                int k = 0;
                for (Object x : c) {
                    if (k++ > 0) sb.append(", ");
                    escribir(sb, x, nivel);
                }
                sb.append(']');
                return;
            }
            sb.append("[\n");
            int k = 0;
            for (Object x : c) {
                sangria(sb, nivel + 1);
                escribir(sb, x, nivel + 1);
                if (++k < c.size()) sb.append(',');
                sb.append('\n');
            }
            sangria(sb, nivel);
            sb.append(']');
        } else {
            throw new IllegalArgumentException("Tipo no serializable en JSON: " + v.getClass());
        }
    }

    private static void sangria(StringBuilder sb, int nivel) {
        for (int i = 0; i < nivel; i++) sb.append("  ");
    }

    private static void cadena(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        sb.append('"');
    }

    // ------------------------------------------------------------------- parser

    private static final class Parser {
        final String t;
        int i = 0;

        Parser(String t) { this.t = t; }

        IllegalArgumentException error(String msg) {
            return new IllegalArgumentException("JSON inválido en la posición " + i + ": " + msg);
        }

        void saltar() {
            while (i < t.length() && Character.isWhitespace(t.charAt(i))) i++;
        }

        Object valor() {
            if (i >= t.length()) throw error("fin de texto inesperado");
            char c = t.charAt(i);
            switch (c) {
                case '{': return objeto();
                case '[': return array();
                case '"': return cadena();
                case 't': literal("true"); return Boolean.TRUE;
                case 'f': literal("false"); return Boolean.FALSE;
                case 'n': literal("null"); return null;
                default: return numero();
            }
        }

        void literal(String l) {
            if (!t.startsWith(l, i)) throw error("se esperaba " + l);
            i += l.length();
        }

        Map<String, Object> objeto() {
            Map<String, Object> m = new LinkedHashMap<>();
            i++; // {
            saltar();
            if (i < t.length() && t.charAt(i) == '}') { i++; return m; }
            while (true) {
                saltar();
                if (i >= t.length() || t.charAt(i) != '"') throw error("se esperaba una clave");
                String k = cadena();
                saltar();
                if (i >= t.length() || t.charAt(i) != ':') throw error("se esperaba ':'");
                i++;
                saltar();
                m.put(k, valor());
                saltar();
                if (i >= t.length()) throw error("objeto sin cerrar");
                char c = t.charAt(i++);
                if (c == '}') return m;
                if (c != ',') throw error("se esperaba ',' o '}'");
            }
        }

        List<Object> array() {
            List<Object> l = new ArrayList<>();
            i++; // [
            saltar();
            if (i < t.length() && t.charAt(i) == ']') { i++; return l; }
            while (true) {
                saltar();
                l.add(valor());
                saltar();
                if (i >= t.length()) throw error("array sin cerrar");
                char c = t.charAt(i++);
                if (c == ']') return l;
                if (c != ',') throw error("se esperaba ',' o ']'");
            }
        }

        String cadena() {
            StringBuilder sb = new StringBuilder();
            i++; // "
            while (true) {
                if (i >= t.length()) throw error("cadena sin cerrar");
                char c = t.charAt(i++);
                if (c == '"') return sb.toString();
                if (c != '\\') { sb.append(c); continue; }
                if (i >= t.length()) throw error("escape incompleto");
                char e = t.charAt(i++);
                switch (e) {
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'u' -> {
                        if (i + 4 > t.length()) throw error("\\u incompleto");
                        sb.append((char) Integer.parseInt(t.substring(i, i + 4), 16));
                        i += 4;
                    }
                    default -> sb.append(e); // \" \\ \/
                }
            }
        }

        Object numero() {
            int ini = i;
            while (i < t.length() && "+-0123456789.eE".indexOf(t.charAt(i)) >= 0) i++;
            String s = t.substring(ini, i);
            if (s.isEmpty()) throw error("valor no reconocido");
            try {
                if (s.contains(".") || s.contains("e") || s.contains("E")) return Double.parseDouble(s);
                return Long.parseLong(s);
            } catch (NumberFormatException ex) {
                throw error("número no válido: " + s);
            }
        }
    }
}
