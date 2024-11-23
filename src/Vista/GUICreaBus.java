package Vista;

import Controlador.ControladorEmpresas;

import javax.swing.*;
import java.awt.event.*;

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
        jcombox();

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
    private void jcombox(){
        String[][] empresas=ControladorEmpresas.getInstance().listEmpresas();
        for (int i=0;i<empresas.length;i++){
            comboBoxNombre.addItem(empresas[i][1]);
            comboBoxRut.addItem(empresas[i][0]);
        }

    }
}
