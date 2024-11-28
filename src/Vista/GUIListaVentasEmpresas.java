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

    public static void main(String[] args) {
        GUIListaVentasEmpresas dialog = new GUIListaVentasEmpresas();
        dialog.pack();
        dialog.setVisible(true);

    }

    //--------------------------------------------------------------------------------------------------------
    public static void ordenarRut(String[][] empresas, JComboBox<String> comboBox1, JComboBox<String> comboBox2) {

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox1.getSelectedItem();

                    for (int i = 0; i < empresas[0].length; i++) {
                        if (empresas[0][i].equalsIgnoreCase(seleccion)) {
                            comboBox2.setSelectedItem(empresas[1][i]);
                            break;
                        }
                    }
                }
            }
        });

        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox2.getSelectedItem();
                    for (int i = 0; i < empresas[1].length; i++) {
                        if (empresas[1][i].equalsIgnoreCase(seleccion)) {
                            comboBox1.setSelectedItem(empresas[0][i]);
                            break;
                        }
                    }
                }
            }
        });
    }
}
