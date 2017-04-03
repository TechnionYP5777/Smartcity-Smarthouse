package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.HashMap;
import java.util.Map;

/** @author Sharon
 * @since 11.12.16 */
public class UpdateMessage extends Message {
    public final String sensorId;

    private final Map<String, String> data;

    /** Creates a new update message for the given sensor without any data.
     * @param sensorId sensor'd id */
    public UpdateMessage(final String sensorId) {
        this(sensorId, new HashMap<>());
    }

    /** Creates a new update message with initial data.
     * @param sensorId sensor's id
     * @param data data to be sent in this update message */
    public UpdateMessage(final String sensorId, final Map<String, String> data) {
        super(MessageType.UPDATE);

        this.sensorId = sensorId;
        this.data = data;
    }

    /** Returns the data this message stores.
     * @return a map from the observation types to the actual observations */
    public Map<String, String> getData() {
        return data;
    }

    /** Returns an observation value given its type.
     * @param key type of the observation
     * @return value of the observation */
    public String getObservation(final String key) {
        return data.get(key);
    }

    /** Removes an observation from the data this message contains
     * @param key observation to remove */
    public void removeObservation(final String key) {
        data.remove(key);
    }

    /** Adds a new observation to this message.
     * @param key observation type
     * @param value observation value */
    public void addObservation(final String key, final String value) {
        data.put(key, value);
    }

    @Override public String toString() {
        return "UpdateMessage [sensorId=" + sensorId + ", data=" + data + "]";
    }
}
