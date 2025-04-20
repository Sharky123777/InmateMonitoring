package View;

import DAO.GuardiaDAO;
import DAO.PresoDAO;
import DAO.SancionDAO;
import Model.Guardia;
import Model.Preso;
import Model.Sancion;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Oficial extends javax.swing.JFrame {

    private File imagenOficialSeleccionada;
    private String presoSeleccionadoIdentificacion;

    public Oficial() {
        initComponents();
        this.setLocationRelativeTo(null);
        FechaSancion.getDateEditor().setEnabled(false);
        cargarDatosPresoEnTabla();
        cargarDatosGuardiaEnTabla();
        configurarTablaImagenesPreso();
        InicializarMenu();
        ComboTipoSancion.addActionListener(e -> filtrarSanciones());

    }

    private void InicializarMenu() {
        JMenuItem historialSanciones = new JMenuItem("Historial de sanciones");

        ppMenuTablaPresos.add(historialSanciones);
        TablaPresos.setComponentPopupMenu(ppMenuTablaPresos);

        historialSanciones.addActionListener(e -> {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                mostrarError("¡Selecciona un preso primero!");
                return;
            }

            presoSeleccionadoIdentificacion = TablaPresos.getValueAt(filaSeleccionada, 4).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(presoSeleccionadoIdentificacion);

            if (preso != null) {
                cargarHistorialSancionesCompleto();
                TabbedOficial.setSelectedIndex(6);
            } else {
                mostrarError("No se encontró el preso");
            }
        });
    }

    private void filtrarSanciones() {
        if (presoSeleccionadoIdentificacion == null) {
            return;
        }

        String tipoSeleccionado = ComboTipoSancion.getSelectedItem().toString();

        if (tipoSeleccionado == null || tipoSeleccionado.equals("< Seleccionar >")) {
            cargarHistorialSancionesCompleto();
        } else {
            cargarHistorialSancionesFiltrado(tipoSeleccionado);
        }
    }

    private void cargarHistorialSancionesCompleto() {
        DefaultTableModel modelo = (DefaultTableModel) TablaHistorialSanciones.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = new SancionDAO().cargarPorIdentificacionPreso(presoSeleccionadoIdentificacion);

        llenarTablaSanciones(modelo, sanciones);
    }

    private void cargarHistorialSancionesFiltrado(String tipoSancion) {
        DefaultTableModel modelo = (DefaultTableModel) TablaHistorialSanciones.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = new SancionDAO().cargarPorTipoYIdentificacionPreso(tipoSancion, presoSeleccionadoIdentificacion);

        llenarTablaSanciones(modelo, sanciones);
    }

    private void llenarTablaSanciones(DefaultTableModel modelo, List<Sancion> sanciones) {
        for (Sancion sancion : sanciones) {
            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo()
            });
        }

        TablaHistorialSanciones.revalidate();
        TablaHistorialSanciones.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppMenuTablaPresos = new javax.swing.JPopupMenu();
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
        jLabel7 = new javax.swing.JLabel();
        CerrarSesionBoton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        FechaIngresoOficial = new javax.swing.JLabel();
        TurnoOficial = new javax.swing.JLabel();
        FondoFoto = new javax.swing.JPanel();
        FotoOficial = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
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
        jLabel3 = new javax.swing.JLabel();
        PanelListaPresos = new javax.swing.JPanel();
        BarraDeBusquedaPreso = new javax.swing.JTextField();
        BotonBuscarPresoIdentificacion = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaPresos = new javax.swing.JTable();
        BotonCargarTodosPresos = new javax.swing.JButton();
        PanelListaGuardias = new javax.swing.JPanel();
        BarraDeBusquedaGuardias = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaGuardias = new javax.swing.JTable();
        BotonBuscarGuardia = new javax.swing.JButton();
        BotonCargarTodosGuardias = new javax.swing.JButton();
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
        Motivo = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        FechaCita = new com.toedter.calendar.JDateChooser();
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

        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Fecha de ingreso");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, -1, -1));

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
        jPanel6.add(FechaIngresoOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 220, 30));
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

        PanelPerfilOficial.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 300, 530));

        jPanel1.setBackground(new java.awt.Color(139, 139, 157));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelPerfilOficial.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("INFORMACIÓN PERSONAL");
        PanelPerfilOficial.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, -1, -1));

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
        jPanel12.add(botonIrPanelActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, 150, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 2, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Para actualizar su información personal:");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 340, -1));

        PanelPerfilOficial.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 630, 450));

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

        TabbedOficial.addTab("PRESOS", PanelListaPresos);

        PanelListaGuardias.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaGuardias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BarraDeBusquedaGuardias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarraDeBusquedaGuardiasActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BarraDeBusquedaGuardias, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 670, 30));

        TablaGuardias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Nombre completo", "Edad", "Identificacion", "Nacionalidad", "Correo electronico", "Turno", "Cargo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TablaGuardias);
        if (TablaGuardias.getColumnModel().getColumnCount() > 0) {
            TablaGuardias.getColumnModel().getColumn(0).setResizable(false);
            TablaGuardias.getColumnModel().getColumn(1).setResizable(false);
            TablaGuardias.getColumnModel().getColumn(2).setResizable(false);
            TablaGuardias.getColumnModel().getColumn(3).setResizable(false);
            TablaGuardias.getColumnModel().getColumn(4).setResizable(false);
            TablaGuardias.getColumnModel().getColumn(5).setResizable(false);
            TablaGuardias.getColumnModel().getColumn(6).setResizable(false);
        }

        PanelListaGuardias.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 980, 450));

        BotonBuscarGuardia.setText("Buscar guardia por identificacion");
        BotonBuscarGuardia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonBuscarGuardiaActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BotonBuscarGuardia, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, -1, 30));

        BotonCargarTodosGuardias.setText("Cargar todos Los guardias");
        BotonCargarTodosGuardias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCargarTodosGuardiasActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BotonCargarTodosGuardias, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, 170, -1));

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
        jPanel2.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, -1));

        IdentificacionGuardia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdentificacionGuardiaActionPerformed(evt);
            }
        });
        jPanel2.add(IdentificacionGuardia, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 380, 30));

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
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));

        Motivo.setColumns(20);
        Motivo.setRows(5);
        jScrollPane3.setViewportView(Motivo);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 360, 230));

        jButton2.setText("Agendar cita medica");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 190, 30));
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 390, 20));
        jPanel2.add(FechaCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 380, 30));

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

    private void cargarDatosPresoEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = null;
            if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                foto = cargarImagenPreso(preso.getFotoPath());
            } else {
                foto = null;
            }

            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombreCompleto(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()

            });
        }

        TablaPresos.revalidate();
        TablaPresos.repaint();
    }

    private ImageIcon cargarImagenPreso(String path) {
        if (path == null || !new File(path).exists()) {
            return null;
        }

        try {
            Image img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    private void configurarTablaImagenesPreso() {
        TablaPresos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (column == 0 && value instanceof ImageIcon) {
                    ImageIcon originalIcon = (ImageIcon) value;
                    Image img = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    ImageIcon roundedIcon = new ImageIcon(createRoundedImage(img));
                    label.setIcon(roundedIcon);
                    label.setText("");
                } else {
                    label.setIcon(null);
                }
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        TablaPresos.setRowHeight(65);
        TablaPresos.getColumnModel().getColumn(0).setPreferredWidth(70);
    }

    private Image createRoundedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        output = g2.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2.dispose();
        g2 = output.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, width, height, 20, 20);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }

    private void cargarDatosGuardiaEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) TablaGuardias.getModel();
        modelo.setRowCount(0);

        GuardiaDAO guardiaDAO = new GuardiaDAO();
        List<Guardia> guardias = guardiaDAO.obtenerGuardias();

        for (Guardia guardia : guardias) {
            ImageIcon foto = null;
            if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
                foto = cargarImagenGuardia(guardia.getRutaImagen());

            } else {
                foto = null;
            }

            modelo.addRow(new Object[]{
                foto,
                guardia.getNombreCompleto(),
                guardia.getEdad(),
                guardia.getIdentificacion(),
                guardia.getNacionalidad(),
                guardia.getCorreo(),
                guardia.getTurno(),
                guardia.getCargo()

            });

        }

    }

    private ImageIcon cargarImagenGuardia(String path) {
        if (path == null || !new File(path).exists()) {
            return null;
        }

        try {
            Image img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
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

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean validarCamposSancion() {
        String identificacion = IdentificacionPresoSancion.getText().trim();
        if (identificacion.isEmpty()) {
            mostrarError("Debe ingresar la identificación del preso");
            return false;
        }

        String motivo = MotivoSancion.getText().trim();
        if (motivo.isEmpty()) {
            mostrarError("Debe especificar el motivo de la sanción");
            return false;
        }

        String tipoSancion = TipoSancion.getSelectedItem().toString();
        if (tipoSancion.equals("Seleccionar")) {
            mostrarError("Debe seleccionar un tipo de sanción");
            return false;
        }

        return true;
    }

    private void limpiarCamposSancion() {
        IdentificacionPresoSancion.setText("");
        MotivoSancion.setText("");
        TipoSancion.setSelectedIndex(0);
        FechaSancion.setDate(null);
    }
    private void BotonAsignarSancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAsignarSancionActionPerformed
        if (!validarCamposSancion()) {
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

    }//GEN-LAST:event_BotonAsignarSancionActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ComboTipoSancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboTipoSancionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboTipoSancionActionPerformed

    private void BotonBuscarGuardiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonBuscarGuardiaActionPerformed
        BotonBuscarGuardia.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                String idBuscado = BarraDeBusquedaGuardias.getText().trim();
                if (idBuscado.isEmpty()) {
                    return;
                }

                String identificacion = BarraDeBusquedaGuardias.getText();
                List<Guardia> todos = new GuardiaDAO().obtenerGuardias();
                DefaultTableModel modelo = (DefaultTableModel) TablaGuardias.getModel();
                modelo.setRowCount(0);

                for (Guardia guardia : todos) {
                    if (guardia.getIdentificacion().equalsIgnoreCase(identificacion)) {

                        ImageIcon foto = null;
                        if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
                            foto = cargarImagenPreso(guardia.getRutaImagen());
                        } else {
                            foto = null;
                        }
                        modelo.addRow(new Object[]{
                            foto,
                            guardia.getNombreCompleto(),
                            guardia.getEdad(),
                            guardia.getIdentificacion(),
                            guardia.getNacionalidad(),
                            guardia.getCorreo(),
                            guardia.getTurno(),
                            guardia.getCargo()

                        });
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro ningun guardia con la identificación " + identificacion);
                    }
                }
            }
        });    }//GEN-LAST:event_BotonBuscarGuardiaActionPerformed

    private void BarraDeBusquedaGuardiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarraDeBusquedaGuardiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BarraDeBusquedaGuardiasActionPerformed

    private void botonIrPanelActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonIrPanelActualizarMouseClicked
        TabbedOficial.setSelectedIndex(4);
    }//GEN-LAST:event_botonIrPanelActualizarMouseClicked

    private void BotonBuscarPresoIdentificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonBuscarPresoIdentificacionActionPerformed
        BotonBuscarPresoIdentificacion.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                String idBuscado = BarraDeBusquedaPreso.getText().trim();
                if (idBuscado.isEmpty()) {
                    return;
                }

                String identificacion = BarraDeBusquedaPreso.getText();
                List<Preso> todos = new PresoDAO().cargarTodos();
                DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
                modelo.setRowCount(0);

                for (Preso preso : todos) {
                    if (preso.getIdentificacion().equalsIgnoreCase(identificacion)) {

                        ImageIcon foto = null;
                        if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                            foto = cargarImagenPreso(preso.getFotoPath());
                        } else {
                            foto = null;
                        }
                        modelo.addRow(new Object[]{
                            foto,
                            preso.getId(),
                            preso.getNombreCompleto(),
                            preso.getEdad(),
                            preso.getIdentificacion(),
                            preso.getNacionalidad(),
                            preso.getCeldaAsignada(),
                            preso.getSeccionAsignada()

                        });
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro ningun preso con la identificación " + identificacion);
                    }
                }
            }
        });

    }//GEN-LAST:event_BotonBuscarPresoIdentificacionActionPerformed

    private void BotonCargarTodosPresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCargarTodosPresosActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = null;
            if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                foto = cargarImagenPreso(preso.getFotoPath());
            } else {
                foto = new ImageIcon(getClass().getResource("/images/default_profile.png"));
            }

            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombreCompleto(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()

            });
        }

        TablaPresos.revalidate();
        TablaPresos.repaint();
     }//GEN-LAST:event_BotonCargarTodosPresosActionPerformed

    private void BotonCargarTodosGuardiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCargarTodosGuardiasActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) TablaGuardias.getModel();
        modelo.setRowCount(0);

        GuardiaDAO guardiaDAO = new GuardiaDAO();
        List<Guardia> guardias = guardiaDAO.obtenerGuardias();

        for (Guardia guardia : guardias) {
            ImageIcon foto = null;
            if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
                foto = cargarImagenGuardia(guardia.getRutaImagen());

            } else {
                foto = null;
            }

            modelo.addRow(new Object[]{
                foto,
                guardia.getNombreCompleto(),
                guardia.getEdad(),
                guardia.getIdentificacion(),
                guardia.getNacionalidad(),
                guardia.getCorreo(),
                guardia.getTurno(),
                guardia.getCargo()

            });

        }
    }//GEN-LAST:event_BotonCargarTodosGuardiasActionPerformed

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
    private javax.swing.JButton BotonBuscarGuardia;
    private javax.swing.JButton BotonBuscarPresoIdentificacion;
    private javax.swing.JButton BotonCargarTodosGuardias;
    private javax.swing.JButton BotonCargarTodosPresos;
    private javax.swing.JButton CerrarSesionBoton;
    private javax.swing.JComboBox<String> ComboTipoSancion;
    private javax.swing.JTextField ContraseñaActual;
    private javax.swing.JLabel EdadOficial;
    private javax.swing.JLabel EmailOficial;
    private com.toedter.calendar.JDateChooser FechaCita;
    private javax.swing.JLabel FechaIngresoOficial;
    private com.toedter.calendar.JDateChooser FechaSancion;
    private javax.swing.JPanel FondoFoto;
    private javax.swing.JLabel FotoOficial;
    private javax.swing.JTextField IdentificacionGuardia;
    private javax.swing.JLabel IdentificacionOficial;
    private javax.swing.JTextField IdentificacionPresoCita;
    private javax.swing.JTextField IdentificacionPresoSancion;
    private javax.swing.JTextArea Motivo;
    private javax.swing.JTextArea MotivoSancion;
    private javax.swing.JLabel NacionalidadOficial;
    private javax.swing.JLabel NombreCompletoOficial;
    private javax.swing.JTextField NuevaContraseña;
    private javax.swing.JTextField NuevaNacionalidad;
    private javax.swing.JTextField NuevoEmail;
    private javax.swing.JTextField NuevoPrimerApellido;
    private javax.swing.JTextField NuevoPrimerNombre;
    private javax.swing.JTextField NuevoSegundoApellido;
    private javax.swing.JTextField NuevoSegundoNombre;
    private javax.swing.JPanel PanelActualizarInformacion;
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
    private javax.swing.JTable TablaGuardias;
    private javax.swing.JTable TablaHistorialSanciones;
    private javax.swing.JTable TablaPresos;
    private javax.swing.JComboBox<String> TipoSancion;
    private javax.swing.JLabel TurnoOficial;
    private javax.swing.JLabel VistaPreviaNuevaFoto;
    private javax.swing.JButton botonIrPanelActualizar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPopupMenu ppMenuTablaPresos;
    // End of variables declaration//GEN-END:variables
}
