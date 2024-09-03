import java.util.ArrayList;
import java.util.List;

public class Bus {

    //atributos
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private List<Viaje> viajes;

    //constructor
    public Bus(String patente, int nroAsientos) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.viajes = new ArrayList<>();
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public void addViaje(Viaje viaje) {
        this.viajes.add(viaje);
    }
}
