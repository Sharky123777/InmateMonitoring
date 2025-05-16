package Controller;

import DAO.ActividadDAO;
import DAO.OficialDAO;
import DAO.PresoDAO;
import Model.Constants.EstadoActividadesEnum;
import Model.Constants.EstadoActividadesPresoEnum;
import Model.Entities.Actividad;
import Model.Entities.Oficial;
import Model.Entities.Preso;
import Utilidades.Validador;
import View.ActividadRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ActividadController {

    private static volatile ActividadController instancia;
    private final OficialDAO oficialDAO;
    private final ActividadDAO actividadDAO;
    private final PresoDAO presoDAO;
    PresoController pc = PresoController.getInstancia();

    private ActividadController() {
        this.actividadDAO = ActividadDAO.getInstancia();
        this.presoDAO = PresoDAO.getInstancia();
        this.oficialDAO = OficialDAO.getInstancia();
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

    public boolean puedeAgregarActividad(Oficial responsable) {
        int limiteActividades = 2;
        int actividadesActuales = actividadDAO.contarActividadesResponsable(responsable.getIdentificacion());
        return actividadesActuales < limiteActividades;
    }

    public boolean agregarActividad(String nombre, String tipo, Object dia, Object horario, String lugar, String cupoMaximoStr, Oficial responsable, String descripcion) {
        try {
            Validador.validarActividadCompleta(nombre, tipo, dia, horario, lugar, cupoMaximoStr);

            ActividadController controller = ActividadController.getInstancia();

            boolean tieneActividad = actividadDAO.tieneActividadEnMismoHorario(responsable, dia, horario);

            if (tieneActividad) {
                Validador.mostrarError("El oficial ya tiene una actividad en el mismo día y horario.");
                return false;
            }

            if (!controller.puedeAgregarActividad(responsable)) {
                Validador.mostrarError("El responsable ya tiene el límite de actividades alcanzado.");
                return false;
            }

            int cupoMaximo = Integer.parseInt(cupoMaximoStr);
            String idActividad = actividadDAO.obtenerProximoIdActividad();

            Actividad actividad = new Actividad(
                    idActividad,
                    nombre,
                    tipo,
                    dia.toString(),
                    horario.toString(),
                    lugar,
                    cupoMaximo,
                    responsable.getIdentificacion(),
                    descripcion
            );

            if (actividadDAO.agregarActividad(actividad)) {
                Validador.mostrarInfo("Actividad agregada exitosamente");
                return true;
            } else {
                Validador.mostrarError("No se pudo agregar la actividad");
                return false;
            }

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
            return false;
        }
    }

    public void cancelarActividad(String idActividad) {
        if (actividadDAO.cancelarActividad(idActividad)) {
            Validador.mostrarInfo("Actividad cancelada correctamente");
        } else {
            Validador.mostrarError("No se pudo cancelar la actividad");
        }
    }

    public boolean asignarPresoAActividad(String idActividad, String identificacionPr, JTable tablaGeneral) {
        if (actividadDAO.asignarPresoAActividad(idActividad, identificacionPr)) {
            cargarActividadesEnTabla(tablaGeneral);
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

    public List<Actividad> buscarActividadesPorEstado(EstadoActividadesEnum estado) {
        return actividadDAO.buscarActividadesPorEstado(estado);
    }

    public void cargarActividadesEnTabla(JTable actividadesTabla) {
        List<Actividad> actividades = actividadDAO.cargarActividades();
        DefaultTableModel modelo = (DefaultTableModel) actividadesTabla.getModel();
        modelo.setRowCount(0);

        for (Actividad actividad : actividades) {
            Oficial oficial = oficialDAO.obtenerOficialPorCedula(actividad.getResponsableOficial());
            String nombreOficial = (oficial != null) ? oficial.getPrimerNombre() + " " + oficial.getPrimerApellido() : "Sin asignar";

            modelo.addRow(new Object[]{
                actividad.getIdActividad(),
                actividad.getNombre(),
                actividad.getTipo(),
                actividad.getDia(),
                actividad.getHorario(),
                actividad.getLugar(),
                actividad.getCupoMaximo(),
                actividad.getPresosInscritos(),
                nombreOficial,
                actividad.getEstado(),
                actividad.getDescripcion()
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

            Oficial oficial = oficialDAO.obtenerOficialPorCedula(actividad.getResponsableOficial());
            String nombreOficial = (oficial != null) ? oficial.getPrimerNombre() + " " + oficial.getPrimerApellido() : "Sin asignar";

            modelo.addRow(new Object[]{
                actividad.getNombre(),
                actividad.getTipo(),
                actividad.getLugar(),
                actividad.getDia(),
                actividad.getHorario(),
                actividad.getEstado(),
                nombreOficial
            });
        }

        tabla.getColumnModel().getColumn(5).setCellRenderer(new ActividadRenderer());
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
                        actividad.getEstadoPreso(preso.getIdentificacion()) // Solo el estado
                    });
                }
            } catch (Exception e) {
                System.err.println("Error cargando preso " + idPreso + ": " + e.getMessage());
            }
        }

    }

    public List<Object[]> obtenerActividadesPorTipo(String tipo) {
        List<Actividad> actividades = actividadDAO.buscarPorTipo(tipo);
        List<Object[]> filas = new ArrayList<>();

        for (Actividad actividad : actividades) {
            Oficial oficial = oficialDAO.obtenerOficialPorCedula(actividad.getResponsableOficial());
            String nombreOficial = (oficial != null) ? oficial.getPrimerNombre() + " " + oficial.getPrimerApellido() : "Sin asignar";

            filas.add(new Object[]{
                actividad.getIdActividad(),
                actividad.getNombre(),
                actividad.getTipo(),
                actividad.getDia(),
                actividad.getHorario(),
                actividad.getLugar(),
                actividad.getCupoMaximo(),
                actividad.getPresosInscritos(),
                nombreOficial
            });
        }

        return filas;
    }

    public List<Object[]> obtenerPresosParaActividades() {
        List<Preso> presos = presoDAO.cargarTodos();

        List<Object[]> filas = new ArrayList<>();

        for (Preso preso : presos) {
            if (preso.getNivelDeRiesgo().equalsIgnoreCase("RIESGO BAJO") && !preso.isEnAislamiento()) {
                ImageIcon foto = pc.obtenerFotoPreso(preso);

                filas.add(new Object[]{
                    foto,
                    preso.getId(),
                    preso.getNombresCompletos(),
                    preso.getApellidosCompletos(),
                    preso.getEdad(),
                    preso.getIdentificacion(),
                    preso.getNacionalidad(),
                    preso.getSeccionAsignada(),
                    preso.getCeldaAsignada()
                });
            }
        }

        return filas;
    }

    public boolean actualizarEstadoActividad(String idActividad, EstadoActividadesEnum nuevoEstado) {
        return actividadDAO.actualizarEstadoActividad(idActividad, nuevoEstado);
    }

    public void actualizarEstadoPresosActividad(String idActividad, EstadoActividadesEnum estado) {
        actividadDAO.actualizarEstadoPresosActividad(idActividad, estado);
    }

    public boolean hayCambiosActividad(Actividad actividadOriginal,
            String nombre,
            Object dia,
            Object horario,
            Object lugar,
            Object cupoMaximo,
            String responsableOficial,
            String descripcion) {

        if (actividadOriginal == null) {
            return false;
        }

        return Stream.of(
                !nombre.trim().isEmpty() && !nombre.equals(actividadOriginal.getNombre()),
                dia != null && !dia.toString().equals("<Seleccione>") && !dia.toString().equals(actividadOriginal.getDia()),
                horario != null && !horario.toString().equals("<Seleccione>") && !horario.toString().equals(actividadOriginal.getHorario()),
                lugar != null && !lugar.toString().equals("<Seleccione>") && !lugar.toString().equals(actividadOriginal.getLugar()),
                cupoMaximo != null && !cupoMaximo.toString().equals("<Seleccione>")
                && Integer.parseInt(cupoMaximo.toString()) != actividadOriginal.getCupoMaximo(),
                responsableOficial != null && !responsableOficial.trim().isEmpty()
                && !responsableOficial.equals(actividadOriginal.getResponsableOficial()),
                descripcion != null && !descripcion.trim().equals(actividadOriginal.getDescripcion())
        ).anyMatch(Boolean::booleanValue);
    }

    public boolean actualizarActividad(Actividad actividadOriginal,
            String nombre,
            Object dia,
            Object horario,
            Object lugar,
            Object cupoMaximo,
            String responsableOficial,
            String descripcion) {

        try {
            if (!hayCambiosActividad(actividadOriginal, nombre, dia, horario, lugar, cupoMaximo, responsableOficial, descripcion)) {
                Validador.mostrarAdvertencia("No hay cambios para guardar");
                return false;
            }

            String nombreFinal = nombre.trim().isEmpty() ? actividadOriginal.getNombre() : nombre.trim();
            String diaFinal = (dia == null || dia.toString().equals("<Seleccione>"))
                    ? actividadOriginal.getDia() : dia.toString();
            String horarioFinal = (horario == null || horario.toString().equals("<Seleccione>"))
                    ? actividadOriginal.getHorario() : horario.toString();
            String lugarFinal = (lugar == null || lugar.toString().equals("<Seleccione>"))
                    ? actividadOriginal.getLugar() : lugar.toString();
            int cupoFinal = (cupoMaximo == null || cupoMaximo.toString().equals("<Seleccione>"))
                    ? actividadOriginal.getCupoMaximo() : Integer.parseInt(cupoMaximo.toString());
            String responsableFinal = (responsableOficial == null || responsableOficial.trim().isEmpty())
                    ? actividadOriginal.getResponsableOficial() : responsableOficial.trim();

            String descripcionFinal = (descripcion == null || descripcion.trim().isEmpty())
                    ? actividadOriginal.getDescripcion() : descripcion.trim();

            if (!nombre.trim().isEmpty()) {
                Validador.validarNombre(nombre);
            }

            if (cupoMaximo != null && !cupoMaximo.toString().equals("<Seleccione>")) {
                Validador.validarCupoMaximo(cupoMaximo.toString());
                int nuevoCupo = Integer.parseInt(cupoMaximo.toString());
                if (nuevoCupo < actividadOriginal.getPresosInscritos()) {
                    throw new Exception("El cupo no puede ser menor a los presos inscritos");
                }
            }

            if (responsableOficial != null
                    && !responsableOficial.trim().isEmpty()
                    && !responsableOficial.equals(actividadOriginal.getResponsableOficial())) {

                Oficial oficial = oficialDAO.obtenerOficialPorCedula(responsableOficial);
                if (oficial == null) {
                    throw new Exception("No se encontró el guardia con ID: " + responsableOficial);
                }

                if (!puedeAgregarActividad(oficial)) {
                    throw new Exception("El guardia ya tiene 2 actividades asignadas");
                }

                if (!diaFinal.equals(actividadOriginal.getDia())
                        || !horarioFinal.equals(actividadOriginal.getHorario())) {

                    if (actividadDAO.tieneActividadEnMismoHorario(oficial, diaFinal, horarioFinal)) {
                        throw new Exception("El guardia ya tiene actividad en ese horario");
                    }
                }
            }

            if ((!diaFinal.equals(actividadOriginal.getDia())
                    || (!horarioFinal.equals(actividadOriginal.getHorario())))) {

                for (String idPreso : actividadOriginal.getPresosAsignadosIds()) {
                    if (actividadDAO.tieneActividadEnMismoHorarioPreso(idPreso, diaFinal, horarioFinal)) {
                        Preso preso = presoDAO.buscarPresoPorIdentificacion(idPreso);
                        throw new Exception("El preso " + preso.getNombresCompletos()
                                + " tiene conflicto de horario con la nueva programación");
                    }
                }
            }

            boolean resultado = actividadDAO.actualizarActividad(
                    actividadOriginal.getIdActividad(),
                    nombreFinal,
                    diaFinal,
                    horarioFinal,
                    lugarFinal,
                    cupoFinal,
                    responsableFinal,
                    descripcionFinal
            );

            if (resultado) {
                Validador.mostrarInfo("Actividad actualizada correctamente");
            } else {
                Validador.mostrarError("No se pudo actualizar la actividad");
            }

            return resultado;

        } catch (Exception e) {
            Validador.mostrarError("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

}
