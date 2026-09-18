package Proyecto;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ataques {
    private static final List<Movimiento> movimientos = new ArrayList<>();
    private static Jugador jugador;
    private static void Ataque() {
        movimientos.add(new Movimiento("Destructor", 40, "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Arainazo", 40, "Normal", 1, getnull()));
        movimientos.add(new Movimiento("Karate", 50, "Fighting", 0, getnull()));
        movimientos.add(new Movimiento("Doblebofeton", 15 * (int) (5 * Math.random() + 1), "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Puino cometa", 18 * (int) (5 * Math.random() + 1), "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Mega Puino", 80, "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Dia de pago", jugador.getDinero(), "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Puino Fuego", 75, "Fire", 0, getQuemar10()));
        movimientos.add(new Movimiento("Puino Hielo", 75, "Ice", 0, getCongelar10()));
        movimientos.add(new Movimiento("Agarre", 55, "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Puino Trueno", 75, "Electric", 0, getParalizar10()));
        movimientos.add(new Movimiento("Guillotina", Math.pow(10, 10), "Normal", 0, getInsta1()));
        movimientos.add(new Movimiento("V. Cortante", 80, "Normal", 0, getCarga1()));
        movimientos.add(new Movimiento("Corte", 50, "Normal", 0, getnull())); //cortar arbustos
        movimientos.add(new Movimiento("Ataque Ala", 60, "Volador", 0, getnull()));
        movimientos.add(new Movimiento("Doble Patada", 30 * 2, "Fighting", 0, getnull()));
        movimientos.add(new Movimiento("Mega Patada", 120, "Fighting", 0, getnull()));
        movimientos.add(new Movimiento("Patada Salto", 85, "Fighting", 0, getnull()));
        movimientos.add(new Movimiento("Patada Giro", 60, "Fighting", 0, getnull()));
        movimientos.add(new Movimiento("Portazo", 80, "Normal", 0, getnull()));
        movimientos.add(new Movimiento("Latigo Cepa", 35, "Grass", 0, getnull()));
        movimientos.add(new Movimiento("Tornado", 40, "Flying", 0));
        movimientos.add(new Movimiento("Vuelo", 90, "Flying", 0, getCarga1()));
        movimientos.add(new Movimiento("Atadura", 15, "Normal", 0));
        movimientos.add(new Movimiento("Pisotón", 65, "Normal", 0));
        movimientos.add(new Movimiento("Golpe Cabeza", 70, "Normal", 0));
        movimientos.add(new Movimiento("Cornada", 65, "Normal", 0));
        movimientos.add(new Movimiento("Ataque Furia", 15 * (int) (5 * Math.random() + 2), "Normal", 0));
        movimientos.add(new Movimiento("Perdorador", Math.pow(10, 10), "Normal", 0, getInsta1()));
        movimientos.add(new Movimiento("Placaje", 35, "Normal", 0));
        movimientos.add(new Movimiento("Golpe cuerpo", 85, "Normal", 0, getParalizar30()));
        movimientos.add(new Movimiento("Repeticion", 15, "Normal", 0));
        movimientos.add(new Movimiento("Derribo", 90, "Normal", 0));
        movimientos.add(new Movimiento("Doble Filo", 120, "Normal", 0));
        movimientos.add(new Movimiento("Picotazo Venenoso", 15, "Poison", 0, getveneno30()));
        movimientos.add(new Movimiento("Doble ataque", 25 * 2, "Bug", 0, getveneno20()));
        movimientos.add(new Movimiento("Pin Misil", 14 * (int) (Math.random() * 4 + 2), "Bug", 0));
        movimientos.add(new Movimiento("Mordisco", 60, "Dark", 0));
        movimientos.add(new Movimiento("Canto", 0, "Normal", 0, getSueino()));
        movimientos.add(new Movimiento("Acido", 40, "Poison", 0, getveneno10()));
        movimientos.add(new Movimiento("Ascuas", 40, "Fire", 0, getQuemar10()));
        movimientos.add(new Movimiento("Lanzallamas", 95, "Fire", 0, getQuemar10()));
        movimientos.add(new Movimiento("Pistola Agua", 40, "Water", 0));
        movimientos.add(new Movimiento("Hidro Bomba", 120, "Water", 0));
        movimientos.add(new Movimiento("Surf", 85, "Water", 0));//permite nadar
        movimientos.add(new Movimiento("Rayo Hielo", 95, "Ice", 0, getCongelar10()));
        movimientos.add(new Movimiento("Ventisca", 120, "Ice", 0, getCongelar20()));
        movimientos.add(new Movimiento("Picotazo", 35, "Flying", 0));
        movimientos.add(new Movimiento("Pico Taladro", 80, "Flying", 0));
        movimientos.add(new Movimiento("Sumision", 80, "Fighting", 0));
        movimientos.add(new Movimiento("Psico-Rayo", 65, "Phsyc", 0));
        movimientos.add(new Movimiento("Rayo Burbuja", 65, "Water", 0));
        movimientos.add(new Movimiento("Rayo Aurora", 65, "Ice", 0));
        movimientos.add(new Movimiento("Hiperrayo", 150, "Normal", 0));
        movimientos.add(new Movimiento("Fuerza", 80, "Normal", 0));//Permite mover rocas
        movimientos.add(new Movimiento("Absorber", 20, "Grass", 0));
        movimientos.add(new Movimiento("Megaagotar", 40, "Grass", 0));
        movimientos.add(new Movimiento("Drenadoras", 0, "Grass", 0));
        movimientos.add(new Movimiento("Desarrollo", 0, "Normal", 0));
        movimientos.add(new Movimiento("Rayo Solar", 120, "Grass", 0, getCarga1()));
        movimientos.add(new Movimiento("Polvo Veneno", 0, "Poison", 0, getveneno100()));
        movimientos.add(new Movimiento("Paralizador", 0, "Grass", 0, getParalizar100()));
        movimientos.add(new Movimiento("Somnifero", 0, "Grass", 0, getSueino()));
        movimientos.add(new Movimiento("Danza Petalo", 90, "Grass", 0));
        movimientos.add(new Movimiento("Furia Dragon", 40, "Dragon", 0));
        movimientos.add(new Movimiento("Giro Fuego", 15, "Fire", 0));
        movimientos.add(new Movimiento("Impactrueno", 40, "Electric", 0, getParalizar10()));
        movimientos.add(new Movimiento("Rayo", 95, "Electric", 0, getParalizar10()));
        movimientos.add(new Movimiento("Onda Trueno", 0, "Electric", 0, getParalizar100()));
        movimientos.add(new Movimiento("Trueno", 120, "Electric", 0, getParalizar30()));
        movimientos.add(new Movimiento("Terremoto", 100, "Ground", 0));
        movimientos.add(new Movimiento("Fisura", Math.pow(10, 10), "Ground", 0, getInsta1()));
        movimientos.add(new Movimiento("Excavar", 60, "Ground", 0, getCarga1()));
        movimientos.add(new Movimiento("Toxico", 0, "Poison", 0, getveneno100()));
        movimientos.add(new Movimiento("Confusion", 50, "Phsyc", 0));
        movimientos.add(new Movimiento("Psiquico", 90, "Phsyc", 0));
        movimientos.add(new Movimiento("Hipnosis", 0, "Phsyc", 0, getSueino()));
        movimientos.add(new Movimiento("Furia", 20, "Normal", 0));
        movimientos.add(new Movimiento("Teletransp", 0, "Phsyc", 0));
        movimientos.add(new Movimiento("Lengüetazo", 20, "Ghost", 0, getParalizar30()));
        movimientos.add(new Movimiento("Polucion", 20, "Poison", 0, getveneno30()));
        movimientos.add(new Movimiento("Residuos", 65, "Poison", 0, getveneno30()));
        movimientos.add(new Movimiento("Llamarada", 120, "Fire", 0, getQuemar10()));
        movimientos.add(new Movimiento("Tenaza", 35, "Water", 0));
        movimientos.add(new Movimiento("Rapidez", 60, "Normal", 0));
        movimientos.add(new Movimiento("Cabezazo", 100, "Normal", 0, getCarga1()));
        movimientos.add(new Movimiento("Clavo Cainon", 20, "Normal", 0));
        movimientos.add(new Movimiento("Restriccion", 10, "Normal", 0));
        movimientos.add(new Movimiento("Patada S. Alta", 100, "Fighting", 0));
        movimientos.add(new Movimiento("Deslumbrar", 0, "Normal", 0, getParalizar100()));
        movimientos.add(new Movimiento("Comesueinos", 100, "Phsyc", 0));
        movimientos.add(new Movimiento("Gas Venenoso", 0, "Poison", 0, getveneno100()));
        movimientos.add(new Movimiento("Presa", 15, "Normal", 0));
        movimientos.add(new Movimiento("Chupa Vidas", 20, "Bug", 0));
        movimientos.add(new Movimiento("Beso Amoroso", 0, "Normal", 0, getSueino()));
        movimientos.add(new Movimiento("Ataque Aereo", 140, "Flying", 0, getCarga1()));
        movimientos.add(new Movimiento("Burbuja", 20, "Water", 0));
        movimientos.add(new Movimiento("Puino-Mareo", 70, "Normal", 0));
        movimientos.add(new Movimiento("Espora", 0, "Grass", 0, getSueino()));
        movimientos.add(new Movimiento("Psico Onda", 1, "Phsyc", 0));
        movimientos.add(new Movimiento("Salpicadura", 0, "Normal", 0));
        movimientos.add(new Movimiento("Martillazo", 90, "Water", 0));
        movimientos.add(new Movimiento("Explosion", 250, "Normal", 0));
        movimientos.add(new Movimiento("Golpes Furia", 18, "Normal", 0));
        movimientos.add(new Movimiento("Huesomerang", 100, "Ground", 0));
        movimientos.add(new Movimiento("Descanso", 0, "Phsyc", 0, getSueino()));
        movimientos.add(new Movimiento("Avalancha", 75, "Rock", 0));
        movimientos.add(new Movimiento("Hip. Colmillo", 80, "Normal", 0));
        movimientos.add(new Movimiento("Tri-Ataque", 80, "Normal", 0));
        movimientos.add(new Movimiento("Cuchillada", 70, "Normal", 0));
        movimientos.add(new Movimiento("Hoja Afilada", 55, "Grass", 0));
        movimientos.add(new Movimiento("Lanza Rocas", 50, "Rock", 0));
        movimientos.add(new Movimiento("Bomba Huevo", 100, "Normal", 0));
        movimientos.add(new Movimiento("Hueso Palo", 65, "Ground", 0));
        movimientos.add(new Movimiento("Cascada", 80, "Water", 0));
    }
    public static Efectos getnull() {
        return new Efectos("Ninguno", 0, null, 0);
    }

    public static Efectos getveneno10() {
        return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 10);
    }

    public static Efectos getveneno20() {
        return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 20);
    }

    public static Efectos getveneno30() {
        return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 30);
    }

    public static Efectos getQuemar10() {
        return new Efectos("Quemar", 0, EstadoAlterado.QUEMADO, 10);
    }

    public static Efectos getCongelar10() {
        return new Efectos("Congelar", 0, EstadoAlterado.CONGELADO, 10);
    }

    public static Efectos getCongelar20() {
        return new Efectos("Congelar", 0, EstadoAlterado.CONGELADO, 20);
    }

    public static Efectos getParalizar10() {
        return new Efectos("Paralizar", 0, EstadoAlterado.PARALIZADO, 10);
    }

    public static Efectos getParalizar30() {
        return new Efectos("Paralizar", 0, EstadoAlterado.PARALIZADO, 30);
    }

    public static Efectos getInsta1() {
        return new Efectos("Hiroshima Mondasaki", Math.pow(10,10), EstadoAlterado.INSTA, 1);
    }

    public static Efectos getCarga1() {
        return new Efectos("Cargando", 0, EstadoAlterado.CARGAR, 100);
    }

    public static Efectos getSueino() {
        return new Efectos("Sueino", 0, EstadoAlterado.DORMIDO, 100);
    }

    public static Efectos getParalizar100() {
        return new Efectos("Paralizar", 0, EstadoAlterado.PARALIZADO, 100);
    }

    public static Efectos getveneno100() {
        return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 100);
    }

    /*public static movimiento getrandomNormal() {
        movimiento f = null;
        int i = (int) Math.random();
        switch (i) {
            case 1 -> f = getFuerza();
        }
        return f;
    }*/
    public static Movimiento getAtaqueAleatorio() {
        // Metodo para cumplir funciones
        List<Method> candidatos = new ArrayList<>();

        for (Method metodo : Ataques.class.getDeclaredMethods()) {
            boolean esEstatico = Modifier.isStatic(metodo.getModifiers());
            boolean devuelveMovimiento = metodo.getReturnType() == Movimiento.class;
            boolean sinParametros = metodo.getParameterCount() == 0;
            boolean esGetter = metodo.getName().startsWith("get");

            // Para que los booleanos no los tire a la basura
            if (esEstatico && devuelveMovimiento && sinParametros && esGetter
                    && !metodo.getName().equals("getAtaqueAleatorio")) {
                candidatos.add(metodo);
            }
        }

        // Por si sale nulo
        if (candidatos.isEmpty()) {
            return null;
        }

        // Randomizador
        Random random = new Random();
        Method elegido = candidatos.get(random.nextInt(candidatos.size()));

        // Para lanzar la excepcion(no espero que pase)
        try {
            return (Movimiento) elegido.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el ataque aleatorio", e);
        }
    }
    public static Movimiento buscarPorNombre(String nombre) {
        for (Movimiento p : movimientos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }
}