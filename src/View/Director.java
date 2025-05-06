/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.CoordinadorDeActividadesController;
import Controller.EnfermeraController;
import Controller.GuardiaController;
import Controller.PersonalDeControlController;
import DAO.CoordinadorDeActividadesDAO;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import Model.Constants.RolEnum;
import View.FrmCamara;
import DAO.EnfermeraDAO;
import DAO.GuardiaDAO;
import Model.Constants.RolEnum;
import Model.Entities.EmailSender;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import Model.Entities.Guardia;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.util.List;
import Model.Entities.Enfermera;
import com.toedter.calendar.JDateChooser;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Sharlok
 */
public class Director extends javax.swing.JFrame {

    EnfermeraDAO enfermeraDAO = new EnfermeraDAO();
    private EnfermeraController enfermeraController;

    private BufferedImage imagenCapturadaModificacion;
    private CoordinadorDeActividadesController coordinadorController;
    private GuardiaController guardiaController;
    private String rutaImagenSeleccionada = "";

    private File imagenSeleccionadaMod;
    private File rutaImagenEnfermera;
    private PersonalDeControlController controller;
    private File imagenPDCSeleccionada;
    private String cedulaActualModificacion;

    public Director() {
        initComponents();

        // Configuración inicial de componentes
        txtFechaContratacion.setEditable(false);
        txtFechaContratacion.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtFechaContratacion1.setEditable(false);
        txtFechaContratacionMod1.setEditable(false);
        txtSexo.setEditable(false);
        txtSexo.setText("Femenino");
        txtFechaContratacion1.setText(LocalDate.now().toString());
        ToolTipManager.sharedInstance().setInitialDelay(10);

        // Inicialización de controladores
        guardiaController = GuardiaController.getInstancia();
        enfermeraController = EnfermeraController.getInstancia();
        coordinadorController = CoordinadorDeActividadesController.getInstancia();

        // Configuración MEJORADA del JDateChooser
    jDateChooserFinContrato = new JDateChooser();
    jDateChooserFinContrato.setDateFormatString("dd/MM/yyyy");
    
    // Establecer fecha mínima (mañana)
    Calendar calendario = Calendar.getInstance();
    calendario.add(Calendar.DAY_OF_MONTH, 1); // Fecha mínima = mañana
    jDateChooserFinContrato.setMinSelectableDate(calendario.getTime());
    
    // Establecer fecha por defecto (opcional: 1 mes después de hoy)
    calendario.add(Calendar.MONTH, 1);
    jDateChooserFinContrato.setDate(calendario.getTime());
    
    // Debug de inicialización
    System.out.println("=== DEBUG INICIALIZACIÓN ===");
    System.out.println("JDateChooser inicializado: " + (jDateChooserFinContrato != null));
    System.out.println("Fecha inicial JDateChooser: " + jDateChooserFinContrato.getDate());
    System.out.println("Fecha mínima seleccionable: " + jDateChooserFinContrato.getMinSelectableDate());
    
    
        // Actualización de tablas
        actualizarTablaGuardias();
        actualizarTablaEnfermeras();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        Modificar = new javax.swing.JMenuItem();
        Eliminar = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        ModificarEnfermera = new javax.swing.JMenuItem();
        EliminarEnfermera = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Director = new javax.swing.JPanel();
        ListaDeGuardias = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaGuardias = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new RoundedPanel(30);
        jSeparator23 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        jSeparator27 = new javax.swing.JSeparator();
        jSeparator28 = new javax.swing.JSeparator();
        jSeparator29 = new javax.swing.JSeparator();
        jSeparator30 = new javax.swing.JSeparator();
        jSeparator32 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblImagen1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtFechaContratacion1 = new javax.swing.JTextField();
        dateFinContrato1 = new com.toedter.calendar.JDateChooser();
        jLabel33 = new javax.swing.JLabel();
        cmbTurno1 = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtCorreo1 = new javax.swing.JTextField();
        jSeparator33 = new javax.swing.JSeparator();
        jButton6 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtNacionalidad1 = new javax.swing.JTextField();
        txtSexo1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtCedula1 = new javax.swing.JTextField();
        txtEdad1 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtSegundoApellido1 = new javax.swing.JTextField();
        txtPrimerApellido1 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtSegundoNombre1 = new javax.swing.JTextField();
        txtPrimerNombre1 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        AñadirGuardia = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPrimerApellido = new javax.swing.JTextField();
        txtPrimerNombre = new javax.swing.JTextField();
        txtSegundoNombre = new javax.swing.JTextField();
        txtSegundoApellido = new javax.swing.JTextField();
        txtEdad = new javax.swing.JTextField();
        txtNacionalidad = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        txtSexo = new javax.swing.JTextField();
        jPanel3 = new RoundedPanel(30);
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtFechaContratacion = new javax.swing.JTextField();
        jDateChooserFinContrato = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        cmbCargo = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cmbTurno = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jSeparator11 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        MostrarEnfermeras = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEnfermeras = new javax.swing.JTable();
        ModificarNurse = new javax.swing.JPanel();
        jPanel10 = new RoundedPanel(30);
        jSeparator34 = new javax.swing.JSeparator();
        jSeparator35 = new javax.swing.JSeparator();
        jSeparator36 = new javax.swing.JSeparator();
        jSeparator37 = new javax.swing.JSeparator();
        jSeparator38 = new javax.swing.JSeparator();
        jSeparator39 = new javax.swing.JSeparator();
        jSeparator40 = new javax.swing.JSeparator();
        jSeparator41 = new javax.swing.JSeparator();
        jSeparator43 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        lblImagenMod1 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtFechaContratacionMod1 = new javax.swing.JTextField();
        dateFinContratoMod1 = new com.toedter.calendar.JDateChooser();
        jLabel48 = new javax.swing.JLabel();
        cmbTurnoMod1 = new javax.swing.JComboBox<>();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtCorreoMod1 = new javax.swing.JTextField();
        jSeparator44 = new javax.swing.JSeparator();
        jButton8 = new javax.swing.JButton();
        txtCorreo4 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtNacionalidadMod1 = new javax.swing.JTextField();
        txtSexoMod1 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txtCedulaMod1 = new javax.swing.JTextField();
        txtEdadMod1 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        txtSegundoApellidoMod1 = new javax.swing.JTextField();
        txtPrimerApellidoMod1 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txtSegundoNombreMod1 = new javax.swing.JTextField();
        txtPrimerNombreMod1 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        ModificarGuardia = new javax.swing.JPanel();
        jPanel4 = new RoundedPanel(30);
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        lblImagenMod = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtFechaContratacionMod = new javax.swing.JTextField();
        dateFinContratoMod = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        txtCargoMod = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cmbTurnoMod = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtCorreoMod = new javax.swing.JTextField();
        jSeparator22 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtNacionalidadMod = new javax.swing.JTextField();
        txtSexoMod = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtCedulaMod = new javax.swing.JTextField();
        txtEdadMod = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtSegundoApellidoMod = new javax.swing.JTextField();
        txtPrimerApellidoMod = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtSegundoNombreMod = new javax.swing.JTextField();
        txtPrimerNombreMod = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new RoundedPanel(30);
        jSeparator42 = new javax.swing.JSeparator();
        jSeparator45 = new javax.swing.JSeparator();
        jSeparator46 = new javax.swing.JSeparator();
        jSeparator47 = new javax.swing.JSeparator();
        jSeparator48 = new javax.swing.JSeparator();
        jSeparator49 = new javax.swing.JSeparator();
        jSeparator50 = new javax.swing.JSeparator();
        jSeparator51 = new javax.swing.JSeparator();
        jSeparator52 = new javax.swing.JSeparator();
        jSeparator53 = new javax.swing.JSeparator();
        jButton9 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        lblImagen2 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        txtFechaContratacion2 = new javax.swing.JTextField();
        dateFinContrato2 = new com.toedter.calendar.JDateChooser();
        jLabel62 = new javax.swing.JLabel();
        cmbCargo2 = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        cmbTurno2 = new javax.swing.JComboBox<>();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        txtCorreo2 = new javax.swing.JTextField();
        jSeparator54 = new javax.swing.JSeparator();
        jButton10 = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        txtNacionalidad2 = new javax.swing.JTextField();
        txtSexo2 = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtCedula2 = new javax.swing.JTextField();
        txtEdad2 = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        txtSegundoApellido2 = new javax.swing.JTextField();
        txtPrimerApellido2 = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        txtSegundoNombre2 = new javax.swing.JTextField();
        txtPrimerNombre2 = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new RoundedPanel(30);
        jSeparator55 = new javax.swing.JSeparator();
        jSeparator56 = new javax.swing.JSeparator();
        jSeparator57 = new javax.swing.JSeparator();
        jSeparator58 = new javax.swing.JSeparator();
        jSeparator59 = new javax.swing.JSeparator();
        jSeparator60 = new javax.swing.JSeparator();
        jSeparator61 = new javax.swing.JSeparator();
        jSeparator62 = new javax.swing.JSeparator();
        jSeparator63 = new javax.swing.JSeparator();
        jSeparator64 = new javax.swing.JSeparator();
        jButton11 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        lblImagenMod2 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        txtFechaContratacionMod2 = new javax.swing.JTextField();
        dateFinContratoMod2 = new com.toedter.calendar.JDateChooser();
        jLabel77 = new javax.swing.JLabel();
        txtCargoMod1 = new javax.swing.JComboBox<>();
        jLabel78 = new javax.swing.JLabel();
        cmbTurnoMod2 = new javax.swing.JComboBox<>();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txtCorreoMod2 = new javax.swing.JTextField();
        jSeparator65 = new javax.swing.JSeparator();
        jButton12 = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtNacionalidadMod2 = new javax.swing.JTextField();
        txtSexoMod2 = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        txtCedulaMod2 = new javax.swing.JTextField();
        txtEdadMod2 = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        txtSegundoApellidoMod2 = new javax.swing.JTextField();
        txtPrimerApellidoMod2 = new javax.swing.JTextField();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        txtSegundoNombreMod2 = new javax.swing.JTextField();
        txtPrimerNombreMod2 = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCDA = new javax.swing.JTable();
        jButton13 = new javax.swing.JButton();

        Modificar.setText("Modificar");
        Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(Modificar);

        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(Eliminar);

        ModificarEnfermera.setText("Modificar enfermera");
        ModificarEnfermera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarEnfermeraActionPerformed(evt);
            }
        });
        jPopupMenu2.add(ModificarEnfermera);

        EliminarEnfermera.setText("Eliminar enfermera");
        EliminarEnfermera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarEnfermeraActionPerformed(evt);
            }
        });
        jPopupMenu2.add(EliminarEnfermera);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Director.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.addTab("Director", Director);

        ListaDeGuardias.setBackground(new java.awt.Color(255, 255, 255));
        ListaDeGuardias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaGuardias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Nombre", "Apellido", "Edad", "Cedula", "Sexo", "Nacionalidad", "Correo", "Turno", "Cargo", "FechaContratacion", "FechaFinContrato"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaGuardias.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tablaGuardias);
        if (tablaGuardias.getColumnModel().getColumnCount() > 0) {
            tablaGuardias.getColumnModel().getColumn(0).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(1).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(2).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(3).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(4).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(5).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(6).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(7).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(8).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(9).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(10).setResizable(false);
            tablaGuardias.getColumnModel().getColumn(11).setResizable(false);
        }

        ListaDeGuardias.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 1080, 550));

        jTabbedPane1.addTab("ListaDeGuardias", ListaDeGuardias);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(29, 35, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jSeparator23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 310, 20));
        jPanel7.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 310, 20));
        jPanel7.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 20));
        jPanel7.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 310, 20));
        jPanel7.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 310, 20));
        jPanel7.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 310, 20));
        jPanel7.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 310, 20));
        jPanel7.add(jSeparator30, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 310, 20));
        jPanel7.add(jSeparator32, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton5.setText("Añadir");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel8.setBackground(new java.awt.Color(0, 0, 51));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel8.add(lblImagen1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 180));

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 190, 220));

        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Agregar Foto de la enfermera:");
        jPanel7.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, -1));

        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Fecha de contratación:");
        jPanel7.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, -1, 20));
        jPanel7.add(txtFechaContratacion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 350, 190, 30));
        jPanel7.add(dateFinContrato1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 410, 190, 30));

        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Fecha de finalización del contrato:");
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 390, -1, -1));

        cmbTurno1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel7.add(cmbTurno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 240, 30));

        jLabel35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Turno:");
        jPanel7.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Correo:");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtCorreo1.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo1.setBorder(null);
        txtCorreo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreo1FocusLost(evt);
            }
        });
        txtCorreo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreo1ActionPerformed(evt);
            }
        });
        txtCorreo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreo1KeyTyped(evt);
            }
        });
        jPanel7.add(txtCorreo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 240, 30));
        jPanel7.add(jSeparator33, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 310, 20));

        jButton6.setText("Seleccionar Imagen");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 290, -1, -1));

        jLabel37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Correo:");
        jPanel7.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Nacionalidad:");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 300, 90, -1));

        txtNacionalidad1.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad1.setBorder(null);
        txtNacionalidad1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad1KeyTyped(evt);
            }
        });
        jPanel7.add(txtNacionalidad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 210, 30));

        txtSexo1.setEditable(false);
        txtSexo1.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo1.setText("Femenino");
        txtSexo1.setBorder(null);
        jPanel7.add(txtSexo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 250, 30));

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Sexo:");
        jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Cedula:");
        jPanel7.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 30));

        txtCedula1.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula1.setBorder(null);
        txtCedula1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedula1ActionPerformed(evt);
            }
        });
        txtCedula1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedula1KeyTyped(evt);
            }
        });
        jPanel7.add(txtCedula1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 240, 30));

        txtEdad1.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad1.setBorder(null);
        txtEdad1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad1KeyTyped(evt);
            }
        });
        jPanel7.add(txtEdad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 250, 30));

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Edad:");
        jPanel7.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Segundo Apellido:");
        jPanel7.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 20));

        txtSegundoApellido1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido1.setBorder(null);
        txtSegundoApellido1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido1KeyTyped(evt);
            }
        });
        jPanel7.add(txtSegundoApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 180, 30));

        txtPrimerApellido1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido1.setBorder(null);
        txtPrimerApellido1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido1KeyTyped(evt);
            }
        });
        jPanel7.add(txtPrimerApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 190, 30));

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Primer Apellido:");
        jPanel7.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 20));

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Segundo nombre:");
        jPanel7.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 30));

        txtSegundoNombre1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre1.setBorder(null);
        txtSegundoNombre1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre1KeyTyped(evt);
            }
        });
        jPanel7.add(txtSegundoNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 180, 30));

        txtPrimerNombre1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre1.setBorder(null);
        txtPrimerNombre1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre1KeyTyped(evt);
            }
        });
        jPanel7.add(txtPrimerNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 200, 30));

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Primer nombre:");
        jPanel7.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 20));

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 820, 510));

        jTabbedPane1.addTab("AñadirEnfermera", jPanel6);

        AñadirGuardia.setBackground(new java.awt.Color(255, 255, 255));
        AñadirGuardia.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Primer nombre:");
        AñadirGuardia.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 27, -1, 20));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Segundo nombre:");
        AñadirGuardia.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, -1, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Primer Apellido:");
        AñadirGuardia.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, -1, 20));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Segundo Apellido:");
        AñadirGuardia.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, -1, 20));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Edad:");
        AñadirGuardia.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cedula:");
        AñadirGuardia.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 220, -1, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Sexo:");
        AñadirGuardia.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nacionalidad:");
        AñadirGuardia.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, -1, -1));

        txtPrimerApellido.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido.setBorder(null);
        txtPrimerApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtPrimerApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 200, 30));

        txtPrimerNombre.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre.setBorder(null);
        txtPrimerNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtPrimerNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 210, 30));

        txtSegundoNombre.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre.setBorder(null);
        txtSegundoNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, 190, 30));

        txtSegundoApellido.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido.setBorder(null);
        txtSegundoApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtSegundoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 190, 30));

        txtEdad.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad.setBorder(null);
        txtEdad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 250, 30));

        txtNacionalidad.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad.setBorder(null);
        txtNacionalidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtNacionalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, 210, 30));

        txtCedula.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula.setBorder(null);
        txtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaActionPerformed(evt);
            }
        });
        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaKeyTyped(evt);
            }
        });
        AñadirGuardia.add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, 240, 30));

        txtSexo.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo.setText("Femenino");
        txtSexo.setBorder(null);
        AñadirGuardia.add(txtSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 250, 30));

        jPanel3.setBackground(new java.awt.Color(29, 35, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 310, 20));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 310, 20));
        jPanel3.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 20));
        jPanel3.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 310, 20));
        jPanel3.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 310, 20));
        jPanel3.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 310, 20));
        jPanel3.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 310, 20));
        jPanel3.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 310, 20));
        jPanel3.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 310, 20));
        jPanel3.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton1.setText("Contratar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(lblFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 180));

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 190, 220));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Agregar Foto del guardia:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, -1, -1));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Fecha de contratación:");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, -1, 20));
        jPanel3.add(txtFechaContratacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 380, 190, 30));
        jPanel3.add(jDateChooserFinContrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 440, 190, 30));

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Fecha de finalización del contrato:");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        cmbCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Guardia jefe", "Guardia" }));
        jPanel3.add(cmbCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 250, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cargo:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        cmbTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel3.add(cmbTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 240, 30));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Turno:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Correo:");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtCorreo.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo.setBorder(null);
        txtCorreo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoFocusLost(evt);
            }
        });
        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoKeyTyped(evt);
            }
        });
        jPanel3.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 210, 30));
        jPanel3.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 310, 20));

        jButton2.setText("Seleccionar Imagen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 320, -1, -1));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pictures/icons8-attention-30.png"))); // NOI18N
        jLabel30.setToolTipText("Asegurese de ingresar correctamente la cedula, ya que este valor no podrá ser modificado.");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, -1, -1));

        AñadirGuardia.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 790, 480));

        jTabbedPane1.addTab("Añadir un guardia", AñadirGuardia);

        MostrarEnfermeras.setBackground(new java.awt.Color(255, 255, 255));
        MostrarEnfermeras.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaEnfermeras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Nombre", "Apellido", "Edad", "Cedula", "Sexo", "Nacionalidad", "Correo", "Turno", "Cargo", "FechaContratacion", "FechaFinContrato"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEnfermeras.setComponentPopupMenu(jPopupMenu2);
        jScrollPane2.setViewportView(tablaEnfermeras);
        if (tablaEnfermeras.getColumnModel().getColumnCount() > 0) {
            tablaEnfermeras.getColumnModel().getColumn(0).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(1).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(2).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(3).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(4).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(5).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(6).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(7).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(8).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(9).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(10).setResizable(false);
            tablaEnfermeras.getColumnModel().getColumn(11).setResizable(false);
        }

        MostrarEnfermeras.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1080, 560));

        jTabbedPane1.addTab("MostrarEnfermera", MostrarEnfermeras);

        ModificarNurse.setBackground(new java.awt.Color(255, 255, 255));
        ModificarNurse.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(29, 35, 51));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 310, 20));
        jPanel10.add(jSeparator35, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 310, 20));
        jPanel10.add(jSeparator36, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 20));
        jPanel10.add(jSeparator37, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 310, 20));
        jPanel10.add(jSeparator38, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 310, 20));
        jPanel10.add(jSeparator39, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 310, 20));
        jPanel10.add(jSeparator40, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 310, 20));
        jPanel10.add(jSeparator41, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 310, 20));
        jPanel10.add(jSeparator43, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton7.setText("Modificar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 180, 40));

        jPanel11.setBackground(new java.awt.Color(0, 0, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(lblImagenMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 180));

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 190, 220));

        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Modificar foto de la enfermera:");
        jPanel10.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, -1));

        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Fecha de contratación:");
        jPanel10.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 320, -1, 20));
        jPanel10.add(txtFechaContratacionMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 340, 190, 30));
        jPanel10.add(dateFinContratoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 400, 190, 30));

        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Fecha de finalización del contrato:");
        jPanel10.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 380, -1, -1));

        cmbTurnoMod1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel10.add(cmbTurnoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 240, 30));

        jLabel50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Turno:");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel51.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Correo:");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtCorreoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod1.setBorder(null);
        txtCorreoMod1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoMod1FocusLost(evt);
            }
        });
        txtCorreoMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtCorreoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 210, 30));
        jPanel10.add(jSeparator44, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 310, 20));

        jButton8.setText("Seleccionar Imagen");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 280, -1, -1));

        txtCorreo4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreo4FocusLost(evt);
            }
        });
        txtCorreo4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreo4KeyTyped(evt);
            }
        });
        jPanel10.add(txtCorreo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 210, 30));

        jLabel52.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Correo:");
        jPanel10.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        jLabel53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Nacionalidad:");
        jPanel10.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 300, 90, -1));

        txtNacionalidadMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod1.setBorder(null);
        txtNacionalidadMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtNacionalidadMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 210, 30));

        txtSexoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod1.setText("Femenino");
        txtSexoMod1.setBorder(null);
        jPanel10.add(txtSexoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 250, 30));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Sexo:");
        jPanel10.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Cedula:");
        jPanel10.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 30));

        txtCedulaMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod1.setBorder(null);
        txtCedulaMod1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaMod1ActionPerformed(evt);
            }
        });
        txtCedulaMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtCedulaMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 240, 30));

        txtEdadMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod1.setBorder(null);
        txtEdadMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadMod1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtEdadMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 250, 30));

        jLabel56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Edad:");
        jPanel10.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel57.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Segundo Apellido:");
        jPanel10.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 20));

        txtSegundoApellidoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod1.setBorder(null);
        txtSegundoApellidoMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtSegundoApellidoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 180, 30));

        txtPrimerApellidoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod1.setBorder(null);
        txtPrimerApellidoMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtPrimerApellidoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 190, 30));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Primer Apellido:");
        jPanel10.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 20));

        jLabel59.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Segundo nombre:");
        jPanel10.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 30));

        txtSegundoNombreMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod1.setBorder(null);
        txtSegundoNombreMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtSegundoNombreMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 180, 30));

        txtPrimerNombreMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod1.setBorder(null);
        txtPrimerNombreMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtPrimerNombreMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 200, 30));

        jLabel60.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Primer nombre:");
        jPanel10.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 20));

        ModificarNurse.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 790, 490));

        jTabbedPane1.addTab("Modificar enfermera", ModificarNurse);

        ModificarGuardia.setBackground(new java.awt.Color(255, 255, 255));
        ModificarGuardia.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(29, 35, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel4.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 310, 20));
        jPanel4.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 310, 20));
        jPanel4.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 20));
        jPanel4.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 310, 20));
        jPanel4.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 310, 20));
        jPanel4.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 310, 20));
        jPanel4.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 310, 20));
        jPanel4.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 310, 20));
        jPanel4.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 310, 20));
        jPanel4.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton3.setText("Modificar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel5.setBackground(new java.awt.Color(0, 0, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(lblImagenMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 180));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 190, 220));

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Agregar Foto del guardia:");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, -1, -1));

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Fecha de contratación:");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, -1, 20));
        jPanel4.add(txtFechaContratacionMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 380, 190, 30));
        jPanel4.add(dateFinContratoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 440, 190, 30));

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Fecha de finalización del contrato:");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        txtCargoMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Guardia jefe", "Guardia" }));
        jPanel4.add(txtCargoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 250, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Cargo:");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        cmbTurnoMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel4.add(cmbTurnoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 240, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Turno:");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Correo:");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtCorreoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod.setBorder(null);
        txtCorreoMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoModFocusLost(evt);
            }
        });
        txtCorreoMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoModKeyTyped(evt);
            }
        });
        jPanel4.add(txtCorreoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 240, 30));
        jPanel4.add(jSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 310, 20));

        jButton4.setText("Seleccionar Imagen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 320, -1, -1));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Correo:");
        jPanel4.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Nacionalidad:");
        jPanel4.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 300, 90, -1));

        txtNacionalidadMod.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod.setBorder(null);
        txtNacionalidadMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadModKeyTyped(evt);
            }
        });
        jPanel4.add(txtNacionalidadMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 210, 30));

        txtSexoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod.setText("Femenino");
        txtSexoMod.setBorder(null);
        jPanel4.add(txtSexoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 250, 30));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Sexo:");
        jPanel4.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Cedula:");
        jPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 30));

        txtCedulaMod.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod.setBorder(null);
        txtCedulaMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaModActionPerformed(evt);
            }
        });
        txtCedulaMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaModKeyTyped(evt);
            }
        });
        jPanel4.add(txtCedulaMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 240, 30));

        txtEdadMod.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod.setBorder(null);
        txtEdadMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadModKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadModKeyTyped(evt);
            }
        });
        jPanel4.add(txtEdadMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 250, 30));

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Edad:");
        jPanel4.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Segundo Apellido:");
        jPanel4.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 20));

        txtSegundoApellidoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod.setBorder(null);
        txtSegundoApellidoMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoModKeyTyped(evt);
            }
        });
        jPanel4.add(txtSegundoApellidoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 180, 30));

        txtPrimerApellidoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod.setBorder(null);
        txtPrimerApellidoMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoModKeyTyped(evt);
            }
        });
        jPanel4.add(txtPrimerApellidoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 190, 30));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Primer Apellido:");
        jPanel4.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 20));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Segundo nombre:");
        jPanel4.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 30));

        txtSegundoNombreMod.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod.setBorder(null);
        txtSegundoNombreMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreModKeyTyped(evt);
            }
        });
        jPanel4.add(txtSegundoNombreMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 180, 30));

        txtPrimerNombreMod.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod.setBorder(null);
        txtPrimerNombreMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreModKeyTyped(evt);
            }
        });
        jPanel4.add(txtPrimerNombreMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 200, 30));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Primer nombre:");
        jPanel4.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 20));

        ModificarGuardia.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 790, 490));

        jTabbedPane1.addTab("ModificarGuardia", ModificarGuardia);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(29, 35, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 310, 20));
        jPanel12.add(jSeparator45, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 310, 20));
        jPanel12.add(jSeparator46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 20));
        jPanel12.add(jSeparator47, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 310, 20));
        jPanel12.add(jSeparator48, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 310, 20));
        jPanel12.add(jSeparator49, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 310, 20));
        jPanel12.add(jSeparator50, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 310, 20));
        jPanel12.add(jSeparator51, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 310, 20));
        jPanel12.add(jSeparator52, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 310, 20));
        jPanel12.add(jSeparator53, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton9.setText("Añadir");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel13.setBackground(new java.awt.Color(0, 0, 51));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel13.add(lblImagen2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 180));

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 190, 220));

        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Agregar Foto de la enfermera:");
        jPanel12.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, -1, -1));

        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Fecha de contratación:");
        jPanel12.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, -1, 20));
        jPanel12.add(txtFechaContratacion2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 380, 190, 30));
        jPanel12.add(dateFinContrato2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 440, 190, 30));

        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Fecha de finalización del contrato:");
        jPanel12.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        cmbCargo2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Oficial", "Guardia" }));
        jPanel12.add(cmbCargo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 250, 30));

        jLabel63.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Cargo:");
        jPanel12.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        cmbTurno2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel12.add(cmbTurno2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 240, 30));

        jLabel64.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Turno:");
        jPanel12.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel65.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Correo:");
        jPanel12.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtCorreo2.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo2.setBorder(null);
        txtCorreo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreo2FocusLost(evt);
            }
        });
        txtCorreo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreo2ActionPerformed(evt);
            }
        });
        txtCorreo2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreo2KeyTyped(evt);
            }
        });
        jPanel12.add(txtCorreo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 240, 30));
        jPanel12.add(jSeparator54, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 310, 20));

        jButton10.setText("Seleccionar Imagen");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 320, -1, -1));

        jLabel66.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("Correo:");
        jPanel12.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        jLabel67.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Nacionalidad:");
        jPanel12.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 300, 90, -1));

        txtNacionalidad2.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad2.setBorder(null);
        txtNacionalidad2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad2KeyTyped(evt);
            }
        });
        jPanel12.add(txtNacionalidad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 210, 30));

        txtSexo2.setEditable(false);
        txtSexo2.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo2.setText("Femenino");
        txtSexo2.setBorder(null);
        jPanel12.add(txtSexo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 250, 30));

        jLabel68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Sexo:");
        jPanel12.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel69.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Cedula:");
        jPanel12.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 30));

        txtCedula2.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula2.setBorder(null);
        txtCedula2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedula2ActionPerformed(evt);
            }
        });
        txtCedula2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedula2KeyTyped(evt);
            }
        });
        jPanel12.add(txtCedula2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 240, 30));

        txtEdad2.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad2.setBorder(null);
        txtEdad2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad2KeyTyped(evt);
            }
        });
        jPanel12.add(txtEdad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 250, 30));

        jLabel70.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Edad:");
        jPanel12.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel71.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Segundo Apellido:");
        jPanel12.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 20));

        txtSegundoApellido2.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido2.setBorder(null);
        txtSegundoApellido2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido2KeyTyped(evt);
            }
        });
        jPanel12.add(txtSegundoApellido2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 180, 30));

        txtPrimerApellido2.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido2.setBorder(null);
        txtPrimerApellido2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido2KeyTyped(evt);
            }
        });
        jPanel12.add(txtPrimerApellido2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 190, 30));

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Primer Apellido:");
        jPanel12.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 20));

        jLabel73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Segundo nombre:");
        jPanel12.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 30));

        txtSegundoNombre2.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre2.setBorder(null);
        txtSegundoNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre2KeyTyped(evt);
            }
        });
        jPanel12.add(txtSegundoNombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 180, 30));

        txtPrimerNombre2.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre2.setBorder(null);
        txtPrimerNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre2KeyTyped(evt);
            }
        });
        jPanel12.add(txtPrimerNombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 200, 30));

        jLabel74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Primer nombre:");
        jPanel12.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 20));

        jPanel9.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 820, 510));

        jTabbedPane1.addTab("AgregarCDA", jPanel9);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(29, 35, 51));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel15.add(jSeparator55, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 310, 20));
        jPanel15.add(jSeparator56, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 310, 20));
        jPanel15.add(jSeparator57, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 20));
        jPanel15.add(jSeparator58, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 310, 20));
        jPanel15.add(jSeparator59, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 310, 20));
        jPanel15.add(jSeparator60, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 310, 20));
        jPanel15.add(jSeparator61, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 310, 20));
        jPanel15.add(jSeparator62, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 310, 20));
        jPanel15.add(jSeparator63, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 310, 20));
        jPanel15.add(jSeparator64, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton11.setText("Modificar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel16.setBackground(new java.awt.Color(0, 0, 51));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel16.add(lblImagenMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 180));

        jPanel15.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 190, 220));

        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Agregar Foto del guardia:");
        jPanel15.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, -1, -1));

        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Fecha de contratación:");
        jPanel15.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, -1, 20));
        jPanel15.add(txtFechaContratacionMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 380, 190, 30));
        jPanel15.add(dateFinContratoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 440, 190, 30));

        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Fecha de finalización del contrato:");
        jPanel15.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        txtCargoMod1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Oficial", "Guardia" }));
        jPanel15.add(txtCargoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 250, 30));

        jLabel78.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Cargo:");
        jPanel15.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        cmbTurnoMod2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel15.add(cmbTurnoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 240, 30));

        jLabel79.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Turno:");
        jPanel15.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel80.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Correo:");
        jPanel15.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtCorreoMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod2.setBorder(null);
        txtCorreoMod2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoMod2FocusLost(evt);
            }
        });
        txtCorreoMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtCorreoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 240, 30));
        jPanel15.add(jSeparator65, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 310, 20));

        jButton12.setText("Seleccionar Imagen");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 320, -1, -1));

        jLabel81.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Correo:");
        jPanel15.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        jLabel82.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("Nacionalidad:");
        jPanel15.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 300, 90, -1));

        txtNacionalidadMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod2.setBorder(null);
        txtNacionalidadMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtNacionalidadMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 210, 30));

        txtSexoMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod2.setText("Femenino");
        txtSexoMod2.setBorder(null);
        jPanel15.add(txtSexoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 250, 30));

        jLabel83.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("Sexo:");
        jPanel15.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel84.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("Cedula:");
        jPanel15.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 30));

        txtCedulaMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod2.setBorder(null);
        txtCedulaMod2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaMod2ActionPerformed(evt);
            }
        });
        txtCedulaMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtCedulaMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 240, 30));

        txtEdadMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod2.setBorder(null);
        txtEdadMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadMod2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtEdadMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 250, 30));

        jLabel85.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("Edad:");
        jPanel15.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel86.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("Segundo Apellido:");
        jPanel15.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 20));

        txtSegundoApellidoMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod2.setBorder(null);
        txtSegundoApellidoMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtSegundoApellidoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 180, 30));

        txtPrimerApellidoMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod2.setBorder(null);
        txtPrimerApellidoMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtPrimerApellidoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 190, 30));

        jLabel87.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(255, 255, 255));
        jLabel87.setText("Primer Apellido:");
        jPanel15.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 20));

        jLabel88.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(255, 255, 255));
        jLabel88.setText("Segundo nombre:");
        jPanel15.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 30));

        txtSegundoNombreMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod2.setBorder(null);
        txtSegundoNombreMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtSegundoNombreMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 180, 30));

        txtPrimerNombreMod2.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod2.setBorder(null);
        txtPrimerNombreMod2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreMod2KeyTyped(evt);
            }
        });
        jPanel15.add(txtPrimerNombreMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 200, 30));

        jLabel89.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setText("Primer nombre:");
        jPanel15.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 20));

        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 790, 490));

        jTabbedPane1.addTab("ModificarCDA", jPanel14);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaCDA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Nombre", "Apellido", "Edad", "Cedula", "Sexo", "Nacionalidad", "Correo", "Turno", "Cargo", "FechaContratacion", "FechaFinContrato"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCDA.setComponentPopupMenu(jPopupMenu1);
        jScrollPane3.setViewportView(tablaCDA);
        if (tablaCDA.getColumnModel().getColumnCount() > 0) {
            tablaCDA.getColumnModel().getColumn(0).setResizable(false);
            tablaCDA.getColumnModel().getColumn(1).setResizable(false);
            tablaCDA.getColumnModel().getColumn(2).setResizable(false);
            tablaCDA.getColumnModel().getColumn(3).setResizable(false);
            tablaCDA.getColumnModel().getColumn(4).setResizable(false);
            tablaCDA.getColumnModel().getColumn(5).setResizable(false);
            tablaCDA.getColumnModel().getColumn(6).setResizable(false);
            tablaCDA.getColumnModel().getColumn(7).setResizable(false);
            tablaCDA.getColumnModel().getColumn(8).setResizable(false);
            tablaCDA.getColumnModel().getColumn(9).setResizable(false);
            tablaCDA.getColumnModel().getColumn(10).setResizable(false);
            tablaCDA.getColumnModel().getColumn(11).setResizable(false);
        }

        jPanel17.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 1080, 550));

        jTabbedPane1.addTab("MostrarCDA", jPanel17);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1100, 590));

        jButton13.setText("SALIR");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed

    }//GEN-LAST:event_txtCedulaActionPerformed

    private void verificarFecha() {
        System.out.println("Fecha seleccionada: " + jDateChooserFinContrato.getDate());
        System.out.println("Componente null? " + (jDateChooserFinContrato == null));
        System.out.println("Editor null? " + (jDateChooserFinContrato.getDateEditor() == null));
    }

    private void cargarTablaGuardias() {
        DefaultTableModel modelo = guardiaController.obtenerModeloTabla();
        tablaGuardias.setModel(modelo);
        tablaGuardias.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
        ajustarImagenesTabla();
    }

    private void ajustarColumnasTablaCDA() {

        TableColumnModel columnModel = tablaCDA.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(120);

        tablaCDA.setRowHeight(60);
    }

    private void cargarDatosGuardiaParaModificar(Guardia guardia) {
        if (guardia == null) {
            return;
        }

        txtPrimerNombreMod.setText(guardia.getPrimerNombre());
        txtSegundoNombreMod.setText(guardia.getSegundoNombre() != null ? guardia.getSegundoNombre() : "");
        txtPrimerApellidoMod.setText(guardia.getPrimerApellido());
        txtSegundoApellidoMod.setText(guardia.getSegundoApellido());
        txtEdadMod.setText(String.valueOf(guardia.getEdad()));
        txtCedulaMod.setText(guardia.getIdentificacion());
        txtNacionalidadMod.setText(guardia.getNacionalidad());
        txtCorreoMod.setText(guardia.getCorreo());
        cmbTurnoMod.setSelectedItem(guardia.getTurno());
        txtCargoMod.setSelectedItem(guardia.getCargo());
        txtFechaContratacionMod.setText(guardia.getFechaInicioContrato().toString());

        try {
            dateFinContratoMod.setDate(
                    Date.from(guardia.getFechaFinContrato().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
        } catch (Exception e) {
            dateFinContratoMod.setDate(null);
        }

        // Manejo seguro de la imagen
        cargarImagenGuardia(guardia.getRutaImagen());
    }

    private void cargarImagenGuardia(String rutaImagen) {
        try {
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                File file = new File(rutaImagen);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(rutaImagen);
                    Image img = icon.getImage().getScaledInstance(
                            lblImagenMod.getWidth(),
                            lblImagenMod.getHeight(),
                            Image.SCALE_SMOOTH
                    );
                    lblImagenMod.setIcon(new ImageIcon(img));
                    imagenSeleccionadaMod = file;
                    return;
                }
            }
            // Imagen por defecto si no hay imagen o no se puede cargar
            lblImagenMod.setIcon(new ImageIcon(getClass().getResource("/Resources/default_guard.png")));
            imagenSeleccionadaMod = null;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            lblImagenMod.setIcon(new ImageIcon(getClass().getResource("/Resources/default_guard.png")));
            imagenSeleccionadaMod = null;
        }
    }

    private void limpiarFormularioModificacion() {
        txtPrimerNombreMod.setText("");
        txtSegundoNombreMod.setText("");
        txtPrimerApellidoMod.setText("");
        txtSegundoApellidoMod.setText("");
        txtEdadMod.setText("");
        txtCedulaMod.setText("");
        txtNacionalidadMod.setText("");
        txtCorreoMod.setText("");
        cmbTurnoMod.setSelectedIndex(0);
        dateFinContratoMod.setDate(null);
        txtFechaContratacionMod.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblImagenMod.setIcon(null);
        rutaImagenSeleccionada = null;
    }

    private class ImagenRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null && value instanceof String) {
                String rutaImagen = (String) value;
                try {

                    ImageIcon icono = new ImageIcon(rutaImagen);
                    Image img = icono.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    setIcon(null);
                    setText("(Imagen no encontrada)");
                }
            } else {
                setIcon(null);
                setText("(Sin imagen)");
            }

            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);

            return this;
        }
    }

    private void limpiarFormularioEnfermera() {
        txtPrimerNombre1.setText("");
        txtSegundoNombre1.setText("");
        txtPrimerApellido1.setText("");
        txtSegundoApellido1.setText("");
        txtEdad1.setText("");
        txtCedula1.setText("");
        txtNacionalidad1.setText("");
        txtCorreo1.setText("");
        cmbTurno1.setSelectedIndex(0);
        dateFinContrato1.setDate(null);
        txtFechaContratacion1.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblImagen1.setIcon(null);
        rutaImagenSeleccionada = null;
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         try {
        // 1. Validar fecha seleccionada
        if (jDateChooserFinContrato.getDate() == null) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar una fecha de fin de contrato válida", 
                "Error", JOptionPane.ERROR_MESSAGE);
            jDateChooserFinContrato.requestFocus();
            return;
        }
        
        // 2. Debug adicional para verificar la fecha
        System.out.println("Fecha seleccionada en JDateChooser: " + jDateChooserFinContrato.getDate());
        
        // 3. Conversión de fechas
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = jDateChooserFinContrato.getDate().toInstant()
                           .atZone(ZoneId.systemDefault())
                           .toLocalDate();
        
        // 4. Llamada al controller
        Guardia nuevoGuardia = guardiaController.registrarGuardia(
            txtPrimerNombre.getText().trim(),
            txtSegundoNombre.getText().trim(),
            txtPrimerApellido.getText().trim(),
            txtSegundoApellido.getText().trim(),
            Integer.parseInt(txtEdad.getText().trim()),
            txtCedula.getText().trim(),
            txtNacionalidad.getText().trim(),
            txtCorreo.getText().trim(),
            cmbTurno.getSelectedItem().toString(),
            fechaInicio, // Asegúrate que es LocalDate.now()
            fechaFin,    // Fecha del JDateChooser convertida
            cmbCargo.getSelectedItem().toString(),
            imagenSeleccionadaMod
        );
        
        // 5. Feedback y limpieza
        limpiarFormulario();
        actualizarTablaGuardias();
        JOptionPane.showMessageDialog(this, "Guardia registrado!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void actualizarTablaGuardias() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        };

        modelo.setColumnIdentifiers(new String[]{
            "Foto", "Nombre", "Apellido", "Edad", "Cédula",
            "Nacionalidad", "Correo", "Turno", "Cargo", "Fin Contrato"
        });

        List<Guardia> guardias = guardiaController.obtenerTodosGuardias();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefectoGuardia();

        for (Guardia g : guardias) {
            ImageIcon icono = iconoPorDefecto;

            if (g.getRutaImagen() != null && !g.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(g.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(g.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                }
            }

            modelo.addRow(new Object[]{
                icono,
                g.getPrimerNombre() + " " + (g.getSegundoNombre() != null ? g.getSegundoNombre() : ""),
                g.getPrimerApellido() + " " + g.getSegundoApellido(),
                g.getEdad(),
                g.getIdentificacion(),
                g.getNacionalidad(),
                g.getCorreo(),
                g.getTurno(),
                g.getCargo(),
                g.getFechaFinContratoFormateada()
            });
        }

        tablaGuardias.setModel(modelo);
        tablaGuardias.setRowHeight(85);
        tablaGuardias.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaGuardias.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private ImageIcon crearIconoPorDefectoGuardia() {
        // Crear una imagen simple por defecto programáticamente para guardias
        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Dibujar un fondo
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, 80, 80);

        // Dibujar un icono simple (puedes personalizarlo diferente al de enfermeras)
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(10, 10, 60, 60);
        g2d.drawLine(25, 50, 55, 50); // Boca
        g2d.fillOval(20, 25, 10, 10); // Ojo izquierdo
        g2d.fillOval(50, 25, 10, 10); // Ojo derecho

        g2d.dispose();

        return new ImageIcon(img);
    }

    private void ajustarImagenesTabla() {
        tablaGuardias.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);

                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                } else {
                    label.setIcon(null);
                    label.setText("(Sin imagen)");
                }

                return label;
            }
        });

        tablaGuardias.setRowHeight(70);
    }

    private void limpiarFormulario() {
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtEdad.setText("");
        txtCedula.setText("");
        txtNacionalidad.setText("");
        txtCorreo.setText("");
        cmbTurno.setSelectedIndex(0);
        cmbCargo.setSelectedIndex(0);
        jDateChooserFinContrato.setDate(null);
        txtFechaContratacion.setText(LocalDate.now().toString());
        lblFoto.setIcon(null);
        rutaImagenSeleccionada = null;
    }

    private void eliminarGuardia(String cedula, int filaSeleccionada) {
        try {

            GuardiaDAO dao = new GuardiaDAO();
            boolean eliminado = dao.eliminarGuardia(cedula);

            if (eliminado) {

                ((DefaultTableModel) tablaGuardias.getModel()).removeRow(filaSeleccionada);

                JOptionPane.showMessageDialog(this,
                        "Guardia eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el guardia",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Mostrar opciones al usuario (cámara o selección de archivo)
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del guardia?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File nuevaImagen = null;

            if (opcion == 0) { // Tomar Foto con cámara
                nuevaImagen = guardiaController.capturarImagenGuardia();
            } else if (opcion == 1) { // Seleccionar archivo del sistema
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                // Configurar el directorio inicial (opcional)
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();

                    // Validar extensión del archivo
                    String nombreArchivo = nuevaImagen.getName().toLowerCase();
                    if (!nombreArchivo.endsWith(".jpg")
                            && !nombreArchivo.endsWith(".jpeg")
                            && !nombreArchivo.endsWith(".png")) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Mostrar previsualización si se seleccionó una imagen
            if (nuevaImagen != null && nuevaImagen.exists()) {
                // Escalar la imagen para que se ajuste al JLabel
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                lblFoto.getWidth(),
                                lblFoto.getHeight(),
                                Image.SCALE_SMOOTH
                        );

                // Mostrar en el JLabel
                lblFoto.setIcon(new ImageIcon(imagenEscalada));

                // Guardar referencia al archivo para cuando se guarde
                imagenSeleccionadaMod = nuevaImagen;

                // Mostrar tooltip con la ruta
                lblFoto.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtEdadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadKeyPressed

    private void txtEdadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtEdadKeyTyped

    private void txtCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoKeyTyped


    }//GEN-LAST:event_txtCorreoKeyTyped

    private void txtCorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoFocusLost
        String email = txtCorreo.getText().trim();

        if (!email.isEmpty() && (!email.contains("@") || !email.endsWith(".com"))) {
            JOptionPane.showMessageDialog(this,
                    "Correo inválido!\nDebe contener @ y terminar con .com",
                    "Error", JOptionPane.ERROR_MESSAGE);

            txtCorreo.requestFocus();
        }
    }//GEN-LAST:event_txtCorreoFocusLost

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtCedulaKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      try {
        // Obtener cédula original (no modificable)
        String cedulaOriginal = txtCedulaMod.getText().trim();
        
        // Crear mapa con todos los cambios
        Map<String, Object> cambios = new HashMap<>();
        cambios.put("primerNombre", txtPrimerNombreMod.getText().trim());
        cambios.put("segundoNombre", txtSegundoNombreMod.getText().trim());
        cambios.put("primerApellido", txtPrimerApellidoMod.getText().trim());
        cambios.put("segundoApellido", txtSegundoApellidoMod.getText().trim());
        cambios.put("edad", Integer.parseInt(txtEdadMod.getText().trim()));
        cambios.put("nacionalidad", txtNacionalidadMod.getText().trim());
        cambios.put("correo", txtCorreoMod.getText().trim());
        cambios.put("turno", cmbTurnoMod.getSelectedItem().toString());
        cambios.put("cargo", txtCargoMod.getSelectedItem().toString());
        cambios.put("fechaFin", dateFinContratoMod.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());

        // Llamar al controller para modificar (la imagen puede ser null)
        boolean resultado = guardiaController.modificarGuardia(
                cedulaOriginal,
                cambios,
                imagenSeleccionadaMod // File o null
        );

        // Feedback al usuario
        if (resultado) {
            JOptionPane.showMessageDialog(this,
                    "Guardia modificado con éxito",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaGuardias();
            jTabbedPane1.setSelectedComponent(ListaDeGuardias);
            
            // Resetear imagen seleccionada después de modificar
            imagenSeleccionadaMod = null;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error al modificar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButton3ActionPerformed


    private void txtCorreoModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoModFocusLost
        String email = txtCorreoMod.getText().trim();

        if (!email.isEmpty() && (!email.contains("@") || !email.endsWith(".com"))) {
            JOptionPane.showMessageDialog(this,
                    "Correo inválido!\nDebe contener @ y terminar con .com",
                    "Error", JOptionPane.ERROR_MESSAGE);

            txtCorreoMod.requestFocus();
        }
    }//GEN-LAST:event_txtCorreoModFocusLost

    private void txtCorreoModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoModKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoModKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Opciones para el diálogo
    Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
    int opcion = JOptionPane.showOptionDialog(
            this,
            "¿Cómo desea obtener la imagen del guardia?",
            "Seleccionar Imagen",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );

    try {
        File nuevaImagen = null;

        if (opcion == 0) { // Tomar Foto con cámara
            nuevaImagen = guardiaController.capturarImagenGuardia();
        } else if (opcion == 1) { // Seleccionar archivo del sistema
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

            int resultado = fileChooser.showOpenDialog(this);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                nuevaImagen = fileChooser.getSelectedFile();

                // Validar extensión del archivo
                String nombreArchivo = nuevaImagen.getName().toLowerCase();
                if (!nombreArchivo.endsWith(".jpg") && 
                    !nombreArchivo.endsWith(".jpeg") && 
                    !nombreArchivo.endsWith(".png")) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        // Mostrar previsualización si se seleccionó una imagen
        if (nuevaImagen != null && nuevaImagen.exists()) {
            // Escalar la imagen para el JLabel
            ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
            Image imagenEscalada = icono.getImage()
                    .getScaledInstance(
                            lblImagenMod.getWidth(),
                            lblImagenMod.getHeight(),
                            Image.SCALE_SMOOTH
                    );

            // Mostrar en el JLabel
            lblImagenMod.setIcon(new ImageIcon(imagenEscalada));

            // Guardar referencia al archivo para cuando se guarde
            imagenSeleccionadaMod = nuevaImagen;

            // Mostrar tooltip con la ruta
            lblImagenMod.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            
            // Actualizar inmediatamente en el JSON (opcional)
            // actualizarImagenEnJSON(nuevaImagen);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error al obtener imagen: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtCedulaModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaModActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaModActionPerformed

    private void txtCedulaModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtCedulaModKeyTyped

    private void txtEdadModKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadModKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadModKeyPressed

    private void txtEdadModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtEdadModKeyTyped

    private void ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarActionPerformed
        try {
            int fila = tablaGuardias.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un guardia primero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cedula = tablaGuardias.getValueAt(fila, 4).toString(); // Asume columna 4 es cédula
            this.cedulaActualModificacion = cedula; // <-- ASIGNACIÓN CLAVE

            Guardia guardia = guardiaController.obtenerGuardiaPorCedula(cedula);

            if (guardia == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el guardia seleccionado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            cargarDatosGuardiaParaModificar(guardia);
            jTabbedPane1.setSelectedComponent(ModificarGuardia);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_ModificarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        int filaSeleccionada = tablaGuardias.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un guardia primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaGuardias.getValueAt(filaSeleccionada, 4).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al guardia con cédula " + cedula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = guardiaController.eliminarGuardia(cedula);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Guardia eliminado con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTablaGuardias();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_EliminarActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // Obtener instancia Singleton del controller
            EnfermeraController controller = EnfermeraController.getInstancia();

            // Obtener datos del formulario (solo obtención, sin validación)
            String primerNombre = txtPrimerNombre1.getText().trim();
            String segundoNombre = txtSegundoNombre1.getText().trim();
            String primerApellido = txtPrimerApellido1.getText().trim();
            String segundoApellido = txtSegundoApellido1.getText().trim();
            int edad = Integer.parseInt(txtEdad1.getText().trim());
            String cedula = txtCedula1.getText().trim();
            String nacionalidad = txtNacionalidad1.getText().trim();
            String correo = txtCorreo1.getText().trim();
            String turno = cmbTurno1.getSelectedItem().toString();
            LocalDate fechaFin = dateFinContrato1.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            // Llamar al controller (todas las validaciones están ahí)
            Enfermera nuevaEnfermera = controller.registrarEnfermera(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno, fechaFin,
                    imagenSeleccionadaMod // Archivo de imagen
            );

            // Si llegamos aquí, todo salió bien
            limpiarFormularioEnfermera();
            actualizarTablaEnfermeras();
            JOptionPane.showMessageDialog(this,
                    "Enfermera registrada exitosamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "La edad debe ser un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    public void actualizarTabla() {
        cargarTablaGuardias();
    }

    private void cargarDatosEnfermeraParaModificar(Enfermera enfermera) {
        if (enfermera == null) {
            return;
        }

        txtPrimerNombreMod1.setText(enfermera.getPrimerNombre());
        txtSegundoNombreMod1.setText(enfermera.getSegundoNombre() != null ? enfermera.getSegundoNombre() : "");
        txtPrimerApellidoMod1.setText(enfermera.getPrimerApellido());
        txtSegundoApellidoMod1.setText(enfermera.getSegundoApellido());
        txtEdadMod1.setText(String.valueOf(enfermera.getEdad()));
        txtCedulaMod1.setText(enfermera.getIdentificacion());
        txtNacionalidadMod1.setText(enfermera.getNacionalidad());
        txtCorreoMod1.setText(enfermera.getCorreo());
        cmbTurnoMod1.setSelectedItem(enfermera.getTurno());
        txtFechaContratacionMod1.setText(enfermera.getFechaContratacion().toString());

        try {
            dateFinContratoMod1.setDate(
                    Date.from(enfermera.getFechaFinContrato().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
        } catch (Exception e) {
            dateFinContratoMod1.setDate(null);
        }

        // Manejo seguro de la imagen
        cargarImagenEnfermera(enfermera.getRutaImagen());
    }

    private void cargarImagenEnfermera(String rutaImagen) {
        try {
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                File file = new File(rutaImagen);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(rutaImagen);
                    Image img = icon.getImage().getScaledInstance(
                            lblImagenMod1.getWidth(),
                            lblImagenMod1.getHeight(),
                            Image.SCALE_SMOOTH
                    );
                    lblImagenMod1.setIcon(new ImageIcon(img));
                    imagenSeleccionadaMod = file;
                    return;
                }
            }
            // Imagen por defecto si no hay imagen o no se puede cargar
            lblImagenMod1.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
            imagenSeleccionadaMod = null;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            lblImagenMod1.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
            imagenSeleccionadaMod = null;
        }
    }

    private void actualizarTablaEnfermeras() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        };

        modelo.setColumnIdentifiers(new String[]{
            "Foto", "Nombre", "Apellido", "Edad", "Cédula",
            "Nacionalidad", "Correo", "Turno", "Fin Contrato"
        });

        List<Enfermera> enfermeras = enfermeraController.obtenerTodasEnfermeras();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefecto();

        for (Enfermera e : enfermeras) {
            ImageIcon icono = iconoPorDefecto; // Usar el icono por defecto como valor inicial

            if (e.getRutaImagen() != null && !e.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(e.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(e.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                }
            }

            modelo.addRow(new Object[]{
                icono,
                e.getPrimerNombre() + " " + (e.getSegundoNombre() != null ? e.getSegundoNombre() : ""),
                e.getPrimerApellido() + " " + e.getSegundoApellido(),
                e.getEdad(),
                e.getIdentificacion(),
                e.getNacionalidad(),
                e.getCorreo(),
                e.getTurno(),
                e.getFechaFinContratoFormateada()
            });
        }

        tablaEnfermeras.setModel(modelo);
        tablaEnfermeras.setRowHeight(85);
        tablaEnfermeras.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaEnfermeras.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private ImageIcon crearIconoPorDefecto() {
        // Crear una imagen simple por defecto programáticamente
        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Dibujar un fondo
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, 80, 80);

        // Dibujar un icono simple
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(10, 10, 60, 60);
        g2d.drawLine(25, 50, 55, 50); // Boca
        g2d.fillOval(20, 25, 10, 10); // Ojo izquierdo
        g2d.fillOval(50, 25, 10, 10); // Ojo derecho

        g2d.dispose();

        return new ImageIcon(img);
    }

    private static class ImagenTablaRenderer extends DefaultTableCellRenderer {

        private final ImageIcon iconoPorDefecto;

        public ImagenTablaRenderer() {
            // Crear imagen por defecto
            BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRect(0, 0, 80, 80);
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(10, 10, 60, 60);
            g2d.drawLine(25, 50, 55, 50);
            g2d.fillOval(20, 25, 10, 10);
            g2d.fillOval(50, 25, 10, 10);
            g2d.dispose();
            this.iconoPorDefecto = new ImageIcon(img);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);

            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
            } else if (value instanceof String) {
                try {
                    File file = new File((String) value);
                    if (file.exists()) {
                        Image img = new ImageIcon((String) value).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                    } else {
                        label.setIcon(iconoPorDefecto);
                    }
                } catch (Exception e) {
                    label.setIcon(iconoPorDefecto);
                }
            } else {
                label.setIcon(iconoPorDefecto);
            }
            return label;
        }
    }

    private void validarFechaFinContrato() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaInicio = LocalDate.parse(txtFechaContratacionMod1.getText().trim(), formatter);

            if (dateFinContratoMod1.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una fecha de fin de contrato",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaFin = dateFinContratoMod1.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            if (!fechaFin.isAfter(fechaInicio)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de fin de contrato debe ser posterior a la fecha de inicio",
                        "Error", JOptionPane.ERROR_MESSAGE);
                dateFinContratoMod1.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en las fechas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarPanelModificacion() {

        txtPrimerNombreMod1.setText("");
        txtSegundoNombreMod1.setText("");
        txtPrimerApellidoMod1.setText("");
        txtSegundoApellidoMod1.setText("");
        txtEdadMod1.setText("");
        txtCedulaMod1.setText("");
        txtNacionalidadMod1.setText("");
        txtCorreoMod1.setText("");
        cmbTurnoMod1.setSelectedIndex(0);
        txtFechaContratacionMod1.setText("");
        dateFinContratoMod1.setDate(null);
        lblImagenMod1.setIcon(null);

        ModificarNurse.setVisible(false);

        rutaImagenSeleccionada = null;
    }

    private Enfermera obtenerDatosModificados() {
        String primerNombre = txtPrimerNombreMod1.getText().trim();
        String segundoNombre = txtSegundoNombreMod1.getText().trim();
        String primerApellido = txtPrimerApellidoMod1.getText().trim();
        String segundoApellido = txtSegundoApellidoMod1.getText().trim();
        int edad = Integer.parseInt(txtEdadMod1.getText().trim());
        String cedula = txtCedulaMod1.getText().trim();
        String nacionalidad = txtNacionalidadMod1.getText().trim();
        String correo = txtCorreoMod1.getText().trim();
        String turno = cmbTurnoMod1.getSelectedItem().toString();

        LocalDate fechaContratacion = LocalDate.now();

        Date fechaFin = dateFinContratoMod1.getDate();
        if (fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una fecha de fin de contrato.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        LocalDate fechaFinContrato = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return new Enfermera(
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                edad,
                "Femenino",
                nacionalidad,
                cedula,
                turno,
                LocalDate.now(),
                fechaFinContrato,
                correo,
                "",
                ""
        );
    }


    private void txtCorreo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1FocusLost

    private void txtCorreo1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1KeyTyped

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Mostrar opciones al usuario
        Object[] options = {"Tomar Foto", "Seleccionar Archivo"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "Seleccione cómo obtener la imagen:",
                "Imagen del guardia",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File imagen = null;
            if (opcion == 0) { // Tomar Foto
                imagen = guardiaController.capturarImagenGuardia();
            } else { // Seleccionar Archivo
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);

                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    imagen = fileChooser.getSelectedFile();
                }
            }

            // Mostrar previsualización
            if (imagen != null) {
                ImageIcon icono = new ImageIcon(imagen.getAbsolutePath());
                Image img = icono.getImage()
                        .getScaledInstance(lblImagen1.getWidth(), lblImagen1.getHeight(), Image.SCALE_SMOOTH);
                lblImagen1.setIcon(new ImageIcon(img));
                imagenSeleccionadaMod = imagen;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtCedula1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedula1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula1ActionPerformed

    private void txtCedula1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedula1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula1KeyTyped

    private void txtEdad1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad1KeyPressed

    private void txtEdad1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad1KeyTyped

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            String cedulaOriginal = txtCedulaMod1.getText().trim();

            // Validar fecha de fin de contrato
            if (dateFinContratoMod1.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una fecha de fin de contrato",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", txtPrimerNombreMod1.getText().trim());
            cambios.put("segundoNombre", txtSegundoNombreMod1.getText().trim());
            cambios.put("primerApellido", txtPrimerApellidoMod1.getText().trim());
            cambios.put("segundoApellido", txtSegundoApellidoMod1.getText().trim());
            cambios.put("edad", Integer.parseInt(txtEdadMod1.getText().trim()));
            cambios.put("nacionalidad", txtNacionalidadMod1.getText().trim());
            cambios.put("correo", txtCorreoMod1.getText().trim());
            cambios.put("turno", cmbTurnoMod1.getSelectedItem().toString());
            cambios.put("fechaFin", dateFinContratoMod1.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());

            // Usar la imagen seleccionada (puede ser null si no se cambió)
            boolean resultado = enfermeraController.modificarEnfermera(
                    cedulaOriginal,
                    cambios,
                    imagenSeleccionadaMod // Este es el File de la imagen capturada/previamente seleccionada
            );

            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        "Enfermera modificada con éxito",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTablaEnfermeras();
                jTabbedPane1.setSelectedComponent(MostrarEnfermeras);

                // Limpiar la imagen seleccionada después de modificar
                imagenSeleccionadaMod = null;
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    public Enfermera obtenerDatosSeleccionados() {
        int filaSeleccionada = tablaEnfermeras.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una enfermera de la tabla",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String cedula = tablaEnfermeras.getValueAt(filaSeleccionada, 4).toString();

        return enfermeraDAO.obtenerEnfermeraPorIdentificacion(cedula);
    }

    private void txtCorreoMod1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoMod1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod1FocusLost

    private void txtCorreoMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoMod1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod1KeyTyped

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Mostrar opciones al usuario (cámara o selección de archivo)
        Object[] options = {"Usar Cámara", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(this,
                "¿Cómo desea obtener la imagen?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        try {
            File nuevaImagen = null;

            if (opcion == 0) { // Usar cámara
                nuevaImagen = enfermeraController.capturarImagenEnfermera();
            } else if (opcion == 1) { // Seleccionar archivo
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();
                }
            }

            if (nuevaImagen != null) {
                // Mostrar previsualización
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                lblImagenMod1.getWidth(),
                                lblImagenMod1.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                lblImagenMod1.setIcon(new ImageIcon(imagenEscalada));
                imagenSeleccionadaMod = nuevaImagen;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtCorreo4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo4FocusLost

    private void txtCorreo4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo4KeyTyped

    private void txtCedulaMod1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod1ActionPerformed

    private void txtCedulaMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod1KeyTyped

    private void txtEdadMod1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod1KeyPressed

    private void txtEdadMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod1KeyTyped

    private void ModificarEnfermeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarEnfermeraActionPerformed
        int fila = tablaEnfermeras.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una enfermera primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaEnfermeras.getValueAt(fila, 4).toString();

        try {
            Enfermera enfermera = enfermeraController.obtenerEnfermeraPorCedula(cedula);

            if (enfermera != null) {
                cargarDatosEnfermeraParaModificar(enfermera);
                jTabbedPane1.setSelectedComponent(ModificarNurse);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ModificarEnfermeraActionPerformed

    private void EliminarEnfermeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarEnfermeraActionPerformed
        int filaSeleccionada = tablaEnfermeras.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una enfermera primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaEnfermeras.getValueAt(filaSeleccionada, 4).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar a la enfermera con cédula " + cedula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = enfermeraController.eliminarEnfermera(cedula);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Enfermera eliminada con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTablaEnfermeras();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_EliminarEnfermeraActionPerformed

    private void txtPrimerApellidoPDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrimerApellidoPDCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellidoPDCActionPerformed

    private void txtNuevoPrimerApellidoPDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNuevoPrimerApellidoPDCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNuevoPrimerApellidoPDCActionPerformed

    private void cmbNacionalidadPDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNacionalidadPDCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbNacionalidadPDCActionPerformed

    private void BotonContratarPDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonContratarPDCActionPerformed
        /* PersonalDeControlController controller = new PersonalDeControlController();
        controller.contratarPersonalDeControl(this);
         */

    }//GEN-LAST:event_BotonContratarPDCActionPerformed

    private void AgregarImagenBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarImagenBotonActionPerformed

    }//GEN-LAST:event_AgregarImagenBotonActionPerformed

    private void txtCorreo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtCorreo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo2FocusLost

    private void txtCorreo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo2ActionPerformed

    private void txtCorreo2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo2KeyTyped

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtCedula2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedula2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula2ActionPerformed

    private void txtCedula2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedula2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula2KeyTyped

    private void txtEdad2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad2KeyPressed

    private void txtEdad2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad2KeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txtCorreoMod2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoMod2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod2FocusLost

    private void txtCorreoMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoMod2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod2KeyTyped

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void txtCedulaMod2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod2ActionPerformed

    private void txtCedulaMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod2KeyTyped

    private void txtEdadMod2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod2KeyPressed

    private void txtEdadMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod2KeyTyped

    private void txtPrimerNombre1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombre1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume();
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerNombre1KeyTyped

    private void txtSegundoNombre1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombre1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume();
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoNombre1KeyTyped

    private void txtPrimerApellido1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellido1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerApellido1KeyTyped

    private void txtSegundoApellido1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellido1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoApellido1KeyTyped

    private void txtNacionalidad1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidad1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtNacionalidad1KeyTyped

    private void txtPrimerNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerNombreKeyTyped

    private void txtSegundoNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoNombreKeyTyped

    private void txtPrimerApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerApellidoKeyTyped

    private void txtSegundoApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoApellidoKeyTyped

    private void txtNacionalidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume();
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtNacionalidadKeyTyped

    private void txtPrimerNombreMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerNombreMod1KeyTyped

    private void txtSegundoNombreMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoNombreMod1KeyTyped

    private void txtPrimerApellidoMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerApellidoMod1KeyTyped

    private void txtSegundoApellidoMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoApellidoMod1KeyTyped

    private void txtNacionalidadMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtNacionalidadMod1KeyTyped

    private void txtPrimerNombreModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerNombreModKeyTyped

    private void txtSegundoNombreModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoNombreModKeyTyped

    private void txtPrimerApellidoModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerApellidoModKeyTyped

    private void txtSegundoApellidoModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoApellidoModKeyTyped

    private void txtNacionalidadModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadModKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtNacionalidadModKeyTyped

    private void txtPrimerNombre2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombre2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerNombre2KeyTyped

    private void txtSegundoNombre2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombre2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoNombre2KeyTyped

    private void txtPrimerApellido2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellido2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerApellido2KeyTyped

    private void txtSegundoApellido2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellido2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoApellido2KeyTyped

    private void txtNacionalidad2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidad2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtNacionalidad2KeyTyped

    private void txtPrimerNombreMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerNombreMod2KeyTyped

    private void txtSegundoNombreMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoNombreMod2KeyTyped

    private void txtPrimerApellidoMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPrimerApellidoMod2KeyTyped

    private void txtSegundoApellidoMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtSegundoApellidoMod2KeyTyped

    private void txtNacionalidadMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ' && c != '\b') {
            evt.consume(); // no permite que el carácter se escriba
            // solo muestra el mensaje si no es backspace
            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtNacionalidadMod2KeyTyped

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        Login otro = new Login();
        otro.setVisible(true); // Mostrar el nuevo JFrame
        this.dispose();
    }//GEN-LAST:event_jButton13ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Director.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Director.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Director.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Director.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Director().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AñadirGuardia;
    private javax.swing.JPanel Director;
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JMenuItem EliminarEnfermera;
    private javax.swing.JPanel ListaDeGuardias;
    private javax.swing.JMenuItem Modificar;
    private javax.swing.JMenuItem ModificarEnfermera;
    private javax.swing.JPanel ModificarGuardia;
    private javax.swing.JPanel ModificarNurse;
    private javax.swing.JPanel MostrarEnfermeras;
    private javax.swing.JComboBox<String> cmbCargo;
    private javax.swing.JComboBox<String> cmbCargo2;
    private javax.swing.JComboBox<String> cmbTurno;
    private javax.swing.JComboBox<String> cmbTurno1;
    private javax.swing.JComboBox<String> cmbTurno2;
    private javax.swing.JComboBox<String> cmbTurnoMod;
    private javax.swing.JComboBox<String> cmbTurnoMod1;
    private javax.swing.JComboBox<String> cmbTurnoMod2;
    private com.toedter.calendar.JDateChooser dateFinContrato1;
    private com.toedter.calendar.JDateChooser dateFinContrato2;
    private com.toedter.calendar.JDateChooser dateFinContratoMod;
    private com.toedter.calendar.JDateChooser dateFinContratoMod1;
    private com.toedter.calendar.JDateChooser dateFinContratoMod2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private com.toedter.calendar.JDateChooser jDateChooserFinContrato;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator43;
    private javax.swing.JSeparator jSeparator44;
    private javax.swing.JSeparator jSeparator45;
    private javax.swing.JSeparator jSeparator46;
    private javax.swing.JSeparator jSeparator47;
    private javax.swing.JSeparator jSeparator48;
    private javax.swing.JSeparator jSeparator49;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator50;
    private javax.swing.JSeparator jSeparator51;
    private javax.swing.JSeparator jSeparator52;
    private javax.swing.JSeparator jSeparator53;
    private javax.swing.JSeparator jSeparator54;
    private javax.swing.JSeparator jSeparator55;
    private javax.swing.JSeparator jSeparator56;
    private javax.swing.JSeparator jSeparator57;
    private javax.swing.JSeparator jSeparator58;
    private javax.swing.JSeparator jSeparator59;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator60;
    private javax.swing.JSeparator jSeparator61;
    private javax.swing.JSeparator jSeparator62;
    private javax.swing.JSeparator jSeparator63;
    private javax.swing.JSeparator jSeparator64;
    private javax.swing.JSeparator jSeparator65;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblImagen1;
    private javax.swing.JLabel lblImagen2;
    private javax.swing.JLabel lblImagenMod;
    private javax.swing.JLabel lblImagenMod1;
    private javax.swing.JLabel lblImagenMod2;
    private javax.swing.JTable tablaCDA;
    private javax.swing.JTable tablaEnfermeras;
    private javax.swing.JTable tablaGuardias;
    private javax.swing.JComboBox<String> txtCargoMod;
    private javax.swing.JComboBox<String> txtCargoMod1;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCedula1;
    private javax.swing.JTextField txtCedula2;
    private javax.swing.JTextField txtCedulaMod;
    private javax.swing.JTextField txtCedulaMod1;
    private javax.swing.JTextField txtCedulaMod2;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtCorreo1;
    private javax.swing.JTextField txtCorreo2;
    private javax.swing.JTextField txtCorreo4;
    private javax.swing.JTextField txtCorreoMod;
    private javax.swing.JTextField txtCorreoMod1;
    private javax.swing.JTextField txtCorreoMod2;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtEdad1;
    private javax.swing.JTextField txtEdad2;
    private javax.swing.JTextField txtEdadMod;
    private javax.swing.JTextField txtEdadMod1;
    private javax.swing.JTextField txtEdadMod2;
    private javax.swing.JTextField txtFechaContratacion;
    private javax.swing.JTextField txtFechaContratacion1;
    private javax.swing.JTextField txtFechaContratacion2;
    private javax.swing.JTextField txtFechaContratacionMod;
    private javax.swing.JTextField txtFechaContratacionMod1;
    private javax.swing.JTextField txtFechaContratacionMod2;
    private javax.swing.JTextField txtNacionalidad;
    private javax.swing.JTextField txtNacionalidad1;
    private javax.swing.JTextField txtNacionalidad2;
    private javax.swing.JTextField txtNacionalidadMod;
    private javax.swing.JTextField txtNacionalidadMod1;
    private javax.swing.JTextField txtNacionalidadMod2;
    private javax.swing.JTextField txtPrimerApellido;
    private javax.swing.JTextField txtPrimerApellido1;
    private javax.swing.JTextField txtPrimerApellido2;
    private javax.swing.JTextField txtPrimerApellidoMod;
    private javax.swing.JTextField txtPrimerApellidoMod1;
    private javax.swing.JTextField txtPrimerApellidoMod2;
    private javax.swing.JTextField txtPrimerNombre;
    private javax.swing.JTextField txtPrimerNombre1;
    private javax.swing.JTextField txtPrimerNombre2;
    private javax.swing.JTextField txtPrimerNombreMod;
    private javax.swing.JTextField txtPrimerNombreMod1;
    private javax.swing.JTextField txtPrimerNombreMod2;
    private javax.swing.JTextField txtSegundoApellido;
    private javax.swing.JTextField txtSegundoApellido1;
    private javax.swing.JTextField txtSegundoApellido2;
    private javax.swing.JTextField txtSegundoApellidoMod;
    private javax.swing.JTextField txtSegundoApellidoMod1;
    private javax.swing.JTextField txtSegundoApellidoMod2;
    private javax.swing.JTextField txtSegundoNombre;
    private javax.swing.JTextField txtSegundoNombre1;
    private javax.swing.JTextField txtSegundoNombre2;
    private javax.swing.JTextField txtSegundoNombreMod;
    private javax.swing.JTextField txtSegundoNombreMod1;
    private javax.swing.JTextField txtSegundoNombreMod2;
    private javax.swing.JTextField txtSexo;
    private javax.swing.JTextField txtSexo1;
    private javax.swing.JTextField txtSexo2;
    private javax.swing.JTextField txtSexoMod;
    private javax.swing.JTextField txtSexoMod1;
    private javax.swing.JTextField txtSexoMod2;
    // End of variables declaration//GEN-END:variables

    private boolean validarFechasContrato() {
        try {
            // Formato consistente en toda la aplicación
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parsear fecha de inicio
            LocalDate fechaInicio = LocalDate.parse(txtFechaContratacion.getText(), formatter);

            // Obtener fecha fin del JDateChooser
            if (jDateChooserFinContrato.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una fecha de fin de contrato",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Convertir a LocalDate usando la misma zona horaria
            LocalDate fechaFin = jDateChooserFinContrato.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Validación flexible (puede ser el mismo día)
            if (fechaFin.isBefore(fechaInicio)) {
                JOptionPane.showMessageDialog(this,
                        String.format("La fecha de fin (%s) debe ser igual o posterior a la fecha de inicio (%s)",
                                fechaFin.format(formatter),
                                fechaInicio.format(formatter)),
                        "Error de fechas", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al validar fechas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
