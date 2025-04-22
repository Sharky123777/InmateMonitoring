package Controller;

import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Preso;
import Model.Delito;
import Model.ExpedienteJudicial;
import Model.Sentencia;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.awt.Image;

public class ExpedienteController {
    
    private final PresoDAO presoDAO;
    private final DelitoDAO delitoDAO;

    public ExpedienteController(PresoDAO presoDAO, DelitoDAO delitoDAO) {
        this.presoDAO = presoDAO;
        this.delitoDAO = delitoDAO;
    }
    
    public ExpedienteController() {
        this.presoDAO = new PresoDAO();
        this.delitoDAO = new DelitoDAO();
    }
    
    public void cargarExpedienteCompleto(Preso preso, 
                                        JLabel registroNum, 
                                        JLabel codExpe,
                                        JLabel fechaAper,
                                        JLabel estado,
                                        JLabel juzgado,
                                        JLabel nivelRiesgo,
                                        JLabel nombre,
                                        JLabel apellidos,
                                        JLabel edad,
                                        JLabel identificacion,
                                        JLabel nacionalidad,
                                        JLabel fotoLabel,
                                        JTable tablaExpediente) {
        
        if (preso == null || preso.getExpediente() == null) {
            throw new IllegalArgumentException("Preso o expediente no pueden ser nulos");
        }

        ExpedienteJudicial expediente = preso.getExpediente();
        
        registroNum.setText(expediente.getNumeroRegistro());
        codExpe.setText(expediente.getCodigoExpediente());
        fechaAper.setText(expediente.getFechaApertura().toString());
        estado.setText(expediente.getEstado());
        juzgado.setText(expediente.getJuzgado());
        nivelRiesgo.setText(preso.getNivelDeRiesgo());

        nombre.setText(preso.getNombresCompletos());
        apellidos.setText(preso.getApellidosCompletos());
        edad.setText(String.valueOf(preso.getEdad()));
        identificacion.setText(preso.getIdentificacion());
        nacionalidad.setText(preso.getNacionalidad());

        cargarFotoPreso(preso.getFotoPath(), fotoLabel);

        cargarTablaDelitos(preso, tablaExpediente);
    }

    private void cargarFotoPreso(String fotoPath, JLabel fotoLabel) {
        if (fotoPath != null && !fotoPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(fotoPath);
            Image img = icon.getImage().getScaledInstance(
                fotoLabel.getWidth(),
                fotoLabel.getHeight(),
                Image.SCALE_SMOOTH
            );
            fotoLabel.setIcon(new ImageIcon(img));
        } else {
            fotoLabel.setIcon(null);
        }
    }

    private void cargarTablaDelitos(Preso preso, JTable tablaExpediente) {
        DefaultTableModel model = (DefaultTableModel) tablaExpediente.getModel();
        model.setRowCount(0);

        if (preso.getExpediente().getDelitos() != null) {
            for (Delito delito : preso.getExpediente().getDelitos()) {
                model.addRow(new Object[]{
                    delito.getNombre(),
                    delito.getId(),
                    preso.getSentencia() != null ? preso.getSentencia().getFechaIngreso() : "",
                    preso.getSentenciaFormateada(),
                    delito.getGravedad(),
                    delito.getFechaComision(),
                    preso.getSentencia() != null ? preso.getSentencia().getFechaSalidaCalculada() : ""
                });
            }
        }
    }
    
    public String obtenerDescripcionDelito(int idDelito, DelitoDAO delitoDAO) {
        Delito delito = delitoDAO.buscarDelitoPorId(idDelito);
        return delito != null ? delito.getDescripcion() : "Descripción no disponible";
    }
}