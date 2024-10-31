package Controlador;

import Modelo.*;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Excepciones.SistemaVentaPasajesExcepcion;
import Utilidades.Rut;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


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


    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SistemaVentaPasajesExcepcion {
        Cliente cliente = new Cliente(id, nom, email);
        cliente.setTelefono(fono);

        if (findCliente(id).isEmpty()) {
            clientes.add(cliente);
        } else {
            // El cliente ya existe y no puede ser agregado a la lista
            throw new SistemaVentaPasajesExcepcion("Ya existe cliente con el id indicado");
        }
    }
    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) throws SistemaVentaPasajesExcepcion {
        Pasajero pasajero  = new Pasajero(id, nom);
        pasajero.setTelefono(fono);
        pasajero.setNomContacto(nomContacto);
        pasajero.setFonoContancto(fonoContacto);

        if (findPasajero(id).isEmpty()){
            pasajeros.add(pasajero);
        } else {
            throw new SistemaVentaPasajesExcepcion("Ya existe pasajero con el id indicado");
        }
    }



    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion,  String patBus, IdPersona[] idTripulantes, String[] comunas) throws SistemaVentaPasajesExcepcion {


        Optional<Viaje> viajeOptional = findViaje(fecha.toString(), hora.toString(), patBus);

        if (viajeOptional.isPresent()) {
            throw new SistemaVentaPasajesExcepcion("Ya existe viaje con fecha, hora y patente indicados");
        }

        Bus busOptional = ctrlEmpresas.findBus(patBus).orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe un bus con la patente indicada"));


        //No existe auxiliar con el id indicado en la empresa con el rut indicado
        //No existe conductor con el id indicado en la empresa con el rut indicado
        // No existe terminal de salida en la comuna indicada
        // No existe terminal de llegada en la comuna indicada

        // todo Falta el rut de la empresa para usar este metodo
        String rutEmpresa = busOptional.getEmpresa().getRut() + "";
        // ctrlEmpresas.findAuxliar(idTripulantes[0], )
        Auxiliar auxiliar = ctrlEmpresas.findAuxliar(idTripulantes[0], Rut.of(rutEmpresa))
                    .orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe Auxiliar con el id indicado en la empresa con el rut indicado"));
        Conductor[] conductores = new Conductor[idTripulantes.length - 1];
        for (int i = 1; i < conductores.length; i++) {
            conductores[i] = ctrlEmpresas.findConductor(idTripulantes[i], Rut.of(rutEmpresa))
                    .orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe conductor con el id indicado en la empresa con el rut indicado"));
        }




        Terminal salida  = ctrlEmpresas.findTerminalPorComuna(comunas[0]).orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe terminal de salida en la comuna indicada"));
        Terminal llegada = ctrlEmpresas.findTerminalPorComuna(comunas[1]).orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe termial de llegada en la comuna indicada"));

        Viaje viaje = new Viaje(fecha, hora, precio, duracion, busOptional, auxiliar, conductores, salida, llegada);
        viajes.add(viaje);

    }


    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaViaje, String comSalida, String comLlegada,IdPersona idCliente, int nroPasajes) throws SistemaVentaPasajesExcepcion {

        Cliente cliente = findCliente(idCliente).orElseThrow(() ->
            new SistemaVentaPasajesExcepcion("No existe cliente con el id indicado"));

        ArrayList<Viaje> viajesDisponibles = new ArrayList<>();

        for (Viaje v : viajes) {
            if(!(v.getFecha().equals(fechaViaje) &&
                    v.getTerminalSalida().getDireccion().getComuna().equals(comSalida) &&
                    v.getTerminalLlegada().getDireccion().getComuna().equals(comLlegada))){
                viajesDisponibles.add(v);
            }
        }

        if(viajesDisponibles.isEmpty()){
            throw new SistemaVentaPasajesExcepcion("No existen viajes disponibles en la fecha y con terminales en las comunas de\n" +
                    "salida y llegada indicados");
        }

        if (findVenta(idDoc, tipo).isPresent()) {
            throw new SistemaVentaPasajesExcepcion("Ya existe una venta con el id y tipo de documento indicados");
        }

        Venta venta = new Venta(idDoc, tipo, fechaViaje, cliente);

        for (Viaje viaje : viajesDisponibles) {
            if(viaje.existeDisponibilidad(nroPasajes)){
                ventas.add(venta);
                return;
            }
        }


    }

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
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipo)) {
                return Optional.of(venta.getMonto());
            }
        }
        return Optional.empty();
    }

    public Optional<String> getNombrePasajero(IdPersona idPasajero) {
        for (Pasajero pasajero: pasajeros) {
            if (pasajero.getIdPersona().equals(idPasajero)) {
                return Optional.of(pasajero.getNombreCompleto().toString());
            }
        }
        return Optional.empty();
    }

    /*
    public boolean vendePasaje(String idDoc,Modelo.TipoDocumento tipo , LocalDate fecha , LocalTime hora, String patBus, int asiento, Utilidades.IdPersona idCli, Utilidades.IdPersona idPas, Utilidades.Nombre nomPas, Utilidades.Nombre nomCto) {
         if(null==findViaje(""+fecha,""+hora,""+patBus)){return false;}
         if(null==findBus(patBus)){return false;}
         if(null==findCliente(idCli)){return false;}
         Modelo.Venta ventapasaje =new Modelo.Venta(idDoc,tipo,fecha,findCliente(idCli));
         ventas.add(ventapasaje);
         Modelo.Pasaje createPasaje=new Modelo.Pasaje(asiento,findViaje(""+fecha,""+hora,""+patBus),findPasajero(idCli), ventapasaje);
        findViaje(""+fecha,""+hora,""+patBus).addPasaje(createPasaje);

        return false;
    }
     */
    public void vendePasaje(String idDoc, TipoDocumento tipo , LocalDate fechaViaje, LocalTime hora, String patBus, int asiento, IdPersona idPasajero) throws SistemaVentaPasajesExcepcion {
        Venta venta = findVenta(idDoc, tipo).orElseThrow(() ->
            new SistemaVentaPasajesExcepcion("No existe una venta con el id y tipo de documento indicados"));

        Viaje viaje = findViaje("" + fechaViaje, "" + hora, patBus).orElseThrow(() ->
            new SistemaVentaPasajesExcepcion("No existe viaje con la fecha, hora y patente del bus indicados"));

        Pasajero pasajero = findPasajero(idPasajero).orElseThrow(() ->
            new SistemaVentaPasajesExcepcion("No existe pasajero con el id indicado"));


        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, venta);
        venta.addPasaje(pasaje);
        viaje.addPasaje(pasaje);

    }

    public String getNombreCliente(IdPersona idCliente){
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(idCliente)) {
                return cliente.getNombreCompleto().toString();
            }
        }
        return null;
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo){
        Venta venta= findVenta(idDocumento, tipo).orElseThrow(() ->
            new SistemaVentaPasajesExcepcion("No existe venta con el id y tipo de documento indicados"));

        if(!venta.pagaMonto()){
            throw new SistemaVentaPasajesExcepcion("La venta ya fue pagada");
        }
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjeta) throws SistemaVentaPasajesExcepcion {
        Venta venta= findVenta(idDocumento, tipo).orElseThrow(() ->
                new SistemaVentaPasajesExcepcion("No existe venta con el id y tipo de documento indicados"));

        if(!venta.pagaMonto(nroTarjeta)){
            throw new SistemaVentaPasajesExcepcion("La venta ya fue pagada");
        }
    }


    public String[][] listVentas() {
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
    }

    public String[][] listViajes() {
        String[][] pasajes = new String[viajes.size()][8] ;
        for(int i=0;i<pasajes.length;i++){
            pasajes[i][0]=""+viajes.get(i).getFecha();
            pasajes[i][1]=""+viajes.get(i).getHora();
            LocalDateTime horaLlegada = viajes.get(i).getFechaHoraTermino();
            pasajes[i][2]=String.format("%02d:%02d", horaLlegada.getHour(), horaLlegada.getMinute());
            pasajes[i][3]=""+viajes.get(i).getPrecio();
            pasajes[i][4]=""+viajes.get(i).getNroAsientosDisponibles();
            pasajes[i][5]=viajes.get(i).getBus().getPatente();
            pasajes[i][6]=""+viajes.get(i).getTerminalSalida();
            pasajes[i][7]=""+viajes.get(i).getTerminalLlegada();


        }
        return pasajes;

    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SistemaVentaPasajesExcepcion {
        Viaje viaje = findViaje(fecha.toString(), hora.toString(), patBus)
                .orElseThrow(() -> new SistemaVentaPasajesExcepcion("No existe viaje con la fecha, hora y patente de bus indicados"));

        return viaje.getListaPasajeros();

    }

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


    private Optional<Cliente> findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (Objects.equals(cliente.getIdPersona(), id)) {
                return Optional.of(cliente);
            }
        }
        return Optional.empty();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo() == tipoDocumento) {
                return Optional.of(venta);
            }
        }
        return Optional.empty();
    }


    private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus) {

        for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha) && viaje.getHora().toString().equals(hora)  && viaje.getBus().getPatente().equals(patenteBus)) {
                return Optional.of(viaje);
            }
        }
        return Optional.empty();
    }

    private Optional<Pasajero> findPasajero(IdPersona idPersona) {
        for (Pasajero pasajero : pasajeros) {
            if (Objects.equals(pasajero.getIdPersona(), idPersona)) {
                return Optional.of(pasajero);
            }
        }
        return Optional.empty();

    }


}
