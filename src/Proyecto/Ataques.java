package Proyecto;

public class Ataques {
    //PokemonLuchador rival=new PokemonLuchador();
    /*https://pokexperto.net/index2.php?seccion=general/ataques1*/
    public static movimiento getDestructor() {
        return new movimiento("Destructor", 40, "Normal", 0, getnull());
    }

    public static movimiento getarañazo() {
        return new movimiento("Arañazo", 40, "Normal", 1, getnull());
    }

    public static movimiento getKarate() {
        return new movimiento("Karate", 50, "Fighting", 0, getnull());
    }

    public static movimiento getDoblebofeton() {
        return new movimiento("Doblebofeton", 15 * (int) (5 * Math.random() + 1), "Normal", 0, getnull());
    }

    public static movimiento getPuñoCometa() {
        return new movimiento("Puño cometa", 18 * (int) (5 * Math.random() + 1), "Normal", 0, getnull());
    }

    public static movimiento getMegaPuño() {
        return new movimiento("Mega Puño", 80, "Normal", 0, getnull());
    }

    public static movimiento getDiaDePago(Jugador jugador) {
        return new movimiento("Dia de pago", jugador.getDinero(), "Normal", 0, getnull());
    }

    public static movimiento getPuñoFuego() {
        return new movimiento("Puño Fuego", 75, "Fire", 0, getQuemar10());
    }

    public static movimiento getPuñoHielo() {
        return new movimiento("Puño Hielo", 75, "Ice", 0, getCongelar10());
    }

    public static movimiento getAgarre() {
        return new movimiento("Agarre", 55, "Normal", 0, getnull());
    }

    public static movimiento getPuñoTrueno() {
        return new movimiento("Puño Trueno", 75, "Electric", 0, getParalizar10());
    }

    public static movimiento getGuillotina() {
        return new movimiento("Guillotina", 10 ^ 10, "Normal", 0, getInsta1());
    }

    public static movimiento getVCortante() {
        return new movimiento("V. Cortante", 80, "Normal", 0, getCarga1());
    }

    public static movimiento getCorte() {
        return new movimiento("Corte", 50, "Normal", 0, getnull());
    } //cortar arbustos

    public static movimiento getAtaqueAla() {
        return new movimiento("Ataque Ala", 60, "Volador", 0, getnull());
    }

    public static movimiento getDoblePatada() {
        return new movimiento("Doble Patada", 30 * 2, "Fighting", 0, getnull());
    }

    public static movimiento getMegaPatada() {
        return new movimiento("Mega Patada", 120, "Fighting", 0, getnull());
    }

    public static movimiento getPatadaSalto() {
        return new movimiento("Patada Salto", 85, "Fighting", 0, getnull());
    }

    public static movimiento getPatadaGiro() {
        return new movimiento("Patada Giro", 60, "Fighting", 0, getnull());
    }

    public static movimiento getPortazo() {
        return new movimiento("Portazo", 80, "Normal", 0, getnull());
    }

    public static movimiento getLatigoCepa() {
        return new movimiento("Latigo Cepa", 35, "Grass", 0, getnull());
    }

    public static movimiento getTornado() {
        return new movimiento("Tornado", 40, "Flying", 0);
    }

    public static movimiento getVuelo() {
        return new movimiento("Vuelo", 90, "Flying", 0, getCarga1());
    }

    public static movimiento getAtadura() {
        return new movimiento("Atadura", 15, "Normal", 0);
    }

    public static movimiento getPisotón() {
        return new movimiento("Pisotón", 65, "Normal", 0);
    }

    public static movimiento getGolpeCabeza() {
        return new movimiento("Golpe Cabeza", 70, "Normal", 0);
    }

    public static movimiento getCornada() {
        return new movimiento("Cornada", 65, "Normal", 0);
    }

    public static movimiento getAtaqueFuria() {
        return new movimiento("Ataque Furia", 15 * (int) (5 * Math.random() + 2), "Normal", 0);
    }

    public static movimiento getPerforador() {
        return new movimiento("Perdorador", 10 ^ 10, "Normal", 0, getInsta1());
    }

