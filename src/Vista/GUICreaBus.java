package Vista;

import Controlador.ControladorEmpresas;
import Excepciones.SVPException;
import Utilidades.Rut;

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
    private JTextField marcatx;
    private JTextField modelotx;
    private JTextField numAsientostx;
    private JLabel advertencia;

    public GUICreaBus() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        String[][] empresa = ControladorEmpresas.getInstance().listEmpresas();
        for (int i = 0; i < empresa.length; i++) {
            comboBoxRut.addItem(empresa[i][0]);
            comboBoxNombre.addItem(empresa[i][1]);
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
        advertencia.setVisible(false);
        numAsientostx.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                verificarNum();
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


        if (entradasCorrectas()) {
            JOptionPane.showMessageDialog(this, "Aún faltan campos por llenar");
           return;
        }

        try {
            String patente = patentetx.getText();
            String marca = marcatx.getText();
            String modelo = modelotx.getText();
            int numAsientos = Integer.parseInt(numAsientostx.getText());
            Rut rut = Rut.of(comboBoxRut.getSelectedItem() + "");
            ControladorEmpresas.getInstance().createBus(patente, marca, modelo, numAsientos, rut);
            JOptionPane.showMessageDialog(null,"Se ah creado el Bus con éxito");
        } catch (SVPException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         return;
        }
        dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUICreaBus dialog = new GUICreaBus();
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }

    private static void ordenarRut(String[][] empresas, JComboBox<String> comboBox1, JComboBox<String> comboBox2) {

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) comboBox1.getSelectedItem();
                    for (int i = 0; i < empresas.length; i++) {
                        if (empresas[i][1].equalsIgnoreCase(seleccion)) {
                            comboBox2.setSelectedItem(empresas[i][0]);
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

                    for (int i = 0; i < empresas.length; i++) {
                        if (empresas[i][0].equalsIgnoreCase(seleccion)) {
                            comboBox1.setSelectedItem(empresas[i][1]);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void verificarNum() {
        String input = numAsientostx.getText();
        boolean verificarnumeros = !input.matches("\\d+");
        advertencia.setVisible(verificarnumeros);
    }

    private boolean entradasCorrectas() {
        return patentetx.getText().trim().isEmpty() ||
                marcatx.getText().trim().isEmpty() ||
                modelotx.getText().trim().isEmpty() ||
                numAsientostx.getText().trim().isEmpty();
    }
}
