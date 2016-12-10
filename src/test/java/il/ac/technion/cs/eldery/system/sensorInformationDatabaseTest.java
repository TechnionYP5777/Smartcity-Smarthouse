package il.ac.technion.cs.eldery.system;

import java.util.*;
import org.junit.*;
import il.ac.technion.cs.eldery.utils.*;

/** @author Inbal Zukerman
 * @since 9.12.2016 */
@SuppressWarnings("boxing")
public class sensorInformationDatabaseTest {
  private final Tuple<String, Integer> infoTemp = new Tuple<>("Temp", 20);
  private final Tuple<String, Integer> infoTemp2 = new Tuple<>("Temp", 18);
  private final Tuple<String, Integer> infoPulse = new Tuple<>("Pulse", 68);
  private final SensorInformationDatabase<String, Integer> tempInfoDB = new SensorInformationDatabase<>("temperature", 5);
  private final SensorInformationDatabase<String, Integer> pulseInfoDB = new SensorInformationDatabase<>("pulse", 10);

  @Test public void initializationTest() {
    Assert.assertEquals(5, tempInfoDB.getMaxCapacity());
    Assert.assertEquals(null, tempInfoDB.getLastUpdate());
    Assert.assertEquals(10, pulseInfoDB.getMaxCapacity());
    Assert.assertEquals(null, pulseInfoDB.getLastUpdate());
  }

  @Test public void addInfoTest() {
    tempInfoDB.addInfo(infoTemp);
    Assert.assertEquals(infoTemp, tempInfoDB.getLastUpdate());
    tempInfoDB.addInfo(infoTemp2);
    Assert.assertNotEquals(infoTemp, tempInfoDB.getLastUpdate());
    Assert.assertEquals(infoTemp2, tempInfoDB.getLastUpdate());
    Assert.assertEquals(null, pulseInfoDB.getLastUpdate());
    pulseInfoDB.addInfo(infoPulse);
    Assert.assertEquals(infoPulse, pulseInfoDB.getLastUpdate());
  }

  @Test public void changeMaxCapacityTest() {
    Assert.assertEquals(0, tempInfoDB.getCurrentCapacity());
    tempInfoDB.changeMaxCapacity(2);
    Assert.assertEquals(2, tempInfoDB.getMaxCapacity());
    tempInfoDB.addInfo(infoTemp);
    tempInfoDB.addInfo(infoTemp2);
    Assert.assertEquals(2, tempInfoDB.getCurrentCapacity());
    final Tuple<String, Integer> anotherTemprature = new Tuple<>("Temp", 23);
    tempInfoDB.addInfo(anotherTemprature);
    Assert.assertEquals(anotherTemprature, tempInfoDB.getLastUpdate());
    Assert.assertEquals(2, tempInfoDB.getCurrentCapacity());
    Assert.assertFalse(tempInfoDB.doesExists(infoTemp));
    Assert.assertTrue(tempInfoDB.doesExists(infoTemp2));
    tempInfoDB.changeMaxCapacity(5);
    Assert.assertEquals(5, tempInfoDB.getMaxCapacity());
    Assert.assertTrue(tempInfoDB.doesExists(anotherTemprature));
    Assert.assertTrue(tempInfoDB.doesExists(infoTemp2));
    tempInfoDB.addInfo(infoTemp);
    Assert.assertTrue(tempInfoDB.doesExists(infoTemp));
  }

  @Test public void recieveLastUpdatesTest() {
    Assert.assertEquals(0, tempInfoDB.getCurrentCapacity());
    tempInfoDB.addInfo(infoTemp);
    tempInfoDB.addInfo(infoTemp2);
    Assert.assertEquals(2, tempInfoDB.getCurrentCapacity());
    ArrayList<Tuple<String, Integer>> lastUpdates = tempInfoDB.recievceLastUpdates(0);
    Assert.assertEquals(null, lastUpdates);
    lastUpdates = tempInfoDB.recievceLastUpdates(1);
    Assert.assertEquals(1, lastUpdates.size());
    Assert.assertEquals(infoTemp2, lastUpdates.get(0));
    lastUpdates = tempInfoDB.recievceLastUpdates(2);
    Assert.assertEquals(2, lastUpdates.size());
  }

  @Test public void sensorIdTest() {
    Assert.assertEquals("temperature", tempInfoDB.getSensorId());
    Assert.assertEquals("pulse", pulseInfoDB.getSensorId());
  }
}
