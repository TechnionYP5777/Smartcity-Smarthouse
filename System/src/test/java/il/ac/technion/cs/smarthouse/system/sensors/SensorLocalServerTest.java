package il.ac.technion.cs.smarthouse.system.sensors;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsLocalServer;
import il.ac.technion.cs.smarthouse.utils.Random;

public class SensorLocalServerTest {
    class TestBasicSensor extends Sensor{
        public TestBasicSensor(String id) {
            super(id, 40001);
        }
    }
    class TestInteractionSensor extends InteractiveSensor{

        public TestInteractionSensor(String id) {
            super(id, 40001, 40002);
        }
    }
    
    String sensorId;
    DatabaseHandler dbh;
    SensorsLocalServer server;
    
    
    @Before public void initServer(){
        sensorId = Random.sensorId();
        dbh = Mockito.mock(DatabaseHandler.class);
        server = new SensorsLocalServer(dbh);
        new Thread(server).start();
    }
    
    @After public  void closeServerSocket(){
        server.closeSockets();
    }
    
    @Test public void basicSensorCanConnectTest(){
        for (Sensor s = new TestBasicSensor(sensorId); !s.register(););
        
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(dbh, Mockito.times(1)).addSensor(idCaptor.capture());
        
        Assert.assertEquals(sensorId.toLowerCase(), idCaptor.getAllValues().get(0).toLowerCase());
    }
    
    @Test public void instructionSensorCanConnectTest(){
        InteractiveSensor s = new TestInteractionSensor(sensorId);
        final String[] result = new String []{"not"+sensorId};
        s.setInstructionHandler(str -> {
                                            result[0] = str;
                                            return true;
                                        });
        
        while(!s.register());
        while(!s.registerInstructions());
        server.sendInstruction(sensorId, sensorId);
        
        while(!s.operate());
        Assert.assertEquals(sensorId.toLowerCase(), result[0].toLowerCase());
    }
}
