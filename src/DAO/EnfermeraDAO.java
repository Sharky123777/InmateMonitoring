package DAO;

import Controller.UsuarioController;
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

    private static final String RUTA_JSON = "src/Resources/DATA/enfermera.json";
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

    private void guardarUsuario(Usuario usuario) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        usuarios.removeIf(u -> u.getUsuario().equals(usuario.getUsuario()));
        usuarios.add(usuario);

        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }

    public Enfermera obtenerEnfermeraPorUsuario(String usuario) {
        List<Enfermera> enfermeras = obtenerEnfermeras();
        return enfermeras.stream()
                .filter(e -> e.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    private List<Usuario> obtenerTodosUsuarios() throws IOException {
        File archivo = new File(RUTA_USUARIOS);

        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {
            JsonArray usuariosArray = JsonParser.parseReader(reader).getAsJsonArray();

            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            return gson.fromJson(usuariosArray, tipoLista);
        }
    }

    private void eliminarUsuario(String usuario) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        usuarios.removeIf(u -> u.getUsuario().equals(usuario));

        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }

    public boolean guardarEnfermera(Enfermera enfermera, File imagen) throws IOException {

        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        enfermera.setUsuario(usuario);
        enfermera.setContrasena(contrasenaEncriptada);

        String nombreImagen = enfermera.getIdentificacion() + "_"
                + System.currentTimeMillis()
                + imagen.getName().substring(imagen.getName().lastIndexOf("."));

        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        enfermera.setRutaImagen(rutaImagenFinal);

        List<Enfermera> enfermeras = obtenerEnfermeras();
        enfermeras.add(enfermera);
        guardarListaEnfermeras(enfermeras);

        Usuario nuevoUsuario = new Usuario(
                enfermera.getPrimerNombre(),
                enfermera.getSegundoNombre(),
                enfermera.getPrimerApellido(),
                enfermera.getSegundoApellido(),
                enfermera.getEdad(),
                enfermera.getSexo(),
                enfermera.getNacionalidad(),
                enfermera.getIdentificacion(),
                usuario,
                contrasenaEncriptada,
                RolEnum.ENFERMERA,
                enfermera.getRutaImagen()
        );

        guardarUsuario(nuevoUsuario);

        boolean correoEnviado = EmailSender.getInstancia().enviarCredenciales(
                enfermera.getCorreo(),
                usuario,
                contrasena,
                RolEnum.ENFERMERA
        );

        if (!correoEnviado) {
            JOptionPane.showMessageDialog(null,
                    "Enfermera registrada pero hubo un error al enviar las credenciales por correo.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Enfermera contratada correctamente.\nRevise su correo para conocer sus credenciales de acceso",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }

        return true;
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

                Type tipoLista = new TypeToken<List<Enfermera>>() {
                }.getType();
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

        if (enfermeras.size() >= 4) {
            return false;
        }

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

                eliminarUsuario(enfermeraAEliminar.get().getUsuario());

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

                    String rutaImagenFinal = e.getRutaImagen();

                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String nombreImagen = enfermeraModificada.getIdentificacion() + "_" + System.currentTimeMillis()
                                + nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    }

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
            e.getFechaContratacionFormateada(),
            e.getFechaFinContratoFormateada()
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
