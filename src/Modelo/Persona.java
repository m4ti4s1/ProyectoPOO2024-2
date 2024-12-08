package Modelo;

import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.io.Serializable;

public abstract class Persona implements Serializable {
    private Utilidades.IdPersona IdPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(IdPersona id, Nombre nombre) {
        this.IdPersona = id;
        this.nombreCompleto = nombre;
    }

    public Persona() {

    }
    public IdPersona getIdPersona() {
        return IdPersona;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(Nombre nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    @Override
    public String toString() {
        return  IdPersona.toString() + ", " + nombreCompleto.toString() + ", " + telefono;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || this.getClass() != otro.getClass()) return false;
        Persona persona = (Persona) otro;
        return IdPersona == persona.IdPersona;
    }
}
