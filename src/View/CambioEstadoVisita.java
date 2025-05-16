package View;

import Controller.VisitaController;
import Model.Constants.EstadoVisitaEnum;
import Model.Entities.Visita;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CambioEstadoVisita extends javax.swing.JDialog {

    private VisitaController controller;
    private Visita visita;
    private String identificacionPreso;
    private javax.swing.JTable tablaVisitas;

    public CambioEstadoVisita(java.awt.Frame parent, boolean modal, Visita visita,
            String identificacionPreso, javax.swing.JTable tablaVisitas) {
        super(parent, modal);
        initComponents();
        this.controller = new VisitaController();
        this.visita = visita;
        this.identificacionPreso = identificacionPreso;
        this.tablaVisitas = tablaVisitas;
        setLocationRelativeTo(parent);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                regresarATabla1();
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                regresarATabla1();
            }

        });

    }

    private void regresarATabla1() {
        if (this.getParent() instanceof PersonalDeControl) {
            PersonalDeControl padre = (PersonalDeControl) this.getParent();
            padre.getTabbedPDC().setSelectedIndex(1);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        JcomboNuevoEstado = new javax.swing.JComboBox<>();
        BotonModificarEstadoVisita = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 26, 52));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CAMBIO ESTADO VISITA");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 180, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 50));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Estado visita:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        JcomboNuevoEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "EN_PROCESO", "FINALIZADA", "CANCELADA" }));
        jPanel1.add(JcomboNuevoEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 100, 140, 30));

        BotonModificarEstadoVisita.setText("Modificar estado");
        BotonModificarEstadoVisita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonModificarEstadoVisitaActionPerformed(evt);
            }
        });
        jPanel1.add(BotonModificarEstadoVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 173, 130, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 260));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonModificarEstadoVisitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonModificarEstadoVisitaActionPerformed
        try {
            if (visita == null) {
                JOptionPane.showMessageDialog(this,
                        "Visita no encontrada",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (visita.getEstado() == EstadoVisitaEnum.FINALIZADA || visita.getEstado() == EstadoVisitaEnum.CANCELADA) {
                JOptionPane.showMessageDialog(this,
                        "No se puede modificar el estado de una visita que ya está FINALIZADA o CANCELADA.",
                        "Acción no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String estadoSeleccionado = JcomboNuevoEstado.getSelectedItem().toString();

            if (estadoSeleccionado.equals("< Seleccionar >")) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un estado válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EstadoVisitaEnum nuevoEstado = EstadoVisitaEnum.valueOf(estadoSeleccionado);

            String razonCancelacion = null;

            if (nuevoEstado == EstadoVisitaEnum.CANCELADA) {
                int opcion = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea cancelar la visita?\n"
                        + "Motivo: El visitante canceló la visita.",
                        "Confirmación de cancelación",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (opcion != JOptionPane.OK_OPTION) {
                    return; 
                }

                razonCancelacion = "El visitante canceló la visita";
            }

            Visita visitaActualizada = controller.cambiarEstadoVisita(visita.getId(), nuevoEstado, razonCancelacion);

            if (visitaActualizada != null) {
                controller.cargarHistorialVisitas(identificacionPreso, tablaVisitas);

                if (nuevoEstado == EstadoVisitaEnum.CANCELADA) {
                    controller.enviarCorreoCancelacionPorVisitante(visitaActualizada);
                }

                JOptionPane.showMessageDialog(this,
                        "Estado de la visita actualizado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                regresarATabla1();
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar estado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        }    }//GEN-LAST:event_BotonModificarEstadoVisitaActionPerformed

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
            java.util.logging.Logger.getLogger(CambioEstadoVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonModificarEstadoVisita;
    private javax.swing.JComboBox<String> JcomboNuevoEstado;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
