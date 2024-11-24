package Vista;

import Controlador.ControladorEmpresas;

import javax.swing.*;
import java.awt.event.*;
import java.util.zip.ZipEntry;

public class GUICreaBus extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField patentetx;
    private JComboBox comboBoxRut;
    private JComboBox comboBoxNombre;

    public GUICreaBus() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        String[][]empresa=ControladorEmpresas.getInstance().listEmpresas();
        for (int i=0;i< empresa.length;i++){
            System.out.println(empresa[i][1]);
            comboBoxNombre.addItem(empresa[i][1]);
            comboBoxRut.addItem(empresa[i][0]);
        }
        ordenarRut(empresa, comboBoxRut, comboBoxNombre);
        ordenarRut(empresa, comboBoxNombre, comboBoxRut);
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


        dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void displayCreaBus() {
        GUICreaBus dialog = new GUICreaBus();
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }

    public static void ordenarRut(String[][] empresas, JComboBox<String> comboBox1, JComboBox<String> comboBox2) {
        // Escuchar cambios de selección en comboBox1 (Nombre)
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox1.getSelectedItem();

                    // Buscar el RUT correspondiente al nombre seleccionado
                    for (int i = 0; i < empresas.length; i++) {
                        if (empresas[i][1].equalsIgnoreCase(seleccion)) {
                            comboBox2.setSelectedItem(empresas[i][0]); // Asignar el RUT en comboBox2
                            break;
                        }
                    }
                }
            }
        });

        // Escuchar cambios de selección en comboBox2 (RUT)
        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox2.getSelectedItem();

                    // Buscar el nombre correspondiente al RUT seleccionado
                    for (int i = 0; i < empresas.length; i++) {
                        if (empresas[i][0].equalsIgnoreCase(seleccion)) {
                            comboBox1.setSelectedItem(empresas[i][1]); // Asignar el nombre de la empresa en comboBox1
                            break;
                        }
                    }
                }
            }
        });
    }

}
