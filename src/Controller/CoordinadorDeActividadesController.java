package Controller;

import DAO.CoordinadorDeActividadesDAO;
import Model.Entities.CoordinadorDeActividades;
import Model.Entities.Usuario;
import Model.Constants.RolEnum;
import View.FrmCamara;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CoordinadorDeActividadesController {
    private static CoordinadorDeActividadesController instancia;
    private final CoordinadorDeActividadesDAO coordinadorDAO;
    private FrmCamara ventanaCamara;

    private CoordinadorDeActividadesController() {
        this.coordinadorDAO = CoordinadorDeActividadesDAO.getInstancia();
    }

    public static synchronized CoordinadorDeActividadesController getInstancia() {
        if (instancia == null) {
            instancia = new CoordinadorDeActividadesController();
        }
        return instancia;
    }
    
    public File capturarImagenCoordinador() {
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
            JOptionPane.showMessageDialog(null, 
                "Error al capturar imagen: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    // Obtener la imagen capturada
    File imagen = ventanaCamara.getImagenCapturada();
    
    // Validar la imagen obtenida
    if (imagen == null) {
        JOptionPane.showMessageDialog(null, 
            "No se capturó ninguna imagen",
            "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }
    
    if (!imagen.exists()) {
        JOptionPane.showMessageDialog(null, 
            "El archivo de imagen no existe",
            "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }
    
    // Verificar que sea una imagen válida
    try {
        BufferedImage img = ImageIO.read(imagen);
        if (img == null) {
            JOptionPane.showMessageDialog(null, 
                "El archivo no es una imagen válida",
                "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al leer la imagen: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }
    
    return imagen;
}

    public void registrarCoordinador(String primerNombre, String segundoNombre,
        String primerApellido, String segundoApellido, int edad, String cedula,
        String nacionalidad, String correo, String turno, String cargo,
        LocalDate fechaFinContrato, File imagenSeleccionadaCDA) throws IOException {
    
    // Validación de campos obligatorios
    validarCamposObligatorios(primerNombre, primerApellido, segundoApellido, 
            edad, cedula, nacionalidad, correo, turno, cargo);
    validarEdad(edad);
    validarFechasContrato(LocalDate.now(), fechaFinContrato);
    
    // Validación de imagen obligatoria
    if (imagenSeleccionadaCDA == null) {
        throw new IllegalArgumentException("Debe proporcionar una imagen del coordinador");
    }
    
    if (!imagenSeleccionadaCDA.exists()) {
        throw new IllegalArgumentException("El archivo de imagen no existe: " + imagenSeleccionadaCDA.getAbsolutePath());
    }
    
    // Verificar que sea una imagen válida
    try {
        BufferedImage img = ImageIO.read(imagenSeleccionadaCDA);
        if (img == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida");
        }
    } catch (IOException e) {
        throw new IllegalArgumentException("Error al leer la imagen: " + e.getMessage());
    }

    CoordinadorDeActividades nuevoCoordinador = new CoordinadorDeActividades(
        primerNombre, segundoNombre, primerApellido, segundoApellido,
        edad, "Femenino", nacionalidad, cedula, correo, turno,
        LocalDate.now(), fechaFinContrato, "", "", cargo
    );

    if (!coordinadorDAO.guardarCoordinador(nuevoCoordinador, imagenSeleccionadaCDA)) {
        throw new RuntimeException("No se pudo guardar el coordinador");
    }
}

    // Método modificado para modificar coordinador con mejores validaciones
public boolean modificarCoordinador(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
    try {
        // 1. Validar existencia del coordinador original
        CoordinadorDeActividades original = validarYObternerOriginal(cedulaOriginal);
        
        // 2. Validar campos básicos
        validarCamposModificados(cambios);
        
        // 3. Validar edad
        int edad = validarEdad((int) cambios.get("edad"));
        
        // 4. Validar fechas
        LocalDate fechaFin = validarFechas(original.getFechaInicioContrato(), (LocalDate) cambios.get("fechaFin"));
        
        // 5. Validar imagen si se proporciona una nueva
        if (nuevaImagen != null) {
            validarImagen(nuevaImagen);
        }
        
        // 6. Crear objeto modificado
        CoordinadorDeActividades coordinadorModificado = crearCoordinadorModificado(
            original, cambios, edad, fechaFin);
        
        // 7. Determinar qué imagen usar
        File imagenFinal = determinarImagenFinal(original, nuevaImagen);
        
        // 8. Ejecutar modificación en DAO
        return coordinadorDAO.modificarCoordinador(cedulaOriginal, coordinadorModificado, imagenFinal);
        
    } catch (IllegalArgumentException e) {
        throw e; // Relanzar excepciones de validación
    } catch (Exception e) {
        throw new RuntimeException("Error al modificar coordinador: " + e.getMessage(), e);
    }
}

// Métodos auxiliares de validación mejorados
private CoordinadorDeActividades validarYObternerOriginal(String cedula) {
    CoordinadorDeActividades original = obtenerCoordinadorPorCedula(cedula);
    if (original == null) {
        throw new IllegalArgumentException("Coordinador no encontrado con cédula: " + cedula);
    }
    return original;
}

private void validarCamposModificados(Map<String, Object> cambios) {
    StringBuilder errores = new StringBuilder();
    
    // Validar que los campos obligatorios no estén vacíos
    validarCampoObligatorio(cambios, "primerNombre", "Primer nombre", errores);
    validarCampoObligatorio(cambios, "primerApellido", "Primer apellido", errores);
    validarCampoObligatorio(cambios, "segundoApellido", "Segundo apellido", errores);
    validarCampoObligatorio(cambios, "nacionalidad", "Nacionalidad", errores);
    validarCampoObligatorio(cambios, "correo", "Correo electrónico", errores);
    validarCampoObligatorio(cambios, "cargo", "Cargo", errores);
    
    // Validación específica para correo electrónico
    if (cambios.containsKey("correo") && !((String)cambios.get("correo")).contains("@")) {
        errores.append("- El correo electrónico debe tener un formato válido\n");
    }
    
    if (errores.length() > 0) {
        throw new IllegalArgumentException(errores.toString());
    }
}

private void validarCampoObligatorio(Map<String, Object> cambios, String campo, String nombreCampo, StringBuilder errores) {
    if (!cambios.containsKey(campo)) {
        errores.append("- ").append(nombreCampo).append(" es un campo requerido\n");
    } else if (cambios.get(campo) instanceof String && ((String)cambios.get(campo)).trim().isEmpty()) {
        errores.append("- ").append(nombreCampo).append(" es obligatorio\n");
    } else if (cambios.get(campo) == null) {
        errores.append("- ").append(nombreCampo).append(" no puede ser nulo\n");
    }
}

private int validarEdad(int edad) {
    if (edad < 0) {
        throw new IllegalArgumentException("La edad debe ser un número positivo");
    }
    if (edad < 18 || edad > 70) {
        throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
    }
    return edad;
}

private LocalDate validarFechas(LocalDate inicio, LocalDate fin) {
    if (fin == null) {
        throw new IllegalArgumentException("La fecha de fin de contrato es obligatoria");
    }
    if (!fin.isAfter(inicio)) {
        throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
    }
    return fin;
}

private CoordinadorDeActividades crearCoordinadorModificado(CoordinadorDeActividades original, 
    Map<String, Object> cambios, int edad, LocalDate fechaFin) {
    
    return new CoordinadorDeActividades(
        (String) cambios.get("primerNombre"),
        (String) cambios.getOrDefault("segundoNombre", original.getSegundoNombre()),
        (String) cambios.get("primerApellido"),
        (String) cambios.get("segundoApellido"),
        edad,
        original.getSexo(), // Mantener el valor original
        (String) cambios.get("nacionalidad"),
        original.getIdentificacion(), // Mantener la misma cédula
        (String) cambios.get("correo"),
        (String) cambios.get("turno"),
        original.getFechaInicioContrato(), // Mantener fecha original
        fechaFin,
        original.getUsuario(), // Credenciales originales
        original.getContrasena(),
        (String) cambios.get("cargo")
    );
}

private File determinarImagenFinal(CoordinadorDeActividades original, File nuevaImagen) {
    if (nuevaImagen != null) {
        return nuevaImagen;
    }
    if (original.getRutaImagen() != null && !original.getRutaImagen().isEmpty()) {
        return new File(original.getRutaImagen());
    }
    return null;
}


    // Métodos de consulta
    public List<CoordinadorDeActividades> obtenerTodosCoordinadores() {
        return coordinadorDAO.obtenerCoordinadores();
    }

    public CoordinadorDeActividades obtenerCoordinadorPorCedula(String cedula) {
        return coordinadorDAO.obtenerCoordinadorPorCedula(cedula);
    }

    public boolean eliminarCoordinador(String cedula) {
        return coordinadorDAO.eliminarCoordinador(cedula);
    }

    public CoordinadorDeActividades obtenerCoordinadorPorUsuario(String usuario) {
        return coordinadorDAO.obtenerCoordinadorPorUsuario(usuario);
    }

    // Métodos de validación
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
        if (((String) cambios.get("cargo")).trim().isEmpty()) {
            errores.append("- Cargo es obligatorio\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    private void validarCamposObligatorios(String primerNombre, String primerApellido,
            String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, String cargo) {
        
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
            throw new IllegalArgumentException("Campos obligatorios faltantes:\n" + camposFaltantes);
        }
    }

    private void validarFechasContrato(LocalDate inicio, LocalDate fin) {
        if (fin == null) {
            throw new IllegalArgumentException("La fecha de fin de contrato es obligatoria");
        }

        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    

   private void validarImagen(File imagen) {
    if (imagen == null) {
        throw new IllegalArgumentException("Debe proporcionar una imagen del coordinador");
    }
    
    if (!imagen.exists()) {
        throw new IllegalArgumentException("El archivo de imagen no existe: " + imagen.getAbsolutePath());
    }
    
    // Verificar que sea un archivo de imagen válido
    try {
        BufferedImage img = ImageIO.read(imagen);
        if (img == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida");
        }
    } catch (IOException e) {
        throw new IllegalArgumentException("Error al leer la imagen: " + e.getMessage());
    }
    
    // Verificar extensión del archivo
    String nombre = imagen.getName().toLowerCase();
    if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
        throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
    }
    
    // Verificar tamaño máximo (opcional)
    long maxSize = 5 * 1024 * 1024; // 5MB
    if (imagen.length() > maxSize) {
        throw new IllegalArgumentException("La imagen es demasiado grande (máximo 5MB)");
    }
}
}