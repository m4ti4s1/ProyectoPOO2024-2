package Vista;

import Controlador.ControladorEmpresas;

import javax.swing.*;
import java.awt.event.*;

public class GUIContrataTripulante extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JRadioButton conductorRadioButton;
    private JRadioButton auxiliarRadioButton;
    private JRadioButton rUTRadioButton;
    private JTextField textField1;
    private JRadioButton pasaporteRadioButton;
    private JComboBox comboBox3;
    private JTextField textField2;
    private JComboBox comboBox4;

    public GUIContrataTripulante() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        String[][] empresas= ControladorEmpresas.getInstance().listEmpresas();
        ordenarRut(empresas,comboBox1,comboBox2);
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
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GUIContrataTripulante dialog = new GUIContrataTripulante();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
    public static void ordenarRut(String[][] empresas, JComboBox<String> comboBox1, JComboBox<String> comboBox2) {
        // Escuchar cambios de selección en comboBox1
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox1.getSelectedItem();

                    // Buscar el RUT correspondiente al nombre seleccionado
                    for (int i = 0; i < empresas[0].length; i++) {
                        if (empresas[0][i].equalsIgnoreCase(seleccion)) {
                            comboBox2.setSelectedItem(empresas[1][i]);
                            break;
                        }
                    }
                }
            }
        });

        // Escuchar cambios
        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox2.getSelectedItem();
                    // Buscar el nombre correspondiente al RUT seleccionado
                    for (int i = 0; i < empresas[1].length; i++) {
                        if (empresas[1][i].equalsIgnoreCase(seleccion)) {
                            // Asigna el nombre de la empresa
                            comboBox1.setSelectedItem(empresas[0][i]);
                            break;
                        }
                    }
                }
            }
        });
    }
}
