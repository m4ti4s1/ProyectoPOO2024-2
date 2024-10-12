package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {

    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente; // Relacion con clase Modelo.Cliente

    private ArrayList<Pasaje> pasajes; // Relacion con clase Modelo.Pasaje

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        this.idDocumento = id;
        this.tipo = tipo;
        this.fecha = fec;
        this.cliente = cli;
        pasajes = new ArrayList<>();

    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
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

    public Pasaje[] getPasajes(){
        Pasaje[] listaPasajes = new Pasaje[pasajes.size()];


        for (int i = 0; i < listaPasajes.length; i++) {
            listaPasajes[i] = pasajes.get(i);
        }

        return listaPasajes;

    }
    public int getMonto(){
        int monto = 0;
        for (Pasaje pasaje : pasajes) {
            monto += pasaje.getViaje().getPrecio();
        }
        return monto;
    }

}
