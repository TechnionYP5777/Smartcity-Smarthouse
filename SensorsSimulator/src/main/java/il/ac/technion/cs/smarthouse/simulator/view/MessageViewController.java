package il.ac.technion.cs.smarthouse.simulator.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.ToggleSwitch;

import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
import il.ac.technion.cs.smarthouse.simulator.model.SensorField;
import il.ac.technion.cs.smarthouse.simulator.model.Types;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

public class MessageViewController implements Initializable {

    @FXML GridPane mainPane;
    private SensorData currentSensor;
    @FXML private ToggleSwitch switchModeButton;
    GridPane singleMode = new GridPane();
    GridPane streamMode = new GridPane();
    @FXML public TextFlow console;
    @FXML private Button sendButton;
    private Map<SensorField, Node> singleFieldMap;
    private Map<SensorField, List<Node>> streamFieldMap;
    private Pair<TextField, TextField> timeInput;

    private enum BooleanValues {
        True,
        False
    }

    @Override
    public void initialize(final URL location, final ResourceBundle __) {
        mainPane.setAlignment(Pos.TOP_LEFT);
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(0, 25, 25, 0));
        sendButton.setOnAction(e -> buildMessage());
        switchModeButton.selectedProperty().addListener((ChangeListener<Boolean>) (b, oldValue, newValue) -> {
            if (newValue.booleanValue()) {
                buildStreamModePane();
                mainPane.getChildren().setAll(streamMode.getChildren());
            } else {
                buildSingleModePane();
                mainPane.getChildren().setAll(singleMode.getChildren());
            }
        });
    }

    void buildSingleModePane() {
        singleFieldMap = new HashMap<>();
        int currentRow = 0;
        singleMode.add(new Label("Name"), 0, currentRow);
        singleMode.add(new Label("Value"), 1, currentRow++);
        for (final SensorField ¢ : currentSensor.getFields()) {
            singleMode.add(new Label(¢.getName()), 0, currentRow);
            if (¢.getType() != Types.BOOLEAN) {
                final TextField t = new TextField();
                singleMode.add(t, 1, currentRow);
                singleFieldMap.put(¢, t);
            } else {
                final ComboBox<BooleanValues> b = new ComboBox<>();
                b.getItems().addAll(BooleanValues.values());
                singleMode.add(b, 1, currentRow);
                singleFieldMap.put(¢, b);
            }
            ++currentRow;
        }
    }

    void buildStreamModePane() {
        streamFieldMap = new HashMap<>();
        int currentRow = 0;
        streamMode.add(new Label("Name"), 0, currentRow);
        streamMode.add(new Label("From"), 1, currentRow);
        streamMode.add(new Label("To"), 2, currentRow++);
        for (final SensorField ¢ : currentSensor.getFields()) {
            streamMode.add(new Label(¢.getName()), 0, currentRow);
            final List<Node> nodes = new ArrayList<>();
            if (¢.getType() == Types.BOOLEAN) {
                final ComboBox<BooleanValues> b = new ComboBox<>();
                b.getItems().addAll(BooleanValues.values());
                streamMode.add(b, 1, currentRow);
                nodes.add(b);
            } else {
                final TextField t1 = new TextField(), t2 = new TextField();
                streamMode.add(t1, 1, currentRow);
                nodes.add(t1);
                if (¢.getType() != Types.STRING) {
                    streamMode.add(t2, 2, currentRow);
                    nodes.add(t2);
                }
            }
            streamFieldMap.put(¢, nodes);
            ++currentRow;
        }
        streamMode.add(new Label("Send Interval(integer only):"), 0, currentRow);
        final TextField t1 = new TextField(), t2 = new TextField();
        streamMode.add(t1, 1, currentRow);
        streamMode.add(t2, 2, currentRow);
        timeInput = new Pair<>(t1, t2);
    }

    private boolean validateInput(final String input, final Types t) {
        switch (t) {
            case DOUBLE:
                try {
                    Double.parseDouble(input);
                } catch (final Exception e) {
                    return false;
                }
                break;
            case INTEGER:
                try {
                    Integer.parseInt(input);
                } catch (final Exception e) {
                    return false;
                }
                break;
            default:
                break;

        }
        return true;
    }

    public void setCurrentSensor(final SensorData currentSensor) {
        this.currentSensor = currentSensor;
        buildSingleModePane();
        mainPane.getChildren().setAll(singleMode.getChildren());
    }

    @SuppressWarnings("unchecked")
    void buildMessage() {
        String message = "";
        for (final SensorField field : singleFieldMap.keySet()) {
            message += " " + field.getName() + " ";
            if (field.getType() == Types.BOOLEAN)
                message += ((ComboBox<BooleanValues>) singleFieldMap.get(field)).getValue();
            else {
                final String Input = ((TextField) singleFieldMap.get(field)).getText();
                if (!validateInput(Input, field.getType()))
                    return;
                message += Input;
            }
        }
        message += "\n";
        showMessage(message);
    }

    private void showMessage(final String message) {
        console.getChildren().add(new Text(message));
    }

}
