
package View;

import Controller.ActividadController;
import Controller.DelitoController;
import Controller.ExpedienteController;
import Controller.PresoController;
import DAO.CeldaDAO;
import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Preso;
import Utilidades.Validador;
import java.awt.Color;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CoordinadorDeActividades extends javax.swing.JFrame {

    private final PresoController presoController;
    private final PresoDAO presoDAO;
    private final DelitoDAO delitoDAO;
    private final CeldaDAO celdaDAO;
    private final Validador validador;
    CeldaDAO celda = new CeldaDAO();
    private Preso presoOriginal;


    public CoordinadorDeActividades() {
        initComponents();
        inicializarMenuActividadesEspecifica();
        inicializarMenuActividadesPreso();
        inicializarMenuActividadesGeneral();
        
ActividadController controlador = new ActividadController();
controlador.cargarActividadesEnTabla(actividadesTabla);
        
        this.presoDAO = new PresoDAO();
        this.celdaDAO = new CeldaDAO();
        this.delitoDAO = new DelitoDAO();
        this.presoController = new PresoController(presoDAO, celda, delitoDAO);
        this.validador = new Validador(presoDAO);
    }

    
    public void inicializarMenuActividadesPreso(){
        JMenuItem AsignarActividad =  new JMenuItem("Asignar actividad");
        JMenuItem MostrarActividad = new JMenuItem("Ver actividades");
        
        ppMenuActividadesPreso.add(AsignarActividad);
        ppMenuActividadesPreso.add(MostrarActividad);
        
        TablaPresosCoor.setComponentPopupMenu(ppMenuActividadesPreso);
        
        AsignarActividad.addActionListener( e -> {
      
        CoordinadorDeActividades.setSelectedIndex(2);
     
        
    });
        
    }
   
    
    public void inicializarMenuActividadesEspecifica(){
          JMenuItem CambiarEstado =  new JMenuItem("Cambiar estado");
          ppMenuTablaActidadSelectiva.add(CambiarEstado);     
          tablaActividadPresos.setComponentPopupMenu(ppMenuTablaActidadSelectiva);     
          
    }
    
    public void inicializarMenuActividadesGeneral(){
        JMenuItem verPresosAsignados = new JMenuItem("Ver presos asignadados");
        
        ppMenuTablaActividadesGeneral.add(verPresosAsignados);
        tablaPresosAsignadosActividad.setComponentPopupMenu(ppMenuTablaActividadesGeneral);
        
        
         verPresosAsignados.addActionListener( e -> {
      
        CoordinadorDeActividades.setSelectedIndex(6);
     
        
    });
    }
    
    
     private void cargarTodosLosPresos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) TablaPresosCoor.getModel();
            modelo.setRowCount(0);

            List<Object[]> datosPresos = presoController.obtenerTodosLosPresosParaTabla();

            for (Object[] fila : datosPresos) {
                modelo.addRow(fila);
            }

            TablaPresosCoor.revalidate();
            TablaPresosCoor.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los presos: " + e.getMessage());
        }
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppMenuActividadesPreso = new javax.swing.JPopupMenu();
        ppMenuTablaActidadSelectiva = new javax.swing.JPopupMenu();
        ppMenuTablaActividadesGeneral = new javax.swing.JPopupMenu();
        Principal = new javax.swing.JPanel();
        PanelFondoTextoPrincipal = new javax.swing.JPanel();
        PanelPerfilS = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        PanelGestionActS = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        PanelAsignacionS = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CoordinadorDeActividades = new javax.swing.JTabbedPane();
        ListaActividades = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        actividadesTabla = new javax.swing.JTable();
        jPanel2 = new RoundedPanel(30);
        ;
        jPanel3 = new RoundedPanel(20)
        ;
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        BuscadorActividad = new javax.swing.JTextField();
        btnBuscarActividad = new javax.swing.JButton();
        CrearActividad = new javax.swing.JPanel();
        jPanel4 = new RoundedPanel(30);
        ;
        jLabel6 = new javax.swing.JLabel();
        btnRegresarLista = new javax.swing.JButton();
        NombreAct5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        cupoMax = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        NombreAct6 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        btnAñadirActividad = new javax.swing.JButton();
        AsignadorPreso = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaPresosCoor = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        ActualizarCoo = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jSeparator69 = new javax.swing.JSeparator();
        nuevoPrimerNombreC = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        jSeparator70 = new javax.swing.JSeparator();
        primerNuevoApellidoC = new javax.swing.JTextField();
        nuevaEdadC = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        jSeparator71 = new javax.swing.JSeparator();
        nuevaIdentiC = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jSeparator72 = new javax.swing.JSeparator();
        nuevaNacioC = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        jSeparator73 = new javax.swing.JSeparator();
        jLabel97 = new javax.swing.JLabel();
        jSeparator74 = new javax.swing.JSeparator();
        nuevoSexoC = new javax.swing.JComboBox<>();
        jLabel104 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        nuevoCorreoC = new javax.swing.JTextField();
        jSeparator75 = new javax.swing.JSeparator();
        jLabel103 = new javax.swing.JLabel();
        jSeparator76 = new javax.swing.JSeparator();
        nuevaContraC = new javax.swing.JTextField();
        LabelFOTOC = new javax.swing.JLabel();
        Actualizarimagenodr = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel112 = new javax.swing.JLabel();
        jSeparator83 = new javax.swing.JSeparator();
        jLabel113 = new javax.swing.JLabel();
        jSeparator85 = new javax.swing.JSeparator();
        segundoNuevoApellidoC = new javax.swing.JTextField();
        nuevoSegundoNombreC = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        btnActualizarCoordinador = new javax.swing.JButton();
        Perfil = new javax.swing.JPanel();
        jPanel1 = new RoundedPanel(30);

        ;
        jLabel4 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jSeparator34 = new javax.swing.JSeparator();
        jSeparator36 = new javax.swing.JSeparator();
        jSeparator37 = new javax.swing.JSeparator();
        jSeparator38 = new javax.swing.JSeparator();
        jSeparator39 = new javax.swing.JSeparator();
        jSeparator40 = new javax.swing.JSeparator();
        ApellidoCoor = new javax.swing.JLabel();
        IdentificacionCoor = new javax.swing.JLabel();
        EdadCoor = new javax.swing.JLabel();
        SexoCoor = new javax.swing.JLabel();
        NacionalidadCoor = new javax.swing.JLabel();
        nombreCoor = new javax.swing.JLabel();
        btnActualizarInfoCoor = new javax.swing.JButton();
        ActividadesPreso = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaActividadPresos = new javax.swing.JTable();
        jLabel67 = new javax.swing.JLabel();
        nom = new javax.swing.JLabel();
        jSeparator42 = new javax.swing.JSeparator();
        jLabel55 = new javax.swing.JLabel();
        ape = new javax.swing.JLabel();
        jSeparator49 = new javax.swing.JSeparator();
        jLabel66 = new javax.swing.JLabel();
        edad = new javax.swing.JLabel();
        jSeparator50 = new javax.swing.JSeparator();
        identi = new javax.swing.JLabel();
        jSeparator51 = new javax.swing.JSeparator();
        jLabel65 = new javax.swing.JLabel();
        naciona = new javax.swing.JLabel();
        jSeparator52 = new javax.swing.JSeparator();
        jPanel6 = new RoundedPanel(30);
        jLabel64 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        fotoPresoActividades = new javax.swing.JLabel();
        PresosEnActividad = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaPresosAsignadosActividad = new javax.swing.JTable();
        jPanel5 = new RoundedPanel(30);
        ;
        jPanel7 = new RoundedPanel(30);
        ;
        jLabel5 = new javax.swing.JLabel();
        lblNombreActividad = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Principal.setBackground(new java.awt.Color(255, 255, 255));
        Principal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelFondoTextoPrincipal.setBackground(new java.awt.Color(29, 35, 51));
        PanelFondoTextoPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelPerfilS.setBackground(new java.awt.Color(29, 35, 51));
        PanelPerfilS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelPerfilSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PanelPerfilSMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PanelPerfilSMouseExited(evt);
            }
        });
        PanelPerfilS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("PERFIL");
        PanelPerfilS.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 59, 30));

        PanelFondoTextoPrincipal.add(PanelPerfilS, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 50));

        PanelGestionActS.setBackground(new java.awt.Color(29, 35, 51));
        PanelGestionActS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelGestionActSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PanelGestionActSMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PanelGestionActSMouseExited(evt);
            }
        });
        PanelGestionActS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("GESTIÓN DE ACTIVIDADES");
        PanelGestionActS.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 210, 20));

        PanelFondoTextoPrincipal.add(PanelGestionActS, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 380, 50));

        PanelAsignacionS.setBackground(new java.awt.Color(29, 35, 51));
        PanelAsignacionS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelAsignacionSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PanelAsignacionSMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PanelAsignacionSMouseExited(evt);
            }
        });
        PanelAsignacionS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ASIGNACIÓN DE PRESOS");
        PanelAsignacionS.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 17, 210, 20));

        PanelFondoTextoPrincipal.add(PanelAsignacionS, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 0, 360, 50));

        Principal.add(PanelFondoTextoPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 50));

        ListaActividades.setBackground(new java.awt.Color(255, 255, 255));
        ListaActividades.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        actividadesTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "Nombre", "Tipo", "Día", "Horario", "Lugar", "Cupo maximo", "Inscritos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(actividadesTabla);

        ListaActividades.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 1020, 370));

        jPanel2.setBackground(new java.awt.Color(180, 180, 195));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(29, 35, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TIPO:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 60, 20));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Educativa", "Laboral", "Recreativa" }));
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 150, 40));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 80));

        jButton1.setBackground(new java.awt.Color(29, 35, 51));
        jButton1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Añadir actividad");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, 160, 40));

        ListaActividades.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 1020, 80));

        BuscadorActividad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscadorActividadActionPerformed(evt);
            }
        });
        ListaActividades.add(BuscadorActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 710, 40));

        btnBuscarActividad.setText("Buscar Actividad");
        ListaActividades.add(btnBuscarActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 37, 130, 40));

        CoordinadorDeActividades.addTab("tab2", ListaActividades);

        CrearActividad.setBackground(new java.awt.Color(255, 255, 255));
        CrearActividad.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(29, 35, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("AÑADIR ACTIVIDADES");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 190, -1));

        btnRegresarLista.setText("Regresar");
        btnRegresarLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarListaActionPerformed(evt);
            }
        });
        jPanel4.add(btnRegresarLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 10, 110, 20));

        CrearActividad.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 1030, 40));

        NombreAct5.setBackground(new java.awt.Color(204, 204, 204));
        NombreAct5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        CrearActividad.add(NombreAct5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 360, 200, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Nombre:");
        CrearActividad.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 70, 20));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Tipo:");
        CrearActividad.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 70, 20));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Identificación supervisor");
        CrearActividad.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, 190, 20));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Lugar:");
        CrearActividad.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 300, 70, 20));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Laboral", "Recreativa", "Educativa" }));
        CrearActividad.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 210, 290, 40));

        cupoMax.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "10", "20", "30" }));
        CrearActividad.add(cupoMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 280, 40));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Taller de Carpintería", "Taller de Soldadura/Herrería", "Taller de Costura y Confección", "Taller de Serigrafía/Estampado", "Taller de Reparación de Electrodomésticos", "Taller de Panadería/Repostería", "Taller de Jardinería y Vivero", "Aula de Alfabetización (Educación Básica)", "Aula de Educación Media/Superior", "Biblioteca Principal", "Sala de Computación", "Aula de Idiomas", "Sala de Talleres de Escritura", "Patio Central", "Cancha de Fútbol", "Cancha de Baloncesto", "Gimnasio (Máquinas/Pesas)", "Sala de Terapia Grupal", "Huerto Terapéutico", "Sala de Meditación/Mindfulness", "Taller de Manejo de Emociones", "Cocina Industrial", "Comedor Principal", "Lavandería/Ropería", "Sala de Estudios Bíblicos/Religiosos" }));
        CrearActividad.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 280, 290, 40));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Día:");
        CrearActividad.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 150, 90, 20));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Horario:");
        CrearActividad.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 230, 110, 20));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes" }));
        CrearActividad.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 140, 300, 40));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "07:00 am - 08:45 am", "08:45 am - 10:15 am", "10:45 am - 12:45 am", "02:00 pm - 04:15 pm", "04:15 pm - 05:15 pm" }));
        CrearActividad.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 210, 290, 40));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Cupo maximo:");
        CrearActividad.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 120, 20));

        NombreAct6.setBackground(new java.awt.Color(204, 204, 204));
        NombreAct6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        CrearActividad.add(NombreAct6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, 280, 30));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        CrearActividad.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 90, 20, 430));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        CrearActividad.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 30, 420));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        CrearActividad.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 520, 1020, 40));

        btnAñadirActividad.setBackground(new java.awt.Color(51, 51, 51));
        btnAñadirActividad.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnAñadirActividad.setForeground(new java.awt.Color(255, 255, 255));
        btnAñadirActividad.setText("Añadir Actividad");
        CrearActividad.add(btnAñadirActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 467, 210, 40));

        CoordinadorDeActividades.addTab("tab3", CrearActividad);

        AsignadorPreso.setBackground(new java.awt.Color(255, 255, 255));
        AsignadorPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaPresosCoor.setBackground(new java.awt.Color(204, 204, 204));
        TablaPresosCoor.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TablaPresosCoor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombres", "Apellidos", "Edad", "Identificación", "Nacionalidad", "Seccion", "Celda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaPresosCoor.setGridColor(new java.awt.Color(0, 0, 0));
        TablaPresosCoor.setShowGrid(false);
        TablaPresosCoor.setShowHorizontalLines(true);
        TablaPresosCoor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaPresosCoorMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(TablaPresosCoor);

        AsignadorPreso.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 1020, 390));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Presos disponibles para actividades");
        AsignadorPreso.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 280, -1));

        CoordinadorDeActividades.addTab("tab4", AsignadorPreso);

        ActualizarCoo.setBackground(new java.awt.Color(255, 255, 255));
        ActualizarCoo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel27.setBackground(new java.awt.Color(180, 180, 195));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel28.setBackground(new java.awt.Color(29, 35, 51));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 960, 10));

        jLabel98.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(0, 0, 0));
        jLabel98.setText("DATOS PERSONALES");
        jPanel27.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        jLabel99.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(0, 0, 0));
        jLabel99.setText("Segundo nombre:");
        jPanel27.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));

        jSeparator69.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator69, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 350, 10));

        nuevoPrimerNombreC.setBackground(new java.awt.Color(204, 204, 204));
        nuevoPrimerNombreC.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerNombreC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPrimerNombreC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPrimerNombreCActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoPrimerNombreC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 230, 30));

        jLabel100.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 0, 0));
        jLabel100.setText("Segundo apellido:");
        jPanel27.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, -1));

        jSeparator70.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator70, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 350, 10));

        primerNuevoApellidoC.setBackground(new java.awt.Color(204, 204, 204));
        primerNuevoApellidoC.setForeground(new java.awt.Color(0, 0, 0));
        primerNuevoApellidoC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        primerNuevoApellidoC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primerNuevoApellidoCActionPerformed(evt);
            }
        });
        jPanel27.add(primerNuevoApellidoC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 230, 30));

        nuevaEdadC.setBackground(new java.awt.Color(204, 204, 204));
        nuevaEdadC.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEdadC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaEdadC, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 280, 30));

        jLabel101.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 0, 0));
        jLabel101.setText("Edad: ");
        jPanel27.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, -1, -1));

        jSeparator71.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator71, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 350, 10));

        nuevaIdentiC.setBackground(new java.awt.Color(204, 204, 204));
        nuevaIdentiC.setForeground(new java.awt.Color(0, 0, 0));
        nuevaIdentiC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaIdentiC, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 240, 30));

        jLabel96.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(0, 0, 0));
        jLabel96.setText("Identificación:");
        jPanel27.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, -1, 20));

        jSeparator72.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator72, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 350, 10));

        nuevaNacioC.setBackground(new java.awt.Color(204, 204, 204));
        nuevaNacioC.setForeground(new java.awt.Color(0, 0, 0));
        nuevaNacioC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaNacioC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 230, 30));

        jLabel95.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(0, 0, 0));
        jLabel95.setText("Nacionalidad: ");
        jPanel27.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 330, -1, -1));

        jSeparator73.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator73, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 350, 350, 10));

        jLabel97.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(0, 0, 0));
        jLabel97.setText("Sexo:");
        jPanel27.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, -1, -1));

        jSeparator74.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator74, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 40, 10));

        nuevoSexoC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "F", "M" }));
        nuevoSexoC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSexoCActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoSexoC, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 130, 30));

        jLabel104.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(0, 0, 0));
        jLabel104.setText("CUENTA");
        jPanel27.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, -1, -1));

        jLabel102.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(0, 0, 0));
        jLabel102.setText("Correo Electronico:");
        jPanel27.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, -1, -1));

        nuevoCorreoC.setBackground(new java.awt.Color(180, 180, 195));
        nuevoCorreoC.setForeground(new java.awt.Color(0, 0, 0));
        nuevoCorreoC.setBorder(null);
        nuevoCorreoC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoCorreoCActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoCorreoC, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 250, 30));

        jSeparator75.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator75, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 130, 380, 10));

        jLabel103.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(0, 0, 0));
        jLabel103.setText("Contraseña:");
        jPanel27.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 160, -1, 20));

        jSeparator76.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator76, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, 380, 10));

        nuevaContraC.setBackground(new java.awt.Color(180, 180, 195));
        nuevaContraC.setForeground(new java.awt.Color(0, 0, 0));
        nuevaContraC.setBorder(null);
        nuevaContraC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaContraCActionPerformed(evt);
            }
        });
        jPanel27.add(nuevaContraC, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 260, 30));

        LabelFOTOC.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel27.add(LabelFOTOC, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 280, 100, 90));

        Actualizarimagenodr.setText("Actualizar foto");
        Actualizarimagenodr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarimagenodrActionPerformed(evt);
            }
        });
        jPanel27.add(Actualizarimagenodr, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, -1, -1));

        jPanel29.setBackground(new java.awt.Color(29, 35, 51));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 10));

        jPanel26.setBackground(new java.awt.Color(29, 35, 51));

        jPanel30.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel27.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 10, 430));

        jPanel31.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 10, 430));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 0, 10, 440));

        jLabel112.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(0, 0, 0));
        jLabel112.setText("Primer nombre:");
        jPanel27.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        jSeparator83.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator83, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 350, 10));

        jLabel113.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(0, 0, 0));
        jLabel113.setText("Primer apellido:");
        jPanel27.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, -1));

        jSeparator85.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator85, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 350, 10));

        segundoNuevoApellidoC.setBackground(new java.awt.Color(204, 204, 204));
        segundoNuevoApellidoC.setForeground(new java.awt.Color(0, 0, 0));
        segundoNuevoApellidoC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        segundoNuevoApellidoC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segundoNuevoApellidoCActionPerformed(evt);
            }
        });
        jPanel27.add(segundoNuevoApellidoC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 230, 30));

        nuevoSegundoNombreC.setBackground(new java.awt.Color(204, 204, 204));
        nuevoSegundoNombreC.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoNombreC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoSegundoNombreC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSegundoNombreCActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoSegundoNombreC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 230, 30));

        ActualizarCoo.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 960, 450));

        jLabel94.setBackground(new java.awt.Color(0, 0, 0));
        jLabel94.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(204, 0, 0));
        jLabel94.setText("Los campos que no desee modificar déjelos en blanco*");
        ActualizarCoo.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, 290, -1));

        btnActualizarCoordinador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizarCoordinador.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarCoordinador.setText("Actualizar");
        ActualizarCoo.add(btnActualizarCoordinador, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 547, 160, 40));

        CoordinadorDeActividades.addTab("tab5", ActualizarCoo);

        Perfil.setBackground(new java.awt.Color(255, 255, 255));
        Perfil.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(180, 180, 195));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 170, 160));

        jPanel22.setBackground(new java.awt.Color(29, 35, 51));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 20));

        jLabel44.setFont(new java.awt.Font("Arial", 2, 15)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(102, 102, 102));
        jLabel44.setText("Coordinador de actividades");
        jPanel1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, -1, 20));

        Perfil.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 270, 440));

        jPanel19.setBackground(new java.awt.Color(180, 180, 195));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel21.setBackground(new java.awt.Color(29, 35, 51));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel19.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 20));

        jLabel45.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("Apellidos:");
        jPanel19.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 140, -1));

        jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 0));
        jLabel50.setText("Identificacion:");
        jPanel19.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 110, -1));

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 0));
        jLabel51.setText("Edad:");
        jPanel19.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 50, -1));

        jLabel52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 0));
        jLabel52.setText("Sexo:");
        jPanel19.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 50, -1));

        jLabel53.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Nacionalidad:");
        jPanel19.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 100, -1));

        jLabel54.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("Nombres:");
        jPanel19.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 150, -1));

        jSeparator34.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 410, 10));

        jSeparator36.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator36, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 410, 10));

        jSeparator37.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator37, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 410, 10));

        jSeparator38.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator38, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 410, 10));

        jSeparator39.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator39, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 410, 10));

        jSeparator40.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator40, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 410, 10));

        ApellidoCoor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ApellidoCoor.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(ApellidoCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 260, 30));

        IdentificacionCoor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        IdentificacionCoor.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(IdentificacionCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 310, 30));

        EdadCoor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        EdadCoor.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(EdadCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 330, 30));

        SexoCoor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SexoCoor.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(SexoCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 330, 30));

        NacionalidadCoor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NacionalidadCoor.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(NacionalidadCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 330, 30));

        nombreCoor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nombreCoor.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(nombreCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 260, 30));

        Perfil.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, 490, 330));

        btnActualizarInfoCoor.setText("Actualizar información");
        btnActualizarInfoCoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarInfoCoorActionPerformed(evt);
            }
        });
        Perfil.add(btnActualizarInfoCoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 490, 160, 40));

        CoordinadorDeActividades.addTab("tab1", Perfil);

        ActividadesPreso.setBackground(new java.awt.Color(255, 255, 255));
        ActividadesPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaActividadPresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Tipo", "Lugar", "Dia", "Horario", "Estado", "Responsable"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaActividadPresos);

        ActividadesPreso.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 261, 1030, 330));

        jLabel67.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Nombre  completo:");
        ActividadesPreso.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, 20));

        nom.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(nom, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 330, 20));

        jSeparator42.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 520, 10));

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("Apellidos completos:");
        ActividadesPreso.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 150, 20));

        ape.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(ape, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 300, 20));

        jSeparator49.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(jSeparator49, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 520, 10));

        jLabel66.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Edad:");
        ActividadesPreso.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, 20));

        edad.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(edad, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 420, 20));

        jSeparator50.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(jSeparator50, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 520, 10));

        identi.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(identi, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 370, 20));

        jSeparator51.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(jSeparator51, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 520, 10));

        jLabel65.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("Nacionalidad:");
        ActividadesPreso.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, 20));

        naciona.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(naciona, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 380, 20));

        jSeparator52.setForeground(new java.awt.Color(0, 0, 0));
        ActividadesPreso.add(jSeparator52, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 520, 10));

        jPanel6.setBackground(new java.awt.Color(29, 35, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel64.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Total de actividades");
        jPanel6.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, 20));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 150, 40));

        ActividadesPreso.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 170, 420, 60));

        jLabel68.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("Identificación:");
        ActividadesPreso.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, -1, 20));

        fotoPresoActividades.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        ActividadesPreso.add(fotoPresoActividades, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 40, 170, 120));

        CoordinadorDeActividades.addTab("tab6", ActividadesPreso);

        PresosEnActividad.setBackground(new java.awt.Color(255, 255, 255));
        PresosEnActividad.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaPresosAsignadosActividad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Apellido", "Edad", "Identificación", "Horario", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaPresosAsignadosActividad);

        PresosEnActividad.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 1050, 440));

        jPanel5.setBackground(new java.awt.Color(180, 180, 195));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(29, 35, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Actividad: ");
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 100, 30));

        lblNombreActividad.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblNombreActividad.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(lblNombreActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 240, 30));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 430, 70));

        PresosEnActividad.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 1040, 70));

        CoordinadorDeActividades.addTab("tab7", PresosEnActividad);

        Principal.add(CoordinadorDeActividades, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 650));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarInfoCoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarInfoCoorActionPerformed



    }//GEN-LAST:event_btnActualizarInfoCoorActionPerformed

    private void nuevoPrimerNombreCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoPrimerNombreCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoPrimerNombreCActionPerformed

    private void primerNuevoApellidoCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primerNuevoApellidoCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_primerNuevoApellidoCActionPerformed

    private void nuevoSexoCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSexoCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoSexoCActionPerformed

    private void nuevoCorreoCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoCorreoCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoCorreoCActionPerformed

    private void nuevaContraCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaContraCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevaContraCActionPerformed

    private void ActualizarimagenodrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarimagenodrActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_ActualizarimagenodrActionPerformed

    private void segundoNuevoApellidoCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segundoNuevoApellidoCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_segundoNuevoApellidoCActionPerformed

    private void nuevoSegundoNombreCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSegundoNombreCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoSegundoNombreCActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    CoordinadorDeActividades.setSelectedIndex(1);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void BuscadorActividadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscadorActividadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscadorActividadActionPerformed

    private void btnRegresarListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarListaActionPerformed
