package Utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneradorCredenciales {
    // Caracteres permitidos para usuarios y contraseñas
    private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final String CARACTERES_ESPECIALES = "ñÑ@#$%&?¿!¡";
    private static final String CARACTERES_SEGUROS_HTML = "ñÑ@#$%&?¿!¡"; // Caracteres que no causan problemas en HTML
    
    private static final SecureRandom random = new SecureRandom();
    
    // Método para generar un nombre de usuario aleatorio
    public static String generarUsuarioAleatorio() {
        int longitud = 8 + random.nextInt(5); // Entre 8 y 12 caracteres
        StringBuilder usuario = new StringBuilder();
        
        // Asegurar al menos 1 mayúscula, 1 minúscula, 1 número y 2 caracteres especiales
        usuario.append(MAYUSCULAS.charAt(random.nextInt(MAYUSCULAS.length())));
        usuario.append(MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length())));
        usuario.append(NUMEROS.charAt(random.nextInt(NUMEROS.length())));
        usuario.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));
        usuario.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));
        
        // Completar con caracteres aleatorios
        String todosCaracteres = MAYUSCULAS + MINUSCULAS + NUMEROS + CARACTERES_SEGUROS_HTML;
        for (int i = usuario.length(); i < longitud; i++) {
            usuario.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }
        
        // Mezclar los caracteres para que no estén en orden predecible
        return mezclarCadena(usuario.toString());
    }
    
    // Método para generar una contraseña aleatoria
    public static String generarContrasenaAleatoria() {
        int longitud = 10 + random.nextInt(7); // Entre 10 y 16 caracteres
        StringBuilder contrasena = new StringBuilder();
        
        // Asegurar al menos 1 mayúscula, 1 minúscula, 1 número y 2 caracteres especiales
        contrasena.append(MAYUSCULAS.charAt(random.nextInt(MAYUSCULAS.length())));
        contrasena.append(MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length())));
        contrasena.append(NUMEROS.charAt(random.nextInt(NUMEROS.length())));
        contrasena.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));
        contrasena.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));
        
        // Completar con caracteres aleatorios
        String todosCaracteres = MAYUSCULAS + MINUSCULAS + NUMEROS + CARACTERES_SEGUROS_HTML;
        for (int i = contrasena.length(); i < longitud; i++) {
            contrasena.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }
        
        // Mezclar los caracteres para que no estén en orden predecible
        return mezclarCadena(contrasena.toString());
    }
    
    // Método para encriptar contraseñas con SHA-256
    public static String encriptarContrasena(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contrasena.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }
    
    // Método auxiliar para mezclar una cadena
    private static String mezclarCadena(String input) {
        List<Character> caracteres = new ArrayList<>();
        for (char c : input.toCharArray()) {
            caracteres.add(c);
        }
        Collections.shuffle(caracteres, random);
        
        StringBuilder resultado = new StringBuilder();
        for (char c : caracteres) {
            resultado.append(c);
        }
        return resultado.toString();
    }
    
    // Método para verificar si una contraseña coincide con su versión encriptada
    public static boolean verificarContrasena(String contrasenaIngresada, String contrasenaEncriptada) {
        return encriptarContrasena(contrasenaIngresada).equals(contrasenaEncriptada);
    }
}