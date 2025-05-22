
package View;

import Controller.PresaController;
import Model.Entities.IntentoFuga;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class IntentosFuga extends javax.swing.JDialog {
    private final PresaController presoController;

    private String identificacionPreso;
    private DefaultTableModel modeloTabla;
   
    public IntentosFuga(java.awt.Frame parent, boolean modal, PresaController controller, String identificacion) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.presoController = controller;
        this.identificacionPreso = identificacion;
        initTabla();
        cargarIntentos(identificacion);
        setLocationRelativeTo(parent);
    }
    
    
    private void initTabla() {
        String[] columnas = {"Fecha Fuga", "Fecha Reingreso", "Duración"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? String.class : LocalDate.class;
            }
        };
        
        tablaIntentosFuga.setModel(model);
        
        tablaIntentosFuga.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            @Override
            protected void setValue(Object value) {
                setText(value == null ? "EN FUGA" : fmt.format((LocalDate)value));
                setHorizontalAlignment(CENTER);
            }
        });
    }
    
    
    private void cargarIntentos(String identificacion) {
        DefaultTableModel model = (DefaultTableModel) tablaIntentosFuga.getModel();
        model.setRowCount(0);
        
        List<IntentoFuga> intentos = presoController.obtenerIntentosFuga(identificacion);
        
        lblIntentosDeFuga.setText(""+intentos.size());

        
        for (IntentoFuga intento : intentos) {
            String duracion = calcularDuracion(intento.getFechaFuga(), intento.getFechaReingreso());
            model.addRow(new Object[]{
                intento.getFechaFuga(),
                intento.getFechaReingreso(),
                duracion
            });
        }
    }
    
    private String calcularDuracion(LocalDate fuga, LocalDate reingreso) {
        if (reingreso == null) {
            long dias = ChronoUnit.DAYS.between(fuga, LocalDate.now());
            return dias + " días (en curso)";
        }
        long dias = ChronoUnit.DAYS.between(fuga, reingreso);
        return dias + " días";
    }
    
    
     public static void mostrarDialog(JFrame parent, PresaController controller, String identificacion) {
        IntentosFuga dialog = new IntentosFuga(parent, true, controller, identificacion);
        dialog.setVisible(true);
    }
    

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaIntentosFuga = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new RoundedPanel(30);
        ;
        jLabel1 = new javax.swing.JLabel();
        lblIntentosDeFuga = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaIntentosFuga.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        tablaIntentosFuga.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha de fuga", "Fecha de reingreso", "Duración"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaIntentosFuga);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, 270));

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 400, 110, 30));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Intentos de fuga: ");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 17, 150, 20));

        lblIntentosDeFuga.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblIntentosDeFuga.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(lblIntentosDeFuga, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 210, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 440, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 450));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.dispose();

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
            java.util.logging.Logger.getLogger(IntentosFuga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntentosFuga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntentosFuga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntentosFuga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

     
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblIntentosDeFuga;
    private javax.swing.JTable tablaIntentosFuga;
    // End of variables declaration//GEN-END:variables
}
