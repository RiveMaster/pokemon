package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** Datos y menú de una ciudad: Centro Pokémon, tienda, gimnasio y (en Vitoria) la sede del Gobierno. */
public class Pueblo {
    private final Ubicacion ubicacion;
    private final String descripcion;
    private final Set<String> tiposTipicos; // para mostrar "Pokémon típicos" según la generación
    private final int gimnasio;             // índice en Gimnasio.getGimnasios(), -1 si no hay
    private final boolean sedeGobierno;
    private final Ubicacion rutaAnterior;
    private final Ubicacion rutaSiguiente;

    public Pueblo(Ubicacion ubicacion, String descripcion, Set<String> tiposTipicos, int gimnasio,
                  boolean sedeGobierno, Ubicacion rutaAnterior, Ubicacion rutaSiguiente) {
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.tiposTipicos = tiposTipicos;
        this.gimnasio = gimnasio;
        this.sedeGobierno = sedeGobierno;
        this.rutaAnterior = rutaAnterior;
        this.rutaSiguiente = rutaSiguiente;
    }

    public Ubicacion getUbicacion() { return ubicacion; }

    public static Ubicacion visitar(Scanner sc, Jugador jugador, Pueblo p) {
        Generacion gen = jugador.getGeneracion();

        System.out.println("\n=== " + p.ubicacion.getNombre() + " [" + gen.etiqueta() + "] ===");
        System.out.println(p.descripcion);
        System.out.println("Ambiente: " + gen.getAmbiente());
        System.out.println("Por las calles se ven: " + String.join(", ", gen.muestra(p.tiposTipicos, 3)) + ".");
        if (p.gimnasio >= 0) {
            System.out.println(Gimnasio.getGimnasios().get(p.gimnasio).resumen(gen, jugador));
        }

        while (true) {
            List<Mapa.Opcion> opciones = new ArrayList<>();
            opciones.add(new Mapa.Opcion("Centro Pokémon", () -> {
                CentroPokemon.visitar(sc, jugador.getPokemon(), jugador);
                return null;
            }));
            opciones.add(new Mapa.Opcion("Tienda", () -> {
                Tienda.entrar(sc, jugador);
                return null;
            }));
            if (p.gimnasio >= 0) {
                opciones.add(new Mapa.Opcion("Gimnasio", () -> {
                    Gimnasio.getGimnasios().get(p.gimnasio).desafiar(sc, jugador);
                    return null;
                }));
            }
            if (p.sedeGobierno) {
                opciones.add(new Mapa.Opcion("Sede del Gobierno", () -> {
                    Gobierno.visitarSede(sc, jugador);
                    return null;
                }));
            }
            opciones.add(new Mapa.Opcion("Menú (mochila, MT, objetos, guardar)", () -> {
                Menu.mostrarmenu(sc, jugador);
                return null;
            }));
            if (p.rutaSiguiente != null) {
                Ubicacion s = p.rutaSiguiente;
                opciones.add(new Mapa.Opcion("Ir a " + s.getNombre(), () -> s));
            }
            Ubicacion a = p.rutaAnterior;
            opciones.add(new Mapa.Opcion("Volver a " + a.getNombre(), () -> a));

            System.out.println("\n¿Qué quieres hacer en " + p.ubicacion.getNombre() + "?");
            Ubicacion destino = Mapa.elegir(sc, opciones);
            if (destino != null) {
                return destino;
            }
        }
    }
}
