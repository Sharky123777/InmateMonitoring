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

    private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final String CARACTERES_ESPECIALES = "ñÑ@#$%&?¿!¡";
    private static final String CARACTERES_SEGUROS_HTML = "ñÑ@#$%&?¿!¡";

    private static final SecureRandom random = new SecureRandom();

    public static String generarUsuarioAleatorio() {
        int longitud = 8 + random.nextInt(5);
        StringBuilder usuario = new StringBuilder();

        usuario.append(MAYUSCULAS.charAt(random.nextInt(MAYUSCULAS.length())));
        usuario.append(MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length())));
        usuario.append(NUMEROS.charAt(random.nextInt(NUMEROS.length())));
        usuario.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));
        usuario.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));

        String todosCaracteres = MAYUSCULAS + MINUSCULAS + NUMEROS + CARACTERES_SEGUROS_HTML;
        for (int i = usuario.length(); i < longitud; i++) {
            usuario.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }

        return mezclarCadena(usuario.toString());
    }

    public static String generarContrasenaAleatoria() {
        int longitud = 10 + random.nextInt(7);
        StringBuilder contrasena = new StringBuilder();

        contrasena.append(MAYUSCULAS.charAt(random.nextInt(MAYUSCULAS.length())));
        contrasena.append(MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length())));
        contrasena.append(NUMEROS.charAt(random.nextInt(NUMEROS.length())));
        contrasena.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));
        contrasena.append(CARACTERES_SEGUROS_HTML.charAt(random.nextInt(CARACTERES_SEGUROS_HTML.length())));

        String todosCaracteres = MAYUSCULAS + MINUSCULAS + NUMEROS + CARACTERES_SEGUROS_HTML;
        for (int i = contrasena.length(); i < longitud; i++) {
            contrasena.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }

        return mezclarCadena(contrasena.toString());
    }

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

    public static boolean verificarContrasena(String contrasenaIngresada, String contrasenaEncriptada) {
        return encriptarContrasena(contrasenaIngresada).equals(contrasenaEncriptada);
    }
}
