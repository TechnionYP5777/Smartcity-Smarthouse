package il.ac.technion.cs.eldery.sensors;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public class SensorTest {
    private Sensor sensor;

    @Before public void initSensor() {
        sensor = new Sensor("1", "iStoves", new ArrayList<>(), "1:1:1:1", 80) {
            @Override public String[] getObservationsNames() {
                return new String[] { "name", "last name" };
            }
        };
    }

    @Test public void initializedNameIsCorrect() {
        Assert.assertEquals("iStoves", sensor.getCommName());
    }

    @Test public void initializedIdIsCorrect() {
        Assert.assertEquals("1", sensor.getId());
    }

    @Test public void initializedObservationsNamesAreCorrect() {
        Assert.assertArrayEquals(new String[] { "name", "last name" }, sensor.getObservationsNames());
    }
}
