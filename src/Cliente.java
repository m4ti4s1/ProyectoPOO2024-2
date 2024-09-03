public class Cliente extends Persona {
    private String email;
    private Persona persona;

    public Cliente(idPersona id, Nombre nom, String email) {
        this.persona = new Persona(id, nom);
        this.email = email
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
