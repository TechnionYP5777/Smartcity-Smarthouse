package il.ac.technion.cs.smarthouse.system;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/**
 * 
 * @author Inbal Zukerman
 * @since 02-06-2017
 */
public class SensorsManagerTest {
    private final SensorsManager sManager = new SensorsManager();
    private final String sensorId = "1122";
    private final String sensorId2 = "11";

    @Test
    public void testSensorManager() {
        sManager.addSensor(sensorId);
        assert sManager.sensorExists(sensorId);

        try {
            sManager.setSensorLocation(sensorId, SensorLocation.DINING_ROOM);
            Assert.assertEquals(SensorLocation.DINING_ROOM, sManager.getSensorLocation(sensorId));
        } catch (SensorNotFoundException e) {
            assert null != null;
        }

    }

    @Test(expected = SensorNotFoundException.class)
    public void testSensorWasntFound() throws SensorNotFoundException {

        sManager.setSensorLocation(sensorId2, SensorLocation.BASEMENT);

    }

    @Test(expected = SensorNotFoundException.class)
    public void testSensorWasntFound2() throws SensorNotFoundException {

        sManager.getSensorLocation(sensorId2);

    }
}
