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

        File imagen = ventanaCamara.getImagenCapturada();

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

        if (coordinadorDAO.existeCoordinadorConCedula(cedula)) {
            throw new IllegalArgumentException("Ya existe una coordinadora con la cédula " + cedula);
        }

        validarCamposObligatorios(primerNombre, primerApellido, segundoApellido,
                edad, cedula, nacionalidad, correo, turno, cargo);
        validarEdad(edad);
        validarFechasContrato(LocalDate.now(), fechaFinContrato);

        if (imagenSeleccionadaCDA == null) {
            throw new IllegalArgumentException("Debe proporcionar una imagen del coordinador");
        }

        if (!imagenSeleccionadaCDA.exists()) {
            throw new IllegalArgumentException("El archivo de imagen no existe: " + imagenSeleccionadaCDA.getAbsolutePath());
        }

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

    public boolean modificarCoordinador(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {

        try {
            CoordinadorDeActividades original = validarYObternerOriginal(cedulaOriginal);

            validarCamposModificados(cambios);

            boolean hayCambios = verificarCambios(original, cambios, nuevaImagen);

            if (!hayCambios) {

                int opcion = JOptionPane.showConfirmDialog(null,
                        "¿Está segura que no desea realizar cambios?",
                        "Sin cambios detectados",
                        JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {

                    return false;
                } else {

                    throw new CancelarModificacionException();
                }
            }

            int edad = validarEdad((int) cambios.get("edad"));

            LocalDate fechaFin = validarFechas(original.getFechaInicioContrato(), (LocalDate) cambios.get("fechaFin"));

            if (nuevaImagen != null) {
                validarImagen(nuevaImagen);
            }

            CoordinadorDeActividades coordinadorModificado = crearCoordinadorModificado(
                    original, cambios, edad, fechaFin);

            File imagenFinal = determinarImagenFinal(original, nuevaImagen);

            boolean resultado = coordinadorDAO.modificarCoordinador(cedulaOriginal, coordinadorModificado, imagenFinal);

            if (resultado) {
                System.out.println("Modificación exitosa, mostrando mensaje");
                JOptionPane.showMessageDialog(null,
                        "COORDINADORA MODIFICADA EXITOSAMENTE",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            return resultado;

        } catch (CancelarModificacionException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar coordinador: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error al modificar coordinador: " + e.getMessage(), e);
        }
    }

    private boolean verificarCambios(CoordinadorDeActividades original, Map<String, Object> cambios, File nuevaImagen) {
        System.out.println("Verificando cambios:");

        if (!original.getPrimerNombre().trim().equals(((String) cambios.get("primerNombre")).trim())) {
            System.out.println("Cambio detectado en primer nombre");
            return true;
        }

        String segundoNombreOriginal = original.getSegundoNombre() != null ? original.getSegundoNombre().trim() : "";
        String segundoNombreNuevo = cambios.getOrDefault("segundoNombre", "").toString().trim();
        if (!segundoNombreOriginal.equals(segundoNombreNuevo)) {
            System.out.println("Cambio detectado en segundo nombre");
            return true;
        }

        if (!original.getPrimerApellido().trim().equals(((String) cambios.get("primerApellido")).trim())) {
            System.out.println("Cambio detectado en primer apellido");
            return true;
        }

        if (!original.getSegundoApellido().trim().equals(((String) cambios.get("segundoApellido")).trim())) {
            System.out.println("Cambio detectado en segundo apellido");
            return true;
        }

        if (original.getEdad() != (int) cambios.get("edad")) {
            System.out.println("Cambio detectado en edad");
            return true;
        }

        if (!original.getNacionalidad().trim().equals(((String) cambios.get("nacionalidad")).trim())) {
            System.out.println("Cambio detectado en nacionalidad");
            return true;
        }

        if (!original.getCorreo().trim().equals(((String) cambios.get("correo")).trim())) {
            System.out.println("Cambio detectado en correo");
            return true;
        }

        if (!original.getTurno().trim().equals(((String) cambios.get("turno")).trim())) {
            System.out.println("Cambio detectado en turno");
            return true;
        }

        if (!original.getCargo().trim().equals(((String) cambios.get("cargo")).trim())) {
            System.out.println("Cambio detectado en cargo");
            return true;
        }

        LocalDate fechaFinOriginal = original.getFechaFinContrato();
        LocalDate fechaFinNueva = (LocalDate) cambios.get("fechaFin");
        if (!fechaFinOriginal.equals(fechaFinNueva)) {
            System.out.println("Cambio detectado en fecha fin");
            return true;
        }

        if (nuevaImagen != null) {
            System.out.println("Cambio detectado: nueva imagen proporcionada");
            return true;
        }

        System.out.println("No se detectaron cambios");
        return false;
    }

    public class CancelarModificacionException extends RuntimeException {

        public CancelarModificacionException() {
            super("El usuario decidió continuar editando");
        }
    }

    private CoordinadorDeActividades validarYObternerOriginal(String cedula) {
        CoordinadorDeActividades original = obtenerCoordinadorPorCedula(cedula);
        if (original == null) {
            throw new IllegalArgumentException("Coordinador no encontrado con cédula: " + cedula);
        }
        return original;
    }

    private void validarCamposModificados(Map<String, Object> cambios) {
        StringBuilder errores = new StringBuilder();

        validarCampoObligatorio(cambios, "primerNombre", "Primer nombre", errores);
        validarCampoObligatorio(cambios, "primerApellido", "Primer apellido", errores);
        validarCampoObligatorio(cambios, "segundoApellido", "Segundo apellido", errores);
        validarCampoObligatorio(cambios, "nacionalidad", "Nacionalidad", errores);
        validarCampoObligatorio(cambios, "correo", "Correo electrónico", errores);
        validarCampoObligatorio(cambios, "turno", "Turno", errores);
        validarCampoObligatorio(cambios, "cargo", "Cargo", errores);
        validarCampoObligatorio(cambios, "edad", "Edad", errores);
        validarCampoObligatorio(cambios, "fechaFin", "Fecha fin contrato", errores);

        if (cambios.containsKey("correo") && !((String) cambios.get("correo")).contains("@")) {
            errores.append("- El correo electrónico debe tener un formato válido\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException("Campos obligatorios faltantes:\n" + errores.toString());
        }
    }

    private void validarCampoObligatorio(Map<String, Object> cambios, String campo, String nombreCampo, StringBuilder errores) {
        if (!cambios.containsKey(campo)) {
            errores.append("- ").append(nombreCampo).append(" es un campo requerido\n");
        } else if (cambios.get(campo) == null) {
            errores.append("- ").append(nombreCampo).append(" no puede ser nulo\n");
        } else if (cambios.get(campo) instanceof String && ((String) cambios.get(campo)).trim().isEmpty()) {
            errores.append("- ").append(nombreCampo).append(" es obligatorio\n");
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
                original.getSexo(),
                (String) cambios.get("nacionalidad"),
                original.getIdentificacion(),
                (String) cambios.get("correo"),
                (String) cambios.get("turno"),
                original.getFechaInicioContrato(),
                fechaFin,
                original.getUsuario(),
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

        try {
            BufferedImage img = ImageIO.read(imagen);
            if (img == null) {
                throw new IllegalArgumentException("El archivo no es una imagen válida");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al leer la imagen: " + e.getMessage());
        }

        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }

        long maxSize = 5 * 1024 * 1024;
        if (imagen.length() > maxSize) {
            throw new IllegalArgumentException("La imagen es demasiado grande (máximo 5MB)");
        }
    }
}