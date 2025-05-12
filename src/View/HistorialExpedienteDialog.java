
package View;

import Controller.ExpedienteController;
import Model.Entities.ExpedienteJudicial;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
public class HistorialExpedienteDialog extends javax.swing.JDialog {

     private static final int REGISTROS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private List<ExpedienteJudicial> todosExpedientes;
    
    
    public HistorialExpedienteDialog(java.awt.Frame parent, String identificacionPreso) {
       super(parent, true);
               setLocationRelativeTo(parent);

        initComponents();
        
        tblExpedientes.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tblExpedientes.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        cargarExpedientes(identificacionPreso);
        actualizarTabla();
        
    }
    
    
    private void cargarExpedientes(String identificacionPreso) {
        this.todosExpedientes = ExpedienteController.getInstancia()
                .obtenerHistorialExpedientes(identificacionPreso);
        actualizarControlesPaginacion();
    }

    private int getTotalPaginas() {
        if (todosExpedientes == null || todosExpedientes.isEmpty()) {
            return 1;
        }
        return (int) Math.ceil((double) todosExpedientes.size() / REGISTROS_POR_PAGINA);
    }

    private List<ExpedienteJudicial> getExpedientesPagina() {
        int inicio = (paginaActual - 1) * REGISTROS_POR_PAGINA;
        int fin = Math.min(inicio + REGISTROS_POR_PAGINA, todosExpedientes.size());
        return todosExpedientes.subList(inicio, fin);
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tblExpedientes.getModel();
        modelo.setRowCount(0);
        
        if (todosExpedientes != null && !todosExpedientes.isEmpty()) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (ExpedienteJudicial exp : getExpedientesPagina()) {
                modelo.addRow(new Object[]{
                    exp.getCodigoExpediente(),
                    exp.getFechaApertura().format(fmt),
                    exp.getEstado(),
                    "Ver Detalle"
                });
            }
        }
        actualizarControlesPaginacion();
    }

    private void actualizarControlesPaginacion() {
        btnAnterior.setEnabled(paginaActual > 1);
        btnSiguiente.setEnabled(paginaActual < getTotalPaginas());
        lblPagina.setText("Página " + paginaActual + " de " + getTotalPaginas());
    }

    // Clase para el renderizado del botón
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

    // Clase para la edición del botón
    class ButtonEditor extends DefaultCellEditor {
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            editorComponent = new JButton();
            ((JButton)editorComponent).addActionListener(e -> {
                int row = tblExpedientes.convertRowIndexToModel(tblExpedientes.getEditingRow());
                String codigo = (String) tblExpedientes.getModel().getValueAt(row, 0);
                verDetalleExpediente(codigo);
                fireEditingStopped();
            });
        }

        private void verDetalleExpediente(String codigo) {
            ExpedienteJudicial exp = ExpedienteController.getInstancia()
                    .obtenerExpedientePorCodigo(codigo);

            if (exp != null) {
                JOptionPane.showMessageDialog(HistorialExpedienteDialog.this,
                    "Detalles del expediente:\n\n" +
                    "Código: " + codigo + "\n" +
                    "Estado: " + exp.getEstado() + "\n" +
                    "Fecha Apertura: " + exp.getFechaApertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                    "N° Delitos: " + (exp.getDelitos() != null ? exp.getDelitos().size() : 0),
                    "Detalle del Expediente",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    

    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblExpedientes = new javax.swing.JTable();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();

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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 870, 480));

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 160, -1));

        btnSiguiente.setText("Siguiente");
        jPanel1.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 160, -1));

        lblPagina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblPagina.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(lblPagina, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 270, 30));

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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
 if (paginaActual > 1) {
            paginaActual--;
            actualizarTabla();
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

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
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JTable tblExpedientes;
    // End of variables declaration//GEN-END:variables
}
