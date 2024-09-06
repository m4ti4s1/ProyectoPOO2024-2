import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentaPasaje {
    private List<Venta> ventas = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Cliente> clientes = new ArrayList<>();
    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        Persona cliente = new Cliente(id, nom, email);
        return true;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        return true;
    }

    public boolean createViaje(LocalDate fecha, int precio, String patBus) {
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
