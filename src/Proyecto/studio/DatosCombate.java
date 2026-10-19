package Proyecto.studio;

/**
 * Reglas opcionales del combate, todas desactivadas por defecto (el combate original no las tenía).
 * Ver {@code Proyecto.ReglasCombate} para lo que hace cada una.
 */
public record DatosCombate(boolean eficaciaTipos, boolean efectosMovimientos, boolean precision, boolean pp,
                           boolean estadisticas, boolean habilidades) {
    public static final DatosCombate NINGUNO = new DatosCombate(false, false, false, false, false, false);

    public DatosCombate conEficaciaTipos(boolean valor) {
        return new DatosCombate(valor, efectosMovimientos, precision, pp, estadisticas, habilidades);
    }

    public boolean alguna() {
        return eficaciaTipos || efectosMovimientos || precision || pp || estadisticas || habilidades;
    }
}
