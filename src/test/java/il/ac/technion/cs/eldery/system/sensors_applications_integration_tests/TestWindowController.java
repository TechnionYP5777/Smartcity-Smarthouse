package il.ac.technion.cs.eldery.system.sensors_applications_integration_tests;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class TestWindowController implements Initializable {
    private final String MSG_PREFIX = "## TEST - ";
    
    @FXML private AnchorPane loader_pane;
    @FXML private Button start_sensor;
    @FXML private Button start_app;

    @Override public void initialize(URL location, ResourceBundle __) {
        return;
    }
    
    public void setApplication(ApplicationsHandler applicationsHandler, Class<?> applicationClass) {
        start_app.setOnAction(e -> {
            System.out.println(MSG_PREFIX + "Trying to add the app...");
            try {
                applicationsHandler.addApplication("someID", new ApplicationPath<>(PathType.CLASS_NAME, applicationClass.getName()))
                .reopen(loader_pane);
                
                System.out.println(MSG_PREFIX + "App added and started successfully!");
            } catch (AppInstallerException | IOException | OnLoadException ¢) {
                System.out.println(MSG_PREFIX + "App installation failed: " + ¢.getMessage());
                System.out.println(String.valueOf(new char[80]).replace("\0", "#"));
            }
        });
    }
}
