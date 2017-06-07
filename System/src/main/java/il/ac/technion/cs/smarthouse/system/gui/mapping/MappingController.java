package il.ac.technion.cs.smarthouse.system.gui.mapping;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.main.SystemGuiController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MappingController extends SystemGuiController {
    private static Logger log = LoggerFactory.getLogger(MappingController.class);

    private static final List<SensorLocation> allLocations = Arrays.asList(SensorLocation.values());
    private final Map<String, SensorInfoController> sensors = new HashMap<>();
    private final Map<SensorLocation, List<String>> locationsContents = new HashMap<>();
    private final Map<String, SensorLocation> sensorsLocations = new HashMap<>();
    private final House house = new House();

    @FXML private VBox sensorsPaneList;
    @FXML private Canvas canvas;

    @Override
    public void init(final SystemCore model, final URL location, final ResourceBundle __) {
        log.debug("initialized the map gui");
        house.addRoom(new Room(320, 320, 150, 150, SensorLocation.LIVING_ROOM));
        house.addRoom(new Room(470, 320, 150, 150, SensorLocation.KITCHEN));
        house.addRoom(new Room(470, 470, 150, 150, SensorLocation.DINING_ROOM));
        house.addRoom(new Room(320, 170, 150, 150, SensorLocation.HALLWAY));
        house.addRoom(new Room(170, 170, 150, 150, SensorLocation.BEDROOM));
        house.addRoom(new Room(20, 170, 150, 150, SensorLocation.BATHROOM));
        house.addRoom(new Room(320, 20, 150, 150, SensorLocation.PORCH));

        canvas.setWidth(2000);
        canvas.setHeight(2000);

        drawMapping();

        log.debug("subscribed to sensors root");
        // this is somewhat whiteboxing. todo: reactor nicer.
        model.getFileSystem().subscribe((p, l) -> {
            log.debug("map gui was notified on (path,val)=(" + p + "," + l + ")");
            final String commname = p.split("\\.")[1];
            final String id = p.split("\\.")[2];
            if (l != null && allLocations.contains(l) && !sensors.containsKey(id))
                Platform.runLater(() -> {
                    try {
                        addSensor(id, commname);
                    } catch (final Exception e) {
                        log.warn("failed to add sensor: " + id + " (received path " + p
                                        + ") to GuiMapping.\nGot execption" + e);
                    }
                });

        }, FileSystemEntries.SENSORS.buildPath(""));
        log.debug("finished initialized the map gui");

    }

    public void addSensor(final String id, final String commName) throws Exception {
        if (sensors.containsKey(id))
            return;
        final SensorInfoController controller = createChildController("sensor_info.fxml");
        sensorsPaneList.getChildren().add(controller.getRootViewNode());
        
        controller.setId(id);
        controller.setName(commName);
        sensors.put(id, controller);
    }

    public void updateSensorLocation(final String id, final SensorLocation l) {
        if (sensorsLocations.containsKey(id) && locationsContents.containsKey(sensorsLocations.get(id)))
            locationsContents.get(sensorsLocations.get(id)).remove(id);

        sensorsLocations.put(id, l);

        if (!locationsContents.containsKey(l))
            locationsContents.put(l, new ArrayList<>());

        locationsContents.get(l).add(id);

        drawMapping();

    }

    private void drawMapping() {
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
                    g.fillText(" (" + id + ")", room.x + 10, room.y + dy + 20);
                    dy += 20;
                }
            }
        }
    }
}
