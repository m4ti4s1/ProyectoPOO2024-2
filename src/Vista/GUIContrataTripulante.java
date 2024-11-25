package Vista;

import Controlador.ControladorEmpresas;
import Excepciones.SVPException;
import Utilidades.*;

import javax.swing.*;
import java.awt.event.*;

public class GUIContrataTripulante extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBoxrut;
    private JComboBox comboBoxnom;
    private JRadioButton conductorRadioButton;
    private JRadioButton auxiliarRadioButton;
    private JRadioButton RUT;
    private JTextField numDoc;
    private JRadioButton PASAPORTE;
    private JComboBox Nacionalidad;
    private JTextField nomtx;
    private JComboBox Comuna;
    private JRadioButton Sr;
    private JRadioButton Sra;
    private JTextField paternotx;
    private JTextField maternotx;
    private JTextField calle;
    private JTextField numCalle;
    private String TipoTripulante;
    private Tratamiento tratamiento;
    IdPersona idPersona=null;

    public GUIContrataTripulante() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        String[][] empresas= ControladorEmpresas.getInstance().listEmpresas();
        for(int i=0;i<empresas.length;i++){
            comboBoxrut.addItem(empresas[i][0]);
            comboBoxnom.addItem(empresas[i][1]);
        }
        ordenarRut(empresas, comboBoxrut, comboBoxnom);
        ButtonGroup grupotripulante=new ButtonGroup();
        grupotripulante.add(conductorRadioButton);
        grupotripulante.add(auxiliarRadioButton);
        conductorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TipoTripulante="Tripulante";
            }
        });
        auxiliarRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TipoTripulante="Auxiliar";
            }
        });
        //selecion tratamiento
        ButtonGroup grupoTratamiento=new ButtonGroup();
        grupoTratamiento.add(Sr);
        grupoTratamiento.add(Sra);
        Sr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tratamiento=Tratamiento.valueOf("SR");
            }
        });
        Sra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tratamiento=Tratamiento.valueOf("SRA");
            }
        });
        //crea rut o pasaporte
        ButtonGroup tipodoc=new ButtonGroup();
        tipodoc.add(RUT);
        tipodoc.add(PASAPORTE);

        RUT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            idPersona=Rut.of(numDoc.getText());
            }
        });
        PASAPORTE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idPersona= Pasaporte.of(numDoc.getText(),Nacionalidad.getSelectedItem()+"");
            }
        });
        //

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
        Nombre nombre=new Nombre();
        nombre.setTratamiento(tratamiento);
        nombre.setNombres(nomtx.getText());
        nombre.setApellidoPaterno(paternotx.getText());
        nombre.setApellidoMaterno(maternotx.getText());
        Direccion direccion = new Direccion(calle.getText(), Integer.parseInt(numCalle.getText()), Comuna.getSelectedItem()+"");
        try{
            Rut rutEmpresa=Rut.of(comboBoxrut.getSelectedItem()+"");
            switch (TipoTripulante.toUpperCase()) {
                case "AUXILIAR":
                    ControladorEmpresas.getInstance() .hireAuxiliarForEmpresa(rutEmpresa, idPersona, nombre, direccion);
                   JOptionPane.showMessageDialog(this,"Auxiliar Creado Exitosamente ","Creacion Auxiliar",JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "TRIPULANTE":
                    ControladorEmpresas.getInstance().hireConductorForEmpresa(rutEmpresa, idPersona, nombre, direccion);
                    JOptionPane.showMessageDialog(this,"Tripulante Creado Exitosamente ","Creacion Tripulante",JOptionPane.INFORMATION_MESSAGE);

                    break;
            }
        }catch (SVPException e){
            JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);

        }
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

    }
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
