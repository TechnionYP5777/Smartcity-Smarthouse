package il.ac.technion.cs.smarthouse.sensors.shutter.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/** This class simulates the shutters application.
* @author Alex
* @since 09.05.17 */

public class ShutterSensorSimulator  extends Application{
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("shutter_ui.fxml"));
        final Scene scene = new Scene(root);
        primaryStage.setTitle("Shutter Sensor Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

}
