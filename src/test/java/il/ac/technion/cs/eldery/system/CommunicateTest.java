/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import org.junit.Assert;
import org.junit.Test;

/** @author Elia Traore
 * @since Jan 16, 2017 */
public class CommunicateTest {
    @Test public void sendEmailFromHouseTest() {
        Assert.assertNotNull(Communicate.throughEmailFromHere("ron.gatenio@gmail.com", "ya bishhhh #worksss"));
    }
}
