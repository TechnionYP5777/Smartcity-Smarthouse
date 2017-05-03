package il.ac.technion.cs.smarthouse.sensors.vitals.gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.RangeSlider;

import il.ac.technion.cs.smarthouse.sensors.vitals.VitalsSensor;
import il.ac.technion.cs.smarthouse.utils.Random;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/** This class is responsible for the visual part of the vitals signs
 * application.
 * @author Yarden
 * @since 16.1.17 */
public class Controller implements Initializable {
    private static final String STYLE_REG = "-fx-fill: #4d4d4c; -fx-font-weight: bold;";
    private static final String STYLE_SYSTOLIC = "-fx-fill: #c82829; -fx-font-weight: bold;";
    private static final String STYLE_DIASTOLIC = "-fx-fill: #ff6600; -fx-font-weight: bold;";
    private static final String STYLE_PULSE = "-fx-fill: #4271ae; -fx-font-weight: bold;";

    private VitalsSensor sensor;
    private RangeSlider bpRSlider;
    @FXML public Label pulseLabelSensor;
    @FXML public Label bpLabelSensor;
    @FXML public Slider pulseSlider;
    @FXML public TextFlow console;
    @FXML public VBox mainVBox;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        sensor = new VitalsSensor(Random.sensorId(), "iVitals", "127.0.0.1", 40001);
        for (boolean res = false; !res;)
            res = sensor.register();
        pulseLabelSensor.setDisable(false);
        bpLabelSensor.setDisable(false);
        bpRSlider = new RangeSlider(0, 200, 80, 120);
        mainVBox.getChildren().add(4, bpRSlider);
        pulseSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int pulse = (int) Math.round(newVal.doubleValue());
            pulseLabelSensor.setText("Pulse: " + pulse);
            sensor.updateSystem(pulse, (int) Math.round(bpRSlider.getHighValue()), (int) Math.round(bpRSlider.getLowValue()));
            printUpdateMessage();
        });
        bpRSlider.lowValueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int diastolicBP = (int) Math.round(newVal.doubleValue());
            bpLabelSensor.setText("Blood Pressure: " + (int) Math.round(bpRSlider.getHighValue()) + "/" + diastolicBP);
            sensor.updateSystem((int) Math.round(pulseSlider.getValue()), (int) Math.round(bpRSlider.getHighValue()), diastolicBP);
            printUpdateMessage();
        });

        bpRSlider.highValueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int systolicBP = (int) Math.round(newVal.doubleValue());
            bpLabelSensor.setText("Blood Pressure: " + systolicBP + "/" + (int) Math.round(bpRSlider.getLowValue()));
            sensor.updateSystem((int) Math.round(pulseSlider.getValue()), systolicBP, (int) Math.round(bpRSlider.getLowValue()));
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

        text = new Text(", systolicBP: ");
        text.setStyle(STYLE_REG);
        children.add(2, text);

        text = new Text((int) Math.round(bpRSlider.getHighValue()) + "");
        text.setStyle(STYLE_SYSTOLIC);
        children.add(3, text);

        text = new Text(", diastolicBP: ");
        text.setStyle(STYLE_REG);
        children.add(4, text);

        text = new Text((int) Math.round(bpRSlider.getLowValue()) + "");
        text.setStyle(STYLE_DIASTOLIC);
        children.add(5, text);

        text = new Text("}\n");
        text.setStyle(STYLE_REG);
        children.add(6, text);
    }
}
