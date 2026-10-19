package Proyecto.studio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Toda la información del juego, como en un proyecto de Pokémon Studio: especies, movimientos,
 * habilidades, objetos y tabla de tipos. Se guarda en una carpeta con un JSON por categoría.
 */
public final class BaseDatos {
    private final Map<String, DatosEspecie> especies = new LinkedHashMap<>();
    private final Map<String, DatosMovimiento> movimientos = new LinkedHashMap<>();
    private final Map<String, DatosHabilidad> habilidades = new LinkedHashMap<>();
    private final Map<String, DatosObjeto> objetos = new LinkedHashMap<>();
    private final Map<String, DatosZona> zonas = new LinkedHashMap<>();
    private final Map<String, DatosGimnasio> gimnasios = new LinkedHashMap<>();
    private final Map<Integer, DatosJefe> jefes = new LinkedHashMap<>();
    private final Map<Integer, DatosGeneracion> generaciones = new LinkedHashMap<>();
    private DatosReglas reglas = DatosReglas.vacias();
    private TablaTipos tipos = TablaTipos.estandar();

    // ---------------------------------------------------------------- acceso

    public Map<String, DatosEspecie> especies() { return Collections.unmodifiableMap(especies); }
    public Map<String, DatosMovimiento> movimientos() { return Collections.unmodifiableMap(movimientos); }
    public Map<String, DatosHabilidad> habilidades() { return Collections.unmodifiableMap(habilidades); }
    public Map<String, DatosObjeto> objetos() { return Collections.unmodifiableMap(objetos); }
    public Map<String, DatosZona> zonas() { return Collections.unmodifiableMap(zonas); }
    public Map<String, DatosGimnasio> gimnasios() { return Collections.unmodifiableMap(gimnasios); }
    public Map<Integer, DatosJefe> jefes() { return Collections.unmodifiableMap(jefes); }
    public Map<Integer, DatosGeneracion> generaciones() { return Collections.unmodifiableMap(generaciones); }
    public DatosReglas reglas() { return reglas; }
    public void reglas(DatosReglas nuevas) { this.reglas = nuevas; }
    public TablaTipos tipos() { return tipos; }
    public void tipos(TablaTipos nueva) { this.tipos = nueva; }

    public DatosEspecie especie(String simbolo) { return especies.get(simbolo); }
    public DatosMovimiento movimiento(String simbolo) { return movimientos.get(simbolo); }
    public DatosHabilidad habilidad(String simbolo) { return habilidades.get(simbolo); }
    public DatosObjeto objeto(String simbolo) { return objetos.get(simbolo); }
    public DatosZona zona(String simbolo) { return zonas.get(simbolo); }
    public DatosGimnasio gimnasio(String simbolo) { return gimnasios.get(simbolo); }
    public DatosJefe jefe(int generacion) { return jefes.get(generacion); }
    public DatosGeneracion generacion(int numero) { return generaciones.get(numero); }

    /** Añade o reemplaza (la edición de un registro es sustituirlo por una copia modificada). */
    public void poner(DatosEspecie e) { especies.put(e.simbolo(), e); }
    public void poner(DatosMovimiento m) { movimientos.put(m.simbolo(), m); }
    public void poner(DatosHabilidad h) { habilidades.put(h.simbolo(), h); }
    public void poner(DatosObjeto o) { objetos.put(o.simbolo(), o); }
    public void poner(DatosZona z) { zonas.put(z.simbolo(), z); }
    public void poner(DatosGimnasio g) { gimnasios.put(g.simbolo(), g); }
    public void poner(DatosJefe j) { jefes.put(j.generacion(), j); }
    public void poner(DatosGeneracion g) { generaciones.put(g.numero(), g); }

