package Proyecto;

/** Pruebas sin interfaz gráfica de las reglas básicas de exploración. */
public final class ExploradorMapaSmokeTest {
    private ExploradorMapaSmokeTest() { }

    public static void main(String[] args) {
        comprobar(!ExploradorMapa.permiteAgua(true, false),
                "sin nadador ni volador el agua está cerrada");
        comprobar(ExploradorMapa.permiteAgua(true, true),
                "un Pokémon con navegación permite cruzar agua");
        comprobar(ExploradorMapa.permiteAgua(false, false),
                "el suelo normal no requiere habilidad de navegación");

        comprobar(!ExploradorMapa.puedeEntrarCasilla(true, false, false),
                "el agua bloquea al equipo que no puede navegar");
        comprobar(ExploradorMapa.puedeEntrarCasilla(true, false, true),
                "el equipo con navegación puede entrar en agua");
        comprobar(!ExploradorMapa.puedeEntrarCasilla(true, true, true),
                "nadar no permite atravesar una pared u obstáculo");
        comprobar(!ExploradorMapa.puedeEntrarCasilla(false, true, false),
                "los árboles y paredes bloquean el paso");
        comprobar(ExploradorMapa.puedeEntrarCasilla(false, false, false),
                "una casilla libre permite caminar");

        comprobar(ExploradorMapa.generaEncuentro(true),
                "la hierba está marcada como terreno de encuentros");
        comprobar(!ExploradorMapa.generaEncuentro(false),
                "no hay encuentros en caminos, agua ni suelo normal");

        PokemonBase agua = new PokemonBase("NadadorPrueba", "Water", null,
                45, 45, 45, 45, 0, null, java.util.List.of());
        PokemonBase volador = new PokemonBase("VoladorPrueba", "Flying", null,
                45, 45, 45, 45, 0, null, java.util.List.of());
        PokemonBase normal = new PokemonBase("NormalPrueba", "Normal", null,
                45, 45, 45, 45, 0, null, java.util.List.of());

        Jugador jugador = new Jugador("Prueba", new PokemonLuchador(normal, 5, 0), 0);
        jugador.getEquipo().clear();
        jugador.getEquipo().add(new PokemonLuchador(agua, 5, 0));
        comprobar(jugador.puedeNavegarAgua(), "un Pokémon de tipo Agua vivo permite nadar");
        jugador.getEquipo().get(0).recibirDaño(9999);
        comprobar(!jugador.puedeNavegarAgua(), "un Pokémon debilitado no permite nadar");
        jugador.getEquipo().clear();
        jugador.getEquipo().add(new PokemonLuchador(volador, 5, 0));
        comprobar(jugador.puedeNavegarAgua(), "un Pokémon Volador vivo permite cruzar el agua por aire");

        jugador.guardarPosicionMapa("RUTA1", 7, 9);
        int[] posicion = jugador.getPosicionMapa("RUTA1");
        comprobar(posicion != null && posicion[0] == 7 && posicion[1] == 9,
                "la posición de exploración queda guardada por mapa");
        posicion[0] = 100;
        comprobar(jugador.getPosicionMapa("RUTA1")[0] == 7,
                "la posición devuelta no permite modificar el estado interno");

        System.out.println("OK: reglas de agua, colisiones, hierba, equipo y posición.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
