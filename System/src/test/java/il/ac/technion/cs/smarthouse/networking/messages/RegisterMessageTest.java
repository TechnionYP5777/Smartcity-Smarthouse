package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.sensors.stove.StoveSensor;

/** @author Sharon
 * @author Yarden
 * @since 11.12.16 */
@SuppressWarnings("static-method")
public class RegisterMessageTest extends MessageTest {
    static StoveSensor sensor;
    
    @Before public void init(){
        sensor = new StoveSensor("iStoves", "00:11:22:33:44:55", 80);
    }
    @Override protected Message defaultMessage() {
        return new RegisterMessage(sensor);
    }

    @Test public void basicRegisterMessageTest() {
        final RegisterMessage message = new RegisterMessage(sensor);
        final JsonParser parser = new JsonParser();

        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }

    @Test public void basicRegisterMessageTest2() {
        final RegisterMessage message = new RegisterMessage(sensor);
        final JsonParser parser = new JsonParser();

        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }

    @Test public void toStringContainsRelevantData() {
        final String $ = new RegisterMessage(sensor) + "";

        assert $.contains("00:11:22");
        assert $.contains("iStoves");
    }
}
