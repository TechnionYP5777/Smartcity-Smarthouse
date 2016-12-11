package il.ac.technion.cs.eldery.applications;

import org.junit.*;

import il.ac.technion.cs.eldery.system.*;
import il.ac.technion.cs.eldery.system.applications.SmartHouseApplication;
import javafx.stage.Stage;

/** @author Roy
 * @since 8.12.16 */
class MockApp extends SmartHouseApplication {
    public String testFeedbak;

    @Override public void onLoad() {
        testFeedbak = "onInstall";
    }

    @Override public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
    }
}

public class SmartHouseApplicationTest {
    MockApp testApp;
    MainSystem mainSystem;

    @Before public void createApplication() {
        testApp = new MockApp();
        mainSystem = new MainSystem();
    }

    @Test public void testSetMainSystem() {
        Assert.assertTrue(testApp.setMainSystemInstance(mainSystem));
    }

    @Test public void testOnInstall() {
        testApp.onLoad();
        Assert.assertEquals(testApp.testFeedbak, "onInstall");
    }

    @Test public void testCheckIfSensorExists() {
        Assert.assertTrue(testApp.checkIfSensorExists("id"));
    }

    @Test public void testSubscribeSensorExists() {
        Assert.assertTrue(testApp.subscribeToSensor("id"));
    }
}
