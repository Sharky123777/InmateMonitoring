package Controller;

import DAO.EnfermeraDAO;
import Model.Entities.Enfermera;
import View.FrmCamara;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EnfermeraController {
    private static EnfermeraController instancia;
    private final EnfermeraDAO enfermeraDAO;
    private FrmCamara ventanaCamara;

    private EnfermeraController() {
        this.enfermeraDAO = EnfermeraDAO.getInstancia();
    }

    public static synchronized EnfermeraController getInstancia() {
        if (instancia == null) {
            instancia = new EnfermeraController();
        }
        return instancia;
    }
    
    public File capturarImagenEnfermera() {
    FrmCamara ventanaCamara = new FrmCamara();
    ventanaCamara.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    // Mostrar como diálogo modal
    JDialog dialog = new JDialog();
    dialog.setModal(true);
    dialog.setContentPane(ventanaCamara.getContentPane());
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
    
    // Esperar hasta que se cierre
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

   public Enfermera registrarEnfermera(String primerNombre, String segundoNombre,
    String primerApellido, String segundoApellido, int edad, String cedula,
    String nacionalidad, String correo, String turno, LocalDate fechaFinContrato,
    File imagen) throws IOException {
    
    // Validaciones (todas las reglas de negocio aquí)
    validarCamposObligatorios(primerNombre, primerApellido, segundoApellido, 
            edad, cedula, nacionalidad, correo, turno);
    validarEdad(edad);
    validarFechasContrato(LocalDate.now(), fechaFinContrato);
    validarLimiteEnfermerasPorTurno(turno, null);
    validarImagen(imagen);

    // Crear nueva enfermera
    Enfermera nuevaEnfermera = new Enfermera(
        primerNombre, segundoNombre, primerApellido, segundoApellido,
        edad, "Femenino", nacionalidad, cedula, turno, 
        LocalDate.now(), fechaFinContrato, correo, "", ""
    );

    // Guardar a través del DAO (Singleton)
    boolean guardado = EnfermeraDAO.getInstancia().guardarEnfermera(nuevaEnfermera, imagen);
    
    if (guardado) {
        return EnfermeraDAO.getInstancia().obtenerEnfermeraPorCedula(cedula);
    }
    throw new RuntimeException("No se pudo guardar la enfermera");
}

private void validarImagen(File imagen) {
    if (imagen == null || !imagen.exists()) {
        throw new IllegalArgumentException("Debe proporcionar una imagen válida de la enfermera");
    }
    
    // Validar extensión del archivo
    String nombre = imagen.getName().toLowerCase();
    if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
        throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
    }
}
    
    public boolean modificarEnfermera(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
    try {
        Enfermera original = obtenerEnfermeraPorCedula(cedulaOriginal);
        if (original == null) {
            throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedulaOriginal);
        }

        // Validar campos
        validarCamposModificacion(cambios);
        validarEdad((int) cambios.get("edad"));
        validarFechasContrato(original.getFechaContratacion(), (LocalDate) cambios.get("fechaFin"));
        validarLimiteEnfermerasPorTurno((String) cambios.get("turno"), cedulaOriginal);

        // Construir enfermera modificada manteniendo credenciales
        Enfermera enfermeraModificada = new Enfermera(
            (String) cambios.get("primerNombre"),
            (String) cambios.get("segundoNombre"),
            (String) cambios.get("primerApellido"),
            (String) cambios.get("segundoApellido"),
            (int) cambios.get("edad"),
            "Femenino",
            (String) cambios.get("nacionalidad"),
            cedulaOriginal, // Mantener la cédula original
            (String) cambios.get("turno"),
            original.getFechaContratacion(), // Mantener fecha original
            (LocalDate) cambios.get("fechaFin"),
            (String) cambios.get("correo"),
            original.getUsuario(), // Mantener usuario
            original.getContrasena() // Mantener contraseña
        );

        // Pasar la nueva imagen si existe, sino mantener la original
        File imagenFinal = (nuevaImagen != null) ? nuevaImagen : 
                         (original.getRutaImagen() != null && !original.getRutaImagen().isEmpty()) ? 
                         new File(original.getRutaImagen()) : null;

        return enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermeraModificada, imagenFinal);
    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error al modificar enfermera: " + e.getMessage(), e);
    }
}

    private boolean procesarModificacion(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        Enfermera original = obtenerEnfermeraPorCedula(cedulaOriginal);
        if (original == null) {
            throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedulaOriginal);
        }

        validarCamposModificacion(cambios);
        validarEdad((int) cambios.get("edad"));
        validarFechasContrato(original.getFechaContratacion(), (LocalDate) cambios.get("fechaFin"));
        validarLimiteEnfermerasPorTurno((String) cambios.get("turno"), cedulaOriginal);

        Enfermera enfermeraModificada = construirEnfermeraModificada(cedulaOriginal, cambios, original);
        
        return enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermeraModificada, nuevaImagen);
    }
    
    private Enfermera construirEnfermeraModificada(String cedulaOriginal, 
            Map<String, Object> cambios, Enfermera original) {
        
        return new Enfermera(
            (String) cambios.get("primerNombre"),
            (String) cambios.get("segundoNombre"),
            (String) cambios.get("primerApellido"),
            (String) cambios.get("segundoApellido"),
            (int) cambios.get("edad"),
            "Femenino",
            (String) cambios.get("nacionalidad"),
            cedulaOriginal,
            (String) cambios.get("turno"),
            original.getFechaContratacion(),
            (LocalDate) cambios.get("fechaFin"),
            (String) cambios.get("correo"),
            original.getUsuario(),
            original.getContrasena()
        );
    }

    private File validarYProcesarImagen(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen de la enfermera");
        }

        File imagen = new File(rutaImagen);
        validarImagen(imagen);
        return imagen;
    }

    // Métodos de consulta
    public List<Enfermera> obtenerTodasEnfermeras() {
        return enfermeraDAO.obtenerEnfermeras();
    }

   public Enfermera obtenerEnfermeraPorCedula(String cedula) {
        return enfermeraDAO.obtenerEnfermeraPorIdentificacion(cedula);
    }

    public boolean eliminarEnfermera(String cedula) {
        return enfermeraDAO.eliminarEnfermera(cedula);
    }

    public Enfermera obtenerEnfermeraPorUsuario(String usuario) {
        return enfermeraDAO.obtenerEnfermeraPorUsuario(usuario);
    }

    // Métodos de validación
    private void validarLimiteEnfermerasPorTurno(String turno, String cedulaOriginal) {
        List<Enfermera> enfermeras = enfermeraDAO.obtenerEnfermeras();
        long count = enfermeras.stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .filter(e -> cedulaOriginal == null || !e.getIdentificacion().equals(cedulaOriginal))
                .count();
        
        if (count >= 2) {
            throw new IllegalArgumentException("Ya existen 2 enfermeras en el turno " + turno);
        }
        
        if (enfermeras.size() >= 4 && cedulaOriginal == null) {
            throw new IllegalArgumentException("Límite máximo de 4 enfermeras alcanzado");
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

    private void validarCamposObligatorios(String primerNombre, String primerApellido,
            String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno) {
        
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

    private void validarEdad(int edad) {
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        }
    }
    
    
}