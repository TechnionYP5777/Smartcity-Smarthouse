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
    private final String sendPath = "foo.send", recPath = "foo.receive";
    private SensorBuilder builder;
    private SystemCore systemCore;
    private SensorsLocalServer server;
    private Integer counter;

    @Before
    public void init() {
        builder = new SensorBuilder().setCommname("testComm").setAlias("testAlias")
                        .addInfoSendingPath(sendPath, String.class).addInstructionsReceiveingPath(recPath);
        systemCore = new SystemCore();
        server = new SensorsLocalServer(systemCore.getFileSystem());
        new Thread(server).start();
        counter = 0;
    }

    private void incCounter() {
        ++counter;
    }

    @Test
    public void sendMessageSuccesfully() throws InterruptedException {
        systemCore.getFileSystem().subscribe((path, val) -> incCounter(),
                        FileSystemEntries.SENSORS_DATA.buildPath(sendPath));
        final Map<String, Object> data = new HashMap<>();
        data.put(sendPath, "yo");
        builder.build().sendMessage(data);
        Thread.sleep(2000);
        Assert.assertEquals((Integer) 1, counter);
    }

    @Test
    public void StreamMessagesSuccesfully() {
        // todo: elia, implement
    }
}
