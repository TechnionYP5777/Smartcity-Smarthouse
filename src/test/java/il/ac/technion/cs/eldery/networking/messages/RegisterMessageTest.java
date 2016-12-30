package il.ac.technion.cs.eldery.networking.messages;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import il.ac.technion.cs.eldery.sensors.stove.StoveSensor;

/** @author Sharon
 * @author Yarden
 * @since 11.12.16 */
@SuppressWarnings("static-method")
public class RegisterMessageTest {
    @Test public void basicRegisterMessageTest() {
        final StoveSensor sensor = new StoveSensor("00:11:22:33:44:55", "iStoves", "1:1:1:1", 80);
        final RegisterMessage message = new RegisterMessage(sensor.getId(), sensor.getCommName());
        final JsonParser parser = new JsonParser();
        
        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }
    
    @Test public void basicRegisterMessageTest2() {
        final StoveSensor sensor = new StoveSensor("00:11:22:33:44:55", "iStoves", "1:1:1:1", 80);
        final RegisterMessage message = new RegisterMessage(sensor);
        final JsonParser parser = new JsonParser();
        
        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }

    @Test public void messageTypeIsRegistration() {
        Assert.assertEquals(MessageType.REGISTRATION, (new RegisterMessage("00", "a sensor")).getType());
    }
    
    @Test public void toStringContainsRelevantData() {
        final String $ = new RegisterMessage("00:11:22", "iStoves") + "";

        assert $.contains("00:11:22");
        assert $.contains("iStoves");
    }
}
