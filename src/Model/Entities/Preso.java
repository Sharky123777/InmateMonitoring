package Model.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Preso extends Persona {

    private float estatura;
    private float peso;
    private List<Delito> delitos = new ArrayList<>();
    private ExpedienteJudicial expediente;
    private String nivelDeSeguridad;
    private String seccionAsignada;
    private String condicion;
    private String celdaAsignada;
    private boolean enAislamiento = false;
    private String nivelDeRiesgo;
    private int numeroDeVisitas = 0;
    private String grupoSanguineo;
    private String fotoPath;
    private String estado = "ACTIVO"; 
    private int id = 0;
    private List<String> actividadesAsignadasIds; 
    private List<String> actividadesCanceladasIds; 
        private LocalDate fechaDefuncion;

    

    public Preso(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
            int edad, String sexo, String nacionalidad, String identificacion,
            float estatura, float peso, List<Delito> delitos,
            String nivelDeSeguridad, String seccionAsignada, String condicion,
            String celdaAsignada, boolean enAislamiento, String nivelDeRiesgo,
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
        this.nivelDeRiesgo = nivelDeRiesgo;
        this.numeroDeVisitas = numeroDeVisitas;
        this.grupoSanguineo = grupoSanguineo;
        this.fotoPath = fotoPath;
        this.id = id;

        this.expediente = crearExpedienteBasico();
    }

    public LocalDate getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(LocalDate fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    
    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean puedeSerAsignadoAActividad() {
        return "ACTIVO".equals(estado) && !enAislamiento;
    }

    public List<String> getActividadesAsignadasIds() {
        if (actividadesAsignadasIds == null) {
            actividadesAsignadasIds = new ArrayList<>();
        }
        return actividadesAsignadasIds;
    }
    
    public void setActividadesAsignadasIds(List<String> actividadesAsignadasIds) {
        this.actividadesAsignadasIds = actividadesAsignadasIds;
    }
    
    public List<String> getActividadesCanceladasIds() {
        if (actividadesCanceladasIds == null) {
            actividadesCanceladasIds = new ArrayList<>();
        }
        return actividadesCanceladasIds;
    }
    
    public void setActividadesCanceladasIds(List<String> actividadesCanceladasIds) {
        this.actividadesCanceladasIds = actividadesCanceladasIds;
    }
    
    public void agregarActividadAsignada(String idActividad) {
        if (puedeSerAsignadoAActividad()) {
            getActividadesAsignadasIds().add(idActividad);
        } else {
            throw new IllegalStateException("El preso no puede ser asignado a actividades en su estado actual");
        }
    }
    
    public void marcarActividadCancelada(String idActividad) {
        if (getActividadesAsignadasIds().remove(idActividad)) {
            getActividadesCanceladasIds().add(idActividad);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private ExpedienteJudicial crearExpedienteBasico() {
        ExpedienteJudicial expediente = new ExpedienteJudicial();
        expediente.setCodigoExpediente(GeneradorDeCodigos.generarCodigoExpediente());
        expediente.setNumeroRegistro(GeneradorDeCodigos.generarNumeroRegistro());
        expediente.setFechaApertura(LocalDate.now());
        expediente.setDelitos(this.delitos);
        expediente.setJuzgado("Juzgado de Ejecución Penal");
        expediente.setNivelRiesgo(this.nivelDeRiesgo);
        return expediente;
    }

    private void actualizarExpediente() {
        if (this.expediente != null) {
            this.expediente.setDelitos(this.delitos);
        }
    }

    public Sentencia getSentenciaTotal() {
        int totalAños = 0;
        int totalMeses = 0;
        LocalDate fechaIngreso = null;

        if (!delitos.isEmpty()) {
            fechaIngreso = delitos.get(0).getSentencia().getFechaIngreso();

            for (Delito delito : delitos) {
                totalAños += delito.getSentencia().getAños();
                totalMeses += delito.getSentencia().getMeses();
            }

            totalAños += totalMeses / 12;
            totalMeses = totalMeses % 12;
        }

        return new Sentencia(totalAños, totalMeses, fechaIngreso);
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
        actualizarExpediente();
    }

    public void agregarDelito(Delito delito) {
        if (delito != null) {
            this.delitos.add(delito);
            actualizarExpediente();
        }
    }

    public boolean eliminarDelito(Delito delito) {
        boolean eliminado = this.delitos.remove(delito);
        if (eliminado) {
            actualizarExpediente();
        }
        return eliminado;
    }

    public ExpedienteJudicial getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedienteJudicial expediente) {
        this.expediente = expediente;
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
        if (this.expediente != null) {
            this.expediente.setNivelRiesgo(nivelDeRiesgo);
        }
    }

    public int getNumeroDeVisitas() {
        return numeroDeVisitas;
    }

    public void setNumeroDeVisitas(int numeroDeVisitas) {
        this.numeroDeVisitas = numeroDeVisitas;
        if (this.expediente != null) {
            this.expediente.setTotalVisitas(numeroDeVisitas);
        }
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

    public String getNumeroRegistro() {
        return expediente.getNumeroRegistro();
    }

    public String getCodigoExpediente() {
        return expediente.getCodigoExpediente();
    }

    public String getDatosExpedienteBasico() {
        StringBuilder sb = new StringBuilder();
        sb.append("Registro: ").append(getNumeroRegistro())
                .append("\nExpediente: ").append(getCodigoExpediente())
                .append("\nDelitos:");
        for (Delito delito : delitos) {
            sb.append("\n- ").append(delito.getNombre())
                    .append(" (Código: ").append(delito.getCodigo())
                    .append(", Gravedad: ").append(delito.getGravedad())
                    .append(", Fecha: ").append(delito.getFechaComision())
                    .append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Preso{"
                + "nombreCompleto='" + getNombreCompleto() + '\''
                + ", identificacion='" + getIdentificacion() + '\''
                + ", delitos=" + delitos.size()
                + ", estado=" + estado
                + '}';
    }
}