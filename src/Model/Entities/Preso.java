package Model.Entities;

import Model.Constants.EstadoPresoEnum;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Preso extends Persona {

    private float estatura;
    private float peso;
    private List<Delito> delitos = new ArrayList<>();
    private String nivelDeSeguridad;
    private String seccionAsignada;
    private String condicion;
    private String celdaAsignada;
    private boolean enAislamiento = false;
    private boolean enVisita = false;
    private boolean enActividad = false;
    private String nivelDeRiesgo;
    private int numeroDeVisitas = 0;
    private String grupoSanguineo;
    private String fotoPath;
    private EstadoPresoEnum estado = EstadoPresoEnum.ACTIVO;
    private int id = 0;
    private List<String> actividadesAsignadasIds = new ArrayList<>();
    private List<String> actividadesCanceladasIds = new ArrayList<>();
    private LocalDate fechaLiberacion;
private LocalDate fechaDefuncion;
private LocalDate fechaFuga;

    
    public Preso(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
            int edad, String sexo, String nacionalidad, String identificacion,
            float estatura, float peso, List<Delito> delitos,
            String nivelDeSeguridad, String seccionAsignada, String condicion,
            String celdaAsignada, boolean enAislamiento, boolean enVisita, boolean enActividad, String nivelDeRiesgo,
            int numeroDeVisitas, String grupoSanguineo, String fotoPath, int id) {

        super(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, sexo, nacionalidad, identificacion);
        this.estatura = estatura;
        this.peso = peso;
        this.delitos = delitos != null ? delitos : new ArrayList<>();
        this.nivelDeSeguridad = nivelDeSeguridad;
        this.seccionAsignada = seccionAsignada;
        this.condicion = condicion;
        this.celdaAsignada = celdaAsignada;
        this.enAislamiento = enAislamiento;
        this.enVisita = enVisita;
        this.enActividad = enActividad;
        this.nivelDeRiesgo = nivelDeRiesgo;
        this.numeroDeVisitas = numeroDeVisitas;
        this.grupoSanguineo = grupoSanguineo;
        this.fotoPath = fotoPath;
        this.id = id;
        this.estado = EstadoPresoEnum.ACTIVO;
        
        
    }

    public boolean isEnVisita() {
        return enVisita;
    }

    public void setEnVisita(boolean enVisita) {
        this.enVisita = enVisita;
    }

    public boolean isEnActividad() {
        return enActividad;
    }

    public void setEnActividad(boolean enActividad) {
        this.enActividad = enActividad;
    }
    
    
    

    public LocalDate getFechaLiberacion() {
        return fechaLiberacion;
    }

    public void setFechaLiberacion(LocalDate fechaLiberacion) {
        this.fechaLiberacion = fechaLiberacion;
    }

    public LocalDate getFechaFuga() {
        return fechaFuga;
    }

    public void setFechaFuga(LocalDate fechaFuga) {
        this.fechaFuga = fechaFuga;
    }

    
    
    
    public float getEstatura() {
        return estatura;
    }

    public void setEstatura(float estatura) {
        this.estatura = estatura;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public List<Delito> getDelitos() {
        return delitos;
    }

    public void setDelitos(List<Delito> delitos) {
        this.delitos = delitos != null ? delitos : new ArrayList<>();
    }

    public String getNivelDeSeguridad() {
        return nivelDeSeguridad;
    }

    public void setNivelDeSeguridad(String nivelDeSeguridad) {
        this.nivelDeSeguridad = nivelDeSeguridad;
    }

    public String getSeccionAsignada() {
        return seccionAsignada;
    }

    public void setSeccionAsignada(String seccionAsignada) {
        this.seccionAsignada = seccionAsignada;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getCeldaAsignada() {
        return celdaAsignada;
    }

    public void setCeldaAsignada(String celdaAsignada) {
        this.celdaAsignada = celdaAsignada;
    }

    public boolean isEnAislamiento() {
        return enAislamiento;
    }

    public void setEnAislamiento(boolean enAislamiento) {
        this.enAislamiento = enAislamiento;
    }

    public String getNivelDeRiesgo() {
        return nivelDeRiesgo;
    }

    public void setNivelDeRiesgo(String nivelDeRiesgo) {
        this.nivelDeRiesgo = nivelDeRiesgo;
    }

    public int getNumeroDeVisitas() {
        return numeroDeVisitas;
    }

    public void setNumeroDeVisitas(int numeroDeVisitas) {
        this.numeroDeVisitas = numeroDeVisitas;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public EstadoPresoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoPresoEnum estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getActividadesAsignadasIds() {
        return actividadesAsignadasIds;
    }

    public void setActividadesAsignadasIds(List<String> actividadesAsignadasIds) {
        this.actividadesAsignadasIds = actividadesAsignadasIds != null ? actividadesAsignadasIds : new ArrayList<>();
    }

    public List<String> getActividadesCanceladasIds() {
        return actividadesCanceladasIds;
    }

    public void setActividadesCanceladasIds(List<String> actividadesCanceladasIds) {
        this.actividadesCanceladasIds = actividadesCanceladasIds != null ? actividadesCanceladasIds : new ArrayList<>();
    }

    public LocalDate getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(LocalDate fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public boolean puedeSerAsignadoAActividad() {
        return estado == EstadoPresoEnum.ACTIVO && !enAislamiento;
    }

    public void agregarActividadAsignada(String idActividad) {
        if (puedeSerAsignadoAActividad()) {
            actividadesAsignadasIds.add(idActividad);
        } else {
            throw new IllegalStateException("El preso no puede ser asignado a actividades en su estado actual");
        }
    }

    public void marcarActividadCancelada(String idActividad) {
        if (actividadesAsignadasIds.remove(idActividad)) {
            actividadesCanceladasIds.add(idActividad);
        }
    }

    public void agregarDelito(Delito delito) {
        if (delito != null) {
            delitos.add(delito);
        }
    }

    public boolean eliminarDelito(Delito delito) {
        return delitos.remove(delito);
    }

    @Override
    public String toString() {
        return "Preso{"
                + "id=" + id
                + ", nombreCompleto='" + getNombreCompleto() + '\''
                + ", identificacion='" + getIdentificacion() + '\''
                + ", estado=" + estado
                + '}';
    }
}
