package Model.Entities;

import Model.Constants.RolEnum;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private static EmailSender instancia;
    private final String username;
    private final String password;
    private final Properties props;
    private final String logoPath = "C:\\Users\\gameV\\Documents\\NetBeansProjects\\InmateMonitorinG\\src\\Pictures\\inpecLooooogo.png"; // Ruta al logo de la institución

    private EmailSender() {
        // Configuración del servidor de correo (ajusta estos valores)
        this.username = "imsharlok@gmail.com";
        this.password = "aydondnxwjrjhagz"; // sin espacios
        
        this.props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    public static synchronized EmailSender getInstancia() {
        if (instancia == null) {
            instancia = new EmailSender();
        }
        return instancia;
    }

    public boolean enviarCredenciales(String destinatario, String usuario, String contrasena, RolEnum rol) {
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
            message.setSubject("Credenciales de Acceso - Sistema de Monitoreo de Reclusos");

            // Crear parte multipart para texto e imagen
            Multipart multipart = new MimeMultipart();

            // Parte HTML del mensaje
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = construirMensajeHTML(usuario, contrasena, rol);
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // Parte de la imagen (logo)
            try {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.attachFile(logoPath);
                imagePart.setContentID("<logo>");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                multipart.addBodyPart(imagePart);
            } catch (Exception e) {
                System.err.println("No se pudo adjuntar el logo: " + e.getMessage());
                // Continuar sin la imagen si hay error
            }

            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
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
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
               ".header { text-align: center; margin-bottom: 20px; }" +
               ".logo { max-width: 150px; height: auto; }" +
               ".credentials { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
               ".footer { margin-top: 20px; font-size: 0.9em; color: #777; text-align: center; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<img src='cid:logo' alt='Logo' class='logo'/>" +
               "<h2>Sistema de Monitoreo de Reclusos</h2>" +
               "</div>" +
               "<p>" + saludo + ",</p>" +
               "<p>Se han generado sus credenciales de acceso al sistema:</p>" +
               "<div class='credentials'>" +
               "<p><strong>Rol:</strong> " + rolFormateado + "</p>" +
               "<p><strong>Usuario:</strong> " + usuario + "</p>" +
               "<p><strong>Contraseña temporal:</strong> " + contrasena + "</p>" +
               "</div>" +
               "<p>Por motivos de seguridad, le recomendamos cambiar su contraseña después del primer inicio de sesión.</p>" +
               "<p>Si no solicitó estas credenciales, por favor contacte al administrador del sistema inmediatamente.</p>" +
               "<div class='footer'>" +
               "<p>Este es un mensaje automático, por favor no responda a este correo.</p>" +
               "<p>&copy; " + java.time.Year.now().getValue() + " Sistema de Monitoreo de Reclusos. Todos los derechos reservados.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }

    private String obtenerSaludoPorRol(RolEnum rol) {
        switch (rol) {
            case DIRECTOR:
                return "Estimado Director";
            case OFICIAL:
            case OFICIAL_DE_REGISTRO:
            case PERSONAL_DE_CONTROL:
                return "Estimado Oficial";
            case COORDINADOR_DE_ACTIVIDADES:
                return "Estimado Coordinador";
            case ENFERMERA:
                return "Estimada Enfermera";
            default:
                return "Estimado Usuario";
        }
    }

    private String formatearRol(RolEnum rol) {
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