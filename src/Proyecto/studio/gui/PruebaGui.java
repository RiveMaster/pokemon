package Proyecto.studio.gui;

import Proyecto.studio.Aprendizaje;
import Proyecto.studio.BaseDatos;
import Proyecto.studio.ClaseZona;
import Proyecto.studio.DatosEspecie;
import Proyecto.studio.DatosHabilidad;
import Proyecto.studio.DatosZona;
import Proyecto.studio.PuenteProyecto;
import Proyecto.studio.TipoPokemon;

import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Container;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Pruebas de la interfaz gráfica que no necesitan pantalla: los formularios, los paneles y sus operaciones
 * se manejan por código. Uso: {@code java Proyecto.studio.Estudio probar-gui}.
 */
public final class PruebaGui {
    private PruebaGui() {}

    private static boolean todoOk;

    private static void chk(String descripcion, boolean ok) {
        System.out.println((ok ? "  OK     " : "  FALLO  ") + descripcion);
        todoOk &= ok;
    }

    @SuppressWarnings("unchecked")
    private static <T> int diferencias(Registros<?> r, Collection<?> items) {
        Registros<T> rr = (Registros<T>) r;
        int malos = 0;
        for (Object o : items) {
            T t = (T) o;
            rr.editor().cargar(t);
            try {
                if (!rr.editor().leer(t).equals(t)) malos++;
            } catch (RuntimeException e) {
                malos++;
            }
        }
        return malos;
    }

    private static <C extends Component> C primero(Container raiz, Class<C> tipo) {
        for (Component c : raiz.getComponents()) {
            if (tipo.isInstance(c)) return tipo.cast(c);
            if (c instanceof Container k) { C r = primero(k, tipo); if (r != null) return r; }
        }
        return null;
    }

    private static void escribirNombre(Registros<?> r, String texto) {
        JTextField t = primero((Container) r.editor().componente(), JTextField.class);
        t.setText(texto);
    }

