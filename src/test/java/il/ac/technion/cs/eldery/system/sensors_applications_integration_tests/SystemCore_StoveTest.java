package il.ac.technion.cs.eldery.system.sensors_applications_integration_tests;

import java.io.IOException;

import il.ac.technion.cs.eldery.applications.stove.StoveModuleGui;
import il.ac.technion.cs.eldery.sensors.stove.gui.StoveSensorSimulator;
import javafx.stage.Stage;

public class SystemCore_StoveTest extends IntegrationAbstructTest {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage primaryStage) {
        try {
            super.start(primaryStage, StoveSensorSimulator.class, StoveModuleGui.class);
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }
}
