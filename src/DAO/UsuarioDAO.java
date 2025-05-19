package DAO;

import Model.Constants.RolEnum;
import Model.Entities.Usuario;
import Utilidades.GeneradorCredenciales;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class UsuarioDAO {

    private static final String JSON_FILE = "src/Resources/DATA/usuarios.json";
    private static UsuarioDAO instancia;
    private final Gson gson;

    private UsuarioDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static synchronized UsuarioDAO getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioDAO();
        }
        return instancia;
    }

    public Usuario obtenerUsuarioPorUsername(String username) throws IOException {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        return usuarios.stream()
                .filter(u -> u.getUsuario().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Usuario validarCredenciales(String usuario, String password, RolEnum rol) {
        try {
            List<Usuario> usuarios = obtenerTodosUsuarios();
            for (Usuario u : usuarios) {
                if (u.getUsuario().equals(usuario) && u.getRol() == rol) {
                    
                    boolean contraseñaEncriptada = u.getPassword().length() == 64;

                    if (contraseñaEncriptada) {
                      
                        if (GeneradorCredenciales.verificarContrasena(password, u.getPassword())) {
                            return u;
                        }
                    } else {
                       
                        
                        if (u.getPassword().equals(password)) {
                            return u;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean agregarUsuario(Usuario nuevoUsuario) {
        try {
            
            List<Usuario> usuarios = obtenerTodosUsuarios();

           
            for (Usuario u : usuarios) {
                if (u.getUsuario().equals(nuevoUsuario.getUsuario())
                        || u.getIdentificacion().equals(nuevoUsuario.getIdentificacion())) {
                    return false;
                }
            }

           
            usuarios.add(nuevoUsuario);

            
            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                JsonObject root = new JsonObject();
                JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
                root.add("usuarios", usuariosArray);
                gson.toJson(root, writer);
            }

            return true;
        } catch (Exception e) {
          
            return false;
        }
    }

    private Usuario crearUsuarioDesdeJson(JsonObject json) {
        try {
            String primerNombre = getStringSafe(json, "primerNombre");
            String segundoNombre = getStringSafe(json, "segundoNombre");
            String primerApellido = getStringSafe(json, "primerApellido");
            String segundoApellido = getStringSafe(json, "segundoApellido");
            int edad = json.has("edad") ? json.get("edad").getAsInt() : 0;
            String sexo = getStringSafe(json, "sexo");
            String nacionalidad = getStringSafe(json, "nacionalidad");
            String identificacion = getStringSafe(json, "identificacion");
            String usuario = getStringSafe(json, "usuario");
            String password = getStringSafe(json, "password");
            RolEnum rol = RolEnum.valueOf(getStringSafe(json, "rol"));
            String rutaImagen = getStringSafe(json, "rutaImagen"); // Nuevo campo

            return new Usuario(
                    primerNombre, segundoNombre,
                    primerApellido, segundoApellido,
                    edad, sexo, nacionalidad, identificacion,
                    usuario, password, rol,
                    rutaImagen 
            );
        } catch (Exception e) {
            System.err.println("Error al crear usuario desde JSON: " + e.getMessage());
            return null;
        }
    }

    
    private String getStringSafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : null;
    }

    public boolean esDirector(Usuario usuario) {
        return usuario != null && usuario.getRol() == RolEnum.DIRECTOR;
    }

    public List<Usuario> obtenerTodosUsuarios() throws IOException {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {
            // Verificar si el archivo está vacío
            if (archivo.length() == 0) {
                return new ArrayList<>();
            }

            JsonElement jsonElement = JsonParser.parseReader(reader);

            
            if (!jsonElement.isJsonObject()) {
                return new ArrayList<>();
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");

            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            return gson.fromJson(usuariosArray, tipoLista);
        } catch (JsonSyntaxException e) {
            
            return new ArrayList<>();
        }
    }
    
    public boolean modificarCredenciales(String usuarioOriginal, String nuevoUsuario, String nuevaContrasena) throws IOException {
    List<Usuario> usuarios = obtenerTodosUsuarios();
    boolean modificado = false;

    for (Usuario usuario : usuarios) {
        if (usuario.getUsuario().equals(usuarioOriginal)) {
            if (nuevoUsuario != null && !nuevoUsuario.isEmpty()) {
                if (usuarios.stream().anyMatch(u -> u.getUsuario().equals(nuevoUsuario) && !nuevoUsuario.equals(usuarioOriginal))) {
                    throw new IllegalArgumentException("El nombre de usuario ya está en uso");
                }
                usuario.setUsuario(nuevoUsuario);
            }
            
            if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
                String contrasenaEncriptada = GeneradorCredenciales.encriptarContrasena(nuevaContrasena);
                usuario.setPassword(contrasenaEncriptada);
            }
            
            modificado = true;
            break;
        }
    }

    if (modificado) {
        guardarUsuarios(usuarios);
        return true;
    }
    
    return false;
}

private void guardarUsuarios(List<Usuario> usuarios) throws IOException {
    try (FileWriter writer = new FileWriter(JSON_FILE)) {
        JsonObject root = new JsonObject();
        JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
        root.add("usuarios", usuariosArray);
        gson.toJson(root, writer);
    }
}

public Usuario obtenerUsuarioPorIdentificacion(String identificacion) {
    try {
        List<Usuario> usuarios = obtenerTodosUsuarios();
        for (Usuario u : usuarios) {
            if (u.getIdentificacion().equals(identificacion)) {
                return u;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}


}
