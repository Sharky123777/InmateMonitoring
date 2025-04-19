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
    private static final String RUTA_JSON = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Resources\\DATA\\enfermera.json";
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
    
    // Método para verificar si una enfermera existe por cédula
    public boolean existeEnfermera(String cedula) {
        return obtenerEnfermeras().stream()
               .anyMatch(e -> e.getIdentificacion().equals(cedula));
    }
    
    public boolean guardarEnfermera(Enfermera enfermera, File imagen) throws IOException {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();

            // 1. Validar campos obligatorios (excepto segundoNombre que es opcional)
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

            // 2. Validar cédula única usando el nuevo método
            if (existeEnfermera(enfermera.getIdentificacion())) {
                JOptionPane.showMessageDialog(null,
                    "Ya existe una enfermera con la cédula " + enfermera.getIdentificacion(),
                    "Cédula duplicada", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 3. Validar límite de enfermeras
            if (enfermeras.size() >= 2) {
                JOptionPane.showMessageDialog(null,
                    "Solo se permiten 2 enfermeras como máximo (una diurna y una nocturna)",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 4. Validar turno único
            for (Enfermera e : enfermeras) {
                if (e.getTurno().equalsIgnoreCase(enfermera.getTurno())) {
                    JOptionPane.showMessageDialog(null,
                        "Ya existe una enfermera para el turno " + enfermera.getTurno(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // 5. Validar que el turno sea Diurno o Nocturno
            if (!enfermera.getTurno().equalsIgnoreCase("Diurno") && !enfermera.getTurno().equalsIgnoreCase("Nocturno")) {
                JOptionPane.showMessageDialog(null,
                    "El turno debe ser 'Diurno' o 'Nocturno'",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 6. Validar fecha fin de contrato
            if (!enfermera.getFechaFinContrato().isAfter(enfermera.getFechaContratacion())) {
                JOptionPane.showMessageDialog(null,
                    "La fecha de fin de contrato debe ser posterior a la fecha de inicio (" + 
                    enfermera.getFechaContratacionFormateada() + ")",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 7. Validar edad
            if (enfermera.getEdad() < 18 || enfermera.getEdad() > 90) {
                JOptionPane.showMessageDialog(null,
                    "La edad debe estar entre 18 y 90 años",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 8. Validar sexo (siempre debe ser femenino)
            if (!enfermera.getSexo().equalsIgnoreCase("Femenino")) {
                enfermera.setSexo("Femenino"); // Forzar a femenino
            }

            // 9. Manejar la imagen
            if (imagen != null && imagen.exists()) {
                // Crear directorio de imágenes si no existe
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

            // 10. Guardar en el JSON
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
    
    public boolean modificarEnfermera(String identificacionOriginal, Enfermera enfermeraActualizada, File nuevaImagen) {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();
            boolean encontrada = false;
            
            for (int i = 0; i < enfermeras.size(); i++) {
                Enfermera e = enfermeras.get(i);
                if (e.getIdentificacion().equals(identificacionOriginal)) {
                    // 1. Validar si se cambió la cédula y si la nueva ya existe
                    if (!identificacionOriginal.equals(enfermeraActualizada.getIdentificacion())) {
                        if (existeEnfermera(enfermeraActualizada.getIdentificacion())) {
                            JOptionPane.showMessageDialog(null, 
                                "Ya existe una enfermera con la cédula " + enfermeraActualizada.getIdentificacion(), 
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    // 2. Validar que no se cambie el turno a uno ya existente (excepto si es el mismo)
                    if (!e.getTurno().equals(enfermeraActualizada.getTurno())) {
                        for (Enfermera otra : enfermeras) {
                            if (!otra.getIdentificacion().equals(identificacionOriginal) && 
                                otra.getTurno().equals(enfermeraActualizada.getTurno())) {
                                JOptionPane.showMessageDialog(null, 
                                    "Ya existe una enfermera para el turno " + enfermeraActualizada.getTurno(), 
                                    "Error", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        }
                    }
                    
                    // 3. Validar fecha fin de contrato
                    if (!enfermeraActualizada.getFechaFinContrato().isAfter(enfermeraActualizada.getFechaContratacion())) {
                        JOptionPane.showMessageDialog(null,
                            "La fecha de fin de contrato debe ser posterior a la fecha de inicio (" + 
                            enfermeraActualizada.getFechaContratacionFormateada() + ")",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    // 4. Validar edad
                    if (enfermeraActualizada.getEdad() < 18 || enfermeraActualizada.getEdad() > 90) {
                        JOptionPane.showMessageDialog(null,
                            "La edad debe estar entre 18 y 90 años",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    
                    // 5. Actualizar datos
                    enfermeras.set(i, enfermeraActualizada);
                    
                    // 6. Manejar imagen
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        // Eliminar imagen anterior si existe
                        if (e.getRutaImagen() != null) {
                            try {
                                Files.deleteIfExists(Paths.get(e.getRutaImagen()));
                            } catch (IOException ex) {
                                System.err.println("Error al eliminar imagen anterior: " + ex.getMessage());
                            }
                        }
                        
                        // Copiar nueva imagen con nombre único
                        String extension = nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        String nombreImagen = enfermeraActualizada.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
                        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                        enfermeraActualizada.setRutaImagen(rutaImagenFinal);
                    } else {
                        // Mantener la imagen anterior si no se proporciona nueva
                        enfermeraActualizada.setRutaImagen(e.getRutaImagen());
                    }
                    
                    encontrada = true;
                    break;
                }
            }
            
            if (encontrada) {
                guardarListaEnfermeras(enfermeras);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No se encontró la enfermera con identificación " + identificacionOriginal, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al modificar enfermera: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean eliminarEnfermera(String identificacion) {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();
            Iterator<Enfermera> iterator = enfermeras.iterator();
            boolean eliminada = false;
            
            while (iterator.hasNext()) {
                Enfermera e = iterator.next();
                if (e.getIdentificacion().equals(identificacion)) {
                    // Eliminar imagen asociada
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
    
    // Añade estos nuevos métodos a tu clase EnfermeraDAO (al final, antes de las clases adaptadoras)

/**
 * Obtiene una lista de todas las enfermeras con sus datos formateados para mostrar en tabla
 * @return Lista de arrays de objetos con los datos de cada enfermera
 */
public List<Object[]> obtenerDatosEnfermerasParaTabla() {
    List<Object[]> datos = new ArrayList<>();
    List<Enfermera> enfermeras = obtenerEnfermeras();
    
    for (Enfermera enfermera : enfermeras) {
        datos.add(new Object[]{
            enfermera.getRutaImagen(), // Columna 0: Ruta de la imagen
            enfermera.getPrimerNombre() + (enfermera.getSegundoNombre() != null ? " " + enfermera.getSegundoNombre() : ""), // Columna 1: Nombre completo
            enfermera.getPrimerApellido() + " " + enfermera.getSegundoApellido(), // Columna 2: Apellidos
            enfermera.getEdad(), // Columna 3: Edad
            enfermera.getIdentificacion(), // Columna 4: Cédula
            enfermera.getSexo(), // Columna 5: Sexo
            enfermera.getNacionalidad(), // Columna 6: Nacionalidad
            enfermera.getCorreo(), // Columna 7: Correo
            enfermera.getTurno(), // Columna 8: Turno
            "Enfermera", // Columna 9: Cargo (fijo)
            enfermera.getFechaContratacionFormateada(), // Columna 10: Fecha contratación
            enfermera.getFechaFinContratoFormateada() // Columna 11: Fecha fin contrato
        });
    }
    
    return datos;
}

/**
 * Obtiene los nombres de las columnas para la tabla
 * @return Array con los nombres de las columnas
 */
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

/**
 * Obtiene los tipos de datos de cada columna para la tabla
 * @return Array con las clases de cada columna
 */
public Class<?>[] getTiposColumnas() {
    return new Class<?>[]{
        String.class, // Foto (ruta)
        String.class, // Nombre
        String.class, // Apellido
        Integer.class, // Edad
        String.class, // Cédula
        String.class, // Sexo
        String.class, // Nacionalidad
        String.class, // Correo
        String.class, // Turno
        String.class, // Cargo
        String.class, // Fecha Contratación
        String.class  // Fecha Fin Contrato
    };
}
    
    
    private static class EnfermeraTypeAdapter implements JsonSerializer<Enfermera> {
        @Override
        public JsonElement serialize(Enfermera enfermera, Type type, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            
            // 1. Campos de Persona (nombres y apellidos primero)
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
            
            // 2. Campos específicos de Enfermera
            jsonObject.addProperty("turno", enfermera.getTurno());
            jsonObject.add("fechaContratacion", context.serialize(enfermera.getFechaContratacion()));
            jsonObject.add("fechaFinContrato", context.serialize(enfermera.getFechaFinContrato()));
            jsonObject.addProperty("rutaImagen", enfermera.getRutaImagen());
            jsonObject.addProperty("correo", enfermera.getCorreo());
            
            return jsonObject;
        }
    }
}