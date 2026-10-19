package Proyecto.studio;

import java.util.ArrayList;
import java.util.List;

/** Editores de zonas, gimnasios, jefes, generaciones y reglas globales. */
final class EditorMundo {
    private final Consola c;
    private final BaseDatos bd;
    private final Runnable marcar;

    EditorMundo(Consola c, BaseDatos bd, Runnable marcar) {
        this.c = c;
        this.bd = bd;
        this.marcar = marcar;
    }

    private String nombreZona(String sim) {
        if (sim == null) return "—";
        DatosZona z = bd.zona(sim);
        return z == null ? sim + " (¡no existe!)" : z.nombre();
    }

    // ----------------------------------------------------------------------- zonas

    void zonas() {
        new Crud<DatosZona>(c, "Zonas", () -> new ArrayList<>(bd.zonas().values()),
                DatosZona::simbolo,
                z -> String.format("%-10s %-16s %-9s %s", z.simbolo(), z.nombre(), z.clase().clave(),
                        z.clase() == ClaseZona.RUTA ? "Nv " + z.nivelMin() + "-" + z.nivelMax() : ""),
                z -> z.nombre() + " (" + z.simbolo() + ", " + z.clase().clave() + ")\n  " + z.descripcion()
                        + "\n  Camino: " + nombreZona(z.anterior()) + " ← aquí → " + nombreZona(z.siguiente())
                        + (z.clase() == ClaseZona.RUTA
                        ? "\n  Salvajes: " + z.tipos().stream().map(TipoPokemon::nombre).toList() + ", niveles " + z.nivelMin() + "-" + z.nivelMax()
                        + "\n  Pueblo cercano: " + nombreZona(z.puebloCercano()) + "\n  Entrenadores: " + z.clasesEntrenador()
                        : "\n  Tipos típicos: " + z.tipos().stream().map(TipoPokemon::nombre).toList()
                        + (z.casino() ? "\n  Tiene casino" : "") + (z.sedeGobierno() ? "\n  Sede del Gobierno" : "")),
                this::pedirZona, this::guardarZona, this::borrarZona, marcar).menu();
    }

    private DatosZona pedirZona(DatosZona a) {
        String nombre = c.textoObligatorio("Nombre", a == null ? null : a.nombre());
        String sim = a != null ? a.simbolo() : DatosJuego.siguienteRanuraLibre(bd);
        if (sim == null) {
            c.println("  No quedan ranuras libres para lugares nuevos (el juego admite " + DatosJuego.ranuras().size()
                    + "). Borra alguna zona nueva antes.");
            return null;
        }
        if (a == null) c.println("  (usará la ranura " + sim + "; quedan " + (DatosJuego.ranurasLibres(bd) - 1) + " libres)");
        ClaseZona clase = c.elegirEnum("Clase", ClaseZona.values(), a == null ? ClaseZona.RUTA : a.clase(), ClaseZona::name);
        String desc = c.texto("Descripción", a == null ? "" : a.descripcion());
        boolean ruta = clase == ClaseZona.RUTA;
        List<TipoPokemon> tipos = clase == ClaseZona.INICIO ? List.of()
                : c.listaTipos(ruta ? "Tipos de Pokémon salvajes" : "Tipos que se ven por las calles", a == null ? List.of() : a.tipos());
        int min = 0, max = 0;
        if (ruta) {
            min = c.entero("Nivel mínimo salvaje", a == null || a.nivelMin() == 0 ? 2 : a.nivelMin(), 1, 100);
            max = c.entero("Nivel máximo salvaje", a == null || a.nivelMax() < min ? min + 3 : a.nivelMax(), min, 100);
        }
        List<DatosZona> otras = new ArrayList<>(bd.zonas().values());
        otras.removeIf(z -> z.simbolo().equals(sim));
        String anterior = c.referencia("Zona anterior", a == null ? null : a.anterior(), otras, DatosZona::simbolo, DatosZona::nombre);
        String siguiente = c.referencia("Zona siguiente", a == null ? null : a.siguiente(), otras, DatosZona::simbolo, DatosZona::nombre);
        String pueblo = null;
        List<String> entrenadores = List.of();
        if (ruta) {
            List<DatosZona> pueblos = otras.stream().filter(z -> z.clase() != ClaseZona.RUTA).toList();
            pueblo = c.referencia("Pueblo cercano (donde despiertas)", a == null ? null : a.puebloCercano(), pueblos, DatosZona::simbolo, DatosZona::nombre);
            entrenadores = c.listaTexto("Clases de entrenador NPC", a == null ? List.of() : a.clasesEntrenador(), ",");
        }
        boolean casino = false, sede = false;
        if (clase == ClaseZona.POBLACION) {
            casino = c.confirmar("¿Tiene casino?", a != null && a.casino());
            sede = c.confirmar("¿Es sede del Gobierno?", a != null && a.sedeGobierno());
        }
        return new DatosZona(sim, nombre, clase, desc, tipos, min, max, anterior, siguiente, pueblo, entrenadores, casino, sede);
    }

