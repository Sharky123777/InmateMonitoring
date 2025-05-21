package Model.Entities;

import Model.Constants.EstadoActividadesEnum;
import Model.Constants.EstadoActividadesPresoEnum;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actividad {

    private Map<String, EstadoActividadesPresoEnum> estadosPorPreso = new HashMap<>();
    private String idActividad;
    private String nombre;
    private String tipo;
    private String dia;
    private String horario;
    private String lugar;
    private int cupoMaximo;
    private List<String> presosAsignadosIds = new ArrayList<>();
    private EstadoActividadesEnum estado;
    private int presosInscritos;
    private String responsableOficial;
    private String descripcion;

    public Actividad() {
        this.estadosPorPreso = new HashMap<>();
        this.presosAsignadosIds = new ArrayList<>();
    }

    public Actividad(String idActividad, String nombre, String tipo, String dia,
            String horario, String lugar, int cupoMaximo,
            String responsableOficial, String descripcion) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dia = dia;
        this.horario = horario;
        this.lugar = lugar;
        this.cupoMaximo = cupoMaximo;
        this.responsableOficial = responsableOficial;
        this.descripcion = descripcion;

        this.presosAsignadosIds = new ArrayList<>();
        this.estadosPorPreso = new HashMap<>();
        this.estado = EstadoActividadesEnum.ACTIVA;
        this.presosInscritos = 0;
    }

    public void setEstadoPreso(String idPreso, EstadoActividadesPresoEnum nuevoEstado) {
        estadosPorPreso.put(idPreso, nuevoEstado);

        if (nuevoEstado == EstadoActividadesPresoEnum.FINALIZADA
                || nuevoEstado == EstadoActividadesPresoEnum.CANCELADA) {
            if (estadosPorPreso.getOrDefault(idPreso, null) == EstadoActividadesPresoEnum.EN_PROCESO) {
                if (presosInscritos > 0) {
                    presosInscritos--;
                }
            }
        } else if (nuevoEstado == EstadoActividadesPresoEnum.EN_PROCESO) {
            presosInscritos++;
        }
    }

    public Map<String, EstadoActividadesPresoEnum> getEstadosPorPreso() {
        return estadosPorPreso;
    }

    public void setEstadosPorPreso(Map<String, EstadoActividadesPresoEnum> estadosPorPreso) {
        this.estadosPorPreso = estadosPorPreso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoActividadesPresoEnum getEstadoPreso(String presoId) {
        return this.estadosPorPreso.getOrDefault(presoId, EstadoActividadesPresoEnum.EN_PROCESO);
    }

    public String getResponsableOficial() {
        return responsableOficial;
    }

    public void setResponsableOficial(String responsableOficial) {
        this.responsableOficial = responsableOficial;
    }

    public int getPresosInscritos() {
        return presosInscritos;
    }

    public void setPresosInscritos(int presosInscritos) {
        this.presosInscritos = presosInscritos;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public void setPresosAsignadosIds(List<String> presosAsignadosIds) {
        this.presosAsignadosIds = presosAsignadosIds;
    }

    public String getDia() {
        return dia;
    }

    public String getHorario() {
        return horario;
    }

    public String getLugar() {
        return lugar;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public List<String> getPresosAsignadosIds() {
        return presosAsignadosIds;
    }

    public EstadoActividadesEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividadesEnum estado) {
        this.estado = estado;
    }

    public boolean tieneCupoDisponible() {
        return presosAsignadosIds.size() < cupoMaximo;
    }

    public void disminuirInscritosSiCorresponde(String idPreso, EstadoActividadesPresoEnum nuevoEstado) {
        EstadoActividadesPresoEnum estadoActual = getEstadoPreso(idPreso);
        if (estadoActual == EstadoActividadesPresoEnum.EN_PROCESO
                && (nuevoEstado == EstadoActividadesPresoEnum.FINALIZADA || nuevoEstado == EstadoActividadesPresoEnum.CANCELADA)) {
            if (presosInscritos > 0) {
                presosInscritos--;
            }
        }
    }

    public boolean asignarPreso(String idPreso) {
        if (tieneCupoDisponible() && !presosAsignadosIds.contains(idPreso)) {
            presosAsignadosIds.add(idPreso);
            return true;
        }
        return false;
    }
}
