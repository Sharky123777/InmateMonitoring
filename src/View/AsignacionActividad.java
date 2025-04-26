package View;

import Controller.ActividadController;
import Model.Preso;
import java.awt.Dialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AsignacionActividad extends javax.swing.JDialog {

    private Preso preso;

    private final ActividadController controlador;

    public AsignacionActividad(java.awt.Frame parent, boolean modal, Preso preso) {
        super(parent, modal);
        this.preso = preso;
        this.controlador = ActividadController.getInstancia();
        initComponents();
        this.setLocationRelativeTo(null);
        controlador.cargarActividadesDisponiblesEnTabla(actividadesTablaParaAsignar, preso.getIdentificacion());

     presoNomApe.setText(preso.getNombresCompletos() + " " + preso.getApellidosCompletos());
identif.setText(preso.getIdentificacion());
    }
    

    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnAsignar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new RoundedPanel(20);
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        actividadesTablaParaAsignar = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        presoNomApe = new javax.swing.JLabel();
        identif = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("LISTA DE ACTIVIDADES DISPONIBLES");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, 280, 30));

        btnAsignar.setBackground(new java.awt.Color(24, 24, 50));
        btnAsignar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAsignar.setForeground(new java.awt.Color(255, 255, 255));
        btnAsignar.setText("Asignar ");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAsignar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 120, 150, 40));

        jButton1.setBackground(new java.awt.Color(51, 0, 0));
        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 70, 150, 40));

        jPanel2.setBackground(new java.awt.Color(32, 32, 53));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ASIGNACIÓN DE ACTIVIDAD");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, -1, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 40));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Preso: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 80, 30));

        actividadesTablaParaAsignar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Seleccionar", "id", "Nombre", "Tipo", "Día", "Horario", "Lugar", "Inscritos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        actividadesTablaParaAsignar.setRowHeight(50);
        jScrollPane1.setViewportView(actividadesTablaParaAsignar);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 930, 270));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 510, 10));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 510, 10));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Identificación:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 130, 30));

        presoNomApe.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        presoNomApe.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(presoNomApe, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 440, 30));

        identif.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        identif.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(identif, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 410, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1040, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        DefaultTableModel model = (DefaultTableModel) actividadesTablaParaAsignar.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0)) {
                String idActividad = (String) model.getValueAt(i, 1);

                if (controlador.asignarPresoAActividad(idActividad, preso.getIdentificacion())) {
                    actualizarTablasDespuesDeAsignacion();
                    this.dispose();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Seleccione una actividad", "Error", JOptionPane.ERROR_MESSAGE);

    }//GEN-LAST:event_btnAsignarActionPerformed

    private void actualizarTablasDespuesDeAsignacion() {

        CoordinadorDeActividades framePrincipal = (CoordinadorDeActividades) SwingUtilities.getWindowAncestor(this);

        controlador.cargarActividadesEnTabla(framePrincipal.getActividadesTabla());

        controlador.cargarActividadesPresoEnTabla(framePrincipal.getActividadesPresoTabla(), preso.getIdentificacion());
    }
    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JOptionPane.showMessageDialog(null, "Se canceló la asignacion de actividad para el preso " + this.preso.getApellidosCompletos());
        dispose();

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable actividadesTablaParaAsignar;
    private javax.swing.JButton btnAsignar;
    private javax.swing.JLabel identif;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel presoNomApe;
    // End of variables declaration//GEN-END:variables
}
