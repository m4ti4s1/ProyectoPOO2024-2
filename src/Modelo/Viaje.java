package Modelo;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private int duracion;

    private Bus bus;
    private Terminal llegada;
    private Terminal salida;
    private Auxiliar auxiliar; //asociacion con auxliar

    List<Pasaje> Listapasajes =new ArrayList<>();
    List<Pasajero> Listapasajeros =new ArrayList<>();
    List<Conductor> conductores; //asosiacion con conductor

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int dur, Bus bus,Auxiliar aux, Conductor cond, Terminal sale, Terminal llega) {
        this.fecha = fecha;;
        this.hora = hora;
        this.precio = precio;
        this.duracion = dur;

        this.bus = bus;
        this.bus.addViaje(this);

        this.auxiliar=aux;
        aux.addViaje(this);

        this.conductores=new ArrayList<>();
        this.conductores.add(cond);
        cond.addViaje(this);

        this.salida = sale;
        this.llegada = llega;
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
        LocalDateTime fechaHoraSalida = LocalDateTime.of(fecha, hora);
        //Suma la duración en minutos a la fecha y hora de salida
        return fechaHoraSalida.plusMinutes(duracion);
    }

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
        }
    }

    public Tripulante[] getTripulantes(){
        Tripulante[] tripulantes = new Tripulante[3];
        tripulantes[0] = auxiliar;

        for (int i = 0; i < conductores.size(); i++) {
            tripulantes[0+1] = conductores.get(i);
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

