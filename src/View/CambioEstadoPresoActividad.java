
package View;

import Controller.ActividadController;
import DAO.ActividadDAO;
import Model.Constants.EstadoActividadesPresoEnum;
import Model.Entities.Actividad;
import Model.Entities.Preso;
import java.util.List;
import javax.swing.JOptionPane;


public class CambioEstadoPresoActividad extends javax.swing.JDialog {

    private Preso preso;

    ActividadController ac = ActividadController.getInstancia();

    public CambioEstadoPresoActividad(java.awt.Frame parent, boolean modal, Preso preso) {
        super(parent, modal);
        this.preso = preso;
        this.setLocationRelativeTo(null);

        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nuevoEstadoPresoAct = new javax.swing.JComboBox<>();
        btnCambiar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 26, 52));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CAMBIO DE ESTADO");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 150, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 30));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Estado:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 70, -1));

        nuevoEstadoPresoAct.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "EN_PROCESO", "FINALIZADA", "CANCELADA" }));
        jPanel1.add(nuevoEstadoPresoAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 140, 40));

        btnCambiar.setBackground(new java.awt.Color(0, 51, 0));
        btnCambiar.setForeground(new java.awt.Color(255, 255, 255));
        btnCambiar.setText("Actualizar");
        btnCambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCambiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 100, 30));

        btnCancelar.setBackground(new java.awt.Color(51, 0, 0));
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 100, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCambiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarActionPerformed

        String estadoSeleccionado = nuevoEstadoPresoAct.getSelectedItem().toString();

        if (estadoSeleccionado.equals("<Seleccione>")) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un estado válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstadoActividadesPresoEnum nuevoEstado;
        try {
            nuevoEstado = EstadoActividadesPresoEnum.valueOf(estadoSeleccionado);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Estado seleccionado no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Actividad> actividades = ActividadDAO.getInstancia().cargarActividades();

        for (Actividad act : actividades) {
            if (act.getPresosAsignadosIds().contains(preso.getIdentificacion())) {
                EstadoActividadesPresoEnum estadoActual = act.getEstadoPreso(preso.getIdentificacion());

                if ((estadoActual == EstadoActividadesPresoEnum.FINALIZADA || estadoActual == EstadoActividadesPresoEnum.CANCELADA)
                        && nuevoEstado == EstadoActividadesPresoEnum.EN_PROCESO) {
                    JOptionPane.showMessageDialog(this, "No se puede cambiar a EN_PROCESO porque la actividad ya fue FINALIZADA o CANCELADA.", "Operación no permitida", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                act.setEstadoPreso(preso.getIdentificacion(), nuevoEstado); 
                break;
            }
        }

        boolean guardado = ActividadDAO.getInstancia().guardarActividades(actividades); 

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Estado del preso actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el cambio en el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.dispose();

    
    }//GEN-LAST:event_btnCambiarActionPerformed

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
        java.util.logging.Logger.getLogger(CambioEstadoPresoActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(CambioEstadoPresoActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(CambioEstadoPresoActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(CambioEstadoPresoActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> nuevoEstadoPresoAct;
    // End of variables declaration//GEN-END:variables
}
