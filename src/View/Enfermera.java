/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.CitaMedicaController;
import DAO.CitaMedicaDAO;
import DAO.PresaDAO;
import Model.Constants.EstadoCitaMedicaEnum;
import Model.Entities.CitaMedica;
import Model.Entities.Guardia;
import Model.Entities.Presa;
import Model.Entities.Usuario;
import Utilidades.GeneradorHistoriaClinica;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gameV
 */
public class Enfermera extends javax.swing.JFrame implements PerfilUsuario {

    /**
     * Creates new form Enfermera
     */
    private final PresaDAO presoDAO;
    private Usuario usuario;
    private CitaMedica citaSeleccionada;
    private File archivoHistoriaClinica;
    private final CitaMedicaController citaController;
    private LocalDateTime fechaHoraAtencion;
    private String rutaHistoriaClinica;
    private EstadoCitaMedicaEnum estado;

    public Enfermera() {
        initComponents();
        setLocationRelativeTo(null);
        this.citaController = new CitaMedicaController(); 
        this.presoDAO = PresaDAO.getInstancia();
        configurarTablas();
    }

    private void configurarTablas() {
       
        tblCitasPendientes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                if (column == 0) { 
                    try {
                        
                        if (table.getModel().getRowCount() > row) {
                            Object idValue = table.getModel().getValueAt(row, 3); 
                            if (idValue != null) {
                                String idPreso = idValue.toString();
                                Presa preso = presoDAO.buscarPresoPorIdentificacion(idPreso);
                                if (preso != null && preso.getFotoPath() != null) {
                                    try {
                                        ImageIcon icon = new ImageIcon(preso.getFotoPath());
                                        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                                        return new JLabel(new ImageIcon(img));
                                    } catch (Exception e) {
                                        return new JLabel("Error foto");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new JLabel("No foto");
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        
        tblCitasPendientes.setRowHeight(60);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        mostrarDatosUsuario();
        cargarCitasPendientes();
        cargarHistorialAtendidos();
    }

    private void mostrarDatosUsuario() {
        if (usuario != null) {
            lblNombre.setText(usuario.getPrimerNombre() + " " + usuario.getPrimerApellido());
            lblRol.setText(usuario.getRol().toString());
            cargarImagenUsuario(usuario.getRutaImagen());
        }
    }

    private void cargarCitasPendientes() {

        if (usuario != null) {
            citaController.cargarCitasPendientes(tblCitasPendientes, usuario.getIdentificacion());
        }
    }

    private void cargarHistorialAtendidos() {
        if (usuario != null) {
            DefaultTableModel modelo = (DefaultTableModel) tblHistorial.getModel();
            modelo.setRowCount(0);

            List<CitaMedica> citas = CitaMedicaDAO.getInstancia()
                    .obtenerPorEnfermera(usuario.getIdentificacion())
                    .stream()
                    .filter(c -> c.getEstado() == EstadoCitaMedicaEnum.ATENDIDO)
                    .collect(Collectors.toList());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            
            tblHistorial.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {

                    if (column == 0) {
                        try {
                            String idPreso = table.getModel().getValueAt(row, 3).toString();
                            Presa preso = presoDAO.buscarPresoPorIdentificacion(idPreso);

                            if (preso != null && preso.getFotoPath() != null) {
                                ImageIcon icon = new ImageIcon(preso.getFotoPath());
                                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                                return new JLabel(new ImageIcon(img));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new JLabel("No foto");
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            });

            for (CitaMedica cita : citas) {
                modelo.addRow(new Object[]{
                    "", 
                    cita.getId(),
                    cita.getPreso().getNombreCompleto(),
                    cita.getPreso().getIdentificacion(),
                    cita.getFechaHoraAtencion() != null
                    ? cita.getFechaHoraAtencion().format(formatter) : "No registrada",
                    cita.getMotivo(),
                    cita.getDiagnostico(),
                    "ATENDIDO",
                    cita.getRutaHistoriaClinica() != null
                    ? new File(cita.getRutaHistoriaClinica()).getName() : "Sin archivo"
                });
            }

            
            tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblHistorial.setRowHeight(60);
        }
    }

    private void cargarImagenUsuario(String rutaImagen) {
        try {
            ImageIcon icon = new ImageIcon(rutaImagen);
            Image img = icon.getImage().getScaledInstance(fotolbl.getWidth(), fotolbl.getHeight(), Image.SCALE_SMOOTH);
            fotolbl.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            cargarImagenPorDefecto();
        }
    }

    private void cargarImagenPorDefecto() {

        try {
            ImageIcon icon = new ImageIcon("src/Resources/Images/default_user.png");
            Image img = icon.getImage().getScaledInstance(
                    fotolbl.getWidth(), fotolbl.getHeight(), Image.SCALE_SMOOTH);
            fotolbl.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            fotolbl.setText("No image");
        }
    }

    public void cargarCitasPendientes(JTable tabla, String identificacionEnfermera) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        modelo.setColumnIdentifiers(new String[]{"Foto", "ID", "Nombre Preso", "Identificación", "Fecha", "Hora", "Motivo", "Estado"});

        List<CitaMedica> citas = CitaMedicaDAO.getInstancia()
                .obtenerPorEnfermera(identificacionEnfermera)
                .stream() 
                .filter(c -> c.getEstado() == EstadoCitaMedicaEnum.PENDIENTE)
                .collect(Collectors.toList());

        for (CitaMedica cita : citas) {
            modelo.addRow(new Object[]{
                "", // Espacio para la foto
                cita.getId(),
                cita.getPreso().getNombreCompleto(),
                cita.getPreso().getIdentificacion(),
                cita.getFecha(),
                cita.getHora(),
                cita.getMotivo(),
                cita.getEstado()
            });
        }

       
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.setRowHeight(60);
    }

    public void cargarHistorialAtendidos(JTable tabla, String identificacionEnfermera) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        modelo.setColumnIdentifiers(new String[]{"Foto", "ID", "Nombre Preso", "Identificación", "Fecha Atención", "Motivo", "Diagnóstico", "Archivo"});

       
        List<CitaMedica> citas = CitaMedicaDAO.getInstancia()
                .obtenerPorEnfermera(identificacionEnfermera)
                .stream() 
                .filter(c -> c.getEstado() == EstadoCitaMedicaEnum.ATENDIDO)
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (CitaMedica cita : citas) {
            modelo.addRow(new Object[]{
                "", 
                cita.getId(),
                cita.getPreso().getNombreCompleto(),
                cita.getPreso().getIdentificacion(),
                cita.getFechaHoraAtencion() != null
                ? cita.getFechaHoraAtencion().format(formatter) : "No registrada",
                cita.getMotivo(),
                cita.getDiagnostico(),
                cita.getRutaHistoriaClinica() != null
                ? new File(cita.getRutaHistoriaClinica()).getName() : "Sin archivo"
            });
        }

        
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.setRowHeight(60);
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
        atenderPresa = new javax.swing.JMenuItem();
        cancelarCita = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        fotolbl = new javax.swing.JLabel();
        lblBienvenida = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblNombre = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblRol = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        atender = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCitasPendientes = new javax.swing.JTable();
        atendidos = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();
        diagnostico = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblNombrePresa = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblApellidoPresa = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblEdadPresa = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblGrupoSanguineo = new javax.swing.JLabel();
        lblIdentificacionPresa = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtEstaturaPresa = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblGuardiaAcompanante = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaDiagnostico = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        lblFotoPreso = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        recetaTextPane = new javax.swing.JTextPane();
        txtPesoPresa = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        lblArchivoAdjunto = new javax.swing.JLabel();

        atenderPresa.setText("jMenuItem1");
        atenderPresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atenderPresaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(atenderPresa);

        cancelarCita.setText("jMenuItem1");
        cancelarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarCitaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(cancelarCita);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(fotolbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 220, 230));

        lblBienvenida.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblBienvenida.setForeground(new java.awt.Color(255, 255, 255));
        lblBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBienvenida.setText("Bienvenida:");
        jPanel2.add(lblBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 90, -1));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 200, 10));

        lblNombre.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 130, 20));
        jPanel2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 130, 10));
        jPanel2.add(lblRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 100, 20));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 670));

        jPanel5.setBackground(new java.awt.Color(0, 51, 102));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("VER RECLUSAS POR ATENDER");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 200, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 430, 70));

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
        });
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("VER RECLUSAS ATENDIDAS");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 200, -1));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 0, 380, 70));

        atender.setBackground(new java.awt.Color(255, 255, 255));
        atender.setForeground(new java.awt.Color(255, 255, 255));
        atender.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblCitasPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "ID", "Nombre", "Identificacion", "Fecha", "Hora", "Motivo", "Estado"
            }
        ));
        tblCitasPendientes.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tblCitasPendientes);

        atender.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 770, 530));

        jTabbedPane1.addTab("porAtender", atender);

        atendidos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "ID", "Nombre", "Identificacion", "Fecha", "Hora", "Motivo", "Estado"
            }
        ));
        tblHistorial.setComponentPopupMenu(jPopupMenu1);
        jScrollPane5.setViewportView(tblHistorial);

        atendidos.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 770, 580));

        jTabbedPane1.addTab("Atendidos", atendidos);

        diagnostico.setBackground(new java.awt.Color(255, 255, 255));
        diagnostico.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("GENERAR DIAGNOSTICO DE RECLUSA");
        diagnostico.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 360, -1));

        lblNombrePresa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblNombrePresa.setForeground(new java.awt.Color(0, 0, 0));
        lblNombrePresa.setText("Nombre");
        diagnostico.add(lblNombrePresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 110, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 102));
        jLabel2.setText("Nombre:");
        diagnostico.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, -1, -1));

        lblApellidoPresa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblApellidoPresa.setForeground(new java.awt.Color(0, 0, 0));
        lblApellidoPresa.setText("Apellido");
        diagnostico.add(lblApellidoPresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 100, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 102));
        jLabel3.setText("Apellido:");
        diagnostico.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 90, -1, -1));

        lblEdadPresa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEdadPresa.setForeground(new java.awt.Color(0, 0, 0));
        lblEdadPresa.setText("Edad");
        diagnostico.add(lblEdadPresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 50, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 102));
        jLabel4.setText("Edad:");
        diagnostico.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 102));
        jLabel5.setText("Grupo sanguineo:");
        diagnostico.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, -1, -1));

        lblGrupoSanguineo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblGrupoSanguineo.setForeground(new java.awt.Color(0, 0, 0));
        lblGrupoSanguineo.setText("sangre");
        diagnostico.add(lblGrupoSanguineo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 120, 70, -1));

        lblIdentificacionPresa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblIdentificacionPresa.setForeground(new java.awt.Color(0, 0, 0));
        lblIdentificacionPresa.setText("Cedula");
        diagnostico.add(lblIdentificacionPresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 100, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 102));
        jLabel6.setText("Diagnostico presuntivo:");
        diagnostico.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 420, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 102));
        jLabel7.setText("Estatura:");
        diagnostico.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 102));
        jLabel8.setText("Peso:");
        diagnostico.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, -1));

        txtEstaturaPresa.setText("Estatura");
        diagnostico.add(txtEstaturaPresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 110, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 102));
        jLabel9.setText("Guardia que acompaña:");
        diagnostico.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));

        lblGuardiaAcompanante.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblGuardiaAcompanante.setForeground(new java.awt.Color(0, 0, 0));
        lblGuardiaAcompanante.setText("Guardia");
        diagnostico.add(lblGuardiaAcompanante, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 160, 70, -1));

        txtAreaDiagnostico.setColumns(20);
        txtAreaDiagnostico.setRows(5);
        jScrollPane3.setViewportView(txtAreaDiagnostico);

        diagnostico.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 440, 660, 130));

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(lblFotoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 220));

        diagnostico.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 60, 200, 220));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 51, 102));
        jLabel10.setText("Identificación:");
        diagnostico.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, -1, -1));

        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Adjuntar historia clinica (en caso de que se haya realizado una consulta previamente)");
        diagnostico.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, -1, -1));

        jButton1.setText("Seleccionar el documento");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        diagnostico.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 102));
        jLabel12.setText("Receta:");
        diagnostico.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 140, 20));

        jScrollPane4.setViewportView(recetaTextPane);

        diagnostico.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 660, 70));

        txtPesoPresa.setText("Peso");
        diagnostico.add(txtPesoPresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 110, -1));

        jButton2.setText("Finalizar consulta");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        diagnostico.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 590, -1, -1));

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Documento seleccionado:");
        diagnostico.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 266, 150, 30));

        lblArchivoAdjunto.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        lblArchivoAdjunto.setForeground(new java.awt.Color(51, 51, 51));
        lblArchivoAdjunto.setText("this");
        diagnostico.add(lblArchivoAdjunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 270, 160, 20));

        jTabbedPane1.addTab("Diagnostico", diagnostico);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 810, 640));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void atenderPresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atenderPresaActionPerformed
       int filaSeleccionada = tblCitasPendientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idCita = (int) tblCitasPendientes.getValueAt(filaSeleccionada, 1);
            citaSeleccionada = citaController.obtenerCitaPorId(idCita);

            if (citaSeleccionada != null) {
                Presa preso = citaSeleccionada.getPreso();

                
                lblNombrePresa.setText(preso.getNombreCompleto());
                lblIdentificacionPresa.setText(preso.getIdentificacion());
                lblEdadPresa.setText(String.valueOf(preso.getEdad()));
                lblGrupoSanguineo.setText(preso.getGrupoSanguineo());
                txtPesoPresa.setText(String.valueOf(preso.getPeso()));
                txtEstaturaPresa.setText(String.valueOf(preso.getEstatura()));

               
                cargarFotoPreso(preso.getFotoPath());

                
                jTabbedPane1.setSelectedComponent(diagnostico);
            }
        }
    }//GEN-LAST:event_atenderPresaActionPerformed

    private void cargarFotoPreso(String rutaFoto) {
        try {
            ImageIcon icon = new ImageIcon(rutaFoto);
            Image img = icon.getImage().getScaledInstance(lblFotoPreso.getWidth(), lblFotoPreso.getHeight(), Image.SCALE_SMOOTH);
            lblFotoPreso.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblFotoPreso.setIcon(null);
            lblFotoPreso.setText("Foto no disponible");
        }
    }

    private void configurarTablasConFotos() {
        // Configurar tabla de citas pendientes
        tblCitasPendientes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Columna de foto (ajusta el índice según tu tabla)
                if (column == 0) {
                    try {
                        String idPreso = table.getModel().getValueAt(row, 2).toString(); // Obtener ID del preso
                        Presa preso = presoDAO.buscarPresoPorIdentificacion(idPreso);

                        if (preso != null && preso.getFotoPath() != null) {
                            ImageIcon icon = new ImageIcon(preso.getFotoPath());
                            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            return new JLabel(new ImageIcon(img));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new JLabel("No foto");
                }
                return c;
            }
        });

        // Configurar tabla de historial atendidos
        tblHistorial.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Columna de foto (ajusta el índice según tu tabla)
                if (column == 0) {
                    try {
                        String idPreso = table.getModel().getValueAt(row, 2).toString(); // Obtener ID del preso
                        Presa preso = presoDAO.buscarPresoPorIdentificacion(idPreso);

                        if (preso != null && preso.getFotoPath() != null) {
                            ImageIcon icon = new ImageIcon(preso.getFotoPath());
                            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            return new JLabel(new ImageIcon(img));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new JLabel("No foto");
                }
                return c;
            }
        });
    }


    private void cancelarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarCitaActionPerformed
       int filaSeleccionada = tblCitasPendientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idCita = (int) tblCitasPendientes.getValueAt(filaSeleccionada, 1);

            String razon = JOptionPane.showInputDialog(
                    this,
                    "Ingrese la razón de la cancelación:",
                    "Cancelar Cita",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (razon != null && !razon.trim().isEmpty()) {
                if (citaController.cancelarCita(idCita, razon)) {
                    cargarCitasPendientes();
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar una razón para cancelar la cita",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_cancelarCitaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Documentos (PDF)", "pdf");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            archivoHistoriaClinica = fileChooser.getSelectedFile();
            lblArchivoAdjunto.setText(archivoHistoriaClinica.getName());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      if (citaSeleccionada == null) {
            return;
        }

        String diagnostico = txtAreaDiagnostico.getText();
        if (diagnostico.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un diagnóstico", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        try {
            double nuevoPeso = Double.parseDouble(txtPesoPresa.getText());
            double nuevaEstatura = Double.parseDouble(txtEstaturaPresa.getText());

            Presa preso = citaSeleccionada.getPreso();
            preso.setPeso((float) nuevoPeso);
            preso.setEstatura((float) nuevaEstatura);
            presoDAO.actualizarPreso(preso);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Peso y estatura deben ser valores numéricos válidos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        citaSeleccionada.setDiagnostico(diagnostico);
        citaSeleccionada.setEstado(EstadoCitaMedicaEnum.ATENDIDO);
        citaSeleccionada.setFechaHoraAtencion(LocalDateTime.now());

        
        String receta = recetaTextPane.getText();
        citaSeleccionada.setReceta(receta);

        
        try {
           
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Historia Clínica");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos PDF", "pdf"));

           
            String nombreArchivo = "HistoriaClinica_"
                    + citaSeleccionada.getPreso().getIdentificacion() + "_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".pdf";
            fileChooser.setSelectedFile(new File(nombreArchivo));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File archivoPDF = fileChooser.getSelectedFile();

                
                if (!archivoPDF.getName().toLowerCase().endsWith(".pdf")) {
                    archivoPDF = new File(archivoPDF.getAbsolutePath() + ".pdf");
                }

                
                GeneradorHistoriaClinica.generarPDF(citaSeleccionada, archivoPDF);

                
                citaSeleccionada.setRutaHistoriaClinica(archivoPDF.getAbsolutePath());

                JOptionPane.showMessageDialog(this,
                        "Historia clínica generada exitosamente en:\n"
                        + archivoPDF.getAbsolutePath(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // El usuario canceló, no generamos PDF pero seguimos con el proceso
                JOptionPane.showMessageDialog(this,
                        "Consulta guardada sin generar PDF",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Llamar al controlador para actualizar
        if (citaController.actualizarCita(citaSeleccionada)) {
            cargarCitasPendientes();
            cargarHistorialAtendidos();
            jTabbedPane1.setSelectedComponent(atendidos);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar los datos de la consulta",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
       jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jPanel6MouseEntered

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
            java.util.logging.Logger.getLogger(Enfermera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Enfermera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Enfermera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Enfermera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Enfermera().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel atender;
    private javax.swing.JMenuItem atenderPresa;
    private javax.swing.JPanel atendidos;
    private javax.swing.JMenuItem cancelarCita;
    private javax.swing.JPanel diagnostico;
    private javax.swing.JLabel fotolbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblApellidoPresa;
    private javax.swing.JLabel lblArchivoAdjunto;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblEdadPresa;
    private javax.swing.JLabel lblFotoPreso;
    private javax.swing.JLabel lblGrupoSanguineo;
    private javax.swing.JLabel lblGuardiaAcompanante;
    private javax.swing.JLabel lblIdentificacionPresa;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombrePresa;
    private javax.swing.JLabel lblRol;
    private javax.swing.JTextPane recetaTextPane;
    private javax.swing.JTable tblCitasPendientes;
    private javax.swing.JTable tblHistorial;
    private javax.swing.JTextArea txtAreaDiagnostico;
    private javax.swing.JTextField txtEstaturaPresa;
    private javax.swing.JTextField txtPesoPresa;
    // End of variables declaration//GEN-END:variables
}
