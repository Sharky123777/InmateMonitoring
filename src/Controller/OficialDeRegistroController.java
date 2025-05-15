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

        File imagenCapturada = ventanaCamara.getImagenCapturada();

        if (imagenCapturada != null) {
            // Mover a un archivo temporal con nombre consistente
            String tempDir = System.getProperty("java.io.tmpdir");
            String nombreTemp = "oficial_registro_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(tempDir, nombreTemp);

            try {
                Files.copy(imagenCapturada.toPath(), tempFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                // DEBUG: Información de la imagen capturada
                System.out.println("Imagen capturada movida a: " + tempFile.getAbsolutePath());
                System.out.println("Tamaño: " + tempFile.length() + " bytes");

                return tempFile;
            } catch (IOException e) {
                System.err.println("Error al mover imagen capturada: " + e.getMessage());
                return null;
            }
        }

        return null;
    }

    public OficialDeRegistro registrarOficial(String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, LocalDate fechaFinContrato,
            File imagen) throws IOException {

        // Debug: verificar parámetros
        System.out.println("=== Parámetros recibidos ===");
        System.out.println("Nombre: " + primerNombre + " " + primerApellido);
        System.out.println("Cédula: " + cedula);
        System.out.println("Imagen: " + (imagen != null ? imagen.getAbsolutePath() : "null"));

        try {
            // Validación de cédula única
            if (oficialDAO.existeOficialConCedula(cedula)) {
                throw new IllegalArgumentException("Ya existe un oficial con la cédula " + cedula);
            }

            // Validar campos obligatorios
            validarCamposObligatorios(primerNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno);

            // Validaciones adicionales
            validarEdad(edad);
            validarFechasContrato(LocalDate.now(), fechaFinContrato);
            validarImagen(imagen);

            // Crear nuevo oficial
            OficialDeRegistro nuevoOficial = new OficialDeRegistro(
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    edad,
                    "Masculino", // Valor por defecto o parámetro
                    nacionalidad,
                    cedula,
                    turno,
                    LocalDate.now(),
                    fechaFinContrato,
                    correo,
                    "", // Usuario se asignará en el DAO
                    "" // Contraseña se asignará en el DAO
            );

            // Guardar a través del DAO
            boolean guardado = oficialDAO.guardarOficial(nuevoOficial, imagen);

            if (guardado) {
                return oficialDAO.obtenerOficialPorCedula(cedula);
            }

            throw new RuntimeException("No se pudo guardar el oficial en la base de datos");

        } catch (IllegalArgumentException e) {
            // Relanzar excepciones de validación
            throw e;
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            System.err.println("Error al registrar oficial: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al registrar oficial: " + e.getMessage(), e);
        }
    }

    private void validarImagen(File imagen) {
        // DEBUG: Mostrar información de validación
        if (imagen == null) {
            System.out.println("Validación fallida: imagen es null");
            throw new IllegalArgumentException("Debe proporcionar una imagen válida del oficial");
        }

        if (!imagen.exists()) {
            System.out.println("Validación fallida: archivo no existe - " + imagen.getAbsolutePath());
            throw new IllegalArgumentException("La imagen proporcionada no existe en la ruta especificada");
        }

        if (imagen.length() == 0) {
            System.out.println("Validación fallida: archivo vacío - " + imagen.getAbsolutePath());
            throw new IllegalArgumentException("La imagen proporcionada está vacía o corrupta");
        }

        // Validar extensión del archivo
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            System.out.println("Validación fallida: formato no válido - " + nombre);
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }

        // DEBUG: Validación exitosa
        System.out.println("Validación de imagen exitosa: " + imagen.getAbsolutePath());
    }

    public int modificarOficial(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {
            // 1. Verificar existencia del oficial
            OficialDeRegistro original = obtenerOficialPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Oficial no encontrado con cédula: " + cedulaOriginal);
            }

            // 2. Verificar si hay cambios reales
            if (!verificarCambios(original, cambios, nuevaImagen)) {
                return 0; // Código 0 = No hay cambios
            }

            // 3. Validar campos modificados
            validarCamposModificacion(cambios);

            // 4. Extraer y validar datos
            int edad = (int) cambios.get("edad");
            LocalDate fechaFin = (LocalDate) cambios.get("fechaFin");
            String turno = (String) cambios.get("turno");

            validarEdad(edad);
            validarFechasContrato(original.getFechaContratacion(), fechaFin);

            // 5. Construir oficial modificado
            OficialDeRegistro oficialModificado = construirOficialModificado(cedulaOriginal, cambios, original);

            // 6. Ejecutar modificación
            boolean resultado = oficialDAO.modificarOficial(cedulaOriginal, oficialModificado, nuevaImagen);

            return resultado ? 1 : -1; // 1=Éxito, -1=Error

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar oficial: " + e.getMessage(), e);
        }
    }

    private boolean verificarCambios(OficialDeRegistro original, Map<String, Object> cambios, File nuevaImagen) {
        // Verificar cambios en campos básicos
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

        // Verificar si se cambió la imagen
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
                "Masculino",
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

    // Métodos de consulta
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
