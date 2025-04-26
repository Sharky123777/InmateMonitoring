package Controller;

import DAO.ActividadDAO;
import Model.Actividad;
import Model.Preso;
import Utilidades.Validador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ActividadController {

  private static volatile ActividadController instancia;
    
    private final ActividadDAO actividadDAO;
    
    private ActividadController() {
        this.actividadDAO = ActividadDAO.getInstancia(); 
    }
    
    public static ActividadController getInstancia() {
        ActividadController result = instancia;
        if (result == null) {
            synchronized (ActividadController.class) {
                result = instancia;
                if (result == null) {
                    instancia = result = new ActividadController();
                }
            }
        }
        return result;
    }
    public void agregarActividad(String nombre, String tipo, Object dia, Object horario, String lugar, String cupoMaximoStr, String responsable) {
        try {
            Validador.validarActividadCompleta(nombre, tipo, dia, horario, lugar, cupoMaximoStr, responsable);

            int cupoMaximo = Integer.parseInt(cupoMaximoStr);
            String idActividad = generarIdUnico();
            Actividad actividad = new Actividad(idActividad, nombre, tipo, dia.toString(), horario.toString(), lugar, cupoMaximo, responsable);

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

    public boolean asignarPresoAActividad(String idActividad, String identificacionPr) {
        if (actividadDAO.asignarPresoAActividad(idActividad, identificacionPr)) {
            Validador.mostrarInfo("Preso asignado exitosamente a la actividad");
            return true;
        } else {
            Validador.mostrarAdvertencia("No se pudo asignar el preso. Verifique el cupo o si ya está asignado");
            return false;
        }
    }

    
    public List<Actividad> buscarActividadesPorPreso(String identificacionP) {
        return actividadDAO.buscarActividadesPorPreso(identificacionP);
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

    public void cargarActividadesDisponiblesEnTabla(JTable tablaActividades, String identificacionP) {
        DefaultTableModel modelo = (DefaultTableModel) tablaActividades.getModel();
        modelo.setRowCount(0);

        List<Actividad> actividadesDisponibles = actividadDAO.buscarActividadesDisponiblesParaPreso(identificacionP);

        for (Actividad actividad : actividadesDisponibles) {
            modelo.addRow(new Object[]{
                false,
                actividad.getIdActividad(),
                actividad.getNombre(),
                actividad.getTipo(),
                actividad.getDia(),
                actividad.getHorario(),
                actividad.getLugar(),
                actividad.getPresosInscritos() + "/" + actividad.getCupoMaximo()
            });
        }
    }

    public void cargarActividadesPresoEnTabla(JTable tabla, String identificacionP) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Actividad> actividades = actividadDAO.buscarActividadesPorPreso(identificacionP);

        for (Actividad actividad : actividades) {
            modelo.addRow(new Object[]{
                actividad.getNombre(),
                actividad.getTipo(),
                actividad.getLugar(),
                actividad.getDia(),
                actividad.getHorario(),
                actividad.getEstado(),
                actividad.getResponsable()
            });
        }
    }
    
public void cargarPresosAsignadosEnTabla(JTable tablaPresos, String idActividad) {
    DefaultTableModel model = (DefaultTableModel) tablaPresos.getModel();
    model.setRowCount(0); 

    Actividad actividad = actividadDAO.buscarActividadPorId(idActividad);
    
    if (actividad == null || actividad.getPresosAsignadosIds().isEmpty()) {
        model.addRow(new Object[]{"", "", "No hay presos asignados", "", "", ""});
        return;
    }

    PresoController presoController = PresoController.getInstancia();
    
    for (String idPreso : actividad.getPresosAsignadosIds()) {
        try {
            Preso preso = presoController.buscarPreso(idPreso);
            
            if (preso != null) {
                model.addRow(new Object[]{
                    preso.getNombresCompletos(),
                    preso.getApellidosCompletos(),
                    preso.getEdad(),
                    preso.getIdentificacion(),
                    actividad.getHorario(),
                    actividad.getEstado()
                });
            }
        } catch (Exception e) {
            System.err.println("Error cargando preso " + idPreso + ": " + e.getMessage());
        }
    }
}
}