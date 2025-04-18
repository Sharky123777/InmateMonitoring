package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Guardia extends Persona {
    private String correo;
    private String turno;
    private LocalDate fechaInicioContrato;
    private LocalDate fechaFinContrato;
    private String rutaImagen;
    private String cargo;

    public Guardia(String primerNombre, String segundoNombre, String primerApellido, 
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

    // Métodos específicos de Guardia
    public String getCorreo() {
        return correo != null ? correo : "";
    }

    public void setCorreo(String correo) {
        if (correo != null && !correo.contains("@")) {
            throw new IllegalArgumentException("El correo debe contener @");
        }
        this.correo = correo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        if (!"Diurno".equals(turno) && !"Nocturno".equals(turno)) {
            throw new IllegalArgumentException("Turno debe ser 'Diurno' o 'Nocturno'");
        }
        this.turno = turno;
    }

    public LocalDate getFechaInicioContrato() {
        return fechaInicioContrato;
    }

    public String getFechaInicioContratoFormateada() {
        return fechaInicioContrato.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    public String getFechaFinContratoFormateada() {
        return fechaFinContrato != null ? 
               fechaFinContrato.format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
    }

    public void setFechaFinContrato(LocalDate fechaFinContrato) {
        if (fechaFinContrato == null || fechaFinContrato.isBefore(fechaInicioContrato)) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior a la fecha de inicio");
        }
        this.fechaFinContrato = fechaFinContrato;
    }

    public String getRutaImagen() {
        return rutaImagen != null ? rutaImagen : "";
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("El cargo no puede estar vacío");
        }
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return String.format("Guardia: %s, Cédula: %s, Cargo: %s", 
               super.toString(), getIdentificacion(), cargo);
    }
}