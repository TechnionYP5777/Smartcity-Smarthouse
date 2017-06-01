package il.ac.technion.cs.smarthouse.sensors.shutter;

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

    public ShutterSensor(final String id, final String systemIP, final int systemPort) {
        super(id, systemPort);
    }

    public void updateSystem(final boolean open, final int fromTime, final int toTime) {

        super.updateSystem(open, "shutter" + PathBuilder.DELIMITER + "open");
        super.updateSystem(fromTime, "shutter" + PathBuilder.DELIMITER + "time");

    }

}
