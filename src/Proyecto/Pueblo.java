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

    public String getDescripcion() { return descripcion; }

    public Set<String> getTiposTipicos() { return tiposTipicos; }

    public Ubicacion getRutaAnterior() { return rutaAnterior; }

    public Ubicacion getRutaSiguiente() { return rutaSiguiente; }

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

        System.out.println("Explora las calles con W, A, S y D. Acércate a las puertas para interactuar.");

        while (true) {
            ExploradorMapa.Resultado exploracion = ExploradorMapa.explorarCiudad(jugador, p.ubicacion);
            if (exploracion.getDestino() != null) return exploracion.getDestino();
            if (exploracion.getEvento() != null) {
                atenderEdificio(sc, jugador, p, exploracion.getEvento());
                continue; // vuelve a la misma calle frente a la puerta
            }
            List<Mapa.Opcion> opciones = new ArrayList<>();
            opciones.add(new Mapa.Opcion("Seguir explorando las calles", () -> null));
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

    /** Las puertas del mapa y los menús llevan a los mismos servicios y progreso. */
    private static void atenderEdificio(Scanner sc, Jugador jugador, Pueblo p, String tipo) {
        switch (tipo) {
            case "CENTRO_POKEMON", "CENTRO_POKÉMON" ->
                    CentroPokemon.visitar(sc, jugador.getPokemon(), jugador);
            case "TIENDA" -> Tienda.entrar(sc, jugador);
            case "GIMNASIO" -> {
                Gimnasio gimnasio = Gimnasio.deCiudad(p.ubicacion);
                if (gimnasio != null) gimnasio.desafiar(sc, jugador);
                else UIJuego.mensaje("Edificio cerrado", "Esta localidad no tiene gimnasio.");
            }
            case "CASINO" -> {
                if (p.casino) Casino.entrar(sc, jugador);
                else UIJuego.mensaje("Edificio", "El casino está cerrado.");
            }
            case "GOBIERNO" -> {
                if (p.sedeGobierno) Gobierno.visitarSede(sc, jugador);
                else UIJuego.mensaje("Edificio", "La sede del Gobierno no está aquí.");
            }
            case "CASA", "CASA_MAMA" -> UIJuego.mensaje("Casa del pueblo",
                    "¡Bienvenido! Un vecino te saluda desde la puerta. Los gimnasios están en las ciudades principales.");
            default -> UIJuego.mensaje("Edificio", "No hay ninguna interacción disponible aquí.");
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
