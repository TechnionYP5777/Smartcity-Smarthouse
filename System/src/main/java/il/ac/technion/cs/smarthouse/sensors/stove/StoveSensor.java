package il.ac.technion.cs.smarthouse.sensors.stove;

import java.util.Arrays;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;



/**
 * This class represents a temperature sensor for a stove and contains its
 * logic.
 * 
 * @author Yarden
 * @author Sharon
 * @author Inbal Zukerman
 * @since 10.12.16
 */
public class StoveSensor extends Sensor {
    final static String onPath = "stove" + PathBuilder.DELIMITER + "is_on";
    final static String temperPath = "stove" + PathBuilder.DELIMITER + "temperature";
    public StoveSensor(final String id, final String systemIP, final int systemPort) {
        super("iStoves", id, Arrays.asList(onPath, temperPath), systemPort);
    }

    public void updateSystem(final boolean on, final int temperature) {
        super.updateSystem(onPath, on);

        super.updateSystem(temperPath, temperature);

    }

}
