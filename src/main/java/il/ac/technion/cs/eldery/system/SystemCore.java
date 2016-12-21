package il.ac.technion.cs.eldery.system;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class SystemCore {
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final SensorsHandler sensorsHandler;
    private final ApplicationsHandler applicationsHandler;

    public SystemCore() {
        sensorsHandler = new SensorsHandler(databaseHandler);
        applicationsHandler = new ApplicationsHandler(databaseHandler);
    }
}
