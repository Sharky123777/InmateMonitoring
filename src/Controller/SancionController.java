package Controller;

import DAO.GuardiaDAO;
import DAO.PresoDAO;
import DAO.SancionDAO;
import Model.Entities.Guardia;
import Model.Entities.Preso;
import Model.Entities.Sancion;
import View.Oficial;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SancionController {

    private final SancionDAO sancionDAO;
    private final PresoDAO presoDAO;

    public SancionController() {
        this.sancionDAO = new SancionDAO();
        this.presoDAO = new PresoDAO();
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

        String identificacionPreso = view.getIdentificacionPresoSancion().getText().trim();
        String motivoSancion = view.getMotivoSancion().getText().trim();
        String tipoSancion = view.getTipoSancion().getSelectedItem().toString();
        Date fechaSeleccionada = view.getFechaSancion().getDate();
        String duracion = view.getDuracionSancion().getSelectedItem().toString();
        String horaStr = view.getHoraSancion().getSelectedItem().toString();
        String identificacionGuardia = view.getIdentificacionGuardiaSancion().getText().trim();

        if (!identificacionPreso.matches("\\d{6,10}")) {
            mostrarError("La identificación del preso debe tener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        if (!identificacionGuardia.matches("\\d{6,10}")) {
            mostrarError("La identificación del guardia debe tener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        if (duracion.equals("< Seleccionar >")) {
            mostrarError("Debe seleccionar una duración para la sanción.");
            return false;
        }

        if (horaStr.equals("< Seleccionar >")) {
            mostrarError("Debe seleccionar una hora para la sanción.");
            return false;
        }

        LocalDate fecha = fechaSeleccionada.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de la sanción no puede ser futura.");
            return false;
        }

        try {
            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                mostrarError("No se encontró ningún preso con esa identificación.");
                return false;
            }

            Guardia guardia = new GuardiaDAO().obtenerGuardiaPorCedula(identificacionGuardia);
            if (guardia == null) {
                mostrarError("No se encontró ningún guardia con esa identificación.");
                return false;
            }

            LocalTime hora = LocalTime.parse(horaStr);

            Sancion nuevaSancion = new Sancion(
                    0, motivoSancion, fecha, hora, duracion, tipoSancion, preso, guardia
            );

            boolean exito = sancionDAO.guardarSancion(nuevaSancion);

            if (exito) {
                mostrarExito("La sanción se registró con éxito.");
                limpiarCamposSancion(view);
                return true;
            } else {
                mostrarError("No se pudo guardar la sanción.");
                return false;
            }

        } catch (Exception e) {
            mostrarError("Error al registrar la sanción: " + e.getMessage());
            return false;
        }
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

        if (view.getDuracionSancion().getSelectedIndex() == 0) {
            mostrarError("Debe seleccionar una duración.");
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

        return true;
    }

    public void limpiarCamposSancion(Oficial view) {
        view.getIdentificacionPresoSancion().setText("");
        view.getMotivoSancion().setText("");
        view.getTipoSancion().setSelectedIndex(0);
        view.getFechaSancion().setDate(null);
        view.getDuracionSancion().setSelectedIndex(0);
        view.getHoraSancion().setSelectedIndex(0);
        view.getIdentificacionGuardiaSancion().setText("");
    }

    public void cargarHistorialSanciones(String identificacionPreso, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = sancionDAO.cargarPorIdentificacionPreso(identificacionPreso);

        for (Sancion sancion : sanciones) {
            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDuracionEnHoras(),
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion()
            });
        }
    }

    public void filtrarSancionesPorTipo(String identificacionPreso, String tipoSancion, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones;

        if (tipoSancion.equals("< Seleccionar >")) {
            sanciones = sancionDAO.cargarPorIdentificacionPreso(identificacionPreso);
        } else {
            sanciones = sancionDAO.cargarPorTipoYIdentificacionPreso(tipoSancion, identificacionPreso);
        }

        for (Sancion sancion : sanciones) {
            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDuracionEnHoras(),
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion()
            });
        }
    }

    public void cargarHistorialSancionesFiltrado(String identificacionPreso, String tipoSancion, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Sancion> sanciones = sancionDAO.cargarPorTipoYIdentificacionPreso(tipoSancion, identificacionPreso);
        llenarTablaSanciones(modelo, sanciones);

        tabla.revalidate();
        tabla.repaint();
    }

    private void llenarTablaSanciones(DefaultTableModel modelo, List<Sancion> sanciones) {
        for (Sancion sancion : sanciones) {
            modelo.addRow(new Object[]{
                sancion.getId(),
                sancion.getTipoSancion(),
                sancion.getFechaSancion(),
                sancion.getHora(),
                sancion.getDuracionEnHoras(),
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo(),
                sancion.getGuardia().getIdentificacion()
            });
        }
    }

}
