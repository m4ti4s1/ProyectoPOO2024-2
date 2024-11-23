
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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.*;


public class SistemaVentaPasajes {

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


        //No existe auxiliar con el id indicado en la empresa con el rut indicado
        //No existe conductor con el id indicado en la empresa con el rut indicado
        // No existe terminal de salida en la comuna indicada
        // No existe terminal de llegada en la comuna indicada

        // todo Falta el rut de la empresa para usar este metodo
        String rutEmpresa = busOptional.getEmpresa().getRut() + "";
        // ctrlEmpresas.findAuxliar(idTripulantes[0], )
        Auxiliar auxiliar = ctrlEmpresas.findAuxliar(idTripulantes[0], Rut.of(rutEmpresa))
                    .orElseThrow(() -> new SVPException("No existe Auxiliar con el id indicado en la empresa con el rut indicado"));
        Conductor[] conductores = new Conductor[idTripulantes.length - 1];
        for (int i = 1; i < conductores.length; i++) {
            conductores[i] = ctrlEmpresas.findConductor(idTripulantes[i], Rut.of(rutEmpresa))
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
        // Crea una lista temporal para almacenar los datos de los viajes que coinciden con la fecha
        ArrayList<String[]> horarios = new ArrayList<>();

        // Recorre todos los viajes
        for (Viaje viaje : viajes) {
            // Verifica si la fecha del viaje coincide con la fecha dada
            if (viaje.getFecha().equals(fechaViaje) &&
                viaje.getTerminalSalida().getDireccion().getComuna().equals(comunaSalida) &&
                viaje.getTerminalLlegada().getDireccion().getComuna().equals(comunaLlegada) &&
                viaje.existeDisponibilidad(nroPasajes)) {

                // Obtener los datos relevantes
                String patenteBus = viaje.getBus().getPatente(); // Obtener la patente del bus
                String horaViaje = viaje.getHora().toString();   // Obtener la hora del viaje
                String precioPasaje = String.valueOf(viaje.getPrecio()); // Obtener el precio del pasaje
                String asientosDisponibles = String.valueOf(viaje.getNroAsientosDisponibles()); // Obtener asientos disponibles

                // Añade estos datos como un arreglo unidimensional a la lista
                horarios.add(new String[] {patenteBus, horaViaje, precioPasaje, asientosDisponibles});
            }
        }

        // Convierte la lista a un arreglo bidimensional
        String[][] resultado = new String[horarios.size()][4];
        for (int i = 0; i < horarios.size(); i++) {
            resultado[i] = horarios.get(i);
        }

        // Retorna el arreglo con los horarios disponibles, o un arreglo vacío si no hay viajes
        return resultado;
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Optional<Viaje> viajeOpt = findViaje("" + fecha, "" + hora, patBus);
        if(findViaje("" + fecha, "" + hora, patBus).isEmpty()){
            return new String[]{"0"};
        }

        String[][] matriz= viajeOpt.get().getAsientos();

        String []listAsientos=new String[matriz.length];
        for (int i=0;i<listAsientos.length;i++){
            if(matriz[i][1].equalsIgnoreCase("vacío")){
                listAsientos[i] = matriz[i][0];
            }else {listAsientos[i]="*";}
        }
        return listAsientos;

    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo) {

        return ventas.stream()
                .filter(venta -> venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipo))
                .map(Venta::getMonto)
                .findFirst();

