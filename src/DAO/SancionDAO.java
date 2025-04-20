package DAO;

import Model.LocalDateAdapter;
import Model.LocalTimeAdapter;
import Model.Sancion;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SancionDAO {

    private static final String JSON_FILE = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\sanciones.json";
    private Gson gson;

    public SancionDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
    }

    public List<Sancion> cargarTodas() {
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
            Type tipoListaSancion = new TypeToken<ArrayList<Sancion>>() {
            }.getType();
            List<Sancion> sanciones = gson.fromJson(reader, tipoListaSancion);
            return sanciones != null ? sanciones : new ArrayList<>();
        } catch (JsonSyntaxException | IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            guardarTodas(new ArrayList<>());
            return new ArrayList<>();
        }
    }

    public void guardarSancion(Sancion sancion) {
        List<Sancion> sanciones = cargarTodas();

        if (sancion.getId() == 0) {
            int nuevoId = obtenerProximoId(sanciones);
            sancion.setId(nuevoId);
        }

        sanciones.add(sancion);
        guardarTodas(sanciones);
    }

    private int obtenerProximoId(List<Sancion> sanciones) {
        if (sanciones.isEmpty()) {
            return 1;
        }
        return sanciones.stream()
                .mapToInt(Sancion::getId)
                .max()
                .orElse(0) + 1;
    }

    public List<Sancion> cargarPorIdentificacionPreso(String identificacionPreso) {
        List<Sancion> todasSanciones = cargarTodas();
        List<Sancion> sancionesFiltradas = new ArrayList<>();

        for (Sancion sancion : todasSanciones) {
            if (sancion.getPreso() != null
                    && sancion.getPreso().getIdentificacion().equals(identificacionPreso)) {
                sancionesFiltradas.add(sancion);
            }
        }
        return sancionesFiltradas;
    }

    public void guardarTodas(List<Sancion> sanciones) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(sanciones, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
        }
    }

    public List<Sancion> cargarPorTipoYIdentificacionPreso(String tipoSancion, String identificacionPreso) {
        List<Sancion> todasSanciones = cargarTodas();
        List<Sancion> sancionesFiltradas = new ArrayList<>();

        for (Sancion sancion : todasSanciones) {
            boolean coincideTipo = tipoSancion == null || tipoSancion.equals(sancion.getTipoSancion());
            boolean coincideIdentificacion = sancion.getPreso() != null && sancion.getPreso().getIdentificacion().equals(identificacionPreso);

            if (coincideTipo && coincideIdentificacion) {
                sancionesFiltradas.add(sancion);
            }
        }

        return sancionesFiltradas;
    }

}
