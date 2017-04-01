/**
 * 
 */
package il.ac.technion.cs.eldery.system.applications.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import il.ac.technion.cs.eldery.sensors.InteractiveSensor;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import il.ac.technion.cs.eldery.utils.Random;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class ApplicationApiTest {    
    // general tests data
    @Rule public TestName testName = new TestName();
    private static Map<String, String> sensorIds = new HashMap<>();
    private static Map<String, String> commName = new HashMap<>();
    private static Map<String, String[]> obserNames = new HashMap<>();
    private static final Core core = new Core(); //todo: init core in every test?
    
    // single test info
    private SmartHouseApplication app;
    private InteractiveSensor sensor;
    private String currentTestName;
    
    @BeforeClass public static void setup_DataMaps(){
        String test = "InquireAboutExistingSensorReturnsId";
        sensorIds.put(test, Random.sensorId());
        commName.put(test, "iSensor"+test.substring(0,20));
        obserNames.put(test, null);
        
        test = "InquireAboutNoneExistingDoesntReturnId";
        sensorIds.put(test, Random.sensorId());
        commName.put(test, test.substring(0,20));
        obserNames.put(test, null);
    }
    
    @AfterClass public static void teardown_killSensorHandler(){
        Thread t = core.getSensorHandlerThread();
        if(t.isAlive())
            t.interrupt();
        ((SensorsHandler)core.getHandler(Handler.SENSORS)).closeSockets();
    }
    
    @Before public void tsetup_installAppAndSensor() throws Exception{
        app = new TestApplication();
        app.setApplicationsHandler((ApplicationsHandler)core.getHandler(Handler.APPS));
        /* assumption here ^- using the appHandler of the core is enough in order to receive apps' API 
         * services from the system. This is somewhat white-box testing and the tests will break if this 
         * changes.
        */
        
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
    
    // JUnits shouldn't be static!
    @Test @SuppressWarnings("static-method") public void InquireAboutExistingSensorReturnsId(){
      
        Assert.assertEquals(app.inquireAbout(commName.get(currentTestName)).get(0), sensorIds.get(currentTestName));
    }
    
    @Test @SuppressWarnings("static-method") public void InquireAboutNoneExistingDoesntReturnId(){
        
        for(String id: app.inquireAbout("not "+commName.get(currentTestName)))        
            Assert.assertFalse(sensorIds.containsValue(id));
    }
    
    // JUnits shouldn't be static!
    @SuppressWarnings("static-method") public void TBD(){
        //todo:
    }
    
    
}

