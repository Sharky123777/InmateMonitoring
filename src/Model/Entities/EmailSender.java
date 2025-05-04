package Model.Entities;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    private static EmailSender instancia;
    private final String username;
    private final String password;
    private final Properties props;

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

    public boolean enviarCredenciales(String destinatario, String usuario, String contrasena) {
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
            message.setSubject("Credenciales de acceso - SISTEMA DE MONITOREO DE RECLUSOS");
            
            String contenido = "Estimada enfermera,\n\n"
                + "Sus credenciales de acceso al sistema son:\n\n"
                + "Usuario: " + usuario + "\n"
                + "Contraseña: " + contrasena + "\n\n"
                + "Por seguridad, cambie su contraseña después del primer inicio de sesión.\n\n"
                + "Atentamente,\nEl equipo de administración";
            
            message.setText(contenido);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}