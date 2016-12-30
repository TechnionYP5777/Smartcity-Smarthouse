package il.ac.technion.cs.eldery.sensors.sos.gui;

import il.ac.technion.cs.eldery.sensors.sos.SosSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** @author Yarden
 * @since 28.12.16 */
public class SosSensorSimulator extends Application {
    private SosSensor sensor;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        sensor = new SosSensor("00:00:00:00:00:01", "iSOS", "127.0.0.1", 40001);
        for (boolean res = false; !res;)
            res = sensor.register();
        final Image sosImage = new Image(getClass().getResourceAsStream("sos_icon.png"), 320, 0, true, true);
        final Button sosButton = new Button();
        sosButton.setGraphic(new ImageView(sosImage));
        sosButton.setStyle("-fx-focus-color: transparent;");
        sosButton.setOnAction(event -> {
            sensor.updateSystem();
        });
        final StackPane layout = new StackPane();
        layout.getChildren().add(sosButton);
        final Scene scene = new Scene(layout, 320, 268);
        s.setScene(scene);
        s.setTitle("SOS Sensor Simulator");
        s.show();

    }

}
