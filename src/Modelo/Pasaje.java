package Modelo;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Pasaje {
    private long numero;
    private int asiento;


    private Viaje viaje;
    private Pasajero pasajero;
    private Venta venta;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {

        LocalDateTime now = LocalDateTime.now();
        this.numero = now.toInstant(ZoneOffset.UTC).toEpochMilli();

        this.asiento = asiento;
        this.viaje = viaje;
        this.pasajero = pasajero;
        this.venta = venta;
    }

    public long getNumero() {
        return numero;
    }

    public int getAsiento() {
        return asiento;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Venta getVenta() {
        return venta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("-------------------------------- PASAJE ELECTRÓNICO --------------------------------\n");

        sb.append(String.format("Nombre Empresa           Número de pasaje%n"));
        sb.append(String.format("%-25s %d%n",
                this.getViaje().getBus().getEmpresa().getNombre(),
                this.getNumero()));

        sb.append(String.format("Nombre Pasajero          RUT/Pasaporte%n"));
        sb.append(String.format("%-25s %s%n",
                this.getPasajero().getNombreCompleto(),
                this.getPasajero().getIdPersona()));

        sb.append(String.format("Patente bus              Asiento      Valor Pagado%n"));
        sb.append(String.format("%-25s %-12d %d%n",
                this.getViaje().getBus().getPatente(),
                this.getAsiento(),
                this.getViaje().getPrecio()));

        sb.append(String.format("Terminal origen          Terminal destino   Fecha        Hora%n"));
        sb.append(String.format("%-25s %-18s %-12s %s%n",
                this.getViaje().getTerminalSalida().getNombre(),
                this.getViaje().getTerminalLlegada().getNombre(),
                this.getViaje().getFecha().toString(),
                this.getViaje().getHora().format(DateTimeFormatter.ofPattern("HH:mm"))));

        sb.append("-----------------------------------------------------------------------------------\n");

        return sb.toString();
    }
}

