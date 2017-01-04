package il.ac.technion.cs.eldery.system.gui.mapping;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class SensorInfoController implements Initializable {
    @FXML Label name;
    @FXML ComboBox<String> room;
    
    @Override public void initialize(URL location, ResourceBundle __) {
        room.getItems().add("Hello");
        room.getItems().add("Hello");
        room.getItems().add("Hello");
    }
    
    public void setName(String name) {
        // TODO
    }
    
    public void setId(String id) {
        // TODO
    }
}
