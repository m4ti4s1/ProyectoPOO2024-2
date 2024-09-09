import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;

    List<Pasaje> Listapasajes =new ArrayList<>();
    List<Pasajero> Listapasajeros =new ArrayList<>();

    public Viaje(LocalDate fecha, LocalTime hora, int precio,Bus bus) {
        this.fecha = fecha;;
        this.hora = hora;
        this.precio = precio;
        this.bus=bus;
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
                        System.out.println("---Este Pasaje ya Existe dentro del Viaje---");
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
        for (int i=0;i<Listapasajes.size();i++){
            ListaAsientos[(Listapasajes.get(i).getAsiento())-1][1]="Ocupado";

        }


        return ListaAsientos;
    }

    //!------------------Lista de Pasajeros ------------------------------------------------
    public String[][] getListaPasajeros(){
        String [][] MatrizPasajero= new String[Listapasajeros.size()][4];
        for(int i=0;i<Listapasajeros.size();i++){
            MatrizPasajero[i][0]=" "+Listapasajeros.get(i).getIdPersona();
            MatrizPasajero[i][1]=" "+Listapasajeros.get(i).getNombreCompleto();
            MatrizPasajero[i][2]=" "+Listapasajeros.get(i).getNomContacto();
            MatrizPasajero[i][3]=" "+Listapasajeros.get(i).getFonoContancto();

        }
        return MatrizPasajero;
    }

    public boolean existeDisponibilidad(){
        if(Listapasajes.size()<this.bus.getNroAsientos()){
            return true;
        }
        return false;
    }
    public int getNroAsientosDisponibles(){
        int total=this.bus.getNroAsientos();
        total-= Listapasajes.size();
        return total;
    }
}

