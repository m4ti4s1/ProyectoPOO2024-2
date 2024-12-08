
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
import java.time.format.DateTimeFormatter;
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
        Empresa empresa = findEmpresa(rutEmp)
                .orElseThrow(() -> new SVPException("No existe empresa con el rut indicado"));
        if (findBus(patente).isPresent()) {
            throw new SVPException("Ya existe bus con la patente indicada");
        }

        Bus bus = new Bus(patente, nroAsientos, empresa);
        bus.setMarca(marca);
        bus.setModelo(modelo);
        buses.add(bus);
        empresa.addBus(bus);
    }





    public void createTerminal(String nombre,Direccion direccion)throws SVPException {

        if(findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SVPException("Ya existe terminal en la comuna indicada");
        }
        if(findTerminal(nombre).isPresent()) {
            throw new SVPException("Ya existe Terminal con el nombre indicado");
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
            throw new SVPException("Ya está contratado conductor con el id dado en la empresa señalada ");
        }
        empresa.get().addConductor(id,nom,dir);
    }




    public void hireAuxiliarForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir) throws SVPException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);


        if(empresa.isEmpty()){
            throw new SVPException("No existe empresa con el rut indicado ");
        }


        if(findAuxliar(id, rutEmp).isPresent()) {
            throw new SVPException("Ya está contratado auxiliar con el id dado en la empresa señalada ");
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

    }

    public String[][] listLlegadaSalidasTerminal(String nombre, LocalDate fecha) throws SVPException {

        Terminal terminal = findTerminal(nombre).orElseThrow(() -> new SVPException("\nNo existe terminal con el nombre indicado"));


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



    }


    public String[][] listVentasEmpresa(Rut rut) throws SVPException {
        Empresa empresa = findEmpresa(rut).orElseThrow(() -> new SVPException("No existe empresa con el rut indicado"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Arrays.stream(empresa.getVentas())
                .map(venta -> new String[]{
                        venta.getFecha().format(dtf),
                        String.valueOf(venta.getTipo()),
                        String.valueOf(venta.getMontoPagado()),
                        String.valueOf(venta.getTipoPago())
                })
                .toArray(String[][]::new);



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
