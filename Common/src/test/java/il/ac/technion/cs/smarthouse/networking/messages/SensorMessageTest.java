package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.utils.Random;

public class SensorMessageTest extends MessageTest {
    private final List<String> obserPaths = Arrays.asList("ACU.temp", "ACU.state");
    private final List<String> instPaths = Arrays.asList("ACU.instruct.set.state", "ACU.instruct.change.tmp");

    private InteractiveSensor sensor;
    private SensorMessage msg;
    private MessageType type = MessageType.REGISTRATION;

    @Before
    public void init() {
        sensor = new InteractiveSensor("ACUSensor", Random.sensorId(), "MyAlias", obserPaths, instPaths) {
            // Noting here
        };
    }

    @Override
    @SuppressWarnings("unused")
    protected SensorMessage defaultMessage() {
        try {
            msg = msg != null ? msg : new SensorMessage(type, sensor);
        } catch (IllegalMessageBaseExecption e) {
            // Ignoring
        }
        return msg;
    }

    @Test
    public void testType() {
        Assert.assertEquals(type, defaultMessage().getType());
    }

    @Test
    public void testSensorId() {
        Assert.assertEquals(sensor.getId(), defaultMessage().getSensorId());
    }

    @Test
    public void testCommname() {
        Assert.assertEquals(sensor.getCommname(), defaultMessage().getSensorCommName());
    }

    @Test
    public void testObserPath() {
        Assert.assertEquals(sensor.getObservationSendingPaths(), defaultMessage().getObservationSendingPaths());
    }

    @Test
    public void testInstPath() {
        Assert.assertEquals(sensor.getInstructionRecievingPaths(), defaultMessage().getInstructionRecievingPaths());
    }

    @Test
    public void testJson() throws IllegalMessageBaseExecption {
        new SensorMessage(defaultMessage().toJson()).toString();
    }

    @Test
    @SuppressWarnings("static-method")
    public void testConstractorFromMsgType() throws IllegalMessageBaseExecption {
        assert (new SensorMessage(MessageType.SUCCESS_ANSWER)).isSuccesful();
    }

    @Test
    public void testAlias() {
        Assert.assertEquals(sensor.getAlias(), defaultMessage().getAlias());
    }

    @Test
    public void testIsSuccesful() {
        Assert.assertNull(defaultMessage().isSuccesful());
    }

    @Test
    public void testIsRespond() {
        assert !defaultMessage().isRespond();
    }

    @Test
    public void testGetData() {
        assert !defaultMessage().getData().isEmpty();
    }

    @Test
    public void testSetData() {
        Map<String, String> m = new HashMap<>();
        defaultMessage().setData(m);
        Assert.assertEquals(m, defaultMessage().getData());
    }
}
