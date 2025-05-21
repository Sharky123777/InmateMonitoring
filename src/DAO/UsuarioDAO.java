package DAO;

import Model.Constants.RolEnum;
import Model.Entities.Usuario;
import Utilidades.EmailSender;
import Utilidades.GeneradorCredenciales;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;

public class UsuarioDAO {

    private static final String JSON_FILE = "src/Resources/DATA/usuarios.json";
    private static UsuarioDAO instancia;
    private final Gson gson;

    private UsuarioDAO() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }

    public static synchronized UsuarioDAO getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioDAO();
        }
        return instancia;
    }

    public List<Usuario> obtenerTodosUsuarios() throws IOException {
    File archivo = new File(JSON_FILE);

    if (!archivo.exists() || archivo.length() == 0) {
        return new ArrayList<>();
    }

    try (Reader reader = new FileReader(archivo)) {
        Type tipoLista = new TypeToken<List<Usuario>>(){}.getType();
        return gson.fromJson(reader, tipoLista);
    } catch (JsonSyntaxException | JsonIOException e) {
        System.err.println("Error al leer usuarios: " + e.getMessage());
        return new ArrayList<>();
    }
}

    private void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(usuarios, writer);
        }
    }

    public Usuario obtenerUsuarioPorUsername(String username) throws IOException {
        return obtenerTodosUsuarios().stream()
            .filter(u -> u.getUsuario().equals(username))
            .findFirst()
            .orElse(null);
    }

    public Usuario validarCredenciales(String usuario, String password, RolEnum rol) {
        try {
            return obtenerTodosUsuarios().stream()
                .filter(u -> u.getUsuario().equals(usuario) && u.getRol() == rol)
                .filter(u -> GeneradorCredenciales.verificarContrasena(password, u.getPassword()))
                .findFirst()
                .orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean agregarUsuario(Usuario nuevoUsuario) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();

        boolean existe = usuarios.stream()
            .anyMatch(u -> u.getUsuario().equals(nuevoUsuario.getUsuario()) || 
                          u.getIdentificacion().equals(nuevoUsuario.getIdentificacion()));

        if (existe) return false;

        usuarios.add(nuevoUsuario);
        guardarUsuarios(usuarios);
        return true;
    }

    public boolean modificarCredenciales(String usuarioOriginal, String nuevoUsuario, String nuevaContra, String correo, RolEnum rol) {
        try {
            List<Usuario> usuarios = obtenerTodosUsuarios();

            for (Usuario u : usuarios) {
                if (u.getUsuario().equals(usuarioOriginal)) {
                    if (nuevoUsuario != null && !nuevoUsuario.isEmpty()) {
                        boolean existeUsuario = usuarios.stream()
                            .anyMatch(user -> user.getUsuario().equals(nuevoUsuario) && !user.getUsuario().equals(usuarioOriginal));
                        if (existeUsuario) {
                            throw new IllegalArgumentException("El nombre de usuario ya existe");
                        }
                        u.setUsuario(nuevoUsuario);
                    }

                    if (nuevaContra != null && !nuevaContra.isEmpty()) {
                        String contraEncriptada = GeneradorCredenciales.encriptarContrasena(nuevaContra);
                        u.setPassword(contraEncriptada);
                    }

                    guardarUsuarios(usuarios);

                    if (correo != null && !correo.isEmpty() && rol != null) {
                        EmailSender.getInstancia().enviarCredencialesActualizadas(
                            correo,
                            nuevoUsuario != null && !nuevoUsuario.isEmpty() ? nuevoUsuario : usuarioOriginal,
                            nuevaContra != null && !nuevaContra.isEmpty() ? nuevaContra : "*** No modificada ***",
                            rol
                        );
                    }

                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar credenciales: " + e.getMessage(), e);
        }
    }

    public Usuario obtenerUsuarioPorIdentificacion(String identificacion) throws IOException {
        return obtenerTodosUsuarios().stream()
            .filter(u -> u.getIdentificacion().equals(identificacion))
            .findFirst()
            .orElse(null);
    }

    public boolean esDirector(Usuario usuario) {
        return usuario != null && usuario.getRol() == RolEnum.DIRECTOR;
    }

    public boolean eliminarUsuario(String username) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        boolean removed = usuarios.removeIf(u -> u.getUsuario().equals(username));
        if (removed) {
            guardarUsuarios(usuarios);
        }
        return removed;
    }
}
