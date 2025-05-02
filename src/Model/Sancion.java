package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Sancion {

    private int id;
    private String motivo;
    private LocalDate fechaSancion;
    private LocalTime hora;
    private String duracionEnHoras;
    private String tipoSancion;
    private Preso preso;
    private Guardia guardia;

    public Sancion(int id, String motivo, LocalDate fechaSancion, LocalTime hora, String duracionEnHoras, String tipoSancion, Preso preso, Guardia guardia) {
        this.id = id;
        this.motivo = motivo;
        this.fechaSancion = fechaSancion;
        this.hora = hora;
        this.duracionEnHoras = duracionEnHoras;
        this.tipoSancion = tipoSancion;
        this.preso = preso;
        this.guardia = guardia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFechaSancion() {
        return fechaSancion;
    }

    public void setFechaSancion(LocalDate fechaSancion) {
        this.fechaSancion = fechaSancion;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getDuracionEnHoras() {
        return duracionEnHoras;
    }

    public void setDuracionEnHoras(String duracionEnHoras) {
        this.duracionEnHoras = duracionEnHoras;
    }

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public Preso getPreso() {
        return preso;
    }

    public void setPreso(Preso preso) {
        this.preso = preso;
    }

    public Guardia getGuardia() {
        return guardia;
    }

    public void setGuardia(Guardia guardia) {
        this.guardia = guardia;
    }
}
