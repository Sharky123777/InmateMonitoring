package Model.Entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.gson.annotations.SerializedName;


public class Enfermera extends Persona {
    private String usuario;
private String contrasena;
    private String turno; // "Diurno" o "Nocturno"
    
    @SerializedName("fechaContratacion")
    private LocalDate fechaContratacion;
    
    @SerializedName("fechaFinContrato")
    private LocalDate fechaFinContrato;
    
    @SerializedName("rutaImagen")
    private String rutaImagen;
    
    @SerializedName("correo")
    private String correo;
    
    public Enfermera(String primerNombre, String segundoNombre, 
               String primerApellido, String segundoApellido,
               int edad, String sexo, String nacionalidad, 
               String identificacion, String turno,
               LocalDate fechaContratacion, LocalDate fechaFinContrato,
               String correo, String usuario, String contrasena) {
    super(primerNombre, segundoNombre, primerApellido, segundoApellido, 
          edad, sexo, nacionalidad, identificacion);
    this.turno = turno;
    this.fechaContratacion = fechaContratacion;
    this.fechaFinContrato = fechaFinContrato;
    this.correo = correo;
    this.usuario = usuario;
    this.contrasena = contrasena;
}

    // Getters y Setters
    public String getTurno() {
        return turno != null ? turno : "";
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    public String getFechaContratacionFormateada() {
    return fechaContratacion != null ? 
           fechaContratacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
}

public String getFechaFinContratoFormateada() {
    return fechaFinContrato != null ? 
           fechaFinContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
}

    public void setFechaFinContrato(LocalDate fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }

    public String getRutaImagen() {
        return rutaImagen != null ? rutaImagen : "";
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getCorreo() {
        return correo != null ? correo : "";
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    
 public String getNombresParaTabla() {
        String nombres = getPrimerNombre();
        if (!getSegundoNombre().isEmpty()) {
            nombres += " " + getSegundoNombre();
        }
        return nombres.trim();
    }
 
 

    // Método específico para mostrar apellidos en tabla
    public String getApellidosParaTabla() {
        String apellidos = getPrimerApellido();
        if (!getSegundoApellido().isEmpty()) {
            apellidos += " " + getSegundoApellido();
        }
        return apellidos.trim();
    }
}