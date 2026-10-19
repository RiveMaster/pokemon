package Proyecto.studio.gui;

import Proyecto.studio.ClaseZona;
import Proyecto.studio.DatosEspecie;
import Proyecto.studio.DatosHabilidad;
import Proyecto.studio.DatosMovimiento;
import Proyecto.studio.DatosObjeto;
import Proyecto.studio.DatosZona;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/** Listas de opciones (símbolo + nombre) para desplegables y selectores, siempre leídas de la base actual. */
final class Opciones {
    private Opciones() {}

    static List<Opcion> movimientos(Contexto c) {
        List<Opcion> l = new ArrayList<>();
        for (DatosMovimiento m : c.bd().movimientos().values()) l.add(new Opcion(m.simbolo(), m.nombre()));
        return l;
    }

    static List<Opcion> especies(Contexto c) {
        List<Opcion> l = new ArrayList<>();
        for (DatosEspecie e : c.bd().especies().values()) l.add(new Opcion(e.simbolo(), String.format("#%d %s", e.id(), e.nombre())));
        return l;
    }

    static List<Opcion> habilidades(Contexto c) {
        List<Opcion> l = new ArrayList<>();
        for (DatosHabilidad h : c.bd().habilidades().values()) l.add(new Opcion(h.simbolo(), h.nombre()));
        return l;
    }

    static List<Opcion> objetos(Contexto c) {
        List<Opcion> l = new ArrayList<>();
        for (DatosObjeto o : c.bd().objetos().values()) l.add(new Opcion(o.simbolo(), o.nombre()));
        return l;
    }

    static List<Opcion> zonas(Contexto c, Predicate<DatosZona> filtro) {
        List<Opcion> l = new ArrayList<>();
        for (DatosZona z : c.bd().zonas().values()) if (filtro.test(z)) l.add(new Opcion(z.simbolo(), z.nombre()));
        return l;
    }

    static List<Opcion> poblaciones(Contexto c) { return zonas(c, z -> z.clase() == ClaseZona.POBLACION); }

    // ---- etiquetas de un símbolo (para listas de símbolos)

    static String movimiento(Contexto c, String sim) {
        DatosMovimiento m = c.bd().movimiento(sim);
        return m == null ? sim + "  (¡no existe!)" : m.nombre();
    }

    static String especie(Contexto c, String sim) {
        DatosEspecie e = c.bd().especie(sim);
        return e == null ? sim + "  (¡no existe!)" : e.nombre();
    }

    static String habilidad(Contexto c, String sim) {
        DatosHabilidad h = c.bd().habilidad(sim);
        return h == null ? sim + "  (¡no existe!)" : h.nombre();
    }
}
