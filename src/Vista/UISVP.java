package Vista;

import Utilidades.*;
import Controlador.*;
import Modelo.*;

import Excepciones.SistemaVentaPasajesExcepcion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UISVP {
    private static UISVP INSTANCE;
    private Scanner sc;

    private UISVP() {
        sc = new Scanner(System.in);
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085]|\t|,");
    }

    public static UISVP getInstance() {
        if (INSTANCE == null) {
            return new UISVP();
        }
        return INSTANCE;
    }

    private static ControladorEmpresas CE = ControladorEmpresas.getInstance();
    private static SistemaVentaPasajes SVP = SistemaVentaPasajes.getInstance();

    public void menu() {
        int opcion;
        do {
            System.out.println("============================");
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
            opcion = sc.nextInt();
            System.out.println();

            if (opcion < 1 || opcion > 14) {
                System.out.println("El número de opción ingresado no es válido");
            }

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
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 14);
    }

    private void createEmpresa() {
        try {
            System.out.println("...:::: Creando una nueva Empresa ::::....");
            System.out.print("R.U.T : ");
            String rut = sc.next();
            System.out.print("\nNombre : ");
            String nom = sc.next();
            System.out.print("\nurl : ");
            String url = sc.next();
            System.out.println();
            CE.createEmpresa(Rut.of(rut), nom, url);
            System.out.println("...:::: Empresa guardada exitosamente ::::...");
        } catch (Excepciones.SistemaVentaPasajesExcepcion e) {
            System.err.println(e.getMessage());
        }
    }

    private void contrataTripulante() {
    }

    private void createTerminal() {
    }

    private void createCliente() {
    }

    private void createBus() {
    }

    private void createViaje() {
    }

    private void vendePasaje() {
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

            System.out.printf("%30s : ", "Fecha del viaje[dd/mm/yyyy]");
            String fechaDelViaje = sc.next();
            LocalDate fecha = LocalDate.parse(fechaDelViaje, dateFormatter);

            System.out.printf("%30s : ", "Hora del viaje[hh:mm]");
            String horaDelViaje = sc.next();
            LocalTime hora = LocalTime.parse(horaDelViaje, timeFormatter);

            System.out.printf("%30s : ", "Patente bus");
            String patenteBus = sc.next();

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
            System.out.printf(" | %-16s | %-10s    | %-20s    | %-2s | %-2s | %-2s |%n",
                    lista[i][0], lista[i][1], lista[i][2], lista[i][3], lista[i][4], lista[i][5]);
            System.out.printf(" *--------------*-------------------------------*-------------------------------*------------------*------------*-------------*%n");
        }
    }

    private void listLlegadasSalidasTerminal() {
        try {
            System.out.println("...:::: Listado de llegadas y salidas de un terminal ::::....\n");

            System.out.println("Nombre terminal : ");
            String nombreTerminal = sc.next();

            System.out.println("Fecha[dd/mm/yyyy] : ");
            String fecha = sc.next();
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
            System.out.println("R.U.T : ");
            String rut = sc.next();
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
}
