package il.ac.technion.cs.eldery.system.applications;

import com.google.gson.JsonObject;

import il.ac.technion.cs.eldery.system.*;
import javafx.application.Application;

/** The API for the apps/modules developers
 * Every app that wants to be installed on the system, MUST extend this class
 * @author RON
 * @author roysh
 * @since 8.12.2016 */
public abstract class SmartHouseApplication extends Application {
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
  public abstract void onLoad();

  /** Adds the app to the listener list of a specific sensor
   * @param sensorID
   * @return true if successful or false if failed */
  @SuppressWarnings("static-method") public boolean subscribeToSensor(final String sensorID) {
    // TODO: RON and ROY - implement this class
    return true;
  }

  /** Check if the sensor specified is active in the house
   * @param sensorID
   * @return true if the system has this sensor or
   *         false otherwise */
  @SuppressWarnings("static-method") public boolean checkIfSensorExists(final String sensorCommercialName) {
    // TODO: RON and ROY - implement this class
    return sensorCommercialName != null;
  }

  /** Saves the app's data to the system's database
   * @param data
   * @return true if the data was saved to the system, or false otherwise */
  @SuppressWarnings("static-method") public boolean saveToDatabase(final JsonObject data) {
    // TODO: RON and ROY - implement this class
    return data != null;
  }

  /** Loads the app's data from the system's database
   * @return a JSObject with the data */
  @SuppressWarnings("static-method") public JsonObject loadFromDatabase() {
    // TODO: RON and ROY - implement this class
    return null;
  }
}
