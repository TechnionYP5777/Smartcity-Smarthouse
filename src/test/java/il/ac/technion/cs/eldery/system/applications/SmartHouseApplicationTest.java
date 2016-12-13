package il.ac.technion.cs.eldery.system.applications;

import org.junit.*;

import il.ac.technion.cs.eldery.system.*;

/** @author Roy
 * @author RON
 * @since 8.12.16 */
public class SmartHouseApplicationTest {
    //MockApp testApp;
    MainSystem mainSystem;
    
    public SmartHouseApplicationTest() {
        System.out.println(getClass().getPackage().getName() + "FFFF");
        // TODO Auto-generated constructor stub
    }
    
    @Before public void createApplication() {
        //testApp = new MockApp();
        try {
            System.out.println(getClass().getPackage().getName());
            getClass().getClassLoader().loadClass("il.ac.technion.cs.eldery.system.applications.examples.MyTestApp");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mainSystem = new MainSystem();
    }

    @Test public void testSetMainSystem() {
        //Assert.assertTrue(testApp.setMainSystemInstance(mainSystem));
    }

    @Test public void testOnInstall() {
        //testApp.onLoad();
        //Assert.assertEquals(testApp.testFeedbak, "onInstall");
    }

    @Test public void testCheckIfSensorExists() {
        //Assert.assertTrue(testApp.checkIfSensorExists("id"));
    }

    @Test public void testSubscribeSensorExists() {
        //Assert.assertTrue(testApp.subscribeToSensor("id"));
    }
}
