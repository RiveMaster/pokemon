package Proyecto.studio;

import java.util.ArrayList;
import java.util.List;

/** Editores de movimientos, habilidades, objetos y tabla de tipos. */
final class EditorDatos {
    private final Consola c;
    private final BaseDatos bd;
    private final Runnable marcar;

    EditorDatos(Consola c, BaseDatos bd, Runnable marcar) {
        this.c = c;
        this.bd = bd;
        this.marcar = marcar;
    }

    private static int siguienteId(java.util.Collection<Integer> ids) {
        return ids.stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }

    // ------------------------------------------------------------------ movimientos

    void movimientos() {
        new Crud<DatosMovimiento>(c, "Movimientos", () -> new ArrayList<>(bd.movimientos().values()),
                DatosMovimiento::simbolo,
                m -> String.format("%-22s %-10s %-8s pot %-6s prec %-3d PP %d", m.nombre(), m.tipo().nombre(),
                        m.categoria().clave(), m.potencia() == (long) m.potencia() ? Long.toString((long) m.potencia()) : Double.toString(m.potencia()),
                        m.precision(), m.pp()),
                m -> m.nombre() + " (" + m.simbolo() + ")\n  " + m.tipo().nombre() + ", " + m.categoria().clave()
                        + ", potencia " + m.potencia() + ", precisión " + m.precision() + ", PP " + m.pp()
                        + ", prioridad " + m.prioridad()
                        + (m.efecto() == null ? "" : "\n  Efecto: " + m.efecto().nombre() + " (" + m.efecto().estado() + " " + m.efecto().probabilidad() + "%, daño extra " + m.efecto().dano() + ")")
                        + (m.banderas().isEmpty() ? "" : "\n  Banderas: " + m.banderas()),
                this::pedirMovimiento, bd::poner, this::borrarMovimiento, marcar).menu();
    }

    private DatosMovimiento pedirMovimiento(DatosMovimiento a) {
        String nombre = c.textoObligatorio("Nombre", a == null ? null : a.nombre());
        String sim = a != null ? a.simbolo()
                : PuenteProyecto.unico(PuenteProyecto.simbolo(nombre), bd.movimientos().keySet());
        int id = a != null ? a.id() : siguienteId(bd.movimientos().values().stream().map(DatosMovimiento::id).toList());
        TipoPokemon tipo = c.tipo("Tipo", a == null ? TipoPokemon.NORMAL : a.tipo(), false);
        CategoriaMovimiento cat = c.elegirEnum("Categoría", CategoriaMovimiento.values(),
                a == null ? CategoriaMovimiento.FISICO : a.categoria(), CategoriaMovimiento::clave);
        double potencia = c.decimal("Potencia", a == null ? 40 : a.potencia(), 0, 1e10);
        int precision = c.entero("Precisión (0 = nunca falla)", a == null ? 100 : a.precision(), 0, 100);
        int pp = c.entero("PP (0 = sin límite)", a == null ? 20 : a.pp(), 0, 100);
        int prioridad = c.entero("Prioridad", a == null ? 0 : a.prioridad(), -7, 5);

        DatosEfecto ef = a == null ? null : a.efecto();
        List<String> opciones = new ArrayList<>();
        opciones.add("(sin estado)");
        opciones.addAll(DatosEfecto.ESTADOS);
        String estadoActual = ef == null || ef.estado() == null ? "(sin estado)" : ef.estado();
        String estado = c.elegir("Estado alterado que provoca", opciones, s -> s, estadoActual);
        if (estado.equals("(sin estado)")) {
            ef = null;
        } else {
            int prob = c.entero("Probabilidad del estado (%)", ef == null ? 10 : ef.probabilidad(), 0, 100);
            ef = new DatosEfecto(ef == null ? capitalizar(estado) : ef.nombre(), ef == null ? 0 : ef.dano(), estado, prob);
        }
        List<String> banderas = c.listaTexto("Banderas", a == null ? List.of() : a.banderas(), ",");
        return new DatosMovimiento(id, sim, nombre, tipo, cat, potencia, precision, pp, prioridad, ef, banderas);
    }

