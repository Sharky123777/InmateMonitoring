package Controller;

import DAO.CoordinadorDeActividadesDAO;
import Model.Entities.CoordinadorDeActividades;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CoordinadorDeActividadesController {
    private static CoordinadorDeActividadesController instancia;
    private final CoordinadorDeActividadesDAO coordinadorDAO;
    
    // Componentes de la vista
    private JTextField txtPrimerNombre2;
    private JTextField txtSegundoNombre2;
    private JTextField txtPrimerApellido2;
    private JTextField txtSegundoApellido2;
    private JTextField txtEdad2;
    private JTextField txtCedula2;
    private JTextField txtNacionalidad2;
    private JTextField txtCorreo2;
    private JComboBox<String> cmbTurno2;
    private JComboBox<String> cmbCargo2;
    private JDateChooser dateFinContrato2;
    private String rutaImagenSeleccionada2;

    // Constructor privado para Singleton
    private CoordinadorDeActividadesController() {
        this.coordinadorDAO = CoordinadorDeActividadesDAO.getInstancia();
    }

    // Método Singleton para obtener instancia
    public static synchronized CoordinadorDeActividadesController getInstancia() {
        if (instancia == null) {
            instancia = new CoordinadorDeActividadesController();
        }
        return instancia;
    }

    // Establecer componentes de la vista
    public void setComponentes(
        JTextField txtPrimerNombre2, 
        JTextField txtSegundoNombre2,
        JTextField txtPrimerApellido2, 
        JTextField txtSegundoApellido2,
        JTextField txtEdad2, 
        JTextField txtCedula2,
        JTextField txtNacionalidad2, 
        JTextField txtCorreo2,
        JComboBox<String> cmbTurno2, 
        JComboBox<String> cmbCargo2,
        JDateChooser dateFinContrato2
    ) {
        this.txtPrimerNombre2 = txtPrimerNombre2;
        this.txtSegundoNombre2 = txtSegundoNombre2;
        this.txtPrimerApellido2 = txtPrimerApellido2;
        this.txtSegundoApellido2 = txtSegundoApellido2;
        this.txtEdad2 = txtEdad2;
        this.txtCedula2 = txtCedula2;
        this.txtNacionalidad2 = txtNacionalidad2;
        this.txtCorreo2 = txtCorreo2;
        this.cmbTurno2 = cmbTurno2;
        this.cmbCargo2 = cmbCargo2;
        this.dateFinContrato2 = dateFinContrato2;
    }

    // ==================== OPERACIONES CRUD ====================

    public boolean agregarCoordinador() {
        try {
            validarComponentes();
            
            StringBuilder errores = validarCamposObligatorios();
            if (errores.length() > 0) {
                throw new IllegalArgumentException(errores.toString());
            }

            String primerNombre = txtPrimerNombre2.getText().trim();
            String segundoNombre = txtSegundoNombre2.getText().trim();
            String primerApellido = txtPrimerApellido2.getText().trim();
            String segundoApellido = txtSegundoApellido2.getText().trim();
            int edad = obtenerEdadValida();
            String cedula = txtCedula2.getText().trim();
            String nacionalidad = txtNacionalidad2.getText().trim();
            String correo = validarCorreo(txtCorreo2.getText().trim());
            String turno = cmbTurno2.getSelectedItem().toString();
            String cargo = cmbCargo2.getSelectedItem().toString();
            LocalDate fechaFinContrato = obtenerFechaValida();
            
            File imagen = null;
            if (rutaImagenSeleccionada2 != null && !rutaImagenSeleccionada2.isEmpty()) {
                imagen = validarYObtenerImagen();
            }

            return coordinadorDAO.guardarCDA(
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
            throw new RuntimeException("Error al guardar coordinador: " + e.getMessage(), e);
        }
    }

    public boolean modificarCoordinador(String cedulaOriginal) {
        try {
            validarComponentes();
            
            String primerNombre = obtenerValorCampo(txtPrimerNombre2, "Primer nombre");
            String primerApellido = obtenerValorCampo(txtPrimerApellido2, "Primer apellido");
            String segundoApellido = obtenerValorCampo(txtSegundoApellido2, "Segundo apellido");
            int edad = obtenerEdadValida();
            String nacionalidad = obtenerValorCampo(txtNacionalidad2, "Nacionalidad");
            String correo = obtenerValorCampo(txtCorreo2, "Correo");
            String turno = obtenerValorCombo(cmbTurno2, "Turno");
            String cargo = obtenerValorCombo(cmbCargo2, "Cargo");
            LocalDate fechaFinContrato = obtenerFechaValida();
            File imagen = rutaImagenSeleccionada2 != null ? new File(rutaImagenSeleccionada2) : null;

            return coordinadorDAO.modificarCoordinador(
                cedulaOriginal,
                primerNombre,
                txtSegundoNombre2.getText().trim(),
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
            throw new RuntimeException("Error al modificar coordinador: " + e.getMessage(), e);
        }
    }
    
    public boolean actualizarCoordinador(
        String cedulaOriginal, 
        String primerNombre, 
        String segundoNombre,
        String primerApellido, 
        String segundoApellido,
        int edad, 
        String nacionalidad, 
        String correo,
        String turno, 
        LocalDate fechaFinContrato,
        String cargo, 
        File nuevaImagen
    ) {
        try {
            if (primerNombre == null || primerNombre.trim().isEmpty() ||
                primerApellido == null || primerApellido.trim().isEmpty() ||
                segundoApellido == null || segundoApellido.trim().isEmpty() ||
                cedulaOriginal == null || cedulaOriginal.trim().isEmpty()) {
                throw new IllegalArgumentException("Campos obligatorios no pueden estar vacíos");
            }

            if (edad < 18 || edad > 70) {
                throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
            }

            if (fechaFinContrato == null || !fechaFinContrato.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Fecha fin de contrato inválida");
            }

            return coordinadorDAO.modificarCoordinador(
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
            throw new RuntimeException("Error al modificar coordinador: " + e.getMessage(), e);
        }
    }

    public boolean eliminarCoordinador(String cedula) {
        try {
            if (cedula == null || cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("Cédula es requerida para eliminar");
            }
            return coordinadorDAO.eliminarCoordinador(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar coordinador: " + e.getMessage(), e);
        }
    }

    public CoordinadorDeActividades obtenerCoordinadorPorCedula(String cedula) {
        try {
            return coordinadorDAO.obtenerCoordinadorPorCedula(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener coordinador: " + e.getMessage(), e);
        }
    }

    public List<CoordinadorDeActividades> obtenerTodosCoordinadores() {
        try {
            return coordinadorDAO.obtenerCDAS();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener coordinadores: " + e.getMessage(), e);
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void validarComponentes() {
        StringBuilder errores = new StringBuilder();
        
        if (txtPrimerNombre2 == null) errores.append("- txtPrimerNombre2 no inicializado\n");
        if (txtPrimerApellido2 == null) errores.append("- txtPrimerApellido2 no inicializado\n");
        if (txtSegundoApellido2 == null) errores.append("- txtSegundoApellido2 no inicializado\n");
        if (txtEdad2 == null) errores.append("- txtEdad2 no inicializado\n");
        if (txtCedula2 == null) errores.append("- txtCedula2 no inicializado\n");
        if (txtNacionalidad2 == null) errores.append("- txtNacionalidad2 no inicializado\n");
        if (txtCorreo2 == null) errores.append("- txtCorreo2 no inicializado\n");
        if (cmbTurno2 == null) errores.append("- cmbTurno2 no inicializado\n");
        if (cmbCargo2 == null) errores.append("- cmbCargo2 no inicializado\n");
        if (dateFinContrato2 == null) errores.append("- dateFinContrato2 no inicializado\n");
        
        if (errores.length() > 0) {
            String mensajeError = "Componentes no inicializados correctamente:\n" + errores.toString();
            System.err.println(mensajeError);
            throw new IllegalStateException(mensajeError);
        }
    }

    private StringBuilder validarCamposObligatorios() {
        StringBuilder errores = new StringBuilder();
        
        if (txtPrimerNombre2.getText().trim().isEmpty()) 
            errores.append("- Primer nombre es obligatorio\n");
        if (txtPrimerApellido2.getText().trim().isEmpty()) 
            errores.append("- Primer apellido es obligatorio\n");
        if (txtSegundoApellido2.getText().trim().isEmpty()) 
            errores.append("- Segundo apellido es obligatorio\n");
        if (txtEdad2.getText().trim().isEmpty()) 
            errores.append("- Edad es obligatoria\n");
        if (txtCedula2.getText().trim().isEmpty()) 
            errores.append("- Cédula es obligatoria\n");
        if (txtNacionalidad2.getText().trim().isEmpty()) 
            errores.append("- Nacionalidad es obligatoria\n");
        if (txtCorreo2.getText().trim().isEmpty()) 
            errores.append("- Correo electrónico es obligatorio\n");
        if (cmbTurno2.getSelectedItem() == null) 
            errores.append("- Turno es obligatorio\n");
        if (cmbCargo2.getSelectedItem() == null) 
            errores.append("- Cargo es obligatorio\n");
        if (dateFinContrato2.getDate() == null) 
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
            int edad = Integer.parseInt(txtEdad2.getText().trim());
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
            Date fechaSeleccionada = dateFinContrato2.getDate();
            
            if (fechaSeleccionada == null) {
                String textoFecha = ((JTextField)dateFinContrato2.getDateEditor().getUiComponent()).getText();
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
        if (rutaImagenSeleccionada2 == null || rutaImagenSeleccionada2.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen");
        }
        
        File imagen = new File(rutaImagenSeleccionada2);
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

    public boolean existeCoordinador(String cedula) {
        try {
            return coordinadorDAO.existeCoordinador(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia: " + e.getMessage(), e);
        }
    }

    // ==================== MÉTODOS PARA LA VISTA ====================

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

        for (CoordinadorDeActividades coordinador : obtenerTodosCoordinadores()) {
            modelo.addRow(new Object[]{
                obtenerImagenCoordinador(coordinador),
                coordinador.getPrimerNombre() + " " + coordinador.getSegundoNombre(),
                coordinador.getPrimerApellido() + " " + coordinador.getSegundoApellido(),
                coordinador.getEdad(),
                coordinador.getIdentificacion(),
                coordinador.getNacionalidad(),
                coordinador.getCorreo(),
                coordinador.getTurno(),
                coordinador.getCargo(),
                coordinador.getFechaInicioContrato() != null ? 
                    coordinador.getFechaInicioContrato().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                coordinador.getFechaFinContrato() != null ? 
                    coordinador.getFechaFinContrato().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""
            });
        }

        return modelo;
    }

    private ImageIcon obtenerImagenCoordinador(CoordinadorDeActividades coordinador) {
        if (coordinador.getRutaImagen() != null && !coordinador.getRutaImagen().isEmpty()) {
            try {
                ImageIcon original = new ImageIcon(coordinador.getRutaImagen());
                Image imagen = original.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new ImageIcon(imagen);
            } catch (Exception e) {
                System.err.println("Error al cargar imagen: " + e.getMessage());
            }
        }
        return null;
    }

    public void cargarDatosEnFormulario(CoordinadorDeActividades coordinador) {
        if (coordinador != null) {
            txtPrimerNombre2.setText(coordinador.getPrimerNombre());
            txtSegundoNombre2.setText(coordinador.getSegundoNombre());
            txtPrimerApellido2.setText(coordinador.getPrimerApellido());
            txtSegundoApellido2.setText(coordinador.getSegundoApellido());
            txtEdad2.setText(String.valueOf(coordinador.getEdad()));
            txtCedula2.setText(coordinador.getIdentificacion());
            txtNacionalidad2.setText(coordinador.getNacionalidad());
            txtCorreo2.setText(coordinador.getCorreo());
            cmbTurno2.setSelectedItem(coordinador.getTurno());
            cmbCargo2.setSelectedItem(coordinador.getCargo());
            if (coordinador.getFechaInicioContrato() != null) {
                // Asumiendo que tienes un dateChooser para fecha inicio
                // dateInicioContrato2.setDate(java.sql.Date.valueOf(coordinador.getFechaInicioContrato()));
            }
            if (coordinador.getFechaFinContrato() != null) {
                dateFinContrato2.setDate(java.sql.Date.valueOf(coordinador.getFechaFinContrato()));
            }
            
            if (coordinador.getRutaImagen() != null && !coordinador.getRutaImagen().isEmpty()) {
                rutaImagenSeleccionada2 = coordinador.getRutaImagen();
            }
        }
    }

    public void limpiarFormulario() {
        txtPrimerNombre2.setText("");
        txtSegundoNombre2.setText("");
        txtPrimerApellido2.setText("");
        txtSegundoApellido2.setText("");
        txtEdad2.setText("");
        txtCedula2.setText("");
        txtNacionalidad2.setText("");
        txtCorreo2.setText("");
        cmbTurno2.setSelectedIndex(0);
        cmbCargo2.setSelectedIndex(0);
        dateFinContrato2.setDate(null);
        rutaImagenSeleccionada2 = null;
    }

    // ==================== GETTERS Y SETTERS ====================

    public JTextField getTxtPrimerNombre2() {
        return txtPrimerNombre2;
    }

    public void setTxtPrimerNombre2(JTextField txtPrimerNombre2) {
        this.txtPrimerNombre2 = txtPrimerNombre2;
    }

    public JTextField getTxtSegundoNombre2() {
        return txtSegundoNombre2;
    }

    public void setTxtSegundoNombre2(JTextField txtSegundoNombre2) {
        this.txtSegundoNombre2 = txtSegundoNombre2;
    }

    public JTextField getTxtPrimerApellido2() {
        return txtPrimerApellido2;
    }

    public void setTxtPrimerApellido2(JTextField txtPrimerApellido2) {
        this.txtPrimerApellido2 = txtPrimerApellido2;
    }

    public JTextField getTxtSegundoApellido2() {
        return txtSegundoApellido2;
    }

    public void setTxtSegundoApellido2(JTextField txtSegundoApellido2) {
        this.txtSegundoApellido2 = txtSegundoApellido2;
    }

    public JTextField getTxtEdad2() {
        return txtEdad2;
    }

    public void setTxtEdad2(JTextField txtEdad2) {
        this.txtEdad2 = txtEdad2;
    }

    public JTextField getTxtCedula2() {
        return txtCedula2;
    }

    public void setTxtCedula2(JTextField txtCedula2) {
        this.txtCedula2 = txtCedula2;
    }

    public JTextField getTxtNacionalidad2() {
        return txtNacionalidad2;
    }

    public void setTxtNacionalidad2(JTextField txtNacionalidad2) {
        this.txtNacionalidad2 = txtNacionalidad2;
    }

    public JTextField getTxtCorreo2() {
        return txtCorreo2;
    }

    public void setTxtCorreo2(JTextField txtCorreo2) {
        this.txtCorreo2 = txtCorreo2;
    }

    public JComboBox<String> getCmbTurno2() {
        return cmbTurno2;
    }

    public void setCmbTurno2(JComboBox<String> cmbTurno2) {
        this.cmbTurno2 = cmbTurno2;
    }

    public JComboBox<String> getCmbCargo2() {
        return cmbCargo2;
    }

    public void setCmbCargo2(JComboBox<String> cmbCargo2) {
        this.cmbCargo2 = cmbCargo2;
    }

    public JDateChooser getDateFinContrato2() {
        return dateFinContrato2;
    }

    public void setDateFinContrato2(JDateChooser dateFinContrato2) {
        this.dateFinContrato2 = dateFinContrato2;
    }

    public String getRutaImagenSeleccionada2() {
        return rutaImagenSeleccionada2;
    }

    public void setRutaImagenSeleccionada2(String rutaImagenSeleccionada2) {
        this.rutaImagenSeleccionada2 = rutaImagenSeleccionada2;
    }
}