package Persistencia;


import Excepciones.SVPException;
import Modelo.*;
import Utilidades.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IOSVP {

    private static IOSVP instance = new IOSVP();

    private IOSVP() {

    }

    public static IOSVP getInstance() {
        return instance;
    }

    public Object[] readDatosIniciales() throws SVPException {
        List<Cliente> clientes = new ArrayList<>();
        List<Pasajero> pasajeros = new ArrayList<>();
        List<Empresa> empresas = new ArrayList<>();
        List<Tripulante> tripulantes = new ArrayList<>();
        List<Terminal> terminales = new ArrayList<>();
        List<Bus> buses = new ArrayList<>();
        List<Viaje> viajes = new ArrayList<>();


        try {

            Scanner leeArchivo = new Scanner(new File("SVPDatosIniciales.txt"));
            leeArchivo.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085]");

            // Lee Seccion clientes y/o pasajeros

            while (leeArchivo.hasNext()) {
                String linea = leeArchivo.next();
                if (linea.equals("+")) {
                    break;
                }

                String[] datos = linea.split(";");
                String tipo = datos[0];
                IdPersona rut = Rut.of(datos[1]);
                String tratamiento = datos[2];
                String nombres = datos[3];
                String apPaterno = datos[4];
                String apMaterno = datos[5];
                String telefono = datos[6];

                // Crear Nombre
                Nombre nombre = crearNombre(tratamiento, nombres, apPaterno, apMaterno);

                if (tipo.equals("C")) { // Cliente
                    String email = datos[7];
                    Cliente cliente = crearCliente(rut, nombre, email, telefono);
                    clientes.add(cliente);

                } else if (tipo.equals("P")) { // Pasajero
                    String tratamientoContacto = datos[7];
                    String nombresContacto = datos[8];
                    String apPaternoContacto = datos[9];
                    String apMaternoContacto = datos[10];
                    String telefonoContacto = datos[11];

                    Nombre contacto = crearNombre(tratamientoContacto, nombresContacto, apPaternoContacto, apMaternoContacto);
                    Pasajero pasajero = crearPasajero(rut, nombre, telefono, contacto, telefonoContacto);
                    pasajeros.add(pasajero);

                } else if (tipo.equals("CP")) { // Cliente y Pasajero
                    String email = datos[7];
                    String tratamientoContacto = datos[8];
                    String nombresContacto = datos[9];
                    String apPaternoContacto = datos[10];
                    String apMaternoContacto = datos[11];
                    String telefonoContacto = datos[12];

                    // Crear Cliente
                    Cliente cliente = crearCliente(rut, nombre, email, telefono);
                    clientes.add(cliente);

                    // Crear Pasajero
                    Nombre contacto = crearNombre(tratamientoContacto, nombresContacto, apPaternoContacto, apMaternoContacto);
                    Pasajero pasajero = crearPasajero(rut, nombre, telefono, contacto, telefonoContacto);
                    pasajeros.add(pasajero);
                }
            }

            // Lee seccion empresas
            while (leeArchivo.hasNext()) {
                // Formato
                // rut;nombreEmpresa;urlEmpresa

                String linea = leeArchivo.next();
                if (linea.equals("+")) {
                    break;
                }

                String[] datos = linea.split(";");
                Rut rut = Rut.of(datos[0]);
                String nombreEmpresa = datos[1];
                String urlEmpresa = datos[2];
                Empresa empresa = new Empresa(rut, nombreEmpresa);
                empresa.setUrl(urlEmpresa);

                empresas.add(empresa);
            }

            // Lee seccion tripulantes
            while (leeArchivo.hasNext()) {
                // Formato
                //tipo:rut;tratamiento;nombres;ap.Paterno;apMaterno;calle;número;nombreComuna;rutEmpresa

                String linea = leeArchivo.next();
                if (linea.equals("+")) {
                    break;
                }

                String[] datos = linea.split(";");
                String tipo = datos[0];
                IdPersona rut = Rut.of(datos[1]);
                String tratamiento = datos[2];
                String nombres = datos[3];
                String apPaterno = datos[4];
                String apMaterno = datos[5];
                String calle = datos[6];
                int numero = Integer.parseInt(datos[7]);
                String nombreComuna = datos[8];
                Rut rutEmpresa = Rut.of(datos[9]);

                Optional<Empresa> empresaOpt = findEmpresa(empresas, rutEmpresa);
                if (empresaOpt.isPresent()) {
                    Empresa empresaEncontrada = empresaOpt.get();

                    Nombre nombreTripulante = crearNombre(tratamiento, nombres, apPaterno, apMaterno);

                    Direccion direccion = new Direccion(calle, numero, nombreComuna);

                    Optional<Tripulante> tripulanteExistente = findTripulante(empresaEncontrada, rut);

                    if (tripulanteExistente.isEmpty()) {

                        if (tipo.equals("A")) {  // Auxiliar
                            Auxiliar auxiliar = new Auxiliar(rut, nombreTripulante, direccion);
                            empresaEncontrada.addAuxiliar(rut, nombreTripulante, direccion);
                            tripulantes.add(auxiliar);
                        } else if (tipo.equals("C")) {  // Conductor
                            Conductor conductor = new Conductor(rut, nombreTripulante, direccion);
                            empresaEncontrada.addConductor(rut, nombreTripulante, direccion);
                            tripulantes.add(conductor);
                        }
                    }
                }
            }

            // Lee seccion terminales
            while (leeArchivo.hasNext()) {
                // Formato
                // nombre;calle;número;nombreComuna

                String linea = leeArchivo.next();
                if (linea.equals("+")) {
                    break;
                }
                String[] datos = linea.split(";");
                String nombre = datos[0];
                String calle = datos[1];
                int numero = Integer.parseInt(datos[2]);
                String nombreComuna = datos[3];
                Direccion direccion = new Direccion(calle, numero, nombreComuna);

                Terminal terminal = new Terminal(nombre, direccion);
                terminales.add(terminal);
            }

            // Lee seccion buses
            while (leeArchivo.hasNext()) {
                // Formato
                // patente;marca;modelo;númeroAsientos;rutEmpresa

                String linea = leeArchivo.next();
                if (linea.equals("+")) {
                    break;
                }
                String[] datos = linea.split(";");
                String patente = datos[0];
                String marca = datos[1];
                String modelo = datos[2];
                int numeroAsientos = Integer.parseInt(datos[3]);
                Rut rutEmpresa = Rut.of(datos[4]);
                Optional<Empresa> empresa = findEmpresa(empresas, rutEmpresa);

                if (empresa.isPresent()) {
                    Bus bus = new Bus(patente, numeroAsientos, empresa.get());
                    bus.setMarca(marca);
                    bus.setModelo(modelo);
                    buses.add(bus);
                    empresa.get().addBus(bus);
                }
            }

            // Lee seccion viajes

            while (leeArchivo.hasNext()) {

                // Formato
                //fecha;hora;precio;duración;patente;rutAuxiliar;rutConductor;nombreTerminalSalida;nombreTerminalLlegada

                String linea = leeArchivo.next();
                if (linea.equals("+")) {
                    break;
                }
                String[] datos = linea.split(";");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate fecha = LocalDate.parse(datos[0], formatter);
                LocalTime hora = LocalTime.parse(datos[1]);
                int precio = Integer.parseInt(datos[2]);
                int duracion = Integer.parseInt(datos[3]);
                String patente = datos[4];
                IdPersona rutAuxiliar = Rut.of(datos[5]);
                IdPersona rutConductor = Rut.of(datos[6]);
                String nombreTerminalSalida = datos[7];
                String nombreTerminalLlegada = datos[8];
                Optional<Bus> bus = findBus(buses, patente);
                Auxiliar auxiliar = null;
                List<Conductor> conductores = new ArrayList<>();


                for (Tripulante tripulante : tripulantes) {
                    if (tripulante.getIdPersona().equals(rutAuxiliar) && tripulante instanceof Auxiliar) {
                        auxiliar = (Auxiliar) tripulante;
                    }
                }


                for (Tripulante tripulante : tripulantes) {
                    if (tripulante.getIdPersona().equals(rutConductor) && tripulante instanceof Conductor) {
                        conductores.add((Conductor) tripulante);
                        if (conductores.size() == 2) {
                            break;
                        }
                    }
                }


                Optional<Terminal> terminalSalida = findTerminal(terminales, nombreTerminalSalida);
                Optional<Terminal> terminalLlegada = findTerminal(terminales, nombreTerminalLlegada);

                if (bus.isPresent() && auxiliar != null && !conductores.isEmpty()
                        && terminalSalida.isPresent() && terminalLlegada.isPresent()) {
                    Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus.get(), auxiliar, conductores.toArray(new Conductor[0]), terminalSalida.get(), terminalLlegada.get());
                    viajes.add(viaje);
                }
            }
        }catch (FileNotFoundException e) {
            throw new SVPException("No existe o no se puede abrir el archivo SVPDatosIniciales.txt");
        }

        Object[] resultado = new Object[]{
                clientes,
                pasajeros,
                empresas,
                terminales,
                buses,
                viajes,
                tripulantes
        };
        return resultado;
    }

    public void saveControladores(Object[] controladores) throws SVPException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SVPObjetos.obj"));
            oos.writeObject(controladores);

        }catch (FileNotFoundException e){
            throw new SVPException("No se puede abrir o crear el archivo SVPObjetos.obj");
        }catch (IOException e){
            throw new SVPException("No se puede grabar en el archivo SVPObjetos.obj");
        }
    }

    public Object[] readControladores() throws SVPException {

        Object[] controladores;

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SVPObjetos.obj"));
            controladores = (Object[]) ois.readObject();

        }catch (FileNotFoundException e){
            throw new SVPException("No existe o no se puede abrir el archivo SVPObjetos.obj");
        }catch (IOException | ClassNotFoundException e) {
            throw new SVPException("No se puede leer el archivo SVPObjetos.obj");
        }
        return controladores;
    }

    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivo) throws SVPException {
        try {

            PrintStream out = new PrintStream(nombreArchivo);
            for (Pasaje pasaje : pasajes) {
                if (pasaje != null) {
                    out.println(pasaje.toString());
                    out.println();
                }
            }

        }catch (IOException e) {
            throw new SVPException("No se puede abrir o crear el archivo " + nombreArchivo);
        }
    }

    private Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut) {
        return empresas.stream()
                .filter(emp -> emp.getRut().equals(rut))
                .findFirst();
    }

    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id){
        Tripulante[] tripulantes = empresa.getTripulantes();

        return Arrays.stream(tripulantes)
                .filter(tripulante -> tripulante.getIdPersona().equals(id))
                .findFirst();
    }

    private Optional<Bus> findBus(List<Bus> buses, String patente){
        return buses.stream()
                .filter(bus -> bus.getPatente().equals(patente))
                .findFirst();

    }

    private Optional<Terminal> findTerminal(List<Terminal> terminales, String nombre){
        return terminales.stream()
                .filter(terminal -> terminal.getNombre().equals(nombre))
                .findFirst();
    }

    private Nombre crearNombre(String tratamiento, String nombres, String apPaterno, String apMaterno) {
        Nombre nombre = new Nombre();
        nombre.setTratamiento(Tratamiento.valueOf(tratamiento));
        nombre.setNombres(nombres);
        nombre.setApellidoPaterno(apPaterno);
        nombre.setApellidoMaterno(apMaterno);
        return nombre;
    }

    private Cliente crearCliente(IdPersona rut, Nombre nombreCliente, String email, String telefono) {
        Cliente cliente = new Cliente(rut, nombreCliente, email);
        cliente.setTelefono(telefono);
        return cliente;
    }

    private Pasajero crearPasajero(IdPersona rut, Nombre nombrePasajero, String telefono, Nombre contacto, String telefonoContacto) {
        Pasajero pasajero = new Pasajero(rut, nombrePasajero);
        pasajero.setTelefono(telefono);
        pasajero.setNomContacto(contacto);
        pasajero.setFonoContancto(telefonoContacto);
        return pasajero;
    }
}