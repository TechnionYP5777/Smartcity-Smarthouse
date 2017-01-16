/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Elia Traore
 * @since Jan 16, 2017
 */
public class CommunicateTest {
    @Test public void sendEmailTest(){
        String from = "smarthouse5777@gmail.com";
        Assert.assertNotNull(Communicate.throughEmail(from, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "smart5house777");
            }
        }, "ron.gatenio@gmail.com", "ya bishhhh #worksss"));
    }
}
