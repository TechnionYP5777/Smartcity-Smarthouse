package il.ac.technion.cs.smarthouse.sensors.vitals.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** This class simulates the vitals signs application.
 * @author Yarden
 * @since 16.1.17 */
public class VitalsSensorSimulator extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("vitals_ui.fxml"));
        final Scene scene = new Scene(root);
        s.setTitle("Vitals Sensor Simulator");
        s.setScene(scene);
        s.show();
    }

}
