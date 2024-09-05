import java.time.*;
import java.util.ArrayList;
public class SistemaVentaPasaje {
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

}
