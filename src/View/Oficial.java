package View;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Oficial extends javax.swing.JFrame {

    private File imagenOficialSeleccionada;

    public Oficial() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelBotones = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
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
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        PanelListaPresos = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        ComboSeccionPreso = new javax.swing.JComboBox<>();
        BarraDeBusquedaPreso = new javax.swing.JTextField();
        BotonBuscarPreso = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaPresos = new javax.swing.JTable();
        PanelListaGuardias = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        ComboSeccionGuardia = new javax.swing.JComboBox<>();
        BarraDeBusquedaGuardias = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaGuardias = new javax.swing.JTable();
        BotonBuscarGuardia = new javax.swing.JButton();
        PanelAgendarCita = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
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
        jTextField4 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel19 = new javax.swing.JPanel();

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

        jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("AGENDAR CITA");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

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

        jButton1.setText("Actualizar información");
        jPanel12.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, 150, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 2, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Para actualizar su información personal:");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 340, -1));

        PanelPerfilOficial.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 630, 450));

        TabbedOficial.addTab("PERFIL", PanelPerfilOficial);

        PanelListaPresos.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaPresos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(180, 180, 195));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ComboSeccionPreso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seccion A", "Seccion B", "Seccion C" }));
        jPanel13.add(ComboSeccionPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 120, 30));

        PanelListaPresos.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 200, 50));
        PanelListaPresos.add(BarraDeBusquedaPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 600, 30));

        BotonBuscarPreso.setText("Buscar");
        PanelListaPresos.add(BotonBuscarPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 30, 90, 30));

        TablaPresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombre completo", "Edad", "Identificacion", "Nacionalidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaPresos);

        PanelListaPresos.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 980, 460));

        TabbedOficial.addTab("PRESOS", PanelListaPresos);

        PanelListaGuardias.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaGuardias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(180, 180, 195));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ComboSeccionGuardia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seccion A", "Seccion B", "Seccion C" }));
        jPanel14.add(ComboSeccionGuardia, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 120, 30));

        PanelListaGuardias.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 200, 50));

        BarraDeBusquedaGuardias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarraDeBusquedaGuardiasActionPerformed(evt);
            }
        });
        PanelListaGuardias.add(BarraDeBusquedaGuardias, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 600, 30));

        TablaGuardias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombre completo", "Edad", "Identificacion", "Nacionalidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TablaGuardias);

        PanelListaGuardias.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 980, 460));

        BotonBuscarGuardia.setText("Buscar");
        PanelListaGuardias.add(BotonBuscarGuardia, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 30, 90, 30));

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

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 380, 30));

        jLabel52.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 0));
        jLabel52.setText("Motivo de la cita medica");
        jPanel2.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, -1, -1));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 380, 30));

        jLabel53.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Identificación del preso:");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("Fecha de la cita:");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 360, 230));

        jButton2.setText("Agendar cita medica");
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 190, 30));
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 390, 20));
        jPanel2.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 380, 30));

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

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 380, 30));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("Identificación del preso:");
        jPanel3.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel59.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("Fecha de la sanción:");
        jPanel3.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane4.setViewportView(jTextArea2);

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 360, 230));

        jButton3.setText("Asignar sanción");
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 190, 30));
        jPanel3.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 390, 20));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Amonestación verbal o escrita", "Limitación de actividades recreativas o deportivas", "Suspensión de visitas o comunicaciones", "Aislamiento en celda disciplinaria" }));
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 380, 30));

        PanelAsignarSanciones.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 980, 490));

        jPanel19.setBackground(new java.awt.Color(139, 139, 157));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelAsignarSanciones.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 80));

        TabbedOficial.addTab("ASIGNAR SANCIONES", PanelAsignarSanciones);

        getContentPane().add(TabbedOficial, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1100, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BarraDeBusquedaGuardiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarraDeBusquedaGuardiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BarraDeBusquedaGuardiasActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

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
    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

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
    private javax.swing.JButton BotonBuscarGuardia;
    private javax.swing.JButton BotonBuscarPreso;
    private javax.swing.JButton CerrarSesionBoton;
    private javax.swing.JComboBox<String> ComboSeccionGuardia;
    private javax.swing.JComboBox<String> ComboSeccionPreso;
    private javax.swing.JTextField ContraseñaActual;
    private javax.swing.JLabel EdadOficial;
    private javax.swing.JLabel EmailOficial;
    private javax.swing.JLabel FechaIngresoOficial;
    private javax.swing.JPanel FondoFoto;
    private javax.swing.JLabel FotoOficial;
    private javax.swing.JLabel IdentificacionOficial;
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
    private javax.swing.JPanel PanelPerfilOficial;
    private javax.swing.JLabel PlacaOficial;
    private javax.swing.JLabel SexoOficial;
    private javax.swing.JButton SubirNuevaFotoPerfil;
    private javax.swing.JTabbedPane TabbedOficial;
    private javax.swing.JTable TablaGuardias;
    private javax.swing.JTable TablaPresos;
    private javax.swing.JLabel TurnoOficial;
    private javax.swing.JLabel VistaPreviaNuevaFoto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
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
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
