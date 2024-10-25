package Vista;

import Controlador.ControladorEmpresas;
import Controlador.SistemaVentaPasajes;
import Utilidades.Rut;

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
    private void listPasajeros(){}
    private void listEmpresas(){}
    private void listLlegadasSalidasTerminal(){}
    private void listVentasEmpresas(){}
}
