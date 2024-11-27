
/*
--------------- TODO --------------
    1. Cambiar ciclos for, por streaming
    2. Crear metodos para persistencia


 */

package Controlador;

import Modelo.*;
import Utilidades.*;
import Excepciones.SVPException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class ControladorEmpresas implements Serializable {
    private static ControladorEmpresas instance = null;

    private ArrayList<Empresa> empresas=new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Terminal> terminales=new ArrayList<>();





    public static ControladorEmpresas getInstance() {
        if (instance == null) {
            instance = new ControladorEmpresas();
        }
        return instance;
    }

    public void createEmpresa(Rut rut,String nombre,String url)throws SVPException {
        Empresa up=new Empresa(rut, nombre);
        up.setUrl(url);
        findEmpresa(rut).ifPresentOrElse(
                t -> {throw new SVPException("Ya existe empresa con el rut indicado");},
                ()->empresas.add(up));
    }



    // todo Arreglar clase Bus, tiene que tener empresa
    public void createBus(String patente, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SVPException {

        Empresa empresa = findEmpresa(rutEmp).orElseThrow(() -> new SVPException("No existe empresa con el rut indicado"));

        Bus bus = new Bus(patente, nroAsientos, empresa);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        if (findBus(patente).isEmpty()) {
            //logica para bus que ya existe
            buses.add(bus);
        } else {
            throw new SVPException("Ya existe bus con la patente indicada");
        }
    }





    public void createTerminal(String nombre,Direccion direccion)throws SVPException {

        if(findTerminal(nombre).isPresent()) {
            throw new SVPException("Ya existe Terminal con el nombre indicado");
        }

        if(findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SVPException("Ya existe terminal en la comuna indicada");
        }

        // Si pasa ambas verificaciones, crea y añade el terminal
        Terminal terminal =new Terminal(nombre,direccion);
        terminales.add(terminal);

    }





    public void hireConductorForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir ) throws SVPException {
        Optional<Empresa> empresa=findEmpresa(rutEmp);
        if(empresa.isEmpty()){
            throw new SVPException("No existe empresa con el rut indicado ");
        }
        if(findConductor(id, rutEmp).isPresent()){
            throw new SVPException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }
        empresa.get().addConductor(id,nom,dir);
    }




    public void hireAuxiliarForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir) throws SVPException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);


        if(empresa.isEmpty()){
            throw new SVPException("No existe empresa con el rut indicado ");
        }


        if(findAuxliar(id, rutEmp).isPresent()) {
            throw new SVPException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }

        empresa.get().addAuxiliar(id,nom,dir);
    }

    public String[][] listEmpresas(){
        if(empresas.isEmpty()){return new String[0][0];}

        return empresas.stream()
                .map(empresa -> new String[] {
                        String.valueOf(empresa.getRut()),
                        empresa.getNombre(),
                        empresa.getUrl(),
                        String.valueOf(empresa.getTripulantes().length),
                        String.valueOf(empresa.getBuses().length),
                        String.valueOf(empresa.getVentas().length)
                })
                .toArray(String[][]::new);
        /*

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

         */

    }

    public String[][] listLlegadaSalidasTerminal(String nombre, LocalDate fecha) throws SVPException {

        Terminal terminal = findTerminal(nombre).orElseThrow(() -> new SVPException("No existe terminal con el nombre indicado"));


        List<String[]> viajes = Stream.concat(
                Arrays.stream(terminal.getSalidas())
                        .filter(viaje -> !viaje.getFecha().isBefore(fecha))
                        .map(viaje -> new String[] {
                                "Salida",
                                viaje.getHora().toString(),
                                viaje.getBus().getPatente(),
                                viaje.getBus().getEmpresa().getNombre(),
                                String.valueOf(viaje.getListaPasajeros().length)

                        }),
                Arrays.stream(terminal.getLlegadas())
                        .filter(viaje -> !viaje.getFecha().isBefore(fecha))
                        .map(viaje -> new String[] {
                                "Llegada",
                                viaje.getHora().toString(),
                                viaje.getBus().getPatente(),
                                viaje.getBus().getEmpresa().getNombre(),
                                String.valueOf(viaje.getListaPasajeros().length)
                        })
        )
        .toList();

        return viajes.toArray(new String[0][0]);


        /*

        Viaje[] llegadas= terminal.getLlegadas();
        Viaje[] salidas= terminal.getSalidas();

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

         */

    }


    public String[][] listVentasEmpresa(Rut rut) throws SVPException {
        Empresa empresa = findEmpresa(rut).orElseThrow(() -> new SVPException("No existe empresa con el rut indicado"));

        return Arrays.stream(empresa.getVentas())
                .map(venta -> new String[]{
                        String.valueOf(venta.getFecha()),
                        String.valueOf(venta.getTipo()),
                        String.valueOf(venta.getMontoPagado()),
                        String.valueOf(venta.getTipoPago())
                })
                .toArray(String[][]::new);


        /*
        Venta[] ArregloEmpresa= empresa.getVentas();

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

         */

    }

    protected void setInstanciaPersistente(ControladorEmpresas instanciaPersistente){
        instance = instanciaPersistente;
    }

    protected void setDatosIniciales(Object[] objetos){
        Object[] datos = objetos;

        empresas.clear();
        buses.clear();
        terminales.clear();

        if (datos[3] instanceof List) {
            empresas.addAll((List<Empresa>) datos[3]);
        }

        if (datos[4] instanceof List) {
            buses.addAll((List<Bus>) datos[4]);
        }
        if (datos[5] instanceof List) {
            terminales.addAll((List<Terminal>) datos[5]);
        }
    }


    // --------------- Finds con Streaming

    protected Optional<Empresa> findEmpresa(Rut rut) {

        return empresas.stream()
                .filter(empresa -> empresa.getRut().equals(rut))
                .findFirst();


    }




    // todo Verificar si se debe validar el case del nombre
    protected Optional<Terminal> findTerminal(String nombre){

        return terminales.stream()
                .filter(terminal -> terminal.getNombre().equals(nombre))
                .findFirst();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {

        return terminales.stream()
                .filter(terminal -> terminal.getDireccion().getComuna().equals(comuna))
                .findFirst();

    }


    protected Optional<Bus> findBus(String patente) {

        return buses.stream()
                .filter(bus -> bus.getPatente().equals(patente))
                .findFirst();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {


        return findEmpresa(rutEmpresa)
                .flatMap(empresa -> Arrays.stream(empresa.getTripulantes())
                        .filter(tripulante -> tripulante.getIdPersona().equals(id))
                        .findFirst()
                        .map(tripulante -> (Conductor) tripulante));

    }

    protected Optional<Auxiliar> findAuxliar(IdPersona id, Rut rutEmpresa) {


        return findEmpresa(rutEmpresa)
                .flatMap(empresa -> Arrays.stream(empresa.getTripulantes())
                        .filter(tripulante -> tripulante.getIdPersona().equals(id))
                        .findFirst()
                        .map(tripulante -> (Auxiliar) tripulante)
                );


    }
}
