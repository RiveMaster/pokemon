package Proyecto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Regresiones de navegación del jugador en entorno sin ventanas:
 * inventario, pociones, menús y controles de depuración no visibles.
 */
public final class InteraccionesGraficasSmokeTest {
    private InteraccionesGraficasSmokeTest() { }

    public static void main(String[] args) throws Exception {
        PokemonBase base = new PokemonBase("PruebaVida", "Normal", null,
                80, 60, 55, 45, 0, null, List.of());
        PokemonLuchador pokemon = new PokemonLuchador(base, 15, 0);
        Jugador jugador = new Jugador("Prueba", pokemon, 2000);
        int saludMax = pokemon.getVidaMax();
        pokemon.recibirDaño(40);
        int saludTrasDanio = pokemon.getVidaActual();
        assert saludMax - saludTrasDanio >= 20;
        jugador.añadirMedicina("Pocion", 1);
        assert jugador.medicinasDisponibles().contains("Pocion");
        assert jugador.usarMedicina("Pocion", pokemon);
        assert pokemon.getVidaActual() == Math.min(saludMax, saludTrasDanio + 20)
                : "La Poción debe curar 20 PS, sin curación doble.";
        assert !jugador.medicinasDisponibles().contains("Pocion");
        assert !jugador.usarMedicina("Pocion", pokemon)
                : "No se pueden usar medicinas agotadas.";

        jugador.añadirPokeball("Poke Ball", 1);
        assert jugador.pokeballsDisponibles().contains("Poke Ball");
        assert jugador.usarPokeball("Poke Ball");
        assert !jugador.pokeballsDisponibles().contains("Poke Ball");

        assert UIJuego.elegir("Prueba", "Sin escritorio", "Aceptar") == -1;
        assert UIJuego.confirmar("Prueba", "Sin escritorio") == false;

        // No revelar acceso de depuración en las opciones normales del pueblo.
        String menuVillaverde = Files.readString(Path.of("src/Proyecto/Villaverde.java"));
        assert !menuVillaverde.contains("Introducir cheatcode");
        System.out.println("InteraccionesGraficasSmokeTest: mochila, medicina, captura y menús OK");
    }
}
