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
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
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
        
        TestData(String sId, String commName, String[] obserNames) {
             this.sId = sId;
             this.commName = commName;
             this.obserNames = obserNames;
        }
        
        TestData(String base, String[] obserNames) {
            this.sId = Random.sensorId();
            this.commName = base.substring(0, Math.min(20, base.length()));
            this.obserNames = obserNames;
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
    
    private SensorApi<TestSensorData> getRealSensor() throws SensorNotFoundException {
        return sensorsManager.getDefaultSensor(TestSensorData.class, currentInfo.commName());
    }
    
    //* TODO: implementation changed, need to change this part... @Elia, @Ron
    // ----------------------------- tests ----------------------------- 
    @Test public void getExistingSensor() throws SensorNotFoundException {
        Assert.assertEquals(getRealSensor().getCommercialName(), currentInfo.commName());
    }    
    
    @Test(expected = SensorNotFoundException.class)
    public void getNoneExistingSensorThrows() throws SensorNotFoundException {
        sensorsManager.getDefaultSensor(TestSensorData.class, "not a sensor commercial name");
    }
    
    @Test public void subscribeToExistingWorks() throws SensorNotFoundException, InterruptedException {
        Boolean[] $ = new Boolean[] {false};
        getRealSensor().subscribe(o -> $[0] = o.b);
        
        Map<String, String> data = new HashMap<>();
        data.put("b", true + "");
        sensor.updateSystem(data);
        
        Thread.sleep(5000);
        
        Assert.assertEquals(true,$[0]);
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
        
        List<TestSensorData> dataReceived = sensorsManager.getDefaultSensor(TestSensorData.class, currentInfo.commName()).receiveLastEntries(3);
        
        for(int i=0; i < dataToBeSent.size(); i++){
            Assert.assertEquals(dataToBeSent.get(i), dataReceived.get(i).b);            
        }
    }

    @Test public void receiveLastEntryOfExistingWorks() throws SensorNotFoundException, InterruptedException{
        Map<String, String> data = new HashMap<>();
        data.put("b", true + "");
        sensor.updateSystem(data);
        
        Thread.sleep(5000);
        
        Assert.assertEquals(true, getRealSensor().receiveLastEntry().b);
    }

    @Test public void sendingInstructionToExistingWorks() throws SensorNotFoundException, InterruptedException {
        String[] $ = new String[] {""};
        sensor.setInstructionHandler(
                data -> {
                    $[0] = data.get("b");
                    return true;
                }
                );
        
        Map<String, String> instruction = new HashMap<>();
        instruction.put("b", true + "");
        getRealSensor().instruct(instruction);
        
        Thread.sleep(5000);
        sensor.operate();
        
        Assert.assertEquals(true + "", $[0]);
    }
}

