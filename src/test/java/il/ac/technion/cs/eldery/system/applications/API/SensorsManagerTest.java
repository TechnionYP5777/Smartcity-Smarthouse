/**
 * 
 */
package il.ac.technion.cs.eldery.system.applications.API;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import il.ac.technion.cs.eldery.sensors.InteractiveSensor;
import il.ac.technion.cs.eldery.system.services.ServiceType;
import il.ac.technion.cs.eldery.system.services.sensors_service.SensorData;
import il.ac.technion.cs.eldery.system.services.sensors_service.SensorsManager;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import il.ac.technion.cs.eldery.utils.Random;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class SensorsManagerTest {    
    // general tests data
    @Rule public TestName testName = new TestName();
    //todo: ELIA create testdata class instead of all the maps
    private static Map<String, String> sensorIds = new HashMap<>();
    private static Map<String, String> commName = new HashMap<>();
    private static Map<String, String[]> obserNames = new HashMap<>();
    private static final Core core = new Core(); //todo: init core in every test?
    
    // single test info
    private SensorsManager sensorsManager;
    private InteractiveSensor sensor;
    private String currentTestName;
    
    @BeforeClass public static void setup_DataMaps(){
        String test = "inquireAboutExistingSensorReturnsId";
        sensorIds.put(test, Random.sensorId());
        commName.put(test, "iSensor"+test.substring(0,20));
        obserNames.put(test, null);
        
        test = "inquireAboutNoneExistingDoesntReturnId";
        sensorIds.put(test, Random.sensorId());
        commName.put(test, test.substring(0,20));
        obserNames.put(test, null);
        
        test = "subscribeToNoneExistingThrows";
        sensorIds.put(test, Random.sensorId());
        commName.put(test, test.substring(0,20));
        obserNames.put(test, null);
        
        test = "subscribeToExistingWorks";
        sensorIds.put(test, Random.sensorId());
        commName.put(test, test.substring(0,20));
        obserNames.put(test, new String[]{"b"});
    }
    
    @AfterClass public static void teardown_killSensorHandler(){
        Thread t = core.getSensorHandlerThread();
        if(t.isAlive())
            t.interrupt();
        ((SensorsHandler)core.getHandler(Handler.SENSORS)).closeSockets();
    }
    
    @Before public void tsetup_SensorsManager() throws Exception{
        sensorsManager = (SensorsManager) core.serviceManager.getService(ServiceType.SENSORS_SERVICE);
        
        currentTestName = testName.getMethodName();
        if(!sensorIds.containsKey(currentTestName)||
                !commName.containsKey(currentTestName) ||
                !obserNames.containsKey(currentTestName))
            throw new Exception("failed to initialize test enviourment for:"+ currentTestName+ ". [missing keys]");
        
        sensor = new TestSensor(sensorIds.get(currentTestName), commName.get(currentTestName), obserNames.get(currentTestName));
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
        Assert.assertEquals(sensorsManager.inquireAbout(commName.get(currentTestName)).get(0), sensorIds.get(currentTestName));
    }
    
    @Test public void inquireAboutNoneExistingDoesntReturnId(){
        for(String id: sensorsManager.inquireAbout("not "+commName.get(currentTestName)))        
            Assert.assertFalse(sensorIds.containsValue(id));
    }
    
    @Test(expected = SensorNotFoundException.class)
    public void subscribeToNoneExistingThrows() throws SensorNotFoundException{
        class TestSensorData extends SensorData {}
        sensorsManager.subscribeToSensor("not a sensor id", TestSensorData.class, tsd -> {});
    }
    
    /**
     * [[SuppressWarningsSpartan]]
     */
    @Ignore //todo: why  java.lang.IllegalStateException: Toolkit not initialized ? 
    @SuppressWarnings("boxing")
    @Test public void subscribeToExistingWorks() throws SensorNotFoundException, InterruptedException{
        class TestSensorData extends SensorData { public Boolean b;}
        Boolean[] $ = new Boolean[] {false};
        sensorsManager.subscribeToSensor(sensorIds.get(currentTestName), TestSensorData.class, o -> { $[0] =  o.b;});
        
        Map<String, String> data = new HashMap<>();
        data.put("b", "true");
        sensor.updateSystem(data);
        
        Thread.sleep(5000);
        
        Assert.assertEquals(true,$[0]);
    }
    
    public void TBD(){
        //todo:
    }
    
    
}

