package Model.Entities;

import Model.Constants.RolEnum;

public class Usuario extends Persona {
    private String usuario;
    private String password;
    private RolEnum rol;

     // Constructor completo (con datos de persona)
    public Usuario(String primerNombre, String segundoNombre, 
                 String primerApellido, String segundoApellido,
                 int edad, String sexo, String nacionalidad, 
                 String identificacion,
                 String usuario, String password, RolEnum rol) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, 
              edad, sexo, nacionalidad, identificacion);
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    

    public String getPassword() {
        return password;
    }

    public RolEnum getRol() {
        return rol;
    }

    // Setters si los necesitas
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
}