package Model;

import java.time.LocalDate;

public class Oficial extends Persona {

    private String placa;
    private String cargo;
    private LocalDate fechaInicioContrato;
    private LocalDate fechaFinContrato;
    private String turno;
    private String fotoPath;

    public Oficial(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
            int edad, String sexo, String nacionalidad, String identificacion,
            String placa, String cargo, LocalDate fechaInicioContrato, LocalDate fechaFinContrato,
            String turno, String fotoPath) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, sexo, nacionalidad, identificacion);
        this.placa = placa;
        this.cargo = cargo;
        this.fechaInicioContrato = fechaInicioContrato;
        this.fechaFinContrato = fechaFinContrato;
        this.turno = turno;
        this.fotoPath = fotoPath;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
