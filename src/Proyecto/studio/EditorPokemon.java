package Proyecto.studio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Editor de especies: crear, ver, editar (stats, evolución, movimientos, habilidades, formas) y borrar. */
final class EditorPokemon {
    private final Consola c;
    private final BaseDatos bd;
    private final Runnable marcar;

    EditorPokemon(Consola c, BaseDatos bd, Runnable marcarCambios) {
        this.c = c;
        this.bd = bd;
        this.marcar = marcarCambios;
    }

    void menu() {
        while (true) {
            int op = c.menu("══ POKÉMON (" + bd.especies().size() + ") ══",
                    "Buscar / listar", "Ver ficha", "Crear", "Editar", "Borrar");
            switch (op) {
                case 0 -> { return; }
                case 1 -> listar();
                case 2 -> { DatosEspecie e = elegir("Pokémon"); if (e != null) ficha(e); }
                case 3 -> crear();
                case 4 -> { DatosEspecie e = elegir("Pokémon a editar"); if (e != null) editar(e); }
                case 5 -> borrar();
                default -> { }
            }
        }
    }

    private DatosEspecie elegir(String titulo) {
        return c.buscar(titulo, bd.especies().values(), DatosEspecie::simbolo, DatosEspecie::nombre);
    }

    private String nombreMov(String sim) {
        DatosMovimiento m = bd.movimiento(sim);
        return m == null ? sim + " (¡no existe!)" : m.nombre();
    }

    private static String tipos(DatosForma f) {
        return f.tipo1().nombre() + (f.tipo2() == null ? "" : "/" + f.tipo2().nombre());
    }

    // ------------------------------------------------------------------ listar / ver

    private void listar() {
        String q = c.leer("Filtro por nombre o tipo (Enter = todos): ");
        String n = Consola.norm(q);
        int mostrados = 0, total = 0;
        for (DatosEspecie e : bd.especies().values()) {
            DatosForma f = e.forma(0);
            boolean ok = n.isEmpty() || Consola.norm(e.nombre()).contains(n)
                    || Consola.norm(tipos(f)).contains(n);
            if (!ok) continue;
            total++;
            if (mostrados < 40) {
                mostrados++;
                int suma = 0;
                for (Estadistica s : Estadistica.values()) suma += f.stat(s);
                c.println(String.format("  #%-4d %-16s %-18s total %d", e.id(), e.nombre(), tipos(f), suma));
            }
        }
        if (total > mostrados) c.println("  ... y " + (total - mostrados) + " más; afina el filtro.");
        if (total == 0) c.println("  Sin resultados.");
    }

    private void ficha(DatosEspecie e) {
        c.println("\n#" + e.id() + " " + e.nombre() + "  (" + e.simbolo() + ")");
        for (DatosForma f : e.formas()) {
            c.println("── Forma " + f.forma() + " · " + tipos(f));
            StringBuilder sb = new StringBuilder("  Stats: ");
            for (Estadistica s : Estadistica.values()) sb.append(s.clave()).append('=').append(f.stat(s)).append(' ');
            c.println(sb.toString());
            c.println("  Altura " + f.altura() + " m, peso " + f.peso() + " kg, curva " + f.curva()
                    + ", captura " + f.ratioCaptura() + ", exp base " + f.expBase());
            for (DatosEvolucion ev : f.evoluciones()) {
                DatosEspecie d = bd.especie(ev.destino());
                c.println("  Evoluciona a " + (d == null ? ev.destino() + " (¡no existe!)" : d.nombre()) + " " + ev.condiciones());
            }
            if (!f.habilidades().isEmpty() || f.habilidadOculta() != null)
                c.println("  Habilidades: " + f.habilidades() + (f.habilidadOculta() == null ? "" : " + oculta " + f.habilidadOculta()));
            if (!f.porNivel().isEmpty()) {
                StringBuilder m = new StringBuilder("  Movimientos: ");
                for (Aprendizaje a : f.porNivel()) m.append("N").append(a.nivel()).append(' ').append(nombreMov(a.movimiento())).append(", ");
                c.println(m.substring(0, m.length() - 2));
            }
            if (!f.mt().isEmpty()) c.println("  MT: " + f.mt());
            if (!f.huevo().isEmpty()) c.println("  Huevo: " + f.huevo());
            if (!f.tutor().isEmpty()) c.println("  Tutor: " + f.tutor());
        }
    }

