package il.ac.technion.cs.smarthouse.sensors.shutter;

import java.util.Arrays;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * This class represents shutters sensor and contains its logic.
 * 
 * @author Alex
 * @author Inbal Zukerman
 * @since 8.5.17
 */
public class ShutterSensor extends Sensor {
    static final String openPath = "shutter" + PathBuilder.DELIMITER + "open";
    static final String timePath = "shutter" + PathBuilder.DELIMITER + "time";

    public ShutterSensor(final String id, final String systemIP, final int systemPort) {
        super("ShutterSensor", id, Arrays.asList(openPath, timePath), systemPort);
    }

    public void updateSystem(final boolean open, final int fromTime, final int toTime) {

        super.updateSystem("shutter" + PathBuilder.DELIMITER + "open", open);
        super.updateSystem("shutter" + PathBuilder.DELIMITER + "time", fromTime);

    }

}
