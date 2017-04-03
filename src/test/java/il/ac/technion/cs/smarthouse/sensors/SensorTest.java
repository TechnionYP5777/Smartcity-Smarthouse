package il.ac.technion.cs.smarthouse.sensors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsHandler;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public class SensorTest {
    private Sensor sensor;
    private DatabaseHandler databaseHandler;
    private Thread sensorsHandlerThread;

    @Before public void initSensor() {
        sensor = new Sensor("1", "iStoves", "127.0.0.1", 40001) {
            @Override public String[] getObservationsNames() {
                return new String[] { "name", "last name" };
            }
        };

        databaseHandler = new DatabaseHandler();
        sensorsHandlerThread = new Thread(new SensorsHandler(databaseHandler));
    }

    @After public void stopThreads() {
        if (sensorsHandlerThread.isAlive())
            sensorsHandlerThread.interrupt();
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
