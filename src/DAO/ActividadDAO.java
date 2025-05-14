package DAO;

import Model.Constants.EstadoActividadesEnum;
import Model.Constants.EstadoActividadesPresoEnum;
import Model.Entities.Actividad;
import Model.Entities.Oficial;
import Model.Entities.Preso;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class ActividadDAO {

    private static ActividadDAO instancia;

    private ActividadDAO() {
    }

    public static ActividadDAO getInstancia() {
        if (instancia == null) {
            instancia = new ActividadDAO();
        }
        return instancia;
    }

    private static final String JSON_FILE = "src/Resources/DATA/actividades.json";
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }

    public List<Actividad> cargarActividades() {
        File archivo = new File(JSON_FILE);
        archivo.getParentFile().mkdirs();

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarActividades(new ArrayList<>());
                return new ArrayList<>();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear el archivo de actividades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return new ArrayList<>();
            }
        }

        if (archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(JSON_FILE)) {
            Type tipoListaActividad = new TypeToken<ArrayList<Actividad>>() {
            }.getType();
            List<Actividad> actividades = gson.fromJson(reader, tipoListaActividad);

            if (actividades != null) {
                for (Actividad act : actividades) {
                    act.setPresosInscritos(act.getPresosAsignadosIds().size());
                }
            }

            return actividades != null ? actividades : new ArrayList<>();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de actividades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<Actividad> buscarPorTipo(String tipo) {
        List<Actividad> todas = cargarActividades();
        return todas.stream()
                .filter(a -> a.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    public boolean guardarActividades(List<Actividad> actividades) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(actividades, writer);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar las actividades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean agregarActividad(Actividad actividad) {
        if (actividad == null) {
            JOptionPane.showMessageDialog(null, "La actividad no puede ser nula.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        List<Actividad> actividades = cargarActividades();
        actividades.add(actividad);
        return guardarActividades(actividades);
    }

    public Actividad buscarActividadPorId(String idActividad) {
        List<Actividad> actividades = cargarActividades();
        for (Actividad act : actividades) {
            if (act.getIdActividad().equals(idActividad)) {
                return act;
            }
        }
        return null;
    }

    public boolean eliminarActividad(String idActividad) {
        List<Actividad> actividades = cargarActividades();
        boolean eliminado = actividades.removeIf(act -> act.getIdActividad().equals(idActividad));

        if (eliminado) {
            if (guardarActividades(actividades)) {
                JOptionPane.showMessageDialog(null, "Actividad eliminada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró la actividad a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public boolean actualizarActividad(String idOriginal,
            String nuevoNombre,
            String nuevoDia,
            String nuevoHorario,
            String nuevoLugar,
            Integer nuevoCupoMaximo,
            String nuevoResponsableOficial) {
        List<Actividad> actividades = cargarActividades();
        boolean encontrado = false;

        for (Actividad actividad : actividades) {
            if (actividad.getIdActividad().equals(idOriginal)) {
                encontrado = true;

                if (nuevoNombre != null) {
                    actividad.setNombre(nuevoNombre);
                }
                if (nuevoDia != null) {
                    actividad.setDia(nuevoDia);
                }
                if (nuevoHorario != null) {
                    actividad.setHorario(nuevoHorario);
                }
                if (nuevoLugar != null) {
                    actividad.setLugar(nuevoLugar);
                }
                if (nuevoCupoMaximo != null) {
                    if (nuevoCupoMaximo < actividad.getPresosInscritos()) {
                        JOptionPane.showMessageDialog(null,
                                "El nuevo cupo no puede ser menor a la cantidad actual de presos inscritos ("
                                + actividad.getPresosInscritos() + ").",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    actividad.setCupoMaximo(nuevoCupoMaximo);
                }
                if (nuevoResponsableOficial != null) {
                    actividad.setResponsableOficial(nuevoResponsableOficial);
                }
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null,
                    "Actividad no encontrada: " + idOriginal,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return guardarActividades(actividades);
    }

    public List<Actividad> buscarActividadesPorPreso(String identificacionP) {
        List<Actividad> actividades = cargarActividades();
        List<Actividad> resultado = new ArrayList<>();
        for (Actividad act : actividades) {
            if (act.getPresosAsignadosIds().contains(String.valueOf(identificacionP))) {
                resultado.add(act);
            }
        }
        return resultado;
    }

    public boolean asignarPresoAActividad(String idActividad, String identificacionP) {
        List<Actividad> actividades = cargarActividades();
        for (Actividad act : actividades) {
            if (act.getIdActividad().equals(idActividad)) {
                if (act.getPresosInscritos() >= act.getCupoMaximo()) {
                    JOptionPane.showMessageDialog(null,
                            "No hay cupo disponible en esta actividad.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                if (act.getPresosAsignadosIds().contains(identificacionP)) {
                    JOptionPane.showMessageDialog(null,
                            "Este preso ya está asignado a esta actividad.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                if (!puedeAsignarActividadAPreso(identificacionP)) {
                    JOptionPane.showMessageDialog(null,
                            "Este preso ha alcanzado el límite de actividades permitidas.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                act.getPresosAsignadosIds().add(identificacionP);
                act.setPresosInscritos(act.getPresosInscritos() + 1);

                if (guardarActividades(actividades)) {
                    JOptionPane.showMessageDialog(null,
                            "Preso asignado a la actividad correctamente.",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    cargarActividades();
                    
                    return true;
                } else {
                    return false;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Actividad no encontrada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    public boolean removerPresoDeActividad(String idActividad, String idPreso) {
        List<Actividad> actividades = cargarActividades();
        for (Actividad act : actividades) {
            if (act.getIdActividad().equals(idActividad)) {
                if (act.getPresosAsignadosIds().remove(idPreso)) {
                    act.setPresosInscritos(act.getPresosInscritos() - 1);
                    if (guardarActividades(actividades)) {
                        JOptionPane.showMessageDialog(null,
                                "Preso removido de la actividad correctamente.",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "El preso no estaba asignado a esta actividad.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Actividad no encontrada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    public boolean cancelarActividad(String idActividad) {
        List<Actividad> actividades = cargarActividades();
        for (Actividad act : actividades) {
            if (act.getIdActividad().equals(idActividad)) {
                act.setEstado(EstadoActividadesEnum.CANCELADA);
                if (guardarActividades(actividades)) {
                    JOptionPane.showMessageDialog(null, "Actividad cancelada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Actividad no encontrada para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    public List<Actividad> buscarActividadesPorEstado(EstadoActividadesEnum estado) {
        List<Actividad> todas = cargarActividades();
        List<Actividad> filtradas = new ArrayList<>();
        for (Actividad actividad : todas) {
            if (actividad.getEstado()== (estado)) {
                filtradas.add(actividad);
            }
        }
        return filtradas;
    }

    public List<Actividad> buscarActividadesDisponiblesParaPreso(String identificacionP) {
        List<Actividad> todas = cargarActividades()
                .stream()
                .filter(a -> a.getEstado()== (EstadoActividadesEnum.ACTIVA))
                .collect(Collectors.toList());

        List<String> idsAsignadas = buscarActividadesPorPreso(identificacionP)
                .stream()
                .map(Actividad::getIdActividad)
                .collect(Collectors.toList());

        return todas.stream()
                .filter(a -> !idsAsignadas.contains(a.getIdActividad()))
                .filter(a -> a.getPresosInscritos() < a.getCupoMaximo())
                .collect(Collectors.toList());
    }

    public boolean tieneActividadEnMismoHorario(Oficial responsable, Object dia, Object horario) {
        List<Actividad> actividades = cargarActividades();
        for (Actividad actividad : actividades) {
            if (actividad.getResponsableOficial() != null
                    && actividad.getResponsableOficial().equals(responsable.getIdentificacion())
                    && actividad.getDia().equals(dia)
                    && actividad.getHorario().equals(horario)) {
                return true;
            }
        }
        return false;
    }

    public int contarActividadesResponsable(String responsableId) {
        List<Actividad> actividades = cargarActividades();

        int count = 0;
        for (Actividad actividad : actividades) {
            if (actividad.getResponsableOficial().equals(responsableId)
                    && !actividad.getHorario().equalsIgnoreCase("Cancelada")) {
                count++;
            }
        }
        return count;
    }

    public int contarActividadesPreso(String presoId) {
        List<Actividad> actividades = cargarActividades();
        int count = 0;

        for (Actividad actividad : actividades) {
            if (actividad.getPresosAsignadosIds().contains(presoId)
                    && !actividad.getEstado().equals(EstadoActividadesEnum.CANCELADA)
                    && !actividad.getEstado().equals(EstadoActividadesPresoEnum.FINALIZADA)) {
                count++;
            }
        }

        return count;
    }

    public boolean puedeAsignarActividadAPreso(String presoId) {
        int actividadesAsignadas = contarActividadesPreso(presoId);
        int limiteActividadesPreso = 5;
        return actividadesAsignadas < limiteActividadesPreso;
    }

    public String obtenerProximoIdActividad() {
        List<Actividad> actividades = cargarActividades();
        if (actividades.isEmpty()) {
            return "ACT-1";
        }

        int maxId = actividades.stream()
                .mapToInt(a -> Integer.parseInt(a.getIdActividad().replace("ACT-", "")))
                .max()
                .orElse(0);

        return "ACT-" + (maxId + 1);
    }

    public boolean actualizarEstadoActividad(String idActividad, EstadoActividadesEnum nuevoEstado) {
        List<Actividad> actividades = cargarActividades();
        boolean encontrado = false;

        for (Actividad actividad : actividades) {
            if (actividad.getIdActividad().equals(idActividad)) {
                actividad.setEstado(nuevoEstado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró la actividad para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return guardarActividades(actividades);
    }

   public void actualizarEstadoPresosActividad(String idActividad, EstadoActividadesEnum nuevoEstado) {
    List<Actividad> actividades = cargarActividades();
    PresoDAO presoDAO = PresoDAO.getInstancia();

    for (Actividad actividad : actividades) {
        if (actividad.getIdActividad().equals(idActividad)) {
            if (nuevoEstado == EstadoActividadesEnum.CANCELADA) {
                for (String idPreso : actividad.getPresosAsignadosIds()) {
                    Preso preso = presoDAO.buscarPresoPorIdentificacion(idPreso);
                    if (preso != null) {
                        preso.marcarActividadCancelada(idActividad);
                        presoDAO.actualizarPreso(preso);
                    }
                }
            }
            
            actividad.setEstado(nuevoEstado);
            
            if (nuevoEstado == EstadoActividadesEnum.CANCELADA || 
                nuevoEstado == EstadoActividadesEnum.FINALIZADA) {
                
                EstadoActividadesPresoEnum estadoPreso = nuevoEstado == EstadoActividadesEnum.CANCELADA ? 
                    EstadoActividadesPresoEnum.CANCELADA : 
                    EstadoActividadesPresoEnum.FINALIZADA;
                
                for (String idPreso : actividad.getPresosAsignadosIds()) {
                    actividad.setEstadoPreso(idPreso, estadoPreso);
                }
            }
            
            break; 
        }
    }

    guardarActividades(actividades);
}
   
   
    public boolean tieneActividadEnMismoHorarioPreso(String idPreso, Object dia, Object horario) {
        List<Actividad> actividades = cargarActividades();
        for (Actividad actividad : actividades) {
            if (!actividad.getEstado().equals("ACTIVA")) {
                continue;
            }

            if (actividad.getPresosAsignadosIds().contains(idPreso)
                    && actividad.getDia().equals(dia)
                    && actividad.getHorario().equals(horario)) {
                return true;
            }
        }
        return false;
    }
}
