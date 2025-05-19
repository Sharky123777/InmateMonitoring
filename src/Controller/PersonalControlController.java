package Controller;

import DAO.PersonalControlDAO;
import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.PersonalControl;
import Model.Entities.Usuario;
import Utilidades.EmailSender;
import Utilidades.GeneradorCredenciales;
import View.FrmCamara;
import com.itextpdf.text.log.Logger;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger.Level;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PersonalControlController {

    private static PersonalControlController instancia;
    private final PersonalControlDAO personalControlDAO;
    private FrmCamara ventanaCamara;
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstancia();

    private PersonalControlController() {
        this.personalControlDAO = PersonalControlDAO.getInstancia();
    }

    public static synchronized PersonalControlController getInstancia() {
        if (instancia == null) {
            instancia = new PersonalControlController();
        }
        return instancia;
    }

    public File capturarImagenPersonalControl() {
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

    public PersonalControl registrarPersonalControl(String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, LocalDate fechaFinContrato,
            File imagen) throws IOException {

        try {
            if (personalControlDAO.existePersonalControlConCedula(cedula)) {
                throw new IllegalArgumentException("Ya existe un personal de control con la cédula " + cedula);
            }

            validarCamposObligatorios(primerNombre, primerApellido, segundoApellido, edad, cedula, nacionalidad, correo, turno, fechaFinContrato);

            validarEdad(edad);
            validarCedula(cedula);
            validarFechasContrato(LocalDate.now(), fechaFinContrato);
            validarImagen(imagen);

            PersonalControl nuevoPersonalControl = new PersonalControl(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, "Femenino", nacionalidad, cedula, turno,
                    LocalDate.now(), fechaFinContrato, correo, "", ""
            );

            boolean guardado = PersonalControlDAO.getInstancia().guardarPersonalControl(nuevoPersonalControl, imagen);

            if (guardado) {
                return PersonalControlDAO.getInstancia().obtenerPersonalControlPorCedula(cedula);
            }
            throw new RuntimeException("No se pudo guardar el personal de control en la base de datos");

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar personal de control: " + e.getMessage(), e);
        }
    }

    public File capturarImagenPDC() {
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

    private void validarImagen(File imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Debe proporcionar una imagen del personal de control");
        }

        if (!imagen.exists()) {
            throw new IllegalArgumentException("El archivo de imagen no existe en la ruta especificada");
        }

        if (imagen.length() == 0) {
            throw new IllegalArgumentException("El archivo de imagen está vacío");
        }

        if (imagen.length() > 5 * 1024 * 1024) { // 5 MB máximo
            throw new IllegalArgumentException("La imagen no puede exceder los 5MB de tamaño");
        }

        String nombre = imagen.getName().toLowerCase();
        if (!nombre.matches("^.+\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }
    }

    public int modificarPersonalControl(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {

            PersonalControl original = obtenerPersonalControlPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Personal de control no encontrado");
            }

            validarCamposModificacion(cambios);

            int edad = (int) cambios.get("edad");
            validarEdad(edad);

            LocalDate fechaFin = (LocalDate) cambios.get("fechaFin");
            validarFechasContrato(original.getFechaContratacion(), fechaFin);

            String correo = (String) cambios.get("correo");
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                throw new IllegalArgumentException("Correo electrónico no válido");
            }

            String turno = (String) cambios.get("turno");
            if (!turno.equalsIgnoreCase("Diurno") && !turno.equalsIgnoreCase("Nocturno")) {
                throw new IllegalArgumentException("Turno debe ser 'Diurno' o 'Nocturno'");
            }

            if (nuevaImagen != null) {
                validarImagen(nuevaImagen);
            }

            boolean hayCambios = verificarCambios(original, cambios, nuevaImagen);
            if (!hayCambios) {
                System.out.println("No hay cambios reales - retornando 0");
                return 0;
            }

            boolean modificadoEnBD = personalControlDAO.modificarPersonalControl(
                    cedulaOriginal,
                    construirPersonalControlModificado(cedulaOriginal, cambios, original),
                    nuevaImagen
            );

            return modificadoEnBD ? 1 : -1;

        } catch (IllegalArgumentException e) {

            throw e;
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Error al modificar personal de control: " + e.getMessage(), e);
        }
    }

    private boolean verificarCambios(PersonalControl original, Map<String, Object> cambios, File nuevaImagen) {
        boolean hayCambios = false;

        if (!Objects.equals(original.getPrimerNombre(), cambios.get("primerNombre"))) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getSegundoNombre(), cambios.get("segundoNombre"))) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getPrimerApellido(), cambios.get("primerApellido"))) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getSegundoApellido(), cambios.get("segundoApellido"))) {
            hayCambios = true;
        }
        if (original.getEdad() != (int) cambios.get("edad")) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getNacionalidad(), cambios.get("nacionalidad"))) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getCorreo(), cambios.get("correo"))) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getTurno(), cambios.get("turno"))) {
            hayCambios = true;
        }
        if (!Objects.equals(original.getFechaFinContrato(), cambios.get("fechaFin"))) {
            hayCambios = true;
        }

        if (nuevaImagen != null) {
            // Siempre hay cambios si se proporciona una nueva imagen
            hayCambios = true;
        }

        return hayCambios;
    }

    private PersonalControl construirPersonalControlModificado(String cedulaOriginal,
            Map<String, Object> cambios, PersonalControl original) {

        return new PersonalControl(
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

    private void validarCedula(String cedula) {
        if (cedula.length() < 8 || cedula.length() > 10) {
            throw new IllegalArgumentException("La cédula debe tener entre 8 y 10 dígitos");
        }
    }

    public List<PersonalControl> obtenerTodosPersonalControl() {
        return personalControlDAO.obtenerPersonalControl();
    }

    public PersonalControl obtenerPersonalControlPorCedula(String cedula) {
        return personalControlDAO.obtenerPersonalControlPorIdentificacion(cedula);
    }

    public boolean eliminarPersonalControl(String cedula) {
        return personalControlDAO.eliminarPersonalControl(cedula);
    }

    public PersonalControl obtenerPersonalControlPorUsuario(String usuario) {
        return personalControlDAO.obtenerPersonalControlPorUsuario(usuario);
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
            String nacionalidad, String correo, String turno,
            LocalDate fechaFinContrato) {

        List<String> errores = new ArrayList<>();

        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            errores.add("Primer nombre es obligatorio");
        } else if (primerNombre.length() > 50) {
            errores.add("Primer nombre no puede exceder 50 caracteres");
        }

        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            errores.add("Primer apellido es obligatorio");
        } else if (primerApellido.length() > 50) {
            errores.add("Primer apellido no puede exceder 50 caracteres");
        }

        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            errores.add("Segundo apellido es obligatorio");
        } else if (segundoApellido.length() > 50) {
            errores.add("Segundo apellido no puede exceder 50 caracteres");
        }

        if (cedula == null || cedula.trim().isEmpty()) {
            errores.add("Cédula es obligatoria");
        } else if (!cedula.matches("^[0-9]{6,20}$")) {
            errores.add("Cédula debe contener solo números (6-20 dígitos)");
        }

        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            errores.add("Nacionalidad es obligatoria");
        } else if (nacionalidad.length() > 50) {
            errores.add("Nacionalidad no puede exceder 50 caracteres");
        }

        if (correo == null || correo.trim().isEmpty()) {
            errores.add("Correo es obligatorio");
        } else if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            errores.add("Correo electrónico no válido");
        } else if (correo.length() > 100) {
            errores.add("Correo no puede exceder 100 caracteres");
        }

        if (turno == null || turno.trim().isEmpty()) {
            errores.add("Turno es obligatorio");
        } else if (!turno.equalsIgnoreCase("Diurno") && !turno.equalsIgnoreCase("Nocturno")) {
            errores.add("Turno debe ser 'Diurno' o 'Nocturno'");
        }

        if (edad <= 0) {
            errores.add("Edad debe ser un número positivo");
        }

        if (fechaFinContrato == null) {
            errores.add("Fecha fin de contrato es obligatoria");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("Errores de validación:\n- " + String.join("\n- ", errores));
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
        if (edad < 18) {
            throw new IllegalArgumentException("La edad mínima es 18 años");
        }

        if (edad > 70) {
            throw new IllegalArgumentException("La edad máxima es 70 años");
        }

        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser un número positivo");
        }
    }

    public int modificarPersonalControlConCredenciales(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {
            PersonalControl original = obtenerPersonalControlPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Personal de control no encontrado con cédula: " + cedulaOriginal);
            }

            PersonalControl modificado = new PersonalControl(
                    cambios.getOrDefault("primerNombre", original.getPrimerNombre()).toString(),
                    cambios.getOrDefault("segundoNombre", original.getSegundoNombre()).toString(),
                    cambios.getOrDefault("primerApellido", original.getPrimerApellido()).toString(),
                    cambios.getOrDefault("segundoApellido", original.getSegundoApellido()).toString(),
                    cambios.containsKey("edad") ? Integer.parseInt(cambios.get("edad").toString()) : original.getEdad(),
                    original.getSexo(),
                    cambios.getOrDefault("nacionalidad", original.getNacionalidad()).toString(),
                    cambios.getOrDefault("identificacion", original.getIdentificacion()).toString(),
                    cambios.getOrDefault("turno", original.getTurno()).toString(),
                    original.getFechaContratacion(),
                    cambios.containsKey("fechaFin") ? (LocalDate) cambios.get("fechaFin") : original.getFechaFinContrato(),
                    cambios.getOrDefault("correo", original.getCorreo()).toString(),
                    cambios.getOrDefault("nuevoUsuario", original.getUsuario()).toString(),
                    cambios.containsKey("nuevaContraseña") ? cambios.get("nuevaContraseña").toString() : original.getContrasena()
            );

            boolean exito = personalControlDAO.modificarPersonalControl(cedulaOriginal, modificado, nuevaImagen);
            if (!exito) {
                return -1;
            }

            if (cambios.containsKey("nuevoUsuario") || cambios.containsKey("nuevaContraseña")) {
                boolean credencialesOk = UsuarioDAO.getInstancia().modificarCredenciales(
                        original.getUsuario(),
                        cambios.containsKey("nuevoUsuario") ? cambios.get("nuevoUsuario").toString() : null,
                        cambios.containsKey("nuevaContraseña") ? cambios.get("nuevaContraseña").toString() : null,
                        original.getCorreo(),
                        RolEnum.PERSONAL_DE_CONTROL
                );

                if (!credencialesOk) {
                    return -1;
                }
            }

            return 1;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar personal de control: " + e.getMessage(), e);
        }
    }

    public int modificarCredencialesPersonalControl(String cedulaOriginal, String nuevoUsuario, String nuevaContra) {
        try {
            PersonalControl pdc = obtenerPersonalControlPorCedula(cedulaOriginal);
            if (pdc == null) {
                throw new IllegalArgumentException("Personal de control no encontrado");
            }

            nuevoUsuario = nuevoUsuario != null ? nuevoUsuario.toLowerCase() : null;

            boolean cambioUsuario = nuevoUsuario != null && !nuevoUsuario.equalsIgnoreCase(pdc.getUsuario());
            boolean cambioContra = nuevaContra != null && !nuevaContra.isEmpty();

            if (!cambioUsuario && !cambioContra) {
                return 0;
            }

            if (cambioContra) {
                pdc.setContrasena(GeneradorCredenciales.encriptarContrasena(nuevaContra));
            }
            if (cambioUsuario) {
                pdc.setUsuario(nuevoUsuario);
            }

            boolean exito = personalControlDAO.modificarPersonalControl(cedulaOriginal, pdc, null);
            if (!exito) {
                return -1;
            }

            boolean credencialesOk = UsuarioDAO.getInstancia().modificarCredenciales(
                    pdc.getUsuario(),
                    cambioUsuario ? nuevoUsuario : null,
                    cambioContra ? nuevaContra : null,
                    pdc.getCorreo(),
                    RolEnum.PERSONAL_DE_CONTROL
            );

            return credencialesOk ? 1 : -1;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar credenciales: " + e.getMessage(), e);
        }
    }

    public Usuario obtenerUsuarioPorIdentificacion(String identificacion) {
        try {
            return usuarioDAO.obtenerUsuarioPorIdentificacion(identificacion);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PersonalControlController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return null;
        }
    }

}
