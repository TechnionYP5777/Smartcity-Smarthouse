package il.ac.technion.cs.smarthouse.system.gui.mapping;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class SensorInfoController implements Initializable {
    private DatabaseHandler dbHandler;
    private MappingController mappingController;

    private String name;
    private String id;
    @FXML private TitledPane titledPane;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private ComboBox<String> room;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        for (final SensorLocation ¢ : SensorLocation.values())
            room.getItems().add(¢ + "");

        room.getSelectionModel().select(0);
        room.valueProperty().addListener((ov, prevVal, newVal) -> {
            try {
                mappingController.updateSensorLocation(id, SensorLocation.fromString(newVal));
                dbHandler.setSensorLocation(id, SensorLocation.fromString(newVal));
            } catch (final SensorNotFoundException ¢) {
                ¢.printStackTrace();
            }
        });
    }

    public SensorInfoController setDatabaseHandler(final DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        return this;
    }

    public SensorInfoController setMappingController(final MappingController ¢) {
        mappingController = ¢;

        return this;
    }

    public SensorInfoController setName(final String name) {
        this.name = name;
        nameLabel.setText("Name: " + name);
        updateUI();

        return this;
    }

    public SensorInfoController setId(final String id) {
        this.id = id;
        idLabel.setText("ID: " + id);
        updateUI();

        return this;
    }

    private void updateUI() {
        titledPane.setText(String.format("%s (ID %s)", name, id));
    }
}