    public static movimiento getPlacaje() {
        return new movimiento("Placaje", 35, "Normal", 0);
    }

    public static movimiento getGolpeCuerpo() {
        return new movimiento("Golpe cuerpo", 85, "Normal", 0, getParalizar30());
    }

    public static movimiento getRepeticion() {
        return new movimiento("Repeticion", 15, "Normal", 0);
    }

    public static movimiento getDerribo() {
        return new movimiento("Derribo", 90, "Normal", 0);
    }

    public static movimiento getDobleFilo() {
        return new movimiento("Doble Filo", 120, "Normal", 0);
    }

    public static movimiento getPicotazoVen() {
        return new movimiento("Picotazo Venenoso", 15, "Poison", 0, getveneno30());
    }

    public static movimiento getDobleAtaque() {
        return new movimiento("Doble ataque", 25 * 2, "Bug", 0, getveneno20());
    }

    public static movimiento getPinMisil() {
        return new movimiento("Pin Misil", 14 * (int) (Math.random() * 4 + 2), "Bug", 0);
    }

    public static movimiento getMordisco() {
        return new movimiento("Mordisco", 60, "Dark", 0);
    }

    public static movimiento getCanto() {
        return new movimiento("Canto", 0, "Normal", 0, getSueño());
    }

    public static movimiento getAcido() {
        return new movimiento("Acido", 40, "Poison", 0, getveneno10());
    }

    public static movimiento getAscuas() {
        return new movimiento("Ascuas", 40, "Fire", 0, getQuemar10());
    }

    public static movimiento getLanzallamas() {
        return new movimiento("Lanzallamas", 95, "Fire", 0, getQuemar10());
    }

    public static movimiento getPistolaAgua() {
        return new movimiento("Pistola Agua", 40, "Water", 0);
    }

    public static movimiento getHidroBomba() {
        return new movimiento("Hidro Bomba", 120, "Water", 0);
    }

    public static movimiento getSurf() {
        return new movimiento("Surf", 85, "Water", 0);
    }//permite nadar

    public static movimiento getRayoHielo() {
        return new movimiento("Rayo Hielo", 95, "Ice", 0, getCongelar10());
    }

    public static movimiento getVentisca() {
        return new movimiento("Ventisca", 120, "Ice", 0, getCongelar20());
    }

    public static movimiento getPicotazo() {
        return new movimiento("Picotazo", 35, "Flying", 0);
    }

    public static movimiento getPicoTaladro() {
        return new movimiento("Pico Taladro", 80, "Flying", 0);
    }

    public static movimiento getSumision() {
        return new movimiento("Sumision", 80, "Fighting", 0);
    }

    public static movimiento getPsicoRayo() {
        return new movimiento("Psico-Rayo", 65, "Phsyc", 0);
    }

    public static movimiento getRayoBurbuja() {
        return new movimiento("Rayo Burbuja", 65, "Water", 0);
    }

    public static movimiento getRayoAurora() {
        return new movimiento("Rayo Aurora", 65, "Ice", 0);
    }

    public static movimiento getHiperrayo() {
        return new movimiento("Hiperrayo", 150, "Normal", 0);
    }

    public static movimiento getFuerza() {
        return new movimiento("Fuerza", 80, "Normal", 0);
    }//Permite mover rocas

    public static movimiento getAbsorber() {
        return new movimiento("Absorber", 20, "Grass", 0);
    }

    public static movimiento getMegaagotar() {
        return new movimiento("Megaagotar", 40, "Grass", 0);
    }

    public static movimiento getDrenadoras() {
        return new movimiento("Drenadoras", 0, "Grass", 0);
    }

    public static movimiento getDesarrollo() {
        return new movimiento("Desarrollo", 0, "Normal", 0);
    }

    public static movimiento getRayoSolar() {
        return new movimiento("Rayo Solar", 120, "Grass", 0, getCarga1());
    }

    public static movimiento getPolvoVeneno() {
        return new movimiento("Polvo Veneno", 0, "Poison", 0, getveneno100());
    }