    private static String capitalizar(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    private void borrarMovimiento(DatosMovimiento m) {
        List<String> refs = Referencias.deMovimiento(bd, m.simbolo());
        if (!refs.isEmpty()) {
            c.println("  Está en uso (" + refs.size() + "):");
            refs.stream().limit(8).forEach(r -> c.println("   - " + r));
            if (refs.size() > 8) c.println("   ... y " + (refs.size() - 8) + " más");
        }
        if (!c.confirmar("¿Borrar " + m.nombre() + (refs.isEmpty() ? "" : " y quitar esas referencias") + "?", false)) return;
        Referencias.borrarMovimiento(bd, m.simbolo());
        marcar.run();
        c.println("  Borrado.");
    }

    // ------------------------------------------------------------------ habilidades

    void habilidades() {
        new Crud<DatosHabilidad>(c, "Habilidades", () -> new ArrayList<>(bd.habilidades().values()),
                DatosHabilidad::simbolo, h -> h.nombre() + " (" + h.simbolo() + ")",
                h -> h.nombre() + " (" + h.simbolo() + ")\n  " + h.descripcion(),
                a -> {
                    String nombre = c.textoObligatorio("Nombre", a == null ? null : a.nombre());
                    String sim = a != null ? a.simbolo()
                            : PuenteProyecto.unico(PuenteProyecto.simbolo(nombre), bd.habilidades().keySet());
                    String desc = c.texto("Descripción", a == null ? "" : a.descripcion());
                    c.println("  Efecto en combate (solo si la regla «habilidades» está activada):");
                    String efecto = c.elegir("Efecto", DatosHabilidad.EFECTOS, s -> s, a == null ? "ninguno" : a.efecto());
                    TipoPokemon tipoH = a == null ? null : a.tipo();
                    String estadoH = a == null ? null : a.estado();
                    double valorH = a == null ? 0 : a.valor();
                    if (efecto.equals("potencia_tipo") || efecto.equals("inmunidad_tipo")) tipoH = c.tipo("Tipo afectado", tipoH, false);
                    if (efecto.equals("contacto_estado"))
                        estadoH = c.elegir("Estado que provoca", DatosEfecto.ESTADOS, s -> s, estadoH == null ? "PARALIZADO" : estadoH);
                    if (efecto.equals("potencia_tipo")) valorH = c.decimal("Multiplicador de daño", valorH <= 0 ? 1.5 : valorH, 1, 5);
                    if (efecto.equals("contacto_estado")) valorH = c.decimal("Probabilidad (%)", valorH <= 0 ? 30 : valorH, 1, 100);
                    if (efecto.equals("regeneracion")) valorH = c.decimal("Recupera 1/N de la vida máxima, N =", valorH <= 0 ? 16 : valorH, 2, 64);
                    return new DatosHabilidad(sim, nombre, desc, efecto, tipoH, estadoH, valorH);
                },
                bd::poner,
                h -> {
                    List<String> refs = Referencias.deHabilidad(bd, h.simbolo());
                    if (!refs.isEmpty()) c.println("  La usan " + refs.size() + " Pokémon.");
                    if (!c.confirmar("¿Borrar " + h.nombre() + (refs.isEmpty() ? "" : " y quitarla de ellos") + "?", false)) return;
                    Referencias.borrarHabilidad(bd, h.simbolo());
                    marcar.run();
                    c.println("  Borrada.");
                }, marcar).menu();
    }

    // --------------------------------------------------------------------- objetos

    void objetos() {
        new Crud<DatosObjeto>(c, "Objetos", () -> new ArrayList<>(bd.objetos().values()),
                DatosObjeto::simbolo, o -> String.format("%-24s %-12s %5d $", o.nombre(), o.bolsillo(), o.precio()),
                o -> o.nombre() + " (" + o.simbolo() + ")\n  Bolsillo " + o.bolsillo() + ", precio " + o.precio()
                        + "\n  " + o.descripcion() + (o.banderas().isEmpty() ? "" : "\n  Banderas: " + o.banderas()),
                a -> {
                    String nombre = c.textoObligatorio("Nombre", a == null ? null : a.nombre());
                    String sim = a != null ? a.simbolo()
                            : PuenteProyecto.unico(PuenteProyecto.simbolo(nombre), bd.objetos().keySet());
                    int id = a != null ? a.id() : siguienteId(bd.objetos().values().stream().map(DatosObjeto::id).toList());
                    return new DatosObjeto(id, sim, nombre, c.texto("Bolsillo", a == null ? "otros" : a.bolsillo()),
                            c.entero("Precio", a == null ? 100 : a.precio(), 0, 1_000_000),
                            c.texto("Descripción", a == null ? "" : a.descripcion()),
                            c.listaTexto("Banderas", a == null ? List.of() : a.banderas(), ","));
                },
                bd::poner,
                o -> {
                    List<String> refs = Referencias.deObjeto(bd, o.simbolo());
                    refs.stream().limit(5).forEach(r -> c.println("   - " + r));
                    if (!c.confirmar("¿Borrar " + o.nombre() + (refs.isEmpty() ? "" : " y quitar esas evoluciones") + "?", false)) return;
                    Referencias.borrarObjeto(bd, o.simbolo());
                    marcar.run();
                    c.println("  Borrado.");
                }, marcar).menu();
    }

    // ------------------------------------------------------------------------ tipos

    void tipos() {
        while (true) {
            int op = c.menu("══ TABLA DE TIPOS ══", "Consultar un ataque contra un Pokémon",
                    "Ver todo lo que hace un tipo atacante", "Cambiar una relación", "Restaurar la tabla oficial");
            switch (op) {
                case 0 -> { return; }
                case 1 -> {
                    TipoPokemon a = c.tipo("Tipo del ataque", TipoPokemon.AGUA, false);
                    TipoPokemon d1 = c.tipo("Tipo 1 del defensor", TipoPokemon.FUEGO, false);
                    TipoPokemon d2 = c.tipo("Tipo 2 del defensor", null, true);
                    c.println("  " + a.nombre() + " → " + d1.nombre() + (d2 == null ? "" : "/" + d2.nombre())
                            + ": x" + bd.tipos().multiplicador(a, d1, d2));
                }
                case 2 -> {
                    TipoPokemon a = c.tipo("Tipo atacante", TipoPokemon.FUEGO, false);
                    StringBuilder x2 = new StringBuilder(), x05 = new StringBuilder(), x0 = new StringBuilder();
                    for (TipoPokemon d : TipoPokemon.values()) {
                        double m = bd.tipos().multiplicador(a, d);
                        StringBuilder sb = m == 2.0 ? x2 : m == 0.5 ? x05 : m == 0.0 ? x0 : null;
                        if (sb != null) sb.append(d.nombre()).append(' ');
                        else if (m != 1.0) x2.append(d.nombre()).append("(x").append(m).append(") ");
                    }
                    c.println("  Muy eficaz (x2): " + x2 + "\n  Poco eficaz (x0.5): " + x05 + "\n  Sin efecto (x0): " + x0);
                }
                case 3 -> {
                    TipoPokemon a = c.tipo("Tipo atacante", null, false);
                    TipoPokemon d = c.tipo("Tipo defensor", null, false);
                    double actual = bd.tipos().multiplicador(a, d);
                    double nuevo = c.decimal("Multiplicador de " + a.nombre() + " contra " + d.nombre(), actual, 0, 4);
                    if (nuevo != actual) { bd.tipos().establecer(a, d, nuevo); marcar.run(); c.println("  Cambiado."); }
                }
                case 4 -> {
                    if (c.confirmar("¿Restaurar toda la tabla a la oficial?", false)) {
                        bd.tipos(TablaTipos.estandar());
                        marcar.run();
                        c.println("  Tabla restaurada.");
                    }
                }
                default -> { }
            }
        }
    }
}
