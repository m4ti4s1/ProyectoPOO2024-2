package Controlador;

import Modelo.*;
import Utilidades.*;
import Excepciones.SistemaVentaPasajesExcepcion;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

// todo implementar singleton
public class ControladorEmpresas {


    ArrayList<Bus> buses = new ArrayList<>();

    public void createBus(String patente, String marca, String modelo, int nroAsientos) throws SistemaVentaPasajesExcepcion {
        Bus bus = new Bus(patente, nroAsientos);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        // Arreglar bus no puede utilizar contains, porque no tiene definido el equals
        if (findBus(patente) == null) {
            //logica para bus que ya existe
            buses.add(bus);
        } else {
            throw new SistemaVentaPasajesExcepcion("Ya existe bus con la patente indicada");
        }
    }

    // todo
    protected Optional<Empresa> findEmpresa(Rut rut) {
        return null;
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        return null;
    }


    protected Optional<Bus> findBus(String patente) {
        return null;
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        return null;
    }

    protected Optional<Auxiliar> findAuxliar(IdPersona id, Rut rutEmpresa) {
        return null;
    }
}
