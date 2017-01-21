package il.ac.technion.cs.eldery.system.sensors_applications_integration_tests;

import java.io.IOException;
import il.ac.technion.cs.eldery.system.SystemCore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** This is an interactive test. Run it as a java application, and when
 * requested, please open the SosSensorSimulator as a java application.
 * @author RON
 * @since 01-01-2017 */
public class IntegrationAbstructTest extends SystemCore {
    private final String MSG_PREFIX = "## TEST - ";

    public void start(final Stage primaryStage, Class<?> sensorSimulatorClass, Class<?> applicationClass) throws IOException {
        super.initializeSystemComponents();
        
        System.out.println(MSG_PREFIX + "System started (NO GUI)!");
        
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("integration_test_gui.fxml"));
        final Scene scene = new Scene(loader.load(), 1000, 800);
        
        primaryStage.setTitle("Integration Test");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        TestWindowController controller = loader.getController();

        System.out.println(MSG_PREFIX + "please start the sensor simulator (" + sensorSimulatorClass.getName() + "), and press the button:");
        controller.setApplication(applicationsHandler, applicationClass);
    }
}
