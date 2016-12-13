package il.ac.technion.cs.eldery.system;

import il.ac.technion.cs.eldery.system.sensors.SensorHandler;
import il.ac.technion.cs.eldery.system.handlers.ApplicationHandler;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class MainSystem {
    SensorHandler sensorHandler = new SensorHandler();

    ApplicationHandler applicationHandler;

    public MainSystem() {}

}
