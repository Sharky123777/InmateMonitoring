package Controller;

import DAO.CoordinadorDeActividadesDAO;
import Model.Entities.CoordinadorDeActividades;
import Model.Entities.Usuario;
import Model.Constants.RolEnum;
import View.FrmCamara;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
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
                return null;
            }
        }
        
        return ventanaCamara.getImagenCapturada();
    }

    public void registrarCoordinador(String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, String cargo,
            LocalDate fechaFinContrato, File imagenSeleccionadaCDA) throws IOException {
        
        validarCamposObligatorios(primerNombre, primerApellido, segundoApellido, 
                edad, cedula, nacionalidad, correo, turno, cargo);
        validarEdad(edad);
        validarFechasContrato(LocalDate.now(), fechaFinContrato);
        validarImagen(imagenSeleccionadaCDA);

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
            CoordinadorDeActividades original = obtenerCoordinadorPorCedula(cedulaOriginal);
            if (original == null) {
                throw new IllegalArgumentException("Coordinador no encontrado con cédula: " + cedulaOriginal);
            }

            validarCamposModificacion(cambios);
            validarEdad((int) cambios.get("edad"));
            validarFechasContrato(original.getFechaInicioContrato(), (LocalDate) cambios.get("fechaFin"));

            CoordinadorDeActividades coordinadorModificado = new CoordinadorDeActividades(
                (String) cambios.get("primerNombre"),
                (String) cambios.get("segundoNombre"),
                (String) cambios.get("primerApellido"),
                (String) cambios.get("segundoApellido"),
                (int) cambios.get("edad"),
                "Femenino",
                (String) cambios.get("nacionalidad"),
                cedulaOriginal,
                (String) cambios.get("correo"),
                (String) cambios.get("turno"),
                original.getFechaInicioContrato(),
                (LocalDate) cambios.get("fechaFin"),
                original.getUsuario(),
                original.getContrasena(),
                (String) cambios.get("cargo")
            );

            File imagenFinal = (nuevaImagen != null) ? nuevaImagen : 
                             (original.getRutaImagen() != null && !original.getRutaImagen().isEmpty()) ? 
                             new File(original.getRutaImagen()) : null;

            return coordinadorDAO.modificarCoordinador(cedulaOriginal, coordinadorModificado, imagenFinal);
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar coordinador: " + e.getMessage(), e);
        }
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
        if (((String) cambios.get("cargo")).trim().isEmpty()) {
            errores.append("- Cargo es obligatorio\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
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

    private void validarEdad(int edad) {
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        } if (edad < 0) {
            throw new IllegalArgumentException("La edad debe ser un número válido");
        }
    }

    private void validarImagen(File imagen) {
        if (imagen == null || !imagen.exists()) {
            throw new IllegalArgumentException("Debe proporcionar una imagen válida del coordinador");
        }
        
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }
    }
}