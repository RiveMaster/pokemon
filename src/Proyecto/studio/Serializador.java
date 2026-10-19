package Proyecto.studio;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Conversión entre los records del modelo y estructuras JSON (Map/List). */
final class Serializador {
    private Serializador() {}

    // ------------------------------------------------------------------ especie

    static Map<String, Object> aMapa(DatosEspecie e) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", e.id());
        m.put("simbolo", e.simbolo());
        m.put("nombre", e.nombre());
        List<Object> formas = new ArrayList<>();
        for (DatosForma f : e.formas()) formas.add(aMapa(f));
        m.put("formas", formas);
        return m;
    }

    static DatosEspecie especieDesde(Map<String, Object> m) {
        List<DatosForma> formas = new ArrayList<>();
        for (Object o : Json.lista(m.get("formas"))) formas.add(formaDesde(Json.mapa(o)));
        return new DatosEspecie(Json.entero(m, "id", 0), Json.texto(m, "simbolo", null),
                Json.texto(m, "nombre", null), formas);
    }

    private static Map<String, Object> aMapa(DatosForma f) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("forma", f.forma());
        m.put("altura", f.altura());
        m.put("peso", f.peso());
        m.put("tipo1", f.tipo1().clave());
        m.put("tipo2", f.tipo2() == null ? null : f.tipo2().clave());
        m.put("stats_base", stats(f.statsBase()));
        m.put("evs", stats(f.evs()));
        List<Object> evos = new ArrayList<>();
        for (DatosEvolucion ev : f.evoluciones()) {
            Map<String, Object> em = new LinkedHashMap<>();
            em.put("destino", ev.destino());
            em.put("condiciones", new LinkedHashMap<String, Object>(ev.condiciones()));
            evos.add(em);
        }
        m.put("evoluciones", evos);
        m.put("curva_exp", f.curva().id());
        m.put("exp_base", f.expBase());
        m.put("felicidad_base", f.felicidadBase());
        m.put("ratio_captura", f.ratioCaptura());
        m.put("ratio_hembra", f.ratioHembra());
        m.put("grupos_huevo", f.gruposHuevo());
        m.put("pasos_eclosion", f.pasosEclosion());
        m.put("habilidades", f.habilidades());
        m.put("habilidad_oculta", f.habilidadOculta());
        List<Object> porNivel = new ArrayList<>();
        for (Aprendizaje a : f.porNivel()) {
            Map<String, Object> am = new LinkedHashMap<>();
            am.put("movimiento", a.movimiento());
            am.put("nivel", a.nivel());
            porNivel.add(am);
        }
        m.put("mov_por_nivel", porNivel);
        m.put("mov_mt", f.mt());
        m.put("mov_huevo", f.huevo());
        m.put("mov_tutor", f.tutor());
        return m;
    }

    private static DatosForma formaDesde(Map<String, Object> m) {
        List<DatosEvolucion> evos = new ArrayList<>();
        for (Object o : Json.lista(m.get("evoluciones"))) {
            Map<String, Object> em = Json.mapa(o);
            Map<String, String> cond = new LinkedHashMap<>();
            Object c = em.get("condiciones");
            if (c != null) for (Map.Entry<String, Object> x : Json.mapa(c).entrySet()) cond.put(x.getKey(), String.valueOf(x.getValue()));
            evos.add(new DatosEvolucion(Json.texto(em, "destino", null), cond));
        }
        List<Aprendizaje> porNivel = new ArrayList<>();
        for (Object o : Json.lista(m.get("mov_por_nivel"))) {
            Map<String, Object> am = Json.mapa(o);
            porNivel.add(new Aprendizaje(Json.texto(am, "movimiento", null), Json.entero(am, "nivel", 0)));
        }
        String t2 = Json.texto(m, "tipo2", null);
        return new DatosForma(
                Json.entero(m, "forma", 0),
                Json.decimal(m, "altura", 0), Json.decimal(m, "peso", 0),
                tipo(Json.texto(m, "tipo1", null)), t2 == null ? null : tipo(t2),
                statsDesde(m.get("stats_base")), statsDesde(m.get("evs")),
                evos,
                CurvaExperiencia.desdeId(Json.entero(m, "curva_exp", 0)),
                Json.entero(m, "exp_base", 0), Json.entero(m, "felicidad_base", 70),
                Json.entero(m, "ratio_captura", 45), Json.decimal(m, "ratio_hembra", -1),
                Json.textos(m.get("grupos_huevo")), Json.entero(m, "pasos_eclosion", 0),
                Json.textos(m.get("habilidades")), Json.texto(m, "habilidad_oculta", null),
                porNivel, Json.textos(m.get("mov_mt")), Json.textos(m.get("mov_huevo")),
                Json.textos(m.get("mov_tutor")));
    }

    private static Map<String, Object> stats(Map<Estadistica, Integer> s) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (Estadistica e : Estadistica.values()) m.put(e.clave(), s.getOrDefault(e, 0));
        return m;
    }

    private static Map<Estadistica, Integer> statsDesde(Object o) {
        Map<Estadistica, Integer> r = new EnumMap<>(Estadistica.class);
        if (o != null) {
            for (Map.Entry<String, Object> e : Json.mapa(o).entrySet())
                r.put(Estadistica.desdeClave(e.getKey()), ((Number) e.getValue()).intValue());
        }
        return r;
    }

    private static TipoPokemon tipo(String texto) {
        return TipoPokemon.buscar(texto)
                .orElseThrow(() -> new IllegalArgumentException("Tipo desconocido: " + texto));
    }

    // --------------------------------------------------------------- movimiento

    static Map<String, Object> aMapa(DatosMovimiento mv) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", mv.id());
        m.put("simbolo", mv.simbolo());
        m.put("nombre", mv.nombre());
        m.put("tipo", mv.tipo().clave());
        m.put("categoria", mv.categoria().clave());
        m.put("potencia", mv.potencia());
        m.put("precision", mv.precision());
        m.put("pp", mv.pp());
        m.put("prioridad", mv.prioridad());
        if (mv.efecto() == null) {
            m.put("efecto", null);
        } else {
            Map<String, Object> e = new LinkedHashMap<>();
            e.put("nombre", mv.efecto().nombre());
            e.put("dano", mv.efecto().dano());
            e.put("estado", mv.efecto().estado());
            e.put("probabilidad", mv.efecto().probabilidad());
            m.put("efecto", e);
        }
        m.put("banderas", mv.banderas());
        return m;
    }

    static DatosMovimiento movimientoDesde(Map<String, Object> m) {
        DatosEfecto ef = null;
        if (m.get("efecto") != null) {
            Map<String, Object> e = Json.mapa(m.get("efecto"));
            ef = new DatosEfecto(Json.texto(e, "nombre", "Ninguno"), Json.decimal(e, "dano", 0),
                    Json.texto(e, "estado", null), Json.entero(e, "probabilidad", 0));
        }
        return new DatosMovimiento(Json.entero(m, "id", 0), Json.texto(m, "simbolo", null),
                Json.texto(m, "nombre", null), tipo(Json.texto(m, "tipo", null)),
                CategoriaMovimiento.desdeClave(Json.texto(m, "categoria", "status")),
                Json.decimal(m, "potencia", 0), Json.entero(m, "precision", 100),
                Json.entero(m, "pp", 0), Json.entero(m, "prioridad", 0), ef, Json.textos(m.get("banderas")));
    }

    // ---------------------------------------------------- habilidad y objeto

    static Map<String, Object> aMapa(DatosHabilidad h) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("simbolo", h.simbolo());
        m.put("nombre", h.nombre());
        m.put("descripcion", h.descripcion());
        m.put("efecto", h.efecto());
        m.put("tipo", h.tipo() == null ? null : h.tipo().clave());
        m.put("estado", h.estado());
        m.put("valor", h.valor());
        return m;
    }

    static DatosHabilidad habilidadDesde(Map<String, Object> m) {
        String t = Json.texto(m, "tipo", null);
        return new DatosHabilidad(Json.texto(m, "simbolo", null), Json.texto(m, "nombre", null),
                Json.texto(m, "descripcion", ""), Json.texto(m, "efecto", "ninguno"), t == null ? null : tipo(t),
                Json.texto(m, "estado", null), Json.decimal(m, "valor", 0));
    }

    static Map<String, Object> aMapa(DatosObjeto o) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", o.id());
        m.put("simbolo", o.simbolo());
        m.put("nombre", o.nombre());
        m.put("bolsillo", o.bolsillo());
        m.put("precio", o.precio());
        m.put("descripcion", o.descripcion());
        m.put("banderas", o.banderas());
        return m;
    }

    static DatosObjeto objetoDesde(Map<String, Object> m) {
        return new DatosObjeto(Json.entero(m, "id", 0), Json.texto(m, "simbolo", null),
                Json.texto(m, "nombre", null), Json.texto(m, "bolsillo", "otros"),
                Json.entero(m, "precio", 0), Json.texto(m, "descripcion", ""), Json.textos(m.get("banderas")));
    }

    // ------------------------------------------------------------------- mundo

    private static List<String> clavesTipos(List<TipoPokemon> l) {
        List<String> r = new ArrayList<>();
        for (TipoPokemon t : l) r.add(t.clave());
        return r;
    }

    private static List<TipoPokemon> tiposDesde(Object o) {
        List<TipoPokemon> r = new ArrayList<>();
        for (String s : Json.textos(o)) r.add(tipo(s));
        return r;
    }

    static Map<String, Object> aMapa(DatosZona z) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("simbolo", z.simbolo());
        m.put("nombre", z.nombre());
        m.put("clase", z.clase().clave());
        m.put("descripcion", z.descripcion());
        m.put("tipos", clavesTipos(z.tipos()));
        m.put("nivel_min", z.nivelMin());
        m.put("nivel_max", z.nivelMax());
        m.put("anterior", z.anterior());
        m.put("siguiente", z.siguiente());
        m.put("pueblo_cercano", z.puebloCercano());
        m.put("clases_entrenador", z.clasesEntrenador());
        m.put("casino", z.casino());
        m.put("sede_gobierno", z.sedeGobierno());
        return m;
    }

    static DatosZona zonaDesde(Map<String, Object> m) {
        return new DatosZona(Json.texto(m, "simbolo", null), Json.texto(m, "nombre", null),
                ClaseZona.desdeClave(Json.texto(m, "clase", "route")), Json.texto(m, "descripcion", ""),
                tiposDesde(m.get("tipos")), Json.entero(m, "nivel_min", 0), Json.entero(m, "nivel_max", 0),
                Json.texto(m, "anterior", null), Json.texto(m, "siguiente", null),
                Json.texto(m, "pueblo_cercano", null), Json.textos(m.get("clases_entrenador")),
                Boolean.TRUE.equals(m.get("casino")), Boolean.TRUE.equals(m.get("sede_gobierno")));
    }

    static Map<String, Object> aMapa(DatosGimnasio g) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("simbolo", g.simbolo());
        m.put("nombre", g.nombre());
        m.put("ciudad", g.ciudad());
        m.put("tipos_equipo", clavesTipos(g.tiposEquipo()));
        m.put("lider", g.lider());
        m.put("titulo_lider", g.tituloLider());
        m.put("miembros", g.miembros());
        m.put("medalla", g.medalla());
        m.put("recompensa_dinero", g.recompensaDinero());
        m.put("mt_recompensa", g.mtRecompensa());
        m.put("nivel_base", g.nivelBase());
        m.put("tamano_equipo", g.tamanoEquipo());
        return m;
    }

    static DatosGimnasio gimnasioDesde(Map<String, Object> m) {
        return new DatosGimnasio(Json.texto(m, "simbolo", null), Json.texto(m, "nombre", null),
                Json.texto(m, "ciudad", null), tiposDesde(m.get("tipos_equipo")),
                Json.texto(m, "lider", null), Json.texto(m, "titulo_lider", ""), Json.textos(m.get("miembros")),
                Json.texto(m, "medalla", ""), Json.entero(m, "recompensa_dinero", 0),
                Json.texto(m, "mt_recompensa", null), Json.entero(m, "nivel_base", 1),
                Json.entero(m, "tamano_equipo", 1));
    }

    static Map<String, Object> aMapa(DatosJefe j) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("generacion", j.generacion());
        m.put("nombre", j.nombre());
        m.put("cargo", j.cargo());
        m.put("tipos", clavesTipos(j.tipos()));
        m.put("nivel_base", j.nivelBase());
        m.put("frase_derrota", j.fraseDerrota());
        m.put("memes", j.memes());
        return m;
    }

    static DatosJefe jefeDesde(Map<String, Object> m) {
        return new DatosJefe(Json.entero(m, "generacion", 0), Json.texto(m, "nombre", null),
                Json.texto(m, "cargo", ""), tiposDesde(m.get("tipos")), Json.entero(m, "nivel_base", 1),
                Json.texto(m, "frase_derrota", ""), Json.textos(m.get("memes")));
    }

    static Map<String, Object> aMapa(DatosGeneracion g) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("numero", g.numero());
        m.put("region", g.region());
        m.put("desde", g.desde());
        m.put("hasta", g.hasta());
        m.put("ambiente", g.ambiente());
        m.put("bonus_nivel", g.bonusNivel());
        return m;
    }

    static DatosGeneracion generacionDesde(Map<String, Object> m) {
        return new DatosGeneracion(Json.entero(m, "numero", 0), Json.texto(m, "region", null),
                Json.entero(m, "desde", 1), Json.entero(m, "hasta", 1), Json.texto(m, "ambiente", ""),
                Json.entero(m, "bonus_nivel", 0));
    }

    static Map<String, Object> aMapa(DatosReglas r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("probabilidad_npc", r.probabilidadNpc());
        m.put("nombres_npc", r.nombresNpc());
        m.put("frases_reto", r.frasesReto());
        m.put("frases_derrota", r.frasesDerrota());
        m.put("premio_npc_por_nivel", r.premioNpcPorNivel());
        m.put("miembros_por_gimnasio", r.miembrosPorGimnasio());
        m.put("pokemon_por_miembro", r.pokemonPorMiembro());
        m.put("tamano_equipo_jefe", r.tamanoEquipoJefe());
        m.put("recompensa_jefe", r.recompensaJefe());
        m.put("excluidos_salvajes", r.excluidosSalvajes());
        m.put("eficacia_tipos", r.combate().eficaciaTipos());
        m.put("efectos_movimientos", r.combate().efectosMovimientos());
        m.put("precision", r.combate().precision());
        m.put("pp", r.combate().pp());
        m.put("estadisticas_en_dano", r.combate().estadisticas());
        m.put("habilidades_en_combate", r.combate().habilidades());
        return m;
    }

    static DatosReglas reglasDesde(Map<String, Object> m) {
        return new DatosReglas(Json.decimal(m, "probabilidad_npc", 0.30), Json.textos(m.get("nombres_npc")),
                Json.textos(m.get("frases_reto")), Json.textos(m.get("frases_derrota")),
                Json.entero(m, "premio_npc_por_nivel", 6), Json.entero(m, "miembros_por_gimnasio", 3),
                Json.entero(m, "pokemon_por_miembro", 2), Json.entero(m, "tamano_equipo_jefe", 5),
                Json.entero(m, "recompensa_jefe", 3000), Json.textos(m.get("excluidos_salvajes")),
                new DatosCombate(Boolean.TRUE.equals(m.get("eficacia_tipos")), Boolean.TRUE.equals(m.get("efectos_movimientos")),
                        Boolean.TRUE.equals(m.get("precision")), Boolean.TRUE.equals(m.get("pp")),
                        Boolean.TRUE.equals(m.get("estadisticas_en_dano")), Boolean.TRUE.equals(m.get("habilidades_en_combate"))));
    }
}
