package Controlador;

import Modelo.*;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Excepciones.SistemaVentaPasajesExcepcion;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class SistemaVentaPasajes {
    private ArrayList<Venta> ventas = new ArrayList<>();
    private ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Viaje> viajes = new ArrayList<>(); // No se para que se implementa el Controlador.SistemaVentaPasajes


    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SistemaVentaPasajesExcepcion {
        Cliente cliente = new Cliente(id, nom, email);
        cliente.setTelefono(fono);

        if (findCliente(id) == null) {
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

        if (findPasajero(id) == null){
            pasajeros.add(pasajero);
        } else {
            throw new SistemaVentaPasajesExcepcion("Ya existe pasajero con el id indicado");
        }

    }



    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patente) {


        // verificar si existe el bus
        if (findBus(patente) != null) {
            Viaje viaje = new Viaje(fecha, hora, precio, findBus(patente));

            // si existe el viaje
            if (findViaje(fecha.toString() , hora.toString() , patente) == null) {
                // viaje existe en el bus
                viajes.add(viaje);
                return true;
            } else {
                return false;
            }

        } else {
            // Modelo.Bus no existe, viaje no se puede crear
            return false;
        }
    }


    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fecha, IdPersona idCliente) throws SistemaVentaPasajesExcepcion {
        // Verificar si el cliente existe
        Cliente cliente = findCliente(idCliente);
        if (cliente == null) {
            throw new SistemaVentaPasajesExcepcion("No existe cliente con el id indicado");
        }


        // verificar si existe una venta igual
        if (findVenta(idDoc, tipo) == null){
            Venta venta = new Venta(idDoc, tipo, fecha, cliente);
            ventas.add(venta);
        } else {
            throw new SistemaVentaPasajesExcepcion("Ya existe una venta con el id y tipo de documento indicados");
        }


        // todo verificar si existen viajes diponibles en la fecha y con termianles en las comunas con salida y llega indicados
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {
        // Crea una lista temporal para almacenar los datos de los viajes que coinciden con la fecha
        ArrayList<String[]> horarios = new ArrayList<>();

        // Recorre todos los viajes
        for (Viaje viaje : viajes) {
            // Verifica si la fecha del viaje coincide con la fecha dada
            if (viaje.getFecha().equals(fechaViaje)) {
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

        if(null==findViaje(""+fecha,""+hora,patBus)){
            return new String[]{"0"};
        }

        String[][] matriz= findViaje(""+fecha,""+hora,patBus).getAsientos();

        String []listAsientos=new String[matriz.length];
        for (int i=0;i<listAsientos.length;i++){
            if(matriz[i][1].equalsIgnoreCase("vacío")){
                listAsientos[i] = matriz[i][0];
            }else {listAsientos[i]="*";}
        }
        return listAsientos;

    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipo)) {
                return venta.getMonto();
            }
        }
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero) {
        for (Pasajero pasajero: pasajeros) {
            if (pasajero.getIdPersona().equals(idPasajero)) {
                return pasajero.getNombreCompleto().toString();
            }
        }
        return null;
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
    public void vendePasaje(String idDoc, TipoDocumento tipo , LocalDate fecha, LocalTime hora, String patBus, int asiento, IdPersona idPasajero) throws SistemaVentaPasajesExcepcion {


        if (findVenta(idDoc, tipo) == null) {
            throw new SistemaVentaPasajesExcepcion("No existe una venta ocn el id y tipo de documento indicados");
        }
        if (findViaje("" + fecha, "" + hora, patBus) == null) {
            throw new SistemaVentaPasajesExcepcion("No existe viaje con la fecha, hora y patente del bus indiados");
        }

        if (findPasajero(idPasajero) == null) {
            throw new SistemaVentaPasajesExcepcion("No existe pasajero con el id indicado");
        }



        // encontrar el pasajero y la venta correspondiente
        Pasajero pasajero = findPasajero(idPasajero);
        Venta venta = findVenta(idDoc, tipo);
        Viaje viaje = findViaje("" + fecha, "" + hora, patBus);

        if (viaje.existeDisponibilidad()) {
            Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, venta);
            venta.addPasaje(pasaje);
            viaje.addPasaje(pasaje);
        }

    }

    public String getNombreCliente(IdPersona idCliente){
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(idCliente)) {
                return cliente.getNombreCompleto().toString();
            }
        }
        return null;
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
        String[][] pasajes = new String[viajes.size()][5] ;
        for(int i=0;i<pasajes.length;i++){
            pasajes[i][0]=""+viajes.get(i).getFecha();
            pasajes[i][1]=""+viajes.get(i).getHora();
            pasajes[i][2]=""+viajes.get(i).getPrecio();
            pasajes[i][3]=""+viajes.get(i).getNroAsientosDisponibles();
            pasajes[i][4]=""+viajes.get(i).getBus().getPatente();
        }
        return pasajes;

    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        String [][]ListaPasajeros;
        ListaPasajeros= findViaje(fecha+"",hora+"",patBus).getListaPasajeros();
        return ListaPasajeros;

    }

    public String[] pasajesAImprimir(String idDocumento, TipoDocumento tipoDocumento) {
        Venta venta = findVenta(idDocumento, tipoDocumento);

        if (venta == null) {
            return new String[] {"No se encontraron pasajes para la venta con ID: " + idDocumento};
        }

        ArrayList<String> resultado = new ArrayList<>();

        resultado.add(":::: Imprimiendo los pasajes\n");
        resultado.add("------------------ PASAJE ------------------");

        for (Pasaje pasaje : venta.getPasajes()) {
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


    private Cliente findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (Objects.equals(cliente.getIdPersona(), id)) {
                return cliente;
            }
        }
        return null;
    }

    private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo() == tipoDocumento) {
                return venta;
            }
        }
        return null;
    }


    private Viaje findViaje(String fecha, String hora, String patenteBus) {

        for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha) && viaje.getHora().toString().equals(hora)  && viaje.getBus().getPatente().equals(patenteBus)) {
                return viaje;
            }
        }
        return null;
    }

    private Pasajero findPasajero(IdPersona idPersona) {
        for (Pasajero pasajero : pasajeros) {
            if (Objects.equals(pasajero.getIdPersona(), idPersona)) {
                return pasajero;
            }
        }
        return null;

    }


}
