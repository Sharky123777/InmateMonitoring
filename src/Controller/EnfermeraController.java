package Controller;

import DAO.EnfermeraDAO;
import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.Enfermera;
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

        try {
            // Validación de cédula única
            if (enfermeraDAO.existeEnfermeraConCedula(cedula)) {
                throw new IllegalArgumentException("Ya existe una enfermera con la cédula " + cedula);
            }

            // Validar campos obligatorios
            validarCamposObligatorios(primerNombre, primerApellido, segundoApellido,
                    edad, cedula, nacionalidad, correo, turno);

            // Validaciones adicionales
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

            // Guardar a través del DAO
            boolean guardado = EnfermeraDAO.getInstancia().guardarEnfermera(nuevaEnfermera, imagen);

            if (guardado) {
                return EnfermeraDAO.getInstancia().obtenerEnfermeraPorCedula(cedula);
            }
            throw new RuntimeException("No se pudo guardar la enfermera en la base de datos");

        } catch (IllegalArgumentException e) {
            // Relanzar excepciones de validación
            throw e;
        } catch (Exception e) {
            // Capturar cualquier otra excepción y lanzarla como RuntimeException
            throw new RuntimeException("Error al registrar enfermera: " + e.getMessage(), e);
        }
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

    public int modificarEnfermera(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        try {
            // 1. Verificar existencia de la enfermera
            Enfermera original = obtenerEnfermeraPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Enfermera no encontrada con cédula: " + cedulaOriginal);
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
            validarLimiteEnfermerasPorTurno(turno, cedulaOriginal);

            // 5. Construir enfermera modificada
            Enfermera enfermeraModificada = construirEnfermeraModificada(cedulaOriginal, cambios, original);

            // 6. Ejecutar modificación
            boolean resultado = enfermeraDAO.modificarEnfermera(cedulaOriginal, enfermeraModificada, nuevaImagen);

            return resultado ? 1 : -1; // 1=Éxito, -1=Error

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar enfermera: " + e.getMessage(), e);
        }
    }

    private boolean verificarCambios(Enfermera original, Map<String, Object> cambios, File nuevaImagen) {
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
