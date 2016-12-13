package il.ac.technion.cs.eldery.networking.messages;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Yarden
 * @author Sharon
 * @since 11.12.16 */
public class RegisterMessage extends Message {
    private Sensor sensor;

    public RegisterMessage(Sensor sensor) {
        super(MessageType.REGISTRATION);

        this.sensor = sensor;
    }

    @Override public String toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", MessageType.REGISTRATION + "");
        json.addProperty("id", sensor.getId());
        JsonArray types = new JsonArray();
        for (String type : sensor.getTypes())
            types.add(type);
        json.add("sensor_types", types);
        JsonArray observations = new JsonArray();
        for (String observation : sensor.getObservationsNames())
            observations.add(observation);
        json.add("observations", observations);
        return json + "";
    }
}
