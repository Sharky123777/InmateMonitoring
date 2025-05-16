package DAO;

import Model.Constants.EstadoVisitaEnum;
import Model.Entities.LocalDateAdapter;
import Model.Entities.LocalTimeAdapter;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class VisitaDAO {

    private static VisitaDAO instancia;
    private static final String JSON_FILE = "src/Resources/DATA/visitas.json";
    private Gson gson;

    public VisitaDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(new TypeToken<Map<Visitante, String>>() {
                }.getType(),
                        new VisitantesMapAdapter())
                .create();

    }

    public static synchronized VisitaDAO getInstancia() {
        if (instancia == null) {
            instancia = new VisitaDAO();
        }
        return instancia;
    }

    public List<Visita> cargarTodas() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarTodas(new ArrayList<>());
                return new ArrayList<>();
            } catch (IOException e) {
                System.err.println("Error al crear archivo JSON: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        if (archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(JSON_FILE)) {
            Type tipoListaVisita = new TypeToken<ArrayList<Visita>>() {
            }.getType();
            List<Visita> visitas = gson.fromJson(reader, tipoListaVisita);
            return visitas != null ? visitas : new ArrayList<>();
        } catch (JsonSyntaxException | IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            guardarTodas(new ArrayList<>());
            return new ArrayList<>();
        }
    }

    public void guardarVisita(Visita visita) {
        List<Visita> visitas = cargarTodas();

        if (visita.getId() == 0) {
            int nuevoId = obtenerProximoId(visitas);
            visita.setId(nuevoId);
        }

        visitas.add(visita);
        guardarTodas(visitas);
    }

    private int obtenerProximoId(List<Visita> visitas) {
        if (visitas.isEmpty()) {
            return 1;
        }
        return visitas.stream()
                .mapToInt(Visita::getId)
                .max()
                .orElse(0) + 1;
    }

    public List<Visita> cargarPorIdentificacionPreso(String identificacionPreso) {
        List<Visita> todasVisitas = cargarTodas();
        List<Visita> visitasFiltradas = new ArrayList<>();

        for (Visita visita : todasVisitas) {
            if (visita.getPreso() != null
                    && visita.getPreso().getIdentificacion().equals(identificacionPreso)) {
                visitasFiltradas.add(visita);
            }
        }

        return visitasFiltradas;
    }

    private void guardarTodas(List<Visita> visitas) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(visitas, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean actualizarVisita(int idVisita, Visita visitaActualizada) {
        List<Visita> visitas = cargarTodas();
        boolean encontrado = false;

        for (int i = 0; i < visitas.size(); i++) {
            if (visitas.get(i).getId() == idVisita) {
                visitas.set(i, visitaActualizada);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null,
                    "Visita no encontrada con ID: " + idVisita,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return guardarCambios(visitas);
    }

    public Visita modificarDatosVisitaYDevolver(int id,
            String tipo, String lugar) {

        List<Visita> visitas = cargarTodas();

        for (Visita visita : visitas) {
            if (visita.getId() == id) {

                if (tipo != null) {
                    visita.setTipoVisita(tipo);
                }
                if (lugar != null) {
                    visita.setLugarVisita(lugar);
                }

                if (guardarCambios(visitas)) {
                    return visita;
                }
                return null;
            }
        }
        return null;
    }

    private boolean guardarCambios(List<?> lista) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(lista, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar cambios: " + e.getMessage());
            return false;
        }
    }

    public Visita modificarEstadoVisitaYDevolver(int id, EstadoVisitaEnum nuevoEstado) {
        List<Visita> visitas = cargarTodas();

        for (Visita visita : visitas) {
            if (visita.getId() == id) {
                visita.setEstado(nuevoEstado);

                if (guardarCambios(visitas)) {
                    return visita;
                }
                return null;
            }
        }
        return null;
    }

    public Visita buscarVisitaPorId(int id) {
        List<Visita> visitas = cargarTodas();

        for (Visita visita : visitas) {
            if (visita.getId() == id) {
                return visita;
            }
        }

        return null;
    }

    public List<Visita> cargarPorIdentificacionPresoFechaYHora(String identificacionPreso, LocalDate fecha, LocalTime hora) {
        List<Visita> todasVisitas = cargarTodas();
        List<Visita> visitasFiltradas = new ArrayList<>();

        for (Visita visita : todasVisitas) {
            if (visita.getPreso() != null
                    && visita.getPreso().getIdentificacion().equals(identificacionPreso)
                    && visita.getFechaVisita().equals(fecha)
                    && visita.getHoraVisita().equals(hora)
                    && visita.getEstado() != EstadoVisitaEnum.CANCELADA) {
                visitasFiltradas.add(visita);
            }
        }

        return visitasFiltradas;
    }

    public boolean tieneVisitaEnFecha(String identificacionVisitante, LocalDate fecha) {
        List<Visita> todasVisitas = cargarTodas();

        for (Visita visita : todasVisitas) {
            if (visita.getEstado() != EstadoVisitaEnum.CANCELADA
                    && visita.getFechaVisita().equals(fecha)) {
                for (Visitante visitante : visita.getVisitantesConRelacion().keySet()) {
                    if (visitante.getIdentificacion().equals(identificacionVisitante)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Visita> obtenerVisitasParaFinalizar() {
        LocalDate hoy = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        return cargarTodas().stream()
                .filter(visita -> visita.getEstado() == EstadoVisitaEnum.EN_PROCESO)
                .filter(visita -> {
                    if (visita.getFechaVisita().isBefore(hoy)) {
                        return true;
                    }
                    if (visita.getFechaVisita().equals(hoy)) {
                        LocalTime horaFinVisita = visita.getHoraVisita().plusHours(1);
                        return horaActual.isAfter(horaFinVisita);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public int finalizarVisitasAutomaticamente() {
        List<Visita> visitasAFinalizar = obtenerVisitasParaFinalizar();
        if (visitasAFinalizar.isEmpty()) {
            return 0;
        }

        List<Visita> todasVisitas = cargarTodas();
        for (Visita visita : todasVisitas) {
            if (visitasAFinalizar.stream().anyMatch(v -> v.getId() == visita.getId())) {
                visita.setEstado(EstadoVisitaEnum.FINALIZADA);
            }
        }

        guardarTodas(todasVisitas);
        return visitasAFinalizar.size();
    }

}
