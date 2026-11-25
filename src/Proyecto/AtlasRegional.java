package Proyecto;

import java.util.List;
import java.util.Scanner;

/** Menú gráfico de 120 mapas explorables: 10 clásicos y 2 nuevos por generación. */
public final class AtlasRegional {
    private AtlasRegional() { }

    public static void abrir(Scanner sc, Jugador jugador) {
        if (jugador == null) return;
        while (true) {
            Generacion generacion = jugador.getGeneracion();
            List<CatalogoMapasGeneracionales.Mapa> regiones = new java.util.ArrayList<>(
                    CatalogoMapasGeneracionales.listar(generacion));
            regiones.addAll(CatalogoMapasGeneracionales.nuevos(generacion));
            String[] opciones = new String[regiones.size() + 1];
            for (int i = 0; i < regiones.size(); i++) opciones[i] = regiones.get(i).etiqueta();
            opciones[regiones.size()] = "Volver";
            int seleccion;
            if (UIJuego.disponible()) {
                seleccion = UIJuego.elegir("Atlas regional · " + generacion.getRegion(),
                        "Explora doce lugares de " + generacion.getRegion()
                                + ".\nPuedes volver cuando quieras con Esc. El mapa principal"
                                + " y las partidas guardadas permanecen intactos.",
                        opciones);
            } else {
                System.out.println("\nAtlas regional · " + generacion.getRegion());
                for (int i = 0; i < opciones.length; i++)
                    System.out.println((i + 1) + ". " + opciones[i]);
                seleccion = Entrada.leerEntero(sc) - 1;
            }
            if (seleccion < 0 || seleccion >= regiones.size()) return;
            CatalogoMapasGeneracionales.Mapa mapa = regiones.get(seleccion);
            while (true) {
                ExploradorMapa.Resultado resultado =
                        ExploradorMapa.explorarMapaRegional(jugador, mapa);
                if (resultado.getEvento() == null) break;
                atenderEdificio(sc, jugador, mapa, resultado.getEvento());
            }
        }
    }

    private static void atenderEdificio(Scanner sc, Jugador jugador,
                                       CatalogoMapasGeneracionales.Mapa mapa, String evento) {
        switch (evento) {
            case "CENTRO_POKEMON", "CENTRO_POKÉMON" ->
                    CentroPokemon.visitar(sc, jugador.getPokemon(), jugador);
            case "TIENDA" -> Tienda.entrar(sc, jugador);
            case "GIMNASIO" -> {
                Gimnasio gimnasio = Gimnasio.deCiudad(jugador.getUbicacion());
                if (gimnasio != null) gimnasio.desafiar(sc, jugador);
                else UIJuego.mensaje("Estadio regional",
                        "Estás ante un estadio de " + mapa.generacion().getRegion()
                                + ". El líder oficial de esta ciudad no se encuentra aquí.");
            }
            case "CASA", "CASA_MAMA" -> UIJuego.mensaje("Casa regional",
                    "Un vecino te cuenta historias sobre " + mapa.nombre() + ".");
            case "CASINO" -> Casino.entrar(sc, jugador);
            case "GOBIERNO" -> UIJuego.mensaje("Edificio regional",
                    "Este edificio pertenece a la historia local de " + mapa.nombre() + ".");
            default -> UIJuego.mensaje("Edificio", "No hay eventos disponibles aquí.");
        }
    }
}
