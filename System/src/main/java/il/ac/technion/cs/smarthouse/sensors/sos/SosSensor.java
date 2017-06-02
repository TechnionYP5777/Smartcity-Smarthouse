package il.ac.technion.cs.smarthouse.sensors.sos;

import java.util.Arrays;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * This class represents an SOS button and contains its logic.
 * 
 * @author Yarden
 * @author Inbal Zukerman
 * @since 28.12.16
 */
public class SosSensor extends Sensor {
    static final String obserPath = "sos" + PathBuilder.DELIMITER + "pressed";

    public SosSensor(final String id, final int systemPort) {
        super("iSOS", id, Arrays.asList(obserPath), systemPort);
    }

    public void updateSystem() {
        super.updateSystem("sos" + PathBuilder.DELIMITER + "pressed", true);
    }

}
