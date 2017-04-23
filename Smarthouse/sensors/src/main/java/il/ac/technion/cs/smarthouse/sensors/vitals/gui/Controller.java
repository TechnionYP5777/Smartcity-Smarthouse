package il.ac.technion.cs.smarthouse.sensors.vitals.gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.RangeSlider;

import il.ac.technion.cs.smarthouse.sensors.vitals.VitalsSensor;
import il.ac.technion.cs.smarthouse.utils.Random;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
    @FXML public Label pulseLabel;
    @FXML public Label bpLabel;
    @FXML public Slider pulseSlider;
    @FXML public TextFlow console;
    @FXML public VBox mainVBox;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        sensor = new VitalsSensor(Random.sensorId(), "iVitals", "127.0.0.1", 40001);
        for (boolean res = false; !res;)
            res = sensor.register();
        pulseLabel.setDisable(false);
        bpLabel.setDisable(false);
        bpRSlider = new RangeSlider(0, 200, 80, 120);
        mainVBox.getChildren().add(4, bpRSlider);
        pulseSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int pulse = (int) Math.round(newVal.doubleValue());
            pulseLabel.setText("Pulse: " + pulse);
            sensor.updateSystem(pulse, (int) Math.round(bpRSlider.getHighValue()), (int) Math.round(bpRSlider.getLowValue()));
            printUpdateMessage();
        });
        bpRSlider.lowValueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int diastolicBP = (int) Math.round(newVal.doubleValue());
            bpLabel.setText("Blood Pressure: " + (int) Math.round(bpRSlider.getHighValue()) + "/" + diastolicBP);
            sensor.updateSystem((int) Math.round(pulseSlider.getValue()), (int) Math.round(bpRSlider.getHighValue()), diastolicBP);
            printUpdateMessage();
        });

        bpRSlider.highValueProperty().addListener((ov, oldVal, newVal) -> {
            if (Math.round(oldVal.doubleValue()) == Math.round(newVal.doubleValue()))
                return;

            final int systolicBP = (int) Math.round(newVal.doubleValue());
            bpLabel.setText("Blood Pressure: " + systolicBP + "/" + (int) Math.round(bpRSlider.getLowValue()));
            sensor.updateSystem((int) Math.round(pulseSlider.getValue()), systolicBP, (int) Math.round(bpRSlider.getLowValue()));
            printUpdateMessage();
        });

        simulatePulseChange();
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

    private void simulatePulseChange() {
        final int bpVals[] = { 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 107, 108, 109, 110, 109, 108, 107, 106, 105, 104, 103,
                102, 101, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 83, 84, 85, 84, 85, 86, 87, 88, 89, 90, 91, 92,
                93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 103, 102, 101, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85,
                84, 83, 82, 81, 80, 81, 82, 83, 84, 85, 86, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 100, 99, 98, 97, 96,
                95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81 };

        Service<Void> bg = new Service<Void>() {
            int p;

            @Override protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override protected Void call() throws Exception {
                        while (true) {
                            updateProgress((bpVals[p]), 150);
                            p = (p + 1) % bpVals.length;
                            Thread.sleep(30);
                        }
                    }
                };
            }
        };

        pulseSlider.valueProperty().bind(bg.workDoneProperty());

        bg.start();
    }

}
