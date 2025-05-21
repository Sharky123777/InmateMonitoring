package DAO;

import Model.Entities.CitaMedica;
import Model.Constants.EstadoCitaMedicaEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CitaMedicaDAO {

    private static final String JSON_FILE = "src/Resources/DATA/citasmedicas.json";
    private static CitaMedicaDAO instancia;
    private final Gson gson;

    public static synchronized CitaMedicaDAO getInstancia() {
        if (instancia == null) {
            instancia = new CitaMedicaDAO();
        }
        return instancia;
    }

    private CitaMedicaDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

    }

    public List<CitaMedica> cargarTodas() {
        try (Reader reader = new FileReader(JSON_FILE)) {
            Type tipoLista = new TypeToken<ArrayList<CitaMedica>>() {
            }.getType();
            List<CitaMedica> citas = gson.fromJson(reader, tipoLista);
            return citas != null ? citas : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public boolean guardarCita(CitaMedica cita) {
        List<CitaMedica> citas = cargarTodas();

        if (cita.getId() == 0) {
            int maxId = citas.stream().mapToInt(CitaMedica::getId).max().orElse(0);
            cita.setId(maxId + 1);
        }

        citas.removeIf(c -> c.getId() == cita.getId());
        citas.add(cita);

        return guardarTodas(citas);
    }

    private boolean guardarTodas(List<CitaMedica> citas) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(citas, writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<CitaMedica> obtenerPorPreso(String identificacionPreso) {
        return cargarTodas().stream()
                .filter(c -> c.getPreso().getIdentificacion().equals(identificacionPreso))
                .collect(Collectors.toList());
    }

    public List<CitaMedica> obtenerPorEstado(EstadoCitaMedicaEnum estado) {
        return cargarTodas().stream()
                .filter(c -> c.getEstado() == estado)
                .collect(Collectors.toList());
    }

    public CitaMedica buscarPorId(int id) {
        return cargarTodas().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<CitaMedica> obtenerPorEnfermera(String identificacionEnfermera) {
        return cargarTodas().stream()
                .filter(c -> c.getEnfermera().getIdentificacion().equals(identificacionEnfermera))
                .collect(Collectors.toList());
    }

    public boolean actualizarDiagnostico(int idCita, String diagnostico, String rutaArchivo) {
        CitaMedica cita = buscarPorId(idCita);
        if (cita != null) {
            cita.setDiagnostico(diagnostico);
            cita.setRutaHistoriaClinica(rutaArchivo);
            cita.setFechaHoraAtencion(LocalDateTime.now());
            cita.setEstado(EstadoCitaMedicaEnum.ATENDIDO);
            return guardarCita(cita);
        }
        return false;
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>,
            JsonDeserializer<LocalDateTime> {

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc,
                JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }

    public boolean marcarComoPrioritario(int idCita) {
        CitaMedica cita = buscarPorId(idCita);
        if (cita != null) {
            cita.setEstado(EstadoCitaMedicaEnum.PRIORITARIO);
            return guardarCita(cita);
        }
        return false;
    }

    public boolean cancelarCita(int idCita) {
        CitaMedica cita = buscarPorId(idCita);
        if (cita != null) {
            cita.setEstado(EstadoCitaMedicaEnum.CANCELADO);
            return guardarCita(cita);
        }
        return false;
    }

    private static class LocalDateAdapter implements com.google.gson.JsonSerializer<LocalDate>,
            com.google.gson.JsonDeserializer<LocalDate> {

        @Override
        public com.google.gson.JsonElement serialize(LocalDate src, Type typeOfSrc,
                com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }

        @Override
        public LocalDate deserialize(com.google.gson.JsonElement json, Type typeOfT,
                com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }

    private static class LocalTimeAdapter implements com.google.gson.JsonSerializer<LocalTime>,
            com.google.gson.JsonDeserializer<LocalTime> {

        @Override
        public com.google.gson.JsonElement serialize(LocalTime src, Type typeOfSrc,
                com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }

        @Override
        public LocalTime deserialize(com.google.gson.JsonElement json, Type typeOfT,
                com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            return LocalTime.parse(json.getAsString());
        }
    }

    public boolean cancelarCita(int idCita, String razon) {
        CitaMedica cita = buscarPorId(idCita);
        if (cita != null) {
            cita.setEstado(EstadoCitaMedicaEnum.CANCELADO);
            cita.setDiagnostico("CANCELADA - Razón: " + razon);
            return guardarCita(cita);
        }
        return false;
    }

}
