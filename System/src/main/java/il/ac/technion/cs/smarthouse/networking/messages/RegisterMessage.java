package il.ac.technion.cs.smarthouse.networking.messages;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.sensors.SensorType;

/** @author Yarden
 * @author Sharon
 * @since 11.12.16 */
public class RegisterMessage extends Message {
    public final String sensorId;
    public final String sensorCommName;
    public final SensorType sensorType;

    /** Creates a new registration message for the given sensor.
     * @param sensor sensor to register */
    public RegisterMessage(final Sensor sensor) {
        super(MessageType.REGISTRATION);

        sensorId = sensor.getId();
        sensorCommName = sensor.getCommName();
        sensorType = sensor.getType();
    }

    /** Creates a new registration message given the sensor's id and commercial
     * name.
     * @param sensorId sensor'd id
     * @param sensorCommName sensor'd commercial name */
    public RegisterMessage(final String sensorId, final String sensorCommName, final SensorType sensorType) {
        super(MessageType.REGISTRATION);

        this.sensorId = sensorId;
        this.sensorCommName = sensorCommName;
        this.sensorType = sensorType;
    }

    @Override public String toString() {
        return "RegisterMessage [sensorId=" + sensorId + ", sensorCommName=" + sensorCommName + ", sensorType=" + sensorType + "]";
    }
}
