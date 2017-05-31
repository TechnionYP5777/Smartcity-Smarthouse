package il.ac.technion.cs.smarthouse.system.services.communication_services;

import org.junit.Test;

/**
 * @author Elia Traore
 * @since Jan 16, 2017
 */
public class CommunicateTest {
    @Test
    public void sendEmailFromHouseTest() {
        assert Communicate.throughEmailFromHere("smarthouse5777@gmail.com", "ya bishhhh #worksss") != null;
    }
}
