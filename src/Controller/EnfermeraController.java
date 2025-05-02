package Controller;

import DAO.EnfermeraDAO;
import Model.Entities.Enfermera;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EnfermeraController {

    private static EnfermeraController instancia;
    private final EnfermeraDAO enfermeraDAO;
    private final JTextField txtPrimerNombre;
    private final JTextField txtSegundoNombre;
    private final JTextField txtPrimerApellido;
    private final JTextField txtSegundoApellido;
    private final JTextField txtEdad;
    private final JTextField txtCedula;
    private final JTextField txtNacionalidad;
    private final JTextField txtCorreo;
    private final JComboBox<String> cmbTurno;
    private final JDateChooser dateChooserFinContrato;
    private String rutaImagenSeleccionada;

    private EnfermeraController(JTextField txtPrimerNombre, JTextField txtSegundoNombre,
            JTextField txtPrimerApellido, JTextField txtSegundoApellido,
            JTextField txtEdad, JTextField txtCedula,
            JTextField txtNacionalidad, JTextField txtCorreo,
            JComboBox<String> cmbTurno,
            JDateChooser dateChooserFinContrato) {
        this.enfermeraDAO = EnfermeraDAO.getInstancia();
        this.txtPrimerNombre = txtPrimerNombre;
        this.txtSegundoNombre = txtSegundoNombre;
        this.txtPrimerApellido = txtPrimerApellido;
        this.txtSegundoApellido = txtSegundoApellido;
        this.txtEdad = txtEdad;
        this.txtCedula = txtCedula;
        this.txtNacionalidad = txtNacionalidad;
        this.txtCorreo = txtCorreo;
        this.cmbTurno = cmbTurno;
        this.dateChooserFinContrato = dateChooserFinContrato;
    }

    public static synchronized EnfermeraController getInstancia(JTextField txtPrimerNombre, JTextField txtSegundoNombre,
            JTextField txtPrimerApellido, JTextField txtSegundoApellido,
            JTextField txtEdad, JTextField txtCedula,
            JTextField txtNacionalidad, JTextField txtCorreo,
            JComboBox<String> cmbTurno,
            JDateChooser dateChooserFinContrato) {
        if (instancia == null) {
            instancia = new EnfermeraController(txtPrimerNombre, txtSegundoNombre,
                    txtPrimerApellido, txtSegundoApellido,
                    txtEdad, txtCedula, txtNacionalidad,
                    txtCorreo, cmbTurno, dateChooserFinContrato);
        }
        return instancia;
    }

    public Map<String, Object> prepararModificacion(String cedula) {
        Enfermera enfermera = obtenerEnfermeraPorCedula(cedula);
        if (enfermera == null) {
            throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedula);
        }

        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("cedula", enfermera.getIdentificacion());
        datos.put("primerNombre", enfermera.getPrimerNombre());
        datos.put("segundoNombre", enfermera.getSegundoNombre());
        datos.put("primerApellido", enfermera.getPrimerApellido());
        datos.put("segundoApellido", enfermera.getSegundoApellido());
        datos.put("edad", enfermera.getEdad());
        datos.put("nacionalidad", enfermera.getNacionalidad());
        datos.put("correo", enfermera.getCorreo());
        datos.put("turno", enfermera.getTurno());
        datos.put("fechaInicio", enfermera.getFechaContratacion());
        datos.put("fechaFin", enfermera.getFechaFinContrato());
        datos.put("sexo", enfermera.getSexo());
        datos.put("rutaImagen", enfermera.getRutaImagen());
        datos.put("usuario", enfermera.getUsuario());
        datos.put("contrasena", enfermera.getContrasena());

        return datos;
    }

    public boolean procesarModificacion(String cedulaOriginal, 
                                  Map<String, Object> cambios, 
                                  File nuevaImagen) {
        try {
            Enfermera original = obtenerEnfermeraPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedulaOriginal);
            }

            boolean cambiosEnCampos = !original.getPrimerNombre().equals(cambios.get("primerNombre")) ||
                    !Objects.equals(original.getSegundoNombre(), cambios.get("segundoNombre")) ||
                    !original.getPrimerApellido().equals(cambios.get("primerApellido")) ||
                    !original.getSegundoApellido().equals(cambios.get("segundoApellido")) ||
                    original.getEdad() != (int) cambios.get("edad") ||
                    !original.getNacionalidad().equals(cambios.get("nacionalidad")) ||
                    !original.getCorreo().equals(cambios.get("correo")) ||
                    !original.getTurno().equals(cambios.get("turno")) ||
                    !original.getFechaFinContrato().equals(cambios.get("fechaFin"));

            boolean cambioImagen = nuevaImagen != null && 
                    (original.getRutaImagen() == null || 
                    !nuevaImagen.getAbsolutePath().equals(original.getRutaImagen()));

            if (!cambiosEnCampos && !cambioImagen) {
                int respuesta = JOptionPane.showConfirmDialog(
                    null, 
                    "No se han detectado cambios en los datos. ¿Desea guardar igualmente?",
                    "Confirmar sin cambios",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    return false;
                }
            }

            validarCamposModificacion(cambios);
            validarEdad((int) cambios.get("edad"));

            LocalDate fechaInicio = original.getFechaContratacion();
            LocalDate fechaFin = (LocalDate) cambios.get("fechaFin");
            validarFechasContrato(fechaInicio, fechaFin);

            validarLimiteEnfermerasPorTurno((String) cambios.get("turno"), cedulaOriginal);

            Enfermera enfermeraModificada = new Enfermera(
                (String) cambios.get("primerNombre"),
                (String) cambios.get("segundoNombre"),
                (String) cambios.get("primerApellido"),
                (String) cambios.get("segundoApellido"),
                (int) cambios.get("edad"),
                "Femenino",
                (String) cambios.get("nacionalidad"),
                cedulaOriginal,
                (String) cambios.get("turno"),
                fechaInicio,
                fechaFin,
                (String) cambios.get("correo"),
                original.getUsuario(),  // Mantener el mismo usuario
                original.getContrasena() // Mantener la misma contraseña
            );

            if (!cambioImagen && original.getRutaImagen() != null) {
                enfermeraModificada.setRutaImagen(original.getRutaImagen());
            }

            return enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermeraModificada, nuevaImagen);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar enfermera: " + e.getMessage(), e);
        }
    }

    public Enfermera agregarEnfermera() {
    try {
        // Validaciones de campos obligatorios
        StringBuilder camposFaltantes = new StringBuilder();
        if (txtPrimerNombre.getText().trim().isEmpty()) camposFaltantes.append("- Primer nombre\n");
        if (txtPrimerApellido.getText().trim().isEmpty()) camposFaltantes.append("- Primer apellido\n");
        if (txtSegundoApellido.getText().trim().isEmpty()) camposFaltantes.append("- Segundo apellido\n");
        if (txtEdad.getText().trim().isEmpty()) camposFaltantes.append("- Edad\n");
        if (txtCedula.getText().trim().isEmpty()) camposFaltantes.append("- Cédula\n");
        if (txtNacionalidad.getText().trim().isEmpty()) camposFaltantes.append("- Nacionalidad\n");
        if (txtCorreo.getText().trim().isEmpty()) camposFaltantes.append("- Correo\n");
        if (cmbTurno.getSelectedItem() == null || cmbTurno.getSelectedItem().toString().trim().isEmpty()) 
            camposFaltantes.append("- Turno\n");

        if (camposFaltantes.length() > 0) {
            throw new IllegalArgumentException("Los siguientes campos son obligatorios:\n" + camposFaltantes);
        }

        // Validación de imagen
        if (rutaImagenSeleccionada == null || rutaImagenSeleccionada.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen de la enfermera");
        }

        File imagen = new File(rutaImagenSeleccionada);
        if (!imagen.exists()) {
            throw new IllegalArgumentException("El archivo de imagen no existe en la ruta especificada");
        }

        if (!imagen.getName().toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, PNG o GIF");
        }

        // Validación de edad
        int edad;
        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
            if (edad < 18 || edad > 70) {
                throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad debe ser un número válido");
        }

        // Validación de fechas
        if (dateChooserFinContrato.getDate() == null) {
            throw new IllegalArgumentException("La fecha de fin de contrato es obligatoria");
        }

        LocalDate fechaFinContrato = dateChooserFinContrato.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        if (!fechaFinContrato.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de fin de contrato debe ser posterior a la fecha actual");
        }

        // Validación de correo
        String correo = txtCorreo.getText().trim();
        if (!correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido");
        }

        // Obtener datos del formulario
        String primerNombre = txtPrimerNombre.getText().trim();
        String segundoNombre = txtSegundoNombre.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim();
        String cedula = txtCedula.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        String turno = cmbTurno.getSelectedItem().toString();

        // Validar límite de enfermeras
        validarLimiteEnfermerasPorTurno(turno, null);

        // Crear nueva enfermera (usuario y contraseña se generarán en el DAO)
        Enfermera nuevaEnfermera = new Enfermera(
            primerNombre, segundoNombre,
            primerApellido, segundoApellido,
            edad, "Femenino", nacionalidad, cedula,
            turno, LocalDate.now(), fechaFinContrato, correo,
            "", "" // Estos campos los generará el DAO
        );

        // Guardar en la base de datos
        boolean guardado = enfermeraDAO.guardarEnfermera(nuevaEnfermera, imagen);
        
        if (guardado) {
            limpiarFormulario();
            // Retornar la enfermera con los datos actualizados (incluyendo usuario y contraseña)
            return enfermeraDAO.obtenerEnfermeraPorCedula(cedula);
        }
        
        return null;

    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error inesperado al guardar la enfermera: " + e.getMessage(), e);
    }
}

    private void limpiarFormulario() {
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtEdad.setText("");
        txtCedula.setText("");
        txtNacionalidad.setText("");
        txtCorreo.setText("");
        cmbTurno.setSelectedIndex(0);
        dateChooserFinContrato.setDate(null);
        rutaImagenSeleccionada = null;
    }

    public List<Enfermera> obtenerTodasEnfermeras() {
        return enfermeraDAO.obtenerEnfermeras();
    }

    public Enfermera obtenerEnfermeraPorCedula(String cedula) {
        return enfermeraDAO.obtenerEnfermeraPorIdentificacion(cedula);
    }

    public boolean eliminarEnfermera(String cedula) {
        return enfermeraDAO.eliminarEnfermera(cedula);
    }

    public DefaultTableModel obtenerModeloTablaEnfermeras() {
        String[] columnas = {
            "Foto", "Nombres", "Apellidos", "Edad", "Cédula", 
            "Sexo", "Nacionalidad", "Correo", "Turno", 
            "Fecha Inicio", "Fecha Fin", "Usuario"
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

        List<Enfermera> enfermeras = enfermeraDAO.obtenerEnfermeras();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Enfermera enfermera : enfermeras) {
            ImageIcon icono = null;
            if (enfermera.getRutaImagen() != null && !enfermera.getRutaImagen().isEmpty()) {
                try {
                    ImageIcon original = new ImageIcon(enfermera.getRutaImagen());
                    Image imagenEscalada = original.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    icono = new ImageIcon(imagenEscalada);
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen: " + e.getMessage());
                }
            }

            modelo.addRow(new Object[]{
                icono,
                enfermera.getNombresParaTabla(),
                enfermera.getApellidosParaTabla(),
                String.valueOf(enfermera.getEdad()),
                enfermera.getIdentificacion(),
                enfermera.getSexo(),
                enfermera.getNacionalidad(),
                enfermera.getCorreo(),
                enfermera.getTurno(),
                enfermera.getFechaContratacion() != null ? 
                    enfermera.getFechaContratacion().format(dateFormatter) : "",
                enfermera.getFechaFinContrato() != null ? 
                    enfermera.getFechaFinContrato().format(dateFormatter) : "",
                enfermera.getUsuario() // Mostrar el usuario en la tabla
            });
        }

        return modelo;
    }

    // Métodos de validación (sin cambios)
    private void validarLimiteEnfermerasPorTurno(String turno, String cedulaOriginal) {
        List<Enfermera> enfermeras = enfermeraDAO.obtenerEnfermeras();
        long count = enfermeras.stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .filter(e -> !e.getIdentificacion().equals(cedulaOriginal))
                .count();
        
        if (count >= 2) {
            throw new IllegalArgumentException("Ya existen 2 enfermeras en el turno " + turno + 
                                             ". No se pueden agregar más.");
        }
        
        if (enfermeras.size() >= 4 && cedulaOriginal == null) {
            throw new IllegalArgumentException("Se ha alcanzado el límite máximo de 4 enfermeras (2 diurnas y 2 nocturnas)");
        }
    }

    private void validarCamposModificacion(Map<String, Object> cambios) {
        StringBuilder errores = new StringBuilder();

        if (((String) cambios.get("primerNombre")).trim().isEmpty()) {
            errores.append("- Primer nombre es obligatorio\n");
        }
        if (((String) cambios.get("primerApellido")).trim().isEmpty()) {
            errores.append("- Primer apellido es obligatorio\n");
        }
        if (((String) cambios.get("segundoApellido")).trim().isEmpty()) {
            errores.append("- Segundo apellido es obligatorio\n");
        }
        if (((String) cambios.get("nacionalidad")).trim().isEmpty()) {
            errores.append("- Nacionalidad es obligatoria\n");
        }
        if (((String) cambios.get("correo")).trim().isEmpty()) {
            errores.append("- Correo es obligatorio\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    private boolean validarCamposObligatorios(String primerNombre, String primerApellido,
            String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo,
            String turno) {
        StringBuilder camposFaltantes = new StringBuilder();

        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            camposFaltantes.append("- Primer nombre\n");
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            camposFaltantes.append("- Primer apellido\n");
        }
        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            camposFaltantes.append("- Segundo apellido\n");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            camposFaltantes.append("- Cédula\n");
        }
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            camposFaltantes.append("- Nacionalidad\n");
        }
        if (correo == null || correo.trim().isEmpty()) {
            camposFaltantes.append("- Correo\n");
        }
        if (turno == null || turno.trim().isEmpty()) {
            camposFaltantes.append("- Turno\n");
        }

        if (camposFaltantes.length() > 0) {
            throw new IllegalArgumentException("Los siguientes campos son obligatorios:\n" + camposFaltantes.toString());
        }

        return true;
    }

    private boolean validarImagen(File imagen) {
        if (imagen == null || !imagen.exists()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen válida");
        }

        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg")
                && !nombre.endsWith(".png") && !nombre.endsWith(".gif")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, PNG o GIF");
        }

        return true;
    }
    
    public Enfermera obtenerEnfermeraPorUsuario(String usuario) {
    return enfermeraDAO.obtenerEnfermeraPorUsuario(usuario);
}

    private boolean validarFechasContrato(LocalDate inicio, LocalDate fin) {
        if (fin == null) {
            throw new IllegalArgumentException("La fecha de fin de contrato no puede estar vacía");
        }

        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La fecha de fin de contrato debe ser posterior a la fecha de inicio");
        }

        return true;
    }

    private void validarEdad(int edad) {
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        }
    }

    public void setRutaImagenSeleccionada(String rutaImagenSeleccionada) {
        this.rutaImagenSeleccionada = rutaImagenSeleccionada;
    }
}