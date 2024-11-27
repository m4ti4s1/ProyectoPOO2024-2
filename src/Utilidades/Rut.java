package Utilidades;

import java.io.Serializable;
import java.util.Objects;

public class Rut implements IdPersona, Serializable {
    private int numero;
    private char dv;

    private Rut(int num, char dv) {
        this.numero = num;
        this.dv = dv;
    }

    @Override
    public String toString() {
        String num = "" + numero;
        char[] Arraynumero = num.toCharArray();
        if (Arraynumero.length == 9) {
            return ""+Arraynumero[0] + Arraynumero[1] + '.' + Arraynumero[2] +
                    Arraynumero[3] + Arraynumero[4] + '.' + Arraynumero[5]
                    + Arraynumero[6] + Arraynumero[7] + Arraynumero[8]+'-' + dv;
        }
        if (Arraynumero.length == 8) {
            return "" + Arraynumero[0] + Arraynumero[1] + '.' + Arraynumero[2] +
                    Arraynumero[3] + Arraynumero[4] + '.' + Arraynumero[5]
                    + Arraynumero[6] + Arraynumero[7] + '-' + dv;
        }
        return "" + Arraynumero[0] + '.' + Arraynumero[1] +
                Arraynumero[2] + Arraynumero[3] + '.' + Arraynumero[4]
                + Arraynumero[5] + Arraynumero[6] + '-' + dv;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rut rut)) return false;
        return numero == rut.numero && dv == rut.dv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, dv);
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }
    //?---------Metodo rut-----------
    public static Rut of(String rutConDv) {
        if(!VerificarRut(rutConDv)){
            return null;
        }

        char[] toChar = rutConDv.toCharArray();
        int cont=0;
        char[] rutvacio=new char[20];
        for (int i = 0; i < toChar.length ; i++) {

            if (toChar[i] != '.' && toChar[i] != '-' ) {
                rutvacio[cont] = toChar[i];
                cont++;
            }
        }
        int deV=rutvacio[cont-1];
        String numString ="";
        for (int i=0;i<cont-1;i++){
            numString +=rutvacio[i];
        }
        int numerorut= Integer.parseInt(numString);

        return new Rut(numerorut,rutvacio[cont-1]);

    }

    //?--------- FUNCIONES Para  RUT OF-----------

    private static boolean VerificarRut(String rutConDv) {
        for (int i = 0; i < rutConDv.length() - 1; i++) {
            if (!Character.isDigit(rutConDv.charAt(i)) && rutConDv.charAt(i) != '.' && rutConDv.charAt(i) != '-') {
                System.out.println("Error: El RUT contiene caracteres no permitidos.");
                return false;
            }
        }
        char[] toChar = rutConDv.toCharArray();
        int posicion = 0;

        int num = 0;
        for (int i = 0; i < toChar.length ; i++) {
            if (toChar[i] == '.' || toChar[i] == '-') {
                num++;

            }
        }
        if(num!=3){
            System.out.println("--- El Formato No es el Correcto ---");return false;}
        char[] rutvacio = new char[20];
        int cont = 0;

        for (int i = 0; i < toChar.length ; i++) {
            if (toChar[i] == '-' ){
                break;
            }
            if (toChar[i] != '.' ) {
                rutvacio[cont] = toChar[i];
                cont++;
            }
        }

        int i = cont-1;
        int mul = 2;
        int sum = 0;
        while (i >= 0) {
            sum += (rutvacio[i]-'0') * mul;
            i--;
            mul++;
            if (mul == 8) {
                mul = 2;
            }
        }


        double modulo = 11-(Math.round(sum %11));

        char dvCalculado;
        if (modulo == 11) {
            dvCalculado = '0';
        } else if (modulo == 10) {
            dvCalculado = 'K';
        } else {
            dvCalculado = (char) (modulo + '0');
        }
        if(dvCalculado==toChar[toChar.length-1]){
            return true;
        }
        return false;
    }

}

