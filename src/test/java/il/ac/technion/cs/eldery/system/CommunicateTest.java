/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Elia Traore
 * @since Jan 16, 2017
 */
public class CommunicateTest {
    @Test public void sendEmailTest(){
        Assert.assertNotNull(Communicate.throughEmail("set@campus.technion.ac.il", "eliat158@gmail.com", "ya bishhhh"));
    }
}