    // ------------------------------------------------------------------ crear / borrar

    private void crear() {
        String nombre = c.textoObligatorio("Nombre", null);
        String sim = PuenteProyecto.unico(PuenteProyecto.simbolo(nombre), bd.especies().keySet());
        int id = 1;
        for (DatosEspecie e : bd.especies().values()) id = Math.max(id, e.id() + 1);
        TipoPokemon t1 = c.tipo("Tipo 1", TipoPokemon.NORMAL, false);
        TipoPokemon t2 = c.tipo("Tipo 2", null, true);
        DatosEspecie e = new DatosEspecie(id, sim, nombre, List.of(DatosForma.porDefecto(t1, t2)));
        bd.poner(e);
        marcar.run();
        c.println("  Creado #" + id + " " + nombre + " (símbolo: " + sim + "). Ahora puedes completarlo.");
        e = editarEstadisticas(e, 0);
        editar(e);
    }

    private void borrar() {
        DatosEspecie e = elegir("Pokémon a borrar");
        if (e == null) return;
        List<String> refs = Referencias.deEspecie(bd, e.simbolo());
        if (!refs.isEmpty()) {
            c.println("  Está en uso:");
            refs.stream().limit(10).forEach(r -> c.println("   - " + r));
            if (refs.size() > 10) c.println("   ... y " + (refs.size() - 10) + " más");
        }
        if (!c.confirmar("¿Borrar " + e.nombre() + (refs.isEmpty() ? "" : " y quitar esas referencias") + "?", false)) return;
        Referencias.borrarEspecie(bd, e.simbolo());
        marcar.run();
        c.println("  Borrado.");
    }

    // ------------------------------------------------------------------ editar

    private DatosEspecie actual(DatosEspecie e) { return bd.especie(e.simbolo()); }

    private DatosEspecie reemplazar(DatosEspecie e, DatosForma f) {
        List<DatosForma> formas = new ArrayList<>(e.formas());
        for (int i = 0; i < formas.size(); i++) if (formas.get(i).forma() == f.forma()) formas.set(i, f);
        DatosEspecie nueva = e.conFormas(formas);
        bd.poner(nueva);
        marcar.run();
        return nueva;
    }

    private void editar(DatosEspecie inicial) {
        int forma = 0;
        while (true) {
            DatosEspecie e = actual(inicial);
            int op = c.menu("Editando " + e.nombre() + " · forma " + forma + " (" + tipos(e.forma(forma)) + ")",
                    "Nombre, tipos y estadísticas", "Evolución", "Movimientos por nivel", "MT / huevo / tutor",
                    "Habilidades", "Captura, experiencia, cría y EVs", "Formas (cambiar / añadir / quitar)", "Ver ficha");
            switch (op) {
                case 0 -> { return; }
                case 1 -> editarEstadisticas(e, forma);
                case 2 -> editarEvolucion(e, forma);
                case 3 -> editarPorNivel(e, forma);
                case 4 -> editarListasMov(e, forma);
                case 5 -> editarHabilidades(e, forma);
                case 6 -> editarCria(e, forma);
                case 7 -> forma = editarFormas(e, forma);
                case 8 -> ficha(e);
                default -> { }
            }
        }
    }

