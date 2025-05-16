package DAO;

import Controller.UsuarioController;
import Model.Entities.Oficial;
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

public class OficialDAO {

    private static final String RUTA_JSON = "src/Resources/DATA/oficial.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_oficiales/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private final Gson gson;
    private static OficialDAO instancia;

    public OficialDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        crearDirectoriosSiNoExisten();
    }

    public static synchronized OficialDAO getInstancia() {
        if (instancia == null) {
            instancia = new OficialDAO();
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

    public Oficial obtenerOficialPorUsuario(String usuario) {
        List<Oficial> oficiales = obtenerOficiales();
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

    public boolean guardarOficial(Oficial oficial, File imagen) throws IOException {
        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        oficial.setUsuario(usuario);
        oficial.setContrasena(contrasenaEncriptada);

        String nombreImagen = oficial.getIdentificacion() + "_"
                + System.currentTimeMillis()
                + imagen.getName().substring(imagen.getName().lastIndexOf("."));

        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        oficial.setRutaImagen(rutaImagenFinal);

        List<Oficial> oficiales = obtenerOficiales();
        oficiales.add(oficial);
        guardarListaOficiales(oficiales);

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
                RolEnum.OFICIAL,
                oficial.getRutaImagen() 
        );

        guardarUsuario(nuevoUsuario);

        boolean correoEnviado = EmailSender.getInstancia().enviarCredenciales(
                oficial.getCorreo(),
                usuario,
                contrasena,
                RolEnum.OFICIAL
        );

        if (correoEnviado) {
            JOptionPane.showMessageDialog(null,
                    "Oficial registrado exitosamente y credenciales enviadas al correo.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Oficial registrado pero hubo un error al enviar las credenciales por correo.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        return true;
    }

    public List<Oficial> obtenerOficiales() {
        List<Oficial> oficiales = new ArrayList<>();
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

                Type tipoLista = new TypeToken<List<Oficial>>() {
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

    private void guardarListaOficiales(List<Oficial> oficiales) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray oficialesArray = new JsonArray();

        for (Oficial oficial : oficiales) {
            JsonObject oficialJson = new JsonObject();
            oficialJson.addProperty("turno", oficial.getTurno());
            oficialJson.addProperty("fechaContratacion", oficial.getFechaContratacion().toString());
            oficialJson.addProperty("fechaFinContrato", oficial.getFechaFinContrato().toString());
            oficialJson.addProperty("rutaImagen", oficial.getRutaImagen());
            oficialJson.addProperty("correo", oficial.getCorreo());
            oficialJson.addProperty("primerNombre", oficial.getPrimerNombre());
            oficialJson.addProperty("segundoNombre", oficial.getSegundoNombre());
            oficialJson.addProperty("primerApellido", oficial.getPrimerApellido());
            oficialJson.addProperty("segundoApellido", oficial.getSegundoApellido());
            oficialJson.addProperty("edad", oficial.getEdad());
            oficialJson.addProperty("sexo", oficial.getSexo());
            oficialJson.addProperty("nacionalidad", oficial.getNacionalidad());
            oficialJson.addProperty("identificacion", oficial.getIdentificacion());

            oficialesArray.add(oficialJson);
        }

        jsonObject.add("oficiales", oficialesArray);

        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(jsonObject, writer);
        }
    }

    public boolean puedeAgregarOficial(String turno) {
        List<Oficial> oficiales = obtenerOficiales();

        // Límite total de 4 oficiales
        if (oficiales.size() >= 4) {
            return false;
        }

        // Límite de 2 por turno
        long countPorTurno = oficiales.stream()
                .filter(o -> o.getTurno().equalsIgnoreCase(turno))
                .count();

        return countPorTurno < 2;
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
            List<Oficial> oficiales = obtenerOficiales();
            Optional<Oficial> oficialAEliminar = oficiales.stream()
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

    public boolean modificarOficial(String cedulaOriginal, Oficial oficialModificado, File nuevaImagen) {
        try {
            List<Oficial> oficiales = obtenerOficiales();

            for (int i = 0; i < oficiales.size(); i++) {
                Oficial o = oficiales.get(i);
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

    public Oficial obtenerOficialPorCedula(String cedula) {
        return obtenerOficiales().stream()
                .filter(o -> o.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public Oficial obtenerOficialPorIdentificacion(String cedula) {
        return obtenerOficialPorCedula(cedula);
    }

    public List<Oficial> obtenerOficialPorTurno(String turno) {
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
