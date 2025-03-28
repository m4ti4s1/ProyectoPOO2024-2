package Modelo;

import Utilidades.*;

import java.io.Serializable;

public class Pasajero extends Persona implements Serializable {
    private Nombre nomContacto;
    private String fonoContacto;

    public Pasajero(IdPersona id, Nombre nom) {
        super(id, nom);
    }

    public Nombre getNomContacto() {
        return nomContacto;
    }

    public void setNomContacto(Nombre nom) {
        this.nomContacto = nom;
    }

    public String getFonoContancto() {
        return fonoContacto;
    }

    public void setFonoContancto(String fono) {
        this.fonoContacto = fono;
    }
}
