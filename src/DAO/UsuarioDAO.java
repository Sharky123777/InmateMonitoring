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
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray usuariosJson = jsonObject.getAsJsonArray("usuarios");
            
            for (int i = 0; i < usuariosJson.size(); i++) {
                JsonObject usuarioJson = usuariosJson.get(i).getAsJsonObject();
                
                String userUsuario = usuarioJson.get("usuario").getAsString();
                String userPassword = usuarioJson.get("password").getAsString();
                Rol userRol = Rol.valueOf(usuarioJson.get("rol").getAsString());
                
                if (userUsuario.equalsIgnoreCase(usuario) && 
                    userPassword.equals(password) && 
                    userRol == rolSeleccionado) {
                   
                    return new Usuario(userUsuario, userPassword, userRol);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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