    public void quitarEspecie(String simbolo) { especies.remove(simbolo); }
    public void quitarMovimiento(String simbolo) { movimientos.remove(simbolo); }
    public void quitarHabilidad(String simbolo) { habilidades.remove(simbolo); }
    public void quitarObjeto(String simbolo) { objetos.remove(simbolo); }
    public void quitarZona(String simbolo) { zonas.remove(simbolo); }
    public void quitarGimnasio(String simbolo) { gimnasios.remove(simbolo); }
    public void quitarJefe(int generacion) { jefes.remove(generacion); }
    public void quitarGeneracion(int numero) { generaciones.remove(numero); }

    // ------------------------------------------------------------ validación

    /** Devuelve la lista de problemas encontrados (vacía si todo es coherente). */
    public List<String> validar() {
        List<String> err = new ArrayList<>();
        comprobarIds(err, "especie", especies.values().stream().map(e -> new Object[]{e.id(), e.simbolo()}).toList());
        comprobarIds(err, "movimiento", movimientos.values().stream().map(m -> new Object[]{m.id(), m.simbolo()}).toList());

        for (DatosEspecie e : especies.values()) {
            Set<Integer> numeros = new HashSet<>();
            for (DatosForma f : e.formas()) {
                String donde = "Especie '" + e.simbolo() + "' forma " + f.forma() + ": ";
                if (!numeros.add(f.forma())) err.add(donde + "número de forma repetido");
                for (Estadistica s : Estadistica.values())
                    if (f.stat(s) < 0 || f.ev(s) < 0) err.add(donde + "estadística negativa (" + s + ")");
                if (f.ratioCaptura() < 0 || f.ratioCaptura() > 255) err.add(donde + "ratio de captura fuera de 0..255");
                for (DatosEvolucion ev : f.evoluciones())
                    if (!especies.containsKey(ev.destino())) err.add(donde + "evoluciona a una especie inexistente: " + ev.destino());
                for (Aprendizaje a : f.porNivel())
                    if (!movimientos.containsKey(a.movimiento())) err.add(donde + "movimiento inexistente: " + a.movimiento());
                for (List<String> lista : List.of(f.mt(), f.huevo(), f.tutor()))
                    for (String s : lista)
                        if (!movimientos.containsKey(s)) err.add(donde + "movimiento inexistente: " + s);
                for (String h : f.habilidades())
                    if (!habilidades.containsKey(h)) err.add(donde + "habilidad inexistente: " + h);
                if (f.habilidadOculta() != null && !habilidades.containsKey(f.habilidadOculta()))
                    err.add(donde + "habilidad oculta inexistente: " + f.habilidadOculta());
            }
        }
        for (DatosMovimiento m : movimientos.values()) {
            if (m.potencia() < 0) err.add("Movimiento '" + m.simbolo() + "': potencia negativa");
            if (m.precision() < 0 || m.precision() > 100) err.add("Movimiento '" + m.simbolo() + "': precisión fuera de 0..100");
            if (m.pp() < 0) err.add("Movimiento '" + m.simbolo() + "': PP negativos");
            if (m.efecto() != null && m.efecto().estado() != null && !DatosEfecto.ESTADOS.contains(m.efecto().estado()))
                err.add("Movimiento '" + m.simbolo() + "': estado alterado desconocido: " + m.efecto().estado());
        }
        for (DatosHabilidad h : habilidades.values()) {
            String d = "Habilidad '" + h.simbolo() + "': ";
            if (!DatosHabilidad.EFECTOS.contains(h.efecto())) err.add(d + "efecto desconocido: " + h.efecto());
            if ((h.efecto().equals("potencia_tipo") || h.efecto().equals("inmunidad_tipo")) && h.tipo() == null)
                err.add(d + "el efecto " + h.efecto() + " necesita un tipo");
            if (h.efecto().equals("contacto_estado") && (h.estado() == null || !DatosEfecto.ESTADOS.contains(h.estado())))
                err.add(d + "el efecto contacto_estado necesita un estado válido (" + DatosEfecto.ESTADOS + ")");
            if (h.efecto().equals("contacto_estado") && (h.valor() <= 0 || h.valor() > 100))
                err.add(d + "el efecto contacto_estado necesita una probabilidad entre 1 y 100");
        }
        validarMundo(err);
        return err;
    }

