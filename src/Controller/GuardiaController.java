package Controller;

import DAO.GuardiaDAO;
import Model.Guardia;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GuardiaController {
    private final GuardiaDAO guardiaDAO;
    private final JTextField txtPrimerNombre;
    private final JTextField txtSegundoNombre;
    private final JTextField txtPrimerApellido;
    private final JTextField txtSegundoApellido;
    private final JTextField txtEdad;
    private final JTextField txtCedula;
    private final JTextField txtNacionalidad;
    private final JTextField txtCorreo;
    private final JComboBox<String> cmbTurno;
    private final JComboBox<String> cmbCargo;
    private final JDateChooser dateChooserFinContrato;
    private String rutaImagenSeleccionada;

    public GuardiaController(JTextField txtPrimerNombre, JTextField txtSegundoNombre,
                           JTextField txtPrimerApellido, JTextField txtSegundoApellido,
                           JTextField txtEdad, JTextField txtCedula,
                           JTextField txtNacionalidad, JTextField txtCorreo,
                           JComboBox<String> cmbTurno, JComboBox<String> cmbCargo,
                           JDateChooser dateChooserFinContrato) {
        this.guardiaDAO = GuardiaDAO.getInstancia();
        this.txtPrimerNombre = txtPrimerNombre;
        this.txtSegundoNombre = txtSegundoNombre;
        this.txtPrimerApellido = txtPrimerApellido;
        this.txtSegundoApellido = txtSegundoApellido;
        this.txtEdad = txtEdad;
        this.txtCedula = txtCedula;
        this.txtNacionalidad = txtNacionalidad;
        this.txtCorreo = txtCorreo;
        this.cmbTurno = cmbTurno;
        this.cmbCargo = cmbCargo;
        this.dateChooserFinContrato = dateChooserFinContrato;
    }

    // Método para preparar modificación (nuevo)
    public Map<String, Object> prepararModificacion(String cedula) {
        Guardia guardia = obtenerGuardiaPorCedula(cedula);
        if (guardia == null) {
            throw new IllegalArgumentException("Guardia no encontrado con cédula: " + cedula);
        }

        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("cedula", guardia.getIdentificacion());
        datos.put("primerNombre", guardia.getPrimerNombre());
        datos.put("segundoNombre", guardia.getSegundoNombre());
        datos.put("primerApellido", guardia.getPrimerApellido());
        datos.put("segundoApellido", guardia.getSegundoApellido());
        datos.put("edad", guardia.getEdad());
        datos.put("nacionalidad", guardia.getNacionalidad());
        datos.put("correo", guardia.getCorreo());
        datos.put("turno", guardia.getTurno());
        datos.put("cargo", guardia.getCargo());
        datos.put("fechaInicio", guardia.getFechaInicioContrato());
        datos.put("fechaFin", guardia.getFechaFinContrato());
        datos.put("sexo", guardia.getSexo());
        datos.put("rutaImagen", guardia.getRutaImagen());

        return datos;
    }

    // Método principal para procesar modificación (nuevo)
    public boolean procesarModificacion(String cedulaOriginal, 
                                      Map<String, Object> cambios, 
                                      File nuevaImagen) {
        try {
            if (!hayCambiosReales(cedulaOriginal, cambios, nuevaImagen)) {
                throw new IllegalArgumentException("No se detectaron cambios");
            }

            validarCamposModificacion(cambios);
            validarEdad((int) cambios.get("edad"));
            validarFechasContrato((LocalDate) cambios.get("fechaInicio"), 
                                 (LocalDate) cambios.get("fechaFin"));

            return guardiaDAO.modificarGuardia(
                cedulaOriginal,
                (String) cambios.get("primerNombre"),
                (String) cambios.get("segundoNombre"),
                (String) cambios.get("primerApellido"),
                (String) cambios.get("segundoApellido"),
                (int) cambios.get("edad"),
                (String) cambios.get("nacionalidad"),
                (String) cambios.get("correo"),
                (String) cambios.get("turno"),
                (LocalDate) cambios.get("fechaFin"),
                (String) cambios.get("cargo"),
                nuevaImagen
            );
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar guardia: " + e.getMessage(), e);
        }
    }

    // Métodos de validación (nuevos)
    private boolean hayCambiosReales(String cedula, Map<String, Object> cambios, File nuevaImagen) {
        Guardia original = obtenerGuardiaPorCedula(cedula);
        
        boolean cambiosEnCampos = !original.getPrimerNombre().equals(cambios.get("primerNombre")) ||
                                !Objects.equals(original.getSegundoNombre(), cambios.get("segundoNombre")) ||
                                !original.getPrimerApellido().equals(cambios.get("primerApellido")) ||
                                !original.getSegundoApellido().equals(cambios.get("segundoApellido")) ||
                                original.getEdad() != (int) cambios.get("edad") ||
                                !original.getNacionalidad().equals(cambios.get("nacionalidad")) ||
                                !original.getCorreo().equals(cambios.get("correo")) ||
                                !original.getTurno().equals(cambios.get("turno")) ||
                                !original.getCargo().equals(cambios.get("cargo")) ||
                                !original.getFechaFinContrato().equals(cambios.get("fechaFin"));

        boolean cambioImagen = nuevaImagen != null && 
                             (original.getRutaImagen() == null || 
                             !nuevaImagen.getAbsolutePath().equals(original.getRutaImagen()));

        return cambiosEnCampos || cambioImagen;
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

  public boolean agregarGuardia() {
    try {
        // 1. Validar campos obligatorios del formulario primero
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
        if (cmbCargo.getSelectedItem() == null || cmbCargo.getSelectedItem().toString().trim().isEmpty()) 
            camposFaltantes.append("- Cargo\n");
        
        if (camposFaltantes.length() > 0) {
            throw new IllegalArgumentException("Los siguientes campos son obligatorios:\n" + camposFaltantes.toString());
        }

        // 2. Validar edad
        int edad;
        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
            if (edad < 18 || edad > 70) {
                throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad debe ser un número válido");
        }

        // 3. Validar fechas con formato consistente (dd/MM/yyyy)
        if (dateChooserFinContrato.getDate() == null) {
            throw new IllegalArgumentException("La fecha de fin de contrato es obligatoria");
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaFinContrato = dateChooserFinContrato.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaHoy = LocalDate.now();
        
        if (!fechaFinContrato.isAfter(fechaHoy)) {
            throw new IllegalArgumentException(String.format(
                "La fecha de fin de contrato (%s) debe ser posterior a la fecha actual (%s)",
                fechaFinContrato.format(formatter),
                fechaHoy.format(formatter)
            ));
        }

        // 4. Validar imagen (solo si pasó todas las validaciones anteriores)
        if (rutaImagenSeleccionada == null || rutaImagenSeleccionada.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen del guardia");
        }
        
        File imagen = new File(rutaImagenSeleccionada);
        if (!imagen.exists()) {
            throw new IllegalArgumentException("El archivo de imagen no existe en la ruta especificada");
        }
        
        // Validar formato de imagen
        String nombreImagen = imagen.getName().toLowerCase();
        if (!nombreImagen.endsWith(".jpg") && !nombreImagen.endsWith(".jpeg") && 
            !nombreImagen.endsWith(".png") && !nombreImagen.endsWith(".gif")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, PNG o GIF");
        }

        // 5. Obtener datos del formulario
        String primerNombre = txtPrimerNombre.getText().trim();
        String segundoNombre = txtSegundoNombre.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim();
        String cedula = txtCedula.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        String correo = txtCorreo.getText().trim();
        String turno = cmbTurno.getSelectedItem().toString();
        String cargo = cmbCargo.getSelectedItem().toString();

        // 6. Guardar el guardia
        return guardiaDAO.guardarGuardia(
            primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, cedula, nacionalidad, correo, turno, fechaFinContrato, cargo, imagen
        );
    } catch (IllegalArgumentException e) {
        // Relanzar excepciones de validación para mostrar mensajes específicos
        throw e;
    } catch (Exception e) {
        // Capturar cualquier otro error inesperado
        throw new RuntimeException("Error inesperado al guardar el guardia: " + e.getMessage(), e);
    }
}
   
    public boolean actualizarGuardia(String cedulaOriginal, String primerNombre, String segundoNombre, 
                                   String primerApellido, String segundoApellido, int edad,
                                   String nacionalidad, String correo, String turno,
                                   LocalDate fechaFinContrato, String cargo, File nuevaImagen) {
        
        // Validar campos obligatorios
        if (!validarCamposObligatorios(primerNombre, primerApellido, segundoApellido, 
                                      edad, cedulaOriginal, nacionalidad, correo, turno, cargo)) {
            return false;
        }
        
        // La imagen puede ser null si no se cambia
        if (nuevaImagen != null && !validarImagen(nuevaImagen)) {
            return false;
        }
        
        // Validar fechas
        if (!validarFechasContrato(LocalDate.now(), fechaFinContrato)) {
            return false;
        }
        
        return guardiaDAO.modificarGuardia(
            cedulaOriginal, primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, nacionalidad, correo, turno, fechaFinContrato, cargo, nuevaImagen
        );
    }
    
    // Métodos de lectura
    public List<Guardia> obtenerTodosGuardias() {
        return guardiaDAO.obtenerGuardias();
    }
    
    public Guardia obtenerGuardiaPorCedula(String cedula) {
        return guardiaDAO.obtenerGuardiaPorCedula(cedula);
    }
    
    // Método de eliminación
    public boolean eliminarGuardia(String cedula) {
        return guardiaDAO.eliminarGuardia(cedula);
    }
    
    // Validation methods (focused on your requirements)
    private boolean validarCamposObligatorios(String primerNombre, String primerApellido, 
                                            String segundoApellido, int edad, String cedula, 
                                            String nacionalidad, String correo, 
                                            String turno, String cargo) {
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
        if (cargo == null || cargo.trim().isEmpty()) {
            camposFaltantes.append("- Cargo\n");
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
        
        // Validar tipos de imagen permitidos
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && 
            !nombre.endsWith(".png") && !nombre.endsWith(".gif")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, PNG o GIF");
        }
        
        
        
        return true;
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
    
    public DefaultTableModel obtenerModeloTablaGuardias() {
    String[] columnas = {
        "Foto",
        "Nombres",
        "Apellidos",
        "Edad",
        "Cédula",
        "Sexo",
        "Nacionalidad",
        "Correo",
        "Turno",
        "Cargo",
        "Fecha Inicio",
        "Fecha Fin"
    };

    DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? ImageIcon.class : String.class;
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Hacer que toda la tabla sea no editable
        }
    };

    List<Guardia> guardias = guardiaDAO.obtenerGuardias();

    for (Guardia guardia : guardias) {
        ImageIcon icono = cargarImagenGuardia(guardia);
        
        modelo.addRow(new Object[]{
            icono,
            guardia.getNombresCompletos(),
            guardia.getApellidosCompletos(),
            String.valueOf(guardia.getEdad()),
            guardia.getIdentificacion(),
            guardia.getSexo(),
            guardia.getNacionalidad(),
            guardia.getCorreo(),
            guardia.getTurno(),
            guardia.getCargo(),
            guardia.getFechaInicioContratoFormateada(),
            guardia.getFechaFinContratoFormateada()
        });
    }

    return modelo;
}

private ImageIcon cargarImagenGuardia(Guardia guardia) {
    if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
        try {
            File imgFile = new File(guardia.getRutaImagen());
            if (imgFile.exists()) {
                ImageIcon original = new ImageIcon(guardia.getRutaImagen());
                Image imagenEscalada = original.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                return new ImageIcon(imagenEscalada);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
    }
    return null;
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