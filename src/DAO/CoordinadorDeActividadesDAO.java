package DAO;

import Model.Entities.CoordinadorDeActividades;
import Model.Constants.RolEnum;
import Model.Entities.EmailSender;
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
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class CoordinadorDeActividadesDAO {
    private static final String RUTA_JSON = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Resources\\DATA\\CDA.json";
    
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_CDA/";
    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private final Gson gson;
    private static CoordinadorDeActividadesDAO instancia;
    
    public CoordinadorDeActividadesDAO (){
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter (LocalDate.class, new CoordinadorDeActividadesDAO.LocalDateAdapter())
                .create();
        
        crearDirectoriosSiNoExisten();
    
}
    
    public static synchronized CoordinadorDeActividadesDAO getInstancia(){
        if (instancia == null){
            instancia = new CoordinadorDeActividadesDAO();
        }
    return instancia;
}
    
    private void crearDirectoriosSiNoExisten (){
        try {
            Files.createDirectories(Paths.get(RUTA_IMAGENES));
            Files.createDirectories(Paths.get(RUTA_JSON).getParent());
            
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Error al crear directorios: " + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
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
    
    public CoordinadorDeActividades obtenerCoordinadorPorUsuario (String usuario){
        List <CoordinadorDeActividades> CDAS = obtenerCoordinador();
        return CDAS.stream()
                .filter (g -> g.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }
    
    private List <Usuario> obtenerTodosUsuarios() throws IOException{
        File archivo = new File(RUTA_USUARIOS);
        if (!archivo.exists() || archivo.length() == 0){
            return new ArrayList<>();
        }
        
        try (FileReader reader = new FileReader(archivo)){
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");
            
            Type tipoLista = new TypeToken <List<Usuario>>() {}.getType();
            return gson.fromJson(usuariosArray, tipoLista);
            
        }
    }
    
    private void eliminarUsuario (String usuario) throws IOException{
        List <Usuario> usuarios = obtenerTodosUsuarios();
        usuarios.removeIf(u -> u.getUsuario().equals(usuario));
        
        try (Writer writer = new FileWriter (RUTA_USUARIOS)){
            JsonObject jsonObject = new JsonObject();
            JsonArray usuariosArray = gson.toJsonTree(usuarios).getAsJsonArray();
            jsonObject.add("usuarios", usuariosArray);
            gson.toJson(jsonObject, writer);
        }
    }
    
    private boolean guardarCoordinador (CoordinadorDeActividades coordinadorDeActividades, File imagen) throws IOException {
        String nombreImagen = coordinadorDeActividades.getIdentificacion() + "_" + 
                System.currentTimeMillis()+
                imagen.getName().substring(imagen.getName().lastIndexOf("."));
        
        String rutaImagenFinal = RUTA_IMAGENES + nombreImagen;
        
        Files.createDirectories(Paths.get(RUTA_IMAGENES));
        
        Files.copy(imagen.toPath(), Paths.get(rutaImagenFinal), StandardCopyOption.REPLACE_EXISTING);
        coordinadorDeActividades.setRutaImagen(rutaImagenFinal);
        
        List <CoordinadorDeActividades> CDAS = obtenerCoordinador();
        coordinadorDeActividades.add(CDAS);
        guardarListaCoordinadores(CDAS);
        
        EmailSender.getInstancia().enviarCredenciales(RUTA_USUARIOS, RUTA_USUARIOS, nombreImagen, RolEnum.DIRECTOR)
        
    }
    
}