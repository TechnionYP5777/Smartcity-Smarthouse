package il.ac.technion.cs.eldery.sensors;

import org.junit.*;

/** @author Yarden
 * @since 10.12.16 */
@SuppressWarnings("static-method")
public class StoveSensorTest extends SensorTest {
  @Test public void observationsAreCorrect() {
    Assert.assertArrayEquals(new String[] { "on / off", "temperature" }, (new StoveSensor("Stove sensor", "1:1:1:1")).getObservationsNames());
  }
}