    /** Guarda la zona y ofrece dejar el camino coherente en los dos sentidos. */
    private void guardarZona(DatosZona z) {
        bd.poner(z);
        DatosZona a = z.anterior() == null ? null : bd.zona(z.anterior());
        if (a != null && !z.simbolo().equals(a.siguiente())
                && c.confirmar("¿Hacer que " + a.nombre() + " tenga a " + z.nombre() + " como siguiente (ahora: " + nombreZona(a.siguiente()) + ")?", true))
            bd.poner(a.conSiguiente(z.simbolo()));
        DatosZona s = z.siguiente() == null ? null : bd.zona(z.siguiente());
        if (s != null && !z.simbolo().equals(s.anterior())
                && c.confirmar("¿Hacer que " + s.nombre() + " tenga a " + z.nombre() + " como anterior (ahora: " + nombreZona(s.anterior()) + ")?", true))
            bd.poner(s.conAnterior(z.simbolo()));
    }

    private void borrarZona(DatosZona z) {
        if (bd.gimnasios().values().stream().anyMatch(g -> g.ciudad().equals(z.simbolo()))) {
            c.println("  No se puede borrar: tiene un gimnasio. Muévelo o bórralo antes.");
            return;
        }
        List<String> refs = Referencias.deZona(bd, z.simbolo());
        refs.forEach(r -> c.println("   - " + r));
        if (!c.confirmar("¿Borrar " + z.nombre() + "?", false)) return;
        DatosZona ant = z.anterior() == null ? null : bd.zona(z.anterior());
        DatosZona sig = z.siguiente() == null ? null : bd.zona(z.siguiente());
        bd.quitarZona(z.simbolo());
        if (ant != null && z.simbolo().equals(ant.siguiente()))
            bd.poner(ant.conSiguiente(sig != null && c.confirmar("¿Unir " + ant.nombre() + " con " + sig.nombre() + "?", true) ? sig.simbolo() : null));
        DatosZona ant2 = z.anterior() == null ? null : bd.zona(z.anterior());
        if (sig != null && z.simbolo().equals(sig.anterior()))
            bd.poner(sig.conAnterior(ant2 != null && ant2.siguiente() != null && ant2.siguiente().equals(sig.simbolo()) ? ant2.simbolo() : null));
        for (DatosZona o : new ArrayList<>(bd.zonas().values()))
            if (z.simbolo().equals(o.puebloCercano())) bd.poner(o.conPuebloCercano(null));
        marcar.run();
        c.println("  Borrada. Pasa por 'Validar' para revisar el camino.");
    }

    // ------------------------------------------------------------------- gimnasios

