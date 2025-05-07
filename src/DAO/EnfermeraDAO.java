package DAO;

import Model.Entities.Enfermera;
import Model.Constants.RolEnum;
import Utilidades.EmailSender;
import Model.Entities.Usuario;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class EnfermeraDAO {
   private static final String RUTA_JSON = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Resources\\DATA\\enfermera.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_enfermeras/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
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
     
     // Método para generar un nombre de usuario único
    private String generarUsuarioUnico(String primerNombre, String primerApellido, List<Usuario> usuariosExistentes) {
        Random random = new Random();
        String usuarioBase = primerNombre + primerApellido;
        String caracteresEspeciales = "!@#$%^&*";
        
        while (true) {
            int numeroRandom = random.nextInt(1000) + 1;
            char caracterEspecial = caracteresEspeciales.charAt(random.nextInt(caracteresEspeciales.length()));
            
            String usuarioGenerado = usuarioBase + numeroRandom + caracterEspecial;
            
            // Verificar si el usuario ya existe
            boolean existe = usuariosExistentes.stream()
                .anyMatch(u -> u.getUsuario().equalsIgnoreCase(usuarioGenerado));
                
            if (!existe) {
                return usuarioGenerado;
            }
        }
    }
    
   private void guardarUsuario(Usuario usuario) throws IOException {
    List<Usuario> usuarios = obtenerTodosUsuarios();
    
    // Eliminar usuario existente si ya está (para evitar duplicados)
    usuarios.removeIf(u -> u.getUsuario().equals(usuario.getUsuario()));
    
    usuarios.add(usuario);
    
    try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
        JsonObject jsonObject = new JsonObject();
        JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
        jsonObject.add("usuarios", usuariosArray);
        gson.toJson(jsonObject, writer);
    }
}
    
    public Enfermera obtenerEnfermeraPorUsuario(String usuario) {
    List<Enfermera> enfermeras = obtenerEnfermeras();
    return enfermeras.stream()
            .filter(e -> e.getUsuario().equals(usuario))
            .findFirst()
            .orElse(null);
}
    
    // Método para obtener todos los usuarios
    private List<Usuario> obtenerTodosUsuarios() throws IOException {
        File archivo = new File(RUTA_USUARIOS);
        
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        
        try (FileReader reader = new FileReader(archivo)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");
            
            Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
            return gson.fromJson(usuariosArray, tipoLista);
        }
    }
    
    // Método para eliminar un usuario por nombre de usuario
    private void eliminarUsuario(String usuario) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        usuarios.removeIf(u -> u.getUsuario().equals(usuario));
        
        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            JsonObject jsonObject = new JsonObject();
            JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
            jsonObject.add("usuarios", usuariosArray);
            gson.toJson(jsonObject, writer);
        }
    }
    
    
    // Método para generar una contraseña aleatoria
    private String generarContrasena() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);
        
        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return sb.toString();
    }
    
    private String guardarImagenDesdeCamara(BufferedImage imagen, String identificacion) throws IOException {
    String nombreImagen = identificacion + "_foto.jpg";
    String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
    
    File outputFile = new File(rutaImagenFinal);
    ImageIO.write(imagen, "jpg", outputFile);
    
    return rutaImagenFinal;
}
    
    
    
   public boolean guardarEnfermera(Enfermera enfermera, File imagen) throws IOException {
    // Generar y asignar credenciales
    List<Usuario> usuarios = obtenerTodosUsuarios();
    String usuario = generarUsuarioUnico(enfermera.getPrimerNombre(), enfermera.getPrimerApellido(), usuarios);
    String contrasena = generarContrasena();
    
    enfermera.setUsuario(usuario);
    enfermera.setContrasena(contrasena);

    // Guardar imagen
    String nombreImagen = enfermera.getIdentificacion() + "_" + 
            System.currentTimeMillis() + 
            imagen.getName().substring(imagen.getName().lastIndexOf("."));
    
    String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
    Files.createDirectories(Paths.get(RUTA_IMAGENES));
    Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
    enfermera.setRutaImagen(rutaImagenFinal);

    // Guardar datos
    List<Enfermera> enfermeras = obtenerEnfermeras();
    enfermeras.add(enfermera);
    guardarListaEnfermeras(enfermeras);

    // Guardar usuario
    Usuario nuevoUsuario = new Usuario(usuario, contrasena, RolEnum.ENFERMERA);
    guardarUsuario(nuevoUsuario);

    // Enviar credenciales
    return EmailSender.getInstancia().enviarCredenciales(
        enfermera.getCorreo(), 
        usuario, 
        contrasena,
        RolEnum.ENFERMERA
    );
}
    
   public List<Enfermera> obtenerEnfermeras() {
    List<Enfermera> enfermeras = new ArrayList<>();
    File archivo = new File(RUTA_JSON);

    try {
        if (!archivo.exists() || archivo.length() == 0) {
            guardarListaEnfermeras(new ArrayList<>());
            return enfermeras;
        }

        String contenido = new String(Files.readAllBytes(archivo.toPath()));
        
        if (contenido.trim().isEmpty()) {
            guardarListaEnfermeras(new ArrayList<>());
            return enfermeras;
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(contenido).getAsJsonObject();
            JsonArray enfermerasArray = jsonObject.getAsJsonArray("enfermeras");
            
            Type tipoLista = new TypeToken<List<Enfermera>>() {}.getType();
            return gson.fromJson(enfermerasArray, tipoLista);
        } catch (JsonSyntaxException e) {
            System.err.println("Formato JSON inválido. Creando nuevo archivo.");
            guardarListaEnfermeras(new ArrayList<>());
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al leer/escribir archivo: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    return enfermeras;
}
    
    private void guardarListaEnfermeras(List<Enfermera> enfermeras) throws IOException {
    JsonObject jsonObject = new JsonObject();
    JsonArray enfermerasArray = new JsonArray();
    
    for (Enfermera enfermera : enfermeras) {
        JsonObject enfermeraJson = new JsonObject();
        // Agregar todos los campos de manera organizada
        enfermeraJson.addProperty("usuario", enfermera.getUsuario());
        enfermeraJson.addProperty("contrasena", enfermera.getContrasena());
        enfermeraJson.addProperty("turno", enfermera.getTurno());
        enfermeraJson.addProperty("fechaContratacion", enfermera.getFechaContratacion().toString());
        enfermeraJson.addProperty("fechaFinContrato", enfermera.getFechaFinContrato().toString());
        enfermeraJson.addProperty("rutaImagen", enfermera.getRutaImagen());
        enfermeraJson.addProperty("correo", enfermera.getCorreo());
        enfermeraJson.addProperty("primerNombre", enfermera.getPrimerNombre());
        enfermeraJson.addProperty("segundoNombre", enfermera.getSegundoNombre());
        enfermeraJson.addProperty("primerApellido", enfermera.getPrimerApellido());
        enfermeraJson.addProperty("segundoApellido", enfermera.getSegundoApellido());
        enfermeraJson.addProperty("edad", enfermera.getEdad());
        enfermeraJson.addProperty("sexo", enfermera.getSexo());
        enfermeraJson.addProperty("nacionalidad", enfermera.getNacionalidad());
        enfermeraJson.addProperty("identificacion", enfermera.getIdentificacion());
        
        enfermerasArray.add(enfermeraJson);
    }
    
    jsonObject.add("enfermeras", enfermerasArray);
    
    try (Writer writer = new FileWriter(RUTA_JSON)) {
        gson.toJson(jsonObject, writer);
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
            Optional<Enfermera> enfermeraAEliminar = enfermeras.stream()
                .filter(e -> e.getIdentificacion().equals(cedula))
                .findFirst();
            
            if (enfermeraAEliminar.isPresent()) {
                // Eliminar usuario asociado
                eliminarUsuario(enfermeraAEliminar.get().getUsuario());
                
                // Eliminar enfermera
                enfermeras.removeIf(e -> e.getIdentificacion().equals(cedula));
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
    
   public boolean modificarEnfermera(String cedulaOriginal, Enfermera enfermeraModificada, File nuevaImagen) {
    try {
        List<Enfermera> enfermeras = obtenerEnfermeras();
        
        for (int i = 0; i < enfermeras.size(); i++) {
            Enfermera e = enfermeras.get(i);
            if (e.getIdentificacion().equals(cedulaOriginal)) {
                // Manejo de la imagen
                String rutaImagenFinal = e.getRutaImagen(); // Mantener la original por defecto
                
                if (nuevaImagen != null && nuevaImagen.exists()) {
                    // Generar nombre único para la nueva imagen
                    String nombreImagen = enfermeraModificada.getIdentificacion() + "_" + System.currentTimeMillis() + 
                                       nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                    rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                    
                    // Copiar la nueva imagen
                    Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                }
                
                // Actualizar datos manteniendo usuario y contraseña
                enfermeraModificada.setUsuario(e.getUsuario());
                enfermeraModificada.setContrasena(e.getContrasena());
                enfermeraModificada.setRutaImagen(rutaImagenFinal);
                
                enfermeras.set(i, enfermeraModificada);
                
                guardarListaEnfermeras(enfermeras);
                return true;
            }
        }
        
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