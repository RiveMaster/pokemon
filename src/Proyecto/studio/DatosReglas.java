package Proyecto.studio;

import java.util.List;

/**
 * Reglas globales del mundo que en el juego original están como constantes sueltas
 * (Npc, Gimnasio, Gobierno, Generacion).
 */
public record DatosReglas(
        double probabilidadNpc,            // probabilidad de que te rete un NPC en vez de salir un salvaje
        List<String> nombresNpc,
        List<String> frasesReto,
        List<String> frasesDerrota,
        int premioNpcPorNivel,             // dinero = suma de niveles del equipo x este valor
        int miembrosPorGimnasio,
        int pokemonPorMiembro,
        int tamanoEquipoJefe,
        int recompensaJefe,
        List<String> excluidosSalvajes,    // especies que nunca salen (legendarios, míticos...) por símbolo
        DatosCombate combate) {            // reglas opcionales del combate (el juego original no tenía ninguna)

    /** Sin eficacia de tipos (el comportamiento original del juego). */
    public DatosReglas(double probabilidadNpc, List<String> nombresNpc, List<String> frasesReto,
                       List<String> frasesDerrota, int premioNpcPorNivel, int miembrosPorGimnasio,
                       int pokemonPorMiembro, int tamanoEquipoJefe, int recompensaJefe, List<String> excluidosSalvajes) {
        this(probabilidadNpc, nombresNpc, frasesReto, frasesDerrota, premioNpcPorNivel, miembrosPorGimnasio,
                pokemonPorMiembro, tamanoEquipoJefe, recompensaJefe, excluidosSalvajes, DatosCombate.NINGUNO);
    }


    /** Compatibilidad: lo que antes era una sola regla. */
    public boolean eficaciaTipos() { return combate.eficaciaTipos(); }

    public DatosReglas conCombate(DatosCombate nuevo) {
        return new DatosReglas(probabilidadNpc, nombresNpc, frasesReto, frasesDerrota, premioNpcPorNivel,
                miembrosPorGimnasio, pokemonPorMiembro, tamanoEquipoJefe, recompensaJefe, excluidosSalvajes, nuevo);
    }

    public DatosReglas {
        combate = combate == null ? DatosCombate.NINGUNO : combate;
        nombresNpc = nombresNpc == null ? List.of() : List.copyOf(nombresNpc);
        frasesReto = frasesReto == null ? List.of() : List.copyOf(frasesReto);
        frasesDerrota = frasesDerrota == null ? List.of() : List.copyOf(frasesDerrota);
        excluidosSalvajes = excluidosSalvajes == null ? List.of() : List.copyOf(excluidosSalvajes);
        if (probabilidadNpc < 0 || probabilidadNpc > 1)
            throw new IllegalArgumentException("probabilidadNpc debe estar entre 0 y 1: " + probabilidadNpc);
    }

    public static DatosReglas vacias() {
        return new DatosReglas(0.30, null, null, null, 6, 3, 2, 5, 3000, null);
    }
}
