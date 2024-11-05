package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;

public class Conductor extends Tripulante {
    private ArrayList<Viaje> conductorViajes; //asociacion con viaje

    public Conductor(IdPersona id, Nombre nom, Direccion dir) {
        super(id,nom,dir);
        this.conductorViajes = new ArrayList<>();
    }

    @Override
    public void addViaje(Viaje viaje){
        conductorViajes.add(viaje);
    }

    @Override
    public int getNroViajes(){
        return conductorViajes.size();
    }
}
