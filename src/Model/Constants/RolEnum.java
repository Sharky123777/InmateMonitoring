package Model.Constants;

public enum RolEnum {
    DIRECTOR("Directora"),
    OFICIAL("Oficial"),
    OFICIAL_DE_REGISTRO("Oficial de Registro"),
    PERSONAL_DE_CONTROL("Personal de Control"),
    COORDINADOR_DE_ACTIVIDADES("Coordinadora de Actividades"),
    ENFERMERA("Enfermera");

    private final String nombreMostrado;

    RolEnum(String nombreMostrado) {
        this.nombreMostrado = nombreMostrado;
    }

    @Override
    public String toString() {
        return nombreMostrado;
    }

    // Método para obtener el enum desde el texto mostrado
    public static RolEnum fromDisplayText(String displayText) {
        for (RolEnum rol : RolEnum.values()) {
            if (rol.nombreMostrado.equalsIgnoreCase(displayText)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("No se encontró rol para: " + displayText);
    }
}