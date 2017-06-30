package il.ac.technion.cs.smarthouse.system.sensors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since 01.06.17
 */
public class SensorLocalServerTest {

    FileSystem fileSystem;
    SensorsLocalServer server;
    SensorBuilder builder;

    @Before
    public void initServer() {
        builder = new SensorBuilder().setSensorId(Random.sensorId()).setCommname("iSensor").setAlias("iAlias");
        fileSystem = Mockito.mock(FileSystem.class);
        server = new SensorsLocalServer(fileSystem);
        new Thread(server).start();
    }

    @After
    public void closeServerSocket() throws InterruptedException {
        server.closeSockets();
        Thread.sleep(1000);
    }

    final long timeout = 5000;

    @Test(timeout = timeout)
    public void basicSensorCanConnectTest() {
        builder.build().connect();
        assert true; // if you got to this line the sensor have connected
    }

    @Test(timeout = timeout)
    public void instructionSensorCanConnectTest() {
        builder.addInstructionsReceiveingPath("idk").build().connect();
        assert true; // if you got to this line the sensor have connected
    }
}
