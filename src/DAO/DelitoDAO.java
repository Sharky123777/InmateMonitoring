package DAO;

import Model.Entities.Delito;
import Model.Entities.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class DelitoDAO {

   private static DelitoDAO instancia;

    private static final String JSON_FILE = "C:\\Users\\ASUS\\Desktop\\InmateMonitoring\\src\\Resources\\DATA\\delitos.json";
    private Gson gson;
    
     public static synchronized DelitoDAO getInstancia() {
        if (instancia == null) {
            instancia = new DelitoDAO();
        }
        return instancia;
    }

    public DelitoDAO() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }
    
    

    public List<Delito> cargarTodos() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                try (FileWriter writer = new FileWriter(JSON_FILE)) {
                    gson.toJson(new ArrayList<Delito>(), writer);
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
            Type tipoListaDelitos = new TypeToken<ArrayList<Delito>>() {
            }.getType();
            List<Delito> delitos = gson.fromJson(reader, tipoListaDelitos);
            return delitos != null ? delitos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int guardarDelito(Delito delito) {
        List<Delito> delitos = cargarTodos();

        for (int i = 0; i < delitos.size(); i++) {
            if (delitos.get(i).getId() == delito.getId()) {
                delitos.set(i, delito);
                guardarTodos(delitos);
                return delito.getId();
            }
        }

        int nuevoId = obtenerProximoIdDelito();
        delito.setId(nuevoId);
        delitos.add(delito);
        guardarTodos(delitos);
        return nuevoId;
    }

    public void guardarTodos(List<Delito> delitos) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(delitos, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
        }
    }

    public void actualizarDelito(Delito delitoActualizado) {
        List<Delito> delitos = cargarTodos();
        for (int i = 0; i < delitos.size(); i++) {
            if (delitos.get(i).getId() == delitoActualizado.getId()) {
                delitos.set(i, delitoActualizado);
                guardarTodos(delitos);
                return;
            }
        }
        guardarDelito(delitoActualizado);
    }

    private int obtenerProximoIdDelito() {
        List<Delito> delitos = cargarTodos();
        return delitos.stream()
                .mapToInt(Delito::getId)
                .filter(id -> id > 0)
                .max()
                .orElse(0) + 1;
    }

    public Delito buscarDelitoPorId(int id) {
        if (id <= 0) {
            return null;
        }

        List<Delito> delitos = cargarTodos();
        return delitos.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void eliminarDelito(int id) {
        List<Delito> delitos = cargarTodos();
        delitos.removeIf(d -> d.getId() == id);
        guardarTodos(delitos);
    }

 

public List<Delito> obtenerDelitosPorPreso(String identificacionPreso) {
    return cargarTodos().stream()
            .filter(d -> identificacionPreso.equals(d.getPresoId()))  
            .collect(Collectors.toList());
}
    public String obtenerDescripcionDelito(int idDelito) {
        List<Delito> delitos = cargarTodos(); 
        return delitos.stream()
                .filter(d -> d.getId() == idDelito)
                .findFirst()
                .map(Delito::getDescripcion)
                .orElse(null);
    }
}
