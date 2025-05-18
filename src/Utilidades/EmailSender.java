package Utilidades;

import Model.Constants.RolEnum;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import java.time.format.DateTimeFormatter;
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
        if (destinatario == null || destinatario.isEmpty()
                || usuario == null || usuario.isEmpty()
                || contrasena == null || contrasena.isEmpty()
                || rol == null) {
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

        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }"
                + ".header { text-align: center; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee; }"
                + ".credentials { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0; }"
                + ".footer { margin-top: 20px; font-size: 0.8em; color: #6c757d; text-align: center; }"
                + "h2 { color: #0056b3; margin-top: 0; }"
                + "strong { color: #343a40; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='header'>"
                + "<h2>Sistema de Monitoreo de Reclusas</h2>"
                + "</div>"
                + "<p>" + saludo + ",</p>"
                + "<p>Se han generado sus credenciales de acceso al sistema:</p>"
                + "<div class='credentials'>"
                + "<p><strong>Rol:</strong> " + rolFormateado + "</p>"
                + "<p><strong>Usuario:</strong> " + usuario + "</p>"
                + "<p><strong>Contraseña: </strong> " + contrasena + "</p>"
                + "</div>"
                + "<p>Por motivos de seguridad, le recomendamos cambiar su contraseña después del primer inicio de sesión.</p>"
                + "<p>Si no solicitó estas credenciales, por favor contacte al administrador del sistema inmediatamente.</p>"
                + "<div class='footer'>"
                + "<p>Este es un mensaje automático, por favor no responda a este correo.</p>"
                + "<p>&copy; " + java.time.Year.now().getValue() + " Sistema de Monitoreo de Reclusos. Todos los derechos reservados.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    private String obtenerSaludoPorRol(RolEnum rol) {
        if (rol == null) {
            return "Estimado/a Usuario";
        }

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
        if (rol == null) {
            return "Usuario";
        }

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

    public boolean enviarDetallesVisita(Visitante visitante, Visita visita) {
        if (visitante == null || visita == null || visitante.getEmail() == null || visitante.getEmail().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se puede enviar el correo: datos de visita o visitante incompletos",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(visitante.getEmail()));
            message.setSubject("Confirmación de Visita - Sistema Penitenciario");

            String htmlContent = construirMensajeVisitaHTML(visitante, visita);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo de visita enviado exitosamente a: " + visitante.getEmail());
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo de visita: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al enviar correo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String construirMensajeVisitaHTML(Visitante visitante, Visita visita) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        String fechaFormateada = visita.getFechaVisita().format(dateFormatter);
        String horaFormateada = visita.getHoraVisita().format(timeFormatter);

        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }"
                + ".header { text-align: center; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee; }"
                + ".details { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0; }"
                + ".footer { margin-top: 20px; font-size: 0.8em; color: #6c757d; text-align: center; }"
                + "h2 { color: #0056b3; margin-top: 0; }"
                + "strong { color: #343a40; }"
                + ".important { color: #dc3545; font-weight: bold; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='header'>"
                + "<h2>Confirmación de Visita Penitenciaria</h2>"
                + "</div>"
                + "<p>Estimado/a " + visitante.getPrimerNombre() + " " + visitante.getPrimerApellido() + ",</p>"
                + "<p>Su visita ha sido registrada exitosamente en nuestro sistema. A continuación encontrará los detalles:</p>"
                + "<div class='details'>"
                + "<p><strong>Fecha de la visita:</strong> " + fechaFormateada + "</p>"
                + "<p><strong>Hora de la visita:</strong> " + horaFormateada + "</p>"
                + "<p><strong>Duración:</strong> " + visita.getDuracionVisitaEnHoras() + " horas</p>"
                + "<p><strong>Tipo de visita:</strong> " + visita.getTipoVisita() + "</p>"
                + "<p><strong>Lugar de la visita:</strong> " + visita.getLugarVisita() + "</p>"
                + "<p><strong>Preso a visitar:</strong> " + visita.getPreso().getNombresCompletos() + " "
                + visita.getPreso().getApellidosCompletos() + "</p>"
                + "<p><strong>Identificación del preso:</strong> " + visita.getPreso().getIdentificacion() + "</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Este es un mensaje automático, por favor no responda a este correo.</p>"
                + "<p>&copy; " + java.time.Year.now().getValue() + " Sistema Penitenciario. Todos los derechos reservados.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    public boolean enviarNotificacionCancelacion(Visitante visitante, Visita visita, String motivo) {
        if (visitante == null || visita == null || visitante.getEmail() == null || visitante.getEmail().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se puede enviar el correo: datos incompletos",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(visitante.getEmail()));
            message.setSubject("Visita Cancelada - Sistema Penitenciario");

            String htmlContent = construirMensajeCancelacionHTML(visitante, visita, motivo);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Notificación de cancelación enviada a: " + visitante.getEmail());
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar notificación de cancelación: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al enviar correo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String construirMensajeCancelacionHTML(Visitante visitante, Visita visita, String motivo) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    String fechaFormateada = visita.getFechaVisita().format(dateFormatter);
    String horaFormateada = visita.getHoraVisita().format(timeFormatter);

    return "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<style>"
            + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }"
            + ".header { text-align: center; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee; }"
            + ".details { background-color: #f9f9f9; padding: 20px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #dc3545; }"
            + ".footer { margin-top: 20px; font-size: 0.8em; color: #6c757d; text-align: center; }"
            + "h2 { color: #0056b3; margin-top: 0; }"
            + "strong { color: #343a40; }"
            + ".important { color: #dc3545; font-weight: bold; }"
            + ".cancel-banner { background-color: #dc3545; color: white; padding: 10px; text-align: center; border-radius: 5px; margin: 20px 0; }"
            + ".instructions { background-color: #e9ecef; padding: 15px; border-radius: 5px; margin: 20px 0; }"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<div class='header'>"
            + "<h2>Notificación de Cancelación de Visita</h2>"
            + "</div>"
            + "<div class='cancel-banner'>"
            + "<h3>VISITA CANCELADA</h3>"
            + "</div>"
            + "<p>Estimado/a " + visitante.getPrimerNombre() + " " + visitante.getPrimerApellido() + ",</p>"
            + "<p>Lamentamos informarle que su visita programada ha sido cancelada debido a circunstancias institucionales.</p>"
            + "<div class='details'>"
            + "<h3>Detalles de la visita cancelada:</h3>"
            + "<p><strong>Fecha programada:</strong> " + fechaFormateada + "</p>"
            + "<p><strong>Hora programada:</strong> " + horaFormateada + "</p>"
            + "<p><strong>Preso:</strong> " + visita.getPreso().getNombresCompletos() + " " + visita.getPreso().getApellidosCompletos() + "</p>"
            + "<p><strong>Identificación del preso:</strong> " + visita.getPreso().getIdentificacion() + "</p>"
            + "<p><strong>Tipo de visita:</strong> " + visita.getTipoVisita() + "</p>"
            + "<p><strong>Motivo de cancelación:</strong> " + motivo + "</p>"
            + "</div>"
            + "<div class='instructions'>"
            + "<h3>¿Qué puede hacer ahora?</h3>"
            + "<ul>"
            + "<li>Para reprogramar su visita, por favor comuníquese con nuestro departamento de visitas.</li>"
            + "<li>Si tiene preguntas sobre esta cancelación, puede responder a este correo electrónico.</li>"
            + "<li>Consulte nuestro reglamento de visitas para conocer las políticas actuales.</li>"
            + "</ul>"
            + "</div>"
            + "<p>Disculpe las molestias ocasionadas y agradecemos su comprensión.</p>"
            + "<div class='footer'>"
            + "<p>Este es un mensaje automático. Para asistencia, contacte a visitas@inpec.gov.co</p>"
            + "<p>&copy; " + java.time.Year.now().getValue() + " Instituto Nacional Penitenciario y Carcelario. Todos los derechos reservados.</p>"
            + "</div>"
            + "</body>"
            + "</html>";
}
}
