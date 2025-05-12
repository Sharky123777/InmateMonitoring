package Controller;

import DAO.DelitoDAO;
import DAO.ExpedienteDAO;
import DAO.IntentoFugaDAO;
import DAO.PresoDAO;
import Model.Constants.EstadoExpedienteEnum;
import Model.Constants.EstadoPresoEnum;
import Model.Entities.Preso;
import Model.Entities.Delito;
import Model.Entities.ExpedienteJudicial;
import Model.Entities.GeneradorExpedientePDF;
import Model.Entities.IntentoFuga;
import Model.Entities.Sentencia;
import Utilidades.Validador;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class ExpedienteController {

    private static ExpedienteController instancia;
    private final IntentoFugaDAO intentoFugaDAO = IntentoFugaDAO.getInstancia();
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

    public void cargarExpedienteCompleto(
            String identificacionPreso,
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
            JLabel sentenciaTotalLabel,
            JPanel panelEstadoEspecialPreso,
            JLabel lblMensajeEspecialPreso
    ) {

        try {
            if (identificacionPreso == null || identificacionPreso.isEmpty()) {
                throw new IllegalArgumentException("Identificación del preso no puede ser nula o vacía");
            }

            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                throw new IllegalStateException("No se encontró preso con identificación: " + identificacionPreso);
            }

            List<ExpedienteJudicial> expedientes = expedienteDAO.buscarExpedientesPorPreso(identificacionPreso);

            ExpedienteJudicial expediente = expedientes.stream()
                    .filter(e -> e.getEstado() == EstadoExpedienteEnum.ABIERTO)
                    .sorted((a, b) -> b.getFechaApertura().compareTo(a.getFechaApertura()))
                    .findFirst()
                    .orElse(null);

            if (expediente == null) {
                expediente = new ExpedienteJudicial(preso);
                expedienteDAO.guardarExpediente(expediente);
            }
            List<Delito> delitosDelExpediente = expediente.getDelitos();
            if (delitosDelExpediente == null || delitosDelExpediente.isEmpty()) {
                delitosDelExpediente = new ArrayList<>();
            }

            cargarDatosPresoUI(
                    preso,
                    nombre,
                    apellidos,
                    edad,
                    identificacion,
                    nacionalidad,
                    fotoLabel,
                    estado,
                    panelEstadoEspecialPreso,
                    lblMensajeEspecialPreso
            );

            cargarDatosExpedienteUI(expediente, registroNum, codExpe, fechaAper, estado, juzgado, nivelRiesgo);

            Sentencia sentenciaTotal = expedienteDAO.calcularSentenciaTotal(delitosDelExpediente);

            sentenciaTotalLabel.setText(sentenciaTotal.getSentenciaFormateada());

            if (sentenciaTotal.getFechaSalidaCalculada() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fechaSalida.setText(sentenciaTotal.getFechaSalidaCalculada().format(formatter));
            } else {
                fechaSalida.setText("No disponible");
            }

            cargarTablaDelitosUI(delitosDelExpediente, tablaExpediente);

        } catch (IllegalArgumentException | IllegalStateException e) {
            Validador.mostrarError(e.getMessage());
        } catch (Exception e) {
            Validador.mostrarError("Error al cargar expediente: " + e.getMessage());
        }
    }

    private void cargarDatosPresoUI(Preso preso, JLabel nombre, JLabel apellidos,
            JLabel edad, JLabel identificacion,
            JLabel nacionalidad, JLabel fotoLabel,
            JLabel estadoPresoLabel, JPanel panelEstadoEspecialPreso, JLabel lblMensajeEspecialPreso) {

        nombre.setText(preso.getNombresCompletos());
        apellidos.setText(preso.getApellidosCompletos());
        edad.setText(String.valueOf(preso.getEdad()));
        identificacion.setText(preso.getIdentificacion());
        nacionalidad.setText(preso.getNacionalidad());
        cargarFotoPreso(preso.getFotoPath(), fotoLabel);

        EstadoPresoEnum estadoPreso = preso.getEstado();
        estadoPresoLabel.setText(estadoPreso.name());

        panelEstadoEspecialPreso.setVisible(false);

        switch (estadoPreso) {
            case FALLECIDO:
                estadoPresoLabel.setForeground(Color.RED);
                lblMensajeEspecialPreso.setText("EL PRESO MURIÓ SIN CUMPLIR LA CONDENA");
                lblMensajeEspecialPreso.setForeground(Color.RED);
                lblMensajeEspecialPreso.setFont(new Font("Arial", Font.BOLD, 13));
                panelEstadoEspecialPreso.setBackground(new Color(255, 204, 204));
                panelEstadoEspecialPreso.setVisible(true);
                break;

            case LIBERADO:
                estadoPresoLabel.setForeground(new Color(0, 102, 0));
                lblMensajeEspecialPreso.setText("EL PRESO FUE LIBERADO");
                lblMensajeEspecialPreso.setForeground(new Color(0, 102, 0));
                lblMensajeEspecialPreso.setFont(new Font("Arial", Font.BOLD, 13));
                panelEstadoEspecialPreso.setBackground(new Color(204, 255, 204));
                panelEstadoEspecialPreso.setVisible(true);
                break;

            default:
                estadoPresoLabel.setForeground(Color.BLACK);
                break;
        }
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

        List<ExpedienteJudicial> expedientes = expedienteDAO.buscarExpedientesPorPreso(identificacionPreso);
        ExpedienteJudicial expediente = expedientes.stream()
                .filter(e -> e.getEstado() == EstadoExpedienteEnum.ABIERTO)
                .findFirst()
                .orElse(null);

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

    public List<IntentoFuga> obtenerIntentosFuga(String identificacionPreso) throws IllegalStateException {
        try {
            return intentoFugaDAO.obtenerPorPreso(identificacionPreso);
        } catch (Exception e) {
            throw new IllegalStateException("Error al obtener intentos de fuga: " + e.getMessage());
        }
    }

    public void exportarExpedienteComoPDF(String identificacionPreso, String filePath) {
        try {
            if (identificacionPreso == null || identificacionPreso.isEmpty()) {
                throw new IllegalArgumentException("Identificación del preso no puede estar vacía");
            }

            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                throw new IllegalStateException("No se encontró preso con identificación: " + identificacionPreso);
            }

            List<ExpedienteJudicial> expedientes = expedienteDAO.buscarExpedientesPorPreso(identificacionPreso);
            ExpedienteJudicial expediente = expedientes.stream()
                    .filter(e -> e.getEstado() == EstadoExpedienteEnum.ABIERTO)
                    .findFirst()
                    .orElse(null);

            if (expediente == null) {
                expediente = new ExpedienteJudicial(preso);
                expedienteDAO.guardarExpediente(expediente);
            }

            List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(identificacionPreso);
            Sentencia sentenciaTotal = expedienteDAO.calcularSentenciaTotal(delitos);

            GeneradorExpedientePDF gn = new GeneradorExpedientePDF();
            gn.generarPDFExpediente(preso, expediente, delitos, sentenciaTotal, filePath);

            JOptionPane.showMessageDialog(null, "Expediente exportado correctamente\n" + filePath, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(filePath));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al exportar a PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    public List<ExpedienteJudicial> obtenerHistorialExpedientes(String identificacionPreso) {
    try {
        Validador.validarFormatoIdentificacion(identificacionPreso);
        
        List<ExpedienteJudicial> expedientes = expedienteDAO.buscarExpedientesPorPreso(identificacionPreso);
        
        if (expedientes == null || expedientes.isEmpty()) {
            throw new IllegalStateException("No se encontraron expedientes para este preso");
        }
        
        // Ordenar por fecha de apertura (más reciente primero)
        expedientes.sort((e1, e2) -> e2.getFechaApertura().compareTo(e1.getFechaApertura()));
        
        return expedientes;
    } catch (IllegalArgumentException | IllegalStateException e) {
        Validador.mostrarError(e.getMessage());
        return new ArrayList<>();
    } catch (Exception e) {
        Validador.mostrarError("Error al obtener historial: " + e.getMessage());
        return new ArrayList<>();
    }
}
    
    public ExpedienteJudicial obtenerExpedientePorCodigo(String codigoExpediente) {
    try {
        if (codigoExpediente == null || codigoExpediente.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de expediente no puede estar vacío");
        }
        
        return expedienteDAO.buscarPorCodigo(codigoExpediente);
    } catch (IllegalArgumentException e) {
        Validador.mostrarError(e.getMessage());
        return null;
    } catch (Exception e) {
        Validador.mostrarError("Error al obtener expediente: " + e.getMessage());
        return null;
    }
}

}
