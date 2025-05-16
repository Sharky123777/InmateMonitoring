package Model.Constants;

public enum RolEnum {
    DIRECTOR("Director"),
    OFICIAL("Oficial"),
    OFICIAL_DE_REGISTRO("Oficial de Registro"),
    PERSONAL_DE_CONTROL("Personal de Control"),
    COORDINADOR_DE_ACTIVIDADES("Coordinador de Actividades"),
    ENFERMERA("Enfermera/a");
    
    private final String nombre;
    
    RolEnum(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}