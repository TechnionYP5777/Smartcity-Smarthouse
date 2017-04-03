package il.ac.technion.cs.smarthouse.sensors.vitals;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.SensorTest;
import il.ac.technion.cs.smarthouse.sensors.vitals.VitalsSensor;

/** @author Yarden
 * @since 31.3.17 */
@SuppressWarnings("static-method")
public class VitalsSensorTest extends SensorTest {
    @Test public void observationsAreCorrect() {
        Assert.assertArrayEquals(new String[] { "pulse", "systolicBP", "diastolicBP" },
                new VitalsSensor("1:1:1:1", "iVitals", "2:2:2:2", 80).getObservationsNames());
    }
}
