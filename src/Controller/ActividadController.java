

package Controller;

import DAO.ActividadDAO;
import Model.Actividad;
import Utilidades.Validador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ActividadController {

    private final ActividadDAO actividadDAO;

    public ActividadController() {
        this.actividadDAO = ActividadDAO.getInstancia();
    }

    public void agregarActividad(String nombre, String tipo, Object dia, Object horario, String lugar, String cupoMaximoStr) {
        try {
            Validador.validarActividadCompleta(nombre, tipo, dia, horario, lugar, cupoMaximoStr);

            int cupoMaximo = Integer.parseInt(cupoMaximoStr);
            String idActividad = generarIdUnico(); 
            Actividad actividad = new Actividad(idActividad, nombre, tipo, dia.toString(), horario.toString(), lugar, cupoMaximo);

            if (actividadDAO.agregarActividad(actividad)) {
                Validador.mostrarInfo("Actividad agregada exitosamente");
            } else {
                Validador.mostrarError("No se pudo agregar la actividad");
            }
        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
        }
    }

    public void actualizarActividad(String idActividad, Object nuevoHorario, String nuevoCupoMaximoStr) {
        try {
            Validador.validarHorarioActividad(nuevoHorario);
            Validador.validarCupoMaximo(nuevoCupoMaximoStr);

            int nuevoCupoMaximo = Integer.parseInt(nuevoCupoMaximoStr);

            if (actividadDAO.actualizarActividad(idActividad, nuevoHorario.toString(), nuevoCupoMaximo)) {
                Validador.mostrarInfo("Actividad actualizada correctamente");
            } else {
                Validador.mostrarError("No se pudo actualizar la actividad");
            }
        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
        }
    }

    public void cancelarActividad(String idActividad) {
        if (actividadDAO.cancelarActividad(idActividad)) {
            Validador.mostrarInfo("Actividad cancelada correctamente");
        } else {
            Validador.mostrarError("No se pudo cancelar la actividad");
        }
    }

    public void asignarPresoAActividad(String idActividad, String idPreso) {
        if (actividadDAO.asignarPresoAActividad(idActividad, idPreso)) {
            Validador.mostrarInfo("Preso asignado exitosamente a la actividad");
        } else {
            Validador.mostrarAdvertencia("No se pudo asignar el preso. Verifique el cupo o si ya está asignado");
        }
    }

    public List<Actividad> buscarActividadesPorPreso(String idPreso) {
        return actividadDAO.buscarActividadesPorPreso(idPreso);
    }

    public List<Actividad> buscarActividadesPorEstado(String estado) {
        return actividadDAO.buscarActividadesPorEstado(estado);
    }

    private String generarIdUnico() {
        return "ACT-" + System.currentTimeMillis();
    }
    



    public void cargarActividadesEnTabla(JTable actividadesTabla) {
        List<Actividad> actividades = actividadDAO.cargarActividades();
        DefaultTableModel modelo = (DefaultTableModel) actividadesTabla.getModel();
        modelo.setRowCount(0);

        for (Actividad actividad : actividades) {
            modelo.addRow(new Object[]{
                actividad.getIdActividad(),
                actividad.getNombre(),
                actividad.getTipo(),
                actividad.getDia(),
                actividad.getHorario(),
                actividad.getLugar(),
                actividad.getCupoMaximo(),
                actividad.getPresosInscritos()
                    
            });
        }
    }

  
    
}
