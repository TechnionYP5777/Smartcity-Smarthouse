package il.ac.technion.cs.smarthouse.sensors.stove.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** This class simulates a temperature sensor for a stove.
 * @author Sharon
 * @since 9.12.16 */
public class StoveSensorSimulator extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("stove_ui.fxml"));
        final Scene scene = new Scene(root);
        s.setTitle("Stove Sensor Simulator");
        s.setScene(scene);
        s.show();
    }
}