    public static movimiento getParalizador() {
        return new movimiento("Paralizador", 0, "Grass", 0, getParalizar100());
    }

    public static movimiento getSomnifero() {
        return new movimiento("Somnifero", 0, "Grass", 0, getSueño());
    }

    public static movimiento getDanzaPetalo() {
        return new movimiento("Danza Petalo", 90, "Grass", 0);
    }

    public static movimiento getFuriaDragon() {
        return new movimiento("Furia Dragon", 40, "Dragon", 0);
    }

    public static movimiento getGiroFuego() {
        return new movimiento("Giro Fuego", 15, "Fire", 0);
    }

    public static movimiento getImpactrueno() {
        return new movimiento("Impactrueno", 40, "Electric", 0, getParalizar10());
    }

    public static movimiento getRayo() {
        return new movimiento("Rayo", 95, "Electric", 0, getParalizar10());
    }

    public static movimiento getOndaTrueno() {
        return new movimiento("Onda Trueno", 0, "Electric", 0, getParalizar100());
    }

    public static movimiento getTrueno() {
        return new movimiento("Trueno", 120, "Electric", 0, getParalizar30());
    }

    public static movimiento getTerremoto() {
        return new movimiento("Terremoto", 100, "Ground", 0);
    }

    public static movimiento getFisura() {
        return new movimiento("Fisura", 10 ^ 10, "Ground", 0, getInsta1());
    }

    public static movimiento getExcavar() {
        return new movimiento("Excavar", 60, "Ground", 0, getCarga1());
    }

    public static movimiento getToxico() {
        return new movimiento("Toxico", 0, "Poison", 0, getveneno100());
    }

    public static movimiento getConfusion() {
        return new movimiento("Confusion", 50, "Phsyc", 0);
    }

    public static movimiento getPsiquico() {
        return new movimiento("Psiquico", 90, "Phsyc", 0);
    }

    public static movimiento getHipnosis() {
        return new movimiento("Hipnosis", 0, "Phsyc", 0, getSueño());
    }

    public static movimiento getFuria() {
        return new movimiento("Furia", 20, "Normal", 0);
    }

    public static movimiento getTeletransp() {
        return new movimiento("Teletransp", 0, "Phsyc", 0);
    }

    public static movimiento getLengüetazo() {
        return new movimiento("Lengüetazo", 20, "Ghost", 0, getParalizar30());
    }

    public static movimiento getPolucion() {
        return new movimiento("Polucion", 20, "Poison", 0, getveneno30());
    }

    public static movimiento getResiduos() {
        return new movimiento("Residuos", 65, "Poison", 0, getveneno30());
    }

    public static movimiento getLlamarada() {
        return new movimiento("Llamarada", 120, "Fire", 0, getQuemar10());
    }

    public static movimiento getTenaza() {
        return new movimiento("Tenaza", 35, "Water", 0);
    }

    public static movimiento getRapidez() {
        return new movimiento("Rapidez", 60, "Normal", 0);
    }

    public static movimiento getCabezazo() {
        return new movimiento("Cabezazo", 100, "Normal", 0, getCarga1());
    }

    public static movimiento getClavoCañon() {
        return new movimiento("Clavo Cañon", 20, "Normal", 0);
    }

    public static movimiento getRestriccion() {
        return new movimiento("Restriccion", 10, "Normal", 0);
    }

    public static movimiento getPatadaSAlta() {
        return new movimiento("Patada S. Alta", 100, "Fighting", 0);
    }

    public static movimiento getDeslumbrar() {
        return new movimiento("Deslumbrar", 0, "Normal", 0, getParalizar100());
    }

    public static movimiento getComesueños() {
        return new movimiento("Comesueños", 100, "Phsyc", 0);
    }

    public static movimiento getGasVenenoso() {
        return new movimiento("Gas Venenoso", 0, "Poison", 0, getveneno100());
    }

    public static movimiento getPresa() {
        return new movimiento("Presa", 15, "Normal", 0);
    }

    public static movimiento getChupaVidas() {
        return new movimiento("Chupa Vidas", 20, "Bug", 0);
    }

