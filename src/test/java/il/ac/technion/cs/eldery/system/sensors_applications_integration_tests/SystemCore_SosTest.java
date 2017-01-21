package il.ac.technion.cs.eldery.system.sensors_applications_integration_tests;

import java.io.IOException;
import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.sensors.sos.gui.SosSensorSimulator;
import javafx.stage.Stage;

/** This is an interactive test. Run it as a java application, and when
 * requested, please open the SosSensorSimulator as a java application.
 * @author RON
 * @since 01-01-2017 */
public class SystemCore_SosTest extends IntegrationAbstructTest {    
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) {
        try {
            super.start(primaryStage, SosSensorSimulator.class, SosAppGui.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
