package DAO;

import Model.Rol;
import Model.Usuario;
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
    
   public Usuario validarCredenciales(String usuario, String password, Rol rolSeleccionado) {
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
            if (!element.isJsonObject()) continue;
            
            JsonObject userJson = element.getAsJsonObject();
            
            // Verificación robusta de campos
            String jsonUser = userJson.has("usuario") ? userJson.get("usuario").getAsString() : null;
            String jsonPass = userJson.has("password") ? userJson.get("password").getAsString() : null;
            String jsonRol = userJson.has("rol") ? userJson.get("rol").getAsString() : null;
            
            if (jsonUser == null || jsonPass == null || jsonRol == null) {
                System.err.println("Usuario en JSON con campos faltantes");
                continue;
            }
            
            // Depuración: Imprime los valores que se están comparando
            System.out.println("Comparando: " + jsonUser + "==" + usuario + ", " + 
                              jsonPass + "==" + password + ", " + 
                              jsonRol + "==" + rolSeleccionado.name());
            
            try {
                Rol rol = Rol.valueOf(jsonRol);
                if (jsonUser.equals(usuario) &&  // Cambiado de equalsIgnoreCase a equals
                    jsonPass.equals(password) && 
                    rol == rolSeleccionado) {
                    return new Usuario(jsonUser, jsonPass, rol);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Rol no válido en JSON: " + jsonRol);
            }
        }
    } catch (Exception e) {
        System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}
    
    // Método auxiliar para obtener valores seguros del JSON
private String getStringSafe(JsonObject jsonObject, String key) {
    JsonElement element = jsonObject.get(key);
    return (element != null && !element.isJsonNull()) ? element.getAsString() : null;
}
    
    public boolean esDirector(Usuario usuario) {
        return usuario != null && usuario.getRol() == Rol.DIRECTOR;
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
        
        Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
        return gson.fromJson(usuariosArray, tipoLista);
    } catch (JsonSyntaxException e) {
        // Si hay un error de sintaxis en el JSON, devolver lista vacía
        return new ArrayList<>();
    }
}
}