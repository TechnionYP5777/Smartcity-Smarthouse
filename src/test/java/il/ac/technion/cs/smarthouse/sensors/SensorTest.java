package il.ac.technion.cs.smarthouse.sensors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsHandler;
import il.ac.technion.cs.smarthouse.system.services.Core;
import il.ac.technion.cs.smarthouse.system.services.Handler;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsManager;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public abstract class SensorTest {
    protected Sensor sensor;
    protected String commName = "iTest";
    protected String id;
    protected String[] observations;
    protected static Core core = null;
    protected SensorsManager sensorsManager;

    /** Here you should initialize the fields: sensor, id, observations.
     * Initialization is based on the a concrete sensor (not the abstract
     * Sensor). */
    public abstract void customInitSensor();

    @BeforeClass public static void initCore() {
        core = new Core();
    }

    @Before public void initSensor() throws Exception {
        customInitSensor();
        sensorsManager = (SensorsManager) core.serviceManager.getService(ServiceType.SENSORS_SERVICE);
        for (int i = 0;; ++i) {
            try {
                if (sensor.register())
                    break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 100)
                throw new Exception("SensorTest: registration failed");
        }
    }

    @AfterClass public static void close() {
        Thread t = core.getSensorHandlerThread();
        if (t.isAlive())
            t.interrupt();
        ((SensorsHandler) core.getHandler(Handler.SENSORS)).closeSockets();
    }

    @Test public void initializedNameIsCorrect() {
        Assert.assertEquals(commName, sensor.getCommName());
    }

    @Test public void initializedIdIsCorrect() {
        Assert.assertEquals(id, sensor.getId());
    }

    @Test public void initializedObservationsNamesAreCorrect() {
        Assert.assertArrayEquals(observations, sensor.getObservationsNames());
    }
}
