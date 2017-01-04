package il.ac.technion.cs.eldery.system.gui.mapping;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class MappingController implements Initializable {
    private DatabaseHandler dbHandler;

    private Map<String, SensorInfoController> sensors = new HashMap<>();
    @FXML private VBox sensorsPaneList;

    @Override public void initialize(URL location, ResourceBundle __) {

    }

    public MappingController setDatabaseHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        dbHandler.addNewSensorsListener(id -> Platform.runLater(() -> {
            try {
                addSensor(id);
            } catch (IOException | SensorNotFoundException ¢) {
                ¢.printStackTrace();
            }
        }));

        return this;
    }

    public void addSensor(String id) throws IOException, SensorNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sensor info.fxml"));
        sensorsPaneList.getChildren().add(loader.load());

        SensorInfoController controller = loader.getController();
        controller.setDatabaseHandler(dbHandler).setId(id).setName(dbHandler.getName(id));
        sensors.put(id, controller);
    }
}
