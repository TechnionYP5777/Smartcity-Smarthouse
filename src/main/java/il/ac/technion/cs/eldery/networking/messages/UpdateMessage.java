package il.ac.technion.cs.eldery.networking.messages;

import java.util.Map;

import com.google.gson.JsonObject;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Sharon
 * @since 11.12.16 */
public class UpdateMessage extends Message {
    Sensor sensor;
    Map<Object, Object> data;

    public UpdateMessage(Sensor sensor, Map<Object, Object> data) {
        this.sensor = sensor;
        this.data = data;
    }

    
    @Override public String toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", MessageType.UPDATE + "");
        json.addProperty("id", sensor.getId());
        JsonObject dataJson = new JsonObject();
        data.entrySet().forEach(entry -> {
            dataJson.addProperty(entry.getKey() + "", entry.getValue() + "");
        });
        json.add("data", dataJson);
        return json + "";
    }
}
