package il.ac.technion.cs.eldery.applications;

import org.junit.*;

import il.ac.technion.cs.eldery.system.*;

/** @author Roy
 * @since 8.12.16 */
class MockApp extends BaseApplication {
  public String testFeedbak;

  @Override public void onInstall() {
    testFeedbak = "onInstall";
  }

  @Override public void main() {
    testFeedbak = "main";
  }
}

public class BaseApplicationTest {
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
    testApp.onInstall();
    Assert.assertEquals(testApp.testFeedbak, "onInstall");
  }

  @Test public void testMain() {
    testApp.main();
    Assert.assertEquals(testApp.testFeedbak, "main");
  }

  @Test public void testCheckIfSensorExists() {
    Assert.assertTrue(testApp.checkIfSensorExists("id"));
  }

  @Test public void testSubscribeSensorExists() {
    Assert.assertTrue(testApp.subscribeToSensor("id"));
  }
}
