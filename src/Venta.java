import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;

    private ArrayList<Pasaje> pasajes;

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.fecha = fec;
        this.cliente = cli;
        pasajes = new ArrayList<>();

    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }


    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero,this);
        pasajes.add(pasaje);
    }

    public String[] getPasajes(){
        String[] listaPasajes = new String[pasajes.size()];

        for (Pasaje pasaje : pasajes) {
            // Terminar para imitar figura 10
        }

        return listaPasajes;

    }
    public int getMonto(){
        return monto;
    }

}
