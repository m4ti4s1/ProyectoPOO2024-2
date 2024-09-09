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

        switch(tipoDocumento) {
            case 1:
                // Rut
                String rut = sc.nextLine();
                idPersona = Rut.of(rut);

                break;
            case 2:
                // Pasaporte
                String numero = sc.next();
                String nacionalidad = sc.next();

                idPersona = Pasaporte.of(numero, nacionalidad);
                break;
        }


        Nombre nombreCliente = new Nombre();

        System.out.print("\nSr. [1] o Sra. [2]: ");
        int opcTratamiento = elegirOpc(2, sc);
        switch(opcTratamiento) {
            case 1:
                nombreCliente.setTratamiento(Tratamiento.valueOf("SR"));
                break;
            case 2:
                nombreCliente.setTratamiento(Tratamiento.valueOf("SRA"));
                break;
        }

        System.out.print("\nNombres: ");
        String primerNombre = sc.next();
        String segundoNombre = sc.next();
        nombreCliente.setNombres(primerNombre + " " + segundoNombre);

        System.out.print("\nApellido Paterno: ");
        String apellidoPaterno = sc.next();
        nombreCliente.setApellidoPaterno(apellidoPaterno);

        System.out.print("\nApellido Materno: ");
        String apellidoMaterno = sc.next();
        nombreCliente.setApellidoMaterno(apellidoMaterno);

        System.out.print("\nTelefono Movil: ");
        String telefono = sc.next();


        System.out.print("\nEmail: ");
        String email = sc.next();

        if (!(svp.createCliente(idPersona, nombreCliente,telefono, email))) {
            System.out.println("Cliente ya existe");
        } else {
            System.out.println("Cliente creado exitosamente");
        }

    }

    private void createBus() {

    }

    private void createViaje() {

    }

    private void vendePasaje() {

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
}
