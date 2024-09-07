import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentaPasaje {
    private ArrayList<Venta> ventas = new ArrayList<>();
    private ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Viaje> viajes = new ArrayList<>(); // No se para que se implementa el SistemaVentaPasajes

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        Cliente cliente = new Cliente(id, nom, email);
        if (findCliente(id) != null) {
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
        if (findBus(patente) != null) {
            //logica para bus que ya existe
            return false;
        } else {
            //mensaje para bus creado exitosamente
            buses.add(bus);
            return true;
        }
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patente) {

        Viaje viaje = new Viaje(fecha, hora, precio, findBus(patente));

        if (findBus(patente) != null) {
            // si existe el viaje
            if (findViaje(fecha.toString() , hora.toString() , patente) != null) {
                // viaje existe en el bus
                return false;
            } else {
                viajes.add(viaje);
                return true;
            }

        } else {
            //logica bus no existe, viaje no se puede crear
            return false;
        }
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

    public int vendePasaje(String idDoc, LocalDate fecha , LocalTime hora, String patBus, int asiento, IdPersona idCli, IdPersona idPas, Nombre nomPas, Nombre nomCto, String fonoCto) {
        return 0;
    }

    public String[][] listVentas() {
        String[][] ventas = new String[8][8];
        return ventas;
    }

    public String[][] listViajes() {
        String[][] pasajes = new String[8][8];
        return pasajes;

    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        String[][] pasajeros = new String[8][8];
        return pasajeros;

    }

    private Cliente findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona() == id) {
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


    private Bus findBus(String patente) {
        for (Bus bus : buses) {
            if (bus.getPatente().equals(patente)) {
                return bus;
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
            if (pasajero.getIdPersona() == idPersona) {
                return pasajero;
            }
        }
        return null;

    }


}
