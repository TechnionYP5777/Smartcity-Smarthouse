package sensors;

import org.junit.*;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public class SensorTest {
  private Sensor sensor;

  @Before public void initSensor() {
    sensor = new Sensor("Default Sensor", "1") {
      @Override protected String[] getObservationsNames() {
        return new String[] { "name", "last name" };
      }
    };
  }

  @Test public void initializedNameIsCorrect() {
    Assert.assertEquals("Default Sensor", sensor.getName());
  }

  @Test public void initializedIdIsCorrect() {
    Assert.assertEquals("1", sensor.getId());
  }

  @Test public void nameIsCorrectAfterChanging() {
    sensor.setName("Stove Sensor");
    Assert.assertEquals("Stove Sensor", sensor.getName());
  }

  @Test public void initializedObservationsNamesAreCorrect() {
    Assert.assertArrayEquals(new String[] { "name", "last name" }, sensor.getObservationsNames());
  }
}
