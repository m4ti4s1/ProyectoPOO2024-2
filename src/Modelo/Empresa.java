package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Utilidades.Rut;

import java.util.ArrayList;
import java.util.Arrays;

public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;


    private ArrayList<Bus> buses=new ArrayList<>();
    private ArrayList<Tripulante> conductores=new ArrayList<>();
    private ArrayList<Tripulante> auxiliares=new ArrayList<>();


    public Empresa(Rut rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
        conductores = new ArrayList<>();
        auxiliares = new ArrayList<>();
    }

    public Rut getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addBus(Bus bus){
        if(!buses.contains(bus)){
            buses.add(bus);
        }
    }

    public Bus[] getBuses() {
        return buses.toArray(new Bus[0]);
    }


    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir ){
        Conductor conductor=new Conductor(id,nom,dir);
        if(conductores.contains(conductor)){
            return false;
        }else{
            conductores.add(conductor);
            return true;
        }
    }


    public boolean addAuxiliar(IdPersona id, Nombre nom, Direccion dir){
        Auxiliar auxiliar = new Auxiliar (id,nom,dir);
        if(conductores.contains(auxiliar)){
            return false;
        }else{
            auxiliares.add(auxiliar);
            return true;
        }
    }


    public Tripulante[] getTripulantes(){
        ArrayList<Tripulante> ListaCombinada =new ArrayList<>();
        // se suman los 2 arreglos
        ListaCombinada.addAll(conductores);
        ListaCombinada.addAll(auxiliares);
        return ListaCombinada.toArray(new Tripulante[0]);
    }



    public Venta[] getVentas(){
        ArrayList<Venta> ListaVentas=new ArrayList<>();
        for (Bus bus : buses) {
            Viaje[] viajes = bus.getViajes();
            for (Viaje viaje : viajes) {
                ListaVentas.addAll(Arrays.asList(viaje.getVentas()));
            }
        }
        return ListaVentas.toArray(new Venta[0]);
    }

}
