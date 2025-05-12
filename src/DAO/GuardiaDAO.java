package DAO;

import Model.Entities.Guardia;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class GuardiaDAO {

    private static final String RUTA_JSON = "src/Resources/DATA/guardias.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_guardias/";
    private final Gson gson;
    private static GuardiaDAO instancia;

    public GuardiaDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        crearDirectoriosSiNoExisten();
    }

    public static synchronized GuardiaDAO getInstancia() {
        if (instancia == null) {
            instancia = new GuardiaDAO();
        }
        return instancia;
    }

    private void crearDirectoriosSiNoExisten() {
        try {
            // Usar rutas relativas desde el directorio de trabajo
            Path directorioImagenes = Paths.get("Resources/imagenes_guardias");
            Path directorioDatos = Paths.get("Resources/DATA");

            if (!Files.exists(directorioImagenes)) {
                Files.createDirectories(directorioImagenes);
            }
            if (!Files.exists(directorioDatos)) {
                Files.createDirectories(directorioDatos);
            }
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

    public boolean guardarGuardia(Guardia guardia, File imagen) throws IOException {
        // Validar que la imagen existe (ya validado en controller)
        String nombreImagen = guardia.getIdentificacion() + "_"
                + System.currentTimeMillis()
                + imagen.getName().substring(imagen.getName().lastIndexOf("."));

        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;

        // Copiar la imagen
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        guardia.setRutaImagen(rutaImagenFinal);

        // Guardar datos
        List<Guardia> guardias = obtenerGuardias();
        guardias.add(guardia);
        guardarListaGuardias(guardias);

        return true;
    }

    public List<Guardia> obtenerGuardias() {
        List<Guardia> guardias = new ArrayList<>();
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                guardarListaGuardias(new ArrayList<>());
                return guardias;
            }

            String contenido = new String(Files.readAllBytes(archivo.toPath()));

            if (contenido.trim().isEmpty()) {
                guardarListaGuardias(new ArrayList<>());
                return guardias;
            }

            try {
                JsonObject jsonObject = JsonParser.parseString(contenido).getAsJsonObject();
                JsonArray guardiasArray = jsonObject.getAsJsonArray("guardias");

                Type tipoLista = new TypeToken<List<Guardia>>() {
                }.getType();
                return gson.fromJson(guardiasArray, tipoLista);
            } catch (JsonSyntaxException e) {
                System.err.println("Formato JSON inválido. Creando nuevo archivo.");
                guardarListaGuardias(new ArrayList<>());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al leer/escribir archivo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return guardias;
    }

    private void guardarListaGuardias(List<Guardia> guardias) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray guardiasArray = new JsonArray();

        for (Guardia guardia : guardias) {
            JsonObject guardiaJson = new JsonObject();

            // Convertir rutas absolutas a relativas si es necesario
            String rutaImagen = guardia.getRutaImagen();
            if (rutaImagen != null && rutaImagen.startsWith("C:")) {
                rutaImagen = rutaImagen.replace("C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\", "src/");
                rutaImagen = rutaImagen.replace("\\", "/"); // Normalizar separadores
            }

            // Agregar todos los campos de manera organizada
            guardiaJson.addProperty("cargo", guardia.getCargo());
            guardiaJson.addProperty("turno", guardia.getTurno());
            guardiaJson.addProperty("fechaInicioContrato", guardia.getFechaInicioContrato().toString());
            guardiaJson.addProperty("fechaFinContrato", guardia.getFechaFinContrato().toString());
            guardiaJson.addProperty("rutaImagen", rutaImagen); // Usar la ruta normalizada
            guardiaJson.addProperty("correo", guardia.getCorreo());
            guardiaJson.addProperty("primerNombre", guardia.getPrimerNombre());
            guardiaJson.addProperty("segundoNombre", guardia.getSegundoNombre());
            guardiaJson.addProperty("primerApellido", guardia.getPrimerApellido());
            guardiaJson.addProperty("segundoApellido", guardia.getSegundoApellido());
            guardiaJson.addProperty("edad", guardia.getEdad());
            guardiaJson.addProperty("sexo", guardia.getSexo());
            guardiaJson.addProperty("nacionalidad", guardia.getNacionalidad());
            guardiaJson.addProperty("identificacion", guardia.getIdentificacion());

            guardiasArray.add(guardiaJson);
        }

        jsonObject.add("guardias", guardiasArray);

        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(jsonObject, writer);
        }
    }

    public boolean existeGuardia(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return obtenerGuardias().stream()
                .anyMatch(g -> g.getIdentificacion() != null && g.getIdentificacion().equals(cedula));
    }

    public boolean eliminarGuardia(String cedula) {
        try {
            List<Guardia> guardias = obtenerGuardias();
            Optional<Guardia> guardiaAEliminar = guardias.stream()
                    .filter(g -> g.getIdentificacion().equals(cedula))
                    .findFirst();

            if (guardiaAEliminar.isPresent()) {
                // Eliminar imagen asociada si existe
                if (guardiaAEliminar.get().getRutaImagen() != null
                        && !guardiaAEliminar.get().getRutaImagen().isEmpty()) {
                    try {
                        Files.deleteIfExists(Paths.get(guardiaAEliminar.get().getRutaImagen()));
                    } catch (IOException e) {
                        System.err.println("Error al eliminar la imagen: " + e.getMessage());
                    }
                }

                // Eliminar guardia
                guardias.removeIf(g -> g.getIdentificacion().equals(cedula));
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

    public boolean modificarGuardia(String cedulaOriginal, Guardia guardiaModificado, File nuevaImagen) {
        try {
            System.out.println("=== INICIO MODIFICACIÓN GUARDIA ===");
            System.out.println("Cédula original: " + cedulaOriginal);
            System.out.println("Nueva imagen proporcionada: " + (nuevaImagen != null ? nuevaImagen.getAbsolutePath() : "null"));

            List<Guardia> guardias = obtenerGuardias();
            System.out.println("Total guardias encontrados: " + guardias.size());

            for (int i = 0; i < guardias.size(); i++) {
                Guardia g = guardias.get(i);
                System.out.println("Comparando con guardia: " + g.getIdentificacion());

                if (g.getIdentificacion().equals(cedulaOriginal)) {
                    System.out.println("Guardia encontrado para modificar");

                    // 1. Mantener ruta original
                    String rutaImagenFinal = g.getRutaImagen();
                    System.out.println("Ruta imagen actual: " + rutaImagenFinal);

                    // 2. Procesar nueva imagen solo si existe
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        System.out.println("Procesando nueva imagen...");
                        // ... código para manejar nueva imagen ...
                    } else {
                        System.out.println("No se proporcionó nueva imagen o no existe. Manteniendo imagen actual.");
                        // Mantener la ruta de imagen existente
                        rutaImagenFinal = g.getRutaImagen();
                    }

                    // Actualizar datos
                    System.out.println("Actualizando datos del guardia...");
                    guardiaModificado.setRutaImagen(rutaImagenFinal); // <-- Asegúrate de establecer la ruta correcta
                    guardias.set(i, guardiaModificado);

                    // Guardar cambios
                    System.out.println("Guardando lista de guardias...");
                    guardarListaGuardias(guardias);

                    System.out.println("=== MODIFICACIÓN EXITOSA ===");
                    return true;
                }
            }

            System.out.println("Guardia no encontrado para modificar");
            return false;
        } catch (Exception e) {
            System.err.println("=== ERROR EN MODIFICACIÓN ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al modificar guardia: " + e.getMessage(), e);
        }
    }

    public Guardia obtenerGuardiaPorCedula(String cedula) {
        return obtenerGuardias().stream()
                .filter(g -> g.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public List<Guardia> obtenerGuardiasPorTurno(String turno) {
        return obtenerGuardias().stream()
                .filter(g -> g.getTurno().equalsIgnoreCase(turno))
                .collect(Collectors.toList());
    }

    public List<Object[]> obtenerDatosGuardiasParaTabla() {
        return obtenerGuardias().stream()
                .map(g -> new Object[]{
            g.getPrimerNombre(),
            g.getSegundoNombre(),
            g.getPrimerApellido(),
            g.getSegundoApellido(),
            g.getEdad(),
            g.getIdentificacion(),
            g.getNacionalidad(),
            g.getCorreo(),
            g.getTurno(),
            g.getCargo(),
            g.getFechaInicioContrato(),
            g.getFechaFinContrato()
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
            "Cargo",
            "Fecha inicio contrato",
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
            String.class, // Tipo para fecha inicio
            String.class // Tipo para fecha fin
        };
    }
}
