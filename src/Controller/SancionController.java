package Controller;

import DAO.GuardiaDAO;
import DAO.PresoDAO;
import DAO.SancionDAO;
import DAO.VisitaDAO;
import Model.Constants.EstadoVisitaEnum;
import Model.Entities.Guardia;
import Model.Entities.Preso;
import Model.Entities.Sancion;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import Utilidades.EmailSender;
import View.Oficial;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SancionController {

    private SancionDAO sancionDAO = SancionDAO.getInstancia();
    private PresoDAO presoDAO = PresoDAO.getInstancia();
    private VisitaDAO visitaDAO = VisitaDAO.getInstancia();
    private GuardiaDAO guardiaDAO = GuardiaDAO.getInstancia();

    public SancionController() {

    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean registrarSancion(Oficial view) {
        if (!validarCamposSancion(view)) {
            return false;
        }

        int opcion = JOptionPane.showConfirmDialog(
                view,
                "¿Está seguro de registrar esta sanción?",
                "Confirmar Registro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion != JOptionPane.YES_OPTION) {
            limpiarCamposSancion(view);
            return false;
        }

        String identificacionPreso = view.getIdentificacionPresoSancion().getText().trim();
        String motivoSancion = view.getMotivoSancion().getText().trim();
        String tipoSancion = view.getTipoSancion().getSelectedItem().toString();
        Date fechaSeleccionada = view.getFechaSancion().getDate();
        String horaStr = view.getHoraSancion().getSelectedItem().toString();
        String identificacionGuardia = view.getIdentificacionGuardiaSancion().getText().trim();

        if (!identificacionPreso.matches("\\d{6,10}") || !identificacionGuardia.matches("\\d{6,10}")) {
            mostrarError("Las identificaciones deben tener entre 6 y 10 dígitos.");
            return false;
        }

        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora = LocalTime.parse(horaStr);

        if (!fecha.equals(LocalDate.now())) {
            mostrarError("Las sanciones solo pueden registrarse el día actual (" + LocalDate.now() + ")");
            return false;
        }

        if (hora.isAfter(LocalTime.now())) {
            mostrarError("No puede registrar sanciones con hora futura.");
            return false;
        }

        try {
            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                mostrarError("Preso no encontrado.");
                return false;
            }

            Guardia guardia = new GuardiaDAO().obtenerGuardiaPorCedula(identificacionGuardia);
            if (guardia == null) {
                mostrarError("Guardia no encontrado.");
                return false;
            }

            String turnoSancion = determinarTurno(hora);
            if (turnoSancion == null || !guardia.getTurno().equalsIgnoreCase(turnoSancion)) {
                mostrarError("El guardia no estaba en turno a esta hora.");
                return false;
            }

            List<Sancion> sancionesExistentes = sancionDAO.obtenerSancionesPorPresoFechaYHora(identificacionPreso, fecha, hora);
            if (!sancionesExistentes.isEmpty()) {
                mostrarError("Ya existe una sanción registrada para este preso en la misma fecha y hora");
                return false;
            }

            Sancion nuevaSancion = new Sancion(0, motivoSancion, fecha, hora, tipoSancion, preso, guardia);
            if (sancionDAO.guardarSancion(nuevaSancion)) {
                if (nuevaSancion.esAislamiento()) {
                    preso.setEnAislamiento(true);
                    presoDAO.actualizarPreso(preso);
                }

                cancelarVisitasPendientes(identificacionPreso, fecha, nuevaSancion.getDiasDuracion());
                mostrarExito("Sanción registrada exitosamente. Duración: " + nuevaSancion.getDiasDuracion() + " días");
                limpiarCamposSancion(view);
                return true;
            } else {
                mostrarError("Error al guardar la sanción.");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
            return false;
        }
    }

    private void cancelarVisitasPendientes(String identificacionPreso, LocalDate fechaSancion, int diasDuracion) {
        List<Visita> visitas = visitaDAO.cargarPorIdentificacionPreso(identificacionPreso);
        int visitasCanceladas = 0;

        String motivoCancelacion = (diasDuracion == 10)
                ? "Preso en aislamiento por " + diasDuracion + " días"
                : "Sanción aplicada al preso (Duración: " + diasDuracion + " días)";

        for (Visita visita : visitas) {
            LocalDate fechaVisita = visita.getFechaVisita();
            LocalDate fechaFinSancion = fechaSancion.plusDays(diasDuracion);

            if ((!fechaVisita.isBefore(fechaSancion) && !fechaVisita.isAfter(fechaFinSancion)
                    && visita.getEstado() == EstadoVisitaEnum.EN_PROCESO)) {

                visitaDAO.modificarEstadoVisitaYDevolver(visita.getId(), EstadoVisitaEnum.CANCELADA);
                visitasCanceladas++;

                notificarCancelacionAVisitantes(visita, motivoCancelacion);
            }
        }

        if (visitasCanceladas > 0) {
            mostrarExito("Se cancelaron " + visitasCanceladas + " visitas programadas. "
                    + (diasDuracion == 10 ? "El preso estará en aislamiento." : ""));
        }
    }

    private void notificarCancelacionAVisitantes(Visita visita, String motivo) {
        try {
            List<Visitante> visitantes = new ArrayList<>(visita.getVisitantesConRelacion().keySet());

            for (Visitante visitante : visitantes) {
                if (visitante.getEdad() >= 18
                        && visitante.getEmail() != null
                        && !visitante.getEmail().isEmpty()) {

                    boolean correoEnviado = EmailSender.getInstancia().enviarNotificacionCancelacion(
                            visitante,
                            visita,
                            motivo
                    );

                    if (!correoEnviado) {
                        System.err.println("Error al enviar notificación a: " + visitante.getEmail());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en notificación de cancelación: " + e.getMessage());
        }
    }

    private String determinarTurno(LocalTime hora) {
        if (!hora.isBefore(LocalTime.of(0, 0)) && hora.isBefore(LocalTime.of(8, 0))) {
            return "Nocturno";
        } else if (!hora.isBefore(LocalTime.of(8, 0)) && hora.isBefore(LocalTime.of(20, 0))) {
            return "Diurno";
        } else if (!hora.isBefore(LocalTime.of(20, 0)) && !hora.isAfter(LocalTime.of(23, 59, 59))) {
            return "Nocturno";
        }
        return null;
    }

    private boolean validarCamposSancion(Oficial view) {
        if (view.getIdentificacionPresoSancion().getText().trim().isEmpty()) {
            mostrarError("La identificación del preso es obligatoria.");
            return false;
        }

        if (view.getMotivoSancion().getText().trim().isEmpty()) {
            mostrarError("El motivo de la sanción es obligatorio.");
            return false;
        }

        if (view.getTipoSancion().getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar un tipo de sanción.");
            return false;
        }

        if (view.getFechaSancion().getDate() == null) {
            mostrarError("Debe seleccionar una fecha.");
            return false;
        }

        if (view.getHoraSancion().getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar una hora.");
            return false;
        }

        if (view.getIdentificacionGuardiaSancion().getText().trim().isEmpty()) {
            mostrarError("La identificación del guardia es obligatoria.");
            return false;
        }

        if (view.getMotivoSancion().getText().trim().length() < 10) {
            mostrarError("El motivo debe tener al menos 10 caracteres");
            return false;
        }

        Date fechaSeleccionada = view.getFechaSancion().getDate();
        LocalDate fechaSeleccionadaLD = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (fechaSeleccionadaLD.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la sanción no puede ser en el pasado.");
            return false;
        }

        return true;
    }

    public void limpiarCamposSancion(Oficial view) {
        view.getIdentificacionPresoSancion().setText("");
        view.getMotivoSancion().setText("");
        view.getTipoSancion().setSelectedIndex(0);
        view.getFechaSancion().setDate(null);
        view.getHoraSancion().setSelectedIndex(0);
        view.getIdentificacionGuardiaSancion().setText("");
    }

    public void cargarHistorialSanciones(String identificacionPreso, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = sancionDAO.cargarPorIdentificacionPreso(identificacionPreso);
        for (Sancion sancion : sanciones) {
            int duracionAcumulada = sancionDAO.obtenerDuracionAcumuladaPorTipo(
                    identificacionPreso,
                    sancion.getTipoSancion()
            );

            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDiasDuracion() + " días",
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion(),
                duracionAcumulada + " días"
            });
        }
    }

    public void filtrarSancionesPorTipo(String identificacionPreso, String tipoSancion, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = tipoSancion.equals("< Seleccionar >")
                ? sancionDAO.cargarPorIdentificacionPreso(identificacionPreso)
                : sancionDAO.cargarPorTipoYIdentificacionPreso(tipoSancion, identificacionPreso);

        for (Sancion sancion : sanciones) {
            int duracionAcumulada = sancionDAO.obtenerDuracionAcumuladaPorTipo(
                    identificacionPreso,
                    sancion.getTipoSancion()
            );

            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDiasDuracion() + " días",
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion(),
                duracionAcumulada + " días"
            });
        }
    }

    public void cargarTodosLosPresos(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = cargarImagen(preso.getFotoPath());
            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getSexo(),
                preso.getNacionalidad(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()
            });
        }
    }

    public void buscarPresoPorIdentificacion(String identificacion, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        if (identificacion.isEmpty()) {
            mostrarError("Ingrese una identificación para buscar");
            return;
        }

        Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);
        if (preso != null) {
            ImageIcon foto = cargarImagen(preso.getFotoPath());
            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getSexo(),
                preso.getNacionalidad(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()
            });
        } else {
            mostrarError("No se encontró ningún preso con esa identificación");
        }
    }

    public File seleccionarImagen(Component parent, JLabel vistaPrevia) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int resultado = fileChooser.showOpenDialog(parent);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File imagenSeleccionada = fileChooser.getSelectedFile();
            try {
                BufferedImage originalImage = ImageIO.read(imagenSeleccionada);
                Image imgEscalada = originalImage.getScaledInstance(
                        vistaPrevia.getWidth(),
                        vistaPrevia.getHeight(),
                        Image.SCALE_SMOOTH);
                vistaPrevia.setIcon(new ImageIcon(imgEscalada));
                return imagenSeleccionada;
            } catch (IOException ex) {
                mostrarError("Error al cargar la imagen: " + ex.getMessage());
                return null;
            }
        }
        return null;
    }

    public void configurarTablaImagenes(JTable tabla) {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (column == 0 && value instanceof ImageIcon) {
                    ImageIcon originalIcon = (ImageIcon) value;
                    Image img = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    ImageIcon roundedIcon = new ImageIcon(createRoundedImage(img));
                    label.setIcon(roundedIcon);
                    label.setText("");
                } else {
                    label.setIcon(null);
                }
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        tabla.setRowHeight(65);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
    }

    private Image createRoundedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        output = g2.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2.dispose();
        g2 = output.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, width, height, 20, 20);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }

    public void cargarTodosLosGuardias(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Guardia> guardias = guardiaDAO.obtenerGuardias();

        for (Guardia guardia : guardias) {
            ImageIcon foto = cargarImagen(guardia.getRutaImagen());

            modelo.addRow(new Object[]{
                foto,
                guardia.getNombresCompletos(),
                guardia.getApellidosCompletos(),
                guardia.getEdad(),
                guardia.getIdentificacion(),
                guardia.getSexo(),
                guardia.getNacionalidad(),
                guardia.getCorreo(),
                guardia.getTurno(),
                guardia.getCargo(),
                guardia.getFechaInicioContrato(),
                guardia.getFechaFinContrato()
            });
        }
    }

    public void cargarDatosPresoEnTablaPorSeccion(String seccionFiltrada, JTable tablaPresos) {
        DefaultTableModel modelo = (DefaultTableModel) tablaPresos.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            if (preso.getSeccionAsignada() != null && preso.getSeccionAsignada().equalsIgnoreCase(seccionFiltrada)) {
                ImageIcon foto = null;
                if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                    foto = cargarImagen(preso.getFotoPath());
                }

                modelo.addRow(new Object[]{
                    foto,
                    preso.getId(),
                    preso.getNombresCompletos(),
                    preso.getApellidosCompletos(),
                    preso.getEdad(),
                    preso.getIdentificacion(),
                    preso.getSexo(),
                    preso.getNacionalidad(),
                    preso.getCeldaAsignada(),
                    preso.getSeccionAsignada()
                });
            }
        }

        tablaPresos.revalidate();
        tablaPresos.repaint();
    }

    public void buscarGuardiaPorIdentificacion(String identificacion, JTable tabla) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una identificación", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Guardia> guardias = guardiaDAO.obtenerGuardias();
        boolean encontrado = false;

        for (Guardia guardia : guardias) {
            if (guardia.getIdentificacion().equalsIgnoreCase(identificacion)) {
                ImageIcon foto = cargarImagen(guardia.getRutaImagen());

                modelo.addRow(new Object[]{
                    foto,
                    guardia.getNombresCompletos(),
                    guardia.getApellidosCompletos(),
                    guardia.getEdad(),
                    guardia.getIdentificacion(),
                    guardia.getSexo(),
                    guardia.getNacionalidad(),
                    guardia.getCorreo(),
                    guardia.getTurno(),
                    guardia.getCargo(),
                    guardia.getFechaInicioContrato(),
                    guardia.getFechaFinContrato()
                });
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró ningún guardia con la identificación " + identificacion,
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private ImageIcon cargarImagen(String path) {
        File imgFile = new File(path);
        if (!imgFile.exists()) {
            return null;
        }
        try {
            Image img = ImageIO.read(imgFile);
            return new ImageIcon(img.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            return null;
        }
    }
}
