package DAO;

import Model.Actividad;
import Model.Oficial;
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

public class OficialDAO {

    private static final String JSON_FILE = "C:\\Users\\ASUS\\Desktop\\InmateMonitoring\\src\\Resources\\DATA\\oficiales.json\\";

        private static OficialDAO instancia;

    public static synchronized OficialDAO getInstancia() {
        if (instancia == null) {
            instancia = new OficialDAO();
        }
        return instancia;
    }
    
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new OficialDAO.LocalDateAdapter())
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
    
    public List<Oficial> cargarTodos() {
        File archivo = new File(JSON_FILE);

        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                guardarTodos(new ArrayList<>());
            }

            if (archivo.length() == 0) return new ArrayList<>();

            try (Reader reader = new FileReader(archivo)) {
                Type tipoLista = new TypeToken<List<Oficial>>() {}.getType();
                List<Oficial> lista = gson.fromJson(reader, tipoLista);
                return lista != null ? lista : new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("Error al cargar oficiales: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarOficial(Oficial oficial) {
        List<Oficial> lista = cargarTodos();
        lista.add(oficial);
        guardarTodos(lista);
    }

    public void guardarTodos(List<Oficial> lista) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar oficiales: " + e.getMessage());
        }
    }

    public Oficial buscarPorIdentificacion(String identificacion) {
        for (Oficial o : cargarTodos()) {
            if (o.getIdentificacion().equals(identificacion)) {
                return o;
            }
        }
        return null;
    }

    public boolean eliminarPorIdentificacion(String identificacion) {
        List<Oficial> lista = cargarTodos();
        boolean eliminado = lista.removeIf(o -> o.getIdentificacion().equals(identificacion));
        if (eliminado) {
            guardarTodos(lista);
        }
        return eliminado;
    }

    public boolean actualizarOficial(
            String identificacionOriginal,
            String nuevoPrimerNombre,
            String nuevoSegundoNombre,
            String nuevoPrimerApellido,
            String nuevoSegundoApellido,
            Integer nuevaEdad,
            String nuevoSexo,
            String nuevaNacionalidad,
            LocalDate nuevaFechaIngreso,
            String nuevoTurno,
            String nuevaPlaca,
            String nuevoCargo,
            String nuevaFotoPath
    ) {
        List<Oficial> lista = cargarTodos();
        boolean encontrado = false;

        for (Oficial o : lista) {
            if (o.getIdentificacion().equals(identificacionOriginal)) {
                if (nuevoPrimerNombre != null) o.setPrimerNombre(nuevoPrimerNombre);
                if (nuevoSegundoNombre != null) o.setSegundoNombre(nuevoSegundoNombre);
                if (nuevoPrimerApellido != null) o.setPrimerApellido(nuevoPrimerApellido);
                if (nuevoSegundoApellido != null) o.setSegundoApellido(nuevoSegundoApellido);
                if (nuevaEdad != null) o.setEdad(nuevaEdad);
                if (nuevoSexo != null) o.setSexo(nuevoSexo);
                if (nuevaNacionalidad != null) o.setNacionalidad(nuevaNacionalidad);
                if (nuevaFechaIngreso != null) o.setFechaInicioContrato(nuevaFechaIngreso);
                if (nuevoTurno != null) o.setTurno(nuevoTurno);
                if (nuevaPlaca != null) o.setPlaca(nuevaPlaca);
                if (nuevoCargo != null) o.setCargo(nuevoCargo);
                if (nuevaFotoPath != null) o.setFotoPath(nuevaFotoPath);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "Oficial no encontrado: " + identificacionOriginal, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        guardarTodos(lista);
        return true;
    }
   public boolean asignarOficialAActividad(String idActividad, String identificacionOficial) {
    ActividadDAO actividadDAO = ActividadDAO.getInstancia();
    Actividad actividad = actividadDAO.buscarActividadPorId(idActividad);

    if (actividad == null) {
        JOptionPane.showMessageDialog(null, "Actividad no encontrada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    Oficial oficial = this.buscarPorIdentificacion(identificacionOficial); 
    if (oficial == null) {
        JOptionPane.showMessageDialog(null, "Oficial no encontrado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    actividad.setResponsableOficial(oficial.getIdentificacion());  
    
    if (actividadDAO.guardarActividades(actividadDAO.cargarActividades())) {
        JOptionPane.showMessageDialog(null, "Oficial asignado a la actividad correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return true;
    } else {
        return false;
    }


}
    
    public List<Actividad> obtenerActividadesPorOficial(String identificacionOficial) {
    ActividadDAO actividadDAO = ActividadDAO.getInstancia();
    List<Actividad> todasActividades = actividadDAO.cargarActividades();
    
    List<Actividad> actividadesOficial = new ArrayList<>();
    for (Actividad actividad : todasActividades) {
        if (actividad.getResponsableOficial() != null && actividad.getResponsableOficial().equals(identificacionOficial)) {
            actividadesOficial.add(actividad);
        }
    }
    return actividadesOficial;
}


    
}
