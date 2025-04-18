package Model;

public class Persona {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private int edad;
    private String sexo;
    private String nacionalidad;
    private String identificacion;

    public Persona(String primerNombre, String segundoNombre, 
                 String primerApellido, String segundoApellido,
                 int edad, String sexo, String nacionalidad, 
                 String identificacion) {
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.edad = edad;
        this.sexo = sexo;
        this.nacionalidad = nacionalidad;
        this.identificacion = identificacion;
    }

    // Métodos básicos de acceso
    public String getPrimerNombre() {
        return primerNombre != null ? primerNombre : "";
    }

    public void setPrimerNombre(String primerNombre) {
        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El primer nombre no puede estar vacío");
        }
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre != null ? segundoNombre : "";
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido != null ? primerApellido : "";
    }

    public void setPrimerApellido(String primerApellido) {
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El primer apellido no puede estar vacío");
        }
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido != null ? segundoApellido : "";
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    // Métodos combinados mejorados
    /**
     * Devuelve los nombres combinados (primer + segundo nombre)
     * Ejemplo: "María José"
     */
    public String getNombresCompletos() {
        String nombres = getPrimerNombre();
        if (!getSegundoNombre().isEmpty()) {
            nombres += " " + getSegundoNombre();
        }
        return nombres.trim();
    }

    /**
     * Devuelve los apellidos combinados (primer + segundo apellido)
     * Ejemplo: "Pérez López"
     */
    public String getApellidosCompletos() {
        String apellidos = getPrimerApellido();
        if (!getSegundoApellido().isEmpty()) {
            apellidos += " " + getSegundoApellido();
        }
        return apellidos.trim();
    }

    /**
     * Devuelve el nombre completo (nombres + apellidos)
     * Ejemplo: "María José Pérez López"
     */
    public String getNombreCompleto() {
        return (getNombresCompletos() + " " + getApellidosCompletos()).trim();
    }

    /**
     * Versión optimizada para mostrar en tablas JTable
     * - Omite espacios extras cuando no hay segundo nombre/apellido
     * - Formato consistente para visualización
     */
    public String getNombresParaTabla() {
        return getNombresCompletos(); // Ya está optimizado
    }

    public String getApellidosParaTabla() {
        return getApellidosCompletos(); // Ya está optimizado
    }

    /**
     * Versión compacta para espacios reducidos
     * Ejemplo: "M. J. Pérez L."
     */
    public String getNombreCompacto() {
        String primerInicial = getPrimerNombre().isEmpty() ? "" : getPrimerNombre().charAt(0) + ".";
        String segundoInicial = getSegundoNombre().isEmpty() ? "" : getSegundoNombre().charAt(0) + ".";
        String primerApInicial = getPrimerApellido().isEmpty() ? "" : getPrimerApellido().charAt(0) + ".";
        String segundoApInicial = getSegundoApellido().isEmpty() ? "" : getSegundoApellido().charAt(0) + ".";
        
        return String.format("%s%s %s%s", 
               primerInicial, segundoInicial, 
               primerApInicial, segundoApInicial).trim();
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}