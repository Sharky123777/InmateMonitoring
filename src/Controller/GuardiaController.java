package Controller;

import DAO.GuardiaDAO;
import Model.Entities.Guardia;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class GuardiaController {
    private static GuardiaController instancia;
    private final GuardiaDAO guardiaDAO;
    
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

    // ==================== OPERACIONES CRUD ====================

    public boolean agregarGuardia(String primerNombre, String segundoNombre,
                                String primerApellido, String segundoApellido,
                                int edad, String cedula, String nacionalidad,
                                String correo, String turno, String cargo,
                                LocalDate fechaFinContrato, File imagen) {
        try {
            // Validaciones antes de proceder
            validarCamposObligatorios(primerNombre, primerApellido, cedula, 
                                    nacionalidad, correo, turno, cargo);
            validarEdad(edad);
            validarCedula(cedula);
            validarCorreo(correo);
            validarFechasContrato(LocalDate.now(), fechaFinContrato);
            validarImagen(imagen);

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
            throw e; // Re-lanzar validaciones
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar guardia: " + e.getMessage(), e);
        }
    }

    public boolean modificarGuardia(String cedulaOriginal, String primerNombre, 
                                  String segundoNombre, String primerApellido,
                                  String segundoApellido, int edad, 
                                  String nacionalidad, String correo, String turno,
                                  LocalDate fechaFinContrato, String cargo, 
                                  File nuevaImagen) {
        try {
            // Validaciones
            validarCamposObligatorios(primerNombre, primerApellido, cedulaOriginal, 
                                    nacionalidad, correo, turno, cargo);
            validarEdad(edad);
            validarCorreo(correo);
            
            // Obtener guardia original para validar fechas
            Guardia original = obtenerGuardiaPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Guardia no encontrado con cédula: " + cedulaOriginal);
            }
            
            validarFechasContrato(original.getFechaInicioContrato(), fechaFinContrato);
            
            // Validar imagen solo si se proporciona una nueva
            if (nuevaImagen != null) {
                validarImagen(nuevaImagen);
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
            throw e; // Re-lanzar validaciones
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar guardia: " + e.getMessage(), e);
        }
    }

    public boolean eliminarGuardia(String cedula) {
        try {
            // Validar que el guardia existe
            if (!guardiaDAO.existeGuardia(cedula)) {
                throw new IllegalArgumentException("No existe un guardia con la cédula proporcionada");
            }
            
            return guardiaDAO.eliminarGuardia(cedula);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar guardia: " + e.getMessage(), e);
        }
    }

    // ==================== MÉTODOS DE CONSULTA ====================

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

    // ==================== MÉTODOS DE VALIDACIÓN ====================

    private void validarCamposObligatorios(String primerNombre, String primerApellido,
                                         String cedula, String nacionalidad,
                                         String correo, String turno, String cargo) {
        StringBuilder errores = new StringBuilder();

        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            errores.append("- Primer nombre es obligatorio\n");
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            errores.append("- Primer apellido es obligatorio\n");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            errores.append("- Cédula es obligatoria\n");
        }
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            errores.append("- Nacionalidad es obligatoria\n");
        }
        if (correo == null || correo.trim().isEmpty()) {
            errores.append("- Correo es obligatorio\n");
        }
        if (turno == null || turno.trim().isEmpty()) {
            errores.append("- Turno es obligatorio\n");
        }
        if (cargo == null || cargo.trim().isEmpty()) {
            errores.append("- Cargo es obligatorio\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    private void validarEdad(int edad) {
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        }
    }

    private void validarCedula(String cedula) {
        if (!cedula.matches("\\d{10}")) {
            throw new IllegalArgumentException("La cédula debe tener 10 dígitos numéricos");
        }
    }

    private void validarCorreo(String correo) {
        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
        }
    }

    // Modifica el método validarFechasContrato
private void validarFechasContrato(LocalDate inicio, LocalDate fin) {
    if (fin == null) {
        throw new IllegalArgumentException("La fecha de fin de contrato es obligatoria");
    }

    // Validación más flexible que permite el mismo día como fecha mínima
    if (fin.isBefore(inicio)) {
        throw new IllegalArgumentException(
            String.format("La fecha de fin (%s) debe ser igual o posterior a la fecha de inicio (%s)",
                fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        );
    }
}

    private void validarImagen(File imagen) {
        // Permitir imagen nula (opcional)
        if (imagen == null) {
            return;
        }

        if (!imagen.exists()) {
            throw new IllegalArgumentException("El archivo de imagen no existe");
        }

        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg")
                && !nombre.endsWith(".png") && !nombre.endsWith(".gif")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, PNG o GIF");
        }
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
}