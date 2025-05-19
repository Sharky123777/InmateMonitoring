package Controller;

import DAO.OficialDeRegistroDAO;
import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.OficialDeRegistro;
import Model.Entities.Usuario;
import Utilidades.EmailSender;
import View.FrmCamara;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OficialDeRegistroController {

    private static OficialDeRegistroController instancia;
    private final OficialDeRegistroDAO oficialDAO;
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstancia();
    private FrmCamara ventanaCamara;

    private OficialDeRegistroController() {
        this.oficialDAO = OficialDeRegistroDAO.getInstancia();
    }

    public static synchronized OficialDeRegistroController getInstancia() {
        if (instancia == null) {
            instancia = new OficialDeRegistroController();
        }
        return instancia;
    }

    public File capturarImagenOficial() {
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

        File imagenCapturada = ventanaCamara.getImagenCapturada();

        if (imagenCapturada != null) {

            String tempDir = System.getProperty("java.io.tmpdir");
            String nombreTemp = "oficial_registro_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(tempDir, nombreTemp);

            try {
                Files.copy(imagenCapturada.toPath(), tempFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                return tempFile;
            } catch (IOException e) {

                return null;
            }
        }

        return null;
    }

    public OficialDeRegistro registrarOficial(String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, LocalDate fechaFinContrato,
            File imagen) throws IOException {

        try {

            if (oficialDAO.existeOficialConCedula(cedula)) {
                throw new IllegalArgumentException("Ya existe un oficial con la cédula " + cedula);
            }

            validarCamposObligatorios(primerNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno);

            validarEdad(edad);
            validarCedula(cedula);
            validarFechasContrato(LocalDate.now(), fechaFinContrato);
            validarImagen(imagen);

            OficialDeRegistro nuevoOficial = new OficialDeRegistro(
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    edad,
                    "Femenino",
                    nacionalidad,
                    cedula,
                    turno,
                    LocalDate.now(),
                    fechaFinContrato,
                    correo,
                    "",
                    ""
            );

            boolean guardado = oficialDAO.guardarOficial(nuevoOficial, imagen);

            if (guardado) {
                return oficialDAO.obtenerOficialPorCedula(cedula);
            }

            throw new RuntimeException("No se pudo guardar el oficial en la base de datos");

        } catch (IllegalArgumentException e) {

            throw e;
        } catch (Exception e) {

            System.err.println("Error al registrar oficial: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al registrar oficial: " + e.getMessage(), e);
        }
    }

    private void validarCedula(String cedula) {
        if (cedula.length() < 8 || cedula.length() > 10) {
            throw new IllegalArgumentException("La cédula debe tener entre 8 y 10 dígitos");
        }
    }

    private void validarImagen(File imagen) {

        if (imagen == null) {

            throw new IllegalArgumentException("Debe proporcionar una imagen válida del oficial");
        }

        if (!imagen.exists()) {

            throw new IllegalArgumentException("La imagen proporcionada no existe en la ruta especificada");
        }

        if (imagen.length() == 0) {

            throw new IllegalArgumentException("La imagen proporcionada está vacía o corrupta");
        }

        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {

            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }

    }

    public int modificarOficial(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {

            OficialDeRegistro original = obtenerOficialPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Oficial no encontrado con cédula: " + cedulaOriginal);
            }

            if (!verificarCambios(original, cambios, nuevaImagen)) {
                return 0;
            }

            validarCamposModificacion(cambios);

            int edad = (int) cambios.get("edad");
            LocalDate fechaFin = (LocalDate) cambios.get("fechaFin");
            String turno = (String) cambios.get("turno");

            validarEdad(edad);
            validarFechasContrato(original.getFechaContratacion(), fechaFin);

            OficialDeRegistro oficialModificado = construirOficialModificado(cedulaOriginal, cambios, original);

            boolean resultado = oficialDAO.modificarOficial(cedulaOriginal, oficialModificado, nuevaImagen);

            return resultado ? 1 : -1; // 1=Éxito, -1=Error

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar oficial: " + e.getMessage(), e);
        }
    }

    private boolean verificarCambios(OficialDeRegistro original, Map<String, Object> cambios, File nuevaImagen) {

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

    private OficialDeRegistro construirOficialModificado(String cedulaOriginal,
            Map<String, Object> cambios, OficialDeRegistro original) {

        return new OficialDeRegistro(
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
            throw new IllegalArgumentException("Debe seleccionar una imagen del oficial");
        }

        File imagen = new File(rutaImagen);
        validarImagen(imagen);
        return imagen;
    }

    public List<OficialDeRegistro> obtenerTodosOficiales() {
        return oficialDAO.obtenerOficiales();
    }

    public OficialDeRegistro obtenerOficialPorCedula(String cedula) {
        return oficialDAO.obtenerOficialPorIdentificacion(cedula);
    }

    public boolean eliminarOficial(String cedula) {
        return oficialDAO.eliminarOficial(cedula);
    }

    public OficialDeRegistro obtenerOficialPorUsuario(String usuario) {
        return oficialDAO.obtenerOficialPorUsuario(usuario);
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

    public int modificarOficialConCredenciales(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {
            OficialDeRegistro original = obtenerOficialPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Oficial no encontrado con cédula: " + cedulaOriginal);
            }

            Integer edad = null;
            if (cambios.containsKey("edad")) {
                try {
                    edad = Integer.parseInt(cambios.get("edad").toString());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("La edad debe ser un número válido");
                }
            }

            OficialDeRegistro modificado = new OficialDeRegistro(
                    cambios.get("primerNombre") != null ? cambios.get("primerNombre").toString() : original.getPrimerNombre(),
                    cambios.get("segundoNombre") != null ? cambios.get("segundoNombre").toString() : original.getSegundoNombre(),
                    cambios.get("primerApellido") != null ? cambios.get("primerApellido").toString() : original.getPrimerApellido(),
                    cambios.get("segundoApellido") != null ? cambios.get("segundoApellido").toString() : original.getSegundoApellido(),
                    edad != null ? edad : original.getEdad(),
                    original.getSexo(),
                    cambios.get("nacionalidad") != null ? cambios.get("nacionalidad").toString() : original.getNacionalidad(),
                    cambios.get("identificacion") != null ? cambios.get("identificacion").toString() : original.getIdentificacion(),
                    original.getTurno(),
                    original.getFechaContratacion(),
                    original.getFechaFinContrato(),
                    original.getCorreo(),
                    cambios.get("nuevoUsuario") != null ? cambios.get("nuevoUsuario").toString() : original.getUsuario(),
                    cambios.containsKey("nuevaContraseña")
                    ? cambios.get("nuevaContraseña").toString()
                    : original.getContrasena()
            );

            boolean hayCambiosReales = !modificado.equals(original) || nuevaImagen != null;

            if (hayCambiosReales) {
                boolean exito = OficialDeRegistroDAO.getInstancia()
                        .modificarOficial(cedulaOriginal, modificado, nuevaImagen);
                if (!exito) {
                    return -1;
                }
            }

            if (cambios.containsKey("nuevoUsuario") || cambios.containsKey("nuevaContraseña")) {
                String nuevoUsuario = modificado.getUsuario();
                String nuevaContra = cambios.containsKey("nuevaContraseña")
                        ? cambios.get("nuevaContraseña").toString()
                        : null;

                boolean credencialesActualizadas = UsuarioDAO.getInstancia().modificarCredenciales(
                        original.getUsuario(),
                        nuevoUsuario,
                        nuevaContra
                );

                if (!credencialesActualizadas) {
                    return -1;
                }

                if (original.getCorreo() != null && !original.getCorreo().isEmpty()) {
                    try {
                        EmailSender.getInstancia().enviarCredenciales(
                                original.getCorreo(),
                                nuevoUsuario,
                                nuevaContra != null ? nuevaContra : "*** No modificada ***",
                                RolEnum.OFICIAL_DE_REGISTRO
                        );
                    } catch (Exception e) {
                        System.err.println("Error enviando correo: " + e.getMessage());
                    }
                }
            }

            return hayCambiosReales ? 1 : 0;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar oficial: " + e.getMessage(), e);
        }
    }

    public int modificarCredencialesOficial(String cedulaOriginal, String nuevoUsuario, String nuevaContra) {
        try {
            OficialDeRegistro oficial = obtenerOficialPorCedula(cedulaOriginal);
            if (oficial == null) {
                throw new IllegalArgumentException("Oficial no encontrado");
            }

            if ((nuevoUsuario == null || nuevoUsuario.isEmpty() || nuevoUsuario.equals(oficial.getUsuario()))
                    && (nuevaContra == null || nuevaContra.isEmpty())) {
                return 0;
            }

            String nuevaContraEncriptada = (nuevaContra != null && !nuevaContra.isEmpty())
                    ? UsuarioController.getInstancia().encriptarContrasena(nuevaContra)
                    : null;

            boolean resultado = UsuarioDAO.getInstancia().modificarCredenciales(
                    oficial.getUsuario(),
                    nuevoUsuario != null && !nuevoUsuario.isEmpty() ? nuevoUsuario : null,
                    nuevaContraEncriptada
            );

            if (resultado && oficial.getCorreo() != null && !oficial.getCorreo().isEmpty()) {
                EmailSender.getInstancia().enviarCredenciales(
                        oficial.getCorreo(),
                        nuevoUsuario != null && !nuevoUsuario.isEmpty() ? nuevoUsuario : oficial.getUsuario(),
                        nuevaContra != null && !nuevaContra.isEmpty() ? nuevaContra : "*** No modificada ***",
                        RolEnum.OFICIAL_DE_REGISTRO
                );
            }

            return resultado ? 1 : -1;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar credenciales: " + e.getMessage(), e);
        }
    }

    public Usuario obtenerUsuarioPorIdentificacion(String identificacion) {
    return usuarioDAO.obtenerUsuarioPorIdentificacion(identificacion);
}

}
