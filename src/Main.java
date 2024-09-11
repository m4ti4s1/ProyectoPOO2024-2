import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    Scanner sc = new Scanner(System.in);
    SistemaVentaPasaje svp = new SistemaVentaPasaje();


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Main main = new Main();
        int opc = 0;

        do {
            main.menu();
            opc = main.elegirOpc(8, sc);

            switch(opc) {
                case 1:
                    main.createCliente(sc);
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
        System.out.println("============================");
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

    private void createCliente(Scanner sc) {
        System.out.println("...::: Crear un nuevo Cliente :::...");

        System.out.print("Rut[1] o Pasaporte[2] : ");
        int tipoDocumento = elegirOpc(2, sc);
        IdPersona idPersona = null;
        String rut = "";
        String numero = "";
        String nacionalidad = "";
        switch(tipoDocumento) {
            case 1:
                // Rut
                rut = sc.next();
                idPersona = Rut.of(rut);

                break;
            case 2:
                // Pasaporte
                System.out.print("Ingresa numero de pasaporte: ");
                numero = sc.next();
                System.out.print("Ingresa nacionalidad: ");
                nacionalidad = sc.next();

                idPersona = Pasaporte.of(numero, nacionalidad);
                break;
        }


        Nombre nombreCliente = new Nombre();

        System.out.print("Sr. [1] o Sra. [2]: ");
        int opcTratamiento = elegirOpc(2, sc);
        switch(opcTratamiento) {
            case 1:
                nombreCliente.setTratamiento(Tratamiento.valueOf("SR"));
                break;
            case 2:
                nombreCliente.setTratamiento(Tratamiento.valueOf("SRA"));
                break;
        }

        System.out.println("Nombres");
        System.out.print("  Primer nombre: ");
        String primerNombre = sc.next();
        System.out.print("  Segundo nombre: ");
        String segundoNombre = sc.next();
        nombreCliente.setNombres(primerNombre + " " + segundoNombre);

        System.out.print("Apellido Paterno: ");
        String apellidoPaterno = sc.next();
        nombreCliente.setApellidoPaterno(apellidoPaterno);

        System.out.print("Apellido Materno: ");
        String apellidoMaterno = sc.next();
        nombreCliente.setApellidoMaterno(apellidoMaterno);

        System.out.print("Telefono Movil: ");
        String telefono = sc.next();


        System.out.print("Email: ");
        String email = sc.next();

        if (!(svp.createCliente(idPersona, nombreCliente,telefono, email))) {
            System.out.println("Cliente ya existe");
        } else {
            System.out.println("\n....:::: Crear un nuevo Cliente ::::....\n");
            System.out.printf("%25s %d%n", "Rut[1] o Pasaporte[2] :", tipoDocumento);
            switch(tipoDocumento) {
                case 1:
                    System.out.printf("%25s %s%n", "R.U.T :", rut);
                    break;
                case 2:
                    System.out.printf("%25s %s%n", "Numero :", numero);
                    System.out.printf("%25s %s%n", "Nacionalidad :", nacionalidad);
                    break;
            }
            System.out.printf("%25s %d%n", "Sr.[1] o Sra.[2] :", opcTratamiento);
            System.out.printf("%25s %s%n", "Nombres :", primerNombre + " " + segundoNombre);
            System.out.printf("%25s %s%n", "Apellido Paterno :", apellidoPaterno);
            System.out.printf("%25s %s%n", "Apellido Materno :", apellidoMaterno);
            System.out.printf("%25s %s%n", "Telefono movil :", telefono);
            System.out.printf("%25s %s%n", "Email :", email);
            System.out.println("\n....:::: Cliente guardado exitosamente ::::....");
        }

    }

    private void createBus() {
        System.out.println("...::: Creacion de un nuevo BUS :::....");
        System.out.print("Patente : ");
        String patente = sc.next();
        sc.nextLine();

        System.out.print("Marca : ");
        String marca = sc.nextLine();

        System.out.print("Modelo : ");
        String modelo = sc.nextLine();

        System.out.print("Número de asientos : ");
        int nroAsientos = sc.nextInt();
        sc.nextLine();

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

        System.out.print("Fecha [dd/mm/yyyy] : ");
        String fechaStr = sc.nextLine().trim();
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);

        System.out.print("Hora [hh:mm] : ");
        String horaStr = sc.nextLine().trim();
        LocalTime hora = LocalTime.parse(horaStr, timeFormatter);

        System.out.print("Precio : ");
        int precio = sc.nextInt();
        sc.nextLine();

        System.out.print("Patente Bus : ");
        String patente = sc.nextLine().trim();

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
                Rut id= Rut.of(idRUT);
                svp.getNombrePasajero(id);
                break;
            case 2:
                System.out.println("..::Pasaporte::.. \n-Nacionalidad : ");String nacionalidad=sc.next();
                System.out.println("-Numero Documento : ");String num=sc.next();
                Pasaporte pasaporte= Pasaporte.of(num,nacionalidad);

                break;
        }

    }

    private void listPasajerosViaje() {

    }

    private void listVentas() {

    }

    private void listViajes() {

    }

    private int elegirOpc(int cantOpciones, Scanner sc) {
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
}
