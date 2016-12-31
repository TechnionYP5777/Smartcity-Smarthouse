package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.Scanner;

import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.stage.Stage;

public class SystemCore_SosTest extends SystemCore {
    public static void main(final String args[]) {
        launch(args);
    }

    @SuppressWarnings("resource") @Override public void start(Stage primaryStage) {
        while (true) {
            System.out.println("## please start SosSensorSimulator, and press Enter:");
            System.out.print(">>");
            new Scanner(System.in).nextLine();

            System.out.println("## Adding app...");
            try {
                applicationsHandler.addApplication("someID", new ApplicationPath<>(PathType.CLASS_NAME, SosAppGui.class.getName()));
                System.out.println("## App added successfully!");
                break;
            } catch (AppInstallerException | IOException | OnLoadException e) {
                System.out.println("## App installation faild: " + e.getMessage() + "\n");
                // e.printStackTrace();
            }
        }
    }
}
