package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.utils.Random;

public class SensorMessageTest extends MessageTest {
    private final List<String> obserPaths = Arrays.asList("ACU.temp","ACU.state");
    private final List<String> instPaths = Arrays.asList("ACU.instruct.set.state","ACU.instruct.change.tmp");
    
    private InteractiveSensor sensor ;
    private SensorMessage msg;
    private MessageType type = MessageType.REGISTRATION;
    
    @Before public void init(){
        sensor = new InteractiveSensor("ACUSensor", Random.sensorId(), obserPaths, instPaths, 40001, 40002){};
    }
    
    @Override
    protected SensorMessage defaultMessage() {
        try{
            msg = msg!= null? msg : new SensorMessage(type, sensor);
        }catch(IllegalMessageBaseExecption e){}
        return msg;
    }
    
    @Test public void testType(){
        Assert.assertEquals(type, defaultMessage().getType());
    }
    
    @Test public void testSensorId(){
        Assert.assertEquals(sensor.getId(), defaultMessage().getSensorId());
    }
    
    @Test public void testCommname(){
        Assert.assertEquals(sensor.getCommname(), defaultMessage().getSensorCommName());
    }
    
    @Test public void testObserPath(){
        Assert.assertEquals(sensor.getObservationSendingPaths(), defaultMessage().getObservationSendingPaths());
    }
    
    @Test public void testInstPath(){
        Assert.assertEquals(sensor.getInstructionRecievingPaths(), defaultMessage().getInstructionRecievingPaths());
    }
}
