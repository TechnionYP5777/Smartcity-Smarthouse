package il.ac.technion.cs.eldery.system;

import java.io.IOException;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.gui.mapping.MappingController;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */

public class SystemCore extends Application {
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    protected final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler);

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws IOException {
        System.out.println("Initializing system...");
        new Thread(sensorsHandler).start();

        // TODO: Roy, add some GUI magic here
        // For now the system initializes the house mapping GUI, should be
        // changed in the future
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/mapping/house mapping.fxml"));

        Scene scene = new Scene(loader.load(), 1000, 800);
        s.setTitle("Test");
        s.setScene(scene);
        s.show();
        
        ((MappingController) loader.getController()).setDatabaseHandler(databaseHandler);
    }
}
