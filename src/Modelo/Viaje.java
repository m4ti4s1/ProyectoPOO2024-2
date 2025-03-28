package Modelo;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class Viaje implements Serializable {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private int duracion;

    private Bus bus;
    private Terminal llegada;
    private Terminal salida;
    private Auxiliar auxiliar;

    private ArrayList<Pasaje> Listapasajes = new ArrayList<>();
    private ArrayList<Pasajero> Listapasajeros = new ArrayList<>();
    private ArrayList<Conductor> conductores;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int dur, Bus bus,Auxiliar aux, Conductor[] cond, Terminal sale, Terminal llega) {
        this.fecha = fecha;;
        this.hora = hora;
        this.precio = precio;
        this.duracion = dur;

        //asociaciones bidireccionales
        this.bus = bus;
        this.bus.addViaje(this);

        this.auxiliar=aux;
        aux.addViaje(this);

        this.conductores=new ArrayList<>();

        for (Conductor c : cond) {
            if (c!=null) {
                this.addConductor(c);
            }
        }

        this.salida = sale;
        this.salida.addSalida(this);

        this.llegada = llega;
        this.llegada.addLlegada(this);
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
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public LocalDateTime getFechaHoraTermino() {
        LocalDateTime fechaHoraSalida = LocalDateTime.of(fecha,hora);
        //Suma la duración en minutos a la fecha y hora de salida
        return fechaHoraSalida.plusMinutes(duracion);
    }

    public void addPasaje(Pasaje pasaje){
        boolean agregar =true;
        if(!Listapasajes.isEmpty()){
            for (Pasaje listapasaje : Listapasajes) {
                if (listapasaje.getNumero() == pasaje.getNumero()) {
                    if (listapasaje.getAsiento() == pasaje.getAsiento()) {
                        agregar = false;
                        break;
                    }
                }
            }
        }

        if(agregar){
            Listapasajeros.add(pasaje.getPasajero());
            Listapasajes.add(pasaje);
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
            MatrizPasajero[i][0]=""+Listapasajes.get(i).getAsiento();
            MatrizPasajero[i][1]=""+Listapasajeros.get(i).getIdPersona();
            MatrizPasajero[i][2]=""+Listapasajeros.get(i).getNombreCompleto();
            MatrizPasajero[i][3]=""+Listapasajeros.get(i).getNomContacto();
            MatrizPasajero[i][4]=Listapasajeros.get(i).getFonoContancto();

        }
        return MatrizPasajero;
    }

    public boolean existeDisponibilidad(int nroAsientos){
        int asientosBus = bus.getNroAsientos();
        int asientosVendidos = Listapasajes.size();
        int asientosLibres = asientosBus - asientosVendidos;

        return asientosLibres >= nroAsientos;
    }

    public int getNroAsientosDisponibles(){
        int total=this.bus.getNroAsientos();
        total-= Listapasajes.size();
        return total;
    }

    public Venta[] getVentas(){
        ArrayList<Venta> ventasLista=new ArrayList<>();

        for (Pasaje p : Listapasajes) {
            Venta venta = p.getVenta();
            if(venta!=null && !ventasLista.contains(venta)){
                ventasLista.add(venta);
            }
        }
        Venta[] ventasArray=new Venta[ventasLista.size()];
        return ventasLista.toArray(ventasArray);
    }

    public void addConductor(Conductor conductor){
        if(conductores.size() < 2) {
            conductores.add(conductor);
            conductor.addViaje(this);
        }
    }

    public Tripulante[] getTripulantes(){
        Tripulante[] tripulantes = new Tripulante[3];
        tripulantes[0] = auxiliar;

        for (int i = 0; i < conductores.size(); i++) {
            tripulantes[i+1] = conductores.get(i);
        }
        return tripulantes;
    }

    public Terminal getTerminalLlegada() {
        return llegada;
    }

    public Terminal getTerminalSalida(){
        return salida;
    }
}

