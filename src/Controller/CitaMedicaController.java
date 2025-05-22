package Controller;

import DAO.CitaMedicaDAO;
import DAO.EnfermeraDAO;
import DAO.GuardiaDAO;
import DAO.PresoDAO;
import Model.Entities.CitaMedica;
import Model.Entities.Enfermera;
import Model.Entities.Guardia;
import Model.Entities.Preso;
import Model.Constants.EstadoCitaMedicaEnum;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CitaMedicaController {

    private final CitaMedicaDAO citaMedicaDAO;
    private final PresoDAO presoDAO = PresoDAO.getInstancia();
    private final GuardiaDAO guardiaDAO = GuardiaDAO.getInstancia();
    private final EnfermeraDAO enfermeraDAO = EnfermeraDAO.getInstancia();

    public CitaMedicaController() {
        this.citaMedicaDAO = CitaMedicaDAO.getInstancia();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean agendarCita(String identificacionPreso, String identificacionGuardia,
            String motivo, LocalDate fecha, LocalTime hora) {
        if (!validarCamposCita(identificacionPreso, identificacionGuardia, motivo, fecha, hora)) {
            return false;
        }

        Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
        Guardia guardia = guardiaDAO.obtenerGuardiaPorCedula(identificacionGuardia);

        if (preso == null || guardia == null) {
            mostrarError("Preso o guardia no encontrado");
            return false;
        }

        String turno = determinarTurno(hora);
        Enfermera enfermera = enfermeraDAO.obtenerEnfermeras().stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .findFirst()
                .orElse(null);

        if (enfermera == null) {
            mostrarError("No hay enfermeras disponibles para el turno " + turno);
            return false;
        }

        CitaMedica nuevaCita = new CitaMedica(0, fecha, hora, motivo, guardia, preso, enfermera);
        boolean resultado = citaMedicaDAO.guardarCita(nuevaCita);

        if (resultado) {
            mostrarExito("Cita agendada exitosamente");
        } else {
            mostrarError("Error al guardar la cita");
        }

        return resultado;
    }

    public boolean atenderCita(int idCita, String diagnostico) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            mostrarError("Debe ingresar un diagnóstico");
            return false;
        }

        boolean resultado = citaMedicaDAO.actualizarDiagnostico(idCita, diagnostico, diagnostico);

        if (resultado) {
            mostrarExito("Cita atendida exitosamente");
        } else {
            mostrarError("Error al atender la cita");
        }

        return resultado;
    }



   public void cargarHistorialAtendidos(JTable tabla, String identificacionEnfermera) {
        
        String[] columnNames = {
            "Foto",
            "ID",
            "Nombre Preso",
            "Identificación",
            "Fecha Atención",
            "Hora Atención",
            "Motivo Consulta",
            "Estado"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<CitaMedica> citas = CitaMedicaDAO.getInstancia()
                .obtenerPorEnfermera(identificacionEnfermera)
                .stream()
                .filter(c -> c.getEstado() == EstadoCitaMedicaEnum.ATENDIDO)
                .collect(Collectors.toList());

        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (CitaMedica cita : citas) {
            Object[] rowData = new Object[]{
                "", 
                cita.getId(), 
                cita.getPreso().getNombreCompleto(), 
                cita.getPreso().getIdentificacion(), 
                cita.getFechaHoraAtencion() != null
                ? cita.getFechaHoraAtencion().toLocalDate().format(fechaFormatter) : "N/A",
                cita.getFechaHoraAtencion() != null
                ? cita.getFechaHoraAtencion().toLocalTime().format(horaFormatter) : "N/A", 
                cita.getMotivo(),      
                cita.getEstado().toString()       
              
            };
            modelo.addRow(rowData);
        }

        tabla.setModel(modelo);

        // Ajustar anchos de columnas
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100); // Fecha
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);  // Hora
        tabla.getColumnModel().getColumn(6).setPreferredWidth(200); // Motivo
        tabla.getColumnModel().getColumn(7).setPreferredWidth(250); // Diagnóstico
    }

    public boolean actualizarCita(CitaMedica citaActualizada) {
        try {
            // Obtener la cita existente
            CitaMedica citaExistente = citaMedicaDAO.buscarPorId(citaActualizada.getId());
            if (citaExistente == null) {
                return false;
            }

            // Actualizar solo los campos necesarios
            if (citaActualizada.getDiagnostico() != null) {
                citaExistente.setDiagnostico(citaActualizada.getDiagnostico());
            }

            if (citaActualizada.getRutaHistoriaClinica() != null) {
                citaExistente.setRutaHistoriaClinica(citaActualizada.getRutaHistoriaClinica());
            }

            if (citaActualizada.getEstado() != null) {
                citaExistente.setEstado(citaActualizada.getEstado());

                // Si se marca como atendida y no tiene fecha, establecerla
                if (citaActualizada.getEstado() == EstadoCitaMedicaEnum.ATENDIDO
                        && citaExistente.getFechaHoraAtencion() == null) {
                    citaExistente.setFechaHoraAtencion(LocalDateTime.now());
                }
            }

            // Guardar los cambios usando el método existente guardarCita
            return citaMedicaDAO.guardarCita(citaExistente);

        } catch (Exception e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelarCita(int idCita) {
        boolean resultado = citaMedicaDAO.cancelarCita(idCita);

        if (resultado) {
            mostrarExito("Cita cancelada exitosamente");
        } else {
            mostrarError("Error al cancelar la cita");
        }

        return resultado;
    }

    public void cargarCitasPendientes(JTable tabla, String identificacionEnfermera) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Foto", "ID", "Nombre", "Identificación", "Fecha", "Hora", "Motivo", "Estado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };

        List<CitaMedica> citas = citaMedicaDAO.obtenerPorEnfermera(identificacionEnfermera)
                .stream()
                .filter(c -> c.getEstado() == EstadoCitaMedicaEnum.PENDIENTE)
                .collect(Collectors.toList());

        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (CitaMedica cita : citas) {
            modelo.addRow(new Object[]{
                "", // Foto (se llena con el renderer)
                cita.getId(),
                cita.getPreso().getNombreCompleto(),
                cita.getPreso().getIdentificacion(),
                cita.getFecha() != null ? cita.getFecha().format(fechaFormatter) : "",
                cita.getHora() != null ? cita.getHora().format(horaFormatter) : "",
                cita.getMotivo(),
                cita.getEstado().toString()
            });
        }

        tabla.setModel(modelo);

        // Ajustar el ancho de columnas
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90); // Fecha
        tabla.getColumnModel().getColumn(5).setPreferredWidth(60); // Hora
        tabla.getColumnModel().getColumn(6).setPreferredWidth(150); // Motivo
    }

    public Preso obtenerPresoDeCita(int idCita) {
        CitaMedica cita = citaMedicaDAO.buscarPorId(idCita);
        return cita != null ? cita.getPreso() : null;
    }

    private boolean validarCamposCita(String identificacionPreso, String identificacionGuardia,
            String motivo, LocalDate fecha, LocalTime hora) {
        if (identificacionPreso == null || identificacionPreso.trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del preso");
            return false;
        }

        if (identificacionGuardia == null || identificacionGuardia.trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del guardia");
            return false;
        }

        if (motivo == null || motivo.trim().isEmpty()) {
            mostrarError("Debe especificar el motivo de la cita");
            return false;
        }

        if (fecha == null) {
            mostrarError("Debe seleccionar una fecha para la cita");
            return false;
        }

        if (hora == null) {
            mostrarError("Debe seleccionar una hora para la cita");
            return false;
        }

        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la cita no puede ser en el pasado");
            return false;
        }

        return true;
    }

    private String determinarTurno(LocalTime hora) {
        if (!hora.isBefore(LocalTime.of(8, 0)) && hora.isBefore(LocalTime.of(16, 0))) {
            return "Diurno";
        } else if (!hora.isBefore(LocalTime.of(16, 0)) && hora.isBefore(LocalTime.of(24, 0))) {
            return "Nocturno";
        }
        return null;
    }

    public CitaMedica obtenerCitaPorId(int idCita) {
        return citaMedicaDAO.buscarPorId(idCita);
    }

    public boolean cancelarCita(int idCita, String razon) {
        boolean resultado = citaMedicaDAO.cancelarCita(idCita, razon);

        if (resultado) {
            mostrarExito("Cita cancelada exitosamente. Razón: " + razon);
        } else {
            mostrarError("Error al cancelar la cita");
        }

        return resultado;
    }

    public boolean atenderCita(int idCita, String diagnostico, String rutaArchivo) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            mostrarError("Debe ingresar un diagnóstico");
            return false;
        }

        boolean resultado = citaMedicaDAO.actualizarDiagnostico(idCita, diagnostico, rutaArchivo);

        if (resultado) {
            mostrarExito("Consulta registrada correctamente - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        } else {
            mostrarError("Error al registrar la consulta");
        }

        return resultado;
    }

}
