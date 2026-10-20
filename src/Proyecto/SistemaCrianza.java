package Proyecto;

import java.util.ArrayList;
import java.util.List;

/** Sistema de crianza con compatibilidad por grupos huevo e IVs heredados. */
public final class SistemaCrianza {
    public static final long COOLDOWN_MS = 30L * 60L * 1000L;

    private SistemaCrianza() { }

    public static void visitar(java.util.Scanner sc, Jugador jugador) {
        while (true) {
            System.out.println("\n=== Criadero Pokémon ===");
            System.out.println("Los dos progenitores quedan en descanso de crianza durante 30 minutos reales.");
            System.out.println("1. Criar Pokémon");
            System.out.println("2. Ver sexo, IVs y cooldown de mi equipo");
            System.out.println("3. Volver");
            System.out.print("Elige: ");
            int opcion = Entrada.leerEntero(sc);
            if (opcion == 1) criar(sc, jugador);
            else if (opcion == 2) verEquipo(jugador);
            else if (opcion == 3) return;
            else System.out.println("Opción no válida.");
        }
    }

    /** Devuelve false sin alterar padres ni cooldown si la pareja no es compatible. */
    public static boolean criar(java.util.Scanner sc, Jugador jugador) {
        List<PokemonLuchador> candidatos = new ArrayList<>();
        List<String> origen = new ArrayList<>();
        for (PokemonLuchador p : jugador.getEquipo()) {
            candidatos.add(p);
            origen.add("equipo");
        }
        for (PokemonLuchador p : jugador.getPc()) {
            candidatos.add(p);
            origen.add("PC");
        }

        List<Integer> disponibles = new ArrayList<>();
        for (int i = 0; i < candidatos.size(); i++) {
            PokemonLuchador p = candidatos.get(i);
            if (!p.getBase().esMega() && GeneticaPokemon.puedeCriar(p.getBase())) disponibles.add(i);
        }
        if (disponibles.size() < 2) {
            System.out.println("Necesitas al menos dos Pokémon con datos de crianza en tu equipo o PC.");
            return false;
        }

        System.out.println("\nElige el primer progenitor (0 cancela):");
        listarCandidatos(candidatos, origen, disponibles);
        int primero = leerIndice(sc, candidatos.size(), disponibles);
        if (primero < 0) return false;
        PokemonLuchador a = candidatos.get(primero);
        if (a.getBase().esMega()) {
            System.out.println("Una forma Mega no puede criar mientras está megaevolucionada.");
            return false;
        }

        System.out.println("\nElige el segundo progenitor (0 cancela):");
        listarCandidatos(candidatos, origen, disponibles);
        int segundo = leerIndice(sc, candidatos.size(), disponibles);
        if (segundo < 0) return false;
        PokemonLuchador b = candidatos.get(segundo);
        if (a == b) {
            System.out.println("Debes escoger dos Pokémon individuales distintos.");
            return false;
        }
        if (b.getBase().esMega()) {
            System.out.println("Una forma Mega no puede criar mientras está megaevolucionada.");
            return false;
        }

        long ahora = System.currentTimeMillis();
        if (!a.puedeCriarAhora() || !b.puedeCriarAhora()) {
            System.out.println("No están listos para criar todavía.");
            mostrarCooldown(a, "Primer progenitor");
            mostrarCooldown(b, "Segundo progenitor");
            return false;
        }

        if (!sonCompatibles(a, b)) {
            System.out.println("La pareja no es compatible. Se requieren sexos compatibles y un grupo huevo común; Ditto puede criar con cualquier especie apta excepto consigo mismo.");
            return false;
        }

        PokemonLuchador noDitto = esDitto(a) ? b : a;
        PokemonLuchador madre = elegirMadre(a, b);
        PokemonBase especie = GeneticaPokemon.especieHuevo(madre == null ? noDitto.getBase() : madre.getBase());
        if (esDitto(a) || esDitto(b)) especie = GeneticaPokemon.especieHuevo(noDitto.getBase());
        if (especie == null) especie = noDitto.getBase();
        PokemonBase baseHuevo = Pokedex.buscarPorNombre(especie.getNombre());
        if (baseHuevo == null || baseHuevo.esMega()) {
            System.out.println("No se ha podido resolver la especie base del huevo: " + especie.getNombre() + ".");
            return false;
        }

        int[] iva = a.getIVs();
        int[] ivb = b.getIVs();
        int[] ivCria = new int[6];
        for (int i = 0; i < ivCria.length; i++) {
            // Por defecto se conserva la regla solicitada: media redondeada de ambos progenitores.
            ivCria[i] = (int) Math.round((iva[i] + ivb[i]) / 2.0);
            int herenciaA = a.getObjeto() == null ? -1 : a.getObjeto().getIvHeredado();
            int herenciaB = b.getObjeto() == null ? -1 : b.getObjeto().getIvHeredado();
            boolean fuerzaA = herenciaA == i;
            boolean fuerzaB = herenciaB == i;
            if (fuerzaA && fuerzaB) {
                // Si ambos llevan el mismo objeto de IV, se elige al azar cuál de los dos transmite ese IV.
                ivCria[i] = java.util.concurrent.ThreadLocalRandom.current().nextBoolean() ? iva[i] : ivb[i];
            } else if (fuerzaA) {
                ivCria[i] = iva[i];
            } else if (fuerzaB) {
                ivCria[i] = ivb[i];
            }
        }

        SexoPokemon sexoCria = GeneticaPokemon.sexoAleatorio(baseHuevo);
        PokemonLuchador cria = new PokemonLuchador(baseHuevo, 1, ivCria, sexoCria);

        long siguiente = ahora + COOLDOWN_MS;
        a.setProximaCrianzEpochMs(siguiente);
        b.setProximaCrianzEpochMs(siguiente);
        boolean equipoLlenoAntes = jugador.equipoLleno();
        jugador.agregarPokemon(cria);

        System.out.println("\n¡Ha nacido un huevo Pokémon!");
        System.out.println("Especie: " + cria.getNombre() + " · Nivel 1 · Sexo: " + cria.getSexo());
        System.out.println("IVs heredados como media de ambos progenitores:");
        System.out.println("  " + cria.resumenIVs());
        System.out.println("Ambos progenitores podrán volver a criar dentro de 30 minutos reales.");
        System.out.println(equipoLlenoAntes
                ? "El equipo estaba completo: el Pokémon se ha enviado al PC."
                : "El Pokémon se ha añadido a tu equipo.");
        return true;
    }

