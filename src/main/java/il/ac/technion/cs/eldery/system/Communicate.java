/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import java.util.Properties;

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
    
    /**@return a string representing the state of the request, or null if the request failed
     * */
    public static String throughEmail(String fromMail, String toMail, String msg){
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "localhost");
        Session session = Session.getDefaultInstance(properties);  
        try{  
            MimeMessage message = new MimeMessage(session);  
            message.setFrom(new InternetAddress(fromMail));  
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toMail));  
            message.setSubject("SmartHouse Alert!");  
            message.setText(msg);  
     
            // Send message  
            Transport.send(message);  
            System.out.println("message sent successfully....");  
     
         }catch (MessagingException mex) {mex.printStackTrace();}
        return "The e-mail was sent to:"+toMail; 
    }
}
