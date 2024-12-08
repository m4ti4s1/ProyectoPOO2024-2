package Utilidades;

import java.io.Serializable;

public class Direccion implements Serializable{
     private String calle;
     private  int numero;
     private String comuna;

    public Direccion(String calle, int numero, String comuna) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public int getNumero() {
        return numero;
    }

    public String getComuna() {
        return comuna;
    }
}
