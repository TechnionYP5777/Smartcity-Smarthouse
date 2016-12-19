package il.ac.technion.cs.eldery.system;

import il.ac.technion.cs.eldery.system.applications.ApplicationHandler;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class MainSystem {
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private SensorsHandler sensorHandler;
    private ApplicationHandler applicationHandler;

    public MainSystem() {
        sensorHandler = new SensorsHandler(databaseHandler);
        applicationHandler = new ApplicationHandler(databaseHandler);
    }
}
