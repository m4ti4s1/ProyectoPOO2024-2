import java.util.Objects;
public class Persona {
    private IdPersona IdPersona;
    private Nombre nombreCompleto;
    private String telefono;


    public Persona(IdPersona id, Nombre nombre) {
        this.IdPersona = id;
        this.nombreCompleto = nombre;
        System.out.println();
    }

    public IdPersona getIdpersona() {
        return IdPersona;
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
        return "{" + IdPersona.toString() + ", " + nombreCompleto.toString() + ", " + telefono + "}";
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || this.getClass() != otro.getClass()) return false;
        Persona persona = (Persona) otro;
        return IdPersona == persona.IdPersona && nombreCompleto == persona.nombreCompleto && Objects.equals(telefono, persona.telefono);
    }
}