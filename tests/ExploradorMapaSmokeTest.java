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

        // La vista compacta ocupa 576x384 px para el mapa de 36x24.
        // El tamaño se cambia solo en pantalla, nunca en la rejilla lógica.
        comprobar(ExploradorMapa.pixelesPorCasilla(32,0)==16,
                "mapa regional compacto: 16 px por casilla");
        comprobar(ExploradorMapa.pixelesPorCasilla(32,1)==32,
                "zoom regional nativo: 32 px por casilla");
        comprobar(ExploradorMapa.pixelesPorCasilla(32,2)==64,
                "zoom regional ampliado x2");
        comprobar(ExploradorMapa.pixelesPorCasilla(32,3)==96,
                "zoom regional ampliado x3");
        comprobar(ExploradorMapa.pixelesPorCasilla(32,4)==128,
                "zoom regional ampliado x4");
        comprobar(ExploradorMapa.pixelesPorCasilla(32,-100)==16,
                "el zoom inferior está limitado");
        comprobar(ExploradorMapa.pixelesPorCasilla(32,100)==128,
                "el zoom superior está limitado");
        comprobar(ExploradorMapa.pixelesPorCasilla(16,0)==48,
                "el zoom de mapas personalizados no cambia");
        comprobar(36*ExploradorMapa.pixelesPorCasilla(32,0)==576
                && 24*ExploradorMapa.pixelesPorCasilla(32,0)==384,
                "el mapa regional completo entra en la vista de escritorio");

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
