package Model;

public class Guardia extends Persona {
    private String segundoNombre;  // Opcional (puede ser null o "")
    private String segundoApellido;

    // Constructor
    public Guardia(String primerNombre, String segundoNombre, 
                  String primerApellido, String segundoApellido, 
                  int edad, int id, String sexo, 
                  String nacionalidad, String identificacion) {
        
        super(primerNombre, primerApellido, edad, id, sexo, nacionalidad, identificacion);
        this.segundoNombre = segundoNombre;
        this.segundoApellido = segundoApellido;
    }

    // Getters básicos (sin lógica de combinación)
    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }
}