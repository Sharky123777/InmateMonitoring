package DAO;

import Model.Entities.IntentoFuga;
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

import javax.swing.JOptionPane;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IntentoFugaDAO {

    private static IntentoFugaDAO instancia;

    public static synchronized IntentoFugaDAO getInstancia() {
        if (instancia == null) {
            instancia = new IntentoFugaDAO();
        }
        return instancia;
    }

    private static final String JSON_FILE = "C:\\Users\\ASUS\\Desktop\\InmateMonitoring\\src\\Resources\\DATA\\intentosFuga.json";

    private final Gson gson = new GsonBuilder()
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

    public List<IntentoFuga> cargarIntentos() {
        File archivo = new File(JSON_FILE);
        archivo.getParentFile().mkdirs();

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarIntentos(new ArrayList<>());
                return new ArrayList<>();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear el archivo de intentos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return new ArrayList<>();
            }
        }

        if (archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(JSON_FILE)) {
            Type tipoLista = new TypeToken<ArrayList<IntentoFuga>>() {
            }.getType();
            List<IntentoFuga> intentos = gson.fromJson(reader, tipoLista);
            return (intentos != null) ? intentos : new ArrayList<>();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de intentos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void guardarIntentos(List<IntentoFuga> intentos) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(intentos, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los intentos de fuga: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void registrarFuga(String identificacion, LocalDate fechaFuga) {
        List<IntentoFuga> intentos = cargarIntentos();
        intentos.add(new IntentoFuga(identificacion, fechaFuga, null));
        guardarIntentos(intentos);
    }


    public List<IntentoFuga> obtenerPorPreso(String identificacion) {
        List<IntentoFuga> todos = cargarIntentos();
        return todos.stream()
                .filter(i -> i.getIdentificacionPreso().equals(identificacion))
                .collect(Collectors.toList());
    }
    
    public boolean tieneFugaActiva(String identificacionPreso) {
        List<IntentoFuga> intentos = obtenerPorPreso(identificacionPreso);
        return intentos.stream().anyMatch(i -> i.getFechaReingreso() == null);
    }
    
    public void registrarReingreso(String identificacionPreso, LocalDate fechaReingreso) {
    List<IntentoFuga> intentos = cargarIntentos();
    
    // Buscar la fuga más reciente sin reingreso
    for (IntentoFuga intento : intentos) {
        if (intento.getIdentificacionPreso().equals(identificacionPreso) && 
            intento.getFechaReingreso() == null) {
            intento.setFechaReingreso(fechaReingreso);
            break;
        }
    }
    
    guardarIntentos(intentos);
}
    
    
}
