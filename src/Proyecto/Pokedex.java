package Proyecto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pokedex {
    private static List<PokemonBase> pokemons = new ArrayList<>();
    private static boolean cargado = false;

    public void cargarpokemons() {
        pokemons.add(new PokemonBase("Charmander", "Fuego", null, 39, 52, 43, 65, 16, "Charmeleon",
                Arrays.asList(
                        new movimiento("Arañazo", 40, "Normal", 1),
                        new movimiento("Ascuas", 40, "Fuego", 7),
                        new movimiento("Garra Metal", 50, "Acero", 13),
                        new movimiento("Lanzallamas", 90, "Fuego", 24)
                )));
        pokemons.add(new PokemonBase("Bulbasaur", "Planta", "Veneno", 45, 49, 49, 45, 16, "Ivysaur",
                Arrays.asList(
                        new movimiento("Placaje", 40, "Normal", 1),
                        new movimiento("Látigo Cepa", 45, "Planta", 7),
                        new movimiento("Drenadoras", 0, "Planta", 10),
                        new movimiento("Gigadrenado", 75, "Planta", 20))));
        pokemons.add(new PokemonBase("Squirtle", "Agua", null, 44, 48, 65, 43, 16, "Wartortle",
                Arrays.asList(
                        new movimiento("Placaje", 40, "Normal", 1),
                        new movimiento("Pistola Agua", 40, "Agua", 7),
                        new movimiento("Rizo Defensa", 0, "Normal", 10),
                        new movimiento("Hidropulso", 60, "Agua", 18)
                )));
        pokemons.add(new PokemonBase("Bunnelby", "Normal", null, 50, 55, 40, 50, 20, "Diggersby",
                Arrays.asList(
                        new movimiento("Arañazo", 40, "Normal", 1),
                        new movimiento("Placaje", 35, "Normal", 5),
                        new movimiento("Golpe Cabeza", 70, "Normal", 12)
                )));
        pokemons.add(new PokemonBase("Pidgey", "Normal", "Volador", 40, 45, 40, 56, 18, "Pidgeotto",
                Arrays.asList(
                        new movimiento("Picotazo", 35, "Normal", 1),
                        new movimiento("Ataque Ala", 60, "Volador", 7),
                        new movimiento("Tornado", 40, "Volador", 15)
                )));
        pokemons.add(new PokemonBase("Shinx", "Eléctrico", null, 45, 65, 34, 45, 15, "Luxio",
                Arrays.asList(
                        new movimiento("Impactrueno", 40, "Eléctrico", 1),
                        new movimiento("Placaje", 35, "Normal", 4),
                        new movimiento("Rayo", 65, "Eléctrico", 20)
                )));
        pokemons.add(new PokemonBase("Mewtow", "Psíquico", null, 106, 110, 90, 130, 0, null,
                Arrays.asList(
                        new movimiento("Psíquico", 90, "Psíquico", 1),
                        new movimiento("Confusión", 50, "Psíquico", 1),
                        new movimiento("Onda Mental", 80, "Psíquico", 20),
                        new movimiento("Impactrueno", 40, "Eléctrico", 25)
                )));
        pokemons.add(new PokemonBase("Mew", "Psíquico", null, 100, 100, 100, 100, 0, null,
                Arrays.asList(
                        new movimiento("Psíquico", 90, "Psíquico", 1),
                        new movimiento("Transformación", 0, "Normal", 1),
                        new movimiento("Confusión", 50, "Psíquico", 5),
                        new movimiento("Onda Mental", 80, "Psíquico", 20)
                )));
        pokemons.add(new PokemonBase("Dedenne", "Eléctrico", "Hada",
                67, 58, 57, 101,
                0, null,
                Arrays.asList(
                        new movimiento("Impactrueno", 40, "Eléctrico", 1),
                        new movimiento("Beso Drenaje", 50, "Hada", 5),
                        new movimiento("Rayo", 90, "Eléctrico", 20),
                        new movimiento("Chispa", 65, "Eléctrico", 10)
                )));
        pokemons.add(new PokemonBase("Nidoran♂", "Veneno", null,
                46, 57, 40, 50,
                0, "Nidorino",
                Arrays.asList(
                        new movimiento("Picotazo Veneno", 35, "Veneno", 1),
                        new movimiento("Arañazo", 40, "Normal", 1),
                        new movimiento("Doble Patada", 30, "Lucha", 10),
                        new movimiento("Cornada", 65, "Normal", 13)
                )));
        pokemons.add(new PokemonBase("Nidoran♀", "Veneno", null,
                55, 47, 52, 41,
                0, "Nidorina",
                Arrays.asList(
                        new movimiento("Picotazo Veneno", 35, "Veneno", 1),
                        new movimiento("Placaje", 40, "Normal", 1),
                        new movimiento("Doble Patada", 30, "Lucha", 12),
                        new movimiento("Cornada", 65, "Normal", 15)
                )));
        pokemons.add(new PokemonBase("Budew", "Planta", "Veneno",
                40, 30, 35, 55,
                0, "Roselia",
                Arrays.asList(
                        new movimiento("Absorber", 20, "Planta", 1),
                        new movimiento("Drenadoras", 0, "Planta", 5),
                        new movimiento("Hoja Afilada", 55, "Planta", 10),
                        new movimiento("Polvo Veneno", 0, "Veneno", 7)
                )));
        pokemons.add(new PokemonBase("Togepi", "Hada", null,
                35, 20, 65, 20,
                0, "Togetic",
                Arrays.asList(
                        new movimiento("Destructor", 40, "Normal", 1),
                        new movimiento("Deseo", 0, "Normal", 5),
                        new movimiento("Encanto", 0, "Hada", 1),
                        new movimiento("Beso Drenaje", 50, "Hada", 12)
                )));
        pokemons.add(new PokemonBase("Ambipom", "Normal", null,
                75, 100, 66, 115,
                0, null,
                Arrays.asList(
                        new movimiento("Arañazo", 40, "Normal", 1),
                        new movimiento("Golpe Furia", 15, "Normal", 5),
                        new movimiento("Persecución", 40, "Siniestro", 10),
                        new movimiento("Doble Golpe", 35, "Normal", 15)
                )));
        pokemons.add(new PokemonBase("Bulbasaur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ivysaur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Venusaur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Charmander",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Charmeleon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Charizard",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Squirtle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wartortle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Blastoise",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Caterpie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Metapod",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Butterfree",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Weedle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kakuna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Beedrill",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pidgey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pidgeotto",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pidgeot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rattata",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Raticate",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spearow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fearow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ekans",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arbok",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pikachu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Raichu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sandshrew",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sandslash",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nidoran♀",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nidorina",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nidoqueen",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nidoran♂",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nidorino",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nidoking",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clefairy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clefable",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vulpix",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ninetales",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jigglypuff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wigglytuff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zubat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Golbat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Oddish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gloom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vileplume",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Paras",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Parasect",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Venonat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Venomoth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Diglett",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dugtrio",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meowth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Persian",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Psyduck",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Golduck",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mankey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Primeape",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Growlithe",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arcanine",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Poliwag",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Poliwhirl",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Poliwrath",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Abra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kadabra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Alakazam",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Machop",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Machoke",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Machamp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bellsprout",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Weepinbell",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Victreebel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tentacool",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tentacruel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Geodude",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Graveler",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Golem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ponyta",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rapidash",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slowpoke",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slowbro",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magnemite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magneton",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Farfetch'd",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Doduo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dodrio",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Seel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dewgong",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grimer",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Muk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shellder",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cloyster",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gastly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Haunter",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gengar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Onix",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drowzee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hypno",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Krabby",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kingler",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Voltorb",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Electrode",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Exeggcute",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Exeggutor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cubone",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Marowak",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hitmonlee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hitmonchan",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lickitung",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Koffing",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Weezing",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rhyhorn",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rhydon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chansey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tangela",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kangaskhan",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Horsea",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Seadra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Goldeen",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Seaking",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Staryu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Starmie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mr. Mime",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scyther",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jynx",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Electabuzz",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magmar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pinsir",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tauros",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magikarp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gyarados",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lapras",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ditto",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Eevee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vaporeon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jolteon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flareon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Porygon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Omanyte",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Omastar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kabuto",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kabutops",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aerodactyl",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Snorlax",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Articuno",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zapdos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Moltres",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dratini",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dragonair",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dragonite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mewtwo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mew",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chikorita",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bayleef",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meganium",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cyndaquil",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Quilava",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Typhlosion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Totodile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Croconaw",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Feraligatr",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sentret",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Furret",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hoothoot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Noctowl",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ledyba",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ledian",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spinarak",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ariados",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Crobat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chinchou",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lanturn",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pichu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cleffa",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Igglybuff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Togepi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Togetic",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Natu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Xatu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mareep",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flaaffy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ampharos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bellossom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Marill",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Azumarill",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sudowoodo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Politoed",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hoppip",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skiploom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jumpluff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aipom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sunkern",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sunflora",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Yanma",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wooper",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Quagsire",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Espeon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Umbreon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Murkrow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slowking",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Misdreavus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Unown",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wobbuffet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Girafarig",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pineco",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Forretress",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dunsparce",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gligar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Steelix",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Snubbull",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Granbull",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Qwilfish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scizor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shuckle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Heracross",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sneasel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Teddiursa",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ursaring",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slugma",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magcargo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swinub",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Piloswine",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Corsola",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Remoraid",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Octillery",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Delibird",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mantine",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skarmory",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Houndour",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Houndoom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kingdra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Phanpy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Donphan",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Porygon2",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stantler",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Smeargle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tyrogue",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hitmontop",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Smoochum",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Elekid",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magby",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Miltank",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Blissey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Raikou",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Entei",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Suicune",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Larvitar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pupitar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tyranitar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lugia",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ho-oh",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Celebi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Treecko",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grovyle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sceptile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Torchic",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Combusken",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Blaziken",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mudkip",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Marshtomp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swampert",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Poochyena",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mightyena",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zigzagoon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Linoone",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wurmple",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Silcoon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Beautifly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cascoon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dustox",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lotad",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lombre",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ludicolo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Seedot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nuzleaf",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shiftry",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Taillow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swellow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wingull",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pelipper",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ralts",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kirlia",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gardevoir",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Surskit",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Masquerain",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shroomish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Breloom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slakoth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vigoroth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slaking",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nincada",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ninjask",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shedinja",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Whismur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Loudred",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Exploud",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Makuhita",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hariyama",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Azurill",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nosepass",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skitty",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Delcatty",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sableye",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mawile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aron",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lairon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aggron",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meditite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Medicham",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Electrike",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Manectric",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Plusle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Minun",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Volbeat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Illumise",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Roselia",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gulpin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swalot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Carvanha",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sharpedo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wailmer",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wailord",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Numel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Camerupt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Torkoal",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spoink",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grumpig",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spinda",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Trapinch",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vibrava",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flygon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cacnea",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cacturne",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swablu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Altaria",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zangoose",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Seviper",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lunatone",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Solrock",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Barboach",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Whiscash",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Corphish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Crawdaunt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Baltoy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Claydol",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lileep",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cradily",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Anorith",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Armaldo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Feebas",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Milotic",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Castform",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kecleon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shuppet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Banette",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Duskull",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dusclops",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tropius",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chimecho",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Absol",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wynaut",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Snorunt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Glalie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spheal",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sealeo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Walrein",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clamperl",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Huntail",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gorebyss",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Relicanth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Luvdisc",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bagon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shelgon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Salamence",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Beldum",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Metang",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Metagross",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Regirock",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Regice",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Registeel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Latias",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Latios",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kyogre",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Groudon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rayquaza",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jirachi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Deoxys",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Turtwig",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grotle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Torterra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chimchar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Monferno",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Infernape",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Piplup",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Prinplup",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Empoleon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Starly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Staravia",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Staraptor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bidoof",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bibarel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kricketot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kricketune",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shinx",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Luxio",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Luxray",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Budew",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Roserade",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cranidos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rampardos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shieldon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bastiodon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Burmy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wormadam",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mothim",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Combee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vespiquen",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pachirisu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Buizel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Floatzel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cherubi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cherrim",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shellos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gastrodon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ambipom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drifloon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drifblim",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Buneary",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lopunny",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mismagius",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Honchkrow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Glameow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Purugly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chingling",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stunky",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skuntank",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bronzor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bronzong",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bonsly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mime Jr.",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Happiny",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chatot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spiritomb",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gible",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gabite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Garchomp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Munchlax",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Riolu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lucario",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hippopotas",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hippowdon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skorupi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drapion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Croagunk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toxicroak",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Carnivine",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Finneon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lumineon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mantyke",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Snover",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Abomasnow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Weavile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magnezone",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lickilicky",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rhyperior",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tangrowth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Electivire",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magmortar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Togekiss",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Yanmega",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Leafeon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Glaceon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gliscor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mamoswine",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Porygon-Z",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gallade",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Probopass",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dusknoir",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Froslass",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rotom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Uxie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mesprit",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Azelf",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dialga",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Palkia",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Heatran",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Regigigas",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Giratina",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cresselia",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Phione",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Manaphy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Darkrai",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shaymin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arceus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Victini",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Snivy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Servine",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Serperior",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tepig",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pignite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Emboar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Oshawott",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dewott",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Samurott",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Patrat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Watchog",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lillipup",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Herdier",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stoutland",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Purrloin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Liepard",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pansage",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Simisage",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pansear",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Simisear",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Panpour",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Simipour",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Munna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Musharna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pidove",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tranquill",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Unfezant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Blitzle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zebstrika",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Roggenrola",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Boldore",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gigalith",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Woobat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swoobat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drilbur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Excadrill",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Audino",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Timburr",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gurdurr",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Conkeldurr",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tympole",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Palpitoad",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Seismitoad",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Throh",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sawk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sewaddle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swadloon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Leavanny",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Venipede",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Whirlipede",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scolipede",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cottonee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Whimsicott",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Petilil",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lilligant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Basculin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sandile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Krokorok",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Krookodile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Darumaka",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Darmanitan",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Maractus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dwebble",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Crustle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scraggy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scrafty",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sigilyph",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Yamask",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cofagrigus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tirtouga",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Carracosta",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Archen",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Archeops",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Trubbish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Garbodor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zorua",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zoroark",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Minccino",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cinccino",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gothita",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gothorita",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gothitelle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Solosis",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Duosion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Reuniclus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ducklett",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swanna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vanillite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vanillish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vanilluxe",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Deerling",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sawsbuck",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Emolga",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Karrablast",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Escavalier",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Foongus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Amoonguss",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Frillish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jellicent",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Alomomola",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Joltik",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Galvantula",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ferroseed",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ferrothorn",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Klink",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Klang",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Klinklang",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tynamo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Eelektrik",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Eelektross",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Elgyem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Beheeyem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Litwick",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lampent",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chandelure",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Axew",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fraxure",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Haxorus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cubchoo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Beartic",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cryogonal",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shelmet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Accelgor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stunfisk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mienfoo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mienshao",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Druddigon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Golett",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Golurk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pawniard",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bisharp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bouffalant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rufflet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Braviary",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vullaby",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mandibuzz",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Heatmor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Durant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Deino",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zweilous",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hydreigon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Larvesta",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Volcarona",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cobalion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Terrakion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Virizion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tornadus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Thundurus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Reshiram",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zekrom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Landorus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kyurem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Keldeo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meloetta",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Genesect",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chespin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Quilladin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chesnaught",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fennekin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Braixen",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Delphox",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Froakie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Frogadier",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Greninja",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bunnelby",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Diggersby",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fletchling",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fletchinder",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Talonflame",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scatterbug",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spewpa",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vivillon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Litleo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pyroar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flabébé",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Floette",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Florges",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skiddo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gogoat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pancham",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pangoro",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Furfrou",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Espurr",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meowstic",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Honedge",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Doublade",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aegislash",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spritzee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aromatisse",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Swirlix",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slurpuff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Inkay",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Malamar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Binacle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Barbaracle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skrelp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dragalge",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clauncher",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clawitzer",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Helioptile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Heliolisk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tyrunt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tyrantrum",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Amaura",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Aurorus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sylveon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hawlucha",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dedenne",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Carbink",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Goomy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sliggoo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Goodra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Klefki",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Phantump",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Trevenant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pumpkaboo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gourgeist",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bergmite",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Avalugg",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Noibat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Noivern",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Xerneas",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Yveltal",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zygarde",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Diancie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hoopa",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Volcanion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rowlet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dartrix",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Decidueye",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Litten",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Torracat",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Incineroar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Popplio",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Brionne",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Primarina",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pikipek",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Trumbeak",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toucannon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Yungoos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gumshoos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grubbin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Charjabug",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Vikavolt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Crabrawler",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Crabominable",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Oricorio",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cutiefly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ribombee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rockruff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lycanroc",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wishiwashi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mareanie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toxapex",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mudbray",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mudsdale",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dewpider",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Araquanid",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fomantis",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lurantis",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Morelull",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shiinotic",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Salandit",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Salazzle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stufful",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bewear",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bounsweet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Steenee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tsareena",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Comfey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Oranguru",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Passimian",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wimpod",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Golisopod",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sandygast",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Palossand",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pyukumuku",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Type: Null",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Silvally",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Minior",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Komala",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Turtonator",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Togedemaru",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mimikyu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bruxish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drampa",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dhelmise",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Jangmo-o",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hakamo-o",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kommo-o",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tapu Koko",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tapu Lele",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tapu Bulu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tapu Fini",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cosmog",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cosmoem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Solgaleo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lunala",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nihilego",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Buzzwole",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pheromosa",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Xurkitree",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Celesteela",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kartana",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Guzzlord",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Necrozma",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Magearna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Marshadow",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Poipole",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Naganadel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stakataka",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Blacephalon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zeraora",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meltan",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Melmetal",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grookey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Thwackey",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rillaboom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scorbunny",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Raboot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cinderace",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sobble",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drizzile",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Inteleon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skwovet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Greedent",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rookidee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Corvisquire",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Corviknight",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Blipbug",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dottler",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Orbeetle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nickit",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Thievul",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gossifleur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Eldegoss",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wooloo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dubwool",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chewtle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drednaw",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Yamper",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Boltund",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rolycoly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Carkol",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Coalossal",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Applin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flapple",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Appletun",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Silicobra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sandaconda",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cramorant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arrokuda",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Barraskewda",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toxel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toxtricity",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sizzlipede",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Centiskorch",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clobbopus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grapploct",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sinistea",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Polteageist",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hatenna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hattrem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hatterene",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Impidimp",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Morgrem",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grimmsnarl",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Obstagoon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Perrserker",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cursola",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sirfetch'd",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mr. Rime",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Runerigus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Milcery",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Alcremie",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Falinks",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pincurchin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Snom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Frosmoth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Stonjourner",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Eiscue",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Indeedee",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Morpeko",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cufant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Copperajah",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dracozolt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arctozolt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dracovish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arctovish",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Duraludon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dreepy",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Drakloak",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dragapult",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zacian",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zamazenta",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Eternatus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kubfu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Urshifu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Zarude",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Regieleki",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Regidrago",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Glastrier",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spectrier",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Calyrex",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wyrdeer",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kleavor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ursaluna",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Basculegion",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sneasler",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Overqwil",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Enamorus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sprigatito",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Floragato",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Meowscarada",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fuecoco",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Crocalor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Skeledirge",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Quaxly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Quaxwell",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Quaquaval",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lechonk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Oinkologne",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tarountula",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Spidops",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nymble",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Lokix",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pawmi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pawmo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pawmot",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tandemaus",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Maushold",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fidough",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dachsbun",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Smoliv",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dolliv",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arboliva",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Squawkabilly",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Nacli",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Naclstack",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Garganacl",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Charcadet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Armarouge",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ceruledge",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tadbulb",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bellibolt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wattrel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kilowattrel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Maschiff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Mabosstiff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Shroodle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Grafaiai",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bramblin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Brambleghast",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toedscool",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Toedscruel",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Klawf",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Capsakid",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scovillain",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rellor",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Rabsca",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flittle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Espathra",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tinkatink",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tinkatuff",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tinkaton",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wiglett",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wugtrio",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Bombirdier",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Finizen",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Palafin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Varoom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Revavroom",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cyclizar",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Orthworm",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Glimmet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Glimmora",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Greavard",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Houndstone",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flamigo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cetoddle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Cetitan",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Veluza",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dondozo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Tatsugiri",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Annihilape",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Clodsire",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Farigiraf",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dudunsparce",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Kingambit",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Great Tusk",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Scream Tail",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Brute Bonnet",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Flutter Mane",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Slither Wing",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sandy Shocks",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Treads",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Bundle",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Hands",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Jugulis",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Moth",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Thorns",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Frigibax",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Arctibax",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Baxcalibur",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gimmighoul",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gholdengo",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Wo-Chien",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chien-Pao",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ting-Lu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Chi-Yu",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Roaring Moon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Valiant",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Koraidon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Miraidon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Walking Wake",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Leaves",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Dipplin",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Poltchageist",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Sinistcha",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Okidogi",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Munkidori",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Fezandipiti",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Ogerpon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Archaludon",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Hydrapple",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Gouging Fire",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Raging Bolt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Boulder",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Iron Crown",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Terapagos",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        pokemons.add(new PokemonBase("Pecharunt",null,null,
                50,50,50,50,0,null,
                Arrays.asList(  new movimiento("Placaje",40,"Normal",1),
                        new movimiento("Latigo Cepa",45,"Planta",0),
                        new movimiento("Ataque Rápido",40,"Normal",10),
                        new movimiento("Golpe Furia",35,"Normal",15)
                )));
        //pokemons de gobierno
        //estos los de pedrito
        pokemons.add(new PokemonBase("Hacienda","Politica","Ladron",
                100,0,1000,50,0,null,
                Arrays.asList(
                        new movimiento("Robo",0,"Dinero",0),
                        new movimiento("Embargacion",0,"Robo",0),
                        new movimiento("Impuestodevida",0,"Curacion",0),
                        new movimiento("Subirimpuestos",0,"Antidefensa(baja la defensa 15 punticos)",0)
                )));
        pokemons.add(new PokemonBase("BarcelonaLore","Inmigracion","Ladron",
                100,30,0,90,0,null,
                Arrays.asList(
                        new movimiento("SegarroAmigo",40,"Metal",0),
                        new movimiento("Independencia",0,"Usa un ataque aleatorio del rival",0),
                        new movimiento("MDLR axe",10,"veneno",0)
                )));
        pokemons.add(new PokemonBase("Familia","Diputados","",400,15,0,0,0,null,
                Arrays.asList(
                        new movimiento("Hermano",5,"confusion",0),
                        new movimiento("Esposa",10,"confusion",0),
                        new movimiento("Koldo",10,"confusion",0)
                )));
        pokemons.add(new PokemonBase("Africa","","",50,0,0,70,0,null,
                Arrays.asList(
                        new movimiento("Patera",70,"Fisico",0),
                        new movimiento("Salto",0,"Evade el sigiente ataque",0),
                        new movimiento("Musulman",100,"Explosion",0),
                        new movimiento("Ascuas",40,"Fuego",0)
                )));
        pokemons.add(new PokemonBase("","","",0,0,0,0,0,null,
                Arrays.asList(
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0)
                )));
        pokemons.add(new PokemonBase("","","",0,0,0,0,0,null,
                Arrays.asList(
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0)
                )));

//esto es lo que se usa que si no pereza...
        pokemons.add(new PokemonBase("","","",0,0,0,0,0,null,
                Arrays.asList(
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0),
                        new movimiento("",0,"",0)
                )));

    }

    public static PokemonBase getByName(String name) {
        PokemonBase pokemon = buscarPorNombre(name);
        if (pokemon == null) {
            System.out.println("Advertencia: No se encontró Pokémon con nombre '" + name + "' en Pokedex.");
        }
        return pokemon;
    }
    private static void cargarSiHaceFalta() {
        if (cargado) return;
        for (java.lang.reflect.Method m : Pokedex.class.getDeclaredMethods()) {
            if (m.getParameterCount() == 0
                    && m.getReturnType() == PokemonBase.class
                    && m.getName().startsWith("get")
                    && !m.getName().equals("getByName")) {
                try {
                    PokemonBase p = (PokemonBase) m.invoke(null);
                    if (p != null && p.getNombre() != null && !p.getNombre().isEmpty()) {
                        pokemons.add(p);
                    }
                } catch (Exception e) {
                    // Si algún getXxx() falla, lo ignoramos y seguimos con el resto.
                }
            }
        }
        cargado = true;
    }

    public static PokemonBase buscarPorNombre(String nombre) {
        cargarSiHaceFalta();
        for (PokemonBase p : pokemons) {
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }

   /* public static PokemonBase buscarPorNombreStatic(String nombre) {
        switch (nombre.toLowerCase()) {
            case "charmander":
                return getCharmander();
            case "bulbasaur":
                return getBulbasaur();
            case "squirtle":
                return getSquirtle();
            case "bunnelby":
                return getBunnelby();
            case "pidgey":
                return getPidgey();
            case "shinx":
                return getShinx();
            case "mew":
                return getMew();
            case "dedenne":
                return getDedenne();
            case "nidoran♀":
                return getNidorana();
            case "budew":
                return getBudew();
            case "togepi":
                return getTogepi();
            case "ambipom":
                return getAmbipom();
            default:
                return null;
        }

    }*/
}