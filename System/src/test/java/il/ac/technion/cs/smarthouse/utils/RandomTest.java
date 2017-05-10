package il.ac.technion.cs.smarthouse.utils;

import org.junit.Assert;
import org.junit.Test;

/** @author Yarden
 * @since 1.4.17 */
@SuppressWarnings("static-method")
public class RandomTest {
    @Test public void CheckIdStructure() {
        String sensorId = Random.sensorId();
        Assert.assertEquals(":", String.valueOf(sensorId.charAt(2)));
        Assert.assertEquals(":", String.valueOf(sensorId.charAt(5)));
        Assert.assertEquals(":", String.valueOf(sensorId.charAt(8)));
    }

    @Test public void CheckIdValues() {
        for (String s : Random.sensorId().split(":")) {
            int d = Integer.decode("0x" + s);
            Assert.assertEquals(d < 256, true);
            Assert.assertEquals(d >= 0, true);
        }
    }

}
