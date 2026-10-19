package Proyecto.studio.gui;

import Proyecto.Ubicacion;
import Proyecto.studio.CategoriaMovimiento;
import Proyecto.studio.ClaseZona;
import Proyecto.studio.DatosEfecto;
import Proyecto.studio.DatosGeneracion;
import Proyecto.studio.DatosGimnasio;
import Proyecto.studio.DatosJuego;
import Proyecto.studio.DatosHabilidad;
import Proyecto.studio.DatosJefe;
import Proyecto.studio.DatosMovimiento;
import Proyecto.studio.DatosObjeto;
import Proyecto.studio.DatosZona;
import Proyecto.studio.PuenteProyecto;
import Proyecto.studio.Referencias;
import Proyecto.studio.TipoPokemon;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Los paneles de edición de movimientos, habilidades, objetos, zonas, gimnasios, jefes y generaciones. */
final class PanelesSimples {
    private PanelesSimples() {}

    private static String requerido(String texto, String nombreCampo) {
        if (texto == null || texto.isBlank()) throw new IllegalArgumentException("El campo '" + nombreCampo + "' es obligatorio.");
        return texto;
    }

    private static int siguienteId(java.util.stream.IntStream ids) { return ids.max().orElse(0) + 1; }

    // ================================================================== MOVIMIENTOS

    static Registros<DatosMovimiento> movimientos(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<String> nombre = f.fila("Nombre", Campos.texto(20));
        JLabel simbolo = new JLabel(" ");
        f.fila("Símbolo", simbolo);
        Campo<Integer> id = f.fila("Número", Campos.entero(1, 99999, 1));
        Campo<TipoPokemon> tipo = f.fila("Tipo", Campos.tipo(false));
        Campo<CategoriaMovimiento> cat = f.fila("Categoría", Campos.combo(CategoriaMovimiento.values(), CategoriaMovimiento::nombre));
        Campo<Double> potencia = f.fila("Potencia", Campos.decimal(0, 1e10, 5));
        Campo<Integer> precision = f.fila("Precisión (0 = nunca falla)", Campos.entero(0, 100, 100));
        Campo<Integer> pp = f.fila("PP (0 = sin límite)", Campos.entero(0, 100, 20));
        Campo<Integer> prioridad = f.fila("Prioridad", Campos.entero(-7, 5, 0));
        f.seccion("Efecto secundario");
        Campo<String> estado = f.fila("Estado alterado", Campos.comboOpcional(DatosEfecto.ESTADOS.toArray(new String[0]), s -> s));
        Campo<Integer> prob = f.fila("Probabilidad (%)", Campos.entero(0, 100, 10));
        Campo<Double> danoExtra = f.fila("Daño extra", Campos.decimal(0, 1e10, 1));
        Campo<String> nombreEfecto = f.fila("Nombre del efecto", Campos.texto(16));
        f.seccion("Otros");
        Campo<List<String>> banderas = f.fila("Banderas (una por línea)", Campos.lineas(3));
        f.rellenar();

        Registros.Editor<DatosMovimiento> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosMovimiento m) {
                nombre.set(m == null ? "" : m.nombre());
                simbolo.setText(m == null ? "(se genera al crear)" : m.simbolo());
                id.set(m == null ? siguienteId(ctx.bd().movimientos().values().stream().mapToInt(DatosMovimiento::id)) : m.id());
                tipo.set(m == null ? TipoPokemon.NORMAL : m.tipo());
                cat.set(m == null ? CategoriaMovimiento.FISICO : m.categoria());
                potencia.set(m == null ? 40.0 : m.potencia());
                precision.set(m == null ? 100 : m.precision());
                pp.set(m == null ? 20 : m.pp());
                prioridad.set(m == null ? 0 : m.prioridad());
                DatosEfecto e = m == null ? null : m.efecto();
                estado.set(e == null ? null : e.estado());
                prob.set(e == null ? 10 : e.probabilidad());
                danoExtra.set(e == null ? 0.0 : e.dano());
                nombreEfecto.set(e == null ? "" : e.nombre());
                banderas.set(m == null ? List.of() : m.banderas());
            }

