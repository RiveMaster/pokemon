package Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Gobierno {

    private String nombre="";
    private List<PokemonLuchador> ministerio = new ArrayList<>();

    private int podemita=0;
    private int femini=1;
    private int afuera=2;
    private int perroS=3;


    public void podemita(){

    }
    public boolean ministerioVM(){
        for (int i=0;i< ministerio.size();i++) {
            if (ministerio.get(i).estaVivo()){
                return true;
            }
        }
        return false;
    }

    public List<PokemonLuchador> getministerio() {
        return ministerio;
    }

    // =====================================================
    // ========   JEFES DEL GOBIERNO (uno por generación) ==
    // =====================================================
    // Son personajes FICTICIOS que parodian arquetipos de la burocracia y la política en general.
    // Sus "memes" son frases inventadas para el juego. Para cambiar nombres, cargos o frases basta
    // con editar la tabla JEFES. Al derrotar al jefe de la generación actual se desbloquea la siguiente.

    public static class Jefe {
        final String nombre;
        final String cargo;
        final Set<String> tipos;
        final int nivelBase;
        final String[] memes;
        final String derrota;

        Jefe(String nombre, String cargo, Set<String> tipos, int nivelBase, String derrota, String... memes) {
            this.nombre = nombre;
            this.cargo = cargo;
            this.tipos = tipos;
            this.nivelBase = nivelBase;
            this.derrota = derrota;
            this.memes = memes;
        }

        public String getNombre() { return nombre; }
    }

    private static final int TAMANO_EQUIPO_JEFE = 5;
    private static final int RECOMPENSA_JEFE = 3000;

    private static final Jefe[] JEFES = {
        new Jefe("Don Anselmo Papeleo", "Ministro de Trámites Eternos", Generacion.tipos("Normal", "Poison"), 36,
                "Está bien... le firmo el papel. Pero necesitaré una copia sellada.",
                "Vuelva usted mañana.", "Para eso necesita la casilla 27-B, que no existe.", "Su expediente está en trámite. Desde 1998."),
        new Jefe("Doña Remedios Rotonda", "Concejala de Rotondas y Aceras", Generacion.tipos("Steel", "Normal"), 36,
                "Cuánto ruido por una rotondita más...",
                "¡Una rotonda más y se acaba el tráfico!", "La obra terminará en breve. (Lleva doce años.)", "Hemos puesto una rotonda para que dé tiempo a pensar."),
        new Jefe("Ramiro Obraseterna", "Delegado de Obras Interminables", Generacion.tipos("Ground", "Rock", "Steel"), 36,
                "No pasa nada, la próxima obra ya la inauguramos otro año.",
                "Vamos con retraso, pero con mucha ilusión.", "El cartel de la obra lleva más años que la obra.", "Desviamos el tráfico por donde ya estaba cortado."),
        new Jefe("Gervasio Comisiones", "Secretario de Chiringuitos Públicos", Generacion.tipos("Dark", "Ghost"), 37,
                "Yo esto no lo he firmado: lo firmó mi cuñado.",
                "Fue un error contable, un error de más de mil millones.", "Ese chiringuito tiene mucho futuro. Y mucho barra libre.", "Tranquilo, es un asunto entre amigos."),
        new Jefe("Pilar Enchufe", "Directora General de Nombramientos", Generacion.tipos("Psychic", "Dark"), 37,
                "Bueno, me marcho... pero mi sobrino se queda con mi puesto.",
                "Mi sobrino está muy cualificado. Tiene un título. Creo.", "Hay muchas plazas, pero pocas con mi apellido.", "En mi departamento todos somos familia. Literalmente."),
        new Jefe("Eusebio Portavoz", "Portavoz del Sí pero No", Generacion.tipos("Poison", "Fairy"), 38,
                "Se ha sacado de contexto. Igual que todo lo que digo.",
                "Yo no he dicho lo que dije, lo que dije fue lo que quise decir.", "Rueda de prensa sin preguntas. Gracias por venir.", "Eso lo dirá el presidente. O yo, otro día."),
        new Jefe("Amparo Sobres", "Consejera de Asuntos Discretos", Generacion.tipos("Dragon", "Steel"), 38,
                "Guárdese el sobre, que me lo van a echar en cara.",
                "Ese sobre solo contenía felicitaciones.", "Mis cuentas están claras: nadie las ha visto.", "No recuerdo nada, qué desmemoria la mía."),
        new Jefe("Baldomero Recortes", "Ministro de Austeridad y Comida Gratis", Generacion.tipos("Fighting", "Dark"), 39,
                "Habrá que recortar... en mi despacho, no.",
                "Hay que apretarse el cinturón (el de los demás).", "No hay dinero para hospitales, pero sí para la cena de Navidad.", "Mi coche oficial es de bajo consumo: consume poco, cuesta mucho."),
        new Jefe("Genoveva Tertulia", "Asesora de Comunicación y Ruido", Generacion.tipos("Ghost", "Psychic"), 39,
                "Mi opinión no cambia... pero hoy me han dado la contraria.",
                "Aquí venimos a hablar de lo nuestro. De lo tuyo, ni una palabra.", "Voy a ser muy breve: tres horas.", "Cuñado, deja de hablar. Es mi turno."),
        new Jefe("El Presidente Cuñado Supremo", "Presidente del Gobierno de la Mezcla", Generacion.tipos("Dark", "Dragon", "Psychic", "Ghost", "Steel"), 40,
                "Está bien... el pueblo ha hablado. Y yo he dejado de escuchar.",
                "Yo de esto sé mucho, aunque no lo parezca.", "Cuando gobierne tú, lo hablamos.", "Todo el mundo tiene un cuñado. Yo soy el de todos."),
    };

    public static Jefe getJefe(Generacion gen) {
        return JEFES[gen.getNumero() - 1];
    }

    private static final Random RAND = new Random();

    /** La sede del Gobierno: se entra con las 5 medallas de la región y se desafía al jefe de esa generación. */
    public static void visitarSede(Scanner sc, Jugador jugador) {
        Generacion gen = jugador.getGeneracion();
        Jefe jefe = getJefe(gen);
        int total = Gimnasio.getGimnasios().size();
        int medallas = jugador.medallasEnGeneracion(gen);

        System.out.println("\n=== SEDE DEL GOBIERNO DE " + gen.getRegion().toUpperCase() + " ===");
        System.out.println("Jefe: " + jefe.nombre + ", " + jefe.cargo + ".");

        if (jugador.jefeDerrotado(gen)) {
            System.out.println(jefe.nombre + ": " + jefe.memes[RAND.nextInt(jefe.memes.length)]);
            System.out.println("(Ya has derrotado al jefe de esta generación. Los guardias no te dejan volver a pasar.)");
            return;
        }
        if (medallas < total) {
            System.out.println("El portero: Sin las " + total + " medallas de " + gen.getRegion() + " no pasas. Tienes " + medallas + ".");
            System.out.println("Portero: Y vuelva usted mañana.");
            return;
        }
        if (!jugador.equipoVM()) {
            System.out.println("Todos tus Pokémon están debilitados. Ve antes al Centro Pokémon.");
            return;
        }

        System.out.println("\n" + jefe.nombre + ": Vaya, un entrenador con " + total + " medallas. Escuche mis declaraciones:");
        for (String meme : jefe.memes) {
            System.out.println("  \"" + meme + "\"");
        }

        int nivelMax = jefe.nivelBase + gen.getBonusNivel() + (TAMANO_EQUIPO_JEFE - 1);
        List<PokemonBase> bases = gen.equipoPara(jefe.tipos, nivelMax, TAMANO_EQUIPO_JEFE);
        List<PokemonLuchador> equipo = new ArrayList<>();
        for (int i = 0; i < bases.size(); i++) {
            equipo.add(new PokemonLuchador(bases.get(i), jefe.nivelBase + gen.getBonusNivel() + i, 3));
        }

        if (!CombateEntrenador.combatir(sc, jugador, jefe.nombre, equipo)) {
            System.out.println("\n" + jefe.nombre + ": " + jefe.memes[RAND.nextInt(jefe.memes.length)]);
            System.out.println("Sales de la sede con los Pokémon hechos polvo. Cúralos y vuelve a intentarlo.");
            return;
        }

        System.out.println("\n¡Has derrotado a " + jefe.nombre + "!");
        System.out.println(jefe.nombre + ": " + jefe.derrota);
        jugador.marcarJefeDerrotado(gen);
        int dinero = RECOMPENSA_JEFE + RECOMPENSA_JEFE * (gen.getNumero() - 1) / 4;
        jugador.agregarDinero(dinero);
        System.out.println("💰 Has ganado " + dinero + "€.");

        Generacion siguiente = gen.siguiente();
        if (siguiente == null) {
            System.out.println("\n🏆 ¡Has derrotado al último jefe del Gobierno y completado las 10 generaciones! ¡ENHORABUENA!");
        } else {
            jugador.desbloquearGeneracion(siguiente);
            System.out.println("🌍 ¡Se ha desbloqueado " + siguiente.etiqueta() + "! Puedes viajar allí desde Villaverde.");
        }
    }
}
