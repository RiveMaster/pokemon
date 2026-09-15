package Proyecto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rival implements Serializable {
    private static final long serialVersionUID = 1L;


    private String nombre;
    private PokemonLuchador pokemon;
    private List<PokemonLuchador> equipoRival = new ArrayList<>();

    private static List<List<PokemonLuchador>> equiposPredefinidos = new ArrayList<>();


        // Equipo 1: Fuego
    private static Equipo1() {

        List<PokemonLuchador> equipo1 = new ArrayList<>();
        equipo1.add(new PokemonLuchador(Pokedex.getPikachu(), 10));
        equipo1.add(new PokemonLuchador(getPokemon().getBase(),10));
    }
    private static Equipo2() {

        List<PokemonLuchador> equipo2 = new ArrayList<>();
        equipo2.add(new PokemonLuchador(getPokemon().getBase(),30));
        equipo2.add(new PokemonLuchador(Pokedex.getMeowth(),25));
        equipo2.add(new PokemonLuchador(Pokedex.getSpearow(),25));
        equipo2.add(new PokemonLuchador(Pokedex.getSandshrew(),25));
    }
    private static Equipo3(){
        List<PokemonLuchador> equipo3 = new ArrayList<>();
        equipo3.add(new PokemonLuchador(getPokemon().getBase(),60));
        equipo3.add(new PokemonLuchador(Pokedex.getPidgeot(),55));
        equipo3.add(new PokemonLuchador(Pokedex.getAlakazam(),55));
        equipo3.add(new PokemonLuchador(Pokedex.getGyarados(),55));
        equipo3.add(new PokemonLuchador(Pokedex.getExeggutor(),55));
        equipo3.add(new PokemonLuchador(Pokedex.getArcanine(),55));


    }


    public Rival(String nombre, PokemonLuchador pokemonInicial) {
        this.nombre = nombre;
        this.pokemon = pokemonInicial;
    }

    // -------- GETTERS --------


    public String getNombre() {
        return nombre;
    }

    public PokemonLuchador getPokemon() {
        return pokemon;
    }

    public List<PokemonLuchador> getEquipoRival() {
        return equipoRival;
    }

    // -------- SETTERS --------


    public void setPokemon(PokemonLuchador pokemon) {
        this.pokemon = pokemon;
    }


    public boolean equipoRVM() {
        for (int i = 0; i < equipoRival.size(); i++) {
            if (equipoRival.get(i).estaVivo()) {
                return true;
            }
        }
        return false;
    }


    public void setEquipoRival1(List<PokemonLuchador> equipoRival) {
        this.equipoRival = equipoRival;
    }

    public void agregarPokemonAlEquipo(PokemonLuchador nuevoPokemon) {
        this.equipoRival.add(nuevoPokemon);
    }
}