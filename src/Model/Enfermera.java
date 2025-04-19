package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Enfermera extends Persona {
    private String turno; // "Diurno" o "Nocturno"
    private LocalDate fechaContratacion;
    private LocalDate fechaFinContrato;
    private String rutaImagen;
    private String correo;
    
    public Enfermera(String primerNombre, String segundoNombre, 
                   String primerApellido, String segundoApellido,
                   int edad, String sexo, String nacionalidad, 
                   String identificacion, String turno,
                   LocalDate fechaContratacion, LocalDate fechaFinContrato,
                   String correo) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, 
              edad, sexo, nacionalidad, identificacion);
        this.turno = turno;
        this.fechaContratacion = fechaContratacion;
        this.fechaFinContrato = fechaFinContrato;
        this.correo = correo;
    }

    // Getters y Setters
    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public String getFechaContratacionFormateada() {
        return fechaContratacion != null ? 
               fechaContratacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    public String getFechaFinContratoFormateada() {
        return fechaFinContrato != null ? 
               fechaFinContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public void setFechaFinContrato(LocalDate fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }

    public String getRutaImagen() {
        return rutaImagen;
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

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + turno + ") - " + correo;
    }
}