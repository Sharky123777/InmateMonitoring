package Utilidades;

import Model.Constants.RolEnum;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.swing.JOptionPane;

public class EmailSender {
    private static EmailSender instancia;
    private final String username;
    private final String password;
    private final Properties props;

    private EmailSender() {
        this.username = "imsharlok@gmail.com";
        this.password = "aydondnxwjrjhagz";
        
        this.props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    public static synchronized EmailSender getInstancia() {
        if (instancia == null) {
            instancia = new EmailSender();
        }
        return instancia;
    }

    public boolean enviarCredenciales(String destinatario, String usuario, String contrasena, RolEnum rol) {
        // Validaciones
        if (destinatario == null || destinatario.isEmpty() || 
            usuario == null || usuario.isEmpty() || 
            contrasena == null || contrasena.isEmpty() || 
            rol == null) {
            JOptionPane.showMessageDialog(null, "Error: Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!destinatario.contains("@") || !destinatario.endsWith(".com")) {
            JOptionPane.showMessageDialog(null, "Error: Formato de correo inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Credenciales de Acceso - Sistema de Monitoreo de Reclusas");

            
            String htmlContent = construirMensajeHTML(usuario, contrasena, rol);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al enviar correo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al enviar correo: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado al enviar correo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String construirMensajeHTML(String usuario, String contrasena, RolEnum rol) {
        String saludo = obtenerSaludoPorRol(rol);
        String rolFormateado = formatearRol(rol);
        
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { text-align: center; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee; }" +
               ".credentials { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
               ".footer { margin-top: 20px; font-size: 0.8em; color: #6c757d; text-align: center; }" +
               "h2 { color: #0056b3; margin-top: 0; }" +
               "strong { color: #343a40; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='header'>" +
               "<h2>Sistema de Monitoreo de Reclusas</h2>" +
               "</div>" +
               "<p>" + saludo + ",</p>" +
               "<p>Se han generado sus credenciales de acceso al sistema:</p>" +
               "<div class='credentials'>" +
               "<p><strong>Rol:</strong> " + rolFormateado + "</p>" +
               "<p><strong>Usuario:</strong> " + usuario + "</p>" +
               "<p><strong>Contraseña: </strong> " + contrasena + "</p>" +
               "</div>" +
               "<p>Por motivos de seguridad, le recomendamos cambiar su contraseña después del primer inicio de sesión.</p>" +
               "<p>Si no solicitó estas credenciales, por favor contacte al administrador del sistema inmediatamente.</p>" +
               "<div class='footer'>" +
               "<p>Este es un mensaje automático, por favor no responda a este correo.</p>" +
               "<p>&copy; " + java.time.Year.now().getValue() + " Sistema de Monitoreo de Reclusos. Todos los derechos reservados.</p>" +
               "</div>" +
               "</body>" +
               "</html>";
    }

    private String obtenerSaludoPorRol(RolEnum rol) {
        if (rol == null) return "Estimado/a Usuario";
        
        switch (rol) {
            case DIRECTOR:
                return "Estimada Directora";
            case OFICIAL:
                return "Estimada Oficial";
            case OFICIAL_DE_REGISTRO:
                return "Estimada oficial de registro";
            case PERSONAL_DE_CONTROL:
                return "Estimada Oficial";
            case COORDINADOR_DE_ACTIVIDADES:
                return "Estimada Coordinadora";
            case ENFERMERA:
                return "Estimada Enfermera";
            default:
                return "Estimada Usuaria";
        }
    }

    private String formatearRol(RolEnum rol) {
        if (rol == null) return "Usuario";
        
        String[] palabras = rol.toString().split("_");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                       .append(palabra.substring(1).toLowerCase())
                       .append(" ");
            }
        }

        return resultado.toString().trim();
    }
}