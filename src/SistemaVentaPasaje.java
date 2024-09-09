import java.time.*;
import java.util.ArrayList;
import java.util.Objects;

public class SistemaVentaPasaje {
    private ArrayList<Venta> ventas = new ArrayList<>();
    private ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Viaje> viajes = new ArrayList<>(); // No se para que se implementa el SistemaVentaPasajes

    public static void main(String[] args) {
        SistemaVentaPasaje svp = new SistemaVentaPasaje();
        IdPersona id1 = Pasaporte.of("11.111.111-1", "chileno");
        IdPersona id2 = Pasaporte.of("22.222.222-2", "argentino");
        IdPersona id3 = Pasaporte.of("33.333.333-3", "boliviano");
        IdPersona id4 = Pasaporte.of("11.111.111-1", "chileno");


        Nombre n1 = new Nombre();
        n1.setNombres("Lucas Daniel");
        n1.setApellidoPaterno("Fernandez");
        n1.setApellidoMaterno("Garcia");

        Nombre n2 = new Nombre();
        n2.setNombres("Sofia Isabel");
        n2.setApellidoPaterno("Martinez");
        n2.setApellidoMaterno("Lopez");

        Nombre n3 = new Nombre();
        n3.setNombres("Carlos Alberto");
        n3.setApellidoPaterno("Rodriguez");
        n3.setApellidoMaterno("Silva");

        Nombre n4 = new Nombre();
        n4.setNombres("Carlos Alberto");
        n4.setApellidoPaterno("Rodriguez");
        n4.setApellidoMaterno("Silva");

        /*
         * Verificado que la creacion de clientes no acepta duplicados

        System.out.println(svp.createCliente(id1, n1, "123", "23@gmail.com"));
        System.out.println(svp.createCliente(id2, n2, "123", "23@gmail.com"));
        System.out.println(svp.createCliente(id3, n3, "123", "23@gmail.com"));
        System.out.println(svp.createCliente(id4, n4, "123", "23@gmail.com"));

         * Verificado que la creacion de pasajeros no acepta duplicados
        System.out.println(svp.createPasajero(id1, n1, "123", n1, "123"));
        System.out.println(svp.createPasajero(id2, n2, "123", n2,  "123"));
        System.out.println(svp.createPasajero(id3, n3, "123", n3, "123"));
        System.out.println(svp.createPasajero(id4, n4, "123", n4, "123"));

         */



    }

    // Verificado
    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        Cliente cliente = new Cliente(id, nom, email);
        cliente.setTelefono(fono);

