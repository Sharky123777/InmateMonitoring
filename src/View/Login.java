
package View;

import Controller.UsuarioController;
import Model.Constants.RolEnum;
import Model.Entities.Usuario;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Login extends javax.swing.JFrame {

    
    public Login() {
        initComponents();
        setLocationRelativeTo(null);


        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

       
        for (RolEnum rol : RolEnum.values()) {
            model.addElement(rol.toString());
        }

        RolCmbBox.setModel(model);
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new RoundedPanel(30);
        tituloInpec = new javax.swing.JLabel();
        lema = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        FieldUsuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Password = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        RolCmbBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(29, 35, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tituloInpec.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        tituloInpec.setForeground(new java.awt.Color(255, 255, 255));
        tituloInpec.setText("INPEC");
        jPanel6.add(tituloInpec, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 160, -1));

        lema.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        lema.setForeground(new java.awt.Color(153, 153, 153));
        lema.setText("Con orden y precisión, aseguramos la institución.");
        jPanel6.add(lema, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Correo:");
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, -1, -1));

        FieldUsuario.setBackground(new java.awt.Color(29, 35, 51));
        FieldUsuario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        FieldUsuario.setForeground(new java.awt.Color(255, 255, 255));
        FieldUsuario.setBorder(null);
        jPanel6.add(FieldUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 200, 350, 40));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Contraseña");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, -1, -1));

        Password.setBackground(new java.awt.Color(29, 35, 51));
        Password.setForeground(new java.awt.Color(255, 255, 255));
        Password.setBorder(null);
        jPanel6.add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, 350, 40));

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, 390, 10));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 390, 10));

        jButton1.setText("Ingresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 390, 170, 30));

        RolCmbBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(RolCmbBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 400, 40));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 890, 510));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void redirigirSegunRol(Usuario usuario) {
        JFrame vista = null;

        switch (usuario.getRol()) {
            case DIRECTOR:
                vista = new Directora();
                break;
            case OFICIAL:
                vista = new Oficial();
                break;
            case OFICIAL_DE_REGISTRO:
                vista = new OficialDeRegistro();
                break;
            case PERSONAL_DE_CONTROL:
                vista = new PersonalDeControl();
                break;
            case COORDINADOR_DE_ACTIVIDADES:
                vista = new CoordinadoraDeActividades();
                break;
            case ENFERMERA:
                vista = new Enfermera();
                break;
            default:
                throw new IllegalArgumentException("Rol no soportado: " + usuario.getRol());
        }

        if (vista != null) {
            if (vista instanceof PerfilUsuario) {
                ((PerfilUsuario) vista).setUsuario(usuario);
            }
            vista.setVisible(true);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String username = FieldUsuario.getText().trim();
        String password = new String(Password.getPassword()).trim();
        String rolTexto = (String) RolCmbBox.getSelectedItem();

        try {
           
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

           
            RolEnum rol = RolEnum.fromDisplayText(rolTexto);

            
            Usuario usuario = UsuarioController.getInstancia().autenticarUsuario(username, password, rol);

           
            redirigirSegunRol(usuario);
            this.dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Rol no válido seleccionado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            String mensajeBloqueo = UsuarioController.getInstancia().obtenerMensajeBloqueo(username);
            JOptionPane.showMessageDialog(this,
                    e.getMessage() + "\n" + mensajeBloqueo,
                    "Error de seguridad",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

 
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FieldUsuario;
    private javax.swing.JPasswordField Password;
    private javax.swing.JComboBox<String> RolCmbBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lema;
    private javax.swing.JLabel tituloInpec;
    // End of variables declaration//GEN-END:variables
}
