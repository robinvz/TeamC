package be.kdg.trips.utility;

import org.apache.log4j.Logger;

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
    private static final Logger logger = Logger.getLogger(MailSender.class);

    public static void sendMail(String subject, String text, String receiverEmail) throws MessagingException
    {
        List<InternetAddress[]> recipients = new ArrayList<>();
        recipients.add(InternetAddress.parse(receiverEmail));
        handleMail(subject, text, recipients);
    }

    public static void sendMail(String subject, String text, List<String> receiverEmailList) throws MessagingException
    {
        List<InternetAddress[]> recipients = new ArrayList<>();
        for(String email : receiverEmailList)
        {
            recipients.add(InternetAddress.parse(email));
        }
        handleMail(subject, text, recipients);
    }

    private static void handleMail(String subject, String text, List<InternetAddress[]> recipients) throws MessagingException
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
            StringBuilder message = new StringBuilder();
            message.append("Failed to send email to ");
            for(InternetAddress[] recipient : recipients)
            {
                message.append(InternetAddress.toString(recipient));
                message.append(";");
            }
            logger.error(message);
            throw new MessagingException("Failed to send email");
        }
    }
}
