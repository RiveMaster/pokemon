package Proyecto.studio;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Menú Listar / Ver / Crear / Editar / Borrar reutilizable. Editar y crear usan la misma función
 * {@code pedir}: recibe el registro actual (null al crear) y devuelve el nuevo, o null si se cancela.
 */
final class Crud<T> {
    private final Consola c;
    private final String titulo;
    private final Supplier<Collection<T>> items;
    private final Function<T, String> clave;
    private final Function<T, String> etiqueta;
    private final Function<T, String> detalle;
    private final UnaryOperator<T> pedir;
    private final Consumer<T> guardar;
    private final Consumer<T> borrar;
    private final Runnable marcar;

    Crud(Consola c, String titulo, Supplier<Collection<T>> items, Function<T, String> clave,
         Function<T, String> etiqueta, Function<T, String> detalle, UnaryOperator<T> pedir,
         Consumer<T> guardar, Consumer<T> borrar, Runnable marcar) {
        this.c = c;
        this.titulo = titulo;
        this.items = items;
        this.clave = clave;
        this.etiqueta = etiqueta;
        this.detalle = detalle;
        this.pedir = pedir;
        this.guardar = guardar;
        this.borrar = borrar;
        this.marcar = marcar;
    }

    void menu() {
        while (true) {
            int op = c.menu("══ " + titulo.toUpperCase() + " (" + items.get().size() + ") ══",
                    "Listar", "Ver detalle", "Crear", "Editar", "Borrar");
            switch (op) {
                case 0 -> { return; }
                case 1 -> listar();
                case 2 -> { T t = elegir(); if (t != null) c.println(detalle.apply(t)); }
                case 3 -> { T t = pedir.apply(null); if (t != null) { guardar.accept(t); marcar.run(); c.println("  Creado."); } }
                case 4 -> { T t = elegir(); if (t != null) { T n = pedir.apply(t); if (n != null) { guardar.accept(n); marcar.run(); c.println("  Guardado en memoria."); } } }
                case 5 -> { T t = elegir(); if (t != null) { borrar.accept(t); } }
                default -> { }
            }
        }
    }

    private T elegir() {
        return c.buscar(titulo, items.get(), clave, etiqueta);
    }

    private void listar() {
        int n = 0;
        for (T t : items.get()) {
            if (n++ >= 60) { c.println("  ... y " + (items.get().size() - 60) + " más (usa Ver detalle para buscar)."); break; }
            c.println("  " + etiqueta.apply(t));
        }
        if (n == 0) c.println("  (vacío)");
    }
}
