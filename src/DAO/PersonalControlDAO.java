package DAO;

import Controller.UsuarioController;
import Model.Entities.PersonalControl;
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

            File archivoJson = new File(RUTA_JSON);
            if (!archivoJson.exists()) {
                archivoJson.createNewFile();
                Files.write(Paths.get(RUTA_JSON), "[]".getBytes());
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


    public boolean guardarPersonalControl(PersonalControl personalControl, File imagen) throws IOException {
        if (personalControl == null) {
            throw new IllegalArgumentException("El objeto personalControl no puede ser null");
        }

        if (imagen == null || !imagen.exists() || imagen.length() == 0) {
            throw new IllegalArgumentException("La imagen proporcionada no es válida");
        }

        UsuarioController.Credenciales credenciales = UsuarioController.getInstancia().generarCredenciales();
        String usuario = credenciales.usuario;
        String contrasena = credenciales.contrasena;
        String contrasenaEncriptada = UsuarioController.getInstancia().encriptarContrasena(contrasena);

        personalControl.setUsuario(usuario);
        personalControl.setContrasena(contrasenaEncriptada);

        String extension = imagen.getName().substring(imagen.getName().lastIndexOf("."));
        String nombreImagen = personalControl.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;

        try {
            Files.createDirectories(Paths.get(RUTA_IMAGENES));
            Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
            personalControl.setRutaImagen(rutaImagenFinal);
        } catch (IOException e) {
            System.err.println("Error al guardar imagen: " + e.getMessage());
            throw new IOException("No se pudo guardar la imagen del personal de control");
        }

        List<PersonalControl> personalControlList = obtenerPersonalControl();

        if (personalControlList.stream().anyMatch(p -> p.getIdentificacion().equals(personalControl.getIdentificacion()))) {
            Files.deleteIfExists(Paths.get(rutaImagenFinal));
            throw new IllegalArgumentException("Ya existe un personal de control con esta cédula");
        }

        personalControlList.add(personalControl);

        try {
            guardarListaPersonales(personalControlList);

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
                    personalControl.getRutaImagen()
            );

            guardarUsuario(nuevoUsuario);

            try {
                EmailSender.getInstancia().enviarCredenciales(
                        personalControl.getCorreo(),
                        usuario,
                        contrasena,
                        RolEnum.PERSONAL_DE_CONTROL
                );
            } catch (Exception e) {
                System.err.println("Error al enviar correo: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Personal de Control registrado pero no se pudieron enviar las credenciales por correo",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            return true;
        } catch (IOException e) {
            Files.deleteIfExists(Paths.get(rutaImagenFinal));
            throw e;
        }
    }

    public List<PersonalControl> obtenerPersonalControl() {
        File archivo = new File(RUTA_JSON);

        try {
            if (!archivo.exists() || archivo.length() == 0) {
                return new ArrayList<>();
            }

            String contenido = new String(Files.readAllBytes(archivo.toPath()), StandardCharsets.UTF_8).trim();

            if (contenido.isEmpty() || contenido.equals("[]")) {
                return new ArrayList<>();
            }

            if (contenido.startsWith("[")) {
                Type tipoLista = new TypeToken<List<PersonalControl>>() {
                }.getType();
                return gson.fromJson(contenido, tipoLista);
            } 
            else if (contenido.startsWith("{")) {
                JsonObject jsonObject = JsonParser.parseString(contenido).getAsJsonObject();
                if (jsonObject.has("Personales")) {
                    JsonArray array = jsonObject.getAsJsonArray("Personales");
                    Type tipoLista = new TypeToken<List<PersonalControl>>() {
                    }.getType();
                    return gson.fromJson(array, tipoLista);
                }
            }

            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al leer personal de control: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void guardarListaPersonales(List<PersonalControl> personales) throws IOException {
        if (personales == null) {
            throw new IllegalArgumentException("La lista de oficiales no puede ser null");
        }

        JsonArray jsonArrayOficiales = new JsonArray();

        for (PersonalControl personalesControl : personales) {
            JsonObject jsonOficial = new JsonObject();

            jsonOficial.addProperty("primerNombre", personalesControl.getPrimerNombre());
            jsonOficial.addProperty("segundoNombre", personalesControl.getSegundoNombre());
            jsonOficial.addProperty("primerApellido", personalesControl.getPrimerApellido());
            jsonOficial.addProperty("segundoApellido", personalesControl.getSegundoApellido());
            jsonOficial.addProperty("edad", personalesControl.getEdad());
            jsonOficial.addProperty("sexo", personalesControl.getSexo());
            jsonOficial.addProperty("nacionalidad", personalesControl.getNacionalidad());
            jsonOficial.addProperty("identificacion", personalesControl.getIdentificacion());
            jsonOficial.addProperty("correo", personalesControl.getCorreo());
            jsonOficial.addProperty("turno", personalesControl.getTurno());
            jsonOficial.addProperty("fechaContratacion", personalesControl.getFechaContratacion().toString());
            jsonOficial.addProperty("fechaFinContrato", personalesControl.getFechaFinContrato().toString());
            jsonOficial.addProperty("rutaImagen", personalesControl.getRutaImagen());
            jsonOficial.addProperty("usuario", personalesControl.getUsuario());

            String contrasenaEncriptada = GeneradorCredenciales.encriptarContrasena(personalesControl.getContrasena());
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
                guardarListaPersonales(personalControlList);
                return true;
            }
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el personal de control: " + e.getMessage(),
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

    public boolean modificarPersonalControl(String cedulaOriginal, PersonalControl personalControlModificado, File nuevaImagen) {
        try {
            List<PersonalControl> personalControlList = obtenerPersonalControl();

            for (int i = 0; i < personalControlList.size(); i++) {
                PersonalControl p = personalControlList.get(i);
                if (p.getIdentificacion().equals(cedulaOriginal)) {
                    String rutaImagenFinal = p.getRutaImagen();

                    if (nuevaImagen != null && nuevaImagen.exists()) {
                        String extension = nuevaImagen.getName().substring(nuevaImagen.getName().lastIndexOf("."));
                        String nombreImagen = personalControlModificado.getIdentificacion() + "_" + System.currentTimeMillis() + extension;
                        rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
                        Files.copy(nuevaImagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
                    }

                    personalControlModificado.setRutaImagen(rutaImagenFinal);
                    personalControlList.set(i, personalControlModificado);

                    guardarListaPersonales(personalControlList);

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
}
