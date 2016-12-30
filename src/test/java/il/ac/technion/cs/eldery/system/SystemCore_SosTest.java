package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.Scanner;

import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.stage.Stage;

public class SystemCore_SosTest extends SystemCore {
    public static void main(final String args[]) {
        launch(args);
    }
    
    @SuppressWarnings("resource")
    @Override public void start(Stage primaryStage) {
        System.out.println("please start SosSensorSimulator, and press Enter");
        
        new Scanner(System.in).nextLine();
        
        System.out.println("adding app");
        
        try {
            applicationsHandler.addApplication("someID",
                    new ApplicationPath<>(PathType.CLASS_NAME, SosAppGui.class.getName()));
        } catch (AppInstallerException | IOException e) {
            e.printStackTrace();
        }
    }
}
