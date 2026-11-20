package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Volar: un Pokémon de tipo Volador o Dragón (y que no esté debilitado) puede llevar al jugador
 * a cualquier pueblo o ruta del mapa. Se usa desde el menú del juego.
 */
public class Vuelo {

    // Devuelve el destino elegido, o null si no puede volar o cancela.
    public static Ubicacion elegirDestino(Scanner sc, Jugador jugador) {
        List<PokemonLuchador> voladores = new ArrayList<>();
        boolean hayVoladorDebilitado = false;
        for (PokemonLuchador p : jugador.getEquipo()) {
            if (p.puedeVolar()) {
                if (p.estaVivo()) voladores.add(p); else hayVoladorDebilitado = true;
            }
        }
        if (voladores.isEmpty()) {
            if (hayVoladorDebilitado) {
                System.out.println("Tu Pokémon volador está debilitado. Cúralo en el Centro Pokémon para poder volar.");
            } else {
                System.out.println("Ningún Pokémon de tu equipo es de tipo Volador o Dragón, así que no puedes volar.");
            }
            return null;
        }
        if (UIJuego.disponible()) {
            String[] candidatos = voladores.stream()
                    .map(p -> p.getNombre() + " · Nv. " + p.getNivel())
                    .toArray(String[]::new);
            int elegido = UIJuego.elegir("Vuelo", "¿Qué Pokémon te llevará volando?", candidatos);
            if (elegido < 0) return null;
            PokemonLuchador volador = voladores.get(elegido);
            List<Ubicacion> destinosDisponibles = Mapa.getRecorrido();
            String[] destinosTexto = destinosDisponibles.stream()
                    .map(u -> Mapa.descripcionCorta(u)
                            + (u == jugador.getUbicacion() ? " · Ubicación actual" : ""))
                    .toArray(String[]::new);
            int seleccion = UIJuego.elegir("Mapa de vuelo",
                    "Elige a dónde viajar con " + volador.getNombre() + ":", destinosTexto);
            if (seleccion < 0 || seleccion >= destinosDisponibles.size()) return null;
            Ubicacion destino = destinosDisponibles.get(seleccion);
            if (destino == jugador.getUbicacion()) {
                UIJuego.mensaje("Vuelo", "Ya estás en " + destino.getNombre() + ".");
                return null;
            }
            UIJuego.mensaje("Vuelo", "¡" + volador.getNombre()
                    + " te lleva volando a " + destino.getNombre() + "!");
            return destino;
        }
        System.out.println("\n-- Volar --");
        System.out.println("¿Qué Pokémon te llevará volando? (0 para cancelar)");
        for (int i = 0; i < voladores.size(); i++) {
            PokemonLuchador p = voladores.get(i);
            System.out.println((i + 1) + ". " + p.getNombre() + " Nv." + p.getNivel());
        }
        System.out.print("Elige: ");
        int eleccion = Entrada.leerEntero(sc);
        if (eleccion < 1 || eleccion > voladores.size()) {
            System.out.println("Cancelado.");
            return null;
        }
        PokemonLuchador volador = voladores.get(eleccion - 1);

        List<Ubicacion> destinos = Mapa.getRecorrido();
        System.out.println("\n¿A dónde quieres volar? (0 para cancelar)");
        for (int i = 0; i < destinos.size(); i++) {
            Ubicacion u = destinos.get(i);
            String aqui = (u == jugador.getUbicacion()) ? "  <-- estás aquí" : "";
            System.out.println((i + 1) + ". " + Mapa.descripcionCorta(u) + aqui);
        }
        System.out.print("Elige: ");
        int n = Entrada.leerEntero(sc);
        if (n < 1 || n > destinos.size()) {
            System.out.println("Cancelado.");
            return null;
        }
        Ubicacion destino = destinos.get(n - 1);
        if (destino == jugador.getUbicacion()) {
            System.out.println("Ya estás en " + destino.getNombre() + ".");
            return null;
        }
        System.out.println("¡" + volador.getNombre() + " te lleva volando a " + destino.getNombre() + "!");
        return destino;
    }
}
