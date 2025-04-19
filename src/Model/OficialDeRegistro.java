package Model;

import java.time.LocalDate;

public class OficialDeRegistro extends Persona {
    private String numeroPlaca;
    private String rango;
    private LocalDate fechaIngreso;
    private String turno;

    public OficialDeRegistro(
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        int edad,
        String sexo,
        String nacionalidad,
        String identificacion,
        String numeroPlaca,
        String rango,
        LocalDate fechaIngreso,
        String turno
    ) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido,
              edad, sexo, nacionalidad, identificacion);
        this.numeroPlaca = numeroPlaca;
        this.rango = rango;
        this.fechaIngreso = fechaIngreso;
        this.turno = turno;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}



