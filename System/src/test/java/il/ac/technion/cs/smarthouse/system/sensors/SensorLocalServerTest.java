package il.ac.technion.cs.smarthouse.system.sensors;


import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.utils.Random;

public class SensorLocalServerTest {
    class TestBasicSensor extends Sensor{
        public TestBasicSensor(String id) {
            super("testBasicSensor", id, "MyAlias", new ArrayList<>(), new ArrayList<>());
        }
    }
    class TestInteractionSensor extends InteractiveSensor{

        public TestInteractionSensor(String id) {
            super("TestInteractionSensor", id, "MyAlias", new ArrayList<>(), new ArrayList<>());
        }
    }
    
    String sensorId;
    FileSystem fileSystem;
    SensorsLocalServer server;
    
    
    @Before public void initServer(){
        sensorId = Random.sensorId();
        fileSystem = Mockito.mock(FileSystem.class);
        server = new SensorsLocalServer(fileSystem);
        new Thread(server).start();
    }
    
    @After public void closeServerSocket(){
        server.closeSockets();
    }
    
    final long timeout = 3000;
    @Test(timeout = timeout) public void basicSensorCanConnectTest(){
        for (Sensor s = new TestBasicSensor(sensorId) ;!s.register(););
        assert true; //if you got to this line the sensor have connected
    }
    
    @Test(timeout = timeout)  public void instructionSensorCanConnectTest(){
        InteractiveSensor s = new TestInteractionSensor(sensorId);
        while(!s.register());
        while(!s.registerInstructions());
        assert true; //if you got to this line the sensor have connected
    }
}
