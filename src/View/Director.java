/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.CoordinadorDeActividadesController;
import Model.Entities.CoordinadorDeActividades;
import Controller.EnfermeraController;
import Controller.GuardiaController;
import Controller.PersonalControlController;
import DAO.CoordinadorDeActividadesDAO;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import Model.Constants.RolEnum;
import Model.Entities.PersonalControl;
import View.FrmCamara;
import Controller.CoordinadorDeActividadesController;
import Model.Entities.OficialDeRegistro;
import Controller.OficialController;
import DAO.EnfermeraDAO;
import DAO.GuardiaDAO;
import Model.Constants.RolEnum;
import Utilidades.EmailSender;
import java.awt.Image;
import java.io.File;
import Model.Entities.Oficial;
import java.time.LocalDate;
import Model.Entities.Usuario;
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import Controller.OficialController;
import Controller.OficialDeRegistroController;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import Model.Entities.PersonalControl;
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
import javax.swing.JPanel;
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
import Utilidades.ModernTopMenu;
import View.PerfilUsuario;
import Model.Entities.Usuario;

/**
 *
 * @author Sharlok
 */
public class Director extends javax.swing.JFrame implements PerfilUsuario {

    EnfermeraDAO enfermeraDAO = new EnfermeraDAO();
    private Usuario usuario;
    private EnfermeraController enfermeraController;
    private OficialController oficialController;
    private OficialDeRegistroController oficialRegistro;
    private CoordinadorDeActividadesController coordinadorController;
    private GuardiaController guardiaController;
    private PersonalControlController personalControl;

    private BufferedImage imagenCapturadaModificacion;
    private String rutaImagenSeleccionada = "";
    public boolean imagenFueModificada;

    private File rutaImagenGuardia;
    private File rutaImagenGuardiaMod;
    private File imagenSeleccionadaCDA;
    private File imagenSeleccionadaModCDA;
    private File imagenSeleccionadaODR;
    private File imagenSeleccionadaModODR;
    private File rutaImagenEnfermera;
    private File rutaImagenEnfermeraMod;
    private File rutaImagenOficial;
    private File rutaImagenOficialMod;
    private File rutaImagenPDC;
    private File rutaImagenPDCMod;

    private PersonalControlController controller;
    private File imagenPDCSeleccionada;
    private String cedulaActualModificacion;
    private ModernTopMenu menuSuperior; // Declaración

    public Director() {
        initComponents();

        txtFechaContratacion.setEditable(false);
        txtFechaContratacion.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        txtFechaContratacion2.setEditable(false);
        txtFechaContratacion2.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        txtFechaContratacion3.setEditable(false);
        txtFechaContratacion3.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        txtFechaContratacion4.setEditable(false);
        txtFechaContratacion4.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        txtFechaContratacion5.setEditable(false);
        txtFechaContratacion5.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        txtFechaContratacion1.setEditable(false);
        txtFechaContratacionMod1.setEditable(false);
        txtSexo.setEditable(false);
        txtSexo.setText("Femenino");
        txtSexoMod.setEditable(false);
        txtSexoMod.setText("Femenino");
        txtSexo2.setEditable(false);
        txtSexo2.setText("Femenino");
        txtSexoMod2.setEditable(false);
        txtSexoMod2.setText("Femenino");
        txtFechaContratacion1.setText(LocalDate.now().toString());
        ToolTipManager.sharedInstance().setInitialDelay(10);
        txtFechaContratacionMod2.setText(LocalDate.now().toString());
        ToolTipManager.sharedInstance().setInitialDelay(10);

        guardiaController = GuardiaController.getInstancia();
        enfermeraController = EnfermeraController.getInstancia();
        coordinadorController = CoordinadorDeActividadesController.getInstancia();
        guardiaController = guardiaController.getInstancia();
        oficialController = OficialController.getInstancia();
        oficialRegistro = OficialDeRegistroController.getInstancia();
        personalControl = PersonalControlController.getInstancia();

        jDateChooserFinContrato = new JDateChooser();
        jDateChooserFinContrato.setDateFormatString("dd/MM/yyyy");

        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 1); // Fecha mínima = mañana
        jDateChooserFinContrato.setMinSelectableDate(calendario.getTime());

        calendario.add(Calendar.MONTH, 1);
        jDateChooserFinContrato.setDate(calendario.getTime());

        actualizarTablaGuardias();
        actualizarTablaEnfermeras();
        actualizarTablaCDA();
        actualizarTablaOficiales();
        actualizarTablaODR();
        actualizarTablaPDC();

        // Configurar el tabbed pane (ocultar las pestañas pero mantener la funcionalidad)
        tabPrincipal.setUI(null);  // Oculta las pestañas del tabbedPane

        // Crear y configurar el menú moderno
        menuSuperior = new ModernTopMenu(tabPrincipal);

        // Colores adaptados para combinar con [20,25,40] y [29,35,51]
        menuSuperior.setColors(
                new Color(29, 35, 51), // Azul principal (más claro)
                new Color(41, 50, 65), // Azul hover (ligeramente más claro que el principal)
                Color.WHITE, // Texto blanco
                new Color(20, 25, 40), // Fondo oscuro (más oscuro de los dos)
                new Color(24, 30, 45) // Fondo desplegable (intermedio)
        );

        // Añadir el menú al contenedor principal, en la parte superior
        // NOTA: Es importante mantener el BorderLayout para que esto funcione correctamente
        getContentPane().setLayout(new BorderLayout());

        // 1. Obtenemos el panel donde está el tabPrincipal actualmente
        Component[] components = getContentPane().getComponents();
        JPanel mainPanel = null;

