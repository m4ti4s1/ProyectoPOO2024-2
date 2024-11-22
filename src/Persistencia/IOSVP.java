package Persistencia;


import Modelo.*;
import Utilidades.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IOSVP {

    private static IOSVP instance = new IOSVP();

    private IOSVP() {

    }

    public static IOSVP getInstance() {
        return instance;
    }

    public Object[] readDatosIniciales() throws FileNotFoundException {
        List<Cliente> clientes = new ArrayList<>();
        List<Pasajero> pasajeros = new ArrayList<>();
        List<Empresa> empresas = new ArrayList<>();
        List<Tripulante> tripulantes = new ArrayList<>();
        List<Terminal> terminales = new ArrayList<>();
        List<Bus> buses = new ArrayList<>();
        List<Viaje> viajes = new ArrayList<>();



        Scanner leeArchivo = new Scanner(new File("SVPDatosIniciales.txt"));
        leeArchivo.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085]");

        // Lee Seccion clientes y/o pasajeros

        while(leeArchivo.hasNext()){
            // Formato
            // tipo;rut;tratamiento;nombres;ap.Paterno;ap.Materno;teléfono;<datosFinales>

            String linea = leeArchivo.next();

            if (linea.equals("+")){
                break;
            }

            String[] datos= linea.split(";");
            String tipo = datos[0];
            IdPersona rut = Rut.of(datos[1]);
            Tratamiento tratamiento = Tratamiento.valueOf(datos[2]);
            String nombres = datos[3];
            String apPaterno = datos[4];
            String apMaterno = datos[5];
            String telefono = datos[6];

            // Si es cliente
            if (tipo.equals("C")){ //<datosFinales> es email
                Nombre nombreCliente = new Nombre();
                String email = datos[7];
                Cliente cliente = new Cliente(rut,nombreCliente, email);
                nombreCliente.setNombres(nombres);
                nombreCliente.setTratamiento(tratamiento);
                nombreCliente.setApellidoPaterno(apPaterno);
                nombreCliente.setApellidoMaterno(apMaterno);
                cliente.setTelefono(telefono);

                clientes.add(cliente);

            // Si es Pasajero
            } else if (tipo.equals("P")){ //<datosContacto> es tratamiento;nombres;ap.Paterno;ap.Materno;teléfono
                Tratamiento tratamientoContacto = Tratamiento.valueOf(datos[7]);
                String nombresContacto = datos[8];
                String apPaternoContacto = datos[9];
                String apMaternoContacto = datos[10];
                String telefonoContacto = datos[11];

                Nombre nombrePasajero = new Nombre();
                nombrePasajero.setTratamiento(tratamiento);
                nombrePasajero.setNombres(nombres);
                nombrePasajero.setApellidoPaterno(apPaterno);
                nombrePasajero.setApellidoMaterno(apMaterno);

                Nombre contacto = new Nombre();

                // Datos Pasajero
                Pasajero pasajero = new Pasajero(rut, nombrePasajero);
                pasajero.setTelefono(telefono);
                pasajero.setNomContacto(contacto);
                pasajero.setFonoContancto(telefonoContacto);

                // Datos contacto
                contacto.setNombres(nombresContacto);
                contacto.setTratamiento(tratamientoContacto);
                contacto.setApellidoPaterno(apPaternoContacto);
                contacto.setApellidoMaterno(apMaternoContacto);

                pasajeros.add(pasajero);

            // Si es Cliente y Pasajero
            } else if (tipo.equals("CP")){ // email;<datosContacto>
                String email = datos[7];
                Tratamiento tratamientoContacto = Tratamiento.valueOf(datos[8]);
                String nombresContacto = datos[9];
                String apPaternoContacto = datos[10];
                String apMaternoContacto = datos[11];
                String telefonoContacto = datos[12];

                // Cliente
                Nombre nombreCliente = new Nombre();
                Cliente cliente = new Cliente(rut,nombreCliente, email);
                nombreCliente.setNombres(nombres);
                nombreCliente.setTratamiento(tratamiento);
                nombreCliente.setApellidoPaterno(apPaterno);
                nombreCliente.setApellidoMaterno(apMaterno);
                cliente.setTelefono(telefono);

                clientes.add(cliente);

                // Pasajero
                Nombre nombrePasajero = new Nombre();
                nombrePasajero.setTratamiento(tratamiento);
                nombrePasajero.setNombres(nombres);
                nombrePasajero.setApellidoPaterno(apPaterno);
                nombrePasajero.setApellidoMaterno(apMaterno);

                Nombre contacto = new Nombre();

                // Datos Pasajero
                Pasajero pasajero = new Pasajero(rut, nombrePasajero);
                pasajero.setTelefono(telefono);
                pasajero.setNomContacto(contacto);
                pasajero.setFonoContancto(telefonoContacto);

                // Datos contacto
                contacto.setNombres(nombresContacto);
                contacto.setTratamiento(tratamientoContacto);
                contacto.setApellidoPaterno(apPaternoContacto);
                contacto.setApellidoMaterno(apMaternoContacto);

                pasajeros.add(pasajero);
            }
        }

        // Lee seccion empresas
        while(leeArchivo.hasNext()){
            // Formato
            // rut;nombreEmpresa;urlEmpresa

            String linea = leeArchivo.next();
            if (linea.equals("+")){
                break;
            }

            String[] datos= linea.split(";");
            Rut rut = Rut.of(datos[0]);
            String nombreEmpresa = datos[1];
            String urlEmpresa = datos[2];
            Empresa empresa = new Empresa(rut, nombreEmpresa);
            empresa.setUrl(urlEmpresa);

            empresas.add(empresa);
        }

        // Lee seccion tripulantes
        while(leeArchivo.hasNext()){
            // Formato
            //tipo:rut;tratamiento;nombres;ap.Paterno;apMaterno;calle;número;nombreComuna;rutEmpresa

            String linea = leeArchivo.next();
            if (linea.equals("+")){
                break;
            }

            String[] datos= linea.split(";");
            String tipo = datos[0];
            IdPersona rut = Rut.of(datos[1]);
            Tratamiento tratamiento = Tratamiento.valueOf(datos[2]);
            String nombres = datos[3];
            String apPaterno = datos[4];
            String apMaterno = datos[5];
            String calle = datos[6];
            int numero = Integer.parseInt(datos[7]);
            String nombreComuna = datos[8];
            Rut rutEmpresa = Rut.of(datos[9]);

            if (tipo.equals("A")){
                Optional<Empresa> empresa = findEmpresa(empresas, rutEmpresa);

                if (empresa.isPresent()){
                    Nombre nombreAuxiliar = new Nombre();
                    nombreAuxiliar.setTratamiento(tratamiento);
                    nombreAuxiliar.setNombres(nombres);
                    nombreAuxiliar.setApellidoPaterno(apPaterno);
                    nombreAuxiliar.setApellidoMaterno(apMaterno);
                    Direccion direccion = new Direccion(calle, numero, nombreComuna);
                    Auxiliar auxiliar = new Auxiliar(rut, nombreAuxiliar, direccion);

                    tripulantes.add(auxiliar);
                    empresa.get().addAuxiliar(rut, nombreAuxiliar, direccion);

                }
            } else if (tipo.equals("C")){
                Optional<Empresa> empresa = findEmpresa(empresas, rutEmpresa);
                if (empresa.isPresent()){
                    Nombre nombreConductor = new Nombre();
                    nombreConductor.setTratamiento(tratamiento);
                    nombreConductor.setNombres(nombres);
                    nombreConductor.setApellidoPaterno(apPaterno);
                    nombreConductor.setApellidoMaterno(apMaterno);
                    Direccion direccion = new Direccion(calle, numero, nombreComuna);

                    Conductor conductor = new Conductor(rut, nombreConductor, direccion);

                    tripulantes.add(conductor);
                    empresa.get().addConductor(rut, nombreConductor, direccion);
                }
            }
        }

        // Lee seccion terminales
        while(leeArchivo.hasNext()){
            // Formato
            // nombre;calle;número;nombreComuna

            String linea = leeArchivo.next();
            if (linea.equals("+")){
                break;
            }
            String[] datos= linea.split(";");
            String nombre = datos[0];
            String calle = datos[1];
            int numero = Integer.parseInt(datos[2]);
            String nombreComuna = datos[3];
            Direccion direccion = new Direccion(calle, numero, nombreComuna);

            Terminal terminal = new Terminal(nombre, direccion);
            terminales.add(terminal);
        }

        // Lee seccion buses
        while(leeArchivo.hasNext()){
            // Formato
            // patente;marca;modelo;númeroAsientos;rutEmpresa

            String linea = leeArchivo.next();
            if (linea.equals("+")){
                break;
            }
            String[] datos= linea.split(";");
            String patente = datos[0];
            String marca = datos[1];
            String modelo = datos[2];
            int numeroAsientos= Integer.parseInt(datos[3]);
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

        while (leeArchivo.hasNext()){

            // Formato
            //fecha;hora;precio;duración;patente;rutAuxiliar;rutConductor;nombreTerminalSalida;nombreTerminalLlegada

            String linea = leeArchivo.next();
            if (linea.equals("+")){
                break;
            }
            String[] datos= linea.split(";");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(datos[0],formatter);
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
}
