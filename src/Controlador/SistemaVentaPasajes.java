
/*
--------------- TODO --------------
    1. Cambiar ciclos for, por streaming
    2. Arreglar metodo generatePasajesVenta
    3. Crear metodos para persistencia


 */


package Controlador;

import Modelo.*;
import Persistencia.IOSVP;
import Utilidades.*;
import Excepciones.SVPException;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.*;


public class SistemaVentaPasajes implements Serializable {

    // instancia unica (Singleton)
    private static SistemaVentaPasajes instance = new SistemaVentaPasajes();

    private ArrayList<Venta> ventas;
    private ArrayList<Pasajero> pasajeros;
    private ArrayList<Cliente> clientes;
    private ArrayList<Viaje> viajes; // No se para que se implementa el Controlador.SistemaVentaPasajes

    private ControladorEmpresas ctrlEmpresas = ControladorEmpresas.getInstance();

    private SistemaVentaPasajes() {
        ventas = new ArrayList<>();
        pasajeros = new ArrayList<>();
        clientes = new ArrayList<>();
        viajes = new ArrayList<>();
    }

    public static SistemaVentaPasajes getInstance() {
        return instance;
    }


    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SVPException {
        Cliente cliente = new Cliente(id, nom, email);
        cliente.setTelefono(fono);

        if (findCliente(id).isEmpty()) {
            clientes.add(cliente);
        } else {
            // El cliente ya existe y no puede ser agregado a la lista
            throw new SVPException("Ya existe cliente con el id indicado");
        }
    }
    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) throws SVPException {
        Pasajero pasajero  = new Pasajero(id, nom);
        pasajero.setTelefono(fono);
        pasajero.setNomContacto(nomContacto);
        pasajero.setFonoContancto(fonoContacto);

        if (findPasajero(id).isEmpty()){
            pasajeros.add(pasajero);
        } else {
            throw new SVPException("Ya existe pasajero con el id indicado");
        }
    }



    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion,  String patBus, IdPersona[] idTripulantes, String[] comunas) throws SVPException {


        Optional<Viaje> viajeOptional = findViaje(fecha.toString(), hora.toString(), patBus);

        if (viajeOptional.isPresent()) {
            throw new SVPException("Ya existe viaje con fecha, hora y patente indicados");
        }

        Bus busOptional = ctrlEmpresas.findBus(patBus).orElseThrow(() -> new SVPException("No existe un bus con la patente indicada"));


        String rutEmpresa = busOptional.getEmpresa().getRut() + "";
        Auxiliar auxiliar = ctrlEmpresas.findAuxliar(idTripulantes[0], Rut.of(rutEmpresa))
                    .orElseThrow(() -> new SVPException("No existe Auxiliar con el id indicado en la empresa con el rut indicado"));

        Conductor[] conductores = new Conductor[idTripulantes.length - 1];
        for (int i = 1; i < idTripulantes.length; i++) {
            conductores[i-1] = ctrlEmpresas.findConductor(idTripulantes[i], Rut.of(rutEmpresa))
                    .orElseThrow(() -> new SVPException("No existe conductor con el id indicado en la empresa con el rut indicado"));
        }




        Terminal salida  = ctrlEmpresas.findTerminalPorComuna(comunas[0]).orElseThrow(() -> new SVPException("No existe terminal de salida en la comuna indicada"));
        Terminal llegada = ctrlEmpresas.findTerminalPorComuna(comunas[1]).orElseThrow(() -> new SVPException("No existe terminal de llegada en la comuna indicada"));

        Viaje viaje = new Viaje(fecha, hora, precio, duracion, busOptional, auxiliar, conductores, salida, llegada);
        viajes.add(viaje);

    }


    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaViaje, String comSalida, String comLlegada,IdPersona idCliente, int nroPasajes) throws SVPException {

        Cliente cliente = findCliente(idCliente).orElseThrow(() ->
            new SVPException("No existe cliente con el id indicado"));

        ArrayList<Viaje> viajesDisponibles = new ArrayList<>();

        for (Viaje v : viajes) {
            if((v.getFecha().equals(fechaViaje) &&
                    v.getTerminalSalida().getDireccion().getComuna().equals(comSalida) &&
                    v.getTerminalLlegada().getDireccion().getComuna().equals(comLlegada))){
                viajesDisponibles.add(v);
            }
        }

        if(viajesDisponibles.isEmpty()){
            throw new SVPException("No existen viajes disponibles en la fecha y con terminales en las comunas de\n" +
                    "salida y llegada indicados");
        }

        if (findVenta(idDoc, tipo).isPresent()) {
            throw new SVPException("Ya existe una venta con el id y tipo de documento indicados");
        }

        Venta venta = new Venta(idDoc, tipo, LocalDate.now(), cliente);

        for (Viaje viaje : viajesDisponibles) {
            if(viaje.existeDisponibilidad(nroPasajes)){
                ventas.add(venta);
                return;
            }
        }


    }

    // Todo cambiar a Streaming
    public String[][] getHorariosDisponibles(LocalDate fechaViaje, String comunaSalida, String comunaLlegada, int nroPasajes) {


        List<String[]> horarios = viajes.stream()
                .filter(viaje -> viaje.getFecha().equals(fechaViaje) &&
                                        viaje.getTerminalSalida().getDireccion().getComuna().equals(comunaSalida) &&
                                        viaje.getTerminalLlegada().getDireccion().getComuna().equals(comunaLlegada) &&
                                        viaje.existeDisponibilidad(nroPasajes))
                .map(viaje -> new String[] {
                        viaje.getBus().getPatente(),
                        viaje.getHora().toString(),
                        String.valueOf(viaje.getPrecio()),
                        String.valueOf(viaje.getNroAsientosDisponibles())
                })
                .toList();

        return horarios.toArray(new String[0][0]);


    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {

        return findViaje(fecha.toString(), hora.toString(), patBus)
                .map (viaje ->
                        Arrays.stream(viaje.getAsientos())
                                .map(asiento -> asiento[1].equalsIgnoreCase("vacío") ? asiento[0] : "*")
                                .toArray(String[]::new)
                )
                .orElse(new String[]{"0"});




    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo) {

        return ventas.stream()
                .filter(venta -> venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipo))
                .map(Venta::getMonto)
                .findFirst();

    }

    public Optional<String> getNombrePasajero(IdPersona idPasajero) {
        return pasajeros.stream()
                .filter(pasajero -> pasajero.getIdPersona().equals(idPasajero))
                .map(Pasajero::getNombreCompleto)
                .map(String::valueOf)
                .findFirst();

    }

    public void vendePasaje(String idDoc, TipoDocumento tipo , LocalDate fechaViaje, LocalTime hora, String patBus, int asiento, IdPersona idPasajero) throws SVPException {
        Venta venta = findVenta(idDoc, tipo).orElseThrow(() ->
            new SVPException("No existe una venta con el id y tipo de documento indicados"));

        Viaje viaje = findViaje("" + fechaViaje, "" + hora, patBus).orElseThrow(() ->
            new SVPException("No existe viaje con la fecha, hora y patente del bus indicados"));

        Pasajero pasajero = findPasajero(idPasajero).orElseThrow(() ->
            new SVPException("No existe pasajero con el id indicado"));


        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, venta);
        venta.addPasaje(pasaje);
        viaje.addPasaje(pasaje);

    }

    // metodo no existe en UML
    public String getNombreCliente(IdPersona idCliente){
        return clientes.stream()
                .filter(cliente -> cliente.getIdPersona().equals(idCliente))
                .map(cliente -> cliente.getNombreCompleto().toString())
                .findFirst()
                .orElse(null);

    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo) throws SVPException {
        Venta venta= findVenta(idDocumento, tipo).orElseThrow(() ->
            new SVPException("No existe venta con el id y tipo de documento indicados"));

        if(!venta.pagaMonto()){
            throw new SVPException("La venta ya fue pagada");
        }
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjeta) throws SVPException {
        Venta venta= findVenta(idDocumento, tipo).orElseThrow(() ->
                new SVPException("No existe venta con el id y tipo de documento indicados"));

        if(!venta.pagaMonto(nroTarjeta)){
            throw new SVPException("La venta ya fue pagada");
        }
    }


    public String[][] listVentas() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return ventas.stream()
                .map(venta -> new String[]{
                        String.valueOf(venta.getIdDocumento()),
                        String.valueOf(venta.getTipo()),
                        venta.getFecha().format(dtf),
                        String.valueOf(venta.getCliente().getIdPersona()),
                        String.valueOf(venta.getCliente().getNombreCompleto()),
                        String.valueOf(venta.getPasajes().length),
                        String.valueOf(venta.getMonto())
                })
                .toArray(String[][]::new);

    }

    public String[][] listViajes() {

        return viajes.stream()
                .map(viaje -> new String[]{
                        viaje.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        String.valueOf(viaje.getHora()),
                        String.format("%02d:%02d", viaje.getFechaHoraTermino().getHour(), viaje.getFechaHoraTermino().getMinute()),
                        String.valueOf(viaje.getPrecio()),
                        String.valueOf(viaje.getNroAsientosDisponibles()),
                        String.valueOf(viaje.getBus().getPatente()),
                        String.valueOf(viaje.getTerminalSalida().getDireccion().getComuna()),
                        String.valueOf(viaje.getTerminalLlegada().getDireccion().getComuna()),
                        String.valueOf(viaje.getBus().getPatente()),
                })
                .toArray(String[][]::new);

    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SVPException {

        return findViaje(String.valueOf(fecha), String.valueOf(hora), String.valueOf(patBus))
                .map(Viaje::getListaPasajeros)
                .orElseThrow(() -> new SVPException("No existe viaje con la fecha, hora y patente de bus indicados"));

    }

    public void generatePasajesVenta(String idDocumento, TipoDocumento tipo) throws SVPException {

        Optional<Venta> venta = findVenta(idDocumento, tipo);

        if(venta.isPresent()) {
            Pasaje[] pasajes = venta.get().getPasajes();
            String nombreArchivo = idDocumento + tipo.toString().toLowerCase() + ".txt";

            try {
                IOSVP.getInstance().savePasajesDeVenta(pasajes, nombreArchivo);

            }catch (SVPException e) {
                throw new SVPException(e.getMessage());
            }
        }
    }

    public void readDatosIniciales() throws SVPException {
        Object[] datosIniciales = IOSVP.getInstance().readDatosIniciales();

        pasajeros.clear();
        clientes.clear();
        viajes.clear();

        if (datosIniciales[0] instanceof List) { // Clientes
            clientes.addAll((List<Cliente>) datosIniciales[0]);
        }
        if (datosIniciales[1] instanceof List) { // Pasajeros
            pasajeros.addAll((List<Pasajero>) datosIniciales[1]);
        }
        if (datosIniciales[2] instanceof List) { // Viajes
            viajes.addAll((List<Viaje>) datosIniciales[2]);
        }

        // Transfiere datos iniciales al controlador de empresas
        ctrlEmpresas.setDatosIniciales(datosIniciales);
    }

    public void saveDatosSistema() throws SVPException {
        Object[] controladores = {this, ctrlEmpresas};
        IOSVP.getInstance().saveControladores(controladores);
    }

    public void readDatosSistema() throws SVPException {
        Object[] controladores = IOSVP.getInstance().readControladores();

        for (Object c : controladores) {
            if (c instanceof SistemaVentaPasajes) {
                instance = (SistemaVentaPasajes) c;
            } else if (c instanceof ControladorEmpresas) {
                ctrlEmpresas.setInstanciaPersistente((ControladorEmpresas) c);
            }
        }
    }





    // --------- finds utilizando streaming
    private Optional<Cliente> findCliente(IdPersona id) {

        return clientes.stream()
                .filter(cliente -> Objects.equals(cliente.getIdPersona(), id))
                .findFirst();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {

        return ventas.stream()
                .filter(venta -> venta.getIdDocumento().equals(idDocumento))
                .filter(venta -> venta.getTipo() == tipoDocumento)
                .findFirst();
    }


    private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus) {

        return viajes.stream()
                .filter(viaje -> viaje.getFecha().toString().equals(fecha))
                .filter(viaje -> viaje.getHora().toString().equals(hora))
                .filter(viaje -> viaje.getBus().getPatente().equals(patenteBus))
                .findFirst();
    }

    private Optional<Pasajero> findPasajero(IdPersona idPersona) {

        return pasajeros.stream()
                .filter(pasajero -> Objects.equals(pasajero.getIdPersona(), idPersona))
                .findFirst();

    }


}
