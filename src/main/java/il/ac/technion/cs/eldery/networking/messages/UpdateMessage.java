package il.ac.technion.cs.eldery.networking.messages;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Sharon
 * @since 11.12.16 */
public class UpdateMessage extends Message {
    private Sensor sensor;
    private Map<Object, Object> data;

    public UpdateMessage(Sensor sensor) {
        this(sensor, new HashMap<>());
    }

    public UpdateMessage(Sensor sensor, Map<Object, Object> data) {
        super(MessageType.UPDATE);

        this.sensor = sensor;
        this.data = data;
    }

    /** @return the sensor this message represents */
    public Sensor getSensor() {
        return sensor;
    }

    /** Sets a new sensor for this registration message
     * @param ¢ new sensor */
    public void setSensor(Sensor ¢) {
        this.sensor = ¢;
    }

    /** Returns an observation value given its type.
     * @param key type of the observation
     * @return value of the observation */
    public Object getObservation(Object key) {
        return data.get(key);
    }

    /** Removes an observation from the data this message contains
     * @param key observation to remove */
    public void removeObservation(Object key) {
        data.remove(key);
    }

    /** Adds a new observation to this message.
     * @param key observation type
     * @param value observation value */
    public void addObservation(Object key, Object value) {
        data.put(key, value);
    }
}
