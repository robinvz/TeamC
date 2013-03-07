package be.kdg.trips.utility;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class MailSender
{
    public static void sendMail(String subject, String text, String receiverEmail) throws MessagingException
    {
        List<InternetAddress[]> recipients = new ArrayList<>();
        recipients.add(InternetAddress.parse(receiverEmail));
        sendMail(subject, text, recipients);
    }

    public static void sendMail(String subject, String text, List<InternetAddress[]> recipients) throws MessagingException
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("tripsnoreply@gmail.com", "tripstrips");
                    }
                });

        try
        {
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            message.setText(text);
            for (InternetAddress[] recipient :recipients)
            {
                message.addRecipients(Message.RecipientType.TO, recipient);
            }
            Transport.send(message);
        }
        catch(MessagingException msgex)
        {
            throw new MessagingException("Failed to send email");
        }
    }
}
