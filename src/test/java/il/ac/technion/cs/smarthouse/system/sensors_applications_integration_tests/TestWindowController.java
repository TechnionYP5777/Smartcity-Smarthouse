package il.ac.technion.cs.smarthouse.system.sensors_applications_integration_tests;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TestWindowController implements Initializable {
    public static final String MSG_PREFIX = "## TEST - ";

    @FXML public AnchorPane loader_pane;
    @FXML public Button start_sensor;
    @FXML public Button start_app;
    @FXML public TextFlow consoleView;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        // no special actions to be done here
    }

    public void setApplication(final ApplicationsCore h, final Class<?> sensorSimulatorClass, final Class<?> applicationClass) {
        final ObservableList<Node> children = consoleView.getChildren();

        children.add(makeText("please start the sensor simulator (" + sensorSimulatorClass.getName() + "), and press the button\n"));

        start_app.setOnAction(e -> {
            children.add(makeText("Trying to add the app..."));
            try {
                h.addApplication(new ApplicationPath<>(PathType.CLASS_NAME, applicationClass.getName())).reopen(loader_pane);

                children.add(makeText("App added and started successfully!"));
            } catch (AppInstallerException | IOException | OnLoadException ¢) {
                children.add(makeText("App installation failed: " + ¢.getMessage() + "\n"));
            }
        });
    }

    private static Text makeText(final String msg) {
        return new Text(MSG_PREFIX + msg + "\n");
    }
}
