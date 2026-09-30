package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Datos y menú de una ciudad o pueblo: Centro Pokémon, tienda, gimnasio (si lo hay; se busca por
 * ciudad en Gimnasio) y, en Vitoria, la sede del Gobierno.
 */
public class Pueblo {
    private static final Random RAND = new Random();

    private final Ubicacion ubicacion;
    private final String descripcion;
    private final Set<String> tiposTipicos; // para mostrar "Pokémon típicos" según la generación
    private final boolean sedeGobierno;
    private final boolean casino;
    private final Ubicacion rutaAnterior;
    private final Ubicacion rutaSiguiente;  // null si es el final del camino

    public Pueblo(Ubicacion ubicacion, String descripcion, Set<String> tiposTipicos,
                  boolean sedeGobierno, boolean casino, Ubicacion rutaAnterior, Ubicacion rutaSiguiente) {
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.tiposTipicos = tiposTipicos;
        this.sedeGobierno = sedeGobierno;
        this.casino = casino;
        this.rutaAnterior = rutaAnterior;
        this.rutaSiguiente = rutaSiguiente;
    }

    public Ubicacion getUbicacion() { return ubicacion; }

    public boolean tieneCasino() { return casino; }

    public boolean tieneSedeGobierno() { return sedeGobierno; }

    public static Ubicacion visitar(Scanner sc, Jugador jugador, Pueblo p) {
        Generacion gen = jugador.getGeneracion();
        Gimnasio gimnasio = Gimnasio.deCiudad(p.ubicacion);

        System.out.println("\n=== " + p.ubicacion.getNombre() + " [" + gen.etiqueta() + "] ===");
        System.out.println(p.descripcion);
        System.out.println("Ambiente: " + gen.getAmbiente());
        System.out.println("Por las calles se ven: " + String.join(", ", gen.muestra(p.tiposTipicos, 3)) + ".");
        if (gimnasio != null) {
            System.out.println(gimnasio.resumen(gen, jugador));
        } else {
            System.out.println("Aquí no hay gimnasio.");
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
            if (gimnasio != null) {
                opciones.add(new Mapa.Opcion("Gimnasio", () -> {
                    gimnasio.desafiar(sc, jugador);
                    return null;
                }));
            } else {
                opciones.add(new Mapa.Opcion("Hablar con los vecinos", () -> {
                    hablarConVecinos();
                    return null;
                }));
            }
            if (p.casino) {
                opciones.add(new Mapa.Opcion("Casino", () -> {
                    Casino.entrar(sc, jugador);
                    return null;
                }));
            }
            if (p.sedeGobierno) {
                opciones.add(new Mapa.Opcion("Sede del Gobierno", () -> {
                    Gobierno.visitarSede(sc, jugador);
                    return null;
                }));
            }
            opciones.add(new Mapa.Opcion("Menú (mochila, MT, objetos, volar, guardar)", () -> Menu.mostrarmenu(sc, jugador)));
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

    /** En los pueblos sin gimnasio, los vecinos te cuentan dónde hay uno. */
    private static void hablarConVecinos() {
        List<Gimnasio> gimnasios = Gimnasio.getGimnasios();
        Gimnasio g = gimnasios.get(RAND.nextInt(gimnasios.size()));
        System.out.println("\nUn vecino: ¿Buscas un gimnasio? En " + g.getCiudad().getNombre()
                + " hay uno de tipo " + g.getTipo() + ". Lo lleva " + g.getLider() + ".");
        System.out.println("Un vecino: Y dicen que antes de llegar a él hay que ganar a sus " + g.getMiembros().length + " miembros.");
    }
}