    void gimnasios() {
        new Crud<DatosGimnasio>(c, "Gimnasios", () -> new ArrayList<>(bd.gimnasios().values()),
                DatosGimnasio::simbolo,
                g -> String.format("%-22s %-14s líder %s", g.nombre(), nombreZona(g.ciudad()), g.lider()),
                g -> g.nombre() + " en " + nombreZona(g.ciudad()) + "\n  Tipos: " + g.tiposEquipo().stream().map(TipoPokemon::nombre).toList()
                        + "\n  Líder: " + g.lider() + " (" + g.tituloLider() + "), equipo de " + g.tamanoEquipo() + ", nivel base " + g.nivelBase()
                        + "\n  Miembros: " + g.miembros() + "\n  Medalla: " + g.medalla() + ", " + g.recompensaDinero() + " $, MT: "
                        + (g.mtRecompensa() == null ? "—" : g.mtRecompensa()),
                this::pedirGimnasio, bd::poner,
                g -> {
                    if (c.confirmar("¿Borrar el gimnasio " + g.nombre() + "?", false)) {
                        bd.quitarGimnasio(g.simbolo());
                        marcar.run();
                        c.println("  Borrado.");
                    }
                }, marcar).menu();
    }

    private DatosGimnasio pedirGimnasio(DatosGimnasio a) {
        String nombre = c.textoObligatorio("Nombre del gimnasio", a == null ? null : a.nombre());
        String sim = a != null ? a.simbolo() : PuenteProyecto.unico(PuenteProyecto.simbolo(nombre), bd.gimnasios().keySet());
        List<DatosZona> libres = new ArrayList<>();
        for (DatosZona z : bd.zonas().values()) {
            boolean ocupada = bd.gimnasios().values().stream().anyMatch(g -> g.ciudad().equals(z.simbolo()) && (a == null || !g.simbolo().equals(a.simbolo())));
            if (z.clase() == ClaseZona.POBLACION && !ocupada) libres.add(z);
        }
        String ciudad;
        if (libres.isEmpty()) {
            c.println("  No hay poblaciones libres: crea antes una zona de clase POBLACION.");
            return null;
        }
        ciudad = c.referencia("Ciudad", a == null ? null : a.ciudad(), libres, DatosZona::simbolo, DatosZona::nombre);
        if (ciudad == null) { c.println("  Un gimnasio necesita ciudad."); return null; }
        List<TipoPokemon> tipos = c.listaTipos("Tipos del equipo", a == null ? List.of() : a.tiposEquipo());
        String lider = c.textoObligatorio("Nombre del líder", a == null ? null : a.lider());
        String titulo = c.texto("Título del líder", a == null ? "" : a.tituloLider());
        int n = bd.reglas().miembrosPorGimnasio();
        List<String> miembros = c.listaTexto("Los " + n + " miembros", a == null ? List.of() : a.miembros(), ",");
        String medalla = c.texto("Medalla", a == null ? "" : a.medalla());
        int dinero = c.entero("Recompensa en dinero", a == null ? 1000 : a.recompensaDinero(), 0, 1_000_000);
        String mt = c.referencia("MT de recompensa", a == null ? null : a.mtRecompensa(), bd.movimientos().values(), DatosMovimiento::simbolo, DatosMovimiento::nombre);
        int nivel = c.entero("Nivel base", a == null ? 15 : a.nivelBase(), 1, 100);
        int equipo = c.entero("Tamaño del equipo del líder", a == null ? 3 : a.tamanoEquipo(), 1, 6);
        if (miembros.size() != n) c.println("  Aviso: el juego espera " + n + " miembros; 'Validar' lo señalará.");
        return new DatosGimnasio(sim, nombre, ciudad, tipos, lider, titulo, miembros, medalla, dinero, mt, nivel, equipo);
    }

    // ------------------------------------------------------------------------ jefes

