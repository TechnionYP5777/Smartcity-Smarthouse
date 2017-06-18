package il.ac.technion.cs.smarthouse.sensors;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsLocalServer;

/**
 * @author Elia Traore
 * @since Jun 18, 2017
 */
public class GenericSensorTest {
    private class Counter {
        Integer c = 0;
        public Counter() {
        }
        public void inc(){c++;}
        public Integer get(){return c;}
    }
    
    private SensorBuilder builder;
    private SystemCore systemCore;
    private SensorsLocalServer server;

    @Before
    public void init() {
        builder = new SensorBuilder().setCommname("testComm").setAlias("testAlias");
        systemCore = new SystemCore();
        server = new SensorsLocalServer(systemCore.getFileSystem());
        new Thread(server).start();
    }


    @Test
    public void sendMessageSuccesfully() throws InterruptedException {
        final Counter c = new Counter();
        String sendPath = "foo.send";
        
        systemCore.getFileSystem().subscribe((path, val) -> c.inc(),
                        FileSystemEntries.SENSORS_DATA.buildPath(sendPath));
        final Map<String, Object> data = new HashMap<>();
        
        data.put(sendPath, "yo");
        builder.addInfoSendingPath(sendPath, String.class).build().sendMessage(data);
        Thread.sleep(2000);
        Assert.assertEquals((Integer) 1, c.get());
    }

    @Test
    public void StreamMessagesSuccesfully() {
        //todo
    }
}