    private void validarMundo(List<String> err) {
        for (DatosZona z : zonas.values()) {
            String d = "Zona '" + z.simbolo() + "': ";
            if (z.anterior() != null && !zonas.containsKey(z.anterior())) err.add(d + "zona anterior inexistente: " + z.anterior());
            if (z.siguiente() != null && !zonas.containsKey(z.siguiente())) err.add(d + "zona siguiente inexistente: " + z.siguiente());
            if (z.puebloCercano() != null && !zonas.containsKey(z.puebloCercano())) err.add(d + "pueblo cercano inexistente: " + z.puebloCercano());
            if (z.clase() != ClaseZona.INICIO && z.anterior() == null)
                err.add(d + "necesita una zona anterior (el juego siempre ofrece «Volver»)");
            if (z.clase() == ClaseZona.RUTA) {
                if (z.tipos().isEmpty()) err.add(d + "una ruta necesita al menos un tipo de Pokémon salvaje");
                if (z.nivelMax() <= 0) err.add(d + "una ruta necesita niveles de Pokémon salvajes");
                if (z.puebloCercano() == null) err.add(d + "una ruta necesita un pueblo cercano");
            }
            // el camino debe ser coherente en los dos sentidos
            if (z.siguiente() != null && zonas.containsKey(z.siguiente())
                    && !z.simbolo().equals(zonas.get(z.siguiente()).anterior()))
                err.add(d + "'" + z.siguiente() + "' no la tiene como zona anterior");
        }
        // todo el camino debe ser alcanzable desde la zona de inicio, sin ciclos
        List<DatosZona> inicios = zonas.values().stream().filter(z -> z.clase() == ClaseZona.INICIO).toList();
        if (!zonas.isEmpty() && inicios.size() != 1) {
            err.add("Debe haber exactamente una zona de inicio y hay " + inicios.size());
        } else if (!zonas.isEmpty()) {
            Set<String> vistas = new HashSet<>();
            DatosZona actual = inicios.get(0);
            while (actual != null && vistas.add(actual.simbolo())) actual = zonas.get(actual.siguiente());
            if (actual != null) err.add("El camino de zonas tiene un ciclo en '" + actual.simbolo() + "'");
            for (String z : zonas.keySet())
                if (!vistas.contains(z)) err.add("Zona '" + z + "' no se alcanza desde la zona de inicio");
        }
        Set<String> ciudades = new HashSet<>();
        for (DatosGimnasio g : gimnasios.values()) {
            String d = "Gimnasio '" + g.simbolo() + "': ";
            DatosZona z = zonas.get(g.ciudad());
            if (z == null) err.add(d + "ciudad inexistente: " + g.ciudad());
            else if (z.clase() != ClaseZona.POBLACION) err.add(d + "'" + g.ciudad() + "' no es una población");
            if (!ciudades.add(g.ciudad())) err.add(d + "ya hay otro gimnasio en " + g.ciudad());
            if (g.miembros().size() != reglas.miembrosPorGimnasio())
                err.add(d + "debe tener " + reglas.miembrosPorGimnasio() + " miembros y tiene " + g.miembros().size());
            if (g.tiposEquipo().isEmpty()) err.add(d + "sin tipos de equipo");
            if (g.mtRecompensa() != null && !movimientos.containsKey(g.mtRecompensa()))
                err.add(d + "movimiento de recompensa inexistente: " + g.mtRecompensa());
        }
        for (DatosJefe j : jefes.values()) {
            if (!generaciones.containsKey(j.generacion()))
                err.add("Jefe '" + j.nombre() + "': generación inexistente: " + j.generacion());
            if (j.tipos().isEmpty()) err.add("Jefe '" + j.nombre() + "': sin tipos");
        }
        for (DatosGeneracion g : generaciones.values()) {
            if (!jefes.containsKey(g.numero())) err.add("Generación " + g.numero() + ": no tiene jefe de Gobierno");
        }
        for (String s : reglas.excluidosSalvajes())
            if (!especies.containsKey(s)) err.add("Reglas: especie excluida inexistente: " + s);
        for (DatosZona z : zonas.values())
            if (z.sedeGobierno() && z.clase() != ClaseZona.POBLACION) err.add("Zona '" + z.simbolo() + "': solo una población puede ser sede del Gobierno");
    }

