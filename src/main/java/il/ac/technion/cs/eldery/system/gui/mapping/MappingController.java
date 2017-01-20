package il.ac.technion.cs.eldery.system.gui.mapping;

import java.awt.Graphics2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.SensorLocation;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MappingController implements Initializable {
    private DatabaseHandler dbHandler;
    private Map<String, SensorInfoController> sensors = new HashMap<>();
    private Map<SensorLocation, List<String>> locationsContents = new HashMap<>();
    private Map<String, SensorLocation> sensorsLocations = new HashMap<>();
    private House house = new House();

    @FXML private VBox sensorsPaneList;
    @FXML private Canvas canvas;

    @Override public void initialize(URL location, ResourceBundle __) {
        house.addRoom(new Room(20, 20, 100, 100, SensorLocation.KITCHEN));
        house.addRoom(new Room(200, 200, 100, 100, SensorLocation.BATHROOM));
        
        canvas.setWidth(2000);
        canvas.setHeight(2000);

        drawMapping();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sensor_info.fxml"));
        sensorsPaneList.getChildren().add(loader.load());

        SensorInfoController controller = loader.getController();
        controller.setDatabaseHandler(dbHandler).setMappingController(this).setId(id).setName(dbHandler.getName(id));
        sensors.put(id, controller);
    }

    public void updateSensorLocation(String id, SensorLocation l) {
        if (sensorsLocations.containsKey(id) && locationsContents.containsKey(sensorsLocations.get(id)))
            locationsContents.get(sensorsLocations.get(id)).remove(id);

        sensorsLocations.put(id, l);

        if (!locationsContents.containsKey(l))
            locationsContents.put(l, new ArrayList<>());

        locationsContents.get(l).add(id);
        
        drawMapping();
    }

    private void drawMapping() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.GREEN);
        g.setStroke(Color.BLACK);
        
        for (Room room : house.getRooms()) {
            g.fillRect(room.x, room.y, room.width, room.height);
        }
    }
}
