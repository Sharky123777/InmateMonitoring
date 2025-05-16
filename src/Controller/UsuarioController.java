package Controller;

import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.Usuario;
import Utilidades.GeneradorCredenciales;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioController {

    private static final int INTENTOS_MAXIMOS = 3;
    private static UsuarioController instancia;
    private final UsuarioDAO usuarioDAO;
    private final Map<String, Integer> intentosPorUsuario;

    private UsuarioController() {
        this.usuarioDAO = UsuarioDAO.getInstancia();
        this.intentosPorUsuario = new HashMap<>();
    }

    public static synchronized UsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioController();
        }
        return instancia;
    }

    public Usuario autenticarUsuario(String username, String password, RolEnum rol) {
        if (!puedeIntentarLogin(username)) {
            throw new SecurityException("Cuenta bloqueada temporalmente por muchos intentos fallidos");
        }

        Usuario usuario = usuarioDAO.validarCredenciales(username, password, rol);

        if (usuario == null) {
            registrarIntentoFallido(username);
            throw new SecurityException("Credenciales incorrectas");
        }

        resetearIntentos(username);
        return usuario;
    }

    private boolean puedeIntentarLogin(String username) {
        int intentos = intentosPorUsuario.getOrDefault(username, 0);
        return intentos < INTENTOS_MAXIMOS;
    }

    private void registrarIntentoFallido(String username) {
        int intentos = intentosPorUsuario.getOrDefault(username, 0);
        intentosPorUsuario.put(username, intentos + 1);
    }

    private void resetearIntentos(String username) {
        intentosPorUsuario.remove(username);
    }

    public String obtenerMensajeBloqueo(String username) {
        int intentos = intentosPorUsuario.getOrDefault(username, 0);
        return String.format("Intentos fallidos: %d/%d. %s",
                intentos, INTENTOS_MAXIMOS,
                intentos >= INTENTOS_MAXIMOS ? "Cuenta temporalmente bloqueada" : "");
    }

    public boolean esDirector(Usuario usuario) {
        return usuarioDAO.esDirector(usuario);
    }

    public List<Usuario> obtenerTodosUsuarios() throws IOException {
        return usuarioDAO.obtenerTodosUsuarios();
    }

    public boolean registrarNuevoUsuario(String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            int edad, String sexo, String nacionalidad,
            String identificacion,
            RolEnum rol, String rutaImagen) { // Añade rutaImagen como parámetro

        // Generar credenciales automáticamente
        String usuario = GeneradorCredenciales.generarUsuarioAleatorio();
        String contrasena = GeneradorCredenciales.generarContrasenaAleatoria();
        String contrasenaEncriptada = GeneradorCredenciales.encriptarContrasena(contrasena);

        Usuario nuevoUsuario = new Usuario(
                primerNombre, segundoNombre,
                primerApellido, segundoApellido,
                edad, sexo, nacionalidad, identificacion,
                usuario, contrasenaEncriptada, rol,
                rutaImagen // Pasamos la ruta de la imagen
        );

        return usuarioDAO.agregarUsuario(nuevoUsuario);
    }

    // Método para generar credenciales (puede ser usado por otros controladores)
    public static class Credenciales {

        public final String usuario;
        public final String contrasena;

        public Credenciales(String usuario, String contrasena) {
            this.usuario = usuario;
            this.contrasena = contrasena;
        }
    }

    public Credenciales generarCredenciales() {
        String usuario = GeneradorCredenciales.generarUsuarioAleatorio();
        String contrasena = GeneradorCredenciales.generarContrasenaAleatoria();
        return new Credenciales(usuario, contrasena);
    }

    public String encriptarContrasena(String contrasena) {
        return GeneradorCredenciales.encriptarContrasena(contrasena);
    }
}
