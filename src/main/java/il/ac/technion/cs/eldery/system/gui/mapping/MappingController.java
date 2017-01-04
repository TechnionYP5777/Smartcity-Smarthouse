package il.ac.technion.cs.eldery.system.gui.mapping;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class MappingController implements Initializable {
    private DatabaseHandler dbHandler;

    private Map<String, SensorInfoController> sensors = new HashMap<>();
    @FXML private VBox sensorsPaneList;

    @Override public void initialize(URL location, ResourceBundle __) {
        try {
            addSensor("00:11:22:33");
        } catch (IOException ¢) {
            ¢.printStackTrace();
        }
    }

    public MappingController setDatabaseHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        return this;
    }

    public void addSensor(String id) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sensor info.fxml"));
        sensorsPaneList.getChildren().add(loader.load());

        SensorInfoController controller = loader.getController();
        controller.setDatabaseHandler(dbHandler).setId(id).setName("Yes");
        sensors.put(id, controller);
    }
}
