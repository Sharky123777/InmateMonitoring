
package View;

import Controller.ActividadController;
import DAO.OficialDAO;
import Model.Constants.EstadoActividadesEnum;
import Model.Entities.Actividad;
import Model.Entities.Oficial;
import javax.swing.JOptionPane;


public class CambioEstadoActividad extends javax.swing.JDialog {

    private final OficialDAO oficialDAO = OficialDAO.getInstancia();
    private Actividad actividadSeleccionada;
    private ActividadController actividadController;
    
    public CambioEstadoActividad(java.awt.Frame parent, boolean modal, Actividad actividad) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        
 
        
        this.actividadSeleccionada = actividad;
        this.actividadController = ActividadController.getInstancia();
        
        nuevoEstadoComb.setSelectedItem(actividad.getEstado());
        

    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nuevoEstadoComb = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnCancealar = new javax.swing.JButton();
        btnActualizarEstado = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 26, 52));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CAMBIO DE ESTADO");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 150, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 30));

        nuevoEstadoComb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "ACTIVA", "CANCELADA" }));
        jPanel1.add(nuevoEstadoComb, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 140, 30));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Estado:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 70, -1));

        btnCancealar.setBackground(new java.awt.Color(51, 0, 0));
        btnCancealar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancealar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancealar.setText("Cancelar");
        btnCancealar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancealarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancealar, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 100, 30));

        btnActualizarEstado.setBackground(new java.awt.Color(0, 51, 0));
        btnActualizarEstado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizarEstado.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarEstado.setText("Actualizar");
        btnActualizarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarEstadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizarEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 100, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarEstadoActionPerformed
        EstadoActividadesEnum nuevoEstado = (EstadoActividadesEnum) nuevoEstadoComb.getSelectedItem();
    String idActividad = actividadSeleccionada.getIdActividad();
    
    if ("ACTIVA".equals(nuevoEstado) && 
        "CANCELADA".equals(actividadSeleccionada.getEstado())) {
        Model.Entities.Oficial responsable = oficialDAO.obtenerOficialPorCedula(actividadSeleccionada.getResponsableOficial());
        
        if (responsable != null) {
            if (!actividadController.puedeAgregarActividad(responsable)) {
                JOptionPane.showMessageDialog(this, 
                    "El responsable ya tiene el límite máximo de actividades (2). No se puede reactivar esta actividad.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    
    if (actividadController.actualizarEstadoActividad(idActividad, nuevoEstado)) {
        JOptionPane.showMessageDialog(this, "Estado de la actividad actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        if ("CANCELADA".equals(nuevoEstado)) {
            actividadController.actualizarEstadoPresosActividad(idActividad, EstadoActividadesEnum.CANCELADA);
        }
        
        this.dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la actividad", "Error", JOptionPane.ERROR_MESSAGE);
        }    }//GEN-LAST:event_btnActualizarEstadoActionPerformed

    private void btnCancealarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancealarActionPerformed
this.dispose();
    }//GEN-LAST:event_btnCancealarActionPerformed

  
    public static void main(String args[]) {
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarEstado;
    private javax.swing.JButton btnCancealar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> nuevoEstadoComb;
    // End of variables declaration//GEN-END:variables
}
