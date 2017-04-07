package il.ac.technion.cs.smarthouse.sensors.sos;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.SensorTest;
import il.ac.technion.cs.smarthouse.sensors.sos.SosSensor;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.utils.Random;

/** @author Yarden
 * @since 31.3.17 */
public class SosSensorTest extends SensorTest {

    class TestSensorData extends SensorData {
        public boolean pressed;
    }

    @Override public void customInitSensor() {
        id = Random.sensorId();
        sensor = new SosSensor(id, "iSOS", "127.0.0.1", 40001);
        commName = "iSOS";
        observations = new String[] { "pressed" };

    }

    @Test public void updateSystemWorks() throws SensorNotFoundException, InterruptedException {
        ((SosSensor) sensor).updateSystem();

        Thread.sleep(5000);

        Assert.assertEquals(true, sensorsManager.getDefaultSensor(TestSensorData.class, commName).receiveLastEntry().pressed);
    }
}
