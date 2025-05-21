package Controller;

import DAO.EnfermeraDAO;
import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.Enfermera;
import Model.Entities.SincronizadorJson;
import Model.Entities.Usuario;
import Utilidades.EmailSender;
import Utilidades.GeneradorCredenciales;
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
                return null;
            }
        }

        return ventanaCamara.getImagenCapturada();
    }

    public Enfermera registrarEnfermera(String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, LocalDate fechaFinContrato,
            File imagen) throws IOException {

        try {
            
            if (enfermeraDAO.existeEnfermeraConCedula(cedula)) {
                throw new IllegalArgumentException("Ya existe una enfermera con la cédula " + cedula);
            }

            
            validarCamposObligatorios(primerNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno);

            
            validarEdad(edad);
            validarFechasContrato(LocalDate.now(), fechaFinContrato);
            validarLimiteEnfermerasPorTurno(turno, null);
            validarCedula(cedula);
            validarImagen(imagen);

            
            Enfermera nuevaEnfermera = new Enfermera(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, "Femenino", nacionalidad, cedula, turno,
                    LocalDate.now(), fechaFinContrato, correo, "", ""
            );

            
            boolean guardado = EnfermeraDAO.getInstancia().guardarEnfermera(nuevaEnfermera, imagen);

            if (guardado) {
                return EnfermeraDAO.getInstancia().obtenerEnfermeraPorCedula(cedula);
            }
            throw new RuntimeException("No se pudo guardar la enfermera en la base de datos");

        } catch (IllegalArgumentException e) {
           
            throw e;
        } catch (Exception e) {
            
            throw new RuntimeException("Error al registrar enfermera: " + e.getMessage(), e);
        }
    }

    private void validarImagen(File imagen) {
        if (imagen == null || !imagen.exists()) {
            throw new IllegalArgumentException("Debe proporcionar una imagen válida de la enfermera");
        }

        
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }
    }
    
    private void validarCedula(String cedula) {
    if (cedula.length() < 8 || cedula.length() > 10) {
        throw new IllegalArgumentException("La cédula debe tener entre 8 y 10 dígitos");
    }
}

public int modificarEnfermera(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
    try {
        Enfermera original = obtenerEnfermeraPorCedula(cedulaOriginal);
        if (original == null) {
            throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedulaOriginal);
        }

        if (!verificarCambios(original, cambios, nuevaImagen)) {
            return 0; // No hay cambios
        }

        validarCamposModificacion(cambios);

        int edad = (int) cambios.get("edad");
        LocalDate fechaFin = (LocalDate) cambios.get("fechaFin");
        String turno = (String) cambios.get("turno");

        validarEdad(edad);
        validarFechasContrato(original.getFechaContratacion(), fechaFin);
        validarLimiteEnfermerasPorTurno(turno, cedulaOriginal);

        Enfermera enfermeraModificada = construirEnfermeraModificada(cedulaOriginal, cambios, original);

        boolean resultado = enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermeraModificada, nuevaImagen);
        
        if (!resultado) {
            return -1; // Error en la modificación
        }

        // Sincronizar con el JSON de usuarios después de modificar exitosamente
        SincronizadorJson.sincronizarConUsuarios(enfermeraModificada);

        return 1; // Éxito

    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error al modificar enfermera: " + e.getMessage(), e);
    }
}

    public int modificarEnfermeraConCredenciales(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
    try {
        Enfermera original = obtenerEnfermeraPorCedula(cedulaOriginal);
        if (original == null) {
            throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedulaOriginal);
        }

        Enfermera modificada = new Enfermera(
                cambios.getOrDefault("primerNombre", original.getPrimerNombre()).toString(),
                cambios.getOrDefault("segundoNombre", original.getSegundoNombre()).toString(),
                cambios.getOrDefault("primerApellido", original.getPrimerApellido()).toString(),
                cambios.getOrDefault("segundoApellido", original.getSegundoApellido()).toString(),
                cambios.containsKey("edad") ? Integer.parseInt(cambios.get("edad").toString()) : original.getEdad(),
                "Femenino", // Sexo fijo para enfermeras
                cambios.getOrDefault("nacionalidad", original.getNacionalidad()).toString(),
                cambios.getOrDefault("identificacion", original.getIdentificacion()).toString(),
                cambios.getOrDefault("turno", original.getTurno()).toString(),
                original.getFechaContratacion(),
                cambios.containsKey("fechaFin") ? (LocalDate) cambios.get("fechaFin") : original.getFechaFinContrato(),
                cambios.getOrDefault("correo", original.getCorreo()).toString(),
                cambios.getOrDefault("nuevoUsuario", original.getUsuario()).toString(),
                cambios.containsKey("nuevaContraseña") ? cambios.get("nuevaContraseña").toString() : original.getContrasena()
        );

        boolean exitoEnfermera = enfermeraDAO.modificarEnfermera(cedulaOriginal, modificada, nuevaImagen);
        if (!exitoEnfermera) {
            return -1;
        }

        SincronizadorJson.sincronizarConUsuarios(modificada);

        if (cambios.containsKey("nuevoUsuario") || cambios.containsKey("nuevaContraseña")) {
            boolean credencialesOk = UsuarioDAO.getInstancia().modificarCredenciales(
                    original.getUsuario(),
                    cambios.containsKey("nuevoUsuario") ? cambios.get("nuevoUsuario").toString() : null,
                    cambios.containsKey("nuevaContraseña") ? cambios.get("nuevaContraseña").toString() : null,
                    original.getCorreo(),
                    RolEnum.ENFERMERA
            );

            if (!credencialesOk) {
                return -1;
            }
        }

        return 1;

    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error al modificar enfermera: " + e.getMessage(), e);
    }
}

