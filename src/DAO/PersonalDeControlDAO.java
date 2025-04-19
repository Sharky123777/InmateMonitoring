package DAO;

import Model.PersonalDeControl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.JOptionPane;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonalDeControlDAO {

    private static final String JSON_FILE = "C:\\Users\\ASUS\\Desktop\\InmateMonitoring\\src\\Resources\\DATA\\personalDeControl.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<PersonalDeControl> cargarTodos() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                try (FileWriter writer = new FileWriter(JSON_FILE)) {
                    gson.toJson(new ArrayList<PersonalDeControl>(), writer);
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
            Type tipoLista = new TypeToken<ArrayList<PersonalDeControl>>() {}.getType();
            List<PersonalDeControl> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarPersonalDeControl(PersonalDeControl personal) {
        List<PersonalDeControl> lista = cargarTodos();
        lista.add(personal);
        guardarTodos(lista);
    }

    public void guardarTodos(List<PersonalDeControl> lista) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
        }
    }

    public PersonalDeControl buscarPorIdentificacion(String identificacion) {
        List<PersonalDeControl> lista = cargarTodos();
        for (PersonalDeControl p : lista) {
            if (p.getIdentificacion().equals(identificacion)) {
                return p;
            }
        }
        return null;
    }

    public boolean eliminarPorIdentificacion(String identificacion) {
        List<PersonalDeControl> lista = cargarTodos();
        boolean eliminado = lista.removeIf(p -> p.getIdentificacion().equals(identificacion));

        if (eliminado) {
            try (Writer writer = new FileWriter(JSON_FILE)) {
                gson.toJson(lista, writer);
                return true;
            } catch (IOException e) {
                System.err.println("Error al guardar después de eliminar: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

    public boolean actualizarPersonalDeControl(
            String identificacionOriginal,
            String nuevoPrimerNombre,
            String nuevoSegundoNombre,
            String nuevoPrimerApellido,
            String nuevoSegundoApellido,
            Integer nuevaEdad,
            String nuevoSexo,
            String nuevaNacionalidad,
            LocalDate nuevaFechaIngreso,
            String nuevoTurno
    ) {
        List<PersonalDeControl> lista = cargarTodos();
        boolean encontrado = false;

        for (PersonalDeControl p : lista) {
            if (p.getIdentificacion().equals(identificacionOriginal)) {
                encontrado = true;

                if (nuevoPrimerNombre != null) p.setPrimerNombre(nuevoPrimerNombre);
                if (nuevoSegundoNombre != null) p.setSegundoNombre(nuevoSegundoNombre);
                if (nuevoPrimerApellido != null) p.setPrimerApellido(nuevoPrimerApellido);
                if (nuevoSegundoApellido != null) p.setSegundoApellido(nuevoSegundoApellido);
                if (nuevaEdad != null) p.setEdad(nuevaEdad);
                if (nuevoSexo != null) p.setSexo(nuevoSexo);
                if (nuevaNacionalidad != null) p.setNacionalidad(nuevaNacionalidad);
                if (nuevaFechaIngreso != null) p.setFechaIngreso(nuevaFechaIngreso);
                if (nuevoTurno != null) p.setTurno(nuevoTurno);

                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null,
                    "Personal de control no encontrado: " + identificacionOriginal,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return guardarCambios(lista);
    }

    private boolean guardarCambios(List<PersonalDeControl> lista) {
        try {
            guardarTodos(lista);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar cambios: " + e.getMessage());
            return false;
        }
    }
}
