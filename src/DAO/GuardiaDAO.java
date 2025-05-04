package DAO;

import Model.Entities.Guardia;
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

public class GuardiaDAO {
    private static final String RUTA_JSON = "C:\\Users\\gameV\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\guardias.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_guardias/";
    private final Gson gson;
    
    
    private static GuardiaDAO instancia;
    
    
    public GuardiaDAO() {
        
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
    
    // 3. Método público estático para obtener la instancia (thread-safe)
    public static synchronized GuardiaDAO getInstancia() {
        if (instancia == null) {
            instancia = new GuardiaDAO();
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

    public boolean guardarGuardia(String primerNombre, String segundoNombre, String primerApellido, 
                                String segundoApellido, int edad, String cedula, 
                                String nacionalidad, String correo, String turno, 
                                LocalDate fechaFinContrato, String cargo, File imagen) {
        try {
            // Validaciones básicas
            if (primerNombre == null || primerNombre.trim().isEmpty() || 
                primerApellido == null || primerApellido.trim().isEmpty() || 
                segundoApellido == null || segundoApellido.trim().isEmpty() || 
                cedula == null || cedula.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validar cédula única
            if (existeGuardiaConCedula(cedula)) {
                JOptionPane.showMessageDialog(null, "Ya existe un guardia con esta cédula: " + cedula, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Crear el objeto Guardia
            Guardia nuevoGuardia = new Guardia(
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
                
                // Copiar la imagen al directorio
                Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                nuevoGuardia.setRutaImagen(rutaImagenFinal);
            }

            // Guardar en el JSON
            List<Guardia> guardias = obtenerGuardias();
            guardias.add(nuevoGuardia);
            guardarListaGuardias(guardias);

            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el guardia: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<Guardia> obtenerGuardias() {
        List<Guardia> guardias = new ArrayList<>();
        File archivo = new File(RUTA_JSON);

        if (!archivo.exists() || archivo.length() == 0) {
            // Crear archivo vacío si no existe
            try {
                archivo.createNewFile();
                guardarListaGuardias(new ArrayList<>());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear archivo: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            return guardias;
        }

        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<Map<String, List<Guardia>>>() {}.getType();
            Map<String, List<Guardia>> datos = gson.fromJson(reader, tipoLista);
            
            if (datos != null && datos.containsKey("guardias")) {
                // Validar cada guardia
                for (Guardia g : datos.get("guardias")) {
                    if (validarGuardia(g)) {
                        guardias.add(g);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al leer archivo. Verifique el formato de los datos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return guardias;
    }

    private boolean validarGuardia(Guardia g) {
        return g != null && 
               g.getEdad() > 0 && 
               g.getIdentificacion() != null && 
               !g.getIdentificacion().isEmpty();
    }

    private void guardarListaGuardias(List<Guardia> guardias) throws IOException {
        // Crear un TypeAdapter personalizado para controlar el orden de serialización
        Gson gsonOrdenado = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Guardia.class, new GuardiaTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
        
        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gsonOrdenado.toJson(Collections.singletonMap("guardias", guardias), writer);
        }
    }

    // Clase interna para controlar el orden de serialización
    private static class GuardiaTypeAdapter extends TypeAdapter<Guardia> {
        @Override
        public void write(JsonWriter out, Guardia guardia) throws IOException {
            out.beginObject();
            
            // Escribir primero los campos de nombres y apellidos
            out.name("primerNombre").value(guardia.getPrimerNombre());
            out.name("segundoNombre").value(guardia.getSegundoNombre());
            out.name("primerApellido").value(guardia.getPrimerApellido());
            out.name("segundoApellido").value(guardia.getSegundoApellido());
            
            // Luego los demás campos en orden lógico
            out.name("edad").value(guardia.getEdad());
            out.name("identificacion").value(guardia.getIdentificacion());
            out.name("sexo").value(guardia.getSexo());
            out.name("nacionalidad").value(guardia.getNacionalidad());
            out.name("correo").value(guardia.getCorreo());
            out.name("turno").value(guardia.getTurno());
            out.name("fechaInicioContrato").value(guardia.getFechaInicioContratoFormateada());
            out.name("fechaFinContrato").value(guardia.getFechaFinContratoFormateada());
            out.name("rutaImagen").value(guardia.getRutaImagen());
            out.name("cargo").value(guardia.getCargo());
            
            out.endObject();
        }

        @Override
        public Guardia read(JsonReader in) throws IOException {
            // Implementación de deserialización (puedes mantener la existente)
            return null; // Esto debería implementarse según tus necesidades
        }
    }

    public boolean existeGuardiaConCedula(String cedula) {
        return obtenerGuardias().stream()
                .anyMatch(g -> g.getIdentificacion().equals(cedula));
    }

    public boolean eliminarGuardia(String cedula) {
        try {
            List<Guardia> guardias = obtenerGuardias();
            Iterator<Guardia> iterator = guardias.iterator();
            boolean encontrado = false;

            while (iterator.hasNext()) {
                Guardia guardia = iterator.next();
                if (guardia.getIdentificacion().equals(cedula)) {
                    // Eliminar la imagen asociada si existe
                    if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
                        try {
                            Files.deleteIfExists(Paths.get(guardia.getRutaImagen()));
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
                guardarListaGuardias(guardias);
                return true;
            }
            return false;
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el guardia: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean modificarGuardia(String cedula, String primerNombre, String segundoNombre, 
                              String primerApellido, String segundoApellido, int edad,
                              String nacionalidad, String correo, String turno,
                              LocalDate fechaFinContrato, String cargo, File nuevaImagen) {
        try {
            List<Guardia> guardias = obtenerGuardias();
            
            // Buscar el guardia por cédula
            for (Guardia guardia : guardias) {
                if (guardia.getIdentificacion().equals(cedula)) {
                    // Actualizar campos modificables
                    guardia.setPrimerNombre(primerNombre);
                    guardia.setSegundoNombre(segundoNombre);
                    guardia.setPrimerApellido(primerApellido);
                    guardia.setSegundoApellido(segundoApellido);
                    guardia.setEdad(edad);
                    guardia.setNacionalidad(nacionalidad);
                    guardia.setCorreo(correo);
                    guardia.setTurno(turno);
                    guardia.setFechaFinContrato(fechaFinContrato);
                    guardia.setCargo(cargo);
                    
                    // Manejar la nueva imagen si se proporciona
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        // Eliminar la imagen anterior si existe
                        if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
                            try {
                                Files.deleteIfExists(Paths.get(guardia.getRutaImagen()));
                            } catch (IOException e) {
                                System.err.println("Error al eliminar la imagen anterior: " + e.getMessage());
                            }
                        }
                        
                        // Copiar la nueva imagen
                        String nombreImagen = cedula + "_" + nuevaImagen.getName();
                        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                        guardia.setRutaImagen(rutaImagenFinal);
                    }
                    
                    // Guardar los cambios
                    guardarListaGuardias(guardias);
                    return true;
                }
            }
            
            JOptionPane.showMessageDialog(null, "No se encontró un guardia con la cédula: " + cedula, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el guardia: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public Guardia obtenerGuardiaPorCedula(String cedula) {
        List<Guardia> guardias = obtenerGuardias();
        return guardias.stream()
                .filter(g -> g.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }
    
    public boolean existeGuardia(String cedula) {
        // Validación básica del parámetro
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        
        // Obtener todos los guardias
        List<Guardia> guardias = obtenerGuardias();
        
        // Buscar si existe algún guardia con esa cédula
        for (Guardia guardia : guardias) {
            if (guardia.getIdentificacion() != null && 
                guardia.getIdentificacion().equalsIgnoreCase(cedula.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
   
}
