package Model;

public class Visitante extends Persona {

    private String relacionConPreso;
    private String fotoPath;
    private String email;

    public Visitante(String nombre, String apellido, int edad, int id, String sexo, String nacionalidad, String identificacion,
            String relacionConPreso, String fotoPath, String email) {
        super(nombre, apellido, edad, id, sexo, nacionalidad, identificacion);
        this.relacionConPreso = relacionConPreso;
        this.fotoPath = fotoPath;
        this.email = email;
    }

    public String getRelacionConPreso() {
        return relacionConPreso;
    }

    public void setRelacionConPreso(String relacionConPreso) {
        this.relacionConPreso = relacionConPreso;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
