package il.ac.technion.cs.eldery.system;

import java.io.IOException;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.gui.mapping.MappingController;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import il.ac.technion.cs.eldery.system.userInformation.UserInformation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */

public class SystemCore extends Application {
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    protected final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler);
    private UserInformation user;
    private boolean userInitialized;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws IOException {
        System.out.println("Initializing system...");
        new Thread(sensorsHandler).start();

        // TODO: Roy, add some GUI magic here
        // For now the system initializes the house mapping GUI, should be
        // changed in the future
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/mapping/house_mapping.fxml"));

        final Scene scene = new Scene(loader.load(), 1000, 800);
        s.setTitle("Test");
        s.setScene(scene);
        s.show();

        ((MappingController) loader.getController()).setDatabaseHandler(databaseHandler);
    }

    public UserInformation getUser() {
        return user;
    }

    public void initializeUser(final String name, final String id, final String phoneNumber, final String homeAddress) {
        user = new UserInformation(name, id, phoneNumber, homeAddress);
        userInitialized = true;
    }

    public boolean isUserInitialized() {
        return userInitialized;
    }

}
