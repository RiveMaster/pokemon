package Proyecto;

import java.util.InputMismatchException;
import java.util.Scanner;

/** Utilidades de lectura por teclado compartidas por los menús nuevos. */
public class Entrada {

    /** Lee un entero; si el usuario escribe otra cosa vuelve a pedirlo. */
    public static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Ingresa un número: ");
                sc.nextLine();
            }
        }
    }
}
