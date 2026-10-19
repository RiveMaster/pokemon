package Proyecto.studio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Qué usa a qué, y borrados que limpian las referencias para no dejar la base rota. */
public final class Referencias {
    private Referencias() {}

    // ----------------------------------------------------------------- consultas

    public static List<String> deEspecie(BaseDatos bd, String sim) {
        List<String> r = new ArrayList<>();
        for (DatosEspecie e : bd.especies().values())
            for (DatosForma f : e.formas())
                for (DatosEvolucion ev : f.evoluciones())
                    if (ev.destino().equals(sim)) r.add(e.nombre() + " evoluciona a ella");
        if (bd.reglas().excluidosSalvajes().contains(sim)) r.add("Está en la lista de excluidos de salvajes");
        return r;
    }

    public static List<String> deMovimiento(BaseDatos bd, String sim) {
        List<String> r = new ArrayList<>();
        for (DatosEspecie e : bd.especies().values()) {
            boolean usa = false;
            for (DatosForma f : e.formas()) {
                usa |= f.porNivel().stream().anyMatch(a -> a.movimiento().equals(sim));
                usa |= f.mt().contains(sim) || f.huevo().contains(sim) || f.tutor().contains(sim);
            }
            if (usa) r.add(e.nombre() + " lo aprende");
        }
        for (DatosGimnasio g : bd.gimnasios().values())
            if (sim.equals(g.mtRecompensa())) r.add(g.nombre() + " lo da como MT");
        return r;
    }

    public static List<String> deHabilidad(BaseDatos bd, String sim) {
        List<String> r = new ArrayList<>();
        for (DatosEspecie e : bd.especies().values())
            for (DatosForma f : e.formas())
                if (f.habilidades().contains(sim) || sim.equals(f.habilidadOculta())) { r.add(e.nombre() + " la tiene"); break; }
        return r;
    }

    public static List<String> deObjeto(BaseDatos bd, String sim) {
        List<String> r = new ArrayList<>();
        for (DatosEspecie e : bd.especies().values())
            for (DatosForma f : e.formas())
                for (DatosEvolucion ev : f.evoluciones())
                    if (sim.equals(ev.condiciones().get(DatosEvolucion.OBJETO))) r.add(e.nombre() + " lo necesita para evolucionar");
        return r;
    }

    public static List<String> deZona(BaseDatos bd, String sim) {
        List<String> r = new ArrayList<>();
        for (DatosZona z : bd.zonas().values()) {
            if (sim.equals(z.anterior())) r.add(z.nombre() + " la tiene como zona anterior");
            if (sim.equals(z.siguiente())) r.add(z.nombre() + " la tiene como zona siguiente");
            if (sim.equals(z.puebloCercano())) r.add(z.nombre() + " la tiene como pueblo cercano");
        }
        for (DatosGimnasio g : bd.gimnasios().values())
            if (sim.equals(g.ciudad())) r.add("Aquí está el gimnasio " + g.nombre());
        return r;
    }

    // ---------------------------------------------------- borrados con limpieza

    public static void borrarEspecie(BaseDatos bd, String sim) {
        bd.quitarEspecie(sim);
        for (DatosEspecie e : new ArrayList<>(bd.especies().values())) {
            List<DatosForma> formas = new ArrayList<>();
            for (DatosForma f : e.formas()) {
                List<DatosEvolucion> evos = new ArrayList<>(f.evoluciones());
                evos.removeIf(ev -> ev.destino().equals(sim));
                formas.add(f.conEvoluciones(evos));
            }
            bd.poner(e.conFormas(formas));
        }
        List<String> excl = new ArrayList<>(bd.reglas().excluidosSalvajes());
        if (excl.remove(sim)) bd.reglas(conExcluidos(bd.reglas(), excl));
    }

    public static DatosReglas conExcluidos(DatosReglas r, List<String> excl) {
        return new DatosReglas(r.probabilidadNpc(), r.nombresNpc(), r.frasesReto(), r.frasesDerrota(),
                r.premioNpcPorNivel(), r.miembrosPorGimnasio(), r.pokemonPorMiembro(), r.tamanoEquipoJefe(),
                r.recompensaJefe(), excl, r.combate());
    }

    public static void borrarMovimiento(BaseDatos bd, String sim) {
        bd.quitarMovimiento(sim);
        for (DatosEspecie e : new ArrayList<>(bd.especies().values())) {
            List<DatosForma> formas = new ArrayList<>();
            for (DatosForma f : e.formas()) {
                List<Aprendizaje> pn = new ArrayList<>(f.porNivel());
                pn.removeIf(a -> a.movimiento().equals(sim));
                List<String> mt = new ArrayList<>(f.mt()), hu = new ArrayList<>(f.huevo()), tu = new ArrayList<>(f.tutor());
                mt.remove(sim); hu.remove(sim); tu.remove(sim);
                formas.add(f.conPorNivel(pn).conListas(mt, hu, tu));
            }
            bd.poner(e.conFormas(formas));
        }
        for (DatosGimnasio g : new ArrayList<>(bd.gimnasios().values())) {
            if (sim.equals(g.mtRecompensa()))
                bd.poner(new DatosGimnasio(g.simbolo(), g.nombre(), g.ciudad(), g.tiposEquipo(), g.lider(),
                        g.tituloLider(), g.miembros(), g.medalla(), g.recompensaDinero(), null, g.nivelBase(),
                        g.tamanoEquipo()));
        }
    }

    public static void borrarHabilidad(BaseDatos bd, String sim) {
        bd.quitarHabilidad(sim);
        for (DatosEspecie e : new ArrayList<>(bd.especies().values())) {
            List<DatosForma> formas = new ArrayList<>();
            for (DatosForma f : e.formas()) {
                List<String> hab = new ArrayList<>(f.habilidades());
                hab.remove(sim);
                formas.add(f.conHabilidades(hab, sim.equals(f.habilidadOculta()) ? null : f.habilidadOculta()));
            }
            bd.poner(e.conFormas(formas));
        }
    }

    public static void borrarObjeto(BaseDatos bd, String sim) {
        bd.quitarObjeto(sim);
        for (DatosEspecie e : new ArrayList<>(bd.especies().values())) {
            List<DatosForma> formas = new ArrayList<>();
            for (DatosForma f : e.formas()) {
                List<DatosEvolucion> evos = new ArrayList<>(f.evoluciones());
                evos.removeIf(ev -> sim.equals(ev.condiciones().get(DatosEvolucion.OBJETO)));
                formas.add(f.conEvoluciones(evos));
            }
            bd.poner(e.conFormas(formas));
        }
    }

    /** Devuelve el siguiente id libre (máximo + 1). */
    public static int siguienteId(Map<String, ? extends Object> datos, java.util.function.ToIntFunction<Object> id) {
        int max = 0;
        for (Object o : datos.values()) max = Math.max(max, id.applyAsInt(o));
        return max + 1;
    }
}
