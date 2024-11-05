package Controlador;

import Modelo.*;
import Utilidades.*;
import Excepciones.SistemaVentaPasajesExcepcion;

import java.time.LocalDate;
import java.util.*;

public class ControladorEmpresas {

    private ArrayList<Empresa> empresas=new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Terminal> terminales=new ArrayList<>();


    private static ControladorEmpresas instance = null;


    public static ControladorEmpresas getInstance() {
        if (instance == null) {
            instance = new ControladorEmpresas();
        }
        return instance;
    }

    public void createEmpresa(Rut rut,String nombre,String url)throws SistemaVentaPasajesExcepcion{
        Empresa up=new Empresa(rut, nombre);
        up.setUrl(url);
        findEmpresa(rut).ifPresentOrElse(
                t -> {throw new SistemaVentaPasajesExcepcion("Ya existe empresa con el rut indicado");},
                ()->empresas.add(up));
    }



    // todo Arreglar clase Bus, tiene que tener empresa
    public void createBus(String patente, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SistemaVentaPasajesExcepcion {

        Empresa empresa = findEmpresa(rutEmp).orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe empresa con el rut indicado"));

        Bus bus = new Bus(patente, nroAsientos, empresa);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        if (findBus(patente).isEmpty()) {
            //logica para bus que ya existe
            buses.add(bus);
        } else {
            throw new SistemaVentaPasajesExcepcion("Ya existe bus con la patente indicada");
        }
    }





    public void createTerminal(String nombre,Direccion direccion)throws SistemaVentaPasajesExcepcion{

        if(findTerminal(nombre).isPresent()) {
            throw new SistemaVentaPasajesExcepcion("Ya existe Terminal con el nombre indicado");
        }

        if(findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SistemaVentaPasajesExcepcion("Ya existe terminal en la comuna indicada");
        }

        // Si pasa ambas verificaciones, crea y añade el terminal
        Terminal terminal =new Terminal(nombre,direccion);
        terminales.add(terminal);

    }







    public void hireConductorForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir ) throws SistemaVentaPasajesExcepcion{
        Optional<Empresa> empresa=findEmpresa(rutEmp);
        if(empresa.isEmpty()){
            throw new SistemaVentaPasajesExcepcion("No existe empresa con el rut indicado ");
        }
        if(findConductor(id, rutEmp).isPresent()){
            throw new SistemaVentaPasajesExcepcion("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }
        empresa.get().addConductor(id,nom,dir);
    }




    public void hireAuxiliarForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir) throws SistemaVentaPasajesExcepcion{
        Optional<Empresa> empresa = findEmpresa(rutEmp);


        if(empresa.isEmpty()){
            throw new SistemaVentaPasajesExcepcion("No existe empresa con el rut indicado ");
        }


        if(findAuxliar(id, rutEmp).isPresent()) {
            throw new SistemaVentaPasajesExcepcion("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }

        empresa.get().addAuxiliar(id,nom,dir);
    }



    public String[][] listEmpresas(){
        if(empresas.size()==0){return new String[0][0];}

        String[][] listEmpresas=new String[empresas.size()][6];


        for (int i=0; i<empresas.size(); i++) {
            listEmpresas[i][0]=(empresas.get(i).getRut())+"";
            listEmpresas[i][1]=(empresas.get(i).getNombre());
            listEmpresas[i][2]=empresas.get(i).getUrl();
            listEmpresas[i][3]=""+((empresas.get(i).getTripulantes()).length);
            listEmpresas[i][4]=""+((empresas.get(i).getBuses()).length);
            listEmpresas[i][5]=""+((empresas.get(i).getVentas()).length);

        }

        return listEmpresas;

    }

    public String[][] listLlegadaSalidasTerminal(String nombre, LocalDate fecha) throws SistemaVentaPasajesExcepcion {
        Optional<Terminal> terminal = findTerminal(nombre);

        if(terminal.isEmpty()) {
            throw new SistemaVentaPasajesExcepcion("No existe terminal con el nombre indicado");
        }

        Viaje[]llegadas= terminal.get().getLlegadas();
        Viaje[]salidas= terminal.get().getSalidas();

        //Pasar ambas a lista
        ArrayList<Viaje> salida= new ArrayList<>(Arrays.asList(salidas));
        ArrayList<Viaje> llegada= new ArrayList<>(Arrays.asList(llegadas));


        //Eliminar las fechas que no nos sirven
        salida.removeIf(viaje -> viaje.getFecha().isBefore(fecha));
        llegada.removeIf(viaje -> viaje.getFecha().isBefore(fecha));


        //creamos el arreglo de retorno
        String[][] ArrayViajes=new String[salida.size()+llegada.size()][5];
        int index = 0;

        // Agregar salidas al arreglo
        for (Viaje viaje : salida) {
            ArrayViajes[index][0] = "Salida";
            ArrayViajes[index][1] = "" + viaje.getHora();
            ArrayViajes[index][2] = viaje.getBus().getPatente();
            ArrayViajes[index][3] = viaje.getBus().getEmpresa().getNombre();
            ArrayViajes[index][4] = "" + viaje.getListaPasajeros().length;
            index++;
        }

        // Agregar llegadas al arreglo
        for (Viaje viaje : llegada) {
            ArrayViajes[index][0] = "Llegada";
            ArrayViajes[index][1] = "" + viaje.getHora();
            ArrayViajes[index][2] = viaje.getBus().getPatente();
            ArrayViajes[index][3] = viaje.getBus().getEmpresa().getNombre();
            ArrayViajes[index][4] = "" + viaje.getListaPasajeros().length;
            index++;
        }

        return ArrayViajes;

    }



    public String[][] listVentasEmpresa(Rut rut) throws SistemaVentaPasajesExcepcion {
        Optional<Empresa> up=findEmpresa(rut);

        if (up.isEmpty()) {
            throw new SistemaVentaPasajesExcepcion("No existe empresa con el rut indicado ");
        }


        Venta[] ArregloEmpresa=up.get().getVentas();

        if(ArregloEmpresa.length==0){
            return new String[0][0];
        }

        String[][] fast=new String[ArregloEmpresa.length][4];


        for (int i=0;i<ArregloEmpresa.length;i++){
            fast[i][0]=""+ArregloEmpresa[i].getFecha();
            fast[i][1]=""+ArregloEmpresa[i].getTipo();
            fast[i][2]=""+ArregloEmpresa[i].getMontoPagado();
            fast[i][3]=ArregloEmpresa[i].getTipoPago();
        }


        return fast;

    }



    // todo
    protected Optional<Empresa> findEmpresa(Rut rut) {

        for(Empresa n:empresas){
            if(n.getRut().equals(rut)){
                return Optional.of(n);
            }
        }

        return Optional.empty();

    }




    protected Optional<Terminal> findTerminal(String nombre){

        for (Terminal n:terminales){
            if(n.getNombre().equalsIgnoreCase(nombre)){
                return Optional.of(n);
            }

        }

        return Optional.empty();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {

        for(Terminal n: terminales){
            if((n.getDireccion().getComuna()).equalsIgnoreCase(comuna)){
                return Optional.of(n);
            }
        }

        return Optional.empty();

    }


    protected Optional<Bus> findBus(String patente) {
        for (Bus n:buses){
            if(n.getPatente().equalsIgnoreCase(patente)){
                return Optional.of(n);
            }
        }
        return Optional.empty();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {

        Optional<Empresa> empre =findEmpresa(rutEmpresa);

        if(empre.isEmpty()){
            return Optional.empty();
        }

        Tripulante[] arregloTripulantes=empre.get().getTripulantes();

        for (Tripulante n:arregloTripulantes){
            if(n.getIdPersona().equals(id)){
                return Optional.of((Conductor) n);
            }
        }

        return Optional.empty();

    }

    protected Optional<Auxiliar> findAuxliar(IdPersona id, Rut rutEmpresa) {

        Optional<Empresa> empre =findEmpresa(rutEmpresa);

        if(empre.isEmpty()){
            return Optional.empty();
        }

        Tripulante[] arregloTripulantes=empre.get().getTripulantes();

        for (Tripulante n:arregloTripulantes){
            if(n.getIdPersona().equals(id)){
                return Optional.of((Auxiliar) n);
            }
        }

        return Optional.empty();

    }
}
