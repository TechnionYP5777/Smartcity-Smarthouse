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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public abstract class SensorTest {
    private static Logger log = LoggerFactory.getLogger(SensorTest.class);

    protected Sensor sensor;
    protected String commName = "iTest";
    protected String id;
    protected String[] observations;
    protected static Core core;
    protected SensorsManager sensorsManager;

    /** Here you should initialize the fields: sensor, id, observations. It is
     * recommended to change commName as well. Initialization is based on the a
     * concrete sensor (not the abstract Sensor). */
    public abstract void customInitSensor();

    @BeforeClass public static void initCore() {
        log.debug("SensorTest: Core starting");
        core = new Core();
    }

    @Before public void initSensor() throws Exception {
        sensorsManager = (SensorsManager) core.serviceManager.getService(ServiceType.SENSORS_SERVICE);
        customInitSensor();
        for (int i = 0;; ++i) {
            try {
                if (sensor.register())
                    break;
            } catch (Exception e) {
                log.error("I/O error occurred, can't regester", e);
            }
            if (i == 100) {
                log.debug("SensorTest: Registration failed");
                throw new Exception("SensorTest: Registration failed");
            }
        }
    }

    @AfterClass public static void close() {
        log.debug("SensorTest: Core closing");
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
