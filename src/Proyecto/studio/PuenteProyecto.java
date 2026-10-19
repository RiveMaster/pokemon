package Proyecto.studio;

import Proyecto.Ataques;
import Proyecto.Efectos;
import Proyecto.EstadoAlterado;
import Proyecto.Generacion;
import Proyecto.Habilidades;
import Proyecto.Gimnasio;
import Proyecto.Gobierno;
import Proyecto.Mapa;
import Proyecto.Movimiento;
import Proyecto.Npc;
import Proyecto.ObjetoEquipable;
import Proyecto.Pokedex;
import Proyecto.PokemonBase;
import Proyecto.Pueblo;
import Proyecto.ReglasCombate;
import Proyecto.Tipos;
import Proyecto.Ubicacion;
import Proyecto.Zona;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Une el modelo de Studio con las clases actuales del juego (PokemonBase, Movimiento, Efectos).
 * <ul>
 *   <li>{@link #importar()}: vuelca Pokedex + Ataques a una {@link BaseDatos}.</li>
 *   <li>{@link #aPokemonBase(BaseDatos)}: genera de nuevo los PokemonBase desde la base de datos.</li>
 * </ul>
 * Los datos que el juego original no tenía (stats especiales, categoría, PP, precisión, altura...)
 * se rellenan con valores neutros; el resultado de {@link #importar()} los lista en {@code avisos}.
 */
public final class PuenteProyecto {
    private PuenteProyecto() {}

    public record Importacion(BaseDatos base, List<String> avisos) {}

    /** "Nidoran♀" -> "nidoran_f", "Puño Fuego" -> "puno_fuego". */
    public static String simbolo(String nombre) {
        String s = nombre.replace("♀", "_f").replace("♂", "_m");
        s = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        s = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
        return s.isEmpty() ? "sin_nombre" : s;
    }

    public static String unico(String base, Set<String> usados) {
        String s = base;
        for (int n = 2; usados.contains(s); n++) s = base + "_" + n;
        return s;
    }

    // ----------------------------------------------------------------- importar

    public static Importacion importar() {
        BaseDatos bd = new BaseDatos();
        List<String> avisos = new ArrayList<>();
        Map<String, String> simboloPorNombre = new HashMap<>(); // nombre en minúsculas -> db_symbol

        int id = 1;
        for (Movimiento m : Ataques.getTodos()) {
            String sim = unico(simbolo(m.getNombre()), bd.movimientos().keySet());
            if (!sim.equals(simbolo(m.getNombre())))
                avisos.add("Movimiento duplicado '" + m.getNombre() + "': renombrado a " + sim);
            simboloPorNombre.putIfAbsent(m.getNombre().toLowerCase(Locale.ROOT), sim);

            TipoPokemon tipo = TipoPokemon.buscar(m.getTipo()).orElse(null);
            if (tipo == null) {
                avisos.add("Movimiento '" + m.getNombre() + "': tipo desconocido '" + m.getTipo() + "' -> Normal");
                tipo = TipoPokemon.NORMAL;
            } else if (!tipo.nombreProyecto().equals(m.getTipo())) {
                avisos.add("Movimiento '" + m.getNombre() + "': tipo '" + m.getTipo() + "' normalizado a " + tipo.nombreProyecto());
            }

            Efectos ef = m.getEfecto();
            DatosEfecto de = (ef == null || (ef.getEstado() == null && ef.getDano() == 0)) ? null
                    : new DatosEfecto(ef.getNombre(), ef.getDano(),
                    ef.getEstado() == null ? null : ef.getEstado().name(), ef.getProbabilidad());

            bd.poner(new DatosMovimiento(id++, sim, m.getNombre(), tipo, CategoriaMovimiento.desdeClave(m.getCategoria()),
                    m.getPotencia(), m.getPrecision(), m.getPp(), m.getPrioridad(), de, List.of()));
        }

        Map<String, String> simboloEspecie = new HashMap<>();
        id = 1;
        for (PokemonBase p : Pokedex.getTodos()) {
            String sim = unico(simbolo(p.getNombre()), bd.especies().keySet());
            simboloEspecie.putIfAbsent(p.getNombre().toLowerCase(Locale.ROOT), sim);
            if (!sim.equals(simbolo(p.getNombre())))
                avisos.add("Pokémon duplicado '" + p.getNombre() + "': renombrado a " + sim);

            TipoPokemon t1 = TipoPokemon.buscar(p.getTipo1()).orElse(null);
            if (t1 == null) {
                avisos.add("Pokémon '" + p.getNombre() + "': tipo1 desconocido '" + p.getTipo1() + "' -> Normal");
                t1 = TipoPokemon.NORMAL;
            }
            TipoPokemon t2 = null;
            if (p.getTipo2() != null) {
                t2 = TipoPokemon.buscar(p.getTipo2()).orElse(null);
                if (t2 == null) avisos.add("Pokémon '" + p.getNombre() + "': tipo2 desconocido '" + p.getTipo2() + "' (ignorado)");
            }

            Map<Estadistica, Integer> stats = new EnumMap<>(Estadistica.class);
            stats.put(Estadistica.VIDA, p.getVidaBase());
            stats.put(Estadistica.ATAQUE, p.getAtaqueBase());
            stats.put(Estadistica.DEFENSA, p.getDefensaBase());
            stats.put(Estadistica.VELOCIDAD, p.getVelocidadBase());
            stats.put(Estadistica.ATAQUE_ESP, p.getAtaqueEspBase());   // 0 = sin dato (el juego original no los tenía)
            stats.put(Estadistica.DEFENSA_ESP, p.getDefensaEspBase());

            List<Aprendizaje> aprende = new ArrayList<>();
            if (p.getMovimientosPorNivel() != null) {
                for (Movimiento m : p.getMovimientosPorNivel()) {
                    if (m == null) { avisos.add("Pokémon '" + p.getNombre() + "': tiene un movimiento null en su lista (ignorado)"); continue; }
                    String ms = simboloPorNombre.get(m.getNombre().toLowerCase(Locale.ROOT));
                    if (ms == null) { avisos.add("Pokémon '" + p.getNombre() + "': movimiento fuera de Ataques: " + m.getNombre()); continue; }
                    aprende.add(new Aprendizaje(ms, m.getNivelRequerido()));
                }
            }

            List<DatosEvolucion> evos = new ArrayList<>();
            if (p.getEvolucion() == null && p.getNivelEvolucion() > 0)
                avisos.add("Pokémon '" + p.getNombre() + "': nivel de evolución " + p.getNivelEvolucion()
                        + " pero sin evolución definida (se descarta el nivel)");
            if (p.getEvolucion() != null) {
                String destino = simbolo(p.getEvolucion());
                evos.add(p.getNivelEvolucion() > 0 ? DatosEvolucion.porNivel(destino, p.getNivelEvolucion())
                        : new DatosEvolucion(destino, Map.of()));
            }

            DatosForma forma = DatosForma.porDefecto(t1, t2).conStats(stats).conEvoluciones(evos).conPorNivel(aprende)
                    .conHabilidades(p.getHabilidades(), null);
            bd.poner(new DatosEspecie(id++, sim, p.getNombre(), List.of(forma)));
        }
        importarMundo(bd, avisos, simboloPorNombre, simboloEspecie);
        return new Importacion(bd, avisos);
    }

    // ------------------------------------------------------------- mundo (fase 2)

    private static String simboloZona(Ubicacion u) {
        return u == null ? null : u.name().toLowerCase(Locale.ROOT);
    }

    private static List<TipoPokemon> tipos(Collection<String> textos, String donde, List<String> avisos) {
        List<TipoPokemon> r = new ArrayList<>();
        for (String t : textos) {
            TipoPokemon x = TipoPokemon.buscar(t).orElse(null);
            if (x == null) avisos.add(donde + ": tipo desconocido '" + t + "' (ignorado)");
            else r.add(x);
        }
        return r;
    }

    private static void importarMundo(BaseDatos bd, List<String> avisos,
                                      Map<String, String> simboloMov, Map<String, String> simboloEspecie) {
        // Generaciones y jefes del Gobierno (uno por generación)
        for (Generacion g : Generacion.values()) {
            bd.poner(new DatosGeneracion(g.getNumero(), g.getRegion(), g.getDesde(), g.getHasta(),
                    g.getAmbiente(), g.getBonusNivel()));
            Gobierno.Jefe j = Gobierno.getJefe(g);
            bd.poner(new DatosJefe(g.getNumero(), j.getNombre(), j.getCargo(),
                    tipos(j.getTipos(), "Jefe '" + j.getNombre() + "'", avisos), j.getNivelBase(),
                    j.getDerrota(), List.of(j.getMemes())));
        }

        // Zonas: se insertan en el orden real del camino
        List<Ubicacion> orden = new ArrayList<>(Mapa.getRecorrido());
        for (Ubicacion u : Ubicacion.values()) {
            // las ranuras libres solo cuentan si el juego tiene un lugar definido en ellas
            boolean usada = Mapa.zonaDe(u) != null || Mapa.puebloDe(u) != null;
            if (!orden.contains(u) && (!u.esRanuraLibre() || usada)) orden.add(u);
        }
        for (Ubicacion u : orden) {
            Zona z = Mapa.zonaDe(u);
            Pueblo p = Mapa.puebloDe(u);
            String sim = simboloZona(u);
            if (z != null) {
                bd.poner(new DatosZona(sim, u.getNombre(), ClaseZona.RUTA, z.getDescripcion(),
                        tipos(z.getTipos(), "Zona '" + sim + "'", avisos), z.getNivelMin(), z.getNivelMax(),
                        simboloZona(z.getAnterior()), simboloZona(z.getSiguiente()), simboloZona(z.getPuebloCercano()),
                        List.of(z.getEntrenadores()), false, false));
            } else if (p != null) {
                bd.poner(new DatosZona(sim, u.getNombre(), ClaseZona.POBLACION, p.getDescripcion(),
                        tipos(p.getTiposTipicos(), "Zona '" + sim + "'", avisos), 0, 0,
                        simboloZona(p.getRutaAnterior()), simboloZona(p.getRutaSiguiente()), null,
                        List.of(), p.tieneCasino(), p.tieneSedeGobierno()));
            } else if (u == Ubicacion.VILLAVERDE) {
                bd.poner(new DatosZona(sim, u.getNombre(), ClaseZona.INICIO, "Tu pueblo natal, donde empieza la aventura.",
                        List.of(), 0, 0, null, simboloZona(Ubicacion.RUTA1), null, List.of(), false, false));
            } else {
                avisos.add("Ubicación sin datos en Mapa: " + u.name());
            }
        }

        // Habilidades que el juego tiene cargadas (vacío si nunca se aplicaron datos con habilidades)
        Habilidades.todas().forEach((sim, d) -> bd.poner(new DatosHabilidad(sim, d.nombre(), d.descripcion(), d.efecto(),
                d.tipo() == null ? null : TipoPokemon.buscar(d.tipo()).orElse(null), d.estado(), d.valor())));

        // Gimnasios
        for (Gimnasio g : Gimnasio.getGimnasios()) {
            String mt = simboloMov.get(g.getMtRecompensa().toLowerCase(Locale.ROOT));
            if (mt == null) avisos.add("Gimnasio '" + g.getNombre() + "': la MT '" + g.getMtRecompensa() + "' no existe en Ataques");
            bd.poner(new DatosGimnasio(simbolo(g.getNombre()), g.getNombre(), simboloZona(g.getCiudad()),
                    tipos(g.getTiposInternos(), "Gimnasio '" + g.getNombre() + "'", avisos), g.getLider(),
                    g.getTituloLider(), List.of(g.getMiembros()), g.getMedalla(), g.getRecompensaDinero(),
                    mt, g.getNivelBase(), g.getTamanoEquipo()));
        }

        // Reglas globales (constantes sueltas del juego original)
        List<String> excluidos = new ArrayList<>();
        for (String nombre : Generacion.getExcluidos()) {
            String sim = simboloEspecie.get(nombre.toLowerCase(Locale.ROOT));
            if (sim == null) avisos.add("Pokémon excluido de salvajes que no está en la Pokedex: " + nombre);
            else excluidos.add(sim);
        }
        java.util.Collections.sort(excluidos);
        bd.reglas(new DatosReglas(Npc.getProbabilidad(), Npc.getNombres(), Npc.getFrasesReto(),
                Npc.getFrasesDerrota(), 6, 3, 2, 5, 3000, excluidos,
                new DatosCombate(Tipos.activa(), ReglasCombate.efectos(), ReglasCombate.precision(), ReglasCombate.pp(),
                        ReglasCombate.estadisticas(), ReglasCombate.habilidades())));

        // Objetos de la tienda
        int id = 1;
        bd.poner(new DatosObjeto(id++, "poke_ball", "Poke Ball", "pokeballs", 50, "Sirve para capturar Pokémon salvajes.", List.of("tienda")));
        bd.poner(new DatosObjeto(id++, "pocion", "Poción", "medicina", 300, "Cura a un Pokémon.", List.of("tienda")));
        bd.poner(new DatosObjeto(id++, "mt_aleatoria", "MT aleatoria", "mts", 200, "Una MT con un movimiento al azar.", List.of("tienda")));
        for (ObjetoEquipable o : ObjetoEquipable.values()) {
            List<String> banderas = new ArrayList<>(List.of("tienda", "equipable"));
            if (o.getTipoPotenciado() != null) {
                TipoPokemon t = TipoPokemon.buscar(o.getTipoPotenciado()).orElse(null);
                if (t != null) banderas.add("potencia_" + t.clave());
            }
            bd.poner(new DatosObjeto(id++, simbolo(o.name()), o.getNombre(), "equipables", o.getPrecio(),
                    o.getDescripcion(), banderas));
        }
    }

    // ------------------------------------------------------------------ exportar

    public static Movimiento aMovimiento(DatosMovimiento m, int nivelRequerido) {
        DatosEfecto e = m.efecto();
        EstadoAlterado estado = (e == null || e.estado() == null) ? null : EstadoAlterado.valueOf(e.estado());
        Efectos ef = e == null ? new Efectos("Ninguno", 0, null, 0)
                : new Efectos(e.nombre(), e.dano(), estado, e.probabilidad());
        Movimiento mov = new Movimiento(m.nombre(), m.potencia(), m.tipo().nombreProyecto(), nivelRequerido, ef);
        mov.configurar(m.categoria().clave(), m.precision(), m.pp(), m.prioridad());
        return mov;
    }

    /** Las especies ordenadas por número de Pokédex (id); a igual id, en orden de inserción. */
    public static List<DatosEspecie> ordenadas(BaseDatos bd) {
        List<DatosEspecie> l = new ArrayList<>(bd.especies().values());
        l.sort(java.util.Comparator.comparingInt(DatosEspecie::id));
        return l;
    }

    /** Genera los PokemonBase (forma 0 de cada especie), ordenados por número de Pokédex. */
    public static List<PokemonBase> aPokemonBase(BaseDatos bd) {
        Map<String, Movimiento> cache = new HashMap<>();
        List<PokemonBase> salida = new ArrayList<>();
        for (DatosEspecie e : ordenadas(bd)) {
            DatosForma f = e.forma(0);
            List<Movimiento> movs = new ArrayList<>();
            for (Aprendizaje a : f.porNivel()) {
                DatosMovimiento dm = bd.movimiento(a.movimiento());
                if (dm == null) continue;
                movs.add(cache.computeIfAbsent(a.movimiento() + "@" + a.nivel(), k -> aMovimiento(dm, a.nivel())));
            }
            String evolucion = null;
            int nivelEvo = 0;
            if (!f.evoluciones().isEmpty()) {
                DatosEvolucion ev = f.evoluciones().get(0);
                DatosEspecie destino = bd.especie(ev.destino());
                evolucion = destino == null ? null : destino.nombre();
                nivelEvo = ev.nivelMinimo().orElse(0);
            }
            PokemonBase pb = new PokemonBase(e.nombre(), f.tipo1().nombreProyecto(),
                    f.tipo2() == null ? null : f.tipo2().nombreProyecto(),
                    f.stat(Estadistica.VIDA), f.stat(Estadistica.ATAQUE), f.stat(Estadistica.DEFENSA),
                    f.stat(Estadistica.VELOCIDAD), nivelEvo, evolucion, movs);
            pb.configurarCombate(f.stat(Estadistica.ATAQUE_ESP), f.stat(Estadistica.DEFENSA_ESP), f.habilidades());
            salida.add(pb);
        }
        return salida;
    }
}
