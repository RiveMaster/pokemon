package Proyecto.studio;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Una forma de una especie (la forma 0 es la normal). Equivale a un "form" de Pokémon Studio. */
public record DatosForma(
        int forma,
        double altura,                       // metros (0 = desconocida)
        double peso,                         // kg (0 = desconocido)
        TipoPokemon tipo1,
        TipoPokemon tipo2,                   // puede ser null
        Map<Estadistica, Integer> statsBase,
        Map<Estadistica, Integer> evs,       // EVs que da al ser derrotado
        List<DatosEvolucion> evoluciones,
        CurvaExperiencia curva,
        int expBase,
        int felicidadBase,
        int ratioCaptura,                    // 0..255
        double ratioHembra,                  // % de hembras; -1 = sin género
        List<String> gruposHuevo,
        int pasosEclosion,
        List<String> habilidades,            // db_symbol de habilidades normales
        String habilidadOculta,              // puede ser null
        List<Aprendizaje> porNivel,
        List<String> mt,                     // movimientos por MT/MO
        List<String> huevo,                  // movimientos huevo
        List<String> tutor) {

    public DatosForma {
        Objects.requireNonNull(tipo1, "tipo1");
        statsBase = copiaStats(statsBase);
        evs = copiaStats(evs);
        evoluciones = evoluciones == null ? List.of() : List.copyOf(evoluciones);
        curva = curva == null ? CurvaExperiencia.RAPIDA_MEDIA : curva;
        gruposHuevo = gruposHuevo == null ? List.of() : List.copyOf(gruposHuevo);
        habilidades = habilidades == null ? List.of() : List.copyOf(habilidades);
        porNivel = porNivel == null ? List.of() : List.copyOf(porNivel);
        mt = mt == null ? List.of() : List.copyOf(mt);
        huevo = huevo == null ? List.of() : List.copyOf(huevo);
        tutor = tutor == null ? List.of() : List.copyOf(tutor);
    }

    private static Map<Estadistica, Integer> copiaStats(Map<Estadistica, Integer> src) {
        EnumMap<Estadistica, Integer> m = new EnumMap<>(Estadistica.class);
        for (Estadistica e : Estadistica.values()) m.put(e, src == null ? 0 : src.getOrDefault(e, 0)); // siempre las 6
        return Collections.unmodifiableMap(m);
    }

    /** Forma 0 con valores neutros, lista para ir rellenándola con los métodos con*. */
    public static DatosForma porDefecto(TipoPokemon tipo1, TipoPokemon tipo2) {
        return new DatosForma(0, 0, 0, tipo1, tipo2, null, null, null, CurvaExperiencia.RAPIDA_MEDIA,
                64, 70, 45, 50.0, null, 5120, null, null, null, null, null, null);
    }

    public int stat(Estadistica e) { return statsBase.getOrDefault(e, 0); }

    public int ev(Estadistica e) { return evs.getOrDefault(e, 0); }

    public DatosForma conStats(Map<Estadistica, Integer> nuevas) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, nuevas, evs, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conEvoluciones(List<DatosEvolucion> nuevas) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, statsBase, evs, nuevas, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conPorNivel(List<Aprendizaje> nuevos) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, statsBase, evs, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, nuevos, mt, huevo, tutor);
    }

    public DatosForma conTipos(TipoPokemon t1, TipoPokemon t2) {
        return new DatosForma(forma, altura, peso, t1, t2, statsBase, evs, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conNumero(int n) {
        return new DatosForma(n, altura, peso, tipo1, tipo2, statsBase, evs, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conFisicos(double nuevaAltura, double nuevoPeso) {
        return new DatosForma(forma, nuevaAltura, nuevoPeso, tipo1, tipo2, statsBase, evs, evoluciones, curva,
                expBase, felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conCria(CurvaExperiencia nuevaCurva, int nuevaExpBase, int nuevaFelicidad, int nuevaCaptura,
                              double nuevoRatioHembra, List<String> nuevosGrupos, int nuevosPasos) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, statsBase, evs, evoluciones, nuevaCurva,
                nuevaExpBase, nuevaFelicidad, nuevaCaptura, nuevoRatioHembra, nuevosGrupos, nuevosPasos,
                habilidades, habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conEvs(Map<Estadistica, Integer> nuevos) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, statsBase, nuevos, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, mt, huevo, tutor);
    }

    public DatosForma conHabilidades(List<String> nuevas, String oculta) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, statsBase, evs, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, nuevas, oculta,
                porNivel, mt, huevo, tutor);
    }

    public DatosForma conListas(List<String> nuevasMt, List<String> nuevosHuevo, List<String> nuevosTutor) {
        return new DatosForma(forma, altura, peso, tipo1, tipo2, statsBase, evs, evoluciones, curva, expBase,
                felicidadBase, ratioCaptura, ratioHembra, gruposHuevo, pasosEclosion, habilidades,
                habilidadOculta, porNivel, nuevasMt, nuevosHuevo, nuevosTutor);
    }
}