CoordinadorDeActividades.setSelectedIndex(0);

    }//GEN-LAST:event_btnRegresarListaActionPerformed

    private void TablaPresosCoorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaPresosCoorMousePressed

    }//GEN-LAST:event_TablaPresosCoorMousePressed

    private void PanelPerfilSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPerfilSMouseClicked
CoordinadorDeActividades.setSelectedIndex(4);

    }//GEN-LAST:event_PanelPerfilSMouseClicked

    private void PanelGestionActSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelGestionActSMouseClicked
CoordinadorDeActividades.setSelectedIndex(0);

    }//GEN-LAST:event_PanelGestionActSMouseClicked

    private void PanelAsignacionSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelAsignacionSMouseClicked
CoordinadorDeActividades.setSelectedIndex(2);

    }//GEN-LAST:event_PanelAsignacionSMouseClicked

    private void PanelPerfilSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPerfilSMouseEntered
        PanelPerfilS.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_PanelPerfilSMouseEntered

    private void PanelGestionActSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelGestionActSMouseEntered
        PanelGestionActS.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_PanelGestionActSMouseEntered

    private void PanelAsignacionSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelAsignacionSMouseEntered
        PanelAsignacionS.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_PanelAsignacionSMouseEntered

    private void PanelPerfilSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPerfilSMouseExited
        PanelPerfilS.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_PanelPerfilSMouseExited

    private void PanelGestionActSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelGestionActSMouseExited
        PanelGestionActS.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_PanelGestionActSMouseExited

    private void PanelAsignacionSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelAsignacionSMouseExited
        PanelAsignacionS.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_PanelAsignacionSMouseExited

   
    
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
            java.util.logging.Logger.getLogger(CoordinadorDeActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CoordinadorDeActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CoordinadorDeActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CoordinadorDeActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CoordinadorDeActividades().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActividadesPreso;
    private javax.swing.JPanel ActualizarCoo;
    private javax.swing.JButton Actualizarimagenodr;
    private javax.swing.JLabel ApellidoCoor;
    private javax.swing.JPanel AsignadorPreso;
    private javax.swing.JTextField BuscadorActividad;
    private javax.swing.JTabbedPane CoordinadorDeActividades;
    private javax.swing.JPanel CrearActividad;
    private javax.swing.JLabel EdadCoor;
    private javax.swing.JLabel IdentificacionCoor;
    private javax.swing.JLabel LabelFOTOC;
    private javax.swing.JPanel ListaActividades;
    private javax.swing.JLabel NacionalidadCoor;
    private javax.swing.JTextField NombreAct5;
    private javax.swing.JTextField NombreAct6;
    private javax.swing.JPanel PanelAsignacionS;
    private javax.swing.JPanel PanelFondoTextoPrincipal;
    private javax.swing.JPanel PanelGestionActS;
    private javax.swing.JPanel PanelPerfilS;
    private javax.swing.JPanel Perfil;
    private javax.swing.JPanel PresosEnActividad;
    private javax.swing.JPanel Principal;
    private javax.swing.JLabel SexoCoor;
    private javax.swing.JTable TablaPresosCoor;
    private javax.swing.JTable actividadesTabla;
    private javax.swing.JLabel ape;
    private javax.swing.JButton btnActualizarCoordinador;
    private javax.swing.JButton btnActualizarInfoCoor;
    private javax.swing.JButton btnAñadirActividad;
    private javax.swing.JButton btnBuscarActividad;
    private javax.swing.JButton btnRegresarLista;
    private javax.swing.JComboBox<String> cupoMax;
    private javax.swing.JLabel edad;
    private javax.swing.JLabel fotoPresoActividades;
    private javax.swing.JLabel identi;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator49;
    private javax.swing.JSeparator jSeparator50;
    private javax.swing.JSeparator jSeparator51;
    private javax.swing.JSeparator jSeparator52;
    private javax.swing.JSeparator jSeparator69;
    private javax.swing.JSeparator jSeparator70;
    private javax.swing.JSeparator jSeparator71;
    private javax.swing.JSeparator jSeparator72;
    private javax.swing.JSeparator jSeparator73;
    private javax.swing.JSeparator jSeparator74;
    private javax.swing.JSeparator jSeparator75;
    private javax.swing.JSeparator jSeparator76;
    private javax.swing.JSeparator jSeparator83;
    private javax.swing.JSeparator jSeparator85;
    private javax.swing.JLabel lblNombreActividad;
    private javax.swing.JLabel naciona;
    private javax.swing.JLabel nom;
    private javax.swing.JLabel nombreCoor;
    private javax.swing.JTextField nuevaContraC;
    private javax.swing.JTextField nuevaEdadC;
    private javax.swing.JTextField nuevaIdentiC;
    private javax.swing.JTextField nuevaNacioC;
    private javax.swing.JTextField nuevoCorreoC;
    private javax.swing.JTextField nuevoPrimerNombreC;
    private javax.swing.JTextField nuevoSegundoNombreC;
    private javax.swing.JComboBox<String> nuevoSexoC;
    private javax.swing.JPopupMenu ppMenuActividadesPreso;
    private javax.swing.JPopupMenu ppMenuTablaActidadSelectiva;
    private javax.swing.JPopupMenu ppMenuTablaActividadesGeneral;
    private javax.swing.JTextField primerNuevoApellidoC;
    private javax.swing.JTextField segundoNuevoApellidoC;
    private javax.swing.JTable tablaActividadPresos;
    private javax.swing.JTable tablaPresosAsignadosActividad;
    // End of variables declaration//GEN-END:variables
}
