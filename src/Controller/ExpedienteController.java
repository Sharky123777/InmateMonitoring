package Controller;

import DAO.DelitoDAO;
import DAO.ExpedienteDAO;
import DAO.PresoDAO;
import Model.Entities.Preso;
import Model.Entities.Delito;
import Model.Entities.ExpedienteJudicial;
import Model.Entities.Sentencia;
import Utilidades.Validador;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ExpedienteController {
    
    private static ExpedienteController instancia;
    private final ExpedienteDAO expedienteDAO = ExpedienteDAO.getInstancia();
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
    
  public void cargarExpedienteCompleto(String identificacionPreso,
        JLabel fechaSalida,
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
        JLabel sentenciaTotalLabel) {

    try {
        if (identificacionPreso == null || identificacionPreso.isEmpty()) {
            throw new IllegalArgumentException("Identificación del preso no puede ser nula o vacía");
        }
        
        Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
        if (preso == null) {
            throw new IllegalStateException("No se encontró preso con identificación: " + identificacionPreso);
        }
        
        ExpedienteJudicial expediente = expedienteDAO.buscarPorPreso(identificacionPreso);
        if (expediente == null) {
            expediente = new ExpedienteJudicial(preso);
            expedienteDAO.guardarExpediente(expediente);
        }
        
        List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(identificacionPreso);
        expediente.setDelitos(delitos);
        expedienteDAO.actualizarExpediente(expediente);
        
        cargarDatosPresoUI(preso, nombre, apellidos, edad, identificacion, nacionalidad, fotoLabel);         
        cargarDatosExpedienteUI(expediente, registroNum, codExpe, fechaAper, estado, juzgado, nivelRiesgo);
        
        Sentencia sentenciaTotal = expedienteDAO.calcularSentenciaTotal(expediente.getDelitos());
        sentenciaTotalLabel.setText(sentenciaTotal.getSentenciaFormateada());
        
        if (sentenciaTotal.getFechaSalidaCalculada() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaSalida.setText(sentenciaTotal.getFechaSalidaCalculada().format(formatter));
        } else {
            fechaSalida.setText("No disponible");
        }
        
        cargarTablaDelitosUI(expediente.getDelitos(), tablaExpediente);
        
    } catch (IllegalArgumentException | IllegalStateException e) {
        Validador.mostrarError(e.getMessage());
    } catch (Exception e) {
        Validador.mostrarError("Error al cargar expediente: " + e.getMessage());
    }
}
    
    private void cargarDatosPresoUI(Preso preso, JLabel nombre, JLabel apellidos,
            JLabel edad, JLabel identificacion,
            JLabel nacionalidad, JLabel fotoLabel) {
        nombre.setText(preso.getNombresCompletos());
        apellidos.setText(preso.getApellidosCompletos());
        edad.setText(String.valueOf(preso.getEdad()));
        identificacion.setText(preso.getIdentificacion());
        nacionalidad.setText(preso.getNacionalidad());
        cargarFotoPreso(preso.getFotoPath(), fotoLabel);
    }
    
    private void cargarDatosExpedienteUI(ExpedienteJudicial expediente,
            JLabel registroNum, JLabel codExpe,
            JLabel fechaAper, JLabel estado,
            JLabel juzgado, JLabel nivelRiesgo) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        registroNum.setText(expediente.getNumeroRegistro());
        codExpe.setText(expediente.getCodigoExpediente());
        fechaAper.setText(expediente.getFechaApertura().format(dateFormatter));
        estado.setText(expediente.getEstado().toString());
        juzgado.setText(expediente.getJuzgado());
        nivelRiesgo.setText(expediente.getNivelRiesgo());
    }
    
private void cargarTablaDelitosUI(List<Delito> delitos, JTable tabla) {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    model.setRowCount(0);
    
    if (delitos == null || delitos.isEmpty()) {
        model.addRow(new Object[]{"No hay delitos registrados"});
        return;
    }
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    for (Delito delito : delitos) {
        model.addRow(new Object[]{
            delito.getNombre(),
            delito.getId(),
            delito.getSentencia().getFechaIngreso().format(formatter),
            delito.getSentencia().getSentenciaFormateada(),
            delito.getGravedad(),
            delito.getFechaComision().format(formatter)
        });
    }
}    
    private void cargarFotoPreso(String fotoPath, JLabel fotoLabel) {
        try {
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
        } catch (Exception e) {
            fotoLabel.setIcon(null);
            System.err.println("Error al cargar foto: " + e.getMessage());
        }
    }
    
    public String obtenerDescripcionDelito(int idDelito) {
        return delitoDAO.obtenerDescripcionDelito(idDelito);
    }
    
    public ExpedienteJudicial actualizarExpedienteConDelitos(String identificacionPreso) {
        List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(identificacionPreso);
        
        ExpedienteJudicial expediente = expedienteDAO.buscarPorPreso(identificacionPreso);
        if (expediente == null) {
            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            expediente = new ExpedienteJudicial(preso);
        }
        
        expediente.setDelitos(delitos);
        expedienteDAO.guardarExpediente(expediente);
        
        return expediente;
    }
    
    public void cargarTablaDelitos(List<Delito> delitos, JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        
        if (delitos == null || delitos.isEmpty()) {
            model.addRow(new Object[]{"No hay delitos registrados"});
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaSalida = expedienteDAO.calcularSentenciaTotal(delitos).getFechaSalidaCalculada();
        
        for (Delito delito : delitos) {
            model.addRow(new Object[]{
                delito.getNombre(),
                delito.getId(),
                delito.getSentencia().getFechaIngreso().format(formatter),
                delito.getSentencia().getSentenciaFormateada(),
                delito.getGravedad(),
                delito.getFechaComision().format(formatter),
                fechaSalida.format(formatter)
            });
        }
    }
    
}
