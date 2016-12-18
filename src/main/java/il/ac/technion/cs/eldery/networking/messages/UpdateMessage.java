package il.ac.technion.cs.eldery.networking.messages;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Sharon
 * @since 11.12.16 */
public class UpdateMessage extends Message {
    private Sensor sensor;
    private Map<String, String> data;

    public UpdateMessage(Sensor sensor) {
        this(sensor, new HashMap<>());
    }

    public UpdateMessage(Sensor sensor, Map<String, String> data) {
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

    /** Returns the data this message stores.
     * @return a map from the observation types to the actual observations */
    public Map<String, String> getData() {
        return this.data;
    }

    /** Returns an observation value given its type.
     * @param key type of the observation
     * @return value of the observation */
    public String getObservation(String key) {
        return data.get(key);
    }

    /** Removes an observation from the data this message contains
     * @param key observation to remove */
    public void removeObservation(String key) {
        data.remove(key);
    }

    /** Adds a new observation to this message.
     * @param key observation type
     * @param value observation value */
    public void addObservation(String key, String value) {
        data.put(key, value);
    }
}
