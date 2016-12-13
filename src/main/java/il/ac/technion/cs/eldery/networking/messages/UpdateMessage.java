package il.ac.technion.cs.eldery.networking.messages;

import java.util.Map;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Sharon
 * @since 11.12.16 */
public class UpdateMessage extends Message {
    private Sensor sensor;
    private Map<Object, Object> data;

    public UpdateMessage(Sensor sensor, Map<Object, Object> data) {
        super(MessageType.UPDATE);

        this.sensor = sensor;
        this.data = data;
    }
}
