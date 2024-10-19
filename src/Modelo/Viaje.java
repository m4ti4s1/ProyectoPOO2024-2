package Modelo;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private Auxiliar auxiliar; //asociacion con auxliar

    List<Pasaje> Listapasajes =new ArrayList<>();
    List<Pasajero> Listapasajeros =new ArrayList<>();
    List<Conductor> conductores; //asosiacion con conductor

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus,Auxiliar aux, Conductor cond) {
        this.fecha = fecha;;
        this.hora = hora;
        this.precio = precio;
        this.bus=bus;
        //parte de la asosiacion con conductor
        this.conductores=new ArrayList<>();
        if (cond !=null){
            this.conductores.add(cond);
            cond.addViaje(this);
        }
        //parte de la asociacion con auxiliar
        if(aux!=null){
            this.auxiliar=aux;
            aux.addViaje(this);
        }
    }
//?----------------Getter and Setter---------------------------------------------------
    public LocalDate getFecha() {
        return fecha;
    }
    public LocalTime getHora() {
        return hora;
    }
    public int getPrecio() {
        return precio;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public Bus getBus(){
        return bus;
    }
    //?-------------------------------------------------------------------------------------
    public void addPasaje(Pasaje pasaje){
        boolean agregar =true;
        if(Listapasajes.size()!=0){
            for(int i = 0; i< Listapasajes.size(); i++){
                if(Listapasajes.get(i).getNumero()==pasaje.getNumero()){
                    if(Listapasajes.get(i).getAsiento()==pasaje.getAsiento()){
                        System.out.println("---Este Modelo.Pasaje ya Existe dentro del Modelo.Viaje---");
                        agregar=false;
                        break;
                    }
                }
            }
        }else{agregar=true;}
        if(agregar){
            Listapasajeros.add(pasaje.getPasajero());
            Listapasajes.add(pasaje);
            System.out.println("--- Se agregado Correctamente ---");
        }

    }
    //!------------Lista de Asientos---------------------------------------------

    public String[][] getAsientos(){
        String [][] ListaAsientos=new String[bus.getNroAsientos()][2];

        for(int i=0;i< bus.getNroAsientos();i++){
            ListaAsientos[i][0]=""+(i+1);
            ListaAsientos[i][1]="Vacío";
        }

        for (Pasaje pasaje : Listapasajes) {
            ListaAsientos[pasaje.getAsiento()-1][1] = "Ocupado";
        }
        /*
        for (int i=0;i<Listapasajes.size();i++){
            ListaAsientos[(Listapasajes.get(i).getAsiento())-1][1]="Ocupado";

        }

         */


        return ListaAsientos;
    }

    //!------------------Lista de Pasajeros ------------------------------------------------
    public String[][] getListaPasajeros(){
        String [][] MatrizPasajero= new String[Listapasajeros.size()][5];
        for(int i=0;i<Listapasajeros.size();i++){
            MatrizPasajero[i][0]=" "+Listapasajes.get(i).getAsiento();
            MatrizPasajero[i][1]=" "+Listapasajeros.get(i).getIdPersona();
            MatrizPasajero[i][2]=" "+Listapasajeros.get(i).getNombreCompleto();
            MatrizPasajero[i][3]=" "+Listapasajeros.get(i).getNomContacto();
            MatrizPasajero[i][4]=" "+Listapasajeros.get(i).getFonoContancto();

        }
        return MatrizPasajero;
    }

    public boolean existeDisponibilidad(){
        if(Listapasajes.size() < this.bus.getNroAsientos()){
            return true;
        }
        return false;
    }
    public int getNroAsientosDisponibles(){
        int total=this.bus.getNroAsientos();
        total-= Listapasajes.size();
        return total;
    }

    public Venta[] getVentas() {
        Venta[] ap={};
        return ap;
    }
}

