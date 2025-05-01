package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaMedica {

    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private Guardia guardia;
    private Preso preso;
    private Enfermera enfermera;

    public CitaMedica(int id, LocalDate fecha, LocalTime hora, String motivo, Guardia guardia, Preso preso, Enfermera enfermera) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.guardia = guardia;
        this.preso = preso;
        this.enfermera = enfermera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Guardia getGuardia() {
        return guardia;
    }

    public void setGuardia(Guardia guardia) {
        this.guardia = guardia;
    }

    public Preso getPreso() {
        return preso;
    }

    public void setPreso(Preso preso) {
        this.preso = preso;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }
}
