package View;

import Controller.VisitaController;
import DAO.PresoDAO;
import DAO.VisitaDAO;
import DAO.VisitanteDAO;
import Model.Entities.Preso;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PersonalDeControl extends javax.swing.JFrame {

    private VisitaController controller = new VisitaController();
    private List<Visitante> visitantesTemporales = new ArrayList<>();
    private File imagenVisitanteSeleccionada;
    private File imagenVisitanteNuevaSeleccionada;
    private File imagenPDCSeleccionada;
    private List<File> imagenesTemporales = new ArrayList<>();
    int cantidadTotal;

    public PersonalDeControl() {
        initComponents();
        controller.verificarVisitasVencidas();
        FechaVisita.getDateEditor().setEnabled(false);
        this.setLocationRelativeTo(null);
        controller.cargarTodosLosPresos(TablaPresos);
        controller.configurarTablaImagenes(TablaPresos);
        controller.configurarTablaImagenes(TablaHistorialVisitantes);
        InicializarMenu();
        inicializarMenuHistorialVisitantes();
        inicializarMenuHistorialVisitas();

    }

    public void InicializarMenu() {
        JMenuItem historialVisita = new JMenuItem("Historial de visitas");
        JMenuItem historialVisitantes = new JMenuItem("Historial visitantes");

        ppMenuTablaPresos.add(historialVisita);
        ppMenuTablaPresos.add(historialVisitantes);

        TablaPresos.setComponentPopupMenu(ppMenuTablaPresos);
        TablaPresos.setComponentPopupMenu(ppMenuTablaPresos);

        historialVisita.addActionListener(e -> {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "¡Selecciona un preso primero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identificacion = TablaPresos.getValueAt(filaSeleccionada, 6).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

            if (preso != null) {
                controller.cargarHistorialVisitas(identificacion, TablaHistorialVisitas);
                TabbedPDC.setSelectedIndex(4);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el preso", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        historialVisitantes.addActionListener(e -> {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "¡Selecciona un preso primero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identificacion = TablaPresos.getValueAt(filaSeleccionada, 6).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

            if (preso != null) {
                controller.cargarHistorialVisitantes(identificacion, TablaHistorialVisitantes);
                TabbedPDC.setSelectedIndex(5);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el preso", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void inicializarMenuHistorialVisitantes() {
        JMenuItem actualizarInfo = new JMenuItem("Actualizar visitante");
        JMenuItem cambiarEstado = new JMenuItem("Cambiar estado visitante");

        ppMenuTablaVisitantes.add(actualizarInfo);
        ppMenuTablaVisitantes.add(cambiarEstado);
        TablaHistorialVisitantes.setComponentPopupMenu(ppMenuTablaVisitantes);

        actualizarInfo.addActionListener(e -> {
            int fila = TablaHistorialVisitantes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un visitante",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identificacion = TablaHistorialVisitantes.getValueAt(fila, 6).toString();
            String identificacionPreso = TablaHistorialVisitantes.getValueAt(fila, 11).toString(); // Asumiendo que la columna 11 es la ID del preso

            Visitante visitante = new VisitanteDAO().buscarVisitantePorIdentificacion(identificacion);

            if (visitante != null) {
                ModificarVisitante dialog = new ModificarVisitante(
                        this,
                        true,
                        visitante,
                        TablaHistorialVisitantes,
                        identificacionPreso
                );
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);

                controller.cargarHistorialVisitantes(identificacionPreso, TablaHistorialVisitantes);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Visitante no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cambiarEstado.addActionListener(e -> {
            int fila = TablaHistorialVisitantes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un visitante",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identificacion = TablaHistorialVisitantes.getValueAt(fila, 6).toString();
            String identificacionPreso = TablaHistorialVisitantes.getValueAt(fila, 11).toString();

            Visitante visitante = new VisitanteDAO().buscarVisitantePorIdentificacion(identificacion);

            if (visitante != null) {
                CambioEstadoVisitante dialog = new CambioEstadoVisitante(
                        this,
                        true,
                        visitante,
                        identificacionPreso,
                        TablaHistorialVisitantes
                );
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);

                controller.cargarHistorialVisitantes(identificacionPreso, TablaHistorialVisitantes);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Visitante no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void inicializarMenuHistorialVisitas() {
        JMenuItem actualizarInfo = new JMenuItem("Actualizar visita");
        JMenuItem cambiarEstado = new JMenuItem("Cambiar estado visita");

        ppMenuTablaVisitas.add(actualizarInfo);
        ppMenuTablaVisitas.add(cambiarEstado);
        TablaHistorialVisitas.setComponentPopupMenu(ppMenuTablaVisitas);
        actualizarInfo.addActionListener(e -> {
            int fila = TablaHistorialVisitas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una visita",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int idVisita = Integer.parseInt(TablaHistorialVisitas.getValueAt(fila, 0).toString());
                String identificacionPreso = TablaHistorialVisitas.getValueAt(fila, 7).toString();

                Visita visita = new VisitaDAO().buscarVisitaPorId(idVisita);

                if (visita != null) {
                    ModificarVisita dialog = new ModificarVisita(
                            this,
                            true,
                            visita,
                            TablaHistorialVisitas,
                            identificacionPreso
                    );
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);

                    controller.cargarHistorialVisitas(identificacionPreso, TablaHistorialVisitas);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Visita no encontrada",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID de visita inválido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cambiarEstado.addActionListener(e -> {
            int fila = TablaHistorialVisitas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una visita",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int idVisita = Integer.parseInt(TablaHistorialVisitas.getValueAt(fila, 0).toString());
                String identificacionPreso = TablaHistorialVisitas.getValueAt(fila, 7).toString();

                Visita visita = new VisitaDAO().buscarVisitaPorId(idVisita);

                if (visita != null) {
                    CambioEstadoVisita dialog = new CambioEstadoVisita(
                            this,
                            true,
                            visita,
                            identificacionPreso,
                            TablaHistorialVisitas
                    );

                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);

                    controller.cargarHistorialVisitas(identificacionPreso, TablaHistorialVisitas);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Visita no encontrada",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID de visita inválido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppMenuTablaPresos = new javax.swing.JPopupMenu();
        ppMenuTablaVisitas = new javax.swing.JPopupMenu();
        ppMenuTablaVisitantes = new javax.swing.JPopupMenu();
        PanelBotones = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        TabbedPDC = new javax.swing.JTabbedPane();
        PanelPerfil = new javax.swing.JPanel();
        FondoFoto = new javax.swing.JPanel();
        FotoPDC = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        FechaInicioContratoPDC = new javax.swing.JLabel();
        TurnoPDC = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        NombreCompletoPDC = new javax.swing.JLabel();
        IdentificacionPDC = new javax.swing.JLabel();
        EdadPDC = new javax.swing.JLabel();
        NacionalidadPDC = new javax.swing.JLabel();
        SexoPDC = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel46 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        EmailPDC = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        FechaFinContratoPDC = new javax.swing.JLabel();
        PanelListaPresos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaPresos = new javax.swing.JTable();
        BarraDeBusquedaPreso = new javax.swing.JTextField();
        BotonCargarTodos = new javax.swing.JButton();
        BotonBuscarPresoIdentificacion = new javax.swing.JButton();
        JcomboSeccion = new javax.swing.JComboBox<>();
        PanelGuardarVisita = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        BotonGuardarVisitante = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        SegundoNombreVisitante = new javax.swing.JTextField();
        PrimerApellidoVisitante = new javax.swing.JTextField();
        SegundoApellidoVisitante = new javax.swing.JTextField();
        IdentificacionVisitante = new javax.swing.JTextField();
        EmailVisitante = new javax.swing.JTextField();
        RelacionConPresoVisitante = new javax.swing.JComboBox<>();
        SexoVisitante = new javax.swing.JComboBox<>();
        EdadVisitante = new javax.swing.JTextField();
        PrimerNombreVisitante = new javax.swing.JTextField();
        NacionalidadVisitante = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        CantidadDeVisitantesCombo = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        IdentificacionPresoVisita = new javax.swing.JTextField();
        AgregarImagenVisitante = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        TipoVisita = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        LugarVisita = new javax.swing.JComboBox<>();
        BotonGuardarVisita = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        VistaPreviaVisitante = new javax.swing.JLabel();
        FechaVisita = new com.toedter.calendar.JDateChooser();
        HoraVisita = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        GuardarVisitaFinal = new javax.swing.JButton();
        botonCancelarProceso = new javax.swing.JButton();
        PanelActualizarInformacion = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
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
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        VistaPreviaNuevaFoto = new javax.swing.JLabel();
        SubirNuevaFotoPerfil = new javax.swing.JButton();
        PanelHistorialVisitas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaHistorialVisitas = new javax.swing.JTable();
        jLabel48 = new javax.swing.JLabel();
        PanelHistorialVisitantes = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaHistorialVisitantes = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelBotones.setBackground(new java.awt.Color(29, 35, 51));
        PanelBotones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(29, 35, 51));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel3MouseExited(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PERFIL");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        PanelBotones.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        jPanel4.setBackground(new java.awt.Color(29, 35, 51));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
        });
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LISTA DE PRESOS");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        PanelBotones.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 280, 60));

        jPanel5.setBackground(new java.awt.Color(29, 35, 51));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel5MouseExited(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("GUARDAR VISITA");
        jPanel5.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        PanelBotones.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 260, 60));

        jPanel15.setBackground(new java.awt.Color(29, 35, 51));
        jPanel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel15MouseExited(evt);
            }
        });
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("ACTUALIZAR INFORMACIÓN");
        jPanel15.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        PanelBotones.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 300, 60));

        getContentPane().add(PanelBotones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        PanelPerfil.setBackground(new java.awt.Color(255, 255, 255));
        PanelPerfil.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FondoFoto.setBackground(new java.awt.Color(204, 204, 204));
        FondoFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        FondoFoto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        FondoFoto.add(FotoPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 200));

        PanelPerfil.add(FondoFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 200, 220));

        jPanel6.setBackground(new java.awt.Color(180, 180, 195));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 2, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Personal de control");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 160, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("fecha inicio contrato");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, -1, -1));

        jButton1.setText("Cerrar sesión");
        jPanel6.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 110, 30));

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
        jPanel6.add(FechaInicioContratoPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 220, 30));
        jPanel6.add(TurnoPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 220, 30));

        PanelPerfil.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 300, 530));

        jPanel2.setBackground(new java.awt.Color(139, 139, 157));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("INFORMACIÓN PERSONAL");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, -1, -1));

        PanelPerfil.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Nombre completo:");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Identificacion:");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Edad:");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        jLabel44.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 0, 0));
        jLabel44.setText("Nacionalidad:");
        jPanel7.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));
        jPanel7.add(NombreCompletoPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 390, 30));
        jPanel7.add(IdentificacionPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 420, 30));
        jPanel7.add(EdadPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 480, 30));
        jPanel7.add(NacionalidadPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 430, 30));
        jPanel7.add(SexoPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 480, 30));
        jPanel7.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 540, 10));
        jPanel7.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 540, 10));
        jPanel7.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 540, 10));
        jPanel7.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 540, 10));
        jPanel7.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 540, 10));

        jLabel46.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 0, 0));
        jLabel46.setText("Correo electronico:");
        jPanel7.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));
        jPanel7.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 540, 10));
        jPanel7.add(EmailPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 270, 380, 30));

        jLabel49.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("Genero:");
        jPanel7.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        jLabel45.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("fecha fin de contrato");
        jPanel7.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, -1));
        jPanel7.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 540, 10));
        jPanel7.add(FechaFinContratoPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, 370, 30));

        PanelPerfil.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 630, 490));

        TabbedPDC.addTab("PERFIL", PanelPerfil);

        PanelListaPresos.setBackground(new java.awt.Color(255, 255, 255));
        PanelListaPresos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaPresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombres", "Apellidos", "Genero", "Edad", "Identificacion", "Nacionalidad", "Celda", "Seccion", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaPresos);
        if (TablaPresos.getColumnModel().getColumnCount() > 0) {
            TablaPresos.getColumnModel().getColumn(0).setResizable(false);
            TablaPresos.getColumnModel().getColumn(1).setResizable(false);
            TablaPresos.getColumnModel().getColumn(2).setResizable(false);
            TablaPresos.getColumnModel().getColumn(3).setResizable(false);
            TablaPresos.getColumnModel().getColumn(4).setResizable(false);
            TablaPresos.getColumnModel().getColumn(5).setResizable(false);
            TablaPresos.getColumnModel().getColumn(6).setResizable(false);
            TablaPresos.getColumnModel().getColumn(7).setResizable(false);
            TablaPresos.getColumnModel().getColumn(8).setResizable(false);
            TablaPresos.getColumnModel().getColumn(9).setResizable(false);
            TablaPresos.getColumnModel().getColumn(10).setResizable(false);
        }

        PanelListaPresos.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 1080, 440));
        PanelListaPresos.add(BarraDeBusquedaPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 640, 30));

        BotonCargarTodos.setText("Cargar Todos Los presos");
        BotonCargarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCargarTodosActionPerformed(evt);
            }
        });
        PanelListaPresos.add(BotonCargarTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, 170, -1));

        BotonBuscarPresoIdentificacion.setText("Buscar preso por identificacion");
        BotonBuscarPresoIdentificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonBuscarPresoIdentificacionActionPerformed(evt);
            }
        });
        PanelListaPresos.add(BotonBuscarPresoIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, 200, 30));

        JcomboSeccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar sección >", "Sección A", "Sección B", "Sección C" }));
        JcomboSeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JcomboSeccionActionPerformed(evt);
            }
        });
        PanelListaPresos.add(JcomboSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 170, 30));

        TabbedPDC.addTab("PRESOS", PanelListaPresos);

        PanelGuardarVisita.setBackground(new java.awt.Color(255, 255, 255));
        PanelGuardarVisita.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(180, 180, 195));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Genero:");
        jPanel9.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Información del visitante");
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Segundo nombre:");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel20.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Primer apellido:");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("Segundo apellido:");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Edad:");
        jPanel9.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Nacionalidad:");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, -1, -1));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Relación con el preso:");
        jPanel9.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, -1, -1));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("Correo electronico:");
        jPanel9.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, -1, -1));

        BotonGuardarVisitante.setText("Guardar datos del visitante");
        BotonGuardarVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonGuardarVisitanteActionPerformed(evt);
            }
        });
        jPanel9.add(BotonGuardarVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 470, 180, 30));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("Identificación:");
        jPanel9.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Primer nombre:");
        jPanel9.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));
        jPanel9.add(SegundoNombreVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 310, 30));
        jPanel9.add(PrimerApellidoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 310, 30));
        jPanel9.add(SegundoApellidoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 310, 30));
        jPanel9.add(IdentificacionVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 310, 30));
        jPanel9.add(EmailVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 300, 30));

        RelacionConPresoVisitante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Esposa/Esposo", "Padre/Madre", "Hijo/Hija", "Hermano/Hermana", "Abuelo/Abuela", "Nieto/Nieta", "Tío/Tía", "Sobrino/Sobrina", "Primo/Prima", "Suegro/Suegra", "Yerno/Nuera", "Cuñado/Cuñada", "Amigo/Amiga", "Compañero de Trabajo", "Vecino/Vecina", "Conocido/Conocida", "Abogado/Abogada", "Asistente Social", "Representante Legal", "Sacerdote/Pastor", "Novio/Novia", "Tutor Legal", "Ex-Esposo/Ex-Esposa", "Familiar Político " }));
        jPanel9.add(RelacionConPresoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 420, 280, 30));

        SexoVisitante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Femenino", "Masculino" }));
        jPanel9.add(SexoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 300, 30));
        jPanel9.add(EdadVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 300, 300, 30));
        jPanel9.add(PrimerNombreVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 310, 30));

        NacionalidadVisitante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Afganistán  ", "Albania  ", "Alemania  ", "Andorra  ", "Angola  ", "Antigua y Barbuda  ", "Arabia Saudita  ", "Argelia  ", "Argentina  ", "Armenia  ", "Australia  ", "Austria  ", "Azerbaiyán  ", "Bahamas  ", "Bahréin  ", "Bangladés  ", "Barbados  ", "Bélgica  ", "Belice  ", "Benín  ", "Bielorrusia  ", "Birmania  ", "Bolivia  ", "Bosnia y Herzegovina  ", "Botsuana  ", "Brasil  ", "Brunéi  ", "Bulgaria  ", "Burkina Faso  ", "Burundi  ", "Bután  ", "Cabo Verde  ", "Camboya  ", "Camerún  ", "Canadá  ", "Chad  ", "Chile  ", "China  ", "Chipre  ", "Colombia  ", "Comoras  ", "Congo  ", "Corea del Norte  ", "Corea del Sur  ", "Costa de Marfil  ", "Costa Rica  ", "Croacia  ", "Cuba  ", "Dinamarca  ", "Dominica  ", "Ecuador  ", "Egipto  ", "El Salvador  ", "Emiratos Árabes Unidos  ", "Eritrea  ", "Eslovaquia  ", "Eslovenia  ", "España  ", "Estados Unidos  ", "Estonia  ", "Etiopía  ", "Filipinas  ", "Finlandia  ", "Fiyi  ", "Francia  ", "Gabón  ", "Gambia  ", "Georgia  ", "Ghana  ", "Granada  ", "Grecia  ", "Guatemala  ", "Guinea  ", "Guinea-Bisáu  ", "Guinea Ecuatorial  ", "Guyana  ", "Haití  ", "Honduras  ", "Hungría  ", "India  ", "Indonesia  ", "Irak  ", "Irán  ", "Irlanda  ", "Islandia  ", "Israel  ", "Italia  ", "Jamaica  ", "Japón  ", "Jordania  ", "Kazajistán  ", "Kenia  ", "Kirguistán  ", "Kiribati  ", "Kuwait  ", "Laos  ", "Lesoto  ", "Letonia  ", "Líbano  ", "Liberia  ", "Libia  ", "Liechtenstein  ", "Lituania  ", "Luxemburgo  ", "Madagascar  ", "Malasia  ", "Malaui  ", "Maldivas  ", "Malí  ", "Malta  ", "Marruecos  ", "Islas Marshall  ", "Mauricio  ", "Mauritania  ", "México  ", "Micronesia  ", "Moldavia  ", "Mónaco  ", "Mongolia  ", "Montenegro  ", "Mozambique  ", "Namibia  ", "Nauru  ", "Nepal  ", "Nicaragua  ", "Níger  ", "Nigeria  ", "Noruega  ", "Nueva Zelanda  ", "Omán  ", "Países Bajos  ", "Pakistán  ", "Palaos  ", "Panamá  ", "Papúa Nueva Guinea  ", "Paraguay  ", "Perú  ", "Polonia  ", "Portugal  ", "Qatar  ", "Reino Unido  ", "República Centroafricana  ", "República Checa  ", "República Dominicana  ", "Ruanda  ", "Rumanía  ", "Rusia  ", "Samoa  ", "San Cristóbal y Nieves  ", "San Marino  ", "San Vicente y las Granadinas  ", "Santa Lucía  ", "Santo Tomé y Príncipe  ", "Senegal  ", "Serbia  ", "Seychelles  ", "Sierra Leona  ", "Singapur  ", "Siria  ", "Somalia  ", "Sri Lanka  ", "Sudáfrica  ", "Sudán  ", "Sudán del Sur  ", "Suecia  ", "Suiza  ", "Surinam  ", "Tailandia  ", "Tanzania  ", "Tayikistán  ", "Timor Oriental  ", "Togo  ", "Tonga  ", "Trinidad y Tobago  ", "Túnez  ", "Turkmenistán  ", "Turquía  ", "Tuvalu  ", "Ucrania  ", "Uganda  ", "Uruguay  ", "Uzbekistán  ", "Vanuatu  ", "Vaticano  ", "Venezuela  ", "Vietnam  ", "Yemen  ", "Yibuti  ", "Zambia  ", "Zimbabue" }));
        NacionalidadVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NacionalidadVisitanteActionPerformed(evt);
            }
        });
        jPanel9.add(NacionalidadVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 382, 300, 30));

        PanelGuardarVisita.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 520, 520));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Cantidad de visitantes:");
        PanelGuardarVisita.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        CantidadDeVisitantesCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "1", "2" }));
        PanelGuardarVisita.add(CantidadDeVisitantesCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 120, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Información de la visita");
        PanelGuardarVisita.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, -1, -1));

        jPanel10.setBackground(new java.awt.Color(180, 180, 195));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("Identificación del preso:");
        jPanel10.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));
        jPanel10.add(IdentificacionPresoVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 180, 30));

        PanelGuardarVisita.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, 400, 70));

        AgregarImagenVisitante.setText("Añadir foto del visitante");
        AgregarImagenVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarImagenVisitanteActionPerformed(evt);
            }
        });
        PanelGuardarVisita.add(AgregarImagenVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 170, -1));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Guardar Visita final:");
        PanelGuardarVisita.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 420, 140, -1));

        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("Fecha de la visita:");
        PanelGuardarVisita.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 160, -1, -1));

        jLabel30.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("Hora de la visita:");
        PanelGuardarVisita.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 210, -1, -1));

        TipoVisita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Visita regular", "Familiar", "Intimas o Conyugales", "Legal", "Religiosas" }));
        PanelGuardarVisita.add(TipoVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 250, 270, 30));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Tipo de visita:");
        PanelGuardarVisita.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 260, 100, -1));

        LugarVisita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Sala de visitas", "Cabinas de visitas conyugales", "Salas de visitas legales" }));
        PanelGuardarVisita.add(LugarVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 300, 270, 30));

        BotonGuardarVisita.setText("Guardar datos de la visita");
        BotonGuardarVisita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonGuardarVisitaActionPerformed(evt);
            }
        });
        PanelGuardarVisita.add(BotonGuardarVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 340, 180, 30));

        jLabel22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Vista previa foto visitante:");
        PanelGuardarVisita.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 370, -1, -1));

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(VistaPreviaVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, 140, 160));

        PanelGuardarVisita.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 390, 160, 180));
        PanelGuardarVisita.add(FechaVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 152, 270, 30));

        HoraVisita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00" }));
        PanelGuardarVisita.add(HoraVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 200, 270, 30));

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 0));
        jLabel51.setText("Lugar de visita:");
        PanelGuardarVisita.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 310, 110, -1));

        GuardarVisitaFinal.setText("Guardar visita final");
        GuardarVisitaFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarVisitaFinalActionPerformed(evt);
            }
        });
        PanelGuardarVisita.add(GuardarVisitaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 410, 130, 30));

        botonCancelarProceso.setBackground(new java.awt.Color(29, 35, 51));
        botonCancelarProceso.setForeground(new java.awt.Color(255, 255, 255));
        botonCancelarProceso.setText("CANCELAR PROCESO");
        botonCancelarProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarProcesoActionPerformed(evt);
            }
        });
        PanelGuardarVisita.add(botonCancelarProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 530, 180, 40));

        TabbedPDC.addTab("GUARDAR VISITA", PanelGuardarVisita);

        PanelActualizarInformacion.setBackground(new java.awt.Color(255, 255, 255));
        PanelActualizarInformacion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(180, 180, 195));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("Segundo nombre:");
        jPanel13.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, -1, -1));

        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 0));
        jLabel37.setText("Primer apellido:");
        jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, -1, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("Segundo apellido:");
        jPanel13.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, -1, -1));

        jLabel39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setText("Primer nombre:");
        jPanel13.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jLabel40.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 0));
        jLabel40.setText("Contraseña actual*:");
        jPanel13.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setText("Nueva contraseña:");
        jPanel13.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, -1, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("Correo electronico:");
        jPanel13.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, -1, -1));

        jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 0));
        jLabel43.setText("Nacionalidad:");
        jPanel13.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));
        jPanel13.add(NuevoEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 320, 170, 30));
        jPanel13.add(ContraseñaActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 170, 30));
        jPanel13.add(NuevoPrimerNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 170, 30));
        jPanel13.add(NuevoPrimerApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 170, 30));
        jPanel13.add(NuevaNacionalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 170, 30));
        jPanel13.add(NuevaContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 170, 30));
        jPanel13.add(NuevoSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 180, 170, 30));
        jPanel13.add(NuevoSegundoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 250, 170, 30));

        BotonActualizarInformacion.setText("Actualizar informacion");
        BotonActualizarInformacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonActualizarInformacionActionPerformed(evt);
            }
        });
        jPanel13.add(BotonActualizarInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 400, 170, 30));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("ACTUALIZAR INFORMACIÓN PERSONAL");
        jPanel13.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, -1));

        PanelActualizarInformacion.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 750, 500));

        jPanel12.setBackground(new java.awt.Color(139, 139, 157));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelActualizarInformacion.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 100));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Vista previa nueva foto");
        PanelActualizarInformacion.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 140, -1, -1));

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel14.add(VistaPreviaNuevaFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, 250));

        PanelActualizarInformacion.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 180, 230, 270));

        SubirNuevaFotoPerfil.setText("Subir foto de perfil");
        SubirNuevaFotoPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubirNuevaFotoPerfilActionPerformed(evt);
            }
        });
        PanelActualizarInformacion.add(SubirNuevaFotoPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 470, 150, 30));

        TabbedPDC.addTab("ACTUALIZAR INFORMACION", PanelActualizarInformacion);

        PanelHistorialVisitas.setBackground(new java.awt.Color(255, 255, 255));
        PanelHistorialVisitas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaHistorialVisitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Identificación visitante", "Fecha", "Hora", "Duración", "Tipo de visita", "Lugar", "visitado", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaHistorialVisitas.setRowHeight(40);
        jScrollPane2.setViewportView(TablaHistorialVisitas);
        if (TablaHistorialVisitas.getColumnModel().getColumnCount() > 0) {
            TablaHistorialVisitas.getColumnModel().getColumn(1).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(2).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(3).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(4).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(5).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(6).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(7).setResizable(false);
            TablaHistorialVisitas.getColumnModel().getColumn(8).setResizable(false);
        }

        PanelHistorialVisitas.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 1040, 490));

        jLabel48.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 0));
        jLabel48.setText("HISTORIAL DE VISITAS");
        PanelHistorialVisitas.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, -1, -1));

        TabbedPDC.addTab("HISTORIAL DE VISITAS", PanelHistorialVisitas);

        PanelHistorialVisitantes.setBackground(new java.awt.Color(255, 255, 255));
        PanelHistorialVisitantes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaHistorialVisitantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombres", "Apellidos", "Email", "Edad", "Identificación", "Genero", "Nacionalidad", "Relación", "Cantidad", "Visitado", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TablaHistorialVisitantes);
        if (TablaHistorialVisitantes.getColumnModel().getColumnCount() > 0) {
            TablaHistorialVisitantes.getColumnModel().getColumn(0).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(1).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(2).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(3).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(4).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(5).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(6).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(7).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(8).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(9).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(10).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(11).setResizable(false);
            TablaHistorialVisitantes.getColumnModel().getColumn(12).setResizable(false);
        }

        PanelHistorialVisitantes.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1080, 490));

        jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 0));
        jLabel34.setText("HISTORIAL DE VISITANTES");
        PanelHistorialVisitantes.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, -1, -1));

        TabbedPDC.addTab("HISTORIAL VISITANTES", PanelHistorialVisitantes);

        getContentPane().add(TabbedPDC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1100, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonGuardarVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonGuardarVisitanteActionPerformed
        controller.registrarVisitante(this, visitantesTemporales, imagenesTemporales);
    }//GEN-LAST:event_BotonGuardarVisitanteActionPerformed

    private void AgregarImagenVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarImagenVisitanteActionPerformed
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del visitante?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File nuevaImagen = null;

            if (opcion == 0) {
                nuevaImagen = controller.capturarImagenVisitante();
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();

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

            if (nuevaImagen != null && nuevaImagen.exists()) {
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                VistaPreviaVisitante.getWidth(),
                                VistaPreviaVisitante.getHeight(),
                                Image.SCALE_SMOOTH
                        );

                VistaPreviaVisitante.setIcon(new ImageIcon(imagenEscalada));

                imagenVisitanteSeleccionada = nuevaImagen;

                VistaPreviaVisitante.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();


    }//GEN-LAST:event_AgregarImagenVisitanteActionPerformed
    }
    private void BotonGuardarVisitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonGuardarVisitaActionPerformed
        controller.guardarVisitaTemporal(this);
    }//GEN-LAST:event_BotonGuardarVisitaActionPerformed

    private void BotonActualizarInformacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonActualizarInformacionActionPerformed


    }//GEN-LAST:event_BotonActualizarInformacionActionPerformed

    private void SubirNuevaFotoPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubirNuevaFotoPerfilActionPerformed

    }//GEN-LAST:event_SubirNuevaFotoPerfilActionPerformed


    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        TabbedPDC.setSelectedIndex(0);
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        TabbedPDC.setSelectedIndex(1);
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        TabbedPDC.setSelectedIndex(2);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jPanel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseClicked
        TabbedPDC.setSelectedIndex(3);
    }//GEN-LAST:event_jPanel15MouseClicked

    private void BotonCargarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCargarTodosActionPerformed
        VisitaController controller = new VisitaController();
        controller.cargarTodosLosPresos(TablaPresos);
    }//GEN-LAST:event_BotonCargarTodosActionPerformed

    private void BotonBuscarPresoIdentificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonBuscarPresoIdentificacionActionPerformed
        VisitaController controller = new VisitaController();
        controller.buscarPresoPorIdentificacion(BarraDeBusquedaPreso.getText().trim(), TablaPresos);
    }//GEN-LAST:event_BotonBuscarPresoIdentificacionActionPerformed

    private void NacionalidadVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NacionalidadVisitanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NacionalidadVisitanteActionPerformed

    private void JcomboSeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JcomboSeccionActionPerformed
        String seccionSeleccionada = JcomboSeccion.getSelectedItem().toString();
        VisitaController visitaController = new VisitaController();
        visitaController.cargarDatosPresoEnTablaPorSeccion(seccionSeleccionada, TablaPresos);
    }//GEN-LAST:event_JcomboSeccionActionPerformed

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        jPanel3.setBackground(new Color(43, 54, 84));     }//GEN-LAST:event_jPanel3MouseEntered

    private void jPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseExited
        jPanel3.setBackground(new Color(29, 35, 51));    }//GEN-LAST:event_jPanel3MouseExited

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        jPanel4.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        jPanel4.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        jPanel5.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseExited
        jPanel5.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_jPanel5MouseExited

    private void jPanel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseEntered
        jPanel15.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_jPanel15MouseEntered

    private void jPanel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseExited
        jPanel15.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_jPanel15MouseExited

    private void GuardarVisitaFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarVisitaFinalActionPerformed
        controller.guardarVisitaFinal(this, visitantesTemporales, imagenesTemporales);
    }//GEN-LAST:event_GuardarVisitaFinalActionPerformed

    private void botonCancelarProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarProcesoActionPerformed
        if (!controller.existeVisitaTemporal()) {
            JOptionPane.showMessageDialog(this,
                    "No hay ninguna visita en proceso para cancelar",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cancelar la visita en proceso?\nSe perderán todos los datos no guardados.",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            controller.cancelarProcesoVisita(this, visitantesTemporales, imagenesTemporales);
        }    }//GEN-LAST:event_botonCancelarProcesoActionPerformed

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
            java.util.logging.Logger.getLogger(PersonalDeControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PersonalDeControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PersonalDeControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PersonalDeControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PersonalDeControl().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AgregarImagenVisitante;
    private javax.swing.JTextField BarraDeBusquedaPreso;
    private javax.swing.JButton BotonActualizarInformacion;
    private javax.swing.JButton BotonBuscarPresoIdentificacion;
    private javax.swing.JButton BotonCargarTodos;
    private javax.swing.JButton BotonGuardarVisita;
    private javax.swing.JButton BotonGuardarVisitante;
    private javax.swing.JComboBox<String> CantidadDeVisitantesCombo;
    private javax.swing.JTextField ContraseñaActual;
    private javax.swing.JLabel EdadPDC;
    private javax.swing.JTextField EdadVisitante;
    private javax.swing.JLabel EmailPDC;
    private javax.swing.JTextField EmailVisitante;
    private javax.swing.JLabel FechaFinContratoPDC;
    private javax.swing.JLabel FechaInicioContratoPDC;
    private com.toedter.calendar.JDateChooser FechaVisita;
    private javax.swing.JPanel FondoFoto;
    private javax.swing.JLabel FotoPDC;
    private javax.swing.JButton GuardarVisitaFinal;
    private javax.swing.JComboBox<String> HoraVisita;
    private javax.swing.JLabel IdentificacionPDC;
    private javax.swing.JTextField IdentificacionPresoVisita;
    private javax.swing.JTextField IdentificacionVisitante;
    private javax.swing.JComboBox<String> JcomboSeccion;
    private javax.swing.JComboBox<String> LugarVisita;
    private javax.swing.JLabel NacionalidadPDC;
    private javax.swing.JComboBox<String> NacionalidadVisitante;
    private javax.swing.JLabel NombreCompletoPDC;
    private javax.swing.JTextField NuevaContraseña;
    private javax.swing.JTextField NuevaNacionalidad;
    private javax.swing.JTextField NuevoEmail;
    private javax.swing.JTextField NuevoPrimerApellido;
    private javax.swing.JTextField NuevoPrimerNombre;
    private javax.swing.JTextField NuevoSegundoApellido;
    private javax.swing.JTextField NuevoSegundoNombre;
    private javax.swing.JPanel PanelActualizarInformacion;
    private javax.swing.JPanel PanelBotones;
    private javax.swing.JPanel PanelGuardarVisita;
    private javax.swing.JPanel PanelHistorialVisitantes;
    private javax.swing.JPanel PanelHistorialVisitas;
    private javax.swing.JPanel PanelListaPresos;
    private javax.swing.JPanel PanelPerfil;
    private javax.swing.JTextField PrimerApellidoVisitante;
    private javax.swing.JTextField PrimerNombreVisitante;
    private javax.swing.JComboBox<String> RelacionConPresoVisitante;
    private javax.swing.JTextField SegundoApellidoVisitante;
    private javax.swing.JTextField SegundoNombreVisitante;
    private javax.swing.JLabel SexoPDC;
    private javax.swing.JComboBox<String> SexoVisitante;
    private javax.swing.JButton SubirNuevaFotoPerfil;
    private javax.swing.JTabbedPane TabbedPDC;
    private javax.swing.JTable TablaHistorialVisitantes;
    private javax.swing.JTable TablaHistorialVisitas;
    private javax.swing.JTable TablaPresos;
    private javax.swing.JComboBox<String> TipoVisita;
    private javax.swing.JLabel TurnoPDC;
    private javax.swing.JLabel VistaPreviaNuevaFoto;
    private javax.swing.JLabel VistaPreviaVisitante;
    private javax.swing.JButton botonCancelarProceso;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPopupMenu ppMenuTablaPresos;
    private javax.swing.JPopupMenu ppMenuTablaVisitantes;
    private javax.swing.JPopupMenu ppMenuTablaVisitas;
    // End of variables declaration//GEN-END:variables

    public JTextField getPrimerNombreVisitante() {
        return PrimerNombreVisitante;
    }

    public JTextField getSegundoNombreVisitante() {
        return SegundoNombreVisitante;
    }

    public JTextField getPrimerApellidoVisitante() {
        return PrimerApellidoVisitante;
    }

    public JTextField getSegundoApellidoVisitante() {
        return SegundoApellidoVisitante;
    }

    public JTextField getIdentificacionVisitante() {
        return IdentificacionVisitante;
    }

    public JTextField getEdadVisitante() {
        return EdadVisitante;
    }

    public JTextField getEmailVisitante() {
        return EmailVisitante;
    }

    public JComboBox<String> getSexoVisitante() {
        return SexoVisitante;
    }

    public JComboBox<String> getNacionalidadVisitante() {
        return NacionalidadVisitante;
    }

    public JComboBox<String> getRelacionConPresoVisitante() {
        return RelacionConPresoVisitante;
    }

    public JComboBox<String> getCantidadDeVisitantesCombo() {
        return CantidadDeVisitantesCombo;
    }

    public JTextField getIdentificacionPresoVisita() {
        return IdentificacionPresoVisita;
    }

    public JDateChooser getFechaVisita() {
        return FechaVisita;
    }

    public JComboBox<String> getHoraVisita() {
        return HoraVisita;
    }

    public JComboBox<String> getTipoVisita() {
        return TipoVisita;
    }

    public JComboBox<String> getLugarVisita() {
        return LugarVisita;
    }

    public File getImagenVisitanteSeleccionada() {
        return imagenVisitanteSeleccionada;
    }

    public void setImagenVisitanteSeleccionada(File file) {
        this.imagenVisitanteSeleccionada = file;
    }

    public JLabel getVistaPreviaVisitante() {
        return VistaPreviaVisitante;
    }

    public JLabel getNombreCompletoPDC() {
        return NombreCompletoPDC;
    }

    public JLabel getIdentificacionPDC() {
        return IdentificacionPDC;
    }

    public JLabel getEdadPDC() {
        return EdadPDC;
    }

    public JLabel getNacionalidadPDC() {
        return NacionalidadPDC;
    }

    public JLabel getEmailPDC() {
        return EmailPDC;
    }

    public JLabel getSexoPDC() {
        return SexoPDC;
    }

    public JLabel getFechaInicioContratoPDC() {
        return FechaInicioContratoPDC;
    }

    public JLabel getFechaFinContratoPDC() {
        return FechaFinContratoPDC;
    }

    public JLabel getTurnoPDC() {
        return TurnoPDC;
    }

    public JLabel getFotoPDC() {
        return FotoPDC;
    }

    public void setImagenVisitanteNuevaSeleccionada(File imagen) {
        this.imagenVisitanteNuevaSeleccionada = imagen;
    }

    public File getImagenVisitanteNuevaSeleccionada() {
        return imagenVisitanteNuevaSeleccionada;
    }

    public JTabbedPane getTabbedPDC() {
        return TabbedPDC;
    }

}
