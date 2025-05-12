package Controller;

import DAO.CitaMedicaDAO;
import DAO.EnfermeraDAO;
import DAO.GuardiaDAO;
import DAO.PresoDAO;
import Model.Entities.CitaMedica;
import Model.Entities.Enfermera;
import Model.Entities.Guardia;
import Model.Entities.Preso;
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

public class CitaMedicaController {

    private final CitaMedicaDAO citaMedicaDAO = new CitaMedicaDAO();
    private final PresoDAO presoDAO = new PresoDAO();
    private final GuardiaDAO guardiaDAO = new GuardiaDAO();
    private final EnfermeraDAO enfermeraDAO = new EnfermeraDAO();

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarCamposCita(Oficial view) {
        view.getIdentificacionPresoCita().setText("");
        view.getIdentificacionGuardia().setText("");
        view.getMotivoCita().setText("");
        view.getFechaCita().setDate(null);
        view.getComboHoraCita().setSelectedIndex(0);
    }

    public boolean agendarCita(Oficial view) {
        if (!validarCamposCita(view)) {
            return false;
        }

        String identificacionPreso = view.getIdentificacionPresoCita().getText().trim();
        String identificacionGuardia = view.getIdentificacionGuardia().getText().trim();
        String motivoCita = view.getMotivoCita().getText().trim();
        Date fechaSeleccionada = view.getFechaCita().getDate();
        String horaSeleccionada = view.getComboHoraCita().getSelectedItem().toString();

        if (!identificacionPreso.matches("\\d+")) {
            mostrarError("La identificación del preso debe contener solo números");
            return false;
        }

        if (!identificacionPreso.matches("\\d{6,10}")) {
            mostrarError("La identificación debe contener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        if (!identificacionGuardia.matches("\\d+")) {
            mostrarError("La identificación del preso debe contener solo números");
            return false;
        }

        if (!identificacionGuardia.matches("\\d{6,10}")) {
            mostrarError("La identificación debe contener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora;

        try {
            hora = LocalTime.parse(horaSeleccionada);
        } catch (Exception e) {
            mostrarError("Formato de hora inválido");
            return false;
        }

        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la cita no puede ser en el pasado");
            return false;
        }

        Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
        if (preso == null) {
            mostrarError("No se encontró ningún preso con esa identificación");
            return false;
        }

        Guardia guardia = guardiaDAO.obtenerGuardiaPorCedula(identificacionGuardia);
        if (guardia == null) {
            mostrarError("No se encontró ningún guardia con esa identificación");
            return false;
        }

        CitaMedica citaExistente = citaMedicaDAO.buscarCitaPorPresoFechaYHora(identificacionPreso, fecha, hora);
        if (citaExistente != null) {
            mostrarError("Ya existe una cita para este preso en la misma fecha y hora");
            return false;
        }

        String turno = determinarTurno(hora);
        if (turno == null) {
            mostrarError("La hora seleccionada no corresponde a un turno válido (8:00-16:00 o 16:20-20:00)");
            return false;
        }

        if (!guardia.getTurno().equalsIgnoreCase(turno)) {
            mostrarError("El guardia no está disponible en el turno " + turno);
            return false;
        }

        Enfermera enfermera = asignarEnfermera(turno);
        if (enfermera == null) {
            mostrarError("No hay enfermeras disponibles para el turno " + turno);
            return false;
        }

        CitaMedica nuevaCita = new CitaMedica(0, fecha, hora, motivoCita, guardia, preso, enfermera);
        boolean exito = citaMedicaDAO.guardarCita(nuevaCita);

        if (exito) {
            mostrarExito("Cita agendada exitosamente");
            limpiarCamposCita(view);
            return true;
        } else {
            mostrarError("Error al guardar la cita en la base de datos");
            return false;
        }
    }

    private boolean validarCamposCita(Oficial view) {
        if (view.getIdentificacionPresoCita().getText().trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del preso");
            return false;
        }

        if (view.getIdentificacionGuardia().getText().trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del guardia");
            return false;
        }

        if (view.getMotivoCita().getText().trim().isEmpty()) {
            mostrarError("Debe especificar el motivo de la cita");
            return false;
        }

        if (view.getFechaCita().getDate() == null) {
            mostrarError("Debe seleccionar una fecha para la cita");
            return false;
        }

        if (view.getComboHoraCita().getSelectedItem() == null
                || view.getComboHoraCita().getSelectedItem().toString().equals("< Seleccionar >")) {
            mostrarError("Debe seleccionar una hora para la cita");
            return false;
        }

        return true;
    }

    private String determinarTurno(LocalTime hora) {
        if (!hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(16, 0))) {
            return "Diurno";
        } else if (!hora.isBefore(LocalTime.of(16, 20)) && !hora.isAfter(LocalTime.of(20, 0))) {
            return "Nocturno";
        }
        return null;
    }

    private Enfermera asignarEnfermera(String turno) {
        List<Enfermera> enfermeras = enfermeraDAO.obtenerEnfermeras();
        for (Enfermera enf : enfermeras) {
            if (enf.getTurno() != null && enf.getTurno().equalsIgnoreCase(turno)) {
                return enf;
            }
        }
        return null;
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
}
