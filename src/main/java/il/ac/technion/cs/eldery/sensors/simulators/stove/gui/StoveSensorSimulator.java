package il.ac.technion.cs.eldery.sensors.simulators.stove.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StoveSensorSimulator extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override public void start(Stage s) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("stove_ui.fxml"));
    Scene scene = new Scene(root);
    s.setTitle("Stove Sensor Simulator");
    s.setScene(scene);
    s.show();
  }
}
