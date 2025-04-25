package Model;

import java.time.LocalDate;

public class Sancion {

    private int id;
    private String motivo;
    private LocalDate fechaSancion;
    private String tipoSancion;
    private Preso preso;

    public Sancion(int id, String motivo, LocalDate fechaSancion, String tipoSancion, Preso preso) {
        this.id = id;
        this.motivo = motivo;
        this.fechaSancion = fechaSancion;
        this.tipoSancion = tipoSancion;
        this.preso = preso;
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
}
