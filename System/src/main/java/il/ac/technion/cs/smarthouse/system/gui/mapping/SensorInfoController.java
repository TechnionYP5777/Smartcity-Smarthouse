package il.ac.technion.cs.smarthouse.system.gui.mapping;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import il.ac.technion.cs.smarthouse.mvp.SystemPresenter;
import il.ac.technion.cs.smarthouse.system.SensorsManager;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class SensorInfoController extends SystemPresenter {
    private FileSystem fileSystem;

    private String name;
    private String id;
    @FXML private TitledPane titledPane;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private ComboBox<String> room;

    @Override
    public void init(final SystemCore model, final URL location, final ResourceBundle __) {
        fileSystem = model.getFileSystem();

        room.getItems().addAll(Arrays.asList(SensorLocation.values()).stream().map(loc->loc+"").collect(Collectors.toList()));

        room.getSelectionModel().select(0);
        room.valueProperty().addListener((ov, prevVal, newVal) -> {
//            try {
//                this.<MappingController>getParentPresenter().updateSensorLocation(id,
//                                SensorLocation.fromString(newVal));
////                dbHandler.setSensorLocation(id, SensorLocation.fromString(newVal));
//            } catch (final SensorNotFoundException ¢) {
//                ¢.printStackTrace();
//            }
        });
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