    void jefes() {
        new Crud<DatosJefe>(c, "Jefes del Gobierno", () -> new ArrayList<>(bd.jefes().values()),
                j -> Integer.toString(j.generacion()),
                j -> "Gen " + j.generacion() + ": " + j.nombre() + " — " + j.cargo(),
                j -> "Gen " + j.generacion() + ": " + j.nombre() + " — " + j.cargo() + "\n  Tipos: "
                        + j.tipos().stream().map(TipoPokemon::nombre).toList() + ", nivel base " + j.nivelBase()
                        + "\n  Al perder: " + j.fraseDerrota() + "\n  Frases: " + j.memes(),
                a -> {
                    int gen = a != null ? a.generacion()
                            : c.entero("Generación", bd.jefes().keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1, 1, 99);
                    if (a == null && bd.jefe(gen) != null) { c.println("  Ya hay jefe para esa generación."); return null; }
                    return new DatosJefe(gen, c.textoObligatorio("Nombre", a == null ? null : a.nombre()),
                            c.texto("Cargo", a == null ? "" : a.cargo()),
                            c.listaTipos("Tipos del equipo", a == null ? List.of() : a.tipos()),
                            c.entero("Nivel base", a == null ? 20 : a.nivelBase(), 1, 100),
                            c.texto("Frase al ser derrotado", a == null ? "" : a.fraseDerrota()),
                            c.listaTexto("Frases durante el combate", a == null ? List.of() : a.memes(), "|"));
                },
                bd::poner,
                j -> {
                    if (c.confirmar("¿Borrar al jefe de la generación " + j.generacion() + "?", false)) {
                        bd.quitarJefe(j.generacion());
                        marcar.run();
                        c.println("  Borrado.");
                    }
                }, marcar).menu();
    }

    // ----------------------------------------------------------------- generaciones

    void generaciones() {
        new Crud<DatosGeneracion>(c, "Generaciones", () -> new ArrayList<>(bd.generaciones().values()),
                g -> Integer.toString(g.numero()),
                g -> "Gen " + g.numero() + " · " + g.region() + " · Pokédex " + g.desde() + "-" + g.hasta() + " · nivel +" + g.bonusNivel(),
                g -> "Gen " + g.numero() + " · " + g.region() + "\n  Pokédex " + g.desde() + "-" + g.hasta() + ", bonus de nivel +" + g.bonusNivel() + "\n  " + g.ambiente(),
                a -> {
                    int num = a != null ? a.numero()
                            : c.entero("Número", bd.generaciones().keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1, 1, 99);
                    if (a == null && bd.generacion(num) != null) { c.println("  Esa generación ya existe."); return null; }
                    int desde = c.entero("Primer número de Pokédex", a == null ? 1 : a.desde(), 1, 99999);
                    int hasta = c.entero("Último número de Pokédex", a == null ? desde : a.hasta(), desde, 99999);
                    return new DatosGeneracion(num, c.textoObligatorio("Región", a == null ? null : a.region()), desde, hasta,
                            c.texto("Ambiente", a == null ? "" : a.ambiente()),
                            c.entero("Bonus de nivel", a == null ? (num - 1) * 3 : a.bonusNivel(), 0, 100));
                },
                bd::poner,
                g -> {
                    if (c.confirmar("¿Borrar la generación " + g.numero() + "?", false)) {
                        bd.quitarGeneracion(g.numero());
                        marcar.run();
                        c.println("  Borrada (su jefe quedará sin generación hasta que lo borres o corrijas).");
                    }
                }, marcar).menu();
    }

    // ----------------------------------------------------------------------- reglas