    private static final Map<Estadistica, String> NOMBRE_STAT = new EnumMap<>(Map.of(
            Estadistica.VIDA, "Vida (PS)", Estadistica.ATAQUE, "Ataque", Estadistica.DEFENSA, "Defensa",
            Estadistica.VELOCIDAD, "Velocidad", Estadistica.ATAQUE_ESP, "Ataque especial",
            Estadistica.DEFENSA_ESP, "Defensa especial"));

    private DatosEspecie editarEstadisticas(DatosEspecie e, int numForma) {
        String nombre = c.textoObligatorio("Nombre", e.nombre());
        DatosForma f = e.forma(numForma);
        TipoPokemon t1 = c.tipo("Tipo 1", f.tipo1(), false);
        TipoPokemon t2 = c.tipo("Tipo 2", f.tipo2(), true);
        Map<Estadistica, Integer> stats = new EnumMap<>(Estadistica.class);
        for (Estadistica s : Estadistica.values())
            stats.put(s, c.entero("  " + NOMBRE_STAT.get(s), f.stat(s), 0, 255));
        DatosForma nueva = f.conTipos(t1, t2).conStats(stats);
        DatosEspecie renombrada = new DatosEspecie(e.id(), e.simbolo(), nombre, e.formas());
        return reemplazar(renombrada, nueva);
    }

    // ---- evolución

    private void editarEvolucion(DatosEspecie e, int numForma) {
        while (true) {
            e = actual(e);
            DatosForma f = e.forma(numForma);
            c.println("\nEvoluciones actuales:");
            if (f.evoluciones().isEmpty()) c.println("  (ninguna)");
            for (int i = 0; i < f.evoluciones().size(); i++) {
                DatosEvolucion ev = f.evoluciones().get(i);
                DatosEspecie d = bd.especie(ev.destino());
                c.println("  " + (i + 1) + ". " + (d == null ? ev.destino() + " (¡no existe!)" : d.nombre()) + " " + ev.condiciones());
            }
            int op = c.menu("Evolución", "Añadir evolución por nivel", "Añadir evolución por objeto",
                    "Quitar una", "Quitar todas");
            List<DatosEvolucion> evos = new ArrayList<>(f.evoluciones());
            switch (op) {
                case 0 -> { return; }
                case 1 -> {
                    DatosEspecie d = elegir("Evoluciona a");
                    if (d == null) break;
                    if (d.simbolo().equals(e.simbolo())) { c.println("  No puede evolucionar a sí misma."); break; }
                    evos.add(DatosEvolucion.porNivel(d.simbolo(), c.entero("Nivel mínimo", 16, 1, 100)));
                    reemplazar(e, f.conEvoluciones(evos));
                }
                case 2 -> {
                    DatosEspecie d = elegir("Evoluciona a");
                    if (d == null) break;
                    DatosObjeto o = c.buscar("Objeto necesario", bd.objetos().values(), DatosObjeto::simbolo, DatosObjeto::nombre);
                    if (o == null) break;
                    evos.add(new DatosEvolucion(d.simbolo(), Map.of(DatosEvolucion.OBJETO, o.simbolo())));
                    reemplazar(e, f.conEvoluciones(evos));
                }
                case 3 -> {
                    if (evos.isEmpty()) break;
                    int n = c.entero("Número a quitar", 1, 1, evos.size());
                    evos.remove(n - 1);
                    reemplazar(e, f.conEvoluciones(evos));
                }
                case 4 -> reemplazar(e, f.conEvoluciones(List.of()));
                default -> { }
            }
        }
    }

    // ---- movimientos

