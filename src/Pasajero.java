public class Pasajero extends Persona {
    private Nombre nomContacto;
    private String fonoContancto;


    public Pasajero() {
        super();
    }

    public Nombre getNomContacto() {
        return nomContacto;
    }

    public void setNomContacto(Nombre nom) {
        this.nomContacto = nom;
    }

    public String getFonoContancto() {
        return fonoContancto;
    }

    public void setFonoContancto(String fono) {
        this.fonoContancto = fono;
    }
}
