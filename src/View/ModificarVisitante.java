package View;

import Controller.VisitaController;
import Model.Entities.Visitante;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ModificarVisitante extends javax.swing.JDialog {

    private VisitaController visitaController = new VisitaController();
    private Visitante visitante;
    private JTable tablaVisitantes;
    private String identificacionPreso;
    private File nuevaImagenVisitante = null;

    public ModificarVisitante(java.awt.Frame parent, boolean modal, Visitante visitante, JTable tablaVisitantes, String identificacionPreso) {
        super(parent, modal);
        initComponents();
        this.visitante = visitante;
        this.tablaVisitantes = tablaVisitantes;
        this.identificacionPreso = identificacionPreso;
        cargarDatosVisitante();
        setLocationRelativeTo(parent);
    }

    private void cargarDatosVisitante() {
        getNuevoPrimerNombreVisitante().setText("");
        getNuevoSegundoNombreVisitante().setText("");
        getNuevoPrimerApellidoVisitante().setText("");
        getNuevoSegundoApellidoVisitante().setText("");
        getNuevaEdadVisitante().setText("");

        getNuevoSexoVisitante().setSelectedIndex(0);
        getNuevaRelacionConPresoVisitante().setSelectedIndex(0);

        getNuevaVistaPreviaVisitante().setIcon(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        NuevoPrimerNombreVisitante = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        NuevoSegundoNombreVisitante = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        NuevoPrimerApellidoVisitante = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        NuevoSegundoApellidoVisitante = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        NuevaEdadVisitante = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        NuevoSexoVisitante = new javax.swing.JComboBox<>();
        jLabel64 = new javax.swing.JLabel();
        NuevaRelacionConPresoVisitante = new javax.swing.JComboBox<>();
        BotonModificarVisitante = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        NuevaImagenVisitante = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        NuevaVistaPreviaVisitante = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Modificar datos de un visitante");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, -1, -1));
        jPanel8.add(NuevoPrimerNombreVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 320, 30));

        jLabel59.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("Segundo nombre:");
        jPanel8.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, -1));
        jPanel8.add(NuevoSegundoNombreVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 310, 30));

        jLabel60.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 0, 0));
        jLabel60.setText("Primer apellido:");
        jPanel8.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));
        jPanel8.add(NuevoPrimerApellidoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 320, 30));

        jLabel61.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 0));
        jLabel61.setText("Segundo apellido:");
        jPanel8.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, -1, -1));
        jPanel8.add(NuevoSegundoApellidoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 310, 30));

        jLabel62.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 0, 0));
        jLabel62.setText("Edad:");
        jPanel8.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));
        jPanel8.add(NuevaEdadVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, 390, 30));

        jLabel63.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 0, 0));
        jLabel63.setText("Sexo:");
        jPanel8.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, -1, -1));

        NuevoSexoVisitante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Femenino", "Masculino" }));
        jPanel8.add(NuevoSexoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 390, 30));

        jLabel64.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 0, 0));
        jLabel64.setText("Vista previa foto");
        jPanel8.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, -1, -1));

        NuevaRelacionConPresoVisitante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "Esposa/Esposo", "Padre/Madre", "Hijo/Hija", "Hermano/Hermana", "Abuelo/Abuela", "Nieto/Nieta", "Tío/Tía", "Sobrino/Sobrina", "Primo/Prima", "Suegro/Suegra", "Yerno/Nuera", "Cuñado/Cuñada", "Amigo/Amiga", "Compañero de Trabajo", "Vecino/Vecina", "Conocido/Conocida", "Abogado/Abogada", "Asistente Social", "Representante Legal", "Sacerdote/Pastor", "Novio/Novia", "Tutor Legal", "Ex-Esposo/Ex-Esposa", "Familiar Político " }));
        jPanel8.add(NuevaRelacionConPresoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, 280, 30));

        BotonModificarVisitante.setText("Modificar visitante");
        BotonModificarVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonModificarVisitanteActionPerformed(evt);
            }
        });
        jPanel8.add(BotonModificarVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 480, 130, 30));

        jLabel65.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("Primer nombre:");
        jPanel8.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        jLabel66.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Relación con el preso:");
        jPanel8.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, -1));

        NuevaImagenVisitante.setText("Selecionar nueva foto");
        NuevaImagenVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevaImagenVisitanteActionPerformed(evt);
            }
        });
        jPanel8.add(NuevaImagenVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, -1, 30));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel16.add(NuevaVistaPreviaVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, 170));

        jPanel8.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 120, 170, 190));
        jPanel8.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 63, 390, 10));

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NuevaImagenVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevaImagenVisitanteActionPerformed
        VisitaController controller = new VisitaController();
        nuevaImagenVisitante = controller.seleccionarImagen(this, getNuevaVistaPreviaVisitante());
    }//GEN-LAST:event_NuevaImagenVisitanteActionPerformed

    private void BotonModificarVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonModificarVisitanteActionPerformed
        try {
            if (visitante == null) {
                JOptionPane.showMessageDialog(this,
                        "Visitante no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String primerNombre = getNuevoPrimerNombreVisitante().getText();
            String segundoNombre = getNuevoSegundoNombreVisitante().getText();
            String primerApellido = getNuevoPrimerApellidoVisitante().getText();
            String segundoApellido = getNuevoSegundoApellidoVisitante().getText();
            String edadStr = getNuevaEdadVisitante().getText();
            String sexo = getNuevoSexoVisitante().getSelectedItem().toString();
            String relacionConPreso = getNuevaRelacionConPresoVisitante().getSelectedItem().toString();

            File imagen = nuevaImagenVisitante != null ? nuevaImagenVisitante : new File(visitante.getFotoPath());

            VisitaController controller = new VisitaController();
            Visitante visitanteActualizado = controller.actualizarVisitante(
                    visitante,
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    edadStr,
                    sexo,
                    relacionConPreso,
                    imagen
            );

            if (visitanteActualizado != null) {
                controller.cargarHistorialVisitantes(identificacionPreso, tablaVisitantes);

                PersonalDeControl padre = (PersonalDeControl) this.getParent();
                padre.getTabbedPDC().setSelectedIndex(1);

                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar visitante: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BotonModificarVisitanteActionPerformed

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
            java.util.logging.Logger.getLogger(ModificarVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModificarVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModificarVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModificarVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonModificarVisitante;
    private javax.swing.JTextField NuevaEdadVisitante;
    private javax.swing.JButton NuevaImagenVisitante;
    private javax.swing.JComboBox<String> NuevaRelacionConPresoVisitante;
    private javax.swing.JLabel NuevaVistaPreviaVisitante;
    private javax.swing.JTextField NuevoPrimerApellidoVisitante;
    private javax.swing.JTextField NuevoPrimerNombreVisitante;
    private javax.swing.JTextField NuevoSegundoApellidoVisitante;
    private javax.swing.JTextField NuevoSegundoNombreVisitante;
    private javax.swing.JComboBox<String> NuevoSexoVisitante;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator11;
    // End of variables declaration//GEN-END:variables

    public JTextField getNuevoPrimerNombreVisitante() {
        return NuevoPrimerNombreVisitante;
    }

    public JTextField getNuevoSegundoNombreVisitante() {
        return NuevoSegundoNombreVisitante;
    }

    public JTextField getNuevoPrimerApellidoVisitante() {
        return NuevoPrimerApellidoVisitante;
    }

    public JTextField getNuevoSegundoApellidoVisitante() {
        return NuevoSegundoApellidoVisitante;
    }

    public JTextField getNuevaEdadVisitante() {
        return NuevaEdadVisitante;
    }

    public JComboBox<String> getNuevoSexoVisitante() {
        return NuevoSexoVisitante;
    }

    public JComboBox<String> getNuevaRelacionConPresoVisitante() {
        return NuevaRelacionConPresoVisitante;
    }

    public JLabel getNuevaVistaPreviaVisitante() {
        return NuevaVistaPreviaVisitante;
    }

}
