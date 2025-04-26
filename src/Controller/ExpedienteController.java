package Controller;

import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Preso;
import Model.Delito;
import Model.ExpedienteJudicial;
import Model.Sentencia;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ExpedienteController {
    
    private static volatile ExpedienteController instancia;
    private final PresoDAO presoDAO;
    private final DelitoDAO delitoDAO;
    private final PresoController presoController;
    
    private ExpedienteController(PresoDAO presoDAO, DelitoDAO delitoDAO, PresoController presoController) {
        this.presoDAO = presoDAO;
        this.delitoDAO = delitoDAO;
        this.presoController = presoController;
    }
    
    public static ExpedienteController getInstancia() {
        ExpedienteController result = instancia;
        if (result == null) {
            synchronized (ExpedienteController.class) {
                result = instancia;
                if (result == null) {
                    instancia = result = new ExpedienteController(
                        PresoDAO.getInstancia(),
                        DelitoDAO.getInstancia(),
                        PresoController.getInstancia()
                    );
                }
            }
        }
        return result;
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
                                        JTable tablaExpediente,
                                        JLabel SentenciaTotal) {
        
        if (preso == null) {
            throw new IllegalArgumentException("Preso no puede ser nulo");
        }

        List<Delito> delitosActualizados = delitoDAO.obtenerDelitosPorPreso(preso.getIdentificacion());
        if (preso.getExpediente() == null) {
            ExpedienteJudicial expediente = new ExpedienteJudicial();
            expediente.setDelitos(delitosActualizados);
            preso.setExpediente(expediente);
        } else {
            preso.getExpediente().setDelitos(delitosActualizados);
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

        Sentencia sentenciaTotal = new Sentencia(0, 0, preso.getExpediente().getDelitos().get(0).getSentencia().getFechaIngreso());
        for (Delito delito : preso.getExpediente().getDelitos()) {
            sentenciaTotal.sumarSentencia(delito.getSentencia());
        }

        SentenciaTotal.setText(sentenciaTotal.getSentenciaFormateada());

        presoController.cargarTablaDelitos(preso, tablaExpediente);
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
    
    public String obtenerDescripcionDelito(int idDelito) {
        return delitoDAO.obtenerDescripcionDelito(idDelito);
    }
    

}