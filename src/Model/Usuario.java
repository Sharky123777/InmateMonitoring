package Model;

public class Usuario {
    private String usuario;
    private String password; // Cambiado a password para consistencia
    private Rol rol;
    private Persona persona;

    public Usuario(String usuario, String password, Rol rol) {
        this(usuario, password, rol, null);
    }

    public Usuario(String usuario, String password, Rol rol, Persona persona) {
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
        this.persona = persona;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}