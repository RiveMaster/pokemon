import Proyecto.studio.gui.GeneradorMapas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Smoke test del generador; no requiere bibliotecas de pruebas externas. */
public final class GeneradorMapasSmokeTest {
    private GeneradorMapasSmokeTest() { }

    public static void main(String[] args) {
        List<GeneradorMapas.DefinicionObjeto> objetos = List.of(
                new GeneradorMapas.DefinicionObjeto(0, "Casa", 4, 3, new int[12]),
                new GeneradorMapas.DefinicionObjeto(1, "Árbol grande", 2, 3, new int[6]),
                new GeneradorMapas.DefinicionObjeto(2, "Gimnasio", 5, 4, new int[20]),
                new GeneradorMapas.DefinicionObjeto(3, "Roca", 2, 2, new int[4]),
                new GeneradorMapas.DefinicionObjeto(4, "Centro Pokémon", 4, 3, new int[12])
        );

        int combinaciones = 0;
        int[] dimensiones = {1, 2, 5, 15, 30, 300};
        for (int ancho : dimensiones) {
            for (int alto : dimensiones) {
                for (GeneradorMapas.Estilo estilo : GeneradorMapas.Estilo.values()) {
                    long semilla = 780_213L + ancho * 31L + alto * 7L + estilo.ordinal();
                    GeneradorMapas.Opciones opciones = new GeneradorMapas.Opciones(
                            ancho, alto, semilla, estilo, 55,
                            true, true, true, true, true,
                            0, 1, 2, 3, 4
                    );
                    GeneradorMapas.Resultado a = GeneradorMapas.generar(opciones, objetos);
                    GeneradorMapas.Resultado b = GeneradorMapas.generar(opciones, objetos);
                    comprobar(a, ancho, alto, objetos);
                    if (!mismosResultados(a, b)) {
                        throw new AssertionError("La semilla no reproduce el mismo mapa: "
                                + ancho + "x" + alto + ", " + estilo);
                    }
                    combinaciones++;
                }
            }
        }

        // Los interruptores desactivados también deben generar un mapa válido.
        GeneradorMapas.Resultado simple = GeneradorMapas.generar(
                new GeneradorMapas.Opciones(8, 9, 42, GeneradorMapas.Estilo.PRADERA, 0,
                        false, false, false, false, false, 0, -1, -1, -1, -1),
                List.of()
        );
        comprobar(simple, 8, 9, List.of());
        if (simple.colocaciones.size() != 0) throw new AssertionError("Se crearon objetos cuando estaban desactivados.");

        System.out.println("OK: " + combinaciones
                + " combinaciones de tamaño/estilo deterministas y caso sin elementos opcionales.");
    }

    private static void comprobar(GeneradorMapas.Resultado r, int ancho, int alto,
                                  List<GeneradorMapas.DefinicionObjeto> objetos) {
        int celdas = ancho * alto;
        if (r.ancho != ancho || r.alto != alto || r.capas.length != 3)
            throw new AssertionError("Dimensiones/capas incorrectas.");
        for (int capa = 0; capa < 3; capa++) {
            if (r.capas[capa].length != celdas) throw new AssertionError("Tamaño de capa incorrecto.");
            for (int indice : r.capas[capa]) {
                if (indice < -1 || indice > 4) throw new AssertionError("Índice de tile fuera de rango: " + indice);
            }
        }
        if (r.colisionesManuales.length != celdas || r.hierbaEncuentros.length != celdas)
            throw new AssertionError("Máscaras con tamaño incorrecto.");
        for (int i = 0; i < celdas; i++) {
            if (r.colisionesManuales[i] && r.hierbaEncuentros[i])
                throw new AssertionError("Una celda no puede ser colisión y hierba a la vez.");
        }

        Map<Integer, GeneradorMapas.DefinicionObjeto> porIndice = new HashMap<>();
        for (GeneradorMapas.DefinicionObjeto objeto : objetos) porIndice.put(objeto.indice, objeto);
        for (GeneradorMapas.Colocacion p : r.colocaciones) {
            GeneradorMapas.DefinicionObjeto objeto = porIndice.get(p.objeto);
            if (objeto == null) throw new AssertionError("Referencia de objeto inexistente.");
            if (p.x < 0 || p.y < 0 || p.x + objeto.ancho > ancho || p.y + objeto.alto > alto)
                throw new AssertionError("Objeto fuera del mapa.");
            if (p.capa < 0 || p.capa > 2) throw new AssertionError("Capa de objeto inválida.");
        }
    }

    private static boolean mismosResultados(GeneradorMapas.Resultado a, GeneradorMapas.Resultado b) {
        if (a.ancho != b.ancho || a.alto != b.alto
                || !Arrays.equals(a.colisionesManuales, b.colisionesManuales)
                || !Arrays.equals(a.hierbaEncuentros, b.hierbaEncuentros)
                || a.colocaciones.size() != b.colocaciones.size()) return false;
        for (int c = 0; c < 3; c++) if (!Arrays.equals(a.capas[c], b.capas[c])) return false;
        for (int i = 0; i < a.colocaciones.size(); i++) {
            GeneradorMapas.Colocacion x = a.colocaciones.get(i), y = b.colocaciones.get(i);
            if (x.objeto != y.objeto || x.x != y.x || x.y != y.y || x.capa != y.capa) return false;
        }
        return true;
    }
}