    private void editarPorNivel(DatosEspecie e, int numForma) {
        while (true) {
            e = actual(e);
            DatosForma f = e.forma(numForma);
            c.println("\nMovimientos por nivel:");
            if (f.porNivel().isEmpty()) c.println("  (ninguno)");
            for (int i = 0; i < f.porNivel().size(); i++) {
                Aprendizaje a = f.porNivel().get(i);
                c.println("  " + (i + 1) + ". Nivel " + a.nivel() + " · " + nombreMov(a.movimiento()));
            }
            int op = c.menu("Movimientos por nivel", "Añadir", "Quitar uno", "Quitar todos");
            List<Aprendizaje> l = new ArrayList<>(f.porNivel());
            switch (op) {
                case 0 -> { return; }
                case 1 -> {
                    DatosMovimiento m = c.buscar("Movimiento", bd.movimientos().values(), DatosMovimiento::simbolo, DatosMovimiento::nombre);
                    if (m == null) break;
                    l.add(new Aprendizaje(m.simbolo(), c.entero("Nivel en que lo aprende", 1, 0, 100)));
                    l.sort(Comparator.comparingInt(Aprendizaje::nivel));
                    reemplazar(e, f.conPorNivel(l));
                }
                case 2 -> {
                    if (l.isEmpty()) break;
                    l.remove(c.entero("Número a quitar", 1, 1, l.size()) - 1);
                    reemplazar(e, f.conPorNivel(l));
                }
                case 3 -> reemplazar(e, f.conPorNivel(List.of()));
                default -> { }
            }
        }
    }

    private void editarListasMov(DatosEspecie e, int numForma) {
        while (true) {
            e = actual(e);
            DatosForma f = e.forma(numForma);
            int op = c.menu("Otras listas de movimientos", "MT / MO (" + f.mt().size() + ")",
                    "Huevo (" + f.huevo().size() + ")", "Tutor (" + f.tutor().size() + ")");
            if (op == 0) return;
            List<String> lista = new ArrayList<>(op == 1 ? f.mt() : op == 2 ? f.huevo() : f.tutor());
            c.println("  Actual: " + lista.stream().map(this::nombreMov).toList());
            int a = c.menu("Lista de " + (op == 1 ? "MT" : op == 2 ? "huevo" : "tutor"), "Añadir movimiento", "Quitar movimiento");
            if (a == 1) {
                DatosMovimiento m = c.buscar("Movimiento", bd.movimientos().values(), DatosMovimiento::simbolo, DatosMovimiento::nombre);
                if (m != null && !lista.contains(m.simbolo())) lista.add(m.simbolo());
            } else if (a == 2 && !lista.isEmpty()) {
                for (int i = 0; i < lista.size(); i++) c.println("  " + (i + 1) + ". " + nombreMov(lista.get(i)));
                lista.remove(c.entero("Número a quitar", 1, 1, lista.size()) - 1);
            } else {
                continue;
            }
            reemplazar(e, op == 1 ? f.conListas(lista, f.huevo(), f.tutor())
                    : op == 2 ? f.conListas(f.mt(), lista, f.tutor()) : f.conListas(f.mt(), f.huevo(), lista));
        }
    }

    // ---- habilidades

    private void editarHabilidades(DatosEspecie e, int numForma) {
        while (true) {
            e = actual(e);
            DatosForma f = e.forma(numForma);
            c.println("\nHabilidades: " + f.habilidades() + (f.habilidadOculta() == null ? "" : " · oculta: " + f.habilidadOculta()));
            int op = c.menu("Habilidades", "Añadir habilidad", "Quitar habilidad", "Definir habilidad oculta", "Quitar habilidad oculta");
            List<String> hab = new ArrayList<>(f.habilidades());
            switch (op) {
                case 0 -> { return; }
                case 1 -> {
                    if (bd.habilidades().isEmpty()) { c.println("  No hay habilidades: créalas antes en el menú Habilidades."); break; }
                    DatosHabilidad h = c.buscar("Habilidad", bd.habilidades().values(), DatosHabilidad::simbolo, DatosHabilidad::nombre);
                    if (h != null && !hab.contains(h.simbolo())) { hab.add(h.simbolo()); reemplazar(e, f.conHabilidades(hab, f.habilidadOculta())); }
                }
                case 2 -> {
                    if (hab.isEmpty()) break;
                    for (int i = 0; i < hab.size(); i++) c.println("  " + (i + 1) + ". " + hab.get(i));
                    hab.remove(c.entero("Número a quitar", 1, 1, hab.size()) - 1);
                    reemplazar(e, f.conHabilidades(hab, f.habilidadOculta()));
                }
                case 3 -> {
                    DatosHabilidad h = c.buscar("Habilidad oculta", bd.habilidades().values(), DatosHabilidad::simbolo, DatosHabilidad::nombre);
                    if (h != null) reemplazar(e, f.conHabilidades(hab, h.simbolo()));
                }
                case 4 -> reemplazar(e, f.conHabilidades(hab, null));
                default -> { }
            }
        }
    }

