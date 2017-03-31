package il.ac.technion.cs.eldery.sensors.sos;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.eldery.sensors.SensorTest;

/** @author Yarden
 * @since 31.3.17 */
@SuppressWarnings("static-method")
public class SosSensorTest extends SensorTest {
    @Test public void observationsAreCorrect() {
        Assert.assertArrayEquals(new String[] { "pressed" }, new SosSensor("1:1:1:1", "iSOS", "2:2:2:2", 80).getObservationsNames());
    }
}
