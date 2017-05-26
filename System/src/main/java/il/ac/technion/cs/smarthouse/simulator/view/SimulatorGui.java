package il.ac.technion.cs.smarthouse.simulator.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SimulatorGui extends Application {

    Circle circle_Red, circle_Green, circle_Blue;

    @Override
    public void start(Stage s) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("main_simulator_window.fxml"));
        final Scene scene = new Scene(root, 1250, 580);
        s.setTitle("Sensor Simulator");
        s.setScene(scene);
        s.setResizable(false);
        s.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
