package Proyecto.studio;

import Proyecto.Ataques;
import Proyecto.Generacion;
import Proyecto.Habilidades;
import Proyecto.ReglasCombate;
import Proyecto.EstadoAlterado;
import Proyecto.Efectos;
import Proyecto.Combate;
import Proyecto.Gimnasio;
import Proyecto.Gobierno;
import Proyecto.Jugador;
import Proyecto.Mapa;
import Proyecto.Pueblo;
import Proyecto.Ruta;
import Proyecto.Villaverde;
import Proyecto.Movimiento;
import Proyecto.Npc;
import Proyecto.PokemonLuchador;
import Proyecto.Tipos;
import Proyecto.ObjetoEquipable;
import Proyecto.Ubicacion;
import Proyecto.Pokedex;
import Proyecto.PokemonBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Punto de entrada de la Fase 1 (modelo + JSON). Uso:
 * <pre>
 *   java Proyecto.studio.Estudio importar [carpeta]   volca Pokedex/Ataques a JSON (por defecto data/studio)
 *   java Proyecto.studio.Estudio validar  [carpeta]   comprueba la coherencia de los JSON
 *   java Proyecto.studio.Estudio resumen  [carpeta]   cuenta registros

 *   java Proyecto.studio.Estudio editor   [carpeta]   editor de datos por consola (Fase 3)
 *   java Proyecto.studio.Estudio gui      [carpeta]   editor gráfico (Swing) (Fase 5)
 *   java Proyecto.studio.Estudio probar-gui          prueba la interfaz gráfica sin pantalla
 *   java Proyecto.studio.Estudio probar-juego        aplica datos al juego y comprueba que los usa (Fase 4)
 *   java Proyecto.studio.Estudio probar              importa -> guarda -> carga -> regenera y compara
 * </pre>
 */
public final class Estudio {
    private static final String CARPETA_POR_DEFECTO = "data/studio";

    private Estudio() {}

    public static void main(String[] args) throws IOException {
        String orden = args.length > 0 ? args[0] : "ayuda";
        Path carpeta = Path.of(args.length > 1 ? args[1] : CARPETA_POR_DEFECTO);
        switch (orden) {
            case "importar" -> importar(carpeta);
            case "validar" -> validar(carpeta);
            case "resumen" -> resumen(BaseDatos.cargar(carpeta));
            case "editor" -> EditorConsola.abrir(carpeta, new java.util.Scanner(System.in));
            case "gui" -> Proyecto.studio.gui.EditorGrafico.abrir(carpeta, true);
            case "mapas" -> Proyecto.studio.gui.EditorMapas.abrir();
            case "probar-gui" -> System.exit(Proyecto.studio.gui.PruebaGui.probar() ? 0 : 1);
            case "probar-juego" -> System.exit(probarJuego(Path.of(System.getProperty("java.io.tmpdir"), "studio-juego")) ? 0 : 1);
            case "probar" -> System.exit(probar(Path.of(System.getProperty("java.io.tmpdir"), "studio-prueba")) ? 0 : 1);
            default -> System.out.println("Órdenes: importar [carpeta] | validar [carpeta] | resumen [carpeta] | editor [carpeta] | gui [carpeta] | mapas | probar | probar-juego | probar-gui");
        }
    }

    private static void importar(Path carpeta) throws IOException {
        PuenteProyecto.Importacion imp = PuenteProyecto.importar();
        imp.base().guardar(carpeta);
        resumen(imp.base());
        System.out.println("Guardado en " + carpeta.toAbsolutePath());
        mostrar("Avisos de importación", imp.avisos());
    }

    private static void validar(Path carpeta) throws IOException {
        List<String> errores = BaseDatos.cargar(carpeta).validar();
        if (errores.isEmpty()) System.out.println("Todo correcto.");
        else mostrar("Problemas", errores);
    }

    private static void resumen(BaseDatos bd) {
        System.out.printf("%d especies, %d movimientos, %d habilidades, %d objetos%n",
                bd.especies().size(), bd.movimientos().size(), bd.habilidades().size(), bd.objetos().size());
        System.out.printf("%d zonas, %d gimnasios, %d jefes, %d generaciones%n",
                bd.zonas().size(), bd.gimnasios().size(), bd.jefes().size(), bd.generaciones().size());
    }

    private static void mostrar(String titulo, List<String> lineas) {
        System.out.println(titulo + " (" + lineas.size() + "):");
        lineas.stream().limit(15).forEach(l -> System.out.println("  - " + l));
        if (lineas.size() > 15) System.out.println("  ... y " + (lineas.size() - 15) + " más");
    }

    /** Importa, guarda, vuelve a cargar y regenera los PokemonBase comparándolos con los originales. */
    static boolean probar(Path carpeta) throws IOException {
        PuenteProyecto.Importacion imp = PuenteProyecto.importar();
        imp.base().guardar(carpeta);
        BaseDatos recargada = BaseDatos.cargar(carpeta);
        boolean ok = true;

        boolean igual = imp.base().especies().equals(recargada.especies())
                && imp.base().movimientos().equals(recargada.movimientos())
                && imp.base().objetos().equals(recargada.objetos())
                && imp.base().zonas().equals(recargada.zonas())
                && imp.base().gimnasios().equals(recargada.gimnasios())
                && imp.base().jefes().equals(recargada.jefes())
                && imp.base().generaciones().equals(recargada.generaciones())
                && imp.base().reglas().equals(recargada.reglas());
        System.out.println("JSON ida y vuelta sin pérdidas: " + (igual ? "OK" : "FALLO"));
        ok &= igual;

        List<String> errores = recargada.validar();
        System.out.println("Validación: " + (errores.isEmpty() ? "OK" : errores.size() + " problemas"));
        errores.stream().limit(10).forEach(e -> System.out.println("  - " + e));
        ok &= errores.isEmpty();

        List<PokemonBase> originales = Pokedex.getTodos();
        List<PokemonBase> regenerados = PuenteProyecto.aPokemonBase(recargada);
        int diferencias = 0;
        if (originales.size() != regenerados.size()) diferencias++;
        for (int i = 0; i < Math.min(originales.size(), regenerados.size()); i++) {
            if (!iguales(originales.get(i), regenerados.get(i))) {
                if (++diferencias <= 5) System.out.println("  Diferencia en " + originales.get(i).getNombre());
            }
        }
        System.out.println("PokemonBase regenerados (" + regenerados.size() + ") idénticos a Pokedex: "
                + (diferencias == 0 ? "OK" : diferencias + " diferencias"));
        ok &= diferencias == 0;

        double x = recargada.tipos().multiplicador(TipoPokemon.AGUA, TipoPokemon.FUEGO, TipoPokemon.ROCA);
        System.out.println("Agua contra Fuego/Roca = x" + x + (x == 4.0 ? " OK" : " FALLO"));
        ok &= x == 4.0;

        resumen(recargada);
        mostrar("Avisos de importación", imp.avisos());
        return ok;
    }

