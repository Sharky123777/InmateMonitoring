package DAO;

import Controller.UsuarioController;
import Model.Entities.CoordinadorDeActividades;
import Model.Constants.RolEnum;
import Utilidades.EmailSender;
import Model.Entities.Usuario;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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

public class CoordinadorDeActividadesDAO {

    private static final String RUTA_JSON = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Resources\\DATA\\CDA.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_CDA/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private final Gson gson;
    private static CoordinadorDeActividadesDAO instancia;

    public CoordinadorDeActividadesDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        crearDirectoriosSiNoExisten();
    }

    public static synchronized CoordinadorDeActividadesDAO getInstancia() {
        if (instancia == null) {
            instancia = new CoordinadorDeActividadesDAO();
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

    public void guardarUsuario(Usuario usuario) throws IOException {
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

    public CoordinadorDeActividades obtenerCoordinadorPorUsuario(String usuario) {
        List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();
        return coordinadores.stream()
                .filter(c -> c.getUsuario().equals(usuario))
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

    // En CoordinadorDeActividadesDAO.java
    public boolean guardarCoordinador(CoordinadorDeActividades coordinador, File imagen) throws IOException {
        // 1. Generar credenciales
        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        // 3. Asignar credenciales
        coordinador.setUsuario(usuario);
        coordinador.setContrasena(contrasenaEncriptada); // AQUÍ GUARDAMOS LA CONTRASEÑA ENCRIPTADA

        // 4. Guardar imagen
        String nombreImagen = coordinador.getIdentificacion() + "_"
                + System.currentTimeMillis()
                + imagen.getName().substring(imagen.getName().lastIndexOf("."));

        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        coordinador.setRutaImagen(rutaImagenFinal);

        // 5. Guardar coordinador
        List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();
        coordinadores.add(coordinador);
        guardarListaCoordinadores(coordinadores);

        // 6. Guardar usuario (CON CONTRASEÑA ENCRIPTADA)
        Usuario nuevoUsuario = new Usuario(
                coordinador.getPrimerNombre(),
                coordinador.getSegundoNombre(),
                coordinador.getPrimerApellido(),
                coordinador.getSegundoApellido(),
                coordinador.getEdad(),
                coordinador.getSexo(),
                coordinador.getNacionalidad(),
                coordinador.getIdentificacion(),
                usuario,
                contrasenaEncriptada,
                RolEnum.COORDINADOR_DE_ACTIVIDADES
        );

        guardarUsuario(nuevoUsuario);

        // 7. Enviar correo (CON CONTRASEÑA SIN ENCRIPTAR)
        return EmailSender.getInstancia().enviarCredenciales(
                coordinador.getCorreo(),
                usuario,
                contrasena, // Usamos la contraseña original sin encriptar
                RolEnum.COORDINADOR_DE_ACTIVIDADES
        );
    }

    public List<CoordinadorDeActividades> obtenerCoordinadores() {
        List<CoordinadorDeActividades> coordinadores = new ArrayList<>();
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                guardarListaCoordinadores(new ArrayList<>());
                return coordinadores;
            }

            String contenido = new String(Files.readAllBytes(archivo.toPath()));

            if (contenido.trim().isEmpty()) {
                guardarListaCoordinadores(new ArrayList<>());
                return coordinadores;
            }

            try {
                JsonObject jsonObject = JsonParser.parseString(contenido).getAsJsonObject();
                JsonArray coordinadoresArray = jsonObject.getAsJsonArray("coordinadores");

                Type tipoLista = new TypeToken<List<CoordinadorDeActividades>>() {
                }.getType();
                return gson.fromJson(coordinadoresArray, tipoLista);
            } catch (JsonSyntaxException e) {
                System.err.println("Formato JSON inválido. Creando nuevo archivo.");
                guardarListaCoordinadores(new ArrayList<>());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al leer/escribir archivo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return coordinadores;
    }

    private void guardarListaCoordinadores(List<CoordinadorDeActividades> coordinadores) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray coordinadoresArray = new JsonArray();

        for (CoordinadorDeActividades coordinador : coordinadores) {
            JsonObject coordinadorJson = new JsonObject();
            coordinadorJson.addProperty("usuario", coordinador.getUsuario());
            coordinadorJson.addProperty("contrasena", coordinador.getContrasena());
            coordinadorJson.addProperty("turno", coordinador.getTurno());
            coordinadorJson.addProperty("cargo", coordinador.getCargo());
            coordinadorJson.addProperty("fechaInicioContrato", coordinador.getFechaInicioContrato().toString());
            coordinadorJson.addProperty("fechaFinContrato", coordinador.getFechaFinContrato().toString());
            coordinadorJson.addProperty("rutaImagen", coordinador.getRutaImagen());
            coordinadorJson.addProperty("correo", coordinador.getCorreo());
            coordinadorJson.addProperty("primerNombre", coordinador.getPrimerNombre());
            coordinadorJson.addProperty("segundoNombre", coordinador.getSegundoNombre());
            coordinadorJson.addProperty("primerApellido", coordinador.getPrimerApellido());
            coordinadorJson.addProperty("segundoApellido", coordinador.getSegundoApellido());
            coordinadorJson.addProperty("edad", coordinador.getEdad());
            coordinadorJson.addProperty("sexo", coordinador.getSexo());
            coordinadorJson.addProperty("nacionalidad", coordinador.getNacionalidad());
            coordinadorJson.addProperty("identificacion", coordinador.getIdentificacion());

            coordinadoresArray.add(coordinadorJson);
        }

        jsonObject.add("coordinadores", coordinadoresArray);

        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(jsonObject, writer);
        }
    }

    public boolean puedeAgregarCoordinador() {
        List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();
        return coordinadores.size() < 2;
    }

    public boolean existeCoordinadorConCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return obtenerCoordinadores().stream()
                .anyMatch(c -> c.getIdentificacion() != null && c.getIdentificacion().equals(cedula));
    }

    public boolean eliminarCoordinador(String cedula) {
        try {
            List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();
            Optional<CoordinadorDeActividades> coordinadorAEliminar = coordinadores.stream()
                    .filter(c -> c.getIdentificacion().equals(cedula))
                    .findFirst();

            if (coordinadorAEliminar.isPresent()) {
                eliminarUsuario(coordinadorAEliminar.get().getUsuario());
                coordinadores.removeIf(c -> c.getIdentificacion().equals(cedula));
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

    public boolean modificarCoordinador(String cedulaOriginal, CoordinadorDeActividades coordinadorModificado, File nuevaImagen) {
        try {
            List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();

            for (int i = 0; i < coordinadores.size(); i++) {
                CoordinadorDeActividades c = coordinadores.get(i);
                if (c.getIdentificacion().equals(cedulaOriginal)) {
                    String rutaImagenFinal = c.getRutaImagen();

                    // Solo actualizar la imagen si se proporciona una nueva
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String nombreImagen = coordinadorModificado.getIdentificacion() + "_" + System.currentTimeMillis()
                                + nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    }

                    coordinadorModificado.setFechaInicioContrato(c.getFechaInicioContrato());
                    coordinadorModificado.setUsuario(c.getUsuario());
                    coordinadorModificado.setContrasena(c.getContrasena());
                    coordinadorModificado.setRutaImagen(rutaImagenFinal);

                    coordinadores.set(i, coordinadorModificado);
                    guardarListaCoordinadores(coordinadores);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar coordinador: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public CoordinadorDeActividades obtenerCoordinadorPorCedula(String cedula) {
        return obtenerCoordinadores().stream()
                .filter(c -> c.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public CoordinadorDeActividades obtenerCoordinadorPorIdentificacion(String cedula) {
        return obtenerCoordinadorPorCedula(cedula);
    }

    public List<Object[]> obtenerDatosCoordinadoresParaTabla() {
        return obtenerCoordinadores().stream()
                .map(c -> new Object[]{
            c.getPrimerNombre(),
            c.getSegundoNombre(),
            c.getPrimerApellido(),
            c.getSegundoApellido(),
            c.getEdad(),
            c.getIdentificacion(),
            c.getNacionalidad(),
            c.getCorreo(),
            c.getCargo(),
            c.getTurno(),
            c.getFechaInicioContrato(),
            c.getFechaFinContrato()
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