    public static boolean probar() throws java.io.IOException {
        todoOk = true;
        System.setProperty("java.awt.headless", System.getProperty("java.awt.headless", "true"));
        BaseDatos original = PuenteProyecto.importar().base();
        Path tmp = Files.createTempDirectory("studio-gui");
        original.guardar(tmp);
        BaseDatos bd = BaseDatos.cargar(tmp);        // copia profunda
        bd.poner(new DatosHabilidad("electricidad_estatica", "Electricidad estática", "Puede paralizar al contacto.",
                "contacto_estado", null, "PARALIZADO", 30));
        bd.poner(new DatosHabilidad("mar_llamas", "Mar Llamas", "Potencia el fuego.", "potencia_tipo", TipoPokemon.FUEGO, null, 1.5));
        bd.poner(new DatosHabilidad("regenerador", "Regenerador", "", "regeneracion", null, null, 16));
        bd.poner(new DatosHabilidad("sin_efecto", "Sin efecto", "Solo informativa."));
        // reglas de combate marcadas a propósito (mezcla de verdaderas y falsas) para comprobar cada casilla
        bd.reglas(bd.reglas().conCombate(new Proyecto.studio.DatosCombate(true, true, false, true, false, true)));

        Contexto ctx = new Contexto(bd, tmp);
        List<String> avisos = new ArrayList<>();
        int[] preguntas = {0};
        boolean[] respuesta = {true};
        ctx.avisador = avisos::add;
        ctx.confirmador = m -> { preguntas[0]++; return respuesta[0]; };
        EditorGrafico.Paneles p = new EditorGrafico.Paneles(ctx);

        System.out.println("1) Cada formulario reproduce exactamente el registro que carga (cargar -> leer):");
        chk("1025 Pokémon", diferencias(p.pokemon, bd.especies().values()) == 0);
        chk("115 movimientos", diferencias(p.movimientos, bd.movimientos().values()) == 0);
        chk("4 habilidades, con sus efectos, tipos, estados y valores", diferencias(p.habilidades, bd.habilidades().values()) == 0 && bd.habilidades().size() == 4);
        chk("14 objetos", diferencias(p.objetos, bd.objetos().values()) == 0);
        chk("23 zonas", diferencias(p.zonas, bd.zonas().values()) == 0);
        chk("5 gimnasios", diferencias(p.gimnasios, bd.gimnasios().values()) == 0);
        chk("10 jefes", diferencias(p.jefes, bd.jefes().values()) == 0);
        chk("10 generaciones", diferencias(p.generaciones, bd.generaciones().values()) == 0);
        chk("reglas", p.reglas.leer().equals(bd.reglas()));
        p.reglas.cargar();
        chk("reglas: cargar -> leer sin cambios", p.reglas.leer().equals(bd.reglas()));
        chk("las 6 casillas de reglas de combate reproducen lo guardado (tipos, estados, PP y habilidades sí; precisión y estadísticas no)",
                p.reglas.leer().combate().equals(new Proyecto.studio.DatosCombate(true, true, false, true, false, true)));
        chk("no se marcó nada como modificado", !ctx.sinGuardar());

        System.out.println("2) Editar a través del panel:");
        Registros<DatosEspecie> pok = castEsp(p.pokemon);
        pok.seleccionar("pikachu");
        chk("seleccionar carga la ficha (sin cambios pendientes)", pok.actual().nombre().equals("Pikachu") && !pok.hayCambios());
        escribirNombre(pok, "Pikachu Edit");
        chk("escribir en el formulario se detecta como cambio", pok.hayCambios());
        PanelPokemon.EditorEspecie ed = (PanelPokemon.EditorEspecie) pok.editor();
        ed.añadirMovimiento("placaje", 7);
        ed.añadirEvolucionNivel("raichu", 22);
        ed.añadirEvolucionObjeto("raichu", "restos");
        pok.guardarActual();
        DatosEspecie pik = bd.especie("pikachu");
        chk("el nombre se guarda y el símbolo no cambia", pik.nombre().equals("Pikachu Edit") && pik.simbolo().equals("pikachu"));
        chk("el movimiento nuevo está en su sitio (nivel 7)", pik.forma(0).porNivel().contains(new Aprendizaje("placaje", 7)));
        chk("las dos evoluciones se guardaron", pik.forma(0).evoluciones().size() == 2
                && pik.forma(0).evoluciones().get(0).nivelMinimo().getAsInt() == 22
                && "restos".equals(pik.forma(0).evoluciones().get(1).condiciones().get("objeto")));
        chk("la base queda marcada como sin guardar", ctx.sinGuardar());
        chk("la base sigue siendo válida", bd.validar().isEmpty());

        System.out.println("3) Crear y borrar:");
        pok.nuevo();
        escribirNombre(pok, "Testmon");
        pok.guardarActual();
        DatosEspecie tm = bd.especie("testmon");
        chk("nuevo Pokémon con id 1026 y símbolo generado", tm != null && tm.id() == 1026);
        chk("aparece en la lista", pok.lista().getModel().getSize() == 1026);
        ed.añadirEvolucionNivel("testmon", 5);
        pok.seleccionar("bulbasaur");            // cambia de registro con cambios pendientes sin guardar
        chk("la evolución añadida y no guardada se descartó al preguntar (sí)", bd.especie("bulbasaur") != null);
        pok.seleccionar("testmon");
        pok.borrarActual();
        chk("borrar quita el Pokémon", bd.especie("testmon") == null && pok.lista().getModel().getSize() == 1025);

        Registros<?> mov = p.movimientos;
        mov.seleccionar("impactrueno");
        boolean usado = bd.especie("pikachu").forma(0).porNivel().stream().anyMatch(a -> a.movimiento().equals("impactrueno"));
        mov.borrarActual();
        chk("borrar un movimiento lo quita de los Pokémon que lo tenían", usado && bd.movimiento("impactrueno") == null
                && bd.especie("pikachu").forma(0).porNivel().stream().noneMatch(a -> a.movimiento().equals("impactrueno")));
        chk("y la base sigue válida", bd.validar().isEmpty());

        System.out.println("4) Preguntar antes de descartar cambios:");
        pok.seleccionar("bulbasaur");
        escribirNombre(pok, "Cambiado sin guardar");
        preguntas[0] = 0;
        respuesta[0] = false;
        pok.lista().setSelectedValue(bd.especie("ivysaur"), true);
        chk("al cambiar de Pokémon con cambios pregunta", preguntas[0] >= 1);
        chk("si se responde que no, se queda en el mismo", pok.actual().simbolo().equals("bulbasaur"));
        respuesta[0] = true;
        pok.lista().setSelectedValue(bd.especie("ivysaur"), true);
        chk("si se responde que sí, cambia", pok.actual().simbolo().equals("ivysaur"));
        chk("lo descartado no llegó a la base", bd.especie("bulbasaur").nombre().equals("Bulbasaur"));

        System.out.println("5) Errores de formulario no tocan la base:");
        pok.seleccionar("ivysaur");
        escribirNombre(pok, "");
        int antes = avisos.size();
        pok.guardarActual();
        chk("un nombre vacío se rechaza con un aviso", avisos.size() == antes + 1 && avisos.get(avisos.size() - 1).contains("Nombre"));
        chk("Ivysaur sigue igual", bd.especie("ivysaur").nombre().equals("Ivysaur"));

        System.out.println("6) Zonas: insertar en el camino y borrar reconectando:");
        DatosZona r1 = bd.zona("ruta1");
        String siguienteDeR1 = r1.siguiente();
        DatosZona nueva = new DatosZona("ruta_nueva", "Ruta Nueva", ClaseZona.RUTA, "x", List.of(TipoPokemon.AGUA), 5, 8,
                "ruta1", siguienteDeR1, "nava", List.of(), false, false);
        PanelesSimples.guardarZona(ctx, nueva);
        chk("la zona vecina anterior apunta a la nueva", bd.zona("ruta1").siguiente().equals("ruta_nueva"));
        chk("la zona vecina siguiente la tiene como anterior", bd.zona(siguienteDeR1).anterior().equals("ruta_nueva"));
        chk("el camino sigue siendo válido", bd.validar().isEmpty());
        PanelesSimples.borrarZona(ctx, bd.zona("ruta_nueva"));
        chk("al borrarla, el camino se une de nuevo", bd.zona("ruta1").siguiente().equals(siguienteDeR1)
                && bd.zona(siguienteDeR1).anterior().equals("ruta1") && bd.zona("ruta_nueva") == null);
        chk("y es válido", bd.validar().isEmpty());
        antes = avisos.size();
        PanelesSimples.borrarZona(ctx, bd.zona("oviedo"));
        chk("no deja borrar una ciudad con gimnasio", avisos.size() == antes + 1 && bd.zona("oviedo") != null);

        System.out.println("7) Tabla de tipos y reglas:");
        PanelTipos pt = p.tipos;
        int fNormal = TipoPokemon.NORMAL.ordinal(), cRoca = TipoPokemon.ROCA.ordinal() + 1;
        chk("la matriz muestra Normal -> Roca = ½", (Double) pt.tabla().getModel().getValueAt(fNormal, cRoca) == 0.5);
        pt.tabla().getModel().setValueAt(2.0, fNormal, cRoca);
        chk("editar una casilla cambia la base", bd.tipos().multiplicador(TipoPokemon.NORMAL, TipoPokemon.ROCA) == 2.0);
        p.reglas.cargar();
        chk("reglas: aplicar sin cambios no modifica nada", sinCambioReglas(p, bd));

        System.out.println("8) Guardar y recargar:");
        EditorGrafico.guardar(ctx, p);
        chk("guardar deja la base como guardada", !ctx.sinGuardar());
        BaseDatos disco = BaseDatos.cargar(tmp);
        chk("lo guardado en disco es lo editado", disco.especies().equals(bd.especies())
                && disco.movimientos().equals(bd.movimientos())
                && disco.zonas().equals(bd.zonas())
                && disco.tipos().multiplicador(TipoPokemon.NORMAL, TipoPokemon.ROCA) == 2.0);
        ctx.sustituir(original, true);
        chk("sustituir la base refresca las listas (1025 Pokémon, 115 movimientos con Impactrueno de vuelta)",
                pok.lista().getModel().getSize() == 1025 && mov.lista().getModel().getSize() == 115
                        && bd.movimiento("impactrueno") == null && original.movimiento("impactrueno") != null);
        chk("y marca sin guardar cuando se reimporta", ctx.sinGuardar());
        pok.seleccionar("pikachu");
        chk("tras sustituir, Pikachu vuelve a llamarse Pikachu", pok.actual().nombre().equals("Pikachu"));

        System.out.println("9) Lugares nuevos desde el editor gráfico:");
        BaseDatos bd9 = ctx.bd();   // la base actual del editor (tras sustituir() ya no es la variable bd)
        Registros<?> zon = p.zonas;
        zon.nuevo();
        escribirNombre(zon, "Ruta Test");
        zon.guardarActual();
        chk("una zona nueva ocupa la ranura extra1 y conserva su nombre", bd9.zona("extra1") != null && bd9.zona("extra1").nombre().equals("Ruta Test"));
        chk("el validador señala que le falta zona anterior", bd9.validar().stream().anyMatch(e -> e.contains("extra1") && e.contains("anterior")));
        zon.nuevo();
        escribirNombre(zon, "Ruta Test 2");
        zon.guardarActual();
        chk("la segunda usa extra2", bd9.zona("extra2") != null);
        zon.seleccionar("extra2");
        zon.borrarActual();
        zon.seleccionar("extra1");
        zon.borrarActual();
        chk("al borrarlas, las ranuras vuelven a estar libres", bd9.zona("extra1") == null && Proyecto.studio.DatosJuego.ranurasLibres(bd9) == 24);
        for (int i = 1; i <= 24; i++)
            bd9.poner(new DatosZona("extra" + i, "Z" + i, ClaseZona.RUTA, "", List.of(TipoPokemon.AGUA), 1, 2, "nava", null, "nava", List.of(), false, false));
        int avisosAntes = avisos.size();
        zon.nuevo();
        escribirNombre(zon, "Una de más");
        zon.guardarActual();
        chk("con las 24 ranuras ocupadas se rechaza una zona más con un aviso claro",
                avisos.size() == avisosAntes + 1 && avisos.get(avisos.size() - 1).contains("ranuras libres") && bd9.zonas().size() == 23 + 24);
        for (int i = 1; i <= 24; i++) bd9.quitarZona("extra" + i);

        System.out.println(todoOk ? "\nTODO CORRECTO" : "\nHAY FALLOS");
        return todoOk;
    }

    private static boolean sinCambioReglas(EditorGrafico.Paneles p, BaseDatos bd) {
        boolean antes = bd.reglas().equals(p.reglas.leer());
        p.reglas.aplicarSiCambio();
        return antes && bd.reglas().equals(p.reglas.leer());
    }

    @SuppressWarnings("unchecked")
    private static Registros<DatosEspecie> castEsp(Registros<?> r) { return (Registros<DatosEspecie>) r; }

    public static void main(String[] args) throws java.io.IOException {
        System.exit(probar() ? 0 : 1);
    }
}
