package DAO;

import Controller.UsuarioController;
import Model.Entities.OficialDeRegistro;
import Model.Constants.RolEnum;
import Utilidades.EmailSender;
import Model.Entities.Usuario;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import DAO.LocalDateAdapter;
import Utilidades.GeneradorCredenciales;
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

            File archivoJson = new File(RUTA_JSON);
            if (!archivoJson.exists()) {
                archivoJson.createNewFile();
                Files.write(Paths.get(RUTA_JSON), "{\"oficiales\":[]}".getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorios/archivo: " + e.getMessage());
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


    public boolean guardarOficial(OficialDeRegistro oficial, File imagen) throws IOException {
        if (oficial == null) {
            throw new IllegalArgumentException("El objeto oficial no puede ser null");
        }

        if (imagen == null || !imagen.exists() || imagen.length() == 0) {
            throw new IllegalArgumentException("La imagen proporcionada no es válida");
        }

        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        oficial.setUsuario(usuario);
        oficial.setContrasena(contrasenaEncriptada);

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

        List<OficialDeRegistro> oficiales = obtenerOficiales();

        if (oficiales.stream().anyMatch(o -> o.getIdentificacion().equals(oficial.getIdentificacion()))) {
            Files.deleteIfExists(Paths.get(rutaImagenFinal));
            throw new IllegalArgumentException("Ya existe un oficial con esta cédula");
        }

        oficiales.add(oficial);

        try {
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
                    RolEnum.OFICIAL_DE_REGISTRO,
                    oficial.getRutaImagen()
            );

            guardarUsuario(nuevoUsuario);

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

            Files.deleteIfExists(Paths.get(rutaImagenFinal));
            throw e;
        }
    }

    private void guardarListaOficiales(List<OficialDeRegistro> oficiales) throws IOException {
        if (oficiales == null) {
            throw new IllegalArgumentException("La lista de oficiales no puede ser null");
        }

        JsonArray jsonArrayOficiales = new JsonArray();

        for (OficialDeRegistro oficial : oficiales) {
            JsonObject jsonOficial = new JsonObject();

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

            String contrasenaEncriptada = GeneradorCredenciales.encriptarContrasena(oficial.getContrasena());
            jsonOficial.addProperty("contrasena", contrasenaEncriptada);

            jsonArrayOficiales.add(jsonOficial);

        }

        Path path = Paths.get(RUTA_JSON);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(jsonArrayOficiales, writer);
            System.out.println("Datos guardados como array correctamente en: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al escribir JSON como array: " + e.getMessage());
            throw e;
        }
    }

    public List<OficialDeRegistro> obtenerOficiales() {
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                return new ArrayList<>();
            }

            String contenido = Files.readString(archivo.toPath()).trim();

            if (contenido.isEmpty() || contenido.equals("[]")) {
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<OficialDeRegistro>>() {
            }.getType();
            return gson.fromJson(contenido, tipoLista);

        } catch (Exception e) {
            System.err.println("Error al leer oficiales como array: " + e.getMessage());
            return new ArrayList<>();
        }
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

    public boolean modificarOficial(String cedulaOriginal, OficialDeRegistro oficialModificado, File nuevaImagen) {
        try {
            List<OficialDeRegistro> oficiales = obtenerOficiales();

            for (int i = 0; i < oficiales.size(); i++) {
                OficialDeRegistro o = oficiales.get(i);
                if (o.getIdentificacion().equals(cedulaOriginal)) {

                    String rutaImagenFinal = o.getRutaImagen();
                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String extension = nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        String nombreImagen = oficialModificado.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
                        rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    }

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
}
