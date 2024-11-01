package Vista;

import Modelo.*;
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
        try {
            DateTimeFormatter fDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter fTime = DateTimeFormatter.ofPattern("HH:mm");

            System.out.println("        ...:::: Venta de pasajes ::::....");
            System.out.println();
            System.out.println();
            System.out.println(":::: Datos de la Venta");

            System.out.print("             ID Documento : ");
            String idDocumento = sc.next();

            System.out.print("Tipo Documento: [1] Boleta [2] Factura : ");
            int tipoDocumento = sc.nextInt();
            TipoDocumento tipo = switch (tipoDocumento) {
                case 1 -> TipoDocumento.BOLETA;
                case 2 -> TipoDocumento.FACTURA;
                default -> null;
            };
            System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
            String fechaString = sc.next();
            LocalDate fecha = LocalDate.parse(fechaString, fDate);

            System.out.print("          Origen (comuna) : ");
            String origen = sc.next();

            System.out.print("         Destino (comuna) : ");
            String destino = sc.next();
            System.out.println(); //salto de linea

            //DATOS DEL CLIENTE
            System.out.println(":::: Datos del cliente");
            System.out.println(); // salto de linea
            System.out.print("    Rut[1] o Pasaporte[2] : ");
            int opcion = sc.nextInt();

            IdPersona idPersona = null;
            if (opcion == 1) {
                System.out.print("                    R.U.T : ");
                String rutConDv = sc.next();
                idPersona = Rut.of(rutConDv);
                System.out.println("      Nombre Cliente : " + SVP.getNombreCliente(idPersona));

            } else if (opcion == 2) {
                System.out.print("       Pasaporte : ");
                String numero = sc.next();
                System.out.print("     Nacionalidad : ");
                String nacionalidad = sc.next();
                idPersona = Pasaporte.of(numero, nacionalidad);
                System.out.println("       Nombre Cliente : " + SVP.getNombreCliente(idPersona));
            }
            System.out.println(); //salto de linea


            System.out.println(":::: Pasajes a vender");
            System.out.print("      Cantidad de pasajes : ");
            int cantidadPasajes = sc.nextInt();

            //VALIDA SI SE PUEDE INICIAR LA VENTA
            SVP.iniciaVenta(idDocumento, tipo, fecha, origen, destino, idPersona, cantidadPasajes);
            String[][] horariosDisponibles = SVP.getHorariosDisponibles(fecha, origen, destino, cantidadPasajes);
            if (horariosDisponibles.length == 0) {
                System.out.println("No hay horarios disponibles en la fecha consultada");
            } else {
                System.out.println(":::: Listado de horarios disponibles");
            }

            System.out.println("     *----------*----------*----------*----------*");
            System.out.println("     | BUS      |   SALIDA |    VALOR | ASIENTOS |");
            System.out.println("     |----------+----------+----------+----------|");
            for (int i = 0; i < horariosDisponibles.length; i++) {
                horariosDisponibles[i][0] = horariosDisponibles[i][0].substring(0, 2) + horariosDisponibles[i][0].substring(2, 4) + horariosDisponibles[i][0].substring(4, 6);
                System.out.printf("%4d | %-9s|%9s |%9s |%9s | %n", i + 1, horariosDisponibles[i][0], horariosDisponibles[i][1], horariosDisponibles[i][2], horariosDisponibles[i][3]);
                if (i == horariosDisponibles.length - 1) {
                    System.out.println("     *----------*----------*----------*----------*");
                } else {
                    System.out.println("     |----------+----------+----------+----------|");
                }
            }


            String[][] viajes = SVP.listViajes();
            System.out.print("Seleccione viaje en [1.." + SVP.listViajes().length + "] : ");
            int opcionViaje = sc.nextInt() - 1;
            String[] viaje = viajes[opcionViaje];


            LocalDate fechaParseada = LocalDate.parse(viaje[0]);
            LocalTime horaParseada = LocalTime.parse(viaje[1]);
            String patenteParseada = viaje[5];



            System.out.println(":::: Asientos disponibles para el viaje seleccionado");
            String[][] asientosDisponibles = new String[][]{SVP.listAsientosDeViaje(fechaParseada, horaParseada, patenteParseada)};

            System.out.println("*---*---*---*---*---*");
            for (int i = 0; i < asientosDisponibles.length; i++) {
                System.out.printf("| %-2s| %-2s|   | %-2s| %-2s|%n", asientosDisponibles[i][0], asientosDisponibles[i][1], asientosDisponibles[i][2], asientosDisponibles[i][3]);
                if (i == 0) {
                    System.out.println("*---*---*---*---*---*");
                } else {
                    System.out.println("|---+---+---+---+---+");
                }
            }


            int[] asientosSeleccionados = new int[cantidadPasajes];


            if (cantidadPasajes == 1) {
                System.out.print("Seleccione su asiento : ");
                int asiento = sc.nextInt();
                asientosSeleccionados[0] = asiento;

            } else {
                System.out.print("Seleccione sus asientos [separe por ,] : ");
                String asientos = sc.next();
                String[] partes = asientos.split(",");

                for (int i = 0; i < cantidadPasajes; i++) {
                    if (i < partes.length) {
                        asientosSeleccionados[i] = Integer.parseInt(partes[i].trim());
                    } else {
                        // Si hay menos asientos ingresados de los necesarios, se asigna un valor predeterminado
                        asientosSeleccionados[i] = -1; // o cualquier otro valor predeterminado que prefieras
                    }
                }
            }
            System.out.println(); //salto de linea
            sc.nextLine();//limpiar buffer

            for (int i = 0; i < cantidadPasajes; i++) {
                int nroPasajero = i + 1;
                System.out.println(":::: Datos pasajero " + nroPasajero);
                System.out.print("    Rut [1] o Pasaporte[2] : ");
                int tipoPasajero = sc.nextInt();
                IdPersona idPasajero = null;
                if (tipoPasajero == 1) {
                    System.out.print("                     R.U.T : ");
                    String rutConDv = sc.next();
                    idPasajero = Rut.of(rutConDv);
                } else if (tipoPasajero == 2) {
                    System.out.print("Pasaporte : ");
                    String numero = sc.next();
                    System.out.print("Nacionalidad : ");
                    String nacionalidad = sc.next();
                    idPasajero = Pasaporte.of(numero, nacionalidad);
                }
                System.out.println(); //salto de linea

                int asientoEscogido = asientosSeleccionados[i];

                String existePasajero = SVP.getNombrePasajero(idPasajero).orElse(null);
                if (existePasajero == null) {

                    //INFORMACION DEL PASAJERO

                    System.out.println("::: INFORMACION PASAJERO");
                    System.out.println(); //salto de linea

                    System.out.print("      Sr.[1] o Sra. [2] : ");
                    int valorTratamientoPasajero = sc.nextInt();
                    sc.nextLine();

                    Tratamiento tratamientoPasajero = switch (valorTratamientoPasajero) {
                        case 1 -> Tratamiento.SR;
                        case 2 -> Tratamiento.SRA;
                        default -> null;
                    };

                    System.out.print("                Nombres : ");
                    String nombresPasajero = sc.nextLine();

                    System.out.print("       Apellido Paterno : ");
                    String apellidoPaterPasajero = sc.next();

                    System.out.print("       Apellido Materno : ");
                    String apellidoMaterPasajero = sc.next();

                    System.out.print("         Telefono movil : ");
                    String numeroTelefonoPasajero = sc.next();


                    Nombre nomPasajero = new Nombre();
                    nomPasajero.setTratamiento(tratamientoPasajero);
                    nomPasajero.setNombres(nombresPasajero);
                    nomPasajero.setApellidoPaterno(apellidoPaterPasajero);
                    nomPasajero.setApellidoMaterno(apellidoMaterPasajero);



                    System.out.println(); //salto de linea
                    System.out.println(":::: INFORMACION CONTACTO");
                    System.out.println(); //salto de linea

                    System.out.print("      Sr.[1] o Sra. [2] : ");
                    int valorTratamientoContacto = sc.nextInt();// guarda la opcion de si es Sr o Sra
                    sc.nextLine();

                    Tratamiento tratamientoContacto = switch (valorTratamientoContacto) {
                        case 1 -> Tratamiento.SR;
                        case 2 -> Tratamiento.SRA;
                        default -> null;
                    };

                    System.out.print("Nombres : ");
                    String nombresContacto = sc.nextLine();

                    System.out.print("       Apellido Paterno : ");
                    String apellidoPaterContacto = sc.next();

                    System.out.print("       Apellido Materno : ");
                    String apellidoMaterContacto = sc.next();

                    System.out.print("         Telefono movil : ");
                    String numeroTelefonoContacto = sc.next();
                    System.out.println(); //salto de linea


                    Nombre nomContacto = new Nombre();
                    nomContacto.setTratamiento(tratamientoContacto);
                    nomContacto.setNombres(nombresContacto);
                    nomContacto.setApellidoPaterno(apellidoPaterContacto);
                    nomContacto.setApellidoMaterno(apellidoMaterContacto);


                    SVP.createPasajero(idPasajero, nomPasajero, numeroTelefonoPasajero, nomContacto, numeroTelefonoContacto);
                    System.out.println(":::: Pasaje agregado exitosamente ");

                    System.out.println(); //salto de linea

                    SVP.vendePasaje(idDocumento, tipo, fechaParseada, horaParseada, patenteParseada, asientoEscogido, idPasajero);
                    System.out.println();

                    int TotalVenta = SVP.getMontoVenta(idDocumento, tipo).get();
                    if (i == cantidadPasajes - 1) {
                        System.out.println(":::: Monto total de la venta: " + TotalVenta);
                        System.out.println(); //salto de linea

                        pagaVentaPasajes(idDocumento, tipo);
                        System.out.println();

                    }
                }
            }

            String[] pasajesImpresion = SVP.pasajesAImprimir(idDocumento, tipo);

            for (String s : pasajesImpresion) {
                System.out.println(s);
            }

        } catch (InputMismatchException e) {
            System.out.println("Entrada no valida, ingrese un dato valido");
            sc.nextLine();
        }
    }
    private void listVentas() {
        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[][] listaVentas = SVP.listVentas();

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

        System.out.printf("\n%44s\n", "...:::: Listado de Viajes ::::....\n");
        String[][] lista= SVP.listViajes();

        System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");
        System.out.printf(" | FECHA        |    HORA SALE |    HORA LLEGA | PRECIO | ASIENTOS DISP. | PATENTE       | ORIGEN         | DESTINO         |%n");
        System.out.printf(" *--------------*--------------*---------------*--------*----------------*---------------*----------------*-----------------*%n");

        for (String[] strings : lista) {
            String fechaOriginal = strings[0];
            LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
            String fechaFormateada = fecha.format(nuevoFormato);
            System.out.printf(" | %-10s   |       %-6s |       %-6s | %-8s |           %-2s | %-8s | %-8s         | %-8s         |%n",
                    fechaFormateada, strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
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

    private void pagaVentaPasajes(String idDocumento, TipoDocumento tipo) {}

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
