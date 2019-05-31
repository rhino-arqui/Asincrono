package integradores;

import entities.Property;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.PasswordAuthentication;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class IntegradorCorreo {
    private static final String SMTP_HOST_NAME = "smtp.office365.com";
    private static final String SMTP_HOST_PORT = "587";
    private static final String SMTP_USER = "";
    private static final String SMTP_PASSWORD = "";
    private final Properties props;
    private final Session mailSession;
    public IntegradorCorreo() {
        props = new Properties();
        System.out.println("constructor123");
        // Nombre del host de correo, es smtp.gmail.com
        props.setProperty("mail.smtp.host", SMTP_HOST_NAME);

        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "true");

        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port", SMTP_HOST_PORT);

        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");
        mailSession = Session.getInstance(props, new SMTPAuthenticator());
    }
    
    public void sendMail(Property p) throws NoSuchProviderException, MessagingException {
        Transport transport = mailSession.getTransport();
        MimeMessage message = new MimeMessage(mailSession);
        
        message.setContent("Correo de notificacion desde OPR System", "text/plain");
        message.setSubject("OPR System Notificaction");
        message.setFrom(new InternetAddress(SMTP_USER));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(p.getClientId().getEmail()));
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
        System.out.println("enviando correo a " + p.getClientId().getEmail());
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_USER;
           String password = SMTP_PASSWORD;
           return new PasswordAuthentication(username, password);
        }
    }
}
