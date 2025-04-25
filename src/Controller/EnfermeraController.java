/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.EnfermeraDAO;
import Model.Enfermera;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class EnfermeraController {
    private final EnfermeraDAO enfermeraDAO;
    
    public EnfermeraController() {
        this.enfermeraDAO = new EnfermeraDAO();
    }
    
    // Operaciones CRUD
    public boolean registrarEnfermera(Enfermera enfermera, File imagen) {
        try {
            return enfermeraDAO.guardarEnfermera(enfermera, imagen);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean modificarEnfermera(String cedulaOriginal, Enfermera enfermeraModificada, File nuevaImagen) {
        return enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermeraModificada, nuevaImagen);
    }
    
    public boolean eliminarEnfermera(String identificacion) {
        return enfermeraDAO.eliminarEnfermera(identificacion);
    }
    
    // Consultas
    public Enfermera obtenerEnfermeraPorCedula(String cedula) {
        return enfermeraDAO.obtenerEnfermeraPorIdentificacion(cedula);
    }
    
    public Enfermera obtenerEnfermeraPorTurno(String turno) {
        return enfermeraDAO.obtenerEnfermeraPorTurno(turno);
    }
    
    public List<Enfermera> listarTodasEnfermeras() {
        return enfermeraDAO.obtenerEnfermeras();
    }
    
    public boolean existeEnfermera(String cedula) {
        return enfermeraDAO.existeEnfermera(cedula);
    }
    
public void cargarDatosEnTabla(JTable tabla) {
    if (tabla == null) return;
    
    try {
        List<Object[]> datos = enfermeraDAO.obtenerDatosEnfermerasParaTabla();
        if (datos == null) datos = new ArrayList<>();
        
        Object[][] datosArray = datos.toArray(new Object[0][]);
        String[] columnas = enfermeraDAO.getNombresColumnas();
        
        DefaultTableModel modelo = new DefaultTableModel(datosArray, columnas) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                Class<?>[] tipos = enfermeraDAO.getTiposColumnas();
                return (columnIndex >= 0 && columnIndex < tipos.length) 
                    ? tipos[columnIndex] 
                    : Object.class;
            }
        };
        
        tabla.setModel(modelo);
    } catch (Exception e) {
        // Manejar el error adecuadamente (log, mostrar mensaje, etc.)
        e.printStackTrace();
    }
}
    
    // Método para construir objeto Enfermera desde la vista
    public Enfermera crearEnfermeraDesdeFormulario(
            String primerNombre, String segundoNombre, 
            String primerApellido, String segundoApellido,
            int edad, String cedula, String nacionalidad,
            String correo, String turno, LocalDate fechaFinContrato) {
        
        return new Enfermera(
            primerNombre,
            segundoNombre.isEmpty() ? null : segundoNombre,
            primerApellido,
            segundoApellido,
            edad,
            "Femenino", // Por requisito del sistema
            nacionalidad,
            cedula,
            turno,
            LocalDate.now(), // Fecha de contratación es hoy
            fechaFinContrato,
            correo
        );
    }
}