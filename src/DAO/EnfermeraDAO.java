package DAO;

import Model.Enfermera;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.JOptionPane;

public class EnfermeraDAO {
    private static final String RUTA_JSON = "C:\\Users\\gameV\\OneDrive\\Escritorio\\InmateMonitoring\\src\\Resources\\DATA\\enfermera.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_enfermeras/";
    private final Gson gson;
    
    public EnfermeraDAO() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(Enfermera.class, new EnfermeraTypeAdapter())
            .create();
        
        crearDirectoriosSiNoExisten();
    }
    
    private void crearDirectoriosSiNoExisten() {
        try {
            Files.createDirectories(Paths.get(RUTA_IMAGENES));
            Files.createDirectories(Paths.get(RUTA_JSON).getParent());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al crear directorios: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean existeEnfermera(String cedula) {
        return obtenerEnfermeras().stream()
               .anyMatch(e -> e.getIdentificacion().equals(cedula));
    }
    
    public boolean guardarEnfermera(Enfermera enfermera, File imagen) throws IOException {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();

            if (enfermera.getPrimerNombre() == null || enfermera.getPrimerNombre().trim().isEmpty() ||
                enfermera.getPrimerApellido() == null || enfermera.getPrimerApellido().trim().isEmpty() ||
                enfermera.getSegundoApellido() == null || enfermera.getSegundoApellido().trim().isEmpty() ||
                enfermera.getIdentificacion() == null || enfermera.getIdentificacion().trim().isEmpty() ||
                enfermera.getNacionalidad() == null || enfermera.getNacionalidad().trim().isEmpty() ||
                enfermera.getCorreo() == null || enfermera.getCorreo().trim().isEmpty() ||
                enfermera.getTurno() == null || enfermera.getTurno().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(null,
                    "Todos los campos obligatorios deben ser llenados (excepto segundo nombre que es opcional)",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (existeEnfermera(enfermera.getIdentificacion())) {
                JOptionPane.showMessageDialog(null,
                    "Ya existe una enfermera con la cédula " + enfermera.getIdentificacion(),
                    "Cédula duplicada", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (enfermeras.size() >= 2) {
                JOptionPane.showMessageDialog(null,
                    "Solo se permiten 2 enfermeras como máximo (una diurna y una nocturna)",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            for (Enfermera e : enfermeras) {
                if (e.getTurno().equalsIgnoreCase(enfermera.getTurno())) {
                    JOptionPane.showMessageDialog(null,
                        "Ya existe una enfermera para el turno " + enfermera.getTurno(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            if (!enfermera.getTurno().equalsIgnoreCase("Diurno") && !enfermera.getTurno().equalsIgnoreCase("Nocturno")) {
                JOptionPane.showMessageDialog(null,
                    "El turno debe ser 'Diurno' o 'Nocturno'",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!enfermera.getFechaFinContrato().isAfter(enfermera.getFechaContratacion())) {
                JOptionPane.showMessageDialog(null,
                    "La fecha de fin de contrato debe ser posterior a la fecha de inicio (" + 
                    enfermera.getFechaContratacionFormateada() + ")",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (enfermera.getEdad() < 18 || enfermera.getEdad() > 90) {
                JOptionPane.showMessageDialog(null,
                    "La edad debe estar entre 18 y 90 años",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!enfermera.getSexo().equalsIgnoreCase("Femenino")) {
                enfermera.setSexo("Femenino");
            }

            if (imagen != null && imagen.exists()) {
                if (!Files.exists(Paths.get(RUTA_IMAGENES))) {
                    Files.createDirectories(Paths.get(RUTA_IMAGENES));
                }
                
                String extension = imagen.getName().substring(imagen.getName().lastIndexOf("."));
                String nombreImagen = enfermera.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
                String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                enfermera.setRutaImagen(rutaImagenFinal);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una imagen para la enfermera",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            enfermeras.add(enfermera);
            guardarListaEnfermeras(enfermeras);

            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error de E/S al guardar enfermera: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error inesperado al guardar enfermera: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public List<Enfermera> obtenerEnfermeras() {
        List<Enfermera> enfermeras = new ArrayList<>();
        File archivo = new File(RUTA_JSON);
        
        if (!archivo.exists() || archivo.length() == 0) {
            return enfermeras;
        }
        
        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<List<Enfermera>>() {}.getType();
            enfermeras = gson.fromJson(reader, tipoLista);
            if (enfermeras == null) {
                enfermeras = new ArrayList<>();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al leer enfermeras: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return enfermeras;
    }
    
    private void guardarListaEnfermeras(List<Enfermera> enfermeras) throws IOException {
        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(enfermeras, writer);
        }
    }
    
    
   public boolean modificarEnfermera(String cedulaOriginal, Enfermera enfermeraModificada, File nuevaImagen) {
    List<Enfermera> enfermeras = obtenerEnfermeras();
    boolean encontrada = false;
    
    for (Enfermera e : enfermeras) {
        if (!e.getIdentificacion().equals(cedulaOriginal) &&
            e.getTurno().equalsIgnoreCase(enfermeraModificada.getTurno())) {
            JOptionPane.showMessageDialog(null,
                "Ya hay una enfermera con el turno '" + enfermeraModificada.getTurno() + "'.",
                "Turno duplicado", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    for (int i = 0; i < enfermeras.size(); i++) {
        if (enfermeras.get(i).getIdentificacion().equals(cedulaOriginal)) {
            // Actualiza la imagen si hay una nueva
            if (nuevaImagen != null && nuevaImagen.exists()) {
                try {
                    String extension = nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                    String nombreImagen = enfermeraModificada.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
                    String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                    Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    enfermeraModificada.setRutaImagen(rutaImagenFinal);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                        "Error al guardar la nueva imagen: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // Si no hay nueva imagen, mantener la anterior
                enfermeraModificada.setRutaImagen(enfermeras.get(i).getRutaImagen());
            }

            enfermeras.set(i, enfermeraModificada);
            encontrada = true;
            break;
        }
    }

    if (encontrada) {
        try {
            guardarListaEnfermeras(enfermeras);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al guardar los cambios: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, 
            "No se encontró la enfermera con cédula " + cedulaOriginal,
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    if (nuevaImagen != null && nuevaImagen.exists()) {
    try {
        // Eliminar imagen anterior si existe
        Enfermera enfermeraOriginal = obtenerEnfermeraPorIdentificacion(cedulaOriginal);
        if (enfermeraOriginal.getRutaImagen() != null) {
            Files.deleteIfExists(Paths.get(enfermeraOriginal.getRutaImagen()));
        }
        
        // Copiar nueva imagen
        String extension = nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
        String nombreImagen = enfermeraModificada.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        enfermeraModificada.setRutaImagen(rutaImagenFinal);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null,
            "Error al guardar la nueva imagen: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
} else {
    // Mantener imagen existente
    Enfermera original = obtenerEnfermeraPorIdentificacion(cedulaOriginal);
    enfermeraModificada.setRutaImagen(original.getRutaImagen());
}

    return false;
}

    public boolean eliminarEnfermera(String identificacion) {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();
            Iterator<Enfermera> iterator = enfermeras.iterator();
            boolean eliminada = false;
            
            while (iterator.hasNext()) {
                Enfermera e = iterator.next();
                if (e.getIdentificacion().equals(identificacion)) {
                    if (e.getRutaImagen() != null) {
                        Files.deleteIfExists(Paths.get(e.getRutaImagen()));
                    }
                    
                    iterator.remove();
                    eliminada = true;
                    break;
                }
            }
            
            if (eliminada) {
                guardarListaEnfermeras(enfermeras);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No se encontró la enfermera con identificación " + identificacion, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al eliminar enfermera: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public Enfermera obtenerEnfermeraPorIdentificacion(String identificacion) {
        return obtenerEnfermeras().stream()
                .filter(e -> e.getIdentificacion().equals(identificacion))
                .findFirst()
                .orElse(null);
    }
    
    public Enfermera obtenerEnfermeraPorTurno(String turno) {
        return obtenerEnfermeras().stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .findFirst()
                .orElse(null);
    }
    
    public List<Object[]> obtenerDatosEnfermerasParaTabla() {
        List<Object[]> datos = new ArrayList<>();
        List<Enfermera> enfermeras = obtenerEnfermeras();
        
        for (Enfermera enfermera : enfermeras) {
            datos.add(new Object[]{
                enfermera.getRutaImagen(),
                enfermera.getPrimerNombre() + (enfermera.getSegundoNombre() != null ? " " + enfermera.getSegundoNombre() : ""),
                enfermera.getPrimerApellido() + " " + enfermera.getSegundoApellido(),
                enfermera.getEdad(),
                enfermera.getIdentificacion(),
                enfermera.getSexo(),
                enfermera.getNacionalidad(),
                enfermera.getCorreo(),
                enfermera.getTurno(),
                "Enfermera",
                enfermera.getFechaContratacionFormateada(),
                enfermera.getFechaFinContratoFormateada()
            });
        }
        
        return datos;
    }
    
    public String[] getNombresColumnas() {
        return new String[]{
            "Foto", 
            "Nombre", 
            "Apellido", 
            "Edad", 
            "Cédula", 
            "Sexo", 
            "Nacionalidad", 
            "Correo", 
            "Turno", 
            "Cargo", 
            "Fecha Contratación", 
            "Fecha Fin Contrato"
        };
    }
    
    public Class<?>[] getTiposColumnas() {
        return new Class<?>[]{
            String.class,
            String.class,
            String.class,
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class
        };
    }
    
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
    
    private static class EnfermeraTypeAdapter implements JsonSerializer<Enfermera> {
        @Override
        public JsonElement serialize(Enfermera enfermera, Type type, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            
            jsonObject.addProperty("primerNombre", enfermera.getPrimerNombre());
            if (enfermera.getSegundoNombre() != null && !enfermera.getSegundoNombre().isEmpty()) {
                jsonObject.addProperty("segundoNombre", enfermera.getSegundoNombre());
            } else {
                jsonObject.add("segundoNombre", JsonNull.INSTANCE);
            }
            jsonObject.addProperty("primerApellido", enfermera.getPrimerApellido());
            jsonObject.addProperty("segundoApellido", enfermera.getSegundoApellido());
            jsonObject.addProperty("edad", enfermera.getEdad());
            jsonObject.addProperty("sexo", enfermera.getSexo());
            jsonObject.addProperty("nacionalidad", enfermera.getNacionalidad());
            jsonObject.addProperty("identificacion", enfermera.getIdentificacion());
            
            jsonObject.addProperty("turno", enfermera.getTurno());
            jsonObject.add("fechaContratacion", context.serialize(enfermera.getFechaContratacion()));
            jsonObject.add("fechaFinContrato", context.serialize(enfermera.getFechaFinContrato()));
            jsonObject.addProperty("rutaImagen", enfermera.getRutaImagen());
            jsonObject.addProperty("correo", enfermera.getCorreo());
            
            return jsonObject;
        }
    }
}