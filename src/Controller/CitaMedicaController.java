package Controller;

import DAO.CitaMedicaDAO;
import DAO.EnfermeraDAO;
import DAO.GuardiaDAO;
import DAO.PresoDAO;
import Model.Entities.CitaMedica;
import Model.Entities.Enfermera;
import Model.Entities.Guardia;
import Model.Entities.Preso;
import View.Oficial;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class CitaMedicaController {

    private CitaMedicaDAO citaMedicaDAO = CitaMedicaDAO.getInstancia();
    private PresoDAO presoDAO = PresoDAO.getInstancia();
    private GuardiaDAO guardiaDAO = GuardiaDAO.getInstancia();
    private EnfermeraDAO enfermeraDAO = EnfermeraDAO.getInstancia();

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarCamposCita(Oficial view) {
        view.getIdentificacionPresoCita().setText("");
        view.getIdentificacionGuardia().setText("");
        view.getMotivoCita().setText("");
        view.getFechaCita().setDate(null);
        view.getComboHoraCita().setSelectedIndex(0);
    }

    public boolean agendarCita(Oficial view) {
        if (!validarCamposCita(view)) {
            return false;
        }

        String identificacionPreso = view.getIdentificacionPresoCita().getText().trim();
        String identificacionGuardia = view.getIdentificacionGuardia().getText().trim();
        String motivoCita = view.getMotivoCita().getText().trim();
        Date fechaSeleccionada = view.getFechaCita().getDate();
        String horaSeleccionada = view.getComboHoraCita().getSelectedItem().toString();

        if (!identificacionPreso.matches("\\d+")) {
            mostrarError("La identificación del preso debe contener solo números");
            return false;
        }

        if (!identificacionPreso.matches("\\d{6,10}")) {
            mostrarError("La identificación debe contener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        if (!identificacionGuardia.matches("\\d+")) {
            mostrarError("La identificación del preso debe contener solo números");
            return false;
        }

        if (!identificacionGuardia.matches("\\d{6,10}")) {
            mostrarError("La identificación debe contener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora;

        try {
            hora = LocalTime.parse(horaSeleccionada);
        } catch (Exception e) {
            mostrarError("Formato de hora inválido");
            return false;
        }

        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la cita no puede ser en el pasado");
            return false;
        }

        Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
        if (preso == null) {
            mostrarError("No se encontró ningún preso con esa identificación");
            return false;
        }

        Guardia guardia = guardiaDAO.obtenerGuardiaPorCedula(identificacionGuardia);
        if (guardia == null) {
            mostrarError("No se encontró ningún guardia con esa identificación");
            return false;
        }

        CitaMedica citaExistente = citaMedicaDAO.buscarCitaPorPresoFechaYHora(identificacionPreso, fecha, hora);
        if (citaExistente != null) {
            mostrarError("Ya existe una cita para este preso en la misma fecha y hora");
            return false;
        }

        String turno = determinarTurno(hora);
        if (turno == null) {
            mostrarError("La hora seleccionada no corresponde a un turno válido (8:00-16:00 o 16:20-20:00)");
            return false;
        }

        if (!guardia.getTurno().equalsIgnoreCase(turno)) {
            mostrarError("El guardia no está disponible en el turno " + turno);
            return false;
        }

        Enfermera enfermera = asignarEnfermera(turno);
        if (enfermera == null) {
            mostrarError("No hay enfermeras disponibles para el turno " + turno);
            return false;
        }

        CitaMedica nuevaCita = new CitaMedica(0, fecha, hora, motivoCita, guardia, preso, enfermera);
        boolean exito = citaMedicaDAO.guardarCita(nuevaCita);

        if (exito) {
            mostrarExito("Cita agendada exitosamente");
            limpiarCamposCita(view);
            return true;
        } else {
            mostrarError("Error al guardar la cita en la base de datos");
            return false;
        }
    }

    private boolean validarCamposCita(Oficial view) {
        if (view.getIdentificacionPresoCita().getText().trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del preso");
            return false;
        }

        if (view.getIdentificacionGuardia().getText().trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del guardia");
            return false;
        }

        if (view.getMotivoCita().getText().trim().isEmpty()) {
            mostrarError("Debe especificar el motivo de la cita");
            return false;
        }

        if (view.getFechaCita().getDate() == null) {
            mostrarError("Debe seleccionar una fecha para la cita");
            return false;
        }

        if (view.getComboHoraCita().getSelectedItem() == null
                || view.getComboHoraCita().getSelectedItem().toString().equals("< Seleccionar >")) {
            mostrarError("Debe seleccionar una hora para la cita");
            return false;
        }

        return true;
    }

    private String determinarTurno(LocalTime hora) {
        if (!hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(16, 0))) {
            return "Diurno";
        } else if (!hora.isBefore(LocalTime.of(16, 20)) && !hora.isAfter(LocalTime.of(20, 0))) {
            return "Nocturno";
        }
        return null;
    }

    private Enfermera asignarEnfermera(String turno) {
        List<Enfermera> enfermeras = enfermeraDAO.obtenerEnfermeras();
        for (Enfermera enf : enfermeras) {
            if (enf.getTurno() != null && enf.getTurno().equalsIgnoreCase(turno)) {
                return enf;
            }
        }
        return null;
    }

}
