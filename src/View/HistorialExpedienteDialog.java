package View;

import Controller.ExpedienteController;
import Controller.PresaController;
import Model.Entities.Delito;
import Model.Entities.ExpedienteJudicial;
import Model.Entities.Presa;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class HistorialExpedienteDialog extends javax.swing.JDialog {

    private final ExpedienteController expedienteController = ExpedienteController.getInstancia();
    private final JTabbedPane tabbedPane;
    private final JLabel fechaSalida, registroNum, codExpe, fechaAper, estado, juzgado, nivelRiesgo;
    private final JLabel nombre, apellidos, edad, identificacion, nacionalidad, fotoPresoExpediente;
    private final JTable tablaExpediente;
    private final JLabel sentenciaTotal;
    private final JPanel panelEstadoEspecialPreso;
    private final JLabel lblMensajeEspecialPreso;

    private List<ExpedienteJudicial> todosExpedientes;
    private PresaController controllerPreso = PresaController.getInstancia();

    public HistorialExpedienteDialog(
            java.awt.Frame parent,
            String identificacionPreso,
            ExpedienteController expedienteController,
            JTabbedPane tabbedPane,
            JLabel fechaSalida,
            JLabel registroNum,
            JLabel codExpe,
            JLabel fechaAper,
            JLabel estado,
            JLabel juzgado,
            JLabel nivelRiesgo,
            JLabel nombre,
            JLabel apellidos,
            JLabel edad,
            JLabel identificacion,
            JLabel nacionalidad,
            JLabel fotoPresoExpediente,
            JTable tablaExpediente,
            JLabel sentenciaTotal,
            JPanel panelEstadoEspecialPreso,
            JLabel lblMensajeEspecialPreso,
            JTextField txtReclusa,
            JTextField txtIdentificacionReclusa,
            JTextField txtEstadoReclusa
    ) {
        super(parent, true);
        setLocationRelativeTo(parent);
        initComponents();

        this.tabbedPane = tabbedPane;
        this.fechaSalida = fechaSalida;
        this.registroNum = registroNum;
        this.codExpe = codExpe;
        this.fechaAper = fechaAper;
        this.estado = estado;
        this.juzgado = juzgado;
        this.nivelRiesgo = nivelRiesgo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.identificacion = identificacion;
        this.nacionalidad = nacionalidad;
        this.fotoPresoExpediente = fotoPresoExpediente;
        this.tablaExpediente = tablaExpediente;
        this.sentenciaTotal = sentenciaTotal;
        this.panelEstadoEspecialPreso = panelEstadoEspecialPreso;
        this.lblMensajeEspecialPreso = lblMensajeEspecialPreso;
        
      ;
        
        

        tblExpedientes.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tblExpedientes.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        cargarExpedientes(identificacionPreso);
        actualizarTabla();

        try {
            Presa preso = PresaController.getInstancia().buscarPreso(identificacionPreso);
            if (preso != null) {
                this.txtReclusa.setText(preso.getNombresCompletos());
                this.txtIdentificacionReclusa.setText(preso.getIdentificacion());
                this.txtEstadoReclusa.setText(preso.getEstado().toString()); 
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró al preso con esa identificación", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar al preso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void cargarExpedientes(String identificacionPreso) {
        this.todosExpedientes = ExpedienteController.getInstancia()
                .obtenerHistorialExpedientes(identificacionPreso);
    }

    

private void actualizarTabla() {
    DefaultTableModel modelo = (DefaultTableModel) tblExpedientes.getModel();
    modelo.setRowCount(0);

    if (todosExpedientes != null && !todosExpedientes.isEmpty()) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (ExpedienteJudicial exp : todosExpedientes) {
            modelo.addRow(new Object[]{
                exp.getCodigoExpediente(),
                exp.getFechaApertura().format(fmt),
                exp.getEstado(),
                "Ver Detalle"
            });
        }
    }
}

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Ver Detalle");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            editorComponent = new JButton();
            ((JButton) editorComponent).addActionListener(e -> {
                int row = tblExpedientes.convertRowIndexToModel(tblExpedientes.getEditingRow());
                String codigo = (String) tblExpedientes.getModel().getValueAt(row, 0);
                verDetalleExpediente(codigo);
                fireEditingStopped();
            });
        }

        private void verDetalleExpediente(String codigo) {
            ExpedienteJudicial exp = ExpedienteController.getInstancia().obtenerExpedientePorCodigo(codigo);

            if (exp != null) {
                expedienteController.cargarExpedienteCompletoDesdeObjeto(
                        exp,
                        fechaSalida, registroNum, codExpe, fechaAper, estado, juzgado, nivelRiesgo,
                        nombre, apellidos, edad, identificacion, nacionalidad, fotoPresoExpediente,
                        tablaExpediente, sentenciaTotal, panelEstadoEspecialPreso, lblMensajeEspecialPreso
                );

                List<Delito> delitos = exp.getDelitos();

                expedienteController.cargarTablaDelitos(delitos, tablaExpediente);

                tabbedPane.setSelectedIndex(6);
                dispose();
            } else {
                JOptionPane.showMessageDialog(HistorialExpedienteDialog.this,
                        "Expediente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblExpedientes = new javax.swing.JTable();
        jPanel2 = new RoundedPanel(30);
        ;
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txtReclusa = new javax.swing.JLabel();
        txtIdentificacionReclusa = new javax.swing.JLabel();
        txtEstadoReclusa = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblExpedientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Fecha apertura", "Estado", "Accion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblExpedientes.setRowHeight(50);
        jScrollPane1.setViewportView(tblExpedientes);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 870, 340));

        jPanel2.setBackground(new java.awt.Color(24, 30, 58));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("HISTORICO DE EXPEDIENTES");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 370, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 850, 60));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Identificación:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 110, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Estado:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110, 80, 30));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 270, 10));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 130, 310, 10));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Reclusa:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 80, 30));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 310, 10));

        txtReclusa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtReclusa.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(txtReclusa, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 300, 20));

        txtIdentificacionReclusa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtIdentificacionReclusa.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(txtIdentificacionReclusa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 270, 20));

        txtEstadoReclusa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtEstadoReclusa.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(txtEstadoReclusa, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 320, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(HistorialExpedienteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HistorialExpedienteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HistorialExpedienteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HistorialExpedienteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable tblExpedientes;
    private javax.swing.JLabel txtEstadoReclusa;
    private javax.swing.JLabel txtIdentificacionReclusa;
    private javax.swing.JLabel txtReclusa;
    // End of variables declaration//GEN-END:variables
}
