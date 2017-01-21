package il.ac.technion.cs.eldery.system.sensors_applications_integration_tests;

import java.io.IOException;
import il.ac.technion.cs.eldery.applications.vitals.VitalsApp;
import il.ac.technion.cs.eldery.sensors.vitals.gui.VitalsSensorSimulator;
import javafx.stage.Stage;

/** @author Yarden
 * @since 19.1.17 */
public class SystemCore_VitalsTest extends IntegrationAbstructTest {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) {
        try {
            super.start(primaryStage, VitalsSensorSimulator.class, VitalsApp.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
