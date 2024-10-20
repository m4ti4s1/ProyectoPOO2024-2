package Controlador;

import Modelo.*;
import Utilidades.*;
import Excepciones.SistemaVentaPasajesExcepcion;

import java.lang.reflect.Array;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

// todo implementar singleton
public class ControladorEmpresas {

    ArrayList<Empresa> empresas=new ArrayList<>();
    ArrayList<Bus> buses = new ArrayList<>();
    ArrayList<Terminal> terminales=new ArrayList<>();

    public void createEmpresa(Rut rut,String nombre,String url)throws SistemaVentaPasajesExcepcion{
        Empresa up=new Empresa(rut, nombre);
        up.setUrl(url);
        findEmpresa(rut).ifPresentOrElse(
                t -> {throw new SistemaVentaPasajesExcepcion("Ya existe empresa con el rut indicado");},
                ()->empresas.add(up));
    }
    public void createBus(String patente, String marca, String modelo, int nroAsientos) throws SistemaVentaPasajesExcepcion {
        Bus bus = new Bus(patente, nroAsientos);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        // Arreglar bus no puede utilizar contains, porque no tiene definido el equals
        if (findBus(patente) == null) {
            //logica para bus que ya existe
            buses.add(bus);
        } else {
            throw new SistemaVentaPasajesExcepcion("Ya existe bus con la patente indicada");
        }
    }
    public void createTerminal(String nombre,Direccion direccion)throws SistemaVentaPasajesExcepcion{
        Terminal up=new Terminal(nombre,direccion);
        findTerminalPorComuna(direccion.getComuna()).ifPresent(
                t->{throw new SistemaVentaPasajesExcepcion("Ya existe terminal en la comuna indicada");});
       findTerminal(nombre).ifPresent(
               t->{throw new SistemaVentaPasajesExcepcion("Ya existe terminal con el nombre indicado ");});
       terminales.add(up);

    }
    public void hireConductorForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir ){
        Optional<Empresa> empresa=findEmpresa(rutEmp);
        if(empresa.isEmpty()){
            throw new SistemaVentaPasajesExcepcion("No existe empresa con el rut indicado ");
        }
        boolean newConductor=empresa.get().addConductor(id,nom,dir);
        if(newConductor){
            throw new SistemaVentaPasajesExcepcion("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }
    }
    public void hireAuxiliarForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir){
        Optional<Empresa> empresa=findEmpresa(rutEmp);
        if(empresa.isEmpty()){
            throw new SistemaVentaPasajesExcepcion("No existe empresa con el rut indicado ");
        }
        boolean newConductor=empresa.get().addAuxiliar(id,nom,dir);
        if(newConductor){
            throw new SistemaVentaPasajesExcepcion("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }
    }
    public String[][] listEmpresas(){
        if(empresas.size()==0){return new String[0][0];}
        String[][] listEmpresas=new String[empresas.size()][6];
        for(int i=0;i<empresas.size();i++){
            listEmpresas[i][0]=(empresas.get(i).getRut())+"";
            listEmpresas[i][1]=(empresas.get(i).getNombre());
            listEmpresas[i][2]=empresas.get(i).getUrl();
            listEmpresas[i][3]=""+((empresas.get(i).getTripulantes()).length);
            listEmpresas[i][4]=""+((empresas.get(i).getBuses()).length);
            listEmpresas[i][5]=""+((empresas.get(i).getVentas()).length);

        }
        return listEmpresas;
    }
    public String[][] listLlegadaSalidasTerminal(String nombre, Date fecha)throws SistemaVentaPasajesExcepcion{
        Optional<Terminal> terminal = findTerminal(nombre);
        if(terminal.isEmpty()){
            throw new SistemaVentaPasajesExcepcion("No existe terminal con el nombre indicado");}
        Viaje[]llegadas= terminal.get().getLlegadas();
        Viaje[]salidas= terminal.get().getSalidas();
        //Pasar ambas a lista
        ArrayList<Viaje> salida= new ArrayList<>(Arrays.asList(salidas));
        ArrayList<Viaje> llegada= new ArrayList<>(Arrays.asList(llegadas));
        //Eliminar las fechas que no nos sirven
        for (int i=0;i<salida.size();i++){
            if(salida.get(i).getFecha().before(fecha)){
                salida.remove(i);
            }
        }
        for (int i=0;i<salida.size();i++){
            if(salida.get(i).getFecha().before(fecha)){
                salida.remove(i);
            }
        }
        //creamos ela arreglo de retorno
        String[][] ArrayViajes=new String[salida.size()+llegada.size()][5];
        for(int n=0;n<ArrayViajes.length;n++){
            ArrayViajes[n][0]="Salida";
            ArrayViajes[n][1]=""+salida.get(n).getHora();
            ArrayViajes[n][2]=(salida.get(n).getBus()).getPatente();
            ArrayViajes[n][3]=(salida.get(n).getBus().getEmpresa()).getNombre();
            ArrayViajes[n][4]=""+(salida.get(n).getListaPasajeros().length);
            n++;
            ArrayViajes[n][0]="Llegada";
            ArrayViajes[n][1]=""+llegada.get(n).getHora();
            ArrayViajes[n][2]=(llegada.get(n).getBus()).getPatente();
            ArrayViajes[n][3]=(llegada.get(n).getBus().getEmpresa()).getNombre();
            ArrayViajes[n][4]=""+(llegada.get(n).getListaPasajeros().length);
        }
        return ArrayViajes;

    }



    // todo
    protected Optional<Empresa> findEmpresa(Rut rut) {
        return null;
    }
    Optional<Terminal> findTerminal(String nombre){return null;}

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        return null;
    }


    protected Optional<Bus> findBus(String patente) {
        return null;
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        return null;
    }

    protected Optional<Auxiliar> findAuxliar(IdPersona id, Rut rutEmpresa) {
        return null;
    }
}
