package DAO;

import Model.CoordinadorDeActividades;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.JOptionPane;

public class CoordinadorDeActividadesDAO {
    private static final String RUTA_JSON = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Resources\\DATA\\CoordinadoresDeActividades.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_CDA/";
    private final Gson gson;
    
    private static CoordinadorDeActividadesDAO instancia;
    
    public CoordinadorDeActividadesDAO() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
        
        // Crear directorios si no existen
        File carpetaImagenes = new File(RUTA_IMAGENES);
        if (!carpetaImagenes.exists()) {
            carpetaImagenes.mkdirs();
        }
        
        File carpetaJson = new File(RUTA_JSON).getParentFile();
        if (!carpetaJson.exists()) {
            carpetaJson.mkdirs();
        }
    }
    
    public static synchronized CoordinadorDeActividadesDAO getInstancia() {
        if (instancia == null) {
            instancia = new CoordinadorDeActividadesDAO();
        }
        return instancia;
    }

    // Adaptador para LocalDate
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(formatter));
        }
        
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }

    public boolean guardarCDA(String primerNombre, String segundoNombre, String primerApellido, 
                            String segundoApellido, int edad, String cedula, 
                            String nacionalidad, String correo, String turno, 
                            LocalDate fechaFinContrato, String cargo, File imagen) {
        try {
            // Validaciones básicas
            if (primerNombre == null || primerNombre.trim().isEmpty() || 
                primerApellido == null || primerApellido.trim().isEmpty() || 
                cedula == null || cedula.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nombre, apellido y cédula son obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validar cédula única
            if (existeCoordinadorConCedula(cedula)) {
                JOptionPane.showMessageDialog(null, "Ya existe un coordinador con esta cédula: " + cedula, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Crear el objeto Coordinador
            CoordinadorDeActividades nuevoCoordinador = new CoordinadorDeActividades(
                primerNombre, 
                segundoNombre, 
                primerApellido, 
                segundoApellido,
                edad, 
                cedula, 
                nacionalidad, 
                correo, 
                turno, 
                fechaFinContrato, 
                cargo
            );

            // Manejar la imagen
            if (imagen != null && imagen.exists()) {
                String nombreImagen = cedula + "_" + imagen.getName();
                String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                
                Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                nuevoCoordinador.setRutaImagen(rutaImagenFinal);
            }

            // Guardar en JSON
            List<CoordinadorDeActividades> coordinadores = obtenerCDAS();
            coordinadores.add(nuevoCoordinador);
            guardarListaCoordinadores(coordinadores);

            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el coordinador: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<CoordinadorDeActividades> obtenerCDAS() {
        List<CoordinadorDeActividades> coordinadores = new ArrayList<>();
        File archivo = new File(RUTA_JSON);

        if (!archivo.exists() || archivo.length() == 0) {
            try {
                archivo.createNewFile();
                guardarListaCoordinadores(new ArrayList<>());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear archivo: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            return coordinadores;
        }

        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<Map<String, List<CoordinadorDeActividades>>>() {}.getType();
            Map<String, List<CoordinadorDeActividades>> datos = gson.fromJson(reader, tipoLista);
            
            if (datos != null && datos.containsKey("coordinadores")) {
                for (CoordinadorDeActividades c : datos.get("coordinadores")) {
                    if (validarCoordinador(c)) {
                        coordinadores.add(c);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al leer archivo. Verifique el formato de los datos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return coordinadores;
    }

    private boolean validarCoordinador(CoordinadorDeActividades c) {
        return c != null && 
               c.getEdad() > 0 && 
               c.getIdentificacion() != null && 
               !c.getIdentificacion().isEmpty();
    }

    private void guardarListaCoordinadores(List<CoordinadorDeActividades> coordinadores) throws IOException {
        Gson gsonOrdenado = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(CoordinadorDeActividades.class, new CoordinadorTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
        
        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gsonOrdenado.toJson(Collections.singletonMap("coordinadores", coordinadores), writer);
        }
    }

    // Adaptador para orden de serialización
    private static class CoordinadorTypeAdapter extends TypeAdapter<CoordinadorDeActividades> {
        @Override
        public void write(JsonWriter out, CoordinadorDeActividades c) throws IOException {
            out.beginObject();
            out.name("primerNombre").value(c.getPrimerNombre());
            out.name("segundoNombre").value(c.getSegundoNombre());
            out.name("primerApellido").value(c.getPrimerApellido());
            out.name("segundoApellido").value(c.getSegundoApellido());
            out.name("edad").value(c.getEdad());
            out.name("identificacion").value(c.getIdentificacion());
            out.name("nacionalidad").value(c.getNacionalidad());
            out.name("correo").value(c.getCorreo());
            out.name("turno").value(c.getTurno());
            out.name("fechaFinContrato").value(c.getFechaFinContrato().toString());
            out.name("rutaImagen").value(c.getRutaImagen());
            out.name("cargo").value(c.getCargo());
            out.endObject();
        }

        @Override
        public CoordinadorDeActividades read(JsonReader in) throws IOException {
            return null; // Implementar si es necesario
        }
    }

    public boolean existeCoordinadorConCedula(String cedula) {
        return obtenerCDAS().stream()
                .anyMatch(c -> c.getIdentificacion().equals(cedula));
    }

    public boolean eliminarCoordinador(String cedula) {
        try {
            List<CoordinadorDeActividades> coordinadores = obtenerCDAS();
            Iterator<CoordinadorDeActividades> iterator = coordinadores.iterator();
            boolean encontrado = false;

            while (iterator.hasNext()) {
                CoordinadorDeActividades c = iterator.next();
                if (c.getIdentificacion().equals(cedula)) {
                    // Eliminar imagen asociada
                    if (c.getRutaImagen() != null && !c.getRutaImagen().isEmpty()) {
                        try {
                            Files.deleteIfExists(Paths.get(c.getRutaImagen()));
                        } catch (IOException e) {
                            System.err.println("Error al eliminar la imagen: " + e.getMessage());
                        }
                    }
                    iterator.remove();
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarListaCoordinadores(coordinadores);
                return true;
            }
            return false;
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el coordinador: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean modificarCoordinador(String cedula, String primerNombre, String segundoNombre, 
                                  String primerApellido, String segundoApellido, int edad,
                                  String nacionalidad, String correo, String turno,
                                  LocalDate fechaFinContrato, String cargo, File nuevaImagen) {
        try {
            List<CoordinadorDeActividades> coordinadores = obtenerCDAS();
            
            for (CoordinadorDeActividades c : coordinadores) {
                if (c.getIdentificacion().equals(cedula)) {
                    // Actualizar campos
                    c.setPrimerNombre(primerNombre);
                    c.setSegundoNombre(segundoNombre);
                    c.setPrimerApellido(primerApellido);
                    c.setSegundoApellido(segundoApellido);
                    c.setEdad(edad);
                    c.setNacionalidad(nacionalidad);
                    c.setCorreo(correo);
                    c.setTurno(turno);
                    c.setFechaFinContrato(fechaFinContrato);
                    c.setCargo(cargo);
                    
                    // Manejar imagen
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        if (c.getRutaImagen() != null && !c.getRutaImagen().isEmpty()) {
                            try {
                                Files.deleteIfExists(Paths.get(c.getRutaImagen()));
                            } catch (IOException e) {
                                System.err.println("Error al eliminar imagen anterior: " + e.getMessage());
                            }
                        }
                        
                        String nombreImagen = cedula + "_" + nuevaImagen.getName();
                        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                        c.setRutaImagen(rutaImagenFinal);
                    }
                    
                    guardarListaCoordinadores(coordinadores);
                    return true;
                }
            }
            
            JOptionPane.showMessageDialog(null, "No se encontró un coordinador con la cédula: " + cedula, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el coordinador: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public CoordinadorDeActividades obtenerCoordinadorPorCedula(String cedula) {
        return obtenerCDAS().stream()
                .filter(c -> c.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }
    
    public boolean existeCoordinador(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        
        for (CoordinadorDeActividades c : obtenerCDAS()) {
            if (c.getIdentificacion() != null && 
                c.getIdentificacion().equalsIgnoreCase(cedula.trim())) {
                return true;
            }
        }
        
        return false;
    }
}