    private static boolean iguales(PokemonBase a, PokemonBase b) {
        if (!a.getNombre().equals(b.getNombre()) || !String.valueOf(a.getTipo1()).equals(String.valueOf(b.getTipo1()))
                || !String.valueOf(a.getTipo2()).equals(String.valueOf(b.getTipo2()))
                || a.getVidaBase() != b.getVidaBase() || a.getAtaqueBase() != b.getAtaqueBase()
                || a.getDefensaBase() != b.getDefensaBase() || a.getVelocidadBase() != b.getVelocidadBase()
                || (a.getEvolucion() != null && a.getNivelEvolucion() != b.getNivelEvolucion())
                || !String.valueOf(a.getEvolucion()).equals(String.valueOf(b.getEvolucion()))) return false;
        List<Movimiento> ma = a.getMovimientosPorNivel().stream().filter(java.util.Objects::nonNull).toList();
        List<Movimiento> mb = b.getMovimientosPorNivel();
        if (ma.size() != mb.size()) return false;
        for (int i = 0; i < ma.size(); i++) {
            if (!ma.get(i).getNombre().equals(mb.get(i).getNombre())
                    || ma.get(i).getPotencia() != mb.get(i).getPotencia()
                    || ma.get(i).getNivelRequerido() != mb.get(i).getNivelRequerido()
                    || TipoPokemon.buscar(ma.get(i).getTipo()).orElse(null) != TipoPokemon.buscar(mb.get(i).getTipo()).orElse(null)) return false;
        }
        return true;
    }

    // ------------------------------------------------------------- prueba de la Fase 4

    private static boolean todoOk = true;

    private static void chk(String descripcion, boolean ok) {
        System.out.println((ok ? "  OK     " : "  FALLO  ") + descripcion);
        todoOk &= ok;
    }

