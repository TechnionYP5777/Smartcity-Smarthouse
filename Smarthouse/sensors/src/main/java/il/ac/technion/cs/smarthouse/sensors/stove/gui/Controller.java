package il.ac.technion.cs.smarthouse.sensors.stove.gui;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.sensors.stove.StoveSensor;
import il.ac.technion.cs.smarthouse.utils.Random;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/** This class is responsible for the visual part of the stove sensor.
 * @author Sharon
 * @since 9.12.16 */
public class Controller implements Initializable {
    private static final String STYLE_REG = "-fx-fill: #4d4d4c; -fx-font-weight: bold;";
    private static final String STYLE_ON = "-fx-fill: #718c00; -fx-font-weight: bold;";
    private static final String STYLE_OFF = "-fx-fill: #c82829; -fx-font-weight: bold;";
    private static final String STYLE_TEMP = "-fx-fill: #4271ae; -fx-font-weight: bold;";

    private StoveSensor sensor;
    private boolean on;
    @FXML public Button onOffButton;
    @FXML public Label tempLabel;
    @FXML public Slider tempSlider;
    @FXML public TextFlow console;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        sensor = new StoveSensor(Random.sensorId(), "iStoves", "127.0.0.1", 40001);
        for (boolean res = false; !res;)
            res = sensor.register();
        onOffButton.setOnAction(event -> {
            on = !on;
            onOffButton.setText("Turn " + (on ? "off" : "on"));
            tempLabel.setDisable(!on);
            tempSlider.setDisable(!on);
            sensor.updateSystem(on, (int) Math.round(tempSlider.getValue()));
            printUpdateMessage();
        });
        tempSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int temp = (int) Math.round(newVal.doubleValue());
            tempLabel.setText("Temperature: " + temp);
            sensor.updateSystem(true, temp);
            printUpdateMessage();
        });
    }

    private void printUpdateMessage() {
        final ObservableList<Node> children = console.getChildren();

        Text text = new Text("Sending update message: {on: ");
        text.setStyle(STYLE_REG);
        children.add(0, text);

        text = new Text(on + "");
        text.setStyle(on ? STYLE_ON : STYLE_OFF);
        children.add(1, text);

        text = new Text(", temperature: ");
        text.setStyle(STYLE_REG);
        children.add(2, text);

        text = new Text((int) tempSlider.getValue() + "");
        text.setStyle(STYLE_TEMP);
        children.add(3, text);

        text = new Text("}\n");
        text.setStyle(STYLE_REG);
        children.add(4, text);
    }
}
