package Model;

import java.time.LocalDate;

public class PersonalDeControl extends Persona {

    private LocalDate fechaIngreso;
    private String turno;
    private static int ultimoId = 0;
    private int id;

    public PersonalDeControl(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, int edad, String sexo, String nacionalidad, String identificacion, LocalDate fechaIngreso, String turno) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, sexo, nacionalidad, identificacion);
        this.id = ++ultimoId;  
        this.fechaIngreso = fechaIngreso;
        this.turno = turno;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDate getFechaIngreso() {
        return this.fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getTurno() {
        return this.turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public static int getUltimoId() {
        return ultimoId;
    }

    public static void setUltimoId(int ultimoId) {
        PersonalDeControl.ultimoId = ultimoId;
    }
}
