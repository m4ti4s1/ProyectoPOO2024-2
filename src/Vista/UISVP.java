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
        GUICreaBus.display();
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

        String fechaViaje = leeString("Fecha de viaje [dd/mm/yyyy]");
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


                idCliente = Rut.of(idRUT);
                break;

            case 2:
                String num = leeString("Numero Pasaporte");
                String nacionalidad = leeString("Nacionalidad");
                idCliente = Pasaporte.of(num, nacionalidad);
                break;

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


}
