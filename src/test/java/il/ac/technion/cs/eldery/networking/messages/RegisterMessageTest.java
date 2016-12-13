package il.ac.technion.cs.eldery.networking.messages;

import org.junit.*;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import il.ac.technion.cs.eldery.sensors.stove.StoveSensor;

/** @author Yarden
 * @since 11.12.16 */
public class RegisterMessageTest {
    @SuppressWarnings("static-method") @Test public void basicRegisterMessageTest() {
        StoveSensor sensor = new StoveSensor("Stove Sensor", "00:11:22:33:44:55", "1:1:1:1", 80);
        RegisterMessage message = new RegisterMessage(sensor);
        JsonParser parser = new JsonParser();
        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }
}
