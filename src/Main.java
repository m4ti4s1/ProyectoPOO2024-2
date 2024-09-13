import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private SistemaVentaPasaje svp = new SistemaVentaPasaje();


    public static void main(String[] args) {
        Main main = new Main();
        int opc = 0;


        do {
            main.menu();
            opc = main.elegirOpc(8);

            switch(opc) {
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
        System.out.println("...::: Crear un nuevo Cliente :::...\n");

        int tipoDocumento = leeOpc("Rut[1] o Pasaporte[2]", 2);

        IdPersona idPersona = null;
        String rut = "";
        String numero = "";
        String nacionalidad = "";

        switch(tipoDocumento) {
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

        switch(opcTratamiento) {
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

        if (!(svp.createCliente(idPersona, nombreCliente,telefono, email))) {
            System.out.println("\n....:::: Cliente ya existe ::::....\n");
        } else {
            System.out.println("\n....:::: Cliente guardado exitosamente ::::....\n");
        }

    }

    private void createBus() {
        System.out.println("...::: Creacion de un nuevo BUS :::....\n");
        String patente = leeString("Patente");

        String marca = leeString("Marca");

        String modelo = leeString("Modelo");

        int nroAsientos = Integer.parseInt(leeString("Numero de asientos"));

        if (svp.createBus(patente, marca, modelo, nroAsientos)) {
            System.out.println("\n...:::: Bus guardado exitosamente ::::....");
        } else {
            System.out.println("\n...:::: Ya existe un bus con esa patente ::::....");
        }
    }

    private void createViaje() {
        System.out.println("...::: Creacion de un nuevo Viaje :::....");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String fechaStr = leeString("Fecha [dd/mm/yyyy]");
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);

        String horaStr = leeString("Hora [hh:mm]");
        LocalTime hora = LocalTime.parse(horaStr, timeFormatter);

        System.out.printf("%30s : ", "Precio");
        int precio = sc.nextInt();

        String patente = leeString("Patente Bus");

        if (svp.createViaje(fecha, hora, precio, patente)) {
            System.out.println("\n...:::: Viaje guardado exitosamente ::::....");
        } else {
            System.out.println("\n...:::: No se pudo crear el viaje. Verifica los datos ::::....");
        }
    }

    private void vendePasaje() {

        System.out.println(".....::: Venta de Pasajes:::....");
        System.out.printf("%n %n %n %s %n",":::Datos de venta");
        System.out.println("ID Documento : "); String IdDocumento = sc.next();
        System.out.println("Tipo Documento : [1] Boleta [2] Factura : ");int tipoDocumento=sc.nextInt();
        System.out.println("Fecha de venta[dd/mm/yyyy] : ");String fecha=sc.next();
        System.out.printf("%n %n %n %s %n %n",":::: Datos del Cliente");
        System.out.println("Rut [1] o Pasaporte [2] : ");int op=sc.nextInt();
        switch (op){
            case 1:
                System.out.println("R.U.T : ");String idRUT=sc.next();
                if(null==svp.getNombrePasajero(Rut.of(idRUT))){
                    System.out.println("No se ah Encontrado al usuario");
                    return;
                }else{

                System.out.println("\nNombre Cliente : "+svp.getNombrePasajero(Rut.of(idRUT)));break;}
            case 2:
                System.out.println("..::Pasaporte::.. \n-Nacionalidad : ");String nacionalidad=sc.next();
                System.out.println("-Numero Documento : ");String num=sc.next();
                if(null==svp.getNombrePasajero(Pasaporte.of(num,nacionalidad))){
                    System.out.println("\nNo se ah Encontrado al Usuario");
                    return;
                }else {

                    System.out.println("\nNombre Cliente : " + svp.getNombreCliente(Pasaporte.of(num, nacionalidad)));break;}
        }
        limpiarConsola();
        System.out.println("::::Pasajes a Vender\n\n   Cantidad de pasajes : ");String cant=sc.next();
        System.out.println("Fecha de Viaje [dd/mm/yyyy] : ");String fechaViaje=sc.next();
        System.out.println("\n\n::::Listado de Horarios Disponibles");
        String [][] matrizViajes = svp.getHorariosDisponibles(LocalDate.parse(fechaViaje));
        System.out.printf("       +------------+----------------+------------+------------+%n");
        System.out.printf("       |   BUS      |     Salida     |   Valor    |  Asientos  |%n");
        System.out.printf("       +------------+----------------+------------+------------+%n");
        for (int i = 0; i < matrizViajes.length; i++) {
            System.out.printf(" %-5d | %-10s | %-14s | %-10s | %-10s |%n",
                    i + 1, matrizViajes[i][0], matrizViajes[i][1], matrizViajes[i][2], matrizViajes[i][3]);
            System.out.printf("       +------------+----------------+------------+------------+%n");
        } //POSICION DE VIAJE ELEGIDO
        System.out.println("\n Selecione viaje en [1.."+matrizViajes.length+"] : ");int Viaje= sc.nextInt()-1;
        String [][]matrizAsientos=svp.listAsientosDeViaje(LocalDate.parse(fechaViaje),LocalTime.parse(matrizViajes[Viaje][1]),matrizViajes[Viaje][0]);




    }

    private void listPasajerosViaje() {

    }

    private void listVentas() {

    }

    private void listViajes() {

    }

    private int elegirOpc(int cantOpciones) {
        int opc = 0;

        boolean valido = false;
        while (!valido) {
            opc = sc.nextInt();

            if (opc > 0 && opc <= cantOpciones){
                valido = true;
            } else {
                System.out.println("Opcion invalida, intente denuevo");
            }
        }
        return opc;
    }
    private  static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }


    // metodos para leer string e ints de opciones para los reportes
    private String leeString(String msg){
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085,;\t]");
        System.out.printf("%30s : ", msg);
        return sc.next();
    }
    private int leeOpc(String msg, int cantOpc) {
        int opc = 0;
        do {
            System.out.printf("%30s : ", msg);
            opc = sc.nextInt();
        } while (opc < 0 && opc > cantOpc);

        return opc;
    }
}
