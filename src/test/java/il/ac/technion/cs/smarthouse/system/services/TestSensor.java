package il.ac.technion.cs.smarthouse.system.services;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class TestSensor extends InteractiveSensor {
    private String[] obserNames;

    /**
     * @param commName
     * @param obserNames the params that will be sent to the system
     */
    public TestSensor(String id, final String commName, final String[] obserNames) {
        super(id != null ? id : Random.sensorId() , commName, "127.0.0.1", 40001, 40002);
        this.obserNames = obserNames;
    }

    /* (non-Javadoc)
     * @see il.ac.technion.cs.eldery.sensors.Sensor#getObservationsNames()
     */
    @Override public String[] getObservationsNames() {
        return obserNames;
    }

}
