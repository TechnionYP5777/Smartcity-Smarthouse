package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.Scanner;

import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.stage.Stage;

/** This is an interactive test. Run it as a java application, and when
 * requested, please open the SosSensorSimulator as a java application.
 * @author RON
 * @since 01-01-2017 */
public class SystemCore_SosTest extends SystemCore {
    public static void main(final String args[]) {
        launch(args);
    }

    @SuppressWarnings("resource") @Override public void start(final Stage primaryStage) {
        System.out.println("## System started!");

        while (true) {
            System.out.println("## please start SosSensorSimulator (" + SosSensorSimulator.class.getName() + "), and press Enter:");
            new Scanner(System.in).nextLine();

            System.out.println("## Trying to add the app...");
            try {
                applicationsHandler.addApplication("someID", new ApplicationPath<>(PathType.CLASS_NAME, SosAppGui.class.getName()));
                System.out.println("## App added and started successfully!");
                break;
            } catch (AppInstallerException | IOException | OnLoadException ¢) {
                System.out.println("## App installation failed: " + ¢.getMessage());
                System.out.println(String.valueOf(new char[80]).replace("\0", "#"));
            }
        }
    }
}
