package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
@SuppressWarnings("unused")
public class SystemCore extends Application {
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    private final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler);

    public SystemCore() {
        System.out.println("Initializing system...");
        new Thread(sensorsHandler).start();
    }

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) {
        return;
    }
}
