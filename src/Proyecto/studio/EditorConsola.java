package Proyecto.studio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/** Menú principal del editor de datos (Fase 3). Trabaja sobre una {@link BaseDatos} en memoria. */
public final class EditorConsola {
    private final Consola c;
    private BaseDatos bd;
    private final Path carpeta;
    private boolean sinGuardar = false;

    private EditorConsola(Scanner sc, BaseDatos bd, Path carpeta) {
        this.c = new Consola(sc);
        this.bd = bd;
        this.carpeta = carpeta;
    }

    /**
     * Abre el editor sobre {@code carpeta}. Si no hay datos, ofrece importarlos del juego.
     * Usa el Scanner que se le pase (puede ser el del juego: solo lee líneas completas).
     */
    public static void abrir(Path carpeta, Scanner sc) throws IOException {
        Consola c = new Consola(sc);
        BaseDatos bd;
        try {
            if (Files.exists(carpeta.resolve("pokemon.json"))) {
                bd = BaseDatos.cargar(carpeta);
                System.out.println("Datos cargados de " + carpeta.toAbsolutePath());
            } else if (c.confirmar("No hay datos en " + carpeta + ". ¿Importarlos del juego (Pokedex, Ataques, mapa...)?", true)) {
                PuenteProyecto.Importacion imp = PuenteProyecto.importar();
                bd = imp.base();
                System.out.println("Importado del juego (" + imp.avisos().size() + " avisos).");
            } else {
                bd = new BaseDatos();
            }
            new EditorConsola(sc, bd, carpeta).ejecutar();
        } catch (Consola.FinEntrada fin) {
            System.out.println("\n(Fin de la entrada: se cierra el editor sin guardar lo pendiente.)");
        }
    }

    private void marcar() { sinGuardar = true; }

    private void ejecutar() throws IOException {
        EditorPokemon pokemon = new EditorPokemon(c, bd, this::marcar);
        EditorDatos datos = new EditorDatos(c, bd, this::marcar);
        EditorMundo mundo = new EditorMundo(c, bd, this::marcar);
        while (true) {
            int op = c.menu("╔══════ EDITOR DE DATOS ══════╗" + (sinGuardar ? "   (cambios sin guardar)" : ""),
                    "Pokémon", "Movimientos", "Habilidades", "Objetos", "Tabla de tipos",
                    "Zonas (mapa)", "Gimnasios", "Jefes del Gobierno", "Generaciones", "Reglas globales",
                    "Validar", "Guardar", "Recargar del disco (descarta cambios)", "Reimportar lo que el juego tiene cargado (descarta cambios)");
            switch (op) {
                case 0 -> { if (salir()) return; }
                case 1 -> pokemon.menu();
                case 2 -> datos.movimientos();
                case 3 -> datos.habilidades();
                case 4 -> datos.objetos();
                case 5 -> datos.tipos();
                case 6 -> mundo.zonas();
                case 7 -> mundo.gimnasios();
                case 8 -> mundo.jefes();
                case 9 -> mundo.generaciones();
                case 10 -> mundo.reglas();
                case 11 -> validar();
                case 12 -> guardar();
                case 13 -> { if (recargar()) return; }
                case 14 -> { if (reimportar()) return; }
                default -> { }
            }
        }
    }

    private void validar() {
        List<String> errores = bd.validar();
        if (errores.isEmpty()) { c.println("  ✔ Todo correcto."); return; }
        c.println("  ✘ " + errores.size() + " problemas:");
        errores.stream().limit(25).forEach(e -> c.println("   - " + e));
        if (errores.size() > 25) c.println("   ... y " + (errores.size() - 25) + " más");
    }

    private void guardar() throws IOException {
        List<String> errores = bd.validar();
        if (!errores.isEmpty()) {
            c.println("  Hay " + errores.size() + " problemas de validación:");
            errores.stream().limit(8).forEach(e -> c.println("   - " + e));
            if (!c.confirmar("¿Guardar de todos modos?", false)) return;
        }
        bd.guardar(carpeta);
        sinGuardar = false;
        c.println("  Guardado en " + carpeta.toAbsolutePath());
    }

    private boolean salir() throws IOException {
        if (sinGuardar) {
            if (c.confirmar("Hay cambios sin guardar. ¿Guardar antes de salir?", true)) guardar();
            else if (!c.confirmar("¿Salir descartando los cambios?", false)) return false;
        }
        return true;
    }

    /** Sustituye la base en memoria: reabre el editor con la nueva y termina el actual. */
    private boolean recargar() throws IOException {
        if (!c.confirmar("Se perderán los cambios sin guardar. ¿Continuar?", false)) return false;
        reabrir(BaseDatos.cargar(carpeta));
        return true;
    }

    private boolean reimportar() throws IOException {
        if (!c.confirmar("Se sustituirá TODO lo que hay en memoria por lo que el juego tiene cargado ahora. ¿Continuar?", false)) return false;
        reabrir(PuenteProyecto.importar().base());
        return true;
    }

    private void reabrir(BaseDatos nueva) throws IOException {
        sinGuardar = false;
        this.bd = nueva;
        new EditorConsola(c.scanner(), nueva, carpeta).ejecutar();
    }
}
