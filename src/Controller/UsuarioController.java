package Controller;

import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.Usuario;
import Utilidades.GeneradorCredenciales;
import java.io.IOException;
import java.util.List;

public class UsuarioController {
    private static UsuarioController instancia;
    private final UsuarioDAO usuarioDAO;
    
    private UsuarioController() {
        this.usuarioDAO = UsuarioDAO.getInstancia();
    }
    
    public static synchronized UsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioController();
        }
        return instancia;
    }
    
    public Usuario autenticarUsuario(String usuario, String contrasena, RolEnum rol) {
        // Primero obtenemos el usuario con la contraseña encriptada
        Usuario usuarioEncontrado = usuarioDAO.validarCredenciales(usuario, contrasena, rol);
        
        if (usuarioEncontrado != null) {
            // Verificamos si la contraseña coincide (comparando versiones encriptadas)
            if (GeneradorCredenciales.verificarContrasena(contrasena, usuarioEncontrado.getPassword())) {
                return usuarioEncontrado;
            }
        }
        return null;
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
                                  RolEnum rol) {
        // Generar credenciales automáticamente
        String usuario = GeneradorCredenciales.generarUsuarioAleatorio();
        String contrasena = GeneradorCredenciales.generarContrasenaAleatoria();
        String contrasenaEncriptada = GeneradorCredenciales.encriptarContrasena(contrasena);
        
        Usuario nuevoUsuario = new Usuario(
            primerNombre, segundoNombre, 
            primerApellido, segundoApellido,
            edad, sexo, nacionalidad, identificacion,
            usuario, contrasenaEncriptada, rol
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