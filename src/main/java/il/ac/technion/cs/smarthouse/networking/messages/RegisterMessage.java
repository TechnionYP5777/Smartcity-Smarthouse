package il.ac.technion.cs.smarthouse.networking.messages;

import il.ac.technion.cs.smarthouse.sensors.Sensor;

/** @author Yarden
 * @author Sharon
 * @since 11.12.16 */
public class RegisterMessage extends Message {
    public final String sensorId;
    public final String sensorCommName;

    /** Creates a new registration message for the given sensor.
     * @param sensor sensor to register */
    public RegisterMessage(final Sensor sensor) {
        super(MessageType.REGISTRATION);

        sensorId = sensor.getId();
        sensorCommName = sensor.getCommName();
    }

    /** Creates a new registration message given the sensor's id and commercial
     * name.
     * @param sensorId sensor'd id
     * @param sensorCommName sensor'd commercial name */
    public RegisterMessage(final String sensorId, final String sensorCommName) {
        super(MessageType.REGISTRATION);

        this.sensorId = sensorId;
        this.sensorCommName = sensorCommName;
    }

    @Override public String toString() {
        return "RegisterMessage [sensorId=" + sensorId + ", sensorCommName=" + sensorCommName + "]";
    }
}
