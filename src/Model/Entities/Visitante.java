package Model.Entities;

import Model.Constants.EstadoVisitanteEnum;

public class Visitante extends Persona {

    private String fotoPath;
    private String email;
    private static int ultimoId = 0;
    private int id;
    private EstadoVisitanteEnum estado;
    private String razonDeshabilitacion;

    public Visitante(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
            int edad, String sexo, String nacionalidad, String identificacion,
            String fotoPath, String email) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, sexo, nacionalidad, identificacion);
        this.id = ++ultimoId;
        this.fotoPath = fotoPath;
        this.email = email;
        this.estado = EstadoVisitanteEnum.HABILITADO;
        this.razonDeshabilitacion = null;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFotoPath() {
        return this.fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static int getUltimoId() {
        return ultimoId;
    }

    public static void setUltimoId(int ultimoId) {
        Visitante.ultimoId = ultimoId;
    }

    public EstadoVisitanteEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisitanteEnum estado) {
        this.estado = estado;
    }

    public String getRazonDeshabilitacion() {
        return razonDeshabilitacion;
    }

    public void setRazonDeshabilitacion(String razonDeshabilitacion) {
        this.razonDeshabilitacion = razonDeshabilitacion;
    }
}
