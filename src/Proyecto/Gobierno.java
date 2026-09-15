package Proyecto;

import java.util.ArrayList;
import java.util.List;

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
}
