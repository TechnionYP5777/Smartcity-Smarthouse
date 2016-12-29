package il.ac.technion.cs.eldery.networking.messages;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import il.ac.technion.cs.eldery.sensors.stove.StoveSensor;

/** @author Sharon
 * @since 11.12.16 */
@SuppressWarnings("static-method")
public class UpdateMessageTest {
    @Test public void basicUpdateMessageTest() {
        final StoveSensor sensor = new StoveSensor("00:11:22:33:44:55", "iStoves", "127.0.0.1", 40001);
        final Map<String, String> data = new HashMap<>();
        data.put("on", Boolean.FALSE + "");
        data.put("temperature", "100");
        final UpdateMessage message = new UpdateMessage(sensor.getId(), data);
        final JsonParser parser = new JsonParser();
        
        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }

    @Test public void messageTypeIsUpdate() {
        Assert.assertEquals(MessageType.UPDATE, (new RegisterMessage("00", "a sensor")).getType());
    }
}
