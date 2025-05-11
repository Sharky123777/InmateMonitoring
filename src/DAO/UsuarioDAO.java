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
    
    public Usuario validarCredenciales(String usuario, String password, RolEnum rolSeleccionado) {
    try (FileReader reader = new FileReader(JSON_FILE)) {
        JsonElement jsonElement = JsonParser.parseReader(reader);

        if (jsonElement == null || !jsonElement.isJsonObject()) {
            System.err.println("El archivo JSON no contiene un objeto válido");
            return null;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray usuariosJson = jsonObject.getAsJsonArray("usuarios");

        if (usuariosJson == null) {
            System.err.println("No se encontró el array 'usuarios' en el JSON");
            return null;
        }

        for (JsonElement element : usuariosJson) {
            if (!element.isJsonObject()) {
                continue;
            }

            JsonObject userJson = element.getAsJsonObject();

            // Verificar credenciales básicas primero
            String jsonUser = getStringSafe(userJson, "usuario");
            String jsonPass = getStringSafe(userJson, "password");
            String jsonRol = getStringSafe(userJson, "rol");

            if (jsonUser == null || jsonPass == null || jsonRol == null) {
                continue;
            }

            // Verificar usuario y rol primero (más rápido que verificar contraseña)
            if (jsonUser.equals(usuario) && RolEnum.valueOf(jsonRol) == rolSeleccionado) {
                // Si el usuario y rol coinciden, verificar contraseña
                if (GeneradorCredenciales.verificarContrasena(password, jsonPass)) {
                    // Si las credenciales son válidas, crear el objeto Usuario completo
                    return crearUsuarioDesdeJson(userJson);
                }
            }
        }
    } catch (Exception e) {
        System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}
    
    public boolean agregarUsuario(Usuario nuevoUsuario) {
    try {
        // Leer usuarios existentes
        List<Usuario> usuarios = obtenerTodosUsuarios();
        
        // Verificar si el usuario ya existe
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(nuevoUsuario.getUsuario()) || 
                u.getIdentificacion().equals(nuevoUsuario.getIdentificacion())) {
                return false;
            }
        }
        
        // Agregar el nuevo usuario
        usuarios.add(nuevoUsuario);
        
        // Escribir de vuelta al archivo
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            JsonObject root = new JsonObject();
            JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
            root.add("usuarios", usuariosArray);
            gson.toJson(root, writer);
        }
        
        return true;
    } catch (Exception e) {
        System.err.println("Error al agregar usuario: " + e.getMessage());
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

            return new Usuario(
                    primerNombre, segundoNombre,
                    primerApellido, segundoApellido,
                    edad, sexo, nacionalidad, identificacion,
                    usuario, password, rol
            );
        } catch (Exception e) {
            System.err.println("Error al crear usuario desde JSON: " + e.getMessage());
            return null;
        }
    }

    // Método auxiliar para obtener valores seguros del JSON
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

            // Si el archivo no es un objeto JSON válido
            if (!jsonElement.isJsonObject()) {
                return new ArrayList<>();
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");

            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            return gson.fromJson(usuariosArray, tipoLista);
        } catch (JsonSyntaxException e) {
            // Si hay un error de sintaxis en el JSON, devolver lista vacía
            return new ArrayList<>();
        }
    }

}
