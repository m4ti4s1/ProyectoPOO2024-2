package Modelo;

import Utilidades.Direccion;

import java.util.ArrayList;

public class Terminal {

    private String nombre;
    private Direccion direccion;

    private ArrayList<Viaje> llegada;
    private ArrayList<Viaje> salida;

    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        llegada = new ArrayList<>();
        salida = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addLlegada(Viaje viaje) {
        llegada.add(viaje);
    }

    public void addSalida(Viaje viaje) {
        salida.add(viaje);
    }

    public Viaje[] getLlegadas() {
        return llegada.toArray(new Viaje[0]);
    }

    public Viaje[] getSalidas() {
        return salida.toArray(new Viaje[0]);
    }
}