        for (Component comp : components) {
            if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0) {
                // Guardamos una referencia al panel principal
                mainPanel = (JPanel) comp;
                break;
            }
        }

        // 2. Reorganizamos los componentes
        if (mainPanel != null) {
            // Eliminamos el panel principal del contentPane
            getContentPane().remove(mainPanel);

            // Añadimos el menú en la parte superior
            getContentPane().add(menuSuperior, BorderLayout.NORTH);

            // Añadimos de nuevo el panel principal en el centro
            getContentPane().add(mainPanel, BorderLayout.CENTER);
        } else {
            // Si no se encontró un panel, simplemente añadimos el menú arriba
            getContentPane().add(menuSuperior, BorderLayout.NORTH);
        }

        // Ajustar el tamaño del frame para acomodar el menú
        pack();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        mostrarDatosUsuario();
    }

    private void mostrarDatosUsuario() {
        if (usuario != null) {
            // Mostrar información básica
            lblNombre.setText(usuario.getPrimerNombre() + " " + usuario.getPrimerApellido());
            lblRol.setText(usuario.getRol().toString());

            // Mostrar imagen
            cargarImagenUsuario();
        }
    }

    private void cargarImagenUsuario() {
        try {
            ImageIcon icon = new ImageIcon(usuario.getRutaImagen());
            Image img = icon.getImage().getScaledInstance(
                    fotolbl.getWidth(), fotolbl.getHeight(), Image.SCALE_SMOOTH);
            fotolbl.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            cargarImagenPorDefecto();
        }
    }

    private void cargarImagenPorDefecto() {
        lblFoto.setIcon(new ImageIcon("src/Resources/default_avatar.png"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        Modificar = new javax.swing.JMenuItem();
        Eliminar = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        ModificarEnfermera = new javax.swing.JMenuItem();
        EliminarEnfermera = new javax.swing.JMenuItem();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        ModificarCDA = new javax.swing.JMenuItem();
        EliminarCDA = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        modOf = new javax.swing.JMenuItem();
        BorrarOf = new javax.swing.JMenuItem();
        jPopupMenu5 = new javax.swing.JPopupMenu();
        modificarODR = new javax.swing.JMenuItem();
        EliminarODR = new javax.swing.JMenuItem();
        jPopupMenu6 = new javax.swing.JPopupMenu();
        modificarOPC = new javax.swing.JMenuItem();
        eliminarPDC = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        tabPrincipal = new javax.swing.JTabbedPane();
        Director = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        fotolbl = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();
        jSeparator125 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jSeparator126 = new javax.swing.JSeparator();
        DisminuirSentencia = new javax.swing.JPanel();
        AñadirDelito = new javax.swing.JPanel();
        AñadirEnfermera = new javax.swing.JPanel();
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
        jPanel14 = new javax.swing.JPanel();
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
        jLabel34 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
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
        jPanel18 = new javax.swing.JPanel();
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
        jLabel95 = new javax.swing.JLabel();
        MostrarEnfermeras = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEnfermeras = new javax.swing.JTable();
        AñadirGuardia = new javax.swing.JPanel();
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
        jPanel17 = new javax.swing.JPanel();
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
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSexo = new javax.swing.JTextField();
        txtNacionalidad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtEdad = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        txtSegundoApellido = new javax.swing.JTextField();
        txtPrimerApellido = new javax.swing.JTextField();
        txtPrimerNombre = new javax.swing.JTextField();
        txtSegundoNombre = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
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
        jPanel19 = new javax.swing.JPanel();
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
        jLabel94 = new javax.swing.JLabel();
        ListaDeGuardias = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaGuardias = new javax.swing.JTable();
        AgregarCDA = new javax.swing.JPanel();
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
        jPanel20 = new javax.swing.JPanel();
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
        jLabel52 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        ModificarCoordinador = new javax.swing.JPanel();
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
        jPanel21 = new javax.swing.JPanel();
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
        jLabel96 = new javax.swing.JLabel();
        mostrarCoordinadora = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCoordinadores = new javax.swing.JTable();
        añadirODR = new javax.swing.JPanel();
        AñadirEnfermera1 = new javax.swing.JPanel();
        jPanel27 = new RoundedPanel(30);
        jSeparator85 = new javax.swing.JSeparator();
        jSeparator86 = new javax.swing.JSeparator();
        jSeparator87 = new javax.swing.JSeparator();
        jSeparator88 = new javax.swing.JSeparator();
        jSeparator89 = new javax.swing.JSeparator();
        jSeparator90 = new javax.swing.JSeparator();
        jSeparator91 = new javax.swing.JSeparator();
        jSeparator92 = new javax.swing.JSeparator();
        jSeparator93 = new javax.swing.JSeparator();
        jButton18 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        lblImagen4 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        txtFechaContratacion4 = new javax.swing.JTextField();
        dateFinContrato4 = new com.toedter.calendar.JDateChooser();
        jLabel128 = new javax.swing.JLabel();
        cmbTurno4 = new javax.swing.JComboBox<>();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        txtCorreo4 = new javax.swing.JTextField();
        jSeparator94 = new javax.swing.JSeparator();
        jButton19 = new javax.swing.JButton();
        jLabel131 = new javax.swing.JLabel();
        txtNacionalidad4 = new javax.swing.JTextField();
        txtSexo4 = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        txtCedula4 = new javax.swing.JTextField();
        txtEdad4 = new javax.swing.JTextField();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        txtSegundoApellido4 = new javax.swing.JTextField();
        txtPrimerApellido4 = new javax.swing.JTextField();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        txtSegundoNombre4 = new javax.swing.JTextField();
        txtPrimerNombre4 = new javax.swing.JTextField();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        mostrarODR = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaRegistradoras = new javax.swing.JTable();
        jButton13 = new javax.swing.JButton();
        AñadirOficial = new javax.swing.JPanel();
        jPanel9 = new RoundedPanel(30);
        jSeparator31 = new javax.swing.JSeparator();
        jSeparator66 = new javax.swing.JSeparator();
        jSeparator67 = new javax.swing.JSeparator();
        jSeparator68 = new javax.swing.JSeparator();
        jSeparator69 = new javax.swing.JSeparator();
        jSeparator70 = new javax.swing.JSeparator();
        jSeparator71 = new javax.swing.JSeparator();
        jSeparator72 = new javax.swing.JSeparator();
        jSeparator73 = new javax.swing.JSeparator();
        jButton14 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        lblImagen3 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        txtFechaContratacion3 = new javax.swing.JTextField();
        dateFinContrato3 = new com.toedter.calendar.JDateChooser();
        jLabel101 = new javax.swing.JLabel();
        cmbTurno3 = new javax.swing.JComboBox<>();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        txtCorreo3 = new javax.swing.JTextField();
        jSeparator74 = new javax.swing.JSeparator();
        jButton15 = new javax.swing.JButton();
        jLabel104 = new javax.swing.JLabel();
        txtNacionalidad3 = new javax.swing.JTextField();
        txtSexo3 = new javax.swing.JTextField();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        txtCedula3 = new javax.swing.JTextField();
        txtEdad3 = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        txtSegundoApellido3 = new javax.swing.JTextField();
        txtPrimerApellido3 = new javax.swing.JTextField();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        txtSegundoNombre3 = new javax.swing.JTextField();
        txtPrimerNombre3 = new javax.swing.JTextField();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        ModificarOficial = new javax.swing.JPanel();
        modificarOficial = new javax.swing.JPanel();
        jPanel24 = new RoundedPanel(30);
        jSeparator75 = new javax.swing.JSeparator();
        jSeparator76 = new javax.swing.JSeparator();
        jSeparator77 = new javax.swing.JSeparator();
        jSeparator78 = new javax.swing.JSeparator();
        jSeparator79 = new javax.swing.JSeparator();
        jSeparator80 = new javax.swing.JSeparator();
        jSeparator81 = new javax.swing.JSeparator();
        jSeparator82 = new javax.swing.JSeparator();
        jSeparator83 = new javax.swing.JSeparator();
        jButton16 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        lblImagenMod3 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        txtFechaContratacionMod3 = new javax.swing.JTextField();
        dateFinContratoMod3 = new com.toedter.calendar.JDateChooser();
        jLabel116 = new javax.swing.JLabel();
        cmbTurnoMod3 = new javax.swing.JComboBox<>();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        txtCorreoMod3 = new javax.swing.JTextField();
        jSeparator84 = new javax.swing.JSeparator();
        jButton17 = new javax.swing.JButton();
        jLabel119 = new javax.swing.JLabel();
        txtNacionalidadMod3 = new javax.swing.JTextField();
        txtSexoMod3 = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        txtCedulaMod3 = new javax.swing.JTextField();
        txtEdadMod3 = new javax.swing.JTextField();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        txtSegundoApellidoMod3 = new javax.swing.JTextField();
        txtPrimerApellidoMod3 = new javax.swing.JTextField();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        txtSegundoNombreMod3 = new javax.swing.JTextField();
        txtPrimerNombreMod3 = new javax.swing.JTextField();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        mostrarOficial = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaOficial = new javax.swing.JTable();
        Credenciales = new javax.swing.JPanel();
        ModificarODR = new javax.swing.JPanel();
        modODR = new javax.swing.JPanel();
        jPanel30 = new RoundedPanel(30);
        jSeparator95 = new javax.swing.JSeparator();
        jSeparator96 = new javax.swing.JSeparator();
        jSeparator97 = new javax.swing.JSeparator();
        jSeparator98 = new javax.swing.JSeparator();
        jSeparator99 = new javax.swing.JSeparator();
        jSeparator100 = new javax.swing.JSeparator();
        jSeparator101 = new javax.swing.JSeparator();
        jSeparator102 = new javax.swing.JSeparator();
        jSeparator103 = new javax.swing.JSeparator();
        jButton20 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        lblImagenMod4 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        txtFechaContratacionMod4 = new javax.swing.JTextField();
        dateFinContratoMod4 = new com.toedter.calendar.JDateChooser();
        jLabel143 = new javax.swing.JLabel();
        cmbTurnoMod4 = new javax.swing.JComboBox<>();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        txtCorreoMod4 = new javax.swing.JTextField();
        jSeparator104 = new javax.swing.JSeparator();
        jButton21 = new javax.swing.JButton();
        jLabel146 = new javax.swing.JLabel();
        txtNacionalidadMod4 = new javax.swing.JTextField();
        txtSexoMod4 = new javax.swing.JTextField();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        txtCedulaMod4 = new javax.swing.JTextField();
        txtEdadMod4 = new javax.swing.JTextField();
        jLabel149 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        txtSegundoApellidoMod4 = new javax.swing.JTextField();
        txtPrimerApellidoMod4 = new javax.swing.JTextField();
        jLabel151 = new javax.swing.JLabel();
        jLabel152 = new javax.swing.JLabel();
        txtSegundoNombreMod4 = new javax.swing.JTextField();
        txtPrimerNombreMod4 = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        aggPDC = new javax.swing.JPanel();
        AñadirEnfermera2 = new javax.swing.JPanel();
        jPanel35 = new RoundedPanel(30);
        jSeparator105 = new javax.swing.JSeparator();
        jSeparator106 = new javax.swing.JSeparator();
        jSeparator107 = new javax.swing.JSeparator();
        jSeparator108 = new javax.swing.JSeparator();
        jSeparator109 = new javax.swing.JSeparator();
        jSeparator110 = new javax.swing.JSeparator();
        jSeparator111 = new javax.swing.JSeparator();
        jSeparator112 = new javax.swing.JSeparator();
        jSeparator113 = new javax.swing.JSeparator();
        jButton22 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        lblImagen5 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        txtFechaContratacion5 = new javax.swing.JTextField();
        dateFinContrato5 = new com.toedter.calendar.JDateChooser();
        jLabel155 = new javax.swing.JLabel();
        cmbTurno5 = new javax.swing.JComboBox<>();
        jLabel156 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        txtCorreo5 = new javax.swing.JTextField();
        jSeparator114 = new javax.swing.JSeparator();
        jButton23 = new javax.swing.JButton();
        jLabel158 = new javax.swing.JLabel();
        txtNacionalidad5 = new javax.swing.JTextField();
        txtSexo5 = new javax.swing.JTextField();
        jLabel159 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        txtCedula5 = new javax.swing.JTextField();
        txtEdad5 = new javax.swing.JTextField();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        txtSegundoApellido5 = new javax.swing.JTextField();
        txtPrimerApellido5 = new javax.swing.JTextField();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        txtSegundoNombre5 = new javax.swing.JTextField();
        txtPrimerNombre5 = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jLabel167 = new javax.swing.JLabel();
        modPDC = new javax.swing.JPanel();
        modPersonal = new javax.swing.JPanel();
        jPanel38 = new RoundedPanel(30);
        jSeparator115 = new javax.swing.JSeparator();
        jSeparator116 = new javax.swing.JSeparator();
        jSeparator117 = new javax.swing.JSeparator();
        jSeparator118 = new javax.swing.JSeparator();
        jSeparator119 = new javax.swing.JSeparator();
        jSeparator120 = new javax.swing.JSeparator();
        jSeparator121 = new javax.swing.JSeparator();
        jSeparator122 = new javax.swing.JSeparator();
        jSeparator123 = new javax.swing.JSeparator();
        jButton24 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        lblImagenMod5 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jLabel169 = new javax.swing.JLabel();
        txtFechaContratacionMod5 = new javax.swing.JTextField();
        dateFinContratoMod5 = new com.toedter.calendar.JDateChooser();
        jLabel170 = new javax.swing.JLabel();
        cmbTurnoMod5 = new javax.swing.JComboBox<>();
        jLabel171 = new javax.swing.JLabel();
        jLabel172 = new javax.swing.JLabel();
        txtCorreoMod5 = new javax.swing.JTextField();
        jSeparator124 = new javax.swing.JSeparator();
        jButton25 = new javax.swing.JButton();
        jLabel173 = new javax.swing.JLabel();
        txtNacionalidadMod5 = new javax.swing.JTextField();
        txtSexoMod5 = new javax.swing.JTextField();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        txtCedulaMod5 = new javax.swing.JTextField();
        txtEdadMod5 = new javax.swing.JTextField();
        jLabel176 = new javax.swing.JLabel();
        jLabel177 = new javax.swing.JLabel();
        txtSegundoApellidoMod5 = new javax.swing.JTextField();
        txtPrimerApellidoMod5 = new javax.swing.JTextField();
        jLabel178 = new javax.swing.JLabel();
        jLabel179 = new javax.swing.JLabel();
        txtSegundoNombreMod5 = new javax.swing.JTextField();
        txtPrimerNombreMod5 = new javax.swing.JTextField();
        jLabel180 = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        mostrarPDC = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        personalTabla = new javax.swing.JTable();

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

        ModificarCDA.setText("Modificar");
        ModificarCDA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarCDAActionPerformed(evt);
            }
        });
        jPopupMenu4.add(ModificarCDA);

        EliminarCDA.setText("Eliminar");
        EliminarCDA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarCDAActionPerformed(evt);
            }
        });
        jPopupMenu4.add(EliminarCDA);

        modOf.setText("Modificar");
        modOf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modOfActionPerformed(evt);
            }
        });
        jPopupMenu3.add(modOf);

        BorrarOf.setText("Eliminar");
        BorrarOf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarOfActionPerformed(evt);
            }
        });
        jPopupMenu3.add(BorrarOf);

        modificarODR.setText("jMenuItem1");
        modificarODR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarODRActionPerformed(evt);
            }
        });
        jPopupMenu5.add(modificarODR);

        EliminarODR.setText("jMenuItem1");
        EliminarODR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarODRActionPerformed(evt);
            }
        });
        jPopupMenu5.add(EliminarODR);

        modificarOPC.setText("jMenuItem1");
        modificarOPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarOPCActionPerformed(evt);
            }
        });
        jPopupMenu6.add(modificarOPC);

        eliminarPDC.setText("jMenuItem2");
        eliminarPDC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarPDCActionPerformed(evt);
            }
        });
        jPopupMenu6.add(eliminarPDC);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(20, 25, 40));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Director.setBackground(new java.awt.Color(255, 255, 255));
        Director.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(20, 25, 40));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fotolbl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel6.add(fotolbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 270, 190));

        lblNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 300, 210, 30));

        lblRol.setBackground(new java.awt.Color(255, 255, 255));
        lblRol.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRol.setForeground(new java.awt.Color(255, 255, 255));
        lblRol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(lblRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 340, 150, 20));
        jPanel6.add(jSeparator125, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 360, 150, 20));

        jLabel20.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("LE DAMOS LA BIENVENIDA.");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, -1, -1));
        jPanel6.add(jSeparator126, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 330, 210, 20));

        Director.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 880, 450));

        tabPrincipal.addTab("Director", Director);
        tabPrincipal.addTab("DisminuirSentencia", DisminuirSentencia);
        tabPrincipal.addTab("AñadirDelito", AñadirDelito);

        AñadirEnfermera.setBackground(new java.awt.Color(20, 25, 40));
        AñadirEnfermera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(29, 35, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jSeparator23, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 310, 20));
        jPanel7.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 310, 20));
        jPanel7.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 310, 20));
        jPanel7.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 310, 20));
        jPanel7.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 310, 20));
        jPanel7.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 310, 20));
        jPanel7.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 310, 20));
        jPanel7.add(jSeparator30, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 310, 20));
        jPanel7.add(jSeparator32, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, 310, 20));

        jButton5.setText("Añadir");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 460, 180, 40));

        jPanel8.setBackground(new java.awt.Color(0, 0, 51));
        jPanel8.setRequestFocusEnabled(false);
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(102, 102, 102));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImagen1.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.add(lblImagen1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel8.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel31.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Agregar Foto de la enfermera:");
        jPanel7.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 30));

        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Fecha de contratación:");
        jPanel7.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 330, -1, 20));

        txtFechaContratacion1.setEditable(false);
        jPanel7.add(txtFechaContratacion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 350, 190, 30));
        jPanel7.add(dateFinContrato1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, 190, 30));

        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Fecha de finalización del contrato:");
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, -1, 20));

        cmbTurno1.setForeground(new java.awt.Color(255, 255, 255));
        cmbTurno1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel7.add(cmbTurno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 240, 30));

        jLabel35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Turno:");
        jPanel7.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Correo:");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));

        txtCorreo1.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo1.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel7.add(txtCorreo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 240, 30));
        jPanel7.add(jSeparator33, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 310, 20));

        jButton6.setText("Seleccionar Imagen");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Nacionalidad:");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 90, -1));

        txtNacionalidad1.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad1.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidad1.setBorder(null);
        txtNacionalidad1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad1KeyTyped(evt);
            }
        });
        jPanel7.add(txtNacionalidad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 210, 30));

        txtSexo1.setEditable(false);
        txtSexo1.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo1.setForeground(new java.awt.Color(255, 255, 255));
        txtSexo1.setText("Femenino");
        txtSexo1.setBorder(null);
        jPanel7.add(txtSexo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 250, 30));

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Genero");
        jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, -1, -1));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Cedula:");
        jPanel7.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, 30));

        txtCedula1.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula1.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel7.add(txtCedula1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 240, 30));

        txtEdad1.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad1.setForeground(new java.awt.Color(255, 255, 255));
        txtEdad1.setBorder(null);
        txtEdad1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad1KeyTyped(evt);
            }
        });
        jPanel7.add(txtEdad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 250, 30));

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Edad:");
        jPanel7.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Segundo Apellido:");
        jPanel7.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, 20));

        txtSegundoApellido1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido1.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellido1.setBorder(null);
        txtSegundoApellido1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido1KeyTyped(evt);
            }
        });
        jPanel7.add(txtSegundoApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 180, 30));

        txtPrimerApellido1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido1.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellido1.setBorder(null);
        txtPrimerApellido1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido1KeyTyped(evt);
            }
        });
        jPanel7.add(txtPrimerApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 190, 30));

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Primer Apellido:");
        jPanel7.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 20));

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Segundo nombre:");
        jPanel7.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, 30));

        txtSegundoNombre1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre1.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombre1.setBorder(null);
        txtSegundoNombre1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre1KeyTyped(evt);
            }
        });
        jPanel7.add(txtSegundoNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 180, 30));

        txtPrimerNombre1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre1.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombre1.setBorder(null);
        txtPrimerNombre1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre1KeyTyped(evt);
            }
        });
        jPanel7.add(txtPrimerNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 200, 30));

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Primer nombre:");
        jPanel7.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 20));

        AñadirEnfermera.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel34.setFont(new java.awt.Font("Arial", 2, 13)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Asegurese de ingresar correctamente la cedula ya que esta no podrá ser modificada más adelante.");
        AñadirEnfermera.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel37.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(153, 0, 0));
        jLabel37.setText("TODOS LOS DATOS SON DE CARACTER OBLIGATORIO A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        AñadirEnfermera.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, 30));

        tabPrincipal.addTab("AñadirEnfermera", AñadirEnfermera);

        ModificarNurse.setBackground(new java.awt.Color(20, 25, 40));
        ModificarNurse.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(29, 35, 51));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel10.add(jSeparator35, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel10.add(jSeparator36, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel10.add(jSeparator37, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel10.add(jSeparator38, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel10.add(jSeparator39, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel10.add(jSeparator40, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel10.add(jSeparator41, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel10.add(jSeparator43, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton7.setText("Modificar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel11.setBackground(new java.awt.Color(0, 0, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(102, 102, 102));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel18.add(lblImagenMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel11.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Modificar foto de la enfermera:");
        jPanel10.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, -1, -1));

        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Fecha de contratación:");
        jPanel10.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, -1, 20));

        txtFechaContratacionMod1.setEditable(false);
        jPanel10.add(txtFechaContratacionMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 360, 190, 30));
        jPanel10.add(dateFinContratoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 190, 30));

        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Fecha de finalización del contrato:");
        jPanel10.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, -1, -1));

        cmbTurnoMod1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel10.add(cmbTurnoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 250, 30));

        jLabel50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Turno:");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        jLabel51.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Correo:");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, -1, -1));

        txtCorreoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod1.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel10.add(txtCorreoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 240, 30));
        jPanel10.add(jSeparator44, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton8.setText("Seleccionar Imagen");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Nacionalidad:");
        jPanel10.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 90, -1));

        txtNacionalidadMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod1.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidadMod1.setBorder(null);
        txtNacionalidadMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtNacionalidadMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 30));

        txtSexoMod1.setEditable(false);
        txtSexoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod1.setForeground(new java.awt.Color(255, 255, 255));
        txtSexoMod1.setText("Femenino");
        txtSexoMod1.setBorder(null);
        jPanel10.add(txtSexoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 250, 30));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Genero");
        jPanel10.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Cedula:");
        jPanel10.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        txtCedulaMod1.setEditable(false);
        txtCedulaMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod1.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel10.add(txtCedulaMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 240, 30));

        txtEdadMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod1.setForeground(new java.awt.Color(255, 255, 255));
        txtEdadMod1.setBorder(null);
        txtEdadMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadMod1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtEdadMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 250, 30));

        jLabel56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Edad:");
        jPanel10.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel57.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Segundo Apellido:");
        jPanel10.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        txtSegundoApellidoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod1.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellidoMod1.setBorder(null);
        txtSegundoApellidoMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtSegundoApellidoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 190, 30));

        txtPrimerApellidoMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod1.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellidoMod1.setBorder(null);
        txtPrimerApellidoMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtPrimerApellidoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 200, 30));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Primer Apellido:");
        jPanel10.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel59.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Segundo nombre:");
        jPanel10.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        txtSegundoNombreMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod1.setBorder(null);
        txtSegundoNombreMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtSegundoNombreMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 180, 30));

        txtPrimerNombreMod1.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod1.setBorder(null);
        txtPrimerNombreMod1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreMod1KeyTyped(evt);
            }
        });
        jPanel10.add(txtPrimerNombreMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        jLabel60.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Primer nombre:");
        jPanel10.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        ModificarNurse.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel95.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(153, 0, 0));
        jLabel95.setText("NINGUN CAMPO DEBE ESTAR VACIO AL MODIFICAR, A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        ModificarNurse.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, 30));

        tabPrincipal.addTab("Modificar enfermera", ModificarNurse);

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

        tabPrincipal.addTab("MostrarEnfermera", MostrarEnfermeras);

        AñadirGuardia.setBackground(new java.awt.Color(20, 25, 40));
        AñadirGuardia.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(29, 35, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel3.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel3.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel3.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel3.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel3.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel3.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel3.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 310, 20));
        jPanel3.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton1.setText("Contratar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(lblFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel17.setBackground(new java.awt.Color(102, 102, 102));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel17.setForeground(new java.awt.Color(102, 102, 102));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 310, 220));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Agregar Foto de la guardia:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, -1, -1));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Fecha de contratación:");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, -1, 20));

        txtFechaContratacion.setEditable(false);
        txtFechaContratacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaContratacionActionPerformed(evt);
            }
        });
        jPanel3.add(txtFechaContratacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 400, 190, 30));
        jPanel3.add(jDateChooserFinContrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 460, 190, 30));

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Fecha de finalización del contrato:");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 440, -1, -1));

        cmbCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Guardia jefe", "Guardia" }));
        jPanel3.add(cmbCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 260, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cargo:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, -1, -1));

        cmbTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel3.add(cmbTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 240, 30));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Turno:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Correo:");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, -1, -1));

        txtCorreo.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel3.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 240, 30));
        jPanel3.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton2.setText("Seleccionar Imagen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 330, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nacionalidad:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Genero:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        txtSexo.setEditable(false);
        txtSexo.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo.setForeground(new java.awt.Color(255, 255, 255));
        txtSexo.setText("Femenino");
        txtSexo.setBorder(null);
        jPanel3.add(txtSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 260, 30));

        txtNacionalidad.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidad.setBorder(null);
        txtNacionalidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadKeyTyped(evt);
            }
        });
        jPanel3.add(txtNacionalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 220, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Segundo Apellido:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Primer Apellido:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Segundo nombre:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cedula:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Edad:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        txtEdad.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad.setForeground(new java.awt.Color(255, 255, 255));
        txtEdad.setBorder(null);
        txtEdad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadKeyTyped(evt);
            }
        });
        jPanel3.add(txtEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 240, 30));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Primer nombre:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        txtCedula.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel3.add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 250, 30));

        txtSegundoApellido.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellido.setBorder(null);
        txtSegundoApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoKeyTyped(evt);
            }
        });
        jPanel3.add(txtSegundoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 190, 30));

        txtPrimerApellido.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellido.setBorder(null);
        txtPrimerApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoKeyTyped(evt);
            }
        });
        jPanel3.add(txtPrimerApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 200, 30));

        txtPrimerNombre.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombre.setBorder(null);
        txtPrimerNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreKeyTyped(evt);
            }
        });
        jPanel3.add(txtPrimerNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        txtSegundoNombre.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombre.setBorder(null);
        txtSegundoNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreKeyTyped(evt);
            }
        });
        jPanel3.add(txtSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 190, 30));

        AñadirGuardia.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel90.setFont(new java.awt.Font("Arial", 2, 13)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(255, 255, 255));
        jLabel90.setText("Asegurese de ingresar correctamente la cedula ya que esta no podrá ser modificada más adelante.");
        AñadirGuardia.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel91.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(153, 0, 0));
        jLabel91.setText("TODOS LOS DATOS SON DE CARACTER OBLIGATORIO A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        AñadirGuardia.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, 30));

        tabPrincipal.addTab("Añadir un guardia", AñadirGuardia);

        ModificarGuardia.setBackground(new java.awt.Color(20, 25, 40));
        ModificarGuardia.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(29, 35, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel4.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel4.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel4.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel4.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel4.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel4.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel4.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel4.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel4.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 310, 20));
        jPanel4.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton3.setText("Modificar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel5.setBackground(new java.awt.Color(0, 0, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBackground(new java.awt.Color(102, 102, 102));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel19.add(lblImagenMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel5.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 310, 220));

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Agregar Foto de la guardia:");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, -1, -1));

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Fecha de contratación:");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 360, -1, 20));

        txtFechaContratacionMod.setEditable(false);
        jPanel4.add(txtFechaContratacionMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, 190, 30));
        jPanel4.add(dateFinContratoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 440, 190, 30));

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Fecha de finalización del contrato:");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, -1, -1));

        txtCargoMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Guardia jefe", "Guardia" }));
        jPanel4.add(txtCargoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 250, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Cargo:");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, -1));

        cmbTurnoMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel4.add(cmbTurnoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 250, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Turno:");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        txtCorreoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel4.add(txtCorreoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 250, 30));
        jPanel4.add(jSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton4.setText("Seleccionar Imagen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 320, -1, -1));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Correo:");
        jPanel4.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, -1, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Nacionalidad:");
        jPanel4.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 90, -1));

        txtNacionalidadMod.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidadMod.setBorder(null);
        txtNacionalidadMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadModKeyTyped(evt);
            }
        });
        jPanel4.add(txtNacionalidadMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 30));

        txtSexoMod.setEditable(false);
        txtSexoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod.setForeground(new java.awt.Color(255, 255, 255));
        txtSexoMod.setText("Femenino");
        txtSexoMod.setBorder(null);
        jPanel4.add(txtSexoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 250, 30));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Genero");
        jPanel4.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Cedula:");
        jPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        txtCedulaMod.setEditable(false);
        txtCedulaMod.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel4.add(txtCedulaMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 250, 30));

        txtEdadMod.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod.setForeground(new java.awt.Color(255, 255, 255));
        txtEdadMod.setBorder(null);
        txtEdadMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadModKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadModKeyTyped(evt);
            }
        });
        jPanel4.add(txtEdadMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 250, 30));

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Edad:");
        jPanel4.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Segundo Apellido:");
        jPanel4.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        txtSegundoApellidoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellidoMod.setBorder(null);
        txtSegundoApellidoMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoModKeyTyped(evt);
            }
        });
        jPanel4.add(txtSegundoApellidoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 190, 30));

        txtPrimerApellidoMod.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellidoMod.setBorder(null);
        txtPrimerApellidoMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoModKeyTyped(evt);
            }
        });
        jPanel4.add(txtPrimerApellidoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 190, 30));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Primer Apellido:");
        jPanel4.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Segundo nombre:");
        jPanel4.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        txtSegundoNombreMod.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombreMod.setBorder(null);
        txtSegundoNombreMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreModKeyTyped(evt);
            }
        });
        jPanel4.add(txtSegundoNombreMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 180, 30));

        txtPrimerNombreMod.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombreMod.setBorder(null);
        txtPrimerNombreMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreModKeyTyped(evt);
            }
        });
        jPanel4.add(txtPrimerNombreMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Primer nombre:");
        jPanel4.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        ModificarGuardia.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel94.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(153, 0, 0));
        jLabel94.setText("NINGUN CAMPO DEBE ESTAR VACIO AL MODIFICAR, A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        ModificarGuardia.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, 30));

        tabPrincipal.addTab("ModificarGuardia", ModificarGuardia);

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

        tabPrincipal.addTab("ListaDeGuardias", ListaDeGuardias);

        AgregarCDA.setBackground(new java.awt.Color(20, 25, 40));
        AgregarCDA.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(29, 35, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel12.add(jSeparator45, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel12.add(jSeparator46, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel12.add(jSeparator47, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel12.add(jSeparator48, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel12.add(jSeparator49, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel12.add(jSeparator50, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel12.add(jSeparator51, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel12.add(jSeparator52, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 310, 20));
        jPanel12.add(jSeparator53, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton9.setText("Contratar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel13.setBackground(new java.awt.Color(0, 0, 51));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBackground(new java.awt.Color(102, 102, 102));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel20.add(lblImagen2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel13.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 310, 220));

        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Agregar Foto de la coordinadora");
        jPanel12.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, -1, -1));

        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Fecha de contratación:");
        jPanel12.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 370, -1, 20));

        txtFechaContratacion2.setEditable(false);
        jPanel12.add(txtFechaContratacion2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 390, 190, 30));
        jPanel12.add(dateFinContrato2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 190, 30));

        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Fecha de finalización del contrato:");
        jPanel12.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, -1, -1));

        cmbCargo2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Coordinadora mañana", "coordinadora tarde" }));
        jPanel12.add(cmbCargo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 250, 30));

        jLabel63.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Cargo:");
        jPanel12.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, -1));

        cmbTurno2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel12.add(cmbTurno2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 260, 30));

        jLabel64.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Turno:");
        jPanel12.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        jLabel65.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Correo:");
        jPanel12.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, -1, -1));

        txtCorreo2.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo2.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel12.add(txtCorreo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 240, 30));
        jPanel12.add(jSeparator54, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton10.setText("Seleccionar Imagen");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 330, -1, -1));

        jLabel67.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Nacionalidad:");
        jPanel12.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 90, -1));

        txtNacionalidad2.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad2.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidad2.setBorder(null);
        txtNacionalidad2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad2KeyTyped(evt);
            }
        });
        jPanel12.add(txtNacionalidad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 30));

        txtSexo2.setEditable(false);
        txtSexo2.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo2.setForeground(new java.awt.Color(255, 255, 255));
        txtSexo2.setText("Femenino");
        txtSexo2.setBorder(null);
        jPanel12.add(txtSexo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 250, 30));

        jLabel68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Genero");
        jPanel12.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel69.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Cedula:");
        jPanel12.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        txtCedula2.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula2.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel12.add(txtCedula2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 240, 30));

        txtEdad2.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad2.setForeground(new java.awt.Color(255, 255, 255));
        txtEdad2.setBorder(null);
        txtEdad2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad2KeyTyped(evt);
            }
        });
        jPanel12.add(txtEdad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 250, 30));

        jLabel70.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Edad:");
        jPanel12.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel71.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Segundo Apellido:");
        jPanel12.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        txtSegundoApellido2.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido2.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellido2.setBorder(null);
        txtSegundoApellido2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido2KeyTyped(evt);
            }
        });
        jPanel12.add(txtSegundoApellido2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 180, 30));

        txtPrimerApellido2.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido2.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellido2.setBorder(null);
        txtPrimerApellido2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido2KeyTyped(evt);
            }
        });
        jPanel12.add(txtPrimerApellido2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 190, 30));

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Primer Apellido:");
        jPanel12.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Segundo nombre:");
        jPanel12.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        txtSegundoNombre2.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre2.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombre2.setBorder(null);
        txtSegundoNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre2KeyTyped(evt);
            }
        });
        jPanel12.add(txtSegundoNombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 180, 30));

        txtPrimerNombre2.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre2.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombre2.setBorder(null);
        txtPrimerNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre2KeyTyped(evt);
            }
        });
        jPanel12.add(txtPrimerNombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        jLabel74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Primer nombre:");
        jPanel12.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        AgregarCDA.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel52.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(153, 0, 0));
        jLabel52.setText("TODOS LOS DATOS SON DE CARACTER OBLIGATORIO A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        AgregarCDA.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, 30));

        jLabel66.setFont(new java.awt.Font("Arial", 2, 13)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("Asegurese de ingresar correctamente la cedula ya que esta no podrá ser modificada más adelante.");
        AgregarCDA.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        tabPrincipal.addTab("AgregarCDA", AgregarCDA);

        ModificarCoordinador.setBackground(new java.awt.Color(20, 25, 40));
        ModificarCoordinador.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel15.add(jSeparator63, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 310, 20));
        jPanel15.add(jSeparator64, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 310, 20));

        jButton11.setText("Modificar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 170, 40));

        jPanel16.setBackground(new java.awt.Color(0, 0, 51));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel21.setBackground(new java.awt.Color(102, 102, 102));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel21.add(lblImagenMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel16.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel15.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 310, 220));

        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Modificar foto de la coordinadora");
        jPanel15.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, -1, -1));

        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Fecha de contratación:");
        jPanel15.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 360, -1, 20));

        txtFechaContratacionMod2.setEditable(false);
        jPanel15.add(txtFechaContratacionMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 380, 190, 30));
        jPanel15.add(dateFinContratoMod2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 440, 190, 30));

        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Fecha de finalización del contrato:");
        jPanel15.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 420, -1, -1));

        txtCargoMod1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Oficial", "Guardia" }));
        jPanel15.add(txtCargoMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 250, 30));

        jLabel78.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Cargo:");
        jPanel15.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, -1, -1));

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
        jPanel15.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 320, -1, -1));

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

        txtSexoMod2.setEditable(false);
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

        txtCedulaMod2.setEditable(false);
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

        ModificarCoordinador.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel96.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(153, 0, 0));
        jLabel96.setText("NINGUN CAMPO DEBE ESTAR VACIO AL MODIFICAR, A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        ModificarCoordinador.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, 30));

        tabPrincipal.addTab("ModificarCDA", ModificarCoordinador);

        mostrarCoordinadora.setBackground(new java.awt.Color(255, 255, 255));
        mostrarCoordinadora.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaCoordinadores.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaCoordinadores.setComponentPopupMenu(jPopupMenu4);
        jScrollPane3.setViewportView(tablaCoordinadores);
        if (tablaCoordinadores.getColumnModel().getColumnCount() > 0) {
            tablaCoordinadores.getColumnModel().getColumn(0).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(1).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(2).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(3).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(4).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(5).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(6).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(7).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(8).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(9).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(10).setResizable(false);
            tablaCoordinadores.getColumnModel().getColumn(11).setResizable(false);
        }

        mostrarCoordinadora.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 1080, 550));

        tabPrincipal.addTab("MostrarCDA", mostrarCoordinadora);

        añadirODR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AñadirEnfermera1.setBackground(new java.awt.Color(20, 25, 40));
        AñadirEnfermera1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel27.setBackground(new java.awt.Color(29, 35, 51));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jSeparator85, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 310, 20));
        jPanel27.add(jSeparator86, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 310, 20));
        jPanel27.add(jSeparator87, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 310, 20));
        jPanel27.add(jSeparator88, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 310, 20));
        jPanel27.add(jSeparator89, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 310, 20));
        jPanel27.add(jSeparator90, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 310, 20));
        jPanel27.add(jSeparator91, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 310, 20));
        jPanel27.add(jSeparator92, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 310, 20));
        jPanel27.add(jSeparator93, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, 310, 20));

        jButton18.setText("Añadir");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 460, 180, 40));

        jPanel28.setBackground(new java.awt.Color(0, 0, 51));
        jPanel28.setRequestFocusEnabled(false);
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel29.setBackground(new java.awt.Color(102, 102, 102));
        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImagen4.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel29.add(lblImagen4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel28.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel27.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel92.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(255, 255, 255));
        jLabel92.setText("Agregar Foto de la registradora:");
        jPanel27.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 30));

        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("Fecha de contratación:");
        jPanel27.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 330, -1, 20));

        txtFechaContratacion4.setEditable(false);
        jPanel27.add(txtFechaContratacion4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 350, 190, 30));
        jPanel27.add(dateFinContrato4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, 190, 30));

        jLabel128.setForeground(new java.awt.Color(255, 255, 255));
        jLabel128.setText("Fecha de finalización del contrato:");
        jPanel27.add(jLabel128, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, -1, 20));

        cmbTurno4.setForeground(new java.awt.Color(255, 255, 255));
        cmbTurno4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel27.add(cmbTurno4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 240, 30));

        jLabel129.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(255, 255, 255));
        jLabel129.setText("Turno:");
        jPanel27.add(jLabel129, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));

        jLabel130.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setText("Correo:");
        jPanel27.add(jLabel130, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));

        txtCorreo4.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo4.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreo4.setBorder(null);
        txtCorreo4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreo4FocusLost(evt);
            }
        });
        txtCorreo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreo4ActionPerformed(evt);
            }
        });
        txtCorreo4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreo4KeyTyped(evt);
            }
        });
        jPanel27.add(txtCorreo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 240, 30));
        jPanel27.add(jSeparator94, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 310, 20));

        jButton19.setText("Seleccionar Imagen");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 280, -1, -1));

        jLabel131.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(255, 255, 255));
        jLabel131.setText("Nacionalidad:");
        jPanel27.add(jLabel131, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 90, -1));

        txtNacionalidad4.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad4.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidad4.setBorder(null);
        txtNacionalidad4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad4KeyTyped(evt);
            }
        });
        jPanel27.add(txtNacionalidad4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 210, 30));

        txtSexo4.setEditable(false);
        txtSexo4.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo4.setForeground(new java.awt.Color(255, 255, 255));
        txtSexo4.setText("Femenino");
        txtSexo4.setBorder(null);
        jPanel27.add(txtSexo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 250, 30));

        jLabel132.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(255, 255, 255));
        jLabel132.setText("Genero");
        jPanel27.add(jLabel132, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, -1, -1));

        jLabel133.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 255, 255));
        jLabel133.setText("Cedula:");
        jPanel27.add(jLabel133, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, 30));

        txtCedula4.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula4.setForeground(new java.awt.Color(255, 255, 255));
        txtCedula4.setBorder(null);
        txtCedula4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedula4ActionPerformed(evt);
            }
        });
        txtCedula4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedula4KeyTyped(evt);
            }
        });
        jPanel27.add(txtCedula4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 240, 30));

        txtEdad4.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad4.setForeground(new java.awt.Color(255, 255, 255));
        txtEdad4.setBorder(null);
        txtEdad4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad4KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad4KeyTyped(evt);
            }
        });
        jPanel27.add(txtEdad4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 250, 30));

        jLabel134.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("Edad:");
        jPanel27.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel135.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setText("Segundo Apellido:");
        jPanel27.add(jLabel135, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, 20));

        txtSegundoApellido4.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido4.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellido4.setBorder(null);
        txtSegundoApellido4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido4KeyTyped(evt);
            }
        });
        jPanel27.add(txtSegundoApellido4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 180, 30));

        txtPrimerApellido4.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido4.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellido4.setBorder(null);
        txtPrimerApellido4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido4KeyTyped(evt);
            }
        });
        jPanel27.add(txtPrimerApellido4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 190, 30));

        jLabel136.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(255, 255, 255));
        jLabel136.setText("Primer Apellido:");
        jPanel27.add(jLabel136, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 20));

        jLabel137.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Segundo nombre:");
        jPanel27.add(jLabel137, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, 30));

        txtSegundoNombre4.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre4.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombre4.setBorder(null);
        txtSegundoNombre4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre4KeyTyped(evt);
            }
        });
        jPanel27.add(txtSegundoNombre4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 180, 30));

        txtPrimerNombre4.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre4.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombre4.setBorder(null);
        txtPrimerNombre4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre4KeyTyped(evt);
            }
        });
        jPanel27.add(txtPrimerNombre4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 200, 30));

        jLabel138.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(255, 255, 255));
        jLabel138.setText("Primer nombre:");
        jPanel27.add(jLabel138, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 20));

        AñadirEnfermera1.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel139.setFont(new java.awt.Font("Arial", 2, 13)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(255, 255, 255));
        jLabel139.setText("Asegurese de ingresar correctamente la cedula ya que esta no podrá ser modificada más adelante.");
        AñadirEnfermera1.add(jLabel139, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel140.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(153, 0, 0));
        jLabel140.setText("TODOS LOS DATOS SON DE CARACTER OBLIGATORIO A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        AñadirEnfermera1.add(jLabel140, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, 30));

        añadirODR.add(AñadirEnfermera1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 580));

        tabPrincipal.addTab("AggODR", añadirODR);

        mostrarODR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaRegistradoras.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaRegistradoras.setComponentPopupMenu(jPopupMenu5);
        jScrollPane5.setViewportView(tablaRegistradoras);
        if (tablaRegistradoras.getColumnModel().getColumnCount() > 0) {
            tablaRegistradoras.getColumnModel().getColumn(0).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(1).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(2).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(3).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(4).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(5).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(6).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(7).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(8).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(9).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(10).setResizable(false);
            tablaRegistradoras.getColumnModel().getColumn(11).setResizable(false);
        }

        mostrarODR.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 1080, 550));

        jButton13.setBackground(new java.awt.Color(255, 255, 255));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pictures/icons8-sign-out-40.png"))); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        mostrarODR.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 0, 50, 50));

        tabPrincipal.addTab("MostrarODR", mostrarODR);

        AñadirOficial.setBackground(new java.awt.Color(20, 25, 40));
        AñadirOficial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(29, 35, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(jSeparator31, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 310, 20));
        jPanel9.add(jSeparator66, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 310, 20));
        jPanel9.add(jSeparator67, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 310, 20));
        jPanel9.add(jSeparator68, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 310, 20));
        jPanel9.add(jSeparator69, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 310, 20));
        jPanel9.add(jSeparator70, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 310, 20));
        jPanel9.add(jSeparator71, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 310, 20));
        jPanel9.add(jSeparator72, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 310, 20));
        jPanel9.add(jSeparator73, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, 310, 20));

        jButton14.setText("Añadir");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 460, 180, 40));

        jPanel22.setBackground(new java.awt.Color(0, 0, 51));
        jPanel22.setRequestFocusEnabled(false);
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel23.setBackground(new java.awt.Color(102, 102, 102));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImagen3.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel23.add(lblImagen3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel22.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel9.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel99.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("Agregar Foto de la oficial:");
        jPanel9.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, 30));

        jLabel100.setForeground(new java.awt.Color(255, 255, 255));
        jLabel100.setText("Fecha de contratación:");
        jPanel9.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 330, -1, 20));

        txtFechaContratacion3.setEditable(false);
        jPanel9.add(txtFechaContratacion3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 350, 190, 30));
        jPanel9.add(dateFinContrato3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, 190, 30));

        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setText("Fecha de finalización del contrato:");
        jPanel9.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, -1, 20));

        cmbTurno3.setForeground(new java.awt.Color(255, 255, 255));
        cmbTurno3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel9.add(cmbTurno3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 240, 30));

        jLabel102.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(255, 255, 255));
        jLabel102.setText("Turno:");
        jPanel9.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));

        jLabel103.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(255, 255, 255));
        jLabel103.setText("Correo:");
        jPanel9.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));

        txtCorreo3.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo3.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreo3.setBorder(null);
        txtCorreo3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreo3FocusLost(evt);
            }
        });
        txtCorreo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreo3ActionPerformed(evt);
            }
        });
        txtCorreo3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreo3KeyTyped(evt);
            }
        });
        jPanel9.add(txtCorreo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 240, 30));
        jPanel9.add(jSeparator74, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 310, 20));

        jButton15.setText("Seleccionar Imagen");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel104.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(255, 255, 255));
        jLabel104.setText("Nacionalidad:");
        jPanel9.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 90, -1));

        txtNacionalidad3.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad3.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidad3.setBorder(null);
        txtNacionalidad3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad3KeyTyped(evt);
            }
        });
        jPanel9.add(txtNacionalidad3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 210, 30));

        txtSexo3.setEditable(false);
        txtSexo3.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo3.setForeground(new java.awt.Color(255, 255, 255));
        txtSexo3.setText("Femenino");
        txtSexo3.setBorder(null);
        jPanel9.add(txtSexo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 250, 30));

        jLabel105.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(255, 255, 255));
        jLabel105.setText("Genero");
        jPanel9.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, -1, -1));

        jLabel106.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel106.setForeground(new java.awt.Color(255, 255, 255));
        jLabel106.setText("Cedula:");
        jPanel9.add(jLabel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, 30));

        txtCedula3.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula3.setForeground(new java.awt.Color(255, 255, 255));
        txtCedula3.setBorder(null);
        txtCedula3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedula3ActionPerformed(evt);
            }
        });
        txtCedula3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedula3KeyTyped(evt);
            }
        });
        jPanel9.add(txtCedula3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 240, 30));

        txtEdad3.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad3.setForeground(new java.awt.Color(255, 255, 255));
        txtEdad3.setBorder(null);
        txtEdad3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad3KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad3KeyTyped(evt);
            }
        });
        jPanel9.add(txtEdad3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 250, 30));

        jLabel107.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(255, 255, 255));
        jLabel107.setText("Edad:");
        jPanel9.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel108.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(255, 255, 255));
        jLabel108.setText("Segundo Apellido:");
        jPanel9.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, 20));

        txtSegundoApellido3.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido3.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellido3.setBorder(null);
        txtSegundoApellido3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido3KeyTyped(evt);
            }
        });
        jPanel9.add(txtSegundoApellido3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 180, 30));

        txtPrimerApellido3.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido3.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellido3.setBorder(null);
        txtPrimerApellido3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido3KeyTyped(evt);
            }
        });
        jPanel9.add(txtPrimerApellido3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 190, 30));

        jLabel109.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(255, 255, 255));
        jLabel109.setText("Primer Apellido:");
        jPanel9.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 20));

        jLabel110.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(255, 255, 255));
        jLabel110.setText("Segundo nombre:");
        jPanel9.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, 30));

        txtSegundoNombre3.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre3.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombre3.setBorder(null);
        txtSegundoNombre3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre3KeyTyped(evt);
            }
        });
        jPanel9.add(txtSegundoNombre3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 180, 30));

        txtPrimerNombre3.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre3.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombre3.setBorder(null);
        txtPrimerNombre3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre3KeyTyped(evt);
            }
        });
        jPanel9.add(txtPrimerNombre3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 200, 30));

        jLabel111.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(255, 255, 255));
        jLabel111.setText("Primer nombre:");
        jPanel9.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 20));

        AñadirOficial.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel112.setFont(new java.awt.Font("Arial", 2, 13)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(255, 255, 255));
        jLabel112.setText("Asegurese de ingresar correctamente la cedula ya que esta no podrá ser modificada más adelante.");
        AñadirOficial.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel113.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(153, 0, 0));
        jLabel113.setText("TODOS LOS DATOS SON DE CARACTER OBLIGATORIO A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        AñadirOficial.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, 30));

        tabPrincipal.addTab("Agregaroficial", AñadirOficial);

        ModificarOficial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        modificarOficial.setBackground(new java.awt.Color(20, 25, 40));
        modificarOficial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel24.setBackground(new java.awt.Color(29, 35, 51));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel24.add(jSeparator75, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel24.add(jSeparator76, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel24.add(jSeparator77, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel24.add(jSeparator78, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel24.add(jSeparator79, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel24.add(jSeparator80, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel24.add(jSeparator81, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel24.add(jSeparator82, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel24.add(jSeparator83, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton16.setText("Modificar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel24.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel25.setBackground(new java.awt.Color(0, 0, 51));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel26.setBackground(new java.awt.Color(102, 102, 102));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel26.add(lblImagenMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel24.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel114.setForeground(new java.awt.Color(255, 255, 255));
        jLabel114.setText("Modificar foto de la oficial:");
        jPanel24.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, -1, -1));

        jLabel115.setForeground(new java.awt.Color(255, 255, 255));
        jLabel115.setText("Fecha de contratación:");
        jPanel24.add(jLabel115, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, -1, 20));

        txtFechaContratacionMod3.setEditable(false);
        jPanel24.add(txtFechaContratacionMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 360, 190, 30));
        jPanel24.add(dateFinContratoMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 190, 30));

        jLabel116.setForeground(new java.awt.Color(255, 255, 255));
        jLabel116.setText("Fecha de finalización del contrato:");
        jPanel24.add(jLabel116, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, -1, -1));

        cmbTurnoMod3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel24.add(cmbTurnoMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 250, 30));

        jLabel117.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(255, 255, 255));
        jLabel117.setText("Turno:");
        jPanel24.add(jLabel117, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        jLabel118.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel118.setForeground(new java.awt.Color(255, 255, 255));
        jLabel118.setText("Correo:");
        jPanel24.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, -1, -1));

        txtCorreoMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreoMod3.setBorder(null);
        txtCorreoMod3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoMod3FocusLost(evt);
            }
        });
        txtCorreoMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtCorreoMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 240, 30));
        jPanel24.add(jSeparator84, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton17.setText("Seleccionar Imagen");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel24.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel119.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(255, 255, 255));
        jLabel119.setText("Nacionalidad:");
        jPanel24.add(jLabel119, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 90, -1));

        txtNacionalidadMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidadMod3.setBorder(null);
        txtNacionalidadMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtNacionalidadMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 30));

        txtSexoMod3.setEditable(false);
        txtSexoMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtSexoMod3.setText("Femenino");
        txtSexoMod3.setBorder(null);
        jPanel24.add(txtSexoMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 250, 30));

        jLabel120.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(255, 255, 255));
        jLabel120.setText("Genero");
        jPanel24.add(jLabel120, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel121.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(255, 255, 255));
        jLabel121.setText("Cedula:");
        jPanel24.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        txtCedulaMod3.setEditable(false);
        txtCedulaMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtCedulaMod3.setBorder(null);
        txtCedulaMod3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaMod3ActionPerformed(evt);
            }
        });
        txtCedulaMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtCedulaMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 240, 30));

        txtEdadMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtEdadMod3.setBorder(null);
        txtEdadMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadMod3KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtEdadMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 250, 30));

        jLabel122.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(255, 255, 255));
        jLabel122.setText("Edad:");
        jPanel24.add(jLabel122, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel123.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(255, 255, 255));
        jLabel123.setText("Segundo Apellido:");
        jPanel24.add(jLabel123, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        txtSegundoApellidoMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellidoMod3.setBorder(null);
        txtSegundoApellidoMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtSegundoApellidoMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 190, 30));

        txtPrimerApellidoMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod3.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellidoMod3.setBorder(null);
        txtPrimerApellidoMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtPrimerApellidoMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 200, 30));

        jLabel124.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(255, 255, 255));
        jLabel124.setText("Primer Apellido:");
        jPanel24.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel125.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(255, 255, 255));
        jLabel125.setText("Segundo nombre:");
        jPanel24.add(jLabel125, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        txtSegundoNombreMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod3.setBorder(null);
        txtSegundoNombreMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtSegundoNombreMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 180, 30));

        txtPrimerNombreMod3.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod3.setBorder(null);
        txtPrimerNombreMod3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreMod3KeyTyped(evt);
            }
        });
        jPanel24.add(txtPrimerNombreMod3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        jLabel126.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(255, 255, 255));
        jLabel126.setText("Primer nombre:");
        jPanel24.add(jLabel126, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        modificarOficial.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel127.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(153, 0, 0));
        jLabel127.setText("NINGUN CAMPO DEBE ESTAR VACIO AL MODIFICAR, A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        modificarOficial.add(jLabel127, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, 30));

        ModificarOficial.add(modificarOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 590));

        tabPrincipal.addTab("ModificarOficial", ModificarOficial);

        mostrarOficial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaOficial.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaOficial.setComponentPopupMenu(jPopupMenu3);
        jScrollPane4.setViewportView(tablaOficial);
        if (tablaOficial.getColumnModel().getColumnCount() > 0) {
            tablaOficial.getColumnModel().getColumn(0).setResizable(false);
            tablaOficial.getColumnModel().getColumn(1).setResizable(false);
            tablaOficial.getColumnModel().getColumn(2).setResizable(false);
            tablaOficial.getColumnModel().getColumn(3).setResizable(false);
            tablaOficial.getColumnModel().getColumn(4).setResizable(false);
            tablaOficial.getColumnModel().getColumn(5).setResizable(false);
            tablaOficial.getColumnModel().getColumn(6).setResizable(false);
            tablaOficial.getColumnModel().getColumn(7).setResizable(false);
            tablaOficial.getColumnModel().getColumn(8).setResizable(false);
            tablaOficial.getColumnModel().getColumn(9).setResizable(false);
            tablaOficial.getColumnModel().getColumn(10).setResizable(false);
            tablaOficial.getColumnModel().getColumn(11).setResizable(false);
        }

        mostrarOficial.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1080, 560));

        tabPrincipal.addTab("MostrarOficial", mostrarOficial);
        tabPrincipal.addTab("CambiarCredenciales", Credenciales);

        ModificarODR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        modODR.setBackground(new java.awt.Color(20, 25, 40));
        modODR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel30.setBackground(new java.awt.Color(29, 35, 51));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel30.add(jSeparator95, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel30.add(jSeparator96, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel30.add(jSeparator97, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel30.add(jSeparator98, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel30.add(jSeparator99, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel30.add(jSeparator100, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel30.add(jSeparator101, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel30.add(jSeparator102, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel30.add(jSeparator103, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton20.setText("Modificar");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel31.setBackground(new java.awt.Color(0, 0, 51));
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel32.setBackground(new java.awt.Color(102, 102, 102));
        jPanel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel32.add(lblImagenMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel31.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel30.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel141.setForeground(new java.awt.Color(255, 255, 255));
        jLabel141.setText("Modificar foto de la registradora:");
        jPanel30.add(jLabel141, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, -1, -1));

        jLabel142.setForeground(new java.awt.Color(255, 255, 255));
        jLabel142.setText("Fecha de contratación:");
        jPanel30.add(jLabel142, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, -1, 20));

        txtFechaContratacionMod4.setEditable(false);
        jPanel30.add(txtFechaContratacionMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 360, 190, 30));
        jPanel30.add(dateFinContratoMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 190, 30));

        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setText("Fecha de finalización del contrato:");
        jPanel30.add(jLabel143, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, -1, -1));

        cmbTurnoMod4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel30.add(cmbTurnoMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 250, 30));

        jLabel144.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(255, 255, 255));
        jLabel144.setText("Turno:");
        jPanel30.add(jLabel144, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        jLabel145.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel145.setForeground(new java.awt.Color(255, 255, 255));
        jLabel145.setText("Correo:");
        jPanel30.add(jLabel145, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, -1, -1));

        txtCorreoMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreoMod4.setBorder(null);
        txtCorreoMod4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoMod4FocusLost(evt);
            }
        });
        txtCorreoMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtCorreoMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 240, 30));
        jPanel30.add(jSeparator104, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton21.setText("Seleccionar Imagen");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel146.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(255, 255, 255));
        jLabel146.setText("Nacionalidad:");
        jPanel30.add(jLabel146, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 90, -1));

        txtNacionalidadMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidadMod4.setBorder(null);
        txtNacionalidadMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtNacionalidadMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 30));

        txtSexoMod4.setEditable(false);
        txtSexoMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtSexoMod4.setText("Femenino");
        txtSexoMod4.setBorder(null);
        jPanel30.add(txtSexoMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 250, 30));

        jLabel147.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(255, 255, 255));
        jLabel147.setText("Genero");
        jPanel30.add(jLabel147, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel148.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(255, 255, 255));
        jLabel148.setText("Cedula:");
        jPanel30.add(jLabel148, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        txtCedulaMod4.setEditable(false);
        txtCedulaMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtCedulaMod4.setBorder(null);
        txtCedulaMod4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaMod4ActionPerformed(evt);
            }
        });
        txtCedulaMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtCedulaMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 240, 30));

        txtEdadMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtEdadMod4.setBorder(null);
        txtEdadMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadMod4KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtEdadMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 250, 30));

        jLabel149.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel149.setForeground(new java.awt.Color(255, 255, 255));
        jLabel149.setText("Edad:");
        jPanel30.add(jLabel149, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel150.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel150.setForeground(new java.awt.Color(255, 255, 255));
        jLabel150.setText("Segundo Apellido:");
        jPanel30.add(jLabel150, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        txtSegundoApellidoMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellidoMod4.setBorder(null);
        txtSegundoApellidoMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtSegundoApellidoMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 190, 30));

        txtPrimerApellidoMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod4.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellidoMod4.setBorder(null);
        txtPrimerApellidoMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtPrimerApellidoMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 200, 30));

        jLabel151.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(255, 255, 255));
        jLabel151.setText("Primer Apellido:");
        jPanel30.add(jLabel151, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel152.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel152.setForeground(new java.awt.Color(255, 255, 255));
        jLabel152.setText("Segundo nombre:");
        jPanel30.add(jLabel152, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        txtSegundoNombreMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod4.setBorder(null);
        txtSegundoNombreMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtSegundoNombreMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 180, 30));

        txtPrimerNombreMod4.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod4.setBorder(null);
        txtPrimerNombreMod4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreMod4KeyTyped(evt);
            }
        });
        jPanel30.add(txtPrimerNombreMod4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        jLabel153.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel153.setForeground(new java.awt.Color(255, 255, 255));
        jLabel153.setText("Primer nombre:");
        jPanel30.add(jLabel153, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        modODR.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel154.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel154.setForeground(new java.awt.Color(153, 0, 0));
        jLabel154.setText("NINGUN CAMPO DEBE ESTAR VACIO AL MODIFICAR, A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        modODR.add(jLabel154, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, 30));

        ModificarODR.add(modODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 580));

        tabPrincipal.addTab("ModificarODR", ModificarODR);

        aggPDC.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AñadirEnfermera2.setBackground(new java.awt.Color(20, 25, 40));
        AñadirEnfermera2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel35.setBackground(new java.awt.Color(29, 35, 51));
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel35.add(jSeparator105, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 310, 20));
        jPanel35.add(jSeparator106, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 310, 20));
        jPanel35.add(jSeparator107, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 310, 20));
        jPanel35.add(jSeparator108, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 310, 20));
        jPanel35.add(jSeparator109, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 310, 20));
        jPanel35.add(jSeparator110, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 310, 20));
        jPanel35.add(jSeparator111, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 310, 20));
        jPanel35.add(jSeparator112, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 310, 20));
        jPanel35.add(jSeparator113, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, 310, 20));

        jButton22.setText("Añadir");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 460, 180, 40));

        jPanel36.setBackground(new java.awt.Color(0, 0, 51));
        jPanel36.setRequestFocusEnabled(false);
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel37.setBackground(new java.awt.Color(102, 102, 102));
        jPanel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImagen5.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel37.add(lblImagen5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel36.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel35.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel97.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("Agregar Foto de la empleada:");
        jPanel35.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 30));

        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("Fecha de contratación:");
        jPanel35.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 330, -1, 20));

        txtFechaContratacion5.setEditable(false);
        jPanel35.add(txtFechaContratacion5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 350, 190, 30));
        jPanel35.add(dateFinContrato5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, 190, 30));

        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setText("Fecha de finalización del contrato:");
        jPanel35.add(jLabel155, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, -1, 20));

        cmbTurno5.setForeground(new java.awt.Color(255, 255, 255));
        cmbTurno5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel35.add(cmbTurno5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 240, 30));

        jLabel156.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel156.setForeground(new java.awt.Color(255, 255, 255));
        jLabel156.setText("Turno:");
        jPanel35.add(jLabel156, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));

        jLabel157.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel157.setForeground(new java.awt.Color(255, 255, 255));
        jLabel157.setText("Correo:");
        jPanel35.add(jLabel157, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));

        txtCorreo5.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreo5.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreo5.setBorder(null);
        txtCorreo5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreo5FocusLost(evt);
            }
        });
        txtCorreo5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreo5ActionPerformed(evt);
            }
        });
        txtCorreo5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreo5KeyTyped(evt);
            }
        });
        jPanel35.add(txtCorreo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 240, 30));
        jPanel35.add(jSeparator114, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 310, 20));

        jButton23.setText("Seleccionar Imagen");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel158.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel158.setForeground(new java.awt.Color(255, 255, 255));
        jLabel158.setText("Nacionalidad:");
        jPanel35.add(jLabel158, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 90, -1));

        txtNacionalidad5.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidad5.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidad5.setBorder(null);
        txtNacionalidad5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidad5KeyTyped(evt);
            }
        });
        jPanel35.add(txtNacionalidad5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 210, 30));

        txtSexo5.setEditable(false);
        txtSexo5.setBackground(new java.awt.Color(29, 35, 51));
        txtSexo5.setForeground(new java.awt.Color(255, 255, 255));
        txtSexo5.setText("Femenino");
        txtSexo5.setBorder(null);
        jPanel35.add(txtSexo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 250, 30));

        jLabel159.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel159.setForeground(new java.awt.Color(255, 255, 255));
        jLabel159.setText("Genero");
        jPanel35.add(jLabel159, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, -1, -1));

        jLabel160.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel160.setForeground(new java.awt.Color(255, 255, 255));
        jLabel160.setText("Cedula:");
        jPanel35.add(jLabel160, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, 30));

        txtCedula5.setBackground(new java.awt.Color(29, 35, 51));
        txtCedula5.setForeground(new java.awt.Color(255, 255, 255));
        txtCedula5.setBorder(null);
        txtCedula5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedula5ActionPerformed(evt);
            }
        });
        txtCedula5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedula5KeyTyped(evt);
            }
        });
        jPanel35.add(txtCedula5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 240, 30));

        txtEdad5.setBackground(new java.awt.Color(29, 35, 51));
        txtEdad5.setForeground(new java.awt.Color(255, 255, 255));
        txtEdad5.setBorder(null);
        txtEdad5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdad5KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdad5KeyTyped(evt);
            }
        });
        jPanel35.add(txtEdad5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 250, 30));

        jLabel161.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(255, 255, 255));
        jLabel161.setText("Edad:");
        jPanel35.add(jLabel161, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel162.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(255, 255, 255));
        jLabel162.setText("Segundo Apellido:");
        jPanel35.add(jLabel162, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, 20));

        txtSegundoApellido5.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellido5.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellido5.setBorder(null);
        txtSegundoApellido5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellido5KeyTyped(evt);
            }
        });
        jPanel35.add(txtSegundoApellido5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 180, 30));

        txtPrimerApellido5.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellido5.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellido5.setBorder(null);
        txtPrimerApellido5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellido5KeyTyped(evt);
            }
        });
        jPanel35.add(txtPrimerApellido5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 190, 30));

        jLabel163.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(255, 255, 255));
        jLabel163.setText("Primer Apellido:");
        jPanel35.add(jLabel163, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 20));

        jLabel164.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(255, 255, 255));
        jLabel164.setText("Segundo nombre:");
        jPanel35.add(jLabel164, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, 30));

        txtSegundoNombre5.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombre5.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoNombre5.setBorder(null);
        txtSegundoNombre5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombre5KeyTyped(evt);
            }
        });
        jPanel35.add(txtSegundoNombre5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 180, 30));

        txtPrimerNombre5.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombre5.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerNombre5.setBorder(null);
        txtPrimerNombre5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombre5KeyTyped(evt);
            }
        });
        jPanel35.add(txtPrimerNombre5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 200, 30));

        jLabel165.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 255, 255));
        jLabel165.setText("Primer nombre:");
        jPanel35.add(jLabel165, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 20));

        AñadirEnfermera2.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel166.setFont(new java.awt.Font("Arial", 2, 13)); // NOI18N
        jLabel166.setForeground(new java.awt.Color(255, 255, 255));
        jLabel166.setText("Asegurese de ingresar correctamente la cedula ya que esta no podrá ser modificada más adelante.");
        AñadirEnfermera2.add(jLabel166, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel167.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel167.setForeground(new java.awt.Color(153, 0, 0));
        jLabel167.setText("TODOS LOS DATOS SON DE CARACTER OBLIGATORIO A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        AñadirEnfermera2.add(jLabel167, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, 30));

        aggPDC.add(AñadirEnfermera2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 1100, 600));

        tabPrincipal.addTab("PDC", aggPDC);

        modPDC.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        modPersonal.setBackground(new java.awt.Color(20, 25, 40));
        modPersonal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel38.setBackground(new java.awt.Color(29, 35, 51));
        jPanel38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel38.add(jSeparator115, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 310, 20));
        jPanel38.add(jSeparator116, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 310, 20));
        jPanel38.add(jSeparator117, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 310, 20));
        jPanel38.add(jSeparator118, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 310, 20));
        jPanel38.add(jSeparator119, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 310, 20));
        jPanel38.add(jSeparator120, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 310, 20));
        jPanel38.add(jSeparator121, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 310, 20));
        jPanel38.add(jSeparator122, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 310, 20));
        jPanel38.add(jSeparator123, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 310, 20));

        jButton24.setText("Modificar");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel38.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 180, 40));

        jPanel39.setBackground(new java.awt.Color(0, 0, 51));
        jPanel39.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel40.setBackground(new java.awt.Color(102, 102, 102));
        jPanel40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel40.add(lblImagenMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 180));

        jPanel39.add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel38.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 310, 220));

        jLabel168.setForeground(new java.awt.Color(255, 255, 255));
        jLabel168.setText("Modificar foto de la enfermera:");
        jPanel38.add(jLabel168, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, -1, -1));

        jLabel169.setForeground(new java.awt.Color(255, 255, 255));
        jLabel169.setText("Fecha de contratación:");
        jPanel38.add(jLabel169, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, -1, 20));

        txtFechaContratacionMod5.setEditable(false);
        jPanel38.add(txtFechaContratacionMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 360, 190, 30));
        jPanel38.add(dateFinContratoMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 190, 30));

        jLabel170.setForeground(new java.awt.Color(255, 255, 255));
        jLabel170.setText("Fecha de finalización del contrato:");
        jPanel38.add(jLabel170, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, -1, -1));

        cmbTurnoMod5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno" }));
        jPanel38.add(cmbTurnoMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 250, 30));

        jLabel171.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel171.setForeground(new java.awt.Color(255, 255, 255));
        jLabel171.setText("Turno:");
        jPanel38.add(jLabel171, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        jLabel172.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel172.setForeground(new java.awt.Color(255, 255, 255));
        jLabel172.setText("Correo:");
        jPanel38.add(jLabel172, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, -1, -1));

        txtCorreoMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtCorreoMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreoMod5.setBorder(null);
        txtCorreoMod5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoMod5FocusLost(evt);
            }
        });
        txtCorreoMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtCorreoMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 240, 30));
        jPanel38.add(jSeparator124, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 310, 20));

        jButton25.setText("Seleccionar Imagen");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel38.add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 280, -1, -1));

        jLabel173.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel173.setForeground(new java.awt.Color(255, 255, 255));
        jLabel173.setText("Nacionalidad:");
        jPanel38.add(jLabel173, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 90, -1));

        txtNacionalidadMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtNacionalidadMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtNacionalidadMod5.setBorder(null);
        txtNacionalidadMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNacionalidadMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtNacionalidadMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 30));

        txtSexoMod5.setEditable(false);
        txtSexoMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtSexoMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtSexoMod5.setText("Femenino");
        txtSexoMod5.setBorder(null);
        jPanel38.add(txtSexoMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 250, 30));

        jLabel174.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel174.setForeground(new java.awt.Color(255, 255, 255));
        jLabel174.setText("Genero");
        jPanel38.add(jLabel174, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel175.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel175.setForeground(new java.awt.Color(255, 255, 255));
        jLabel175.setText("Cedula:");
        jPanel38.add(jLabel175, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 30));

        txtCedulaMod5.setEditable(false);
        txtCedulaMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtCedulaMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtCedulaMod5.setBorder(null);
        txtCedulaMod5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaMod5ActionPerformed(evt);
            }
        });
        txtCedulaMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtCedulaMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 240, 30));

        txtEdadMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtEdadMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtEdadMod5.setBorder(null);
        txtEdadMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEdadMod5KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtEdadMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 250, 30));

        jLabel176.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel176.setForeground(new java.awt.Color(255, 255, 255));
        jLabel176.setText("Edad:");
        jPanel38.add(jLabel176, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel177.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel177.setForeground(new java.awt.Color(255, 255, 255));
        jLabel177.setText("Segundo Apellido:");
        jPanel38.add(jLabel177, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, 20));

        txtSegundoApellidoMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoApellidoMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtSegundoApellidoMod5.setBorder(null);
        txtSegundoApellidoMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoApellidoMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtSegundoApellidoMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 190, 30));

        txtPrimerApellidoMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerApellidoMod5.setForeground(new java.awt.Color(255, 255, 255));
        txtPrimerApellidoMod5.setBorder(null);
        txtPrimerApellidoMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerApellidoMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtPrimerApellidoMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 200, 30));

        jLabel178.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel178.setForeground(new java.awt.Color(255, 255, 255));
        jLabel178.setText("Primer Apellido:");
        jPanel38.add(jLabel178, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 20));

        jLabel179.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel179.setForeground(new java.awt.Color(255, 255, 255));
        jLabel179.setText("Segundo nombre:");
        jPanel38.add(jLabel179, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        txtSegundoNombreMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtSegundoNombreMod5.setBorder(null);
        txtSegundoNombreMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSegundoNombreMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtSegundoNombreMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 180, 30));

        txtPrimerNombreMod5.setBackground(new java.awt.Color(29, 35, 51));
        txtPrimerNombreMod5.setBorder(null);
        txtPrimerNombreMod5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimerNombreMod5KeyTyped(evt);
            }
        });
        jPanel38.add(txtPrimerNombreMod5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 30));

        jLabel180.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel180.setForeground(new java.awt.Color(255, 255, 255));
        jLabel180.setText("Primer nombre:");
        jPanel38.add(jLabel180, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, 20));

        modPersonal.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 890, 520));

        jLabel181.setFont(new java.awt.Font("Arial", 2, 10)); // NOI18N
        jLabel181.setForeground(new java.awt.Color(153, 0, 0));
        jLabel181.setText("NINGUN CAMPO DEBE ESTAR VACIO AL MODIFICAR, A EXCEPCIÓN DEL SEGUNDO NOMBRE.");
        modPersonal.add(jLabel181, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, 30));

        modPDC.add(modPersonal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 1100, 590));

        tabPrincipal.addTab("modPDC", modPDC);

        mostrarPDC.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        personalTabla.setModel(new javax.swing.table.DefaultTableModel(
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
        personalTabla.setComponentPopupMenu(jPopupMenu6);
        jScrollPane6.setViewportView(personalTabla);
        if (personalTabla.getColumnModel().getColumnCount() > 0) {
            personalTabla.getColumnModel().getColumn(0).setResizable(false);
            personalTabla.getColumnModel().getColumn(1).setResizable(false);
            personalTabla.getColumnModel().getColumn(2).setResizable(false);
            personalTabla.getColumnModel().getColumn(3).setResizable(false);
            personalTabla.getColumnModel().getColumn(4).setResizable(false);
            personalTabla.getColumnModel().getColumn(5).setResizable(false);
            personalTabla.getColumnModel().getColumn(6).setResizable(false);
            personalTabla.getColumnModel().getColumn(7).setResizable(false);
            personalTabla.getColumnModel().getColumn(8).setResizable(false);
            personalTabla.getColumnModel().getColumn(9).setResizable(false);
            personalTabla.getColumnModel().getColumn(10).setResizable(false);
            personalTabla.getColumnModel().getColumn(11).setResizable(false);
        }

        mostrarPDC.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1080, 560));

        tabPrincipal.addTab("ShowPDC", mostrarPDC);

        jPanel1.add(tabPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 650));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 670));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed

    }//GEN-LAST:event_txtCedulaActionPerformed

    private void verificarFecha() {
        System.out.println("Fecha seleccionada: " + jDateChooserFinContrato.getDate());
        System.out.println("Componente null? " + (jDateChooserFinContrato == null));
        System.out.println("Editor null? " + (jDateChooserFinContrato.getDateEditor() == null));
    }

    // Métodos para Oficiales en la vista Director
    private void cargarTablaOficiales() {
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Inicio Contrato",
            "Fin Contrato"
        });

        List<Oficial> oficiales = OficialController.getInstancia().obtenerTodosOficiales();

        for (Oficial o : oficiales) {
            ImageIcon icono = null; // Inicialmente sin imagen

            // Intentar cargar la imagen solo si existe la ruta y el archivo
            if (o.getRutaImagen() != null && !o.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(o.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(o.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen para oficial " + o.getIdentificacion() + ": " + ex.getMessage());
                    icono = null; // Asegurarse que sea null si hay error
                }
            }

            modelo.addRow(new Object[]{
                icono, // Puede ser null
                o.getPrimerNombre() + " " + (o.getSegundoNombre() != null ? o.getSegundoNombre() : ""),
                o.getPrimerApellido() + " " + o.getSegundoApellido(),
                o.getEdad(),
                o.getIdentificacion(),
                o.getNacionalidad(),
                o.getCorreo(),
                o.getTurno(),
                o.getFechaContratacion(),
                o.getFechaFinContrato()
            });
        }

        tablaOficial.setModel(modelo);
        tablaOficial.setRowHeight(85);
        tablaOficial.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaOficial.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    // Métodos para Oficiales en la vista Director
    private void cargarTablaPDC() {
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Inicio Contrato",
            "Fin Contrato"
        });

        List<PersonalControl> PDC = PersonalControlController.getInstancia().obtenerTodosPersonalControl();

        for (PersonalControl p : PDC) {
            ImageIcon icono = null; // Inicialmente sin imagen

            // Intentar cargar la imagen solo si existe la ruta y el archivo
            if (p.getRutaImagen() != null && !p.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(p.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(p.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen para la empleada " + p.getIdentificacion() + ": " + ex.getMessage());
                    icono = null; // Asegurarse que sea null si hay error
                }
            }

            modelo.addRow(new Object[]{
                icono, // Puede ser null
                p.getPrimerNombre() + " " + (p.getSegundoNombre() != null ? p.getSegundoNombre() : ""),
                p.getPrimerApellido() + " " + p.getSegundoApellido(),
                p.getEdad(),
                p.getIdentificacion(),
                p.getNacionalidad(),
                p.getCorreo(),
                p.getTurno(),
                p.getFechaContratacion(),
                p.getFechaFinContrato()
            });
        }

        personalTabla.setModel(modelo);
        personalTabla.setRowHeight(85);
        personalTabla.getColumnModel().getColumn(0).setPreferredWidth(85);
        personalTabla.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private void cargarTablaGuardias() {
        DefaultTableModel modelo = guardiaController.obtenerModeloTabla();
        tablaGuardias.setModel(modelo);
        tablaGuardias.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
        ajustarImagenesTabla();
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

        // Cargar fecha de inicio de contrato (CORRECCIÓN IMPORTANTE)
        txtFechaContratacionMod2.setText(
                guardia.getFechaInicioContrato().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

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
                    rutaImagenGuardia = file;
                    return;
                }
            }
            // Imagen por defecto si no hay imagen o no se puede cargar
            lblImagenMod.setIcon(new ImageIcon(getClass().getResource("/Resources/default_guard.png")));
            rutaImagenGuardia = null;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            lblImagenMod.setIcon(new ImageIcon(getClass().getResource("/Resources/default_guard.png")));
            rutaImagenGuardia = null;
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

    private void limpiarFormularioCDA() {
        // Limpiar campos de texto
        txtCedula2.setText("");
        txtPrimerNombre2.setText("");
        txtSegundoNombre2.setText("");
        txtPrimerApellido2.setText("");
        txtSegundoApellido2.setText("");
        txtEdad2.setText("");
        txtNacionalidad2.setText("");
        txtCorreo2.setText("");
        txtFechaContratacion2.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        // Limpiar comboboxes
        cmbTurno2.setSelectedIndex(0);
        cmbCargo2.setSelectedIndex(0);

        // Limpiar fecha
        dateFinContrato2.setDate(null);

        // Limpiar imagen
        lblImagen2.setIcon(null);
        imagenSeleccionadaCDA = null;
    }

    private void limpiarFormularioODR() {
        // Limpiar campos de texto
        txtCedula4.setText("");
        txtPrimerNombre4.setText("");
        txtSegundoNombre4.setText("");
        txtPrimerApellido4.setText("");
        txtSegundoApellido4.setText("");
        txtEdad4.setText("");
        txtNacionalidad4.setText("");
        txtCorreo4.setText("");
        txtFechaContratacion4.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        // Limpiar comboboxes
        cmbTurno4.setSelectedIndex(0);

        // Limpiar fecha
        dateFinContrato4.setDate(null);

        // Limpiar imagen
        lblImagen4.setIcon(null);
        imagenSeleccionadaCDA = null;
    }

    private void limpiarFormularioModificacionODR() {
        // Limpiar campos de texto
        txtCedulaMod4.setText("");
        txtPrimerNombreMod4.setText("");
        txtSegundoNombreMod4.setText("");
        txtPrimerApellidoMod4.setText("");
        txtSegundoApellidoMod4.setText("");
        txtEdadMod4.setText("");
        txtNacionalidadMod4.setText("");
        txtCorreoMod4.setText("");
        txtFechaContratacionMod4.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        // Limpiar comboboxes
        cmbTurnoMod4.setSelectedIndex(0);

        // Limpiar fecha
        dateFinContratoMod4.setDate(null);

        // Limpiar imagen
        lblImagenMod4.setIcon(null);
        imagenSeleccionadaModODR = null;
    }

    private void limpiarFormularioModificacionCDA() {
        // Limpiar campos de texto
        txtCedulaMod2.setText("");
        txtPrimerNombreMod2.setText("");
        txtSegundoNombreMod2.setText("");
        txtPrimerApellidoMod2.setText("");
        txtSegundoApellidoMod2.setText("");
        txtEdadMod2.setText("");
        txtNacionalidadMod2.setText("");
        txtCorreoMod2.setText("");
        txtFechaContratacionMod2.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        // Limpiar comboboxes
        cmbTurnoMod2.setSelectedIndex(0);
        txtCargoMod1.setSelectedIndex(0);

        // Limpiar fecha
        dateFinContratoMod2.setDate(null);

        // Limpiar imagen
        lblImagenMod2.setIcon(null);
        imagenSeleccionadaModCDA = null;
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
        rutaImagenEnfermera = null; // Limpiar variable de registro
    }

    private void limpiarFormularioEnfermeraMod() {
        txtPrimerNombreMod1.setText("");
        txtSegundoNombreMod1.setText("");
        txtPrimerApellidoMod1.setText("");
        txtSegundoApellidoMod1.setText("");
        txtEdadMod1.setText("");
        txtCedulaMod1.setText("");
        txtNacionalidadMod1.setText("");
        txtCorreoMod1.setText("");
        cmbTurnoMod1.setSelectedIndex(0);
        dateFinContratoMod1.setDate(null);
        txtFechaContratacionMod1.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        lblImagenMod1.setIcon(null);
        rutaImagenEnfermeraMod = null; // Limpiar variable de registro
    }

    private void cargarDatosOficialParaModificar(Oficial oficial) {
        if (oficial == null) {
            return;
        }

        txtPrimerNombreMod3.setText(oficial.getPrimerNombre());
        txtSegundoNombreMod3.setText(oficial.getSegundoNombre() != null ? oficial.getSegundoNombre() : "");
        txtPrimerApellidoMod3.setText(oficial.getPrimerApellido());
        txtSegundoApellidoMod3.setText(oficial.getSegundoApellido());
        txtEdadMod3.setText(String.valueOf(oficial.getEdad()));
        txtCedulaMod3.setText(oficial.getIdentificacion());
        txtNacionalidadMod3.setText(oficial.getNacionalidad());
        txtCorreoMod3.setText(oficial.getCorreo());
        cmbTurnoMod3.setSelectedItem(oficial.getTurno());
        txtFechaContratacionMod3.setText(oficial.getFechaContratacion().toString());

        try {
            dateFinContratoMod3.setDate(
                    Date.from(oficial.getFechaFinContrato().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
        } catch (Exception e) {
            dateFinContratoMod3.setDate(null);
        }

        // Manejo seguro de la imagen
        cargarImagenOficial(oficial.getRutaImagen());
    }

    private void cargarImagenOficial(String rutaImagen) {
        try {
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                File file = new File(rutaImagen);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(rutaImagen);
                    Image img = icon.getImage().getScaledInstance(
                            lblImagenMod3.getWidth(),
                            lblImagenMod3.getHeight(),
                            Image.SCALE_SMOOTH
                    );
                    lblImagenMod3.setIcon(new ImageIcon(img));
                    rutaImagenOficialMod = file;
                    return;
                }
            }
            // Imagen por defecto si no hay imagen o no se puede cargar
            lblImagenMod3.setIcon(new ImageIcon(getClass().getResource("/Resources/default_officer.png")));
            rutaImagenOficialMod = null;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            lblImagenMod3.setIcon(new ImageIcon(getClass().getResource("/Resources/default_officer.png")));
            rutaImagenOficialMod = null;
        }
    }

    private void limpiarFormularioOficial() {
        txtPrimerNombre3.setText("");
        txtSegundoNombre3.setText("");
        txtPrimerApellido3.setText("");
        txtSegundoApellido3.setText("");
        txtEdad3.setText("");
        txtCedula3.setText("");
        txtNacionalidad3.setText("");
        txtCorreo3.setText("");
        cmbTurno3.setSelectedIndex(0);
        dateFinContrato3.setDate(null);
        txtFechaContratacion3.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        lblImagen3.setIcon(null);
        rutaImagenOficial = null;
    }

    private void limpiarFormularioOficialMod() {
        txtPrimerNombreMod3.setText("");
        txtSegundoNombreMod3.setText("");
        txtPrimerApellidoMod3.setText("");
        txtSegundoApellidoMod3.setText("");
        txtEdadMod3.setText("");
        txtCedulaMod3.setText("");
        txtNacionalidadMod3.setText("");
        txtCorreoMod3.setText("");
        cmbTurnoMod3.setSelectedIndex(0);
        dateFinContratoMod3.setDate(null);
        txtFechaContratacionMod3.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        lblImagenMod3.setIcon(null);
        rutaImagenOficialMod = null;
    }

    private void limpiarFormularioPDC() {
        txtPrimerNombre5.setText("");
        txtSegundoNombre5.setText("");
        txtPrimerApellido5.setText("");
        txtSegundoApellido5.setText("");
        txtEdad5.setText("");
        txtCedula5.setText("");
        txtNacionalidad5.setText("");
        txtCorreo5.setText("");
        cmbTurno5.setSelectedIndex(0);
        dateFinContrato5.setDate(null);
        txtFechaContratacion5.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        lblImagen5.setIcon(null);
        rutaImagenPDC = null;
    }

    private void limpiarFormularioPDCMod() {
        txtPrimerNombreMod5.setText("");
        txtSegundoNombreMod5.setText("");
        txtPrimerApellidoMod5.setText("");
        txtSegundoApellidoMod5.setText("");
        txtEdadMod5.setText("");
        txtCedulaMod5.setText("");
        txtNacionalidadMod5.setText("");
        txtCorreoMod5.setText("");
        cmbTurnoMod5.setSelectedIndex(0);
        dateFinContratoMod5.setDate(null);
        txtFechaContratacionMod5.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        lblImagenMod5.setIcon(null);
        rutaImagenPDCMod = null;
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // Obtener valores del formulario
            String primerNombre = txtPrimerNombre.getText().trim();
            String segundoNombre = txtSegundoNombre.getText().trim();
            String primerApellido = txtPrimerApellido.getText().trim();
            String segundoApellido = txtSegundoApellido.getText().trim();
            String cedula = txtCedula.getText().trim();
            String nacionalidad = txtNacionalidad.getText().trim();
            String correo = txtCorreo.getText().trim();
            String turno = cmbTurno.getSelectedItem().toString();
            String cargo = cmbCargo.getSelectedItem().toString();

            // Validar edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdad.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido.");
            }

            // Validar fecha fin
            if (jDateChooserFinContrato.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato.");
            }
            LocalDate fechaFin = jDateChooserFinContrato.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Validar imagen (similar a EnfermeraController)
            if (rutaImagenGuardia == null || !rutaImagenGuardia.exists()) {
                throw new IllegalArgumentException("Debe seleccionar una imagen válida del guardia.");
            }

            // Validar formato de imagen
            String nombreImagen = rutaImagenGuardia.getName().toLowerCase();
            if (!nombreImagen.endsWith(".jpg") && !nombreImagen.endsWith(".jpeg") && !nombreImagen.endsWith(".png")) {
                throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG.");
            }

            // Llamar al Controller (centraliza validaciones adicionales)
            Guardia nuevoGuardia = guardiaController.registrarGuardia(
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    edad,
                    cedula,
                    nacionalidad,
                    correo,
                    turno,
                    fechaFin,
                    cargo,
                    rutaImagenGuardia
            );

            // Éxito
            limpiarFormulario();
            actualizarTablaGuardias();
            JOptionPane.showMessageDialog(this, "Guardia registrado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Cargo",
            "Inicio Contrato", // Nueva columna
            "Fin Contrato"
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
                g.getFechaInicioContrato(),
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
                rutaImagenGuardiaMod = nuevaImagen;

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
            // Obtener valores del formulario
            String cedulaOriginal = txtCedulaMod.getText().trim();
            String primerNombre = txtPrimerNombreMod.getText().trim();
            String segundoNombre = txtSegundoNombreMod.getText().trim();
            String primerApellido = txtPrimerApellidoMod.getText().trim();
            String segundoApellido = txtSegundoApellidoMod.getText().trim();
            String nacionalidad = txtNacionalidadMod.getText().trim();
            String correo = txtCorreoMod.getText().trim();
            String turno = cmbTurnoMod.getSelectedItem().toString();
            String cargo = txtCargoMod.getSelectedItem().toString();

            // Validar edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdadMod.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido.");
            }

            // Validar fecha fin
            if (dateFinContratoMod.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato.");
            }
            LocalDate fechaFin = dateFinContratoMod.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Validar imagen (si se seleccionó una nueva)
            if (rutaImagenGuardiaMod != null) {
                if (!rutaImagenGuardiaMod.exists()) {
                    throw new IllegalArgumentException("La imagen seleccionada no existe.");
                }
                String nombreImagen = rutaImagenGuardiaMod.getName().toLowerCase();
                if (!nombreImagen.endsWith(".jpg") && !nombreImagen.endsWith(".jpeg") && !nombreImagen.endsWith(".png")) {
                    throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG.");
                }
            }

            // Preparar cambios
            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", primerNombre);
            cambios.put("segundoNombre", segundoNombre);
            cambios.put("primerApellido", primerApellido);
            cambios.put("segundoApellido", segundoApellido);
            cambios.put("edad", edad);
            cambios.put("nacionalidad", nacionalidad);
            cambios.put("correo", correo);
            cambios.put("turno", turno);
            cambios.put("cargo", cargo);
            cambios.put("fechaFin", fechaFin);

            // Llamar al Controller
            boolean modificado = guardiaController.modificarGuardia(
                    cedulaOriginal,
                    cambios,
                    rutaImagenGuardiaMod // Puede ser null
            );

            if (modificado) {
                limpiarFormularioModificacion();
                actualizarTablaGuardias();
                JOptionPane.showMessageDialog(this, "Guardia modificado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(),
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
                rutaImagenGuardiaMod = nuevaImagen;

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
            tabPrincipal.setSelectedComponent(ModificarGuardia);

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
            // Obtener valores del formulario
            String primerNombre = txtPrimerNombre1.getText().trim();
            String segundoNombre = txtSegundoNombre1.getText().trim();
            String primerApellido = txtPrimerApellido1.getText().trim();
            String segundoApellido = txtSegundoApellido1.getText().trim();
            String cedula = txtCedula1.getText().trim();
            String nacionalidad = txtNacionalidad1.getText().trim();
            String correo = txtCorreo1.getText().trim();
            String turno = cmbTurno1.getSelectedItem().toString();

            // Validar y convertir edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdad1.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido");
            }

            // Validar fecha
            if (dateFinContrato1.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato");
            }
            LocalDate fechaFin = dateFinContrato1.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            // Validar imagen
            if (rutaImagenEnfermera == null) {
                throw new IllegalArgumentException("Debe seleccionar una imagen de la enfermera");
            }

            // Llamar al controller para registrar
            Enfermera nuevaEnfermera = enfermeraController.registrarEnfermera(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno, fechaFin,
                    rutaImagenEnfermera
            );

            // Éxito - limpiar y actualizar
            limpiarFormularioEnfermera();
            actualizarTablaEnfermeras();

        } catch (IllegalArgumentException e) {
            // Mostrar mensajes de error de validación
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Mostrar errores inesperados
            JOptionPane.showMessageDialog(this,
                    "Error al registrar enfermera: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    public void actualizarTabla() {
        cargarTablaGuardias();
    }

    private void cargarDatosCoordinadorParaModificar(CoordinadorDeActividades coordinador) {
        try {
            if (coordinador == null) {
                throw new IllegalArgumentException("No se encontró el coordinador");
            }

            // Cargar datos básicos
            txtCedulaMod2.setText(coordinador.getIdentificacion());
            txtPrimerNombreMod2.setText(coordinador.getPrimerNombre());
            txtSegundoNombreMod2.setText(coordinador.getSegundoNombre() != null ? coordinador.getSegundoNombre() : "");
            txtPrimerApellidoMod2.setText(coordinador.getPrimerApellido());
            txtSegundoApellidoMod2.setText(coordinador.getSegundoApellido());
            txtEdadMod2.setText(String.valueOf(coordinador.getEdad()));
            txtNacionalidadMod2.setText(coordinador.getNacionalidad());
            txtCorreoMod2.setText(coordinador.getCorreo());
            txtFechaContratacionMod2.setText(coordinador.getFechaInicioContrato().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

            // Cargar comboboxes
            cmbTurnoMod2.setSelectedItem(coordinador.getTurno());
            txtCargoMod1.setSelectedItem(coordinador.getCargo());

            // Cargar fecha fin contrato
            if (coordinador.getFechaFinContrato() != null) {
                dateFinContratoMod2.setDate(Date.from(coordinador.getFechaFinContrato()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }

            // Cargar imagen existente
            if (coordinador.getRutaImagen() != null && !coordinador.getRutaImagen().isEmpty()) {
                File imagenExistente = new File(coordinador.getRutaImagen());
                if (imagenExistente.exists()) {
                    ImageIcon icon = new ImageIcon(imagenExistente.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(
                            lblImagenMod2.getWidth(),
                            lblImagenMod2.getHeight(),
                            Image.SCALE_SMOOTH);
                    lblImagenMod2.setIcon(new ImageIcon(img));
                    // No asignamos a imagenSeleccionadaModCDA porque es la imagen original
                } else {
                    lblImagenMod2.setIcon(new ImageIcon(getClass().getResource("/Resources/default_coordinator.png")));
                }
            } else {
                lblImagenMod2.setIcon(new ImageIcon(getClass().getResource("/Resources/default_coordinator.png")));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

        // Cargar imagen existente
        if (enfermera.getRutaImagen() != null && !enfermera.getRutaImagen().isEmpty()) {
            File imagenExistente = new File(enfermera.getRutaImagen());
            if (imagenExistente.exists()) {
                ImageIcon icon = new ImageIcon(imagenExistente.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(
                        lblImagenMod1.getWidth(),
                        lblImagenMod1.getHeight(),
                        Image.SCALE_SMOOTH
                );
                lblImagenMod1.setIcon(new ImageIcon(img));
            } else {
                lblImagenMod1.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
            }
        } else {
            lblImagenMod1.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
        }
    }

    private void cargarDatosPDCParaModificar(PersonalControl pdc) {
        if (pdc == null) {
            return;
        }

        txtPrimerNombreMod5.setText(pdc.getPrimerNombre());
        txtSegundoNombreMod5.setText(pdc.getSegundoNombre() != null ? pdc.getSegundoNombre() : "");
        txtPrimerApellidoMod5.setText(pdc.getPrimerApellido());
        txtSegundoApellidoMod5.setText(pdc.getSegundoApellido());
        txtEdadMod5.setText(String.valueOf(pdc.getEdad()));
        txtCedulaMod5.setText(pdc.getIdentificacion());
        txtNacionalidadMod5.setText(pdc.getNacionalidad());
        txtCorreoMod5.setText(pdc.getCorreo());
        cmbTurnoMod5.setSelectedItem(pdc.getTurno());
        txtFechaContratacionMod5.setText(pdc.getFechaContratacion().toString());

        try {
            dateFinContratoMod5.setDate(
                    Date.from(pdc.getFechaFinContrato().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
        } catch (Exception e) {
            dateFinContratoMod5.setDate(null);
        }

        // Cargar imagen existente
        if (pdc.getRutaImagen() != null && !pdc.getRutaImagen().isEmpty()) {
            File imagenExistente = new File(pdc.getRutaImagen());
            if (imagenExistente.exists()) {
                ImageIcon icon = new ImageIcon(imagenExistente.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(
                        lblImagenMod5.getWidth(),
                        lblImagenMod5.getHeight(),
                        Image.SCALE_SMOOTH
                );
                lblImagenMod5.setIcon(new ImageIcon(img));
            } else {
                lblImagenMod5.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
            }
        } else {
            lblImagenMod5.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
        }
    }

    private void cargarDatosODRParaModificar(OficialDeRegistro odr) {
        if (odr == null) {
            return;
        }

        txtPrimerNombreMod4.setText(odr.getPrimerNombre());
        txtSegundoNombreMod4.setText(odr.getSegundoNombre() != null ? odr.getSegundoNombre() : "");
        txtPrimerApellidoMod4.setText(odr.getPrimerApellido());
        txtSegundoApellidoMod4.setText(odr.getSegundoApellido());
        txtEdadMod4.setText(String.valueOf(odr.getEdad()));
        txtCedulaMod4.setText(odr.getIdentificacion());
        txtNacionalidadMod4.setText(odr.getNacionalidad());
        txtCorreoMod4.setText(odr.getCorreo());
        cmbTurnoMod4.setSelectedItem(odr.getTurno());
        txtFechaContratacionMod4.setText(odr.getFechaContratacion().toString());

        try {
            dateFinContratoMod4.setDate(
                    Date.from(odr.getFechaFinContrato().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
        } catch (Exception e) {
            dateFinContratoMod4.setDate(null);
        }

        // Cargar imagen existente
        if (odr.getRutaImagen() != null && !odr.getRutaImagen().isEmpty()) {
            File imagenExistente = new File(odr.getRutaImagen());
            if (imagenExistente.exists()) {
                ImageIcon icon = new ImageIcon(imagenExistente.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(
                        lblImagenMod4.getWidth(),
                        lblImagenMod4.getHeight(),
                        Image.SCALE_SMOOTH
                );
                lblImagenMod4.setIcon(new ImageIcon(img));
            } else {
                lblImagenMod4.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
            }
        } else {
            lblImagenMod4.setIcon(new ImageIcon(getClass().getResource("/Resources/default_nurse.png")));
        }
    }

    private void actualizarTablaCDA() {
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Cargo",
            "Inicio Contrato", // Nueva columna
            "Fin Contrato"
        });

        // Asegúrate de importar Model.Entities.CoordinadorDeActividades
        List<CoordinadorDeActividades> coordinadores
                = CoordinadorDeActividadesController.getInstancia().obtenerTodosCoordinadores();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefecto();

        for (CoordinadorDeActividades c : coordinadores) {
            ImageIcon icono = iconoPorDefecto;

            if (c.getRutaImagen() != null && !c.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(c.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(c.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                }
            }

            modelo.addRow(new Object[]{
                icono,
                c.getPrimerNombre() + " " + (c.getSegundoNombre() != null ? c.getSegundoNombre() : ""),
                c.getPrimerApellido() + " " + c.getSegundoApellido(),
                c.getEdad(),
                c.getIdentificacion(),
                c.getNacionalidad(),
                c.getCorreo(),
                c.getCargo(),
                c.getTurno(),
                c.getFechaInicioContrato(),
                c.getFechaFinContrato() != null
                ? c.getFechaFinContrato().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : ""
            });
        }

        tablaCoordinadores.setModel(modelo);
        tablaCoordinadores.setRowHeight(85);
        tablaCoordinadores.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaCoordinadores.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private void actualizarTablaPDC() {
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Inicio Contrato", // Nueva columna
            "Fin Contrato"
        });

        List<PersonalControl> pdc = PersonalControlController.getInstancia().obtenerTodosPersonalControl();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefecto();

        for (PersonalControl p : pdc) {
            ImageIcon icono = iconoPorDefecto;

            if (p.getRutaImagen() != null && !p.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(p.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(p.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                }
            }

            modelo.addRow(new Object[]{
                icono,
                p.getPrimerNombre() + " " + (p.getSegundoNombre() != null ? p.getSegundoNombre() : ""),
                p.getPrimerApellido() + " " + p.getSegundoApellido(),
                p.getEdad(),
                p.getIdentificacion(),
                p.getNacionalidad(),
                p.getCorreo(),
                p.getTurno(),
                p.getFechaContratacion(),
                p.getFechaFinContratoFormateada()
            });
        }

        personalTabla.setModel(modelo);
        personalTabla.setRowHeight(85);
        personalTabla.getColumnModel().getColumn(0).setPreferredWidth(85);
        personalTabla.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private void actualizarTablaOficiales() {
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Inicio Contrato", // Nueva columna
            "Fin Contrato"
        });

        List<Oficial> oficiales = oficialController.obtenerTodosOficiales();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefecto();

        for (Oficial o : oficiales) {
            ImageIcon icono = iconoPorDefecto;

            if (o.getRutaImagen() != null && !o.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(o.getRutaImagen());
                    if (file.exists()) {
                        Image img = new ImageIcon(o.getRutaImagen()).getImage()
                                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icono = new ImageIcon(img);
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                }
            }

            modelo.addRow(new Object[]{
                icono,
                o.getPrimerNombre() + " " + (o.getSegundoNombre() != null ? o.getSegundoNombre() : ""),
                o.getPrimerApellido() + " " + o.getSegundoApellido(),
                o.getEdad(),
                o.getIdentificacion(),
                o.getNacionalidad(),
                o.getCorreo(),
                o.getTurno(),
                o.getFechaContratacion(),
                o.getFechaFinContratoFormateada()
            });
        }

        tablaOficial.setModel(modelo);
        tablaOficial.setRowHeight(85);
        tablaOficial.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaOficial.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private void actualizarTablaODR() {
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Inicio Contrato",
            "Fin Contrato"
        });

        List<OficialDeRegistro> odr = OficialDeRegistroController.getInstancia().obtenerTodosOficiales();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefecto();

        for (OficialDeRegistro or : odr) {
            ImageIcon icono = iconoPorDefecto;

            // CORRECCIÓN PRINCIPAL: Cambiar la condición para verificar la ruta de la imagen
            if (or.getRutaImagen() != null && !or.getRutaImagen().isEmpty()) {
                try {
                    File file = new File(or.getRutaImagen());
                    if (file.exists()) {
                        // DEBUG: Mostrar información de la imagen
                        System.out.println("Cargando imagen de: " + or.getRutaImagen());
                        System.out.println("Tamaño archivo: " + file.length() + " bytes");

                        // Cargar la imagen con mejor manejo de errores
                        Image img = ImageIO.read(file);
                        if (img != null) {
                            img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            icono = new ImageIcon(img);
                        } else {
                            System.err.println("La imagen no pudo ser cargada (null)");
                        }
                    } else {
                        System.err.println("Archivo no existe: " + or.getRutaImagen());
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Oficial sin ruta de imagen: " + or.getIdentificacion());
            }

            modelo.addRow(new Object[]{
                icono,
                or.getPrimerNombre() + " " + (or.getSegundoNombre() != null ? or.getSegundoNombre() : ""),
                or.getPrimerApellido() + " " + or.getSegundoApellido(),
                or.getEdad(),
                or.getIdentificacion(),
                or.getNacionalidad(),
                or.getCorreo(),
                or.getTurno(),
                or.getFechaContratacion().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                or.getFechaFinContrato() != null
                ? or.getFechaFinContrato().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : ""
            });
        }

        tablaRegistradoras.setModel(modelo);
        tablaRegistradoras.setRowHeight(85);
        tablaRegistradoras.getColumnModel().getColumn(0).setPreferredWidth(85);

        // Asegurar que el renderizador está configurado correctamente
        tablaRegistradoras.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
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
            "Foto",
            "Nombre",
            "Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Inicio Contrato", // Nueva columna
            "Fin Contrato"
        });

        List<Enfermera> enfermeras = enfermeraController.obtenerTodasEnfermeras();

        // Crear una imagen por defecto segura
        ImageIcon iconoPorDefecto = crearIconoPorDefecto();

        for (Enfermera e : enfermeras) {
            ImageIcon icono = iconoPorDefecto;

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
                e.getFechaContratacion(),
                e.getFechaFinContratoFormateada()
            });
        }

        tablaEnfermeras.setModel(modelo);
        tablaEnfermeras.setRowHeight(85);
        tablaEnfermeras.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaEnfermeras.getColumnModel().getColumn(0).setCellRenderer(new ImagenTablaRenderer());
    }

    private ImageIcon crearIconoPorDefecto() {

        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, 80, 80);

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


    private void txtCorreo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1FocusLost

    private void txtCorreo1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1KeyTyped

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        Object[] options = {"Tomar Foto", "Seleccionar Archivo"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "Seleccione cómo obtener la imagen:",
                "Imagen de la enfermera",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File imagen = null;
            if (opcion == 0) {
                imagen = enfermeraController.capturarImagenEnfermera();
            } else { // Seleccionar Archivo
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);

                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    imagen = fileChooser.getSelectedFile();
                }
            }

            if (imagen != null) {
                ImageIcon icono = new ImageIcon(imagen.getAbsolutePath());
                Image img = icono.getImage()
                        .getScaledInstance(lblImagen1.getWidth(), lblImagen1.getHeight(), Image.SCALE_SMOOTH);
                lblImagen1.setIcon(new ImageIcon(img));
                rutaImagenEnfermera = imagen; // Asignar a variable de registro
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
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtCedula1KeyTyped

    private void txtEdad1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad1KeyPressed

    private void txtEdad1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtEdad1KeyTyped

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            // 1. Recoger datos del formulario
            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", txtPrimerNombreMod1.getText().trim());
            cambios.put("segundoNombre", txtSegundoNombreMod1.getText().trim());
            cambios.put("primerApellido", txtPrimerApellidoMod1.getText().trim());
            cambios.put("segundoApellido", txtSegundoApellidoMod1.getText().trim());

            // Validar y convertir edad
            try {
                cambios.put("edad", Integer.parseInt(txtEdadMod1.getText().trim()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido");
            }

            cambios.put("nacionalidad", txtNacionalidadMod1.getText().trim());
            cambios.put("correo", txtCorreoMod1.getText().trim());
            cambios.put("turno", cmbTurnoMod1.getSelectedItem().toString());

            // Validar fecha
            if (dateFinContratoMod1.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato");
            }
            cambios.put("fechaFin", dateFinContratoMod1.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());

            // 2. Obtener cédula original (no editable)
            String cedulaOriginal = txtCedulaMod1.getText().trim();

            // 3. Llamar al controller
            int resultado = enfermeraController.modificarEnfermera(
                    cedulaOriginal,
                    cambios,
                    rutaImagenEnfermeraMod // Puede ser null si no se cambió la imagen
            );

            // 4. Manejar resultados
            switch (resultado) {
                case 1: // Éxito
                    JOptionPane.showMessageDialog(this,
                            "Enfermera modificada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tabPrincipal.setSelectedComponent(MostrarEnfermeras);
                    actualizarTablaEnfermeras();
                    break;

                case 0: // No hay cambios
                    int opcion = JOptionPane.showConfirmDialog(this,
                            "No se detectaron cambios. ¿Desea cancelar la modificación?",
                            "Sin cambios",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        tabPrincipal.setSelectedComponent(MostrarEnfermeras);
                    }
                    break;

                case -1: // Error (debería haber lanzado excepción)
                    throw new RuntimeException("Error desconocido al modificar enfermera");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar enfermera: " + e.getMessage(),
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

            if (opcion == 0) {
                nuevaImagen = enfermeraController.capturarImagenEnfermera();
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();
                }
            }

            if (nuevaImagen != null) {
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                lblImagenMod1.getWidth(),
                                lblImagenMod1.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                lblImagenMod1.setIcon(new ImageIcon(imagenEscalada));
                rutaImagenEnfermeraMod = nuevaImagen;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtCedulaMod1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod1ActionPerformed

    private void txtCedulaMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtCedulaMod1KeyTyped

    private void txtEdadMod1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod1KeyPressed

    private void txtEdadMod1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod1KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
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
                tabPrincipal.setSelectedComponent(ModificarNurse);
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

    }//GEN-LAST:event_BotonContratarPDCActionPerformed

    private void AgregarImagenBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarImagenBotonActionPerformed

    }//GEN-LAST:event_AgregarImagenBotonActionPerformed

    private void txtCorreo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {

            if (imagenSeleccionadaCDA == null || !imagenSeleccionadaCDA.exists()) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar o capturar una foto del coordinador",
                        "Foto requerida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BufferedImage img = ImageIO.read(imagenSeleccionadaCDA);
                if (img == null) {
                    JOptionPane.showMessageDialog(this,
                            "El archivo seleccionado no es una imagen válida",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al leer la imagen: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CoordinadorDeActividadesController controller = CoordinadorDeActividadesController.getInstancia();

            String primerNombre = txtPrimerNombre2.getText().trim();
            String segundoNombre = txtSegundoNombre2.getText().trim();
            String primerApellido = txtPrimerApellido2.getText().trim();
            String segundoApellido = txtSegundoApellido2.getText().trim();
            int edad = Integer.parseInt(txtEdad2.getText().trim());
            String cedula = txtCedula2.getText().trim();
            String nacionalidad = txtNacionalidad2.getText().trim();
            String correo = txtCorreo2.getText().trim();
            String turno = cmbTurno2.getSelectedItem().toString();
            String cargo = cmbCargo2.getSelectedItem().toString();

            if (dateFinContrato2.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una fecha de fin de contrato",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaFin = dateFinContrato2.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            controller.registrarCoordinador(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno, cargo,
                    fechaFin, imagenSeleccionadaCDA
            );

            JOptionPane.showMessageDialog(this,
                    "Coordinador registrado exitosamente!\nLas credenciales fueron enviadas al correo.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormularioCDA();
            actualizarTablaCDA();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de validación: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar el coordinador: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de E/S: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del coordinador?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            if (opcion == 0) {
                imagenSeleccionadaCDA = coordinadorController.capturarImagenCoordinador();
                if (imagenSeleccionadaCDA == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo capturar la imagen",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    imagenSeleccionadaCDA = fileChooser.getSelectedFile();

                    String nombreArchivo = imagenSeleccionadaCDA.getName().toLowerCase();
                    if (!nombreArchivo.endsWith(".jpg")
                            && !nombreArchivo.endsWith(".jpeg")
                            && !nombreArchivo.endsWith(".png")) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        imagenSeleccionadaCDA = null;
                        return;
                    }
                }
            }

            if (imagenSeleccionadaCDA != null && imagenSeleccionadaCDA.exists()) {
                ImageIcon icono = new ImageIcon(imagenSeleccionadaCDA.getAbsolutePath());
                Image img = icono.getImage()
                        .getScaledInstance(
                                lblImagen2.getWidth(),
                                lblImagen2.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                lblImagen2.setIcon(new ImageIcon(img));
                lblImagen2.setToolTipText("Imagen seleccionada: " + imagenSeleccionadaCDA.getAbsolutePath());
            } else {
                lblImagen2.setIcon(null);
                lblImagen2.setToolTipText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtCedula2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedula2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula2ActionPerformed

    private void txtCedula2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedula2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtCedula2KeyTyped

    private void txtEdad2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad2KeyPressed

    private void txtEdad2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtEdad2KeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {

            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", txtPrimerNombreMod2.getText().trim());
            cambios.put("segundoNombre", txtSegundoNombreMod2.getText().trim());
            cambios.put("primerApellido", txtPrimerApellidoMod2.getText().trim());
            cambios.put("segundoApellido", txtSegundoApellidoMod2.getText().trim());

            // Validar y convertir edad
            try {
                cambios.put("edad", Integer.parseInt(txtEdadMod2.getText().trim()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido");
            }

            cambios.put("nacionalidad", txtNacionalidadMod2.getText().trim());
            cambios.put("correo", txtCorreoMod2.getText().trim());
            cambios.put("turno", cmbTurnoMod2.getSelectedItem().toString());
            cambios.put("cargo", txtCargoMod1.getSelectedItem().toString());

            if (dateFinContratoMod2.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato");
            }
            cambios.put("fechaFin", dateFinContratoMod2.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());

            // 2. Obtener cédula original (no editable)
            String cedulaOriginal = txtCedulaMod2.getText().trim();

            try {
                // 3. Llamar al controller
                boolean resultado = CoordinadorDeActividadesController.getInstancia().modificarCoordinador(
                        cedulaOriginal,
                        cambios,
                        imagenSeleccionadaModCDA // Usamos la imagen modificada
                );

                tabPrincipal.setSelectedComponent(mostrarCoordinadora);
                actualizarTablaCDA();
                limpiarFormularioModificacionCDA();
                imagenSeleccionadaModCDA = null;
                imagenSeleccionadaCDA = null;

            } catch (CoordinadorDeActividadesController.CancelarModificacionException e) {

            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al modificar coordinadora: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar coordinadora: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txtCorreoMod2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoMod2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod2FocusLost

    private void txtCorreoMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoMod2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod2KeyTyped

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del coordinador?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            if (opcion == 0) {
                imagenSeleccionadaModCDA = coordinadorController.capturarImagenCoordinador();
                if (imagenSeleccionadaModCDA == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo capturar la imagen",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    imagenSeleccionadaModCDA = fileChooser.getSelectedFile();

                    String nombreArchivo = imagenSeleccionadaModCDA.getName().toLowerCase();
                    if (!nombreArchivo.endsWith(".jpg")
                            && !nombreArchivo.endsWith(".jpeg")
                            && !nombreArchivo.endsWith(".png")) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        imagenSeleccionadaModCDA = null;
                        return;
                    }
                }
            }

            if (imagenSeleccionadaModCDA != null && imagenSeleccionadaModCDA.exists()) {
                ImageIcon icono = new ImageIcon(imagenSeleccionadaModCDA.getAbsolutePath());
                Image img = icono.getImage()
                        .getScaledInstance(
                                lblImagenMod2.getWidth(),
                                lblImagenMod2.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                lblImagenMod2.setIcon(new ImageIcon(img));
                lblImagenMod2.setToolTipText("Imagen seleccionada: " + imagenSeleccionadaModCDA.getAbsolutePath());
            } else {
                lblImagenMod2.setIcon(null);
                lblImagenMod2.setToolTipText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void txtCedulaMod2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod2ActionPerformed

    private void txtCedulaMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtCedulaMod2KeyTyped

    private void txtEdadMod2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod2KeyPressed

    private void txtEdadMod2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod2KeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume();

            if (c != '\b') {
                JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
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

    private void txtFechaContratacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaContratacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaContratacionActionPerformed

    private void ModificarCDAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarCDAActionPerformed
        int fila = tablaCoordinadores.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un coordinador primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la cédula de la columna correcta (ajusta el índice según tu tabla)
        String cedula = tablaCoordinadores.getValueAt(fila, 4).toString(); // Cambia 4 por el índice correcto

        try {

            CoordinadorDeActividades coordinador = CoordinadorDeActividadesController.getInstancia()
                    .obtenerCoordinadorPorCedula(cedula);

            if (coordinador != null) {

                cargarDatosCoordinadorParaModificar(coordinador);

                // Cambiar al panel de modificación (asegúrate que el nombre sea correcto)
                tabPrincipal.setSelectedComponent(ModificarCoordinador);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el coordinador con cédula: " + cedula,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_ModificarCDAActionPerformed

    private void EliminarCDAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarCDAActionPerformed
        int fila = tablaCoordinadores.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un coordinador primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaCoordinadores.getValueAt(fila, 4).toString(); // Asumiendo que la cédula está en la columna 5

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este coordinador?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = CoordinadorDeActividadesController.getInstancia()
                        .eliminarCoordinador(cedula);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Coordinador eliminado con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTablaCDA();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar el coordinador",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_EliminarCDAActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        try {
            // Obtener valores del formulario
            String primerNombre = txtPrimerNombre3.getText().trim();
            String segundoNombre = txtSegundoNombre3.getText().trim();
            String primerApellido = txtPrimerApellido3.getText().trim();
            String segundoApellido = txtSegundoApellido3.getText().trim();
            String cedula = txtCedula3.getText().trim();
            String nacionalidad = txtNacionalidad3.getText().trim();
            String correo = txtCorreo3.getText().trim();
            String turno = cmbTurno3.getSelectedItem().toString();

            // Validar edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdad3.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido.");
            }

            // Validar fecha fin
            if (dateFinContrato3.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato.");
            }
            LocalDate fechaFin = dateFinContrato3.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Validar imagen
            if (rutaImagenOficial == null || !rutaImagenOficial.exists()) {
                throw new IllegalArgumentException("Debe seleccionar una imagen válida del oficial.");
            }

            // Validar formato de imagen
            String nombreImagen = rutaImagenOficial.getName().toLowerCase();
            if (!nombreImagen.endsWith(".jpg") && !nombreImagen.endsWith(".jpeg") && !nombreImagen.endsWith(".png")) {
                throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG.");
            }

            // Llamar al Controller
            Oficial nuevoOficial = OficialController.getInstancia().registrarOficial(
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    edad,
                    cedula,
                    nacionalidad,
                    correo,
                    turno,
                    fechaFin,
                    rutaImagenOficial
            );

            // Éxito
            limpiarFormularioOficial();
            cargarTablaOficiales();
            JOptionPane.showMessageDialog(this, "Oficial registrado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void txtCorreo3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo3FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo3FocusLost

    private void txtCorreo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo3ActionPerformed

    private void txtCorreo3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo3KeyTyped

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // Mostrar opciones al usuario (cámara o selección de archivo)
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del oficial?",
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
                nuevaImagen = OficialController.getInstancia().capturarImagenOficial();
            } else if (opcion == 1) { // Seleccionar archivo del sistema
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                // Configurar el directorio inicial
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
                                lblImagen3.getWidth(),
                                lblImagen3.getHeight(),
                                Image.SCALE_SMOOTH
                        );

                // Mostrar en el JLabel
                lblImagen3.setIcon(new ImageIcon(imagenEscalada));

                // Guardar referencia al archivo
                rutaImagenOficial = nuevaImagen;

                // Mostrar tooltip con la ruta
                lblImagen3.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void txtNacionalidad3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidad3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNacionalidad3KeyTyped

    private void txtCedula3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedula3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula3ActionPerformed

    private void txtCedula3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedula3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula3KeyTyped

    private void txtEdad3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad3KeyPressed

    private void txtEdad3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad3KeyTyped

    private void txtSegundoApellido3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellido3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoApellido3KeyTyped

    private void txtPrimerApellido3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellido3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellido3KeyTyped

    private void txtSegundoNombre3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombre3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoNombre3KeyTyped

    private void txtPrimerNombre3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombre3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombre3KeyTyped

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        try {
            // 1. Preparar cambios
            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", txtPrimerNombreMod3.getText().trim());
            cambios.put("segundoNombre", txtSegundoNombreMod3.getText().trim());
            cambios.put("primerApellido", txtPrimerApellidoMod3.getText().trim());
            cambios.put("segundoApellido", txtSegundoApellidoMod3.getText().trim());
            cambios.put("edad", Integer.parseInt(txtEdadMod3.getText().trim()));
            cambios.put("nacionalidad", txtNacionalidadMod3.getText().trim());
            cambios.put("correo", txtCorreoMod3.getText().trim());
            cambios.put("turno", cmbTurnoMod3.getSelectedItem().toString());
            cambios.put("fechaFin", dateFinContratoMod3.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());

            String cedulaOriginal = txtCedulaMod3.getText().trim();

            // 2. Solo considerar como nueva imagen si el usuario seleccionó una explícitamente
            File imagenModificada = imagenFueModificada ? rutaImagenOficialMod : null;

            // 3. Llamar al controlador
            int resultado = OficialController.getInstancia().modificarOficial(
                    cedulaOriginal, cambios, imagenModificada);

            // 4. Manejar resultados
            switch (resultado) {
                case 1: // Éxito
                    JOptionPane.showMessageDialog(this,
                            "Oficial modificado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tabPrincipal.setSelectedComponent(mostrarOficial); // Cambiar al panel de tabla
                    cargarTablaOficiales(); // Actualizar tabla
                    break;

                case 0: // No hay cambios
                    int opcion = JOptionPane.showConfirmDialog(this,
                            "No se detectaron cambios. ¿Desea cancelar la modificación?",
                            "Sin cambios",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        tabPrincipal.setSelectedComponent(mostrarOficial); // Cambiar al panel de tabla
                    }
                    break;

                case -1: // Error
                    throw new RuntimeException("Error desconocido al modificar oficial");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar oficial: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void txtCorreoMod3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoMod3FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod3FocusLost

    private void txtCorreoMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod3KeyTyped

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // Opciones para el diálogo
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del oficial?",
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
                nuevaImagen = OficialController.getInstancia().capturarImagenOficial();
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
                // Escalar la imagen para el JLabel
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                lblImagenMod3.getWidth(),
                                lblImagenMod3.getHeight(),
                                Image.SCALE_SMOOTH
                        );

                // Mostrar en el JLabel
                lblImagenMod3.setIcon(new ImageIcon(imagenEscalada));

                // Guardar referencia al archivo
                rutaImagenOficialMod = nuevaImagen;

                // Mostrar tooltip con la ruta
                lblImagenMod3.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void txtNacionalidadMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNacionalidadMod3KeyTyped

    private void txtCedulaMod3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod3ActionPerformed

    private void txtCedulaMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod3KeyTyped

    private void txtEdadMod3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod3KeyPressed

    private void txtEdadMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod3KeyTyped

    private void txtSegundoApellidoMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoApellidoMod3KeyTyped

    private void txtPrimerApellidoMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellidoMod3KeyTyped

    private void txtSegundoNombreMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoNombreMod3KeyTyped

    private void txtPrimerNombreMod3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreMod3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombreMod3KeyTyped

    private void BorrarOfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarOfActionPerformed
        int filaSeleccionada = tablaOficial.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una oficial primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaOficial.getValueAt(filaSeleccionada, 4).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar a la guardia con cédula " + cedula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = OficialController.getInstancia().eliminarOficial(cedula);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Guardia eliminada con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    actualizarTablaOficiales();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_BorrarOfActionPerformed

    private void modOfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modOfActionPerformed
        int fila = tablaOficial.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un oficial primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaOficial.getValueAt(fila, 4).toString(); // Cambia 4 por el índice correcto

        try {

            Oficial oficial = OficialController.getInstancia()
                    .obtenerOficialPorCedula(cedula);

            if (oficial != null) {

                cargarDatosOficialParaModificar(oficial);

                // Cambiar al panel de modificación (asegúrate que el nombre sea correcto)
                tabPrincipal.setSelectedComponent(ModificarOficial);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el Oficial con cédula: " + cedula,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_modOfActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {

            String primerNombre = txtPrimerNombre4.getText().trim();
            String segundoNombre = txtSegundoNombre4.getText().trim();
            String primerApellido = txtPrimerApellido4.getText().trim();
            String segundoApellido = txtSegundoApellido4.getText().trim();
            String cedula = txtCedula4.getText().trim();
            String nacionalidad = txtNacionalidad4.getText().trim();
            String correo = txtCorreo4.getText().trim();
            String turno = cmbTurno4.getSelectedItem().toString();

            // Validar edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdad4.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido.");
            }

            // Validar fecha fin
            if (dateFinContrato4.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato.");
            }
            LocalDate fechaFin = dateFinContrato4.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (imagenSeleccionadaODR == null || !imagenSeleccionadaODR.exists()) {
                throw new IllegalArgumentException("Debe seleccionar una imagen válida de la registradora.");
            }

            // Validar formato de imagen
            String nombreImagen = imagenSeleccionadaODR.getName().toLowerCase();
            if (!nombreImagen.endsWith(".jpg") && !nombreImagen.endsWith(".jpeg") && !nombreImagen.endsWith(".png")) {
                throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG.");
            }

            OficialDeRegistro nuevoOficialR = oficialRegistro.registrarOficial(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, cedula, nacionalidad, correo, turno, fechaFin, imagenSeleccionadaODR);
            // Llamar al Controller (centraliza validaciones adicionales)

            // Éxito
            limpiarFormularioODR();
            actualizarTablaODR();
            JOptionPane.showMessageDialog(this, "Registradora agregada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void txtCorreo4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo4FocusLost

    private void txtCorreo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo4ActionPerformed

    private void txtCorreo4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo4KeyTyped

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen de la registradora?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            if (opcion == 0) { // Tomar foto
                imagenSeleccionadaODR = oficialRegistro.capturarImagenOficial();
                if (imagenSeleccionadaODR == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo capturar la imagen",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (opcion == 1) { // Seleccionar archivo
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivoSeleccionado = fileChooser.getSelectedFile();

                    // Validar extensión
                    String nombreArchivo = archivoSeleccionado.getName().toLowerCase();
                    if (!nombreArchivo.endsWith(".jpg") && !nombreArchivo.endsWith(".jpeg")
                            && !nombreArchivo.endsWith(".png")) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        imagenSeleccionadaODR = null;
                        return;
                    }

                    // Crear copia en directorio temporal
                    String tempDir = System.getProperty("java.io.tmpdir");
                    String nombreTemp = "oficial_registro_" + System.currentTimeMillis()
                            + nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
                    imagenSeleccionadaODR = new File(tempDir, nombreTemp);

                    try {
                        Files.copy(archivoSeleccionado.toPath(), imagenSeleccionadaODR.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this,
                                "Error al copiar la imagen: " + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        imagenSeleccionadaODR = null;
                        return;
                    }
                }
            }

            // Actualizar vista
            if (imagenSeleccionadaODR != null && imagenSeleccionadaODR.exists()) {
                ImageIcon icono = new ImageIcon(imagenSeleccionadaODR.getAbsolutePath());
                Image img = icono.getImage()
                        .getScaledInstance(
                                lblImagen4.getWidth(),
                                lblImagen4.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                lblImagen4.setIcon(new ImageIcon(img));
                lblImagen4.setToolTipText("Imagen seleccionada: " + imagenSeleccionadaODR.getAbsolutePath());
            } else {
                lblImagen4.setIcon(null);
                lblImagen4.setToolTipText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void txtNacionalidad4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidad4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNacionalidad4KeyTyped

    private void txtCedula4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedula4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula4ActionPerformed

    private void txtCedula4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedula4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula4KeyTyped

    private void txtEdad4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad4KeyPressed

    private void txtEdad4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad4KeyTyped

    private void txtSegundoApellido4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellido4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoApellido4KeyTyped

    private void txtPrimerApellido4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellido4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellido4KeyTyped

    private void txtSegundoNombre4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombre4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoNombre4KeyTyped

    private void txtPrimerNombre4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombre4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombre4KeyTyped

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        try {
            // 1. Preparar cambios
            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", txtPrimerNombreMod4.getText().trim());
            cambios.put("segundoNombre", txtSegundoNombreMod4.getText().trim());
            cambios.put("primerApellido", txtPrimerApellidoMod4.getText().trim());
            cambios.put("segundoApellido", txtSegundoApellidoMod4.getText().trim());
            cambios.put("edad", Integer.parseInt(txtEdadMod4.getText().trim()));
            cambios.put("nacionalidad", txtNacionalidadMod4.getText().trim());
            cambios.put("correo", txtCorreoMod4.getText().trim());
            cambios.put("turno", cmbTurnoMod4.getSelectedItem().toString());
            cambios.put("fechaFin", dateFinContratoMod4.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());

            String cedulaOriginal = txtCedulaMod4.getText().trim();

            // 2. Solo considerar como nueva imagen si el usuario seleccionó una explícitamente
            File imagenModificada = imagenFueModificada ? imagenSeleccionadaModODR : null;

            // 3. Llamar al controlador
            int resultado = OficialDeRegistroController.getInstancia().modificarOficial(cedulaOriginal, cambios, imagenSeleccionadaModODR);

            // 4. Manejar resultados
            switch (resultado) {
                case 1: // Éxito
                    JOptionPane.showMessageDialog(this,
                            "Registradora modificada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTablaODR();
                    limpiarFormularioModificacionODR();
                    tabPrincipal.setSelectedComponent(mostrarODR); // Cambiar al panel de tabla
                    cargarTablaOficiales(); // Actualizar tabla
                    break;

                case 0: // No hay cambios
                    int opcion = JOptionPane.showConfirmDialog(this,
                            "No se detectaron cambios. ¿Desea cancelar la modificación?",
                            "Sin cambios",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        tabPrincipal.setSelectedComponent(mostrarODR); // Cambiar al panel de tabla
                    }
                    break;

                case -1: // Error
                    throw new RuntimeException("Error desconocido al modificar la registradora");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar registradora: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void txtCorreoMod4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoMod4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod4FocusLost

    private void txtCorreoMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod4KeyTyped

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
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

            if (opcion == 0) {
                nuevaImagen = oficialRegistro.capturarImagenOficial();
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();
                }
            }

            if (nuevaImagen != null) {
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                lblImagenMod4.getWidth(),
                                lblImagenMod4.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                lblImagenMod4.setIcon(new ImageIcon(imagenEscalada));
                imagenSeleccionadaModODR = nuevaImagen;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void txtNacionalidadMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNacionalidadMod4KeyTyped

    private void txtCedulaMod4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod4ActionPerformed

    private void txtCedulaMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod4KeyTyped

    private void txtEdadMod4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod4KeyPressed

    private void txtEdadMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod4KeyTyped

    private void txtSegundoApellidoMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoApellidoMod4KeyTyped

    private void txtPrimerApellidoMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellidoMod4KeyTyped

    private void txtSegundoNombreMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoNombreMod4KeyTyped

    private void txtPrimerNombreMod4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreMod4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombreMod4KeyTyped

    private void modificarODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarODRActionPerformed
        int fila = tablaRegistradoras.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una registradora primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaRegistradoras.getValueAt(fila, 4).toString(); // Cambia 4 por el índice correcto

        try {

            OficialDeRegistro oficialR = OficialDeRegistroController.getInstancia()
                    .obtenerOficialPorCedula(cedula);

            if (oficialR != null) {

                cargarDatosODRParaModificar(oficialR);

                // Cambiar al panel de modificación (asegúrate que el nombre sea correcto)
                tabPrincipal.setSelectedComponent(ModificarODR);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el Oficial con cédula: " + cedula,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_modificarODRActionPerformed

    private void EliminarODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarODRActionPerformed
        int filaSeleccionada = tablaRegistradoras.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una registradora primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = tablaRegistradoras.getValueAt(filaSeleccionada, 4).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar a la registradora con cédula " + cedula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = OficialDeRegistroController.getInstancia().eliminarOficial(cedula);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Registradora eliminada con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    actualizarTablaODR();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_EliminarODRActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        try {
            // Obtener valores del formulario
            String primerNombre = txtPrimerNombre5.getText().trim();
            String segundoNombre = txtSegundoNombre5.getText().trim();
            String primerApellido = txtPrimerApellido5.getText().trim();
            String segundoApellido = txtSegundoApellido5.getText().trim();
            String cedula = txtCedula5.getText().trim();
            String nacionalidad = txtNacionalidad5.getText().trim();
            String correo = txtCorreo5.getText().trim();
            String turno = cmbTurno5.getSelectedItem().toString();

            // Validar edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdad5.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido.");
            }

            // Validar fecha fin
            if (dateFinContrato5.getDate() == null) {
                throw new IllegalArgumentException("Seleccione una fecha de fin de contrato.");
            }
            LocalDate fechaFin = dateFinContrato5.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Validar imagen
            if (rutaImagenPDC == null || !rutaImagenPDC.exists()) {
                throw new IllegalArgumentException("Debe seleccionar una imagen válida del personal de control.");
            }

            String nombreImagen = rutaImagenPDC.getName().toLowerCase();
            if (!nombreImagen.endsWith(".jpg") && !nombreImagen.endsWith(".jpeg") && !nombreImagen.endsWith(".png")) {
                throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG.");
            }

            PersonalControl nuevoPersonal = PersonalControlController.getInstancia().registrarPersonalControl(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, cedula, nacionalidad, correo, turno, fechaFin, rutaImagenPDC);

            // Éxito
            limpiarFormularioPDC();
            cargarTablaPDC();
            JOptionPane.showMessageDialog(this, "Empleada registrada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void txtCorreo5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreo5FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo5FocusLost

    private void txtCorreo5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo5ActionPerformed

    private void txtCorreo5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreo5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo5KeyTyped

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        Object[] options = {"Tomar Foto", "Seleccionar Archivo"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "Seleccione cómo obtener la imagen:",
                "Imagen de la empleada",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File imagen = null;
            if (opcion == 0) {
                imagen = PersonalControlController.getInstancia().capturarImagenPersonalControl();
            } else { // Seleccionar Archivo
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);

                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    imagen = fileChooser.getSelectedFile();
                }
            }

            if (imagen != null) {
                ImageIcon icono = new ImageIcon(imagen.getAbsolutePath());
                Image img = icono.getImage()
                        .getScaledInstance(lblImagen5.getWidth(), lblImagen5.getHeight(), Image.SCALE_SMOOTH);
                lblImagen5.setIcon(new ImageIcon(img));
                rutaImagenPDC = imagen;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void txtNacionalidad5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidad5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNacionalidad5KeyTyped

    private void txtCedula5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedula5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula5ActionPerformed

    private void txtCedula5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedula5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedula5KeyTyped

    private void txtEdad5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad5KeyPressed

    private void txtEdad5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdad5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdad5KeyTyped

    private void txtSegundoApellido5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellido5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoApellido5KeyTyped

    private void txtPrimerApellido5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellido5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellido5KeyTyped

    private void txtSegundoNombre5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombre5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoNombre5KeyTyped

    private void txtPrimerNombre5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombre5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombre5KeyTyped

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        try {
            // 1. Preparar cambios
            Map<String, Object> cambios = new HashMap<>();
            cambios.put("primerNombre", txtPrimerNombreMod5.getText().trim());
            cambios.put("segundoNombre", txtSegundoNombreMod5.getText().trim());
            cambios.put("primerApellido", txtPrimerApellidoMod5.getText().trim());
            cambios.put("segundoApellido", txtSegundoApellidoMod5.getText().trim());

            // Validar edad antes de parsear
            if (txtEdadMod5.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("La edad es obligatoria");
            }
            cambios.put("edad", Integer.parseInt(txtEdadMod5.getText().trim()));

            cambios.put("nacionalidad", txtNacionalidadMod5.getText().trim());
            cambios.put("correo", txtCorreoMod5.getText().trim());
            cambios.put("turno", cmbTurnoMod5.getSelectedItem().toString());

            // Validar fecha
            if (dateFinContratoMod5.getDate() == null) {
                throw new IllegalArgumentException("La fecha fin de contrato es obligatoria");
            }
            cambios.put("fechaFin", dateFinContratoMod5.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());

            String cedulaOriginal = txtCedulaMod5.getText().trim();

            // 2. Solo considerar como nueva imagen si el usuario seleccionó una explícitamente
            File imagenModificada = imagenFueModificada ? rutaImagenPDCMod : null;

            // 3. Llamar al controlador
            int resultado = PersonalControlController.getInstancia()
                    .modificarPersonalControl(cedulaOriginal, cambios, imagenModificada);

            switch (resultado) {
                case 1: // Éxito
                    JOptionPane.showMessageDialog(this,
                            "Empleada modificada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tabPrincipal.setSelectedComponent(mostrarPDC);
                    cargarTablaPDC();
                    break;

                case 0:
                    int opcion = JOptionPane.showConfirmDialog(this,
                            "No se detectaron cambios. ¿Desea cancelar la modificación?",
                            "Sin cambios",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        tabPrincipal.setSelectedComponent(mostrarPDC);
                    }
                    break;

                case -1: // Error
                    JOptionPane.showMessageDialog(this,
                            "Error al guardar los cambios en la base de datos",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de validación:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar empleada: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void txtCorreoMod5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoMod5FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod5FocusLost

    private void txtCorreoMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMod5KeyTyped

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25ActionPerformed

    private void txtNacionalidadMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNacionalidadMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNacionalidadMod5KeyTyped

    private void txtCedulaMod5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaMod5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod5ActionPerformed

    private void txtCedulaMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaMod5KeyTyped

    private void txtEdadMod5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod5KeyPressed

    private void txtEdadMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMod5KeyTyped

    private void txtSegundoApellidoMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoApellidoMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoApellidoMod5KeyTyped

    private void txtPrimerApellidoMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerApellidoMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerApellidoMod5KeyTyped

    private void txtSegundoNombreMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoNombreMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSegundoNombreMod5KeyTyped

    private void txtPrimerNombreMod5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimerNombreMod5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombreMod5KeyTyped

    private void modificarOPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarOPCActionPerformed
        int fila = personalTabla.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un registro de personal de control primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = personalTabla.getValueAt(fila, 4).toString(); // Ajusta el índice según tu tabla

        try {
            PersonalControl personal = PersonalControlController.getInstancia()
                    .obtenerPersonalControlPorCedula(cedula);

            if (personal != null) {
                // Cargar datos en el formulario de modificación
                cargarDatosPDCParaModificar(personal);

                // Cambiar al panel de modificación
                tabPrincipal.setSelectedComponent(modPDC);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el personal de control con cédula: " + cedula,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_modificarOPCActionPerformed

    private void eliminarPDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarPDCActionPerformed
        int filaSeleccionada = personalTabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una empleada primero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = personalTabla.getValueAt(filaSeleccionada, 4).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar a la empleada con cédula " + cedula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = PersonalControlController.getInstancia().eliminarPersonalControl(cedula);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Empleada eliminada con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    actualizarTablaPDC();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_eliminarPDCActionPerformed

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
    private javax.swing.JPanel AgregarCDA;
    private javax.swing.JPanel AñadirDelito;
    private javax.swing.JPanel AñadirEnfermera;
    private javax.swing.JPanel AñadirEnfermera1;
    private javax.swing.JPanel AñadirEnfermera2;
    private javax.swing.JPanel AñadirGuardia;
    private javax.swing.JPanel AñadirOficial;
    private javax.swing.JMenuItem BorrarOf;
    private javax.swing.JPanel Credenciales;
    private javax.swing.JPanel Director;
    private javax.swing.JPanel DisminuirSentencia;
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JMenuItem EliminarCDA;
    private javax.swing.JMenuItem EliminarEnfermera;
    private javax.swing.JMenuItem EliminarODR;
    private javax.swing.JPanel ListaDeGuardias;
    private javax.swing.JMenuItem Modificar;
    private javax.swing.JMenuItem ModificarCDA;
    private javax.swing.JPanel ModificarCoordinador;
    private javax.swing.JMenuItem ModificarEnfermera;
    private javax.swing.JPanel ModificarGuardia;
    private javax.swing.JPanel ModificarNurse;
    private javax.swing.JPanel ModificarODR;
    private javax.swing.JPanel ModificarOficial;
    private javax.swing.JPanel MostrarEnfermeras;
    private javax.swing.JPanel aggPDC;
    private javax.swing.JPanel añadirODR;
    private javax.swing.JComboBox<String> cmbCargo;
    private javax.swing.JComboBox<String> cmbCargo2;
    private javax.swing.JComboBox<String> cmbTurno;
    private javax.swing.JComboBox<String> cmbTurno1;
    private javax.swing.JComboBox<String> cmbTurno2;
    private javax.swing.JComboBox<String> cmbTurno3;
    private javax.swing.JComboBox<String> cmbTurno4;
    private javax.swing.JComboBox<String> cmbTurno5;
    private javax.swing.JComboBox<String> cmbTurnoMod;
    private javax.swing.JComboBox<String> cmbTurnoMod1;
    private javax.swing.JComboBox<String> cmbTurnoMod2;
    private javax.swing.JComboBox<String> cmbTurnoMod3;
    private javax.swing.JComboBox<String> cmbTurnoMod4;
    private javax.swing.JComboBox<String> cmbTurnoMod5;
    private com.toedter.calendar.JDateChooser dateFinContrato1;
    private com.toedter.calendar.JDateChooser dateFinContrato2;
    private com.toedter.calendar.JDateChooser dateFinContrato3;
    private com.toedter.calendar.JDateChooser dateFinContrato4;
    private com.toedter.calendar.JDateChooser dateFinContrato5;
    private com.toedter.calendar.JDateChooser dateFinContratoMod;
    private com.toedter.calendar.JDateChooser dateFinContratoMod1;
    private com.toedter.calendar.JDateChooser dateFinContratoMod2;
    private com.toedter.calendar.JDateChooser dateFinContratoMod3;
    private com.toedter.calendar.JDateChooser dateFinContratoMod4;
    private com.toedter.calendar.JDateChooser dateFinContratoMod5;
    private javax.swing.JMenuItem eliminarPDC;
    private javax.swing.JLabel fotolbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
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
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
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
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
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
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JPopupMenu jPopupMenu5;
    private javax.swing.JPopupMenu jPopupMenu6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator100;
    private javax.swing.JSeparator jSeparator101;
    private javax.swing.JSeparator jSeparator102;
    private javax.swing.JSeparator jSeparator103;
    private javax.swing.JSeparator jSeparator104;
    private javax.swing.JSeparator jSeparator105;
    private javax.swing.JSeparator jSeparator106;
    private javax.swing.JSeparator jSeparator107;
    private javax.swing.JSeparator jSeparator108;
    private javax.swing.JSeparator jSeparator109;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator110;
    private javax.swing.JSeparator jSeparator111;
    private javax.swing.JSeparator jSeparator112;
    private javax.swing.JSeparator jSeparator113;
    private javax.swing.JSeparator jSeparator114;
    private javax.swing.JSeparator jSeparator115;
    private javax.swing.JSeparator jSeparator116;
    private javax.swing.JSeparator jSeparator117;
    private javax.swing.JSeparator jSeparator118;
    private javax.swing.JSeparator jSeparator119;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator120;
    private javax.swing.JSeparator jSeparator121;
    private javax.swing.JSeparator jSeparator122;
    private javax.swing.JSeparator jSeparator123;
    private javax.swing.JSeparator jSeparator124;
    private javax.swing.JSeparator jSeparator125;
    private javax.swing.JSeparator jSeparator126;
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
    private javax.swing.JSeparator jSeparator31;
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
    private javax.swing.JSeparator jSeparator66;
    private javax.swing.JSeparator jSeparator67;
    private javax.swing.JSeparator jSeparator68;
    private javax.swing.JSeparator jSeparator69;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator70;
    private javax.swing.JSeparator jSeparator71;
    private javax.swing.JSeparator jSeparator72;
    private javax.swing.JSeparator jSeparator73;
    private javax.swing.JSeparator jSeparator74;
    private javax.swing.JSeparator jSeparator75;
    private javax.swing.JSeparator jSeparator76;
    private javax.swing.JSeparator jSeparator77;
    private javax.swing.JSeparator jSeparator78;
    private javax.swing.JSeparator jSeparator79;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator80;
    private javax.swing.JSeparator jSeparator81;
    private javax.swing.JSeparator jSeparator82;
    private javax.swing.JSeparator jSeparator83;
    private javax.swing.JSeparator jSeparator84;
    private javax.swing.JSeparator jSeparator85;
    private javax.swing.JSeparator jSeparator86;
    private javax.swing.JSeparator jSeparator87;
    private javax.swing.JSeparator jSeparator88;
    private javax.swing.JSeparator jSeparator89;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSeparator jSeparator90;
    private javax.swing.JSeparator jSeparator91;
    private javax.swing.JSeparator jSeparator92;
    private javax.swing.JSeparator jSeparator93;
    private javax.swing.JSeparator jSeparator94;
    private javax.swing.JSeparator jSeparator95;
    private javax.swing.JSeparator jSeparator96;
    private javax.swing.JSeparator jSeparator97;
    private javax.swing.JSeparator jSeparator98;
    private javax.swing.JSeparator jSeparator99;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblImagen1;
    private javax.swing.JLabel lblImagen2;
    private javax.swing.JLabel lblImagen3;
    private javax.swing.JLabel lblImagen4;
    private javax.swing.JLabel lblImagen5;
    private javax.swing.JLabel lblImagenMod;
    private javax.swing.JLabel lblImagenMod1;
    private javax.swing.JLabel lblImagenMod2;
    private javax.swing.JLabel lblImagenMod3;
    private javax.swing.JLabel lblImagenMod4;
    private javax.swing.JLabel lblImagenMod5;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRol;
    private javax.swing.JPanel modODR;
    private javax.swing.JMenuItem modOf;
    private javax.swing.JPanel modPDC;
    private javax.swing.JPanel modPersonal;
    private javax.swing.JMenuItem modificarODR;
    private javax.swing.JMenuItem modificarOPC;
    private javax.swing.JPanel modificarOficial;
    private javax.swing.JPanel mostrarCoordinadora;
    private javax.swing.JPanel mostrarODR;
    private javax.swing.JPanel mostrarOficial;
    private javax.swing.JPanel mostrarPDC;
    private javax.swing.JTable personalTabla;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tablaCoordinadores;
    private javax.swing.JTable tablaEnfermeras;
    private javax.swing.JTable tablaGuardias;
    private javax.swing.JTable tablaOficial;
    private javax.swing.JTable tablaRegistradoras;
    private javax.swing.JComboBox<String> txtCargoMod;
    private javax.swing.JComboBox<String> txtCargoMod1;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCedula1;
    private javax.swing.JTextField txtCedula2;
    private javax.swing.JTextField txtCedula3;
    private javax.swing.JTextField txtCedula4;
    private javax.swing.JTextField txtCedula5;
    private javax.swing.JTextField txtCedulaMod;
    private javax.swing.JTextField txtCedulaMod1;
    private javax.swing.JTextField txtCedulaMod2;
    private javax.swing.JTextField txtCedulaMod3;
    private javax.swing.JTextField txtCedulaMod4;
    private javax.swing.JTextField txtCedulaMod5;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtCorreo1;
    private javax.swing.JTextField txtCorreo2;
    private javax.swing.JTextField txtCorreo3;
    private javax.swing.JTextField txtCorreo4;
    private javax.swing.JTextField txtCorreo5;
    private javax.swing.JTextField txtCorreoMod;
    private javax.swing.JTextField txtCorreoMod1;
    private javax.swing.JTextField txtCorreoMod2;
    private javax.swing.JTextField txtCorreoMod3;
    private javax.swing.JTextField txtCorreoMod4;
    private javax.swing.JTextField txtCorreoMod5;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtEdad1;
    private javax.swing.JTextField txtEdad2;
    private javax.swing.JTextField txtEdad3;
    private javax.swing.JTextField txtEdad4;
    private javax.swing.JTextField txtEdad5;
    private javax.swing.JTextField txtEdadMod;
    private javax.swing.JTextField txtEdadMod1;
    private javax.swing.JTextField txtEdadMod2;
    private javax.swing.JTextField txtEdadMod3;
    private javax.swing.JTextField txtEdadMod4;
    private javax.swing.JTextField txtEdadMod5;
    private javax.swing.JTextField txtFechaContratacion;
    private javax.swing.JTextField txtFechaContratacion1;
    private javax.swing.JTextField txtFechaContratacion2;
    private javax.swing.JTextField txtFechaContratacion3;
    private javax.swing.JTextField txtFechaContratacion4;
    private javax.swing.JTextField txtFechaContratacion5;
    private javax.swing.JTextField txtFechaContratacionMod;
    private javax.swing.JTextField txtFechaContratacionMod1;
    private javax.swing.JTextField txtFechaContratacionMod2;
    private javax.swing.JTextField txtFechaContratacionMod3;
    private javax.swing.JTextField txtFechaContratacionMod4;
    private javax.swing.JTextField txtFechaContratacionMod5;
    private javax.swing.JTextField txtNacionalidad;
    private javax.swing.JTextField txtNacionalidad1;
    private javax.swing.JTextField txtNacionalidad2;
    private javax.swing.JTextField txtNacionalidad3;
    private javax.swing.JTextField txtNacionalidad4;
    private javax.swing.JTextField txtNacionalidad5;
    private javax.swing.JTextField txtNacionalidadMod;
    private javax.swing.JTextField txtNacionalidadMod1;
    private javax.swing.JTextField txtNacionalidadMod2;
    private javax.swing.JTextField txtNacionalidadMod3;
    private javax.swing.JTextField txtNacionalidadMod4;
    private javax.swing.JTextField txtNacionalidadMod5;
    private javax.swing.JTextField txtPrimerApellido;
    private javax.swing.JTextField txtPrimerApellido1;
    private javax.swing.JTextField txtPrimerApellido2;
    private javax.swing.JTextField txtPrimerApellido3;
    private javax.swing.JTextField txtPrimerApellido4;
    private javax.swing.JTextField txtPrimerApellido5;
    private javax.swing.JTextField txtPrimerApellidoMod;
    private javax.swing.JTextField txtPrimerApellidoMod1;
    private javax.swing.JTextField txtPrimerApellidoMod2;
    private javax.swing.JTextField txtPrimerApellidoMod3;
    private javax.swing.JTextField txtPrimerApellidoMod4;
    private javax.swing.JTextField txtPrimerApellidoMod5;
    private javax.swing.JTextField txtPrimerNombre;
    private javax.swing.JTextField txtPrimerNombre1;
    private javax.swing.JTextField txtPrimerNombre2;
    private javax.swing.JTextField txtPrimerNombre3;
    private javax.swing.JTextField txtPrimerNombre4;
    private javax.swing.JTextField txtPrimerNombre5;
    private javax.swing.JTextField txtPrimerNombreMod;
    private javax.swing.JTextField txtPrimerNombreMod1;
    private javax.swing.JTextField txtPrimerNombreMod2;
    private javax.swing.JTextField txtPrimerNombreMod3;
    private javax.swing.JTextField txtPrimerNombreMod4;
    private javax.swing.JTextField txtPrimerNombreMod5;
    private javax.swing.JTextField txtSegundoApellido;
    private javax.swing.JTextField txtSegundoApellido1;
    private javax.swing.JTextField txtSegundoApellido2;
    private javax.swing.JTextField txtSegundoApellido3;
    private javax.swing.JTextField txtSegundoApellido4;
    private javax.swing.JTextField txtSegundoApellido5;
    private javax.swing.JTextField txtSegundoApellidoMod;
    private javax.swing.JTextField txtSegundoApellidoMod1;
    private javax.swing.JTextField txtSegundoApellidoMod2;
    private javax.swing.JTextField txtSegundoApellidoMod3;
    private javax.swing.JTextField txtSegundoApellidoMod4;
    private javax.swing.JTextField txtSegundoApellidoMod5;
    private javax.swing.JTextField txtSegundoNombre;
    private javax.swing.JTextField txtSegundoNombre1;
    private javax.swing.JTextField txtSegundoNombre2;
    private javax.swing.JTextField txtSegundoNombre3;
    private javax.swing.JTextField txtSegundoNombre4;
    private javax.swing.JTextField txtSegundoNombre5;
    private javax.swing.JTextField txtSegundoNombreMod;
    private javax.swing.JTextField txtSegundoNombreMod1;
    private javax.swing.JTextField txtSegundoNombreMod2;
    private javax.swing.JTextField txtSegundoNombreMod3;
    private javax.swing.JTextField txtSegundoNombreMod4;
    private javax.swing.JTextField txtSegundoNombreMod5;
    private javax.swing.JTextField txtSexo;
    private javax.swing.JTextField txtSexo1;
    private javax.swing.JTextField txtSexo2;
    private javax.swing.JTextField txtSexo3;
    private javax.swing.JTextField txtSexo4;
    private javax.swing.JTextField txtSexo5;
    private javax.swing.JTextField txtSexoMod;
    private javax.swing.JTextField txtSexoMod1;
    private javax.swing.JTextField txtSexoMod2;
    private javax.swing.JTextField txtSexoMod3;
    private javax.swing.JTextField txtSexoMod4;
    private javax.swing.JTextField txtSexoMod5;
    // End of variables declaration//GEN-END:variables

}
