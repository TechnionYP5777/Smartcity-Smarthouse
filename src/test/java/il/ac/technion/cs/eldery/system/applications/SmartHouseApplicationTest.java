package il.ac.technion.cs.eldery.system.applications;

import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.eldery.system.SystemCore;

/** @author Roy
 * @author RON
 * @since 8.12.16 */
public class SmartHouseApplicationTest {
    // MockApp testApp;
    SystemCore mainSystem;

    public SmartHouseApplicationTest() {}

    @Before public void createApplication() {
        // mainSystem = new SystemCore();
    }

    @Test public void testSetMainSystem() {
        // Assert.assertTrue(testApp.setMainSystemInstance(mainSystem));
    }

    @Test public void testOnInstall() {
        // testApp.onLoad();
        // Assert.assertEquals(testApp.testFeedbak, "onInstall");
    }

    @Test public void testCheckIfSensorExists() {
        // Assert.assertTrue(testApp.checkIfSensorExists("id"));
    }

    @Test public void testSubscribeSensorExists() {
        // Assert.assertTrue(testApp.subscribeToSensor("id"));
    }
}