        /*
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipo)) {
                return Optional.of(venta.getMonto());
            }
        }
        return Optional.empty();
         */
    }

    public Optional<String> getNombrePasajero(IdPersona idPasajero) {
        return pasajeros.stream()
                .filter(pasajero -> pasajero.getIdPersona().equals(idPasajero))
                .map(Pasajero::getNombreCompleto)
                .map(String::valueOf)
                .findFirst();

        /*
        for (Pasajero pasajero: pasajeros) {
            if (pasajero.getIdPersona().equals(idPasajero)) {
                return Optional.of(pasajero.getNombreCompleto().toString());
            }
        }
        return Optional.empty();

         */
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
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(idCliente)) {
                return cliente.getNombreCompleto().toString();
            }
        }
        return null;
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

        return ventas.stream()
                .map(venta -> new String[]{
                        String.valueOf(venta.getIdDocumento()),
                        String.valueOf(venta.getTipo()),
                        String.valueOf(venta.getFecha()),
                        String.valueOf(venta.getCliente().getIdPersona()),
                        String.valueOf(venta.getCliente().getNombreCompleto()),
                        String.valueOf(venta.getPasajes().length),
                        String.valueOf(venta.getMonto())
                })
                .toArray(String[][]::new);

        /*
        String[][] listventas = new String[ventas.size()][7];
        for (int i=0;i<listventas.length;i++){
            listventas[i][0]=""+ventas.get(i).getIdDocumento();
            listventas[i][1]=""+ventas.get(i).getTipo();
            listventas[i][2]=""+ventas.get(i).getFecha();
            listventas[i][3]=""+ventas.get(i).getCliente().getIdPersona();
            listventas[i][4]=""+ventas.get(i).getCliente().getNombreCompleto();
            listventas[i][5]=""+ventas.get(i).getPasajes().length;
            listventas[i][6]=""+ventas.get(i).getMonto();

        }
        return listventas;

         */
    }

    public String[][] listViajes() {

        return viajes.stream()
                .map(viaje -> new String[]{
                        String.valueOf(viaje.getHora()),
                        String.valueOf(viaje.getFecha()),
                        String.format("%02d:%02d", viaje.getFechaHoraTermino().getHour(), viaje.getFechaHoraTermino().getMinute()),
                        String.valueOf(viaje.getPrecio()),
                        String.valueOf(viaje.getNroAsientosDisponibles()),
                        String.valueOf(viaje.getBus().getPatente()),
                        String.valueOf(viaje.getTerminalSalida().getDireccion().getComuna()),
                        String.valueOf(viaje.getTerminalLlegada().getDireccion().getComuna()),
                        String.valueOf(viaje.getBus().getPatente()),
                })
                .toArray(String[][]::new);
        /*

        String[][] pasajes = new String[viajes.size()][8] ;
        for(int i=0;i<pasajes.length;i++){
            pasajes[i][0]=""+viajes.get(i).getFecha();
            pasajes[i][1]=""+viajes.get(i).getHora();
            LocalDateTime horaLlegada = viajes.get(i).getFechaHoraTermino();
            pasajes[i][2]=String.format("%02d:%02d", horaLlegada.getHour(), horaLlegada.getMinute());
            pasajes[i][3]=""+viajes.get(i).getPrecio();
            pasajes[i][4]=""+viajes.get(i).getNroAsientosDisponibles();
            pasajes[i][5]=viajes.get(i).getBus().getPatente();
            pasajes[i][6]=viajes.get(i).getTerminalSalida().getDireccion().getComuna();
            pasajes[i][7]=viajes.get(i).getTerminalLlegada().getDireccion().getComuna();


        }
        return pasajes;

         */

    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SVPException {

        return findViaje(String.valueOf(fecha), String.valueOf(hora), String.valueOf(patBus))
                .map(Viaje::getListaPasajeros)
                .orElseThrow(() -> new SVPException("No existe viaje con la fecha, hora y patente de bus indicados"));

        /*
        Viaje viaje = findViaje(fecha.toString(), hora.toString(), patBus)
                .orElseThrow(() -> new SVPException("No existe viaje con la fecha, hora y patente de bus indicados"));

        return viaje.getListaPasajeros();

         */


    }

    // metodo no esta en el UML
    // cambiar nombre a genearte PasajesVenta y cambiar retorno por void
    // Genera un archivo de texto con los pasajes
    public String[] pasajesAImprimir(String idDocumento, TipoDocumento tipoDocumento) {
        Optional<Venta> venta = findVenta(idDocumento, tipoDocumento);

        if (venta.isEmpty()) {
            return new String[] {"No se encontraron pasajes para la venta con ID: " + idDocumento};
        }

        ArrayList<String> resultado = new ArrayList<>();

        resultado.add(":::: Imprimiendo los pasajes\n");
        resultado.add("------------------ PASAJE ------------------");

        for (Pasaje pasaje : venta.get().getPasajes()) {
            Viaje viaje = pasaje.getViaje();
            Pasajero pasajero = pasaje.getPasajero();

            resultado.add("NUMERO DE PASAJE  : " + pasaje.getNumero());
            resultado.add("FECHA DE VIAJE    : " + viaje.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            resultado.add("HORA DE VIAJE     : " + viaje.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
            resultado.add("PATENTE BUS       : " + viaje.getBus().getPatente());
            resultado.add("ASIENTO           : " + pasaje.getAsiento());
            resultado.add("RUT/PASAPORTE     : " + pasajero.getIdPersona());
            resultado.add("NOMBRE PASAJERO   : " + pasajero.getNombreCompleto().toString());
            resultado.add("--------------------------------------------\n");
        }

        return resultado.toArray(new String[0]);
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

        for (Object obj : datosIniciales) {

            if (obj instanceof Pasajero) {
                pasajeros.add((Pasajero) obj);
            } else if (obj instanceof Cliente) {
                clientes.add((Cliente) obj);
            }else if (obj instanceof Viaje) {
                viajes.add((Viaje) obj);
            }
        }
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