    void reglas() {
        while (true) {
            DatosReglas r = bd.reglas();
            c.println("\n  Probabilidad de NPC " + r.probabilidadNpc() + " · premio x" + r.premioNpcPorNivel()
                    + " · " + r.miembrosPorGimnasio() + " miembros x " + r.pokemonPorMiembro() + " Pokémon · jefe: equipo de "
                    + r.tamanoEquipoJefe() + ", " + r.recompensaJefe() + " $ · excluidos: " + r.excluidosSalvajes().size()
                    + " · nombres NPC: " + r.nombresNpc().size() + ", frases: " + r.frasesReto().size() + "/" + r.frasesDerrota().size());
            int op = c.menu("══ REGLAS GLOBALES ══", "Números (probabilidad, premios, tamaños)", "Nombres y frases de NPC",
                    "Pokémon excluidos de salvajes", "Reglas de combate (opcionales)");
            switch (op) {
                case 0 -> { return; }
                case 1 -> {
                    bd.reglas(new DatosReglas(c.decimal("Probabilidad de NPC (0..1)", r.probabilidadNpc(), 0, 1),
                            r.nombresNpc(), r.frasesReto(), r.frasesDerrota(),
                            c.entero("Premio por nivel de equipo del NPC", r.premioNpcPorNivel(), 0, 1000),
                            c.entero("Miembros por gimnasio", r.miembrosPorGimnasio(), 1, 10),
                            c.entero("Pokémon por miembro", r.pokemonPorMiembro(), 1, 6),
                            c.entero("Tamaño del equipo de jefe", r.tamanoEquipoJefe(), 1, 6),
                            c.entero("Recompensa de jefe", r.recompensaJefe(), 0, 1_000_000), r.excluidosSalvajes(),
                            r.combate()));
                    marcar.run();
                }
                case 2 -> {
                    bd.reglas(new DatosReglas(r.probabilidadNpc(), c.listaTexto("Nombres de NPC", r.nombresNpc(), ","),
                            c.listaTexto("Frases de reto", r.frasesReto(), "|"),
                            c.listaTexto("Frases de derrota", r.frasesDerrota(), "|"),
                            r.premioNpcPorNivel(), r.miembrosPorGimnasio(), r.pokemonPorMiembro(),
                            r.tamanoEquipoJefe(), r.recompensaJefe(), r.excluidosSalvajes(), r.combate()));
                    marcar.run();
                }
                case 3 -> excluidos();
                case 4 -> reglasCombate();
                default -> { }
            }
        }
    }

    private void reglasCombate() {
        DatosReglas r = bd.reglas();
        DatosCombate k = r.combate();
        c.println("\n  Todas desactivadas = el combate original del juego. Cada regla es independiente.");
        DatosCombate nuevo = new DatosCombate(
                c.confirmar("¿Aplicar la tabla de tipos (muy eficaz x2, poco eficaz x0.5, inmune x0)?", k.eficaciaTipos()),
                c.confirmar("¿Los movimientos aplican sus estados (dormido, quemado, paralizado...)?", k.efectosMovimientos()),
                c.confirmar("¿Los movimientos pueden fallar según su precisión?", k.precision()),
                c.confirmar("¿Los movimientos gastan PP (y se usa Forcejeo al agotarlos)?", k.pp()),
                c.confirmar("¿El daño depende de la categoría (físico/especial) y de ataque/defensa?", k.estadisticas()),
                c.confirmar("¿Las habilidades tienen efecto en combate?", k.habilidades()));
        bd.reglas(r.conCombate(nuevo));
        marcar.run();
    }

    private void excluidos() {
        while (true) {
            List<String> lista = new ArrayList<>(bd.reglas().excluidosSalvajes());
            int op = c.menu("Excluidos de salvajes (" + lista.size() + ")", "Ver la lista", "Añadir", "Quitar");
            switch (op) {
                case 0 -> { return; }
                case 1 -> c.println("  " + String.join(", ", lista));
                case 2 -> {
                    DatosEspecie e = c.buscar("Pokémon a excluir", bd.especies().values(), DatosEspecie::simbolo, DatosEspecie::nombre);
                    if (e != null && !lista.contains(e.simbolo())) {
                        lista.add(e.simbolo());
                        bd.reglas(Referencias.conExcluidos(bd.reglas(), lista));
                        marcar.run();
                    }
                }
                case 3 -> {
                    String q = c.leer("Texto del Pokémon a quitar de la lista: ");
                    String s = c.resolver(q, lista, x -> x, x -> x);
                    if (s != null) {
                        lista.remove(s);
                        bd.reglas(Referencias.conExcluidos(bd.reglas(), lista));
                        marcar.run();
                    }
                }
                default -> { }
            }
        }
    }
}
