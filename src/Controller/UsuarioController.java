 package Controller;

import DAO.UsuarioDAO;
import Model.Constants.RolEnum;
import Model.Entities.Usuario;
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
        return usuarioDAO.validarCredenciales(usuario, contrasena, rol);
    }
    
    public boolean esDirector(Usuario usuario) {
        return usuarioDAO.esDirector(usuario);
    }
    
    public List<Usuario> obtenerTodosUsuarios() throws IOException {
        return usuarioDAO.obtenerTodosUsuarios();
    }
}