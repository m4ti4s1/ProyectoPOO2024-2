package Vista;

import Modelo.TipoDocumento;
import Utilidades.*;
import Controlador.*;

import Excepciones.SistemaVentaPasajesExcepcion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UISVP {
    private static UISVP INSTANCE = new UISVP();
    private Scanner sc;

    private UISVP() {
        sc = new Scanner(System.in);
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085]|\t|,");
    }

    public static UISVP getInstance() {
        return INSTANCE;
    }

    private final static ControladorEmpresas CE = ControladorEmpresas.getInstance();
    private final static SistemaVentaPasajes SVP = SistemaVentaPasajes.getInstance();

    public void menu() {
        int opcion;
        do {
            System.out.println("\n\n============================");
            System.out.println("...::: Menú Principal :::...");
            System.out.println();
            System.out.println("  1) Crear empresa");
            System.out.println("  2) Contratar tripulante");
            System.out.println("  3) Crear terminal");
            System.out.println("  4) Crear cliente");
            System.out.println("  5) Crear bus");
            System.out.println("  6) Crear viaje");
            System.out.println("  7) Vender pasajes");
            System.out.println("  8) Listar ventas");
            System.out.println("  9) Listar viajes");
            System.out.println(" 10) Listar pasajeros de viaje");
            System.out.println(" 11) Listar empresas");
            System.out.println(" 12) Listar llegadas/salidas del terminal");
            System.out.println(" 13) Listar ventas de empresa");
            System.out.println(" 14) Salir");
            System.out.println("____________________________");
            System.out.print("..:: Ingrese número de opción: ");
            opcion = elegirOpc(14);
            System.out.println();


            switch (opcion) {
                case 1 -> createEmpresa();
                case 2 -> contrataTripulante();
                case 3 -> createTerminal();
                case 4 -> createCliente();
                case 5 -> createBus();
                case 6 -> createViaje();
                case 7 -> vendePasaje();
                case 8 -> listVentas();
                case 9 -> listViajes();
                case 10 -> listPasajerosViaje();
                case 11 -> listEmpresas();
                case 12 -> listLlegadasSalidasTerminal();
                case 13 -> listVentasEmpresas();
                case 14 -> System.out.println("Saliendo del programa");
            }
        } while (opcion != 14);
    }








   // Metodo para cargar datos predeterminados, para tests

    public void cargaDatosPredeterminados() {

        // Empresa 1
        Rut rutEmpresa1 = Rut.of("11.111.111-1");
        String nomEmpresa1 = "Empresa 1";
        String urlEmpresa1 = "https://empresa1.cl";
        CE.createEmpresa(rutEmpresa1, nomEmpresa1, urlEmpresa1);

        // Auxiliar 1
        Rut rutEmpresaAuxiliar1= Rut.of("11.111.111-1");
        IdPersona idAuxiliar1 = Rut.of("22.222.222-2");
        Nombre nombreAuxiliar1 = new Nombre();
        nombreAuxiliar1.setTratamiento(Tratamiento.valueOf("SR"));
        nombreAuxiliar1.setNombres("Pedro Alejandro");
        nombreAuxiliar1.setApellidoPaterno("Ramirez");
        nombreAuxiliar1.setApellidoMaterno("Torres");
        Direccion direccionAuxiliar1 = new Direccion("Avenida. UBB", 882, "Chillan");
        CE.hireAuxiliarForEmpresa(rutEmpresaAuxiliar1, idAuxiliar1, nombreAuxiliar1, direccionAuxiliar1);

        // Condutor 1
        Rut rutEmpresaConductor1= Rut.of("11.111.111-1");
        IdPersona idConductor1 = Rut.of("33.333.333-3");
        Nombre nombreConductor1 = new Nombre();
        nombreConductor1.setTratamiento(Tratamiento.valueOf("SR"));
        nombreConductor1.setNombres("Miguel Angel");
        nombreConductor1.setApellidoPaterno("Fernandez");
        nombreConductor1.setApellidoMaterno("Garcia");
        Direccion direccionConductor1 = new Direccion("Avenida. Udec", 374, "San Carlos");
        CE.hireConductorForEmpresa(rutEmpresaConductor1, idConductor1, nombreConductor1, direccionConductor1);


        // Terminal 1
        String nombreT1 = "Terminal 1";
        Direccion  direccionT1 = new Direccion("Calle terminal1", 222, "Chillan");
        CE.createTerminal(nombreT1, direccionT1);

    }









    private void createEmpresa() {
        try {
            System.out.println("...:::: Creando una nueva Empresa ::::....");
            String rut = leeString("R.U.T");
            String nom = leeString("Nombre");
            String url = leeString("url");

            CE.createEmpresa(Rut.of(rut), nom, url);

            System.out.println("...:::: Empresa guardada exitosamente ::::...");

        } catch (SistemaVentaPasajesExcepcion e) {
            System.out.println("...::::Error : " + e.getMessage());
        }
    }


    private void contrataTripulante() {
        try {
            System.out.println("...:::: Contratando un nuevo tripulante ::::....\n");

            System.out.println(":::: Dato de la Empresa");

            Rut rutEmpresa = Rut.of(leeString("R.U.T"));

            System.out.println(":::: Datos tripulante");

            int opcTripulante = leeOpc("Auxiliar[1] o Conductor[2]", 2);

            int opcTipoDocumento = leeOpc("Rut[1] o Pasaporte[2]", 2);

            IdPersona idPersona = null;
            String rut = "";
            String numero = "";
            String nacionalidad = "";

            switch (opcTipoDocumento) {
                case 1:
                    // Rut
                    rut = leeString("R.U.T");
                    idPersona = Rut.of(rut);

                    break;
                case 2:
                    // Pasaporte
                    numero = leeString("Numero");
                    nacionalidad = leeString("Nacionalidad");

                    idPersona = Pasaporte.of(numero, nacionalidad);
                    break;
            }

            Nombre nombreTripulante = new Nombre();

            int opcTratamiento = leeOpc("Sr. [1] o Sra. [2]", 2);

            switch (opcTratamiento) {
                case 1:
                    nombreTripulante.setTratamiento(Tratamiento.valueOf("SR"));
                    break;
                case 2:
                    nombreTripulante.setTratamiento(Tratamiento.valueOf("SRA"));
                    break;
            }

            String nombres = leeString("Nombres");
            nombreTripulante.setNombres(nombres);

            String apellidoPaterno = leeString("Apellido Paterno");
            nombreTripulante.setApellidoPaterno(apellidoPaterno);

            String apellidoMaterno = leeString("Apellido Materno");
            nombreTripulante.setApellidoMaterno(apellidoMaterno);

            String calle = leeString("Calle");
            int numCalle = leeInt("Numero");
            String Comuna = leeString("Comuna");

            Direccion dir = new Direccion(calle, numCalle, Comuna);

            switch (opcTripulante) {
                case 1:
                    CE.hireAuxiliarForEmpresa(rutEmpresa, idPersona, nombreTripulante, dir);
                    System.out.println("\n...:::: Auxiliar contratado exitosamente ::::....");
                    break;
                case 2:
                    CE.hireConductorForEmpresa(rutEmpresa, idPersona, nombreTripulante, dir);
                    System.out.println("\n...:::: Conductor contratado exitosamente ::::....");
                    break;
            }
        } catch (SistemaVentaPasajesExcepcion e) {
            System.out.println("\t\t...::: Error : " + e.getMessage());
        }
    }


    private void createTerminal() {
        try {
            System.out.println("...:::: Creando un nuevo Terminal ::::....");

            String nombre = leeString("Nombre");
            String calle = leeString("Calle");
            int numCalle = leeInt("Numero");
            String Comuna = leeString("Comuna");

            Direccion dir = new Direccion(calle, numCalle, Comuna);
            CE.createTerminal(nombre, dir);

            System.out.println("...:::: Terminal guardado exitosamente ::::....");
        } catch (SistemaVentaPasajesExcepcion e) {
            System.out.println("\t\t...::: Error : " + e.getMessage());
        }
    }


    private void createCliente() {

        System.out.println("...::: Crear un nuevo Modelo.Cliente :::...\n");

        int tipoDocumento = leeOpc("Rut[1] o Pasaporte[2]", 2);

        IdPersona idPersona = null;
        String rut = "";
        String numero = "";
        String nacionalidad = "";

        switch (tipoDocumento) {
            case 1:
                // Rut
                rut = leeString("R.U.T");
                idPersona = Rut.of(rut);

                break;
            case 2:
                // Pasaporte
                numero = leeString("Numero");
                nacionalidad = leeString("Nacionalidad");

                idPersona = Pasaporte.of(numero, nacionalidad);
                break;
        }

        Nombre nombreCliente = new Nombre();

        int opcTratamiento = leeOpc("Sr. [1] o Sra. [2]", 2);

        switch (opcTratamiento) {
            case 1:
                nombreCliente.setTratamiento(Tratamiento.valueOf("SR"));
                break;
            case 2:
                nombreCliente.setTratamiento(Tratamiento.valueOf("SRA"));
                break;
        }

        String nombres = leeString("Nombres");
        nombreCliente.setNombres(nombres);

        String apellidoPaterno = leeString("Apellido Paterno");
        nombreCliente.setApellidoPaterno(apellidoPaterno);

        String apellidoMaterno = leeString("Apellido Materno");
        nombreCliente.setApellidoMaterno(apellidoMaterno);

        String telefono = leeString("Telefono Movil");


        String email = leeString("Email");

        try {
            SVP.createCliente(idPersona, nombreCliente, telefono, email);
            System.out.println("\n....:::: Cliente guardado exitosamente ::::....\n");

        } catch (SistemaVentaPasajesExcepcion e ) {
            System.out.println("..:: Error : " + e.getMessage());
        }

    }






    // todo Revisar desde este metodo hacia abajo
    private void createBus() {
            System.out.println("...:::: Creando un nuevo Bus ::::....");

            String patente = leeString("Patente");

            do {
                if (!esPatenteAlfanumerica(patente)) {
                    System.out.println("La Patente debe ser alfanumerica");
                    patente = leeString("Patente");
                }
            } while (!esPatenteAlfanumerica(patente));

            String marca = leeString("Marca");
            String modelo = leeString("Modelo");
            int nroAsientos = leeInt("Numero de asientos");

            System.out.println(":::: Dato de la empresa");
            Rut rutEmpresa = Rut.of(leeString("R.U.T"));

        try {
            CE.createBus(patente, marca, modelo, nroAsientos, rutEmpresa);
            System.out.println("...:::: Bus guardado exitosamente ::::....");
        } catch (SistemaVentaPasajesExcepcion e) {
            System.err.println("..:: Error : " + e.getMessage());
        }
    }

    private void createViaje() {

        System.out.println("...::: Creacion de un nuevo Modelo.Viaje :::....");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String fechaStr = leeString("Fecha [dd/mm/yyyy]");
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);

        String horaStr = leeString("Hora [hh:mm]");
        LocalTime hora = LocalTime.parse(horaStr, timeFormatter);

        int precio = leeInt("Precio");

        int duracion = leeInt("Duracion (minutos)");

        String patente = leeString("Patente Modelo.Bus");

        int cantConductores = elegirOpc(2);

        System.out.printf("%30s", ":: Id Auxiliar ::");

        int opcIdAuxiliar = leeOpc("Rut[1] o Pasaporte[2]", 2);


        ArrayList<IdPersona> idTripulantes = new ArrayList<>();

        IdPersona idPersona = null;
        String rut, numero, nacionalidad;

        switch (opcIdAuxiliar) {
            case 1:
                rut = leeString("R.U.T");
                idPersona = Rut.of(rut);
                // Rut
                break;
            case 2:
                // Pasaporte
                numero = leeString("Numero");
                nacionalidad = leeString("Nacionalidad");
                idPersona = Pasaporte.of(numero, nacionalidad);
                break;
        }
        idTripulantes.add(idPersona);

        for (int i = 0; i < cantConductores; i++) {
            int opcIdConductor = leeOpc("Rut[1] o Pasaporte[2]", 2);

            switch (opcIdConductor) {
                case 1:
                    // Rut
                    rut = leeString("R.U.T");
                    idPersona = Rut.of(rut);
                    break;
                case 2:
                    // Pasaporte
                    numero = leeString("Numero");
                    nacionalidad = leeString("Nacionalidad");
                    idPersona = Pasaporte.of(numero, nacionalidad);
                    break;
            }
            idTripulantes.add(idPersona);
        }

        IdPersona[] idTripulantesArray = idTripulantes.toArray(new IdPersona[0]);

        String[] comunas = new String[2];
        comunas[0] = leeString("Nombre comuna salida");
        comunas[1] = leeString("Nombre comuna llegada");

        try {
            SVP.createViaje(fecha, hora, precio,duracion, patente, idTripulantesArray ,comunas);
            System.out.println("\n...:::: Viaje guardado exitosamente ::::....");

        } catch (SistemaVentaPasajesExcepcion e) {
            System.out.println("..:: Error : " + e.getMessage());
        }

    }

    private void vendePasaje() {
        System.out.println(".....::: Modelo.Venta de Pasajes:::....\n\n");
        System.out.println(":::Datos de venta");

        String idDocumento = leeString("ID Documento");

        System.out.print("Tipo Documento : [1] Boleta [2] Factura : ");
        int tipo = elegirOpc(2);

        TipoDocumento tipoDocumento = (tipo == 1) ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;

        String fecha = leeString("Fecha de Venta [dd/mm/yyyy]");
        LocalDate fechaVenta = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println("\n\n:::: Datos del Cliente");
        int op = leeOpc("Utilidades.Rut[1] o Utilidades.Pasaporte[2]", 2);

        IdPersona idCliente = (op == 1) ? Rut.of(leeString("R.U.T"))
                : Pasaporte.of(leeString("Número Pasaporte"), leeString("Nacionalidad"));

        if (SVP.getNombreCliente(idCliente) == null) {
            System.out.println(":::: Cliente no encontrado");
            return;
        }

        System.out.printf("Nombre Cliente: %s%n", SVP.getNombreCliente(idCliente));

        if (!SVP.iniciaVenta(idDocumento, tipoDocumento, fechaVenta, idCliente)) {
            System.out.println("....:::: Surgió un problema, la venta no se pudo inicializar");
            return;
        }

        System.out.println("\n\n:::: Datos del Viaje");
        int cant = leeInt("Cantidad de pasajes");

        String fechaViaje = leeString("Fecha de viaje [dd/mm/yyyy]");
        LocalDate fechaV = LocalDate.parse(fechaViaje, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println("\n\n:::: Listado de Horarios Disponibles");
        String[][] matrizViajes = SVP.getHorariosDisponibles(fechaV);

        for (int i = 0; i < matrizViajes.length; i++) {
            System.out.printf("%d. BUS: %s | Salida: %s | Valor: %s | Asientos: %s%n",
                    i + 1, matrizViajes[i][0], matrizViajes[i][1], matrizViajes[i][2], matrizViajes[i][3]);
        }

        System.out.print("\nSeleccione viaje [1.." + matrizViajes.length + "]: ");
        int viaje = sc.nextInt() - 1;

        LocalTime horaV = LocalTime.parse(matrizViajes[viaje][1], DateTimeFormatter.ofPattern("HH:mm"));
        String patBus = matrizViajes[viaje][0];

        // Solicitar y validar tripulantes y terminales
        IdPersona[] idTripulantes = new IdPersona[3];
        for (int i = 0; i < idTripulantes.length; i++) {
            idTripulantes[i] = Rut.of(leeString("Ingrese RUT del tripulante " + (i + 1)));
        }

        String[] comunas = new String[2];
        comunas[0] = leeString("Comuna de salida");
        comunas[1] = leeString("Comuna de llegada");

        try {
            SVP.createViaje(fechaV, horaV, Integer.parseInt(matrizViajes[viaje][2]), 120,
                    patBus, idTripulantes, comunas);
        } catch (SistemaVentaPasajesExcepcion e) {
            System.out.println("Error al crear el viaje: " + e.getMessage());
            return;
        }

        String[] matrizAsientos = SVP.listAsientosDeViaje(fechaV, horaV, patBus);
        for (String asiento : matrizAsientos) {
            System.out.print(asiento + " ");
        }
        System.out.println();

        boolean asientosDisponibles;
        int[] numAsientos;
        do {
            String asientos = leeString("Seleccione sus asientos [separe por comas]");
            numAsientos = separador(asientos, cant);
            asientosDisponibles = true;

            for (int asientoIndex : numAsientos) {
                if (matrizAsientos[asientoIndex - 1].equals("*")) {
                    System.out.println("Asiento " + asientoIndex + " está ocupado");
                    asientosDisponibles = false;
                }
            }
        } while (!asientosDisponibles);

        for (int i = 0; i < cant; i++) {
            System.out.println("\n:::: Datos del Pasajero " + (i + 1));
            int opcId = leeOpc("Utilidades.Rut[1] o Utilidades.Pasaporte[2]", 2);

            IdPersona idPasajero = (opcId == 1) ? Rut.of(leeString("R.U.T"))
                    : Pasaporte.of(leeString("Número Pasaporte"), leeString("Nacionalidad"));

            if (SVP.getNombrePasajero(idPasajero).isEmpty()) {
                System.out.println(":::: Pasajero no encontrado. Cree al pasajero.");
                Nombre nuevoPasajero = new Nombre();
                nuevoPasajero.setNombres(leeString("Nombres"));
                nuevoPasajero.setApellidoPaterno(leeString("Apellido Paterno"));
                nuevoPasajero.setApellidoMaterno(leeString("Apellido Materno"));

                if (!SVP.createPasajero(idPasajero, nuevoPasajero, leeString("Fono"), nuevoPasajero, leeString("Fono Contacto"))) {
                    System.out.println(":::: No se pudo agregar el pasajero.");
                    return;
                }
            }

            SVP.vendePasaje(idDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], idPasajero);
        }

        int montoVenta = SVP.getMontoVenta(idDocumento, tipoDocumento);
        System.out.println(":::: Monto Total de la venta: " + montoVenta);

        String[] boleta = SVP.pasajesAImprimir(idDocumento, tipoDocumento);
        for (String linea : boleta) {
            System.out.println(linea);
        }

        System.out.printf("\n...:::: Venta generada exitosamente ::::....%n");
    }

    private void listViajes() {
        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.printf("\n%44s\n", "...:::: Listado de Viajes ::::....\n");
        String[][] lista= SVP.listViajes();

        System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");
        System.out.printf(" | FECHA        |    HORA SALE |    HORA LLEGA | PRECIO | ASIENTOS DISP. | PATENTE       | ORIGEN         | DESTINO         |%n");
        System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");

        for (int i = 0; i < lista.length; i++) {
            String fechaOriginal = lista[i][0];
            LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
            String fechaFormateada = fecha.format(nuevoFormato);
            System.out.printf(" | %-10s   |       %-6s |       %-6s | %-8s |           %-2s | %-8s | %-8s         | %-8s         |%n",
                    fechaFormateada, lista[i][1], lista[i][2], lista[i][3], lista[i][4], lista[i][5], lista[i][6], lista[i][7]);
            System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");
        }
    }

    private void listPasajerosViaje() {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String fechaDelViaje = leeString("Fecha del viaje[dd/mm/yyyy]");
            LocalDate fecha = LocalDate.parse(fechaDelViaje, dateFormatter);

            String horaDelViaje = leeString("Hora del viaje[hh:mm]");
            LocalTime hora = LocalTime.parse(horaDelViaje, timeFormatter);

            String patenteBus = leeString("Patente bus");

            System.out.printf("\n%44s\n", "...:::: Listado de pasajeros de un viaje ::::....\n");
            System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");
            System.out.printf(" | ASIENTO |        RUT/PASS | PASAJERO                          | CONTACTO                          | TELEFONO CONTACTO |%n");
            System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");

            String[][] listaPasajeros = SVP.listPasajerosViaje(fecha, hora, patenteBus);
            for (int i = 0; i < listaPasajeros.length; i++) {
                System.out.printf(" |     %-3s |   %-13s | %-33s | %-33s | %-17s |%n",
                        listaPasajeros[i][0], listaPasajeros[i][1], listaPasajeros[i][2], listaPasajeros[i][3], listaPasajeros[i][4]);
                System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");
            }
        } catch (SistemaVentaPasajesExcepcion e) {
            System.err.println(e.getMessage());
        }
    }

    private void listEmpresas() {
        System.out.printf("\n%44s\n", "...:::: Listado de empresas ::::....\n");
        String[][] lista = CE.listEmpresas();

        System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*--------------------------*%n");
        System.out.printf(" | RUT EMPRESA  | NOMBRE                        | URL                           | NRO. TRIPULANTES | NRO. BUSES | NRO. VENTAS |%n");
        System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*------------*-------------*%n");

        for (int i = 0; i < lista.length; i++) {
            System.out.printf(" | %10s | %26s    | %26s    | %16s | %10s | %11s |%n",
                    lista[i][0], lista[i][1], lista[i][2], lista[i][3], lista[i][4], lista[i][5]);
            System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*------------*-------------*%n");
        }
    }

    private void listLlegadasSalidasTerminal() {
        try {
            System.out.println("...:::: Listado de llegadas y salidas de un terminal ::::....\n");

            String nombreTerminal = leeString("Nombre terminal");


            String fecha = leeString("Fecha[dd/mm/yyyy]");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaFinal = LocalDate.parse(fecha, formatter);
            String[][] lista = CE.listLlegadaSalidasTerminal(nombreTerminal, fechaFinal);

            System.out.printf(" *----------------*-------*-------------*---------------------------------*----------------*%n");
            System.out.printf(" | LLEGADA/SALIDA | HORA  | PATENTE BUS | NOMBRE EMPRESA                  | NRO. PASAJEROS |%n");
            System.out.printf(" *----------------*-------*-------------*---------------------------------*----------------*%n");

            for (int i = 0; i < lista.length; i++) {
                System.out.printf(" | %-10s     | %-6s    | %-8s  | %-14s | %-2s |%n",
                        lista[i][0], lista[i][1], lista[i][2], lista[i][3], lista[i][4]);
                System.out.printf(" *----------------*-------*-------------*---------------------------------*----------------*%n");
            }
        } catch (SistemaVentaPasajesExcepcion e) {
            System.err.println(e.getMessage());
        }
    }

    private void listVentasEmpresas() {
        try {
            System.out.println("...:::: Listado de ventas de una empresa ::::....\n");

            String rut = leeString("R.U.T");
            String[][] lista = CE.listVentasEmpresa(Rut.of(rut));
            System.out.printf(" *-----------*---------*---------------*--------------*%n");
            System.out.printf(" | FECHA     | TIPO    | MONTO PAGADO  |    TIPO PAGO |%n");
            System.out.printf(" *-----------*---------*---------------*--------------*%n");

            for (int i = 0; i < lista.length; i++) {
                System.out.printf(" | %-10s     | %-6s    | %-6s  | %-14s |%n",
                        lista[i][0], lista[i][1], lista[i][2], lista[i][3]);
                System.out.printf(" *-----------*---------*---------------*--------------*%n");
            }
        } catch (SistemaVentaPasajesExcepcion e) {
            System.err.println(e.getMessage());
        }
    }

    private void pagaVentaPasajes() {}

    private int elegirOpc(int cantOpciones) {
        int opc = 0;

        boolean valido = false;
        while (!valido) {
            try {
                opc = sc.nextInt();

                if (opc > 0 && opc <= cantOpciones) {
                    valido = true;
                } else {
                    System.out.print("..:: Opcion fuera de rango, intente denuevo : ");
                }
            } catch (InputMismatchException e) {
                System.out.print("..:: La opcion debe ser un numero entero, intente denuevo : ");
                sc.next();
            }
        }
        return opc;
    }

    // metodos para leer string e ints de opciones para los reportes
    private String leeString(String msg) {
        System.out.printf("%30s : ", msg);
        return sc.next();
    }

    private int leeInt(String msg) {
        System.out.printf("%30s : ", msg);
        int num = 0;
        do {
            num = sc.nextInt();
        } while (num < 0);
        return num;
    }

    //separe numero de comas "Arreglado"
    private int [] separador(String asientos,int cant){

        String[] numerosString = asientos.split(",");
        int[] numAsientos = new int[cant];

        for (int i = 0; i < cant; i++){
            numAsientos[i] = Integer.parseInt(numerosString[i]);
        }

        return numAsientos;
    }
    private int leeOpc(String msg, int cantOpc) {
        int opc = 0;
        boolean valido = false;
        while (!valido) {

            try {
                System.out.printf("%30s : ", msg);
                opc = sc.nextInt();

                if (opc < 0 || opc > cantOpc) {
                    System.out.println("..:: Opcion fuera de rango, intente denuevo... ");
                } else {
                    valido = true;
                }

            } catch (InputMismatchException e) {
                System.out.println("..:: La opcion debe ser un numero entero, intente denuevo... ");
                sc.next();
            }
        }
        return opc;
    }

    private static boolean esPatenteAlfanumerica(String input) {
        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
