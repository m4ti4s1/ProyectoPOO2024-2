package Utilidades;

import Excepciones.SVPException;

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
    //---------Metodo rut-----------
    public static Rut of(String rutConDv) {
        boolean matches = rutConDv.matches("\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]");
        if(matches){
            throw new SVPException("El Formato del rut no es Correcto(xx.xxx.xxx-x)");
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

}

