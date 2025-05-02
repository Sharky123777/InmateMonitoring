package Model.Entities;

import java.time.LocalDate;

public class Visitante extends Persona {

    private String relacionConPreso;
    private String fotoPath;
    private String email;
    private static int ultimoId = 0;
    private int id;

    public Visitante(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, LocalDate fechaNacimiento, String sexo, String nacionalidad, String identificacion) {
        super(primerNombre, segundoNombre, primerApellido, segundoApellido, fechaNacimiento, sexo, nacionalidad, identificacion);
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelacionConPreso() {
        return this.relacionConPreso;
    }

    public void setRelacionConPreso(String relacionConPreso) {
        this.relacionConPreso = relacionConPreso;
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
}
