package il.ac.technion.cs.eldery.system.gui.mapping;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class MappingController implements Initializable {
    @FXML VBox sensorsPaneList;
    
    @Override public void initialize(URL location, ResourceBundle __) {
        try {
            sensorsPaneList.getChildren().add(new TitledPane("FSAFSAFSA", FXMLLoader.load(getClass().getResource("sensor info.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
