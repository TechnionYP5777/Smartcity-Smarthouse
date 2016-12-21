package il.ac.technion.cs.eldery.sensors.stove.gui;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.sensors.stove.StoveSensor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

/** @author Sharon
 * @since 9.12.16 */
public class Controller implements Initializable {
    private StoveSensor sensor;
    private boolean on;
    @FXML public Button onOffButton;
    @FXML public Label tempLabel;
    @FXML public Slider tempSlider;
    @FXML public TextArea console;

    @SuppressWarnings("boxing") @Override public void initialize(final URL location, final ResourceBundle __) {
        sensor = new StoveSensor("Stove Sensor Simulator", "00:00:00:00:00:00", "1:1:1:1", 80);
        // for (boolean res = false; !res;)
        // res = sensor.register();
        onOffButton.setOnAction(event -> {
            on = !on;
            onOffButton.setText("Turn " + (on ? "off" : "on"));
            tempLabel.setDisable(!on);
            tempSlider.setDisable(!on);
            console.setText(
                    String.format("Sending update message: {on: %1$b, temperature: %2$d}\n", on, (int) tempSlider.getValue()) + console.getText());
            sensor.updateSystem(on, (int) Math.round(tempSlider.getValue()));
        });
        tempSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
            final int temp = (int) Math.round(newVal.doubleValue());
            tempLabel.setText("Temperature: " + temp);
            console.setText(
                    String.format("Sending update message: {on: %1$b, temperature: %2$d}\n", on, (int) tempSlider.getValue()) + console.getText());
            sensor.updateSystem(true, temp);
        });
    }
}
