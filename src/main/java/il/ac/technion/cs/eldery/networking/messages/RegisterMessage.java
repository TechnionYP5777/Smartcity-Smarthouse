package il.ac.technion.cs.eldery.networking.messages;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Yarden
 * @author Sharon
 * @since 11.12.16 */
public class RegisterMessage extends Message {
    private Sensor sensor;

    public RegisterMessage(final Sensor sensor) {
        super(MessageType.REGISTRATION);

        this.sensor = sensor;
    }

    /** @return the sensor this message represents */
    public Sensor getSensor() {
        return sensor;
    }

    /** Sets a new sensor for this registration message
     * @param ¢ new sensor */
    public void setSensor(final Sensor ¢) {
        sensor = ¢;
    }
}
