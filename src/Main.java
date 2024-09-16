import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private SistemaVentaPasajes svp = new SistemaVentaPasajes();


    public static void main(String[] args) {

        Main main = new Main();
        int opc = 0;
        main.inicia(); // cargar clientes, viajes y buses, para iniciar desde la venta

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
    private void inicia() {
        IdPersona id1 = Pasaporte.of("1234", "chileno");
        IdPersona id2 = Rut.of("11.111.111-1");
        IdPersona id3 = Pasaporte.of("91011", "boliviano");
        IdPersona id4 = Pasaporte.of("1324", "chileno");


        Nombre n1 = new Nombre();
        n1.setNombres("Lucas Daniel");
        n1.setApellidoPaterno("Fernandez");
        n1.setApellidoMaterno("Garcia");
        n1.setTratamiento(Tratamiento.valueOf("SR"));

        Nombre n2 = new Nombre();
        n2.setNombres("Sofia Isabel");
        n2.setApellidoPaterno("Martinez");
        n2.setApellidoMaterno("Lopez");
        n2.setTratamiento(Tratamiento.valueOf("SRA"));

        Nombre n3 = new Nombre();
        n3.setNombres("Carlos Alberto");
        n3.setApellidoPaterno("Rodriguez");
        n3.setApellidoMaterno("Silva");

        Nombre n4 = new Nombre();
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

    }

    private void listVentas() {
    }


    private void listViajes() {
        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[][] listaViajes = svp.listViajes();
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

        if (!(svp.createCliente(idPersona, nombreCliente, telefono, email))) {
            System.out.println("\n....:::: Cliente ya existe ::::....\n");
        } else {
            System.out.println("\n....:::: Cliente guardado exitosamente ::::....\n");
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

        System.out.println(".....::: Venta de Pasajes:::....\n\n");
        System.out.println(":::Datos de venta");

        String IdDocumento = leeString("ID Documento");

        System.out.print("Tipo Documento : [1] Boleta [2] Factura : ");
        int tipo = elegirOpc(2);

        TipoDocumento tipoDocumento = null;
        switch (tipo) {
            case 1:
                tipoDocumento = TipoDocumento.valueOf("BOLETA");
            case 2:
                tipoDocumento = TipoDocumento.valueOf("FACTURA");
        }


        String fecha = leeString("Fecha de Venta [dd/mm/yyyy");

        System.out.println("\n\n:::: Datos del Cliente");
        int op = leeOpc("Rut[1] o Pasaporte[2]", 2);

        IdPersona idCliente = null;
        switch (op) {
            case 1:
                String idRUT = leeString("R.U.T");

                if (null == svp.getNombreCliente(Rut.of(idRUT))) {
                    System.out.println(":::: Cliente no Encontrado");
                    return;
                } else {
                    System.out.printf("%30s : %s", "Nombre Cliente", svp.getNombreCliente(Rut.of(idRUT)) );
                    idCliente = Rut.of(idRUT);
                    break;
                }

            case 2:
                String num = leeString("Numero Pasaporte");
                String nacionalidad = leeString("Nacionalidad");

                if (null == svp.getNombreCliente(Pasaporte.of(num, nacionalidad))) {
                    System.out.println("\nNo se ah Encontrado al Usuario");
                    return;
                } else {

                    System.out.println("\nNombre Cliente : " + svp.getNombreCliente(Pasaporte.of(num, nacionalidad)));

                    idCliente = Pasaporte.of(num, nacionalidad);
                    break;
                }


        }

        // Inicializar la venta fuera del swtich
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Todo: Identificar los fallos de inicializacion de la venta y desplegarlos
        if (!(svp.iniciaVenta(IdDocumento, tipoDocumento, LocalDate.parse(fecha, formatter),idCliente))) {
            System.out.println("....:::: Ah Surgido un problema, La venta no se pudo inicializar");
        }





        // TODO Hasta aqui, funciona bien si todos los datos son validos

        System.out.println("\n\n::::Pasajes a Vender\n\n   Cantidad de pasajes : ");
        int cant = sc.nextInt();
        System.out.println("Fecha de Viaje [dd/mm/yyyy] : ");
        String fechaViaje = sc.next();

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

        System.out.println("\n Selecione viaje en [1.." + matrizViajes.length + "] : ");
        int Viaje = sc.nextInt() - 1;


        LocalTime horaV = LocalTime.parse(matrizViajes[Viaje][1], DateTimeFormatter.ofPattern("HH:mm"));
        String patBus = matrizViajes[Viaje][0];

        String[] matrizAsientos = svp.listAsientosDeViaje(fechaV, LocalTime.parse(matrizViajes[Viaje][1]), matrizViajes[Viaje][0]);

        System.out.printf("       *---*---*---*---*---*%n");
        for (int i = 0; i < matrizAsientos.length ; i++) {

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

        String asientos = leeString("Seleccione sus asientos [separe por ,]");
        int [] numAsientos=separador(asientos,cant);

        // Todo Verificar que los asientos esten disponibles

        // ciclo para cada pasaje
        for(int i=0 ; i<cant ; i++) {

            System.out.println("::::Datos pasajeros "+(i+1));

            int opcId = leeOpc("Rut[1] o Pasaporte[2]",2);

            switch (opcId) {
                case 1:
                    // caso rut

                    String idRut = leeString("R.U.T");

                    if (null == svp.getNombrePasajero (Rut.of(idRut))) {

                        System.out.println(":::: Pasajero no Encontrado");

                        Nombre newPasajero = new Nombre();
                        System.out.println("..:Cree al pasajero:..");

                        System.out.println("   Ingrese SRA[1] o SR[2]");
                        int opcTratamiento = leeOpc("Sr.[1] o Sra.[2]", 2);

                        switch (opcTratamiento) {
                            case 1 -> newPasajero.setTratamiento(Tratamiento.valueOf("SRA"));
                            case 2 -> newPasajero.setTratamiento(Tratamiento.valueOf("SR"));
                        }

                        // Todo Arreglar copiando el formato al crear Cliente
                        System.out.printf("%n %5s","Ingrese nombres");newPasajero.setNombres(sc.next());
                        System.out.printf("%n %5s","Apellido paterno");newPasajero.setApellidoPaterno(sc.next());
                        System.out.printf("%n %5s","Apellido materno");newPasajero.setApellidoMaterno(sc.next());

                        Nombre contacto = new Nombre();
                        System.out.printf("%n %5s","Ingrese SRA[1] o SR[2] del contacto");
                        switch (sc.nextInt()){
                            case 1:contacto.setTratamiento(Tratamiento.valueOf("SRA"));break;
                            case 2:contacto.setTratamiento(Tratamiento.valueOf("SR"));break;
                        }

                        System.out.printf("%n %5s","Ingrese nombres del contacto");contacto.setNombres(sc.next());
                        System.out.printf("%n %5s","Apellido paterno del contacto");contacto.setApellidoPaterno(sc.next());
                        System.out.printf("%n %5s","Apellido materno del contacto");contacto.setApellidoMaterno(sc.next());
                        System.out.printf("%n %5s","..::Ingrese fono Pasajero");String fono=sc.next();
                        System.out.printf("%n %5s %n","..::Ingrese fono contacto");String fonocontacto=sc.next();

                        svp.createPasajero(Rut.of(idRut), newPasajero, fono, contacto, fonocontacto);


                    }

                    svp.vendePasaje(IdDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], Rut.of(idRut));
                    break;


                case 2:
                    // caso pasaporte
                    String numeroPasaporte = leeString("Numero Pasaporte");
                    String nacionalidad = leeString("Nacionalidad");
                    if (null == svp.getNombrePasajero (Pasaporte.of(numeroPasaporte, nacionalidad))) {

                        System.out.println(":::: Pasajero no Encontrado");

                        Nombre newPasajero = new Nombre();
                        System.out.println("..:Cree al pasajero:..");

                        System.out.println("   Ingrese SRA[1] o SR[2]");
                        int opcTratamiento = leeOpc("Sr.[1] o Sra.[2]", 2);

                        switch (opcTratamiento) {
                            case 1 -> newPasajero.setTratamiento(Tratamiento.valueOf("SRA"));
                            case 2 -> newPasajero.setTratamiento(Tratamiento.valueOf("SR"));
                        }

                        // Todo Arreglar copiando el formato al crear Cliente
                        System.out.printf("%n %5s","Ingrese nombres");newPasajero.setNombres(sc.next());
                        System.out.printf("%n %5s","Apellido paterno");newPasajero.setApellidoPaterno(sc.next());
                        System.out.printf("%n %5s","Apellido materno");newPasajero.setApellidoMaterno(sc.next());

                        Nombre contacto = new Nombre();
                        System.out.printf("%n %5s","Ingrese SRA[1] o SR[2] del contacto");
                        switch (sc.nextInt()){
                            case 1:contacto.setTratamiento(Tratamiento.valueOf("SRA"));break;
                            case 2:contacto.setTratamiento(Tratamiento.valueOf("SR"));break;
                        }

                        System.out.printf("%n %5s","Ingrese nombres del contacto");contacto.setNombres(sc.next());
                        System.out.printf("%n %5s","Apellido paterno del contacto");contacto.setApellidoPaterno(sc.next());
                        System.out.printf("%n %5s","Apellido materno del contacto");contacto.setApellidoMaterno(sc.next());
                        System.out.printf("%n %5s","..::Ingrese fono Pasajero");String fono=sc.next();
                        System.out.printf("%n %5s %n","..::Ingrese fono contacto");String fonocontacto=sc.next();

                        svp.createPasajero(Pasaporte.of(numeroPasaporte, nacionalidad), newPasajero, fono, contacto, fonocontacto);


                    }

                    svp.vendePasaje(IdDocumento, tipoDocumento, fechaV, horaV, patBus, numAsientos[i], Pasaporte.of(numeroPasaporte, nacionalidad));
                    break;
            }
        }
        // despues de finalizar la venta de pasajes, suponiendo todos exitosos
        // todo Dar el monto de la venta


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

//separe numero de comas
    private int [] separador(String x,int cant){
        char[] tochar = x.toCharArray();
        int [] toint = new int[cant];

        String num = "";
        int contador = 0;
        for (int i=0;i<tochar.length;i++){
            if (tochar[i]==','){
                toint[contador] = Integer.parseInt(num);
                contador++;
            num="";
            }else{num=""+tochar[i];}
        }
        return toint;
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
    


