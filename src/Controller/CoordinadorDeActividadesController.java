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

        // Validación de cédula única (agregar al inicio)
    if (coordinadorDAO.existeCoordinadorConCedula(cedula)) {
        throw new IllegalArgumentException("Ya existe una coordinadora con la cédula " + cedula);
    }
    
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

    public boolean modificarCoordinador(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
        System.out.println("Iniciando proceso de modificación para cédula: " + cedulaOriginal);
        
        try {
            // 1. Validar existencia del coordinador original
            CoordinadorDeActividades original = validarYObternerOriginal(cedulaOriginal);
            System.out.println("Coordinador original encontrado: " + original.getPrimerNombre());
            
            // 2. Verificar que todos los campos requeridos estén presentes
            validarCamposModificados(cambios);
            System.out.println("Validación de campos completada");

            // 3. Verificar si hay cambios reales - PUNTO CRÍTICO
            boolean hayCambios = verificarCambios(original, cambios, nuevaImagen);
            System.out.println("Resultado de verificación de cambios: " + hayCambios);

            if (!hayCambios) {
                System.out.println("No se detectaron cambios. Mostrando diálogo de confirmación.");
                // Mostrar diálogo de confirmación
                int opcion = JOptionPane.showConfirmDialog(null,
                        "¿Está segura que no desea realizar cambios?",
                        "Sin cambios detectados",
                        JOptionPane.YES_NO_OPTION);
                
                System.out.println("Opción seleccionada: " + (opcion == JOptionPane.YES_OPTION ? "SI" : "NO"));

                if (opcion == JOptionPane.YES_OPTION) {
                    // Retornar false para indicar que no se realizaron cambios
                    System.out.println("Usuario confirmó no realizar cambios");
                    return false;
                } else {
                    // El usuario quiere seguir editando
                    System.out.println("Usuario decidió seguir editando");
                    throw new CancelarModificacionException();
                }
            }

            System.out.println("Hay cambios, procediendo con la validación y modificación");
            
            // 4. Validar edad
            int edad = validarEdad((int) cambios.get("edad"));

            // 5. Validar fechas
            LocalDate fechaFin = validarFechas(original.getFechaInicioContrato(), (LocalDate) cambios.get("fechaFin"));

            // 6. Validar imagen si se proporciona una nueva
            if (nuevaImagen != null) {
                validarImagen(nuevaImagen);
            }

            // 7. Crear objeto modificado
            CoordinadorDeActividades coordinadorModificado = crearCoordinadorModificado(
                    original, cambios, edad, fechaFin);

            // 8. Determinar qué imagen usar
            File imagenFinal = determinarImagenFinal(original, nuevaImagen);

            // 9. Ejecutar modificación en DAO
            boolean resultado = coordinadorDAO.modificarCoordinador(cedulaOriginal, coordinadorModificado, imagenFinal);
            
            if (resultado) {
                // Mostrar mensaje de éxito
                System.out.println("Modificación exitosa, mostrando mensaje");
                JOptionPane.showMessageDialog(null,
                        "COORDINADORA MODIFICADA EXITOSAMENTE",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            return resultado;

        } catch (CancelarModificacionException e) {
            // El usuario decidió seguir editando
            throw e;
        } catch (IllegalArgumentException e) {
            // Relanzar excepciones de validación
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (Exception e) {
            // Capturar cualquier otro error
            JOptionPane.showMessageDialog(null, 
                    "Error al modificar coordinador: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error al modificar coordinador: " + e.getMessage(), e);
        }
    }

    private boolean verificarCambios(CoordinadorDeActividades original, Map<String, Object> cambios, File nuevaImagen) {
    System.out.println("Verificando cambios:");
    
    // Verificar cambios en campos básicos
    if (!original.getPrimerNombre().trim().equals(((String)cambios.get("primerNombre")).trim())) {
        System.out.println("Cambio detectado en primer nombre");
        return true;
    }
    
    String segundoNombreOriginal = original.getSegundoNombre() != null ? original.getSegundoNombre().trim() : "";
    String segundoNombreNuevo = cambios.getOrDefault("segundoNombre", "").toString().trim();
    if (!segundoNombreOriginal.equals(segundoNombreNuevo)) {
        System.out.println("Cambio detectado en segundo nombre");
        return true;
    }
    
    if (!original.getPrimerApellido().trim().equals(((String)cambios.get("primerApellido")).trim())) {
        System.out.println("Cambio detectado en primer apellido");
        return true;
    }
    
    if (!original.getSegundoApellido().trim().equals(((String)cambios.get("segundoApellido")).trim())) {
        System.out.println("Cambio detectado en segundo apellido");
        return true;
    }
    
    if (original.getEdad() != (int) cambios.get("edad")) {
        System.out.println("Cambio detectado en edad");
        return true;
    }
    
    if (!original.getNacionalidad().trim().equals(((String)cambios.get("nacionalidad")).trim())) {
        System.out.println("Cambio detectado en nacionalidad");
        return true;
    }

    if (!original.getCorreo().trim().equals(((String)cambios.get("correo")).trim())) {
        System.out.println("Cambio detectado en correo");
        return true;
    }
    
    if (!original.getTurno().trim().equals(((String)cambios.get("turno")).trim())) {
        System.out.println("Cambio detectado en turno");
        return true;
    }
    
    if (!original.getCargo().trim().equals(((String)cambios.get("cargo")).trim())) {
        System.out.println("Cambio detectado en cargo");
        return true;
    }
    
    LocalDate fechaFinOriginal = original.getFechaFinContrato();
    LocalDate fechaFinNueva = (LocalDate) cambios.get("fechaFin");
    if (!fechaFinOriginal.equals(fechaFinNueva)) {
        System.out.println("Cambio detectado en fecha fin");
        return true;
    }

    // Verificar si se cambió la imagen (solo si se proporciona una nueva)
    if (nuevaImagen != null) {
        System.out.println("Cambio detectado: nueva imagen proporcionada");
        return true;
    }

    System.out.println("No se detectaron cambios");
    return false;
}
    
    // Excepción personalizada para cuando el usuario cancela la modificación
    public class CancelarModificacionException extends RuntimeException {
        public CancelarModificacionException() {
            super("El usuario decidió continuar editando");
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
        validarCampoObligatorio(cambios, "turno", "Turno", errores);
        validarCampoObligatorio(cambios, "cargo", "Cargo", errores);
        validarCampoObligatorio(cambios, "edad", "Edad", errores);
        validarCampoObligatorio(cambios, "fechaFin", "Fecha fin contrato", errores);

        // Validación específica para correo electrónico
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