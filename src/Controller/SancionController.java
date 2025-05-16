package Controller;

import DAO.GuardiaDAO;
import DAO.PresoDAO;
import DAO.SancionDAO;
import DAO.VisitaDAO;
import Model.Constants.EstadoVisitaEnum;
import Model.Entities.Guardia;
import Model.Entities.Preso;
import Model.Entities.Sancion;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import Utilidades.EmailSender;
import View.Oficial;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SancionController {

    private SancionDAO sancionDAO = SancionDAO.getInstancia();
    private PresoDAO presoDAO = PresoDAO.getInstancia();
    private VisitaDAO visitaDAO = VisitaDAO.getInstancia();

    public SancionController() {

    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean registrarSancion(Oficial view) {
        if (!validarCamposSancion(view)) {
            return false;
        }

        int opcion = JOptionPane.showConfirmDialog(
                view,
                "¿Está seguro de registrar esta sanción?",
                "Confirmar Registro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion != JOptionPane.YES_OPTION) {
            limpiarCamposSancion(view);
            return false;
        }

        String identificacionPreso = view.getIdentificacionPresoSancion().getText().trim();
        String motivoSancion = view.getMotivoSancion().getText().trim();
        String tipoSancion = view.getTipoSancion().getSelectedItem().toString();
        Date fechaSeleccionada = view.getFechaSancion().getDate();
        String horaStr = view.getHoraSancion().getSelectedItem().toString();
        String identificacionGuardia = view.getIdentificacionGuardiaSancion().getText().trim();

        if (!identificacionPreso.matches("\\d{6,10}") || !identificacionGuardia.matches("\\d{6,10}")) {
            mostrarError("Las identificaciones deben tener entre 6 y 10 dígitos.");
            return false;
        }

        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora = LocalTime.parse(horaStr);

        if (!fecha.equals(LocalDate.now())) {
            mostrarError("Las sanciones solo pueden registrarse el día actual (" + LocalDate.now() + ")");
            return false;
        }

        if (hora.isAfter(LocalTime.now())) {
            mostrarError("No puede registrar sanciones con hora futura.");
            return false;
        }

        try {
            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                mostrarError("Preso no encontrado.");
                return false;
            }

            Guardia guardia = new GuardiaDAO().obtenerGuardiaPorCedula(identificacionGuardia);
            if (guardia == null) {
                mostrarError("Guardia no encontrado.");
                return false;
            }

            String turnoSancion = determinarTurno(hora);
            if (turnoSancion == null || !guardia.getTurno().equalsIgnoreCase(turnoSancion)) {
                mostrarError("El guardia no estaba en turno a esta hora.");
                return false;
            }

            List<Sancion> sancionesExistentes = sancionDAO.obtenerSancionesPorPresoFechaYHora(identificacionPreso, fecha, hora);
            if (!sancionesExistentes.isEmpty()) {
                mostrarError("Ya existe una sanción registrada para este preso en la misma fecha y hora");
                return false;
            }

            Sancion nuevaSancion = new Sancion(0, motivoSancion, fecha, hora, tipoSancion, preso, guardia);
            if (sancionDAO.guardarSancion(nuevaSancion)) {
                if (nuevaSancion.esAislamiento()) {
                    preso.setEnAislamiento(true);
                    presoDAO.actualizarPreso(preso);
                }

                cancelarVisitasPendientes(identificacionPreso, fecha, nuevaSancion.getDiasDuracion());
                mostrarExito("Sanción registrada exitosamente. Duración: " + nuevaSancion.getDiasDuracion() + " días");
                limpiarCamposSancion(view);
                return true;
            } else {
                mostrarError("Error al guardar la sanción.");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
            return false;
        }
    }

    private void cancelarVisitasPendientes(String identificacionPreso, LocalDate fechaSancion, int diasDuracion) {
        List<Visita> visitas = visitaDAO.cargarPorIdentificacionPreso(identificacionPreso);
        int visitasCanceladas = 0;

        String motivoCancelacion = (diasDuracion == 10)
                ? "Preso en aislamiento por " + diasDuracion + " días"
                : "Sanción aplicada al preso (Duración: " + diasDuracion + " días)";

        for (Visita visita : visitas) {
            LocalDate fechaVisita = visita.getFechaVisita();
            LocalDate fechaFinSancion = fechaSancion.plusDays(diasDuracion);

            if ((!fechaVisita.isBefore(fechaSancion) && !fechaVisita.isAfter(fechaFinSancion)
                    && visita.getEstado() == EstadoVisitaEnum.EN_PROCESO)) {

                visitaDAO.modificarEstadoVisitaYDevolver(visita.getId(), EstadoVisitaEnum.CANCELADA);
                visitasCanceladas++;

                notificarCancelacionAVisitantes(visita, motivoCancelacion);
            }
        }

        if (visitasCanceladas > 0) {
            mostrarExito("Se cancelaron " + visitasCanceladas + " visitas programadas. "
                    + (diasDuracion == 10 ? "El preso estará en aislamiento." : ""));
        }
    }

    private void notificarCancelacionAVisitantes(Visita visita, String motivo) {
        try {
            List<Visitante> visitantes = new ArrayList<>(visita.getVisitantesConRelacion().keySet());

            for (Visitante visitante : visitantes) {
                if (visitante.getEdad() >= 18
                        && visitante.getEmail() != null
                        && !visitante.getEmail().isEmpty()) {

                    boolean correoEnviado = EmailSender.getInstancia().enviarNotificacionCancelacion(
                            visitante,
                            visita,
                            motivo
                    );

                    if (!correoEnviado) {
                        System.err.println("Error al enviar notificación a: " + visitante.getEmail());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en notificación de cancelación: " + e.getMessage());
        }
    }

    private String determinarTurno(LocalTime hora) {
        if (!hora.isBefore(LocalTime.of(0, 0)) && hora.isBefore(LocalTime.of(8, 0))) {
            return "Nocturno";
        } else if (!hora.isBefore(LocalTime.of(8, 0)) && hora.isBefore(LocalTime.of(20, 0))) {
            return "Diurno";
        } else if (!hora.isBefore(LocalTime.of(20, 0)) && !hora.isAfter(LocalTime.of(23, 59, 59))) {
            return "Nocturno";
        }
        return null;
    }

    private boolean validarCamposSancion(Oficial view) {
        if (view.getIdentificacionPresoSancion().getText().trim().isEmpty()) {
            mostrarError("La identificación del preso es obligatoria.");
            return false;
        }

        if (view.getMotivoSancion().getText().trim().isEmpty()) {
            mostrarError("El motivo de la sanción es obligatorio.");
            return false;
        }

        if (view.getTipoSancion().getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar un tipo de sanción.");
            return false;
        }

        if (view.getFechaSancion().getDate() == null) {
            mostrarError("Debe seleccionar una fecha.");
            return false;
        }

        if (view.getHoraSancion().getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar una hora.");
            return false;
        }

        if (view.getIdentificacionGuardiaSancion().getText().trim().isEmpty()) {
            mostrarError("La identificación del guardia es obligatoria.");
            return false;
        }

        if (view.getMotivoSancion().getText().trim().length() < 10) {
            mostrarError("El motivo debe tener al menos 10 caracteres");
            return false;
        }

        Date fechaSeleccionada = view.getFechaSancion().getDate();
        LocalDate fechaSeleccionadaLD = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (fechaSeleccionadaLD.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la sanción no puede ser en el pasado.");
            return false;
        }

        return true;
    }

    public void limpiarCamposSancion(Oficial view) {
        view.getIdentificacionPresoSancion().setText("");
        view.getMotivoSancion().setText("");
        view.getTipoSancion().setSelectedIndex(0);
        view.getFechaSancion().setDate(null);
        view.getHoraSancion().setSelectedIndex(0);
        view.getIdentificacionGuardiaSancion().setText("");
    }

    public void cargarHistorialSanciones(String identificacionPreso, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = sancionDAO.cargarPorIdentificacionPreso(identificacionPreso);
        for (Sancion sancion : sanciones) {
            int duracionAcumulada = sancionDAO.obtenerDuracionAcumuladaPorTipo(
                    identificacionPreso,
                    sancion.getTipoSancion()
            );

            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDiasDuracion() + " días",
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion(),
                duracionAcumulada + " días"
            });
        }
    }

    public void filtrarSancionesPorTipo(String identificacionPreso, String tipoSancion, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = tipoSancion.equals("< Seleccionar >")
                ? sancionDAO.cargarPorIdentificacionPreso(identificacionPreso)
                : sancionDAO.cargarPorTipoYIdentificacionPreso(tipoSancion, identificacionPreso);

        for (Sancion sancion : sanciones) {
            int duracionAcumulada = sancionDAO.obtenerDuracionAcumuladaPorTipo(
                    identificacionPreso,
                    sancion.getTipoSancion()
            );

            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDiasDuracion() + " días",
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion(),
                duracionAcumulada + " días"
            });
        }
    }
}
