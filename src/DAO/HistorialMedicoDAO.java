package DAO;

import Model.Entities.HistorialMedico;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorialMedicoDAO {

    private static final String JSON_FILE = "src/Resources/DATA/historiales_medicos.json";
    private static HistorialMedicoDAO instancia;
    private final Gson gson;
    private Map<String, HistorialMedico> historiales;

    private HistorialMedicoDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new Model.Entities.LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new Model.Entities.LocalTimeAdapter())
                .create();
        cargarHistoriales();
    }

    public static synchronized HistorialMedicoDAO getInstancia() {
        if (instancia == null) {
            instancia = new HistorialMedicoDAO();
        }
        return instancia;
    }

    private void cargarHistoriales() {
        File archivo = new File(JSON_FILE);
        if (!archivo.exists()) {
            historiales = new HashMap<>();
            return;
        }

        try (FileReader reader = new FileReader(archivo)) {
            Type type = new TypeToken<Map<String, HistorialMedico>>() {
            }.getType();
            historiales = gson.fromJson(reader, type);
            if (historiales == null) {
                historiales = new HashMap<>();
            }
        } catch (IOException e) {
            historiales = new HashMap<>();
        }
    }

    private boolean guardarHistoriales() {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(historiales, writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void agregarRegistro(String identificacionPreso, String diagnostico,
            LocalDateTime fecha, String enfermera, String motivo) {

        HistorialMedico historial = historiales.computeIfAbsent(
                identificacionPreso,
                k -> new HistorialMedico(k)
        );

        historial.agregarRegistro(diagnostico, fecha, enfermera, motivo);
        guardarHistoriales();
    }

    public HistorialMedico obtenerHistorial(String identificacionPreso) {
        return historiales.getOrDefault(identificacionPreso, new HistorialMedico(identificacionPreso));
    }
}
