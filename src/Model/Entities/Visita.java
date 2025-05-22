package Model.Entities;

import Model.Constants.EstadoVisitaEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Visita {

    private int id;
    private LocalDate fechaVisita;
    private LocalTime horaVisita;
    private final String duracionVisitaEnHoras = "1";
    private String tipoVisita;
    private String lugarVisita;
    private Presa preso;
    private Map<Visitante, String> visitantesConRelacion;
    private EstadoVisitaEnum estado;
    private String razonCancelacion;

    public Visita(int id, LocalDate fechaVisita, LocalTime horaVisita, String tipoVisita,
            String lugarVisita, Presa preso) {
        this.id = id;
        this.fechaVisita = fechaVisita;
        this.horaVisita = horaVisita;
        this.tipoVisita = tipoVisita;
        this.lugarVisita = lugarVisita;
        this.preso = preso;
        this.visitantesConRelacion = new HashMap<>();
        this.estado = EstadoVisitaEnum.PROGRAMADA;
        this.razonCancelacion = null;
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

    public Presa getPreso() {
        return preso;
    }

    public void setPreso(Presa preso) {
        this.preso = preso;
    }

    public void agregarVisitante(Visitante visitante, String relacion) {
        this.visitantesConRelacion.put(visitante, relacion);
    }

    public Map<Visitante, String> getVisitantesConRelacion() {
        return new HashMap<>(visitantesConRelacion);
    }

    public String getRazonCancelacion() {
        return razonCancelacion;
    }

    public void setRazonCancelacion(String razonCancelacion) {
        this.razonCancelacion = razonCancelacion;
    }

}
