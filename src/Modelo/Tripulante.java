package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.io.Serializable;

public abstract class Tripulante extends Persona implements Serializable {

    private Direccion direccion;

    public Tripulante(Utilidades.IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom);
        direccion = dir;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public abstract void addViaje(Viaje viaje);

    public abstract int getNroViajes();

}
