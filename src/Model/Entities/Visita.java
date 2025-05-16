package Model.Entities;

import Model.Constants.EstadoVisitaEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Visita {

    private int id;
    private LocalDate fechaVisita;
    private LocalTime horaVisita;
    private final String duracionVisitaEnHoras = "1";
    private String tipoVisita;
    private String lugarVisita;
    private Preso preso;
    private List<Visitante> visitantes;
    private EstadoVisitaEnum estado;

    public Visita(int id, LocalDate fechaVisita, LocalTime horaVisita, String tipoVisita,
            String lugarVisita, Preso preso, List<Visitante> visitantes) {
        this.id = id;
        this.fechaVisita = fechaVisita;
        this.horaVisita = horaVisita;
        this.tipoVisita = tipoVisita;
        this.lugarVisita = lugarVisita;
        this.preso = preso;
        this.visitantes = new ArrayList<>(visitantes);
        this.estado = EstadoVisitaEnum.EN_PROCESO;
    }

    public EstadoVisitaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisitaEnum estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public LocalTime getHoraVisita() {
        return horaVisita;
    }

    public void setHoraVisita(LocalTime horaVisita) {
        this.horaVisita = horaVisita;
    }

    public String getDuracionVisitaEnHoras() {
        return duracionVisitaEnHoras;
    }

    public String getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public String getLugarVisita() {
        return lugarVisita;
    }

    public void setLugarVisita(String lugarVisita) {
        this.lugarVisita = lugarVisita;
    }

    public Preso getPreso() {
        return preso;
    }

    public void setPreso(Preso preso) {
        this.preso = preso;
    }

    public List<Visitante> getVisitantes() {
        return visitantes;
    }

    public void setVisitantes(List<Visitante> visitantes) {
        this.visitantes = visitantes;
    }

}
