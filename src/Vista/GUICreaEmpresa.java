package Vista;

import Controlador.ControladorEmpresas;
import Excepciones.SVPException;
import Utilidades.Rut;

import javax.swing.*;
import java.awt.event.*;

public class GUICreaEmpresa extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField ruttx;
    private JTextField nombretx;
    private JTextField urltx;
    private JLabel advertencia;
    private JLabel advertencianom;

    public GUICreaEmpresa() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        advertencia.setVisible(false);
        ruttx.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                VerificacionRut();
            }
        });
        advertencianom.setVisible(false);

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
        if(verificarllenado()){
            JOptionPane.showMessageDialog(null,"Aún Faltan Campos por llenar");
         return;
        }
        if(!(ruttx.getText()).matches("\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]")){
            JOptionPane.showMessageDialog(null,"Error en el formato del rut(xx.xxx.xxx-x)");
            return;
        }
        try {
            String rut = ruttx.getText();
            String nom = nombretx.getText();
            String Url = urltx.getText();
            ControladorEmpresas.getInstance().createEmpresa(Rut.of(rut), nom, Url);
            JOptionPane.showMessageDialog(this, "Empresa guardada Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);

        } catch (SVPException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUICreaEmpresa dialog = new GUICreaEmpresa();
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private void VerificacionRut() {
        String input = ruttx.getText();
        advertencia.setVisible(!input.matches("\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]"));

    }

    private boolean verificarllenado(){
        return ruttx.getText().trim().isEmpty() ||
                nombretx.getText().trim().isEmpty() ||
                urltx.getText().trim().isEmpty() ;
    }
}
