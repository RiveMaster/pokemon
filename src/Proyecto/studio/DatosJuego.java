package Proyecto.studio;

import Proyecto.Ataques;
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
import Proyecto.Tienda;
import Proyecto.ReglasCombate;
import Proyecto.Tipos;
import Proyecto.Ubicacion;
import Proyecto.Zona;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Fase 4: hace que el juego use los datos de {@link BaseDatos} en vez de los escritos en el código.
 * <ul>
 *   <li>{@link #iniciar()}: si existe la carpeta de datos, la carga y la aplica (si algo falla, el juego
 *       sigue con sus datos integrados).</li>
 *   <li>{@link #aplicar(BaseDatos)}: valida, prepara todos los objetos y solo entonces sustituye los datos
 *       del juego, de modo que una base inválida nunca deja el juego a medias.</li>
 * </ul>
 * Lugares: son el enum {@code Ubicacion} (también se guarda en las partidas). Se pueden editar los 23 originales y
 * crear hasta 24 nuevos, que ocupan las ranuras libres {@code extra1..extra24}. Tampoco hay más de 10 generaciones.
 */
public final class DatosJuego {
    /** Carpeta por defecto; se puede cambiar con -Dstudio.dir=ruta. -Dstudio=off desactiva la carga. */
    public static final String CARPETA = "data/studio";

    private DatosJuego() {}

    /** Carpeta de datos en uso (por defecto data/studio, o la de -Dstudio.dir). */
    public static Path carpeta() {
        return Path.of(System.getProperty("studio.dir", CARPETA));
    }

    // ------------------------------------------------------------------ arranque

    /** Carga y aplica los datos si existen. Nunca lanza excepciones: ante un fallo conserva los datos del código. */
    public static void iniciar() {
        if ("off".equalsIgnoreCase(System.getProperty("studio"))) return;
        Path dir = carpeta();
        if (!Files.exists(dir.resolve("pokemon.json"))) return; // sin datos: se usan los integrados
        try {
            BaseDatos bd = BaseDatos.cargar(dir);
            List<String> avisos = aplicar(bd);
            System.out.println("📂 Datos de Studio cargados de " + dir + " (" + bd.especies().size() + " Pokémon, "
                    + bd.movimientos().size() + " movimientos, " + bd.zonas().size() + " zonas, "
                    + bd.gimnasios().size() + " gimnasios).");
            avisos.forEach(a -> System.out.println("   ⚠ " + a));
        } catch (Exception e) {
            System.out.println("⚠ No se pudieron cargar los datos de Studio (" + e.getMessage()
                    + "). El juego usa sus datos integrados.");
        }
    }

    // ------------------------------------------------------------------ ranuras para lugares nuevos

    /** Símbolos de las ranuras libres del juego ("extra1".."extra24"), en orden. */
    public static List<String> ranuras() {
        List<String> l = new ArrayList<>();
        for (Ubicacion u : Ubicacion.values()) if (u.esRanuraLibre()) l.add(u.name().toLowerCase(Locale.ROOT));
        return l;
    }

    /** Primera ranura que ninguna zona de {@code bd} está usando, o null si están todas ocupadas. */
    public static String siguienteRanuraLibre(BaseDatos bd) {
        for (String r : ranuras()) if (bd.zona(r) == null) return r;
        return null;
    }

    public static int ranurasLibres(BaseDatos bd) {
        int n = 0;
        for (String r : ranuras()) if (bd.zona(r) == null) n++;
        return n;
    }

    // ------------------------------------------------------------------ aplicar

    private static Ubicacion ubicacion(String simbolo) {
        if (simbolo == null) return null;
        for (Ubicacion u : Ubicacion.values()) if (u.name().toLowerCase(Locale.ROOT).equals(simbolo)) return u;
        return null;
    }

    private static String[] tiposComoTexto(List<TipoPokemon> tipos) {
        return tipos.stream().map(TipoPokemon::nombreProyecto).toArray(String[]::new);
    }

    /**
     * Sustituye los datos del juego por los de {@code bd}. Devuelve los avisos (cosas que la base define
     * pero el juego no puede usar). Lanza IllegalStateException, sin cambiar nada, si la base no es válida.
     */
    public static List<String> aplicar(BaseDatos bd) {
        List<String> errores = bd.validar();
        if (!errores.isEmpty())
            throw new IllegalStateException(errores.size() + " errores de validación (el primero: " + errores.get(0) + ")");
        if (bd.especies().isEmpty() || bd.movimientos().isEmpty())
            throw new IllegalStateException("la base necesita al menos un Pokémon y un movimiento");

        List<String> avisos = new ArrayList<>();

        // ---------------------------------------------- 1. preparar (no se cambia nada del juego)
        List<Movimiento> movimientos = new ArrayList<>();
        for (DatosMovimiento m : bd.movimientos().values()) movimientos.add(PuenteProyecto.aMovimiento(m, 0));
        List<PokemonBase> pokemon = PuenteProyecto.aPokemonBase(bd);
        List<DatosEspecie> orden = PuenteProyecto.ordenadas(bd);

        // Generaciones: desde/hasta de la base son números de Pokédex (ids); el juego usa posiciones.
        Map<Generacion, int[]> rangos = new HashMap<>();
        Map<Generacion, DatosGeneracion> datosGen = new HashMap<>();
        for (DatosGeneracion g : bd.generaciones().values()) {
            Generacion gen = null;
            for (Generacion x : Generacion.values()) if (x.getNumero() == g.numero()) gen = x;
            if (gen == null) {
                avisos.add("Generación " + g.numero() + ": el juego solo tiene " + Generacion.values().length + " generaciones (se ignora)");
                continue;
            }
            int desde = 1, hasta = 0;
            for (DatosEspecie e : orden) {
                if (e.id() < g.desde()) desde++;
                if (e.id() <= g.hasta()) hasta++;
            }
            if (hasta < desde) {
                avisos.add("Generación " + g.numero() + ": ningún Pokémon en el rango " + g.desde() + "-" + g.hasta() + " (se mantiene el rango anterior)");
                continue;
            }
            rangos.put(gen, new int[]{desde, hasta});
            datosGen.put(gen, g);
        }

        List<String> excluidos = new ArrayList<>();
        for (String s : bd.reglas().excluidosSalvajes()) {
            DatosEspecie e = bd.especie(s);
            if (e != null) excluidos.add(e.nombre());
        }

        Map<Integer, Gobierno.Jefe> jefes = new HashMap<>();
        for (DatosJefe j : bd.jefes().values()) {
            jefes.put(j.generacion(), new Gobierno.Jefe(j.nombre(), j.cargo(), Generacion.tipos(tiposComoTexto(j.tipos())),
                    j.nivelBase(), j.fraseDerrota(), j.memes().toArray(new String[0])));
        }

        List<Gimnasio> gimnasios = new ArrayList<>();
        for (DatosGimnasio g : bd.gimnasios().values()) {
            Ubicacion u = ubicacion(g.ciudad());
            if (u == null) {
                avisos.add("Gimnasio '" + g.nombre() + "': la ciudad '" + g.ciudad() + "' no existe en el juego (se ignora)");
                continue;
            }
            DatosMovimiento mt = g.mtRecompensa() == null ? null : bd.movimiento(g.mtRecompensa());
            gimnasios.add(Gimnasio.crear(g.nombre(), u, g.tipo() == null ? "" : g.tipo().nombre(),
                    Generacion.tipos(tiposComoTexto(g.tiposEquipo())), g.lider(), g.tituloLider(),
                    g.miembros().toArray(new String[0]), g.medalla(), g.recompensaDinero(),
                    mt == null ? "" : mt.nombre(), g.nivelBase(), g.tamanoEquipo()));
        }

        List<Zona> zonas = new ArrayList<>();
        List<Pueblo> pueblos = new ArrayList<>();
        Map<Ubicacion, String> nombres = new HashMap<>();
        for (DatosZona z : bd.zonas().values()) {
            Ubicacion u = ubicacion(z.simbolo());
            if (u == null) {
                avisos.add("Zona '" + z.simbolo() + "': el juego no tiene ese lugar y se ignora (los lugares nuevos usan los símbolos "
                        + "extra1..extra" + ranuras().size() + ")");
                continue;
            }
            nombres.put(u, z.nombre());
            switch (z.clase()) {
                case INICIO -> { /* Villaverde: solo se aplica el nombre */ }
                case RUTA -> zonas.add(new Zona(u, Generacion.tipos(tiposComoTexto(z.tipos())), z.nivelMin(), z.nivelMax(),
                        z.descripcion(), ubicacion(z.anterior()), ubicacion(z.siguiente()), ubicacion(z.puebloCercano()),
                        z.clasesEntrenador().toArray(new String[0])));
                case POBLACION -> pueblos.add(new Pueblo(u, z.descripcion(), Generacion.tipos(tiposComoTexto(z.tipos())),
                        z.sedeGobierno(), z.casino(), ubicacion(z.anterior()), ubicacion(z.siguiente())));
            }
        }
        List<Ubicacion> recorrido = new ArrayList<>();
        DatosZona z = bd.zonas().values().stream().filter(x -> x.clase() == ClaseZona.INICIO).findFirst().orElse(null);
        Set<String> vistas = new HashSet<>();
        while (z != null && vistas.add(z.simbolo())) {
            Ubicacion u = ubicacion(z.simbolo());
            if (u == null) { avisos.add("El camino se interrumpe en '" + z.simbolo() + "' (lugar desconocido para el juego)"); break; }
            recorrido.add(u);
            z = z.siguiente() == null ? null : bd.zona(z.siguiente());
        }
        for (Ubicacion u : Ubicacion.values())
            if (!bd.zonas().isEmpty() && !recorrido.contains(u) && !(u.esRanuraLibre() && bd.zona(u.name().toLowerCase(Locale.ROOT)) == null))
                avisos.add("'" + u.getNombre() + "' ya no está en el camino (las partidas guardadas allí volverán a Villaverde)");

        // ---------------------------------------------- 2. sustituir (el primero que puede fallar va primero)
        DatosReglas r = bd.reglas();
        if (gimnasios.isEmpty()) avisos.add("La base no tiene gimnasios aplicables: se mantienen los del juego");
        else Gimnasio.reemplazar(gimnasios, r.miembrosPorGimnasio(), r.pokemonPorMiembro());

        Ataques.reemplazar(movimientos);
        Pokedex.reemplazar(pokemon);
        Generacion.reemplazarExcluidos(excluidos);
        rangos.forEach((gen, rango) -> {
            DatosGeneracion g = datosGen.get(gen);
            gen.configurar(g.region(), rango[0], rango[1], g.ambiente(), g.bonusNivel());
        });
        Generacion.reiniciarCaches();

        if (jefes.isEmpty()) avisos.add("La base no tiene jefes del Gobierno: se mantienen los del juego");
        else Gobierno.reemplazar(jefes, r.tamanoEquipoJefe(), r.recompensaJefe());

        if (zonas.isEmpty() && pueblos.isEmpty()) avisos.add("La base no tiene zonas aplicables: se mantiene el mapa del juego");
        else Mapa.reemplazar(zonas, pueblos, recorrido);
        nombres.forEach(Ubicacion::setNombre);
        for (Ubicacion u : Ubicacion.values())
            if (u.esRanuraLibre() && !nombres.containsKey(u)) u.setNombre("Lugar libre " + u.name().substring("EXTRA".length()));

        DatosCombate cb = r.combate();
        ReglasCombate.configurar(cb.efectosMovimientos(), cb.precision(), cb.pp(), cb.estadisticas(), cb.habilidades());
        Map<String, Habilidades.Definicion> habs = new HashMap<>();
        for (DatosHabilidad h : bd.habilidades().values())
            habs.put(h.simbolo(), new Habilidades.Definicion(h.nombre(), h.descripcion(), h.efecto(),
                    h.tipo() == null ? null : h.tipo().nombreProyecto(), h.estado(), h.valor()));
        Habilidades.reemplazar(habs);

        if (cb.eficaciaTipos()) {
            TablaTipos tabla = TablaTipos.desdeMapa(bd.tipos().aMapa());   // copia: el juego no comparte el objeto editable
            Tipos.configurar((ataque, defensa) -> {
                TipoPokemon a = TipoPokemon.buscar(ataque).orElse(null), d = TipoPokemon.buscar(defensa).orElse(null);
                return a == null || d == null ? 1.0 : tabla.multiplicador(a, d);
            });
        } else {
            Tipos.configurar(null);
        }

        Npc.configurar(r.probabilidadNpc(), r.premioNpcPorNivel(), r.nombresNpc(), r.frasesReto(), r.frasesDerrota());

        DatosObjeto pokeball = bd.objeto("poke_ball"), pocion = bd.objeto("pocion"), mt = bd.objeto("mt_aleatoria");
        Tienda.configurarPrecios(pokeball == null ? 50 : pokeball.precio(), pocion == null ? 300 : pocion.precio(),
                mt == null ? 200 : mt.precio());
        for (ObjetoEquipable o : ObjetoEquipable.values()) {
            DatosObjeto d = bd.objeto(PuenteProyecto.simbolo(o.name()));
            if (d != null) o.configurar(d.descripcion(), d.precio());
        }
        return avisos;
    }
}
