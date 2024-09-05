public class Pasajero extends Persona {
    private Nombre nomContacto;
    private String fonoContacto;

    public Nombre getNomContacto() {
        return nomContacto;
    }

    public void setNomContacto(Nombre nom) {
        this.nomContacto = nom;
    }

    public String getFonoContancto() {
        return fonoContacto;
    }

    public void setFonoContancto(String fono) {
        this.fonoContacto = fono;
    }
}
