package Utilidades;

import DAO.PresoDAO;
import Model.Preso;
import javax.swing.JOptionPane;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class Validador {
    
    private final PresoDAO presoDAO;
    
    public Validador(PresoDAO presoDAO) {
        this.presoDAO = presoDAO;
    }
    
    public void validarIdentificacionUnica(String identificacion) {
        validarFormatoIdentificacion(identificacion);
        
        if (presoDAO.existePresoConIdentificacion(identificacion)) {
            throw new IllegalArgumentException("Ya existe un preso con esta identificación en el sistema");
        }
    }
    
    public static void validarFormatoIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificación es obligatoria");
        }
        
        if (!Pattern.matches("^\\d{9,15}$", identificacion)) {
            throw new IllegalArgumentException("La identificación debe tener entre 9 y 15 dígitos");
        }
    }
    
    
    public static void validarDatosPersonales(String primerNombre, String primerApellido, 
                                           String edad, String nacionalidad) {
        validarCampoObligatorio("primer nombre", primerNombre);
        validarCampoObligatorio("primer apellido", primerApellido);
        validarEdad(edad);
        validarCampoObligatorio("nacionalidad", nacionalidad);
        
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$", primerNombre)) {
            throw new IllegalArgumentException("El primer nombre solo puede contener letras y espacios (2-50 caracteres)");
        }
        
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$", primerApellido)) {
            throw new IllegalArgumentException("El primer apellido solo puede contener letras y espacios (2-50 caracteres)");
        }
    }
    
    public static void validarMedidasFisicas(String estaturaStr, String pesoStr) {
        validarCampoObligatorio("estatura", estaturaStr);
        validarCampoObligatorio("peso", pesoStr);
        
        try {
            float estatura = Float.parseFloat(estaturaStr);
            if (estatura < 1.0 || estatura > 2.5) {
                throw new IllegalArgumentException("La estatura debe estar entre 1.0 y 2.5 metros");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La estatura debe ser un número válido");
        }
        
        try {
            float peso = Float.parseFloat(pesoStr);
            if (peso < 30 || peso > 300) {
                throw new IllegalArgumentException("El peso debe estar entre 30 y 300 kg");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El peso debe ser un número válido");
        }
    }
    
    public static void validarSeleccion(String campo, Object valor, String valorPorDefecto) {
        if (valor == null || valor.toString().equals(valorPorDefecto)) {
            throw new IllegalArgumentException("Debe seleccionar un valor para " + campo);
        }
    }
    
 
    
    public static void validarCampoObligatorio(String campo, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }
    
    public static void validarListaNoVacia(String campo, List<?> lista) {
        if (lista == null || lista.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un " + campo);
        }
    }
    
 
    
    public static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
  
  
    
    public static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$", nombre)) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios (2-50 caracteres)");
        }
    }
    
    public static void validarEdad(String edadStr) {
    try {
        int edad = Integer.parseInt(edadStr);
        if (edad < 18 || edad > 110) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 110 años");
        }
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("La edad debe ser un número válido");
    }
}
    
    public static void validarEstatura(String estaturaStr) {
        validarCampoObligatorio("estatura", estaturaStr);
        
        try {
            float estatura = Float.parseFloat(estaturaStr);
            if (estatura < 1.0 || estatura > 2.5) {
                throw new IllegalArgumentException("La estatura debe estar entre 1.0 y 2.5 metros");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La estatura debe ser un número válido");
        }
    }
    
    public static void validarSexoFemenino(Object sexo) {
    if (sexo == null || sexo.toString().trim().isEmpty()) {
        throw new IllegalArgumentException("El campo sexo es obligatorio");
    }
    
    String valor = sexo.toString().trim().toUpperCase();
    
    if (!valor.equals("F")) {
        throw new IllegalArgumentException("Esta es una cárcel exclusiva para mujeres. Solo se permite el ingreso de personas de sexo femenino (F).");
    }
}

    
    public static void validarPeso(String pesoStr) {
        validarCampoObligatorio("peso", pesoStr);
        
        try {
            float peso = Float.parseFloat(pesoStr);
            if (peso < 30 || peso > 300) {
                throw new IllegalArgumentException("El peso debe estar entre 30 y 300 kg");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El peso debe ser un número válido");
        }
    }
    
    public static void validarSentencia(int años, int meses) {
        if (años < 0 || meses < 0 || meses > 11 || (años == 0 && meses == 0)) {
            throw new IllegalArgumentException("La sentencia debe ser entre 1 mes y 99 años");
        }
    }
    
    public static void validarFechaNoFutura(LocalDate fecha, String campo) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de " + campo + " es obligatoria");
        }
        
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de " + campo + " no puede ser futura");
        }
    }
    
    public static void validarImagen(File imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Debe seleccionar una foto del preso");
        }
        
        long maxSize = 5 * 1024 * 1024; 
        if (imagen.length() > maxSize) {
            throw new IllegalArgumentException("La imagen no puede superar los 5MB");
        }
        
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }
    }
}