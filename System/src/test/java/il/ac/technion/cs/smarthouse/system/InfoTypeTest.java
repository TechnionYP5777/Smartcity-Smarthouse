package il.ac.technion.cs.smarthouse.system;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Inbal Zukerman
 * @date May 26, 2017
 */

public class InfoTypeTest {

    @Test
    public void toStringTest() {

        Assert.assertEquals("contact", InfoType.CONTACT.toString());
        Assert.assertEquals("home_address", InfoType.HOME_ADDRESS.toString());
        Assert.assertEquals("id", InfoType.ID.toString());
        Assert.assertEquals("name", InfoType.NAME.toString());
        Assert.assertEquals("phone_number", InfoType.PHONE_NUMBER.toString());
        Assert.assertEquals("saveall", InfoType.SAVEALL.toString());
        Assert.assertEquals("sensor", InfoType.SENSOR.toString());
        Assert.assertEquals("system", InfoType.SYSTEM.toString());
        Assert.assertEquals("user", InfoType.USER.toString());
        Assert.assertEquals("test", InfoType.TEST.toString());
    }
}
