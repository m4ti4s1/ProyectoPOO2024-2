package Vista;

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
    Scanner sc=new Scanner(System.in);
    public void menu(){}
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
