package il.ac.technion.cs.smarthouse.system;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class takes part in managing the communication with the emergency
 * contacts
 * @author Elia Traore
 * @since Jan 16, 2017 */
public class Communicate {

    private static Logger log = LoggerFactory.getLogger(Communicate.class);

    private static String forwardMsgToAdmin(final String request) {
        return "The request to: " + request + ", was foward to the community security admin.";
    }

    /** @return a string representing the state of the request, or null if the
     *         request failed */
    public static String throughPhone(final String phoneNumber) {
        return forwardMsgToAdmin("call the number " + phoneNumber);
    }

    /** @return a string representing the state of the request, or null if the
     *         request failed */
    public static String throughSms(final String phoneNumber, final String msg) {
        return forwardMsgToAdmin("SMS the number " + phoneNumber + " the following message - \"" + msg + "\"");
    }

    /** based on:
     * http://www.javatpoint.com/example-of-sending-email-using-java-mail-api-through-gmail-server
     * @param a an Authentactor of the fromMail email address param
     * @return a string representing the state of the request, or null if the
     *         request failed */
    public static String throughEmail(final String fromMail, final Authenticator a, final String $, final String msg) {
        final Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        final Session session = Session.getDefaultInstance(properties, a);
        try {
            final MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromMail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress($));
            message.setSubject("SmartHouse Alert!");
            message.setText(msg);

            // Send message
            Transport.send(message);

        } catch (final MessagingException mex) {
            log.error("A messaging error occurred", mex);
            return null;
        }
        return "The e-mail was sent to:" + $;
    }

    /** @return a string representing the state of the request, or null if the
     *         request failed */
    public static String throughEmailFromHere(final String toMail, final String msg) {
        final String $ = "smarthouse5777@gmail.com";
        return Communicate.throughEmail($, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication($, "smart5house777");
            }
        }, toMail, msg);
    }
}
