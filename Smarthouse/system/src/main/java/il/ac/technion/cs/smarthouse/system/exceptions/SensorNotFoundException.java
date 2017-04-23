package il.ac.technion.cs.smarthouse.system.exceptions;

/** This exception describes an error caused by unfound sensor
 * @author Inbal Zukerman
 * @since Dec 18, 2016 */
public class SensorNotFoundException extends Exception {
    private static final long serialVersionUID = -0x544637A010876493L;

    public SensorNotFoundException(String sensorId) {
        super("Sensor with ID: " + sensorId + " was not found");
    }
}
