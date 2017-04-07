package il.ac.technion.cs.smarthouse.sensors.vitals;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.SensorTest;
import il.ac.technion.cs.smarthouse.sensors.vitals.VitalsSensor;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.utils.Random;

/** @author Yarden
 * @since 31.3.17 */
public class VitalsSensorTest extends SensorTest {
    class TestSensorData extends SensorData {
        int pulse;
        int systolicBP;
        int diastolicBP;
    }

    @Override public void customInitSensor() {
        id = Random.sensorId();
        sensor = new VitalsSensor(id, "iVitals", "127.0.0.1", 40001);
        commName = "iVitals";
        observations = new String[] { "pulse", "systolicBP", "diastolicBP" };

    }

    @Test public void updateSystemWorks() throws SensorNotFoundException, InterruptedException {
        ((VitalsSensor) sensor).updateSystem(80, 120, 90);

        Thread.sleep(5000);

        SensorApi<TestSensorData> s = sensorsManager.getDefaultSensor(TestSensorData.class, commName);
        Assert.assertEquals(80, s.receiveLastEntry().pulse);
        Assert.assertEquals(120, s.receiveLastEntry().systolicBP);
        Assert.assertEquals(90, s.receiveLastEntry().diastolicBP);
    }
}
