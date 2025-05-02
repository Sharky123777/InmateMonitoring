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
    private static GuardiaController instancia;
    private final GuardiaDAO guardiaDAO;
    
    // Componentes de la vista
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtEdad;
    private JTextField txtCedula;
    private JTextField txtNacionalidad;
    private JTextField txtCorreo;
    private JComboBox<String> cmbTurno;
    private JComboBox<String> cmbCargo;
    private JDateChooser jDateChooserFinContrato;
    private String rutaImagenSeleccionada;

    // Constructor privado para Singleton
    private GuardiaController() {
        this.guardiaDAO = GuardiaDAO.getInstancia();
    }

    // Método Singleton para obtener instancia
    public static synchronized GuardiaController getInstancia() {
        if (instancia == null) {
            instancia = new GuardiaController();
        }
        return instancia;
    }

 // Establecer componentes de la vista
public void setComponentes(JTextField txtPrimerNombre, JTextField txtSegundoNombre,
                     JTextField txtPrimerApellido, JTextField txtSegundoApellido,
                     JTextField txtEdad, JTextField txtCedula,
                     JTextField txtNacionalidad, JTextField txtCorreo,
                     JComboBox<String> cmbTurno, JComboBox<String> cmbCargo,
                     JDateChooser jDateChooserFinContrato) {
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
    this.jDateChooserFinContrato = jDateChooserFinContrato; // Corregido: mismo nombre que el parámetro
}
    // ==================== OPERACIONES CRUD ====================


    public boolean agregarGuardia() {
    try {
        // 1. Validación de componentes
        validarComponentes();
        
        // 2. Validación de campos obligatorios
        StringBuilder errores = validarCamposObligatorios();
        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }

        // 3. Obtener y validar datos
        String primerNombre = txtPrimerNombre.getText().trim();
        String segundoNombre = txtSegundoNombre.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim();
        int edad = obtenerEdadValida();
        String cedula = txtCedula.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        String correo = validarCorreo(txtCorreo.getText().trim());
        String turno = cmbTurno.getSelectedItem().toString();
        String cargo = cmbCargo.getSelectedItem().toString();
        LocalDate fechaFinContrato = obtenerFechaValida();
        
        // Validación de imagen (opcional si no es obligatoria)
        File imagen = null;
        if (rutaImagenSeleccionada != null && !rutaImagenSeleccionada.isEmpty()) {
            imagen = validarYObtenerImagen();
        }

        // 4. Crear y guardar el guardia
        return guardiaDAO.guardarGuardia(
            primerNombre,
            segundoNombre,
            primerApellido,
            segundoApellido,
            edad,
            cedula,
            nacionalidad,
            correo,
            turno,
            fechaFinContrato,
            cargo,
            imagen
        );
        
    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar guardia: " + e.getMessage(), e);
    }
}

    public boolean modificarGuardia(String cedulaOriginal) {
        try {
            validarComponentes();
            
            // Obtener y validar datos
            String primerNombre = obtenerValorCampo(txtPrimerNombre, "Primer nombre");
            String primerApellido = obtenerValorCampo(txtPrimerApellido, "Primer apellido");
            String segundoApellido = obtenerValorCampo(txtSegundoApellido, "Segundo apellido");
            int edad = obtenerEdadValida();
            String nacionalidad = obtenerValorCampo(txtNacionalidad, "Nacionalidad");
            String correo = obtenerValorCampo(txtCorreo, "Correo");
            String turno = obtenerValorCombo(cmbTurno, "Turno");
            String cargo = obtenerValorCombo(cmbCargo, "Cargo");
            LocalDate fechaFinContrato = obtenerFechaValida();
            File imagen = rutaImagenSeleccionada != null ? new File(rutaImagenSeleccionada) : null;

            return guardiaDAO.modificarGuardia(
                cedulaOriginal,
                primerNombre,
                txtSegundoNombre.getText().trim(),
                primerApellido,
                segundoApellido,
                edad,
                nacionalidad,
                correo,
                turno,
                fechaFinContrato,
                cargo,
                imagen
            );
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar guardia: " + e.getMessage(), e);
        }
    }
    
    public boolean actualizarGuardia(String cedulaOriginal, 
                               String primerNombre, String segundoNombre,
                               String primerApellido, String segundoApellido,
                               int edad, String nacionalidad, String correo,
                               String turno, LocalDate fechaFinContrato,
                               String cargo, File nuevaImagen) {
    try {
        // Validar campos obligatorios
        if (primerNombre == null || primerNombre.trim().isEmpty() ||
            primerApellido == null || primerApellido.trim().isEmpty() ||
            segundoApellido == null || segundoApellido.trim().isEmpty() ||
            cedulaOriginal == null || cedulaOriginal.trim().isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios no pueden estar vacíos");
        }

        // Validar edad
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        }

        // Validar fecha
        if (fechaFinContrato == null || !fechaFinContrato.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha fin de contrato inválida");
        }

        return guardiaDAO.modificarGuardia(
            cedulaOriginal,
            primerNombre,
            segundoNombre,
            primerApellido,
            segundoApellido,
            edad,
            nacionalidad,
            correo,
            turno,
            fechaFinContrato,
            cargo,
            nuevaImagen
        );
    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error al modificar guardia: " + e.getMessage(), e);
    }
}

    public boolean eliminarGuardia(String cedula) {
        try {
            if (cedula == null || cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("Cédula es requerida para eliminar");
            }
            return guardiaDAO.eliminarGuardia(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar guardia: " + e.getMessage(), e);
        }
    }

    public Guardia obtenerGuardiaPorCedula(String cedula) {
        try {
            return guardiaDAO.obtenerGuardiaPorCedula(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener guardia: " + e.getMessage(), e);
        }
    }

    public List<Guardia> obtenerTodosGuardias() {
        try {
            return guardiaDAO.obtenerGuardias();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener guardias: " + e.getMessage(), e);
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void validarComponentes() {
    StringBuilder errores = new StringBuilder();
    
    if (txtPrimerNombre == null) errores.append("- txtPrimerNombre no inicializado\n");
    if (txtPrimerApellido == null) errores.append("- txtPrimerApellido no inicializado\n");
    if (txtSegundoApellido == null) errores.append("- txtSegundoApellido no inicializado\n");
    if (txtEdad == null) errores.append("- txtEdad no inicializado\n");
    if (txtCedula == null) errores.append("- txtCedula no inicializado\n");
    if (txtNacionalidad == null) errores.append("- txtNacionalidad no inicializado\n");
    if (txtCorreo == null) errores.append("- txtCorreo no inicializado\n");
    if (cmbTurno == null) errores.append("- cmbTurno no inicializado\n");
    if (cmbCargo == null) errores.append("- cmbCargo no inicializado\n");
    if (jDateChooserFinContrato == null) errores.append("- jDateChooserFinContrato no inicializado\n");
    
    if (errores.length() > 0) {
        // Mensaje detallado para depuración
        String mensajeError = "Componentes no inicializados correctamente:\n" + errores.toString();
        System.err.println(mensajeError); // Log en consola
        throw new IllegalStateException(mensajeError);
    }
}

    private StringBuilder validarCamposObligatorios() {
    StringBuilder errores = new StringBuilder();
    
    if (txtPrimerNombre.getText().trim().isEmpty()) 
        errores.append("- Primer nombre es obligatorio\n");
    if (txtPrimerApellido.getText().trim().isEmpty()) 
        errores.append("- Primer apellido es obligatorio\n");
    if (txtSegundoApellido.getText().trim().isEmpty()) 
        errores.append("- Segundo apellido es obligatorio\n");
    if (txtEdad.getText().trim().isEmpty()) 
        errores.append("- Edad es obligatoria\n");
    if (txtCedula.getText().trim().isEmpty()) 
        errores.append("- Cédula es obligatoria\n");
    if (txtNacionalidad.getText().trim().isEmpty()) 
        errores.append("- Nacionalidad es obligatoria\n");
    if (txtCorreo.getText().trim().isEmpty()) 
        errores.append("- Correo electrónico es obligatorio\n");
    if (cmbTurno.getSelectedItem() == null) 
        errores.append("- Turno es obligatorio\n");
    if (cmbCargo.getSelectedItem() == null) 
        errores.append("- Cargo es obligatorio\n");
    if (jDateChooserFinContrato.getDate() == null) 
        errores.append("- Fecha de fin de contrato es obligatoria\n");
    
    return errores;
}

    private String validarCorreo(String correo) {
    if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
    }
    return correo;
}
    
    private String obtenerValorCampo(JTextField campo, String nombreCampo) {
        String valor = campo.getText().trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio");
        }
        return valor;
    }

    private String obtenerValorCombo(JComboBox<String> combo, String nombreCampo) {
        Object seleccion = combo.getSelectedItem();
        if (seleccion == null || seleccion.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio");
        }
        return seleccion.toString().trim();
    }

    private int obtenerEdadValida() {
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            if (edad < 18 || edad > 70) {
                throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
            }
            return edad;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad debe ser un número válido");
        }
    }
private LocalDate obtenerFechaValida() {
    try {
        // Obtener la fecha directamente del JDateChooser
        Date fechaSeleccionada = jDateChooserFinContrato.getDate();
        
        if (fechaSeleccionada == null) {
            // Intenta obtener la fecha del texto del editor
            String textoFecha = ((JTextField)jDateChooserFinContrato.getDateEditor().getUiComponent()).getText();
            if (!textoFecha.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    return LocalDate.parse(textoFecha, formatter);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Use DD/MM/AAAA");
                }
            }
            throw new IllegalArgumentException("Debe seleccionar una fecha de fin de contrato");
        }
        
        return fechaSeleccionada.toInstant()
               .atZone(ZoneId.systemDefault())
               .toLocalDate();
        
    } catch (Exception e) {
        throw new IllegalArgumentException("Error al procesar la fecha: " + e.getMessage());
    }
}

    private File validarYObtenerImagen() {
        if (rutaImagenSeleccionada == null || rutaImagenSeleccionada.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen");
        }
        
        File imagen = new File(rutaImagenSeleccionada);
        if (!imagen.exists()) {
            throw new IllegalArgumentException("El archivo de imagen no existe");
        }
        
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && 
            !nombre.endsWith(".png") && !nombre.endsWith(".gif")) {
            throw new IllegalArgumentException("Formato de imagen no válido");
        }
        
        return imagen;
    }

    public boolean existeGuardia(String cedula) {
    try {
        return guardiaDAO.existeGuardia(cedula);
    } catch (Exception e) {
        throw new RuntimeException("Error al verificar existencia: " + e.getMessage(), e);
    }
}

    // ==================== MÉTODOS PARA LA VISTA ====================

    public DefaultTableModel obtenerModeloTabla() {
        String[] columnas = {
            "Foto", "Nombres", "Apellidos", "Edad", "Cédula", 
            "Sexo", "Nacionalidad", "Correo", "Turno", "Cargo",
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
                guardia.getSexo(),
                guardia.getNacionalidad(),
                guardia.getCorreo(),
                guardia.getTurno(),
                guardia.getCargo(),
                guardia.getFechaInicioContrato(),
                guardia.getFechaFinContrato()
            });
        }

        return modelo;
    }

    private ImageIcon obtenerImagenGuardia(Guardia guardia) {
        if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
            try {
                ImageIcon original = new ImageIcon(guardia.getRutaImagen());
                Image imagen = original.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new ImageIcon(imagen);
            } catch (Exception e) {
                System.err.println("Error al cargar imagen: " + e.getMessage());
            }
        }
        return null;
    }

    public void cargarDatosEnFormulario(Guardia guardia) {
        if (guardia != null) {
            txtPrimerNombre.setText(guardia.getPrimerNombre());
            txtSegundoNombre.setText(guardia.getSegundoNombre());
            txtPrimerApellido.setText(guardia.getPrimerApellido());
            txtSegundoApellido.setText(guardia.getSegundoApellido());
            txtEdad.setText(String.valueOf(guardia.getEdad()));
            txtCedula.setText(guardia.getIdentificacion());
            txtNacionalidad.setText(guardia.getNacionalidad());
            txtCorreo.setText(guardia.getCorreo());
            cmbTurno.setSelectedItem(guardia.getTurno());
            cmbCargo.setSelectedItem(guardia.getCargo());
            jDateChooserFinContrato.setDate(java.sql.Date.valueOf(guardia.getFechaFinContrato()));
            
            if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
                rutaImagenSeleccionada = guardia.getRutaImagen();
                // Aquí puedes cargar la imagen en un JLabel si lo deseas
            }
        }
    }

    public void limpiarFormulario() {
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtEdad.setText("");
        txtCedula.setText("");
        txtNacionalidad.setText("");
        txtCorreo.setText("");
        cmbTurno.setSelectedIndex(0);
        cmbCargo.setSelectedIndex(0);
        jDateChooserFinContrato.setDate(null);
        rutaImagenSeleccionada = null;
    }

    public JTextField getTxtPrimerNombre() {
        return txtPrimerNombre;
    }

    public void setTxtPrimerNombre(JTextField txtPrimerNombre) {
        this.txtPrimerNombre = txtPrimerNombre;
    }

    public JTextField getTxtSegundoNombre() {
        return txtSegundoNombre;
    }

    public void setTxtSegundoNombre(JTextField txtSegundoNombre) {
        this.txtSegundoNombre = txtSegundoNombre;
    }

    public JTextField getTxtPrimerApellido() {
        return txtPrimerApellido;
    }

    public void setTxtPrimerApellido(JTextField txtPrimerApellido) {
        this.txtPrimerApellido = txtPrimerApellido;
    }

    public JTextField getTxtSegundoApellido() {
        return txtSegundoApellido;
    }

    public void setTxtSegundoApellido(JTextField txtSegundoApellido) {
        this.txtSegundoApellido = txtSegundoApellido;
    }

    public JTextField getTxtEdad() {
        return txtEdad;
    }

    public void setTxtEdad(JTextField txtEdad) {
        this.txtEdad = txtEdad;
    }

    public JTextField getTxtCedula() {
        return txtCedula;
    }

    public void setTxtCedula(JTextField txtCedula) {
        this.txtCedula = txtCedula;
    }

    public JTextField getTxtNacionalidad() {
        return txtNacionalidad;
    }

    public void setTxtNacionalidad(JTextField txtNacionalidad) {
        this.txtNacionalidad = txtNacionalidad;
    }

    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    public void setTxtCorreo(JTextField txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    public JComboBox<String> getCmbTurno() {
        return cmbTurno;
    }

    public void setCmbTurno(JComboBox<String> cmbTurno) {
        this.cmbTurno = cmbTurno;
    }

    public JComboBox<String> getCmbCargo() {
        return cmbCargo;
    }

    public void setCmbCargo(JComboBox<String> cmbCargo) {
        this.cmbCargo = cmbCargo;
    }

    public JDateChooser getDateChooserFinContrato() {
        return jDateChooserFinContrato;
    }

    public void setDateChooserFinContrato(JDateChooser dateChooserFinContrato) {
        this.jDateChooserFinContrato = dateChooserFinContrato;
    }

    

    public void setRutaImagenSeleccionada(String rutaImagenSeleccionada) {
        this.rutaImagenSeleccionada = rutaImagenSeleccionada;
    }

    public String getRutaImagenSeleccionada() {
        return rutaImagenSeleccionada;
    }
}