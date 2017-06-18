package il.ac.technion.cs.smarthouse.utils;

import org.junit.Test;

import il.ac.technion.cs.smarthouse.utils.Communicate;

/**
 * @author Elia Traore
 * @since Jan 16, 2017
 */
public class CommunicateTest {
	@Test
	public void sendEmailFromHouseTest() {
		assert Communicate.throughEmailFromHere("smarthouse5777@gmail.com", "ya bishhhh #worksss") != null;
	}
	
	@Test
    public void phoneTest() {
        assert Communicate.throughPhone("1234").contains("1234");
    }
	
	@Test
    public void smsTest() {
        Communicate.throughSms("1234", "HI").contains("HI");
    }
}
