package Model.Entities;

import Model.Constants.EstadoCitaMedicaEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CitaMedica {

    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private Guardia guardia;
    private Presa preso;
    private Enfermera enfermera;
    private EstadoCitaMedicaEnum estado;
    private String diagnostico;  
    private String rutaHistoriaClinica;
    private LocalDateTime fechaHoraAtencion;
    private String receta;

     public CitaMedica() {
        this.estado = EstadoCitaMedicaEnum.PENDIENTE;
        this.diagnostico = "";
    }
     
    public CitaMedica(int id, LocalDate fecha, LocalTime hora, String motivo, Guardia guardia, Presa preso, Enfermera enfermera) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.guardia = guardia;
        this.preso = preso;
        this.enfermera = enfermera;
        this.estado = EstadoCitaMedicaEnum.PENDIENTE;
        this.diagnostico = ""; 
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

    public Presa getPreso() {
        return preso;
    }

    public void setPreso(Presa preso) {
        this.preso = preso;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }

    public EstadoCitaMedicaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoCitaMedicaEnum estado) {
        this.estado = estado;

    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    
     public void setRutaHistoriaClinica(String rutaHistoriaClinica) {
        this.rutaHistoriaClinica = rutaHistoriaClinica;
    }

    public String getRutaHistoriaClinica() {
        return rutaHistoriaClinica;
    }

    public void setFechaHoraAtencion(LocalDateTime fechaHoraAtencion) {
        this.fechaHoraAtencion = fechaHoraAtencion;
    }

    public LocalDateTime getFechaHoraAtencion() {
        return fechaHoraAtencion;
    }

    public String getReceta() {
        return receta;
    }

    public void setReceta(String receta) {
        this.receta = receta;
    }
    
     
}
