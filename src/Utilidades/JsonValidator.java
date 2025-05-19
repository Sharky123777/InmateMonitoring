package Utilidades;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class JsonValidator {
    private static JsonValidator instancia;
    private final Gson gson;
    private static final SimpleDateFormat BACKUP_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private JsonValidator() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static synchronized JsonValidator getInstancia() {
        if (instancia == null) {
            instancia = new JsonValidator();
        }
        return instancia;
    }

    public JsonObject validarArchivoJson(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        String nombreArchivo = new File(rutaArchivo).getName();
        
        // 1. Validar existencia del archivo
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null, 
                "Archivo no encontrado: " + nombreArchivo + "\nSe creará uno nuevo.",
                "Archivo faltante", JOptionPane.WARNING_MESSAGE);
            return crearArchivoJsonVacio(rutaArchivo);
        }

        // 2. Validar que no esté vacío
        if (archivo.length() == 0) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                "El archivo " + nombreArchivo + " está vacío.\n¿Desea inicializarlo con estructura válida?",
                "Archivo vacío", JOptionPane.YES_NO_OPTION);
            
            if (respuesta == JOptionPane.YES_OPTION) {
                return crearArchivoJsonVacio(rutaArchivo);
            }
            throw new JsonParseException("El usuario canceló la operación");
        }

        // 3. Leer y validar contenido
        try (InputStream is = new FileInputStream(archivo);
             JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            
            // Detección manual de errores básicos
            if (!contenido.trim().startsWith("{")) {
                return manejarErrorEstructural(nombreArchivo, rutaArchivo, 
                    "Falta llave de apertura '{' al inicio del archivo");
            }
            
            if (!contenido.trim().endsWith("}")) {
                return manejarErrorEstructural(nombreArchivo, rutaArchivo,
                    "Falta llave de cierre '}' al final del archivo");
            }
            
            JsonElement element = JsonParser.parseString(contenido);
            
            if (!element.isJsonObject()) {
                return manejarErrorEstructural(nombreArchivo, rutaArchivo,
                    "El contenido no es un objeto JSON válido");
            }
            
            return element.getAsJsonObject();
            
        } catch (JsonSyntaxException e) {
            return manejarErrorSintaxis(nombreArchivo, rutaArchivo, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error crítico en " + nombreArchivo + ": " + e.getMessage(),
                "Error de sistema", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error procesando " + rutaArchivo, e);
        }
    }

    private JsonObject manejarErrorEstructural(String nombreArchivo, String rutaArchivo, String mensajeError) {
        // Crear backup antes de cualquier modificación
        String backupPath = crearBackup(rutaArchivo);
        
        int respuesta = JOptionPane.showConfirmDialog(null,
            "ERROR EN ARCHIVO: " + nombreArchivo + "\n\n"
            + "Problema detectado: " + mensajeError + "\n\n"
            + "Se ha creado un backup en: " + backupPath + "\n\n"
            + "¿Desea reparar el archivo con estructura básica?",
            "Error estructural en JSON", JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            return crearArchivoJsonVacio(rutaArchivo);
        }
        throw new JsonParseException("El usuario rechazó reparar el archivo " + nombreArchivo);
    }

    private JsonObject manejarErrorSintaxis(String nombreArchivo, String rutaArchivo, String mensajeError) {
        String backupPath = crearBackup(rutaArchivo);
        String errorAnalizado = analizarErrorSintaxis(mensajeError);
        
        int respuesta = JOptionPane.showConfirmDialog(null,
            "ERROR DE SINTAXIS EN: " + nombreArchivo + "\n\n"
            + "Causa exacta:\n" + errorAnalizado + "\n\n"
            + "Se creó backup en: " + backupPath + "\n\n"
            + "¿Desea reparar el archivo?",
            "Error de sintaxis en JSON", JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            return crearArchivoJsonVacio(rutaArchivo);
        }
        throw new JsonParseException("El usuario rechazó reparar el archivo " + nombreArchivo);
    }

    private String crearBackup(String rutaOriginal) {
        try {
            Path pathOriginal = Paths.get(rutaOriginal);
            if (!Files.exists(pathOriginal)) {
                return "No se pudo crear backup (archivo no existe)";
            }
            
            String timestamp = BACKUP_FORMAT.format(new Date());
            String backupDir = Paths.get(rutaOriginal).getParent() + "/backups/";
            Files.createDirectories(Paths.get(backupDir));
            
            String nombreBackup = "backup_" + timestamp + "_" + Paths.get(rutaOriginal).getFileName();
            String rutaBackup = backupDir + nombreBackup;
            
            Files.copy(pathOriginal, Paths.get(rutaBackup), StandardCopyOption.REPLACE_EXISTING);
            return rutaBackup;
        } catch (Exception e) {
            return "Error creando backup: " + e.getMessage();
        }
    }

    private String analizarErrorSintaxis(String mensajeError) {
        // Análisis detallado del error
        if (mensajeError.contains("Expected BEGIN_OBJECT")) {
            return "- El archivo debe comenzar con '{'";
        } else if (mensajeError.contains("Expected END_OBJECT")) {
            return "- Falta la llave de cierre '}'";
        } else if (mensajeError.contains("Expected BEGIN_ARRAY")) {
            return "- Falta corchete de apertura '['";
        } else if (mensajeError.contains("Expected END_ARRAY")) {
            return "- Falta corchete de cierre ']'";
        } else if (mensajeError.contains("Expected name")) {
            return "- Las propiedades deben estar entre comillas dobles";
        } else if (mensajeError.contains("Expected value")) {
            return "- Falta el valor después de los dos puntos";
        } else if (mensajeError.contains("Expected ':'")) {
            return "- Falta el caracter ':' entre propiedad y valor";
        } else if (mensajeError.contains("Expected ','")) {
            return "- Falta coma ',' entre elementos";
        } else if (mensajeError.contains("Unterminated object")) {
            return "- Objeto no cerrado correctamente";
        } else if (mensajeError.contains("Unterminated array")) {
            return "- Array no cerrado correctamente";
        }
        return "- Error de sintaxis: " + mensajeError;
    }

    private JsonObject crearArchivoJsonVacio(String rutaArchivo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("coordinadores", new JsonArray());
        
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(jsonObject, writer);
            return jsonObject;
        } catch (IOException e) {
            throw new RuntimeException("Error al crear archivo JSON", e);
        }
    }

    public boolean validarJsonArray(JsonElement element, String nombreArray) {
        if (element == null || !element.isJsonObject()) {
            return false;
        }
        
        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject.has(nombreArray) && jsonObject.get(nombreArray).isJsonArray();
    }
}