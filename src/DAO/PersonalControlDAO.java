package DAO;

import Controller.UsuarioController;
import Model.Entities.PersonalControl;
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
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class PersonalControlDAO {

    private static final String RUTA_JSON = "src/Resources/DATA/personalDeControl.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_personal_control/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private final Gson gson;
    private static PersonalControlDAO instancia;

    public PersonalControlDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        crearDirectoriosSiNoExisten();
    }

    public static synchronized PersonalControlDAO getInstancia() {
        if (instancia == null) {
            instancia = new PersonalControlDAO();
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
            return date != null ? new JsonPrimitive(date.format(formatter)) : JsonNull.INSTANCE;
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json == null || json.isJsonNull() || json.getAsString().isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(json.getAsString(), formatter);
            } catch (DateTimeParseException e) {
                throw new JsonParseException("Formato de fecha inválido: " + json.getAsString(), e);
            }
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

    public PersonalControl obtenerPersonalControlPorUsuario(String usuario) {
        List<PersonalControl> personalControl = obtenerPersonalControl();
        return personalControl.stream()
                .filter(p -> p.getUsuario().equals(usuario))
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

    public boolean guardarPersonalControl(PersonalControl personalControl, File imagen) throws IOException {
        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        personalControl.setUsuario(usuario);
        personalControl.setContrasena(contrasenaEncriptada);

        String nombreImagen = personalControl.getIdentificacion() + "_"
                + System.currentTimeMillis()
                + imagen.getName().substring(imagen.getName().lastIndexOf("."));

        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        personalControl.setRutaImagen(rutaImagenFinal);

        List<PersonalControl> personalControlList = obtenerPersonalControl();
        personalControlList.add(personalControl);
        guardarListaPersonalControl(personalControlList);

        Usuario nuevoUsuario = new Usuario(
                personalControl.getPrimerNombre(),
                personalControl.getSegundoNombre(),
                personalControl.getPrimerApellido(),
                personalControl.getSegundoApellido(),
                personalControl.getEdad(),
                personalControl.getSexo(),
                personalControl.getNacionalidad(),
                personalControl.getIdentificacion(),
                usuario,
                contrasenaEncriptada,
                RolEnum.PERSONAL_DE_CONTROL,
                personalControl.getRutaImagen() // Usamos getRutaImagen()
        );

        guardarUsuario(nuevoUsuario);

        boolean correoEnviado = EmailSender.getInstancia().enviarCredenciales(
                personalControl.getCorreo(),
                usuario,
                contrasena,
                RolEnum.PERSONAL_DE_CONTROL
        );

        if (correoEnviado) {
            JOptionPane.showMessageDialog(null,
                    "Personal de Control registrado exitosamente y credenciales enviadas al correo.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Personal de Control registrado pero hubo un error al enviar las credenciales por correo.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        return true;
    }

    public List<PersonalControl> obtenerPersonalControl() {
        List<PersonalControl> personalControlList = new ArrayList<>();
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                guardarListaPersonalControl(new ArrayList<>());
                return personalControlList;
            }

            String contenido = new String(Files.readAllBytes(archivo.toPath()));

            if (contenido.trim().isEmpty()) {
                guardarListaPersonalControl(new ArrayList<>());
                return personalControlList;
            }

            try {
                JsonObject jsonObject = JsonParser.parseString(contenido).getAsJsonObject();
                JsonArray personalControlArray = jsonObject.getAsJsonArray("personal_control");

                Type tipoLista = new TypeToken<List<PersonalControl>>() {
                }.getType();
                return gson.fromJson(personalControlArray, tipoLista);
            } catch (JsonSyntaxException e) {
                System.err.println("Formato JSON inválido. Creando nuevo archivo.");
                guardarListaPersonalControl(new ArrayList<>());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al leer/escribir archivo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return personalControlList;
    }

    private void guardarListaPersonalControl(List<PersonalControl> personalControlList) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray personalControlArray = new JsonArray();

        for (PersonalControl personalControl : personalControlList) {
            JsonObject personalControlJson = new JsonObject();
            personalControlJson.addProperty("turno", personalControl.getTurno());
            personalControlJson.addProperty("fechaContratacion", personalControl.getFechaContratacion().toString());
            personalControlJson.addProperty("fechaFinContrato", personalControl.getFechaFinContrato().toString());
            personalControlJson.addProperty("rutaImagen", personalControl.getRutaImagen());
            personalControlJson.addProperty("correo", personalControl.getCorreo());
            personalControlJson.addProperty("primerNombre", personalControl.getPrimerNombre());
            personalControlJson.addProperty("segundoNombre", personalControl.getSegundoNombre());
            personalControlJson.addProperty("primerApellido", personalControl.getPrimerApellido());
            personalControlJson.addProperty("segundoApellido", personalControl.getSegundoApellido());
            personalControlJson.addProperty("edad", personalControl.getEdad());
            personalControlJson.addProperty("sexo", personalControl.getSexo());
            personalControlJson.addProperty("nacionalidad", personalControl.getNacionalidad());
            personalControlJson.addProperty("identificacion", personalControl.getIdentificacion());

            personalControlArray.add(personalControlJson);
        }

        jsonObject.add("personal_control", personalControlArray);

        try (Writer writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(jsonObject, writer);
        }
    }

    public boolean puedeAgregarPersonalControl(String turno) {
        List<PersonalControl> personalControlList = obtenerPersonalControl();

        // Límite total de 4 personal de control
        if (personalControlList.size() >= 4) {
            return false;
        }

        // Límite de 2 por turno
        long countPorTurno = personalControlList.stream()
                .filter(p -> p.getTurno().equalsIgnoreCase(turno))
                .count();

        return countPorTurno < 2;
    }

    public boolean existePersonalControlConCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return obtenerPersonalControl().stream()
                .anyMatch(p -> p.getIdentificacion() != null && p.getIdentificacion().equals(cedula));
    }

    public boolean eliminarPersonalControl(String cedula) {
        try {
            List<PersonalControl> personalControlList = obtenerPersonalControl();
            Optional<PersonalControl> personalControlAEliminar = personalControlList.stream()
                    .filter(p -> p.getIdentificacion().equals(cedula))
                    .findFirst();

            if (personalControlAEliminar.isPresent()) {
                eliminarUsuario(personalControlAEliminar.get().getUsuario());
                personalControlList.removeIf(p -> p.getIdentificacion().equals(cedula));
                guardarListaPersonalControl(personalControlList);
                return true;
            }
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el personal de control: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean modificarPersonalControl(String cedulaOriginal, PersonalControl personalControlModificado, File nuevaImagen) {
        try {
            List<PersonalControl> personalControlList = obtenerPersonalControl();

            for (int i = 0; i < personalControlList.size(); i++) {
                PersonalControl p = personalControlList.get(i);
                if (p.getIdentificacion().equals(cedulaOriginal)) {
                    String rutaImagenFinal = p.getRutaImagen();

                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String nombreImagen = personalControlModificado.getIdentificacion() + "_" + System.currentTimeMillis()
                                + nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    }

                    personalControlModificado.setUsuario(p.getUsuario());
                    personalControlModificado.setContrasena(p.getContrasena());
                    personalControlModificado.setRutaImagen(rutaImagenFinal);

                    personalControlList.set(i, personalControlModificado);

                    guardarListaPersonalControl(personalControlList);
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar personal de control: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public PersonalControl obtenerPersonalControlPorCedula(String cedula) {
        return obtenerPersonalControl().stream()
                .filter(p -> p.getIdentificacion().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public PersonalControl obtenerPersonalControlPorIdentificacion(String cedula) {
        return obtenerPersonalControlPorCedula(cedula);
    }

    public List<PersonalControl> obtenerPersonalControlPorTurno(String turno) {
        return obtenerPersonalControl().stream()
                .filter(p -> p.getTurno().equalsIgnoreCase(turno))
                .collect(Collectors.toList());
    }

    public List<Object[]> obtenerDatosPersonalControlParaTabla() {
        return obtenerPersonalControl().stream()
                .map(p -> new Object[]{
            p.getPrimerNombre(),
            p.getSegundoNombre(),
            p.getPrimerApellido(),
            p.getSegundoApellido(),
            p.getEdad(),
            p.getIdentificacion(),
            p.getNacionalidad(),
            p.getCorreo(),
            p.getTurno(),
            p.getFechaContratacionFormateada(),
            p.getFechaFinContratoFormateada()
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
