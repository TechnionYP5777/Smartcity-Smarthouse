/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsHandler;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsManager;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class SensorsManagerTest {    
    public static class TestData {
        String sId, commName;
        String[] obserNames;
        
        TestData(String sId, String commName, String[] obsers) {
             this.sId = sId;
             this.commName = commName;
             this.obserNames = obsers;
        }
        
        TestData(String base, String[] obsers) {
            this.sId = Random.sensorId();
            this.commName = base.substring(0,20);
            this.obserNames = obsers;
       }
        
        void setObserNames(String[] obsers){
            obserNames = obsers;
        }
        
        String sensorId(){
            return sId;
        }

        String commName(){
            return commName;
        }
        
        String[] obserNames(){
            return obserNames;
        }

    }
    class TestSensorData extends SensorData { public Boolean b;}
    
    // general tests data
    @Rule public TestName testName = new TestName();

//    private static Map<String, TestData> testsInfo = new HashMap<>();
    private static final Core core = new Core(); //todo: init core in every test?
    
    // single test info
    private SensorsManager sensorsManager;
    private InteractiveSensor sensor;
//    private String currentTestName;
    private TestData currentInfo;
    
    /*
    @BeforeClass public static void setup_DacurrentTestNametaMaps(){//todo: something less WET
//        SensorsManagerTest javaIsStupid = new SensorsManagerTest(); DO NOT DELETE
        
        String test = "inquireAboutExistingSensorReturnsId";
        testsInfo.put(test, new TestData(Random.sensorId(), "iSensor"+test.substring(0,20), null));
        
        test = "inquireAboutNoneExistingDoesntReturnId";
        testsInfo.put(test, );
        
        test = "subscribeToNoneExistingThrows";
        testsInfo.put(test, new TestData(test));
        
        test = "subscribeToExistingWorks";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
        test = "receiveLastEntriesOfNoneExistingThrows";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
        test = "receiveLastEntriesOfExistingWorks";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
        test = "receiveLastEntryOfExistingWorks";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
        test = "receiveLastEntryOfNoneExistingThrows";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
        test = "sendingInstructionToNoneExistingThrows";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
        test = "sendingInstructionToExistingWorks";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
    }
    */
    
    @AfterClass public static void teardown_killSensorHandler(){
        Thread t = core.getSensorHandlerThread();
        if(t.isAlive())
            t.interrupt();
        ((SensorsHandler)core.getHandler(Handler.SENSORS)).closeSockets();
    }
    
    @Before public void testsetup_SensorsManager() throws Exception{
        sensorsManager = (SensorsManager) core.serviceManager.getService(ServiceType.SENSORS_SERVICE);
        
        String currentTestName = testName.getMethodName();
//        if(!testsInfo.containsKey(currentTestName))
//            throw new Exception("failed to initialize test's environment for:"+ currentTestName+ ". [missing keys]");
//        
//        currentInfo = testsInfo.get(currentTestName);
        currentInfo = new TestData(currentTestName, new String[]{"b"});
        sensor = new TestSensor(currentInfo.sensorId(), currentInfo.commName(), currentInfo.obserNames());
        for (int i=0;;++i){
            try {
              System.out.println(currentTestName+"'s sensor attempts to register");
              if(sensor.register() && sensor.registerInstructions())
                  break;
            }catch(Exception e){
                e.printStackTrace();
            }
            if(i == 100)
                throw new Exception("failed to register sensor on test:"+ currentTestName); 
        }
    }
    
    // ----------------------------- tests ----------------------------- 
    @Test public void inquireAboutExistingSensorReturnsId(){
        Assert.assertEquals(sensorsManager.inquireAbout(currentInfo.commName()).get(0), currentInfo.sensorId());
    }
    
    /**
     * [[SuppressWarningsSpartan]]
     */
    
    @Test public void inquireAboutNoneExistingDoesntReturnId(){
        List<String> idsInSystem = sensorsManager.inquireAbout("not " + currentInfo.commName()); 
        Assert.assertEquals(0, idsInSystem.size());
//        Assert.assertEquals(testsInfo.values().stream().map(i -> i.sensorId())
//                                                        .filter(id -> idsInSystem.contains(id)).count(),0);
    }
    
    @Test(expected = SensorNotFoundException.class)
    public void subscribeToNoneExistingThrows() throws SensorNotFoundException{
        sensorsManager.subscribeToSensor("not a sensor id", TestSensorData.class, tsd -> {});
    }
    
    @Test public void subscribeToExistingWorks() throws SensorNotFoundException, InterruptedException{
        Boolean[] $ = new Boolean[] {false};
        sensorsManager.subscribeToSensor(currentInfo.sensorId(), TestSensorData.class, o -> {$[0] =  o.b;});
        
        Map<String, String> data = new HashMap<>();
        data.put("b", true + "");
        sensor.updateSystem(data);
        
        Thread.sleep(5000);
        
        Assert.assertEquals(true,$[0]);
    }
    
    @Test(expected = SensorNotFoundException.class)
    public void receiveLastEntriesOfNoneExistingThrows() throws SensorNotFoundException{
        sensorsManager.receiveLastEntries("not a sensor id", TestSensorData.class, 3);
    }
    
    /**
     * [[SuppressWarningsSpartan]]
     */
    @Test public void receiveLastEntriesOfExistingWorks() throws SensorNotFoundException, InterruptedException{
        List<Boolean> dataToBeSent = Arrays.asList(false, true, false);
        
        for(Boolean b : dataToBeSent){
            Map<String, String> data = new HashMap<>();
            data.put("b", b + "");
            sensor.updateSystem(data);
        }
        Thread.sleep(5000);
        
        List<TestSensorData> dataReceived = sensorsManager.receiveLastEntries(currentInfo.sensorId(), TestSensorData.class, 3);
        
        for(int i=0; i < dataToBeSent.size(); i++){
            Assert.assertEquals(dataToBeSent.get(i), dataReceived.get(i).b);            
        }
    }
    
    @Test(expected = SensorNotFoundException.class)
    public void receiveLastEntryOfNoneExistingThrows() throws SensorNotFoundException{
        sensorsManager.receiveLastEntry("not a sensor id", TestSensorData.class);
    }

    @Test public void receiveLastEntryOfExistingWorks() throws SensorNotFoundException, InterruptedException{
        Map<String, String> data = new HashMap<>();
        data.put("b", true + "");
        sensor.updateSystem(data);
        
        Thread.sleep(5000);
        
        Assert.assertEquals(true, sensorsManager.receiveLastEntry(currentInfo.sensorId(), TestSensorData.class).b);
    }
    
    @Test(expected = SensorNotFoundException.class)
    public void sendingInstructionToNoneExistingThrows() throws SensorNotFoundException{
        sensorsManager.instructSensor("not a sensor id", new HashMap<String, String>());
    }

    /**
     * [[SuppressWarningsSpartan]]
     */
    @Test public void sendingInstructionToExistingWorks() throws SensorNotFoundException, InterruptedException{
        String[] $ = new String[] {""};
        sensor.setInstructionHandler(
                data -> {
                    $[0] = data.get("b");
                    return true;
                }
                );
        
        Map<String, String> instruction = new HashMap<>();
        instruction.put("b", true + "");
        sensorsManager.instructSensor(currentInfo.sensorId(), instruction);
        
        Thread.sleep(5000);
        sensor.operate();
        
        Assert.assertEquals(true + "", $[0]);
    }
}

