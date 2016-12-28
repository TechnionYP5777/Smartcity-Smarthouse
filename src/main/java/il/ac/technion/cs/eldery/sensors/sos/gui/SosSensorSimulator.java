package il.ac.technion.cs.eldery.sensors.sos.gui;

import il.ac.technion.cs.eldery.sensors.sos.SosSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** @author Yarden
 * @since 28.12.16 */
public class SosSensorSimulator extends Application {
    private SosSensor sensor;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(Stage s) throws Exception {
        sensor = new SosSensor("00:00:00:00:00:01", "iSOS", "127.0.0.1", 40001);
        for (boolean res = false; !res;)
            res = sensor.register();
        Button sosButton = new Button();
        sosButton.setText("SOS");
        sosButton.setOnAction(event -> {
            sensor.updateSystem();
        });
        StackPane layout = new StackPane();
        layout.getChildren().add(sosButton);
        Scene scene = new Scene(layout, 300, 300);
        s.setScene(scene);
        s.setTitle("SOS Sensor Simulator");
        s.show();

    }

}
