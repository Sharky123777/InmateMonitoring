
package Model;

import java.time.LocalDate;


public class CoordinadorDeActividades extends Persona {
     private String correo;
    private String turno;
    private LocalDate fechaInicioContrato;
    private LocalDate fechaFinContrato;
    private String rutaImagen;
    private String cargo;
    
    public CoordinadorDeActividades(String primerNombre, String segundoNombre, String primerApellido, 
                  String segundoApellido, int edad, String cedula, String nacionalidad, 
                  String correo, String turno, LocalDate fechaFinContrato, String cargo) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido,
              edad, "Femenino", nacionalidad, cedula);
        this.correo = correo;
        this.turno = turno;
        this.fechaInicioContrato = LocalDate.now();
        this.fechaFinContrato = fechaFinContrato;
        this.cargo = cargo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDate getFechaInicioContrato() {
        return fechaInicioContrato;
    }

    public void setFechaInicioContrato(LocalDate fechaInicioContrato) {
        this.fechaInicioContrato = fechaInicioContrato;
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    
}