        if (findCliente(id) == null) {
            clientes.add(cliente);
            return true;
        } else {
            return false;
            //mensaje para cliente creado exitosamente
        }
    }


    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto){
        Pasajero pasajero  = new Pasajero(id, nom);
        pasajero.setTelefono(fono);
        pasajero.setNomContacto(nomContacto);
        pasajero.setFonoContancto(fonoContacto);

        if (findPasajero(id) != null){
            return false;
        } else {
            pasajeros.add(pasajero);
            return true;
        }

    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        Bus bus = new Bus(patente, nroAsientos);
        bus.setMarca(marca);
        bus.setModelo(modelo);
        // Arreglar bus no puede utilizar contains, porque no tiene definido el equals
        if (findBus(patente) != null) {
            //logica para bus que ya existe
            return false;
        } else {
            //mensaje para bus creado exitosamente
            buses.add(bus);
            return true;
        }
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patente) {

        Viaje viaje = new Viaje(fecha, hora, precio, findBus(patente));

        if (findBus(patente) != null) {
            // si existe el viaje
            if (findViaje(fecha.toString() , hora.toString() , patente) != null) {
                // viaje existe en el bus
                return false;
            } else {
                viajes.add(viaje);
                return true;
            }

        } else {
            //logica bus no existe, viaje no se puede crear
            return false;
        }
    }


    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fecha, IdPersona idCliente) {
        // Verificar si el cliente existe
        Cliente cliente = findCliente(idCliente);
        if (cliente == null) {
            return false; // El cliente no existe
        }

        // Verificar si ya existe una venta con el mismo idDocumento y tipoDocumento
        Venta ventaExistente = findVenta(idDoc, tipo);
        if (ventaExistente != null) {
            return false; // Ya existe una venta con este idDocumento y tipoDocumento
        }

        // Crear una nueva venta
        Venta nuevaVenta = new Venta(idDoc, tipo, fecha, cliente);

        // Añadir la venta directamente a la lista de ventas
        ventas.add(nuevaVenta);

        return true; // Venta creada exitosamente
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {
        // Crea una lista temporal para almacenar los datos de los viajes que coinciden con la fecha
        ArrayList<String[]> horarios = new ArrayList<>();

        // Recorre todos los viajes
        for (Viaje viaje : viajes) {
            // Verifica si la fecha del viaje coincide con la fecha dada
            if (viaje.getFecha().equals(fechaViaje)) {
                // Obtener los datos relevantes
                String patenteBus = viaje.getBus().getPatente(); // Obtener la patente del bus
                String horaViaje = viaje.getHora().toString();   // Obtener la hora del viaje
                String precioPasaje = String.valueOf(viaje.getPrecio()); // Obtener el precio del pasaje
                String asientosDisponibles = String.valueOf(viaje.getNroAsientosDisponibles()); // Obtener asientos disponibles

                // Añade estos datos como un arreglo unidimensional a la lista
                horarios.add(new String[] {patenteBus, horaViaje, precioPasaje, asientosDisponibles});
            }
        }

        // Convierte la lista a un arreglo bidimensional
        String[][] resultado = new String[horarios.size()][4];
        for (int i = 0; i < horarios.size(); i++) {
            resultado[i] = horarios.get(i);
        }

        // Retorna el arreglo con los horarios disponibles, o un arreglo vacío si no hay viajes
        return resultado;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        if(null==findViaje(""+fecha,""+hora,patBus)){return new String[][];}
        return findViaje(""+fecha,""+hora,patBus).getAsientos();
        

    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipo)) {
                return venta.getMonto();
            }
        }
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero) {
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(idPasajero)) {
                return pasajero.getNombreCompleto().toString();
            }
        }
        return null;
    }

    public boolean vendePasaje(String idDoc,TipoDocumento tipo , LocalDate fecha , LocalTime hora, String patBus, int asiento, IdPersona idCli, IdPersona idPas, Nombre nomPas, Nombre nomCto, String fonoCto) {
         if(null==findViaje(""+fecha,""+hora,""+patBus)){return false;}
         if(null==findBus(patBus)){return false;}
         if(null==findCliente(idCli)){return false;}
         Venta ventapasaje =new Venta(idDoc,tipo,fecha,findCliente(idCli));
         Pasaje createPasaje=new Pasaje(asiento,findViaje(""+fecha,""+hora,""+patBus),findPasajero(idCli), ventapasaje);
        findViaje(""+fecha,""+hora,""+patBus).addPasaje(createPasaje);

        return false;
    }

    public String[][] listVentas() {
        String[][] listventas = new String[ventas.size()][7];
        for (int i=0;i<listventas.length;i++){
            listventas[i][0]=""+ventas.get(i).getIdDocumento();
            listventas[i][1]=""+ventas.get(i).getTipo();
            listventas[i][2]=""+ventas.get(i).getFecha();
            listventas[i][3]=""+ventas.get(i).getCliente().getIdPersona();
            listventas[i][4]=""+ventas.get(i).getCliente().getNombreCompleto();
            listventas[i][5]=""+ventas.get(i).getPasajes().length;
            listventas[i][6]=""+ventas.get(i).getMonto();

        }
        return listventas;
    }

    public String[][] listViajes() {
        String[][] pasajes = new String[viajes.size()][5] ;
        for(int i=0;i<pasajes.length;i++){
            pasajes[i][0]=""+viajes.get(i).getFecha();
            pasajes[i][1]=""+viajes.get(i).getHora();
            pasajes[i][2]=""+viajes.get(i).getPrecio();
            pasajes[i][3]=""+viajes.get(i).getNroAsientosDisponibles();
            pasajes[i][4]=""+viajes.get(i).getBus().getPatente();
        }
        return pasajes;

    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        String [][]ListaPasajeros;
        ListaPasajeros= findViaje(fecha+"",hora+"",patBus).getListaPasajeros();
        return ListaPasajeros;

    }

    private Cliente findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (Objects.equals(cliente.getIdPersona(), id)) {
                return cliente;
            }
        }
        return null;
    }

    private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo() == tipoDocumento) {
                return venta;
            }
        }
        return null;
    }


    private Bus findBus(String patente) {
        for (Bus bus : buses) {
            if (bus.getPatente().equals(patente)) {
                return bus;
            }
        }
        return null;
    }

    private Viaje findViaje(String fecha, String hora, String patenteBus) {

        for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha) && viaje.getHora().toString().equals(hora)  && viaje.getBus().getPatente().equals(patenteBus)) {
                return viaje;
            }
        }
        return null;
    }

    private Pasajero findPasajero(IdPersona idPersona) {
        for (Pasajero pasajero : pasajeros) {
            if (Objects.equals(pasajero.getIdPersona(), idPersona)) {
                return pasajero;
            }
        }
        return null;

    }


}
