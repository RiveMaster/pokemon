package Proyecto;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Verifica arte generado, accesos y conexiones de las ciudades sin abrir ventanas. */
public final class ArteEdificiosSmokeTest {
    private static final Method GENERAR;
    static {
        try {
            GENERAR = ExploradorMapa.class.getDeclaredMethod("crearMapaProvisional", Ubicacion.class);
            GENERAR.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) throws Exception {
        for (String tipo : new String[]{
                "CENTRO_POKEMON", "TIENDA", "GIMNASIO", "CASA", "CASINO", "GOBIERNO"}) {
            BufferedImage imagen = ArteEdificios.crear(tipo, 5, 4, 1);
            assert imagen.getWidth() == 80 && imagen.getHeight() == 64 : tipo;
            assert (imagen.getRGB(40, 20) >>> 24) != 0 : tipo;
            assert (imagen.getRGB(40, 55) >>> 24) != 0 : tipo;
        }

        revisar(Ubicacion.VILLAVERDE, "CASA_MAMA");
        revisar(Ubicacion.OVIEDO, "GIMNASIO");
        revisar(Ubicacion.GIJON, "GIMNASIO");
        revisar(Ubicacion.COMILLAS, "CASINO");
        revisar(Ubicacion.VITORIA, "GOBIERNO");
        System.out.println("ArteEdificiosSmokeTest: gráficos y puertas accesibles OK");
    }

    private static void revisar(Ubicacion ciudad, String esperado) throws Exception {
        Object mapa = GENERAR.invoke(null, ciudad);
        Class<?> tipoMapa = mapa.getClass();
        Field entradasField = tipoMapa.getDeclaredField("entradas");
        entradasField.setAccessible(true);
        List<?> puertas = (List<?>) entradasField.get(mapa);
        assert puertas.size() == 8 : ciudad + " debe tener ocho edificios";

        Method evento = tipoMapa.getDeclaredMethod("eventoEn", int.class, int.class);
        evento.setAccessible(true);
        Method pisar = tipoMapa.getDeclaredMethod("puedePisar", int.class, int.class, boolean.class);
        pisar.setAccessible(true);
        boolean encontrado = false;
        Set<Integer> accesibles = recorrido(mapa, pisar);

        for (Object puerta : puertas) {
            Field xf = puerta.getClass().getDeclaredField("x");
            Field yf = puerta.getClass().getDeclaredField("y");
            Field tf = puerta.getClass().getDeclaredField("tipo");
            xf.setAccessible(true);
            yf.setAccessible(true);
            tf.setAccessible(true);
            int x = xf.getInt(puerta), y = yf.getInt(puerta);
            String categoria = (String) tf.get(puerta);
            if (esperado.equals(categoria)) encontrado = true;
            assert categoria.equals(evento.invoke(mapa, x, y));
            assert (boolean) pisar.invoke(mapa, x, y, false)
                    : ciudad + " puerta no transitable: " + categoria;
            assert accesibles.contains(y * 36 + x + 36)
                    : ciudad + " sin acceso exterior: " + categoria + " en " + x + "," + y;
        }
        assert encontrado : ciudad + " no contiene " + esperado;
        assert (boolean) pisar.invoke(mapa, 0, 12, false) : "Salida izquierda bloqueada";
        assert (boolean) pisar.invoke(mapa, 35, 12, false) : "Salida derecha bloqueada";
    }

    private static Set<Integer> recorrido(Object mapa, Method pisar) throws Exception {
        Set<Integer> visitadas = new HashSet<>();
        ArrayDeque<int[]> pendientes = new ArrayDeque<>();
        pendientes.add(new int[]{18, 12});
        while (!pendientes.isEmpty()) {
            int[] pos = pendientes.removeFirst();
            int x = pos[0], y = pos[1], key = y * 36 + x;
            if (x < 0 || y < 0 || x >= 36 || y >= 24
                    || visitadas.contains(key)
                    || !(boolean) pisar.invoke(mapa, x, y, false)) continue;
            visitadas.add(key);
            pendientes.add(new int[]{x - 1, y});
            pendientes.add(new int[]{x + 1, y});
            pendientes.add(new int[]{x, y - 1});
            pendientes.add(new int[]{x, y + 1});
        }
        return visitadas;
    }
}
