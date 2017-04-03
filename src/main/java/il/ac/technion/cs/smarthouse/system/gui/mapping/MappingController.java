package il.ac.technion.cs.smarthouse.system.gui.mapping;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MappingController implements Initializable {
    private DatabaseHandler dbHandler;
    private final Map<String, SensorInfoController> sensors = new HashMap<>();
    private final Map<SensorLocation, List<String>> locationsContents = new HashMap<>();
    private final Map<String, SensorLocation> sensorsLocations = new HashMap<>();
    private final House house = new House();

    @FXML private VBox sensorsPaneList;
    @FXML private Canvas canvas;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        house.addRoom(new Room(320, 320, 150, 150, SensorLocation.LIVING_ROOM));
        house.addRoom(new Room(470, 320, 150, 150, SensorLocation.KITCHEN));
        house.addRoom(new Room(470, 470, 150, 150, SensorLocation.DINING_ROOM));
        house.addRoom(new Room(320, 170, 150, 150, SensorLocation.HALLWAY));
        house.addRoom(new Room(170, 170, 150, 150, SensorLocation.BEDROOM));
        house.addRoom(new Room(20, 170, 150, 150, SensorLocation.BATHROOM));
        house.addRoom(new Room(320, 20, 150, 150, SensorLocation.PORCH));

        canvas.setWidth(2000);
        canvas.setHeight(2000);

        try {
            drawMapping();
        } catch (final SensorNotFoundException ¢) {
            ¢.printStackTrace();
        }
    }

    public MappingController setDatabaseHandler(final DatabaseHandler dbHandler) {
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

    public void addSensor(final String id) throws IOException, SensorNotFoundException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("sensor_info.fxml"));
        sensorsPaneList.getChildren().add(loader.load());

        final SensorInfoController controller = loader.getController();
        controller.setDatabaseHandler(dbHandler).setMappingController(this).setId(id).setName(dbHandler.getName(id));
        sensors.put(id, controller);
    }

    public void updateSensorLocation(final String id, final SensorLocation l) {
        if (sensorsLocations.containsKey(id) && locationsContents.containsKey(sensorsLocations.get(id)))
            locationsContents.get(sensorsLocations.get(id)).remove(id);

        sensorsLocations.put(id, l);

        if (!locationsContents.containsKey(l))
            locationsContents.put(l, new ArrayList<>());

        locationsContents.get(l).add(id);

        try {
            drawMapping();
        } catch (final SensorNotFoundException ¢) {
            ¢.printStackTrace();
        }
    }

    private void drawMapping() throws SensorNotFoundException {
        final GraphicsContext g = canvas.getGraphicsContext2D();

        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setStroke(Color.BLACK);

        for (final Room room : house.getRooms()) {
            g.strokeRect(room.x, room.y, room.width, room.height);
            g.strokeLine(room.x, room.y + 20, room.x + room.width, room.y + 20);
            g.setFill(Color.BLACK);
            g.fillText(room.location.name(), room.x + 4, room.y + 15);
            g.setFill(Color.BLUE);

            if (locationsContents.containsKey(room.location)) {
                int dy = 20;

                for (final String id : locationsContents.get(room.location)) {
                    g.fillText(dbHandler.getName(id) + " (" + id + ")", room.x + 10, room.y + dy + 20);
                    dy += 20;
                }
            }
        }
    }
}
