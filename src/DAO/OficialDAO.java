package DAO;

import Model.Oficial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class OficialDAO {

    private static final String JSON_FILE = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\oficiales.json";
    private static final String IMAGES_DIR = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\Images\\Oficiales\\";

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<Oficial> cargarTodos() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                try (FileWriter writer = new FileWriter(JSON_FILE)) {
                    gson.toJson(new ArrayList<Oficial>(), writer);
                }
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
            Type tipoListaOficial = new TypeToken<ArrayList<Oficial>>() {
            }.getType();
            List<Oficial> oficiales = gson.fromJson(reader, tipoListaOficial);

            return oficiales != null ? oficiales : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardarOficial(Oficial oficial, File imagenSeleccionada) {
        if (oficial == null) {
            System.err.println("Error: El oficial no puede ser nulo");
            return false;
        }

        List<Oficial> oficiales = cargarTodos();

        if (oficial.getId() == 0) {
            int nuevoId = obtenerProximoId(oficiales);
            oficial.setId(nuevoId);
        }

        if (imagenSeleccionada != null) {
            try {
                String nombreArchivo = "oficial_" + oficial.getId() + getExtension(imagenSeleccionada.getName());
                String rutaDestino = IMAGES_DIR + nombreArchivo;

                new File(IMAGES_DIR).mkdirs();

                Files.copy(imagenSeleccionada.toPath(),
                        new File(rutaDestino).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                oficial.setFotoPath(rutaDestino);
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
                oficial.setFotoPath(null);
            }
        }

        boolean existe = false;
        for (int i = 0; i < oficiales.size(); i++) {
            if (oficiales.get(i).getId() == oficial.getId()) {
                oficiales.set(i, oficial);
                existe = true;
                break;
            }
        }

        if (!existe) {
            oficiales.add(oficial);
        }

        try {
            guardarTodos(oficiales);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar la lista de oficiales: " + e.getMessage());
            return false;
        }
    }

    private int obtenerProximoId(List<Oficial> oficiales) {
        if (oficiales.isEmpty()) {
            return 1;
        }
        return oficiales.stream()
                .mapToInt(Oficial::getId)
                .max()
                .orElse(0) + 1;
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }

    public Oficial buscarOficialPorIdentificacion(String identificacion) {
        List<Oficial> oficiales = cargarTodos();
        for (Oficial oficial : oficiales) {
            if (oficial.getIdentificacion().equals(identificacion)) {
                return oficial;
            }
        }
        return null;
    }

    public boolean eliminarOficial(String identificacion) {
        List<Oficial> oficiales = cargarTodos();
        boolean removed = oficiales.removeIf(o -> o.getIdentificacion().equals(identificacion));
        if (removed) {
            guardarTodos(oficiales);
        }
        return removed;
    }

    public void guardarTodos(List<Oficial> oficiales) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(oficiales, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
            throw new RuntimeException("Error al guardar los datos", e);
        }
    }
}
