package Modelo;

import java.util.ArrayList;

public class Bus {

    //atributos
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private Empresa empresa;
    private ArrayList<Viaje> viajes;

    //constructor
    public Bus(String patente, int nroAsientos, Empresa emp) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.empresa = emp;
        // Asocia el bus con la empresa
        empresa.addBus(this);
        this.viajes = new ArrayList<>();
    }

    public String getPatente() {
        return patente;
    }

    public Empresa getEmpresa() {
        return empresa;
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

    public Viaje[] getViajes() {
        return viajes.toArray(new Viaje[0]);
    }
}
