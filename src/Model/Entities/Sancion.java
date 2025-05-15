package Model.Entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Sancion {

    private int id;
    private String motivo;
    private LocalDate fechaSancion;
    private LocalTime hora;
    private String tipoSancion;
    private Preso preso;
    private Guardia guardia;
    private int diasDuracion;

    public Sancion(int id, String motivo, LocalDate fechaSancion, LocalTime hora,
            String tipoSancion, Preso preso, Guardia guardia) {
        this.id = id;
        this.motivo = motivo;
        this.fechaSancion = fechaSancion;
        this.hora = hora;
        this.tipoSancion = tipoSancion;
        this.preso = preso;
        this.guardia = guardia;
        this.diasDuracion = calcularDuracionPorTipo(tipoSancion);
    }

    private int calcularDuracionPorTipo(String tipoSancion) {
        switch (tipoSancion) {
            case "Amonestación verbal":
                return 1;
            case "Suspensión de visitas":
                return 7;
            case "Aislamiento":
                return 10;
            default:
                return 1;
        }
    }

    public int getDuracionBase() {
        return this.diasDuracion;
    }

    public boolean esDeTipo(String tipo) {
        return this.tipoSancion.equals(tipo);
    }

    public boolean esAislamiento() {
        return "Aislamiento".equals(this.tipoSancion);
    }

    public boolean estaActiva() {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaFin = fechaSancion.plusDays(diasDuracion);
        return !hoy.isBefore(fechaSancion) && !hoy.isAfter(fechaFin);
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

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
        this.diasDuracion = calcularDuracionPorTipo(tipoSancion);
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

    public int getDiasDuracion() {
        return diasDuracion;
    }

    public void setDiasDuracion(int diasDuracion) {
        this.diasDuracion = diasDuracion;
    }
}