    public static boolean sonCompatibles(PokemonLuchador a, PokemonLuchador b) {
        if (a == null || b == null || a == b || a.getBase() == null || b.getBase() == null) return false;
        if (a.getBase().esMega() || b.getBase().esMega()) return false;
        boolean dittoA = esDitto(a), dittoB = esDitto(b);
        if (dittoA && dittoB) return false;
        if (dittoA || dittoB) {
            PokemonLuchador otro = dittoA ? b : a;
            return GeneticaPokemon.puedeCriar(otro.getBase());
        }
        if (!a.getSexo().compatibleCon(b.getSexo())) return false;
        return GeneticaPokemon.compartenGrupoHuevo(a.getBase(), b.getBase());
    }

    private static boolean esDitto(PokemonLuchador p) {
        return p != null && p.getBase() != null && p.getBase().getNombre().equalsIgnoreCase("Ditto");
    }

    private static PokemonLuchador elegirMadre(PokemonLuchador a, PokemonLuchador b) {
        if (a.getSexo() == SexoPokemon.HEMBRA) return a;
        if (b.getSexo() == SexoPokemon.HEMBRA) return b;
        // Ditto más un progenitor de cualquier sexo: la especie no Ditto determina la cría.
        if (esDitto(a)) return b;
        if (esDitto(b)) return a;
        return a;
    }

    private static void listarCandidatos(List<PokemonLuchador> candidatos, List<String> origen, List<Integer> disponibles) {
        for (int idx : disponibles) {
            PokemonLuchador p = candidatos.get(idx);
            System.out.println((idx + 1) + ". " + p.getNombre() + " Nv." + p.getNivel() + " — "
                    + p.getSexo() + " — " + origen.get(idx) + " — " + p.resumenIVs()
                    + (p.puedeCriarAhora() ? " — listo" : " — cooldown " + tiempo(p.milisegundosParaCriar())));
        }
    }

    private static int leerIndice(java.util.Scanner sc, int max, List<Integer> disponibles) {
        int elegido = Entrada.leerEntero(sc);
        if (elegido == 0) return -1;
        if (elegido < 1 || elegido > max || !disponibles.contains(elegido - 1)) {
            System.out.println("Selección no válida. Escoge un número de los que aparecen en la lista.");
            return -1;
        }
        return elegido - 1;
    }

    private static void mostrarCooldown(PokemonLuchador p, String etiqueta) {
        if (p.puedeCriarAhora()) System.out.println(etiqueta + ": listo para criar.");
        else System.out.println(etiqueta + ": " + tiempo(p.milisegundosParaCriar()) + " restantes.");
    }

    private static String tiempo(long ms) {
        long totalSegundos = Math.max(0L, (ms + 999L) / 1000L);
        long minutos = totalSegundos / 60L;
        long segundos = totalSegundos % 60L;
        return String.format("%02d:%02d", minutos, segundos);
    }

    private static void verEquipo(Jugador jugador) {
        List<PokemonLuchador> todos = new ArrayList<>();
        todos.addAll(jugador.getEquipo());
        todos.addAll(jugador.getPc());
        if (todos.isEmpty()) {
            System.out.println("No tienes Pokémon.");
            return;
        }
        for (int i = 0; i < todos.size(); i++) {
            PokemonLuchador p = todos.get(i);
            System.out.println((i + 1) + ". " + p.getNombre() + " — " + p.getSexo());
            System.out.println("   " + p.resumenIVs());
            mostrarCooldown(p, "   Crianza");
        }
    }
}
