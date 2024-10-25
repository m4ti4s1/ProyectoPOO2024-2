package Vista;

import Controlador.ControladorEmpresas;
import Controlador.SistemaVentaPasajes;
import Excepciones.SistemaVentaPasajesExcepcion;
import Utilidades.Rut;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UISVP {
    private static UISVP INSTANCE;

    private UISVP() {
    }
    public static UISVP getInstance(){
        if(INSTANCE==null){
        return  new UISVP();}
        return INSTANCE;
    }
    private static ControladorEmpresas CE=ControladorEmpresas.getInstance();
    private static SistemaVentaPasajes SVP=SistemaVentaPasajes.getInstance();
    Scanner sc=new Scanner(System.in);
    public void menu(){
        try{
            System.out.println("...:::: Creando una nueva Empresa ::::....");
            System.out.print("R.U.T : ");
            String rut= sc.next();
            System.out.print("\nNombre : ");
            String nom= sc.next();
            System.out.print("\nurl : ");
            String url= sc.next();
            System.out.println();
            CE.createEmpresa(Rut.of(rut),nom,url);
            System.out.println("...:::: Empresa guardada exitosamente ::::...");
        }catch (Excepciones.SistemaVentaPasajesExcepcion e){
            System.err.println(e.getMessage());
        }
    }
    private void createEmpresa(){}
    private void contrataTripulante(){}
    private void createTerminal(){}
    private void createCliente(){}
    private void createBus(){}
    private void createViaje(){}
    private void vendePasaje(){}
    private void pagaVentaPasajes(){}
    private void listVentas(){}
    private void listViajes(){}
    private void listPasajerosViaje(){}

    private void listEmpresas(){
        System.out.println("...:::: Listado de empresas ::::....");
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

    private void listLlegadasSalidasTerminal(){
        try {
            System.out.println("...:::: Listado de llegadas y salidas de un terminal ::::....");

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
        } catch (SistemaVentaPasajesExcepcion e){
            System.err.println(e.getMessage());
        }
    }

    private void listVentasEmpresas(){
        try {
            System.out.println("...:::: Listado de ventas de una empresa ::::....");
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
        } catch (SistemaVentaPasajesExcepcion e){
            System.err.println(e.getMessage());
        }
    }
}
