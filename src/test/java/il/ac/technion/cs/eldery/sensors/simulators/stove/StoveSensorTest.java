package il.ac.technion.cs.eldery.sensors.simulators.stove;

import org.junit.*;

import il.ac.technion.cs.eldery.sensors.SensorTest;
import il.ac.technion.cs.eldery.sensors.simulators.stove.StoveSensor;

/** @author Yarden
 * @since 10.12.16 */
@SuppressWarnings("static-method")
public class StoveSensorTest extends SensorTest {
    @Test public void observationsAreCorrect() {
        Assert.assertArrayEquals(new String[] { "on / off", "temperature" },
                (new StoveSensor("Stove sensor", "1:1:1:1", "2:2:2:2", 80)).getObservationsNames());
    }
}
