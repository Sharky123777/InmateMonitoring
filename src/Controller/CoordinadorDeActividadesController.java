package Controller;

import DAO.CoordinadorDeActividadesDAO;
import Model.CoordinadorDeActividades;
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
    
    // Componentes de la vista (con sufijo 2)
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
    private JDateChooser jDateChooserFinContrato2;
    private String rutaImagenSeleccionada2;

    // Singleton
    public static synchronized CoordinadorDeActividadesController getInstancia() {
        if (instancia == null) {
            instancia = new CoordinadorDeActividadesController();
        }
        return instancia;
    }

    private CoordinadorDeActividadesController() {
        this.coordinadorDAO = CoordinadorDeActividadesDAO.getInstancia();
    }

    // ================ CRUD ================
    public boolean agregarCoordinador() {
        try {
            validarComponentes();
            
            // Validar campos obligatorios
            if (txtPrimerNombre2.getText().trim().isEmpty() || 
                txtPrimerApellido2.getText().trim().isEmpty() || 
                txtCedula2.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Nombre, apellido y cédula son obligatorios");
            }

            // Obtener datos
            LocalDate fechaFin = jDateChooserFinContrato2.getDate() != null ?
                jDateChooserFinContrato2.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate() :
                null;

            File imagen = (rutaImagenSeleccionada2 != null && !rutaImagenSeleccionada2.isEmpty()) ?
                new File(rutaImagenSeleccionada2) : null;

            return coordinadorDAO.guardarCDA(
                txtPrimerNombre2.getText().trim(),
                txtSegundoNombre2.getText().trim(),
                txtPrimerApellido2.getText().trim(),
                txtSegundoApellido2.getText().trim(),
                Integer.parseInt(txtEdad2.getText().trim()),
                txtCedula2.getText().trim(),
                txtNacionalidad2.getText().trim(),
                txtCorreo2.getText().trim(),
                cmbTurno2.getSelectedItem().toString(),
                fechaFin,
                cmbCargo2.getSelectedItem().toString(),
                imagen
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar: " + e.getMessage(), e);
        }
    }

    public boolean modificarCoordinador(String cedulaOriginal) {
        try {
            LocalDate fechaFin = jDateChooserFinContrato2.getDate() != null ?
                jDateChooserFinContrato2.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate() :
                null;

            File imagen = (rutaImagenSeleccionada2 != null && !rutaImagenSeleccionada2.isEmpty()) ?
                new File(rutaImagenSeleccionada2) : null;

            return coordinadorDAO.modificarCoordinador(
                cedulaOriginal,
                txtPrimerNombre2.getText().trim(),
                txtSegundoNombre2.getText().trim(),
                txtPrimerApellido2.getText().trim(),
                txtSegundoApellido2.getText().trim(),
                Integer.parseInt(txtEdad2.getText().trim()),
                txtNacionalidad2.getText().trim(),
                txtCorreo2.getText().trim(),
                cmbTurno2.getSelectedItem().toString(),
                fechaFin,
                cmbCargo2.getSelectedItem().toString(),
                imagen
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar: " + e.getMessage(), e);
        }
    }

    public boolean eliminarCoordinador(String cedula) {
        try {
            return coordinadorDAO.eliminarCoordinador(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar: " + e.getMessage(), e);
        }
    }

    // ================ MÉTODOS DE CONSULTA ================
    public List<CoordinadorDeActividades> obtenerTodosCoordinadores() {
        try {
            return coordinadorDAO.obtenerCDAS();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener lista: " + e.getMessage(), e);
        }
    }

    public CoordinadorDeActividades buscarPorCedula(String cedula) {
        try {
            return coordinadorDAO.obtenerCoordinadorPorCedula(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar: " + e.getMessage(), e);
        }
    }

    // ================ MÉTODOS PARA TABLA ================
    public DefaultTableModel crearModeloTabla() {
        String[] columnas = {"Foto", "Nombres", "Apellidos", "Cédula", "Turno", "Cargo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? ImageIcon.class : String.class;
            }
        };

        for (CoordinadorDeActividades c : obtenerTodosCoordinadores()) {
            modelo.addRow(new Object[]{
                obtenerMiniaturaImagen(c.getRutaImagen()),
                c.getPrimerNombre() + " " + c.getSegundoNombre(),
                c.getPrimerApellido() + " " + c.getSegundoApellido(),
                c.getIdentificacion(),
                c.getTurno(),
                c.getCargo()
            });
        }
        return modelo;
    }

    private ImageIcon obtenerMiniaturaImagen(String ruta) {
        if (ruta != null && !ruta.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(ruta);
                return new ImageIcon(icon.getImage()
                    .getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                System.err.println("Error al cargar imagen: " + e.getMessage());
            }
        }
        return null;
    }

    // ================ MÉTODOS DE FORMULARIO ================
    public void cargarDatosEnFormulario(CoordinadorDeActividades c) {
        if (c != null) {
            txtPrimerNombre2.setText(c.getPrimerNombre());
            txtSegundoNombre2.setText(c.getSegundoNombre());
            txtPrimerApellido2.setText(c.getPrimerApellido());
            txtSegundoApellido2.setText(c.getSegundoApellido());
            txtEdad2.setText(String.valueOf(c.getEdad()));
            txtCedula2.setText(c.getIdentificacion());
            txtNacionalidad2.setText(c.getNacionalidad());
            txtCorreo2.setText(c.getCorreo());
            cmbTurno2.setSelectedItem(c.getTurno());
            cmbCargo2.setSelectedItem(c.getCargo());
            if (c.getFechaFinContrato() != null) {
                jDateChooserFinContrato2.setDate(
                    java.sql.Date.valueOf(c.getFechaFinContrato()));
            }
            rutaImagenSeleccionada2 = c.getRutaImagen();
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
        jDateChooserFinContrato2.setDate(null);
        rutaImagenSeleccionada2 = null;
    }

    // ================ VALIDACIONES ================
    private void validarComponentes() {
        if (txtPrimerNombre2 == null || txtCedula2 == null || jDateChooserFinContrato2 == null) {
            throw new IllegalStateException("Componentes no inicializados");
        }
    }

    public boolean validarCorreo(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // ================ GETTERS Y SETTERS ================
    public void setComponentes(JTextField pNombre, JTextField sNombre, JTextField pApellido, 
                             JTextField sApellido, JTextField edad, JTextField cedula,
                             JTextField nacionalidad, JTextField correo, JComboBox<String> turno,
                             JComboBox<String> cargo, JDateChooser fechaFin) {
        this.txtPrimerNombre2 = pNombre;
        this.txtSegundoNombre2 = sNombre;
        this.txtPrimerApellido2 = pApellido;
        this.txtSegundoApellido2 = sApellido;
        this.txtEdad2 = edad;
        this.txtCedula2 = cedula;
        this.txtNacionalidad2 = nacionalidad;
        this.txtCorreo2 = correo;
        this.cmbTurno2 = turno;
        this.cmbCargo2 = cargo;
        this.jDateChooserFinContrato2 = fechaFin;
    }

    public void setRutaImagenSeleccionada2(String ruta) {
        this.rutaImagenSeleccionada2 = ruta;
    }

    public String getRutaImagenSeleccionada2() {
        return rutaImagenSeleccionada2;
    }
}