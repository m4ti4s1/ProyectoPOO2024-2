package Vista;

//import Modelo.*;

import Utilidades.*;
import Controlador.*;

import Excepciones.SVPException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class UISVP {
    private static UISVP INSTANCE = new UISVP();
    private Scanner sc;

    private UISVP() {
        sc = new Scanner(System.in);
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085]|\t");
    }

    public static UISVP getInstance() {
        return INSTANCE;
    }

    public void menu() {
        int opcion;
        do {
            System.out.println();
            System.out.print("""
                    ============================
                    ...::: Menú Principal :::...

                      1) Crear empresa
                      2) Contratar tripulante
                      3) Crear terminal
                      4) Crear cliente
                      5) Crear bus
                      6) Crear viaje
                      7) Vender pasajes
                      8) Listar ventas
                      9) Listar viajes
                     10) Listar pasajeros de viaje
                     11) Listar empresas
                     12) Listar llegadas/salidas del terminal
                     13) Listar ventas de empresa
                     14) Generar pasajes venta
                     15) Leer datos iniciales
                     16) Guardar datos del sistema
                     17) Leer datos del sistema
                     18) Salir
                    ____________________________
                    ..:: Ingrese número de opción:\s""");

            opcion = elegirOpc(18);
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
                case 14 -> generatePasajesVenta();
                case 15 -> readDatosIniciales();
                case 16 -> saveDatosSistema();
                case 17 -> readDatosSistema();
                case 18 -> System.out.println("Saliendo del programa");
            }
        } while (opcion != 18);
    }


    private void createEmpresa() {
        GUICreaEmpresa.display();
    }


    private void contrataTripulante() {
        GUIContrataTripulante.display();
//        try {
//            System.out.println("...:::: Contratando un nuevo tripulante ::::....\n");
//
//            System.out.println(":::: Dato de la Empresa");
//
//            Rut rutEmpresa = Rut.of(leeString("R.U.T"));
//
//            System.out.println(":::: Datos tripulante");
//
//            int opcTripulante = leeOpc("Auxiliar[1] o Conductor[2]", 2);
//
//            int opcTipoDocumento = leeOpc("Rut[1] o Pasaporte[2]", 2);
//
//            IdPersona idPersona = null;
//            String rut = "";
//            String numero = "";
//            String nacionalidad = "";
//
//            switch (opcTipoDocumento) {
//                case 1:
//                    // Rut
//                    rut = leeString("R.U.T");
//                    idPersona = Rut.of(rut);
//
//                    break;
//                case 2:
//                    // Pasaporte
//                    numero = leeString("Numero");
//                    nacionalidad = leeString("Nacionalidad");
//
//                    idPersona = Pasaporte.of(numero, nacionalidad);
//                    break;
//            }
//
//            Nombre nombreTripulante = new Nombre();
//
//            int opcTratamiento = leeOpc("Sr. [1] o Sra. [2]", 2);
//
//            switch (opcTratamiento) {
//                case 1:
//                    nombreTripulante.setTratamiento(Tratamiento.valueOf("SR"));
//                    break;
//                case 2:
//                    nombreTripulante.setTratamiento(Tratamiento.valueOf("SRA"));
//                    break;
//            }
//
//            String nombres = leeString("Nombres");
//            nombreTripulante.setNombres(nombres);
//
//            String apellidoPaterno = leeString("Apellido Paterno");
//            nombreTripulante.setApellidoPaterno(apellidoPaterno);
//
//            String apellidoMaterno = leeString("Apellido Materno");
//            nombreTripulante.setApellidoMaterno(apellidoMaterno);
//
//            String calle = leeString("Calle");
//            int numCalle = leeInt("Numero");
//            String Comuna = leeString("Comuna");
//
//            Direccion dir = new Direccion(calle, numCalle, Comuna);
//
//            switch (opcTripulante) {
//                case 1:
//                    ControladorEmpresas.getInstance().hireAuxiliarForEmpresa(rutEmpresa, idPersona, nombreTripulante, dir);
//                    System.out.println("\n...:::: Auxiliar contratado exitosamente ::::....");
//                    break;
//                case 2:
//                    ControladorEmpresas.getInstance().hireConductorForEmpresa(rutEmpresa, idPersona, nombreTripulante, dir);
//                    System.out.println("\n...:::: Conductor contratado exitosamente ::::....");
//                    break;
//            }
//        } catch (SVPException e) {
//            System.out.println("\t\t...::: Error : " + e.getMessage());
//        }
    }


    private void createTerminal() {
        try {
            System.out.println("...:::: Creando un nuevo Terminal ::::....");

            String nombre = leeString("Nombre");
            String calle = leeString("Calle");
            int numCalle = leeInt("Numero");
            String Comuna = leeString("Comuna");

            Direccion dir = new Direccion(calle, numCalle, Comuna);
            ControladorEmpresas.getInstance().createTerminal(nombre, dir);

            System.out.println("...:::: Terminal guardado exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("\t\t...::: Error : " + e.getMessage());
        }
    }


    private void createCliente() {

        System.out.println("...::: Crear un nuevo Cliente :::...\n");

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
            SistemaVentaPasajes.getInstance().createCliente(idPersona, nombreCliente, telefono, email);
            System.out.println("\n....:::: Cliente guardado exitosamente ::::....\n");

        } catch (SVPException e) {
            System.out.println("..:: Error : " + e.getMessage());
        }

    }


    private void createBus() {
        //Interface Grafica
        GUICreaBus.display();

//        System.out.println("...:::: Creando un nuevo Bus ::::....");
//
//        String patente = leeString("Patente");
//
//        do {
//            if (!esPatenteAlfanumerica(patente)) {
//                System.out.println("La Patente debe ser alfanumerica");
//                patente = leeString("Patente");
//            }
//        } while (!esPatenteAlfanumerica(patente));
//
//        String marca = leeString("Marca");
//        String modelo = leeString("Modelo");
//        int nroAsientos = leeInt("Numero de asientos");
//
//        System.out.println(":::: Dato de la empresa");
//        Rut rutEmpresa = Rut.of(leeString("R.U.T"));
//
//        try {
//            ControladorEmpresas.getInstance().createBus(patente, marca, modelo, nroAsientos, rutEmpresa);
//            System.out.println("...:::: Bus guardado exitosamente ::::....");
//        } catch (SVPException e) {
//            System.err.println("..:: Error : " + e.getMessage());
//        }
    }


    private void createViaje() {

        System.out.println("...::: Creacion de un nuevo Viaje :::....");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String fechaStr = leeString("Fecha [dd/mm/yyyy]");
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);

        String horaStr = leeString("Hora [hh:mm]");
        LocalTime hora = LocalTime.parse(horaStr, timeFormatter);

        int precio = leeInt("Precio");

        int duracion = leeInt("Duracion (minutos)");

        String patente = leeString("Patente Bus");

        System.out.printf("%30s : ", "Nro. de Conductores [1 o 2]");
        int cantConductores = elegirOpc(2);

        System.out.printf("%30s%n", ":: Id Auxiliar ::");

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

        System.out.printf("%30s%n", ":: Id Conductor ::");
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
            SistemaVentaPasajes.getInstance().createViaje(fecha, hora, precio, duracion, patente, idTripulantesArray, comunas);
            System.out.println("\n...:::: Viaje guardado exitosamente ::::....");

        } catch (SVPException e) {
            System.out.println("..:: Error : " + e.getMessage());
        }

    }

    private void vendePasaje() {
        System.out.println("...:::: Venta de Pasajes ::::....\n\n");
        System.out.println(":::: Datos de la venta");

        String IdDocumento = leeString("ID Documento");

        System.out.print("Tipo Documento : [1] Boleta [2] Factura : ");
        int tipo = elegirOpc(2);

        LocalDate fechaVenta = LocalDate.now();

        String fechaViaje = leeString("Fecha de viaje[dd/mm/yyyy]");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaV = LocalDate.parse(fechaViaje, formatter);
        // Salida
        String origen = leeString("Origen (comuna)");

        // Llegada
        String destino = leeString("Destino (comuna)");

        TipoDocumento tipoDocumento = null;
        switch (tipo) {
            case 1:
                tipoDocumento = TipoDocumento.valueOf("BOLETA");
                break;
            case 2:
                tipoDocumento = TipoDocumento.valueOf("FACTURA");
                break;
        }

        System.out.println("\n\n:::: Datos del Cliente\n");
        int op = leeOpc("Rut[1] o Pasaporte[2]", 2);

        IdPersona idCliente = null;
        switch (op) {
            case 1:
                String idRUT = leeString("R.U.T");

                if (null == SistemaVentaPasajes.getInstance().getNombreCliente(Rut.of(idRUT))) {
                    System.out.println(":::: Cliente no Encontrado");
                    return;
                } else {
                    System.out.printf("%30s : %s", "Nombre Cliente", SistemaVentaPasajes.getInstance().getNombreCliente(Rut.of(idRUT)));
                    idCliente = Rut.of(idRUT);
                    break;
                }

            case 2:
                String num = leeString("Numero Pasaporte");
                String nacionalidad = leeString("Nacionalidad");

                if (null == SistemaVentaPasajes.getInstance().getNombreCliente(Pasaporte.of(num, nacionalidad))) {
                    System.out.println("\nNo se ah Encontrado al Usuario");
                    return;
                } else {

                    System.out.println("\nNombre Cliente : " + SistemaVentaPasajes.getInstance().getNombreCliente(Pasaporte.of(num, nacionalidad)));

                    idCliente = Pasaporte.of(num, nacionalidad);
                    break;
                }


        }

        System.out.println("\n\n:::: Pasajes a Vender");
        int cant = leeInt("Cantidad de pasajes");

        try {
            SistemaVentaPasajes.getInstance().iniciaVenta(IdDocumento, tipoDocumento, fechaV, origen, destino, idCliente, cant);


            String[][] matrizViajes = SistemaVentaPasajes.getInstance().getHorariosDisponibles(fechaV, origen, destino, cant);

            System.out.println("\n\n:::: Listado de Horarios Disponibles");


            System.out.printf("       +------------+----------------+------------+------------+%n");
            System.out.printf("       |   BUS      |     Salida     |   Valor    |  Asientos  |%n");
            System.out.printf("       +------------+----------------+------------+------------+%n");
            for (int i = 0; i < matrizViajes.length; i++) {
                System.out.printf(" %-5d | %-10s | %-14s | %-10s | %-10s |%n",
                        i + 1, matrizViajes[i][0], matrizViajes[i][1], "$" + matrizViajes[i][2], matrizViajes[i][3]);
                System.out.printf("       +------------+----------------+------------+------------+%n");
            } //POSICION DE VIAJE ELEGIDO

            System.out.print("\n Selecione viaje en [1.." + matrizViajes.length + "] : ");
            int Viaje = sc.nextInt() - 1;


            LocalTime horaV = LocalTime.parse(matrizViajes[Viaje][1], DateTimeFormatter.ofPattern("HH:mm"));
            String patBus = matrizViajes[Viaje][0];

            String[] matrizAsientos = SistemaVentaPasajes.getInstance().listAsientosDeViaje(fechaV, LocalTime.parse(matrizViajes[Viaje][1]), matrizViajes[Viaje][0]);

            System.out.printf("       *---*---*---*---*---*%n");
            for (int i = 0; i < matrizAsientos.length; i++) {

                System.out.printf("       |%-2s |", matrizAsientos[i]);
                i++;
                System.out.printf(" %-2s|   |", matrizAsientos[i]);
                i += 2;
                System.out.printf(" %-2s|", matrizAsientos[i]);
                i--;
                System.out.printf(" %-2s|%n", matrizAsientos[i]);
                System.out.printf("       |---+---+---+---+---|%n");
                i++;
            }
            String asientos;
            int[] numAsientos;
            boolean asientosDisponibles;

            do {
                asientos = leeString("Seleccione sus asientos [separe por ,]");
                numAsientos = separador(asientos, cant);

                System.out.println();
                // Todo Verificar que los asientos esten disponibles
                asientosDisponibles = true;
                for (int i = 0; i < numAsientos.length; i++) {
                    int asientoIndex = numAsientos[i] - 1;
                    if (asientoIndex >= 0 && asientoIndex < matrizAsientos.length) {
                        if (matrizAsientos[asientoIndex].equals("*")) {
                            System.out.println("Asiento " + (asientoIndex + 1) + " Está ocupado");
                            asientosDisponibles = false;
                        }
                    }
                }
            } while (!asientosDisponibles);

            // ciclo para cada pasaje
            for (int i = 0; i < cant; i++) {

                System.out.println("\n:::: Datos pasajeros " + (i + 1));

                int opcId = leeOpc("Rut[1] o Pasaporte[2]", 2);

                switch (opcId) {
                    case 1:
                        // caso rut

                        String idRut = leeString("R.U.T");

                        if (SistemaVentaPasajes.getInstance().getNombrePasajero(Rut.of(idRut)).isEmpty()) {

                            System.out.println("\n\n:::: Pasajero no Encontrado\n");

                            Nombre newPasajero = new Nombre();
                            System.out.println("..: Cree al pasajero :..");
                            System.out.println("\n...::: Datos Pasajero :::...");

                            int opcTratamiento = leeOpc("Sr.[1] o Sra.[2]", 2);

                            switch (opcTratamiento) {
                                case 1 -> newPasajero.setTratamiento(Tratamiento.valueOf("SRA"));
                                case 2 -> newPasajero.setTratamiento(Tratamiento.valueOf("SR"));
                            }

                            String nombres = leeString("Nombres");
                            String apellidoPaterno = leeString("Apellido Paterno");
                            String apellidoMaterno = leeString("Apellido Materno");
                            String fono = leeString("Ingresa fono");

                            newPasajero.setNombres(nombres);
                            newPasajero.setApellidoPaterno(apellidoPaterno);
                            newPasajero.setApellidoMaterno(apellidoMaterno);

                            Nombre contacto = new Nombre();

                            // No necesario porque ya lo ingreso antes
                            System.out.println("\n...::: Datos Contacto Pasajero :::...");

                            opcTratamiento = leeOpc("Ingrese Sr.[1] o Sra.[2]", 2);
                            switch ((opcTratamiento)) {
                                case 1 -> contacto.setTratamiento(Tratamiento.valueOf("SR"));
                                case 2 -> contacto.setTratamiento(Tratamiento.valueOf("SRA"));
                            }

                            nombres = leeString("Nombres contacto");
                            apellidoPaterno = leeString("Apellido Paterno");
                            apellidoMaterno = leeString("Apellido Materno");
                            String fonoContacto = leeString("Fono contacto");

                            contacto.setNombres(nombres);
                            contacto.setApellidoPaterno(apellidoPaterno);
                            contacto.setApellidoMaterno(apellidoMaterno);


                            SistemaVentaPasajes.getInstance().createPasajero(Rut.of(idRut), newPasajero, fono, contacto, fonoContacto);
                            System.out.println("\n:::: Pasaje agregado exitosamente");

                        }

                        SistemaVentaPasajes.getInstance().vendePasaje(IdDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], Rut.of(idRut));
                        break;


                    case 2:
                        // caso pasaporte
                        String numeroPasaporte = leeString("Numero Pasaporte");
                        String nacionalidad = leeString("Nacionalidad");

                        if (SistemaVentaPasajes.getInstance().getNombrePasajero(Pasaporte.of(numeroPasaporte, nacionalidad)).isEmpty()) {

                            System.out.println("\n\n:::: Pasajero no Encontrado\n");

                            Nombre newPasajero = new Nombre();
                            System.out.println("..: Cree al pasajero :..");
                            System.out.println("\n...::: Datos Pasajero :::...");

                            int opcTratamiento = leeOpc("Sr.[1] o Sra.[2]", 2);

                            switch (opcTratamiento) {
                                case 1 -> newPasajero.setTratamiento(Tratamiento.valueOf("SRA"));
                                case 2 -> newPasajero.setTratamiento(Tratamiento.valueOf("SR"));
                            }

                            String nombres = leeString("Nombres");
                            String apellidoPaterno = leeString("Apellido Paterno");
                            String apellidoMaterno = leeString("Apellido Materno");
                            String fono = leeString("Ingresa fono");

                            newPasajero.setNombres(nombres);
                            newPasajero.setApellidoPaterno(apellidoPaterno);
                            newPasajero.setApellidoMaterno(apellidoMaterno);

                            Nombre contacto = new Nombre();

                            // No necesario porque ya lo ingreso antes
                            System.out.println("\n...::: Datos Contacto Pasajero :::...");

                            opcTratamiento = leeOpc("Ingrese Sr.[1] o Sra.[2]", 2);
                            switch ((opcTratamiento)) {
                                case 1 -> contacto.setTratamiento(Tratamiento.valueOf("SR"));
                                case 2 -> contacto.setTratamiento(Tratamiento.valueOf("SRA"));
                            }

                            nombres = leeString("Nombres contacto");
                            apellidoPaterno = leeString("Apellido Paterno");
                            apellidoMaterno = leeString("Apellido Materno");
                            String fonoContacto = leeString("Fono contacto");

                            contacto.setNombres(nombres);
                            contacto.setApellidoPaterno(apellidoPaterno);
                            contacto.setApellidoMaterno(apellidoMaterno);


                            SistemaVentaPasajes.getInstance().createPasajero(Pasaporte.of(numeroPasaporte, nacionalidad), newPasajero, fono, contacto, fonoContacto);
                            System.out.println("\n:::: Pasaje agregado exitosamente");

                        }

                        SistemaVentaPasajes.getInstance().vendePasaje(IdDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], Pasaporte.of(numeroPasaporte, nacionalidad));
                        break;
                } //cierre del switch
            }// cierre del for

            Optional<Integer> montoVenta = SistemaVentaPasajes.getInstance().getMontoVenta(IdDocumento, tipoDocumento);
            System.out.println();
            System.out.println("\n:::: Monto Total de la venta: " + montoVenta.get());
            System.out.println();

            pagaVentaPasajes(IdDocumento, tipoDocumento);
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listVentas() {

        String[][] listaVentas = SistemaVentaPasajes.getInstance().listVentas();


        if (listaVentas.length != 0) {
            System.out.printf("\n%44s\n", "...:::: Listado de Ventas ::::....\n");
            System.out.printf(" +------------+----------+------------+-----------------+----------------------------------+--------------+--------------+%n");
            System.out.printf(" | ID DOCUMENT| TIPO DOCU|      FECHA |   RUT/PASAPORTE | CLIENTE                          | CANT BOLETOS |  TOTAL VENTA |%n");
            System.out.printf(" +------------+----------+------------+-----------------+----------------------------------+--------------+--------------+%n");

            for (int i = 0; i < listaVentas.length; i++) {

                System.out.printf(" |      %-5s | %-7s  | %-10s |    %-5s | %-32s |            %-2s|       %-6s |%n",
                        listaVentas[i][0], listaVentas[i][1], listaVentas[i][2], listaVentas[i][3], listaVentas[i][4], listaVentas[i][5], "$" + listaVentas[i][6]);
                System.out.printf(" +------------+----------+------------+-----------------+----------------------------------+--------------+--------------+%n");
            }
        } else {
            System.out.println("...::: No existen ventas registradas");
        }


    }

    private void listViajes() {

        String[][] lista = SistemaVentaPasajes.getInstance().listViajes();

        if (lista.length != 0) {
            System.out.printf("\n%44s\n", "...:::: Listado de Viajes ::::....\n");
            System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");
            System.out.printf(" | FECHA        |    HORA SALE |    HORA LLEGA | PRECIO | ASIENTOS DISP. | PATENTE       | ORIGEN         | DESTINO         |%n");
            System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");

            for (String[] strings : lista) {
                String fechaOriginal = strings[0];
                //LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
                //String fechaFormateada = fecha.format(nuevoFormato);
                System.out.printf(" | %-10s   |       %-6s |        %-6s | %-6s |             %-2s | %-8s      | %-12s   | %-12s    |%n",
                        fechaOriginal, strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
                System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");
            }
        } else {
            System.out.println("...::: No existen viajes registrados");
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
            String[][] listaPasajeros = SistemaVentaPasajes.getInstance().listPasajerosViaje(fecha, hora, patenteBus);

            if (listaPasajeros.length != 0) {

                System.out.printf("\n%44s\n", "...:::: Listado de pasajeros de un viaje ::::....\n");
                System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");
                System.out.printf(" | ASIENTO |        RUT/PASS | PASAJERO                          | CONTACTO                          | TELEFONO CONTACTO |%n");
                System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");

                for (int i = 0; i < listaPasajeros.length; i++) {
                    System.out.printf(" |     %-3s |   %-13s | %-33s | %-33s | %-17s |%n",
                            listaPasajeros[i][0], listaPasajeros[i][1], listaPasajeros[i][2], listaPasajeros[i][3], listaPasajeros[i][4]);
                    System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");
                }
            } else {
                System.out.println("\n...::: No existen pasajeros en el viaje indicado");
            }
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listEmpresas() {
        String[][] lista = ControladorEmpresas.getInstance().listEmpresas();
        if (lista.length != 0) {
            System.out.printf("\n%44s\n", "...:::: Listado de empresas ::::....\n");

            System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*--------------------------*%n");
            System.out.printf(" | RUT EMPRESA  | NOMBRE                        | URL                           | NRO. TRIPULANTES | NRO. BUSES | NRO. VENTAS |%n");
            System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*------------*-------------*%n");

            for (int i = 0; i < lista.length; i++) {
                System.out.printf(" | %-10s | %-26s    | %-26s    | %-16s | %-10s | %-11s |%n",
                        lista[i][0], lista[i][1], lista[i][2], lista[i][3], lista[i][4], lista[i][5]);
                System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*------------*-------------*%n");
            }
        } else {
            System.out.println("...::: No existen empresas registradas");
        }
    }

    private void listLlegadasSalidasTerminal() {
        try {
            System.out.println("...:::: Listado de llegadas y salidas de un terminal ::::....\n");

            String nombreTerminal = leeString("Nombre terminal");


            String fecha = leeString("Fecha[dd/mm/yyyy]");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaFinal = LocalDate.parse(fecha, formatter);
            String[][] lista = ControladorEmpresas.getInstance().listLlegadaSalidasTerminal(nombreTerminal, fechaFinal);

            if (lista.length != 0) {
                System.out.printf(" *----------------*-------*-------------*---------------------------------*----------------*%n");
                System.out.printf(" | LLEGADA/SALIDA | HORA  | PATENTE BUS | NOMBRE EMPRESA                  | NRO. PASAJEROS |%n");
                System.out.printf(" *----------------*-------*-------------*---------------------------------*----------------*%n");

                for (int i = 0; i < lista.length; i++) {
                    System.out.printf(" | %-10s     | %-4s | %-8s    | %-20s            | %-2s             |%n",
                            lista[i][0], lista[i][1], lista[i][2], lista[i][3], lista[i][4]);
                    System.out.printf(" *----------------*-------*-------------*---------------------------------*----------------*%n");
                }

            } else {
                System.out.println("...::: No existen llegadas/salidas del terminal indicado");
            }

        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listVentasEmpresas() {
        GUIListaVentasEmpresas.display();
//        try {
//            System.out.println("...:::: Listado de ventas de una empresa ::::....\n");
//
//            String rut = leeString("R.U.T");
//            String[][] lista = ControladorEmpresas.getInstance().listVentasEmpresa(Rut.of(rut));
//
//
//            if (lista.length != 0) {
//
//                System.out.printf(" *-------------*-----------*---------------*----------------*%n");
//                System.out.printf(" | FECHA       | TIPO      | MONTO PAGADO  |      TIPO PAGO |%n");
//                System.out.printf(" *-------------*-----------*---------------*----------------*%n");
//
//                DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                for (int i = 0; i < lista.length; i++) {
//                    String fechaOriginal = lista[i][0];
//                    LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
//                    String fechaFormateada = fecha.format(nuevoFormato);
//                    System.out.printf(" | %-10s  | %-8s  | %-8s      | %-14s |%n",
//                            fechaFormateada, lista[i][1], "$" + lista[i][2], "Pago " + lista[i][3]);
//                    System.out.printf(" *-------------*-----------*---------------*----------------*%n");
//                }
//            } else {
//                System.out.println("...::: No existen ventas registradas en la empresa");
//            }
//        } catch (SVPException e) {
//            System.out.println("...::: Error : " + e.getMessage());
//        }
    }

    private void pagaVentaPasajes(String idDocumento, TipoDocumento tipo) {
        System.out.println(":::: Pago de la venta");
        int opcPago = leeOpc("Efectivo[1] o Tarjeta[2]", 2);

        if (opcPago == 1) {
            SistemaVentaPasajes.getInstance().pagaVenta(idDocumento, tipo);

        } else {
            long nroTarjeta = Long.parseLong(leeString("Ingrese numero de Tarjeta"));
            SistemaVentaPasajes.getInstance().pagaVenta(idDocumento, tipo, nroTarjeta);
        }
        System.out.println();
        System.out.println("\n...:::: Venta realizada exitosamente ::::....\n\n");
    }

    private void generatePasajesVenta() {
        try {

            System.out.print("Ingrese id de la venta: ");
            String id = sc.next();

            System.out.print("Tipo Documento : [1] Boleta [2] Factura : ");
            int tipoOpc = elegirOpc(2);

            TipoDocumento tipo = switch (tipoOpc) {
                case 1 -> TipoDocumento.BOLETA;
                case 2 -> TipoDocumento.FACTURA;
                default -> null;
            };

            SistemaVentaPasajes.getInstance().generatePasajesVenta(id, tipo);
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void readDatosIniciales() {
        try {

            SistemaVentaPasajes.getInstance().readDatosIniciales();
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveDatosSistema() {
        try {
            SistemaVentaPasajes.getInstance().saveDatosSistema();
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void readDatosSistema() {
        try {
            SistemaVentaPasajes.getInstance().readDatosSistema();
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

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

    private int[] separador(String asientos, int cant) {

        String[] numerosString = asientos.split(",");
        int[] numAsientos = new int[cant];

        for (int i = 0; i < cant; i++) {
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


    // Datos predeterminados

    /*
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
        Utilidades.Nombre nombreConductor1 = new Utilidades.Nombre();
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

        // Terminal 2
        String nombreT2 = "Terminal 2";
        Direccion  direccionT2 = new Direccion("Calle terminal2", 333, "Talca");
        CE.createTerminal(nombreT2, direccionT2);

        // Bus
        CE.createBus("HIID", "Mercedes", "kjfdsl", 40, rutEmpresa1);

        // Viaje
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        IdPersona[] idTripulantes = new IdPersona[2];
        idTripulantes[0] = idAuxiliar1;
        idTripulantes[1] = idConductor1;
        String[] comunas = new String[2];
        comunas[0] = "Chillan";          // Comuna de salida
        comunas[1] = "Talca";       // Comuna de llegada

        SVP.createViaje(LocalDate.parse("20/03/2025", dateFormatter),
                LocalTime.parse("15:30", timeFormatter),
                1000, 90, "HIID", idTripulantes, comunas);

        // cliente
        Utilidades.IdPersona id1 = Utilidades.Pasaporte.of("1234", "chileno");
        Utilidades.IdPersona id2 = Utilidades.Rut.of("66.666.666-6");
        Utilidades.IdPersona id3 = Utilidades.Pasaporte.of("91011", "boliviano");
        Utilidades.IdPersona id4 = Utilidades.Pasaporte.of("1324", "chileno");
        Utilidades.Nombre n1 = new Utilidades.Nombre();
        n1.setNombres("Lucas Daniel");
        n1.setApellidoPaterno("Fernandez");
        n1.setApellidoMaterno("Garcia");
        n1.setTratamiento(Utilidades.Tratamiento.valueOf("SR"));

        Utilidades.Nombre n2 = new Utilidades.Nombre();
        n2.setNombres("Sofia Isabel");
        n2.setApellidoPaterno("Martinez");
        n2.setApellidoMaterno("Lopez");
        n2.setTratamiento(Utilidades.Tratamiento.valueOf("SRA"));

        Utilidades.Nombre n3 = new Utilidades.Nombre();
        n3.setNombres("Carlos Alberto");
        n3.setApellidoPaterno("Rodriguez");
        n3.setApellidoMaterno("Silva");

        Utilidades.Nombre n4 = new Utilidades.Nombre();
        n4.setNombres("Carlos Alberto");
        n4.setApellidoPaterno("Rodriguez");

        SVP.createCliente(id1, n1,"95234", "matias@gmail.com"); //cliente con pasaporte
        SVP.createCliente(id2, n2,"4873", "some@gmail.com"); //cliente con rut

        SVP.createPasajero(id1, n1, "83247", n1, "83247"); // pasajero con pasaporte
        SVP.createPasajero(id2, n2, "1234", n2, "1234"); // pasajero con rut
    }

     */
}
