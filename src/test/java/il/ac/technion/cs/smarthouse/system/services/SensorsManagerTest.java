/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
        
        TestData(String base) {
            this.sId = Random.sensorId();
            this.commName = base.substring(0,20);
            this.obserNames = null;
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
    
    // general tests data
    @Rule public TestName testName = new TestName();

    private static Map<String, TestData> testsInfo = new HashMap<>();
    private static final Core core = new Core(); //todo: init core in every test?
    
    // single test info
    private SensorsManager sensorsManager;
    private InteractiveSensor sensor;
    private String currentTestName;
    private TestData currentInfo;
    
    @BeforeClass public static void setup_DataMaps(){
//        SensorsManagerTest javaIsStupid = new SensorsManagerTest(); DO NOT DELETE
        
        String test = "inquireAboutExistingSensorReturnsId";
        testsInfo.put(test, new TestData(Random.sensorId(), "iSensor"+test.substring(0,20), null));
        
        test = "inquireAboutNoneExistingDoesntReturnId";
        testsInfo.put(test, new TestData(test));
        
        test = "subscribeToNoneExistingThrows";
        testsInfo.put(test, new TestData(test));
        
        test = "subscribeToExistingWorks";
        testsInfo.put(test, new TestData(test));
        testsInfo.get(test).setObserNames(new String[]{"b"});
        
    }
    
    @AfterClass public static void teardown_killSensorHandler(){
        Thread t = core.getSensorHandlerThread();
        if(t.isAlive())
            t.interrupt();
        ((SensorsHandler)core.getHandler(Handler.SENSORS)).closeSockets();
    }
    
    @Before public void testsetup_SensorsManager() throws Exception{
        sensorsManager = (SensorsManager) core.serviceManager.getService(ServiceType.SENSORS_SERVICE);
        
        currentTestName = testName.getMethodName();
        if(!testsInfo.containsKey(currentTestName))
            throw new Exception("failed to initialize test enviourment for:"+ currentTestName+ ". [missing keys]");
        
        currentInfo = testsInfo.get(currentTestName);
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
        Assert.assertEquals(testsInfo.values().stream().map(i -> i.sensorId())
                                                        .filter(id -> idsInSystem.contains(id)).count(),0);
    }
    
    @Test(expected = SensorNotFoundException.class)
    public void subscribeToNoneExistingThrows() throws SensorNotFoundException{
        class TestSensorData extends SensorData {}
        sensorsManager.subscribeToSensor("not a sensor id", TestSensorData.class, tsd -> {});
    }
    
    /**
     * [[SuppressWarningsSpartan]]
     */
    @Ignore // TODO: comment the annotation and debug 
    @SuppressWarnings("boxing")
    @Test public void subscribeToExistingWorks() throws SensorNotFoundException, InterruptedException{
        class TestSensorData extends SensorData { public Boolean b;}
        Boolean[] $ = new Boolean[] {false};
        sensorsManager.subscribeToSensor(currentInfo.sensorId(), TestSensorData.class, o -> { 
            System.out.println(""+$[0]);
            $[0] =  o == null;
            System.out.println(""+$[0]);
            });
        
        Map<String, String> data = new HashMap<>();
        data.put("b", true + "");
        sensor.updateSystem(data);
        
        Thread.sleep(5000);
        
        Assert.assertEquals(true,$[0]);
    }
    
    public void TBD(){
        //todo:
    }
    
    
}