    /** Aplica datos al juego (modificados a propósito) y comprueba que las clases del juego los reflejan. */
    static boolean probarJuego(Path carpeta) throws IOException {
        todoOk = true;
        PuenteProyecto.Importacion base = PuenteProyecto.importar();
        BaseDatos original = base.base();

        System.out.println("1) Aplicar los datos originales no cambia nada (importar -> aplicar -> importar):");
        List<String> avisos = DatosJuego.aplicar(original);
        BaseDatos otra = PuenteProyecto.importar().base();
        chk("especies iguales", original.especies().equals(otra.especies()));
        chk("movimientos iguales", original.movimientos().equals(otra.movimientos()));
        chk("zonas iguales", original.zonas().equals(otra.zonas()));
        chk("gimnasios iguales", original.gimnasios().equals(otra.gimnasios()));
        chk("jefes y generaciones iguales", original.jefes().equals(otra.jefes()) && original.generaciones().equals(otra.generaciones()));
        chk("objetos y reglas iguales", original.objetos().equals(otra.objetos()) && original.reglas().equals(otra.reglas()));
        chk("sin avisos al aplicar", avisos.isEmpty());

        System.out.println("2) Cambios hechos en los datos se ven en el juego:");
        original.guardar(carpeta);
        BaseDatos bd = BaseDatos.cargar(carpeta);   // copia profunda vía JSON
        DatosEspecie pikachu = bd.especie("pikachu");
        java.util.Map<Estadistica, Integer> stats = new java.util.EnumMap<>(Estadistica.class);
        stats.putAll(pikachu.forma(0).statsBase());
        stats.put(Estadistica.VIDA, 99);
        bd.poner(pikachu.conFormas(List.of(pikachu.forma(0).conStats(stats))));
        DatosMovimiento placaje = bd.movimiento("placaje");
        bd.poner(new DatosMovimiento(placaje.id(), placaje.simbolo(), "Placaje Plus", placaje.tipo(), placaje.categoria(),
                placaje.potencia(), placaje.precision(), placaje.pp(), placaje.prioridad(), placaje.efecto(), placaje.banderas()));
        bd.poner(new DatosEspecie(1026, "testmon", "Testmon", List.of(DatosForma.porDefecto(TipoPokemon.HADA, null)
                .conPorNivel(List.of(new Aprendizaje("placaje", 1))))));
        DatosGeneracion g10 = bd.generacion(10);
        bd.poner(new DatosGeneracion(10, "Mezcla Total", g10.desde(), 1026, g10.ambiente(), g10.bonusNivel()));
        DatosGimnasio gim = bd.gimnasio("gimnasio_penascal");
        bd.poner(new DatosGimnasio(gim.simbolo(), gim.nombre(), gim.ciudad(), gim.tiposEquipo(), "Lider Nuevo",
                gim.tituloLider(), gim.miembros(), gim.medalla(), gim.recompensaDinero(), gim.mtRecompensa(),
                gim.nivelBase(), gim.tamanoEquipo()));
        DatosZona r1 = bd.zona("ruta1");
        bd.poner(new DatosZona(r1.simbolo(), "Ruta Uno", r1.clase(), r1.descripcion(), r1.tipos(), r1.nivelMin(), 9,
                r1.anterior(), r1.siguiente(), r1.puebloCercano(), r1.clasesEntrenador(), false, false));
        DatosObjeto restos = bd.objeto("restos");
        bd.poner(new DatosObjeto(restos.id(), restos.simbolo(), restos.nombre(), restos.bolsillo(), 777,
                restos.descripcion(), restos.banderas()));
        DatosReglas rg = bd.reglas();
        bd.reglas(new DatosReglas(0.5, List.of("Zoe"), rg.frasesReto(), rg.frasesDerrota(), rg.premioNpcPorNivel(),
                rg.miembrosPorGimnasio(), rg.pokemonPorMiembro(), rg.tamanoEquipoJefe(), rg.recompensaJefe(), rg.excluidosSalvajes()));
        DatosJefe j1 = bd.jefe(1);
        bd.poner(new DatosJefe(1, "Jefe Nuevo", j1.cargo(), j1.tipos(), j1.nivelBase(), j1.fraseDerrota(), j1.memes()));

        avisos = DatosJuego.aplicar(bd);
        chk("Pokedex: Pikachu tiene 99 de vida", Pokedex.getByName("Pikachu").getVidaBase() == 99);
        chk("Ataques: existe 'Placaje Plus' y ya no 'Placaje'", Ataques.buscarPorNombre("Placaje Plus") != null
                && Ataques.buscarPorNombre("Placaje") == null);
        chk("Pokedex: los Pokémon aprenden 'Placaje Plus'", Pokedex.getTodos().stream().anyMatch(p ->
                p.getMovimientosPorNivel().stream().anyMatch(m -> m != null && m.getNombre().equals("Placaje Plus"))));
        chk("Pokedex: hay 1026 Pokémon y existe Testmon (Hada)", Pokedex.getTodos().size() == 1026
                && Pokedex.getByName("Testmon") != null && "Fairy".equals(Pokedex.getByName("Testmon").getTipo1()));
        chk("Generacion 10: región nueva e incluye a Testmon", Generacion.GEN10.getRegion().equals("Mezcla Total")
                && Generacion.GEN10.getPokemons().stream().anyMatch(p -> p.getNombre().equals("Testmon")));
        chk("Generacion 1 sigue siendo Kanto con 151 Pokémon menos excluidos",
                Generacion.GEN1.getRegion().equals("Kanto") && Generacion.GEN1.getPokemons().size() > 100
                        && Generacion.GEN1.getPokemons().stream().noneMatch(p -> p.getNombre().equals("Testmon")));
        chk("Gimnasio de Oviedo: líder 'Lider Nuevo'", Gimnasio.deCiudad(Ubicacion.OVIEDO).getLider().equals("Lider Nuevo"));
        chk("Ruta 1: nivel máximo 9 y nombre 'Ruta Uno'", Mapa.zonaDe(Ubicacion.RUTA1).getNivelMax() == 9
                && Ubicacion.RUTA1.getNombre().equals("Ruta Uno"));
        chk("Camino intacto: 23 lugares, empieza en Villaverde", Mapa.getRecorrido().size() == 23
                && Mapa.getRecorrido().get(0) == Ubicacion.VILLAVERDE);
        chk("Restos cuesta 777", ObjetoEquipable.RESTOS.getPrecio() == 777);
        chk("NPC: probabilidad 0.5 y un solo nombre", Npc.getProbabilidad() == 0.5 && Npc.getNombres().equals(List.of("Zoe")));
        chk("Gobierno: jefe de la gen 1 es 'Jefe Nuevo'", Gobierno.getJefe(Generacion.GEN1).getNombre().equals("Jefe Nuevo"));
        chk("Testmon aprende 'Placaje Plus' al nivel 1", Pokedex.getByName("Testmon").getMovimientosPorNivel().get(0)
                .getNombre().equals("Placaje Plus"));
        chk("sin avisos", avisos.isEmpty());

        System.out.println("3) Una base inválida se rechaza y el juego no cambia:");
        DatosEspecie roto = bd.especie("bulbasaur");
        bd.poner(roto.conFormas(List.of(roto.forma(0).conEvoluciones(List.of(DatosEvolucion.porNivel("no_existe", 5))))));
        boolean rechazada = false;
        try {
            DatosJuego.aplicar(bd);
        } catch (IllegalStateException e) {
            rechazada = true;
            System.out.println("         (" + e.getMessage() + ")");
        }
        chk("aplicar lanza IllegalStateException", rechazada);
        chk("el juego sigue con los datos anteriores (Pikachu 99, 1026 Pokémon)",
                Pokedex.getByName("Pikachu").getVidaBase() == 99 && Pokedex.getTodos().size() == 1026);

        System.out.println("4) Zonas que el juego no puede alojar se avisan, no rompen:");
        BaseDatos bd2 = BaseDatos.cargar(carpeta);
        DatosZona ruta7 = bd2.zona("ruta7");
        bd2.poner(new DatosZona("ruta_nueva", "Ruta Nueva", ClaseZona.RUTA, "x", List.of(TipoPokemon.AGUA), 5, 8,
                "ruta7", "aldea_sombria", "aldea_sombria", List.of(), false, false));
        bd2.poner(ruta7.conSiguiente("ruta_nueva"));
        bd2.poner(bd2.zona("aldea_sombria").conAnterior("ruta_nueva"));
        List<String> av2 = DatosJuego.aplicar(bd2);
        av2.forEach(a -> System.out.println("         aviso: " + a));
        chk("hay avisos de lugar desconocido y de camino interrumpido", av2.stream().anyMatch(a -> a.contains("ruta_nueva"))
                && av2.stream().anyMatch(a -> a.contains("camino se interrumpe")));

        System.out.println("5) Volver a aplicar los datos originales restaura el juego:");
        DatosJuego.aplicar(original);
        chk("Pikachu vuelve a su vida original", Pokedex.getByName("Pikachu").getVidaBase() == original.especie("pikachu").forma(0).stat(Estadistica.VIDA));
        chk("Pokedex vuelve a 1025", Pokedex.getTodos().size() == 1025);
        chk("líder de Oviedo restaurado", !Gimnasio.deCiudad(Ubicacion.OVIEDO).getLider().equals("Lider Nuevo"));
        chk("Ruta 1 restaurada", Ubicacion.RUTA1.getNombre().equals(original.zona("ruta1").nombre()));
        System.out.println("6) Eficacia de tipos en combate (regla opcional, desactivada por defecto):");
        PokemonBase squirtle = Pokedex.getByName("Squirtle"), charmander = Pokedex.getByName("Charmander"), gastly = Pokedex.getByName("Gastly");
        Movimiento chorro = new Movimiento("Chorro", 10.0, "Water", 0), golpe = new Movimiento("Golpe", 10.0, "Normal", 0);
        chk("por defecto está desactivada", !Tipos.activa());
        chk("sin la regla, un ataque quita solo su potencia (10) aunque sea de tipo eficaz", perdida(squirtle, charmander, chorro) == 10);
        BaseDatos con = BaseDatos.cargar(carpeta);
        DatosReglas rg6 = con.reglas();
        con.reglas(new DatosReglas(rg6.probabilidadNpc(), rg6.nombresNpc(), rg6.frasesReto(), rg6.frasesDerrota(),
                rg6.premioNpcPorNivel(), rg6.miembrosPorGimnasio(), rg6.pokemonPorMiembro(), rg6.tamanoEquipoJefe(),
                rg6.recompensaJefe(), rg6.excluidosSalvajes(), rg6.combate().conEficaciaTipos(true)));
        DatosJuego.aplicar(con);
        chk("con la regla activada, Tipos.activa()", Tipos.activa());
        chk("Agua contra Fuego es muy eficaz: 10 -> 20", perdida(squirtle, charmander, chorro) == 20);
        chk("Agua contra Agua es poco eficaz: 10 -> 5", perdida(squirtle, squirtle, chorro) == 5);
        chk("Normal contra Fantasma no afecta: 0", perdida(squirtle, gastly, golpe) == 0);
        chk("Tipos.multiplicador usa la tabla de dos tipos (Agua contra Fuego/Roca = x4)", Tipos.multiplicador("Water", "Fire", "Rock") == 4.0);
        con.tipos().establecer(TipoPokemon.AGUA, TipoPokemon.FUEGO, 0.0);
        DatosJuego.aplicar(con);
        chk("una tabla de tipos editada se respeta (Agua contra Fuego = 0)", perdida(squirtle, charmander, chorro) == 0);
        Path tmp6 = Files.createTempDirectory("studio-eficacia");
        con.guardar(tmp6);
        chk("la regla se guarda y se carga del JSON", BaseDatos.cargar(tmp6).reglas().eficaciaTipos());
        DatosJuego.aplicar(original);
        chk("volver a los datos originales la desactiva", !Tipos.activa() && perdida(squirtle, charmander, chorro) == 10);

        System.out.println("7) Lugares nuevos en las ranuras libres, y partidas guardadas:");
        Path fixture = Path.of("data/pruebas/partida_original.bin");
        if (Files.exists(fixture)) {
            try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(Files.newInputStream(fixture))) {
                Jugador antigua = (Jugador) in.readObject();
                chk("una partida guardada con el código ORIGINAL carga con el enum ampliado (Ruta 7)",
                        antigua.getUbicacion() == Ubicacion.RUTA7 && antigua.getEquipo().size() == 1);
            } catch (Exception e) {
                chk("carga de la partida original: " + e, false);
            }
        } else {
            System.out.println("  (no está data/pruebas/partida_original.bin: se omite la prueba de partida antigua)");
        }
        BaseDatos nuevo = BaseDatos.cargar(carpeta);
        chk("hay 24 ranuras y todas están libres", DatosJuego.ranuras().size() == 24 && DatosJuego.ranurasLibres(nuevo) == 24);
        // Nava -> Ruta Nueva (extra1) -> Pueblo Nuevo (extra2, con gimnasio) -> Ruta 11
        nuevo.poner(new DatosZona("extra1", "Ruta Nueva", ClaseZona.RUTA, "Un camino recién abierto.", List.of(TipoPokemon.AGUA),
                4, 6, "nava", "extra2", "nava", List.of("Pescador"), false, false));
        nuevo.poner(new DatosZona("extra2", "Pueblo Nuevo", ClaseZona.POBLACION, "Un pueblo recién fundado.", List.of(TipoPokemon.HIELO),
                0, 0, "extra1", "ruta11", null, List.of(), false, false));
        nuevo.poner(nuevo.zona("nava").conSiguiente("extra1"));
        nuevo.poner(nuevo.zona("ruta11").conAnterior("extra2"));
        nuevo.poner(new DatosGimnasio("gimnasio_nuevo", "Gimnasio Nuevo", "extra2", List.of(TipoPokemon.HIELO), "Nieves",
                "la reina del hielo", List.of("A", "B", "C"), "Medalla Nieve", 900, "placaje", 12, 3));
        chk("la base con lugares nuevos es válida", nuevo.validar().isEmpty());
        List<String> avisosNuevos = DatosJuego.aplicar(nuevo);
        avisosNuevos.forEach(a -> System.out.println("         aviso: " + a));
        chk("se aplican sin avisos", avisosNuevos.isEmpty());
        List<Ubicacion> camino = Mapa.getRecorrido();
        int iNava = camino.indexOf(Ubicacion.NAVA);
        chk("el camino tiene 25 lugares y pasa por los nuevos en su sitio (Nava, Ruta Nueva, Pueblo Nuevo, Ruta 11)",
                camino.size() == 25 && camino.get(iNava + 1) == Ubicacion.EXTRA1 && camino.get(iNava + 2) == Ubicacion.EXTRA2
                        && camino.get(iNava + 3) == Ubicacion.RUTA11);
        chk("los nombres nuevos se usan en el juego", Ubicacion.EXTRA1.getNombre().equals("Ruta Nueva")
                && Ubicacion.EXTRA2.getNombre().equals("Pueblo Nuevo"));
        chk("la ruta nueva es una ruta y el pueblo nuevo un pueblo", Mapa.zonaDe(Ubicacion.EXTRA1) != null
                && Mapa.puebloDe(Ubicacion.EXTRA2) != null && Mapa.zonaDe(Ubicacion.EXTRA2) == null);
        chk("el gimnasio nuevo está en el pueblo nuevo (6 gimnasios) y el menú de vuelo lo anuncia",
                Gimnasio.deCiudad(Ubicacion.EXTRA2) != null && Gimnasio.getGimnasios().size() == 6
                        && Mapa.descripcionCorta(Ubicacion.EXTRA2).contains("gimnasio de"));

