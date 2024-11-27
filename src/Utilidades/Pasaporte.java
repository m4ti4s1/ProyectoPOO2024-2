package Utilidades;

import java.io.Serializable;
import java.util.Objects;

public class Pasaporte implements IdPersona, Serializable {
    private String numero;
    private String nacionalidad;

    private Pasaporte(String num, String nacionalidad) {
        this.numero = num;
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return String.format("%s %s", numero, nacionalidad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasaporte pasaporte)) return false;
        return Objects.equals(numero, pasaporte.numero) && Objects.equals(nacionalidad, pasaporte.nacionalidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, nacionalidad);
    }

    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public static Pasaporte of(String numero,String nacionalidad){
        if(!esAlfanumerico(numero)){
            return null;
        }
        return new Pasaporte(numero, nacionalidad);
    }
    private static boolean esAlfanumerico(String texto) {
        char[] caracteres = texto.toCharArray();

        for (char c : caracteres) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
