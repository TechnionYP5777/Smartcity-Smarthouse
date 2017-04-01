/**
 * 
 */
package il.ac.technion.cs.eldery.system.applications.API;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.eldery.sensors.InteractiveSensor;
import il.ac.technion.cs.eldery.utils.Random;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class TestSensor extends InteractiveSensor {
    private String[] obserNames = null;

    /**
     * @param commName
     * @param obserNames the params that will be sent to the system
     */
    public TestSensor(String commName, String[] obserNames) {
        super(Random.sensorId(), commName, "127.0.0.1", 40001, 40002);
        this.obserNames = obserNames;
    }

    /* (non-Javadoc)
     * @see il.ac.technion.cs.eldery.sensors.Sensor#getObservationsNames()
     */
    @Override public String[] getObservationsNames() {
        return obserNames;
    }

}
