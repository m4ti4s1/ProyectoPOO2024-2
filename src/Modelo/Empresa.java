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


    private ArrayList<Bus> buses;
    private ArrayList<Tripulante> conductores;
    private ArrayList<Tripulante> Auxiliares;


    public Empresa(Rut rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
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
        Conductor conductor=new Conductor(id,nom,dir);
        if(conductores.contains(conductor)){
            return false;
        }else{
            conductores.add(conductor);
            return true;
        }
    }
    public Tripulante[] getTripulantes(){
        ArrayList<Tripulante> ListaCombinada =new ArrayList<>();
        // se suman los 2 arreglos
        ListaCombinada.addAll(conductores);
        ListaCombinada.addAll(Auxiliares);
        Tripulante[] ArregloTripulantes=ListaCombinada.toArray(new Tripulante[0]);
        return ArregloTripulantes;
    }
    public Venta[] getVentas(){
        ArrayList<Venta> ListaVentas=new ArrayList<>();
        for(int i=0;i<buses.size();i++){
            Viaje[] viajes=buses.get(i).getViajes();
            for (int n=0;n< viajes.length;n++){
                ListaVentas.addAll(Arrays.asList(viajes[n].getVentas()));
            }
        }
        Venta[] ArregloVentas=ListaVentas.toArray(new Venta[0]);
        return ArregloVentas;
    }

}
