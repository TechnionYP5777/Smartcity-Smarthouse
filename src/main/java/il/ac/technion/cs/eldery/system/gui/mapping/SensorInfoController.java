package il.ac.technion.cs.eldery.system.gui.mapping;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.SensorLocation;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
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
    private SensorLocation sensorLocation;
    
    @FXML private TitledPane titledPane;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private ComboBox<String> room;
    
    @Override public void initialize(URL location, ResourceBundle __) {
        for (SensorLocation ¢ : SensorLocation.values())
            room.getItems().add((¢ + ""));
        
        room.getSelectionModel().select(0);
        room.valueProperty().addListener((ov, prevVal, newVal) -> {
            try {
                mappingController.updateSensorLocation(this.id, SensorLocation.fromString(newVal));
                dbHandler.setSensorLocation(this.id, SensorLocation.fromString(newVal));
            } catch (SensorNotFoundException ¢) {
                ¢.printStackTrace();
            }
        });
    }

    public SensorInfoController setDatabaseHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        
        return this;
    }
    
    public SensorInfoController setMappingController(MappingController ¢) {
        this.mappingController = ¢;
        
        return this;
    }

    public SensorInfoController setName(String name) {
        this.name = name;
        this.nameLabel.setText("Name: " + name);
        updateUI();
        
        return this;
    }
    
    public SensorInfoController setId(String id) {
        this.id = id;
        this.idLabel.setText("ID: " + id);
        updateUI();
        
        return this;
    }
    
    private void updateUI() {
        titledPane.setText(String.format("%s (ID %s)", name, id));
    }
}
