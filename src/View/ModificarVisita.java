package View;

import Controller.VisitaController;
import Model.Entities.Visita;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class ModificarVisita extends javax.swing.JDialog {

    private VisitaController controller = new VisitaController();
    private Visita visita;
    private JTable tablaVisitas;
    private String identificacionPreso;

    public ModificarVisita(java.awt.Frame parent, boolean modal, Visita visita, JTable tablaVisitas, String identificacionPreso) {
        super(parent, modal);
        initComponents();
        this.visita = visita;
        this.tablaVisitas = tablaVisitas;
        this.identificacionPreso = identificacionPreso;
        cargarDatosVisita();
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

    private void cargarDatosVisita() {
        if (visita != null) {
            NuevoTipoVisita.setSelectedIndex(0);
            NuevoLugarVisita.setSelectedIndex(0);
        }
    }
    
    public void limpiarCamposVisita(){
        NuevoTipoVisita.setSelectedIndex(0);
        NuevoLugarVisita.setSelectedItem(0);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        NuevoLugarVisita = new javax.swing.JComboBox<>();
        NuevoTipoVisita = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        BotonModificarVisita = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Lugar de visita:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Modificar Visita");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Tipo de visita:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));

        NuevoLugarVisita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Sala de visitas", "Cabinas de visitas conyugales", "Salas de visitas legales" }));
        jPanel3.add(NuevoLugarVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 230, 30));

        NuevoTipoVisita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Visita regular", "Familiar", "Intimas o Conyugales", "Legal", "Religiosas" }));
        jPanel3.add(NuevoTipoVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 122, 230, 30));
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 330, 20));

        BotonModificarVisita.setText("Modificar visita");
        BotonModificarVisita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonModificarVisitaActionPerformed(evt);
            }
        });
        jPanel3.add(BotonModificarVisita, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 150, 30));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonModificarVisitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonModificarVisitaActionPerformed
        try {
            if (visita == null) {
                JOptionPane.showMessageDialog(this,
                        "Visita no encontrada",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoTipo = NuevoTipoVisita.getSelectedItem().toString();
            String nuevoLugar = NuevoLugarVisita.getSelectedItem().toString();

            Visita visitaActualizada = controller.actualizarVisita(
                    visita.getId(),
                    nuevoTipo,
                    nuevoLugar
            );

            if (visitaActualizada != null) {
                controller.cargarHistorialVisitas(identificacionPreso, tablaVisitas);

                limpiarCamposVisita();
                regresarATabla1();

                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar visita: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }    }//GEN-LAST:event_BotonModificarVisitaActionPerformed

   
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
            java.util.logging.Logger.getLogger(ModificarVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModificarVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModificarVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModificarVisita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
    /* Create and display the dialog */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonModificarVisita;
    private javax.swing.JComboBox<String> NuevoLugarVisita;
    private javax.swing.JComboBox<String> NuevoTipoVisita;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    public JComboBox<String> getNuevoTipoVisita() {
        return NuevoTipoVisita;
    }

    public JComboBox<String> getNuevoLugarVisita() {
        return NuevoLugarVisita;
    }
}
