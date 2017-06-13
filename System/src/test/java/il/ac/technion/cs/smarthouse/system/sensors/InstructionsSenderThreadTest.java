package il.ac.technion.cs.smarthouse.system.sensors;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since 07.06.17
 * */
public class InstructionsSenderThreadTest {

    private static class TestInteractiveSensor extends InteractiveSensor {
        Integer numOfInstruction = 0;
        
        public TestInteractiveSensor(){
            super(getCommName(), Random.sensorId(), "MyAlias", 
                            Arrays.asList(), 
                            Arrays.asList(getInstructionPath()));
           setInstructionHandler((path, inst)->  {
               System.out.println("path:"+path+"; inst:"+inst);
               numOfInstruction += inst.equals(true+"")? 1: 0;
               return true;
           });
        }
        
        public static String getCommName(){
            return "testSensor";
        }
        
        public static String getInstructionPath(){
            return "acu.state";
        }
        
        public Boolean didGetInstruction(){
            return numOfInstruction > 0;
        }
        
        public Integer numOfReceivedInstructions(){
            return numOfInstruction;
        }
    }
    
    private static class TestISdata extends SensorData {
        
    }
    
    //system
    private FileSystem fileSystem;
    private SensorsLocalServer server;
    private SystemCore systemCore;
    private SensorsService sensorsService;
    
    //sensor + app
    private TestInteractiveSensor sensor;
    private SensorApi<TestISdata> sensorRepresentingObj;
    
    @Before public void initSystem(){
        systemCore = new SystemCore();
        fileSystem = systemCore.getFileSystem();
        server = new SensorsLocalServer(fileSystem);
        sensorsService = (SensorsService)systemCore.getSystemServiceManager().getService(ServiceType.SENSORS_SERVICE);
        new Thread(server).start();
        
        sensor = new TestInteractiveSensor();
        sensorRepresentingObj = sensorsService.getSensor(TestInteractiveSensor.getCommName(), TestISdata.class);
    }
    
    @After public void closeServerSocket(){
        server.closeSockets();
    }

    //------------------------- private helpers -------------------------------------------
    private void instructInc(){
        sensorRepresentingObj.instruct(true+"", TestInteractiveSensor.getInstructionPath());
    }
    
    private void fullyRegisterSensor(){
        while(!sensor.register());
        while(!sensor.registerInstructions());
    }
    
    //------------------------- tests -----------------------------------------------------
    @Test public void GetsInstuctionOnPathTest() {
        fullyRegisterSensor();
        instructInc();
       
        while(!sensor.operate());
        assert sensor.didGetInstruction();
    }
    
    //TODO: move this test to InteractiveSensorTest class [create mutual parent class with initSystem]
    @Test public void GetsInstuctionByPollingTest() {
        fullyRegisterSensor();
        
        final long waitTime = 500;
        sensor.pollInstructions(waitTime);
        
        final Integer times =4;
        new Thread(()->{
            for(int i=0; i<times; ++i){
                instructInc();
                try {
                    Thread.sleep(2 * waitTime);
                } catch (InterruptedException e) {
                    assert false;
                }
            }
        }).start();
        
        
        try {
            Thread.sleep(2*waitTime*(times+1)); //+1 to assure the other threads are done
        } catch (InterruptedException e) {
            assert false;
        } 
       
        assert sensor.numOfReceivedInstructions() == times;
    }
    
    @Test public void GetsAlreadyWaitingInstructionTest() {
        instructInc();
        
        fullyRegisterSensor();
       
        while(!sensor.operate());
        assert sensor.didGetInstruction();
    }

}
