package Controller;

import DAO.OficialDeRegistroDAO;
import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.SincronizadorJson;
import Model.Entities.OficialDeRegistro;
import Model.Entities.Usuario;
import Utilidades.EmailSender;
import Utilidades.GeneradorCredenciales;
import View.FrmCamara;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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

            OficialDeRegistro modificado = new OficialDeRegistro(
                    cambios.getOrDefault("primerNombre", original.getPrimerNombre()).toString(),
                    cambios.getOrDefault("segundoNombre", original.getSegundoNombre()).toString(),
                    cambios.getOrDefault("primerApellido", original.getPrimerApellido()).toString(),
                    cambios.getOrDefault("segundoApellido", original.getSegundoApellido()).toString(),
                    cambios.containsKey("edad") ? Integer.parseInt(cambios.get("edad").toString()) : original.getEdad(),
                    original.getSexo(),
                    cambios.getOrDefault("nacionalidad", original.getNacionalidad()).toString(),
                    cambios.getOrDefault("identificacion", original.getIdentificacion()).toString(),
                    original.getTurno(),
                    original.getFechaContratacion(),
                    original.getFechaFinContrato(),
                    original.getCorreo(),
                    cambios.getOrDefault("nuevoUsuario", original.getUsuario()).toString(),
                    cambios.containsKey("nuevaContraseña") ? cambios.get("nuevaContraseña").toString() : original.getContrasena()
            );

            boolean exitoOficial = OficialDeRegistroDAO.getInstancia()
                    .modificarOficial(cedulaOriginal, modificado, nuevaImagen);

            if (!exitoOficial) {
                return -1;
            }

            SincronizadorJson.sincronizarConUsuarios(modificado);

            if (cambios.containsKey("nuevoUsuario") || cambios.containsKey("nuevaContraseña")) {
                boolean credencialesOk = usuarioDAO.modificarCredenciales(
                        original.getUsuario(),
                        cambios.containsKey("nuevoUsuario") ? cambios.get("nuevoUsuario").toString() : null,
                        cambios.containsKey("nuevaContraseña") ? cambios.get("nuevaContraseña").toString() : null,
                        original.getCorreo(), 
                        RolEnum.OFICIAL_DE_REGISTRO 
                );

                if (!credencialesOk) {
                    return -1;
                }
            }

            return 1;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar oficial: " + e.getMessage(), e);
        }
    }

public int modificarCredencialesOficial(String cedulaOriginal, String nuevoUsuario, String nuevaContra) {
    try {
        OficialDeRegistro oficial = obtenerOficialPorCedula(cedulaOriginal);
        if (oficial == null) throw new IllegalArgumentException("Oficial no encontrado");

        nuevoUsuario = nuevoUsuario != null ? nuevoUsuario.toLowerCase() : null;
        
        boolean cambioUsuario = nuevoUsuario != null && !nuevoUsuario.equals(oficial.getUsuario().toLowerCase());
        boolean cambioContra = nuevaContra != null && !nuevaContra.isEmpty();

        if (!cambioUsuario && !cambioContra) return 0;

        if (cambioContra) {
            oficial.setContrasena(GeneradorCredenciales.encriptarContrasena(nuevaContra));
        }
        if (cambioUsuario) {
            oficial.setUsuario(nuevoUsuario);
        }

        boolean exito = OficialDeRegistroDAO.getInstancia()
                        .modificarOficial(cedulaOriginal, oficial, null);
        if (!exito) return -1;

        SincronizadorJson.sincronizarConUsuarios(oficial);

        boolean credencialesOk = UsuarioDAO.getInstancia().modificarCredenciales(
            oficial.getUsuario(),
            cambioUsuario ? nuevoUsuario : null,
            cambioContra ? nuevaContra : null,
            oficial.getCorreo(),
            RolEnum.OFICIAL_DE_REGISTRO
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
            Logger.getLogger(OficialDeRegistroController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
