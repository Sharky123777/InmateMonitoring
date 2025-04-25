package DAO;

import Model.Oficial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.JOptionPane;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OficialDAO {

    private static final String JSON_FILE = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\oficiales.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
}
