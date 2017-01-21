package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.Scanner;

import il.ac.technion.cs.eldery.applications.vitals.VitalsApp;
import il.ac.technion.cs.eldery.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.stage.Stage;

/** @author Yarden
 * @since 19.1.17 */
public class SystemCore_VitalsTest extends SystemCore {
    public static void main(final String args[]) {
        launch(args);
    }

    @Override @SuppressWarnings("resource") public void start(final Stage primaryStage) {
        new Thread(sensorsHandler).start();
        System.out.println("## System started!");

        while (true) {
            System.out.println("## please start VitalsSensorSimulator (" + VitalsSensorSimulator.class.getName() + "), and press Enter:");
            new Scanner(System.in).nextLine();

            System.out.println("## Trying to add the app...");
            try {
                applicationsHandler.addApplication("someID", new ApplicationPath<>(PathType.CLASS_NAME, VitalsApp.class.getName()));
                System.out.println("## App added and started successfully!");
                break;
            } catch (AppInstallerException | IOException | OnLoadException ¢) {
                System.out.println("## App installation failed: " + ¢.getMessage());
                System.out.println(String.valueOf(new char[80]).replace("\0", "#"));
            }
        }
    }

}
