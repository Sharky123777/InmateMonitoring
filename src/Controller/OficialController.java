package Controller;

import DAO.OficialDAO;
import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.Oficial;
import Model.Entities.Usuario;
import Utilidades.EmailSender;
import View.FrmCamara;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OficialController {

    private static OficialController instancia;
    private final OficialDAO oficialDAO;
    private FrmCamara ventanaCamara;

    private OficialController() {
        this.oficialDAO = OficialDAO.getInstancia();
    }

    public static synchronized OficialController getInstancia() {
        if (instancia == null) {
            instancia = new OficialController();
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

        return ventanaCamara.getImagenCapturada();
    }

    public Oficial registrarOficial(String primerNombre, String segundoNombre,
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
            validarFechasContrato(LocalDate.now(), fechaFinContrato);
            validarImagen(imagen);

            Oficial nuevoOficial = new Oficial(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, "Masculino", nacionalidad, cedula, turno,
                    LocalDate.now(), fechaFinContrato, correo, "", ""
            );

            boolean guardado = OficialDAO.getInstancia().guardarOficial(nuevoOficial, imagen);

            if (guardado) {
                return OficialDAO.getInstancia().obtenerOficialPorCedula(cedula);
            }
            throw new RuntimeException("No se pudo guardar el oficial en la base de datos");

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar oficial: " + e.getMessage(), e);
        }
    }

    private void validarImagen(File imagen) {
        if (imagen == null || !imagen.exists()) {
            throw new IllegalArgumentException("Debe proporcionar una imagen válida del oficial");
        }

        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }
    }

    public int modificarOficial(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {
            // Validar cédula original
            if (cedulaOriginal == null || cedulaOriginal.trim().isEmpty()) {
                throw new IllegalArgumentException("Cédula original es requerida");
            }

            // Validar cambios
            if (cambios == null || cambios.isEmpty()) {
                throw new IllegalArgumentException("No se proporcionaron cambios");
            }

            // Validar campos obligatorios
            validarCamposModificacion(cambios);

            Oficial original = obtenerOficialPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Oficial no encontrado con cédula: " + cedulaOriginal);
            }

            // Verificar si hay cambios reales
            boolean hayCambios = verificarCambios(original, cambios, nuevaImagen);

            if (!hayCambios) {
                return 0; // Código para "no hay cambios"
            }

            // Realizar la modificación
            boolean exito = oficialDAO.modificarOficial(cedulaOriginal,
                    construirOficialModificado(cedulaOriginal, cambios, original),
                    nuevaImagen);

            return exito ? 1 : -1;

        } catch (IllegalArgumentException e) {
            // Lanzar nuevamente para manejo en la UI
            throw e;
        } catch (Exception e) {
            // Loggear error y lanzar una excepción controlada
            System.err.println("Error al modificar oficial: " + e.getMessage());
            throw new RuntimeException("Error interno al modificar oficial", e);
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
        } else if (!((String) cambios.get("correo")).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errores.append("- Correo electrónico no válido\n");
        }
        if (((String) cambios.get("turno")).trim().isEmpty()) {
            errores.append("- Turno es obligatorio\n");
        }
        if ((int) cambios.get("edad") <= 0) {
            errores.append("- Edad debe ser un número positivo\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    private boolean verificarCambios(Oficial original, Map<String, Object> cambios, File nuevaImagen) {
        boolean hayCambios = false;

        // Verificar cambios en campos normales
        if (!Objects.equals(original.getPrimerNombre(), cambios.get("primerNombre"))
                || !Objects.equals(original.getSegundoNombre(), cambios.get("segundoNombre"))
                || !Objects.equals(original.getPrimerApellido(), cambios.get("primerApellido"))
                || !Objects.equals(original.getSegundoApellido(), cambios.get("segundoApellido"))
                || original.getEdad() != (int) cambios.get("edad")
                || !Objects.equals(original.getNacionalidad(), cambios.get("nacionalidad"))
                || !Objects.equals(original.getCorreo(), cambios.get("correo"))
                || !Objects.equals(original.getTurno(), cambios.get("turno"))
                || !Objects.equals(original.getFechaFinContrato(), cambios.get("fechaFin"))) {
            hayCambios = true;
        }

        // Verificar cambio de imagen (solo si se proporcionó una nueva)
        if (nuevaImagen != null) {
            hayCambios = true; // Siempre que haya nueva imagen, considerar como cambio
        }

        return hayCambios;
    }

    private Oficial construirOficialModificado(String cedulaOriginal,
            Map<String, Object> cambios, Oficial original) {

        return new Oficial(
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

    public List<Oficial> obtenerTodosOficiales() {
        return oficialDAO.obtenerOficiales();
    }

    public Oficial obtenerOficialPorCedula(String cedula) {
        return oficialDAO.obtenerOficialPorIdentificacion(cedula);
    }

    public boolean eliminarOficial(String cedula) {
        return oficialDAO.eliminarOficial(cedula);
    }

    public Oficial obtenerOficialPorUsuario(String usuario) {
        return oficialDAO.obtenerOficialPorUsuario(usuario);
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
        } else if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
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
