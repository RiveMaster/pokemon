package Proyecto;

import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Casino de Comillas con una ruleta europea (0 a 36, un solo cero). Se apuesta con el dinero
 * del jugador. Los pagos son los de la ruleta real, así que la banca tiene una ventaja del 2,7%
 * a la larga: jugar aquí es un riesgo, no una fuente de ingresos.
 */
public class Casino {

    public static final int APUESTA_MINIMA = 10;

    /** Aleatoriedad de la ruleta (ajustable desde pruebas). */
    static Random rng = new Random();

    static final Set<Integer> ROJOS = Set.of(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);

    public enum Tipo { NUMERO, ROJO, NEGRO, PAR, IMPAR, FALTA, PASA, DOCENA1, DOCENA2, DOCENA3 }

    // ------------------------------------------------------------------
    // Reglas (funciones puras, fáciles de probar)
    // ------------------------------------------------------------------

    /* "verde" para el 0, "rojo" o "negro" para el resto. */
    public static String color(int n) {
        if (n == 0) return "verde";
        return ROJOS.contains(n) ? "rojo" : "negro";
    }

    /**
     * Total que se devuelve al jugador si acierta (la apuesta incluida) o 0 si pierde.
     * Número: 35 a 1 (36x). Rojo/Negro, Par/Impar y Falta/Pasa: 1 a 1 (2x). Docenas: 2 a 1 (3x).
     * Si sale el 0 solo gana quien apostó al 0.
     */
    public static int pago(Tipo tipo, int numeroApostado, int apuesta, int resultado) {
        boolean gana;
        int multiplicador;
        switch (tipo) {
            case NUMERO -> { gana = resultado == numeroApostado; multiplicador = 36; }
            case ROJO -> { gana = resultado != 0 && ROJOS.contains(resultado); multiplicador = 2; }
            case NEGRO -> { gana = resultado != 0 && !ROJOS.contains(resultado); multiplicador = 2; }
            case PAR -> { gana = resultado != 0 && resultado % 2 == 0; multiplicador = 2; }
            case IMPAR -> { gana = resultado % 2 == 1; multiplicador = 2; }
            case FALTA -> { gana = resultado >= 1 && resultado <= 18; multiplicador = 2; }
            case PASA -> { gana = resultado >= 19 && resultado <= 36; multiplicador = 2; }
            case DOCENA1 -> { gana = resultado >= 1 && resultado <= 12; multiplicador = 3; }
            case DOCENA2 -> { gana = resultado >= 13 && resultado <= 24; multiplicador = 3; }
            case DOCENA3 -> { gana = resultado >= 25 && resultado <= 36; multiplicador = 3; }
            default -> { gana = false; multiplicador = 0; }
        }
        return gana ? apuesta * multiplicador : 0;
    }

    static int girar() {
        return rng.nextInt(37);
    }

    // ------------------------------------------------------------------
    // Menús
    // ------------------------------------------------------------------

    public static void entrar(Scanner sc, Jugador jugador) {
        if (UIJuego.disponible()) {
            ruletaGrafica(jugador);
            return;
        }
        System.out.println("\n === CASINO DE COMILLAS === ");
        System.out.println("Crupier: Bienvenido. Recuerde: la casa siempre gana... a la larga.");

        while (true) {
            System.out.println("\nDinero: " + jugador.getDinero() + "€");
            System.out.println("1. Jugar a la ruleta");
            System.out.println("2. Salir del casino");
            System.out.print("Elige: ");

            int opcion = Entrada.leerEntero(sc);
            if (opcion == 1) {
                ruleta(sc, jugador);
            } else if (opcion == 2) {
                System.out.println("Sales del casino con " + jugador.getDinero() + "€.");
                return;
            } else {
                System.out.println("Opción no válida.");
            }
        }
    }

    /** Ruleta con opciones por botones; ningún número se escribe en consola. */
    private static void ruletaGrafica(Jugador jugador) {
        String[] apuestas = {"Un número (0–36)", "Rojo", "Negro", "Par",
                "Impar", "Falta (1–18)", "Pasa (19–36)",
                "Primera docena", "Segunda docena", "Tercera docena", "Salir"};
        Tipo[] tipos = {Tipo.NUMERO, Tipo.ROJO, Tipo.NEGRO, Tipo.PAR, Tipo.IMPAR,
                Tipo.FALTA, Tipo.PASA, Tipo.DOCENA1, Tipo.DOCENA2, Tipo.DOCENA3};
        while (true) {
            if (jugador.getDinero() < APUESTA_MINIMA) {
                UIJuego.mensaje("Casino", "No tienes suficiente dinero para apostar.");
                return;
            }
            int opcion = UIJuego.elegir("Casino de Comillas",
                    "Dinero: " + jugador.getDinero() + "€\\nElige tu apuesta:", apuestas);
            if (opcion < 0 || opcion >= tipos.length) return;
            int numero = -1;
            if (opcion == 0) {
                String[] numeros = new String[37];
                for (int n = 0; n <= 36; n++) numeros[n] = n + " · " + color(n);
                numero = UIJuego.elegir("Número de la ruleta",
                        "Elige uno de los 37 números:", numeros);
                if (numero < 0) continue;
            }
            int dinero = jugador.getDinero();
            int[] sugeridos = {10, 25, 50, 100, 250, 500, 1000, dinero};
            java.util.List<Integer> montos = new java.util.ArrayList<>();
            for (int monto : sugeridos)
                if (monto >= APUESTA_MINIMA && monto <= dinero && !montos.contains(monto))
                    montos.add(monto);
            String[] opcionesMonto = montos.stream()
                    .map(monto -> monto + "€")
                    .toArray(String[]::new);
            int elegido = UIJuego.elegir("Cantidad de apuesta",
                    "Saldo: " + dinero + "€\\n¿Cuánto quieres apostar?", opcionesMonto);
            if (elegido < 0 || elegido >= montos.size()) continue;
            int apuesta = montos.get(elegido);
            if (!UIJuego.confirmar("Confirmar apuesta", "¿Apostar " + apuesta + "€?")) continue;
            if (!jugador.gastarDinero(apuesta)) {
                UIJuego.mensaje("Casino", "No tienes dinero suficiente.");
                continue;
            }
            int resultado = girar();
            int cobro = pago(tipos[opcion], numero, apuesta, resultado);
            if (cobro > 0) jugador.agregarDinero(cobro);
            UIJuego.mensaje("Resultado de la ruleta",
                    "La bola cae en " + resultado + " (" + color(resultado) + ").\\n"
                            + (cobro > 0 ? "¡Has ganado " + cobro + "€!" :
                            "Has perdido " + apuesta + "€.")
                            + "\\nSaldo: " + jugador.getDinero() + "€");
        }
    }

