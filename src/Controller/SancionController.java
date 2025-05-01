package Controller;

import DAO.PresoDAO;
import DAO.SancionDAO;
import Model.Preso;
import Model.Sancion;
import View.Oficial;
import java.time.LocalDate;
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

        if (!identificacionPreso.matches("\\d+")) {
            mostrarError("La identificación del preso debe contener solo números");
            return false;
        }

        if (!identificacionPreso.matches("\\d{6,10}")) {
            mostrarError("La identificación debe contener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        LocalDate fecha = fechaSeleccionada.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de la sanción no puede ser futura.\nEste formulario es para registrar sanciones ya aplicadas.");
            return false;
        }

        try {
            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacionPreso);
            if (preso == null) {
                mostrarError("No se encontró ningún preso con esa identificación");
                return false;
            }
            Sancion nuevaSancion = new Sancion(0, motivoSancion, fecha, tipoSancion, preso);
            boolean exito = sancionDAO.guardarSancion(nuevaSancion);

            if (exito) {
                mostrarExito("La sanción se registró con éxito");
                limpiarCamposSancion(view);
                return true;
            } else {
                mostrarError("No se pudo guardar la sanción");
                return false;
            }

        } catch (Exception e) {
            mostrarError("Error al registrar la sanción: " + e.getMessage());
            return false;
        }
    }

    private boolean validarCamposSancion(Oficial view) {
        if (view.getIdentificacionPresoSancion().getText().trim().isEmpty()) {
            mostrarError("Debe ingresar la identificación del preso");
            return false;
        }

        if (view.getMotivoSancion().getText().trim().isEmpty()) {
            mostrarError("Debe especificar el motivo de la sanción");
            return false;
        }

        if (view.getTipoSancion().getSelectedItem().toString().equals("< Seleccionar >")) {
            mostrarError("Debe seleccionar un tipo de sanción");
            return false;
        }

        if (view.getFechaSancion().getDate() == null) {
            mostrarError("Debe seleccionar la fecha de la sanción");
            return false;
        }

        return true;
    }

    public void limpiarCamposSancion(Oficial view) {
        view.getIdentificacionPresoSancion().setText("");
        view.getMotivoSancion().setText("");
        view.getTipoSancion().setSelectedIndex(0);
        view.getFechaSancion().setDate(null);
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
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo()
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
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo()
            });
        }

    }

    public void filtrarSanciones(String identificacionPreso, String tipoSeleccionado, JTable tabla) {
        if (identificacionPreso == null || identificacionPreso.trim().isEmpty()) {
            mostrarError("No se ha seleccionado un preso.");
            return;
        }

        if (tipoSeleccionado == null || tipoSeleccionado.equals("< Seleccionar >")) {
            cargarHistorialSanciones(identificacionPreso, tabla);
        } else {
            cargarHistorialSancionesFiltrado(identificacionPreso, tipoSeleccionado, tabla);
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
                sancion.getPreso().getIdentificacion(),
                sancion.getMotivo()
            });
        }
    }

}
