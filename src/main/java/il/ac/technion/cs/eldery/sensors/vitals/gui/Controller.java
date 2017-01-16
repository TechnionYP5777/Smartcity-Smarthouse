package il.ac.technion.cs.eldery.sensors.vitals.gui;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.sensors.vitals.VitalsSensor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/** @author Yarden
 * @since 16.1.17 */
public class Controller implements Initializable {
    private static final String STYLE_REG = "-fx-fill: #4d4d4c; -fx-font-weight: bold;";
    private static final String STYLE_BP = "-fx-fill: #c82829; -fx-font-weight: bold;";
    private static final String STYLE_PULSE = "-fx-fill: #4271ae; -fx-font-weight: bold;";

    private VitalsSensor sensor;
    @FXML public Label pulseLabel;
    @FXML public Label bpLabel;
    @FXML public Slider pulseSlider;
    @FXML public Slider bpSlider;
    @FXML public TextFlow console;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        sensor = new VitalsSensor("00:00:00:00:00:02", "iVitals", "127.0.0.1", 40001);
        for (boolean res = false; !res;)
            res = sensor.register();
        pulseLabel.setDisable(false);
        bpLabel.setDisable(false);
        pulseSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int pulse = (int) Math.round(newVal.doubleValue());
            pulseLabel.setText("Pulse: " + pulse);
            sensor.updateSystem(pulse, (int) Math.round(bpSlider.getValue()));
            printUpdateMessage();
        });
        bpSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int bloodPressure = (int) Math.round(newVal.doubleValue());
            bpLabel.setText("Blood Pressure: " + bloodPressure);
            sensor.updateSystem((int) Math.round(pulseSlider.getValue()), bloodPressure);
            printUpdateMessage();
        });
    }

    private void printUpdateMessage() {
        final ObservableList<Node> children = console.getChildren();

        Text text = new Text("Sending update message: {pulse: ");
        text.setStyle(STYLE_REG);
        children.add(0, text);

        text = new Text((int) Math.round(pulseSlider.getValue()) + "");
        text.setStyle(STYLE_PULSE);
        children.add(1, text);

        text = new Text(", blood pressure: ");
        text.setStyle(STYLE_REG);
        children.add(2, text);

        text = new Text((int) Math.round(bpSlider.getValue()) + "");
        text.setStyle(STYLE_BP);
        children.add(3, text);

        text = new Text("}\n");
        text.setStyle(STYLE_REG);
        children.add(4, text);
    }

}
