package Model.Entities;

import Model.Constants.RolEnum;
import Model.Constants.RolEnum;
import Model.Entities.OficialDeRegistro;
import Model.Entities.Usuario;
import Model.Entities.CoordinadorDeActividades;
import Model.Entities.Oficial;
import Model.Entities.Persona;
import Model.Entities.PersonalControl;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SincronizadorJson {

    private static final String RUTA_USUARIOS = "src/Resources/DATA/usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static void sincronizarUsuarioGenerico(
            String identificacion,
            String usuario,
            String contrasena,
            String rutaImagen,
            Consumer<Usuario> actualizador,
            Supplier<Usuario> creador) {
        try {
            List<Usuario> usuarios = cargarUsuarios();

            boolean encontrado = false;
            for (Usuario u : usuarios) {
                if (u.getIdentificacion().equals(identificacion)) {
                    actualizador.accept(u);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                usuarios.add(creador.get());
            }

            guardarUsuarios(usuarios);

        } catch (Exception e) {
            System.err.println("Error en sincronización: " + e.getMessage());
            throw new RuntimeException("Error al sincronizar usuarios", e);
        }
    }

    public static void sincronizarConUsuarios(OficialDeRegistro oficial) {
        sincronizarUsuarioGenerico(
                oficial.getIdentificacion(),
                oficial.getUsuario(),
                oficial.getContrasena(),
                oficial.getRutaImagen(),
                u -> actualizarCamposUsuario(u, oficial),
                () -> crearUsuarioDesdeOficial(oficial)
        );
    }

    private static void actualizarCamposUsuario(Usuario usuario, OficialDeRegistro oficial) {
        actualizarCamposComunes(usuario, oficial);
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

    public static void sincronizarConUsuarios(CoordinadorDeActividades coordinador) {
        sincronizarUsuarioGenerico(
                coordinador.getIdentificacion(),
                coordinador.getUsuario(),
                coordinador.getContrasena(),
                coordinador.getRutaImagen(),
                u -> actualizarCamposUsuario(u, coordinador),
                () -> crearUsuarioDesdeCoordinador(coordinador)
        );
    }

    private static void actualizarCamposUsuario(Usuario usuario, CoordinadorDeActividades coordinador) {
        actualizarCamposComunes(usuario, coordinador);
        usuario.setRutaImagen(coordinador.getRutaImagen());
    }

    private static Usuario crearUsuarioDesdeCoordinador(CoordinadorDeActividades coordinador) {
        return new Usuario(
                coordinador.getPrimerNombre(),
                coordinador.getSegundoNombre(),
                coordinador.getPrimerApellido(),
                coordinador.getSegundoApellido(),
                coordinador.getEdad(),
                coordinador.getSexo(),
                coordinador.getNacionalidad(),
                coordinador.getIdentificacion(),
                coordinador.getUsuario().toLowerCase(),
                coordinador.getContrasena(),
                RolEnum.COORDINADOR_DE_ACTIVIDADES,
                coordinador.getRutaImagen()
        );
    }

    public static void sincronizarConUsuarios(PersonalControl personal) {
        sincronizarUsuarioGenerico(
                personal.getIdentificacion(),
                personal.getUsuario(),
                personal.getContrasena(),
                personal.getRutaImagen(),
                u -> actualizarCamposUsuario(u, personal),
                () -> crearUsuarioDesdePersonal(personal)
        );
    }

    private static void actualizarCamposUsuario(Usuario usuario, PersonalControl personal) {
        actualizarCamposComunes(usuario, personal);
        usuario.setRutaImagen(personal.getRutaImagen());
    }

    private static Usuario crearUsuarioDesdePersonal(PersonalControl personal) {
        return new Usuario(
                personal.getPrimerNombre(),
                personal.getSegundoNombre(),
                personal.getPrimerApellido(),
                personal.getSegundoApellido(),
                personal.getEdad(),
                personal.getSexo(),
                personal.getNacionalidad(),
                personal.getIdentificacion(),
                personal.getUsuario().toLowerCase(),
                personal.getContrasena(),
                RolEnum.PERSONAL_DE_CONTROL,
                personal.getRutaImagen()
        );
    }

    public static void sincronizarConUsuarios(Oficial oficial) {
        sincronizarUsuarioGenerico(
                oficial.getIdentificacion(),
                oficial.getUsuario(),
                oficial.getContrasena(),
                oficial.getRutaImagen(),
                u -> actualizarCamposUsuario(u, oficial),
                () -> crearUsuarioDesdeOficial(oficial)
        );
    }

    private static void actualizarCamposUsuario(Usuario usuario, Oficial oficial) {
        actualizarCamposComunes(usuario, oficial);
        usuario.setRutaImagen(oficial.getRutaImagen());
    }

    private static Usuario crearUsuarioDesdeOficial(Oficial oficial) {
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
                RolEnum.OFICIAL,
                oficial.getRutaImagen()
        );
    }

    private static void actualizarCamposComunes(Usuario usuario, Object persona) {
        try {
            Method getPrimerNombre = persona.getClass().getMethod("getPrimerNombre");
            Method getSegundoNombre = persona.getClass().getMethod("getSegundoNombre");
            Method getPrimerApellido = persona.getClass().getMethod("getPrimerApellido");
            Method getSegundoApellido = persona.getClass().getMethod("getSegundoApellido");
            Method getEdad = persona.getClass().getMethod("getEdad");
            Method getSexo = persona.getClass().getMethod("getSexo");
            Method getNacionalidad = persona.getClass().getMethod("getNacionalidad");

            usuario.setPrimerNombre((String) getPrimerNombre.invoke(persona));
            usuario.setSegundoNombre((String) getSegundoNombre.invoke(persona));
            usuario.setPrimerApellido((String) getPrimerApellido.invoke(persona));
            usuario.setSegundoApellido((String) getSegundoApellido.invoke(persona));
            usuario.setEdad((int) getEdad.invoke(persona));
            usuario.setSexo((String) getSexo.invoke(persona));
            usuario.setNacionalidad((String) getNacionalidad.invoke(persona));
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar campos comunes", e);
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
            return gson.fromJson(elemento, new TypeToken<List<Usuario>>() {
            }.getType());
        } else if (elemento.isJsonObject()) {
            JsonObject obj = elemento.getAsJsonObject();
            if (obj.has("usuarios")) {
                return gson.fromJson(obj.get("usuarios"), new TypeToken<List<Usuario>>() {
                }.getType());
            }
        }

        return new ArrayList<>();
    }

    private static void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }

    public static void sincronizarConUsuarios(Enfermera enfermera) {
        sincronizarUsuarioGenerico(
                enfermera.getIdentificacion(),
                enfermera.getUsuario(),
                enfermera.getContrasena(),
                enfermera.getRutaImagen(),
                u -> actualizarCamposUsuario(u, enfermera),
                () -> crearUsuarioDesdeEnfermera(enfermera)
        );
    }

    private static void actualizarCamposUsuario(Usuario usuario, Enfermera enfermera) {
        actualizarCamposComunes(usuario, enfermera);
        usuario.setRutaImagen(enfermera.getRutaImagen());
    }

    private static Usuario crearUsuarioDesdeEnfermera(Enfermera enfermera) {
        return new Usuario(
                enfermera.getPrimerNombre(),
                enfermera.getSegundoNombre(),
                enfermera.getPrimerApellido(),
                enfermera.getSegundoApellido(),
                enfermera.getEdad(),
                enfermera.getSexo(),
                enfermera.getNacionalidad(),
                enfermera.getIdentificacion(),
                enfermera.getUsuario().toLowerCase(),
                enfermera.getContrasena(),
                RolEnum.ENFERMERA,
                enfermera.getRutaImagen()
        );
    }

}
