import java.util.Objects;
public class Persona {
    private idPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(idPersona id, Nombre nombre) {
        this.idPersona = id;
        this.nombreCompleto = nombre;
    }

    public idPersona getIdpersona() {
        return idPersona;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(Nombre nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    @Override
    public String toString() {
        return "{" + idPersona.toString() + ", " + nombreCompleto.toString() + ", " + telefono + "}";
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || this.getClass() != otro.getClass()) return false;
        Persona persona = (Persona) otro;
        return idPersona == persona.idPersona && nombreCompleto == persona.nombreCompleto && Objects.equals(telefono, persona.telefono);
    }
}
