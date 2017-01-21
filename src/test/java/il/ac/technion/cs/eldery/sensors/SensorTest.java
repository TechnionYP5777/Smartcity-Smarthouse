package il.ac.technion.cs.eldery.sensors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

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

    @Before public void initSensor() {
        sensor = new Sensor("1", "iStoves", "127.0.0.1", 40001) {
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
    
    @Test public void registerMessageReturnsTrueWhenHandlerIsUp() {
        new Thread(new Runnable() {
            @Override public void run() {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                new Thread(new SensorsHandler(databaseHandler)).start();
            }
        }).start();

        assert sensor.register();
    }
    
    @Test public void registerMessageReturnsFalseWhenSystemIsDown() {
        assert !sensor.register();
    }
    
    @Test public void updateMessageReturnsTrueWhenHandlerIsUp() {
        new Thread(new Runnable() {
            @Override public void run() {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                new Thread(new SensorsHandler(databaseHandler)).start();
            }
        }).start();

        sensor.updateSystem(new HashMap<>());
    }
}