    private static void comprobarIds(List<String> err, String clase, List<Object[]> filas) {
        Set<Integer> vistos = new HashSet<>();
        for (Object[] f : filas) {
            int id = (Integer) f[0];
            if (id != 0 && !vistos.add(id)) err.add("Id repetido en " + clase + "s: " + id + " (" + f[1] + ")");
        }
    }

    // ------------------------------------------------------------ persistencia

    public void guardar(Path carpeta) throws IOException {
        Files.createDirectories(carpeta);
        escribir(carpeta.resolve("pokemon.json"), lista(especies, Serializador::aMapa));
        escribir(carpeta.resolve("movimientos.json"), lista(movimientos, Serializador::aMapa));
        escribir(carpeta.resolve("habilidades.json"), lista(habilidades, Serializador::aMapa));
        escribir(carpeta.resolve("objetos.json"), lista(objetos, Serializador::aMapa));
        escribir(carpeta.resolve("tipos.json"), tipos.aMapa());
        escribir(carpeta.resolve("zonas.json"), lista(zonas, Serializador::aMapa));
        escribir(carpeta.resolve("gimnasios.json"), lista(gimnasios, Serializador::aMapa));
        escribir(carpeta.resolve("jefes.json"), lista(jefes, Serializador::aMapa));
        escribir(carpeta.resolve("generaciones.json"), lista(generaciones, Serializador::aMapa));
        escribir(carpeta.resolve("reglas.json"), Serializador.aMapa(reglas));
    }

    public static BaseDatos cargar(Path carpeta) throws IOException {
        BaseDatos bd = new BaseDatos();
        for (Object o : leer(carpeta.resolve("pokemon.json"))) bd.poner(Serializador.especieDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("movimientos.json"))) bd.poner(Serializador.movimientoDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("habilidades.json"))) bd.poner(Serializador.habilidadDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("objetos.json"))) bd.poner(Serializador.objetoDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("zonas.json"))) bd.poner(Serializador.zonaDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("gimnasios.json"))) bd.poner(Serializador.gimnasioDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("jefes.json"))) bd.poner(Serializador.jefeDesde(Json.mapa(o)));
        for (Object o : leer(carpeta.resolve("generaciones.json"))) bd.poner(Serializador.generacionDesde(Json.mapa(o)));
        Path rp = carpeta.resolve("reglas.json");
        if (Files.exists(rp)) bd.reglas = Serializador.reglasDesde(Json.mapa(Json.parse(Files.readString(rp, StandardCharsets.UTF_8))));
        Path tp = carpeta.resolve("tipos.json");
        if (Files.exists(tp)) bd.tipos = TablaTipos.desdeMapa(Json.mapa(Json.parse(Files.readString(tp, StandardCharsets.UTF_8))));
        return bd;
    }

    private static <K, T> List<Object> lista(Map<K, T> datos, Function<T, Map<String, Object>> f) {
        List<Object> l = new ArrayList<>();
        for (T t : datos.values()) l.add(f.apply(t));
        return l;
    }

    private static void escribir(Path destino, Object json) throws IOException {
        Files.writeString(destino, Json.escribir(json), StandardCharsets.UTF_8);
    }

    private static List<Object> leer(Path origen) throws IOException {
        if (!Files.exists(origen)) return new ArrayList<>(); // categoría aún sin datos
        return Json.lista(Json.parse(Files.readString(origen, StandardCharsets.UTF_8)));
    }
}