    // ---- captura, experiencia, cría y EVs

    private void editarCria(DatosEspecie e, int numForma) {
        DatosForma f = e.forma(numForma);
        double altura = c.decimal("Altura (m)", f.altura(), 0, 100);
        double peso = c.decimal("Peso (kg)", f.peso(), 0, 10000);
        for (CurvaExperiencia cv : CurvaExperiencia.values())
            c.println("  " + cv + ": nivel 50 = " + cv.expParaNivel(50) + ", nivel 100 = " + cv.expParaNivel(100));
        CurvaExperiencia curva = c.elegirEnum("Curva de experiencia", CurvaExperiencia.values(), f.curva(), CurvaExperiencia::name);
        int expBase = c.entero("Experiencia base", f.expBase(), 0, 1000);
        int felicidad = c.entero("Felicidad base", f.felicidadBase(), 0, 255);
        int captura = c.entero("Ratio de captura", f.ratioCaptura(), 0, 255);
        double hembra = c.decimal("% de hembras (-1 = sin género)", f.ratioHembra(), -1, 100);
        List<String> grupos = c.listaTexto("Grupos de huevo", f.gruposHuevo(), ",");
        int pasos = c.entero("Pasos de eclosión", f.pasosEclosion(), 0, 100000);
        Map<Estadistica, Integer> evs = new EnumMap<>(Estadistica.class);
        c.println("EVs que da al ser derrotado:");
        for (Estadistica s : Estadistica.values()) evs.put(s, c.entero("  " + NOMBRE_STAT.get(s), f.ev(s), 0, 3));
        reemplazar(e, f.conFisicos(altura, peso).conCria(curva, expBase, felicidad, captura, hembra, grupos, pasos).conEvs(evs));
    }

    // ---- formas

    private int editarFormas(DatosEspecie e, int actualForma) {
        for (DatosForma f : e.formas()) c.println("  Forma " + f.forma() + ": " + tipos(f));
        int op = c.menu("Formas", "Cambiar de forma", "Añadir forma (copia de la actual)", "Quitar forma");
        List<DatosForma> formas = new ArrayList<>(e.formas());
        switch (op) {
            case 1 -> {
                List<Integer> nums = formas.stream().map(DatosForma::forma).toList();
                return c.elegir("Forma", nums, n -> "forma " + n, actualForma);
            }
            case 2 -> {
                int nueva = formas.stream().mapToInt(DatosForma::forma).max().orElse(0) + 1;
                formas.add(e.forma(actualForma).conNumero(nueva));
                bd.poner(e.conFormas(formas));
                marcar.run();
                c.println("  Forma " + nueva + " añadida (copia de la " + actualForma + ").");
                return nueva;
            }
            case 3 -> {
                if (formas.size() == 1) { c.println("  Una especie necesita al menos una forma."); return actualForma; }
                int n = c.entero("Forma a quitar", actualForma, 0, 999);
                if (formas.removeIf(f -> f.forma() == n)) {
                    bd.poner(e.conFormas(formas));
                    marcar.run();
                    return formas.get(0).forma();
                }
                c.println("  Esa forma no existe.");
            }
            default -> { }
        }
        return actualForma;
    }
}
