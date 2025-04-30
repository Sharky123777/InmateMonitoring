package DAO;

import Model.Enfermera;
import Model.Guardia;
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
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class EnfermeraDAO {
    private static final String RUTA_JSON = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Resources\\DATA\\enfermera.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_enfermeras/";
    private final Gson gson;
    private static EnfermeraDAO instancia;
    
    public EnfermeraDAO() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
        
        crearDirectoriosSiNoExisten();
    }
    
    public static synchronized EnfermeraDAO getInstancia() {
        if (instancia == null) {
            instancia = new EnfermeraDAO();
        }
        return instancia;
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
    
    public boolean guardarEnfermera(Enfermera enfermera, File imagen) {
    try {
        // Validar que la identificación no sea nula
        if (enfermera.getIdentificacion() == null || enfermera.getIdentificacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        
         if (!puedeAgregarEnfermera(enfermera.getTurno())) {
            throw new IllegalArgumentException("No se puede agregar más enfermeras. Límite alcanzado.");
        }
        
        if (enfermera == null) {
            JOptionPane.showMessageDialog(null, "La enfermera no puede ser nula", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (existeEnfermeraConCedula(enfermera.getIdentificacion())) {
            JOptionPane.showMessageDialog(null, "Ya existe una enfermera con esta cédula: " + enfermera.getIdentificacion(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!puedeAgregarEnfermera(enfermera.getTurno())) {
            JOptionPane.showMessageDialog(null, "No se puede agregar más enfermeras al turno " + enfermera.getTurno() + 
                ". Máximo 2 por turno y 4 en total.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Manejar la imagen
        if (imagen != null && imagen.exists()) {
            String nombreImagen = enfermera.getIdentificacion() + "_" + imagen.getName();
            String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
            Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
            enfermera.setRutaImagen(rutaImagenFinal);
        }
        
        


        List<Enfermera> enfermeras = obtenerEnfermeras();
        enfermeras.add(enfermera);
        guardarListaEnfermeras(enfermeras);

        return true;
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar la enfermera: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

    
   public List<Enfermera> obtenerEnfermeras() {
    List<Enfermera> enfermeras = new ArrayList<>();
    File archivo = new File(RUTA_JSON);

    try {
        if (!archivo.exists() || archivo.length() == 0) {
            // Si el archivo no existe o está vacío, crea uno con estructura básica
            guardarListaEnfermeras(new ArrayList<>());
            return enfermeras;
        }

        // Leer el archivo con manejo de errores mejorado
        String contenido = new String(Files.readAllBytes(archivo.toPath()));
        
        // Verificar si el contenido es un JSON válido
        if (contenido.trim().isEmpty()) {
            guardarListaEnfermeras(new ArrayList<>());
            return enfermeras;
        }

        try {
            Type tipoLista = new TypeToken<Map<String, List<Enfermera>>>() {}.getType();
            Map<String, List<Enfermera>> datos = gson.fromJson(contenido, tipoLista);
            
            if (datos != null && datos.containsKey("enfermeras")) {
                return datos.get("enfermeras");
            }
        } catch (JsonSyntaxException e) {
            // Si falla, intentar leer como array directo (para compatibilidad)
            try {
                Type tipoListaDirecta = new TypeToken<List<Enfermera>>() {}.getType();
                List<Enfermera> listaDirecta = gson.fromJson(contenido, tipoListaDirecta);
                if (listaDirecta != null) {
                    // Migrar a nuevo formato
                    guardarListaEnfermeras(listaDirecta);
                    return listaDirecta;
                }
            } catch (JsonSyntaxException e2) {
                System.err.println("Formato JSON inválido. Creando nuevo archivo.");
                guardarListaEnfermeras(new ArrayList<>());
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al leer/escribir archivo: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    return enfermeras;
}
    
    private void guardarListaEnfermeras(List<Enfermera> enfermeras) throws IOException {
        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(Collections.singletonMap("enfermeras", enfermeras), writer);
        }
    }
    
   public boolean puedeAgregarEnfermera(String turno) {
    List<Enfermera> enfermeras = obtenerEnfermeras();
    
    // Límite total de 4 enfermeras
    if (enfermeras.size() >= 4) {
        return false;
    }
    
    // Límite de 2 por turno
    long countPorTurno = enfermeras.stream()
            .filter(e -> e.getTurno().equalsIgnoreCase(turno))
            .count();
    
    return countPorTurno < 2;
}
    
   


    public boolean existeEnfermeraConCedula(String cedula) {
    if (cedula == null || cedula.trim().isEmpty()) {
        return false;
    }
    return obtenerEnfermeras().stream()
            .anyMatch(e -> e.getIdentificacion() != null && e.getIdentificacion().equals(cedula));
}

    public boolean eliminarEnfermera(String cedula) {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();
            boolean removido = enfermeras.removeIf(e -> e.getIdentificacion().equals(cedula));
            
            if (removido) {
                guardarListaEnfermeras(enfermeras);
                return true;
            }
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la enfermera: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Nuevo método para modificar recibiendo objeto Enfermera
    public boolean modificarEnfermera(String cedulaOriginal, Enfermera enfermeraModificada, File nuevaImagen) {
        try {
            List<Enfermera> enfermeras = obtenerEnfermeras();
            
            for (int i = 0; i < enfermeras.size(); i++) {
                Enfermera e = enfermeras.get(i);
                if (e.getIdentificacion().equals(cedulaOriginal)) {
                    // Actualizar la enfermera
                    enfermeras.set(i, enfermeraModificada);
                    
                    // Manejar imagen
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String nombreImagen = enfermeraModificada.getIdentificacion() + "_" + nuevaImagen.getName();
                        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                        enfermeraModificada.setRutaImagen(rutaImagenFinal);
                    }
                    
                    guardarListaEnfermeras(enfermeras);
                    return true;
                }
            }
            
            JOptionPane.showMessageDialog(null, "No se encontró la enfermera con cédula: " + cedulaOriginal, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar enfermera: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public Enfermera obtenerEnfermeraPorCedula(String cedula) {
        return obtenerEnfermeras().stream()
                .filter(e -> e.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }
    
    // Método equivalente a obtenerEnfermeraPorIdentificacion
    public Enfermera obtenerEnfermeraPorIdentificacion(String cedula) {
        return obtenerEnfermeraPorCedula(cedula);
    }
    
    public List<Enfermera> obtenerEnfermeraPorTurno(String turno) {
        return obtenerEnfermeras().stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .collect(Collectors.toList());
    }
    
    public List<Object[]> obtenerDatosEnfermerasParaTabla() {
        return obtenerEnfermeras().stream()
                .map(e -> new Object[]{
                    e.getPrimerNombre(),
                    e.getSegundoNombre(),
                    e.getPrimerApellido(),
                    e.getSegundoApellido(),
                    e.getEdad(),
                    e.getIdentificacion(),
                    e.getNacionalidad(),
                    e.getCorreo(),
                    e.getTurno(),
                    e.getFechaFinContrato()
                })
                .collect(Collectors.toList());
    }
    
    public String[] getNombresColumnas() {
        return new String[]{
            "Primer Nombre",
            "Segundo Nombre",
            "Primer Apellido",
            "Segundo Apellido",
            "Edad",
            "Cédula",
            "Nacionalidad",
            "Correo",
            "Turno",
            "Fin de Contrato"
        };
    }
    
    public Class<?>[] getTiposColumnas() {
        return new Class<?>[]{
            String.class,
            String.class,
            String.class,
            String.class,
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class
        };
    }
}