package il.ac.technion.cs.eldery.applications;

import il.ac.technion.cs.eldery.system.*;

/** @author roysh
 * @since 8.12.2016 */
public abstract class BaseApplication {
  MainSystem mainSystem;

  /** Adds the mainSystem Object to the app
   * @param mainSystem
   * @return true if successful or false if failed */
  public boolean setMainSystemInstance(final MainSystem ¢) {
    mainSystem = ¢;
    return true;
  }

  /** the function that will run when the il.ac.technion.cs.eldery.system
   * installs the app in the il.ac.technion.cs.eldery.system */
  public abstract void onInstall();

  /** The main loop of the application. Will be runed on a thread in the main
   * il.ac.technion.cs.eldery.system */
  public abstract void main();

  /** Adds the app to the listener list of a specific sensor
   * @param sensorID
   * @return true if successful or false if failed */
  @SuppressWarnings("static-method") public boolean subscribeToSensor(final String sensorID) {
    return true;
  }

  /** Check if the sensor specified is active in the house
   * @param sensorID
   * @return true if the main il.ac.technion.cs.eldery.system has this sensor or
   *         false otherwise */
  @SuppressWarnings("static-method") public boolean checkIfSensorExists(final String sensorID) {
    return true;
  }
}
