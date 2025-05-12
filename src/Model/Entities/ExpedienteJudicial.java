package Model.Entities;

import Model.Constants.EstadoExpedienteEnum;
import Model.Constants.EstadoPresoEnum;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpedienteJudicial {

    private String codigoExpediente;
    private String numeroRegistro;
    private LocalDate fechaApertura;
    private EstadoExpedienteEnum estado = EstadoExpedienteEnum.ABIERTO;
    private List<Delito> delitos;
    private LocalDate fechaSentencia;
    private String juzgado;
    private int totalVisitas;
    private String nivelAdaptacion;
    private List<String> actividades;
    private String observacionesConducta;
    private String nivelRiesgo;
    private Preso preso;

    public ExpedienteJudicial(Preso preso) {
        if (preso == null) {
            throw new IllegalArgumentException("El expediente judicial requiere un preso asociado");
        }
        this.preso = preso;
        this.codigoExpediente = GeneradorDeCodigos.generarCodigoExpediente();
        this.numeroRegistro = GeneradorDeCodigos.generarNumeroRegistro();
        this.fechaApertura = LocalDate.now();
        this.delitos = new ArrayList<>(preso.getDelitos());
        this.actividades = new ArrayList<>();
        this.nivelAdaptacion = "Por evaluar";
        this.observacionesConducta = "Ninguna";
        this.juzgado = "Juzgado penal";
        this.nivelRiesgo = preso.getNivelDeRiesgo();
    }

    public ExpedienteJudicial(String codigoExpediente, String numeroRegistro, LocalDate fechaApertura,
            EstadoExpedienteEnum estado, List<Delito> delitos, LocalDate fechaSentencia,
            String juzgado, int totalVisitas, String nivelAdaptacion,
            List<String> actividades, String observacionesConducta,
            String nivelRiesgo, Preso preso) {
        this(preso);
        this.codigoExpediente = codigoExpediente != null ? codigoExpediente : GeneradorDeCodigos.generarCodigoExpediente();
        this.numeroRegistro = numeroRegistro != null ? numeroRegistro : GeneradorDeCodigos.generarNumeroRegistro();
        this.fechaApertura = fechaApertura != null ? fechaApertura : LocalDate.now();
        this.estado = estado != null ? estado : EstadoExpedienteEnum.ABIERTO;
        this.delitos = delitos != null ? delitos : new ArrayList<>(preso.getDelitos());
        this.fechaSentencia = fechaSentencia;
        this.juzgado = "Juzgado penal";
        this.totalVisitas = totalVisitas;
        this.nivelAdaptacion = nivelAdaptacion != null ? nivelAdaptacion : "Por evaluar";
        this.actividades = actividades != null ? actividades : new ArrayList<>();
        this.observacionesConducta = observacionesConducta != null ? observacionesConducta : "Ninguna";
        this.nivelRiesgo = nivelRiesgo != null ? nivelRiesgo : preso.getNivelDeRiesgo();
    }

    public String getCodigoExpediente() {
        return codigoExpediente;
    }

    public void setCodigoExpediente(String codigoExpediente) {
        this.codigoExpediente = codigoExpediente;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public EstadoExpedienteEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoExpedienteEnum estado) {
        this.estado = estado != null ? estado : EstadoExpedienteEnum.ABIERTO;
    }

    public List<Delito> getDelitos() {
        return delitos;
    }

    public void setDelitos(List<Delito> delitos) {
        this.delitos = delitos != null ? delitos : new ArrayList<>();
    }

    public LocalDate getFechaSentencia() {
        return fechaSentencia;
    }

    public void setFechaSentencia(LocalDate fechaSentencia) {
        this.fechaSentencia = fechaSentencia;
    }

    public String getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(String juzgado) {
        this.juzgado = juzgado;
    }

    public int getTotalVisitas() {
        return totalVisitas;
    }

    public void setTotalVisitas(int totalVisitas) {
        this.totalVisitas = totalVisitas;
    }

    public String getNivelAdaptacion() {
        return nivelAdaptacion;
    }

    public void setNivelAdaptacion(String nivelAdaptacion) {
        this.nivelAdaptacion = nivelAdaptacion != null ? nivelAdaptacion : "Por evaluar";
    }

    public List<String> getActividades() {
        return actividades;
    }

    public void setActividades(List<String> actividades) {
        this.actividades = actividades != null ? actividades : new ArrayList<>();
    }

    public String getObservacionesConducta() {
        return observacionesConducta;
    }

    public void setObservacionesConducta(String observacionesConducta) {
        this.observacionesConducta = observacionesConducta != null ? observacionesConducta : "Ninguna";
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public Preso getPreso() {
        return preso;
    }

    public void setPreso(Preso preso) {
        if (preso == null) {
            throw new IllegalArgumentException("El expediente judicial requiere un preso asociado");
        }
        this.preso = preso;
        sincronizarConPreso();
    }

    public void sincronizarConPreso() {
        if (preso != null) {
            this.delitos = new ArrayList<>(preso.getDelitos());
            this.nivelRiesgo = preso.getNivelDeRiesgo();
        }
    }

    public void agregarDelito(Delito delito) {
        if (delito != null) {
            delitos.add(delito);
            if (preso != null) {
                preso.agregarDelito(delito);
            }
        }
    }

    public boolean eliminarDelito(Delito delito) {
        boolean eliminado = delitos.remove(delito);
        if (eliminado && preso != null) {
            preso.eliminarDelito(delito);
        }
        return eliminado;
    }

    public void agregarActividad(String idActividad) {
        if (idActividad != null && !idActividad.isEmpty()) {
            actividades.add(idActividad);
        }
    }

    public String getDatosCompletos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expediente: ").append(codigoExpediente)
                .append("\nRegistro: ").append(numeroRegistro)
                .append("\nFecha apertura: ").append(fechaApertura)
                .append("\nEstado: ").append(estado)
                .append("\n\nDatos del Preso:\n").append(preso != null ? preso.toString() : "Ninguno")
                .append("\n\nDelitos (").append(delitos.size()).append("):");

        for (Delito delito : delitos) {
            sb.append("\n- ").append(delito.toString());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "ExpedienteJudicial{"
                + "codigoExpediente='" + codigoExpediente + '\''
                + ", preso=" + (preso != null ? preso.getNombreCompleto() : "Ninguno")
                + ", delitos=" + delitos.size()
                + '}';
    }
}
