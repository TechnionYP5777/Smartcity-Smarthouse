package il.ac.technion.cs.eldery.sensors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;

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

    @After @SuppressWarnings("deprecation") public void stopThreads() {
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
