package DAO;

import Controller.UsuarioController;
import Model.Entities.CoordinadorDeActividades;
import Model.Constants.RolEnum;
import Utilidades.EmailSender;
import Model.Entities.Usuario;
import Utilidades.GeneradorCredenciales;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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

public class CoordinadoraDeActividadesDAO {

    private static final String RUTA_JSON = "src/Resources/DATA/CDA.json";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_CDA/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private final Gson gson;
    private static CoordinadoraDeActividadesDAO instancia;

    public CoordinadoraDeActividadesDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        crearDirectoriosSiNoExisten();
    }

    public static synchronized CoordinadoraDeActividadesDAO getInstancia() {
        if (instancia == null) {
            instancia = new CoordinadoraDeActividadesDAO();
        }
        return instancia;
    }

    private void crearDirectoriosSiNoExisten() {
        try {
            Files.createDirectories(Paths.get(RUTA_IMAGENES));
            Files.createDirectories(Paths.get(RUTA_JSON).getParent());
            Files.createDirectories(Paths.get(RUTA_USUARIOS).getParent());
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

    public List<Usuario> obtenerTodosUsuarios() throws IOException {
        File archivo = new File(RUTA_USUARIOS);

        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        } catch (JsonSyntaxException | JsonIOException e) {
            System.err.println("Error al leer usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void eliminarUsuario(String usuario) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        usuarios.removeIf(u -> u.getUsuario().equals(usuario));

        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }

    public CoordinadorDeActividades obtenerCoordinadorPorUsuario(String usuario) {
        List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();
        return coordinadores.stream()
                .filter(c -> c.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    public boolean guardarCoordinador(CoordinadorDeActividades coordinador, File imagen) throws IOException {

        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        coordinador.setUsuario(usuario);
        coordinador.setContrasena(contrasenaEncriptada);

        String nombreImagen = coordinador.getIdentificacion() + "_"
                + System.currentTimeMillis()
                + imagen.getName().substring(imagen.getName().lastIndexOf("."));

        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        coordinador.setRutaImagen(rutaImagenFinal);

        List<CoordinadorDeActividades> coordinadores = obtenerCoordinadores();
        coordinadores.add(coordinador);
        guardarListaCoordinadores(coordinadores);

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
                RolEnum.COORDINADOR_DE_ACTIVIDADES,
                coordinador.getRutaImagen()
        );

        guardarUsuario(nuevoUsuario);

        return EmailSender.getInstancia().enviarCredenciales(
                coordinador.getCorreo(),
                usuario,
                contrasena,
                RolEnum.COORDINADOR_DE_ACTIVIDADES
        );
    }

    public List<CoordinadorDeActividades> obtenerCoordinadores() {
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                return new ArrayList<>();
            }

            String contenido = Files.readString(archivo.toPath()).trim();

            if (contenido.isEmpty() || contenido.equals("[]")) {
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<CoordinadorDeActividades>>() {}.getType();
            return gson.fromJson(contenido, tipoLista);

        } catch (Exception e) {
            System.err.println("Error al leer coordinadores como array: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarListaCoordinadores(List<CoordinadorDeActividades> coordinadores) throws IOException {
        if (coordinadores == null) {
            throw new IllegalArgumentException("La lista de coordinadores no puede ser null");
        }

        JsonArray jsonArrayCoordinadores = new JsonArray();

        for (CoordinadorDeActividades coordinador : coordinadores) {
            JsonObject jsonCoordinador = new JsonObject();

            jsonCoordinador.addProperty("primerNombre", coordinador.getPrimerNombre());
            jsonCoordinador.addProperty("segundoNombre", coordinador.getSegundoNombre());
            jsonCoordinador.addProperty("primerApellido", coordinador.getPrimerApellido());
            jsonCoordinador.addProperty("segundoApellido", coordinador.getSegundoApellido());
            jsonCoordinador.addProperty("edad", coordinador.getEdad());
            jsonCoordinador.addProperty("sexo", coordinador.getSexo());
            jsonCoordinador.addProperty("nacionalidad", coordinador.getNacionalidad());
            jsonCoordinador.addProperty("identificacion", coordinador.getIdentificacion());

            jsonCoordinador.addProperty("correo", coordinador.getCorreo());
            jsonCoordinador.addProperty("turno", coordinador.getTurno());
            jsonCoordinador.addProperty("cargo", coordinador.getCargo());
            jsonCoordinador.addProperty("fechaInicioContrato", coordinador.getFechaInicioContrato().toString());
            jsonCoordinador.addProperty("fechaFinContrato", coordinador.getFechaFinContrato().toString());
            jsonCoordinador.addProperty("rutaImagen", coordinador.getRutaImagen());
            jsonCoordinador.addProperty("usuario", coordinador.getUsuario());

            String contrasenaEncriptada = GeneradorCredenciales.encriptarContrasena(coordinador.getContrasena());
            jsonCoordinador.addProperty("contrasena", contrasenaEncriptada);

            jsonArrayCoordinadores.add(jsonCoordinador);
        }

        Path path = Paths.get(RUTA_JSON);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(jsonArrayCoordinadores, writer);
            System.out.println("Datos de coordinadores guardados como array correctamente en: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al escribir JSON de coordinadores como array: " + e.getMessage());
            throw e;
        }
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
            String.class,
            String.class
        };
    }
}
