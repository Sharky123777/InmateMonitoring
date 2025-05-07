package Utilidades;

import Model.Constants.RolEnum;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;

public class EmailSender {
    private static EmailSender instancia;
    private final String username;
    private final String password;
    private final Properties props;
    private final String logoPath;

    private EmailSender() {
        // Configuración del servidor de correo (ajusta estos valores)
        this.username = "imsharlok@gmail.com";
        this.password = "aydondnxwjrjhagz"; // sin espacios
        
        // Usa rutas relativas o corrige las barras invertidas
        this.logoPath = System.getProperty("user.dir") + "/src/Pictures/inpecLooooogo.png";
        
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
        if (destinatario == null || destinatario.isEmpty() || 
            usuario == null || usuario.isEmpty() || 
            contrasena == null || contrasena.isEmpty()) {
            System.err.println("Error: Datos de correo inválidos");
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
            message.setSubject("Credenciales de Acceso - Sistema de Monitoreo de Reclusos");

            // Crear parte multipart para texto e imagen
            Multipart multipart = new MimeMultipart("related");

            // Parte HTML del mensaje
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = construirMensajeHTML(usuario, contrasena, rol);
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // Parte de la imagen (logo)
            try {
                File logoFile = new File(logoPath);
                if (logoFile.exists()) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.attachFile(logoFile);
                    imagePart.setContentID("<logo>");
                    imagePart.setDisposition(MimeBodyPart.INLINE);
                    imagePart.setHeader("Content-Type", "image/png");
                    multipart.addBodyPart(imagePart);
                } else {
                    System.err.println("El archivo de logo no existe en: " + logoPath);
                }
            } catch (Exception e) {
                System.err.println("Error al adjuntar el logo: " + e.getMessage());
                // Continuar sin la imagen si hay error
            }

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a: " + destinatario);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            if (e instanceof SendFailedException) {
                SendFailedException sfe = (SendFailedException) e;
                Address[] invalid = sfe.getInvalidAddresses();
                if (invalid != null) {
                    System.err.println("Direcciones inválidas:");
                    for (Address a : invalid) {
                        System.err.println(a.toString());
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al enviar correo: " + e.getMessage());
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
        if (rol == null) return "Estimado Usuario";
        
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