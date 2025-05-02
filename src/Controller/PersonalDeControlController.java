package Controller;

import DAO.PersonalDeControlDAO;
import View.Director;
import Model.Entities.PersonalDeControl;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PersonalDeControlController {

    private PersonalDeControlDAO personalDeControlDAO = new PersonalDeControlDAO();
/*
    public void contratarPersonalDeControl(Director view) {
        try {
            String primerNombrePDC = view.getTxtPrimerNombrePDC().getText().trim();
            String segundoNombrePDC = view.getTxtSegundoNombrePDC().getText().trim();
            String primerApellidoPDC = view.getTxtPrimerApellidoPDC().getText().trim();
            String segundoApellidoPDC = view.getTxtSegundoApellidoPDC().getText().trim();
            String edadText = view.getTxtEdadPDC().getText().trim();
            String identificacionPDC = view.getTxtCedulaPDC().getText().trim();
            String sexoPDC = view.getTxtSexoPDC().getText().trim();
            String nacionalidadPDC = view.getCmbNacionalidadPDC().getSelectedItem().toString().trim();
            String correoPDC = view.getTxtCorreoPDC().getText().trim();
            String turnoPDC = view.getCmbTurnoPDC().getSelectedItem().toString().trim();
            Date fechaContrato = view.getJDateChooserFechaContratoPDC().getDate();
            Date finContrato = view.getJDateChooserFinContratoPDC().getDate();

            if (primerNombrePDC.isEmpty() || primerApellidoPDC.isEmpty() || identificacionPDC.isEmpty()
                    || sexoPDC.isEmpty() || nacionalidadPDC.isEmpty() || correoPDC.isEmpty() || turnoPDC.isEmpty()) {
                mostrarError("Todos los campos son obligatorios, excepto el segundo nombre.");
                return;
            }

            int edadPDC;
            try {
                edadPDC = Integer.parseInt(edadText);
                if (edadPDC < 18 || edadPDC > 110) {
                    mostrarError("La edad debe estar entre 18 y 110 años.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarError("La edad debe ser un número válido.");
                return;
            }

            if (identificacionPDC.length() < 6 || identificacionPDC.length() > 10) {
                mostrarError("La cédula debe tener entre 6 y 10 caracteres.");
                return;
            }

            if (fechaContrato == null || finContrato == null) {
                mostrarError("Debe seleccionar las fechas de contrato.");
                return;
            }

            LocalDate fechaLocalContrato = fechaContrato.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate fechaLocalFin = finContrato.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (fechaLocalFin.isBefore(fechaLocalContrato)) {
                mostrarError("La fecha de fin de contrato no puede ser anterior a la fecha de inicio.");
                return;
            }

            if (!correoPDC.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                mostrarError("Por favor ingrese un correo electrónico válido.");
                return;
            }

            if (personalDeControlDAO.buscarPorIdentificacion(identificacionPDC) != null) {
                mostrarError("La identificación ya está registrada.");
                return;
            }

            File imagen = view.getImagenPDCSeleccionada();
            if (imagen == null || !imagen.exists()) {
                mostrarError("Debe seleccionar una imagen válida.");
                return;
            }
            String rutaImagen = imagen.getAbsolutePath();

            PersonalDeControl nuevo = new PersonalDeControl(
                    primerNombrePDC,
                    segundoNombrePDC,
                    primerApellidoPDC,
                    segundoApellidoPDC,
                    edadPDC,
                    sexoPDC,
                    nacionalidadPDC,
                    identificacionPDC,
                    turnoPDC,
                    correoPDC,
                    "Personal de control",
                    fechaLocalContrato,
                    fechaLocalFin,
                    rutaImagen
            );

            new PersonalDeControlDAO().guardarPersonalDeControl(nuevo, imagen);
            JOptionPane.showMessageDialog(null, "Personal de control contratado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    public void mostrarDatosPersonalDeControl(View.PersonalDeControl view, String identificacion) {
        Model.PersonalDeControl personal = personalDeControlDAO.buscarPorIdentificacion(identificacion);

        if (personal != null) {
            view.getNombreCompletoPDC().setText(personal.getNombreCompleto());
            view.getIdentificacionPDC().setText(personal.getIdentificacion());
            view.getEdadPDC().setText(String.valueOf(personal.getEdad()));
            view.getNacionalidadPDC().setText(personal.getNacionalidad());
            view.getEmailPDC().setText(personal.getCorreo());
            view.getSexoPDC().setText(personal.getSexo());
            view.getFechaInicioContratoPDC().setText(personal.getFechaInicioContrato().toString());
            view.getFechaFinContratoPDC().setText(personal.getFechaFinContrato().toString());
            view.getTurnoPDC().setText(personal.getTurno());

            ImageIcon imagen = new ImageIcon(personal.getFotoPath());
            Image imgEscalada = imagen.getImage().getScaledInstance(
                    view.getFotoPDC().getWidth(),
                    view.getFotoPDC().getHeight(),
                    Image.SCALE_SMOOTH
            );
            view.getFotoPDC().setIcon(new ImageIcon(imgEscalada));

        } else {
            JOptionPane.showMessageDialog(null, "No se encontró personal con esa identificación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
*/
}