        Jugador j = new Jugador("Prueba", new PokemonLuchador(Pokedex.getByName("Squirtle"), 5), 100);
        // Pueblo Nava: 1 Centro, 2 Tienda, 3 Vecinos, 4 Menú, 5 Ir a (siguiente), 6 Volver
        chk("desde Nava se puede ir a la ruta nueva", Pueblo.visitar(new java.util.Scanner("5\n"), j, Mapa.puebloDe(Ubicacion.NAVA)) == Ubicacion.EXTRA1);
        j.getEquipo().forEach(p -> p.recibirDaño(1e9));
        chk("al caer en la ruta nueva despiertas en su pueblo cercano (Nava)",
                Ruta.explorar(new java.util.Scanner(""), j, Mapa.zonaDe(Ubicacion.EXTRA1)) == Ubicacion.NAVA);
        // Pueblo Nuevo: 1 Centro, 2 Tienda, 3 Gimnasio, 4 Menú, 5 Ir a (Ruta 11), 6 Volver
        chk("desde el pueblo nuevo se sigue a la Ruta 11",
                Pueblo.visitar(new java.util.Scanner("5\n"), j, Mapa.puebloDe(Ubicacion.EXTRA2)) == Ubicacion.RUTA11);
        chk("y se vuelve a la ruta nueva",
                Pueblo.visitar(new java.util.Scanner("6\n"), j, Mapa.puebloDe(Ubicacion.EXTRA2)) == Ubicacion.EXTRA1);
        j.setUbicacion(Ubicacion.EXTRA2);
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            try (java.io.ObjectOutputStream o = new java.io.ObjectOutputStream(bos)) { o.writeObject(j); }
            try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bos.toByteArray()))) {
                chk("una partida guardada en un lugar nuevo se recupera tal cual", ((Jugador) in.readObject()).getUbicacion() == Ubicacion.EXTRA2);
            }
        } catch (Exception e) {
            chk("guardar y cargar en un lugar nuevo: " + e, false);
        }
        chk("siguiente ranura libre = extra3 y quedan 22", "extra3".equals(DatosJuego.siguienteRanuraLibre(nuevo)) && DatosJuego.ranurasLibres(nuevo) == 22);

        // La salida de Villaverde sigue el camino de los datos (antes estaba fija en la Ruta 1)
        BaseDatos inicio = BaseDatos.cargar(carpeta);
        inicio.poner(new DatosZona("extra1", "Senda Inicial", ClaseZona.RUTA, "Primer tramo.", List.of(TipoPokemon.NORMAL), 2, 3,
                "villaverde", "ruta1", "villaverde", List.of(), false, false));
        inicio.poner(inicio.zona("villaverde").conSiguiente("extra1"));
        inicio.poner(inicio.zona("ruta1").conAnterior("extra1"));
        DatosJuego.aplicar(inicio);
        chk("Villaverde lleva a la primera ruta del camino (Senda Inicial), no siempre a la Ruta 1",
                Villaverde.mostrarMenu(new java.util.Scanner("2\n"), j) == Ubicacion.EXTRA1);

        // Un lugar nuevo sin zona anterior se detecta (el juego siempre ofrece "Volver")
        BaseDatos sinAnterior = BaseDatos.cargar(carpeta);
        sinAnterior.poner(new DatosZona("extra1", "Ruta Suelta", ClaseZona.RUTA, "", List.of(TipoPokemon.AGUA), 2, 3,
                null, null, "nava", List.of(), false, false));
        chk("el validador exige zona anterior a las rutas y poblaciones",
                sinAnterior.validar().stream().anyMatch(e -> e.contains("necesita una zona anterior")));

        DatosJuego.aplicar(original);
        chk("al volver a los datos originales desaparecen los lugares nuevos y sus ranuras quedan libres",
                Mapa.getRecorrido().size() == 23 && Gimnasio.getGimnasios().size() == 5 && Mapa.zonaDe(Ubicacion.EXTRA1) == null
                        && Ubicacion.EXTRA1.getNombre().equals("Lugar libre 1") && Ubicacion.EXTRA2.getNombre().equals("Lugar libre 2"));

        System.out.println("8) Reglas de combate opcionales (efectos, precisión, PP, estadísticas, habilidades):");
        ReglasCombate.azar(new java.util.Random(12345));
        ReglasCombate.configurar(false, false, false, false, false);
        PokemonBase pa = baseTrucada("Atacante", 100, 45, 45), pd = baseTrucada("Defensor", 195, 45, 45);

        // ---- 8.0 cableado: reglas.json -> ReglasCombate
        BaseDatos cb = BaseDatos.cargar(carpeta);
        cb.reglas(cb.reglas().conCombate(new DatosCombate(false, true, true, true, true, true)));
        DatosJuego.aplicar(cb);
        chk("las 5 reglas de combate se activan desde los datos y la de tipos sigue aparte",
                ReglasCombate.efectos() && ReglasCombate.precision() && ReglasCombate.pp() && ReglasCombate.estadisticas()
                        && ReglasCombate.habilidades() && !Tipos.activa());
        Path tmp8 = Files.createTempDirectory("studio-combate");
        cb.guardar(tmp8);
        chk("las reglas se guardan y se cargan del JSON", BaseDatos.cargar(tmp8).reglas().combate().equals(cb.reglas().combate()));
        chk("el JSON usa claves legibles", Files.readString(tmp8.resolve("reglas.json")).contains("\"estadisticas_en_dano\": true"));
        ReglasCombate.configurar(false, false, false, false, false);
        chk("por defecto (datos originales) todas están apagadas", !DatosCombate.NINGUNO.alguna() && !ReglasCombate.pp());

        // ---- 8.1 efectos de los movimientos
        Movimiento sueno = new Movimiento("Sueño", 0, "Psychic", 0, new Efectos("Sueño", 0, EstadoAlterado.DORMIDO, 100));
        PokemonLuchador d1 = luchador(pd);
        luchador(pa).usarMovimiento(sueno, 0, d1);
        chk("sin la regla, un movimiento con estado no hace nada (como siempre)", d1.getEstado() == EstadoAlterado.NORMAL);
        ReglasCombate.configurar(true, false, false, false, false);   // solo efectos
        PokemonLuchador d2 = luchador(pd);
        luchador(pa).usarMovimiento(sueno, 0, d2);
        chk("con la regla, el movimiento deja dormido 2-3 turnos", d2.getEstado() == EstadoAlterado.DORMIDO
                && d2.getTurnosDormido() >= 2 && d2.getTurnosDormido() <= 3);
        PokemonLuchador d3 = luchador(pd);
        d3.setEstado(EstadoAlterado.QUEMADO);
        luchador(pa).usarMovimiento(sueno, 0, d3);
        chk("un Pokémon que ya tiene un estado no recibe otro", d3.getEstado() == EstadoAlterado.QUEMADO);
        Movimiento paraliza = new Movimiento("Golpe trueno", 10, "Electric", 0, new Efectos("Parálisis", 0, EstadoAlterado.PARALIZADO, 100));
        PokemonLuchador d4 = luchador(pd);
        luchador(pa).usarMovimiento(paraliza, 10, d4);
        chk("un ataque con daño y estado hace las dos cosas (10 de daño + paralizado)", d4.getEstado() == EstadoAlterado.PARALIZADO && d4.getVidaMax() - d4.getVidaActual() == 10);
        Movimiento ko = new Movimiento("Guillotina", 1, "Normal", 0, new Efectos("K.O.", 1e10, EstadoAlterado.INSTA, 100));
        PokemonLuchador d5 = luchador(pd);
        luchador(pa).usarMovimiento(ko, 1, d5);
        chk("un efecto INSTA deja K.O. al objetivo", !d5.estaVivo());
        Movimiento treinta = new Movimiento("Veneno 30", 0, "Poison", 0, new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 30));
        int envenenados = 0;
        for (int i = 0; i < 2000; i++) {
            PokemonLuchador d = luchador(pd);
            silencio(() -> luchador(pa).usarMovimiento(treinta, 0, d));
            if (d.getEstado() == EstadoAlterado.ENVENENADO) envenenados++;
        }
        chk("una probabilidad del 30% se cumple (" + envenenados + " de 2000, esperado ~600)", envenenados > 500 && envenenados < 700);

        // ---- 8.2 precisión
        Movimiento imprecisa = new Movimiento("Imprecisa", 10, "Normal", 0);
        imprecisa.configurar("physical", 50, 20, 0);
        ReglasCombate.configurar(false, false, false, false, false);
        int aciertosApagada = 0;
        for (int i = 0; i < 500; i++) { PokemonLuchador d = luchador(pd); silencio(() -> luchador(pa).usarMovimiento(imprecisa, 10, d)); if (d.getVidaActual() < d.getVidaMax()) aciertosApagada++; }
        ReglasCombate.configurar(false, true, false, false, false);   // solo precisión
        int aciertos = 0, ciento = 0;
        for (int i = 0; i < 2000; i++) { PokemonLuchador d = luchador(pd); silencio(() -> luchador(pa).usarMovimiento(imprecisa, 10, d)); if (d.getVidaActual() < d.getVidaMax()) aciertos++; }
        Movimiento exacta = new Movimiento("Exacta", 10, "Normal", 0);
        Movimiento nuncaFalla = new Movimiento("NuncaFalla", 10, "Normal", 0);
        nuncaFalla.configurar("physical", 0, 20, 0);
        for (int i = 0; i < 300; i++) { PokemonLuchador d = luchador(pd), e = luchador(pd); luchador(pa).usarMovimiento(exacta, 10, d); luchador(pa).usarMovimiento(nuncaFalla, 10, e); if (d.getVidaActual() < d.getVidaMax() && e.getVidaActual() < e.getVidaMax()) ciento++; }
        chk("sin la regla, la precisión se ignora (500 de 500 aciertan)", aciertosApagada == 500);
        chk("con precisión 50 acierta la mitad (" + aciertos + " de 2000)", aciertos > 900 && aciertos < 1100);
        chk("precisión 100 y precisión 0 (= nunca falla) no fallan jamás", ciento == 300);
        String fallo = capturar(() -> { ReglasCombate.azar(new java.util.Random(1)); for (int i = 0; i < 20; i++) luchador(pa).usarMovimiento(imprecisa, 10, luchador(pd)); });
        chk("al fallar muestra el mensaje", fallo.contains("falla el ataque"));

        // ---- 8.3 PP
        Movimiento golpe3 = new Movimiento("Golpe3", 10, "Normal", 0);
        golpe3.configurar("physical", 100, 3, 0);
        PokemonBase solo = baseTrucada("Solo", 95, 45, 45);
        solo = new PokemonBase("Solo", "Normal", null, 95, 45, 45, 45, 0, null, List.of(golpe3));
        ReglasCombate.configurar(false, false, false, false, false);
        PokemonLuchador p0 = luchador(solo);
        for (int i = 0; i < 5; i++) silencio(() -> p0.usarMovimiento(golpe3, 10, luchador(pd)));
        chk("sin la regla los PP no existen: siempre se puede usar y nunca hay Forcejeo", p0.tienePp(golpe3) && !p0.sinPp());
        ReglasCombate.configurar(false, false, true, false, false);   // solo PP
        PokemonLuchador p1 = luchador(solo);
        chk("un movimiento con PP 3 empieza con 3", p1.ppRestantes(golpe3) == 3 && p1.tienePp(golpe3));
        for (int i = 0; i < 3; i++) silencio(() -> p1.usarMovimiento(golpe3, 10, luchador(pd)));
        chk("tras usarlo 3 veces no quedan PP y el Pokémon se queda sin movimientos", p1.ppRestantes(golpe3) == 0 && !p1.tienePp(golpe3) && p1.sinPp());
        p1.curarTotal();
        chk("el descanso (Centro Pokémon) restaura los PP", p1.ppRestantes(golpe3) == 3 && !p1.sinPp());
        Movimiento ilimitado = new Movimiento("Ilimitado", 10, "Normal", 0);
        ilimitado.configurar("physical", 100, 0, 0);
        PokemonLuchador p2 = luchador(new PokemonBase("Ilim", "Normal", null, 95, 45, 45, 45, 0, null, List.of(ilimitado)));
        for (int i = 0; i < 100; i++) silencio(() -> p2.usarMovimiento(ilimitado, 10, luchador(pd)));
        chk("PP 0 significa sin límite", p2.tienePp(ilimitado) && !p2.sinPp());

        // el menú del jugador: muestra PP, consume y pasa a Forcejeo cuando se agotan
        PokemonLuchador p3 = luchador(new PokemonBase("Solo", "Normal", null, 95, 45, 45, 45, 0, null, List.of(golpe3)));
        PokemonLuchador rival = luchador(pd);
        java.util.Scanner tres = new java.util.Scanner("1\n1\n1\n1\n");
        String menu = capturar(() -> Combate.turnoJugador(p3, rival, tres));
        chk("el menú muestra los PP del movimiento (PP 3/3)", menu.contains("PP 3/3"));
        chk("usarlo gasta un PP", p3.ppRestantes(golpe3) == 2);
        capturar(() -> Combate.turnoJugador(p3, rival, tres));
        capturar(() -> Combate.turnoJugador(p3, rival, tres));
        int vidaAntes = p3.getVidaActual();
        String forcejeo = capturar(() -> Combate.turnoJugador(p3, rival, tres));
        chk("sin PP el turno usa Forcejeo automáticamente, sin pedir elección y con aviso", p3.sinPp()
                && forcejeo.contains("no le quedan PP") && forcejeo.contains("usa Forcejeo"));
        chk("Forcejeo tiene retroceso: pierde 1/4 de su vida máxima", vidaAntes - p3.getVidaActual() == Math.max(1, p3.getVidaMax() / 4));
        PokemonLuchador agotado = luchador(new PokemonBase("Solo", "Normal", null, 95, 45, 45, 45, 0, null, List.of(golpe3)));
        for (int i = 0; i < 3; i++) silencio(() -> agotado.usarMovimiento(golpe3, 10, luchador(pd)));
        String ia = capturar(() -> Combate.turnoRival(luchador(pd), agotado));
        chk("el rival controlado por el juego también usa Forcejeo al agotarse", ia.contains("usa Forcejeo"));

        // ---- 8.4 daño por categoría y estadísticas (calibrado: multiplica por ataque/defensa, entre x0.5 y x2)
        Movimiento fis = new Movimiento("Fis", 10, "Normal", 0);
        Movimiento esp = new Movimiento("Esp", 10, "Normal", 0);
        esp.configurar("special", 100, 20, 0);
        Movimiento est = new Movimiento("Est", 0, "Normal", 0);
        PokemonBase fuerte = baseTrucada("Fuerte", 100, 95, 45), debil = baseTrucada("Debil", 100, 45, 45), blindado = baseTrucada("Blindado", 195, 45, 195);
        PokemonBase espFuerte = baseTrucada("EspFuerte", 100, 45, 45);
        espFuerte.configurarCombate(95, 45, List.of());
        PokemonBase espBlindado = baseTrucada("EspBlindado", 195, 45, 45);
        espBlindado.configurarCombate(45, 45, List.of());
        ReglasCombate.configurar(false, false, false, false, false);
        chk("sin la regla el daño es la potencia pase lo que pase (10 y 10)",
                danio(luchador(fuerte), fis, 10, luchador(pd)) == 10 && danio(luchador(espFuerte), esp, 10, luchador(espBlindado)) == 10);
        ReglasCombate.configurar(false, false, false, true, false);   // solo estadísticas
        chk("ataque 100 contra defensa 50 (x2): 10 -> 20", danio(luchador(fuerte), fis, 10, luchador(pd)) == 20);
        chk("ataque igual que defensa (x1): 10 -> 10", danio(luchador(debil), fis, 10, luchador(pd)) == 10);
        chk("ataque 50 contra defensa 200 se acota a x0.5: 10 -> 5", danio(luchador(debil), fis, 10, luchador(blindado)) == 5);
        chk("un movimiento especial usa el ataque especial: 10 -> 20 (el físico del mismo Pokémon daría 10)",
                danio(luchador(espFuerte), esp, 10, luchador(espBlindado)) == 20 && danio(luchador(espFuerte), fis, 10, luchador(espBlindado)) == 10);
        chk("sin dato de ataque/defensa especial se usa el físico (x1): 10 -> 10", danio(luchador(debil), esp, 10, luchador(pd)) == 10);
        chk("un movimiento de estado (potencia 0) no hace daño", danio(luchador(fuerte), est, 0, luchador(pd)) == 0);

        // ---- 8.5 habilidades (definidas en los datos y aplicadas por DatosJuego)
        BaseDatos hb = BaseDatos.cargar(carpeta);
        hb.poner(new DatosHabilidad("mar_llamas", "Mar Llamas", "Potencia el fuego con poca vida.", "potencia_tipo", TipoPokemon.FUEGO, null, 1.5));
        hb.poner(new DatosHabilidad("levitacion", "Levitación", "Inmune a Tierra.", "inmunidad_tipo", TipoPokemon.TIERRA, null, 0));
        hb.poner(new DatosHabilidad("estatica", "Estática", "Puede paralizar al contacto.", "contacto_estado", null, "PARALIZADO", 100));
        hb.poner(new DatosHabilidad("regenerador", "Regenerador", "Recupera vida cada turno.", "regeneracion", null, null, 16));
        String[][] especiesHab = {{"hab_llamas", "HabLlamas", "mar_llamas"}, {"hab_levitacion", "HabLevitacion", "levitacion"},
                {"hab_estatica", "HabEstatica", "estatica"}, {"hab_regenerador", "HabRegenerador", "regenerador"}};
        for (int i = 0; i < especiesHab.length; i++)
            hb.poner(new DatosEspecie(1026 + i, especiesHab[i][0], especiesHab[i][1], List.of(DatosForma.porDefecto(TipoPokemon.NORMAL, null)
                    .conStats(stats(195, 45, 45)).conHabilidades(List.of(especiesHab[i][2]), null))));
        hb.reglas(hb.reglas().conCombate(new DatosCombate(false, false, false, false, false, true)));
        chk("la base con habilidades es válida", hb.validar().isEmpty());
        List<String> avisosHab = DatosJuego.aplicar(hb);
        chk("se aplica sin avisos", avisosHab.isEmpty());
        chk("la especie lleva su habilidad al juego", Pokedex.getByName("HabLlamas").getHabilidades().equals(List.of("mar_llamas")));
        Movimiento fuego = new Movimiento("Llama", 10, "Fire", 0), tierra = new Movimiento("Terremoto", 10, "Ground", 0);
        PokemonLuchador llamas = luchador(Pokedex.getByName("HabLlamas"));
        chk("potencia_tipo: con la vida llena no cambia (10)", danio(llamas, fuego, 10, luchador(pd)) == 10);
        llamas.recibirDaño(llamas.getVidaMax() * 2 / 3 + 1);
        chk("potencia_tipo: con la vida al tercio o menos, el fuego hace x1.5 (15)", danio(llamas, fuego, 10, luchador(pd)) == 15);
        chk("potencia_tipo: otros tipos no cambian (10)", danio(llamas, tierra, 10, luchador(pd)) == 10);
        PokemonLuchador levita = luchador(Pokedex.getByName("HabLevitacion"));
        chk("inmunidad_tipo: no recibe daño de Tierra (0) pero sí de otros tipos (10)",
                danio(luchador(pa), tierra, 10, levita) == 0 && danio(luchador(pa), fuego, 10, luchador(Pokedex.getByName("HabLevitacion"))) == 10);
        PokemonLuchador agresor = luchador(pa);
        luchador(Pokedex.getByName("HabEstatica"));
        agresor.usarMovimiento(fis, 10, luchador(Pokedex.getByName("HabEstatica")));
        chk("contacto_estado: el atacante queda paralizado al golpearle", agresor.getEstado() == EstadoAlterado.PARALIZADO);
        PokemonLuchador regenera = luchador(Pokedex.getByName("HabRegenerador"));
        regenera.recibirDaño(100);
        int antesRegen = regenera.getVidaActual();
        regenera.efectoHabilidadInicioTurno();
        chk("regeneracion: al empezar el turno recupera 1/16 de la vida máxima (+" + (regenera.getVidaMax() / 16) + ")",
                regenera.getVidaActual() - antesRegen == regenera.getVidaMax() / 16);
        ReglasCombate.configurar(false, false, false, false, false);
        PokemonLuchador llamas2 = luchador(Pokedex.getByName("HabLlamas"));
        llamas2.recibirDaño(llamas2.getVidaMax() * 2 / 3 + 1);
        PokemonLuchador regenera2 = luchador(Pokedex.getByName("HabRegenerador"));
        regenera2.recibirDaño(100);
        int a2 = regenera2.getVidaActual();
        regenera2.efectoHabilidadInicioTurno();
        PokemonLuchador agresor2 = luchador(pa);
        agresor2.usarMovimiento(fis, 10, luchador(Pokedex.getByName("HabEstatica")));
        chk("con la regla apagada las habilidades no hacen nada (daño 10, sin regeneración, sin parálisis, tierra normal)",
                danio(llamas2, fuego, 10, luchador(pd)) == 10 && regenera2.getVidaActual() == a2
                        && agresor2.getEstado() == EstadoAlterado.NORMAL && danio(luchador(pa), tierra, 10, luchador(Pokedex.getByName("HabLevitacion"))) == 10);
        BaseDatos reimp = PuenteProyecto.importar().base();
        chk("importar devuelve las habilidades del juego tal cual (nombre, efecto, tipo, estado y valor)",
                reimp.habilidades().equals(hb.habilidades()));
        chk("y los Pokémon con su habilidad", reimp.especie("habllamas").forma(0).habilidades().equals(List.of("mar_llamas")));
        BaseDatos malo = BaseDatos.cargar(carpeta);
        malo.poner(new DatosHabilidad("rota", "Rota", "", "potencia_tipo", null, null, 1.5));
        malo.poner(new DatosHabilidad("rota2", "Rota2", "", "contacto_estado", null, "VOLAR", 50));
        chk("el validador rechaza habilidades incompletas (sin tipo o con estado desconocido)",
                malo.validar().stream().filter(e -> e.contains("rota")).count() == 2);

        // ---- 8.6 partidas antiguas
        if (Files.exists(fixture)) {
            try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(Files.newInputStream(fixture))) {
                Jugador antigua = (Jugador) in.readObject();
                ReglasCombate.configurar(true, true, true, true, true);
                PokemonLuchador viejo = antigua.getPokemonActivo();
                Movimiento mv = viejo.getMovimientos().get(0);
                for (int i = 0; i < 60; i++) silencio(() -> viejo.usarMovimiento(mv, 10, luchador(pd)));
                chk("una partida antigua con TODAS las reglas activas: sus movimientos no fallan ni se agotan (precisión 0, PP 0) y sin habilidad",
                        mv.getPrecision() == 0 && mv.getPp() == 0 && viejo.tienePp(mv) && !viejo.sinPp() && viejo.getHabilidad() == null);
            } catch (Exception e) {
                chk("partida antigua con las reglas activas: " + e, false);
            }
        }

        ReglasCombate.configurar(false, false, false, false, false);
        ReglasCombate.azar(new java.util.Random());
        DatosJuego.aplicar(original);
        chk("al volver a los datos originales todo queda apagado y sin habilidades",
                !ReglasCombate.efectos() && !ReglasCombate.precision() && !ReglasCombate.pp() && !ReglasCombate.estadisticas()
                        && !ReglasCombate.habilidades() && Habilidades.todas().isEmpty() && Pokedex.getTodos().size() == 1025);

        System.out.println(todoOk ? "\nTODO CORRECTO" : "\nHAY FALLOS");
        return todoOk;
    }

    /** Vida que pierde {@code defensor} al recibir {@code mov} de {@code atacante} (a nivel 5, sin objetos). */
    private static int perdida(PokemonBase atacante, PokemonBase defensor, Movimiento mov) {
        PokemonLuchador a = new PokemonLuchador(atacante, 5), d = new PokemonLuchador(defensor, 5);
        int antes = d.getVidaActual();
        a.atacarCon(mov, mov.getPotencia(), d);
        return antes - d.getVidaActual();
    }

    // ------------------------------------------------------------- ayudas de las pruebas de combate

    /** Una especie "trucada" con estadísticas conocidas: a nivel 5 y sin IVs, vida/ataque/defensa = base + 5. */
    private static PokemonBase baseTrucada(String nombre, int vida, int ataque, int defensa) {
        return new PokemonBase(nombre, "Normal", null, vida, ataque, defensa, 45, 0, null, new java.util.ArrayList<>());
    }

    private static PokemonLuchador luchador(PokemonBase base) {
        return new PokemonLuchador(base, 5, 0);
    }

    private static java.util.Map<Estadistica, Integer> stats(int vida, int ataque, int defensa) {
        java.util.Map<Estadistica, Integer> m = new java.util.EnumMap<>(Estadistica.class);
        m.put(Estadistica.VIDA, vida);
        m.put(Estadistica.ATAQUE, ataque);
        m.put(Estadistica.DEFENSA, defensa);
        m.put(Estadistica.VELOCIDAD, 45);
        return m;
    }

    /** Daño que recibe {@code defensor} cuando {@code atacante} usa {@code mov} con esa potencia. */
    private static int danio(PokemonLuchador atacante, Movimiento mov, double potencia, PokemonLuchador defensor) {
        int antes = defensor.getVidaActual();
        silencio(() -> atacante.usarMovimiento(mov, potencia, defensor));
        return antes - defensor.getVidaActual();
    }

    private static void silencio(Runnable r) { capturar(r); }

    private static String capturar(Runnable r) {
        java.io.PrintStream anterior = System.out;
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(buffer, true, java.nio.charset.StandardCharsets.UTF_8));
        try {
            r.run();
        } finally {
            System.setOut(anterior);
        }
        return buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
    }
}
