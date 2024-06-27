package com.easj.chromosmobile.Logica;

import android.app.Application;
import android.content.Context;

public class CryptorSJ {
    public static String desencriptarCadena(String cadenaADescifrar) throws Exception{

        int maximoIndiceCadena = cadenaADescifrar.length() - 3;
        int[] aCadena = new int[maximoIndiceCadena + 1];
        int sumaA = 0, sumaB = 0;
        int claveA, claveB;
        boolean claveAEsPar;
        int desplazamientoADerecha;
        String cadenaIzquierda = "";
        String cadenaDerecha = "";
        String cadenaFinal = "";
        claveA = Character.getNumericValue(cadenaADescifrar.charAt(maximoIndiceCadena + 1));
        claveB = Character.getNumericValue(cadenaADescifrar.charAt(maximoIndiceCadena + 2));
        cadenaFinal = cadenaADescifrar.substring(0, maximoIndiceCadena + 1);
        for (char c : cadenaFinal.toCharArray()) {
            sumaB += Character.getNumericValue(c);
        }
        if (claveB != sumaB % 10) {
            return "error_al_obtener_codigo";
//            Swal.warning(ctx, "Cuidado!", "El código no coincide con la estructura de San Juan.", 2000);
//            throw new Exception("Código no coincide con estructura San Juan.");
        }
        desplazamientoADerecha = maximoIndiceCadena - ((maximoIndiceCadena < claveA) ? claveA - maximoIndiceCadena : claveA);
        cadenaIzquierda = "";
        cadenaDerecha = "";
        for (int i = 0; i <= maximoIndiceCadena; i++) {
            if (i <= desplazamientoADerecha) {
                cadenaIzquierda += cadenaFinal.charAt(i);
            } else {
                cadenaDerecha += cadenaFinal.charAt(i);
            }
        }
        cadenaFinal = cadenaDerecha + cadenaIzquierda;
        for (int i = 0; i <= maximoIndiceCadena; i++) {
            aCadena[i] = Character.getNumericValue(cadenaFinal.charAt(i));
        }
        claveAEsPar = claveA % 2 == 0;
        for (int i = 0; i <= maximoIndiceCadena; i++) {
            if (aCadena[i] != 0) {
                aCadena[i] = 10 - aCadena[i];
            }
        }
        for (int i = 1; i <= maximoIndiceCadena; i++) {
            if ((claveAEsPar && i % 2 == 0) || (!claveAEsPar && i % 2 != 0)) {
                if (aCadena[i] < claveA) {
                    aCadena[i] += 10;
                }
                aCadena[i] -= claveA;
            }
        }
        for (int c : aCadena) {
            sumaA += c;
        }
        if (claveA != sumaA % 10) {
            return "error_al_obtener_codigo";
//            throw new Exception("Código no coincide con estructura San Juan.");
        }
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < aCadena.length; i++) {
            resultado.append(aCadena[i]);
        }
        return resultado.toString();
    }
}
