package Utilidades;

public class Direccion {
     private String calle;
     private  int numero;
     private String Comuna;

    public Direccion(String calle, int numero, String comuna) {
        this.calle = calle;
        this.numero = numero;
        Comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public int getNumero() {
        return numero;
    }

    public String getComuna() {
        return Comuna;
    }
}
