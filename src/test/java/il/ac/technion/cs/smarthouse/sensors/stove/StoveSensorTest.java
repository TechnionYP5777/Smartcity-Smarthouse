package il.ac.technion.cs.smarthouse.sensors.stove;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.SensorTest;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.utils.Random;

/** @author Yarden
 * @since 10.12.16 */
public class StoveSensorTest extends SensorTest {
    class TestSensorData extends SensorData {
        public boolean on;
        public int temperature;
    }

    @Override public void customInitSensor() {
        id = Random.sensorId();
        sensor = new StoveSensor(id, "iStove", "127.0.0.1", 40001);
        commName = "iStove";
        observations = new String[] { "on", "temperature" };

    }

    @Test public void updateSystemWorks() throws SensorNotFoundException, InterruptedException {
        ((StoveSensor) sensor).updateSystem(true, 90);

        Thread.sleep(5000);

        SensorApi<TestSensorData> s = sensorsManager.getDefaultSensor(TestSensorData.class, commName);
        Assert.assertEquals(true, s.receiveLastEntry().on);
        Assert.assertEquals(90, s.receiveLastEntry().temperature);
    }
}
