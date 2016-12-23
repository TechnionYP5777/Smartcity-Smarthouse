package il.ac.technion.cs.eldery.system;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
@SuppressWarnings("unused")
public class SystemCore {
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    private final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler);

    public SystemCore() {
        new Thread(sensorsHandler).start();
    }
    
    public static void main(String[] args) {
        System.out.println("FSAFSA");
        new SystemCore();
        System.out.println("AAAAA");
    }
}
