package Proyecto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class PokemonBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String tipo1;
    private String tipo2;


    private int vidaBase, ataqueBase, defensaBase, velocidadBase;
    private int nivelEvolucion;
    private String evolucion;
    private List<movimiento> movimientosPorNivel;
    private HashMap<String, Double> efectos;
    private HashMap<String, String> efectosPasivos;

    public PokemonBase(String nombre, String tipo1, String tipo2, int vidaBase, int ataqueBase, int defensaBase, int velocidadBase, int nivelEvolucion, String evolucion, List<movimiento> movimientosPorNivel) {


        this.nombre = nombre;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.vidaBase = vidaBase;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.velocidadBase = velocidadBase;
        this.nivelEvolucion = nivelEvolucion;
        this.evolucion = evolucion;
        this.movimientosPorNivel = movimientosPorNivel;
        this.efectos = new HashMap<String, Double>();
        efectos.put("Congelar",4.8);
        efectos.put("Fuego",10.0);
        efectos.put("Paralizar",5.0);
        efectos.put("Envenenado",12.0);
        efectos.put("Dormido",0.0);
        this.efectosPasivos =new HashMap<String, String>();
        efectosPasivos.put("Paralisis","paralizar al enemigo");
        efectosPasivos.put("Congelacion","congelar al enemigo");
        efectosPasivos.put("Fuego","quemar al enemigo");
        efectosPasivos.put("Envenenado","enveneno al enemigo");
        efectosPasivos.put("Dormido","hace que el pokemon se duerma");
        }


        public String getNombre() {
            return nombre;
        }


        public List<movimiento> getMovimientosPorNivel() {
            return movimientosPorNivel;
        }


        public int getNivelEvolucion() {
            return nivelEvolucion;
        }


        public String getEvolucion() {
            return evolucion;
        }

        public String getbuscar(String buscar){
        return String.valueOf(buscar.equals(nombre));
        }

        public int getVidaBase() {
            return vidaBase;
        }


        public int getAtaqueBase() {
            return ataqueBase;
        }


        public int getDefensaBase() {
            return defensaBase;
        }


        public int getVelocidadBase() {
            return velocidadBase;
        }


}

