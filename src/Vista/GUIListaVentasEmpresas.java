package Vista;

import Controlador.ControladorEmpresas;
import Excepciones.SVPException;
import Utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class GUIListaVentasEmpresas extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBoxRut;
    private JComboBox comboBoxnom;
    private JTable listaVentas;
    private JScrollPane scrollpane;

    public GUIListaVentasEmpresas() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        String[][] Empresas = ControladorEmpresas.getInstance().listEmpresas();

        for (int i = 0; i < Empresas.length; i++) {
            comboBoxnom.addItem(Empresas[i][1]);
            comboBoxRut.addItem(Empresas[i][0]);
        }
        ordenarRut(Empresas, comboBoxRut, comboBoxnom);

        listaVentas.setModel(new DefaultTableModel());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            if (comboBoxRut.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una empresa");
                return;
            }
            String[][] ventasEmpresaX = ControladorEmpresas.getInstance().listVentasEmpresa(Rut.of(comboBoxRut.getSelectedItem() + ""));
            if (ventasEmpresaX.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay Ventas Registradas");
                return;
            }
            String[] columnaName = {"Fecha", "Tipo", "Monto Pagado", "Tipo Pago"};
            listaVentas.setModel(new DefaultTableModel(ventasEmpresaX, columnaName));
        }catch (SVPException e){
            JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUIListaVentasEmpresas dialog = new GUIListaVentasEmpresas();
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }

    //--------------------------------------------------------------------------------------------------------
    private static void ordenarRut(String[][] empresas, JComboBox<String> comborut, JComboBox<String> combonom) {


        comborut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) comborut.getSelectedItem();
                combonom.setSelectedItem(null);
                for (int i = 0; i < empresas.length; i++) {
                    if (empresas[i][0].equalsIgnoreCase(seleccion)) {
                        combonom.setSelectedItem(empresas[i][1]);
                        break;
                    }
                }
            }
        });


        combonom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) combonom.getSelectedItem();
                comborut.setSelectedItem(null);
                for (int i = 0; i < empresas.length; i++) {
                    if (empresas[i][1].equalsIgnoreCase(seleccion)) {
                        comborut.setSelectedItem(empresas[i][0]);
                        break;
                    }
                }
            }
        });




    }
}
