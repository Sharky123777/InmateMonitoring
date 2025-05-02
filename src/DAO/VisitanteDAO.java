package DAO;

import Model.Entities.Visita;
import Model.Entities.Visitante;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class VisitanteDAO {

    private static final String JSON_FILE = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\visitantes.json";
    private static final String IMAGES_DIR = "C:\\Users\\nicol\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\Images\\";

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<Visitante> cargarTodos() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                try (FileWriter writer = new FileWriter(JSON_FILE)) {
                    gson.toJson(new ArrayList<Visitante>(), writer);
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
            Type tipoListaVisitante = new TypeToken<ArrayList<Visitante>>() {
            }.getType();
            List<Visitante> personalesDeControl = gson.fromJson(reader, tipoListaVisitante);

            return personalesDeControl != null ? personalesDeControl : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardarVisitante(Visitante visitante, File imagenSeleccionada) {
        if (visitante == null) {
            System.err.println("Error: El visitante no puede ser nulo");
            return false;
        }

        List<Visitante> visitantes = cargarTodos();

        if (visitante.getId() == 0) {
            int nuevoId = obtenerProximoId(visitantes);
            visitante.setId(nuevoId);
        }

        if (imagenSeleccionada != null) {
            try {
                String nombreArchivo = "visitante_" + visitante.getId() + getExtension(imagenSeleccionada.getName());
                String rutaDestino = IMAGES_DIR + nombreArchivo;

                new File(IMAGES_DIR).mkdirs();

                Files.copy(imagenSeleccionada.toPath(),
                        new File(rutaDestino).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                visitante.setFotoPath(rutaDestino);
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
                visitante.setFotoPath(null);
            }
        }

        boolean existe = false;
        for (int i = 0; i < visitantes.size(); i++) {
            if (visitantes.get(i).getId() == visitante.getId()) {
                visitantes.set(i, visitante);
                existe = true;
                break;
            }
        }

        if (!existe) {
            visitantes.add(visitante);
        }

        try {
            guardarTodos(visitantes);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar la lista de visitantes: " + e.getMessage());
            return false;
        }
    }

    private int obtenerProximoId(List<Visitante> visitantes) {
        if (visitantes.isEmpty()) {
            return 1;
        }
        return visitantes.stream()
                .mapToInt(Visitante::getId)
                .max()
                .orElse(0) + 1;
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }

    public Visitante buscarVisitantePorIdentificacion(String identificacion) {
        List<Visitante> visitantes = cargarTodos();
        for (Visitante visitante : visitantes) {
            if (visitante.getIdentificacion().equals(identificacion)) {
                return visitante;
            }
        }
        return null;
    }

    public boolean eliminarVisitante(String identificacion) {
        List<Visitante> visitantes = cargarTodos();
        boolean removed = visitantes.removeIf(v -> v.getIdentificacion().equals(identificacion));
        if (removed) {
            guardarTodos(visitantes);
        }
        return removed;
    }

    public void guardarTodos(List<Visitante> visitantes) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(visitantes, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
            throw new RuntimeException("Error al guardar los datos", e);
        }
    }

    public boolean modificarDatosVisitante(String identificacion,
            String nuevoPrimerNombre,
            String nuevoSegundoNombre,
            String nuevoPrimerApellido,
            String nuevoSegundoApellido,
            int nuevaEdad,
            String nuevoSexo,
            String nuevaRelacionConPreso,
            File nuevaImagen) {
        List<Visitante> visitantes = cargarTodos();
        boolean encontrado = false;

        for (Visitante visitante : visitantes) {
            if (visitante.getIdentificacion().equals(identificacion)) {
                encontrado = true;

                if (nuevoPrimerNombre != null && !nuevoPrimerNombre.isEmpty()) {
                    visitante.setPrimerNombre(nuevoPrimerNombre);
                }
                if (nuevoSegundoNombre != null) {
                    visitante.setSegundoNombre(nuevoSegundoNombre);
                }
                if (nuevoPrimerApellido != null && !nuevoPrimerApellido.isEmpty()) {
                    visitante.setPrimerApellido(nuevoPrimerApellido);
                }
                if (nuevoSegundoApellido != null) {
                    visitante.setSegundoApellido(nuevoSegundoApellido);
                }
                if (nuevaEdad > 0) {
                    visitante.setEdad(nuevaEdad);
                }
                if (nuevoSexo != null && !nuevoSexo.isEmpty()) {
                    visitante.setSexo(nuevoSexo);
                }
                if (nuevaRelacionConPreso != null && !nuevaRelacionConPreso.isEmpty()) {
                    visitante.setRelacionConPreso(nuevaRelacionConPreso);
                }

                if (nuevaImagen != null) {
                    try {
                        String extension = getExtension(nuevaImagen.getName());
                        String nombreArchivo = "visitante_" + visitante.getId() + extension;
                        String rutaDestino = IMAGES_DIR + nombreArchivo;

                        new File(IMAGES_DIR).mkdirs();
                        Files.copy(nuevaImagen.toPath(),
                                new File(rutaDestino).toPath(),
                                StandardCopyOption.REPLACE_EXISTING);

                        visitante.setFotoPath(rutaDestino);
                    } catch (IOException e) {
                        System.err.println("Error al actualizar imagen: " + e.getMessage());
                        return false;
                    }
                }

                break;
            }
        }

        if (!encontrado) {
            return false;
        }

        try {
            guardarTodos(visitantes);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar cambios en visitantes: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarVisitante(String identificacionOriginal, Visitante visitanteActualizado, File nuevaImagen) {
        List<Visitante> visitantes = cargarTodos();
        boolean encontrado = false;

        for (int i = 0; i < visitantes.size(); i++) {
            if (visitantes.get(i).getIdentificacion().equals(identificacionOriginal)) {
                encontrado = true;

                if (nuevaImagen != null) {
                    String nuevaRutaImagen = guardarImagenVisitante(nuevaImagen, visitanteActualizado.getIdentificacion());
                    visitanteActualizado.setFotoPath(nuevaRutaImagen);
                } else {
                    visitanteActualizado.setFotoPath(visitantes.get(i).getFotoPath());
                }

                visitantes.set(i, visitanteActualizado);
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null,
                    "Visitante no encontrado: " + identificacionOriginal,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return guardarCambios(visitantes);
    }

    public Visitante modificarDatosVisitanteYDevolver(String identificacion,
            String primerNombre, String segundoNombre, String primerApellido,
            String segundoApellido, int edad, String sexo, String relacion,
            File imagen) {

        List<Visitante> visitantes = cargarTodos();

        for (Visitante visitante : visitantes) {
            if (visitante.getIdentificacion().equals(identificacion)) {
                // Actualizar campos
                if (primerNombre != null) {
                    visitante.setPrimerNombre(primerNombre);
                }
                if (segundoNombre != null) {
                    visitante.setSegundoNombre(segundoNombre);
                }
                if (primerApellido != null) {
                    visitante.setPrimerApellido(primerApellido);
                }
                if (segundoApellido != null) {
                    visitante.setSegundoApellido(segundoApellido);
                }
                visitante.setEdad(edad);
                if (sexo != null) {
                    visitante.setSexo(sexo);
                }
                if (relacion != null) {
                    visitante.setRelacionConPreso(relacion);
                }

                if (imagen != null) {
                    String nuevaRuta = guardarImagenVisitante(imagen, identificacion);
                    visitante.setFotoPath(nuevaRuta);
                }

                if (guardarCambios(visitantes)) {
                    return visitante;
                }
                return null;
            }
        }
        return null;
    }

    private String guardarImagenVisitante(File imagen, String identificacion) {
        try {
            String extension = imagen.getName().substring(imagen.getName().lastIndexOf("."));
            String nombreArchivo = "visitante_" + identificacion + extension;
            String rutaDestino = IMAGES_DIR + nombreArchivo;

            new File(IMAGES_DIR).mkdirs();
            Files.copy(imagen.toPath(), new File(rutaDestino).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return rutaDestino;
        } catch (IOException e) {
            System.err.println("Error al guardar imagen del visitante: " + e.getMessage());
            return null;
        }
    }

    private boolean guardarCambios(List<?> lista) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(lista, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar cambios: " + e.getMessage());
            return false;
        }
    }
}
