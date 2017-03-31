package il.ac.technion.cs.eldery.sensors.vitals;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.eldery.sensors.SensorTest;

/** @author Yarden
 * @since 31.3.17 */
@SuppressWarnings("static-method")
public class VitalsSensorTest extends SensorTest {
    @Test public void observationsAreCorrect() {
        Assert.assertArrayEquals(new String[] { "pulse", "systolicBP", "diastolicBP" },
                new VitalsSensor("1:1:1:1", "iVitals", "2:2:2:2", 80).getObservationsNames());
    }
}
