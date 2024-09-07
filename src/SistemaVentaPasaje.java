import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentaPasaje {
    private List<Venta> ventas = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Bus> buses = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>(); // No se para que se implementa el SistemaVentaPasajes

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        Cliente cliente = new Cliente(id, nom, email);
        if (clientes.contains(cliente)) {
            //logica para cliente que ya existe
            return false;
        } else {
            //mensaje para cliente creado exitosamente
            clientes.add(cliente);
            return true;
        }
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        Bus bus = new Bus(patente, nroAsientos);
        bus.setMarca(marca);
        bus.setModelo(modelo);
        // Arreglar bus no puede utilizar contains, porque no tiene definido el equals
        if (buses.contains(bus)) {
            //logica para bus que ya existe
            return false;
        } else {
            //mensaje para bus creado exitosamente
            buses.add(bus);

            return true;
        }
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patente) {

        for (Bus bus : buses) {
            if (bus.getPatente().equals(patente)) {
                // ArrayList de los viajes del bus que tiene igual patente
                ArrayList<Viaje> viajesBus = bus.getViajes();

                Viaje viaje = new Viaje(fecha, hora, precio, bus);

                // Verificar si el viaje existe en el bus
                for (Viaje viajeBus : viajesBus) {
                    if (viajeBus.getFecha() == viaje.getFecha() && viajeBus.getHora() == viaje.getHora() && viajeBus.getBus() == viaje.getBus()) {
                        // viaje existe en el bus
                        // Mensaje diciendo que el viaje ya existe
                        return false;
                    }
                }
                // agregar viaje al bus si no existe
                bus.addViaje(viaje);

                // agregar viaje a la lista de viajes
                viajes.add(viaje);

            } else {

                //  Bus no existe, no se puede crear viaje

                return false;
            }
        }
        // mensaje viaje agregado exitosamente
        return true;
    }


    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fecha, IdPersona idCliente) {
        return true;
    }

    public String[][] getHorariosDisponibles(LocalDate fecha) {
        String[][] horarios = new String[3][3];
        return horarios;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        String[][] asientos= new String[3][3];
        return asientos;

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
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(idPasajero)) {
                return pasajero.getNombreCompleto().toString();
            }
        }
        return null;
    }
}
