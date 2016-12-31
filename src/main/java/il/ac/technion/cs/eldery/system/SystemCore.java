package il.ac.technion.cs.eldery.system;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import javafx.application.Application;
import javafx.stage.Stage;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */

public class SystemCore extends Application {
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    protected final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler);

    public SystemCore() {
        System.out.println("Initializing system...");
        new Thread(sensorsHandler).start();
    }

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage primaryStage) {
        // TODO: Roy, add some GUI magic here
        return;
    }
}
