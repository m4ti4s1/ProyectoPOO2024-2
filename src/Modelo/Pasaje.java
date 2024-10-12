package Modelo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Pasaje {
    private long numero;
    private int asiento;
    private Viaje viaje;
    private Pasajero pasajero;
    private Venta venta;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {

        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.toInstant(ZoneOffset.UTC).toEpochMilli();
        this.numero = timestamp;
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
}

