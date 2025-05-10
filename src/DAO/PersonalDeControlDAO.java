package DAO;

import Model.Entities.PersonalDeControl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import javax.swing.JOptionPane;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalDeControlDAO {

    private static PersonalDeControlDAO instancia;
    private static final String JSON_FILE = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\personalDeControl.json";
    private static final String IMAGES_DIR = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\Images\\";
    private Gson gson;

    public PersonalDeControlDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public static synchronized PersonalDeControlDAO getInstancia() {
        if (instancia == null) {
            instancia = new PersonalDeControlDAO();
        }
        return instancia;
    }

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
            Type tipoLista = new TypeToken<ArrayList<PersonalDeControl>>() {
            }.getType();
            List<PersonalDeControl> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardarPersonalDeControl(PersonalDeControl personal, File imagenSeleccionada) {
        if (personal == null) {
            System.err.println("Error: El personal no puede ser nulo");
            return false;
        }

        if (personal.getFechaFinContrato() != null && personal.getFechaInicioContrato() != null
                && personal.getFechaFinContrato().isBefore(personal.getFechaInicioContrato())) {
            mostrarError("La fecha fin de contrato no puede ser anterior a la fecha inicio");
            return false;
        }

        List<PersonalDeControl> lista = cargarTodos();

        if (personal.getId() == 0) {
            int nuevoId = obtenerProximoId(lista);
            personal.setId(nuevoId);
        }

        if (imagenSeleccionada != null) {
            try {
                String nombreArchivo = "personal_" + personal.getId() + getExtension(imagenSeleccionada.getName());
                String rutaDestino = IMAGES_DIR + nombreArchivo;

                new File(IMAGES_DIR).mkdirs();

                Files.copy(imagenSeleccionada.toPath(),
                        new File(rutaDestino).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                personal.setFotoPath(rutaDestino);
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
                personal.setFotoPath(null);
            }
        }

        boolean existe = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == personal.getId()) {
                lista.set(i, personal);
                existe = true;
                break;
            }
        }

        if (!existe) {
            lista.add(personal);
        }

        try {
            guardarTodos(lista);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar la lista de personal de control: " + e.getMessage());
            return false;
        }
    }

    private int obtenerProximoId(List<PersonalDeControl> personalesDeControl) {
        if (personalesDeControl.isEmpty()) {
            return 1;
        }
        return personalesDeControl.stream()
                .mapToInt(PersonalDeControl::getId)
                .max()
                .orElse(0) + 1;
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
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

    public boolean actualizarPersonalDeControlPorDirector(
            String identificacionOriginal,
            String nuevoPrimerNombre,
            String nuevoSegundoNombre,
            String nuevoPrimerApellido,
            String nuevoSegundoApellido,
            Integer nuevaEdad,
            String nuevaIdentificacion,
            String nuevoSexo,
            String nuevaNacionalidad,
            String nuevoCorreo,
            String nuevoTurno,
            String nuevoCargo,
            String nuevaFotoPath,
            Date nuevoInicioContratoDate,
            Date nuevoFinContratoDate
    ) {
        if (identificacionOriginal == null || identificacionOriginal.trim().isEmpty()) {
            mostrarError("La identificación original no puede estar vacía");
            return false;
        }

        List<PersonalDeControl> lista = cargarTodos();
        boolean encontrado = false;

        LocalDate nuevoInicioContrato = null;
        LocalDate nuevoFinContrato = null;

        try {
            if (nuevoInicioContratoDate != null) {
                nuevoInicioContrato = nuevoInicioContratoDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }

            if (nuevoFinContratoDate != null) {
                nuevoFinContrato = nuevoFinContratoDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }
        } catch (Exception e) {
            mostrarError("Error al convertir fechas: " + e.getMessage());
            return false;
        }

        if (nuevoFinContrato != null && nuevoInicioContrato != null
                && nuevoFinContrato.isBefore(nuevoInicioContrato)) {
            mostrarError("La fecha fin de contrato no puede ser anterior a la fecha inicio");
            return false;
        }

        for (PersonalDeControl p : lista) {
            if (p.getIdentificacion().equals(identificacionOriginal)) {
                encontrado = true;

                if (nuevoPrimerNombre != null) {
                    p.setPrimerNombre(nuevoPrimerNombre);
                }
                if (nuevoSegundoNombre != null) {
                    p.setSegundoNombre(nuevoSegundoNombre);
                }
                if (nuevoPrimerApellido != null) {
                    p.setPrimerApellido(nuevoPrimerApellido);
                }
                if (nuevoSegundoApellido != null) {
                    p.setSegundoApellido(nuevoSegundoApellido);
                }
                if (nuevaEdad != null && nuevaEdad > 0) {
                    p.setEdad(nuevaEdad);
                }
                if (nuevaIdentificacion != null) {
                    p.setIdentificacion(nuevaIdentificacion);
                }
                if (nuevoSexo != null) {
                    p.setSexo(nuevoSexo);
                }
                if (nuevaNacionalidad != null) {
                    p.setNacionalidad(nuevaNacionalidad);
                }
                if (nuevoCorreo != null) {
                    p.setCorreo(nuevoCorreo);
                }
                if (nuevoTurno != null) {
                    p.setTurno(nuevoTurno);
                }
                if (nuevoCargo != null) {
                    p.setCargo(nuevoCargo);
                }
                if (nuevaFotoPath != null) {
                    p.setFotoPath(nuevaFotoPath);
                }
                if (nuevoInicioContrato != null) {
                    p.setFechaInicioContrato(nuevoInicioContrato);
                }
                if (nuevoFinContrato != null) {
                    p.setFechaFinContrato(nuevoFinContrato);
                }

                break;
            }
        }

        if (!encontrado) {
            mostrarError("Personal de control no encontrado: " + identificacionOriginal);
            return false;
        }

        return guardarCambios(lista);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean guardarCambios(List<PersonalDeControl> lista) {
        try {
            guardarTodos(lista);
            JOptionPane.showMessageDialog(null,
                    "Cambios guardados exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            mostrarError("Error al guardar cambios: " + e.getMessage());
            return false;
        }
    }
}
