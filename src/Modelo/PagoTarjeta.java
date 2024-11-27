package Modelo;

import java.io.Serializable;

public class PagoTarjeta extends Pago implements Serializable {

    private long nroTarjeta;

    public PagoTarjeta(int monto, long nroTarjeta) {
        super(monto);
        this.nroTarjeta = nroTarjeta;
    }
}