public int modificarCredencialesEnfermera(String cedulaOriginal, String nuevoUsuario, String nuevaContra) {
    try {
        Enfermera enfermera = obtenerEnfermeraPorCedula(cedulaOriginal);
        if (enfermera == null) {
            throw new IllegalArgumentException("Enfermera no encontrada");
        }

        nuevoUsuario = nuevoUsuario != null ? nuevoUsuario.toLowerCase() : null;
        
        boolean cambioUsuario = nuevoUsuario != null && !nuevoUsuario.equals(enfermera.getUsuario().toLowerCase());
        boolean cambioContra = nuevaContra != null && !nuevaContra.isEmpty();

        if (!cambioUsuario && !cambioContra) {
            return 0;
        }

        if (cambioContra) {
            enfermera.setContrasena(GeneradorCredenciales.encriptarContrasena(nuevaContra));
        }
        if (cambioUsuario) {
            enfermera.setUsuario(nuevoUsuario);
        }

        boolean exito = enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermera, null);
        if (!exito) {
            return -1;
        }

        SincronizadorJson.sincronizarConUsuarios(enfermera);

        boolean credencialesOk = UsuarioDAO.getInstancia().modificarCredenciales(
                enfermera.getUsuario(),
                cambioUsuario ? nuevoUsuario : null,
                cambioContra ? nuevaContra : null,
                enfermera.getCorreo(),
                RolEnum.ENFERMERA
        );

        return credencialesOk ? 1 : -1;
    } catch (Exception e) {
        throw new RuntimeException("Error al modificar credenciales de enfermera: " + e.getMessage(), e);
    }
}

    private boolean verificarCambios(Enfermera original, Map<String, Object> cambios, File nuevaImagen) {
        
        if (!original.getPrimerNombre().equals(cambios.get("primerNombre"))) {
            return true;
        }
        if (!Objects.equals(original.getSegundoNombre(), cambios.get("segundoNombre"))) {
            return true;
        }
        if (!original.getPrimerApellido().equals(cambios.get("primerApellido"))) {
            return true;
        }
        if (!original.getSegundoApellido().equals(cambios.get("segundoApellido"))) {
            return true;
        }
        if (original.getEdad() != (int) cambios.get("edad")) {
            return true;
        }
        if (!original.getNacionalidad().equals(cambios.get("nacionalidad"))) {
            return true;
        }
        if (!original.getCorreo().equals(cambios.get("correo"))) {
            return true;
        }
        if (!original.getTurno().equals(cambios.get("turno"))) {
            return true;
        }
        if (!original.getFechaFinContrato().equals(cambios.get("fechaFin"))) {
            return true;
        }

  
        return nuevaImagen != null;
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

        List<String> errores = new ArrayList<>();

        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            errores.add("Primer nombre es obligatorio");
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            errores.add("Primer apellido es obligatorio");
        }
        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            errores.add("Segundo apellido es obligatorio");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            errores.add("Cédula es obligatoria");
        }
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            errores.add("Nacionalidad es obligatoria");
        }
        if (correo == null || correo.trim().isEmpty()) {
            errores.add("Correo es obligatorio");
        } else if (!correo.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$")) {
            errores.add("Correo electrónico no válido");
        }
        if (turno == null || turno.trim().isEmpty()) {
            errores.add("Turno es obligatorio");
        }
        if (edad <= 0) {
            errores.add("Edad debe ser un número positivo");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("Errores de validación:\n- "
                    + String.join("\n- ", errores));
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
        if (edad < 0) {
            throw new IllegalArgumentException("La edad debe ser un número valido");
        }
    }

}
