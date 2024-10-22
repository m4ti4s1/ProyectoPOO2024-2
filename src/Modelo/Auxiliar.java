package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;

public class Auxiliar extends Tripulante {
    private ArrayList<Viaje> auxiliarViajes; //asociacion con viaje

    public Auxiliar(Utilidades.IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
        this.auxiliarViajes = new ArrayList<>();
    }

    @Override
    public void addViaje(Viaje viaje){
        auxiliarViajes.add(viaje);
    }

    @Override
    public int getNroViajes(){
        return auxiliarViajes.size();
    }
}
