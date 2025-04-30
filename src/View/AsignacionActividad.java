package View;

import Controller.ActividadController;
import Model.Preso;
import javax.swing.JOptionPane;

public class AsignacionActividad extends javax.swing.JDialog {

    ActividadController controller = ActividadController.getInstancia();

    public AsignacionActividad(java.awt.Frame parent, boolean modal, Preso preso) {
        super(parent, modal);
        initComponents();
            controller.cargarActividadesDisponiblesEnTabla(actividadesAsignacionTabla, preso.getIdentificacion());
        identiPreso.setText(preso.getIdentificacion());
        nombreApePreso.setText(preso.getNombresCompletos() + " " + preso.getApellidosCompletos());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        actividadesAsignacionTabla = new javax.swing.JTable();
        btnCancelar = new javax.swing.JButton();
        btnAsignar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        identiPreso = new javax.swing.JLabel();
        nombreApePreso = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(20, 20, 53));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 40));

        actividadesAsignacionTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Asignar", "Id", "Nombre", "Tipo", "Día", "Horario", "Lugar", "Inscritos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        actividadesAsignacionTabla.setRowHeight(50);
        jScrollPane1.setViewportView(actividadesAsignacionTabla);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 930, 330));

        btnCancelar.setBackground(new java.awt.Color(51, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 70, 120, 30));

        btnAsignar.setBackground(new java.awt.Color(0, 102, 51));
        btnAsignar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnAsignar.setText("Asignar");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAsignar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 110, 120, 40));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 540, 10));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 93, 540, 10));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Identificacion:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 100, 20));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Preso:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 70, 20));

        identiPreso.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        identiPreso.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(identiPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 440, 20));

        nombreApePreso.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nombreApePreso.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(nombreApePreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 480, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        int filasSeleccionadas = actividadesAsignacionTabla.getRowCount();
        boolean actividadSeleccionada = false;

        for (int i = 0; i < filasSeleccionadas; i++) {
            boolean actividadMarcada = (Boolean) actividadesAsignacionTabla.getValueAt(i, 0);
            if (actividadMarcada) {
                actividadSeleccionada = true;
                String idActividad = (String) actividadesAsignacionTabla.getValueAt(i, 1);
                String identificacionPreso = identiPreso.getText();
                boolean asignado = controller.asignarPresoAActividad(idActividad, identificacionPreso);

                this.dispose();

                if (!actividadSeleccionada) {
                    JOptionPane.showMessageDialog(this, "Por favor seleccione al menos una actividad.");
                }
            }
        }
    }//GEN-LAST:event_btnAsignarActionPerformed

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
            java.util.logging.Logger.getLogger(AsignacionActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsignacionActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsignacionActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsignacionActividad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable actividadesAsignacionTabla;
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel identiPreso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel nombreApePreso;
    // End of variables declaration//GEN-END:variables
}
