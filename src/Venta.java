import java.time.LocalDate;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.fecha = fec;
        this.cliente = cli;

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
    public createPasaje(int asiento, Viaje viaje,Pasajero pasajero) {
        Pasaje pasaje = new Pasaje(asiento,viaje,pasajero,this);
    }
    public String[] getPasajes(){
        return Pasaje[];

    }
    public int getMonto(){
        return monto;
    }

}
