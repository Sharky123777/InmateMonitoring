package Controller;

import DAO.GuardiaDAO;
import Model.Entities.Guardia;
import View.FrmCamara;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public class GuardiaController {

    private static GuardiaController instancia;
    private final GuardiaDAO guardiaDAO;
    private FrmCamara ventanaCamara;

    private GuardiaController() {
        this.guardiaDAO = GuardiaDAO.getInstancia();
    }

    public static synchronized GuardiaController getInstancia() {
        if (instancia == null) {
            instancia = new GuardiaController();
        }
        return instancia;
    }

    public File capturarImagenGuardia() {
        FrmCamara ventanaCamara = new FrmCamara();
        ventanaCamara.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setContentPane(ventanaCamara.getContentPane());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        while (dialog.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return ventanaCamara.getImagenCapturada();
    }

    public Guardia registrarGuardia(
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            int edad, String cedula, String nacionalidad,
            String correo, String turno,
            LocalDate fechaFinContrato,
            String cargo,
            File imagen) throws IOException {

        try {

            // Validación de cédula única (agregar al inicio)
            if (guardiaDAO.existeGuardia(cedula)) {
                throw new IllegalArgumentException("Ya existe una guardia con la cédula " + cedula);
            }
            // Validaciones básicas
            validarCamposObligatorios(primerNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno, cargo);
            validarEdad(edad);
            validarImagen(imagen); // Validación obligatoria de imagen

            // Fecha de inicio siempre es hoy
            LocalDate fechaInicio = LocalDate.now();

            // Si fechaFinContrato es null, establecerla como hoy + 1 día (mínimo)
            if (fechaFinContrato == null) {
                fechaFinContrato = fechaInicio.plusDays(1);
            }

            // Validación robusta de fechas
            validarFechasContrato(fechaInicio, fechaFinContrato);

            // Crear y guardar el guardia
            Guardia nuevoGuardia = new Guardia(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno, fechaFinContrato, cargo);

            boolean guardado = guardiaDAO.guardarGuardia(nuevoGuardia, imagen);

            if (guardado) {
                return guardiaDAO.obtenerGuardiaPorCedula(cedula);
            }
            throw new RuntimeException("No se pudo guardar la guardia en la base de datos");

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al registrar la guardia: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    public boolean modificarGuardia(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {
            // Validar parámetros básicos
            if (cedulaOriginal == null || cedulaOriginal.trim().isEmpty()) {
                throw new IllegalArgumentException("La cédula original no puede estar vacía");
            }

            // Obtener guardia original
            Guardia original = guardiaDAO.obtenerGuardiaPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("No se encontró una guardia con la cédula: " + cedulaOriginal);
            }

            // Validar que se mantenga la imagen existente o se proporcione una nueva
            if (nuevaImagen == null && original.getRutaImagen() == null) {
                throw new IllegalArgumentException("Debe seleccionar una imagen de la guardia");
            }

            // Validar campos modificados
            validarCamposModificacion(cambios);

            // Validar edad si fue modificada
            if (cambios.containsKey("edad")) {
                validarEdad((int) cambios.get("edad"));
            }

            // Obtener fecha fin (modificada o original)
            LocalDate fechaFin = cambios.containsKey("fechaFin")
                    ? (LocalDate) cambios.get("fechaFin") : original.getFechaFinContrato();

            // Validación robusta de fechas
            if (fechaFin == null) {
                throw new IllegalArgumentException("La fecha de fin de contrato no puede estar vacía");
            }
            validarFechasContrato(original.getFechaInicioContrato(), fechaFin);

            // Validar imagen si fue modificada
            if (nuevaImagen != null) {
                validarImagen(nuevaImagen);
            }

            // Crear guardia modificado
            Guardia guardiaModificado = new Guardia(
                    (String) cambios.getOrDefault("primerNombre", original.getPrimerNombre()),
                    (String) cambios.getOrDefault("segundoNombre", original.getSegundoNombre()),
                    (String) cambios.getOrDefault("primerApellido", original.getPrimerApellido()),
                    (String) cambios.getOrDefault("segundoApellido", original.getSegundoApellido()),
                    (int) cambios.getOrDefault("edad", original.getEdad()),
                    cedulaOriginal, // Mantener la cédula original
                    (String) cambios.getOrDefault("nacionalidad", original.getNacionalidad()),
                    (String) cambios.getOrDefault("correo", original.getCorreo()),
                    (String) cambios.getOrDefault("turno", original.getTurno()),
                    fechaFin,
                    (String) cambios.getOrDefault("cargo", original.getCargo())
            );

            // Llamar al DAO para modificar
            return guardiaDAO.modificarGuardia(cedulaOriginal, guardiaModificado, nuevaImagen);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al modificar guardia: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void validarImagen(File imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Debe seleccionar una imagen de la guardia");
        }

        if (!imagen.exists()) {
            throw new IllegalArgumentException("La imagen seleccionada no existe en la ruta especificada");
        }

        // Validar extensión del archivo
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }

        // Validar tamaño mínimo (opcional)
        long sizeInBytes = imagen.length();
        long sizeInKB = sizeInBytes / 1024;
        if (sizeInKB < 10) { // 10KB como mínimo
            throw new IllegalArgumentException("La imagen es demasiado pequeña (mínimo 10KB)");
        }
    }

    private void validarFechasContrato(LocalDate inicio, LocalDate fin) {
        if (inicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (fin == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }

        // Validar que fin sea estrictamente posterior a inicio
        if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
            String mensaje = String.format(
                    "Fecha inválida:\nFin: %s\nInicio: %s\nLa fecha de fin debe ser posterior a la de inicio",
                    fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            throw new IllegalArgumentException(mensaje);
        }
    }

    // Métodos de consulta
    public List<Guardia> obtenerTodosGuardias() {
        return guardiaDAO.obtenerGuardias();
    }

    public Guardia obtenerGuardiaPorCedula(String cedula) {
        return guardiaDAO.obtenerGuardiaPorCedula(cedula);
    }

    public boolean eliminarGuardia(String cedula) {
        try {
            return guardiaDAO.eliminarGuardia(cedula);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar guardia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Métodos de validación mejorados
    private void validarCamposModificacion(Map<String, Object> cambios) {
        List<String> errores = new ArrayList<>();

        if (cambios.containsKey("primerNombre") && ((String) cambios.get("primerNombre")).trim().isEmpty()) {
            errores.add("Primer nombre es obligatorio");
        }
        if (cambios.containsKey("primerApellido") && ((String) cambios.get("primerApellido")).trim().isEmpty()) {
            errores.add("Primer apellido es obligatorio");
        }
        if (cambios.containsKey("segundoApellido") && ((String) cambios.get("segundoApellido")).trim().isEmpty()) {
            errores.add("Segundo apellido es obligatorio");
        }
        if (cambios.containsKey("nacionalidad") && ((String) cambios.get("nacionalidad")).trim().isEmpty()) {
            errores.add("Nacionalidad es obligatoria");
        }
        if (cambios.containsKey("correo") && ((String) cambios.get("correo")).trim().isEmpty()) {
            errores.add("Correo es obligatorio");
        }
        if (cambios.containsKey("turno") && ((String) cambios.get("turno")).trim().isEmpty()) {
            errores.add("Turno es obligatorio");
        }
        if (cambios.containsKey("cargo") && ((String) cambios.get("cargo")).trim().isEmpty()) {
            errores.add("Cargo es obligatorio");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("Errores en los campos:\n- " + String.join("\n- ", errores));
        }
    }

    private void validarCamposObligatorios(String primerNombre, String primerApellido,
            String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, String cargo) {

        List<String> camposFaltantes = new ArrayList<>();

        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            camposFaltantes.add("Primer nombre");
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            camposFaltantes.add("Primer apellido");
        }
        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            camposFaltantes.add("Segundo apellido");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            camposFaltantes.add("Cédula");
        }
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            camposFaltantes.add("Nacionalidad");
        }
        if (correo == null || correo.trim().isEmpty()) {
            camposFaltantes.add("Correo");
        }
        if (turno == null || turno.trim().isEmpty()) {
            camposFaltantes.add("Turno");
        }
        if (cargo == null || cargo.trim().isEmpty()) {
            camposFaltantes.add("Cargo");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios faltantes:\n- " + String.join("\n- ", camposFaltantes));
        }
    }

    private void validarEdad(int edad) {
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        }
    }

    public DefaultTableModel obtenerModeloTabla() {
        String[] columnas = {
            "Foto", "Nombres", "Apellidos", "Edad", "Cédula",
            "Nacionalidad", "Correo", "Turno", "Cargo",
            "Fecha Inicio", "Fecha Fin"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Guardia guardia : obtenerTodosGuardias()) {
            modelo.addRow(new Object[]{
                obtenerImagenGuardia(guardia),
                guardia.getPrimerNombre() + " " + guardia.getSegundoNombre(),
                guardia.getPrimerApellido() + " " + guardia.getSegundoApellido(),
                guardia.getEdad(),
                guardia.getIdentificacion(),
                guardia.getNacionalidad(),
                guardia.getCorreo(),
                guardia.getTurno(),
                guardia.getCargo(),
                formatFecha(guardia.getFechaInicioContrato()),
                formatFecha(guardia.getFechaFinContrato())
            });
        }

        return modelo;
    }

    private String formatFecha(LocalDate fecha) {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private ImageIcon obtenerImagenGuardia(Guardia guardia) {
        if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
            try {
                File imagenFile = new File(guardia.getRutaImagen());
                if (!imagenFile.exists()) {
                    imagenFile = new File("src/" + guardia.getRutaImagen());
                }

                if (imagenFile.exists()) {
                    ImageIcon original = new ImageIcon(imagenFile.getAbsolutePath());
                    Image imagen = original.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    return new ImageIcon(imagen);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar imagen: " + e.getMessage());
            }
        }
        return crearIconoPorDefectoGuardia();
    }

    private ImageIcon crearIconoPorDefectoGuardia() {
        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(200, 220, 255));
        g2d.fillRect(0, 0, 80, 80);
        g2d.setColor(new Color(0, 0, 139));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(5, 5, 70, 70);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(25, 10, 30, 30);
        g2d.fillRect(35, 40, 10, 25);
        g2d.drawLine(35, 45, 15, 35);
        g2d.drawLine(45, 45, 65, 35);
        g2d.drawLine(35, 65, 25, 75);
        g2d.drawLine(45, 65, 55, 75);

        g2d.setColor(Color.YELLOW);
        g2d.fillOval(37, 45, 6, 6);

        g2d.dispose();

        return new ImageIcon(img);
    }
}
