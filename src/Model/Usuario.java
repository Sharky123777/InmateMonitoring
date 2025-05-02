/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;


public class Usuario {
     private String usuario;
    private String contraseña;
    private Rol rol;
    private Persona persona;

   
    public Usuario(String usuario, String contraseña, Rol rol) {
        this(usuario, contraseña, rol, null);
    }

    
    public Usuario(String usuario, String contraseña, Rol rol, Persona persona) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.persona = persona;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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