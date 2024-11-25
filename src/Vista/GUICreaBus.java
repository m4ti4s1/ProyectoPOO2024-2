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
        try {
            String patente = patentetx.getText();
            String marca = marcatx.getText();
            String modelo = modelotx.getText();
            int numAsientos = Integer.parseInt(numAsientostx.getText());
            Rut rut = Rut.of(comboBoxRut.getSelectedItem() + "");
            ControladorEmpresas.getInstance().createBus(patente, marca, modelo, numAsientos, rut);
        }catch (SVPException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
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

}
