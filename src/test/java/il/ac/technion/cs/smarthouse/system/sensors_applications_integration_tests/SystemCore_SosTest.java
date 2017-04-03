package il.ac.technion.cs.smarthouse.system.sensors_applications_integration_tests;

import java.io.IOException;

import il.ac.technion.cs.smarthouse.applications.sos.SosAppGui;
import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import javafx.stage.Stage;

/** This is an interactive test. Run it as a java application, and when
 * requested, please open the SosSensorSimulator as a java application.
 * @author RON
 * @since 01-01-2017 */
public class SystemCore_SosTest extends IntegrationAbstructTest {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage primaryStage) {
        try {
            super.start(primaryStage, SosSensorSimulator.class, SosAppGui.class);
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }
}
