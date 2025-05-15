package DAO;

import Controller.UsuarioController;
import Model.Entities.OficialDeRegistro;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class OficialDeRegistroDAO {

    private static final String RUTA_JSON = "src/Resources/DATA/oficiales_registro.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_oficiales_registro/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private final Gson gson;
    private static OficialDeRegistroDAO instancia;

    public OficialDeRegistroDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        crearDirectoriosSiNoExisten();
    }

    public static synchronized OficialDeRegistroDAO getInstancia() {
        if (instancia == null) {
            instancia = new OficialDeRegistroDAO();
        }
        return instancia;
    }

       private void crearDirectoriosSiNoExisten() {
        try {
            Files.createDirectories(Paths.get(RUTA_IMAGENES));
            Files.createDirectories(Paths.get(RUTA_JSON).getParent());
            
            // Crear archivo JSON si no existe
            File archivoJson = new File(RUTA_JSON);
            if (!archivoJson.exists()) {
                archivoJson.createNewFile();
                // Inicializar con estructura básica
                Files.write(Paths.get(RUTA_JSON), "{\"oficiales\":[]}".getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorios/archivo: " + e.getMessage());
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

    private String generarUsuarioUnico(String primerNombre, String primerApellido, List<Usuario> usuariosExistentes) {
        Random random = new Random();
        String usuarioBase = primerNombre + primerApellido;
        String caracteresEspeciales = "!@#$%^&*";

        while (true) {
            int numeroRandom = random.nextInt(1000) + 1;
            char caracterEspecial = caracteresEspeciales.charAt(random.nextInt(caracteresEspeciales.length()));

            String usuarioGenerado = usuarioBase + numeroRandom + caracterEspecial;

            boolean existe = usuariosExistentes.stream()
                    .anyMatch(u -> u.getUsuario().equalsIgnoreCase(usuarioGenerado));

            if (!existe) {
                return usuarioGenerado;
            }
        }
    }

    private void guardarUsuario(Usuario usuario) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        usuarios.removeIf(u -> u.getUsuario().equals(usuario.getUsuario()));
        usuarios.add(usuario);

        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            JsonObject jsonObject = new JsonObject();
            JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
            jsonObject.add("usuarios", usuariosArray);
            gson.toJson(jsonObject, writer);
        }
    }

    public OficialDeRegistro obtenerOficialPorUsuario(String usuario) {
        List<OficialDeRegistro> oficiales = obtenerOficiales();
        return oficiales.stream()
                .filter(o -> o.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    private List<Usuario> obtenerTodosUsuarios() throws IOException {
        File archivo = new File(RUTA_USUARIOS);

        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");

            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            return gson.fromJson(usuariosArray, tipoLista);
        }
    }

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

   public boolean guardarOficial(OficialDeRegistro oficial, File imagen) throws IOException {
    // Validaciones iniciales mejoradas
    if (oficial == null) {
        throw new IllegalArgumentException("El objeto oficial no puede ser null");
    }
    
    if (imagen == null || !imagen.exists() || imagen.length() == 0) {
        throw new IllegalArgumentException("La imagen proporcionada no es válida");
    }

    // Generar credenciales
    UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
    String usuario = credenciales.usuario;
    String contrasena = credenciales.contrasena;
    String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

    oficial.setUsuario(usuario);
    oficial.setContrasena(contrasenaEncriptada);

    // Guardar imagen con manejo de errores
    String extension = imagen.getName().substring(imagen.getName().lastIndexOf("."));
    String nombreImagen = oficial.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
    String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
    
    try {
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        oficial.setRutaImagen(rutaImagenFinal);
    } catch (IOException e) {
        System.err.println("Error al guardar imagen: " + e.getMessage());
        throw new IOException("No se pudo guardar la imagen del oficial");
    }

    // Obtener y actualizar lista de oficiales
    List<OficialDeRegistro> oficiales = obtenerOficiales();
    
    // Verificar duplicados
    if (oficiales.stream().anyMatch(o -> o.getIdentificacion().equals(oficial.getIdentificacion()))) {
        // Eliminar la imagen recién copiada si hay duplicado
        Files.deleteIfExists(Paths.get(rutaImagenFinal));
        throw new IllegalArgumentException("Ya existe un oficial con esta cédula");
    }

    oficiales.add(oficial);
    
    // Guardar la lista actualizada
    try {
        guardarListaOficiales(oficiales);
        
        // Crear y guardar usuario
        Usuario nuevoUsuario = new Usuario(
            oficial.getPrimerNombre(),
            oficial.getSegundoNombre(),
            oficial.getPrimerApellido(),
            oficial.getSegundoApellido(),
            oficial.getEdad(),
            oficial.getSexo(),
            oficial.getNacionalidad(),
            oficial.getIdentificacion(),
            usuario,
            contrasenaEncriptada,
            RolEnum.OFICIAL_DE_REGISTRO
        );
        
        guardarUsuario(nuevoUsuario);

        // Enviar correo (manejar posible error)
        try {
            EmailSender.getInstancia().enviarCredenciales(
                oficial.getCorreo(),
                usuario,
                contrasena,
                RolEnum.OFICIAL_DE_REGISTRO
            );
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Oficial registrado pero no se pudieron enviar las credenciales por correo",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        
        return true;
    } catch (IOException e) {
        // Revertir cambios si falla
        Files.deleteIfExists(Paths.get(rutaImagenFinal));
        throw e;
    }
}

private void guardarListaOficiales(List<OficialDeRegistro> oficiales) throws IOException {
    // Validación adicional
    if (oficiales == null) {
        throw new IllegalArgumentException("La lista de oficiales no puede ser null");
    }

    // Crear estructura JSON completa
    JsonObject jsonPrincipal = new JsonObject();
    JsonArray jsonArrayOficiales = new JsonArray();

    for (OficialDeRegistro oficial : oficiales) {
        JsonObject jsonOficial = new JsonObject();
        
        // Mapear todos los campos necesarios
        jsonOficial.addProperty("primerNombre", oficial.getPrimerNombre());
        jsonOficial.addProperty("segundoNombre", oficial.getSegundoNombre());
        jsonOficial.addProperty("primerApellido", oficial.getPrimerApellido());
        jsonOficial.addProperty("segundoApellido", oficial.getSegundoApellido());
        jsonOficial.addProperty("edad", oficial.getEdad());
        jsonOficial.addProperty("sexo", oficial.getSexo());
        jsonOficial.addProperty("nacionalidad", oficial.getNacionalidad());
        jsonOficial.addProperty("identificacion", oficial.getIdentificacion());
        jsonOficial.addProperty("correo", oficial.getCorreo());
        jsonOficial.addProperty("turno", oficial.getTurno());
        jsonOficial.addProperty("fechaContratacion", oficial.getFechaContratacion().toString());
        jsonOficial.addProperty("fechaFinContrato", oficial.getFechaFinContrato().toString());
        jsonOficial.addProperty("rutaImagen", oficial.getRutaImagen());
        jsonOficial.addProperty("usuario", oficial.getUsuario());
        jsonOficial.addProperty("contrasena", oficial.getContrasena());
        
        jsonArrayOficiales.add(jsonOficial);
    }

    jsonPrincipal.add("oficiales", jsonArrayOficiales);

    // Escribir con manejo de errores
    Path path = Paths.get(RUTA_JSON);
    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
        gson.toJson(jsonPrincipal, writer);
        System.out.println("Datos guardados correctamente en: " + path.toAbsolutePath());
    } catch (IOException e) {
        System.err.println("Error al escribir JSON: " + e.getMessage());
        throw e;
    }
}

    public List<OficialDeRegistro> obtenerOficiales() {
        List<OficialDeRegistro> oficiales = new ArrayList<>();
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                guardarListaOficiales(new ArrayList<>());
                return oficiales;
            }

            String contenido = new String(Files.readAllBytes(archivo.toPath()));

            if (contenido.trim().isEmpty()) {
                guardarListaOficiales(new ArrayList<>());
                return oficiales;
            }

            try {
                JsonObject jsonObject = JsonParser.parseString(contenido).getAsJsonObject();
                JsonArray oficialesArray = jsonObject.getAsJsonArray("oficiales");

                Type tipoLista = new TypeToken<List<OficialDeRegistro>>() {
                }.getType();
                return gson.fromJson(oficialesArray, tipoLista);
            } catch (JsonSyntaxException e) {
                System.err.println("Formato JSON inválido. Creando nuevo archivo.");
                guardarListaOficiales(new ArrayList<>());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al leer/escribir archivo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return oficiales;
    }

   

    public boolean existeOficialConCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return obtenerOficiales().stream()
                .anyMatch(o -> o.getIdentificacion() != null && o.getIdentificacion().equals(cedula));
    }

    public boolean eliminarOficial(String cedula) {
        try {
            List<OficialDeRegistro> oficiales = obtenerOficiales();
            Optional<OficialDeRegistro> oficialAEliminar = oficiales.stream()
                    .filter(o -> o.getIdentificacion().equals(cedula))
                    .findFirst();

            if (oficialAEliminar.isPresent()) {
                eliminarUsuario(oficialAEliminar.get().getUsuario());
                oficiales.removeIf(o -> o.getIdentificacion().equals(cedula));
                guardarListaOficiales(oficiales);
                return true;
            }
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el oficial: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean modificarOficial(String cedulaOriginal, OficialDeRegistro oficialModificado, File nuevaImagen) {
        try {
            List<OficialDeRegistro> oficiales = obtenerOficiales();

            for (int i = 0; i < oficiales.size(); i++) {
                OficialDeRegistro o = oficiales.get(i);
                if (o.getIdentificacion().equals(cedulaOriginal)) {
                    String rutaImagenFinal = o.getRutaImagen();

                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String nombreImagen = oficialModificado.getIdentificacion() + "_" + System.currentTimeMillis()
                                + nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    }

                    oficialModificado.setUsuario(o.getUsuario());
                    oficialModificado.setContrasena(o.getContrasena());
                    oficialModificado.setRutaImagen(rutaImagenFinal);

                    oficiales.set(i, oficialModificado);

                    guardarListaOficiales(oficiales);
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar oficial: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public OficialDeRegistro obtenerOficialPorCedula(String cedula) {
        return obtenerOficiales().stream()
                .filter(o -> o.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public OficialDeRegistro obtenerOficialPorIdentificacion(String cedula) {
        return obtenerOficialPorCedula(cedula);
    }

    public List<OficialDeRegistro> obtenerOficialPorTurno(String turno) {
        return obtenerOficiales().stream()
                .filter(o -> o.getTurno().equalsIgnoreCase(turno))
                .collect(Collectors.toList());
    }

    public List<Object[]> obtenerDatosOficialesParaTabla() {
        return obtenerOficiales().stream()
                .map(o -> new Object[]{
                    o.getPrimerNombre(),
                    o.getSegundoNombre(),
                    o.getPrimerApellido(),
                    o.getSegundoApellido(),
                    o.getEdad(),
                    o.getIdentificacion(),
                    o.getNacionalidad(),
                    o.getCorreo(),
                    o.getTurno(),
                    o.getFechaContratacionFormateada(),
                    o.getFechaFinContratoFormateada()
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
            "Inicio Contrato",
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
            String.class,
            String.class
        };
    }
}