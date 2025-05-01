package View;

import Controller.CitaMedicaController;
import Controller.SancionController;
import DAO.PresoDAO;
import Model.Preso;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Oficial extends javax.swing.JFrame {

    private File imagenOficialSeleccionada;
    private String presoSeleccionadoIdentificacion;
    private final SancionController sancionController = new SancionController();

    public Oficial() {
        initComponents();
        this.setLocationRelativeTo(null);
        FechaSancion.getDateEditor().setEnabled(false);
        FechaCita.getDateEditor().setEnabled(false);
        CitaMedicaController controller = new CitaMedicaController();
        controller.cargarTodosLosPresos(TablaPresos);
        controller.configurarTablaImagenes(TablaPresos);
        controller.cargarTodosLosGuardias(tablaGuardias);
        controller.configurarTablaImagenes(tablaGuardias);
        InicializarMenuSanciones();

        InicializarMenu();
        ComboTipoSancion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoSeleccionado = ComboTipoSancion.getSelectedItem().toString();
                String identificacionPreso = presoSeleccionadoIdentificacion.trim();
                sancionController.filtrarSanciones(identificacionPreso, tipoSeleccionado, TablaHistorialSanciones);
            }
        });
    }

    private void InicializarMenu() {
        JMenuItem historialSanciones = new JMenuItem("Historial de sanciones");

        ppMenuTablaPresos.add(historialSanciones);
        TablaPresos.setComponentPopupMenu(ppMenuTablaPresos);
        TablaHistorialSanciones.setComponentPopupMenu(ppMenuTablaSanciones);

        historialSanciones.addActionListener(e -> {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                sancionController.mostrarError("¡Selecciona un preso primero!");
                return;
            }

            presoSeleccionadoIdentificacion = TablaPresos.getValueAt(filaSeleccionada, 4).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(presoSeleccionadoIdentificacion);

            if (preso != null) {
                sancionController.cargarHistorialSanciones(presoSeleccionadoIdentificacion, TablaHistorialSanciones);
                TabbedOficial.setSelectedIndex(6);
            } else {
                sancionController.mostrarError("No se encontró el preso");
            }
        });
    }

    private void InicializarMenuSanciones() {
        JMenuItem eliminarSancion = new JMenuItem("Eliminar sanción");

        ppMenuTablaSanciones.add(eliminarSancion);

        eliminarSancion.addActionListener(e -> {
            int filaSeleccionada = TablaHistorialSanciones.getSelectedRow();
            if (filaSeleccionada == -1) {
                sancionController.mostrarError("¡Selecciona una sanción primero!");
                return;
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppMenuTablaPresos = new javax.swing.JPopupMenu();
        ppMenuTablaSanciones = new javax.swing.JPopupMenu();
        PanelBotones = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        TabbedOficial = new javax.swing.JTabbedPane();
        PanelPerfilOficial = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        CerrarSesionBoton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        FechaInicioContratoOficial = new javax.swing.JLabel();
        TurnoOficial = new javax.swing.JLabel();
        FondoFoto = new javax.swing.JPanel();
        FotoOficial = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        NombreCompletoOficial = new javax.swing.JLabel();
        IdentificacionOficial = new javax.swing.JLabel();
        EdadOficial = new javax.swing.JLabel();
        NacionalidadOficial = new javax.swing.JLabel();
        PlacaOficial = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel46 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        EmailOficial = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        SexoOficial = new javax.swing.JLabel();
        botonIrPanelActualizar = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        FechaFinContratoOficial = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        PanelListaPresos = new javax.swing.JPanel();
        BarraDeBusquedaPreso = new javax.swing.JTextField();
        BotonBuscarPresoIdentificacion = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaPresos = new javax.swing.JTable();
        BotonCargarTodosPresos = new javax.swing.JButton();
        JcomboSeccion1 = new javax.swing.JComboBox<>();
        PanelListaGuardias = new javax.swing.JPanel();
        BarraDeBusquedaGuardias = new javax.swing.JTextField();
        BotonCargarTodosGuardias = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaGuardias = new javax.swing.JTable();
        BotonBuscarGuardia1 = new javax.swing.JButton();
        PanelAgendarCita = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        IdentificacionGuardia = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        IdentificacionPresoCita = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        MotivoCita = new javax.swing.JTextArea();
        botonAgendarCita = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        FechaCita = new com.toedter.calendar.JDateChooser();
        JcomboHoraCita = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        PanelActualizarInformacion = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        NuevoEmail = new javax.swing.JTextField();
        ContraseñaActual = new javax.swing.JTextField();
        NuevoPrimerNombre = new javax.swing.JTextField();
        NuevoPrimerApellido = new javax.swing.JTextField();
        NuevaNacionalidad = new javax.swing.JTextField();
        NuevaContraseña = new javax.swing.JTextField();
        NuevoSegundoNombre = new javax.swing.JTextField();
        NuevoSegundoApellido = new javax.swing.JTextField();
        BotonActualizarInformacion = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        VistaPreviaNuevaFoto = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        SubirNuevaFotoPerfil = new javax.swing.JButton();
        PanelAsignarSanciones = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        IdentificacionPresoSancion = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        MotivoSancion = new javax.swing.JTextArea();
        BotonAsignarSancion = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        TipoSancion = new javax.swing.JComboBox<>();
        FechaSancion = new com.toedter.calendar.JDateChooser();
        jPanel19 = new javax.swing.JPanel();
        PanelListaSanciones = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaHistorialSanciones = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        ComboTipoSancion = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        PanelActualizarSancion = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        IdentificacionPresoSancionModificar = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        NuevoMotivoSancion = new javax.swing.JTextArea();
        BotonModificarSancion = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JSeparator();
        NuevoTipoSancion = new javax.swing.JComboBox<>();
        NuevaFechaSancion = new com.toedter.calendar.JDateChooser();
        jLabel68 = new javax.swing.JLabel();
        IdSancionPresoModificar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelBotones.setBackground(new java.awt.Color(29, 35, 51));
        PanelBotones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(29, 35, 51));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PERFIL");
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        PanelBotones.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 60));

        jPanel8.setBackground(new java.awt.Color(29, 35, 51));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
        });
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LISTA DE PRESOS");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        PanelBotones.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 240, 60));

        jPanel9.setBackground(new java.awt.Color(29, 35, 51));
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel9MouseClicked(evt);
            }
        });
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("LISTA DE GUARDIAS");
        jPanel9.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        PanelBotones.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 240, 60));

        jPanel10.setBackground(new java.awt.Color(29, 35, 51));
        jPanel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel10MouseClicked(evt);
            }
        });
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel61.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("AGENDAR CITA");
        jPanel10.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        PanelBotones.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, 210, 60));

        jPanel11.setBackground(new java.awt.Color(29, 35, 51));
        jPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel11MouseClicked(evt);
            }
        });
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("ASIGNAR SANCIONES");
        jPanel11.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        PanelBotones.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 0, 220, 60));

        getContentPane().add(PanelBotones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        PanelPerfilOficial.setBackground(new java.awt.Color(255, 255, 255));
        PanelPerfilOficial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(180, 180, 195));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CerrarSesionBoton.setText("Cerrar sesión");
        jPanel6.add(CerrarSesionBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 110, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Cargo");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Turno");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, -1, -1));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, 220, 20));
        jPanel6.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 220, 20));
        jPanel6.add(FechaInicioContratoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 220, 30));
        jPanel6.add(TurnoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 220, 30));

        FondoFoto.setBackground(new java.awt.Color(255, 255, 255));
        FondoFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        FondoFoto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        FondoFoto.add(FotoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 200));

        jPanel6.add(FondoFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 200, 220));

        jLabel4.setFont(new java.awt.Font("Arial", 2, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Oficial superior");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 130, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("fecha inicio contrato");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, -1, -1));

        PanelPerfilOficial.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 300, 530));

        jPanel1.setBackground(new java.awt.Color(139, 139, 157));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("INFORMACIÓN PERSONAL");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, -1, -1));

        PanelPerfilOficial.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Nombre completo:");
        jPanel12.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Identificacion:");
        jPanel12.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Edad:");
        jPanel12.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        jLabel44.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 0, 0));
        jLabel44.setText("Nacionalidad:");
        jPanel12.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, -1, -1));

        jLabel45.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("Placa:");
        jPanel12.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, -1, -1));
        jPanel12.add(NombreCompletoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 390, 30));
        jPanel12.add(IdentificacionOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 420, 30));
        jPanel12.add(EdadOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 480, 30));
        jPanel12.add(NacionalidadOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 430, 30));
        jPanel12.add(PlacaOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 480, 30));
        jPanel12.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 540, 10));
        jPanel12.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 540, 10));
        jPanel12.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 540, 10));
        jPanel12.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 540, 10));
        jPanel12.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 540, 10));

        jLabel46.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 0, 0));
        jLabel46.setText("Correo electronico:");
        jPanel12.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, -1, -1));
        jPanel12.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 540, 10));
        jPanel12.add(EmailOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 380, 30));

        jLabel49.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("Sexo:");
        jPanel12.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, -1, -1));
        jPanel12.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 540, 10));
        jPanel12.add(SexoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 480, 30));

        botonIrPanelActualizar.setText("Actualizar información");
        botonIrPanelActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonIrPanelActualizarMouseClicked(evt);
            }
        });
        jPanel12.add(botonIrPanelActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, 150, -1));
        jPanel12.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 440, 540, 10));
        jPanel12.add(FechaFinContratoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 400, 370, 30));

        jLabel50.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 0));
        jLabel50.setText("fecha fin de contrato");
        jPanel12.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, -1, -1));

        PanelPerfilOficial.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 630, 500));

        TabbedOficial.addTab("PERFIL", PanelPerfilOficial);

        PanelListaPresos.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaPresos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelListaPresos.add(BarraDeBusquedaPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 610, 30));

        BotonBuscarPresoIdentificacion.setText("Buscar preso por identificacion");
        BotonBuscarPresoIdentificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonBuscarPresoIdentificacionActionPerformed(evt);
            }
        });
        PanelListaPresos.add(BotonBuscarPresoIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, 200, 30));

        TablaPresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombre completo", "Edad", "Identificacion", "Nacionalidad", "Celda", "Sección"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaPresos);

        PanelListaPresos.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 980, 440));

        BotonCargarTodosPresos.setText("Cargar Todos Los presos");
        BotonCargarTodosPresos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCargarTodosPresosActionPerformed(evt);
            }
        });
        PanelListaPresos.add(BotonCargarTodosPresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, 170, -1));

        JcomboSeccion1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar sección >", "Sección A", "Sección B", "Sección C" }));
        JcomboSeccion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JcomboSeccion1ActionPerformed(evt);
            }
        });
        PanelListaPresos.add(JcomboSeccion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 170, 30));

        TabbedOficial.addTab("PRESOS", PanelListaPresos);

        PanelListaGuardias.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaGuardias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BarraDeBusquedaGuardias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarraDeBusquedaGuardiasActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BarraDeBusquedaGuardias, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 670, 30));

        BotonCargarTodosGuardias.setText("Cargar todos Los guardias");
        BotonCargarTodosGuardias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCargarTodosGuardiasActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BotonCargarTodosGuardias, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, 170, -1));

        tablaGuardias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Nombre", "Edad", "Cedula", "Sexo", "Nacionalidad", "Correo", "Turno", "Cargo", "FechaContratacion", "FechaFinContrato"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tablaGuardias);

        PanelListaGuardias.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1080, 470));

        BotonBuscarGuardia1.setText("Buscar guardia por identificacion");
        BotonBuscarGuardia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonBuscarGuardia1ActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BotonBuscarGuardia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 30, 210, 30));

        TabbedOficial.addTab("GUARDIAS", PanelListaGuardias);

        PanelAgendarCita.setBackground(new java.awt.Color(255, 255, 255));
        PanelAgendarCita.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel48.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 0));
        jLabel48.setText("AGENDAR CITA MEDICA");
        jPanel2.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, -1));

        jLabel51.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 0));
        jLabel51.setText("Identificación del guardia asignado:");
        jPanel2.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        IdentificacionGuardia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdentificacionGuardiaActionPerformed(evt);
            }
        });
        jPanel2.add(IdentificacionGuardia, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 380, 30));

        jLabel52.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 0));
        jLabel52.setText("Motivo de la cita medica");
        jPanel2.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, -1, -1));

        IdentificacionPresoCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdentificacionPresoCitaActionPerformed(evt);
            }
        });
        jPanel2.add(IdentificacionPresoCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 380, 30));

        jLabel53.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Identificación del preso:");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("Fecha de la cita:");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        MotivoCita.setColumns(20);
        MotivoCita.setRows(5);
        jScrollPane3.setViewportView(MotivoCita);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 360, 230));

        botonAgendarCita.setText("Agendar cita medica");
        botonAgendarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgendarCitaActionPerformed(evt);
            }
        });
        jPanel2.add(botonAgendarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 420, 190, 30));
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 390, 20));
        jPanel2.add(FechaCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 380, 30));

        JcomboHoraCita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "08:00", "08:20", "08:40", "09:00", "09:20", "09:40", "10:00", "10:20", "10:40", "11:00", "11:20", "11:40", "12:00", "12:20", "12:40", "13:00", "13:20", "13:40", "14:00", "14:20", "14:40", "15:00", "15:20", "15:40", "16:00", "16:20", "16:40", "17:00", "17:20", "17:40", "18:00", "18:20", "18:40", "19:00", "19:20", "19:40", "20:00" }));
        jPanel2.add(JcomboHoraCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 380, 30));

        jLabel62.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 0, 0));
        jLabel62.setText("Hora de la cita:");
        jPanel2.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        PanelAgendarCita.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 980, 490));

        jPanel18.setBackground(new java.awt.Color(139, 139, 157));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelAgendarCita.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 80));

        TabbedOficial.addTab("AGENDAR CITA", PanelAgendarCita);

        PanelActualizarInformacion.setBackground(new java.awt.Color(255, 255, 255));
        PanelActualizarInformacion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel16.setBackground(new java.awt.Color(180, 180, 195));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("Segundo nombre:");
        jPanel16.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, -1, -1));

        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 0));
        jLabel37.setText("Primer apellido:");
        jPanel16.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, -1, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("Segundo apellido:");
        jPanel16.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, -1, -1));

        jLabel39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setText("Primer nombre:");
        jPanel16.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jLabel40.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 0));
        jLabel40.setText("Contraseña actual*:");
        jPanel16.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setText("Nueva contraseña:");
        jPanel16.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, -1, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("Correo electronico:");
        jPanel16.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, -1, -1));

        jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 0));
        jLabel43.setText("Nacionalidad:");
        jPanel16.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));
        jPanel16.add(NuevoEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 320, 170, 30));
        jPanel16.add(ContraseñaActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 170, 30));
        jPanel16.add(NuevoPrimerNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 170, 30));
        jPanel16.add(NuevoPrimerApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 170, 30));
        jPanel16.add(NuevaNacionalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 170, 30));
        jPanel16.add(NuevaContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 170, 30));
        jPanel16.add(NuevoSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 180, 170, 30));
        jPanel16.add(NuevoSegundoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 250, 170, 30));

        BotonActualizarInformacion.setText("Actualizar informacion");
        BotonActualizarInformacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonActualizarInformacionActionPerformed(evt);
            }
        });
        jPanel16.add(BotonActualizarInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 400, 170, 30));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("ACTUALIZAR INFORMACIÓN PERSONAL");
        jPanel16.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, -1));

        PanelActualizarInformacion.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 750, 500));

        jPanel15.setBackground(new java.awt.Color(139, 139, 157));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelActualizarInformacion.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 100));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel17.add(VistaPreviaNuevaFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, 250));

        PanelActualizarInformacion.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 180, 230, 270));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Vista previa nueva foto");
        PanelActualizarInformacion.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 140, -1, -1));

        SubirNuevaFotoPerfil.setText("Subir foto de perfil");
        SubirNuevaFotoPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubirNuevaFotoPerfilActionPerformed(evt);
            }
        });
        PanelActualizarInformacion.add(SubirNuevaFotoPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 470, 150, 30));

        TabbedOficial.addTab("ACTUALIZAR INFORMACION", PanelActualizarInformacion);

        PanelAsignarSanciones.setBackground(new java.awt.Color(255, 255, 255));
        PanelAsignarSanciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel55.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("ASIGNAR SANCIONES");
        jPanel3.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, -1));

        jLabel56.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("Tipo de sanción:");
        jPanel3.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, -1));

        jLabel57.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(0, 0, 0));
        jLabel57.setText("Motivo de la sanción:");
        jPanel3.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, -1, -1));

        IdentificacionPresoSancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdentificacionPresoSancionActionPerformed(evt);
            }
        });
        jPanel3.add(IdentificacionPresoSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 380, 30));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("Identificación del preso:");
        jPanel3.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel59.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("Fecha de la sanción:");
        jPanel3.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));

        MotivoSancion.setColumns(20);
        MotivoSancion.setRows(5);
        jScrollPane4.setViewportView(MotivoSancion);

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 360, 230));

        BotonAsignarSancion.setText("Asignar sanción");
        BotonAsignarSancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAsignarSancionActionPerformed(evt);
            }
        });
        jPanel3.add(BotonAsignarSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 190, 30));
        jPanel3.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 390, 20));

        TipoSancion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Amonestación verbal o escrita", "Limitación de actividades recreativas o deportivas", "Suspensión de visitas o comunicaciones", "Aislamiento en celda disciplinaria" }));
        jPanel3.add(TipoSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 380, 30));
        jPanel3.add(FechaSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 380, 30));

        PanelAsignarSanciones.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 980, 490));

        jPanel19.setBackground(new java.awt.Color(139, 139, 157));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelAsignarSanciones.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 80));

        TabbedOficial.addTab("ASIGNAR SANCIONES", PanelAsignarSanciones);

        PanelListaSanciones.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaSanciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaHistorialSanciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Tipo Sanción", "Fecha sanción", "Preso sancionado", "Motivo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(TablaHistorialSanciones);
        if (TablaHistorialSanciones.getColumnModel().getColumnCount() > 0) {
            TablaHistorialSanciones.getColumnModel().getColumn(0).setResizable(false);
            TablaHistorialSanciones.getColumnModel().getColumn(1).setResizable(false);
            TablaHistorialSanciones.getColumnModel().getColumn(2).setResizable(false);
            TablaHistorialSanciones.getColumnModel().getColumn(3).setResizable(false);
            TablaHistorialSanciones.getColumnModel().getColumn(4).setResizable(false);
        }

        PanelListaSanciones.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 1060, 450));

        jPanel4.setBackground(new java.awt.Color(139, 139, 157));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ComboTipoSancion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Amonestación verbal o escrita", "Limitación de actividades recreativas o deportivas", "Suspensión de visitas o comunicaciones", "Aislamiento en celda disciplinaria" }));
        ComboTipoSancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboTipoSancionActionPerformed(evt);
            }
        });
        jPanel4.add(ComboTipoSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 310, 40));

        jLabel60.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 0, 0));
        jLabel60.setText("Filtrar sanciones por tipo:");
        jPanel4.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        PanelListaSanciones.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1060, 60));

        TabbedOficial.addTab("LISTA DE SANCIONES", PanelListaSanciones);

        PanelActualizarSancion.setBackground(new java.awt.Color(255, 255, 255));
        PanelActualizarSancion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel63.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 0, 0));
        jLabel63.setText("MODIFICAR SANCIONES");
        jPanel5.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, -1));

        jLabel64.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 0, 0));
        jLabel64.setText("Id de la sanción a modificar:");
        jPanel5.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        jLabel65.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("Motivo de la sanción:");
        jPanel5.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, -1, -1));

        IdentificacionPresoSancionModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdentificacionPresoSancionModificarActionPerformed(evt);
            }
        });
        jPanel5.add(IdentificacionPresoSancionModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 440, 30));

        jLabel66.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Identificación del preso para modificar su sanción:");
        jPanel5.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel67.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Fecha de la sanción:");
        jPanel5.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, -1, -1));

        NuevoMotivoSancion.setColumns(20);
        NuevoMotivoSancion.setRows(5);
        jScrollPane7.setViewportView(NuevoMotivoSancion);

        jPanel5.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 360, 230));

        BotonModificarSancion.setText("Modificar sancion");
        BotonModificarSancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonModificarSancionActionPerformed(evt);
            }
        });
        jPanel5.add(BotonModificarSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 190, 30));
        jPanel5.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 390, 20));

        NuevoTipoSancion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Amonestación verbal o escrita", "Limitación de actividades recreativas o deportivas", "Suspensión de visitas o comunicaciones", "Aislamiento en celda disciplinaria" }));
        jPanel5.add(NuevoTipoSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 440, 30));
        jPanel5.add(NuevaFechaSancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 440, 30));

        jLabel68.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("Tipo de sanción:");
        jPanel5.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, -1));
        jPanel5.add(IdSancionPresoModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 440, 30));

        PanelActualizarSancion.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 980, 490));

        TabbedOficial.addTab("ACTUALIZAR SANCIONES", PanelActualizarSancion);

        getContentPane().add(TabbedOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1100, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdentificacionGuardiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdentificacionGuardiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdentificacionGuardiaActionPerformed

    private void IdentificacionPresoCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdentificacionPresoCitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdentificacionPresoCitaActionPerformed

    private void BotonActualizarInformacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonActualizarInformacionActionPerformed
        String contraseñaActual = ContraseñaActual.getText().trim();
        String nuevaContraseña = NuevaContraseña.getText().trim();
        String nuevoPrimerNombre = NuevoPrimerNombre.getText().trim();
        String nuevoSegundoNombre = NuevoSegundoNombre.getText().trim();
        String nuevoPrimerApellido = NuevoPrimerApellido.getText().trim();
        String nuevoSegundoApellido = NuevoSegundoApellido.getText().trim();
        String nuevaNacionalidad = NuevaNacionalidad.getText().trim();
        String nuevoEmail = NuevoEmail.getText().trim();
    }//GEN-LAST:event_BotonActualizarInformacionActionPerformed

    private void SubirNuevaFotoPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubirNuevaFotoPerfilActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            imagenOficialSeleccionada = fileChooser.getSelectedFile();

            try {
                BufferedImage originalImage = ImageIO.read(imagenOficialSeleccionada);

                ImageIcon icon = new ImageIcon(originalImage);
                Image img = icon.getImage();
                Image imgEscalada = img.getScaledInstance(
                        VistaPreviaNuevaFoto.getWidth(),
                        VistaPreviaNuevaFoto.getHeight(),
                        Image.SCALE_SMOOTH);

                VistaPreviaNuevaFoto.setIcon(new ImageIcon(imgEscalada));

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_SubirNuevaFotoPerfilActionPerformed
    }


    private void IdentificacionPresoSancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdentificacionPresoSancionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdentificacionPresoSancionActionPerformed

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        TabbedOficial.setSelectedIndex(0);
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        TabbedOficial.setSelectedIndex(1);
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        TabbedOficial.setSelectedIndex(2);
    }//GEN-LAST:event_jPanel9MouseClicked

    private void jPanel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseClicked
        TabbedOficial.setSelectedIndex(3);
    }//GEN-LAST:event_jPanel10MouseClicked

    private void jPanel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MouseClicked
        TabbedOficial.setSelectedIndex(5);
    }//GEN-LAST:event_jPanel11MouseClicked


    private void BotonAsignarSancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAsignarSancionActionPerformed
        SancionController controller = new SancionController();
        controller.registrarSancion(this);

        /* if (!validarCamposSancion()) {
            return;

        }

        String identificacionPreso = IdentificacionPresoSancion.getText().trim();
        String motivoSancion = MotivoSancion.getText().trim();
        String tipoSancion = TipoSancion.getSelectedItem().toString();
        Date fechaSeleccionada = FechaSancion.getDate();

        if (fechaSeleccionada == null) {
            mostrarError("Debe seleccionar la fecha en la que se hizo la sanción");
            return;
        }
        LocalDate fecha = fechaSeleccionada.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de la sancion no puede ser en el futuro \nEste formulario es para guardar sanciones ya realizadas");
            return;
        }
        try {
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                mostrarError("No se encontró ningún preso con esa identificacion");
                return;
            }
            Sancion nuevaSancion = new Sancion(0, motivoSancion, fecha, tipoSancion, preso);
            new SancionDAO().guardarSancion(nuevaSancion);

            JOptionPane.showMessageDialog(this, "La sancion se guardo con exito");

            limpiarCamposSancion();

        } catch (Exception e) {
            mostrarError("La sancion no se pudo guardar" + e.getMessage());
        }
         */
    }//GEN-LAST:event_BotonAsignarSancionActionPerformed


    private void botonAgendarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgendarCitaActionPerformed
        CitaMedicaController citaController = new CitaMedicaController();
        citaController.agendarCita(this);
    }//GEN-LAST:event_botonAgendarCitaActionPerformed

    private void ComboTipoSancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboTipoSancionActionPerformed
        SancionController controller = new SancionController();
        String tipo = ComboTipoSancion.getSelectedItem().toString();
        controller.filtrarSancionesPorTipo(presoSeleccionadoIdentificacion, tipo, TablaHistorialSanciones);    }//GEN-LAST:event_ComboTipoSancionActionPerformed

    private void BarraDeBusquedaGuardiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarraDeBusquedaGuardiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BarraDeBusquedaGuardiasActionPerformed

    private void botonIrPanelActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonIrPanelActualizarMouseClicked
        TabbedOficial.setSelectedIndex(4);
    }//GEN-LAST:event_botonIrPanelActualizarMouseClicked

    private void BotonBuscarPresoIdentificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonBuscarPresoIdentificacionActionPerformed
        CitaMedicaController CitaMedicaController = new CitaMedicaController();
        CitaMedicaController.buscarPresoPorIdentificacion(BarraDeBusquedaPreso.getText().trim(), TablaPresos);
    }//GEN-LAST:event_BotonBuscarPresoIdentificacionActionPerformed

    private void BotonCargarTodosPresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCargarTodosPresosActionPerformed
        CitaMedicaController CitaMedicaController = new CitaMedicaController();
        CitaMedicaController.cargarTodosLosPresos(TablaPresos);

     }//GEN-LAST:event_BotonCargarTodosPresosActionPerformed

    private void BotonCargarTodosGuardiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCargarTodosGuardiasActionPerformed
        CitaMedicaController CitaMedicaController = new CitaMedicaController();
        CitaMedicaController.cargarTodosLosGuardias(tablaGuardias);
    }//GEN-LAST:event_BotonCargarTodosGuardiasActionPerformed

    private void JcomboSeccion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JcomboSeccion1ActionPerformed
        String seccionSeleccionada = JcomboSeccion1.getSelectedItem().toString();
        CitaMedicaController citaMedicaController = new CitaMedicaController();
        citaMedicaController.cargarDatosPresoEnTablaPorSeccion(seccionSeleccionada, TablaPresos);

    }//GEN-LAST:event_JcomboSeccion1ActionPerformed

    private void BotonBuscarGuardia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonBuscarGuardia1ActionPerformed
        CitaMedicaController citaMedicaController = new CitaMedicaController();
        citaMedicaController.buscarGuardiaPorIdentificacion(BarraDeBusquedaGuardias.getText().trim(), tablaGuardias);
    }//GEN-LAST:event_BotonBuscarGuardia1ActionPerformed

    private void IdentificacionPresoSancionModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdentificacionPresoSancionModificarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdentificacionPresoSancionModificarActionPerformed

    private void BotonModificarSancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonModificarSancionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonModificarSancionActionPerformed

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
            java.util.logging.Logger.getLogger(Oficial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Oficial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Oficial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Oficial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Oficial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BarraDeBusquedaGuardias;
    private javax.swing.JTextField BarraDeBusquedaPreso;
    private javax.swing.JButton BotonActualizarInformacion;
    private javax.swing.JButton BotonAsignarSancion;
    private javax.swing.JButton BotonBuscarGuardia1;
    private javax.swing.JButton BotonBuscarPresoIdentificacion;
    private javax.swing.JButton BotonCargarTodosGuardias;
    private javax.swing.JButton BotonCargarTodosPresos;
    private javax.swing.JButton BotonModificarSancion;
    private javax.swing.JButton CerrarSesionBoton;
    private javax.swing.JComboBox<String> ComboTipoSancion;
    private javax.swing.JTextField ContraseñaActual;
    private javax.swing.JLabel EdadOficial;
    private javax.swing.JLabel EmailOficial;
    private com.toedter.calendar.JDateChooser FechaCita;
    private javax.swing.JLabel FechaFinContratoOficial;
    private javax.swing.JLabel FechaInicioContratoOficial;
    private com.toedter.calendar.JDateChooser FechaSancion;
    private javax.swing.JPanel FondoFoto;
    private javax.swing.JLabel FotoOficial;
    private javax.swing.JTextField IdSancionPresoModificar;
    private javax.swing.JTextField IdentificacionGuardia;
    private javax.swing.JLabel IdentificacionOficial;
    private javax.swing.JTextField IdentificacionPresoCita;
    private javax.swing.JTextField IdentificacionPresoSancion;
    private javax.swing.JTextField IdentificacionPresoSancionModificar;
    private javax.swing.JComboBox<String> JcomboHoraCita;
    private javax.swing.JComboBox<String> JcomboSeccion1;
    private javax.swing.JTextArea MotivoCita;
    private javax.swing.JTextArea MotivoSancion;
    private javax.swing.JLabel NacionalidadOficial;
    private javax.swing.JLabel NombreCompletoOficial;
    private javax.swing.JTextField NuevaContraseña;
    private com.toedter.calendar.JDateChooser NuevaFechaSancion;
    private javax.swing.JTextField NuevaNacionalidad;
    private javax.swing.JTextField NuevoEmail;
    private javax.swing.JTextArea NuevoMotivoSancion;
    private javax.swing.JTextField NuevoPrimerApellido;
    private javax.swing.JTextField NuevoPrimerNombre;
    private javax.swing.JTextField NuevoSegundoApellido;
    private javax.swing.JTextField NuevoSegundoNombre;
    private javax.swing.JComboBox<String> NuevoTipoSancion;
    private javax.swing.JPanel PanelActualizarInformacion;
    private javax.swing.JPanel PanelActualizarSancion;
    private javax.swing.JPanel PanelAgendarCita;
    private javax.swing.JPanel PanelAsignarSanciones;
    private javax.swing.JPanel PanelBotones;
    private javax.swing.JPanel PanelListaGuardias;
    private javax.swing.JPanel PanelListaPresos;
    private javax.swing.JPanel PanelListaSanciones;
    private javax.swing.JPanel PanelPerfilOficial;
    private javax.swing.JLabel PlacaOficial;
    private javax.swing.JLabel SexoOficial;
    private javax.swing.JButton SubirNuevaFotoPerfil;
    private javax.swing.JTabbedPane TabbedOficial;
    private javax.swing.JTable TablaHistorialSanciones;
    private javax.swing.JTable TablaPresos;
    private javax.swing.JComboBox<String> TipoSancion;
    private javax.swing.JLabel TurnoOficial;
    private javax.swing.JLabel VistaPreviaNuevaFoto;
    private javax.swing.JButton botonAgendarCita;
    private javax.swing.JButton botonIrPanelActualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPopupMenu ppMenuTablaPresos;
    private javax.swing.JPopupMenu ppMenuTablaSanciones;
    private javax.swing.JTable tablaGuardias;
    // End of variables declaration//GEN-END:variables
   public JTextField getIdentificacionPresoCita() {
        return IdentificacionPresoCita;
    }

    public JTextField getIdentificacionGuardia() {
        return IdentificacionGuardia;
    }

    public JTextArea getMotivoCita() {
        return MotivoCita;
    }

    public JDateChooser getFechaCita() {
        return FechaCita;
    }

    public JComboBox<String> getComboHoraCita() {
        return JcomboHoraCita;
    }

    public JTextField getIdentificacionPresoSancion() {
        return IdentificacionPresoSancion;
    }

    public JTextArea getMotivoSancion() {
        return MotivoSancion;
    }

    public JComboBox<String> getTipoSancion() {
        return TipoSancion;
    }

    public JDateChooser getFechaSancion() {
        return FechaSancion;
    }

    public JTable getTablaPresos() {
        return TablaPresos;
    }

    public JTable getTablaGuardias() {
        return tablaGuardias;
    }

    public JTable getTablaHistorialSanciones() {
        return TablaHistorialSanciones;
    }

}
