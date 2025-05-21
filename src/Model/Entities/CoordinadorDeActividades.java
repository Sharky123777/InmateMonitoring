package Model.Entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.gson.annotations.SerializedName;

public class CoordinadorDeActividades extends Persona {

    private String usuario;
    private String contrasena;

    @SerializedName("correo")
    private String correo;

    private String turno;

    @SerializedName("fechaInicioContrato")
    private LocalDate fechaInicioContrato;

    @SerializedName("fechaFinContrato")
    private LocalDate fechaFinContrato;

    @SerializedName("rutaImagen")
    private String rutaImagen;

    private String cargo;

    public CoordinadorDeActividades(String primerNombre, String segundoNombre, String primerApellido,
                                    String segundoApellido, int edad, String sexo, String nacionalidad,
                                    String cedula, String correo, String turno, LocalDate fechaInicioContrato,
                                    LocalDate fechaFinContrato, String usuario, String contrasena, String cargo) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, sexo, nacionalidad, cedula);
        this.correo = correo;
        this.turno = turno;
        this.fechaInicioContrato = fechaInicioContrato;
        this.fechaFinContrato = fechaFinContrato;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.cargo = cargo;
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

    public String getCorreo() {
        return correo != null ? correo : "";
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTurno() {
        return turno != null ? turno : "";
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

    public String getFechaInicioContratoFormateada() {
        return fechaInicioContrato != null ?
               fechaInicioContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    public void setFechaFinContrato(LocalDate fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }

    public String getFechaFinContratoFormateada() {
        return fechaFinContrato != null ?
               fechaFinContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public String getRutaImagen() {
        return rutaImagen != null ? rutaImagen : "";
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getCargo() {
        return cargo != null ? cargo : "";
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    
    

    public String getNombresParaTabla() {
        String nombres = getPrimerNombre();
        if (!getSegundoNombre().isEmpty()) {
            nombres += " " + getSegundoNombre();
        }
        return nombres.trim();
    }

    public String getApellidosParaTabla() {
        String apellidos = getPrimerApellido();
        if (!getSegundoApellido().isEmpty()) {
            apellidos += " " + getSegundoApellido();
        }
        return apellidos.trim();
    }
    
    
}
