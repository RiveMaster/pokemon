package Proyecto;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class Villaverde {

    // Menú de Villaverde. Devuelve a dónde va el jugador (Ruta 1); el resto de opciones se quedan aquí.
    public static Ubicacion mostrarMenu(Scanner sc, Jugador jugadorCompleto) {

        System.out.println("Explora Villaverde con W, A, S y D. Pulsa Esc para abrir el menú.");
        ExploradorMapa.Resultado exploracionInicial = ExploradorMapa.explorarCiudad(jugadorCompleto, Ubicacion.VILLAVERDE);
        if (exploracionInicial.getDestino() != null) return exploracionInicial.getDestino();

        while (true) {
            Ubicacion primera = Mapa.getRecorrido().size() > 1 ? Mapa.getRecorrido().get(1) : Ubicacion.RUTA1;
            int opcion;
            if (UIJuego.disponible()) {
                int seleccion = UIJuego.elegir("Pueblo Villaverde",
                        "Generación actual: " + jugadorCompleto.getGeneracion().etiqueta()
                                + "\\n¿Qué quieres hacer?",
                        "Visitar a mamá", "Ir a " + primera.getNombre(),
                        "Mochila y opciones", "Viajar a otra generación",
                        "Explorar el pueblo", "Introducir cheatcode");
                if (seleccion < 0) continue;
                if (seleccion == 5) {
                    String codigo = UIJuego.pedirTexto("Cheatcode", "Introduce el código secreto:", "");
                    opcion = "777999222".equals(codigo) ? 777999222 : -1;
                } else opcion = seleccion + 1;
            } else {
                System.out.println("\\nPUEBLO VILLAVERDE");
                System.out.println("1. Visitar a mamá");
                System.out.println("2. Ir a " + primera.getNombre());
                System.out.println("3. Menú (mochila, MT, objetos, volar, guardar)");
                System.out.println("4. Viajar a otra generación");
                System.out.println("5. Volver a explorar el pueblo (WASD)");
                System.out.print("Elige una opción: ");
                opcion = leerEntero(sc);
                sc.nextLine();
            }

            switch (opcion) {
                case 1 -> visitarMama(sc, jugadorCompleto);
                case 2 -> {
                    return primera;
                }
                case 3 -> {
                    Ubicacion destino = Menu.mostrarmenu(sc, jugadorCompleto);
                    if (destino != null) {
                        return destino; // el jugador ha volado a otro sitio
                    }
                }
                case 4 -> viajarEntreGeneraciones(sc, jugadorCompleto);
                case 5 -> {
                    ExploradorMapa.Resultado exploracion = ExploradorMapa.explorarCiudad(jugadorCompleto, Ubicacion.VILLAVERDE);
                    if (exploracion.getDestino() != null) return exploracion.getDestino();
                }
                case 777999222 ->{
                    System.out.println("Entrando en modo debug para estats modificadas.");
                    System.out.println("Introduce la opcion:");
                    System.out.println("1. Dinero");
                    System.out.println("2. Pokemon");
                    System.out.println("3. MT");
                    System.out.println("4. Objeto");
                    System.out.println("5. Desbloquear todas las generaciones");
                    System.out.println("6. salir.");
                    System.out.println("7. Obtener todas las megapiedras");
                    System.out.println("8. Obtener Banda Dinamax");
                    int opcional=sc.nextInt();
                    sc.nextLine();
                    switch (opcional){
                        case 1->{
                            System.out.println("Introduce la cantidad de dinero deseada: ");
                            Jugador.ganarDinero(sc.nextInt(), jugadorCompleto);
                        }
                        case 2->{
                            System.out.println("Introduce el nombre del pokemon deseado: ");
                            String nombre=sc.nextLine();
                            if (Pokedex.buscarPorNombre(nombre) == null) {
                                System.out.println("No se encontro el Pokemon deseado, saliendo del menu de pokemon.");
                            } else{
                                System.out.println("Introduce el nivel: ");
                                int nivel = sc.nextInt();
                                sc.nextLine();
                                PokemonLuchador creado = new PokemonLuchador(Pokedex.buscarPorNombre(nombre), nivel, 31);
                                System.out.print("¿Quieres que sea shiny? (s/n): ");
                                creado.setShiny(sc.nextLine().trim().equalsIgnoreCase("s"));
                                jugadorCompleto.agregarPokemon(creado);
                                System.out.println(creado.getNombre() + (creado.esShiny() ? " ✨ (Shiny)" : "") + " añadido.");
                            }
                        }
                        case 3->{
                            System.out.println("Introduce el nombre del ataque: ");
                            Movimiento mov = Ataques.buscarPorNombre(sc.nextLine());
                            if (mov == null) {
                                System.out.println("No se encontró ese ataque.");
                            } else {
                                jugadorCompleto.añadirMT(mov.getNombre());
                                System.out.println("MT añadida: " + mov.getNombre());
                            }
                        }
                        case 4->{
                            System.out.println("Objetos disponibles:");
                            for (ObjetoEquipable o : ObjetoEquipable.values()) {
                                System.out.println("  " + o.getNombre());
                            }
                            System.out.println("Introduce el nombre del objeto: ");
                            ObjetoEquipable objeto = ObjetoEquipable.buscarPorNombre(sc.nextLine());
                            if (objeto == null) {
                                System.out.println("No se encontró ese objeto.");
                            } else {
                                jugadorCompleto.añadirObjetoEquipable(objeto, 1);
                                System.out.println("Objeto añadido: " + objeto.getNombre());
                            }
                        }
                        case 5->{
                            jugadorCompleto.desbloquearTodasLasGeneraciones();
                            System.out.println("Generaciones 1 a 10 desbloqueadas.");
                        }
                        case 6->{ }
                        case 7->{
                            for (String piedra : MegaEvoluciones.todasLasPiedras()) jugadorCompleto.obtenerMegapiedra(piedra);
                            System.out.println("Todas las megapiedras del catálogo añadidas a la mochila.");
                        }
                        case 8->{
                            jugadorCompleto.añadirObjetoClave("Banda Dinamax");
                            System.out.println("Banda Dinamax añadida. En combate, usa -1 con una especie compatible para activar Gigamax durante tres turnos.");
                        }
                        default -> System.out.println("Valor no permitido");
                    }
                }
                case -1 -> UIJuego.mensaje("Cheatcode", "Código incorrecto.");
                default -> UIJuego.mensaje("Villaverde", "Opción no válida.");
            }
        }
    }

    // Elige una de las generaciones desbloqueadas: cambia qué Pokémon salen y el nivel del mundo entero
    private static void viajarEntreGeneraciones(Scanner sc, Jugador jugador) {
        String[] opciones = new String[10];
        for (int n = 1; n <= 10; n++) {
            Generacion g = Generacion.de(n);
            String estado = n > jugador.getGeneracionMaxima() ? "Bloqueada"
                    : (jugador.jefeDerrotado(g) ? "Jefe derrotado" : "Desbloqueada");
            String actual = n == jugador.getGeneracion().getNumero() ? " · Actual" : "";
            opciones[n - 1] = g.etiqueta() + " · Nivel +" + g.getBonusNivel()
                    + " · " + estado + actual;
        }
        if (UIJuego.disponible()) {
            int seleccion = UIJuego.elegir("Viajar entre generaciones",
                    "Para desbloquear la siguiente debes conseguir las 5 medallas y derrotar al jefe del Gobierno.",
                    opciones);
            if (seleccion >= 0) {
                if (jugador.viajarAGeneracion(seleccion + 1))
                    UIJuego.mensaje("Viaje completado", "Has viajado a " + jugador.getGeneracion().etiqueta()
                            + ": " + jugador.getGeneracion().getAmbiente());
                else UIJuego.mensaje("Generación bloqueada", "Aún no has desbloqueado esa generación.");
            }
            return;
        }
        for (int n = 0; n < opciones.length; n++) System.out.println((n + 1) + ". " + opciones[n]);
        int n = leerEntero(sc);
        if (jugador.viajarAGeneracion(n))
            System.out.println("Has viajado a " + jugador.getGeneracion().etiqueta());
    }

    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Ingresa un número: ");
                sc.nextLine();
            }
        }
    }
    public static void visitarMama(Scanner sc, Jugador jugador) {
        if (!jugador.isMamaVisitada()) {
            jugador.agregarDinero(1000);
            jugador.setMamaVisitada(true);
            UIJuego.mensaje("Casa de mamá", "¡Hijo mío! Te doy 1000€ para empezar tu aventura.\\nHas recibido 1000€.");
        } else {
            UIJuego.mensaje("Casa de mamá", "Ya te di dinero, ¡ve y conviértete en un gran entrenador!");
        }
        UIJuego.mensaje("Dinero", "Ahora tienes " + jugador.getDinero() + "€");
    }
}
