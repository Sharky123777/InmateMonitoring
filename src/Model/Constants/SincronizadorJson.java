package Model.Constants;

import Model.Constants.RolEnum;
import Model.Entities.OficialDeRegistro;
import Model.Entities.Usuario;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SincronizadorJson {

    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void sincronizarConUsuarios(OficialDeRegistro oficialActualizado) {
        try {
            List<Usuario> usuarios = cargarUsuarios();
            
            boolean encontrado = false;
            for (Usuario u : usuarios) {
                if (u.getIdentificacion().equals(oficialActualizado.getIdentificacion())) {
                    actualizarCamposUsuario(u, oficialActualizado);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                usuarios.add(crearUsuarioDesdeOficial(oficialActualizado));
            }
            
            guardarUsuarios(usuarios);
            
        } catch (Exception e) {
            System.err.println("Error en sincronización: " + e.getMessage());
            throw new RuntimeException("Error al sincronizar usuarios", e);
        }
    }

    private static List<Usuario> cargarUsuarios() throws IOException {
        File archivo = new File(RUTA_USUARIOS);
        
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        String contenido = Files.readString(archivo.toPath());
        JsonElement elemento = JsonParser.parseString(contenido);
        
        if (elemento.isJsonArray()) {
            return gson.fromJson(elemento, new TypeToken<List<Usuario>>(){}.getType());
        } else if (elemento.isJsonObject()) {
            JsonObject obj = elemento.getAsJsonObject();
            if (obj.has("usuarios")) {
                return gson.fromJson(obj.get("usuarios"), new TypeToken<List<Usuario>>(){}.getType());
            }
        }
        
        return new ArrayList<>();
    }

    private static void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }

    private static void actualizarCamposUsuario(Usuario usuario, OficialDeRegistro oficial) {
        usuario.setPrimerNombre(oficial.getPrimerNombre());
        usuario.setSegundoNombre(oficial.getSegundoNombre());
        usuario.setPrimerApellido(oficial.getPrimerApellido());
        usuario.setSegundoApellido(oficial.getSegundoApellido());
        usuario.setEdad(oficial.getEdad());
        usuario.setSexo(oficial.getSexo());
        usuario.setNacionalidad(oficial.getNacionalidad());
        usuario.setRutaImagen(oficial.getRutaImagen());
    }

    private static Usuario crearUsuarioDesdeOficial(OficialDeRegistro oficial) {
    return new Usuario(
        oficial.getPrimerNombre(),
        oficial.getSegundoNombre(),
        oficial.getPrimerApellido(),
        oficial.getSegundoApellido(),
        oficial.getEdad(),
        oficial.getSexo(),
        oficial.getNacionalidad(),
        oficial.getIdentificacion(),
        oficial.getUsuario().toLowerCase(), 
        oficial.getContrasena(), 
        RolEnum.OFICIAL_DE_REGISTRO,
        oficial.getRutaImagen()
    );
}
}