package Controller;

import DAO.GuardiaDAO;
import Model.Guardia;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GuardiaController {
    private final GuardiaDAO guardiaDAO;
    
    public GuardiaController() {
        this.guardiaDAO = new GuardiaDAO();
    }
    
    // Operaciones CRUD
    public boolean registrarGuardia(String primerNombre, String segundoNombre, 
            String primerApellido, String segundoApellido, int edad, 
            String cedula, String nacionalidad, String correo, 
            String turno, LocalDate fechaFinContrato, String cargo, 
            File imagen) {
        
        return guardiaDAO.guardarGuardia(
            primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, cedula, nacionalidad, correo, turno, fechaFinContrato, 
            cargo, imagen
        );
    }
    
    public boolean modificarGuardia(String cedula, String primerNombre, 
            String segundoNombre, String primerApellido, String segundoApellido, 
            int edad, String nacionalidad, String correo, String turno, 
            LocalDate fechaFinContrato, String cargo, File nuevaImagen) {
        
        return guardiaDAO.modificarGuardia(
            cedula, primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, nacionalidad, correo, turno, fechaFinContrato, cargo, nuevaImagen
        );
    }
    
    public boolean eliminarGuardia(String cedula) {
        return guardiaDAO.eliminarGuardia(cedula);
    }
    
    // Consultas
    public Guardia obtenerGuardiaPorCedula(String cedula) {
        return guardiaDAO.obtenerGuardiaPorCedula(cedula);
    }
    
    public List<Guardia> listarTodosGuardias() {
        return guardiaDAO.obtenerGuardias();
    }
    
    public boolean existeGuardia(String cedula) {
        return guardiaDAO.existeGuardia(cedula);
    }
    
    // Métodos para la tabla
    public void cargarDatosEnTabla(JTable tabla) {
        DefaultTableModel modelo = crearModeloTabla();
        tabla.setModel(modelo);
        // Aquí puedes agregar configuración adicional de la tabla si es necesario
    }
    
    private DefaultTableModel crearModeloTabla() {
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
        };
        
        List<Guardia> guardias = listarTodosGuardias();
        for (Guardia guardia : guardias) {
            modelo.addRow(new Object[]{
                guardia.getRutaImagen(), // Asume que hay un método en Guardia para obtener ImageIcon
                guardia.getNombresCompletos(),
                guardia.getApellidosCompletos(),
                guardia.getEdad(),
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
}