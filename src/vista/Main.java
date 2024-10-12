package vista;// Test para rama version 2 de proyecto POO

import Controlador.SistemaVentaPasajes;
import Utilidades.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private SistemaVentaPasajes svp = new SistemaVentaPasajes();


    public static void main(String[] args) {

        Main main = new Main();
        int opc = 0;
        //main.inicia(); // cargar clientes, viajes y buses, para iniciar desde la venta

        do {
            main.menu();
            opc = main.elegirOpc(8);

            switch (opc) {
                case 1:
                    main.createCliente();
                    break;
                case 2:
                    main.createBus();
                    break;
                case 3:
                    main.createViaje();
                    break;
                case 4:
                    main.vendePasaje();
                    break;
                case 5:
                    main.listPasajerosViaje();
                    break;
                case 6:
                    main.listVentas();
                    break;
                case 7:
                    main.listViajes();
                    break;
                case 8:
                    System.out.println("Saliendo...");
                    break;
            }
        } while (opc != 8);

    }
    /*
    private void inicia() {
        Utilidades.IdPersona id1 = Utilidades.Pasaporte.of("1234", "chileno");
        Utilidades.IdPersona id2 = Utilidades.Rut.of("11.111.111-1");
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

        svp.createCliente(id1, n1,"95234", "matias@gmail.com"); //cliente con pasaporte
        svp.createCliente(id2, n2,"4873", "some@gmail.com"); //cliente con rut

        svp.createBus("HIID", "Mercedes", "kjfdsl", 40);
        svp.createBus("HELLO", "Mercedes", "Benz", 80);


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        svp.createViaje(LocalDate.parse("20/03/2025", dateFormatter), LocalTime.parse("15:30", timeFormatter), 1000, "HIID");
        svp.createViaje(LocalDate.parse("23/04/2025", dateFormatter), LocalTime.parse("18:00", timeFormatter), 2000, "HELLO");

        svp.createPasajero(id1, n1, "83247", n1, "83247"); // pasajero con pasaporte
        svp.createPasajero(id2, n2, "1234", n2, "1234"); // pasajero con rut

    }
    */

    private void listVentas() {
        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[][] listaVentas = svp.listVentas();

        System.out.printf("\n%44s\n", "...:::: Listado de Ventas ::::....\n");
        System.out.printf(" +------------+----------+------------+-----------------+----------------------------------+--------------+--------------+%n");
        System.out.printf(" | ID DOCUMENT| TIPO DOCU|      FECHA |   RUT/PASAPORTE | CLIENTE                          | CANT BOLETOS |  TOTAL VENTA |%n");
        System.out.printf(" +------------+----------+------------+-----------------+----------------------------------+--------------+--------------+%n");

        for (int i = 0; i < listaVentas.length; i++) {
            String fechaOriginal = listaVentas[i][2];
            LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
            String fechaFormateada = fecha.format(nuevoFormato);

            System.out.printf(" |      %-5s | %-7s  | %-10s |    %-5s | %-32s |            %-2s|       %-6s |%n",
                    listaVentas[i][0], listaVentas[i][1], fechaFormateada, listaVentas[i][3], listaVentas[i][4], listaVentas[i][5], "$"+listaVentas[i][6]);
            System.out.printf(" +------------+----------+------------+-----------------+----------------------------------+--------------+--------------+%n");
        }

    }


    private void listViajes() {
        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[][] listaViajes = svp.listViajes();
        System.out.printf("\n%44s\n", "...:::: Listado de Viajes ::::....\n");
        System.out.printf(" +--------------+------------+------------+---------------+------------+%n");
        System.out.printf(" |        FECHA |       HORA |     PRECIO |   DISPONIBLES | PATENTE    |%n");
        System.out.printf(" +--------------+------------+------------+---------------+------------+%n");

        for (int i = 0; i < listaViajes.length; i++) {
            String fechaOriginal = listaViajes[i][0];
            LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
            String fechaFormateada = fecha.format(nuevoFormato);

            System.out.printf(" |   %-10s |      %-5s |       %-4s |            %-2s | %-10s |%n",
                    fechaFormateada, listaViajes[i][1], listaViajes[i][2], listaViajes[i][3], listaViajes[i][4]);
            System.out.printf(" +--------------+------------+------------+---------------+------------+%n");
        }
    }

    private void listPasajerosViaje() {
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

        String[][] listaPasajeros = svp.listPasajeros(fecha, hora, patenteBus);
        for (int i = 0; i < listaPasajeros.length; i++) {
            System.out.printf(" |     %-3s |   %-13s | %-33s | %-33s | %-17s |%n",
                    listaPasajeros[i][0],listaPasajeros[i][1], listaPasajeros[i][2], listaPasajeros[i][3], listaPasajeros[i][4]);
            System.out.printf(" +---------+-----------------+-----------------------------------+-----------------------------------+-------------------+%n");
        }
    }
    

    private void menu() {
        System.out.println("\n============================");
        System.out.println("...::: Menu principal :::...\n");
        System.out.println("1) Crear cliente");
        System.out.println("2) Crear bus");
        System.out.println("3) Crear viaje");
        System.out.println("4) Vender pasajes");
        System.out.println("5) Lista de pasajero");
        System.out.println("6) Lista de ventas");
        System.out.println("7) Lista de viajes");
        System.out.println("8) Salir");
        System.out.println("----------------------------");
        System.out.print("..:: Ingrese numero de opcion: ");
    }

    private void createCliente() {
        System.out.println("...::: Crear un nuevo Modelo.Cliente :::...\n");

        int tipoDocumento = leeOpc("Utilidades.Rut[1] o Utilidades.Pasaporte[2]", 2);

        IdPersona idPersona = null;
        String rut = "";
        String numero = "";
        String nacionalidad = "";

        switch (tipoDocumento) {
            case 1:
                // Utilidades.Rut
                rut = leeString("R.U.T");
                idPersona = Rut.of(rut);

                break;
            case 2:
                // Utilidades.Pasaporte
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

        if (!(svp.createCliente(idPersona, nombreCliente, telefono, email))) {
            System.out.println("\n....:::: Modelo.Cliente ya existe ::::....\n");
        } else {
            System.out.println("\n....:::: Modelo.Cliente guardado exitosamente ::::....\n");
        }

    }

    private void createBus() {
        System.out.println("...::: Creacion de un nuevo BUS :::....\n");
        String patente = leeString("Patente");

        do {
            if(!esPatenteAlfanumerica(patente)) {
                System.out.println("La Patente debe ser alfanumerica");
                patente = leeString("Patente");
            }
        } while (!esPatenteAlfanumerica(patente));

        String marca = leeString("Marca");

        String modelo = leeString("Modelo");

        int nroAsientos = Integer.parseInt(leeString("Numero de asientos"));

        if (svp.createBus(patente, marca, modelo, nroAsientos)) {
            System.out.println("\n...:::: Modelo.Bus guardado exitosamente ::::....");
        } else {
            System.out.println("\n...:::: Ya existe un bus con esa patente ::::....");
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

        System.out.printf("%30s : ", "Precio");
        int precio = sc.nextInt();

        String patente = leeString("Patente Modelo.Bus");

        if (svp.createViaje(fecha, hora, precio, patente)) {
            System.out.println("\n...:::: Modelo.Viaje guardado exitosamente ::::....");
        } else {
            System.out.println("\n...:::: No se pudo crear el viaje. Verifica los datos ::::....");
        }
    }

    private void vendePasaje() {

        System.out.println(".....::: Modelo.Venta de Pasajes:::....\n\n");
        System.out.println(":::Datos de venta");

        String IdDocumento = leeString("ID Documento");

        System.out.print("Tipo Documento : [1] Boleta [2] Factura : ");
        int tipo = elegirOpc(2);

        TipoDocumento tipoDocumento = null;
        switch (tipo) {
            case 1:
                tipoDocumento = TipoDocumento.valueOf("BOLETA");
                break;
            case 2:
                tipoDocumento = TipoDocumento.valueOf("FACTURA");
                break;
        }


        String fecha = leeString("Fecha de Modelo.Venta [dd/mm/yyyy]");

        System.out.println("\n\n:::: Datos del Modelo.Cliente");
        int op = leeOpc("Utilidades.Rut[1] o Utilidades.Pasaporte[2]", 2);

        IdPersona idCliente = null;
        switch (op) {
            case 1:
                String idRUT = leeString("R.U.T");

                if (null == svp.getNombreCliente(Rut.of(idRUT))) {
                    System.out.println(":::: Modelo.Cliente no Encontrado");
                    return;
                } else {
                    System.out.printf("%30s : %s", "Utilidades.Nombre Modelo.Cliente", svp.getNombreCliente(Rut.of(idRUT)));
                    idCliente = Rut.of(idRUT);
                    break;
                }

            case 2:
                String num = leeString("Numero Utilidades.Pasaporte");
                String nacionalidad = leeString("Nacionalidad");

                if (null == svp.getNombreCliente(Pasaporte.of(num, nacionalidad))) {
                    System.out.println("\nNo se ah Encontrado al Usuario");
                    return;
                } else {

                    System.out.println("\nUtilidades.Nombre Modelo.Cliente : " + svp.getNombreCliente(Pasaporte.of(num, nacionalidad)));

                    idCliente = Pasaporte.of(num, nacionalidad);
                    break;
                }


        }

        // Inicializar la venta fuera del swtich
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Todo: Identificar los fallos de inicializacion de la venta y desplegarlos
        if (!(svp.iniciaVenta(IdDocumento, tipoDocumento, LocalDate.parse(fecha, formatter), idCliente))) {
            System.out.println("....:::: Ah Surgido un problema, La venta no se pudo inicializar");
            return;
        }


        // TODO Hasta aqui, funciona bien si todos los datos son validos

        System.out.println("\n\n::::Pasajes a Vender");
        int cant = leeInt("Cantidad de pasajes");

        String fechaViaje = leeString("Fecha de viaje[dd/mm/yyyy]");

        LocalDate fechaV = LocalDate.parse(fechaViaje, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println("\n\n::::Listado de Horarios Disponibles");

        String[][] matrizViajes = svp.getHorariosDisponibles(fechaV);

        System.out.printf("       +------------+----------------+------------+------------+%n");
        System.out.printf("       |   BUS      |     Salida     |   Valor    |  Asientos  |%n");
        System.out.printf("       +------------+----------------+------------+------------+%n");
        for (int i = 0; i < matrizViajes.length; i++) {
            System.out.printf(" %-5d | %-10s | %-14s | %-10s | %-10s |%n",
                    i + 1, matrizViajes[i][0], matrizViajes[i][1], matrizViajes[i][2], matrizViajes[i][3]);
            System.out.printf("       +------------+----------------+------------+------------+%n");
        } //POSICION DE VIAJE ELEGIDO

        System.out.print("\n Selecione viaje en [1.." + matrizViajes.length + "] : ");
        int Viaje = sc.nextInt() - 1;


        LocalTime horaV = LocalTime.parse(matrizViajes[Viaje][1], DateTimeFormatter.ofPattern("HH:mm"));
        String patBus = matrizViajes[Viaje][0];

        String[] matrizAsientos = svp.listAsientosDeViaje(fechaV, LocalTime.parse(matrizViajes[Viaje][1]), matrizViajes[Viaje][0]);

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
                        System.out.println("Asiento " + (asientoIndex + 1) + " está ocupado");
                        asientosDisponibles = false;
                    }
                }
            }
        } while (!asientosDisponibles);

        // ciclo para cada pasaje
        for (int i = 0; i < cant; i++) {

            System.out.println("\n::::Datos pasajeros " + (i + 1));

            int opcId = leeOpc("Utilidades.Rut[1] o Utilidades.Pasaporte[2]", 2);

            switch (opcId) {
                case 1:
                    // caso rut

                    String idRut = leeString("R.U.T");

                    if (null == svp.getNombrePasajero(Rut.of(idRut))) {

                        System.out.println("\n\n:::: Modelo.Pasajero no Encontrado\n");

                        Nombre newPasajero = new Nombre();
                        System.out.println("..: Cree al pasajero :..");
                        System.out.println("\n...::: Datos Modelo.Pasajero :::...");

                        int opcTratamiento = leeOpc("Sr.[1] o Sra.[2]", 2);

                        switch (opcTratamiento) {
                            case 1 -> newPasajero.setTratamiento(Tratamiento.valueOf("SRA"));
                            case 2 -> newPasajero.setTratamiento(Tratamiento.valueOf("SR"));
                        }

                        // Todo Arreglar copiando el formato al crear Modelo.Cliente
                        String nombres = leeString("Nombres");
                        String apellidoPaterno = leeString("Apellido Paterno");
                        String apellidoMaterno = leeString("Apellido Materno");
                        String fono = leeString("Ingresa fono");

                        newPasajero.setNombres(nombres);
                        newPasajero.setApellidoPaterno(apellidoPaterno);
                        newPasajero.setApellidoMaterno(apellidoMaterno);

                        Nombre contacto = new Nombre();

                        // No necesario porque ya lo ingreso antes
                        System.out.println("\n...::: Datos Contacto Modelo.Pasajero :::...");

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


                        if (svp.createPasajero(Rut.of(idRut), newPasajero, fono, contacto, fonoContacto)) {
                            System.out.println("\n:::: Modelo.Pasaje agregado exitosamente");
                        } else {
                            System.out.println(":::: El pasaje no pudo agregarse");
                        }


                    }

                    svp.vendePasaje(IdDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], Rut.of(idRut));
                    break;


                case 2:
                    // caso pasaporte
                    String numeroPasaporte = leeString("Numero Utilidades.Pasaporte");
                    String nacionalidad = leeString("Nacionalidad");

                    if (null == svp.getNombrePasajero(Pasaporte.of(numeroPasaporte, nacionalidad))) {

                        System.out.println("\n\n:::: Modelo.Pasajero no Encontrado\n");

                        Nombre newPasajero = new Nombre();
                        System.out.println("..: Cree al pasajero :..");
                        System.out.println("\n...::: Datos Modelo.Pasajero :::...");

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
                        System.out.println("\n...::: Datos Contacto Modelo.Pasajero :::...");

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


                        if (svp.createPasajero(Pasaporte.of(numeroPasaporte, nacionalidad), newPasajero, fono, contacto, fonoContacto)) {
                            System.out.println("\n:::: Modelo.Pasaje agregado exitosamente");
                        } else {
                            System.out.println(":::: El pasaje no pudo agregarse");
                        }

                    }

                    svp.vendePasaje(IdDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], Pasaporte.of(numeroPasaporte, nacionalidad));
                    break;
            } //cierre del switch
        }// cierre del for

        int montoVenta = svp.getMontoVenta(IdDocumento, tipoDocumento);
        System.out.println(":::: Monto Total de la venta: " + montoVenta);
        String msg = "...:::: Modelo.Venta generada exitosamente ::::....";
        System.out.printf("\n%47s\n", msg);

        String[] boleta = svp.pasajesAImprimir(IdDocumento, tipoDocumento);
        for (String s : boleta) {
            System.out.println(s);
        }
    }



    private int elegirOpc(int cantOpciones) {
        int opc = 0;

        boolean valido = false;
        while (!valido) {
            opc = sc.nextInt();

            if (opc > 0 && opc <= cantOpciones) {
                valido = true;
            } else {
                System.out.println("Opcion invalida, intente denuevo");
            }
        }
        return opc;
    }


    // metodos para leer string e ints de opciones para los reportes
    private String leeString(String msg) {
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085;\t]");
        System.out.printf("%30s : ", msg);
        return sc.next();
    }
    private int leeInt(String msg) {
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085;\t]");
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
        do {
            System.out.printf("%30s : ", msg);
            opc = sc.nextInt();
        } while (opc < 0 && opc > cantOpc);

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
    


