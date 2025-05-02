package Model.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Actividad {
    private String idActividad;
    private String nombre;
    private String tipo; 
    private String dia;
    private String horario; 
    private String lugar;
    private int cupoMaximo;
    private List<String> presosAsignadosIds; 
    private String estado; 
    private int presosInscritos;
    private String responsableOficial;

    public Actividad(String idActividad, String nombre, String tipo, String dia, 
                    String horario, String lugar, int cupoMaximo, String responsableOficial) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dia = dia;
        this.horario = horario;
        this.lugar = lugar;
        this.cupoMaximo = cupoMaximo;
        this.presosAsignadosIds = new ArrayList<>();
        this.presosInscritos = presosInscritos;
        this.estado = "Activa";
        this.responsableOficial = responsableOficial;
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

    public String getEstado() {
        return estado;
    }

  

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean tieneCupoDisponible() {
        return presosAsignadosIds.size() < cupoMaximo;
    }

    public boolean asignarPreso(String idPreso) {
        if (tieneCupoDisponible() && !presosAsignadosIds.contains(idPreso)) {
            presosAsignadosIds.add(idPreso);
            return true;
        }
        return false;
    }
}