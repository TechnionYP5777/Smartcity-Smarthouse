package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.sensors.stove.StoveSensor;

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
        Assert.assertEquals(MessageType.UPDATE, new UpdateMessage("00").getType());
    }

    @Test public void initialDataIsEmpty() {
        Assert.assertEquals(0, new UpdateMessage("00:11:22:33").getData().size());
    }

    @Test public void successfullyAddTwoObservations() {
        final UpdateMessage message = new UpdateMessage("00:11:22:33");
        message.addObservation("on", "true");
        message.addObservation("temp", "100");

        Assert.assertEquals(2, message.getData().size());
        assert message.getData().containsKey("on");
        assert message.getData().containsValue("true");
        assert message.getData().containsKey("temp");
        assert message.getData().containsValue("100");
    }

    @Test public void successfullyRemoveObservations() {
        final UpdateMessage message = new UpdateMessage("00:11:22:33");
        message.addObservation("on", "true");
        message.addObservation("temp", "100");
        message.addObservation("pulse", "250");
        message.removeObservation("on");
        message.removeObservation("pulse");

        Assert.assertEquals(1, message.getData().size());
        assert message.getData().containsKey("temp");
        assert message.getData().containsValue("100");
        assert !message.getData().containsKey("on");
        assert !message.getData().containsValue("true");
        assert !message.getData().containsKey("pulse");
        assert !message.getData().containsValue("250");
    }

    @Test public void dataReturnedIsCorrect() {
        final UpdateMessage message = new UpdateMessage("00:11:22:33");
        message.addObservation("on", "true");
        message.addObservation("temp", "100");

        final Map<String, String> map = new HashMap<>();
        map.put("on", "true");
        map.put("temp", "100");

        Assert.assertEquals(map, message.getData());
    }

    @Test public void correctObservationsAreReturned() {
        final UpdateMessage message = new UpdateMessage("00:11:22:33");
        message.addObservation("on", "true");
        message.addObservation("temp", "100");

        Assert.assertEquals("true", message.getObservation("on"));
        Assert.assertEquals("100", message.getObservation("temp"));
    }

    @Test public void toStringContainsAllNeededData() {
        final UpdateMessage message = new UpdateMessage("00:11:22:33");
        message.addObservation("on", "true");
        message.addObservation("temp", "100");
        final String $ = message + "";

        assert $.contains("00:11:22:33");
        assert $.contains("on");
        assert $.contains("true");
        assert $.contains("temp");
        assert $.contains("100");
    }
}