            public DatosMovimiento leer(DatosMovimiento actual) {
                String n = requerido(nombre.get(), "Nombre");
                String sim = actual != null ? actual.simbolo()
                        : PuenteProyecto.unico(PuenteProyecto.simbolo(n), ctx.bd().movimientos().keySet());
                DatosEfecto ef = null;
                if (estado.get() != null || danoExtra.get() != 0.0) {
                    String en = nombreEfecto.get().isBlank()
                            ? (estado.get() == null ? "Efecto" : estado.get().charAt(0) + estado.get().substring(1).toLowerCase(Locale.ROOT))
                            : nombreEfecto.get();
                    ef = new DatosEfecto(en, danoExtra.get(), estado.get(), prob.get());
                }
                return new DatosMovimiento(id.get(), sim, n, tipo.get(), cat.get(), potencia.get(), precision.get(),
                        pp.get(), prioridad.get(), ef, banderas.get());
            }
        };
        return new Registros<>(ctx, "Movimiento", () -> new ArrayList<>(ctx.bd().movimientos().values()),
                DatosMovimiento::simbolo, m -> m.nombre(), ed, m -> ctx.bd().poner(m), m -> {
            List<String> refs = Referencias.deMovimiento(ctx.bd(), m.simbolo());
            String msg = "¿Borrar el movimiento " + m.nombre() + "?";
            if (!refs.isEmpty()) msg += "\n\nEstá en uso (" + refs.size() + "); se quitará de todos esos sitios:\n" + Util.primeras(refs, 8);
            if (!ctx.confirmar(msg)) return;
            Referencias.borrarMovimiento(ctx.bd(), m.simbolo());
            ctx.cambiado();
        });
    }

    // ================================================================== HABILIDADES

    static Registros<DatosHabilidad> habilidades(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<String> nombre = f.fila("Nombre", Campos.texto(20));
        JLabel simbolo = new JLabel(" ");
        f.fila("Símbolo", simbolo);
        Campo<String> descripcion = f.fila("Descripción", Campos.area(4));
        f.seccion("Efecto en combate (solo con la regla «habilidades» activada)");
        Campo<String> efecto = f.fila("Efecto", Campos.combo(DatosHabilidad.EFECTOS.toArray(new String[0]), s -> s));
        Campo<TipoPokemon> tipo = f.fila("Tipo afectado", Campos.tipo(true));
        Campo<String> estado = f.fila("Estado que provoca", Campos.comboOpcional(DatosEfecto.ESTADOS.toArray(new String[0]), s -> s));
        Campo<Double> valor = f.fila("Valor", Campos.decimal(0, 100, 0.5));
        JLabel ayuda = Util.nota(" ");
        f.ancho(null, ayuda);
        f.rellenar();

        Runnable segunEfecto = () -> {
            String e = efecto.get() == null ? "ninguno" : efecto.get();
            Util.habilitar(tipo, e.equals("potencia_tipo") || e.equals("inmunidad_tipo"));
            Util.habilitar(estado, e.equals("contacto_estado"));
            Util.habilitar(valor, !e.equals("ninguno") && !e.equals("inmunidad_tipo"));
            ayuda.setText(switch (e) {
                case "potencia_tipo" -> "Con la vida al tercio o menos, los ataques de ese tipo hacen daño × valor (p. ej. 1.5).";
                case "inmunidad_tipo" -> "No recibe daño de los ataques de ese tipo.";
                case "contacto_estado" -> "Al recibir un golpe, «valor» % de probabilidad de dejar al atacante en ese estado.";
                case "regeneracion" -> "Al empezar cada turno recupera 1/valor de su vida máxima (p. ej. 16).";
                default -> "Solo informativa: no hace nada en combate.";
            });
        };
        ((javax.swing.JComboBox<?>) efecto.comp()).addActionListener(e -> segunEfecto.run());

        Registros.Editor<DatosHabilidad> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosHabilidad h) {
                nombre.set(h == null ? "" : h.nombre());
                simbolo.setText(h == null ? "(se genera al crear)" : h.simbolo());
                descripcion.set(h == null ? "" : h.descripcion());
                efecto.set(h == null ? "ninguno" : h.efecto());
                tipo.set(h == null ? null : h.tipo());
                estado.set(h == null ? null : h.estado());
                valor.set(h == null ? 0.0 : h.valor());
                segunEfecto.run();
            }

            public DatosHabilidad leer(DatosHabilidad actual) {
                String n = requerido(nombre.get(), "Nombre");
                String sim = actual != null ? actual.simbolo()
                        : PuenteProyecto.unico(PuenteProyecto.simbolo(n), ctx.bd().habilidades().keySet());
                String e = efecto.get() == null ? "ninguno" : efecto.get();
                if ((e.equals("potencia_tipo") || e.equals("inmunidad_tipo")) && tipo.get() == null)
                    throw new IllegalArgumentException("El efecto " + e + " necesita un tipo.");
                if (e.equals("contacto_estado") && estado.get() == null)
                    throw new IllegalArgumentException("El efecto contacto_estado necesita un estado.");
                if (e.equals("contacto_estado") && (valor.get() <= 0 || valor.get() > 100))
                    throw new IllegalArgumentException("La probabilidad debe estar entre 1 y 100.");
                return new DatosHabilidad(sim, n, descripcion.get(), e, tipo.get(), estado.get(), valor.get());
            }
        };
        return new Registros<>(ctx, "Habilidad", () -> new ArrayList<>(ctx.bd().habilidades().values()),
                DatosHabilidad::simbolo, DatosHabilidad::nombre, ed, h -> ctx.bd().poner(h), h -> {
            List<String> refs = Referencias.deHabilidad(ctx.bd(), h.simbolo());
            String msg = "¿Borrar la habilidad " + h.nombre() + "?";
            if (!refs.isEmpty()) msg += "\n\nLa tienen " + refs.size() + " Pokémon; se les quitará.";
            if (!ctx.confirmar(msg)) return;
            Referencias.borrarHabilidad(ctx.bd(), h.simbolo());
            ctx.cambiado();
        });
    }

    // ===================================================================== OBJETOS

    static Registros<DatosObjeto> objetos(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<String> nombre = f.fila("Nombre", Campos.texto(20));
        JLabel simbolo = new JLabel(" ");
        f.fila("Símbolo", simbolo);
        Campo<Integer> id = f.fila("Número", Campos.entero(1, 99999, 1));
        Campo<String> bolsillo = f.fila("Bolsillo", Campos.texto(14));
        Campo<Integer> precio = f.fila("Precio", Campos.entero(0, 1_000_000, 100));
        Campo<String> descripcion = f.fila("Descripción", Campos.area(3));
        Campo<List<String>> banderas = f.fila("Banderas (una por línea)", Campos.lineas(3));
        f.rellenar();
        Registros.Editor<DatosObjeto> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosObjeto o) {
                nombre.set(o == null ? "" : o.nombre());
                simbolo.setText(o == null ? "(se genera al crear)" : o.simbolo());
                id.set(o == null ? siguienteId(ctx.bd().objetos().values().stream().mapToInt(DatosObjeto::id)) : o.id());
                bolsillo.set(o == null ? "otros" : o.bolsillo());
                precio.set(o == null ? 100 : o.precio());
                descripcion.set(o == null ? "" : o.descripcion());
                banderas.set(o == null ? List.of() : o.banderas());
            }

            public DatosObjeto leer(DatosObjeto actual) {
                String n = requerido(nombre.get(), "Nombre");
                String sim = actual != null ? actual.simbolo()
                        : PuenteProyecto.unico(PuenteProyecto.simbolo(n), ctx.bd().objetos().keySet());
                return new DatosObjeto(id.get(), sim, n, bolsillo.get(), precio.get(), descripcion.get(), banderas.get());
            }
        };
        return new Registros<>(ctx, "Objeto", () -> new ArrayList<>(ctx.bd().objetos().values()),
                DatosObjeto::simbolo, DatosObjeto::nombre, ed, o -> ctx.bd().poner(o), o -> {
            List<String> refs = Referencias.deObjeto(ctx.bd(), o.simbolo());
            String msg = "¿Borrar el objeto " + o.nombre() + "?";
            if (!refs.isEmpty()) msg += "\n\nSe quitarán estas evoluciones:\n" + Util.primeras(refs, 8);
            if (!ctx.confirmar(msg)) return;
            Referencias.borrarObjeto(ctx.bd(), o.simbolo());
            ctx.cambiado();
        });
    }

    // ===================================================================== ZONAS

    private static boolean existeEnElJuego(String simbolo) {
        for (Ubicacion u : Ubicacion.values()) if (u.name().toLowerCase(Locale.ROOT).equals(simbolo)) return true;
        return false;
    }

    static Registros<DatosZona> zonas(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<String> nombre = f.fila("Nombre", Campos.texto(20));
        JLabel simbolo = new JLabel(" ");
        f.fila("Símbolo", simbolo);
        JLabel aviso = new JLabel(" ");
        f.ancho(null, aviso);
        Campo<ClaseZona> clase = f.fila("Clase", Campos.combo(ClaseZona.values(), ClaseZona::nombre));
        Campo<String> descripcion = f.fila("Descripción", Campos.area(3));
        Campo<List<TipoPokemon>> tipos = f.fila("Tipos (salvajes o de calle)", Campos.tipos());
        f.seccion("Camino");
        Campo<String> anterior = f.fila("Zona anterior", Campos.referencia(() -> Opciones.zonas(ctx, z -> true)));
        Campo<String> siguiente = f.fila("Zona siguiente", Campos.referencia(() -> Opciones.zonas(ctx, z -> true)));
        f.seccion("Rutas");
        Campo<Integer> min = f.fila("Nivel mínimo salvaje", Campos.entero(0, 100, 2));
        Campo<Integer> max = f.fila("Nivel máximo salvaje", Campos.entero(0, 100, 5));
        Campo<String> pueblo = f.fila("Pueblo cercano", Campos.referencia(() -> Opciones.zonas(ctx, z -> z.clase() != ClaseZona.RUTA)));
        Campo<List<String>> entrenadores = f.fila("Clases de entrenador NPC", Campos.lineas(3));
        f.seccion("Poblaciones");
        Campo<Boolean> casino = f.fila("Casino", Campos.check("Tiene casino"));
        Campo<Boolean> sede = f.fila("Gobierno", Campos.check("Es sede del Gobierno"));
        f.rellenar();

        Runnable segunClase = () -> {
            ClaseZona c = clase.get();
            boolean ruta = c == ClaseZona.RUTA, poblacion = c == ClaseZona.POBLACION;
            Util.habilitar(min, ruta); Util.habilitar(max, ruta); Util.habilitar(pueblo, ruta); Util.habilitar(entrenadores, ruta);
            Util.habilitar(casino, poblacion); Util.habilitar(sede, poblacion);
            Util.habilitar(tipos, c != ClaseZona.INICIO);
        };
        ((javax.swing.JComboBox<?>) clase.comp()).addActionListener(e -> segunClase.run());

        Registros.Editor<DatosZona> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosZona z) {
                nombre.set(z == null ? "" : z.nombre());
                simbolo.setText(z == null ? "(se genera al crear)" : z.simbolo());
                aviso.setText(z == null
                        ? "Nota: un lugar nuevo ocupa una de las ranuras libres del juego (quedan "
                        + DatosJuego.ranurasLibres(ctx.bd()) + " de " + DatosJuego.ranuras().size() + ")."
                        : existeEnElJuego(z.simbolo()) ? " " : "Aviso: este lugar no existe en el juego y se ignorará al jugar.");
                clase.set(z == null ? ClaseZona.RUTA : z.clase());
                descripcion.set(z == null ? "" : z.descripcion());
                tipos.set(z == null ? List.of() : z.tipos());
                anterior.set(z == null ? null : z.anterior());
                siguiente.set(z == null ? null : z.siguiente());
                min.set(z == null ? 2 : z.nivelMin());
                max.set(z == null ? 5 : z.nivelMax());
                pueblo.set(z == null ? null : z.puebloCercano());
                entrenadores.set(z == null ? List.of() : z.clasesEntrenador());
                casino.set(z != null && z.casino());
                sede.set(z != null && z.sedeGobierno());
                segunClase.run();
            }

            public DatosZona leer(DatosZona actual) {
                String n = requerido(nombre.get(), "Nombre");
                String sim = actual != null ? actual.simbolo() : DatosJuego.siguienteRanuraLibre(ctx.bd());
                if (sim == null)
                    throw new IllegalArgumentException("No quedan ranuras libres para lugares nuevos (el juego admite "
                            + DatosJuego.ranuras().size() + "). Borra alguna zona nueva antes.");
                ClaseZona c = clase.get();
                boolean ruta = c == ClaseZona.RUTA;
                return new DatosZona(sim, n, c, descripcion.get(), c == ClaseZona.INICIO ? List.of() : tipos.get(),
                        ruta ? min.get() : 0, ruta ? max.get() : 0, anterior.get(), siguiente.get(),
                        ruta ? pueblo.get() : null, ruta ? entrenadores.get() : List.of(),
                        c == ClaseZona.POBLACION && casino.get(), c == ClaseZona.POBLACION && sede.get());
            }
        };
        return new Registros<>(ctx, "Zona", () -> new ArrayList<>(ctx.bd().zonas().values()), DatosZona::simbolo,
                z -> z.nombre() + "  · " + z.clase().nombre().toLowerCase(Locale.ROOT), ed,
                z -> guardarZona(ctx, z), z -> borrarZona(ctx, z));
    }

    private static String nombreZona(Contexto ctx, String sim) {
        DatosZona z = sim == null ? null : ctx.bd().zona(sim);
        return z == null ? String.valueOf(sim) : z.nombre();
    }

    /** Guarda la zona y ofrece dejar el camino coherente en los dos sentidos (insertar en el camino). */
    static void guardarZona(Contexto ctx, DatosZona z) {
        ctx.bd().poner(z);
        DatosZona a = z.anterior() == null ? null : ctx.bd().zona(z.anterior());
        if (a != null && !z.simbolo().equals(a.siguiente())
                && ctx.confirmar("¿Hacer que " + a.nombre() + " tenga a " + z.nombre() + " como siguiente?\n(ahora: "
                + nombreZona(ctx, a.siguiente()) + ")"))
            ctx.bd().poner(a.conSiguiente(z.simbolo()));
        DatosZona s = z.siguiente() == null ? null : ctx.bd().zona(z.siguiente());
        if (s != null && !z.simbolo().equals(s.anterior())
                && ctx.confirmar("¿Hacer que " + s.nombre() + " tenga a " + z.nombre() + " como anterior?\n(ahora: "
                + nombreZona(ctx, s.anterior()) + ")"))
            ctx.bd().poner(s.conAnterior(z.simbolo()));
    }

    static void borrarZona(Contexto ctx, DatosZona z) {
        if (ctx.bd().gimnasios().values().stream().anyMatch(g -> g.ciudad().equals(z.simbolo()))) {
            ctx.error("No se puede borrar " + z.nombre() + ": tiene un gimnasio.\nMuévelo o bórralo antes.");
            return;
        }
        List<String> refs = Referencias.deZona(ctx.bd(), z.simbolo());
        if (!ctx.confirmar("¿Borrar la zona " + z.nombre() + "?" + (refs.isEmpty() ? "" : "\n\nSe usa en:\n" + Util.primeras(refs, 8))))
            return;
        DatosZona ant = z.anterior() == null ? null : ctx.bd().zona(z.anterior());
        DatosZona sig = z.siguiente() == null ? null : ctx.bd().zona(z.siguiente());
        ctx.bd().quitarZona(z.simbolo());
        boolean unir = ant != null && sig != null && z.simbolo().equals(ant.siguiente()) && z.simbolo().equals(sig.anterior())
                && ctx.confirmar("¿Unir " + ant.nombre() + " con " + sig.nombre() + " para no cortar el camino?");
        if (ant != null && z.simbolo().equals(ant.siguiente())) ctx.bd().poner(ant.conSiguiente(unir ? sig.simbolo() : null));
        if (sig != null && z.simbolo().equals(sig.anterior())) ctx.bd().poner(sig.conAnterior(unir ? ant.simbolo() : null));
        for (DatosZona o : new ArrayList<>(ctx.bd().zonas().values()))
            if (z.simbolo().equals(o.puebloCercano())) ctx.bd().poner(o.conPuebloCercano(null));
        ctx.cambiado();
    }

    // ================================================================== GIMNASIOS

    static Registros<DatosGimnasio> gimnasios(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<String> nombre = f.fila("Nombre", Campos.texto(22));
        JLabel simbolo = new JLabel(" ");
        f.fila("Símbolo", simbolo);
        Campo<String> ciudad = f.fila("Ciudad", Campos.referencia(() -> Opciones.poblaciones(ctx)));
        Campo<List<TipoPokemon>> tipos = f.fila("Tipos del equipo (el primero marcado es el principal)", Campos.tipos());
        Campo<String> lider = f.fila("Líder", Campos.texto(22));
        Campo<String> titulo = f.fila("Título del líder", Campos.texto(22));
        Campo<List<String>> miembros = f.fila("Miembros (uno por línea)", Campos.lineas(4));
        Campo<String> medalla = f.fila("Medalla", Campos.texto(22));
        Campo<Integer> dinero = f.fila("Recompensa (dinero)", Campos.entero(0, 1_000_000, 1000));
        Campo<String> mt = f.fila("MT de recompensa", Campos.referencia(() -> Opciones.movimientos(ctx)));
        Campo<Integer> nivel = f.fila("Nivel base", Campos.entero(1, 100, 15));
        Campo<Integer> equipo = f.fila("Pokémon del líder", Campos.entero(1, 6, 3));
        f.rellenar();
        Registros.Editor<DatosGimnasio> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosGimnasio g) {
                nombre.set(g == null ? "" : g.nombre());
                simbolo.setText(g == null ? "(se genera al crear)" : g.simbolo());
                ciudad.set(g == null ? null : g.ciudad());
                tipos.set(g == null ? List.of() : g.tiposEquipo());
                lider.set(g == null ? "" : g.lider());
                titulo.set(g == null ? "" : g.tituloLider());
                miembros.set(g == null ? List.of() : g.miembros());
                medalla.set(g == null ? "" : g.medalla());
                dinero.set(g == null ? 1000 : g.recompensaDinero());
                mt.set(g == null ? null : g.mtRecompensa());
                nivel.set(g == null ? 15 : g.nivelBase());
                equipo.set(g == null ? 3 : g.tamanoEquipo());
            }

            public DatosGimnasio leer(DatosGimnasio actual) {
                String n = requerido(nombre.get(), "Nombre");
                String sim = actual != null ? actual.simbolo()
                        : PuenteProyecto.unico(PuenteProyecto.simbolo(n), ctx.bd().gimnasios().keySet());
                if (ciudad.get() == null) throw new IllegalArgumentException("Elige la ciudad del gimnasio.");
                for (DatosGimnasio otro : ctx.bd().gimnasios().values())
                    if (!otro.simbolo().equals(sim) && otro.ciudad().equals(ciudad.get()))
                        throw new IllegalArgumentException("Ya hay un gimnasio en esa ciudad (" + otro.nombre() + ").");
                if (tipos.get().isEmpty()) throw new IllegalArgumentException("Marca al menos un tipo para el equipo.");
                return new DatosGimnasio(sim, n, ciudad.get(), tipos.get(), requerido(lider.get(), "Líder"), titulo.get(),
                        miembros.get(), medalla.get(), dinero.get(), mt.get(), nivel.get(), equipo.get());
            }
        };
        return new Registros<>(ctx, "Gimnasio", () -> new ArrayList<>(ctx.bd().gimnasios().values()),
                DatosGimnasio::simbolo, g -> g.nombre() + "  · " + nombreZona(ctx, g.ciudad()), ed,
                g -> ctx.bd().poner(g), g -> {
            if (!ctx.confirmar("¿Borrar el gimnasio " + g.nombre() + "?")) return;
            ctx.bd().quitarGimnasio(g.simbolo());
            ctx.cambiado();
        });
    }

    // ===================================================================== JEFES

    static Registros<DatosJefe> jefes(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<Integer> generacion = f.fila("Generación", Campos.entero(1, 99, 1));
        Campo<String> nombre = f.fila("Nombre", Campos.texto(22));
        Campo<String> cargo = f.fila("Cargo", Campos.texto(22));
        Campo<List<TipoPokemon>> tipos = f.fila("Tipos del equipo", Campos.tipos());
        Campo<Integer> nivel = f.fila("Nivel base", Campos.entero(1, 100, 20));
        Campo<String> derrota = f.fila("Frase al ser derrotado", Campos.area(2));
        Campo<List<String>> memes = f.fila("Frases en combate (una por línea)", Campos.lineas(6));
        f.rellenar();
        Registros.Editor<DatosJefe> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosJefe j) {
                generacion.set(j == null ? ctx.bd().jefes().keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1 : j.generacion());
                Util.habilitar(generacion, j == null);
                nombre.set(j == null ? "" : j.nombre());
                cargo.set(j == null ? "" : j.cargo());
                tipos.set(j == null ? List.of() : j.tipos());
                nivel.set(j == null ? 20 : j.nivelBase());
                derrota.set(j == null ? "" : j.fraseDerrota());
                memes.set(j == null ? List.of() : j.memes());
            }

            public DatosJefe leer(DatosJefe actual) {
                int gen = actual != null ? actual.generacion() : generacion.get();
                if (actual == null && ctx.bd().jefe(gen) != null) throw new IllegalArgumentException("Ya hay jefe para la generación " + gen + ".");
                return new DatosJefe(gen, requerido(nombre.get(), "Nombre"), cargo.get(), tipos.get(), nivel.get(),
                        derrota.get(), memes.get());
            }
        };
        return new Registros<>(ctx, "Jefe", () -> new ArrayList<>(ctx.bd().jefes().values()),
                j -> Integer.toString(j.generacion()), j -> "Gen " + j.generacion() + "  · " + j.nombre(), ed,
                j -> ctx.bd().poner(j), j -> {
            if (!ctx.confirmar("¿Borrar al jefe de la generación " + j.generacion() + "?")) return;
            ctx.bd().quitarJefe(j.generacion());
            ctx.cambiado();
        });
    }

    // ============================================================== GENERACIONES

    static Registros<DatosGeneracion> generaciones(Contexto ctx) {
        Formulario f = new Formulario();
        Campo<Integer> numero = f.fila("Número", Campos.entero(1, 99, 1));
        Campo<String> region = f.fila("Región", Campos.texto(22));
        Campo<Integer> desde = f.fila("Primer Pokémon (nº de Pokédex)", Campos.entero(1, 99999, 1));
        Campo<Integer> hasta = f.fila("Último Pokémon (nº de Pokédex)", Campos.entero(1, 99999, 1));
        Campo<String> ambiente = f.fila("Ambiente", Campos.area(3));
        Campo<Integer> bonus = f.fila("Bonus de nivel", Campos.entero(0, 100, 0));
        f.rellenar();
        Registros.Editor<DatosGeneracion> ed = new Registros.Editor<>() {
            public JComponent componente() { return f; }

            public void cargar(DatosGeneracion g) {
                int sig = ctx.bd().generaciones().keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
                numero.set(g == null ? sig : g.numero());
                Util.habilitar(numero, g == null);
                region.set(g == null ? "" : g.region());
                int ultimo = ctx.bd().generaciones().values().stream().mapToInt(DatosGeneracion::hasta).max().orElse(0);
                desde.set(g == null ? ultimo + 1 : g.desde());
                hasta.set(g == null ? ultimo + 1 : g.hasta());
                ambiente.set(g == null ? "" : g.ambiente());
                bonus.set(g == null ? (sig - 1) * 3 : g.bonusNivel());
            }

            public DatosGeneracion leer(DatosGeneracion actual) {
                int num = actual != null ? actual.numero() : numero.get();
                if (actual == null && ctx.bd().generacion(num) != null) throw new IllegalArgumentException("La generación " + num + " ya existe.");
                return new DatosGeneracion(num, requerido(region.get(), "Región"), desde.get(), hasta.get(), ambiente.get(), bonus.get());
            }
        };
        return new Registros<>(ctx, "Generación", () -> new ArrayList<>(ctx.bd().generaciones().values()),
                g -> Integer.toString(g.numero()), g -> "Gen " + g.numero() + "  · " + g.region(), ed,
                g -> ctx.bd().poner(g), g -> {
            if (!ctx.confirmar("¿Borrar la generación " + g.numero() + "?\n(su jefe quedará sin generación)")) return;
            ctx.bd().quitarGeneracion(g.numero());
            ctx.cambiado();
        });
    }
}
