package Modelo;

import Utilidades.Nombre;

import java.util.ArrayList;
public class Cliente extends Persona {

    private String email;
    private Persona persona;
    private ArrayList<Venta> ventas;

    public Cliente(Utilidades.IdPersona id, Nombre nom, String email) {
        super(id, nom);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
