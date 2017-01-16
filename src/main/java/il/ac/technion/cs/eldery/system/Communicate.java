/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Elia Traore
 * @since Jan 16, 2017
 */
public class Communicate {
    private static String forwardMsgToAdmin(String request){
        return "The request to: "+request+ ", was foward to the community security admin.";
    }
    
    /**@return a string representing the state of the request, or null if the request failed
     * */
    public static String throughPhone(String phoneNumber){
        return forwardMsgToAdmin("call the number "+phoneNumber);
    }
    
    /**@return a string representing the state of the request, or null if the request failed
     * */
    public static String throughSms(String phoneNumber, String msg){
        return forwardMsgToAdmin("SMS the number "+phoneNumber+ " the following message - \""+msg+"\"");
    }
    
    /**@param auth an Authentactor of the fromMail email address param
     * @return a string representing the state of the request, or null if the request failed
     * */
    public static String throughEmail(String fromMail, Authenticator auth, String toMail, String msg){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");  
        properties.put("mail.smtp.socketFactory.port", "465");  
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        properties.put("mail.smtp.auth", "true");  
        properties.put("mail.smtp.port", "465"); 
        Session session = Session.getDefaultInstance(properties, auth);  
        try{  
            MimeMessage message = new MimeMessage(session);  
            message.setFrom(new InternetAddress(fromMail));  
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toMail));  
            message.setSubject("SmartHouse Alert!");  
            message.setText(msg);  
     
            // Send message  
            Transport.send(message);  
     
         }catch (MessagingException mex) {
             mex.printStackTrace();
             return null;
        }
        return "The e-mail was sent to:"+toMail; 
    }
}
