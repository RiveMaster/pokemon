package Proyecto.studio.gui;

import Proyecto.studio.DatosCombate;
import Proyecto.studio.DatosReglas;
import Proyecto.studio.Referencias;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/** Reglas globales del mundo (NPC, gimnasios, jefes, Pokémon excluidos). Se aplican al salir de la pestaña. */
final class PanelReglas extends JPanel {
    private final Contexto ctx;
    private final Campo<Double> probabilidad = Campos.decimal(0, 1, 0.05);
    private final Campo<Integer> premio = Campos.entero(0, 1000, 6);
    private final Campo<Integer> miembros = Campos.entero(1, 10, 3);
    private final Campo<Integer> porMiembro = Campos.entero(1, 6, 2);
    private final Campo<Integer> equipoJefe = Campos.entero(1, 6, 5);
    private final Campo<Integer> recompensaJefe = Campos.entero(0, 1_000_000, 3000);
    private final Campo<List<String>> nombres = Campos.lineas(6);
    private final Campo<List<String>> reto = Campos.lineas(6);
    private final Campo<List<String>> derrota = Campos.lineas(6);
    private final Campo<Boolean> eficacia = Campos.check("Aplicar la tabla de tipos (muy eficaz ×2, poco eficaz ×½, inmune ×0)");
    private final Campo<Boolean> efectos = Campos.check("Los movimientos aplican sus estados (dormido, quemado, paralizado…) con su probabilidad");
    private final Campo<Boolean> precision = Campos.check("Los movimientos pueden fallar según su precisión");
    private final Campo<Boolean> pp = Campos.check("Los movimientos gastan PP (al agotarlos se usa Forcejeo; el Centro Pokémon los restaura)");
    private final Campo<Boolean> estadisticas = Campos.check("El daño depende de la categoría (físico/especial) y de ataque/defensa");
    private final Campo<Boolean> habilidades = Campos.check("Las habilidades tienen efecto en combate");
    private final EditorLista excluidos;

    PanelReglas(Contexto ctx) {
        super(new BorderLayout());
        this.ctx = ctx;
        excluidos = new EditorLista("Excluir de salvajes", () -> Opciones.especies(ctx), s -> Opciones.especie(ctx, s));
        Formulario f = new Formulario();
        f.seccion("Combate");
        f.ancho(null, Util.nota("Todas desactivadas = el combate original del juego. Cada regla es independiente."));
        f.ancho(null, eficacia.comp());
        f.ancho(null, efectos.comp());
        f.ancho(null, precision.comp());
        f.ancho(null, pp.comp());
        f.ancho(null, estadisticas.comp());
        f.ancho(null, habilidades.comp());
        f.seccion("Entrenadores NPC");
        f.fila("Probabilidad de que te rete un NPC (0 a 1)", probabilidad);
        f.fila("Premio: dinero por nivel del equipo", premio);
        f.ancho("Nombres de NPC (uno por línea)", nombres.comp());
        f.ancho("Frases al retar (una por línea)", reto.comp());
        f.ancho("Frases al perder (una por línea)", derrota.comp());
        f.seccion("Gimnasios");
        f.fila("Miembros por gimnasio", miembros);
        f.fila("Pokémon por miembro", porMiembro);
        f.seccion("Sede del Gobierno");
        f.fila("Pokémon del jefe", equipoJefe);
        f.fila("Recompensa por vencer al jefe", recompensaJefe);
        f.seccion("Pokémon que nunca salen en rutas, gimnasios ni sedes");
        f.ancho(null, excluidos.comp());
        f.rellenar();

        JButton aplicar = new JButton("Aplicar cambios");
        aplicar.addActionListener(e -> aplicarSiCambio());
        JButton descartar = new JButton("Descartar");
        descartar.addActionListener(e -> cargar());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        botones.add(descartar);
        botones.add(aplicar);
        JScrollPane sp = new JScrollPane(f);
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        cargar();
    }

    void cargar() {
        DatosReglas r = ctx.bd().reglas();
        probabilidad.set(r.probabilidadNpc());
        premio.set(r.premioNpcPorNivel());
        miembros.set(r.miembrosPorGimnasio());
        porMiembro.set(r.pokemonPorMiembro());
        equipoJefe.set(r.tamanoEquipoJefe());
        recompensaJefe.set(r.recompensaJefe());
        nombres.set(r.nombresNpc());
        reto.set(r.frasesReto());
        derrota.set(r.frasesDerrota());
        excluidos.set(r.excluidosSalvajes());
        eficacia.set(r.combate().eficaciaTipos());
        efectos.set(r.combate().efectosMovimientos());
        precision.set(r.combate().precision());
        pp.set(r.combate().pp());
        estadisticas.set(r.combate().estadisticas());
        habilidades.set(r.combate().habilidades());
    }

    DatosReglas leer() {
        return new DatosReglas(probabilidad.get(), nombres.get(), reto.get(), derrota.get(), premio.get(),
                miembros.get(), porMiembro.get(), equipoJefe.get(), recompensaJefe.get(), excluidos.get(),
                new DatosCombate(eficacia.get(), efectos.get(), precision.get(), pp.get(), estadisticas.get(), habilidades.get()));
    }

    /** Aplica el formulario a la base si es distinto (así no marca cambios sin motivo). */
    void aplicarSiCambio() {
        DatosReglas nuevo = leer();
        if (nuevo.equals(ctx.bd().reglas())) return;
        ctx.bd().reglas(nuevo);
        ctx.cambiado();
    }

    EditorLista excluidos() { return excluidos; }
}
