package View;

import Controller.VisitaController;
import Model.Constants.EstadoVisitanteEnum;
import Model.Entities.Visitante;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CambioEstadoVisitante extends javax.swing.JDialog {

    private VisitaController controller;
    private Visitante visitante;
    private String identificacionPreso;
    private javax.swing.JTable tablaVisitantes;

    public CambioEstadoVisitante(java.awt.Frame parent, boolean modal, Visitante visitante,
            String identificacionPreso, javax.swing.JTable tablaVisitantes) {
        super(parent, modal);
        initComponents();
        this.controller = new VisitaController();
        this.visitante = visitante;
        this.identificacionPreso = identificacionPreso;
        this.tablaVisitantes = tablaVisitantes;
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
        JcomboNuevoEstadoVisitante = new javax.swing.JComboBox<>();
        BotonModificarEstadoVisitante = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 26, 52));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CAMBIO ESTADO VISITANTE");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 210, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 50));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Estado visitante:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        JcomboNuevoEstadoVisitante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "< Seleccionar >", "HABILITADO", "DESHABILITADO" }));
        jPanel1.add(JcomboNuevoEstadoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 140, 30));

        BotonModificarEstadoVisitante.setText("Modificar estado");
        BotonModificarEstadoVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonModificarEstadoVisitanteActionPerformed(evt);
            }
        });
        jPanel1.add(BotonModificarEstadoVisitante, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 173, 130, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 260));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonModificarEstadoVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonModificarEstadoVisitanteActionPerformed
        try {
            if (visitante == null) {
                JOptionPane.showMessageDialog(this,
                        "Visitante no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String estadoSeleccionado = JcomboNuevoEstadoVisitante.getSelectedItem().toString();

            if (estadoSeleccionado.equals("< Seleccionar >")) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un estado válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EstadoVisitanteEnum nuevoEstado = EstadoVisitanteEnum.valueOf(estadoSeleccionado);

            if (visitante.getEstado() == nuevoEstado) {
                JOptionPane.showMessageDialog(this,
                        "El visitante ya tiene el estado: " + nuevoEstado.toString(),
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String razonDeshabilitacion = null;

            if (nuevoEstado == EstadoVisitanteEnum.DESHABILITADO) {
                JTextArea textArea = new JTextArea(5, 20);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textArea);

                boolean entradaValida = false;

                while (!entradaValida) {
                    int opcion = JOptionPane.showConfirmDialog(this,
                            new Object[]{"Ingrese la razón de deshabilitación (obligatorio):", scrollPane},
                            "Razón de deshabilitación",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                        return; 
                    }

                    razonDeshabilitacion = textArea.getText().trim();

                    if (razonDeshabilitacion.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Debe ingresar una razón para deshabilitar al visitante.",
                                "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
                    } else {
                        entradaValida = true;
                    }
                }
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de cambiar el estado del visitante a " + nuevoEstado + "?",
                    "Confirmar cambio",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Visitante visitanteActualizado = controller.cambiarEstadoVisitante(
                    visitante.getIdentificacion(),
                    nuevoEstado,
                    razonDeshabilitacion,
                    identificacionPreso,
                    tablaVisitantes
            );

            if (visitanteActualizado != null) {
                visitante = visitanteActualizado;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al actualizar el estado del visitante",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar estado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BotonModificarEstadoVisitanteActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CambioEstadoVisitante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonModificarEstadoVisitante;
    private javax.swing.JComboBox<String> JcomboNuevoEstadoVisitante;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
