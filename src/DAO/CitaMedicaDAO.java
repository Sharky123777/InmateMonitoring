package DAO;

import Model.CitaMedica;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Model.LocalDateAdapter;

public class CitaMedicaDAO {

    private static final String JSON_FILE = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\citasMedicas.json";
    private Gson gson;

    public CitaMedicaDAO() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public List<CitaMedica> cargarTodas() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarTodas(new ArrayList<>());
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de citas: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        try (FileReader reader = new FileReader(archivo)) {
            Type listType = new TypeToken<List<CitaMedica>>(){}.getType();
            List<CitaMedica> citas = gson.fromJson(reader, listType);
            return citas != null ? citas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de citas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarTodas(List<CitaMedica> citas) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(citas, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar las citas: " + e.getMessage());
        }
    }

    public boolean guardarCita(CitaMedica cita) {
        List<CitaMedica> citas = cargarTodas();

        if (cita.getId() == 0) {
            cita.setId(obtenerProximoId(citas));
        }

        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getId() == cita.getId()) {
                citas.set(i, cita);
                guardarTodas(citas);
                return true;
            }
        }

        citas.add(cita);
        guardarTodas(citas);
        return true;
    }

    public boolean eliminarCita(int id) {
        List<CitaMedica> citas = cargarTodas();
        boolean removed = citas.removeIf(c -> c.getId() == id);
        if (removed) {
            guardarTodas(citas);
        }
        return removed;
    }

    public CitaMedica buscarPorId(int id) {
        List<CitaMedica> citas = cargarTodas();
        for (CitaMedica c : citas) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private int obtenerProximoId(List<CitaMedica> citas) {
        return citas.stream().mapToInt(CitaMedica::getId).max().orElse(0) + 1;
    }
}
