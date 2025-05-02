package Model.Entities;

import Model.Constants.EstadoVisitaEnum;
import java.time.LocalDateTime;
import java.util.List;

public class Visita {

    private int id;
    private LocalDateTime fechaHoraVisita;
    private String duracionVisitaEnHoras;
    private String tipoVisita;
    private String lugarVisita;
    private Preso preso;
    private List<Visitante> visitantes;
    private EstadoVisitaEnum estado;
    private static int ultimoId = 0;

    public Visita(LocalDateTime fechaHoraVisita, String duracionVisitaEnHoras, String tipoVisita, String lugarVisita, Preso preso, List<Visitante> visitantes) {
        this.id = ++ultimoId;
        this.fechaHoraVisita = fechaHoraVisita;
        this.duracionVisitaEnHoras = duracionVisitaEnHoras;
        this.tipoVisita = tipoVisita;
        this.lugarVisita = lugarVisita;
        this.preso = preso;
        this.visitantes = visitantes;
        this.estado = EstadoVisitaEnum.EN_PROCESO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

    }

    public LocalDateTime getFechaHoraVisita() {
        return fechaHoraVisita;
    }

    public void setFechaHoraVisita(LocalDateTime fechaHoraVisita) {
        this.fechaHoraVisita = fechaHoraVisita;
    }

    public String getDuracionVisitaEnHoras() {
        return duracionVisitaEnHoras;
    }

    public void setDuracionVisitaEnHoras(String duracionVisitaEnHoras) {
        this.duracionVisitaEnHoras = duracionVisitaEnHoras;
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

    public EstadoVisitaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisitaEnum estado) {
        this.estado = estado;
    }

    public static int getUltimoId() {
        return ultimoId;
    }

    public static void setUltimoId(int ultimoId) {
        Visita.ultimoId = ultimoId;
    }

}