    private static void ruleta(Scanner sc, Jugador jugador) {
        System.out.println("\n-- RULETA (mínimo " + APUESTA_MINIMA + "€ por apuesta) --");

        while (true) {
            if (jugador.getDinero() < APUESTA_MINIMA) {
                System.out.println("No tienes dinero suficiente para apostar (mínimo " + APUESTA_MINIMA + "€).");
                return;
            }

            System.out.println("\nDinero: " + jugador.getDinero() + "€");
            System.out.println("¿A qué apuestas?");
            System.out.println("1. Un número (0-36)        paga 35 a 1");
            System.out.println("2. Rojo o negro            paga 1 a 1");
            System.out.println("3. Par o impar             paga 1 a 1");
            System.out.println("4. Falta (1-18) o pasa (19-36)  paga 1 a 1");
            System.out.println("5. Una docena              paga 2 a 1");
            System.out.println("0. Volver");
            System.out.print("Elige: ");

            int categoria = Entrada.leerEntero(sc);
            if (categoria == 0) {
                return;
            }

            Tipo tipo;
            int numero = -1;
            switch (categoria) {
                case 1 -> {
                    System.out.print("¿Qué número (0-36)?: ");
                    numero = Entrada.leerEntero(sc);
                    if (numero < 0 || numero > 36) {
                        System.out.println("Ese número no existe en la ruleta.");
                        continue;
                    }
                    tipo = Tipo.NUMERO;
                }
                case 2 -> tipo = elegirDos(sc, "Rojo", "Negro", Tipo.ROJO, Tipo.NEGRO);
                case 3 -> tipo = elegirDos(sc, "Par", "Impar", Tipo.PAR, Tipo.IMPAR);
                case 4 -> tipo = elegirDos(sc, "Falta (1-18)", "Pasa (19-36)", Tipo.FALTA, Tipo.PASA);
                case 5 -> {
                    System.out.println("1. Primera docena (1-12)");
                    System.out.println("2. Segunda docena (13-24)");
                    System.out.println("3. Tercera docena (25-36)");
                    System.out.print("Elige: ");
                    int d = Entrada.leerEntero(sc);
                    tipo = switch (d) {
                        case 1 -> Tipo.DOCENA1;
                        case 2 -> Tipo.DOCENA2;
                        case 3 -> Tipo.DOCENA3;
                        default -> null;
                    };
                }
                default -> {
                    System.out.println("Opción no válida.");
                    continue;
                }
            }
            if (tipo == null) {
                System.out.println("Apuesta cancelada.");
                continue;
            }

            System.out.print("¿Cuánto apuestas? (" + APUESTA_MINIMA + " a " + jugador.getDinero() + "€, 0 para cancelar): ");
            int apuesta = Entrada.leerEntero(sc);
            if (apuesta == 0) {
                System.out.println("Apuesta cancelada.");
                continue;
            }
            if (apuesta < APUESTA_MINIMA) {
                System.out.println("La apuesta mínima es de " + APUESTA_MINIMA + "€.");
                continue;
            }
            if (!jugador.gastarDinero(apuesta)) {
                System.out.println("No tienes tanto dinero.");
                continue;
            }

            // La bola gira
            int resultado = girar();
            System.out.println("\nLa bola gira... y cae en el " + resultado + " (" + color(resultado) + ").");

            int cobro = pago(tipo, numero, apuesta, resultado);
            if (cobro > 0) {
                jugador.agregarDinero(cobro);
                System.out.println(" ¡Ganas! Cobras " + cobro + "€ (beneficio: " + (cobro - apuesta) + "€).");
            } else {
                System.out.println(" Pierdes " + apuesta + "€.");
            }
        }
    }

    /** Pregunta entre dos opciones. Devuelve null si el jugador elige otra cosa. */
    private static Tipo elegirDos(Scanner sc, String a, String b, Tipo ta, Tipo tb) {
        System.out.println("1. " + a);
        System.out.println("2. " + b);
        System.out.print("Elige: ");
        int n = Entrada.leerEntero(sc);
        return n == 1 ? ta : (n == 2 ? tb : null);
    }
}