    public static movimiento getBesoAmoroso() {
        return new movimiento("Beso Amoroso", 0, "Normal", 0, getSueño());
    }

    public static movimiento getAtaqueAereo() {
        return new movimiento("Ataque Aereo", 140, "Flying", 0, getCarga1());
    }

    public static movimiento getBurbuja() {
        return new movimiento("Burbuja", 20, "Water", 0);
    }

    public static movimiento getPuñoMareo() {
        return new movimiento("Puño-Mareo", 70, "Normal", 0);
    }

    public static movimiento getEspora() {
        return new movimiento("Espora", 0, "Grass", 0, getSueño());
    }

    public static movimiento getPsicoOnda() {
        return new movimiento("Psico Onda", 1, "Phsyc", 0);
    }

    public static movimiento getSalpicadura() {
        return new movimiento("Salpicadura", 0, "Normal", 0);
    }

    public static movimiento getMartillazo() {
        return new movimiento("Martillazo", 90, "Water", 0);
    }

    public static movimiento getExplosion() {
        return new movimiento("Explosion", 250, "Normal", 0);
    }

    public static movimiento getGolpesFuria() {
        return new movimiento("Golpes Furia", 18, "Normal", 0);
    }

    public static movimiento getHuesomerang() {
        return new movimiento("Huesomerang", 100, "Ground", 0);
    }

    public static movimiento getDescanso() {
        return new movimiento("Descanso", 0, "Phsyc", 0, getSueño());
    }

    public static movimiento getAvalancha() {return new movimiento("Avalancha", 75, "Rock", 0);}
    public static movimiento getHipColmillo() {return new movimiento("Hip. Colmillo", 80, "Normal", 0);}
    public static movimiento getTriAtaque() {return new movimiento("Tri-Ataque", 80, "Normal", 0);}
    public static movimiento getCuchillada() {return new movimiento("Cuchillada", 70, "Normal", 0);}
    public static movimiento getHojaAfilada() {return new movimiento("Hoja Afilada", 55, "Grass", 0);}
    public static movimiento getLanzaRocas() {return new movimiento("Lanza Rocas", 50, "Rock", 0);}
    public static movimiento getBombaHuevo() {return new movimiento("Bomba Huevo", 100, "Normal", 0);}
    public static movimiento getHuesoPalo() {return new movimiento("Hueso Palo", 65, "Ground", 0);}
    public static movimiento getCascada() {return new movimiento("Cascada", 80, "Water", 0);}

    public static Efectos getnull() {return new Efectos("Ninguno", 0, null, 0);}
    public static Efectos getveneno10() {return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 10);}
    public static Efectos getveneno20() {return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 20);}
    public static Efectos getveneno30() {return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 30);}
    public static Efectos getQuemar10() {return new Efectos("Quemar", 0, EstadoAlterado.QUEMADO, 10);}
    public static Efectos getCongelar10() {return new Efectos("Congelar", 0, EstadoAlterado.CONGELADO, 10);}
    public static Efectos getCongelar20() {return new Efectos("Congelar", 0, EstadoAlterado.CONGELADO, 20);}
    public static Efectos getParalizar10() {return new Efectos("Paralizar", 0, EstadoAlterado.PARALIZADO, 10);}
    public static Efectos getParalizar30() {return new Efectos("Paralizar", 0, EstadoAlterado.PARALIZADO, 30);}
    public static Efectos getInsta1() {return new Efectos("Hiroshima Mondasaki", 10 ^ 10, EstadoAlterado.INSTA, 1);}
    public static Efectos getCarga1() {return new Efectos("Cargando", 0, EstadoAlterado.CARGAR, 100);}
    public static Efectos getSueño() {return new Efectos("Sueño", 0, EstadoAlterado.DORMIDO, 100);}
    public static Efectos getParalizar100() {return new Efectos("Paralizar", 0, EstadoAlterado.PARALIZADO, 100);}
    public static Efectos getveneno100() {return new Efectos("Veneno", 0, EstadoAlterado.ENVENENADO, 100);}
}