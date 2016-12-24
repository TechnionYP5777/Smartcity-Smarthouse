package il.ac.technion.cs.eldery.networking.messages;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Yarden
 * @author Sharon
 * @since 11.12.16 */
public class RegisterMessage extends Message {
    public final String sensorId;
    public final String sensorCommName;
    
    /** Creates a new register message.
     * @param sensor sensor to register */
    public RegisterMessage(final String sensorId, final String sensorCommName) {
        super(MessageType.REGISTRATION);
        
        this.sensorId = sensorId;
        this.sensorCommName = sensorCommName;
    }

    @Override public String toString() {
        return "RegisterMessage [sensorId=" + sensorId + ", sensorCommName=" + sensorCommName + "]";
    }
}
